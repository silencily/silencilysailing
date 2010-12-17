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

	public static final String FORWARD_LIST = "list";// ��ҵ����Ϣ�б�

	public static final String FORWARD_INFO = "info";// ҵ�������б�

	public static final String FORWARD_PARTINFO = "partInfo";// ������ϸ��Ϣ���������ڣ���

	public static final String WF_INFO = "wf_info";

	private String upId = "";// ������Ϣ��id

	String operNameSelect;

	String nameSelect;

	String opeditionSelect;

	/**
	 * @author gejianbao time 2009-02-20 �������� ���ýӿڷ���
	 * 
	 */
	public static OperationService service() {
		return (OperationService) ServiceProvider
				.getService(OperationService.SERVICE_NAME);
	}

	/**
	 * @author gejianbao time 2009-02-26 �������� ���ù��ýӿ�
	 * 
	 */
	private CommonService getService() {
		return (CommonService) ServiceProvider
				.getService(CommonService.SERVICE_NAME);
	}

	/**
	 * @author gejianbao time 2009-02-26 �������� ��¼
	 * 
	 */
	public ActionForward entry(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward(FORWARD_ENTRY);
	}

	/**
	 * @author yangxl time 2007-11-27 �������� �б�
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
		 * ��ȡjsp��table��ǩ�е�operviewBean ��ͨ��dealList(List
		 * listTemp,HttpServletRequest request)�������з�ҳ��
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
	 * @author yangxl time 2007-11-27 �������� ������ϸ��Ϣ
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
		// �༭״̬�жϣ�
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

				// �����ύ�˻��б�
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
				// �ύ
				String stepOn = tblWfParticularInfo.getCommitStep();
				String[] sO = null;
				List lo = new ArrayList();// ѡ���ύ����
				List ls = new ArrayList();// �����ύ����
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
							String vsel = "SELECTED";// ѡ��״̬
							if (vN == ln.size()) {
								ls.add(new StepsVo(("END" + "����"), vsel, vnum));
							} else {
								ls
										.add(new StepsVo((vnum + stName), vsel,
												vnum));
							}
						} else {
							String vsel = "";
							if (vN == ln.size()) {
								ls.add(new StepsVo(("END" + "����"), vsel, vnum));
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
							ls.add(new StepsVo("END" + "����", vsel, num));
						} else {
							ls.add(new StepsVo(num + stName, vsel, num));
						}
					}
				}
				// �˻�
				String stepOff = tblWfParticularInfo.getGobackStep();
				List lt = new ArrayList();// �����˻ز���
				for (int n = 0; n < ln.size(); n++) {
					int vN = n + 1;
					String vnum = new Integer(vN).toString();
					String stName = (wFService.findTblWfParticularInfoName(
							ooid, vnum));
					if (ln.toArray()[n].toString().equals(stepOff)) {
						String vsel = "SELECTED";// ѡ��״̬
						if (vN == ln.size()) {
							lt.add(new StepsVo("END" + "����", vsel, vnum));
						} else {
							lt.add(new StepsVo(vnum + stName, vsel, vnum));
						}
					} else {
						String vsel = "";
						if (vN == ln.size()) {
							lt.add(new StepsVo("END" + "����", vsel, vnum));
						} else {
							lt.add(new StepsVo(vnum + stName, vsel, vnum));
						}
					}
				}
				theForm.setGobacklist(lt);
				theForm.setCommitlist(ls);

				// ��ɫ
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
				// Ȩ��
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
				// �����ύ�˻��б�
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
						l1.add(new StepsVo("END" + "����", vsel, num));
					} else {
						l1.add(new StepsVo(num + stName, vsel, num));
					}

				}
				theForm.setGobacklist(l1);
				theForm.setCommitlist(l1);

				List colums = new ArrayList();
				colums.add(new Column("", "", "", ""));
				request.setAttribute("list", colums);

				// yangxl 2008-5-27 �¼� �������Ʋ���id�Զ���һ start
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

		String nameoper = tblWfOperationInfo.getName();// ��������
		request.setAttribute("nameoper", nameoper);
		return mapping.findForward(FORWARD_PARTINFO);
	}

	/**
	 * @author yangxl time 2007-11-28 �������� �༭ҳ�棨ҵ�������б�
	 * 
	 */
	public ActionForward info(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		OperationForm theForm = (OperationForm) form;
		String oid = request.getParameter("oid");
		
		wFService = (WFService) ServiceProvider
		.getService(WFService.SERVICE_NAME);

		// ��ȡ�����˲��ɱ༭�б������б����ݣ�
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
		// ����Ա
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
	 * @author yangxl time 2007-11-28 �������� �����½�������
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

		// ����Ա
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
	 * @author yangxl time 2007-11-28 �������� �߼�ɾ��������
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
	 * @author yangxl time 2008-01-27 �������� �����޸�ҳ�棨ҵ�������б�
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
		// ����Ա
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
		// ��ȡ�����˲��ɱ༭�б������б����ݣ�
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
	 * @author yangxl time 2007-11-30 �������� ɾ������
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

		// ��ȡ�����˲��ɱ༭�б������б����ݣ�
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
	 * @author yangxl time 2007-11-30 �������� ����XML
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
		// �ж��������Ƿ��в��������� end
		return info(mapping, form, request, response);
	}

	/**
	 * @author yangxl time 2008-5-29 �������� ����������XML
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
			// 2008-5-29yangxl �ж��������Ƿ��в��������� start
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
	 * @author yangxl time 2007-11-30 �������� ���沽����Ϣ
	 * 
	 */
	public ActionForward saveParticular(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		OperationForm theForm = (OperationForm) form;
		String ooid = request.getParameter("ooid");// ������Ϣid
		// 2008-5-29 yangxl stepIdΨһ����֤ Start
		boolean boolStepId = false;
		boolStepId = stepIdCheck(theForm, request, ooid);
		if (!boolStepId) {
			return mapping.findForward(FORWARD_PARTINFO);
		}
		// stepIdΨһ����֤end
		// 2008-6-30 yangxl stepNamwΨһ����֤ Start
		boolean boolStepName = false;
		boolStepName = stepNameCheck(theForm, request, ooid);
		if (!boolStepName) {
			return mapping.findForward(FORWARD_PARTINFO);
		}
		// stepNameΨһ����֤end
		String fid = null;// id
		String fieldName = null;// ����
		String fieldCode = null;// ��KEY
		String editorFlg = null;// �Ƿ������
		// 2008-6-2 yangxlͨ��ooid��ò�������Strat
		TblWfOperationInfo tblWfOperationInfo = new TblWfOperationInfo();
		tblWfOperationInfo = wFService.findWfOperInfoById(ooid);
		String stepIdSum = null;// ��������
		if (tblWfOperationInfo != null) {
			stepIdSum = tblWfOperationInfo.getStepNum();
		}
		// end
		TblWfParticularInfo tblWfParticularInfosave = theForm.getWfpinfo();

		// 2008-6-2yangxl ��֤�ύ�����Ƿ������һ��
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
		// ��ɫ
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
		// fieldStatus��
		// try {
		String fieldStatus = request.getParameter("fieldStatus");
		tblWfParticularInfosave.setFieldStatus(fieldStatus);
		// ȡ��Ȩ��
		if (fieldStatus.equals("3")) {
			Set formSet = tblWfParticularInfosave.getTblWfFormInfos();
			List listForm = new ArrayList();
			String[] popedom = request.getParameterValues("row");
			for (Iterator it = formSet.iterator(); it.hasNext();) {

				boolean delflg = true;
				TblWfFormInfo tbl = (TblWfFormInfo) it.next();
				// �ж��Ƿ�ɾ��
				int i = 0;
				if (popedom != null && !("").equals(popedom)) {
					for (i = 0; i < popedom.length; i += 4) {
						if (popedom[i].equalsIgnoreCase(tbl.getId())) {
							delflg = false;
							break;
						}
					}
					if (!delflg)// ����
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
				// ɾ��
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
			// ����
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
		// ����������ɹ�ʱ�Զ�����������stepNum+1��
		/*
		 * if (("").equals(upId) || upId == null) { int tempstepNumInt =
		 * Integer.parseInt(stepIdSum); tempstepNumInt++; String tempstepNumStr
		 * = Integer.toString(tempstepNumInt);
		 * tblWfOperationInfo.setStepNum(tempstepNumStr);
		 * wFService.updateWfOperInfo(tblWfOperationInfo); }
		 */

		// �����ת����particularInfo.jspҳ���е���ʾ��Ϣ��start
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
		// �����ύ�˻��б�
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
		// �ύ
		String stepOn = tblWfParticularInfosave.getCommitStep();
		String[] sO = null;
		List lo = new ArrayList();// ѡ���ύ����
		List ls = new ArrayList();// �����ύ����
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
					String vsel = "SELECTED";// ѡ��״̬
					if (vN == ln.size()) {
						ls.add(new StepsVo(("END" + "����"), vsel, vnum));
					} else {
						ls.add(new StepsVo((vnum + stName), vsel, vnum));
					}
				} else {
					String vsel = "";
					if (vN == ln.size()) {
						ls.add(new StepsVo(("END" + "����"), vsel, vnum));
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
					ls.add(new StepsVo("END" + "����", vsel, num));
				} else {
					ls.add(new StepsVo(num + stName, vsel, num));
				}
			}
		}
		// �˻�
		String stepOff = tblWfParticularInfosave.getGobackStep();
		List lt = new ArrayList();// �����˻ز���
		for (int n = 0; n < ln.size(); n++) {
			int vN = n + 1;
			String vnum = new Integer(vN).toString();
			String stName = (wFService.findTblWfParticularInfoName(ooid, vnum));
			if (ln.toArray()[n].toString().equals(stepOff)) {
				String vsel = "SELECTED";// ѡ��״̬
				if (vN == ln.size()) {
					lt.add(new StepsVo("END" + "����", vsel, vnum));
				} else {
					lt.add(new StepsVo(vnum + stName, vsel, vnum));
				}
			} else {
				String vsel = "";
				if (vN == ln.size()) {
					lt.add(new StepsVo("END" + "����", vsel, vnum));
				} else {
					lt.add(new StepsVo(vnum + stName, vsel, vnum));
				}
			}
		}
		theForm.setGobacklist(lt);
		theForm.setCommitlist(ls);
		// ��ɫ
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
		// Ȩ��
		if (qxli == null || qxli.size() <= 0) {
			List colums = new ArrayList();
			colums.add(new Column("", "", "", ""));
			request.setAttribute("list", colums);
		} else {
			request.setAttribute("list", qxli);
		}
		// �����ת����particularInfo.jspҳ���е���ʾ��Ϣ; end

		request.setAttribute("ooid", ooid);
		MessageUtils.addMessage(request, MessageInfo.factory().getMessage(
				"WF_I027_p_0"));
		return mapping.findForward(FORWARD_PARTINFO);
		// return particularInfo(mapping, form, request, response);
	}

	/**
	 * @author yangxl time 2008-05-28 �������� ���stepId�Ƿ�Ψһ
	 * 
	 */
	public boolean stepIdCheck(OperationForm theForm,
			HttpServletRequest request, String ooid) {

		String fid = null;// id
		String fieldName = null;// ����
		String fieldCode = null;// ��KEY
		String editorFlg = null;// �Ƿ������

		boolean bool = false;
		String stepId = theForm.getWfpinfo().getStepId().toString();
		TblWfOperationInfo tblWfOperChecks = new TblWfOperationInfo();
		TblWfParticularInfo tblWfParChecks = new TblWfParticularInfo();
		tblWfOperChecks = wFService.findWfOperInfoById(ooid);
		if (tblWfOperChecks != null) {

			Set set = tblWfOperChecks.getTblWfParticularInfos();
			// String numStep = tblWfOperChecks.getStepNum();// ���̶���Ĳ���
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

							// ת����particularInfo.jspҳ���е���ʾ��Ϣ��start
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
							// �����ύ�˻��б�
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
							// �ύ
							String stepOn = theForm.getWfpinfo()
									.getCommitStep();
							String[] sO = null;
							List lo = new ArrayList();// ѡ���ύ����
							List ls = new ArrayList();// �����ύ����
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
										String vsel = "SELECTED";// ѡ��״̬
										if (vN == ln.size()) {
											ls.add(new StepsVo(("END" + "����"),
													vsel, vnum));
										} else {
											ls.add(new StepsVo((vnum + stName),
													vsel, vnum));
										}
									} else {
										String vsel = "";
										if (vN == ln.size()) {
											ls.add(new StepsVo(("END" + "����"),
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
										ls.add(new StepsVo("END" + "����", vsel,
												num));
									} else {
										ls.add(new StepsVo(num + stName, vsel,
												num));
									}
								}
							}
							// �˻�
							String stepOff = theForm.getWfpinfo()
									.getGobackStep();
							List lt = new ArrayList();// �����˻ز���
							for (int n = 0; n < ln.size(); n++) {
								int vN = n + 1;
								String vnum = new Integer(vN).toString();
								String stName = (wFService
										.findTblWfParticularInfoName(ooid, vnum));
								if (ln.toArray()[n].toString().equals(stepOff)) {
									String vsel = "SELECTED";// ѡ��״̬
									if (vN == ln.size()) {
										lt.add(new StepsVo("END" + "����", vsel,
												vnum));
									} else {
										lt.add(new StepsVo(vnum + stName, vsel,
												vnum));
									}
								} else {
									String vsel = "";
									if (vN == ln.size()) {
										lt.add(new StepsVo("END" + "����", vsel,
												vnum));
									} else {
										lt.add(new StepsVo(vnum + stName, vsel,
												vnum));
									}
								}
							}
							theForm.setGobacklist(lt);
							theForm.setCommitlist(ls);
							// ��ɫ
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
							// Ȩ��
							if (qxli == null || qxli.size() <= 0) {
								List colums = new ArrayList();
								colums.add(new Column("", "", "", ""));
								request.setAttribute("list", colums);
							} else {
								request.setAttribute("list", qxli);
							}
							// ת����particularInfo.jspҳ���е���ʾ��Ϣ; end

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
	 * @author yangxl time 2008-06-26 �������� ���stepName�Ƿ�Ψһ
	 * 
	 */
	public boolean stepNameCheck(OperationForm theForm,
			HttpServletRequest request, String ooid) {

		String fid = null;// id
		String fieldName = null;// ����
		String fieldCode = null;// ��KEY
		String editorFlg = null;// �Ƿ������

		boolean bool = false;
		String stepName = theForm.getWfpinfo().getStepName();
		TblWfOperationInfo tblWfOperChecks = new TblWfOperationInfo();
		TblWfParticularInfo tblWfParChecks = new TblWfParticularInfo();
		tblWfOperChecks = wFService.findWfOperInfoById(ooid);
		if (tblWfOperChecks != null) {
			Set set = tblWfOperChecks.getTblWfParticularInfos();
			// String numStep = tblWfOperChecks.getStepNum();// ���̶���Ĳ���
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

							// ת����particularInfo.jspҳ���е���ʾ��Ϣ��start
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
							// �����ύ�˻��б�
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
							// �ύ
							String stepOn = theForm.getWfpinfo()
									.getCommitStep();
							String[] sO = null;
							List lo = new ArrayList();// ѡ���ύ����
							List ls = new ArrayList();// �����ύ����
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
										String vsel = "SELECTED";// ѡ��״̬
										if (vN == ln.size()) {
											ls.add(new StepsVo(("END" + "����"),
													vsel, vnum));
										} else {
											ls.add(new StepsVo((vnum + stName),
													vsel, vnum));
										}
									} else {
										String vsel = "";
										if (vN == ln.size()) {
											ls.add(new StepsVo(("END" + "����"),
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
										ls.add(new StepsVo("END" + "����", vsel,
												num));
									} else {
										ls.add(new StepsVo(num + stName, vsel,
												num));
									}
								}
							}
							// �˻�
							String stepOff = theForm.getWfpinfo()
									.getGobackStep();
							List lt = new ArrayList();// �����˻ز���
							for (int n = 0; n < ln.size(); n++) {
								int vN = n + 1;
								String vnum = new Integer(vN).toString();
								String stName = (wFService
										.findTblWfParticularInfoName(ooid, vnum));
								if (ln.toArray()[n].toString().equals(stepOff)) {
									String vsel = "SELECTED";// ѡ��״̬
									if (vN == ln.size()) {
										lt.add(new StepsVo("END" + "����", vsel,
												vnum));
									} else {
										lt.add(new StepsVo(vnum + stName, vsel,
												vnum));
									}
								} else {
									String vsel = "";
									if (vN == ln.size()) {
										lt.add(new StepsVo("END" + "����", vsel,
												vnum));
									} else {
										lt.add(new StepsVo(vnum + stName, vsel,
												vnum));
									}
								}
							}
							theForm.setGobacklist(lt);
							theForm.setCommitlist(ls);
							// ��ɫ
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
							// Ȩ��
							if (qxli == null || qxli.size() <= 0) {
								List colums = new ArrayList();
								colums.add(new Column("", "", "", ""));
								request.setAttribute("list", colums);
							} else {
								request.setAttribute("list", qxli);
							}
							// ת����particularInfo.jspҳ���е���ʾ��Ϣ; end

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
