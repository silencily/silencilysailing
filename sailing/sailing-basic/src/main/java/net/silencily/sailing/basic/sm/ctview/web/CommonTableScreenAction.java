package net.silencily.sailing.basic.sm.ctview.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.common.crud.domain.CommonTableScreen;
import net.silencily.sailing.common.crud.domain.CommonTableView;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.common.crud.service.CommonTableViewService;
import net.silencily.sailing.common.crud.service.ViewBean;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.security.SecurityContextInfo;
import net.silencily.sailing.security.model.CurrentUser;
import net.silencily.sailing.struts.DispatchActionPlus;
import net.silencily.sailing.utils.MessageInfo;
import net.silencily.sailing.utils.MessageUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 
 * @author
 * @version
 */
public class CommonTableScreenAction extends DispatchActionPlus {
	public static final String FORWARD_LIST = "list";
	public static final String FORWARD_INFO = "info";
	public static final String FORWARD_ENTRY = "entry";
	public static final String SM_CTVIEW = "sm_ctview";

	/**
	 * 公共service入口
	 * 
	 * @return
	 * @version 1.0
	 * @author
	 */
	private CommonService getCommonService() {

		return (CommonService) ServiceProvider
				.getService(CommonService.SERVICE_NAME);
	}

	/**
	 * 
	 * @return
	 * @version 1.0
	 * @author
	 */
	private CommonTableViewService getService() {

		return (CommonTableViewService) ServiceProvider
				.getService(CommonTableViewService.SERVICE_NAME);
	}

	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @version 1.0
	 * @author
	 * @throws Exception
	 */
	public ActionForward entry(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward(FORWARD_ENTRY);
	}

	/**
	 * 列表页面入口
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @version 1.0
	 * @author
	 * @throws Exception
	 */
	public ActionForward list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CurrentUser ucn = SecurityContextInfo.getCurrentUser();
		ViewBean viewBean = getCommonService().fetchAll(
				CommonTableScreen.class, ucn.getEmpCd(), SM_CTVIEW);
		request.setAttribute("viewBean", viewBean);
		return mapping.findForward(FORWARD_LIST);
	}

	/**
	 * 列表页面删除
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @version 1.0
	 * @author
	 * @throws Exception
	 */
	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String oid = request.getParameter("oid");
		CommonTableScreen bean = (CommonTableScreen) getCommonService().load(
				CommonTableScreen.class, oid);
		try {
			getCommonService().delete(bean);
		} catch (Exception e) {
			MessageUtils.addErrorMessage(request, MessageInfo.factory()
					.getMessage("CM_I046_C_0"));
			return list(mapping, form, request, response);
		}
		MessageUtils.addMessage(request,
				MessageInfo.factory().getMessage("CM_I047_C_0"));
		return list(mapping, form, request, response);
	}

	/**
	 * 详细页面入口
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @version 1.0
	 * @author
	 * @throws Exception
	 */
	public ActionForward info(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CommonTableScreenForm theForm = (CommonTableScreenForm) form;
		List list = new ArrayList();
		if (StringUtils.isBlank(theForm.getOid())) {
			theForm.setBean(new CommonTableScreen());
			theForm.setOrderAsc("0");
			MessageUtils.addMessage(request,
					MessageInfo.factory().getMessage("CM_I016_P_0"));
		} else {
			String tableName = theForm.getBean().getTableName();
			list = getService().getTableViews(tableName);
			MessageUtils.addMessage(request,
					MessageInfo.factory().getMessage("CM_I017_P_0"));
		}
		theForm.setTableViews(list);
		if (list.size() > 0) {
			CommonTableView tableView = (CommonTableView) list.get(0);
			theForm.setOrderAsc(tableView.getOrderAsc());
		}
		return mapping.findForward(FORWARD_INFO);
	}

	/**
	 * 详细页面保存
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @version 1.0
	 * @author
	 * @throws Exception
	 */
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CommonTableScreenForm theForm = (CommonTableScreenForm) form;
		String oid = theForm.getBean().getId();
		CommonTableScreen bean = theForm.getBean();
		List tableViews = theForm.getTableViews();
		try {
			getService().save(tableViews, bean);
			if (StringUtils.isBlank(oid)) {
				MessageUtils.addMessage(request, MessageInfo.factory()
						.getMessage("CM_I005_R_0"));
			} else {
				MessageUtils.addMessage(request, MessageInfo.factory()
						.getMessage("CM_I004_R_0"));
			}
			theForm.setOid(bean.getId());
		} catch (RuntimeException e) {
			MessageUtils.addErrorMessage(request, MessageInfo.factory()
					.getMessage(e.getMessage()));
		} finally {

		}
		return mapping.findForward(FORWARD_INFO);
	}
}