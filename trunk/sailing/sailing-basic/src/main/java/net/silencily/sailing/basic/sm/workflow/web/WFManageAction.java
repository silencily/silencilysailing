package net.silencily.sailing.basic.sm.workflow.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.silencily.sailing.basic.wf.dto.WfSearch;
import net.silencily.sailing.basic.wf.service.IWorkflowService;
import net.silencily.sailing.basic.wf.util.BisWfServiceLocator;
import net.silencily.sailing.security.SecurityContextInfo;
import net.silencily.sailing.struts.DispatchActionPlus;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


/**
 * 
 * @author wenjb
 * @version 1.0
 */
public class WFManageAction extends DispatchActionPlus {

	public static final String FORWARD_ENTRY = "entry";

	public static final String FORWARD_LIST = "list";

	// public static final String FORWARD_INFO = "info";

	/**
	 * �������� ���ù������ӿ�
	 */
	private IWorkflowService getService() {

		return BisWfServiceLocator.getWorkflowService();
	}

	/**
	 * 
	 * �������� entryҳ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2008-1-17 ����01:57:40
	 * @version 1.0
	 * @author wenjb
	 */
	public ActionForward entry(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward(FORWARD_ENTRY);
	}

	/**
	 * 
	 * �������� ���й����б�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2008-1-17 ����01:57:40
	 * @version 1.0
	 * @author wenjb
	 */
	public ActionForward list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		IWorkflowService IWorkflowService = this.getService();
		List allList = new ArrayList(0);
		;

		WFManageForm wFManageForm = (WFManageForm) form;
		// ��װ��ѯ����
		WfSearch wfsearch3 = wFManageForm.getWfSearch();
		String title = (request.getParameter("title")) == null ? ""
				: ((String) request.getParameter("title"));
		String state = (request.getParameter("state")) == null ? ""
				: ((String) request.getParameter("state"));
		String committer = (request.getParameter("committer")) == null ? ""
				: ((String) request.getParameter("committer"));
		String startTime = (request.getParameter("startTime")) == null ? ""
				: ((String) request.getParameter("startTime"));
		String endTime = (request.getParameter("endTime")) == null ? ""
				: ((String) request.getParameter("endTime"));
		String clearwf = (request.getParameter("clearwf")) == null ? ""
				: ((String) request.getParameter("clearwf"));
		wfsearch3.setTitle(title);
		wfsearch3.setState(state);
		wfsearch3.setCommitter(committer);
		wfsearch3.setStartTime(startTime);
		wfsearch3.setEndTime(endTime);
		// ��ȡpaginater.page
		String pageUrl = (request.getParameter("paginater.page")) == null ? "0"
				: ((String) request.getParameter("paginater.page"));
		int countTemp = 0;
		int pageTemp2 = 0;

		// ���ڲ�ѯ����������£������ǰ��Ĳ�ѯ����
		// ��SESSION���ڲ�ѯ������ʱ�򣬸��µ�ǰFORMBEAN�еĲ�ѯ����
		if ("true".equals(clearwf)) {
			// ��ǰ��URL�д��ڲ�ѯ����
			HttpSession session = SecurityContextInfo.getSession();
			if (null != session.getAttribute("wfsearch3")) {
				session.removeAttribute("wfsearch3");
			}
			session.setAttribute("wfsearch3", wfsearch3);
		} else {
			// ��ǰ��URL�в����ڲ�ѯ����
			// ���SESSION���в�ѯ���������ò�ѯ����
			HttpSession session = SecurityContextInfo.getSession();
			if (null != session.getAttribute("wfsearch3")) {
				wfsearch3 = (WfSearch) session.getAttribute("wfsearch3");
			}
		}

		// ��ʼ��ѯ
		if ("".equals(wfsearch3.getTitle()) && "".equals(wfsearch3.getState())
				&& "".equals(wfsearch3.getCommitter())
				&& "".equals(wfsearch3.getStartTime())
				&& "".equals(wfsearch3.getEndTime())) {
			// �޲�ѯ����
			// ����ҳ�����õ���ǰҳ��
			int countPage = IWorkflowService.getFlowEntryCount(null);
			countTemp = countPage;
			// ��������
			wFManageForm.getPaginater().setCount(countPage);
			// �ܵ�ҳ��Ŀ
			int pageCountTemp = wFManageForm.getPaginater().getPageCount();
			// ����õ�pageNo
			// ����ҳ��,�޸�ҳ����ԶΪ0��Σ��
			try {
				if (Integer.parseInt(pageUrl) <= pageCountTemp) {
					wFManageForm.getPaginater().setPage(
							Integer.parseInt(pageUrl));
				}
			} catch (Exception e) {
				wFManageForm.getPaginater().setPage(0);
			}
			int pageNo = wFManageForm.getPaginater().getPage() + 1;
			int pageSize = wFManageForm.getPaginater().getPageSize();
			pageTemp2 = pageNo - 1;
			allList = IWorkflowService.findFlowEntry(null, pageNo, pageSize);
		} else {
			// �в�ѯ����
			// ����ҳ�����õ���ǰҳ��
			int countPage = IWorkflowService.getFlowEntryCount(wfsearch3);
			countTemp = countPage;
			// ��������
			wFManageForm.getPaginater().setCount(countPage);
			// �ܵ�ҳ��Ŀ
			int pageCountTemp = wFManageForm.getPaginater().getPageCount();
			// ����õ�pageNo
			// ����ҳ��,�޸�ҳ����ԶΪ0��Σ��
			try {
				if (Integer.parseInt(pageUrl) <= pageCountTemp) {
					wFManageForm.getPaginater().setPage(
							Integer.parseInt(pageUrl));
				}
			} catch (Exception e) {
				wFManageForm.getPaginater().setPage(0);
			}
			int pageNo = wFManageForm.getPaginater().getPage() + 1;
			int pageSize = wFManageForm.getPaginater().getPageSize();
			pageTemp2 = pageNo - 1;
			allList = IWorkflowService.findFlowEntry(wfsearch3, pageNo,
					pageSize);
		}
		if (allList == null) {
			allList = new ArrayList();
		}
		wFManageForm.setAllWfList(allList);
		wFManageForm.getPaginater().setCount(countTemp);
		wFManageForm.getPaginater().setPage(pageTemp2);
		return mapping.findForward(FORWARD_LIST);
	}

	/**
	 * 
	 * �������� ������ϸ û��ѡȡ�Ļ���Ĭ��ѡ���һ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2008-1-17 ����01:57:40
	 * @version 1.0
	 * @author wenjb
	 */
	/*
	 * public void info(ActionMapping mapping, ActionForm form,
	 * HttpServletRequest request, HttpServletResponse response) throws
	 * Exception { IWorkflowService IWorkflowService = this.getService(); String
	 * tag = (request.getParameter("tag")) == null ? "" : ((String)
	 * request.getParameter("tag")); // ��ת�ַ��� String wfname = ""; String wfstep =
	 * ""; String url = ""; // �б��й����ģ������������ if ("".equals(tag)) { //��ѯ���ݿ�
	 * WfEntry wfEntry = new WfEntry(); List allList = null; //��ʱ���δ˷������޷��鵽���
	 * //allList = IWorkflowService.findTask(null, null);
	 * allList=IWorkflowService.findWaitTask(SecurityContextInfo
	 * .getCurrentUser(), null); if(null==allList||null==allList.get(0)){
	 * //û�����ݵ������ô���� //ĿǰΪһ�հ�ҳ�� response.sendRedirect(url); // }else{ //ȡ��һ������
	 * wfEntry = (WfEntry) allList.get(0); // ȡ��һ�� wfname = wfEntry.getWfName();
	 * wfstep = wfEntry.getCurrentStepName(); url = wfEntry.getUrl(); String
	 * contextPath = request.getContextPath(); url = contextPath + url;
	 * WorkFlowFormContext.loadFormPermission(wfname, wfstep);
	 * response.sendRedirect(url); } } else { // �б��й����� // ȡ��ǰ���� wfname =
	 * (request.getParameter("wfname")) == null ? "" : ((String)
	 * request.getParameter("wfname")); wfstep =
	 * (request.getParameter("wfstep")) == null ? "" : ((String)
	 * request.getParameter("wfstep")); url = (request.getParameter("wfurl")) ==
	 * null ? "" : ((String) request.getParameter("wfurl")); url =
	 * url.replace('|', '&'); String contextPath = request.getContextPath(); url =
	 * contextPath + url; WorkFlowFormContext.loadFormPermission(wfname,
	 * wfstep); response.sendRedirect(url); } }
	 */
}
