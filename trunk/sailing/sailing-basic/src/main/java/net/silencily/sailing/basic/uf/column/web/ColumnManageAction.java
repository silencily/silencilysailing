package net.silencily.sailing.basic.uf.column.web;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.basic.sm.user.service.UserManageService;
import net.silencily.sailing.basic.uf.column.service.ColumnManageService;
import net.silencily.sailing.basic.uf.domain.TblUfColumn;
import net.silencily.sailing.basic.uf.domain.TblUfPubPermission;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.struts.DispatchActionPlus;
import net.silencily.sailing.utils.MessageInfo;
import net.silencily.sailing.utils.MessageUtils;
import net.silencily.sailing.utils.Tools;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


public class ColumnManageAction extends DispatchActionPlus {
	
    private static final String FORWARD_ENTRY = "entry";
	private static final String FORWARD_LIST = "list";
    private static final String FORWARD_INFO = "info";

    /**
     * ��ת��Entryҳ�档
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
    public ActionForward entry(ActionMapping mapping, 
            ActionForm form,
            HttpServletRequest request, 
            HttpServletResponse response) {
        return mapping.findForward(FORWARD_ENTRY);
    }

    /**
	 * ���涨����Ŀά����
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
	   ColumnManageForm theForm = (ColumnManageForm)form;
	   theForm.setListDesktop(lds);
	   return mapping.findForward(FORWARD_LIST);
   }
	/**
	 * ���涨����Ŀ���¡�
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
   public ActionForward update(   ActionMapping mapping,
						           ActionForm    form,
						           HttpServletRequest request,
						           HttpServletResponse response){
	    ColumnManageForm theForm = (ColumnManageForm)form;
		for (int i = 0;i < theForm.getDesk().size(); i++){
			if (theForm.getDesk().get(i) == null){
				continue;
			}
			//TblCommonDesktop tcdt = (TblCommonDesktop)theForm.getDesk().get(i); 
			TblUfColumn tcdt = (TblUfColumn)theForm.getDesk().get(i);
		
			//��ҳ�洫������IDΪ��ʱ�����Ʊ�������ݣ�����������ݡ�
			getService().save(tcdt);
		}
		//���ݸ������ʱ��ˢ��ҳ��ʱʹ��
		
		
		List lds = getService().list();
		theForm.setListDesktop(lds);
		MessageUtils.addMessage(request, "������Ŀά���ɹ���");
		return mapping.findForward(FORWARD_LIST);
	}

    /**
     * ɾ��
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward delete(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

//        ColumnManageForm theForm = (ColumnManageForm) form;
        String oid = request.getParameter("oid");
        
//        theForm.setBean(getService().load(oid));
        if (getService().deleteAllRel(TblUfColumn.class, TblUfPubPermission.class, "tblUfColumn", Tools.split(oid,"\\$"))) {
//        getService().delete(theForm.getBean());
            MessageUtils.addMessage(request, MessageInfo.factory().getMessage("UF_I013_R_0"));
        } else {
            MessageUtils.addWarnMessage(request, MessageInfo.factory().getMessage("UF_I021_R_0"));
        }
        return list(mapping, form, request, response);
    }

    /**
     * �༭ҳ�����
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward info(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        
        ColumnManageForm theForm = (ColumnManageForm) form;
        if (StringUtils.isBlank(theForm.getBean().getId())) {
        //�½�ʱ
            MessageUtils.addMessage(request, MessageInfo.factory().getMessage("UF_I009_P_0"));
            theForm.setBean(getService().newInstance());
        } else {
        //�༭ʱ
            MessageUtils.addMessage(request, MessageInfo.factory().getMessage("UF_I016_P_0"));
            theForm.setPubPermission(new ArrayList(theForm.getBean().getTblUfPubPermission()));
        }
        
        return mapping.findForward(FORWARD_INFO);
    }

    /**
     * ���涨����Ŀ���¡�
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
   public ActionForward save(   ActionMapping mapping,
                                ActionForm    form,
                                HttpServletRequest request,
                                HttpServletResponse response){
        ColumnManageForm theForm = (ColumnManageForm)form;
        TblUfColumn tuc = (TblUfColumn)theForm.getBean();
        tuc.setTblUfPubPermission(new HashSet());
        
        for (int i = 0;i < theForm.getPubPermission().size();i++){
            if(theForm.getPubPermission(i)==null)
                continue;
            if(theForm.getPubPermission(i).getTblHrEmpInfo()==null) //ְ����Ϣ�գ��˳�
                continue;           
            if(StringUtils.isBlank(theForm.getPubPermission(i).getDelFlg()))
                theForm.getPubPermission(i).setDelFlg("0");

            theForm.getPubPermission(i).setTblUfColumn(tuc);
            theForm.getPubPermission(i).getTblHrEmpInfo().getEmpCd();
            tuc.getTblUfPubPermission().add(theForm.getPubPermission(i));
        }

        //��ҳ�洫������IDΪ��ʱ�����Ʊ�������ݣ�����������ݡ�
        getService().save(tuc);

        for (int i = theForm.getPubPermission().size() - 1;i >= 0;i--){
            if(theForm.getPubPermission(i)==null) {
                theForm.getPubPermission().remove(i);
                continue;
            }
            if(theForm.getPubPermission(i).getTblHrEmpInfo()==null) {
                theForm.getPubPermission().remove(i);
                continue;
            }
            if ("1".equals(theForm.getPubPermission(i).getDelFlg())) {
                theForm.getPubPermission().remove(i);
            }
        }

        if (StringUtils.isBlank(request.getParameter("oid"))) {
        //�½�ʱ
            MessageUtils.addMessage(request, MessageInfo.factory().getMessage("UF_I015_R_0"));
        } else {
        //�༭ʱ
            MessageUtils.addMessage(request, MessageInfo.factory().getMessage("UF_I014_R_0"));
        }
        return mapping.findForward(FORWARD_INFO);
//        return info(mapping, theForm, request, response);

    }

    public ColumnManageService getService(){
		return (ColumnManageService)ServiceProvider.getService(ColumnManageService.SERVICE_NAME);
    }

    public UserManageService getEmpService(){
        return(UserManageService)ServiceProvider.getService(UserManageService.SERVICE_NAME);
    }
}
