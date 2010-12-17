package net.silencily.sailing.common.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.basic.sm.domain.TblCmnUserRole;
import net.silencily.sailing.basic.wf.WorkflowInfo;
import net.silencily.sailing.basic.wf.domain.TblWfFormInfo;
import net.silencily.sailing.basic.wf.domain.TblWfOperationInfo;
import net.silencily.sailing.basic.wf.domain.TblWfParticularInfo;
import net.silencily.sailing.basic.wf.dto.FlowTaskInfo;
import net.silencily.sailing.basic.wf.thread.ThreadAttribute;
import net.silencily.sailing.basic.wf.util.WfUtils;
import net.silencily.sailing.common.message.MessageBean;
import net.silencily.sailing.common.message.MessageInsert;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.context.BusinessContext;
import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.hibernate3.CodeWrapperPlus;
import net.silencily.sailing.security.SecurityContextInfo;
import net.silencily.sailing.utils.MessageInfo;
import net.silencily.sailing.utils.Tools;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;


/**
 * 
 * @author wenjb
 * @version 1.0
 */
public class WorkFlowFormContext extends BusinessContext {

	/**
	 * ��ȡ��ǰ��SESSION,˽�еķ���
	 */
	private static HttpSession getHttpSession() {
		return SecurityContextInfo.getSession();
	}

	/**
	 * ����˵�������ݴ���Ĳ�����������Ӧ��FieldStatus
	 */
	public static String getFieldStatus() {
		return (String) getHttpSession().getAttribute("fieldStatus");
	}

	public static void setFieldStatus(String fieldStatus) {
		HttpSession session = getHttpSession();
		if (null == session.getAttribute("fieldStatus")) {
			session.setAttribute("fieldStatus", fieldStatus);
		} else {
			session.removeAttribute("fieldStatus");
			session.setAttribute("fieldStatus", fieldStatus);
		}
	}

	/**
	 * ����˵������־��ǰ�ı��Ƿ��ǹ������ڲ��ı�
	 * 
	 */
	public static String getTag() {
		// ��ǰ��URL����TASKID����"1"�������ڹ������ڲ�,û�еĻ�����"2"
		String tag = null;
		try {
			tag = (String) getHttpSession().getAttribute("tag");
		} catch (Exception e) {
			tag = "2";
		}
		return tag;
	}

	public static void setTag(String tag) {
		HttpSession session = getHttpSession();
		if (null == session.getAttribute("tag")) {
			session.setAttribute("tag", tag);
		} else {
			session.removeAttribute("tag");
			session.setAttribute("tag", tag);
		}
	}

	/**
	 * ����˵������ǰ�����������Ӧ��id
	 */
	public static String getTblWfParInfID() {
		return (String) getHttpSession().getAttribute("tblWfParInfID");
	}

	public static void setTblWfParInfID(String tblWfParInfID) {
		HttpSession session = getHttpSession();
		if (null == session.getAttribute("tblWfParInfID")) {
			session.setAttribute("tblWfParInfID", tblWfParInfID);
		} else {
			session.removeAttribute("tblWfParInfID");
			session.setAttribute("tblWfParInfID", tblWfParInfID);
		}
	}

	/**
	 * ����˵������ǰ�����������Ӧ�����б���Ȩ��,�Ƿ�ɱ༭
	 */
	public static Map getPermissionMap() {
		return (Map) getHttpSession().getAttribute("permissionList");
	}

	public static void setPermissionMap(Map permissionList) {
		HttpSession session = getHttpSession();
		if (null == session.getAttribute("permissionList")) {
			session.setAttribute("permissionList", permissionList);
		} else {
			session.removeAttribute("permissionList");
			session.setAttribute("permissionList", permissionList);
		}
	}

	/**
	 * ����˵������ǰ�����������Ӧ�����б���Ȩ��,�Ƿ������
	 */
	public static Map getPermissionIsNeedMap() {
		return (Map) getHttpSession().getAttribute("permissionIsNeddMap");
	}

	public static void setPermissionIsNeedMap(Map permissionIsNeddMap) {
		HttpSession session = getHttpSession();
		if (null == session.getAttribute("permissionIsNeddMap")) {
			session.setAttribute("permissionIsNeddMap", permissionIsNeddMap);
		} else {
			session.removeAttribute("permissionIsNeddMap");
			session.setAttribute("permissionIsNeddMap", permissionIsNeddMap);
		}
	}

	/*
	 * ͨ������stepName��workFlowName���WorkflowFormContext�ĸ�ֵ tag ���ô˷����ͽ��˱���Ϊ��1��
	 * tblWfParInfID permissionList ͨ��workFlowName��ѯ�õ�
	 */
	public static void loadFormPermission(String workFlowName, String stepName){
		//�����ǰ�Ĺ����������ڣ����߲鲻���Ļ������޷����й�����Ȩ�޵ļ���
		try{
            //����Ȩ��ǰ�������ǰ���ڹ�������Ȩ��
			clearFormPermission();
			// �õ�WorkflowFormContext��Ҫ�����ݣ�
			// ���ô˷�������־��FORM���߹������ڲ�
			String tag = new String("1");
			// ���建�����
			String tblWfParInfID = new String("");
			String tblWfParInfFieldStatus = new String("");
			Map permissionMap = new HashMap(0);
			Map permissionIsNeddMap = new HashMap(0);
			TblWfOperationInfo tblWfOperationInfo = null;
			Set tblWfParticularInfos = null;
			TblWfParticularInfo tblWfParticularInfo = null;
			TblWfFormInfo tblWfFormInfo = null;

			// ��ѯ���ݿ⣬�õ��༭״̬�б�
			HibernateTemplate hibernateTemplate = (HibernateTemplate) ServiceProvider
					.getService("common.hibernateTemplate");
			// ��һ�β�ѯ����ѯ�õ�TblWfOperationInfo��һ��ʵ��
			ContextInfo.concealQuery();
			DetachedCriteria dc = DetachedCriteria
					.forClass(TblWfOperationInfo.class);
			dc.add(Restrictions.eq("name", workFlowName));
			dc.add(Restrictions.eq("delFlg", "0"));
			dc.addOrder(Order.desc("createdTime"));
			dc.add(Restrictions.eq("status", new CodeWrapperPlus("0")));
			// �˴����ڷ��գ�������������ݣ�û�����ã���ô����������ݾʹ�����
			List list = hibernateTemplate.findByCriteria(dc);
			tblWfOperationInfo = (TblWfOperationInfo) list.get(0);
			// ѭ����ȡTblWfOperationInfo�е�TblWfParticularInfo����Ϣ���õ�����Ҫ��Ĳ�����Ϣ
			tblWfParticularInfos = tblWfOperationInfo.getTblWfParticularInfos();
			for (int i = 0; i < tblWfParticularInfos.size(); i++) {
				tblWfParticularInfo = (TblWfParticularInfo) (tblWfParticularInfos
						.toArray()[i]);
				// �˴��з��գ��������Ʋ�Ψһ
				if (stepName.equals(tblWfParticularInfo.getStepName())) {
					break;
				}
			}
			// ���ݵ�ǰSTEPID���õ���ص�Ȩ����Ϣ
			tblWfParInfID = tblWfParticularInfo.getId();
			tblWfParInfFieldStatus = tblWfParticularInfo.getFieldStatus();

			// ���ݲ�ѯ�õ���tblWfParInfFieldStatus,�����Ƿ�ȥ��ѯ���Ȩ������
			if ("3".equals(tblWfParInfFieldStatus)) {
				// �ڶ��β�ѯ
				ContextInfo.concealQuery();
				DetachedCriteria dc2 = DetachedCriteria
						.forClass(TblWfFormInfo.class);
				// ��ѯ����
				dc2.add(Restrictions.eq("tblWfParticularInfo.id", tblWfParInfID));
				// TblWfFormInfo������ݣ�����ɾ��
				// dc2.add(Restrictions.eq("delFlg", "0"));
				List tblWfFormInfos = hibernateTemplate.findByCriteria(dc2);
				for (int i = 0; i < tblWfFormInfos.size(); i++) {
					tblWfFormInfo = (TblWfFormInfo) (tblWfFormInfos.toArray()[i]);
					// ��FIELD_CODE����key,�洢���Ȩ��
					permissionMap.put(tblWfFormInfo.getFieldCode(), "Y");
					// ������������Ŀ��ӵ�permissionIsNeddMap�м�
					if ("Y".equals(tblWfFormInfo.getEditorFlg())) {
						permissionIsNeddMap.put(tblWfFormInfo.getFieldCode(),
								tblWfFormInfo.getEditorFlg());
					}

				}

			}
			// ��WorkflowFormContext������ֵ
			WorkFlowFormContext.setTblWfParInfID(tblWfParInfID);
			WorkFlowFormContext.setTag(tag);
			WorkFlowFormContext.setFieldStatus(tblWfParInfFieldStatus);
			WorkFlowFormContext.setPermissionMap(permissionMap);
			WorkFlowFormContext.setPermissionIsNeedMap(permissionIsNeddMap);
		}catch(IndexOutOfBoundsException e){
			String errorMsg = MessageInfo.factory().getMessage("WF_I001_P_0");
			//String errorMsg = "��ǰ�Ĺ��������ݴ������⣬�������̹���Ա��ϵлл��";
			throw new RuntimeException(errorMsg);
		}
	}

	/*
	 * ����˵�������WorkflowFormContext�е�ֵ
	 */
	public static void clearFormPermission() {
		// ��WorkflowFormContext������ֵ
		if (null != getHttpSession().getAttribute("fieldStatus")) {
			getHttpSession().removeAttribute("fieldStatus");
		}
		if (null != getHttpSession().getAttribute("tag")) {
			getHttpSession().removeAttribute("tag");
		}
		if (null != getHttpSession().getAttribute("tblWfParInfID")) {
			getHttpSession().removeAttribute("tblWfParInfID");
		}
		if (null != getHttpSession().getAttribute("permissionList")) {
			getHttpSession().removeAttribute("permissionList");
		}
		if (null != getHttpSession().getAttribute("permissionIsNeddMap")) {
			getHttpSession().removeAttribute("permissionIsNeddMap");
		}
	}

	/*
	 * ����˵���������Ϣ��Ⱥ������ @parm signList#List
	 */
	public static void workFlowSendMessage(String workFlowName,
			String stepName, String url) {
		// ��װ��Ϣ
		String sendUserId = null;
		String receiveUserId = null;
		String title = null;
		String messageContent = null;
		String urlTemp = null;

		// ���建�����
		String roleID = null;
		List tblCmnUsers = null;
		TblWfOperationInfo tblWfOperationInfo = null;
		Set tblWfParticularInfos = null;
		TblWfParticularInfo tblWfParticularInfo = null;
		// ��ѯ���ݿ⣬�õ�TblWfParticularInfo��Ӧ��ROLEID
		HibernateTemplate hibernateTemplate = (HibernateTemplate) ServiceProvider
				.getService("common.hibernateTemplate");
		// ��һ�β�ѯ����ѯ�õ�TblWfOperationInfo��һ��ʵ��
		ContextInfo.concealQuery();
		DetachedCriteria dc = DetachedCriteria
				.forClass(TblWfOperationInfo.class);
		dc.add(Restrictions.eq("name", workFlowName));
		dc.add(Restrictions.eq("delFlg", "0"));
		dc.addOrder(Order.desc("createdTime"));
		dc.add(Restrictions.eq("status", new CodeWrapperPlus("0")));
		List list = hibernateTemplate.findByCriteria(dc);
		// �˴����ڷ��գ�����ѯ����ж����ʱ��Ĭ��ȡ��һ��
		tblWfOperationInfo = (TblWfOperationInfo) list.get(0);
		// ѭ����ȡTblWfOperationInfo�е�TblWfParticularInfo����Ϣ���õ�����Ҫ��Ĳ�����Ϣ
		tblWfParticularInfos = tblWfOperationInfo.getTblWfParticularInfos();
		for (int i = 0; i < tblWfParticularInfos.size(); i++) {
			tblWfParticularInfo = (TblWfParticularInfo) (tblWfParticularInfos
					.toArray()[i]);
			if (stepName.equals(tblWfParticularInfo.getStepName())) {
				break;
			}
		}
		// �ڶ��β�ѯ
		roleID = tblWfParticularInfo.getTblUserId();
		// ���ݽ�ɫID����ѯ�õ���Ӧ���û�id�����б�
		// ��ѯ��TblCmnUserRole���õ�TblCmnUser�б�
		ContextInfo.concealQuery();
		DetachedCriteria dc2 = DetachedCriteria.forClass(TblCmnUserRole.class);
		dc2.add(Restrictions.eq("tblCmnRole.id", roleID));
		dc2.add(Restrictions.eq("delFlg", "0"));
		tblCmnUsers = hibernateTemplate.findByCriteria(dc2);

		// receiveUserIds ����roleID��ѯ�õ���

		// ��ʼ����Ϣ����
		//sendUserId = SecurityContextInfo.getCurrentUser().getUserId();
		title = "����������Ϣ";
		messageContent = "���ã������µ���������ȴ�����";
		urlTemp = url;
		MessageBean msg = new MessageBean();
		// ѭ��������Ϣ
		for (int i = 0; i < tblCmnUsers.size(); i++) {
			receiveUserId = ((TblCmnUser) tblCmnUsers.toArray()[i]).getId();
			msg.setReceiveUserId(receiveUserId);
			//msg.setSendUserId(sendUserId);
			msg.setSendUserId(null);
			msg.setTitle(title);
			msg.setMessageContent(messageContent);
			msg.setUrl(urlTemp);
			msg.setMessageId((String)ThreadAttribute.TaskId.get());
			
			// ������Ϣ
			try {
				MessageInsert.createMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("��Ϣ����ʧ�ܣ�");
			}
		}
	}

	/**
	 * Get emp of roles.
	 * 
	 * @parm roles
	 */
	public static List getEmpOfRole(String rolez) {
		HibernateTemplate hibernateTemplate = (HibernateTemplate) ServiceProvider
				.getService("common.hibernateTemplate");
		String[] roles = rolez == null ? null : rolez.split(",");
		List resultList = new ArrayList();
		List tblCmnUsers = new ArrayList();
		// ���ݽ�ɫID����ѯ�õ���Ӧ���û�id�����б�
		// ��ѯ��TblCmnUserRole���õ�TblCmnUser�б�
		if (roles != null) {
			for (int i = 0; i < roles.length; i++) {
				String roleCd = roles[i];
				// receiveUserIds ����roleID��ѯ�õ���
				ContextInfo.recoverQuery();
				DetachedCriteria dc2 = DetachedCriteria
						.forClass(TblCmnUserRole.class);
				dc2.createAlias("tblCmnRole", "tblCmnRole");
				dc2.add(Restrictions.eq("tblCmnRole.roleCd", roleCd));
				dc2.add(Restrictions.eq("delFlg", "0"));
				List l = hibernateTemplate.findByCriteria(dc2);
				java.util.Iterator it = l.iterator();
				while (it.hasNext()) {
					TblCmnUserRole ur = (TblCmnUserRole) it.next();
					tblCmnUsers.add(ur.getTblCmnUser());
				}
			}
		}
		for (int i = 0; i < tblCmnUsers.size(); i++) {
			FlowTaskInfo fti = new FlowTaskInfo();
			String empCd = ((TblCmnUser) tblCmnUsers.get(i))
					.getEmpCd();
			fti.setProcessId(empCd);
			resultList.add(fti);
		}

		return resultList;
	}

	/**
	 * Send mail when workflow step over.
	 * 
	 * @parm info#WorkflowInfo
	 */
	public static void workFlowSendMessage(WorkflowInfo info) {
		if (info == null) {
			return;
		}
		List list = info.getSignerList();
		boolean isRoleSign = false;
		String[] roles = null;
		if (list != null && list.size() > 0) {
			FlowTaskInfo fti = (FlowTaskInfo) list.get(0);
			if ("Y".equals(fti.getRoleSignFlag())) {
				isRoleSign = true;
				if (StringUtils.isNotBlank(fti.getRoleSignRole())) {
					roles = fti.getRoleSignRole().split(",");
				}
			}
		}

		HibernateTemplate hibernateTemplate = (HibernateTemplate) ServiceProvider
				.getService("common.hibernateTemplate");

		List tblCmnUsers = new ArrayList();
		// ���ݽ�ɫID����ѯ�õ���Ӧ���û�id�����б�
		// ��ѯ��TblCmnUserRole���õ�TblCmnUser�б�
		if (roles != null) {
			for (int i = 0; i < roles.length; i++) {
				String roleCd = roles[i];
				// receiveUserIds ����roleID��ѯ�õ���
				ContextInfo.recoverQuery();
				DetachedCriteria dc2 = DetachedCriteria
						.forClass(TblCmnUserRole.class);
				dc2.createAlias("tblCmnRole", "tblCmnRole");
				dc2.add(Restrictions.eq("tblCmnRole.roleCd", roleCd));
				dc2.add(Restrictions.eq("delFlg", "0"));
				List l = hibernateTemplate.findByCriteria(dc2);
				java.util.Iterator it = l.iterator();
				while (it.hasNext()) {
					TblCmnUserRole ur = (TblCmnUserRole) it.next();
					tblCmnUsers.add(ur.getTblCmnUser());
				}
			}
		}

		// ��ʼ����Ϣ����
		//String sendUserId = SecurityContextInfo.getCurrentUser().getUserId();
		String sendUserId = null;
		String receiveUserId = null;
		String title = "����������Ϣ";
		
		String messageContent = "���ã������µ���������ȴ����� : ";
			
		try {
			/*messageContent += WorkflowTaskTools.getTaskName(info, 
					WfCommonTools.getWorkflowCnName(info.getWorkflowName()));*/	//08-07-08 by liuzhuo
			messageContent += info.getTaskName(title);
		} catch (Exception e) {
			System.out.println("������Ϣ����ʱ���쳣 : " + e.getMessage());
			messageContent += "�뼰ʱ���ա�";
		}
		String urlTemp = info.getWorkflowEditPath(info.getId()) + "&taskId=" + ((String)ThreadAttribute.TaskId.get());

		if (isRoleSign) {
			MessageBean msg = new MessageBean();
			// ѭ��������Ϣ
			for (int i = 0; i < tblCmnUsers.size(); i++) {
				receiveUserId = ((TblCmnUser) tblCmnUsers.toArray()[i]).getId();
				msg.setReceiveUserId(receiveUserId);
				msg.setSendUserId(sendUserId);
				msg.setTitle(title);
				msg.setMessageContent(messageContent);
				msg.setUrl(Tools.getWFSecurityURL(info.getWorkflowName(), info.getCurrentStep().getName(), urlTemp));
				msg.setMessageId((String)ThreadAttribute.TaskId.get());
				// ������Ϣ
				try {
					MessageInsert.createMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			for (int i = 0; i < list.size(); i++) {
				MessageBean msg = new MessageBean();
				FlowTaskInfo fti = (FlowTaskInfo) list.get(i);
				msg.setReceiveUserId(WfUtils.getEmpId(fti.getProcessId()));
				msg.setSendUserId(sendUserId);
				msg.setTitle(title);
				msg.setMessageContent(messageContent);
				msg.setUrl(Tools.getWFSecurityURL(info.getWorkflowName(), info.getCurrentStep().getName(), urlTemp));
				msg.setMessageId((String)ThreadAttribute.TaskId.get());
				// ������Ϣ
				try {
					MessageInsert.createMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * ����˵������־��ǰ��URL�Ƿ��Ǵ��Ѱ������ǴӴ�����������
	 * 
	 */
	public static String getUrlKey() {
		String urlKey = "";
		try {
			urlKey = (String) getHttpSession().getAttribute("urlKey");
		} catch (Exception e) {
			urlKey = "";
		}
		return urlKey;
	}

	public static void setUrlKey(String urlKey) {
		HttpSession session = getHttpSession();
		if (null == session.getAttribute("urlKey")) {
			session.setAttribute("urlKey", urlKey);
		} else {
			session.removeAttribute("urlKey");
			session.setAttribute("urlKey", urlKey);
		}
	}

}
