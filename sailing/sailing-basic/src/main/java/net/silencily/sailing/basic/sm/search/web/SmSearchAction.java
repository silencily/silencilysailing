package net.silencily.sailing.basic.sm.search.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.basic.sm.domain.TblCmnDept;
import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.basic.sm.user.service.UserManageService;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.framework.codename.UserCodeName;
import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.framework.persistent.filter.Condition;
import net.silencily.sailing.framework.persistent.filter.ConditionConstants;
import net.silencily.sailing.security.SecurityContextInfo;
import net.silencily.sailing.security.model.CurrentUser;
import net.silencily.sailing.struts.DispatchActionPlus;
import net.silencily.sailing.utils.MessageInfo;
import net.silencily.sailing.utils.MessageUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


/**
 * @author zhaoyifei
 *
 */
public class SmSearchAction extends DispatchActionPlus {

	public static final String FORWARD_ENTRY = "entry";
	public static final String FORWARD_LIST = "list";
	public static final String FORWARD_INFO = "info";
	public static final String FORWARD_OTHERLIST = "otherList";
	public static final String FORWARD_DEGEDU = "degEdu";
	public static final String PAGEID="hr_search";
	
	public static final String FORWARE_PERSONALENTRY = "personalEntry";
	public static final String FORWARE_PERSONALLIST = "personalList";
	
	public ActionForward entry(
		ActionMapping mapping, 
		ActionForm form, 
		HttpServletRequest request, 
		HttpServletResponse response) 
		throws Exception {
		SmSearchForm sf=(SmSearchForm)form;
		sf.setDeptRoot((TblCmnDept)getService().load(TblCmnDept.class,TblCmnDept.ROOT_NODE_CODE));
		return mapping.findForward(FORWARD_ENTRY);
	}

	public ActionForward list(
		ActionMapping mapping, 
		ActionForm form, 
		HttpServletRequest request, 
		HttpServletResponse response) 
		throws Exception {
		
		SmSearchForm theForm=(SmSearchForm)form;
		UserCodeName ucn = ContextInfo.getContextUser();
		String parentCode = theForm.getParentCode();
		if (parentCode == null)
			parentCode = request.getParameter("parentid");
		if (!StringUtils.isBlank(parentCode)) {
			if (!parentCode.equals("root")) {
				List deptIDList = serviceEmpInfo().getSubDept(parentCode);
				theForm.setParentCode(parentCode);
				Condition condition1 = new Condition();
				Condition[] condition2 = new Condition[deptIDList.size()];
				for (int i=0; i<deptIDList.size(); i++) {
					Condition condition = new Condition();
					condition.setName("tblCmnDept.id");
					condition.setOperator(ConditionConstants.EQUAL);
					condition.setValue(deptIDList.get(i));
					condition.setPrepend(ConditionConstants.OR);
					condition2[i] = condition;
				}
				condition1.setCompositeConditions(condition2);
				ContextInfo.getContextCondition()
				.addAppendConditions(condition1);
			}
		}		
		request.setAttribute("viewBean",getService().fetchAll(TblCmnUser.class,ucn.getUsername(),PAGEID));
		return mapping.findForward(FORWARD_LIST);
	}
	
	public ActionForward personalEntry(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) 
			throws Exception {
		
		SmSearchForm sf=(SmSearchForm)form;
		sf.setDeptRoot((TblCmnDept)getService().load(TblCmnDept.class,TblCmnDept.ROOT_NODE_CODE));
		return mapping.findForward(FORWARE_PERSONALENTRY);
	}

	public ActionForward personalList(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) 
			throws Exception {
		
		CurrentUser currentUser = SecurityContextInfo.getCurrentUser();		
		Condition condition = new Condition();
		condition.setName("empCd");
		condition.setOperator(ConditionConstants.EQUAL);
		condition.setValue(currentUser.getEmpCd());
		ContextInfo.getContextCondition().addAppendConditions(condition);

		request.setAttribute("viewBean",getService().fetchAll(TblCmnUser.class,currentUser.getEmpCd(),PAGEID));

		return mapping.findForward(FORWARE_PERSONALLIST);
	}
	
	public ActionForward info(
		ActionMapping mapping, 
		ActionForm form, 
		HttpServletRequest request, 
		HttpServletResponse response) 
		throws Exception {
		String oid=request.getParameter("oid");
		SmSearchForm sf=(SmSearchForm)form;
		if(StringUtils.isBlank(oid))
		{
			MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("HR_I053_C_0"));
			return mapping.findForward("globalMessage");
		}
		TblCmnUser result=(TblCmnUser) getService().load(TblCmnUser.class,oid);
		
		sf.setResult(result);
		request.setAttribute(mapping.getAttribute(),sf);
		return mapping.findForward(FORWARD_INFO);
	}
	
	public ActionForward otherList(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) 
			throws Exception {
		
		return mapping.findForward(FORWARD_OTHERLIST);
	}
	public ActionForward changeInfoList(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) 
			throws Exception {
			
		return mapping.findForward("change");
	}
	public ActionForward degEduInfo(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) 
			throws Exception {
			
		return mapping.findForward(FORWARD_DEGEDU);
	}
	public ActionForward empEntRecInfo(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) 
			throws Exception {
			
		return mapping.findForward("entRec");
	}
	public ActionForward empTraRecInfo (
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) 
			throws Exception {
			
		return mapping.findForward("traRec");
	}

	/**
	 * 调用共通服务
	 */
	private CommonService getService() {
		return (CommonService)ServiceProvider.getService(CommonService.SERVICE_NAME);
	}
	
	public static UserManageService serviceEmpInfo() {
		return (UserManageService) ServiceProvider
				.getService(UserManageService.SERVICE_NAME);
	}
	
	public ActionForward selectEntry(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) 
			throws Exception {
		SmSearchForm sf=(SmSearchForm)form;
			
		String checkType = request.getParameter("checkType");
		if (checkType != null && !"".equals(checkType)) {
			sf.setCheckType(checkType);
		} else {
			sf.setCheckType("radio");
		}
		
		sf.setDeptRoot((TblCmnDept)getService().load(TblCmnDept.class,TblCmnDept.ROOT_NODE_CODE));
		return mapping.findForward("selectEntry");
	}
	public ActionForward selectInfo(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) 
			throws Exception {
		
		SmSearchForm sf=(SmSearchForm)form;
		
		String checkType = request.getParameter("checkType");
		if (checkType != null && !"".equals(checkType)) {
			sf.setCheckType(checkType);
		} else {
			sf.setCheckType("radio");
		}

		String parentCode = sf.getParentCode();
		if(parentCode==null)
			parentCode=request.getParameter("parentCode");
		if (!StringUtils.isBlank(parentCode)) {
			if(!parentCode.equals("root")) {
				List deptIDList = serviceEmpInfo().getSubDept(parentCode);
				sf.setParentCode(parentCode);
				Condition condition1 = new Condition();
				Condition[] condition2 = new Condition[deptIDList.size()];
				for (int i=0; i<deptIDList.size(); i++) {
					Condition condition = new Condition();
					condition.setName("tblCmnDept.id");
					condition.setOperator(ConditionConstants.EQUAL);
					condition.setValue(deptIDList.get(i));
					condition.setPrepend(ConditionConstants.OR);
					condition2[i] = condition;
				}
				condition1.setCompositeConditions(condition2);
				ContextInfo.getContextCondition()
					.addAppendConditions(condition1);
			}
		}
		sf.setInfoList(getService().getList(TblCmnUser.class));
		
		return mapping.findForward("selectInfo");
	}
}
