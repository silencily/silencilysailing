package net.silencily.sailing.common.dict.web;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.common.dict.domain.CommonBasicCode;
import net.silencily.sailing.common.dict.service.CommonBasicCodeService;
import net.silencily.sailing.common.domain.tree.FlatTreeUtils;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.framework.web.view.ComboSupportList;
import net.silencily.sailing.framework.web.view.taglibs.ExtendComboCompositeTag;
import net.silencily.sailing.struts.BaseFormPlus;
import net.silencily.sailing.struts.DispatchActionPlus;
import net.silencily.sailing.utils.MessageInfo;
import net.silencily.sailing.utils.MessageUtils;
import net.silencily.sailing.utils.Tools;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataIntegrityViolationException;


/**
 * @author zhaoyifei
 */
public class CommonBasicCodeAction extends DispatchActionPlus {
	public static CommonBasicCodeService service() {
		return (CommonBasicCodeService) ServiceProvider.getService(CommonBasicCodeService.SERVICE_NAME);
	}

	public ActionForward entry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
		HttpServletResponse response) throws Exception {
		CommonBasicCodeForm theForm = (CommonBasicCodeForm) form;
		String parentCode = request.getParameter("parentCode");
		if (StringUtils.isBlank(parentCode)) {
			parentCode = CommonBasicCode.ROOT_NODE_CODE;
		}
		CommonBasicCode hr = service().load(parentCode);
		if (theForm.getRoot() == null) {
			theForm.setRoot(parentCode);
			theForm.setBean(hr);
		}
		return mapping.findForward(theForm.getStep());
	}

	public ActionForward tree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
		HttpServletResponse response) throws Exception {
		CommonBasicCodeForm theForm = (CommonBasicCodeForm) form;
		String parentCode = theForm.getParentCode();
		if (StringUtils.isBlank(parentCode)) {
			parentCode = theForm.getRoot();
		}
		CommonBasicCode hr = service().load(parentCode);
		List list = service().list(hr);
		response.setContentType("text/plain; charset=GBK");
		response.getWriter().print(FlatTreeUtils.serialize(list, false));
		return null;
	}

	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request,
		HttpServletResponse response) throws Exception {
		CommonBasicCodeForm theForm = (CommonBasicCodeForm) form;
		String parentCode = theForm.getParentCode();
		if (StringUtils.isBlank(parentCode)) {
			parentCode = CommonBasicCode.ROOT_NODE_CODE;
		}
		CommonBasicCode hr = service().load(parentCode);
		List list = service().list(hr);
		// if(list.size()==0)
		// list=service().list(hr.getParent());
		/* 用于显示所属节点信息 */
		theForm.setBean(hr);
		for (int i=0; i<list.size(); i++) {
			CommonBasicCode basicCd = (CommonBasicCode) list.get(i);
			basicCd.setCodeDesc(Tools.abbreviate(basicCd.getCodeDesc(), 14));
		}
		theForm.setList(list);
		return mapping.findForward("list");
	}

	public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
		HttpServletResponse response) throws Exception {
		CommonBasicCodeForm theForm = (CommonBasicCodeForm) form;
		String id = request.getParameter("oid");
		String type = request.getParameter("type");
		
		if (StringUtils.isBlank(id)) {
			id = theForm.getOid();
		}
		if (!"new".equals(type) && StringUtils.isBlank(id)) {
			id = theForm.getParent();
			if (StringUtils.isBlank(id)) {
				id = theForm.getParentCode();
			}
		}
		CommonBasicCode hr;
		if (!StringUtils.isBlank(id)) {
			if ("new".equals(type)) {
				hr = service().newInstance(id);
				MessageUtils.addMessage(request, MessageInfo.factory().getMessage("CM_I016_P_0"));
			} else {
				hr = service().get(id);
				if (hr != null) {
					MessageUtils.addMessage(request, MessageInfo.factory().getMessage("CM_I017_P_0"));
				}
			}
			if (hr == null) {
				hr = service().newInstance(theForm.getParentCode());
				MessageUtils.clearMessages(request);
				MessageUtils.addMessage(request, MessageInfo.factory().getMessage("CM_I016_P_0"));
			}
		} else {
			if (StringUtils.isBlank(theForm.getParent())) {
				hr = service().newInstance(theForm.getParentCode());
			} else {
				hr = service().newInstance(theForm.getParent());
			}
			MessageUtils.addMessage(request, MessageInfo.factory().getMessage("CM_I016_P_0"));
		}
		if (hr == null) {
			MessageUtils.clearMessages(request);
			MessageUtils.addMessage(request, MessageInfo.factory().getMessage("CM_I016_P_0"));
			return mapping.findForward(theForm.getStep());
		}
		theForm.setBean(hr);
		return mapping.findForward(theForm.getStep());
	}

	public ActionForward detail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
		HttpServletResponse response) throws Exception {
		CommonBasicCodeForm theForm = (CommonBasicCodeForm) form;
		return mapping.findForward(theForm.getStep());
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
		HttpServletResponse response) throws Exception {
		CommonBasicCodeForm theForm = (CommonBasicCodeForm) form;
		String id = request.getParameter("oid");
		CommonBasicCode hr = service().load(id);
		if ("1".equals(hr.getDeleteState())) {
			service().delete(hr);
			theForm.setStep("list");
			MessageUtils.addMessage(request, MessageInfo.factory().getMessage("HR_I043_R_0"));
		} else {
			MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("CM_I041_C_0"));
		}
		return list(mapping, form, request, response);
	}

	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request,
		HttpServletResponse response) throws Exception {
		boolean isCreate = false;
		CommonBasicCodeForm theForm = (CommonBasicCodeForm) form;
		theForm.getBean().setParent(service().load(theForm.getParent()));
		if (theForm.getBean().getVersion() == null) {
			isCreate = true;
			theForm.getBean().setDeleteState("1");
			theForm.getBean().setLayerNum(new Integer(theForm.getBean().getParent().getLayerNum().intValue() + 1));
		}

		try {
			String newCode = theForm.getBean().getCode();
			String oldCode = request.getParameter("oldCode");
			if (!newCode.equals(oldCode)) {
				List existCode = service().getListByCode(theForm.getBean().getCode());
				if (existCode.size() > 0) {
					MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("HR_I031_C_1",theForm.getBean().getCode()));
					theForm.setStep("edit");
					return mapping.findForward(theForm.getStep());
				}
			}
			service().saveOrUpdate(theForm.getBean());
		} catch (DataIntegrityViolationException e) {
			MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("HR_I031_C_1",theForm.getBean().getCode()));// HR_I047_C_0
			theForm.setStep("edit");
			return mapping.findForward(theForm.getStep());
		}
		MessageUtils.clearMessages(request);
		if (isCreate) {
			MessageUtils.addMessage(request, MessageInfo.factory().getMessage("CM_I005_R_0"));
		} else {
			MessageUtils.addMessage(request, MessageInfo.factory().getMessage("CM_I004_R_0"));
		}
		theForm.setStep("edit");
		return mapping.findForward(theForm.getStep());
	}
	
	//根据URL传入的CODE值，得到基础编码列表
	public ActionForward getBasicCodeBySubIdAndCode(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		//根据URL传入的CODE值，得到基础编码列表
		String typeCode = request.getParameter("typeCode");
		String subId =  request.getParameter("subId");
		Object temp = (BaseFormPlus.getCodes(subId).get(typeCode));	
        response.setContentType("text/plain; charset=GBK");
		response.getWriter().write(ExtendComboCompositeTag.getJavaScriptArrayFromComboSupportList((ComboSupportList) temp));	
		return null;
	}	
	
}
