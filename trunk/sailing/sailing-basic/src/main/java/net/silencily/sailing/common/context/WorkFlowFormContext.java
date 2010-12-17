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
	 * 获取当前的SESSION,私有的方法
	 */
	private static HttpSession getHttpSession() {
		return SecurityContextInfo.getSession();
	}

	/**
	 * 功能说明：根据传入的参数，设置相应的FieldStatus
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
	 * 功能说明：标志当前的表单是否是工作流内部的表单
	 * 
	 */
	public static String getTag() {
		// 当前的URL包含TASKID返回"1"，表明在工作流内部,没有的话返回"2"
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
	 * 功能说明：当前工作流步骤对应的id
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
	 * 功能说明：当前工作流步骤对应的所有表单的权限,是否可编辑
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
	 * 功能说明：当前工作流步骤对应的所有表单的权限,是否可入力
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
	 * 通过参数stepName和workFlowName完成WorkflowFormContext的赋值 tag 调用此方法就将此标设为“1”
	 * tblWfParInfID permissionList 通过workFlowName查询得到
	 */
	public static void loadFormPermission(String workFlowName, String stepName){
		//如果当前的工作流不存在，或者查不到的话，就无法进行工作流权限的加载
		try{
            //载入权限前，清除以前关于工作流的权限
			clearFormPermission();
			// 得到WorkflowFormContext需要的数据；
			// 调用此方法，标志此FORM，走工作流内部
			String tag = new String("1");
			// 定义缓冲变量
			String tblWfParInfID = new String("");
			String tblWfParInfFieldStatus = new String("");
			Map permissionMap = new HashMap(0);
			Map permissionIsNeddMap = new HashMap(0);
			TblWfOperationInfo tblWfOperationInfo = null;
			Set tblWfParticularInfos = null;
			TblWfParticularInfo tblWfParticularInfo = null;
			TblWfFormInfo tblWfFormInfo = null;

			// 查询数据库，得到编辑状态列表
			HibernateTemplate hibernateTemplate = (HibernateTemplate) ServiceProvider
					.getService("common.hibernateTemplate");
			// 第一次查询，查询得到TblWfOperationInfo的一个实例
			ContextInfo.concealQuery();
			DetachedCriteria dc = DetachedCriteria
					.forClass(TblWfOperationInfo.class);
			dc.add(Restrictions.eq("name", workFlowName));
			dc.add(Restrictions.eq("delFlg", "0"));
			dc.addOrder(Order.desc("createdTime"));
			dc.add(Restrictions.eq("status", new CodeWrapperPlus("0")));
			// 此处存在风险，如果新增的数据，没有启用，那么查出来的数据就错误了
			List list = hibernateTemplate.findByCriteria(dc);
			tblWfOperationInfo = (TblWfOperationInfo) list.get(0);
			// 循环读取TblWfOperationInfo中的TblWfParticularInfo的信息，得到符合要求的步骤信息
			tblWfParticularInfos = tblWfOperationInfo.getTblWfParticularInfos();
			for (int i = 0; i < tblWfParticularInfos.size(); i++) {
				tblWfParticularInfo = (TblWfParticularInfo) (tblWfParticularInfos
						.toArray()[i]);
				// 此处有风险，步骤名称不唯一
				if (stepName.equals(tblWfParticularInfo.getStepName())) {
					break;
				}
			}
			// 根据当前STEPID，得到相关的权限信息
			tblWfParInfID = tblWfParticularInfo.getId();
			tblWfParInfFieldStatus = tblWfParticularInfo.getFieldStatus();

			// 根据查询得到的tblWfParInfFieldStatus,决定是否去查询相关权限设置
			if ("3".equals(tblWfParInfFieldStatus)) {
				// 第二次查询
				ContextInfo.concealQuery();
				DetachedCriteria dc2 = DetachedCriteria
						.forClass(TblWfFormInfo.class);
				// 查询条件
				dc2.add(Restrictions.eq("tblWfParticularInfo.id", tblWfParInfID));
				// TblWfFormInfo表的数据，物理删除
				// dc2.add(Restrictions.eq("delFlg", "0"));
				List tblWfFormInfos = hibernateTemplate.findByCriteria(dc2);
				for (int i = 0; i < tblWfFormInfos.size(); i++) {
					tblWfFormInfo = (TblWfFormInfo) (tblWfFormInfos.toArray()[i]);
					// 将FIELD_CODE当做key,存储相关权限
					permissionMap.put(tblWfFormInfo.getFieldCode(), "Y");
					// 将必须入力项目添加到permissionIsNeddMap中间
					if ("Y".equals(tblWfFormInfo.getEditorFlg())) {
						permissionIsNeddMap.put(tblWfFormInfo.getFieldCode(),
								tblWfFormInfo.getEditorFlg());
					}

				}

			}
			// 对WorkflowFormContext进行设值
			WorkFlowFormContext.setTblWfParInfID(tblWfParInfID);
			WorkFlowFormContext.setTag(tag);
			WorkFlowFormContext.setFieldStatus(tblWfParInfFieldStatus);
			WorkFlowFormContext.setPermissionMap(permissionMap);
			WorkFlowFormContext.setPermissionIsNeedMap(permissionIsNeddMap);
		}catch(IndexOutOfBoundsException e){
			String errorMsg = MessageInfo.factory().getMessage("WF_I001_P_0");
			//String errorMsg = "当前的工作流数据存在问题，请与流程管理员联系谢谢！";
			throw new RuntimeException(errorMsg);
		}
	}

	/*
	 * 功能说明：清除WorkflowFormContext中的值
	 */
	public static void clearFormPermission() {
		// 对WorkflowFormContext进行设值
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
	 * 功能说明：完成消息的群发功能 @parm signList#List
	 */
	public static void workFlowSendMessage(String workFlowName,
			String stepName, String url) {
		// 封装消息
		String sendUserId = null;
		String receiveUserId = null;
		String title = null;
		String messageContent = null;
		String urlTemp = null;

		// 定义缓冲变量
		String roleID = null;
		List tblCmnUsers = null;
		TblWfOperationInfo tblWfOperationInfo = null;
		Set tblWfParticularInfos = null;
		TblWfParticularInfo tblWfParticularInfo = null;
		// 查询数据库，得到TblWfParticularInfo对应的ROLEID
		HibernateTemplate hibernateTemplate = (HibernateTemplate) ServiceProvider
				.getService("common.hibernateTemplate");
		// 第一次查询，查询得到TblWfOperationInfo的一个实例
		ContextInfo.concealQuery();
		DetachedCriteria dc = DetachedCriteria
				.forClass(TblWfOperationInfo.class);
		dc.add(Restrictions.eq("name", workFlowName));
		dc.add(Restrictions.eq("delFlg", "0"));
		dc.addOrder(Order.desc("createdTime"));
		dc.add(Restrictions.eq("status", new CodeWrapperPlus("0")));
		List list = hibernateTemplate.findByCriteria(dc);
		// 此处存在风险，当查询结果有多个的时候，默认取第一个
		tblWfOperationInfo = (TblWfOperationInfo) list.get(0);
		// 循环读取TblWfOperationInfo中的TblWfParticularInfo的信息，得到符合要求的步骤信息
		tblWfParticularInfos = tblWfOperationInfo.getTblWfParticularInfos();
		for (int i = 0; i < tblWfParticularInfos.size(); i++) {
			tblWfParticularInfo = (TblWfParticularInfo) (tblWfParticularInfos
					.toArray()[i]);
			if (stepName.equals(tblWfParticularInfo.getStepName())) {
				break;
			}
		}
		// 第二次查询
		roleID = tblWfParticularInfo.getTblUserId();
		// 根据角色ID，查询得到对应的用户id，的列表
		// 查询表TblCmnUserRole，得到TblCmnUser列表
		ContextInfo.concealQuery();
		DetachedCriteria dc2 = DetachedCriteria.forClass(TblCmnUserRole.class);
		dc2.add(Restrictions.eq("tblCmnRole.id", roleID));
		dc2.add(Restrictions.eq("delFlg", "0"));
		tblCmnUsers = hibernateTemplate.findByCriteria(dc2);

		// receiveUserIds 根据roleID查询得到。

		// 初始化消息内容
		//sendUserId = SecurityContextInfo.getCurrentUser().getUserId();
		title = "流程任务信息";
		messageContent = "您好，您有新的流程任务等待处理";
		urlTemp = url;
		MessageBean msg = new MessageBean();
		// 循环发送消息
		for (int i = 0; i < tblCmnUsers.size(); i++) {
			receiveUserId = ((TblCmnUser) tblCmnUsers.toArray()[i]).getId();
			msg.setReceiveUserId(receiveUserId);
			//msg.setSendUserId(sendUserId);
			msg.setSendUserId(null);
			msg.setTitle(title);
			msg.setMessageContent(messageContent);
			msg.setUrl(urlTemp);
			msg.setMessageId((String)ThreadAttribute.TaskId.get());
			
			// 发送消息
			try {
				MessageInsert.createMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("消息发送失败！");
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
		// 根据角色ID，查询得到对应的用户id，的列表
		// 查询表TblCmnUserRole，得到TblCmnUser列表
		if (roles != null) {
			for (int i = 0; i < roles.length; i++) {
				String roleCd = roles[i];
				// receiveUserIds 根据roleID查询得到。
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
		// 根据角色ID，查询得到对应的用户id，的列表
		// 查询表TblCmnUserRole，得到TblCmnUser列表
		if (roles != null) {
			for (int i = 0; i < roles.length; i++) {
				String roleCd = roles[i];
				// receiveUserIds 根据roleID查询得到。
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

		// 初始化消息内容
		//String sendUserId = SecurityContextInfo.getCurrentUser().getUserId();
		String sendUserId = null;
		String receiveUserId = null;
		String title = "流程任务信息";
		
		String messageContent = "您好，您有新的流程任务等待处理 : ";
			
		try {
			/*messageContent += WorkflowTaskTools.getTaskName(info, 
					WfCommonTools.getWorkflowCnName(info.getWorkflowName()));*/	//08-07-08 by liuzhuo
			messageContent += info.getTaskName(title);
		} catch (Exception e) {
			System.out.println("生成消息内容时有异常 : " + e.getMessage());
			messageContent += "请及时查收。";
		}
		String urlTemp = info.getWorkflowEditPath(info.getId()) + "&taskId=" + ((String)ThreadAttribute.TaskId.get());

		if (isRoleSign) {
			MessageBean msg = new MessageBean();
			// 循环发送消息
			for (int i = 0; i < tblCmnUsers.size(); i++) {
				receiveUserId = ((TblCmnUser) tblCmnUsers.toArray()[i]).getId();
				msg.setReceiveUserId(receiveUserId);
				msg.setSendUserId(sendUserId);
				msg.setTitle(title);
				msg.setMessageContent(messageContent);
				msg.setUrl(Tools.getWFSecurityURL(info.getWorkflowName(), info.getCurrentStep().getName(), urlTemp));
				msg.setMessageId((String)ThreadAttribute.TaskId.get());
				// 发送消息
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
				// 发送消息
				try {
					MessageInsert.createMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 功能说明：标志当前的URL是否是从已办任务还是从待办任务来的
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
