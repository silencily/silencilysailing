package net.silencily.sailing.common.ui.selector.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.common.ui.selector.service.SelectorService;
import net.silencily.sailing.container.ServiceProvider;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;


/**
 * 功用选择树的<code>Action</code>
 * @author liuz
 */
public class SelectorAction extends DispatchAction{
	
	//private static Log log = LogFactory.getLog(SelectorAction.class);
	
	private SelectorService selectorService = (SelectorService) ServiceProvider.getService(SelectorService.SERVICE_NAME);
	
	/**
	 * 显示用户树
	 */
	public ActionForward selectUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		SelectorForm theForm  = (SelectorForm) form;
		String id = theForm.getId();
		List list = selectorService.listUserTree(id);
		theForm.setUserList(list);
		return mapping.findForward("selectUser");
	}
	
	/**
	 * 显示部门树
	 * 
	 */
	public ActionForward selectDept(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) 
			throws IOException{
		
		SelectorForm theForm  = (SelectorForm) form;
		String id = theForm.getId();
		List list = selectorService.listDeptTree(id);
		theForm.setDeptList(list);
		return mapping.findForward("selectDept");
	}
	
	/**
	 * 显式显示指定代码<code>codeId</code>下所有代码节点
	 * 注意这里是显式（一次性）加栽，因此codeId下节点数目不能太多
	 */
	public ActionForward selectCode(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) 
			throws IOException{
		
		SelectorForm theForm  = (SelectorForm) form;
		String id = theForm.getId();
		List list = selectorService.listAllCodeByParentId(id);
		theForm.setCodeList(list);
		return mapping.findForward("selectCode");
	} 
	
	/**
	 * 缓式显示指定代码<code>codeId</code>下根层次下代码节点
	 * 注意这里是缓式加栽，这里没有节点数目限制
	 * 这里采用了ajax实现
	 */
	public ActionForward getCodeTreeNode(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) 
			throws IOException{
		
		SelectorForm theForm  = (SelectorForm) form;
		String id = theForm.getId();
		List list = selectorService.listCodeByParentId(id);
		theForm.setCodeList(list);
		return mapping.findForward("displayCode");
	}
	
}
