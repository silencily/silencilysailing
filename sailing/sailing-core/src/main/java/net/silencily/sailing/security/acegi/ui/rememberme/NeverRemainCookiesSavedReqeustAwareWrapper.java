/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.security.acegi.ui.rememberme;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.acegisecurity.ui.AbstractProcessingFilter;
import org.acegisecurity.ui.savedrequest.Enumerator;
import org.acegisecurity.ui.savedrequest.FastHttpDateFormat;
import org.acegisecurity.ui.savedrequest.SavedRequest;
import org.acegisecurity.util.PortResolver;
import org.acegisecurity.wrapper.SavedRequestAwareWrapper;
import org.acegisecurity.wrapper.SecurityContextHolderAwareRequestWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @since 2006-7-9
 * @author ����
 * @version $Id: NeverRemainCookiesSavedReqeustAwareWrapper.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public class NeverRemainCookiesSavedReqeustAwareWrapper extends SecurityContextHolderAwareRequestWrapper {

    //~ Static fields/initializers =====================================================================================

    protected static final Log logger = LogFactory.getLog(SavedRequestAwareWrapper.class);
    protected static final TimeZone GMT_ZONE = TimeZone.getTimeZone("GMT");

    /** The default Locale if none are specified. */
    protected static Locale defaultLocale = Locale.getDefault();

    //~ Instance fields ================================================================================================

    protected SavedRequest savedRequest = null;

    /**
     * The set of SimpleDateFormat formats to use in getDateHeader(). Notice that because SimpleDateFormat is
     * not thread-safe, we can't declare formats[] as a static variable.
     */
    protected SimpleDateFormat[] formats = new SimpleDateFormat[3];

    //~ Constructors ===================================================================================================

    public NeverRemainCookiesSavedReqeustAwareWrapper(HttpServletRequest request, PortResolver portResolver) {
        super(request);

        HttpSession session = request.getSession(false);

        if (session == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Wrapper not replaced; no session available for SavedRequest extraction");
            }

            return;
        }

        SavedRequest saved = (SavedRequest) session.getAttribute(AbstractProcessingFilter.ACEGI_SAVED_REQUEST_KEY);

        if ((saved != null) && saved.doesRequestMatch(request, portResolver)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Wrapper replaced; SavedRequest was: " + saved);
            }

            savedRequest = saved;
            session.removeAttribute(AbstractProcessingFilter.ACEGI_SAVED_REQUEST_KEY);

            formats[0] = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
            formats[1] = new SimpleDateFormat("EEEEEE, dd-MMM-yy HH:mm:ss zzz", Locale.US);
            formats[2] = new SimpleDateFormat("EEE MMMM d HH:mm:ss yyyy", Locale.US);

            formats[0].setTimeZone(GMT_ZONE);
            formats[1].setTimeZone(GMT_ZONE);
            formats[2].setTimeZone(GMT_ZONE);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Wrapper not replaced; SavedRequest was: " + saved);
            }
        }
    }

    //~ Methods ========================================================================================================

    /**
     * The default behavior of this method is to return getCookies() on the wrapped request object.
     *
     * @return DOCUMENT ME!
     */
    public Cookie[] getCookies() {
    	return super.getCookies();
    }

    /**
     * The default behavior of this method is to return getDateHeader(String name) on the wrapped request
     * object.
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public long getDateHeader(String name) {
        if (savedRequest == null) {
            return super.getDateHeader(name);
        } else {
            String value = getHeader(name);

            if (value == null) {
                return (-1L);
            }

            // Attempt to convert the date header in a variety of formats
            long result = FastHttpDateFormat.parseDate(value, formats);

            if (result != (-1L)) {
                return result;
            }

            throw new IllegalArgumentException(value);
        }
    }

    /**
     * The default behavior of this method is to return getHeader(String name) on the wrapped request object.
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getHeader(String name) {
        if (savedRequest == null) {
            return super.getHeader(name);
        } else {
            String header = null;
            Iterator iterator = savedRequest.getHeaderValues(name);

            while (iterator.hasNext()) {
                header = (String) iterator.next();

                break;
            }

            return header;
        }
    }

    /**
     * The default behavior of this method is to return getHeaderNames() on the wrapped request object.
     *
     * @return DOCUMENT ME!
     */
    public Enumeration getHeaderNames() {
        if (savedRequest == null) {
            return super.getHeaderNames();
        } else {
            return new Enumerator(savedRequest.getHeaderNames());
        }
    }

    /**
     * The default behavior of this method is to return getHeaders(String name) on the wrapped request object.
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Enumeration getHeaders(String name) {
        if (savedRequest == null) {
            return super.getHeaders(name);
        } else {
            return new Enumerator(savedRequest.getHeaderValues(name));
        }
    }

    /**
     * The default behavior of this method is to return getIntHeader(String name) on the wrapped request
     * object.
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getIntHeader(String name) {
        if (savedRequest == null) {
            return super.getIntHeader(name);
        } else {
            String value = getHeader(name);

            if (value == null) {
                return (-1);
            } else {
                return (Integer.parseInt(value));
            }
        }
    }

    /**
     * The default behavior of this method is to return getLocale() on the wrapped request object.
     *
     * @return DOCUMENT ME!
     */
    public Locale getLocale() {
        if (savedRequest == null) {
            return super.getLocale();
        } else {
            Locale locale = null;
            Iterator iterator = savedRequest.getLocales();

            while (iterator.hasNext()) {
                locale = (Locale) iterator.next();

                break;
            }

            if (locale == null) {
                return defaultLocale;
            } else {
                return locale;
            }
        }
    }

    /**
     * The default behavior of this method is to return getLocales() on the wrapped request object.
     *
     * @return DOCUMENT ME!
     */
    public Enumeration getLocales() {
        if (savedRequest == null) {
            return super.getLocales();
        } else {
            Iterator iterator = savedRequest.getLocales();

            if (iterator.hasNext()) {
                return new Enumerator(iterator);
            } else {
                ArrayList results = new ArrayList();
                results.add(defaultLocale);

                return new Enumerator(results.iterator());
            }
        }
    }

    /**
     * The default behavior of this method is to return getMethod() on the wrapped request object.
     *
     * @return DOCUMENT ME!
     */
    public String getMethod() {
        if (savedRequest == null) {
            return super.getMethod();
        } else {
            return savedRequest.getMethod();
        }
    }

    /**
     * The default behavior of this method is to return getParameter(String name) on the wrapped request
     * object.
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getParameter(String name) {
/*
   if (savedRequest == null) {
       return super.getParameter(name);
   } else {
       String value = null;
       String[] values = savedRequest.getParameterValues(name);
       if (values == null)
           return null;
       for (int i = 0; i < values.length; i++) {
           value = values[i];
           break;
       }
       return value;
   }
 */

        //we do not get value from super.getParameter because there is a bug in Jetty servlet-container
        String value = null;
        String[] values = null;

        if (savedRequest == null) {
            values = super.getParameterValues(name);
        } else {
            values = savedRequest.getParameterValues(name);
        }

        if (values == null) {
            return null;
        }

        for (int i = 0; i < values.length; i++) {
            value = values[i];

            break;
        }

        return value;
    }

    /**
     * The default behavior of this method is to return getParameterMap() on the wrapped request object.
     *
     * @return DOCUMENT ME!
     */
    public Map getParameterMap() {
        if (savedRequest == null) {
            return super.getParameterMap();
        } else {
            return savedRequest.getParameterMap();
        }
    }

    /**
     * The default behavior of this method is to return getParameterNames() on the wrapped request object.
     *
     * @return DOCUMENT ME!
     */
    public Enumeration getParameterNames() {
        if (savedRequest == null) {
            return super.getParameterNames();
        } else {
            return new Enumerator(savedRequest.getParameterNames());
        }
    }

    /**
     * The default behavior of this method is to return getParameterValues(String name) on the wrapped request
     * object.
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String[] getParameterValues(String name) {
        if (savedRequest == null) {
            return super.getParameterValues(name);
        } else {
            return savedRequest.getParameterValues(name);
        }
    }
	
}
