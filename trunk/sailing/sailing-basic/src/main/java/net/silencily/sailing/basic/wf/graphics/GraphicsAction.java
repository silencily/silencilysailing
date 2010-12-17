package net.silencily.sailing.basic.wf.graphics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.basic.wf.WorkflowInfo;
import net.silencily.sailing.basic.wf.domain.TblWfOperationInfo;
import net.silencily.sailing.basic.wf.domain.TblWfParticularInfo;
import net.silencily.sailing.basic.wf.entry.WorkflowRelation;
import net.silencily.sailing.basic.wf.entry.WorkflowStep;
import net.silencily.sailing.basic.wf.service.WFService;
import net.silencily.sailing.basic.wf.util.BisWfServiceLocator;
import net.silencily.sailing.basic.wf.util.WfUtils;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

public class GraphicsAction extends DispatchAction {

	public ActionForward wfNoEntityInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String wfId = request.getParameter("idw");
		WFService wFService = null;
		String wfName;
		List listWfName = new ArrayList();
		List listStepId = new ArrayList();
		List listStepName = new ArrayList();
		List listCommitStepId = new ArrayList();
		if (wfId == null || wfId.equals("") || wfId.length() < 1) {
			throw new Exception("������������");
		}
		TblWfOperationInfo tblWfOperationInfo = null;
		try {
			wFService = (WFService) ServiceProvider
					.getService(WFService.SERVICE_NAME);
			tblWfOperationInfo = wFService.findWfOperInfoById(wfId);
			wfName = tblWfOperationInfo.getName();
			listWfName.add(wfName);

		} catch (Exception e) {
			e.printStackTrace();
		}
		Set tblWfOperationInfoSet = tblWfOperationInfo
				.getTblWfParticularInfos();
		Iterator itr = null;
		try {
			itr = tblWfOperationInfoSet.iterator();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String temp;
		String concatTemp;
		while (itr.hasNext()) {
			concatTemp = "";
			concatTemp = "";
			TblWfParticularInfo element = (TblWfParticularInfo) itr.next();
			listStepId.add(element.getStepId().toString());
			listStepName.add(element.getStepName().toString());
			try {
				temp = element.getCommitStep().toString();
				System.out.println(temp);
			} catch (Exception e) {
				temp = "";
			}
			try {
				concatTemp = element.getGobackStep().toString();
			} catch (Exception e) {
				concatTemp = "";
			}
			concatTemp = temp + concatTemp;
			listCommitStepId.add(concatTemp);

		}
		request.setAttribute("listWorkFlowName", listWfName);
		/*
		 * request.setAttribute("listCurrentStepID", listCurrentStepID);
		 * request.setAttribute("listHistoryStepID", listHistoryStepID);
		 * request.setAttribute("listStepStatus", listStepStatus);
		 */
		request.setAttribute("listStepId", listStepId);
		request.setAttribute("listStepName", listStepName);
		request.setAttribute("listCommitStepId", listCommitStepId);
		/* request.setAttribute("listHistoryInfo", historyInfo); */

		return mapping.findForward("graphics");
	}

	/*public ActionForward wfinfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String oid = request.getParameter("oid");
		String dtoClassName = request.getParameter("dtoClassName");
		Class dtoClass = null;
		WorkflowInfo wfinfo = null;
		WFService wFService = null;

		if (oid == null || oid.equals("") || oid.length() < 1) {
			throw new Exception("ʵ����������Ϊ��");
		} 
		if (dtoClassName == null || dtoClassName.equals("")
				|| dtoClassName.length() < 1) {
			throw new Exception("ʵ����������Ϊ��");
		}
		try {
			int getIndex = dtoClassName.toString().indexOf("$");
			if (getIndex == -1) {
				getIndex = dtoClassName.length();
			}
			String dtoName = (String) dtoClassName.subSequence(0, getIndex);
			dtoClass = Class.forName(dtoName);

		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
			throw new Exception("ʵ���������Ͳ�����");
		}
		try {
			wfinfo = (WorkflowInfo) getService().load(dtoClass, oid);
			// com.qware.rm.domain.TblRmStPlanMaster tblRmStPlanMaster =
			// (com.qware.rm.domain.TblRmStPlanMaster)getService().load(dtoClass,
			// oid);
			// Object obj = getService().load(dtoClass, oid);
		} catch (ClassCastException cce) {
			cce.printStackTrace();
			throw new Exception("ʵ����ĸ��಻��WorkflowInfo");
		}

		List listWfName = new ArrayList();
		String wfName = wfinfo.getWorkflowName();

		wFService = (WFService) ServiceProvider
				.getService(WFService.SERVICE_NAME);
		TblWfOperationInfo tblWfOperationInfo;
		try {
			tblWfOperationInfo = (TblWfOperationInfo) wFService
					.findWfOperInfoByName(wfName);
			listWfName.add(wfName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("���������Ʋ�����");
		}
		// --------------------------------------------------------------------
		Set tblWfOperationInfoSet = tblWfOperationInfo
				.getTblWfParticularInfos();
		Iterator itr = null;
		try {
			itr = tblWfOperationInfoSet.iterator();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ����XML�ļ�������StepID
		List listStepId = new ArrayList();
		// ����XML�ļ�������StepName
		List listStepName = new ArrayList();
		// ����XML�ļ��е�������һ����Steps
		List listCommitStepId = new ArrayList();
		String temp;
		String concatTemp;
		while (itr.hasNext()) {
			concatTemp = "";
			concatTemp = "";
			TblWfParticularInfo element = (TblWfParticularInfo) itr.next();
			listStepId.add(element.getStepId().toString());
			listStepName.add(element.getStepName().toString());
			try {
				temp = element.getCommitStep().toString();
				System.out.println(temp);
			} catch (Exception e) {
				temp = "";
			}
			try {
				concatTemp = element.getGobackStep().toString();
			} catch (Exception e) {
				concatTemp = "";
			}
			concatTemp = temp + concatTemp;
			listCommitStepId.add(concatTemp);

		}
		System.out.println("����StepID   StepName    CommitSteps");
		for (int i = 0; i < listCommitStepId.size(); i++) {
			System.out.println(listStepId.get(i).toString() + "    "
					+ listStepName.get(i).toString() + "      "
					+ listCommitStepId.get(i));
		}

		// ��ȡ��ǰ�������ʷ����
		String currentStepID = wfinfo.getCurrentStep().getId();
		List listCurrentStepID = new ArrayList();

		if (!(wfinfo.getCurrentStep().getId() == null)) {
			listCurrentStepID.add(currentStepID.toString());
		}

		List listHistoryStepObject = wfinfo.getHistorySteps();
		for (int i = 0; i < listHistoryStepObject.size(); i++) {
			try {
				if (((WorkflowStep) listHistoryStepObject.get(i)).getAction()
						.getName().indexOf("ȡ��") >= 0) {
					listHistoryStepObject.remove(i);
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		List listHistoryStepID = new ArrayList();
		List listTotalStepId = new ArrayList();

		List listHistoryInfo = new ArrayList();

		List historyInfo = new ArrayList();

		for (int i = 0; i < listHistoryStepObject.size(); i++) {
			String strHistoryStepId = ((WorkflowStep) listHistoryStepObject
					.get(i)).getId().toString();
			StringBuffer sbHistoryInfo = new StringBuffer();
			String strProcessPerson = ((WorkflowStep) listHistoryStepObject
					.get(i)).getUserChineseName().toString();
			Date strCommitTime1 = ((WorkflowStep) listHistoryStepObject.get(i))
					.getFinishTime();

			strProcessPerson.replaceAll(" ", "-");
			String strCommitTime = new SimpleDateFormat("yyyy��MM��dd��HHʱmm��")
					.format(strCommitTime1);
			strCommitTime.replaceAll(" ", "-");

			sbHistoryInfo.append("��������:"
					+ strProcessPerson.replaceAll(" ", "-") + "��");
			sbHistoryInfo.append("������ʱ��:" + strCommitTime.replaceAll(" ", "-")
					+ "��");

			listHistoryInfo.add(sbHistoryInfo);
			listTotalStepId.add(strHistoryStepId);
			listHistoryStepID.add(strHistoryStepId);
		}

		List list1 = new ArrayList();

		for (int i = 0; i < listHistoryStepID.size(); i++) {
			String str = "";
			for (int j = 0; j < listHistoryStepID.size(); j++) {
				if (listHistoryStepID.get(i).equals(listHistoryStepID.get(j))) {
					str = j + "," + str;
				}
			}
			list1.add(str);
		}
		for (int i = 0; i < list1.size(); i++) {
			String strTemp = list1.get(i).toString();
			String[] str = strTemp.split(",");
			String strH = "";
			for (int j = 0; j < str.length; j++) {
				strH = listHistoryInfo.get(Integer.parseInt(str[j])).toString()
						+ strH;
			}
			historyInfo.add(strH);
		}

		System.out.println("��ʷ���� : ");
		for (int i = 0; i < listHistoryStepID.size(); i++) {
			System.out.print(listHistoryStepID.get(i).toString() + "   ");
		}

		for (int i = 0; i < listHistoryInfo.size(); i++) {
			System.out.println(listHistoryInfo.get(i).toString());
		}
		listTotalStepId.add(currentStepID);

		List listStepStatus = new ArrayList();
		for (int i = 0; i < listStepId.size(); i++) {
			listStepStatus.add("2");
		}

		for (int i = 0; i < listStepId.size(); i++) {
			for (int j = 0; j < listHistoryStepID.size(); j++) {
				if (listStepId.get(i).toString().equals(
						listHistoryStepID.get(j).toString())) {
					listStepStatus.set(i, "0");
				}
			}
			if (listStepId.get(i).toString().equals(currentStepID)) {
				listStepStatus.set(i, "1");
			}
		}

//		start
		String stepIdSuper = "";
		String commitStepSuper = "";
		String specialObjectSuper = "";
		Set tblWfOperationInfoSetSuper = tblWfOperationInfo.getTblWfParticularInfos();
		Iterator itrSuper = null;
		try {
			itrSuper = tblWfOperationInfoSetSuper.iterator();
		} catch (Exception e) {
			e.printStackTrace();
		}
		WfUtils util = new WfUtils();
		WorkflowRelation wr = new WorkflowRelation();
		while (itrSuper.hasNext()) {
			
			TblWfParticularInfo element = (TblWfParticularInfo) itrSuper.next();
			try{
				stepIdSuper = element.getStepId().toString();
			}catch(Exception e){
				stepIdSuper = "";
			}
			try {
				commitStepSuper = element.getCommitStep().toString();
			} catch (Exception e) {
				commitStepSuper = "";
			}
			try{
				specialObjectSuper = element.getPointStep().toString();
			}catch(Exception e){
				specialObjectSuper = "";
			}
			try{
				if(listHistoryStepID.size()>0){
					for( int n = 0; n < listHistoryStepID.size(); n++){
						if(listHistoryStepID.get(n).toString().equals(stepIdSuper)){
							wr.setStepId(listHistoryStepID.get(n).toString());
							wr.setSuperOid(oid);
							wr.setSuperClass(dtoClass.getName());
							String subStatus = util.getSubwfInfoStatus(wr);
							if(subStatus!=null && !("").equals(subStatus)){
								String specialObjectSuperToSub = com.qware.am.wo.wo.WoCommon.Inherit(specialObjectSuper,"0");
								if(specialObjectSuperToSub!=null){
									String[] stepIdStr =commitStepSuper.split(",");
									for( int i = 0; i < stepIdStr.length; i++){
										String nextStepSpecObjs = BisWfServiceLocator.getWorkflowService().getSpecialObject(wfName,stepIdStr[i].toString());
										if(nextStepSpecObjs!=null && !nextStepSpecObjs.equals("")){
											String[] nextStepSpecObj = nextStepSpecObjs.split(":");
											for(int j = 0; j < nextStepSpecObj.length; j++){
												//�ж��ύ��������������Ƿ���INHERIT �����Ҫ��¼stepId
												if(nextStepSpecObj[j].equals("SUBFLOW")){
													int superNumber = new Integer(stepIdSuper).intValue();
													int subNumber = new Integer(stepIdStr[i]).intValue();
													
													if(subStatus.equals("0")){
														listStepStatus.set(subNumber-1, "1");
														listStepStatus.set(superNumber-1, "0");
													}else if(subStatus.equals("1")){
														listStepStatus.set(subNumber-1, "0");
														//listStepStatus.set(superNumber-1, "0");
													}else{
														listStepStatus.set(subNumber-1, "1");
														listStepStatus.set(superNumber-1, "0");
													}
													try{
														for(int k = 0; k < listHistoryStepID.size(); k++){
															if(listHistoryStepID.get(k).equals(stepIdSuper) && listHistoryStepID.get(k+1).equals(stepIdSuper)){
																listHistoryStepID.set(k+1, stepIdStr[i]);
															}
														}
													}catch(Exception e){
														//
													}
													if(listHistoryStepID.get(listHistoryStepID.size()-1).equals(stepIdSuper)){
														//listHistoryStepID.add(stepIdStr[i]);
														//historyInfo.add("��������:��������������ʱ��:2008��07��08��10ʱ19�֣�");
														listCurrentStepID.set(0, stepIdStr[i]);
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		//end
		request.setAttribute("listWorkFlowName", listWfName);
		request.setAttribute("listCurrentStepID", listCurrentStepID);
		request.setAttribute("listHistoryStepID", listHistoryStepID);
		request.setAttribute("listStepStatus", listStepStatus);
		request.setAttribute("listStepId", listStepId);
		request.setAttribute("listStepName", listStepName);
		request.setAttribute("listCommitStepId", listCommitStepId);
		request.setAttribute("listHistoryInfo", historyInfo);

		return mapping.findForward("graphics");
	}*/

	private static CommonService getService() {
		return (CommonService) ServiceProvider
				.getService(CommonService.SERVICE_NAME);
	}

}
