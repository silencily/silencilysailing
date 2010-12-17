package net.silencily.sailing.basic.wf.search.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.silencily.sailing.basic.wf.dto.WfSearch;
import net.silencily.sailing.basic.wf.service.IWorkflowService;
import net.silencily.sailing.basic.wf.util.BisWfServiceLocator;
import net.silencily.sailing.common.context.WorkFlowFormContext;
import net.silencily.sailing.security.SecurityContextInfo;
import net.silencily.sailing.struts.DispatchActionPlus;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 要求 所有的查询结果按时间进行排序
 */
public class PersonWfSearchAction extends DispatchActionPlus {

	public static final String FORWARD_ENTRY = "entry";

	public static final String FORWARD_WAITLIST = "waitList";

	public static final String FORWARD_ALREADYLIST = "alreadyList";

	//add start 2008-02-20 14:00 liyan
	public static final String FORWARD_PASSROUNDLIST = "passroundList";
	//add end 2008-02-20 14:00 liyan
	
	// public static final String FORWARD_RECIEVELIST = "recieveList";
	//
	// public static final String FORWARD_ENTRUSTLIST = "entrustList";
	//
	// public static final String FORWARD_ALLLIST = "allList";

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
	 *             2007-12-17 下午01:57:40
	 * @version 1.0
	 * @author wenjb
	 */
	public ActionForward entry(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String urlKey = (String) request.getParameter("urlkey");
		request.setAttribute("urlkey", urlKey);
		return mapping.findForward(FORWARD_ENTRY);
	}

	/**
	 * 
	 * 功能描述 列表，进入工作流，载入表单权限也从这进
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2007-12-17 下午01:57:40
	 * @version 1.0
	 * @author wenjb
	 */
	public ActionForward waitList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		List waitWfList;
		IWorkflowService IWorkflowService = this.getService();
		PersonWfSearchForm personWfSerarchForm = (PersonWfSearchForm) form;
		// 封装查询条件
		WfSearch wfsearch = personWfSerarchForm.getWfSearch();
		String title = (request.getParameter("title")) == null ? ""
				: ((String) request.getParameter("title"));
		String committer = (request.getParameter("committer")) == null ? ""
				: ((String) request.getParameter("committer"));
		String startTime = (request.getParameter("startTime")) == null ? ""
				: ((String) request.getParameter("startTime"));
		String endTime = (request.getParameter("endTime")) == null ? ""
				: ((String) request.getParameter("endTime"));
		String clearwf = (request.getParameter("clearwf")) == null ? ""
				: ((String) request.getParameter("clearwf"));
		wfsearch.setTitle(title);
		wfsearch.setCommitter(committer);
		wfsearch.setStartTime(startTime);
		wfsearch.setEndTime(endTime);

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
			if (null != session.getAttribute("wfsearch")) {
				session.removeAttribute("wfsearch");
			}
			session.setAttribute("wfsearch", wfsearch);
		} else {
			// 当前的URL中不存在查询条件
			// 如果SESSION中有查询条件，重置查询条件
			HttpSession session = SecurityContextInfo.getSession();
			if (null != session.getAttribute("wfsearch")) {
				wfsearch = (WfSearch) session.getAttribute("wfsearch");
			}
		}

		// 开始查询
		if ("".equals(wfsearch.getTitle())
				&& "".equals(wfsearch.getCommitter())
				&& "".equals(wfsearch.getStartTime())
				&& "".equals(wfsearch.getEndTime())) {
			// 无查询条件
			// 计算页数，得到当前页数
			int countPage = IWorkflowService.getWaitTaskCount(
					SecurityContextInfo.getCurrentUser(), null);
			countTemp = countPage;
			// 设置行数
			personWfSerarchForm.getPaginater().setCount(countPage);
			// 总的页数目
			int pageCountTemp = personWfSerarchForm.getPaginater()
					.getPageCount();
			// 处理得到pageNo
			// 设置页数,修复页数永远为0的危险
			try {
				if (Integer.parseInt(pageUrl) <= pageCountTemp) {
					personWfSerarchForm.getPaginater().setPage(
							Integer.parseInt(pageUrl));
				}
			} catch (Exception e) {
				personWfSerarchForm.getPaginater().setPage(0);
			}
			int pageNo = personWfSerarchForm.getPaginater().getPage() + 1;
			int pageSize = personWfSerarchForm.getPaginater().getPageSize();
			pageTemp2 = pageNo - 1;
			waitWfList = IWorkflowService.findWaitTask(SecurityContextInfo
					.getCurrentUser(), null, pageNo, pageSize);
		} else {
			// 有查询条件
			// 计算页数，得到当前页数
			int countPage = IWorkflowService.getWaitTaskCount(
					SecurityContextInfo.getCurrentUser(), wfsearch);
			countTemp = countPage;
			// 设置行数
			personWfSerarchForm.getPaginater().setCount(countPage);
			// 总的页数目
			int pageCountTemp = personWfSerarchForm.getPaginater()
					.getPageCount();
			// 处理得到pageNo
			// 设置页数,修复页数永远为0的危险
			try {
				if (Integer.parseInt(pageUrl) <= pageCountTemp) {
					personWfSerarchForm.getPaginater().setPage(
							Integer.parseInt(pageUrl));
				}
			} catch (Exception e) {
				personWfSerarchForm.getPaginater().setPage(0);
			}
			int pageNo = personWfSerarchForm.getPaginater().getPage() + 1;
			int pageSize = personWfSerarchForm.getPaginater().getPageSize();
			pageTemp2 = pageNo - 1;
			waitWfList = IWorkflowService.findWaitTask(SecurityContextInfo
					.getCurrentUser(), wfsearch, pageNo, pageSize);
		}
		if (waitWfList == null) {
			waitWfList = new ArrayList();
		}
		personWfSerarchForm.setWaitWfList(waitWfList);
		personWfSerarchForm.getPaginater().setCount(countTemp);
		personWfSerarchForm.getPaginater().setPage(pageTemp2);
		return mapping.findForward(FORWARD_WAITLIST);
	}

	/**
	 * 
	 * 功能描述 工作流跳转，截获Action,处理之后再跳
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2007-12-17 下午01:57:40
	 * @version 1.0
	 * @author wenjb
	 */
	public void startStep(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 读入配置的参数
		String workFlowName = "";
		String stepName = "";
		String url = "";
		try {
			workFlowName = (String) request.getParameter("wfname");
			stepName = (String) request.getParameter("wfstep");
			url = (String) request.getParameter("wfurl");
		} catch (Exception e) {
			workFlowName = "";
			stepName = "";
			url = "";
		}
		url = url.replace('|', '&');
		String contextPath = request.getContextPath();
		url = contextPath + url;

		String step = "";
		int index = url.lastIndexOf("=");
		step = url.substring(index + 1);
		if ("waitList".equals(step)) {
			// 待办任务，才进行权限的载入
			WorkFlowFormContext.loadFormPermission(workFlowName, stepName);
			//特殊业务需求，需要在代办任务中增加STEPID和WFNAME
			String stepId = (String) request.getParameter("stepId");
			url = url + "&stepId="+stepId +"&wfname="+workFlowName;	
		} else {
			WorkFlowFormContext.clearFormPermission();
		}
		response.sendRedirect(url);
	}

	/**
	 * 
	 * 功能描述 列表
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2007-12-17 下午01:57:40
	 * @version 1.0
	 * @author wenjb
	 */
	public ActionForward alreadyList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		IWorkflowService IWorkflowService = this.getService();
		List alreadyWfList;
		PersonWfSearchForm personWfSerarchForm = (PersonWfSearchForm) form;
		// 封装查询条件
		WfSearch wfsearch2 = personWfSerarchForm.getWfSearch();
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
		wfsearch2.setTitle(title);
		wfsearch2.setState(state);
		wfsearch2.setCommitter(committer);
		wfsearch2.setStartTime(startTime);
		wfsearch2.setEndTime(endTime);

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
			if (null != session.getAttribute("wfsearch2")) {
				session.removeAttribute("wfsearch2");
			}
			session.setAttribute("wfsearch2", wfsearch2);
		} else {
			// 当前的URL中不存在查询条件
			// 如果SESSION中有查询条件，重置查询条件
			HttpSession session = SecurityContextInfo.getSession();
			if (null != session.getAttribute("wfsearch2")) {
				wfsearch2 = (WfSearch) session.getAttribute("wfsearch2");
			}
		}

		// 开始查询
		if ("".equals(wfsearch2.getTitle()) && "".equals(wfsearch2.getState())
				&& "".equals(wfsearch2.getCommitter())
				&& "".equals(wfsearch2.getStartTime())
				&& "".equals(wfsearch2.getEndTime())) {
			// 无查询条件
			// 计算页数，得到当前页数
			int countPage = IWorkflowService.getOverTaskCount(
					SecurityContextInfo.getCurrentUser(), null);
			countTemp = countPage;
			// 设置行数
			personWfSerarchForm.getPaginater().setCount(countPage);
			// 总的页数目
			int pageCountTemp = personWfSerarchForm.getPaginater()
					.getPageCount();
			// 处理得到pageNo
			// 设置页数,修复页数永远为0的危险
			try {
				if (Integer.parseInt(pageUrl) <= pageCountTemp) {
					personWfSerarchForm.getPaginater().setPage(
							Integer.parseInt(pageUrl));
				}
			} catch (Exception e) {
				personWfSerarchForm.getPaginater().setPage(0);
			}
			int pageNo = personWfSerarchForm.getPaginater().getPage() + 1;
			int pageSize = personWfSerarchForm.getPaginater().getPageSize();
			pageTemp2 = pageNo - 1;
			alreadyWfList = IWorkflowService.findOverTask(SecurityContextInfo
					.getCurrentUser(), null, pageNo, pageSize);
		} else {
			// 有查询条件
			// 计算页数，得到当前页数
			int countPage = IWorkflowService.getOverTaskCount(
					SecurityContextInfo.getCurrentUser(), wfsearch2);
			countTemp = countPage;
			// 设置行数
			personWfSerarchForm.getPaginater().setCount(countPage);
			// 总的页数目
			int pageCountTemp = personWfSerarchForm.getPaginater()
					.getPageCount();
			// 处理得到pageNo
			// 设置页数,修复页数永远为0的危险
			try {
				if (Integer.parseInt(pageUrl) <= pageCountTemp) {
					personWfSerarchForm.getPaginater().setPage(
							Integer.parseInt(pageUrl));
				}
			} catch (Exception e) {
				personWfSerarchForm.getPaginater().setPage(0);
			}
			int pageNo = personWfSerarchForm.getPaginater().getPage() + 1;
			int pageSize = personWfSerarchForm.getPaginater().getPageSize();
			pageTemp2 = pageNo - 1;
			alreadyWfList = IWorkflowService.findOverTask(SecurityContextInfo
					.getCurrentUser(), wfsearch2, pageNo, pageSize);
		}
		if (alreadyWfList == null) {
			alreadyWfList = new ArrayList();
		}
		personWfSerarchForm.setAlreadyList(alreadyWfList);
		personWfSerarchForm.getPaginater().setCount(countTemp);
		personWfSerarchForm.getPaginater().setPage(pageTemp2);

		return mapping.findForward(FORWARD_ALREADYLIST);
	}

	// /**
	// *
	// * 功能描述 列表
	// *
	// * @param mapping
	// * @param form
	// * @param request
	// * @param response
	// * @return
	// * @throws Exception
	// * 2007-12-17 下午01:57:40
	// * @version 1.0
	// * @author wenjb
	// */
	// public ActionForward recieveList(ActionMapping mapping, ActionForm form,
	// HttpServletRequest request, HttpServletResponse response)
	// throws Exception {
	// IWorkflowService IWorkflowService = this.getService();
	// List recieveList;
	//
	// PersonWfSearchForm personWfSerarchForm = (PersonWfSearchForm) form;
	// // 封装查询条件
	// WfSearch wfsearch = personWfSerarchForm.getWfSearch();
	// String title = (request.getParameter("title")) == null ? ""
	// : ((String) request.getParameter("title"));
	// String committer = (request.getParameter("committer")) == null ? ""
	// : ((String) request.getParameter("committer"));
	// String startTime = (request.getParameter("startTime")) == null ? ""
	// : ((String) request.getParameter("startTime"));
	// String endTime = (request.getParameter("endTime")) == null ? ""
	// : ((String) request.getParameter("endTime"));
	// wfsearch.setTitle(title);
	// wfsearch.setCommitter(committer);
	// wfsearch.setStartTime(startTime);
	// wfsearch.setEndTime(endTime);
	// // 开始查询
	// if ("".equals(title) && "".equals(committer) && "".equals(startTime)
	// && "".equals(endTime)) {
	// // 不存在查询条件的出情况
	// recieveList = IWorkflowService.findConsignedTask(
	// SecurityContextInfo.getCurrentUser(), null);
	// } else {
	// recieveList = IWorkflowService.findConsignedTask(
	// SecurityContextInfo.getCurrentUser(), wfsearch);
	// }
	//
	// if (recieveList == null) {
	// recieveList = new ArrayList();
	// }
	// personWfSerarchForm.setRecieveList(personWfSerarchForm.getPaginater()
	// .dealList(recieveList, request));
	// return mapping.findForward(FORWARD_RECIEVELIST);
	// }
	//
	// /**
	// *
	// * 功能描述 列表
	// *
	// * @param mapping
	// * @param form
	// * @param request
	// * @param response
	// * @return
	// * @throws Exception
	// * 2007-12-17 下午01:57:40
	// * @version 1.0
	// * @author wenjb
	// */
	// public ActionForward entrustList(ActionMapping mapping, ActionForm form,
	// HttpServletRequest request, HttpServletResponse response)
	// throws Exception {
	// IWorkflowService IWorkflowService = this.getService();
	// List entrustList;
	//
	// PersonWfSearchForm personWfSerarchForm = (PersonWfSearchForm) form;
	// // 封装查询条件
	// WfSearch wfsearch = personWfSerarchForm.getWfSearch();
	// String title = (request.getParameter("title")) == null ? ""
	// : ((String) request.getParameter("title"));
	// String committer = (request.getParameter("committer")) == null ? ""
	// : ((String) request.getParameter("committer"));
	// String startTime = (request.getParameter("startTime")) == null ? ""
	// : ((String) request.getParameter("startTime"));
	// String endTime = (request.getParameter("endTime")) == null ? ""
	// : ((String) request.getParameter("endTime"));
	// wfsearch.setTitle(title);
	// wfsearch.setCommitter(committer);
	// wfsearch.setStartTime(startTime);
	// wfsearch.setEndTime(endTime);
	// // 开始查询
	// if ("".equals(title) && "".equals(committer) && "".equals(startTime)
	// && "".equals(endTime)) {
	// // 不存在查询条件的出情况
	// entrustList = IWorkflowService.findConsignTask(SecurityContextInfo
	// .getCurrentUser(), null);
	// } else {
	// entrustList = IWorkflowService.findConsignTask(SecurityContextInfo
	// .getCurrentUser(), wfsearch);
	// }
	// if (entrustList == null) {
	// entrustList = new ArrayList();
	// }
	// personWfSerarchForm.setEntrustList(personWfSerarchForm.getPaginater()
	// .dealList(entrustList, request));
	// return mapping.findForward(FORWARD_ENTRUSTLIST);
	// }
	/**
	 * 
	 * 功能描述 传阅任务列表
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return personPassroundWfList.jsp
	 * @throws Exception	             
	 * @author Bis liyan
	 */
	public ActionForward passroundList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		List passroundList;
		IWorkflowService IWorkflowService = this.getService();
		PersonWfSearchForm personWfSerarchForm = (PersonWfSearchForm) form;
		// 封装查询条件
		WfSearch wfsearch4 = personWfSerarchForm.getWfSearch();
		String title = (request.getParameter("title")) == null ? ""
				: ((String) request.getParameter("title"));
		String committer = (request.getParameter("committer")) == null ? ""
				: ((String) request.getParameter("committer"));
		String startTime = (request.getParameter("startTime")) == null ? ""
				: ((String) request.getParameter("startTime"));
		String endTime = (request.getParameter("endTime")) == null ? ""
				: ((String) request.getParameter("endTime"));
		String clearwf = (request.getParameter("clearwf")) == null ? ""
				: ((String) request.getParameter("clearwf"));
		wfsearch4.setTitle(title);
		wfsearch4.setCommitter(committer);
		wfsearch4.setStartTime(startTime);
		wfsearch4.setEndTime(endTime);

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
			if (null != session.getAttribute("wfsearch4")) {
				session.removeAttribute("wfsearch4");
			}
			session.setAttribute("wfsearch4", wfsearch4);
		} else {
			// 当前的URL中不存在查询条件
			// 如果SESSION中有查询条件，重置查询条件
			HttpSession session = SecurityContextInfo.getSession();
			if (null != session.getAttribute("wfsearch4")) {
				wfsearch4 = (WfSearch) session.getAttribute("wfsearch4");
			}
		}

		// 开始查询
		if ("".equals(wfsearch4.getTitle())
				&& "".equals(wfsearch4.getCommitter())
				&& "".equals(wfsearch4.getStartTime())
				&& "".equals(wfsearch4.getEndTime())) {
			// 无查询条件
			// 计算页数，得到当前页数
			int countPage = IWorkflowService.getPassRoundTaskCount(
					SecurityContextInfo.getCurrentUser(), null);
			countTemp = countPage;
			// 设置行数
			personWfSerarchForm.getPaginater().setCount(countPage);
			// 总的页数目
			int pageCountTemp = personWfSerarchForm.getPaginater()
					.getPageCount();
			// 处理得到pageNo
			// 设置页数,修复页数永远为0的危险
			try {
				if (Integer.parseInt(pageUrl) <= pageCountTemp) {
					personWfSerarchForm.getPaginater().setPage(
							Integer.parseInt(pageUrl));
				}
			} catch (Exception e) {
				personWfSerarchForm.getPaginater().setPage(0);
			}
			int pageNo = personWfSerarchForm.getPaginater().getPage() + 1;
			int pageSize = personWfSerarchForm.getPaginater().getPageSize();
			pageTemp2 = pageNo - 1;
			passroundList = IWorkflowService.findPassRoundTask(SecurityContextInfo
					.getCurrentUser(), null, pageNo, pageSize);
		} else {
			// 有查询条件
			// 计算页数，得到当前页数
			int countPage = IWorkflowService.getPassRoundTaskCount(
					SecurityContextInfo.getCurrentUser(), wfsearch4);
			countTemp = countPage;
			// 设置行数
			personWfSerarchForm.getPaginater().setCount(countPage);
			// 总的页数目
			int pageCountTemp = personWfSerarchForm.getPaginater()
					.getPageCount();
			// 处理得到pageNo
			// 设置页数,修复页数永远为0的危险
			try {
				if (Integer.parseInt(pageUrl) <= pageCountTemp) {
					personWfSerarchForm.getPaginater().setPage(
							Integer.parseInt(pageUrl));
				}
			} catch (Exception e) {
				personWfSerarchForm.getPaginater().setPage(0);
			}
			int pageNo = personWfSerarchForm.getPaginater().getPage() + 1;
			int pageSize = personWfSerarchForm.getPaginater().getPageSize();
			pageTemp2 = pageNo - 1;
			passroundList = IWorkflowService.findPassRoundTask(SecurityContextInfo
					.getCurrentUser(), wfsearch4, pageNo, pageSize);
		}
		if (passroundList == null) {
			passroundList = new ArrayList();
		}
		personWfSerarchForm.setPassroundList(passroundList);
		personWfSerarchForm.getPaginater().setCount(countTemp);
		personWfSerarchForm.getPaginater().setPage(pageTemp2);
		return mapping.findForward(FORWARD_PASSROUNDLIST);
	}

}
