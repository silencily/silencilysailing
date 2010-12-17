/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.security.acegi.ui.sso.ddny;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.silencily.sailing.security.acegi.ui.sso.CrossThreadPassedValue;
import net.silencily.sailing.security.acegi.ui.sso.SingleSignOnService;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.id.UUIDHexGenerator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;



/**
 * @since 2006-7-13
 * @author java2enterprise
 * @version $Id: RdbmsBasedSingleSignOnService.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 */
public class RdbmsBasedSingleSignOnService extends JdbcDaoSupport implements SingleSignOnService {

	private static transient Log logger = LogFactory.getLog(RdbmsBasedSingleSignOnService.class);
	
	public static final String SSO_SERVICE_ID_SESSION_KEY = "com.coheg.security.acegi.ui.sso.ddny.RdbmsBasedSingleSignOnService.serviceID";
	
	private List clientSystemNames = new ArrayList();
	
	/**
	 * @param clientSystemNames the clientSystemNames to set
	 */
	public void setClientSystemNames(List clientSystemNames) {
		this.clientSystemNames = clientSystemNames;
	}
	
	public CrossThreadPassedValue syncOperationBeforeSSOLoginRunnerStart(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {
		String serviceId = (String) new UUIDHexGenerator().generate(null, null);
		
		// set service id to session
		HttpSession session = request.getSession(true);
		if (session != null) {
			session.setAttribute(SSO_SERVICE_ID_SESSION_KEY, serviceId);
		}
		
		return new ServiceIdClientIpAuthResultPassedValue(serviceId, request.getRemoteAddr(), successfulAuthentication);
	}

	
	public void loginSuccess(CrossThreadPassedValue passedValue) {	
		if (CollectionUtils.isEmpty(clientSystemNames)) {
			if (logger.isErrorEnabled()) {
				logger.error(" 未注册 clientSystemNames, 忽略单点登录 ");
			}
			return;
		}
		
		Assert.isInstanceOf(ServiceIdClientIpAuthResultPassedValue.class, passedValue);
		ServiceIdClientIpAuthResultPassedValue serviceIdClientIpAuthResultPassedValue = (ServiceIdClientIpAuthResultPassedValue) passedValue;
		
		final String serviceId = serviceIdClientIpAuthResultPassedValue.getServiceId();
		final String username = serviceIdClientIpAuthResultPassedValue.getAuthResult().getName();
		final String clientIp = serviceIdClientIpAuthResultPassedValue.getClientIp();
		
		Assert.notNull(serviceId);
		Assert.notNull(username);
		Assert.notNull(clientIp);
		
		//final String orignalToken = serviceId + "@" + username + "@" + clientIp;
		//final String serviceToken = new String(Base64.encodeBase64(DigestUtils.md5(orignalToken)));
				
		// persistent 
		/*
		String sql = " insert into platform_sso_communication (service_id, client_name, service_token) values (?, ?, ?) ";
		getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			public int getBatchSize() {
				return clientSystemNames.size();
			}
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, serviceId);
				ps.setString(2, (String) clientSystemNames.get(i));
				ps.setString(3, serviceToken);
			}		
		});
		
		if (logger.isDebugEnabled()) {
			logger.debug(" 成功注册单点登录信息 " + serviceIdClientIpAuthResultPassedValue);
		}
		*/
	}

	public CrossThreadPassedValue syncOperationBeforeSSOLogoutRunnerStart(HttpServletRequest request, HttpServletResponse response) {
		String serviceId = null;
		
		// set service id to session
		HttpSession session = request.getSession(false);
		if (session != null) {
			serviceId = (String) session.getAttribute(SSO_SERVICE_ID_SESSION_KEY);
		}
		
		String clientIp = request.getRemoteAddr();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		return new ServiceIdClientIpAuthResultPassedValue(serviceId, clientIp, authentication);
	}
	
	public void logout(CrossThreadPassedValue passedValue) {
		Assert.isInstanceOf(ServiceIdClientIpAuthResultPassedValue.class, passedValue);
		ServiceIdClientIpAuthResultPassedValue serviceIdClientIpAuthResultPassedValue = (ServiceIdClientIpAuthResultPassedValue) passedValue;
		final String serviceId = serviceIdClientIpAuthResultPassedValue.getServiceId();
		if (serviceId != null) {
			//String sql = " delete from platform_sso_communication where service_id = ? ";
			//getJdbcTemplate().update(sql, new Object[] {serviceId}, new int[] {Types.VARCHAR});
			//if (logger.isDebugEnabled()) {
			//	logger.debug(" 成功注销单点登录信息 " + serviceIdClientIpAuthResultPassedValue);
			//}
		}		
	}




}
