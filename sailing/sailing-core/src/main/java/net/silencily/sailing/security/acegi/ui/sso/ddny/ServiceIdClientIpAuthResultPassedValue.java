/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.security.acegi.ui.sso.ddny;

import net.silencily.sailing.security.acegi.ui.sso.CrossThreadPassedValue;

import org.acegisecurity.Authentication;


/**
 * @since 2006-7-13
 * @author java2enterprise
 * @version $Id: ServiceIdClientIpAuthResultPassedValue.java,v 1.1 2006/07/13
 *          05:53:36 wangzheng Exp $
 */
public class ServiceIdClientIpAuthResultPassedValue implements CrossThreadPassedValue {

	private String serviceId;

	private String clientIp;

	private Authentication authResult;

	public ServiceIdClientIpAuthResultPassedValue(String serviceId, String clientIp, Authentication authResult) {
		this.serviceId = serviceId;
		this.clientIp = clientIp;
		this.authResult = authResult;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new StringBuffer("serviceId [")
					.append(serviceId)
					.append("] clientIp [")
					.append(clientIp)
					.append("] authResult [")
					.append(authResult == null ? null : authResult.getName())
					.append("]")
					.toString();
	}

	/**
	 * @return the authResult
	 */
	public Authentication getAuthResult() {
		return authResult;
	}

	/**
	 * @return the clientIp
	 */
	public String getClientIp() {
		return clientIp;
	}

	/**
	 * @return the serviceId
	 */
	public String getServiceId() {
		return serviceId;
	}

}
