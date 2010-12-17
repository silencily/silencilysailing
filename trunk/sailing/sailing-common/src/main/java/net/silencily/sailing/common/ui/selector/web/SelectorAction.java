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
 * ����ѡ������<code>Action</code>
 * @author liuz
 */
public class SelectorAction extends DispatchAction{
	
	//private static Log log = LogFactory.getLog(SelectorAction.class);
	
	private SelectorService selectorService = (SelectorService) ServiceProvider.getService(SelectorService.SERVICE_NAME);
	
	/**
	 * ��ʾ�û���
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
	 * ��ʾ������
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
	 * ��ʽ��ʾָ������<code>codeId</code>�����д���ڵ�
	 * ע����������ʽ��һ���ԣ����ԣ����codeId�½ڵ���Ŀ����̫��
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
	 * ��ʽ��ʾָ������<code>codeId</code>�¸�����´���ڵ�
	 * ע�������ǻ�ʽ���ԣ�����û�нڵ���Ŀ����
	 * ���������ajaxʵ��
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
