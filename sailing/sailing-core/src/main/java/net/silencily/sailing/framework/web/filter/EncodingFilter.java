package net.silencily.sailing.framework.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ±àÂë¹ýÂËÆ÷
 * @author scott
 * @since 2006-4-10
 * @version $Id: EncodingFilter.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 *
 */
public class EncodingFilter implements Filter {
    private String encodingKey = "encoding";
    private String encodingValue = "GBK";
    protected FilterConfig filterConfig;
    private Log logger = LogFactory.getLog(EncodingFilter.class);

    public void init(FilterConfig config) throws ServletException {
        if (StringUtils.isNotBlank(config.getInitParameter(encodingKey))) {
            encodingValue = config.getInitParameter(encodingKey);
        }
        filterConfig = config;
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) 
        throws IOException, ServletException {
        
        HttpServletRequest request = (HttpServletRequest) req;
        request.setCharacterEncoding(encodingValue);
        chain.doFilter(req, res);
        
        if ("GET".equals(request.getMethod())) {
            request.setCharacterEncoding("ISO-8859-1");
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("request uri=[" + request.getRequestURI() + "]");
        }
    }

    public void destroy() {
        filterConfig = null;
    }
}
