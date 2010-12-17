/**
 * Name: WfCommonTools.java
 * Author: Bis liyan
 */
package net.silencily.sailing.basic.wf.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.basic.sm.domain.TblCmnUserRole;
import net.silencily.sailing.basic.sm.user.service.UserManageService;
import net.silencily.sailing.basic.wf.WorkflowInfo;
import net.silencily.sailing.basic.wf.constant.WfUnionCodeConstants;
import net.silencily.sailing.basic.wf.db.WfDBConnection;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.security.SecurityContextInfo;
import net.silencily.sailing.security.model.CurrentUser;
import net.silencily.sailing.utils.Tools;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;


/**
 * The common tools for workflow 工作流共同功能工具
 * 
 * @author Bis liyan
 */
public class WfCommonTools {	
	/**
	 * Get workflow's Chinese name 获得流程的中文名称
	 * 
	 * @param workflowName
	 *            workflow's English name
	 * @throws Exception
	 * @return workflowCnName
	 */
	public static String getWorkflowCnName(String workflowName)
			throws Exception {
		String workflowCnName = "";
		String sql = "SELECT NAME AS ENGLISH_NAME,OPER_NAME AS CHINESE_NAME,STATUS,DEL_FLG "
				+ "FROM TBL_WF_OPERATION_INFO GROUP BY NAME,OPER_NAME,STATUS,DEL_FLG "
				+ "HAVING STATUS = 0 AND DEL_FLG = 0 AND NAME = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {			
			conn = WfDBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, workflowName);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				workflowCnName = rs.getString("CHINESE_NAME");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("get workflow's English name");
		} finally {
			if (rs != null)
				rs.close();

			if (pstmt != null)
				pstmt.close();

			if (conn != null)
				conn.close();
		}
		return workflowCnName;
	}

	/**
	 * 判断当前登录人是否有某个步骤上的角色权限
	 * 
	 * @param currentUser
	 *            当前登录人
	 * @param workflowName
	 *            workflow's English name
	 * @param stepId
	 *            step's id
	 * @throws Exception
	 * @return isHas
	 */
	public static boolean isHasPopedom(CurrentUser currUser,
			String workflowName, String stepId) throws Exception {
		boolean isHas = false;
		String[] roles = WfUtils.getRoles(workflowName, stepId).split(",");
		for (int i = 0; i < roles.length; i++) {
			if (currUser.hasRoleCd(roles[i])) {
				isHas = true;
			}
		}
		return isHas;
	}

	/**
	 * 在流程初始化时，发送一个已办任务
	 * 
	 * @param workflowInfo
	 * @throws Exception
	 */
	public static void sendFirstTask(WorkflowInfo info) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;

		String sql = "INSERT INTO TBL_WF_USERSIGN "
				+ "(ID,PROCESSED,PROCESS_ID,PROCESS_USERNAME,PROCESS_ROLE,COMMIT_USERNAME,"
				+ "COMMIT_ROLE,COMMIT_ID,PROCESS_DEPT,COMMIT_DEPT,OID,WORKFLOW_NAME,CURR_STEP_ID,"
				+ "CURR_STEP_NAME,TO_STEP_ID,TO_STEP_NAME,ROLE_SIGN_FLAG,ROLE_SIGN_ROLE,ROLE_SIGN_DEPT,"
				+ "CONSIGN_FLAG,CONSIGNED_FLAG,CONSIGNER,CONSIGNEDER,CONSIGNER_ROLE,CONSIGNEDER_ROLE,"
				+ "CLASS_NAME,TASK_ID,FROM_TASK_ID,TASK_TYPE,TASK_NAME) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		try {
			conn = WfDBConnection.getConnection();
			String taskId = Tools.getPKCode();
			info.setTaskId(taskId);
			String taskType = "A";
			
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, Tools.getPKCode());
			pstmt.setString(2, "Y");
			pstmt.setString(3, SecurityContextInfo.getCurrentUser().getEmpCd());
			pstmt.setString(4, SecurityContextInfo.getCurrentUser().getUserName());
			pstmt.setString(5, "");
			pstmt.setString(6, SecurityContextInfo.getCurrentUser().getUserName());

			pstmt.setString(7, "");
			pstmt.setString(8, SecurityContextInfo
					.getCurrentUser().getEmpCd());
			pstmt.setString(9, "");
			pstmt.setString(10, "");
			pstmt.setString(11, info.getId());
			pstmt.setString(12, info.getWorkflowName());

			pstmt.setString(13, "0");
			pstmt.setString(14, "初始化");
			pstmt.setString(15, "1");
			pstmt.setString(16, info.getCurrentStep().getName());
			
			pstmt.setString(17, "N");
			pstmt.setString(18, "");
			pstmt.setString(19, "");

			pstmt.setString(20, "N");
			pstmt.setString(21, "N");
			pstmt.setString(22, "");
			pstmt.setString(23, "");
			pstmt.setString(24, "");
			pstmt.setString(25, "");

			String className = info.getClass().getName();
			int x = className.indexOf(("$"));
			if (-1 != x) {
				className = className.substring(0, x);
			}
			pstmt.setString(26, className);
			pstmt.setString(27, taskId);
			pstmt.setString(28, "");
			
			pstmt.setString(29, taskType);

			String taskName = getWorkflowCnName(info.getWorkflowName());
//			if (StringUtils.isNotBlank(info.getCurrentStep().getName())) {
//				taskName += ("_" + info.getCurrentStep().getName());
//			} 
			pstmt.setString(30, taskName);

			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("send first task");
		} finally {
			if (pstmt != null)
				pstmt.close();

			if (conn != null)
				conn.close();
		}

	}

	/**
	 * 根据业务过滤操作
	 * 
	 * @param taskType
	 * @param operateName
	 * @return flg
	 */
	public static boolean operateFilter(String taskType, String operateName) {
		boolean flg = true;
		boolean isRetake = false;
		
		if (StringUtils.isNotBlank(operateName)) {
			if (operateName.indexOf(WfUnionCodeConstants.RETAKE) != -1) {
				isRetake = true;
			} else {
				isRetake = false;
			}
		}
		if ("waitList".equals(taskType)) {
			flg = !isRetake;
		} else if ("alreadyList".equals(taskType)) {
			flg = isRetake;
		}                                                                                                                                                                        
		//2008-7-16 yangxl 在代办任务中可以操作取消(注释后) 
		/*if (WfUnionCodeConstants.KILL.equals(operateName)) {
			if ("waitList".equals(taskType) || "alreadyList".equals(taskType)) {
				return false;
			} else {
				return true;
			}
		}*/
		return flg;
	}
	/**
	 * 得到流程发起者
	 * 
	 * @param workflowInfo
	 * @return empCd
	 */
	public static String findWorkflowSponsor(WorkflowInfo info) throws Exception {
		String empCd = "";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT PROCESS_ID AS SPONSOR FROM TBL_WF_USERSIGN WHERE FROM_TASK_ID IS NULL" +
				" AND CURR_STEP_ID = '0' AND OID = ?";
		try {
			conn = WfDBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, info.getId());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				empCd = rs.getString("SPONSOR");
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw new Exception("find workflow sponsor");
		} finally {
			if (rs != null)
				rs.close();
			
			if (pstmt != null)
				pstmt.close();
			
			if (conn != null)
				conn.close();
		}
		return empCd;
	}
	public static List castEmpsByCds(Set cds) {
		if (cds == null)
			return new ArrayList();
		List list = new ArrayList();
		Iterator it = cds.iterator();
		while (it.hasNext()) {
			String empCd = (String)it.next();
			TblCmnUser user = ((UserManageService) ServiceProvider
					.getService(UserManageService.SERVICE_NAME)).getUser(empCd);
			if (user != null) {
				list.add(user);
			}
		}
		return list;
	}
	public static List getPointedUserOfStepWithoutTaskId(String workflowName, String currStepId, String oid) throws Exception{
		if (workflowName == null || currStepId == null)
			throw new Exception("get pointed user of step with out task id : workflowName or current step id is null");
		List resultList = new ArrayList();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM TBL_WF_USERSIGN WHERE OID = ? AND WORKFLOW_NAME = ? AND TO_STEP_ID = ? ORDER BY COMMIT_TIME DESC";
		try {
			conn = WfDBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, oid);
			pstmt.setString(2, workflowName);
			pstmt.setString(3, currStepId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				String taskId = rs.getString("TASK_ID");
				resultList = castEmpsByCds(WfUtils.getPointedUserOfStep(taskId));
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw new Exception("get pointed user of step without task id", e);
		} finally {
			if (rs != null)
				rs.close();
			
			if (pstmt != null)
				pstmt.close();
			
			if (conn != null)
				conn.close();
		}
		return resultList;
	}
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
			String empCd = ((TblCmnUser) tblCmnUsers.get(i))
					.getEmpCd();			
			resultList.add(empCd);
		}

		return resultList;
	}
	public static void main(String[] args) throws Exception {
		//System.out.println(operateFilter("waitList", "取回到开始"));		
		System.out.println("");
	}
}
