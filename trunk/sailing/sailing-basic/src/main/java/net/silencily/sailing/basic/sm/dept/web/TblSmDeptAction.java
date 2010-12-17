package net.silencily.sailing.basic.sm.dept.web;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.basic.sm.dept.service.TblSmDeptService;
import net.silencily.sailing.basic.sm.domain.TblCmnDept;
import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.common.dict.service.BasicCodeService;
import net.silencily.sailing.common.domain.tree.FlatTreeUtils;
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
import org.springframework.dao.DataIntegrityViolationException;

/**
 * 组织部门(DeptAction)
 * 
 * @author gaojing
 * @version $Id: TblSmDeptAction.java,v 1.1 2010/12/10 10:56:44 silencily Exp $
 * @since 2007-8-29
 */
public class TblSmDeptAction extends DispatchActionPlus {

	public static TblSmDeptService service() {
		return (TblSmDeptService) ServiceProvider
				.getService(TblSmDeptService.SERVICE_NAME);
	}

	public ActionForward entry(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		TblSmDeptForm theForm = castForm(form);
		TblCmnDept bean = (TblCmnDept) getService().load(TblCmnDept.class,
				TblCmnDept.ROOT_NODE_CODE);
		theForm.setBean(bean);
		return mapping.findForward(theForm.getStep());
	}

	public ActionForward tree(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		TblSmDeptForm theForm = castForm(form);
		String parentCode = theForm.getParentCode();
		if (StringUtils.isBlank(parentCode)) {
			parentCode = TblCmnDept.ROOT_NODE_CODE;
		}
		TblCmnDept dept = (TblCmnDept) getService().load(TblCmnDept.class,
				parentCode);
		List list = service().list(dept, "2");
		response.setContentType("text/plain; charset=GBK");
		response.getWriter().print(FlatTreeUtils.serialize(list, false));
		return null;
	}

	public ActionForward filterTree(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		TblSmDeptForm theForm = castForm(form);
		String parentCode = theForm.getParentCode();
		if (StringUtils.isBlank(parentCode)) {
			parentCode = TblCmnDept.ROOT_NODE_CODE;
		}
		TblCmnDept dept = (TblCmnDept) getService().load(TblCmnDept.class,
				parentCode);
		// 0 正在使用
		List list = service().list(dept, "0");
		response.setContentType("text/plain; charset=GBK");
		response.getWriter().print(FlatTreeUtils.serialize(list, false));
		return null;
	}

	// 部门合并时的部门选择树

	public ActionForward showDeptTree(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		TblSmDeptForm theForm = castForm(form);
		String treeType = request.getParameter("treeType");
		if (treeType == null || treeType.equals("")) {
			theForm.setTreeType(treeType);
		}
		String parentCode = request.getParameter("parentCode");
		if (parentCode == null || parentCode.equals("")) {
			parentCode = TblCmnDept.ROOT_NODE_CODE;
		}
		TblCmnDept dept = (TblCmnDept) getService().load(TblCmnDept.class,
				parentCode);
		if (theForm.getRoot() == null) {
			theForm.setRoot(parentCode);
			theForm.setBean(dept);
		}
		return mapping.findForward(theForm.getStep());
	}

	public ActionForward list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		TblSmDeptForm theForm = castForm(form);
		String parentCode = theForm.getParentCode();
		if (StringUtils.isBlank(parentCode)) {
			parentCode = TblCmnDept.ROOT_NODE_CODE;
		}
		TblCmnDept dept = (TblCmnDept) getService().load(TblCmnDept.class,
				parentCode);
		List list = service().list(dept, "0");
		theForm.setBean(dept);
		theForm.setList(list);
		return mapping.findForward(theForm.getStep());
	}

	public ActionForward edit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		TblSmDeptForm theForm = castForm(form);
		if (theForm.getBean().getImageType().equals("0")) {
			MessageUtils
					.addErrorMessage(
							request,
							MessageInfo.factory().getMessage("HR_I036_C_1",
									theForm.getBean().getDeptName()));
			return mapping.findForward("error");
		}
		if ("SHI".equalsIgnoreCase(theForm.getBean().getParent().getIsGroup()
				.getCode())) {
			MessageUtils.addErrorMessage(
					request,
					MessageInfo.factory().getMessage("HR_I087_R_1",
							theForm.getBean().getParent().getDeptName()));
			return mapping.findForward("error");
		}
		if (StringUtils.isBlank(theForm.getBean().getDeptCd())) {
			// 20071225 PHRJGBM00002 START
			// 获取部门的Name
			String deptName = theForm.getBean().getParent().getDeptName();
			// 判断部门的状态
			if (theForm.getBean().getParent().getDeptState().equals("1")) {
				// 部门状态为停止使用时
				MessageUtils.addErrorMessage(request, MessageInfo.factory()
						.getMessage("HR_I085_C_1", deptName));
				return mapping.findForward("error");
			} else {
				// 部门状态为使用时
				MessageUtils.addMessage(request, MessageInfo.factory()
						.getMessage("HR_I041_P_0"));
				theForm.setBean(service().newInstance(theForm.getParentCode()));
			}
			// 20071225 PHRJGBM00002 END
		} else {
			MessageUtils.addMessage(request,
					MessageInfo.factory().getMessage("HR_I042_P_0"));
		}
		return mapping.findForward(theForm.getStep());
	}

	public ActionForward detail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		TblSmDeptForm theForm = castForm(form);
		return mapping.findForward(theForm.getStep());
	}

	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		TblSmDeptForm theForm = castForm(form);
		String deptId = request.getParameter("oid");
		if (!StringUtils.isBlank(deptId)) {
			if ("SHI"
					.equalsIgnoreCase(theForm.getBean().getIsGroup().getCode())) {
				TblCmnDept parentId = (TblCmnDept) getService().load(
						TblCmnDept.class, deptId);
				if (parentId.isHasChildren()) {
					MessageUtils.addErrorMessage(request, MessageInfo.factory()
							.getMessage("HR_I090_C_0"));
					return mapping.findForward("edit");
				}
			}
		}
		// 20071229 CHRZZJG00005 START
		theForm.getBean().setDeptCd(theForm.getBean().getDeptCd().trim());
		// 20071229 CHRZZJG00005 END
		TblCmnDept dept = theForm.getBean();
		String deptCd = request.getParameter("bean.deptCd");
		// 20071213 PHRJGBM01004/PHRJGBM01006 START
		if (dept.getDeptState().trim().equals("正在使用")) {
			dept.setDeptState("0");
			dept.setDelFlg("0");
		} else {
			dept.setDeptState("1");
			dept.setDelFlg("1");
		}
		try {
			if (null == theForm.getBean().getId()
					|| "" == theForm.getBean().getId()) {
				MessageUtils.addMessage(request, MessageInfo.factory()
						.getMessage("HR_I004_R_0"));
			} else {
				MessageUtils.addMessage(request, MessageInfo.factory()
						.getMessage("HR_I003_R_0"));
			}
			getService().saveOrUpdate(theForm.getBean());
		} catch (DataIntegrityViolationException de) {
			// 20071229 CHRZZJG00007 START
			theForm.getBean().setId("");
			MessageUtils.clearMessages(request);
			// 20071229 CHRZZJG00007 END
			MessageUtils.addErrorMessage(request, MessageInfo.factory()
					.getMessage("HR_I031_C_1", deptCd));
		}
		// 20071213 PHRJGBM01004/PHRJGBM01006 END

		theForm.setStep("edit");

		// 20071212 PHRJGBM01001 START
		return mapping.findForward(theForm.getStep());
		// 20071212 PHRJGBM01001 END
	}

	public ActionForward selectAllRadio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		TblSmDeptForm theForm = castForm(form);

		String parentCode = theForm.getParentCode();

		if (StringUtils.isBlank(parentCode)) {

			parentCode = TblCmnDept.ROOT_NODE_CODE;
		}
		TblCmnDept dept = (TblCmnDept) getService().load(TblCmnDept.class,
				parentCode);

		// 0 正在使用 1停止使用 2 所有

		List list = service().list(dept, "2");

		theForm.setBean(dept);

		theForm.setList(list);

		return mapping.findForward(theForm.getStep());
	}

	public ActionForward selectRadio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		TblSmDeptForm theForm = castForm(form);

		String parentCode = theForm.getParentCode();

		if (StringUtils.isBlank(parentCode)) {

			parentCode = TblCmnDept.ROOT_NODE_CODE;
		}
		TblCmnDept dept = (TblCmnDept) getService().load(TblCmnDept.class,
				parentCode);

		// 0 正在使用 1停止使用 2 所有

		List list = service().list(dept, "1");

		theForm.setBean(dept);

		theForm.setList(list);

		return mapping.findForward(theForm.getStep());
	}

	/* 部门合并 */

	public ActionForward deptEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		TblSmDeptForm theForm = castForm(form);

		TblCmnDept dept = new TblCmnDept();

		dept = theForm.getBean();

		if (theForm.getBean().getDeptName()
				.equals(theForm.getDepartmentnameNew())) {

			MessageUtils.addErrorMessage(request, MessageInfo.factory()
					.getMessage("HR_I073_C_0", theForm.getDepartmentnameNew()));
		} else {

			TblCmnDept parent = (TblCmnDept) getService().load(
					TblCmnDept.class, theForm.getBean().getParentId());

			Set s = dept.getTblCmnUsers();

			Iterator i = s.iterator();

			while (i.hasNext()) {

				TblCmnUser the = (TblCmnUser) i.next();

				the.setTblCmnDept(parent);

				parent.getTblCmnUsers().add(the);
			}
			/* 清空合并部门下的人员信息 */

			dept.getTblCmnUsers().clear();

			getService().saveOrUpdate(dept);

			getService().saveOrUpdate(parent);

			/* 获取用户 */

			CurrentUser currentUser = SecurityContextInfo.getCurrentUser();
			theForm.getStatusBean().setOperator(currentUser.getUserName());

			/* 获取当前系统时间 */

			theForm.getStatusBean().setOperateTime(Tools.getNowTime());

			if (theForm.getBeginstate().trim().equals("正在使用")) {

				theForm.getStatusBean().setBeginState("0");

				// 0 没删除 1删除

				theForm.getBean().setDelFlg("0");
			}
			if (theForm.getEndstate().trim().equals("停止使用")) {

				theForm.getStatusBean().setEndState("1");

				theForm.getBean().setDeptState("1");

				theForm.getBean().setDelFlg("1");
			}
			theForm.getStatusBean().setRemark(
					"原部门为" + "[" + theForm.getBeginPDN() + "]" + "合并到" + "["
							+ theForm.getDepartmentnameNew() + "]" + "状态为"
							+ "[" + theForm.getEndstate() + "]");

			getService().saveOrUpdate(theForm.getStatusBean());

			List list = getService().getList(TblCmnDept.class);

			theForm.setList(list);

			MessageUtils.addMessage(
					request,
					MessageInfo.factory().getMessage("HR_I006_C_2",
							theForm.getBean().getDeptName(),
							theForm.getDepartmentnameNew(),
							theForm.getDepartmentnameNew()));
		}
		theForm.setStep("deptEdit");

		return mapping.findForward(theForm.getStep());
	}

	public ActionForward info(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		TblSmDeptForm theForm = castForm(form);

		String parent = request.getParameter("oid");

		// 20071229 CHRZZJG00006 START
		if (StringUtils.isBlank(parent)) {
			parent = theForm.getParentCode();
		}
		String deptName = theForm.getBean().getDeptName();
		if (StringUtils.isBlank(deptName)) {
			deptName = theForm.getBean().getParent().getDeptName();
		}
		// 20071229 CHRZZJG00006 END
		TblCmnDept parentId = (TblCmnDept) getService().load(TblCmnDept.class,
				parent);

		boolean notChange = false;
		int childrenCnt = parentId.getChildren().size();
		for (int i = 0; i < childrenCnt; i++) {
			TblCmnDept childrenDept = (TblCmnDept) parentId.getChildren()
					.get(i);
			if ("0".equals(childrenDept.getDeptState())) {
				notChange = true;
				break;
			}
		}
		// if (parentId.isHasChildren()) {
		if (notChange) {
			MessageUtils.addErrorMessage(request, MessageInfo.factory()
					.getMessage("HR_I005_C_1", deptName));

			return mapping.findForward("error");
		}
		// 20071229 CHRZZJG00006 START
		if (theForm.getBean().getDeptState() == null) {
			if (theForm.getBean().getParent().getDeptState().equals("1")) {

				MessageUtils.addErrorMessage(request, MessageInfo.factory()
						.getMessage("HR_I034_C_1", deptName));

				return mapping.findForward("error");
			}
		} else {
			if (theForm.getBean().getDeptState().equals("1")) {

				MessageUtils.addErrorMessage(request, MessageInfo.factory()
						.getMessage("HR_I034_C_1", deptName));

				return mapping.findForward("error");
			}
		}
		return mapping.findForward(theForm.getStep());
		// 20071229 CHRZZJG00006 START

	}

	private TblSmDeptForm castForm(ActionForm form) {
		return (TblSmDeptForm) form;
	}

	public static BasicCodeService getBasicCodeService() {
		return (BasicCodeService) ServiceProvider
				.getService(BasicCodeService.SERVICE_NAME);
	}

	private CommonService getService() {
		return (CommonService) ServiceProvider
				.getService(CommonService.SERVICE_NAME);
	}
}