package net.silencily.sailing.basic.wf.web;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.basic.sm.domain.TblCmnRole;
import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.basic.wf.domain.TblWfFormInfo;
import net.silencily.sailing.basic.wf.domain.TblWfOperationInfo;
import net.silencily.sailing.basic.wf.domain.TblWfParticularInfo;
import net.silencily.sailing.basic.wf.service.OperationService;
import net.silencily.sailing.basic.wf.service.WFService;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.common.crud.service.ViewBean;
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

/**
 * @author yangxl time 2007-11-27
 */
public class OperationAction extends DispatchActionPlus {

	WFService wFService = null;

	public static final String FORWARD_ENTRY = "entry";

	public static final String FORWARD_LIST = "list";// 总业务信息列表；

	public static final String FORWARD_INFO = "info";// 业务名称列表；

	public static final String FORWARD_PARTINFO = "partInfo";// 步骤详细信息（弹出窗口）；

	public static final String WF_INFO = "wf_info";

	private String upId = "";// 步骤信息表id

	String operNameSelect;

	String nameSelect;

	String opeditionSelect;

	/**
	 * @author gejianbao time 2009-02-20 功能描述 调用接口方法
	 * 
	 */
	public static OperationService service() {
		return (OperationService) ServiceProvider
				.getService(OperationService.SERVICE_NAME);
	}

	/**
	 * @author gejianbao time 2009-02-26 功能描述 调用公用接口
	 * 
	 */
	private CommonService getService() {
		return (CommonService) ServiceProvider
				.getService(CommonService.SERVICE_NAME);
	}

	/**
	 * @author gejianbao time 2009-02-26 功能描述 登录
	 * 
	 */
	public ActionForward entry(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward(FORWARD_ENTRY);
	}

	/**
	 * @author yangxl time 2007-11-27 功能描述 列表
	 * 
	 */
	public ActionForward list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserCodeName ucn = ContextInfo.getContextUser();
		ViewBean operviewBean = getService().fetchAll(TblWfOperationInfo.class,
				ucn.getUsername(), WF_INFO);
		/*
		 * OperationForm theForm = (OperationForm) form; wFService = (WFService)
		 * ServiceProvider .getService(WFService.SERVICE_NAME); List operlist =
		 * wFService.findAllWfOper();
		 */
		/**
		 * 获取jsp中table标签中的operviewBean 并通过dealList(List
		 * listTemp,HttpServletRequest request)方法进行分页；
		 * 
		 */
		/*
		 * ViewBean operviewBean = getService().listSetToVB(WF_INFO,
		 * theForm.getPaginater().dealList(operlist, request));
		 */
		request.setAttribute("operviewBean", operviewBean);
		return mapping.findForward(FORWARD_LIST);

	}

	/**
	 * @author yangxl time 2007-11-27 功能描述 步骤详细信息
	 * 
	 */
	public ActionForward particularInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		TblWfParticularInfo tblWfParticularInfo = new TblWfParticularInfo();
		TblWfOperationInfo tblWfOperationInfo = new TblWfOperationInfo();
		Set qxli = new HashSet();
		// List qxli=new ArrayList();
		Set set1 = new HashSet();
		String fid = "";
		String fname = "";
		String fcode = "";
		String feditor = "";
		OperationForm theForm = (OperationForm) form;
		upId = request.getParameter("oid2");
		String ooid = request.getParameter("ooid");

		request.setAttribute("ooid", ooid);
		wFService = (WFService) ServiceProvider
				.getService(WFService.SERVICE_NAME);
		// 编辑状态判断；
		if (ooid != null && !("").equals(ooid)) {
			tblWfOperationInfo = wFService.findWfOperInfoById(ooid);
			if (upId != null && !("").equals(upId)) {
				theForm.setOid(upId);
				// theForm.setWfoperinfo(tblWfOperationInfo);
				tblWfParticularInfo = wFService.findWfParticularInfoById(upId);
				// theForm.setWfpinfo(tblWfParticularInfo);
				set1 = tblWfParticularInfo.getTblWfFormInfos();

				if (set1.size() != '0') {
					for (int j = 0; j < set1.size(); j++) {

						TblWfFormInfo tblWfFormInfo = (TblWfFormInfo) set1
								.toArray()[j];
						if (tblWfFormInfo.getId() != null
								&& !("").equals(tblWfFormInfo.getId())) {
							fid = tblWfFormInfo.getId();
						}
						if (tblWfFormInfo.getFieldName() != null
								&& !("").equals(tblWfFormInfo.getFieldName())) {
							fname = tblWfFormInfo.getFieldName();
						}

						if (tblWfFormInfo.getFieldCode() != null
								&& !("").equals(tblWfFormInfo.getFieldCode())) {
							fcode = tblWfFormInfo.getFieldCode();
						}
						if (tblWfFormInfo.getEditorFlg() != null
								&& !("").equals(tblWfFormInfo.getEditorFlg())) {
							feditor = tblWfFormInfo.getEditorFlg();
						}
						qxli.add(new Column(fid, fname, fcode, feditor));
					}
				}

				// 生成提交退回列表
				String stepNmu = tblWfOperationInfo.getStepNum();
				int sN = 0;
				if (stepNmu != null && !("").equals(stepNmu)) {
					sN = new Integer(stepNmu).intValue();
				} else {
					sN = 50;
				}
				List ln = new ArrayList();
				for (int i = 1; i < sN; i++) {
					String num = new Integer(i).toString();
					ln.add(num);
				}
				ln.add("99");
				// 提交
				String stepOn = tblWfParticularInfo.getCommitStep();
				String[] sO = null;
				List lo = new ArrayList();// 选中提交步骤
				List ls = new ArrayList();// 所有提交步骤
				if (stepOn != null && !("").equals(stepOn)) {
					sO = stepOn.split(",");
					for (int j = 0; j < sO.length; j++) {
						lo.add(sO[j].toString());
					}
					for (int m = 0; m < ln.size(); m++) {

						int vN = m + 1;
						String vnum = new Integer(vN).toString();
						String stName = (wFService.findTblWfParticularInfoName(
								ooid, vnum));
						if (lo.contains(ln.toArray()[m].toString())) {
							String vsel = "SELECTED";// 选中状态
							if (vN == ln.size()) {
								ls.add(new StepsVo(("END" + "结束"), vsel, vnum));
							} else {
								ls
										.add(new StepsVo((vnum + stName), vsel,
												vnum));
							}
						} else {
							String vsel = "";
							if (vN == ln.size()) {
								ls.add(new StepsVo(("END" + "结束"), vsel, vnum));
							} else {
								ls
										.add(new StepsVo((vnum + stName), vsel,
												vnum));
							}
						}
					}
				} else {
					for (int i = 1; i <= sN; i++) {
						String num = new Integer(i).toString();
						String stName = (wFService.findTblWfParticularInfoName(
								ooid, num));
						String vsel = "";
						if (i == sN) {
							ls.add(new StepsVo("END" + "结束", vsel, num));
						} else {
							ls.add(new StepsVo(num + stName, vsel, num));
						}
					}
				}
				// 退回
				String stepOff = tblWfParticularInfo.getGobackStep();
				List lt = new ArrayList();// 所有退回步骤
				for (int n = 0; n < ln.size(); n++) {
					int vN = n + 1;
					String vnum = new Integer(vN).toString();
					String stName = (wFService.findTblWfParticularInfoName(
							ooid, vnum));
					if (ln.toArray()[n].toString().equals(stepOff)) {
						String vsel = "SELECTED";// 选中状态
						if (vN == ln.size()) {
							lt.add(new StepsVo("END" + "结束", vsel, vnum));
						} else {
							lt.add(new StepsVo(vnum + stName, vsel, vnum));
						}
					} else {
						String vsel = "";
						if (vN == ln.size()) {
							lt.add(new StepsVo("END" + "结束", vsel, vnum));
						} else {
							lt.add(new StepsVo(vnum + stName, vsel, vnum));
						}
					}
				}
				theForm.setGobacklist(lt);
				theForm.setCommitlist(ls);

				// 角色
				TblCmnRole tcr = new TblCmnRole();
				String roles = tblWfParticularInfo.getTblUserId();
				String[] ro = null;
				List lro = new ArrayList();
				if (roles != null && !("").equals(roles)) {
					ro = roles.split(",");
					for (int g = 0; g < ro.length; g++) {
						String roleCd = ro[g].toString();
						tcr = wFService.findroleCdRole(roleCd);
						if (tcr != null && !tcr.equals("")) {
							lro.add(tcr);
						}
					}
				}
				if (lro.size() > 0) {
					theForm.setRoleli(lro);
				}
				// 权限
				if (qxli == null || qxli.size() <= 0) {
					List colums = new ArrayList();
					colums.add(new Column("", "", "", ""));
					request.setAttribute("list", colums);
				} else {
					request.setAttribute("list", qxli);
				}
				MessageUtils.addMessage(request, MessageInfo.factory()
						.getMessage("WF_I010_p_0"));
				// request.setAttribute(mapping.getAttribute(), theForm);

			} else {
				// 生成提交退回列表
				// tblWfOperationInfo = wFService.findWfOperInfoById(ooid);
				// theForm.setWfoperinfo(tblWfOperationInfo);

				String stepNmu = tblWfOperationInfo.getStepNum();
				int sN = 0;
				if (stepNmu != null && !("").equals(stepNmu)) {
					sN = new Integer(stepNmu).intValue();
				} else {
					sN = 50;
				}
				List l1 = new ArrayList();
				for (int i = 1; i <= sN; i++) {
					String num = new Integer(i).toString();
					String stName = wFService.findTblWfParticularInfoName(ooid,
							num);
					String vsel = "";
					if (i == sN) {
						l1.add(new StepsVo("END" + "结束", vsel, num));
					} else {
						l1.add(new StepsVo(num + stName, vsel, num));
					}

				}
				theForm.setGobacklist(l1);
				theForm.setCommitlist(l1);

				List colums = new ArrayList();
				colums.add(new Column("", "", "", ""));
				request.setAttribute("list", colums);

				// yangxl 2008-5-27 新加 用来控制步骤id自动加一 start
				Set sets = tblWfOperationInfo.getTblWfParticularInfos();
				int maxStepId = 0;
				List listStepId = new ArrayList();
				if (sets.size() > 0) {
					for (Iterator it = sets.iterator(); it.hasNext();) {
						TblWfParticularInfo tblWfPar = (TblWfParticularInfo) it
								.next();
						if (tblWfPar != null) {
							if (tblWfPar.getStepId() != null) {
								listStepId.add(tblWfPar.getStepId());
							}
						}
					}
					for (int i = 0; i < listStepId.size(); i++) {
						for (int j = 0; j < listStepId.size(); j++) {
							int a = new Long(listStepId.get(i).toString())
									.intValue();
							int b = new Long(listStepId.get(j).toString())
									.intValue();
							if (b >= a) {
								if (b != 99) {
									maxStepId = b + 1;
								}
							}
						}
					}
					tblWfParticularInfo.setStepId(new Long(maxStepId));
					// theForm.setWfpinfo(tblWfParticularInfo);
				} else {
					tblWfParticularInfo.setStepId(new Long("1"));
					// theForm.setWfpinfo(tblWfParticularInfo);
				}
				// end
				MessageUtils.addMessage(request, MessageInfo.factory()
						.getMessage("WF_I009_p_0"));
				// request.setAttribute(mapping.getAttribute(), theForm);
			}
		}
		theForm.setWfoperinfo(tblWfOperationInfo);
		theForm.setWfpinfo(tblWfParticularInfo);

		String nameoper = tblWfOperationInfo.getName();// 流程名称
		request.setAttribute("nameoper", nameoper);
		return mapping.findForward(FORWARD_PARTINFO);
	}

	/**
	 * @author yangxl time 2007-11-28 功能描述 编辑页面（业务名称列表）
	 * 
	 */
	public ActionForward info(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		OperationForm theForm = (OperationForm) form;
		String oid = request.getParameter("oid");
		
		wFService = (WFService) ServiceProvider
		.getService(WFService.SERVICE_NAME);

		// 获取并过滤不可编辑列表（步骤列表）数据；
		List partList = new ArrayList();
		Iterator details = theForm.getWfoperinfo().getTblWfParticularInfos()
				.iterator();
		while (details.hasNext()) {
			TblWfParticularInfo detail = (TblWfParticularInfo) details.next();
			if ("0".equals(detail.getDelFlg())) {
				partList.add(detail);
			}
		}
		// theForm.getPaginater().dealList(partList, request);
		theForm.setParticulars(theForm.getPaginater().dealList(partList,
				request));
		// 管理员
		TblCmnUser the = new TblCmnUser();
		String flowManager = theForm.getWfoperinfo().getFlowManager();
		String[] fm = null;
		List lfm = new ArrayList();
		if (flowManager != null && !("").equals(flowManager)) {
			fm = flowManager.split(",");
			for (int g = 0; g < fm.length; g++) {
				String empCd = fm[g].toString();
				
				the = wFService.findempCdUser(empCd);
				lfm.add(the);
			}
		}
		request.setAttribute("manager", lfm);

		if ((oid == null || "".equals(oid))
				&& (theForm.getOid() == null || "".equals(theForm.getOid()))) {
			TblWfOperationInfo wfinfo = new TblWfOperationInfo();
			MessageUtils.addMessage(request, MessageInfo.factory().getMessage(
					"WF_I009_p_0"));
			theForm.setWfoperinfo(wfinfo);
			request.setAttribute(mapping.getAttribute(), theForm);
			return mapping.findForward(FORWARD_INFO);
		}

		MessageUtils.addMessage(request, MessageInfo.factory().getMessage(
				"WF_I010_p_0"));
		request.setAttribute(mapping.getAttribute(), theForm);
		return mapping.findForward(FORWARD_INFO);
	}

	/**
	 * @author yangxl time 2007-11-28 功能描述 保存新建工作流
	 * 
	 */

	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		OperationForm theForm = (OperationForm) form;
		String flowManager = request.getParameter("flowManager");
		theForm.getWfoperinfo().setFlowManager(flowManager);

		TblWfOperationInfo wfoperinfo = theForm.getWfoperinfo();
		wFService = (WFService) ServiceProvider
				.getService(WFService.SERVICE_NAME);
		wFService.saveWfOperInfo(wfoperinfo);
		theForm.setOid(wfoperinfo.getId());

		// 管理员
		TblCmnUser the = new TblCmnUser();
		flowManager = theForm.getWfoperinfo().getFlowManager();
		String[] fm = null;
		List lfm = new ArrayList();
		if (flowManager != null && !("").equals(flowManager)) {
			fm = flowManager.split(",");
			for (int g = 0; g < fm.length; g++) {
				String empCd = fm[g].toString();
				the = wFService.findempCdUser(empCd);
				lfm.add(the);
			}
		}
		request.setAttribute("manager", lfm);

		MessageUtils.addMessage(request, MessageInfo.factory().getMessage(
				"WF_I014_p_0"));

		return mapping.findForward(FORWARD_INFO);
	}

	/**
	 * @author yangxl time 2007-11-28 功能描述 逻辑删除工作流
	 * 
	 */
	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String deleteId = request.getParameter("oid");
		wFService = (WFService) ServiceProvider
				.getService(WFService.SERVICE_NAME);
		try {
			wFService.deleteWfOperInfo(deleteId);
		} catch (Exception e) {
			MessageUtils.addErrorMessage(request, MessageInfo.factory()
					.getMessage("WF_I012_p_0"));
			return list(mapping, form, request, response);
		}
		MessageUtils.addMessage(request, MessageInfo.factory().getMessage(
				"WF_I008_p_0"));
		return list(mapping, form, request, response);
	}

	/**
	 * @author yangxl time 2008-01-27 功能描述 保存修改页面（业务名称列表）
	 * 
	 */

	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		OperationForm theForm = (OperationForm) form;
		String flowManager = request.getParameter("flowManager");
		theForm.getWfoperinfo().setFlowManager(flowManager);
		wFService = (WFService) ServiceProvider
				.getService(WFService.SERVICE_NAME);
		wFService.updateWfOperInfo(theForm.getWfoperinfo());
		request.setAttribute("ooid", theForm.getWfoperinfo().getId());
		MessageUtils.addMessage(request, MessageInfo.factory().getMessage(
				"WF_I013_p_0"));
		// 管理员
		TblCmnUser the = new TblCmnUser();
		flowManager = theForm.getWfoperinfo().getFlowManager();
		String[] fm = null;
		List lfm = new ArrayList();
		if (flowManager != null && !("").equals(flowManager)) {
			fm = flowManager.split(",");
			for (int g = 0; g < fm.length; g++) {
				String empCd = fm[g].toString();
				the = wFService.findempCdUser(empCd);
				lfm.add(the);
			}
		}
		request.setAttribute("manager", lfm);
		// 获取并过滤不可编辑列表（步骤列表）数据；
		List partList = new ArrayList();
		Iterator details = theForm.getWfoperinfo().getTblWfParticularInfos()
				.iterator();
		while (details.hasNext()) {
			TblWfParticularInfo detail = (TblWfParticularInfo) details.next();
			if ("0".equals(detail.getDelFlg())) {
				partList.add(detail);
			}
		}
		theForm.setParticulars(theForm.getPaginater().dealList(partList,
				request));
		return mapping.findForward(FORWARD_INFO);
	}

	/**
	 * @author yangxl time 2007-11-30 功能描述 删除步骤
	 * 
	 */
	public ActionForward deleteParticular(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		OperationForm theForm = (OperationForm) form;
		String deleteId = request.getParameter("oid1");
		wFService = (WFService) ServiceProvider
				.getService(WFService.SERVICE_NAME);
		wFService.deleteWfParInfo(deleteId);

		// 获取并过滤不可编辑列表（步骤列表）数据；
		List partList = new ArrayList();
		Iterator details = theForm.getWfoperinfo().getTblWfParticularInfos()
				.iterator();
		while (details.hasNext()) {
			TblWfParticularInfo detail = (TblWfParticularInfo) details.next();
			if ("0".equals(detail.getDelFlg())) {
				partList.add(detail);
			}
		}
		// theForm.getPaginater().dealList(partList, request);
		theForm.setParticulars(theForm.getPaginater().dealList(partList,
				request));
		MessageUtils.addMessage(request, MessageInfo.factory().getMessage(
				"WF_I008_p_0"));
		return mapping.findForward(FORWARD_INFO);
	}

	/**
	 * @author yangxl time 2007-11-30 功能描述 生成XML
	 * 
	 */
	public ActionForward createXML(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		boolean boolStepId = false;
		String dir = this.getServlet().getServletContext().getRealPath("")
				+ "\\" + "WEB-INF" + "\\" + "classes" + "\\" + "META-INF";
		OperationForm theForm = (OperationForm) form;
		wFService = (WFService) ServiceProvider
				.getService(WFService.SERVICE_NAME);
		TblWfOperationInfo tblWfOperationInfo = wFService
				.findWfOperInfoById(theForm.getOid());
		Set set = tblWfOperationInfo.getTblWfParticularInfos();
		for (Iterator it = set.iterator(); it.hasNext();) {
			TblWfParticularInfo tblWfParticularInfo = (TblWfParticularInfo) it
					.next();
			if (tblWfParticularInfo != null) {
				String stepId = tblWfParticularInfo.getStepId().toString();
				if (!stepId.equals("99")) {
					boolStepId = false;
				} else {
					boolStepId = true;
				}
			}
		}
		if (!boolStepId) {
			MessageUtils.addErrorMessage(request, MessageInfo.factory()
					.getMessage("WF_I003_p_0"));
			return mapping.findForward(FORWARD_INFO);
		} else {

			CreateXml createXml = new CreateXml();
			createXml.saveWfInfo(tblWfOperationInfo, dir);
			MessageUtils.addMessage(request, MessageInfo.factory().getMessage(
					"WF_I004_p_0"));
		}
		// 判断流程中是否有步骤结束语句 end
		return info(mapping, form, request, response);
	}

	/**
	 * @author yangxl time 2008-5-29 功能描述 批量并生成XML
	 * 
	 */
	public ActionForward createAllXML(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		boolean boolStepId = false;

		String dir = this.getServlet().getServletContext().getRealPath("")
				+ "\\" + "WEB-INF" + "\\" + "classes" + "\\" + "META-INF";

		TblWfOperationInfo tblWfOperationInfo = new TblWfOperationInfo();
		wFService = (WFService) ServiceProvider
				.getService(WFService.SERVICE_NAME);
		List list = wFService.findWfOperInfoAll();
		for (Iterator itOper = list.iterator(); itOper.hasNext();) {
			tblWfOperationInfo = (TblWfOperationInfo) itOper.next();
			// 2008-5-29yangxl 判断流程中是否有步骤结束语句 start
			Set set = tblWfOperationInfo.getTblWfParticularInfos();
			for (Iterator it = set.iterator(); it.hasNext();) {
				TblWfParticularInfo tblWfParticularInfo = (TblWfParticularInfo) it
						.next();
				if (tblWfParticularInfo != null) {
					String stepId = tblWfParticularInfo.getStepId().toString();
					if (!stepId.equals("99")) {
						boolStepId = false;
					} else {
						boolStepId = true;
					}
				}
			}
			if (!boolStepId) {
				MessageUtils.addErrorMessage(request, MessageInfo.factory()
						.getMessage("WF_I003_p_0"));
				return list(mapping, form, request, response);
			} else {
				CreateXml createXml = new CreateXml();
				createXml.saveWfInfo(tblWfOperationInfo, dir);
			}
		}
		MessageUtils.addMessage(request, MessageInfo.factory().getMessage(
				"WF_I005_p_0"));
		return list(mapping, form, request, response);
	}

	/**
	 * @author yangxl time 2007-11-30 功能描述 保存步骤信息
	 * 
	 */
	public ActionForward saveParticular(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		OperationForm theForm = (OperationForm) form;
		String ooid = request.getParameter("ooid");// 流程信息id
		// 2008-5-29 yangxl stepId唯一性验证 Start
		boolean boolStepId = false;
		boolStepId = stepIdCheck(theForm, request, ooid);
		if (!boolStepId) {
			return mapping.findForward(FORWARD_PARTINFO);
		}
		// stepId唯一性验证end
		// 2008-6-30 yangxl stepNamw唯一性验证 Start
		boolean boolStepName = false;
		boolStepName = stepNameCheck(theForm, request, ooid);
		if (!boolStepName) {
			return mapping.findForward(FORWARD_PARTINFO);
		}
		// stepName唯一性验证end
		String fid = null;// id
		String fieldName = null;// 表单项
		String fieldCode = null;// 表单KEY
		String editorFlg = null;// 是否可入例
		// 2008-6-2 yangxl通过ooid获得步骤总数Strat
		TblWfOperationInfo tblWfOperationInfo = new TblWfOperationInfo();
		tblWfOperationInfo = wFService.findWfOperInfoById(ooid);
		String stepIdSum = null;// 步骤总数
		if (tblWfOperationInfo != null) {
			stepIdSum = tblWfOperationInfo.getStepNum();
		}
		// end
		TblWfParticularInfo tblWfParticularInfosave = theForm.getWfpinfo();

		// 2008-6-2yangxl 验证提交步骤是否是最后一步
		String commitStepChecks = request.getParameter("commitStep");
		StringBuffer commitStepChecksnew = new StringBuffer();
		if (StringUtils.isNotBlank(commitStepChecks)) {
			String[] commitStepsz = commitStepChecks.split(",");
			for (int t = 0; t < commitStepsz.length; t++) {
				if (commitStepsz[t].toString().equals(stepIdSum)) {
					commitStepsz[t] = "99";
				}
				commitStepChecksnew.append(commitStepsz[t]);
				commitStepChecksnew.append(",");
			}
		}
		// end
		tblWfParticularInfosave.setCommitStep(commitStepChecksnew.toString());

		wFService = (WFService) ServiceProvider
				.getService(WFService.SERVICE_NAME);
		// 角色
		StringBuffer tblUserId = new StringBuffer();
		// String userId = theForm.getWfpinfo().getTblUserId();
		String userId = request.getParameter("tblUserId");
		if (userId != null && !("").equals(userId)) {
			String[] userIdsz = userId.split(",");
			if (userIdsz.length > 0) {
				TblCmnRole tcr = new TblCmnRole();
				for (int u = 0; u < userIdsz.length; u++) {
					tcr = wFService.findidRole(userIdsz[u]);
					if (tcr != null) {
						if (tcr.getRoleCd() != null
								&& !("").equals(tcr.getRoleCd())) {
							tblUserId.append(tcr.getRoleCd());
							tblUserId.append(",");
						}
					}
				}
			}
		}
		tblWfParticularInfosave.setTblUserId(tblUserId.toString());
		// fieldStatus；
		// try {
		String fieldStatus = request.getParameter("fieldStatus");
		tblWfParticularInfosave.setFieldStatus(fieldStatus);
		// 取得权限
		if (fieldStatus.equals("3")) {
			Set formSet = tblWfParticularInfosave.getTblWfFormInfos();
			List listForm = new ArrayList();
			String[] popedom = request.getParameterValues("row");
			for (Iterator it = formSet.iterator(); it.hasNext();) {

				boolean delflg = true;
				TblWfFormInfo tbl = (TblWfFormInfo) it.next();
				// 判断是否删除
				int i = 0;
				if (popedom != null && !("").equals(popedom)) {
					for (i = 0; i < popedom.length; i += 4) {
						if (popedom[i].equalsIgnoreCase(tbl.getId())) {
							delflg = false;
							break;
						}
					}
					if (!delflg)// 更新
					{
						fid = popedom[i];
						fieldName = popedom[i + 1];
						fieldCode = popedom[i + 2];
						editorFlg = popedom[i + 3];
						tbl.setFieldName(fieldName);
						tbl.setFieldCode(fieldCode);
						tbl.setTblWfOpeInfId(ooid);
						tbl.setEditorFlg(editorFlg);
						tbl.setDelFlg("0");
					}
				}
				// 删除
				if (delflg) {
					listForm.add(tbl);
				}
			}
			for (Iterator it = listForm.iterator(); it.hasNext();) {
				TblWfFormInfo tbl = (TblWfFormInfo) it.next();
				formSet.remove(tbl);
				tbl.setTblWfParticularInfo(null);
				tbl.setDelFlg("1");
				// getService().delete(tbl);
			}
			// 新增
			if (popedom != null && !("").equals(popedom)) {
				for (int i = 0; i < popedom.length; i += 4) {
					fid = popedom[i];
					if (StringUtils.isBlank(fid)) {
						fieldName = popedom[i + 1];
						fieldCode = popedom[i + 2];
						editorFlg = popedom[i + 3];
						TblWfFormInfo tblWfFormInfo = new TblWfFormInfo();
						tblWfFormInfo.setFieldName(fieldName);
						tblWfFormInfo.setFieldCode(fieldCode);
						tblWfFormInfo.setEditorFlg(editorFlg);
						tblWfFormInfo.setTblWfOpeInfId(ooid);
						tblWfFormInfo.setDelFlg("0");
						tblWfFormInfo
								.setTblWfParticularInfo(tblWfParticularInfosave);
						tblWfParticularInfosave.getTblWfFormInfos().add(
								tblWfFormInfo);
					}
				}
			}
		}
		tblWfParticularInfosave.setTblWfOperationInfo(tblWfOperationInfo);
		tblWfOperationInfo.getTblWfParticularInfos().add(
				tblWfParticularInfosave);
		wFService.updateWfParInfo(tblWfParticularInfosave);
		// 当新增保存成功时自动将工作流的stepNum+1；
		/*
		 * if (("").equals(upId) || upId == null) { int tempstepNumInt =
		 * Integer.parseInt(stepIdSum); tempstepNumInt++; String tempstepNumStr
		 * = Integer.toString(tempstepNumInt);
		 * tblWfOperationInfo.setStepNum(tempstepNumStr);
		 * wFService.updateWfOperInfo(tblWfOperationInfo); }
		 */

		// 保存后转发到particularInfo.jsp页面中的显示信息；start
		// fieldStates;
		Set fSet = tblWfParticularInfosave.getTblWfFormInfos();
		Set qxli = new HashSet();
		if (fSet.size() != '0') {
			for (int j = 0; j < fSet.size(); j++) {

				TblWfFormInfo tblWfFormInfo = (TblWfFormInfo) fSet.toArray()[j];
				if (tblWfFormInfo.getId() != null
						&& !("").equals(tblWfFormInfo.getId())) {
					fid = tblWfFormInfo.getId();
				}
				if (tblWfFormInfo.getFieldName() != null
						&& !("").equals(tblWfFormInfo.getFieldName())) {
					fieldName = tblWfFormInfo.getFieldName();
				}

				if (tblWfFormInfo.getFieldCode() != null
						&& !("").equals(tblWfFormInfo.getFieldCode())) {
					fieldCode = tblWfFormInfo.getFieldCode();
				}
				if (tblWfFormInfo.getEditorFlg() != null
						&& !("").equals(tblWfFormInfo.getEditorFlg())) {
					editorFlg = tblWfFormInfo.getEditorFlg();
				}
				qxli.add(new Column(fid, fieldName, fieldCode, editorFlg));
			}
		}
		// 生成提交退回列表
		String stepNmu = tblWfOperationInfo.getStepNum();
		int sN = 0;
		if (stepNmu != null && !("").equals(stepNmu)) {
			sN = new Integer(stepNmu).intValue();
		} else {
			sN = 50;
		}
		List ln = new ArrayList();
		for (int i = 1; i < sN; i++) {
			String num = new Integer(i).toString();
			ln.add(num);
		}
		ln.add("99");
		// 提交
		String stepOn = tblWfParticularInfosave.getCommitStep();
		String[] sO = null;
		List lo = new ArrayList();// 选中提交步骤
		List ls = new ArrayList();// 所有提交步骤
		if (stepOn != null && !("").equals(stepOn)) {
			sO = stepOn.split(",");
			for (int j = 0; j < sO.length; j++) {
				lo.add(sO[j].toString());
			}
			for (int m = 0; m < ln.size(); m++) {

				int vN = m + 1;
				String vnum = new Integer(vN).toString();
				String stName = (wFService.findTblWfParticularInfoName(ooid,
						vnum));
				if (lo.contains(ln.toArray()[m].toString())) {
					String vsel = "SELECTED";// 选中状态
					if (vN == ln.size()) {
						ls.add(new StepsVo(("END" + "结束"), vsel, vnum));
					} else {
						ls.add(new StepsVo((vnum + stName), vsel, vnum));
					}
				} else {
					String vsel = "";
					if (vN == ln.size()) {
						ls.add(new StepsVo(("END" + "结束"), vsel, vnum));
					} else {
						ls.add(new StepsVo((vnum + stName), vsel, vnum));
					}
				}
			}
		} else {
			for (int i = 1; i <= sN; i++) {
				String num = new Integer(i).toString();
				String stName = (wFService.findTblWfParticularInfoName(ooid,
						num));
				String vsel = "";
				if (i == sN) {
					ls.add(new StepsVo("END" + "结束", vsel, num));
				} else {
					ls.add(new StepsVo(num + stName, vsel, num));
				}
			}
		}
		// 退回
		String stepOff = tblWfParticularInfosave.getGobackStep();
		List lt = new ArrayList();// 所有退回步骤
		for (int n = 0; n < ln.size(); n++) {
			int vN = n + 1;
			String vnum = new Integer(vN).toString();
			String stName = (wFService.findTblWfParticularInfoName(ooid, vnum));
			if (ln.toArray()[n].toString().equals(stepOff)) {
				String vsel = "SELECTED";// 选中状态
				if (vN == ln.size()) {
					lt.add(new StepsVo("END" + "结束", vsel, vnum));
				} else {
					lt.add(new StepsVo(vnum + stName, vsel, vnum));
				}
			} else {
				String vsel = "";
				if (vN == ln.size()) {
					lt.add(new StepsVo("END" + "结束", vsel, vnum));
				} else {
					lt.add(new StepsVo(vnum + stName, vsel, vnum));
				}
			}
		}
		theForm.setGobacklist(lt);
		theForm.setCommitlist(ls);
		// 角色
		TblCmnRole tcr = new TblCmnRole();
		String roles = tblWfParticularInfosave.getTblUserId();
		String[] ro = null;
		List lro = new ArrayList();
		if (roles != null && !("").equals(roles)) {
			ro = roles.split(",");
			for (int g = 0; g < ro.length; g++) {
				String roleCd = ro[g].toString();
				tcr = wFService.findroleCdRole(roleCd);
				if (tcr != null && !tcr.equals("")) {
					lro.add(tcr);
				}
			}
		}
		if (lro.size() > 0) {
			theForm.setRoleli(lro);
		}
		// 权限
		if (qxli == null || qxli.size() <= 0) {
			List colums = new ArrayList();
			colums.add(new Column("", "", "", ""));
			request.setAttribute("list", colums);
		} else {
			request.setAttribute("list", qxli);
		}
		// 保存后转发到particularInfo.jsp页面中的显示信息; end

		request.setAttribute("ooid", ooid);
		MessageUtils.addMessage(request, MessageInfo.factory().getMessage(
				"WF_I027_p_0"));
		return mapping.findForward(FORWARD_PARTINFO);
		// return particularInfo(mapping, form, request, response);
	}

	/**
	 * @author yangxl time 2008-05-28 功能描述 检查stepId是否唯一
	 * 
	 */
	public boolean stepIdCheck(OperationForm theForm,
			HttpServletRequest request, String ooid) {

		String fid = null;// id
		String fieldName = null;// 表单项
		String fieldCode = null;// 表单KEY
		String editorFlg = null;// 是否可入例

		boolean bool = false;
		String stepId = theForm.getWfpinfo().getStepId().toString();
		TblWfOperationInfo tblWfOperChecks = new TblWfOperationInfo();
		TblWfParticularInfo tblWfParChecks = new TblWfParticularInfo();
		tblWfOperChecks = wFService.findWfOperInfoById(ooid);
		if (tblWfOperChecks != null) {

			Set set = tblWfOperChecks.getTblWfParticularInfos();
			// String numStep = tblWfOperChecks.getStepNum();// 流程定义的步骤
			if (set.size() > 0) {
				for (Iterator it = set.iterator(); it.hasNext();) {
					tblWfParChecks = (TblWfParticularInfo) it.next();
					if (tblWfParChecks != null
							&& !upId.equals(tblWfParChecks.getId())) {
						String stepIdInfo = tblWfParChecks.getStepId()
								.toString();
						if (stepId.equals(stepIdInfo)) {
							MessageUtils.addErrorMessage(request, MessageInfo
									.factory().getMessage("WF_I002_p_0"));

							// 转发到particularInfo.jsp页面中的显示信息；start
							// fieldStates;
							Set fSet = theForm.getWfpinfo().getTblWfFormInfos();
							Set qxli = new HashSet();
							if (fSet.size() != '0') {
								for (int j = 0; j < fSet.size(); j++) {

									TblWfFormInfo tblWfFormInfo = (TblWfFormInfo) fSet
											.toArray()[j];
									if (tblWfFormInfo.getId() != null
											&& !("").equals(tblWfFormInfo
													.getId())) {
										fid = tblWfFormInfo.getId();
									}
									if (tblWfFormInfo.getFieldName() != null
											&& !("").equals(tblWfFormInfo
													.getFieldName())) {
										fieldName = tblWfFormInfo
												.getFieldName();
									}

									if (tblWfFormInfo.getFieldCode() != null
											&& !("").equals(tblWfFormInfo
													.getFieldCode())) {
										fieldCode = tblWfFormInfo
												.getFieldCode();
									}
									if (tblWfFormInfo.getEditorFlg() != null
											&& !("").equals(tblWfFormInfo
													.getEditorFlg())) {
										editorFlg = tblWfFormInfo
												.getEditorFlg();
									}
									qxli.add(new Column(fid, fieldName,
											fieldCode, editorFlg));
								}
							}
							// 生成提交退回列表
							String stepNmu = tblWfOperChecks.getStepNum();
							int sN = 0;
							if (stepNmu != null && !("").equals(stepNmu)) {
								sN = new Integer(stepNmu).intValue();
							} else {
								sN = 50;
							}
							List ln = new ArrayList();
							for (int i = 1; i < sN; i++) {
								String num = new Integer(i).toString();
								ln.add(num);
							}
							ln.add("99");
							// 提交
							String stepOn = theForm.getWfpinfo()
									.getCommitStep();
							String[] sO = null;
							List lo = new ArrayList();// 选中提交步骤
							List ls = new ArrayList();// 所有提交步骤
							if (stepOn != null && !("").equals(stepOn)) {
								sO = stepOn.split(",");
								for (int j = 0; j < sO.length; j++) {
									lo.add(sO[j].toString());
								}
								for (int m = 0; m < ln.size(); m++) {

									int vN = m + 1;
									String vnum = new Integer(vN).toString();
									String stName = (wFService
											.findTblWfParticularInfoName(ooid,
													vnum));
									if (lo.contains(ln.toArray()[m].toString())) {
										String vsel = "SELECTED";// 选中状态
										if (vN == ln.size()) {
											ls.add(new StepsVo(("END" + "结束"),
													vsel, vnum));
										} else {
											ls.add(new StepsVo((vnum + stName),
													vsel, vnum));
										}
									} else {
										String vsel = "";
										if (vN == ln.size()) {
											ls.add(new StepsVo(("END" + "结束"),
													vsel, vnum));
										} else {
											ls.add(new StepsVo((vnum + stName),
													vsel, vnum));
										}
									}
								}
							} else {
								for (int i = 1; i <= sN; i++) {
									String num = new Integer(i).toString();
									String stName = (wFService
											.findTblWfParticularInfoName(ooid,
													num));
									String vsel = "";
									if (i == sN) {
										ls.add(new StepsVo("END" + "结束", vsel,
												num));
									} else {
										ls.add(new StepsVo(num + stName, vsel,
												num));
									}
								}
							}
							// 退回
							String stepOff = theForm.getWfpinfo()
									.getGobackStep();
							List lt = new ArrayList();// 所有退回步骤
							for (int n = 0; n < ln.size(); n++) {
								int vN = n + 1;
								String vnum = new Integer(vN).toString();
								String stName = (wFService
										.findTblWfParticularInfoName(ooid, vnum));
								if (ln.toArray()[n].toString().equals(stepOff)) {
									String vsel = "SELECTED";// 选中状态
									if (vN == ln.size()) {
										lt.add(new StepsVo("END" + "结束", vsel,
												vnum));
									} else {
										lt.add(new StepsVo(vnum + stName, vsel,
												vnum));
									}
								} else {
									String vsel = "";
									if (vN == ln.size()) {
										lt.add(new StepsVo("END" + "结束", vsel,
												vnum));
									} else {
										lt.add(new StepsVo(vnum + stName, vsel,
												vnum));
									}
								}
							}
							theForm.setGobacklist(lt);
							theForm.setCommitlist(ls);
							// 角色
							TblCmnRole tcr = new TblCmnRole();
							String roles = theForm.getWfpinfo().getTblUserId();
							String[] ro = null;
							List lro = new ArrayList();
							if (roles != null && !("").equals(roles)) {
								ro = roles.split(",");
								for (int g = 0; g < ro.length; g++) {
									String roleCd = ro[g].toString();
									tcr = wFService.findroleCdRole(roleCd);
									if (tcr != null && !tcr.equals("")) {
										lro.add(tcr);
									}
								}
							}
							if (lro.size() > 0) {
								theForm.setRoleli(lro);
							}
							// 权限
							if (qxli == null || qxli.size() <= 0) {
								List colums = new ArrayList();
								colums.add(new Column("", "", "", ""));
								request.setAttribute("list", colums);
							} else {
								request.setAttribute("list", qxli);
							}
							// 转发到particularInfo.jsp页面中的显示信息; end

							request.setAttribute("ooid", ooid);
							bool = false;
							break;
						} else {
							bool = true;
						}
					}
				}
			} else {
				bool = true;
			}
		}
		return bool;
	}

	/**
	 * @author yangxl time 2008-06-26 功能描述 检查stepName是否唯一
	 * 
	 */
	public boolean stepNameCheck(OperationForm theForm,
			HttpServletRequest request, String ooid) {

		String fid = null;// id
		String fieldName = null;// 表单项
		String fieldCode = null;// 表单KEY
		String editorFlg = null;// 是否可入例

		boolean bool = false;
		String stepName = theForm.getWfpinfo().getStepName();
		TblWfOperationInfo tblWfOperChecks = new TblWfOperationInfo();
		TblWfParticularInfo tblWfParChecks = new TblWfParticularInfo();
		tblWfOperChecks = wFService.findWfOperInfoById(ooid);
		if (tblWfOperChecks != null) {
			Set set = tblWfOperChecks.getTblWfParticularInfos();
			// String numStep = tblWfOperChecks.getStepNum();// 流程定义的步骤
			if (set.size() > 0) {
				for (Iterator it = set.iterator(); it.hasNext();) {
					tblWfParChecks = (TblWfParticularInfo) it.next();
					if (tblWfParChecks != null
							&& !upId.equals(tblWfParChecks.getId())) {
						String stepNameInfo = tblWfParChecks.getStepName()
								.toString();
						if (stepName.equals(stepNameInfo)) {
							MessageUtils.addErrorMessage(request, MessageInfo
									.factory().getMessage("WF_I029_p_0"));

							// 转发到particularInfo.jsp页面中的显示信息；start
							// fieldStates;
							Set fSet = theForm.getWfpinfo().getTblWfFormInfos();
							Set qxli = new HashSet();
							if (fSet.size() != '0') {
								for (int j = 0; j < fSet.size(); j++) {

									TblWfFormInfo tblWfFormInfo = (TblWfFormInfo) fSet
											.toArray()[j];
									if (tblWfFormInfo.getId() != null
											&& !("").equals(tblWfFormInfo
													.getId())) {
										fid = tblWfFormInfo.getId();
									}
									if (tblWfFormInfo.getFieldName() != null
											&& !("").equals(tblWfFormInfo
													.getFieldName())) {
										fieldName = tblWfFormInfo
												.getFieldName();
									}

									if (tblWfFormInfo.getFieldCode() != null
											&& !("").equals(tblWfFormInfo
													.getFieldCode())) {
										fieldCode = tblWfFormInfo
												.getFieldCode();
									}
									if (tblWfFormInfo.getEditorFlg() != null
											&& !("").equals(tblWfFormInfo
													.getEditorFlg())) {
										editorFlg = tblWfFormInfo
												.getEditorFlg();
									}
									qxli.add(new Column(fid, fieldName,
											fieldCode, editorFlg));
								}
							}
							// 生成提交退回列表
							String stepNmu = tblWfOperChecks.getStepNum();
							int sN = 0;
							if (stepNmu != null && !("").equals(stepNmu)) {
								sN = new Integer(stepNmu).intValue();
							} else {
								sN = 50;
							}
							List ln = new ArrayList();
							for (int i = 1; i < sN; i++) {
								String num = new Integer(i).toString();
								ln.add(num);
							}
							ln.add("99");
							// 提交
							String stepOn = theForm.getWfpinfo()
									.getCommitStep();
							String[] sO = null;
							List lo = new ArrayList();// 选中提交步骤
							List ls = new ArrayList();// 所有提交步骤
							if (stepOn != null && !("").equals(stepOn)) {
								sO = stepOn.split(",");
								for (int j = 0; j < sO.length; j++) {
									lo.add(sO[j].toString());
								}
								for (int m = 0; m < ln.size(); m++) {

									int vN = m + 1;
									String vnum = new Integer(vN).toString();
									String stName = (wFService
											.findTblWfParticularInfoName(ooid,
													vnum));
									if (lo.contains(ln.toArray()[m].toString())) {
										String vsel = "SELECTED";// 选中状态
										if (vN == ln.size()) {
											ls.add(new StepsVo(("END" + "结束"),
													vsel, vnum));
										} else {
											ls.add(new StepsVo((vnum + stName),
													vsel, vnum));
										}
									} else {
										String vsel = "";
										if (vN == ln.size()) {
											ls.add(new StepsVo(("END" + "结束"),
													vsel, vnum));
										} else {
											ls.add(new StepsVo((vnum + stName),
													vsel, vnum));
										}
									}
								}
							} else {
								for (int i = 1; i <= sN; i++) {
									String num = new Integer(i).toString();
									String stName = (wFService
											.findTblWfParticularInfoName(ooid,
													num));
									String vsel = "";
									if (i == sN) {
										ls.add(new StepsVo("END" + "结束", vsel,
												num));
									} else {
										ls.add(new StepsVo(num + stName, vsel,
												num));
									}
								}
							}
							// 退回
							String stepOff = theForm.getWfpinfo()
									.getGobackStep();
							List lt = new ArrayList();// 所有退回步骤
							for (int n = 0; n < ln.size(); n++) {
								int vN = n + 1;
								String vnum = new Integer(vN).toString();
								String stName = (wFService
										.findTblWfParticularInfoName(ooid, vnum));
								if (ln.toArray()[n].toString().equals(stepOff)) {
									String vsel = "SELECTED";// 选中状态
									if (vN == ln.size()) {
										lt.add(new StepsVo("END" + "结束", vsel,
												vnum));
									} else {
										lt.add(new StepsVo(vnum + stName, vsel,
												vnum));
									}
								} else {
									String vsel = "";
									if (vN == ln.size()) {
										lt.add(new StepsVo("END" + "结束", vsel,
												vnum));
									} else {
										lt.add(new StepsVo(vnum + stName, vsel,
												vnum));
									}
								}
							}
							theForm.setGobacklist(lt);
							theForm.setCommitlist(ls);
							// 角色
							TblCmnRole tcr = new TblCmnRole();
							String roles = theForm.getWfpinfo().getTblUserId();
							String[] ro = null;
							List lro = new ArrayList();
							if (roles != null && !("").equals(roles)) {
								ro = roles.split(",");
								for (int g = 0; g < ro.length; g++) {
									String roleCd = ro[g].toString();
									tcr = wFService.findroleCdRole(roleCd);
									if (tcr != null && !tcr.equals("")) {
										lro.add(tcr);
									}
								}
							}
							if (lro.size() > 0) {
								theForm.setRoleli(lro);
							}
							// 权限
							if (qxli == null || qxli.size() <= 0) {
								List colums = new ArrayList();
								colums.add(new Column("", "", "", ""));
								request.setAttribute("list", colums);
							} else {
								request.setAttribute("list", qxli);
							}
							// 转发到particularInfo.jsp页面中的显示信息; end

							request.setAttribute("ooid", ooid);
							bool = false;
							break;
						} else {
							bool = true;
						}
					}
				}
			} else {
				bool = true;
			}
		}
		return bool;
	}

}
