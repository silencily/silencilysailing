package net.silencily.sailing.common.crud.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.common.crud.service.CommonTableViewService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.framework.codename.UserCodeName;
import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.framework.web.struts.BaseDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


/**
 * @author zhaoyifei
 *
 */
public class TableViewAction extends BaseDispatchAction {

	public ActionForward info(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) 
			throws Exception {
			String pageId=request.getParameter("pageId");
			UserCodeName ucn=ContextInfo.getContextUser();
			getService().getRows(pageId);
			getService().getRows(ucn.getUsername(), pageId);
			return mapping.findForward("info");
		}
	private CommonTableViewService getService() {
		return (CommonTableViewService)ServiceProvider.getService(CommonTableViewService.SERVICE_NAME);
	}
}
