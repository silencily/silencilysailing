package net.silencily.sailing.basic.sm.dept.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.basic.sm.domain.TblCmnDept;
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
 * @version $Id: TblCmnDeptStatusRecAction.java,v 1.1 2007/09/21 09:58:36 gaoj
 * Exp $
 * @since 2007-8-29
 */
public class TblSmDeptStatusRecAction extends DispatchActionPlus {

	/**
	 * 获取共通服务
	 * @return
	 */
	private CommonService getService() {
		
		return (CommonService)ServiceProvider.getService(CommonService.SERVICE_NAME);
	}
	
	public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		TblSmDeptStatusRecForm theForm = castForm(form);
		
		String parentCode = theForm.getOid();
		//20071229 CHRZZJG00006 START
		String deptName = theForm.getBean().getDeptName();
		if(StringUtils.isBlank(deptName)){
			deptName = theForm.getBean().getParent().getDeptName();
		}
		TblCmnDept dept = new TblCmnDept();
		if(StringUtils.isBlank(parentCode)){
			parentCode = theForm.getParentCode();
		}
		//20071229 CHRZZJG00006 END
		if (theForm.getBean().getImageType().equals("0")) {
			//20071229 CHRZZJG00006 START
			MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("HR_I036_C_1",deptName));
			//20071229 CHRZZJG00006 END
			return mapping.findForward("error");
		}
		TblCmnDept parentId = (TblCmnDept)getService().load(TblCmnDept.class, parentCode);
		
		/* 得到部门的bean */
		//20071229 CHRZZJG00006 START
		if(StringUtils.isBlank(theForm.getOid())){
			theForm.setBean(parentId);
		}
		dept = theForm.getBean();
		//20071229 CHRZZJG00006 END
		boolean notChange = false;
		int childrenCnt = parentId.getChildren().size();
		for (int i=0; i<childrenCnt; i++) {
			TblCmnDept childrenDept = (TblCmnDept)parentId.getChildren().get(i);
			if ("0".equals(childrenDept.getDeptState())) {
				notChange = true;
				break;
			}
		}
		
		if (notChange || dept.getTblCmnUsers().size() > 0) {
			
			MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("HR_I007_C_1",theForm.getBean().getDeptName()));
			
			return mapping.findForward("error");
		}
		if (StringUtils.isBlank(parentCode)) {
			
			MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("HR_I056_C_0"));
			
			return mapping.findForward("error");
		}
		else {
			
			return mapping.findForward(theForm.getStep());
		}
	}
	
	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		TblSmDeptStatusRecForm theForm = castForm(form);
		
		/* 获取用户 */
		
		CurrentUser currentUser = SecurityContextInfo.getCurrentUser();		
		/* 操作人 */		
		theForm.getStatusBean().setOperator(currentUser.getUserName());		
		/* 获取当前系统时间 */
		
		theForm.getStatusBean().setOperateTime(Tools.getNowTime());
		
		/* 变更前部门状态 */
		
		theForm.getStatusBean().setBeginState(theForm.getBeginstate());
		
		/* 变更后部门状态 */
		theForm.getStatusBean().setEndState(theForm.getStatusBean().getTblCmnDept().getDeptState());
		
		// 0没删除 1删除
		if ("0".equals(theForm.getStatusBean().getTblCmnDept().getDeptState())) {
			
			theForm.getStatusBean().getTblCmnDept().setDelFlg("0");
		}
		else {
			
			theForm.getStatusBean().getTblCmnDept().setDelFlg("1");
		}
		if (theForm.getBeginstate().equals(theForm.getStatusBean().getTblCmnDept().getDeptState())) {
			
			MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("HR_I033_C_1",theForm.getStatusBean().getTblCmnDept().getDeptName()));
		}
		else {
			
			getService().saveOrUpdate(theForm.getStatusBean());
			
			MessageUtils.addMessage(request, MessageInfo.factory().getMessage("HR_I008_R_1",theForm.getStatusBean().getTblCmnDept().getDeptName()));
		}
		theForm.setStep("edit");
		
		return edit(mapping, form, request, response);
	}
	
	private TblSmDeptStatusRecForm castForm(ActionForm form) {
		
		return (TblSmDeptStatusRecForm) form;
	}
}