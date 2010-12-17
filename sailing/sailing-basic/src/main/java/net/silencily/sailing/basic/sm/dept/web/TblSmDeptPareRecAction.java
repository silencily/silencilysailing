package net.silencily.sailing.basic.sm.dept.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.basic.sm.dept.service.TblSmDeptService;
import net.silencily.sailing.basic.sm.domain.TblCmnDept;
import net.silencily.sailing.basic.sm.domain.TblCmnDeptPareRec;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.security.SecurityContextInfo;
import net.silencily.sailing.security.model.CurrentUser;
import net.silencily.sailing.struts.DispatchActionPlus;
import net.silencily.sailing.utils.MessageInfo;
import net.silencily.sailing.utils.MessageUtils;
import net.silencily.sailing.utils.Tools;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


/**
 * 上级部门变更(DeptPareRecAction)
 * @author gaojing
 * @version $Id: TblSmDeptPareRecAction.java,v 1.1 2010/12/10 10:56:44 silencily Exp $
 * @since 2007-8-29
 */
public class TblSmDeptPareRecAction extends DispatchActionPlus {

//	public static TblCmnDeptPareRecService service() {
//		return (TblCmnDeptPareRecService) ServiceProvider.getService(TblCmnDeptPareRecService.SERVICE_NAME);
//	}

	/* 添加dept的服务 */
	public static TblSmDeptService serviceP() {
		return (TblSmDeptService) ServiceProvider.getService(TblSmDeptService.SERVICE_NAME);
	}
	
	private CommonService getService() {
		return (CommonService)ServiceProvider.getService(CommonService.SERVICE_NAME);
	}
/*
	public ActionForward entry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		TblCmnDeptPareRecForm theForm = castForm(form);
		return mapping.findForward(theForm.getStep());
	}
*/
	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		TblSmDeptPareRecForm theForm = castForm(form);
		/**/
		String p = request.getParameter("bean.parent.parentCd");
		

		if (!checkTree(theForm, p, request, response)) {
			return mapping.findForward(theForm.getStep());
		}
		TblCmnDeptPareRec dept = theForm.getPareBean();
		String deptName = theForm.getBean().getDeptName();
//		TblCmnDept parent = serviceP().load(p);
		TblCmnDept parent = (TblCmnDept)getService().load(TblCmnDept.class, p);
		if ("SHI".equalsIgnoreCase(parent.getIsGroup().getCode())){
			MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("HR_I087_R_1",parent.getDeptName()));
			return  mapping.findForward(theForm.getStep());
		}
		dept.getTblCmnDept().setParent(parent);
		getService().saveOrUpdate(dept);
//		service().saveOrUpdate(dept);
		/* 获取用户 */
		CurrentUser currentUser = SecurityContextInfo.getCurrentUser();
		/* 操作人 */
		theForm.getPareBean().setOperater(currentUser.getUserName());
		/* 获取当前系统时间 */
		theForm.getPareBean().setOperateTime(Tools.getNowTime());
		/* 原上级部门 变更前 */
		String begPDN = theForm.getBeginPDN();
		theForm.getPareBean().setBeginParent(begPDN);
		theForm.getPareBean().setBeginParentId(theForm.getBeginPDI());
		/* 新上级部门 变更后 */
		String deptN = request.getParameter("departmentnameNew");
		theForm.getPareBean().setEndParent(deptN);
		theForm.getPareBean().setEndParentId(theForm.getEndPDI());
//		serviceP().saveOrUpdate(theForm.getBean());
		getService().saveOrUpdate(theForm.getBean());
		List list = getService().getList(TblCmnDeptPareRec.class);
//		List list = service().list();
		theForm.setList(list);
		MessageUtils.addMessage(request, MessageInfo.factory().getMessage("HR_I009_R_3", deptName, begPDN, deptN));
		theForm.setStep("save");
		return mapping.findForward(theForm.getStep());
	}

	public ActionForward info(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		TblSmDeptPareRecForm theForm = castForm(form);
		//20071229 CHRZZJG00006 START
		String deptName = theForm.getBean().getDeptName();
		if(StringUtils.isBlank(deptName)){
			deptName = theForm.getBean().getParent().getDeptName();
		}
		String parentCode = theForm.getOid();
		if(StringUtils.isBlank(parentCode)){
			parentCode = theForm.getParentCode();
		}
		//20071229 CHRZZJG00006 END
		if (theForm.getBean().getImageType().equals("0")) {
			MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("HR_I037_C_1",
					theForm.getBean().getDeptName()));
			return mapping.findForward("error");
		}

		//20071229 CHRZZJG00006 START
		if(theForm.getBean().getDeptState()==null){
			if ("1".equals(theForm.getBean().getParent().getDeptState())) {
				MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("HR_I035_C_1", deptName));
				return mapping.findForward("error");
			}
		}else{
			if ("1".equals(theForm.getBean().getDeptState())) {
				
				MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("HR_I035_C_1", deptName));
				return mapping.findForward("error");
			}
		}
		//theForm.setBean(serviceP().load(parentCode));
		theForm.setBean((TblCmnDept)getService().load(TblCmnDept.class, parentCode));
		return mapping.findForward(theForm.getStep());
		//20071229 CHRZZJG00006 END
	}

	/** String id 要合并部门id */
	private boolean checkTree(TblSmDeptPareRecForm theForm, String id, HttpServletRequest request,
			HttpServletResponse response) {
		/* 当前部门id */
		String staticId = theForm.getBeginPDI();
		String parentId = "";
		if (staticId.equals(id)) {
			MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("HR_I055_C_0"));
			return false;
		}
		while (true) {
			if (null == id || id.equals("")) {
				MessageUtils.addErrorMessage(request, "error!!!");
				return false;
			}
			else {
				if (id.equals(TblCmnDept.ROOT_NODE_CODE)) {
					return true;
				}
				parentId = serviceP().getParentId(id);
				if (parentId.equals("")) {
					MessageUtils.addErrorMessage(request, "error!!!");
					return false;
				}
				if (parentId.equals(staticId)) {
					MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("HR_I032_C_1",
							theForm.getDepartmentnameNew()));
					return false;
				}
			}
			if (parentId.equals(TblCmnDept.ROOT_NODE_CODE)) {
				return true;
			}
			id = parentId;
		}
	}

	private TblSmDeptPareRecForm castForm(ActionForm form) {
		return (TblSmDeptPareRecForm) form;
	}
}
