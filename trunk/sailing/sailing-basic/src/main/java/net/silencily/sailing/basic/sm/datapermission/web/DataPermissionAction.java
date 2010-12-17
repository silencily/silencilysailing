package net.silencily.sailing.basic.sm.datapermission.web;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.basic.sm.datapermission.service.TblDataPermissionService;
import net.silencily.sailing.basic.sm.domain.TblCmnEntity;
import net.silencily.sailing.basic.sm.domain.TblCmnEntityMember;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.framework.codename.UserCodeName;
import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.struts.DispatchActionPlus;
import net.silencily.sailing.utils.MessageInfo;
import net.silencily.sailing.utils.MessageUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class DataPermissionAction extends DispatchActionPlus {

	public static final String FORWARD_ENTRY = "entry";

	public static final String FORWARD_LIST = "list";

	public static final String FORWARD_INFO = "info";
	
	public static final String PAGEID = "cmn_entity";


	/**
	 * 调用共同Service()接口
	 */
	private CommonService getService() {
		return (CommonService) ServiceProvider
				.getService(CommonService.SERVICE_NAME);
	}
	
	
	public static TblDataPermissionService service() {
		return (TblDataPermissionService) ServiceProvider
				.getService(TblDataPermissionService.SERVICE_NAME);
	}


	/**
	 * 进入画面
	 */
	public ActionForward entry(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward("entry");
	}

	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             列表页面
	 */
	public ActionForward list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		UserCodeName ucn = ContextInfo.getContextUser();
		request.setAttribute("viewBean", getService().fetchAll(
				TblCmnEntity.class, ucn.getUsername(), PAGEID));
		
		return mapping.findForward("list");

	}
	

	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             详细信息页面
	 */
	public ActionForward info(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DataPermissionForm theForm = (DataPermissionForm) form;
		if (theForm.getOid() == null || "".equals(theForm.getOid())) {
			TblCmnEntity bean = new TblCmnEntity();
			theForm.setMember(null);
			MessageUtils.addMessage(request, MessageInfo.factory().getMessage(
					"SM_I001_P_0"));
			theForm.setBean(bean);
			request.setAttribute(mapping.getAttribute(), theForm);
			return mapping.findForward(FORWARD_INFO);
		}
		else {
		theForm.setMember(service().list(TblCmnEntityMember.class,
				theForm.getOid()));
		request.setAttribute(mapping.getAttribute(), theForm); 
		return mapping.findForward("info");
		}
	    }   
	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             删除
	 */
	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DataPermissionForm theForm = (DataPermissionForm) form;
		String oid = request.getParameter("oid");
		TblCmnEntity Bean = (TblCmnEntity)getService().load(TblCmnEntity.class, oid);
		if (null != theForm.getBean().getTblCmnEntityMember() && 0 != theForm.getBean().getTblCmnEntityMember().size()){
            MessageUtils.addWarnMessage(request, MessageInfo.factory().getMessage("SM_I052_R_0"));              
        } 
		else {
		getService().deleteLogic(Bean);
		}
		return list(mapping, theForm, request, response);
	}
	
	
	   /**
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     *             新增
     */
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DataPermissionForm theForm = (DataPermissionForm) form;
		try {
            getService().saveOrUpdate(theForm.getBean());
        } catch (Exception e) {
            MessageUtils.addErrorMessage(request, MessageInfo.factory()
                    .getMessage("SM_I023_R_0"));
            return info(mapping, theForm, request, response);
        }
        for (int i = 0; i < theForm.getMember().size(); i++) {
            if (theForm.getMember().get(i) == null)
                continue;
            if(StringUtils.isBlank(theForm.getMember(i).getDelFlg()))
                theForm.getMember(i).setDelFlg("0");
            TblCmnEntityMember ths = (TblCmnEntityMember) theForm.getMember().get(i);
            ths.setTblCmnEntity(theForm.getBean());
            if(StringUtils.isBlank(ths.getId())){
                getService().saveOrUpdate(ths);
            }
        }
		MessageUtils.addMessage(request, MessageInfo.factory().getMessage(
				"HR_I003_R_0"));

		return mapping.findForward(FORWARD_INFO);
	}

	   /**
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     *             修改
     */
	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DataPermissionForm theForm = (DataPermissionForm) form;	
        for (int i = 0; i < theForm.getMember().size(); i++) {
            if (theForm.getMember().get(i) == null)
                continue;
            TblCmnEntityMember ths = (TblCmnEntityMember) theForm.getMember().get(i);
            if (null != ths.getTblCmnUserMember() && 0 != ths.getTblCmnUserMember().size()&& "1".equals(theForm.getMember(i).getDelFlg())){
                MessageUtils.addWarnMessage(request, MessageInfo.factory().getMessage("SM_I052_R_0"));
                theForm.getMember(i).setDelFlg("0");
                return info(mapping, theForm, request, response);
            } 
        }
                try {
                    getService().saveOrUpdate(theForm.getBean());
                } catch (Exception e) {
                    MessageUtils.addErrorMessage(request, MessageInfo.factory()
                            .getMessage("HR_I049_C_0"));
                    return info(mapping, theForm, request, response);
                }
                for (int i = 0; i < theForm.getMember().size(); i++) {
                    if (theForm.getMember().get(i) == null)
                        continue;
                TblCmnEntityMember ths = (TblCmnEntityMember) theForm.getMember().get(i);
                if(StringUtils.isBlank(theForm.getMember(i).getDelFlg()))
                    theForm.getMember(i).setDelFlg("0");
                ths.setTblCmnEntity(theForm.getBean());
                if(StringUtils.isBlank(ths.getId())){
                    getService().saveOrUpdate(ths);
            }
                MessageUtils.addMessage(request, MessageInfo.factory().getMessage(
                "HR_I003_R_0"));
            }
	return info(mapping, theForm, request, response);
	}
	
}
