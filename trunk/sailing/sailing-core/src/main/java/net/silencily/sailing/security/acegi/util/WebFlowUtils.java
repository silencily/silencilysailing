/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.security.acegi.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.util.PortMapper;
import org.acegisecurity.util.PortMapperImpl;
import org.acegisecurity.util.PortResolver;
import org.acegisecurity.util.PortResolverImpl;
import org.springframework.util.Assert;

/**
 * @since 2006-4-17
 * @author 王政
 * @version $Id: WebFlowUtils.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 */
public abstract class WebFlowUtils {
	
	public static final String STANDARD_URL_PREFIX = "/";
	
	public static final int HTTP_DEFAULT_SERVER_PORT = 80;
	
	public static final int HTTPS_DEFAULT_SERVLET_PORT = 8443;
	
	
	/**
	 * 重定向方法, 主要处理 http 协议与 https 协议的转换
	 * @param request the request
	 * @param response the response
	 * @param forceHttps 是否强制使用 https 协议
	 * @param url the url
	 * @throws IOException if exception throws 
	 */
	public static void sendRedirect(
		HttpServletRequest request, 
		HttpServletResponse response, 
		boolean forceHttps, 
		String url) throws IOException {
        
		Assert.notNull(url, " url is required! ");
		
		if (url.startsWith("http://") || url.startsWith("https://")) {
			response.sendRedirect(url);
			return;
		}
		
	    String redirectUrl = buildFullRequestUrl(request, forceHttps, url);

		response.sendRedirect(response.encodeRedirectURL(redirectUrl));
	}

	
	public static String buildFullRequestUrl(HttpServletRequest request, boolean forceHttps, String url) {
		if (url.startsWith("http://") || url.startsWith("https://")) {
			return url;
		}
		
		PortMapper portMapper = new PortMapperImpl();
	    PortResolver portResolver = new PortResolverImpl();
		
		String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = portResolver.getServerPort(request);
        String contextPath = request.getContextPath();

        boolean inHttp = "http".equals(scheme.toLowerCase());

        boolean includePort = true;

        if ("http".equals(scheme.toLowerCase()) && (serverPort == HTTP_DEFAULT_SERVER_PORT)) {
            includePort = false;
        }

        if ("https".equals(scheme.toLowerCase()) && (serverPort == HTTPS_DEFAULT_SERVLET_PORT)) {
            includePort = false;
        }
        
        String fixedUrl = url.startsWith(STANDARD_URL_PREFIX) ? url : STANDARD_URL_PREFIX + url;
        
        String redirectUrl = scheme + "://" + serverName
            + ((includePort) ? (":" + serverPort) : "") + contextPath
            + fixedUrl;
        
        if (forceHttps && inHttp) {
            Integer httpPort = new Integer(portResolver.getServerPort(request));
            Integer httpsPort = (Integer) portMapper.lookupHttpsPort(httpPort);

            if (httpsPort != null) {
                if (httpsPort.intValue() == 443) {
                    includePort = false;
                } else {
                    includePort = true;
                }

                redirectUrl = "https://" + serverName
                    + ((includePort) ? (":" + httpsPort) : "") + contextPath
                    + fixedUrl;
            }
        }
		return redirectUrl;
	}
	
}
