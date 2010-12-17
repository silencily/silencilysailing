package net.silencily.sailing.basic.wf.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.basic.wf.domain.TblWfEditInfo;
import net.silencily.sailing.basic.wf.service.NapeEditService;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.struts.DispatchActionPlus;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class NapeEditAction extends DispatchActionPlus{
	
	/**
	 * �õ��ĳ����Ķ���
	 */
	private static final String FORWARD_SELECT = "select";

	/**
	 * 
	 * �������� ȡ�ÿɱ༭��service
	 * @return  �ɱ༭���service
	 * 2008-5-7����11:20:51
	 * @version 1.0
	 * @author yangxl
	 */
	public static NapeEditService service() {
		return (NapeEditService) ServiceProvider
				.getService(NapeEditService.SERVICE_NAME);
	}
	
	public ActionForward select(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		try{
			String nameoper = request.getParameter("nameoper");
			NapeEditForm theForm = (NapeEditForm) form;
			List list = service().getList(TblWfEditInfo.class,nameoper);
			theForm.setList(list);
			request.setAttribute("nameoper", nameoper);
		}catch(Exception e){
			e.printStackTrace();
		}
		return mapping.findForward(FORWARD_SELECT);		
	}
	/**
	 * 
	 * ��ȡ��ͨ����
	 * @return  ��ͨ��service
	 * 2008-5-7����11:30:51
	 * @version 1.0
	 * @author yangxl
	 */
	private CommonService getService() {
		return (CommonService)ServiceProvider.getService(CommonService.SERVICE_NAME);
	}
}
