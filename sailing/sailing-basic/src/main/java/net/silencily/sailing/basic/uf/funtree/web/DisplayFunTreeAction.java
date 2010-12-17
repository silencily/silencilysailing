package net.silencily.sailing.basic.uf.funtree.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.basic.uf.funtree.service.DisplayFunTreeService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.security.SecurityContextInfo;
import net.silencily.sailing.struts.DispatchActionPlus;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


/**
 * 一体化办公功能树显示Action
 * @author huxf
 * @version $Id: DisplayFunTreeAction.java,v 1.1 2010/12/10 10:56:46 silencily Exp $
 * @since 2007-11-21
 */
public class DisplayFunTreeAction extends DispatchActionPlus{
	private final String DISPLAY_FORWARD = "display";
	/**
	 * 获得本功能实现Service
	 * @return XXXService
	 */
	public static DisplayFunTreeService service() {
		return (DisplayFunTreeService) ServiceProvider.getService(DisplayFunTreeService.SERVICE_NAME);
	}
	
	/**
	 * 功能Tree初期显示
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward display(ActionMapping mapping, 
								ActionForm form, 
								HttpServletRequest request,
								HttpServletResponse response)
								throws Exception {
		try {
			// get the tree form
			DisplayFunTreeForm theForm = new DisplayFunTreeForm();
			
			List treeList = new ArrayList();
			treeList = service().getFunTree(SecurityContextInfo.getCurrentUser().getUserId(), request.getContextPath());
			theForm.setTreeList(treeList);
			request.setAttribute("theForm", theForm);

			return mapping.findForward(this.DISPLAY_FORWARD);
		} catch (Exception e) {
			e.printStackTrace();
			return mapping.findForward(this.DISPLAY_FORWARD);
		}
	}
}
