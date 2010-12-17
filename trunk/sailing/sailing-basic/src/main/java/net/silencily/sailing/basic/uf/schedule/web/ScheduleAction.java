package net.silencily.sailing.basic.uf.schedule.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.basic.uf.domain.TblUfSchedule;
import net.silencily.sailing.basic.uf.schedule.service.ScheduleService;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.common.message.MessageInsert;
import net.silencily.sailing.common.message.domain.TblUfMessage;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.security.SecurityContextInfo;
import net.silencily.sailing.struts.DispatchActionPlus;
import net.silencily.sailing.utils.MessageInfo;
import net.silencily.sailing.utils.MessageUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


/**
 * @author zhangwenqi
 *
 */
public class ScheduleAction extends DispatchActionPlus {
	private static String FORWARD_ENTRY = "entry";
	private static String FORWARD_CALENDAR = "calendar";
	private static String FORWARD_LIST = "list";
	private static String FORWARD_INFO = "info";
	public static final String PAGEID = "uf_schedule";
	
	/**
	 * 跳转到Entry页面。
	 * @return ActionForward(entry)
	 */
	public ActionForward entry(ActionMapping mapping, 
			                    ActionForm form,
			                    HttpServletRequest request, 
			                    HttpServletResponse response)
			throws Exception {		
		return mapping.findForward(FORWARD_ENTRY);
	}
	
	/**
	 * 跳转到calendar页面。
	 * @return ActionForward(calendar)
	 */
	public ActionForward calendar(ActionMapping mapping, 
			                    ActionForm form,
			                    HttpServletRequest request, 
			                    HttpServletResponse response)
			throws Exception {
		ScheduleForm theForm = (ScheduleForm)form;
		if(StringUtils.isBlank(theForm.getCurrentDay()))
		{
			theForm.setCurrentDay(service().getCurrentDay());
		}
		theForm.setMemo(service().getMemo(theForm.getCurrentDay()));
		return mapping.findForward(FORWARD_CALENDAR);
	}
	
	/**
	 * 
	 * 功能描述 跳转到list页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-26 下午06:45:30
	 * @version 1.0
	 * @author yuqian
	 */
	public ActionForward list( ActionMapping mapping, 
			                    ActionForm form,
			                    HttpServletRequest request, 
			                    HttpServletResponse response)
			throws Exception {	
		ScheduleForm theForm = (ScheduleForm)form;
		theForm.setCurrentDay(request.getParameter("currentDay"));
		theForm.setList(service().list(theForm.getCurrentDay()));
		return mapping.findForward(FORWARD_LIST);
	}
	
	/**
	 * 
	 * 功能描述 跳转到详细页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * 2007-12-27 上午09:53:32
	 * @version 1.0
	 * @author yuqian
	 */
	public ActionForward info( ActionMapping mapping, 
			                    ActionForm form,
			                    HttpServletRequest request, 
			                    HttpServletResponse response){		
		ScheduleForm theForm = (ScheduleForm)form;
		String oid = request.getParameter("oid");
		if(StringUtils.isBlank(oid))//如果oid为空，尝试从form中取得oid
		{
			oid = theForm.getOid();
		}
		if (StringUtils.isBlank(oid)){ //如果oid为空，,新建一条记录，设置新建状态提示信息
			
			theForm.setBean(new TblUfSchedule());
			MessageUtils.addMessage(request, MessageInfo.factory().getMessage("UF_I009_P_0"));
			//BusinessContext.setOperType(OperType.ADD);
		}else
		{//如果oid不为空，读取记录，设置修改状态提示信息
			theForm.setBean((TblUfSchedule)getService().load(TblUfSchedule.class, oid));
			if(!"YIWC".equals(theForm.getBean().getCompleteFlg().getCode())){
				MessageUtils.addMessage(request, MessageInfo.factory().getMessage("UF_I016_P_0"));
			}
			//BusinessContext.setOperType(OperType.EDIT);
		}
		return mapping.findForward(FORWARD_INFO);
	}
	
	/**
	 * 
	 * 功能描述 保存详细页面信息
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-27 上午10:01:40
	 * @version 1.0
	 * @author yuqian
	 */
	public ActionForward save( ActionMapping mapping, 
			                    ActionForm form,
			                    HttpServletRequest request, 
			                    HttpServletResponse response){	
		ScheduleForm theForm = (ScheduleForm)form;
		theForm.getBean().setEmpCd(SecurityContextInfo.getCurrentUser().getEmpCd());
		try{
			getService().saveOrUpdate(theForm.getBean());
			if(StringUtils.isBlank(theForm.getOid())){//如果oid为空，表示当前为新建，设置新建成功的提示信息
				if(!"0".equals(theForm.getBean().getAlertTime()))
				{
					TblUfMessage tblUfMessage = service().saveMseeage(theForm.getBean());
					theForm.getBean().setMessageId(tblUfMessage.getId());
				}
				getService().saveOrUpdate(theForm.getBean());
				MessageUtils.addMessage(request,MessageInfo.factory().getMessage("UF_I015_R_0"));
			}else{//如果oid不为空，表示当前为修改，设置修改成功的提示信息
				if(!"0".equals(theForm.getBean().getAlertTime()))
				{
					if(StringUtils.isBlank(theForm.getBean().getMessageId()))
					{
						TblUfMessage tblUfMessage = service().saveMseeage(theForm.getBean());
						theForm.getBean().setMessageId(tblUfMessage.getId());
						getService().saveOrUpdate(theForm.getBean());
					}
					service().updataMessage(theForm.getBean());
				}
				MessageUtils.addMessage(request,MessageInfo.factory().getMessage("UF_I014_R_0"));
			}
			theForm.setOid(theForm.getBean().getId());
			//BusinessContext.setOperType(OperType.EDIT);
		}catch (Exception e) {
			e.printStackTrace();
			MessageUtils.addErrorMessage(request,"保存失败");
		}
		//return info(mapping,theForm,request,response);
		return mapping.findForward(FORWARD_INFO);
	}
	
	/**
	 * 
	 * 功能描述 删除日程记录的方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-27 下午06:10:40
	 * @version 1.0
	 * @author yuqian
	 */
	public ActionForward delete(ActionMapping mapping, 
					            ActionForm form,
					            HttpServletRequest request, 
					            HttpServletResponse response) 
	throws Exception{
		ScheduleForm theForm = (ScheduleForm)form;
		String oid = request.getParameter("oid");
		theForm.setBean((TblUfSchedule)getService().load(TblUfSchedule.class, oid));
		try{
			getService().delete(theForm.getBean());
			if(!StringUtils.isBlank(theForm.getBean().getMessageId()))
			{
				MessageInsert.delete(theForm.getBean().getMessageId());
			}
			MessageUtils.addMessage(request,MessageInfo.factory().getMessage("UF_I013_R_0"));
		}catch (Exception e) {
			e.printStackTrace();
			MessageUtils.addErrorMessage(request,MessageInfo.factory().getMessage("UF_I006_R_0"));
		}
		return list(mapping,theForm,request,response);
	}
	
	/**
	 * 
	 * 功能描述 取得服务的方法
	 * @return
	 * 2007-12-27 上午09:47:08
	 * @version 1.0
	 * @author yuqian
	 */
	public ScheduleService service(){
		return (ScheduleService)ServiceProvider.getService(ScheduleService.SERVICE_NAME);
	}
	
	/**
	 * 
	 * 功能描述 取得共通方法
	 * @return
	 * 2007-12-26 下午02:27:21
	 * @version 1.0
	 * @author yuqian
	 */
	private CommonService getService() {
		return (CommonService) ServiceProvider
				.getService(CommonService.SERVICE_NAME);
	}
}