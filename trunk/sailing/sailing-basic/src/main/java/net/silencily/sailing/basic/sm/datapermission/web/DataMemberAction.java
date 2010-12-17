package net.silencily.sailing.basic.sm.datapermission.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.basic.sm.datapermission.service.TblDataPermissionService;
import net.silencily.sailing.basic.sm.domain.TblCmnEntity;
import net.silencily.sailing.basic.sm.domain.TblCmnEntityMember;
import net.silencily.sailing.basic.sm.domain.TblCmnModel;
import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.basic.sm.domain.TblCmnUserMember;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.common.dict.domain.CommonBasicCode;
import net.silencily.sailing.common.domain.tree.FlatTreeUtils;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.framework.codename.UserCodeName;
import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.framework.web.view.ComboSupportList;
import net.silencily.sailing.struts.DispatchActionPlus;
import net.silencily.sailing.utils.MessageInfo;
import net.silencily.sailing.utils.MessageUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class DataMemberAction extends DispatchActionPlus {

	public static final String FORWARD_ENTRY = "entry";

	public static final String FORWARD_LIST = "list";

	public static final String FORWARD_INFO = "info";

	public static final String PAGEID = "cmn_member";

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
		request.setAttribute(
				"viewBean",
				getService().fetchAll(TblCmnEntityMember.class,
						ucn.getUsername(), PAGEID));

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
		DataMemberForm theForm = (DataMemberForm) form;
		String parentId = theForm.getParentId();
		TblCmnEntityMember bean = new TblCmnEntityMember();
		if ("2".equals(request.getParameter("level"))) {
			bean = (TblCmnEntityMember) getService().load(
					TblCmnEntityMember.class, parentId);

			theForm.setBean(bean);
			request.setAttribute(mapping.getAttribute(), theForm);
			return mapping.findForward("info");
		}
		return null;
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

		DataMemberForm theForm = (DataMemberForm) form;
		String oid = request.getParameter("oid");
		TblCmnEntityMember Bean = (TblCmnEntityMember) getService().load(
				TblCmnEntityMember.class, oid);
		getService().deleteLogic(Bean);
		return list(mapping, theForm, request, response);
	}

	/**
	 * 功能描述：新增后保存员工调入信息
	 */
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DataMemberForm theForm = (DataMemberForm) form;
		TblCmnEntityMember memberBean = theForm.getBean();
		String userId = request.getParameter("userId");
		TblCmnUser userBean = (TblCmnUser) getService().load(TblCmnUser.class,
				userId);
		TblCmnUserMember userMemberBean = new TblCmnUserMember();
		userMemberBean.setSearchScope(theForm.getSearchScope());
		userMemberBean.setUpdateScope(theForm.getUpdateScope());
		userMemberBean.setCreateScope(theForm.getCreateScope());
		userMemberBean.setDeleteScope(theForm.getDeleteScope());
		userMemberBean.setTblCmnEntityMember(memberBean);
		userMemberBean.setTblCmnUser(userBean);
		getService().saveOrUpdate(userMemberBean);

		MessageUtils.addMessage(request,
				MessageInfo.factory().getMessage("HR_I003_R_0"));
		return mapping.findForward("info");
	}

	public ActionForward tree(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DataMemberForm theForm = (DataMemberForm) form;
		String level = request.getParameter("level");
		String parentId = theForm.getParentId();
		if (StringUtils.isBlank(parentId)) {
			parentId = "root";
		}
		Collection items = new ArrayList();
		if (parentId == "root") {
			// items = new ArrayList();
			ComboSupportList sysList = (ComboSupportList) ((Map) theForm
					.getSysCodes().get("SM")).get("MODELS");
			Iterator it = sysList.iterator();
			while (it.hasNext()) {
				TblCmnModel m = new TblCmnModel();
				CommonBasicCode bc = (CommonBasicCode) it.next();
				m.setCode(bc.getCode());
				m.setName(bc.getName());
				items.add(m);
			}

		} else if (StringUtils.isNotBlank(level) && level.equals("0")) {
			// items = new ArrayList();
			TblCmnModel m = new TblCmnModel();
			m.setCode(parentId);
			items = m.getChildren();
		} else {
			TblCmnEntity cate = (TblCmnEntity) getService().load(
					TblCmnEntity.class, parentId);
			items = cate.getChildren();
			// items = service().list(cate, "2");
		}

		// TblCmnModels models=
		// (TblCmnModels)((Map)theForm.getSysCodes().get("SM")).get("MODELS");
		// theForm.getSysCodes("SM").getSysCodes("MODELS").getClass();
		// List sysList = (List) theForm.getCodes("SM").get("MODELS");

		/* 获得parentId的子节点信息 */

		response.setContentType("text/plain; charset=GBK");
		response.getWriter().print(FlatTreeUtils.serialize(items, false));
		// response.getWriter().print(FlatTreeUtils.serialize(list, false));
		return null;
	}

}
