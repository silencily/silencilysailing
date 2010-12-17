package net.silencily.sailing.basic.uf.columnorder.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.basic.uf.columnorder.service.TblColumnOrderService;
import net.silencily.sailing.basic.uf.domain.TblUfColumn;
import net.silencily.sailing.basic.uf.domain.TblUfColumnOrder;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.security.SecurityContextInfo;
import net.silencily.sailing.struts.DispatchActionPlus;
import net.silencily.sailing.utils.MessageUtils;
import net.silencily.sailing.utils.Tools;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


/**
 * @author wangchc
 *
 * @version 1.0 2007-11-9
 */

public class TblColumnOrderAction extends DispatchActionPlus {
   private static final String FORWARD_LIST = "list";
	/**
	 * ���涨����Ŀ�б���ʾ��
	 * 
	 * @param mapping
	 *            ActionMapping.
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * 
	 * @return ActionForward(list)
	 */
   public ActionForward list( ActionMapping mapping,
							   ActionForm form,
					           HttpServletRequest request,
		                       HttpServletResponse response){
	   List lds = getService().list();
	   TblColumnOrderForm theForm = (TblColumnOrderForm)form;
	   theForm.setListDesktop(lds);
	   request.setAttribute("lds", lds);
	   theForm.setLoginCd(SecurityContextInfo.getCurrentUser().getEmpCd());
	   return mapping.findForward(FORWARD_LIST);
   }
   
	/**
	 * �������涨����Ŀ��
	 * 
	 * @param mapping
	 *            ActionMapping.
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * 
	 * @return ActionForward(list)
	 */
   public ActionForward update( ActionMapping mapping,
		                         ActionForm    form,
		                         HttpServletRequest request,
		                         HttpServletResponse response){
	   TblColumnOrderForm theForm = (TblColumnOrderForm)form;
	   for (int i = 0;i < theForm.getDesk().size(); i++){
		   if (theForm.getDesk().get(i) == null){
			   continue;
		   }
		   //TblCommonDesktop tcdt = (TblCommonDesktop)theForm.getDesk().get(i); 
		   TblUfColumnOrder tcdt = (TblUfColumnOrder)theForm.getDesk().get(i);
		   TblUfColumnOrder tcdtdb = new TblUfColumnOrder();
		   //��ҳ�洫������IDΪ��ʱ�����Ʊ�������ݣ�����������ݡ�
		   if (StringUtils.isNotBlank(tcdt.getId())){
			   tcdtdb = getService().loadOrder(tcdt.getId());
			   if (StringUtils.isNotBlank(tcdtdb.getId())){
				   tcdtdb.setIsSelect(tcdt.getIsSelect());
				   tcdtdb.setDisplaySort(tcdt.getDisplaySort());
				   //tcdt.setTblUfColumn(getService().load(((TblUfColumn)theForm.getParentid().get(i)).getId()));
				   getService().update(tcdtdb);
			   }	   
		   }else if(tcdt.getIsSelect().equals("1")){ //��һ���ڶ�����ʾʱ���붨�Ʊ�
			   tcdtdb.setId(Tools.getPKCode());
			   tcdtdb.setTblUfColumn(getService().load(((TblUfColumn)theForm.getParentid().get(i)).getId()));
			   tcdtdb.setEmpCd(SecurityContextInfo.getCurrentUser().getEmpCd());
			   tcdtdb.setIsSelect(tcdt.getIsSelect());
			   tcdtdb.setDisplaySort(tcdt.getDisplaySort());
			   tcdtdb.setVersion(Integer.valueOf("0"));
			   getService().save(tcdtdb);
			   tcdtdb = null;
		   }
	   }
	   //���ݸ������ʱ��ˢ��ҳ��ʱʹ��
	   List lds = getService().list();
	   theForm.setListDesktop(lds);
	   theForm.setLoginCd(SecurityContextInfo.getCurrentUser().getEmpCd());
	   MessageUtils.addMessage(request, "��������ɹ���");
	   return mapping.findForward(FORWARD_LIST);
   }
   
   
   public TblColumnOrderService getService(){
		return (TblColumnOrderService)ServiceProvider.getService(TblColumnOrderService.SERVICE_NAME);
   }
}


