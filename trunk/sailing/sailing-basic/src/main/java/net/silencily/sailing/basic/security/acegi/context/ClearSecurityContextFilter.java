/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.basic.security.acegi.context;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.silencily.sailing.common.context.WorkFlowFormContext;
import net.silencily.sailing.context.BusinessContext;
import net.silencily.sailing.context.CreateAndSaveButtonCtrlCommon;
import net.silencily.sailing.security.SecurityContextInfo;

import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 清除 {@link SecurityContextHolder} 的 filter, 使用这个 filter 的原因是 tomcat 线程池没有清空
 * SecurityContext, 注意此 filter 必须放在所有安全 filter 的第一位
 * 
 * @since 2006-11-11
 * @author java2enterprise
 * @version $Id: ClearSecurityContextFilter.java,v 1.13 2008/03/05 06:44:25
 *          wenjb Exp $
 */
public class ClearSecurityContextFilter extends OncePerRequestFilter {

	private static String STEP = "step";

	private static String STEPTYPE = "stepType";

	private static String MAIN_TABLE_CLASS_NAME = "mainTableClassName";

	private static String CALCULATE_RWCTRLTYPE_BY_ID = "calculateRwCtrlTypeByID";

	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
	
		// 设定当前URL
		String url = request.getServletPath();
		String step = request.getParameter(STEP);
		if (step != null) {
			url = url + "?" + STEP + "=" + step;
		}
		String stepType = request.getParameter(STEPTYPE);
		if (StringUtils.isNotBlank(stepType)) {
			if (step != null) {
				url = url + "&" + STEPTYPE + "=" + stepType;
			} else {
				url = url + "?" + STEPTYPE + "=" + stepType;
			}
		}
		SecurityContextInfo.setCurrentPageUrl(url);

		// 设定主表类名
		String tableClass = request.getParameter(MAIN_TABLE_CLASS_NAME);
		if (tableClass == null) {
			tableClass = "";
		}
		SecurityContextInfo.setMainTableClassName(tableClass);

		SecurityContextHolder.clearContext();
		// 强制创建 session, 避免 weblogic 取不到 session 的 bug
		request.getSession(true);

		// 将当前获取的session，放到ThreadLocal里面
		SecurityContextInfo.setSession(request.getSession());

		// 设定calculateRwCtrlTypeByID
		String calculateRwCtrlTypeByID = request
				.getParameter(CALCULATE_RWCTRLTYPE_BY_ID);
		if (calculateRwCtrlTypeByID == null) {
			calculateRwCtrlTypeByID = "";
		}
		BusinessContext.setCalculateRwCtrlTypeByID(calculateRwCtrlTypeByID);

		// 清除当前BusinessContext中的OperType
		HttpSession session = SecurityContextInfo.getSession();
		if (null != session.getAttribute("operType") && !"true".equals(request.getParameter("doNotClearOperType"))) {
			session.removeAttribute("operType");
		}

		// 如果当前URL存在TASKID，表明此次请求在工作流内
		// 如果TASKID取不到值，也是在流外
		// 清除流内和流外标志信息
		WorkFlowFormContext.setTag("2");
		CreateAndSaveButtonCtrlCommon.setFormInTheWorkFlowForCommonButton("2");
		// 设定流标志位信息
		// 注意已办任务也有taskId，但是不在流内
		if (request.getParameterMap().containsKey("taskId")
				&& null != request.getParameter("taskId")
				&& !("".equals(request.getParameter("taskId")))) {
			// 仅待办任务，采用流内权限
			if (request.getParameterMap().containsKey("urlKey")
					&& "waitList".equals(request.getParameter("urlKey"))) {
				WorkFlowFormContext.setTag("1");
				CreateAndSaveButtonCtrlCommon.setFormInTheWorkFlowForCommonButton("1");
			}
		}
		
		// 将当前URL的KEY值存到SESSION中
		WorkFlowFormContext.setUrlKey(request.getParameter("urlKey"));

		if (request.getParameter("oid") != null) {
			BusinessContext.setUserSetedOid(request.getParameter("oid"));
		}else{
			BusinessContext.setUserSetedOid(null);
		}
		filterChain.doFilter(request, response);
	}

}
