/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project casclient
 */
package net.silencily.sailing.security.acegi.sso.cas.listener;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @since 2006-1-17
 * @author ÍõÕþ
 * @version $Id: RegisterHostnameVerifierListener.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 */
public class RegisterHostnameVerifierListener implements ServletContextListener {

	private static Log logger = LogFactory.getLog(RegisterHostnameVerifierListener.class);
	
	public void contextInitialized(ServletContextEvent sc) {
		reigisterHostnameVerifier();
	}

	public void contextDestroyed(ServletContextEvent sc) {
		// donothing
	}
	
	public static void reigisterHostnameVerifier() {
    	// Note , In the default case, jvm will use "com.sun.net.ssl.internal.www.protocol" £¡£¡£¡
		logger.info(" Setting System.property [ java.protocol.handler.pkgs ] to [ javax.net.ssl] ");
    	System.setProperty("java.protocol.handler.pkgs", "javax.net.ssl");

    	
    	HostnameVerifier hv = new HostnameVerifier() {
    	    public boolean verify(String urlHostName, SSLSession session) {
//    	        System.out.println("Warning: URL Host: "+urlHostName+" vs. " + session.getPeerHost());
//    	        return true;
    	    	return urlHostName.equals(session.getPeerHost());
    	    }
    	};
    	 
    	HttpsURLConnection.setDefaultHostnameVerifier(hv);
	}

}
