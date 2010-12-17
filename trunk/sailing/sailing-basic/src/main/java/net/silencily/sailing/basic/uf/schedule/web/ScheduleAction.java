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
	 * ��ת��Entryҳ�档
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
	 * ��ת��calendarҳ�档
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
	 * �������� ��ת��listҳ��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-26 ����06:45:30
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
	 * �������� ��ת����ϸҳ��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * 2007-12-27 ����09:53:32
	 * @version 1.0
	 * @author yuqian
	 */
	public ActionForward info( ActionMapping mapping, 
			                    ActionForm form,
			                    HttpServletRequest request, 
			                    HttpServletResponse response){		
		ScheduleForm theForm = (ScheduleForm)form;
		String oid = request.getParameter("oid");
		if(StringUtils.isBlank(oid))//���oidΪ�գ����Դ�form��ȡ��oid
		{
			oid = theForm.getOid();
		}
		if (StringUtils.isBlank(oid)){ //���oidΪ�գ�,�½�һ����¼�������½�״̬��ʾ��Ϣ
			
			theForm.setBean(new TblUfSchedule());
			MessageUtils.addMessage(request, MessageInfo.factory().getMessage("UF_I009_P_0"));
			//BusinessContext.setOperType(OperType.ADD);
		}else
		{//���oid��Ϊ�գ���ȡ��¼�������޸�״̬��ʾ��Ϣ
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
	 * �������� ������ϸҳ����Ϣ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-27 ����10:01:40
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
			if(StringUtils.isBlank(theForm.getOid())){//���oidΪ�գ���ʾ��ǰΪ�½��������½��ɹ�����ʾ��Ϣ
				if(!"0".equals(theForm.getBean().getAlertTime()))
				{
					TblUfMessage tblUfMessage = service().saveMseeage(theForm.getBean());
					theForm.getBean().setMessageId(tblUfMessage.getId());
				}
				getService().saveOrUpdate(theForm.getBean());
				MessageUtils.addMessage(request,MessageInfo.factory().getMessage("UF_I015_R_0"));
			}else{//���oid��Ϊ�գ���ʾ��ǰΪ�޸ģ������޸ĳɹ�����ʾ��Ϣ
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
			MessageUtils.addErrorMessage(request,"����ʧ��");
		}
		//return info(mapping,theForm,request,response);
		return mapping.findForward(FORWARD_INFO);
	}
	
	/**
	 * 
	 * �������� ɾ���ճ̼�¼�ķ���
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-27 ����06:10:40
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
	 * �������� ȡ�÷���ķ���
	 * @return
	 * 2007-12-27 ����09:47:08
	 * @version 1.0
	 * @author yuqian
	 */
	public ScheduleService service(){
		return (ScheduleService)ServiceProvider.getService(ScheduleService.SERVICE_NAME);
	}
	
	/**
	 * 
	 * �������� ȡ�ù�ͨ����
	 * @return
	 * 2007-12-26 ����02:27:21
	 * @version 1.0
	 * @author yuqian
	 */
	private CommonService getService() {
		return (CommonService) ServiceProvider
				.getService(CommonService.SERVICE_NAME);
	}
}