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
 * ��� {@link SecurityContextHolder} �� filter, ʹ����� filter ��ԭ���� tomcat �̳߳�û�����
 * SecurityContext, ע��� filter ����������а�ȫ filter �ĵ�һλ
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
	
		// �趨��ǰURL
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

		// �趨��������
		String tableClass = request.getParameter(MAIN_TABLE_CLASS_NAME);
		if (tableClass == null) {
			tableClass = "";
		}
		SecurityContextInfo.setMainTableClassName(tableClass);

		SecurityContextHolder.clearContext();
		// ǿ�ƴ��� session, ���� weblogic ȡ���� session �� bug
		request.getSession(true);

		// ����ǰ��ȡ��session���ŵ�ThreadLocal����
		SecurityContextInfo.setSession(request.getSession());

		// �趨calculateRwCtrlTypeByID
		String calculateRwCtrlTypeByID = request
				.getParameter(CALCULATE_RWCTRLTYPE_BY_ID);
		if (calculateRwCtrlTypeByID == null) {
			calculateRwCtrlTypeByID = "";
		}
		BusinessContext.setCalculateRwCtrlTypeByID(calculateRwCtrlTypeByID);

		// �����ǰBusinessContext�е�OperType
		HttpSession session = SecurityContextInfo.getSession();
		if (null != session.getAttribute("operType") && !"true".equals(request.getParameter("doNotClearOperType"))) {
			session.removeAttribute("operType");
		}

		// �����ǰURL����TASKID�������˴������ڹ�������
		// ���TASKIDȡ����ֵ��Ҳ��������
		// ������ں������־��Ϣ
		WorkFlowFormContext.setTag("2");
		CreateAndSaveButtonCtrlCommon.setFormInTheWorkFlowForCommonButton("2");
		// �趨����־λ��Ϣ
		// ע���Ѱ�����Ҳ��taskId�����ǲ�������
		if (request.getParameterMap().containsKey("taskId")
				&& null != request.getParameter("taskId")
				&& !("".equals(request.getParameter("taskId")))) {
			// ���������񣬲�������Ȩ��
			if (request.getParameterMap().containsKey("urlKey")
					&& "waitList".equals(request.getParameter("urlKey"))) {
				WorkFlowFormContext.setTag("1");
				CreateAndSaveButtonCtrlCommon.setFormInTheWorkFlowForCommonButton("1");
			}
		}
		
		// ����ǰURL��KEYֵ�浽SESSION��
		WorkFlowFormContext.setUrlKey(request.getParameter("urlKey"));

		if (request.getParameter("oid") != null) {
			BusinessContext.setUserSetedOid(request.getParameter("oid"));
		}else{
			BusinessContext.setUserSetedOid(null);
		}
		filterChain.doFilter(request, response);
	}

}
