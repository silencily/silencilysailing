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
	 * 功能描述 调用工作流接口
	 */
	private IWorkflowService getService() {

		return BisWfServiceLocator.getWorkflowService();
	}

	/**
	 * 
	 * 功能描述 entry页
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2008-1-17 下午01:57:40
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
	 * 功能描述 所有工作列表
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2008-1-17 下午01:57:40
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
		// 封装查询条件
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
		// 获取paginater.page
		String pageUrl = (request.getParameter("paginater.page")) == null ? "0"
				: ((String) request.getParameter("paginater.page"));
		int countTemp = 0;
		int pageTemp2 = 0;

		// 存在查询条件的情况下，清空以前存的查询条件
		// 当SESSION存在查询条件的时候，更新当前FORMBEAN中的查询条件
		if ("true".equals(clearwf)) {
			// 当前的URL中存在查询条件
			HttpSession session = SecurityContextInfo.getSession();
			if (null != session.getAttribute("wfsearch3")) {
				session.removeAttribute("wfsearch3");
			}
			session.setAttribute("wfsearch3", wfsearch3);
		} else {
			// 当前的URL中不存在查询条件
			// 如果SESSION中有查询条件，重置查询条件
			HttpSession session = SecurityContextInfo.getSession();
			if (null != session.getAttribute("wfsearch3")) {
				wfsearch3 = (WfSearch) session.getAttribute("wfsearch3");
			}
		}

		// 开始查询
		if ("".equals(wfsearch3.getTitle()) && "".equals(wfsearch3.getState())
				&& "".equals(wfsearch3.getCommitter())
				&& "".equals(wfsearch3.getStartTime())
				&& "".equals(wfsearch3.getEndTime())) {
			// 无查询条件
			// 计算页数，得到当前页数
			int countPage = IWorkflowService.getFlowEntryCount(null);
			countTemp = countPage;
			// 设置行数
			wFManageForm.getPaginater().setCount(countPage);
			// 总的页数目
			int pageCountTemp = wFManageForm.getPaginater().getPageCount();
			// 处理得到pageNo
			// 设置页数,修复页数永远为0的危险
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
			// 有查询条件
			// 计算页数，得到当前页数
			int countPage = IWorkflowService.getFlowEntryCount(wfsearch3);
			countTemp = countPage;
			// 设置行数
			wFManageForm.getPaginater().setCount(countPage);
			// 总的页数目
			int pageCountTemp = wFManageForm.getPaginater().getPageCount();
			// 处理得到pageNo
			// 设置页数,修复页数永远为0的危险
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
	 * 功能描述 工作详细 没有选取的话，默认选择第一条
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2008-1-17 下午01:57:40
	 * @version 1.0
	 * @author wenjb
	 */
	/*
	 * public void info(ActionMapping mapping, ActionForm form,
	 * HttpServletRequest request, HttpServletResponse response) throws
	 * Exception { IWorkflowService IWorkflowService = this.getService(); String
	 * tag = (request.getParameter("tag")) == null ? "" : ((String)
	 * request.getParameter("tag")); // 跳转字符串 String wfname = ""; String wfstep =
	 * ""; String url = ""; // 列表中过来的，还是其他情况 if ("".equals(tag)) { //查询数据库
	 * WfEntry wfEntry = new WfEntry(); List allList = null; //暂时屏蔽此方法，无法查到结果
	 * //allList = IWorkflowService.findTask(null, null);
	 * allList=IWorkflowService.findWaitTask(SecurityContextInfo
	 * .getCurrentUser(), null); if(null==allList||null==allList.get(0)){
	 * //没有数据的情况怎么处理 //目前为一空白页面 response.sendRedirect(url); // }else{ //取第一条数据
	 * wfEntry = (WfEntry) allList.get(0); // 取第一条 wfname = wfEntry.getWfName();
	 * wfstep = wfEntry.getCurrentStepName(); url = wfEntry.getUrl(); String
	 * contextPath = request.getContextPath(); url = contextPath + url;
	 * WorkFlowFormContext.loadFormPermission(wfname, wfstep);
	 * response.sendRedirect(url); } } else { // 列表中过来的 // 取当前数据 wfname =
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
