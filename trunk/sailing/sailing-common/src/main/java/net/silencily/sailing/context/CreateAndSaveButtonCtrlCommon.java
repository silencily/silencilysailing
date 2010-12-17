package net.silencily.sailing.context;

import javax.servlet.http.HttpSession;

import net.silencily.sailing.security.SecurityContextInfo;
import net.silencily.sailing.security.model.CurrentUser;


/**
 * ����˵��������Ĵ��ڣ���ȫ����Ϊҵ��ͨû�С�
 * 
 * @author wenjb
 * @version 1.0
 */
public class CreateAndSaveButtonCtrlCommon {

	/**
	 * �жϵ�ǰ��ҳ���Ƿ�������������ť ��������
	 * 
	 * @return 2008-1-3 ����04:55:32
	 * @version 1.0
	 * @author wenjb
	 */
	public static boolean disableCreateButton() {

		// ������ť��־
		boolean disableCreateButton = false;
		boolean formInTheWorkFlow = false;
		// �������ж�
		if ("1".equals(getFormInTheWorkFlowForCommonButton())) {
			formInTheWorkFlow = true;
		}

		// ��ȡ����currentUser,Ȩ�޷ſ�
		int pageRWCtrlType = getPageRWCtrlType();

		// ��������
		if (formInTheWorkFlow) {
			// ���ڣ���ֹ
			disableCreateButton = true;
		} else {
			// ����URLĬ�϶�д����Ȩ��
			if (2 != pageRWCtrlType) {
				disableCreateButton = true;
			}
			// ���Ѱ�����URL������
			// ���Ѱ�URL�����ģ������������URL������
			if ("alreadyList".equals(WorkFlowFormContextCommon.getUrlKey())
					|| "system".equals(WorkFlowFormContextCommon.getUrlKey())
					|| "passroundList".equals(WorkFlowFormContextCommon
							.getUrlKey())) {
				disableCreateButton = true;
			}
		}
		return disableCreateButton;

	}

	/**
	 * �жϵ�ǰ��ҳ���Ƿ������ñ��水ť ��������
	 * 
	 * @return 2008-1-3 ����04:55:32
	 * @version 1.0
	 * @author wenjb
	 */
	public static boolean disableSaveButton() {
		// ���水ť��־
		boolean disableSaveButton = false;
		boolean formInTheWorkFlow = false;

		// �������ж�
		if ("1".equals(getFormInTheWorkFlowForCommonButton())) {
			formInTheWorkFlow = true;
		}
		// ��ȡ����currentUser,Ȩ�޷ſ�
		int rowRWCtrlType = getRowRWCtrlType();

		// ��ҵ���Լ����ó����ҳ���������յ�����
		if (BusinessContext.getOperType() == 3) {
			disableSaveButton = true;
		}
		// ���洦��
		if (formInTheWorkFlow) {
			// ���ڣ��ж��ǲ���ȫ�����ɱ༭
			String fieldStatus = WorkFlowFormContextCommon.getFieldStatus();
			if ("2".equals(fieldStatus)) {
				disableSaveButton = true;
			}
		} else {
			// ����URLĬ�϶�д����Ȩ��
			if (2 != rowRWCtrlType) {
				disableSaveButton = true;
			}
			// ���Ѱ�URL�����ģ������������URL������
			if ("alreadyList".equals(WorkFlowFormContextCommon.getUrlKey())
					|| "system".equals(WorkFlowFormContextCommon.getUrlKey())
					|| "passroundList".equals(WorkFlowFormContextCommon
							.getUrlKey())) {
				disableSaveButton = true;
			}
		}
		return disableSaveButton;
	}

	/**
	 * ���������� �жϵ�ǰ��URL��PageRWCtrlType
	 * 
	 * @return 2008-2-27 ����04:55:32
	 * @version 1.0
	 * @author wenjb
	 */
	public static int getPageRWCtrlType() {
		// ��ȡ����currentUser,Ȩ�޷ſ�
		int pageRWCtrlType = 2;
		try {
			// ����URLĬ�϶�д����Ȩ��
			CurrentUser currentUser = SecurityContextInfo.getCurrentUser();
			String url = SecurityContextInfo.getCurrentPageUrl();
			pageRWCtrlType = currentUser.getPageDefaultRWCtrlType(url);
		} catch (Exception e) {
			pageRWCtrlType = 2;
		}
		return pageRWCtrlType;
	}

	/**
	 * ���������� �жϵ�ǰ�������еĵ�ROWRWCtrlType
	 * 
	 * @return 2008-2-27 ����04:55:32
	 * @version 1.0
	 * @author wenjb
	 */
	public static int getRowRWCtrlType() {
		// ��ȡ����currentUser,Ȩ�޷ſ�
		int rowRWCtrlType = 2;
		// �жϵ�ǰҳ�棬����ʲô����Ȩ�޻��ƣ�������һ�£����ǲ��ôӱ������Ȩ��
		String calculateRwCtrlTypeByID = BusinessContext
				.getCalculateRwCtrlTypeByID();
		if ("".equals(calculateRwCtrlTypeByID)) {
			try {
				// ����oidĬ�϶�д����Ȩ��
				String oid = BusinessContext.getUserSetedOid();
				if (null == oid) {
					// ������OID�����������PAGERWCTRLTYPE�ж�
					return getPageRWCtrlType();
				}
				// ��ǰ����SESSION�е�Ȩ����Ϣ
				Integer integer = (Integer) (SecurityContextInfo
						.getRwCtrlTypeMap().get(oid));
				rowRWCtrlType = integer.intValue();
				//������ ,����Ȩ�ޣ���д����Ȩ�ޣ�ֻ���Ļ����������ݵı��治���á�
				int temp = getPageRWCtrlType();
				if(1 == temp){
					rowRWCtrlType = 1;
				}
			} catch (Exception e) {
				rowRWCtrlType = 2;
			}
		}
		return rowRWCtrlType;
	}

	/**
	 * ���������� ���¼������ڻ�������,�����ڹ�ͨ��ť�ͻ�����������һ�µ������
	 * 
	 * @return 2008-2-27 ����04:55:32
	 * @version 1.0
	 * @author wenjb
	 */
	public static String getFormInTheWorkFlowForCommonButton() {
		String formInTheWorkFlow = "2";
		HttpSession session = SecurityContextInfo.getSession();
		if(null==session){
			return "2";
		}else{
			try{
				formInTheWorkFlow = session.getAttribute("formInTheWorkFlowForCommonButton") == null ? null
				: (String) session.getAttribute("formInTheWorkFlowForCommonButton");
			}catch(Exception e){
				//����webLogic������ڵĽ�����ռ������session��Ч����
			}
		}
		return formInTheWorkFlow;
	}

	public static void setFormInTheWorkFlowForCommonButton(
			String formInTheWorkFlowForCommonButton) {
		HttpSession session = SecurityContextInfo.getSession();
		if(null==session){
			return;
		}
		session.setAttribute("formInTheWorkFlowForCommonButton",
				formInTheWorkFlowForCommonButton);
	}

}
