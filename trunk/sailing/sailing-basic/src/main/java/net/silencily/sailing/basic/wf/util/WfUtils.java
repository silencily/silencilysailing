package net.silencily.sailing.basic.wf.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.basic.sm.user.service.UserManageService;
import net.silencily.sailing.basic.wf.WorkflowInfo;
import net.silencily.sailing.basic.wf.constant.WorkflowConstants;
import net.silencily.sailing.basic.wf.db.WfDBConnection;
import net.silencily.sailing.basic.wf.domain.TblWfOperationInfo;
import net.silencily.sailing.basic.wf.domain.TblWfParticularInfo;
import net.silencily.sailing.basic.wf.dto.FlowTaskInfo;
import net.silencily.sailing.basic.wf.dto.WfEntry;
import net.silencily.sailing.basic.wf.dto.WfSearch;
import net.silencily.sailing.basic.wf.entry.WorkflowAction;
import net.silencily.sailing.basic.wf.entry.WorkflowRelation;
import net.silencily.sailing.basic.wf.entry.WorkflowStep;
import net.silencily.sailing.basic.wf.service.WFService;
import net.silencily.sailing.basic.wf.thread.ThreadAttribute;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.hibernate3.CodeWrapperPlus;
import net.silencily.sailing.security.SecurityContextInfo;
import net.silencily.sailing.security.model.CurrentUser;
import net.silencily.sailing.utils.Tools;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

public class WfUtils {

	private Connection transactionConn = null;

	public Connection getTransactionConn() {
		return transactionConn;
	}

	public void setTransactionConn(Connection transactionConn) {
		this.transactionConn = transactionConn;
	}

	public WfUtils() {

	}

	public static boolean isTogetherSign(String workflowName, String stepId) {
		return "Y".equals(getStep(workflowName, stepId).getTogethersign());
	}

	public static String getWorkflowCnName(String workflowName) {
		if (StringUtils.isNotBlank(workflowName)) {
			TblWfOperationInfo twoi = new TblWfOperationInfo();
			TblWfOperationInfo twoi2 = null;
	        //	 modifiey by gejianbao;
			CodeWrapperPlus cwp=new CodeWrapperPlus();
			cwp.setCode("0");
			twoi.setName(workflowName);
			twoi.setStatus(cwp);
			List wfList = ((WFService) ServiceProvider
					.getService(WFService.SERVICE_NAME)).findsearch(twoi);
			if (wfList != null && wfList.size() > 0) {
				twoi2 = (TblWfOperationInfo) wfList.get(0);
				return twoi2.getOperName();
			}
		}
		return "";
	}

	public static Class getUnProxyClass(Class proxyClass) {
		String proxyClassName = "";
		Class unProxyClass = null;
		if (proxyClass != null) {
			proxyClassName = proxyClass.getName();
			int x = proxyClassName.indexOf(("$"));
			if (-1 != x) {
				proxyClassName = proxyClassName.substring(0, x);
			}
			try {
				unProxyClass = Class.forName(proxyClassName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return unProxyClass;
	}

	public static Class getUnProxyClass(String proxyClassName) {
		Class unProxyClass = null;
		if (StringUtils.isNotBlank(proxyClassName)) {
			int x = proxyClassName.indexOf(("$"));
			if (-1 != x) {
				proxyClassName = proxyClassName.substring(0, x);
			}
			try {
				unProxyClass = Class.forName(proxyClassName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return unProxyClass;
	}

	public static String getUnProxyClassName(Class c) {
		String className = (c == null ? "" : c.getName());
		int x = className.indexOf(("$"));
		if (-1 != x) {
			className = className.substring(0, x);
		}
		return className;
	}

	public static String getUnProxyClassName(String proxyClassName) {
		if (StringUtils.isNotBlank(proxyClassName)) {
			int x = proxyClassName.indexOf(("$"));
			if (-1 != x) {
				proxyClassName = proxyClassName.substring(0, x);
			}
		}
		return proxyClassName;
	}

	public static void removeKilledFlowTask(WorkflowInfo wfInfo)
			throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "DELETE TBL_WF_USERSIGN WHERE OID = ? AND PROCESSED = 'N'";
		try {
			conn = WfDBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, wfInfo.getId());
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("remove killed flow task fail");
		} finally {
			if (pstmt != null)
				pstmt.close();

			if (conn != null)
				conn.close();
		}
	}

	public List getRetakeEmp(WorkflowInfo wfInfo) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List flowTaskInfoList = new ArrayList();

		try {
			conn = WfDBConnection.getConnection();
			String sql = "SELECT COMMIT_ID,FROM_TASK_ID FROM TBL_WF_USERSIGN "
					+ "GROUP BY COMMIT_ID,FROM_TASK_ID HAVING FROM_TASK_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, wfInfo.getTaskId());

			rs = pstmt.executeQuery();
			if (rs.next()) {
				FlowTaskInfo fti = new FlowTaskInfo();
				fti.setProcessId(rs.getString("COMMIT_ID"));
				flowTaskInfoList.add(fti);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return flowTaskInfoList;
	}

	public boolean isHasRetakeByStepId(WorkflowInfo wfInfo) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flg = false;
		String sql = "SELECT CURR_STEP_ID FROM TBL_WF_USERSIGN WHERE FROM_TASK_ID = ?";
		try {
			conn = WfDBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, wfInfo.getTaskId());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				String currStepId = rs.getString("CURR_STEP_ID");
				String[] canRetakedStep = WfUtils.getStep(
						wfInfo.getWorkflowName(), currStepId).getCommitStep()
						.split(",");
				for (int i = 0; i < canRetakedStep.length; i++) {
					if (canRetakedStep[i].equals(wfInfo.getCurrentStep()
							.getId())
							&& "Y".equals(WfUtils.getStep(
									wfInfo.getWorkflowName(), currStepId)
									.getFetch())) {
						flg = true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("is has retake by step id");
		} finally {
			if (rs != null)
				rs.close();

			if (pstmt != null)
				pstmt.close();

			if (conn != null)
				conn.close();

		}
		return flg;
	}

	public List getUntreadEmp(WorkflowInfo wfInfo) throws Exception {
		String untreadId = getStep(wfInfo.getWorkflowName(),
				wfInfo.getCurrentStep().getId()).getGobackStep();

		Connection conn = null;
		PreparedStatement pstmt = null;
		// String sql = "SELECT COMMIT_ID,CURR_STEP_ID,FROM_TASK_ID,TASK_ID FROM
		// TBL_WF_USERSIGN WHERE TASK_ID = ?";
		// delete start liyan 2008-02-15 17:48
		// String sql = "SELECT * FROM TBL_WF_USERSIGN WHERE CURR_STEP_ID = ?
		// AND OID = ? ";
		// delete end liyan 2008-02-15 17:48
		// add start liyan 2008-02-15 17:51
		String unWhenTs = "SELECT * FROM TBL_WF_USERSIGN WHERE CURR_STEP_ID = ? AND OID = ? "
				+ "AND COMMIT_TIME = (SELECT MAX(COMMIT_TIME) FROM TBL_WF_USERSIGN WHERE CURR_STEP_ID = ? AND OID = ?) ";
		String whenTs = "SELECT * FROM TBL_WF_USERSIGN WHERE TASK_ID IN ("
				+ "SELECT TASK_ID FROM(" + unWhenTs + "))";
		String sql = isTogetherSign(wfInfo.getWorkflowName(), untreadId) ? whenTs
				: unWhenTs;
		// add end liyan 2008-02-15 17:51
		ResultSet rs = null;
		List flowTaskInfoList = new ArrayList();

		try {
			conn = WfDBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, untreadId);
			pstmt.setString(2, wfInfo.getId());
			pstmt.setString(3, untreadId);
			pstmt.setString(4, wfInfo.getId());

			rs = pstmt.executeQuery();
			while (rs.next()) {
				FlowTaskInfo fti = new FlowTaskInfo();
				fti.setProcessId(rs.getString("COMMIT_ID"));
				fti.setProcessUsername(rs.getString("COMMIT_USERNAME"));
				fti.setProcessRole(rs.getString("COMMIT_ROLE"));
				fti.setProcessDept(rs.getString("COMMIT_DEPT"));
				fti.setToStepId(untreadId);
				fti.setToStepName(this.getStepName(wfInfo.getWorkflowName(),
						untreadId));
				flowTaskInfoList.add(fti);
				// if (StringUtils.isNotBlank(untreadId)
				// && untreadId.equals(rs.getString("CURR_STEP_ID"))) {
				//				
				// String taskId2 = rs.getString("FROM_TASK_ID");
				//					
				// pstmt = conn.prepareStatement("SELECT PROCESS_ID FROM
				// TBL_WF_USERSIGN WHERE TASK_ID = ?");
				// pstmt.setString(1, taskId2);
				//					
				// rs = pstmt.executeQuery();
				// while (rs.next()) {
				// FlowTaskInfo fti = new FlowTaskInfo();
				// fti.setProcessId(rs.getString("PROCESS_ID"));
				// flowTaskInfoList.add(fti);
				// }
				//					
				// return flowTaskInfoList;
				// } else {
				//					
				// String taskId = rs.getString("FROM_TASK_ID");
				// while (true) {
				// pstmt = conn.prepareStatement(sql);
				// pstmt.setString(1, taskId);
				//						
				// rs = pstmt.executeQuery();
				// if (rs.next()) {
				// if (StringUtils.isNotBlank(untreadId)
				// && untreadId.equals(rs
				// .getString("CURR_STEP_ID"))) {
				//							
				// String taskId3 = rs.getString("FROM_TASK_ID");
				// pstmt = conn.prepareStatement("SELECT PROCESS_ID FROM
				// TBL_WF_USERSIGN WHERE TASK_ID = ?");
				// pstmt.setString(1, taskId3);
				//							
				// rs = pstmt.executeQuery();
				// if (rs.next()) {
				// do {
				// FlowTaskInfo fti = new FlowTaskInfo();
				// fti.setProcessId(rs
				// .getString("PROCESS_ID"));
				// flowTaskInfoList.add(fti);
				// } while (rs.next());
				// } else {
				// break;
				// }
				// return flowTaskInfoList;
				// } else {
				// taskId = rs.getString("FROM_TASK_ID");
				// }
				// } else {
				// break;
				// }
				// }
				// }
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
			if (flowTaskInfoList.size() < 1) {
				throw new Exception("untread emp is null.");
			}
		}
		return flowTaskInfoList;
	}

	public void alsoUpdateOtherSigner(WorkflowInfo wfInfo) throws Exception {

		String sql = "DELETE TBL_WF_USERSIGN WHERE PROCESSED = 'N' AND TASK_ID = ?";
		PreparedStatement pstmt = null;
		try {
			pstmt = getTransactionConn().prepareStatement(sql);
			pstmt.setString(1, wfInfo.getTaskId());
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("post flow task information.");
		} finally {
			if (pstmt != null)
				pstmt.close();
		}

	}

	public void postFlowTaskInfo(WorkflowInfo wfInfo, CurrentUser user)
			throws Exception {
		String sql = "UPDATE TBL_WF_USERSIGN SET PROCESSED = 'Y' , PROCESS_TIME = ? , PROCESS_ID = ? "
				+ "WHERE TASK_ID = ?  AND ((PROCESS_ID = ?) OR (ROLE_SIGN_FLAG = 'Y'))";
		PreparedStatement pstmt = null;
		try {
			pstmt = getTransactionConn().prepareStatement(sql);
			java.util.Date date = new java.util.Date();
			java.sql.Date nowDate = new java.sql.Date(date.getTime());

			pstmt.setDate(1, nowDate);
			pstmt.setString(2, user.getEmpCd());
			pstmt.setString(3, wfInfo.getTaskId());
			pstmt.setString(4, user.getEmpCd());
			pstmt.executeUpdate();
			//			
			// if (i == 0) {
			// List signerList = wfInfo.getSignerList();
			// FlowTaskInfo fti = new FlowTaskInfo();
			// fti.setRoleSignFlag("Y");
			// signerList.add(fti);
			// wfInfo.setSignerList(signerList);
			// setSigner(wfInfo);
			// setFinishData(wfInfo);
			// }

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("post flow task information.");
		} finally {
			if (pstmt != null)
				pstmt.close();
		}
	}

	public static void removeTaskBeforeWorkflowInit(WorkflowInfo wfInfo)
			throws Exception {
		String sql = "UPDATE TBL_WF_USERSIGN SET PROCESSED = 'Y' , PROCESS_TIME = ? , PROCESS_ID = ? "
				+ "WHERE TASK_ID = ?  AND ((PROCESS_ID = ?) OR (ROLE_SIGN_FLAG = 'Y'))";
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = WfDBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			java.util.Date date = new java.util.Date();
			java.sql.Date nowDate = new java.sql.Date(date.getTime());

			pstmt.setDate(1, nowDate);
			pstmt.setString(2, SecurityContextInfo
					.getCurrentUser().getEmpCd());
			pstmt.setString(3, wfInfo.getTaskId());
			pstmt.setString(4, SecurityContextInfo
					.getCurrentUser().getEmpCd());
			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("remove task before workflow init");
		} finally {
			if (pstmt != null)
				pstmt.close();
			if (conn != null)
				conn.close();
		}
	}
	public boolean isCanbeAddTaskWithoutWorkflow(WorkflowInfo wfInfo) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String oid = wfInfo.getId();
		boolean flag = false;
		String sql = "SELECT * FROM TBL_WF_USERSIGN WHERE OID = ? AND TASK_TYPE = 'S' AND PROCESSED = 'N'";
		try {
			conn = WfDBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, oid);
			rs = pstmt.executeQuery();
			if (rs.next())
				throw new Exception("The businesss has another task when status is scratch.");
			else
				flag = true;
				
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("When execute isCanbeAddTaskWithoutWorkflow method.");
		} finally {
			if (pstmt != null)
				pstmt.close();
			
			if (conn != null)
				conn.close();
		}
		return flag;
	}
	public void addTaskWithoutWorkflow(WorkflowInfo wfInfo) throws Exception {
		if (this.isCanbeAddTaskWithoutWorkflow(wfInfo)) {
			Connection conn = null;
			PreparedStatement pstmt = null;
			List signerList = wfInfo.getSignerList();
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

				String taskType = "S";

				FlowTaskInfo fti = (FlowTaskInfo) signerList.get(0);
				pstmt = conn.prepareStatement(sql);

				pstmt.setString(1, Tools.getPKCode());
				pstmt.setString(2, "N");
				pstmt.setString(3, fti.getProcessId());
				pstmt.setString(4, WfUtils.getEmpName(fti.getProcessId()));
				pstmt.setString(5, fti.getProcessRole());
				pstmt.setString(6, fti.getCommitUsername());

				pstmt.setString(7, fti.getCommitRole());
				//2008-06-17 yangxl加判断
				if(null != SecurityContextInfo.getCurrentUser()){
					pstmt.setString(8, SecurityContextInfo
							.getCurrentUser().getEmpCd());
				}else{
					pstmt.setString(8,null);
				}
				pstmt.setString(9, fti.getProcessDept());
				pstmt.setString(10, fti.getCommitDept());
				pstmt.setString(11, wfInfo.getId());
				pstmt.setString(12, wfInfo.getWorkflowName());

				pstmt.setString(13, "未启动流程");
				pstmt.setString(14, "未启动流程");

				pstmt.setString(15, "未启动流程");
				pstmt.setString(16, "未启动流程");

				pstmt.setString(17, fti.getRoleSignFlag());
				pstmt.setString(18, fti.getRoleSignRole());
				pstmt.setString(19, fti.getRoleSignDept());

				pstmt.setString(20, "N");
				pstmt.setString(21, "N");
				pstmt.setString(22, fti.getConsigner());
				pstmt.setString(23, fti.getConsigneder());
				pstmt.setString(24, fti.getConsignerRole());
				pstmt.setString(25, fti.getConsignederRole());

				String className = wfInfo.getClass().getName();
				int x = className.indexOf(("$"));
				if (-1 != x) {
					className = className.substring(0, x);
				}
				pstmt.setString(26, className);
				pstmt.setString(27, taskId);
				if (StringUtils.isBlank(wfInfo.getTaskId())) {
					pstmt.setString(28,Tools.getPKCode());
				} else {
					pstmt.setString(28, wfInfo.getTaskId());
				}
				pstmt.setString(29, taskType);

				String taskName = WfCommonTools.getWorkflowCnName(wfInfo
						.getWorkflowName());

				pstmt.setString(30, taskName);

				pstmt.executeUpdate();

			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception(e.getMessage());
			} finally {
				if (pstmt != null) {
					pstmt.close();
				}

				if (conn != null) {
					conn.close();
				}
			}
		}		
	}

	public void setSigner(WorkflowInfo wfInfo) throws Exception {
		PreparedStatement pstmt = null;
		List signerList = wfInfo.getSignerList();
		if (signerList == null) {
			signerList = new ArrayList();
		}
		String sql = "INSERT INTO TBL_WF_USERSIGN "
				+ "(ID,PROCESSED,PROCESS_ID,PROCESS_USERNAME,PROCESS_ROLE,COMMIT_USERNAME,"
				+ "COMMIT_ROLE,COMMIT_ID,PROCESS_DEPT,COMMIT_DEPT,OID,WORKFLOW_NAME,CURR_STEP_ID,"
				+ "CURR_STEP_NAME,TO_STEP_ID,TO_STEP_NAME,ROLE_SIGN_FLAG,ROLE_SIGN_ROLE,ROLE_SIGN_DEPT,"
				+ "CONSIGN_FLAG,CONSIGNED_FLAG,CONSIGNER,CONSIGNEDER,CONSIGNER_ROLE,CONSIGNEDER_ROLE,"
				+ "CLASS_NAME,TASK_ID,FROM_TASK_ID,TASK_TYPE,TASK_NAME) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		try {
			String taskId = Tools.getPKCode();
			ThreadAttribute.TaskId.set(taskId);
			String taskType = "";
			if (isTogetherSign(wfInfo.getWorkflowName(), this.getNextStep(
					wfInfo).getId())) {
				taskType = "P";
			} else {
				taskType = "A";
			}
			for (int i = 0; i < signerList.size(); i++) {
				FlowTaskInfo fti = (FlowTaskInfo) signerList.get(i);
				pstmt = getTransactionConn().prepareStatement(sql);

				pstmt.setString(1, Tools.getPKCode());
				pstmt.setString(2, "N");
				pstmt.setString(3, fti.getProcessId());
				pstmt.setString(4, WfUtils.getEmpName(fti.getProcessId()));
				pstmt.setString(5, fti.getProcessRole());
				pstmt.setString(6, fti.getCommitUsername());

				pstmt.setString(7, fti.getCommitRole());
				pstmt.setString(8, SecurityContextInfo
						.getCurrentUser().getEmpCd());
				pstmt.setString(9, fti.getProcessDept());
				pstmt.setString(10, fti.getCommitDept());
				pstmt.setString(11, wfInfo.getId());
				pstmt.setString(12, wfInfo.getWorkflowName());

				if (StringUtils.isNotBlank(fti.getCurrStepId())) {
					pstmt.setString(13, fti.getCurrStepId());
					pstmt.setString(14, fti.getCurrStepName());
				} else {
					pstmt.setString(13, wfInfo.getCurrentStep().getId());
					pstmt.setString(14, wfInfo.getCurrentStep().getName());
				}

				String toStepName = "";
				if (StringUtils.isNotBlank(fti.getToStepId())) {
					pstmt.setString(15, fti.getToStepId());
					pstmt.setString(16, fti.getToStepName());
					toStepName = fti.getToStepName();
				} else {
					WorkflowStep nextStep = getNextStep(wfInfo);
					pstmt.setString(15, nextStep.getId());
					pstmt.setString(16, nextStep.getName());
					toStepName = nextStep.getName();
				}

				pstmt.setString(17, fti.getRoleSignFlag());
				pstmt.setString(18, fti.getRoleSignRole());
				pstmt.setString(19, fti.getRoleSignDept());

				pstmt.setString(20, "N");
				pstmt.setString(21, "N");
				pstmt.setString(22, fti.getConsigner());
				pstmt.setString(23, fti.getConsigneder());
				pstmt.setString(24, fti.getConsignerRole());
				pstmt.setString(25, fti.getConsignederRole());

				String className = wfInfo.getClass().getName();
				int x = className.indexOf(("$"));
				if (-1 != x) {
					className = className.substring(0, x);
				}
				pstmt.setString(26, className);
				pstmt.setString(27, taskId);
				if (StringUtils.isBlank(wfInfo.getTaskId())) {
					pstmt.setString(28, Tools.getPKCode());
				} else {
					pstmt.setString(28, wfInfo.getTaskId());
				}
				pstmt.setString(29, taskType);

				String taskName = WfCommonTools.getWorkflowCnName(wfInfo
						.getWorkflowName());
				// if (StringUtils.isNotBlank(toStepName)) {
				// taskName += ("_" + toStepName);
				// } else {
				// taskName += ("_" + wfInfo.getWorkflowStatusName());
				// }
				pstmt.setString(30, taskName);

				pstmt.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			if (pstmt != null) {
				pstmt.close();
			}
		}
	}

	public WorkflowStep getNextStep(WorkflowInfo wfInfo) throws Exception {
		String actionId = wfInfo.getActionId();
		if (StringUtils.isBlank(actionId)) {
			return new WorkflowStep();
		}
		List actionList = wfInfo.getCurrentStep().getAvailableActions();
		for (int i = 0; i < actionList.size(); i++) {
			WorkflowAction action = (WorkflowAction) actionList.get(i);
			if (actionId.equals(action.getId())) {
				WorkflowStep step = action.getNextStep();
				Assert.isTrue(step != null, "The next step is null.");
				step.setRoles(WfUtils.getRoles(wfInfo.getWorkflowName(), step
						.getId()));
				return step;
			}
		}
		return new WorkflowStep();
	}

	public void retakeTask(WorkflowInfo wfInfo) throws Exception {
		PreparedStatement pstmt = null;
		String sql = "DELETE FROM TBL_WF_USERSIGN WHERE FROM_TASK_ID = ?";
		try {
			pstmt = getTransactionConn().prepareStatement(sql);
			pstmt.setString(1, wfInfo.getTaskId());
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			if (pstmt != null) {
				pstmt.close();
			}
		}
	}

	/**
	 * 挂起的后处理,用于扩展.
	 */
	public void postSuspend(WorkflowInfo wfInfo) throws Exception {

	}

	/**
	 * 激活的后处理,用于扩展.
	 */
	public void postActive(WorkflowInfo wfInfo) throws Exception {

	}

	public boolean isMyTask(WorkflowInfo wfInfo) throws Exception {
		boolean flag = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT ID FROM TBL_WF_USERSIGN WHERE "
				+ "TASK_ID = ? AND PROCESSED = 'N' AND PROCESS_ID = ?";
		try {
			conn = WfDBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			if (StringUtils.isBlank(wfInfo.getTaskId())) {
				return false;
			}
			pstmt.setString(1, wfInfo.getTaskId());
			pstmt.setString(2, SecurityContextInfo
					.getCurrentUser().getEmpCd());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return flag;
	}

	public boolean isMyTaskOfCommited(WorkflowInfo wfInfo) throws Exception {
		boolean flag = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = WfDBConnection.getConnection();
			String sql = "SELECT ID FROM TBL_WF_USERSIGN WHERE FROM_TASK_ID = ? AND COMMIT_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, wfInfo.getTaskId());
			pstmt.setString(2, SecurityContextInfo.getCurrentUser().getEmpCd());

			rs = pstmt.executeQuery();
			if (rs.next()) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return flag;
	}

	/**
	 * 判断用户是否在roles里
	 */
	public static boolean hasRole(CurrentUser user, String roles) {
		if (StringUtils.isNotBlank(roles)) {
			String[] role = roles.split(",");
			for (int i = 0; i < role.length; i++) {
				if (user.hasRoleCd(role[i])) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isSomeOneSign(WorkflowInfo wfInfo) throws Exception {
		boolean flag = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = WfDBConnection.getConnection();
			String sql = "SELECT ID FROM TBL_WF_USERSIGN WHERE FROM_TASK_ID = ? AND PROCESSED = 'Y'";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, wfInfo.getTaskId());

			rs = pstmt.executeQuery();
			if (rs.next()) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return flag;
	}

	public String getRolesOfCurrentUser(CurrentUser user) {
		Map roleMap = user.getRoles();
		Set roleSet = new HashSet(roleMap.values());
		String resultVal = "";
		Iterator it = roleSet.iterator();
		while (it.hasNext()) {
			String role = (String) it.next();
			resultVal += (role + ",");
		}
		return resultVal;
	}

	public int getWaitTaskCount(CurrentUser user, WfSearch search)
			throws Exception {
		int resultVal = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String preCount = "SELECT COUNT(*) AS CNT FROM (";
		String postCount = " ) S";
		StringBuffer sqlBuf = new StringBuffer();
		String preSql = "SELECT * FROM (SELECT * FROM (SELECT TASK_NAME,OID AS OID,CLASS_NAME AS CLASS_NAME,COMMIT_TIME AS COMMIT_TIME,"
				+ "COMMIT_ID AS COMMIT_ID,TASK_ID AS TASK_ID,ROLE_SIGN_FLAG AS ROLE_SIGN_FLAG,"
				+ "ROLE_SIGN_ROLE AS ROLE_SIGN_ROLE,PROCESS_ID AS PROCESS_ID,PROCESSED AS PROCESSED "
				+ "FROM TBL_WF_USERSIGN TEMP WHERE PROCESS_ID = ? AND PROCESSED = 'N' "
				+ "AND TASK_ID NOT IN (SELECT TASK_ID FROM TBL_WF_USERSIGN WHERE TASK_ID = TEMP.TASK_ID AND PROCESSED = 'Y' AND NVL(TASK_TYPE, 'A')"
				+ " <> 'P')"
				+ "UNION SELECT TASK_NAME,OID,CLASS_NAME,COMMIT_TIME,COMMIT_ID,TASK_ID,ROLE_SIGN_FLAG,ROLE_SIGN_ROLE,"
				+ "PROCESS_ID,PROCESSED FROM TBL_WF_USERSIGN T1,";
		String postSql = "WHERE T1.ROLE_SIGN_ROLE LIKE '%' || T2.ROLE ||'%' "
				+ "AND NOT T1.ROLE_SIGN_ROLE LIKE '%' || T2.ROLE || '.' ||'%' "
				+ "AND NOT T1.ROLE_SIGN_ROLE LIKE '%' || '.' || T2.ROLE ||'%' AND ROLE_SIGN_FLAG = 'Y' AND PROCESSED = 'N') "
				+ "ORDER BY COMMIT_TIME DESC)";

		String str = this.getRolesOfCurrentUser(user);
		String[] role = str.split(",");

		if (role.length > 0)
			sqlBuf.append("(");
		if (role.length == 1) {
			sqlBuf.append("SELECT '" + role[0] + "' ROLE FROM DUAL) T2 ");
		} else {
			for (int i = 0; i < role.length; i++) {
				if (i == (role.length - 1)) {
					sqlBuf.append("SELECT '" + role[i] + "' FROM DUAL) T2 ");
				} else if (i == 0) {
					sqlBuf.append("SELECT '" + role[i]
							+ "' ROLE FROM DUAL UNION ");
				} else {
					sqlBuf.append("SELECT '" + role[i] + "' FROM DUAL UNION ");
				}
			}
		}

		String finalSql = "";
		StringBuffer buf2 = new StringBuffer(preSql + sqlBuf.toString()
				+ postSql);
		buf2.append(" WHERE");
		java.sql.Date startTime = null;
		java.sql.Date endTime = null;
		boolean flag = false;

		try {
			if (search != null) {
				// if (StringUtils.isNotBlank(search.getState())) {
				// flag = true;
				// buf2.append(" CSTATUS = '" + search.getState() + "' AND");
				// }
				if (StringUtils.isNotBlank(search.getStartTime())
						&& StringUtils.isNotBlank(search.getEndTime())) {
					flag = true;
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					java.util.Date start = sdf.parse(search.getStartTime());
					java.util.Date end = sdf.parse(search.getEndTime());
					startTime = new java.sql.Date(start.getTime());
					endTime = new java.sql.Date(end.getTime());
					buf2.append(" ? < COMMIT_TIME AND COMMIT_TIME < ? AND");
				}
				if (StringUtils.isNotBlank(search.getTitle())) {
					flag = true;
					buf2.append(" TASK_NAME LIKE '%" + search.getTitle()
							+ "%' AND");
				}
			}
			if (flag) {
				String sqlWithoutLastAnd = buf2.toString();
				finalSql = sqlWithoutLastAnd.substring(0, sqlWithoutLastAnd
						.length() - 3);
			} else {
				finalSql = preSql + sqlBuf.toString() + postSql;
			}
			conn = WfDBConnection.getConnection();
			pstmt = conn.prepareStatement(preCount + finalSql + postCount);
			if (startTime != null && endTime != null) {
				pstmt.setString(1, user.getEmpCd());
				pstmt.setDate(2, startTime);
				pstmt.setDate(3, endTime);
			} else {
				pstmt.setString(1, user.getEmpCd());
			}

			rs = pstmt.executeQuery();
			if (rs.next()) {
				String count = rs.getString("CNT");
				resultVal = Integer.parseInt(count);
				return resultVal;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return resultVal;
	}

	public List findWaitTask(CurrentUser user, WfSearch search, int pageNo,
			int pageSize) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List resultList = new ArrayList();

		StringBuffer sqlBuf = new StringBuffer();
		String prePageSql = "SELECT * FROM (SELECT S.*,ROWNUM R FROM (";
		String postPageSql = ") S WHERE ROWNUM <= ?) WHERE R >= ?";// <=max(10),>=min(6)
		String preSql = "SELECT * FROM (SELECT * FROM (SELECT TASK_NAME,OID AS OID,CLASS_NAME AS CLASS_NAME,COMMIT_TIME AS COMMIT_TIME,"
				+ "COMMIT_ID AS COMMIT_ID,TASK_ID AS TASK_ID,ROLE_SIGN_FLAG AS ROLE_SIGN_FLAG,"
				+ "ROLE_SIGN_ROLE AS ROLE_SIGN_ROLE,PROCESS_ID AS PROCESS_ID,PROCESSED AS PROCESSED "
				+ "FROM TBL_WF_USERSIGN TEMP WHERE PROCESS_ID = ? AND PROCESSED = 'N' "
				+ "AND TASK_ID NOT IN (SELECT TASK_ID FROM TBL_WF_USERSIGN WHERE TASK_ID = TEMP.TASK_ID AND PROCESSED = 'Y' AND NVL(TASK_TYPE, 'A') <> 'P')"
				+ "UNION SELECT TASK_NAME,OID,CLASS_NAME,COMMIT_TIME,COMMIT_ID,TASK_ID,ROLE_SIGN_FLAG,ROLE_SIGN_ROLE,"
				+ "PROCESS_ID,PROCESSED FROM TBL_WF_USERSIGN T1,";
		String postSql = "WHERE T1.ROLE_SIGN_ROLE LIKE '%' || T2.ROLE ||'%' "
				+ "AND NOT T1.ROLE_SIGN_ROLE LIKE '%' || T2.ROLE || '.' ||'%' "
				+ "AND NOT T1.ROLE_SIGN_ROLE LIKE '%' || '.' || T2.ROLE ||'%' AND ROLE_SIGN_FLAG = 'Y' AND PROCESSED = 'N') "
				+ "ORDER BY COMMIT_TIME DESC)";

		String str = this.getRolesOfCurrentUser(user);
		String[] role = str.split(",");

		if (role.length > 0)
			sqlBuf.append("(");
		if (role.length == 1) {
			sqlBuf.append("SELECT '" + role[0] + "' ROLE FROM DUAL) T2 ");
		} else {
			for (int i = 0; i < role.length; i++) {
				if (i == (role.length - 1)) {
					sqlBuf.append("SELECT '" + role[i] + "' FROM DUAL) T2 ");
				} else if (i == 0) {
					sqlBuf.append("SELECT '" + role[i]
							+ "' ROLE FROM DUAL UNION ");
				} else {
					sqlBuf.append("SELECT '" + role[i] + "' FROM DUAL UNION ");
				}
			}
		}

		System.out.println(preSql + sqlBuf.toString() + postSql);

		String finalSql = "";
		StringBuffer buf2 = new StringBuffer(preSql + sqlBuf.toString()
				+ postSql);
		buf2.append(" WHERE");
		java.sql.Date startTime = null;
		java.sql.Date endTime = null;
		boolean flag = false;
		int last = pageSize * pageNo;
		int first = pageSize * (pageNo - 1) + 1;
		try {
			if (search != null) {
				// if (StringUtils.isNotBlank(search.getState())) {
				// flag = true;
				// buf2.append(" CSTATUS = '" + search.getState() + "' AND");
				// }
				if (StringUtils.isNotBlank(search.getStartTime())
						&& StringUtils.isNotBlank(search.getEndTime())) {
					flag = true;
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					java.util.Date start = sdf.parse(search.getStartTime());
					java.util.Date end = sdf.parse(search.getEndTime());
					startTime = new java.sql.Date(start.getTime());
					endTime = new java.sql.Date(end.getTime());
					buf2.append(" ? < COMMIT_TIME AND COMMIT_TIME < ? AND");
				}
				if (StringUtils.isNotBlank(search.getTitle())) {
					flag = true;
					buf2.append(" TASK_NAME LIKE '%" + search.getTitle()
							+ "%' AND");
				}
			}
			if (flag) {
				String sqlWithoutLastAnd = buf2.toString();
				finalSql = sqlWithoutLastAnd.substring(0, sqlWithoutLastAnd
						.length() - 3);
			} else {
				finalSql = preSql + sqlBuf.toString() + postSql;
			}

			conn = WfDBConnection.getConnection();
			pstmt = conn.prepareStatement(prePageSql + finalSql + postPageSql);

			if (startTime != null && endTime != null) {
				pstmt.setString(1, user.getEmpCd());
				pstmt.setDate(2, startTime);
				pstmt.setDate(3, endTime);
				pstmt.setInt(4, last);
				pstmt.setInt(5, first);
			} else {
				pstmt.setString(1, user.getEmpCd());
				pstmt.setInt(2, last);
				pstmt.setInt(3, first);
			}

			rs = pstmt.executeQuery();

			while (rs.next()) {
				try {
					WfEntry wfEntry = new WfEntry();
					resultList.add(wfEntry);
					String taskId = rs.getString("TASK_ID");
					wfEntry.setTaskId(taskId);
					String oid = rs.getString("OID");
					wfEntry.setOid(oid);

					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					java.util.Date commitDate = sdf.parse(rs
							.getString("COMMIT_TIME"));
					String commitTime = sdf.format(commitDate);
					wfEntry.setCommiter(getEmpName(rs.getString("COMMIT_ID")));
					wfEntry.setCommitTime(commitTime);
					String className = rs.getString("CLASS_NAME");
					String title = rs.getString("TASK_NAME");

					WorkflowInfo wfInfo = null;
					wfInfo = (WorkflowInfo) ((CommonService) ServiceProvider
							.getService(CommonService.SERVICE_NAME)).load(Class
							.forName(className), oid);
					wfEntry.setCurrentStepName(wfInfo.getCurrentStep()
							.getName());
					//同时封装工作流步骤信息
					wfEntry.setStepId(wfInfo.getCurrentStep().getId());
					
					wfInfo.setTaskId(taskId);
					String url = wfInfo.getWorkflowEditPath(oid);
					StringBuffer buf = new StringBuffer(
							null == url ? "not found url" : url);
					buf.append("&taskId=" + taskId);
					wfEntry.setUrl(buf.toString());
					wfEntry.setWfName(wfInfo.getWorkflowName());
					wfEntry.setWfState(wfInfo.getWorkflowStatusName());
//					wfEntry.setUrl(Tools.getWFSecurityURL(wfEntry.getWfName(),
//							wfEntry.getCurrentStepName(), wfEntry.getUrl()));
					/*wfEntry.setTitle(WorkflowTaskTools.getTaskName(wfInfo,
							title));*///08-07-08 by liuzhuo
					wfEntry.setTitle(wfInfo.getTaskName(title));
				} catch (Exception e) {
					continue;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("when find wait task.");
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

	public int getOverTaskCount(CurrentUser user, WfSearch search)
			throws Exception {
		int resultVal = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String preCount = "SELECT COUNT(*) AS CNT FROM (";
		String postCount = " ) S";
		// String sql = "SELECT * FROM (SELECT * FROM "
		// + "(SELECT
		// TASK_NAME,OID,CLASS_NAME,COMMIT_ID,FROM_TASK_ID,MAX(COMMIT_TIME) AS
		// COMMIT_TIME "
		// + "FROM TBL_WF_USERSIGN WHERE NVL(TASK_TYPE, 'A') <> 'P' AND
		// COMMIT_ID = ? GROUP BY
		// TASK_NAME,OID,CLASS_NAME,COMMIT_ID,FROM_TASK_ID "
		// + "UNION SELECT TASK_NAME,OID,CLASS_NAME,PROCESS_ID AS
		// COMMIT_ID,TASK_ID,COMMIT_TIME "
		// + "FROM TBL_WF_USERSIGN WHERE NVL(TASK_TYPE, 'A') = 'P' AND PROCESSED
		// = 'Y' AND PROCESS_ID = ?) "
		// + "ORDER BY COMMIT_TIME DESC)";
		String sql = "SELECT * FROM TBL_WF_USERSIGN WHERE PROCESSED = 'Y' AND PROCESS_ID = ? ";
		String post = " ORDER BY COMMIT_TIME DESC";
		StringBuffer buf2 = new StringBuffer(sql);
		buf2.append(" AND");
		java.sql.Date startTime = null;
		java.sql.Date endTime = null;
		boolean flag = false;

		try {
			if (search != null) {
				// if (StringUtils.isNotBlank(search.getState())) {
				// flag = true;
				// buf2.append(" CSTATUS = '" + search.getState() + "' AND");
				// }
				if (StringUtils.isNotBlank(search.getStartTime())
						&& StringUtils.isNotBlank(search.getEndTime())) {
					flag = true;
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					java.util.Date start = sdf.parse(search.getStartTime());
					java.util.Date end = sdf.parse(search.getEndTime());
					startTime = new java.sql.Date(start.getTime());
					endTime = new java.sql.Date(end.getTime());
					buf2.append(" ? < COMMIT_TIME AND COMMIT_TIME < ? AND");
				}
				if (StringUtils.isNotBlank(search.getTitle())) {
					flag = true;
					buf2.append(" TASK_NAME LIKE '%" + search.getTitle()
							+ "%' AND");
				}
			}
			if (flag) {
				String sqlWithoutLastAnd = buf2.toString();
				sql = sqlWithoutLastAnd.substring(0,
						sqlWithoutLastAnd.length() - 3);
			}

			conn = WfDBConnection.getConnection();
			pstmt = conn.prepareStatement(preCount + sql + postCount + post);

			pstmt.setString(1, user.getEmpCd());
			if (startTime != null && endTime != null) {
				pstmt.setDate(2, startTime);
				pstmt.setDate(3, endTime);
			}

			rs = pstmt.executeQuery();
			if (rs.next()) {
				String count = rs.getString("CNT");
				resultVal = Integer.parseInt(count);
				return resultVal;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return resultVal;
	}

	public List findOverTask(CurrentUser user, WfSearch search, int pageNo,
			int pageSize) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List resultList = new ArrayList();
		String preSql = "SELECT * FROM (SELECT S.*,ROWNUM R FROM (";
		String postSql = " ORDER BY COMMIT_TIME DESC) S WHERE ROWNUM <= ?) WHERE R >= ?";// <=max(10),>=min(6)
		// String sql = "SELECT * FROM (SELECT * FROM "
		// + "(SELECT TASK_NAME,OID,CLASS_NAME,COMMIT_ID,FROM_TASK_ID AS
		// TASK_ID,MAX(COMMIT_TIME) AS COMMIT_TIME "
		// + "FROM TBL_WF_USERSIGN WHERE NVL(TASK_TYPE, 'A') <> 'P' AND
		// COMMIT_ID = ? GROUP BY
		// TASK_NAME,OID,CLASS_NAME,COMMIT_ID,FROM_TASK_ID "
		// + "UNION SELECT TASK_NAME,OID,CLASS_NAME,PROCESS_ID AS
		// COMMIT_ID,TASK_ID,COMMIT_TIME "
		// + "FROM TBL_WF_USERSIGN WHERE NVL(TASK_TYPE, 'A') = 'P' AND PROCESSED
		// = 'Y' AND PROCESS_ID = ?) "
		// + "ORDER BY COMMIT_TIME DESC)";
		String sql = "SELECT * FROM TBL_WF_USERSIGN WHERE PROCESSED = 'Y' AND PROCESS_ID = ? ";
		// String post = " ORDER BY COMMIT_TIME DESC";
		StringBuffer buf2 = new StringBuffer(sql);
		buf2.append(" AND");
		java.sql.Date startTime = null;
		java.sql.Date endTime = null;
		boolean flag = false;
		int last = pageSize * pageNo;
		int first = pageSize * (pageNo - 1) + 1;
		try {
			if (search != null) {
				// if (StringUtils.isNotBlank(search.getState())) {
				// flag = true;
				// buf2.append(" CSTATUS = '" + search.getState() + "' AND");
				// }
				if (StringUtils.isNotBlank(search.getStartTime())
						&& StringUtils.isNotBlank(search.getEndTime())) {
					flag = true;
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					java.util.Date start = sdf.parse(search.getStartTime());
					java.util.Date end = sdf.parse(search.getEndTime());
					startTime = new java.sql.Date(start.getTime());
					endTime = new java.sql.Date(end.getTime());
					buf2.append(" ? < COMMIT_TIME AND COMMIT_TIME < ? AND");
				}
				if (StringUtils.isNotBlank(search.getTitle())) {
					flag = true;
					buf2.append(" TASK_NAME LIKE '%" + search.getTitle()
							+ "%' AND");
				}
			}
			if (flag) {
				String sqlWithoutLastAnd = buf2.toString();
				sql = sqlWithoutLastAnd.substring(0,
						sqlWithoutLastAnd.length() - 3);
			}

			conn = WfDBConnection.getConnection();
			pstmt = conn.prepareStatement(preSql + sql + postSql);

			pstmt.setString(1, user.getEmpCd());
			if (startTime != null && endTime != null) {
				pstmt.setDate(2, startTime);
				pstmt.setDate(3, endTime);
				pstmt.setInt(4, last);
				pstmt.setInt(5, first);
			} else {
				pstmt.setInt(2, last);
				pstmt.setInt(3, first);
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
				try {
					WfEntry wfEntry = new WfEntry();
					resultList.add(wfEntry);
					String taskId = rs.getString("TASK_ID");
					wfEntry.setTaskId(taskId);
					String oid = rs.getString("OID");
					wfEntry.setOid(oid);
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					java.util.Date commitDate = sdf.parse(rs
							.getString("COMMIT_TIME"));
					String commitTime = sdf.format(commitDate);
					wfEntry.setCommiter(getEmpName(rs.getString("COMMIT_ID")));
					wfEntry.setCommitTime(commitTime);
					String className = rs.getString("CLASS_NAME");
					String title = rs.getString("TASK_NAME");
					wfEntry.setTitle(title);
					WorkflowInfo wfInfo = null;
					wfInfo = (WorkflowInfo) ((CommonService) ServiceProvider
							.getService(CommonService.SERVICE_NAME)).load(Class
							.forName(className), oid);
					// wfEntry.setCurrentStepName(wfInfo.getCurrentStep().getName());
					wfEntry.setCurrentStepName(rs.getString("TO_STEP_NAME"));
					//WENJB增加参数STEPID
					wfEntry.setStepId(rs.getString("TO_STEP_ID"));
					wfInfo.setTaskId(taskId);
					String url = wfInfo.getWorkflowEditPath(oid);
					StringBuffer buf = new StringBuffer(
							null == url ? "not found url" : url);
					buf.append("&taskId=" + taskId);
					wfEntry.setUrl(buf.toString());
					wfEntry.setWfName(wfInfo.getWorkflowName());
					wfEntry.setWfState(wfInfo.getWorkflowStatusName());
					/*wfEntry.setTitle(WorkflowTaskTools.getTaskName(wfInfo,
							title));*///08-07-08 liuzhuo
					wfEntry.setTitle(wfInfo.getTaskName(title));
				} catch (Exception e) {
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("find over task");
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return resultList;
	}

	public List getWfInitSplit(String workflowName) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt1 = null;
		ResultSet rs1 = null;
		List resultList = new ArrayList();
		String COMMIT_STEP = "SELECT COMMIT_STEP FROM TBL_WF_PARTICULAR_INFO WHERE "
				+ "TBL_WF_OPE_INF_ID IN "
				+ "(SELECT ID FROM TBL_WF_OPERATION_INFO WHERE CREATED_TIME = "
				+ "(SELECT MAX(CREATED_TIME) FROM TBL_WF_OPERATION_INFO WHERE "
				+ "STATUS = '0' AND DEL_FLG = '0' AND NAME = ?)) AND STEP_ID = '1'";
		String commitSteps = "";
		String[] commitStep = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs2 = null;

		String STEP_NAME = "SELECT STEP_NAME,HELPMAN,POINT_STEP FROM TBL_WF_PARTICULAR_INFO T WHERE"
				+ " STEP_ID = ? AND TBL_WF_OPE_INF_ID = "
				+ "(SELECT ID FROM TBL_WF_OPERATION_INFO WHERE "
				+ "CREATED_TIME = (SELECT MAX(CREATED_TIME) FROM TBL_WF_OPERATION_INFO T2 WHERE "
				+ "T2.DEL_FLG = '0' AND T2.STATUS = '0' AND "
				+ " NAME = ?)) AND T.DEL_FLG = '0'";
		String firstStepId = "1";
		try {
			conn = WfDBConnection.getConnection();
			pstmt1 = conn.prepareStatement(COMMIT_STEP);
			pstmt1.setString(1, workflowName);
			rs1 = pstmt1.executeQuery();
			if (rs1.next()) {
				commitSteps = rs1.getString(1);
				commitStep = commitSteps.split(",");
				if (commitStep == null || commitStep.length < 1) {
					return new ArrayList();
				}
				for (int i = 0; i < commitStep.length; i++) {
					pstmt2 = conn.prepareStatement(STEP_NAME);
					pstmt2.setString(1, commitStep[i]);
					pstmt2.setString(2, workflowName);
					rs2 = pstmt2.executeQuery();
					if (rs2.next()) {
						WorkflowStep step = new WorkflowStep();
						step.setId(commitStep[i]);
						step.setSpecialObject(rs2.getString("POINT_STEP"));
						step.setName(rs2.getString("STEP_NAME"));
						step.setPointType(rs2.getString("HELPMAN"));
						step.setActionId(firstStepId + i);
						resultList.add(step);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {

			if (rs2 != null) {
				rs2.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (pstmt2 != null) {
				pstmt2.close();
			}
			if (pstmt1 != null) {
				pstmt1.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		if (!WfCommonTools.isHasPopedom(SecurityContextInfo.getCurrentUser(),
				workflowName, "1"))
			resultList = new ArrayList();
		return resultList;
	}

	public boolean isLaster(WorkflowInfo wfInfo) throws Exception {
		boolean flag = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT OID FROM TBL_WF_USERSIGN WHERE PROCESSED = 'N' AND TASK_ID = ?";

		try {
			conn = WfDBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			if (StringUtils.isBlank(wfInfo.getTaskId())) {
				return false;
			}
			pstmt.setString(1, wfInfo.getTaskId());
			rs = pstmt.executeQuery();
			int n = 0;
			while (rs.next()) {
				n++;
			}
			if (1 == n) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return flag;
	}

	public static Set getPointedUserOfStep(String taskId) throws Exception {
		Set resultSet = new HashSet();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM TBL_WF_USERSIGN WHERE TASK_ID = ? AND PROCESSED = 'N'";
		try {
			conn = WfDBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			if (StringUtils.isBlank(taskId)) {
				return new HashSet();
			}
			pstmt.setString(1, taskId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String empCd = rs.getString("PROCESS_ID");
				String roleSignRole = rs.getString("ROLE_SIGN_FLAG");
				String roles = rs.getString("ROLE_SIGN_ROLE");
				if ("Y".equals(roleSignRole)) {
					return ObjectOperateTools.toSet(WfCommonTools
							.getEmpOfRole(roles));
				} else {
					resultSet.add(empCd);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

		return resultSet;
	}

	public WorkflowInfo getSubWfInfo(WorkflowRelation wr) throws Exception {
		WorkflowInfo wfInfo = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String RELATION_ID = "SELECT SUB_OID,SUB_CLASS FROM WORKFLOW_RELATION WHERE STATUS = '0' AND SUPER_OID = ? AND SUPER_CLASS = ?";
		try {
			conn = WfDBConnection.getConnection();
			pstmt = conn.prepareStatement(RELATION_ID);
			pstmt.setString(1, wr.getSuperOid());
			pstmt.setString(2, wr.getSuperClass());

			rs = pstmt.executeQuery();

			if (rs.next()) {
				String subOid = rs.getString("SUB_OID");
				String subClassName = rs.getString("SUB_CLASS");

				wfInfo = (WorkflowInfo) ((CommonService) ServiceProvider
						.getService(CommonService.SERVICE_NAME)).load(Class
						.forName(subClassName), subOid);
				return wfInfo;
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("get sub workflow information.");
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return wfInfo;
	}
	//通过父流信息获得子流状态信息  2008-7-4 yangxl start
	public String getSubwfInfoStatus(WorkflowRelation wr) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String status = "";
		String RELATION_ID = "SELECT STATUS FROM WORKFLOW_RELATION WHERE SUPER_OID = ? AND SUPER_CLASS = ?  AND STEP_ID = ?";
		try{
			conn = WfDBConnection.getConnection();
			pstmt = conn.prepareStatement(RELATION_ID);
			pstmt.setString(1, wr.getSuperOid());
			pstmt.setString(2, wr.getSuperClass());
			pstmt.setString(3, wr.getStepId());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				status = rs.getString("STATUS");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return status;
	}
	/*public List getFinishedSubWfInfo(WorkflowRelation wr) throws Exception {
		List wfInfos = new ArrayList();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String RELATION_ID = "SELECT SUB_OID,SUB_CLASS FROM WORKFLOW_RELATION WHERE STATUS = '1' AND SUPER_OID = ? AND SUPER_CLASS = ?";
		try {
			conn = WfDBConnection.getConnection();
			pstmt = conn.prepareStatement(RELATION_ID);
			pstmt.setString(1, wr.getSuperOid());
			pstmt.setString(2, wr.getSuperClass());

			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				String subOid = rs.getString("SUB_OID");
				String subClassName = rs.getString("SUB_CLASS");

				WorkflowInfo wfInfo = (WorkflowInfo) ((CommonService) ServiceProvider
						.getService(CommonService.SERVICE_NAME)).load(Class
						.forName(subClassName), subOid);
				wfInfos.add(wfInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("get sub workflow information.");
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return wfInfos;
	}*/
	
	public WorkflowInfo getSuperWfInfo(WorkflowRelation wr) throws Exception {
		WorkflowInfo wfInfo = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String RELATION_ID = "SELECT SUPER_OID,SUPER_CLASS,UNLOCK_ID FROM WORKFLOW_RELATION WHERE STATUS = '0' AND SUB_OID = ? AND SUB_CLASS = ?";
		try {
			conn = WfDBConnection.getConnection();
			pstmt = conn.prepareStatement(RELATION_ID);

			pstmt.setString(1, wr.getSubOid());
			pstmt.setString(2, wr.getSubClass());

			rs = pstmt.executeQuery();

			if (rs.next()) {
				String superOid = rs.getString("SUPER_OID");
				String superClassName = rs.getString("SUPER_CLASS");

				String unlockId = rs.getString("UNLOCK_ID");

				wfInfo = (WorkflowInfo) ((CommonService) ServiceProvider
						.getService(CommonService.SERVICE_NAME)).load(Class
						.forName(superClassName), superOid);

				wfInfo.setActionId(unlockId);
				return wfInfo;
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("get super workflow information.");
		} finally {
			if (rs != null) {
				rs.close();
			}

			if (pstmt != null) {
				pstmt.close();
			}

			if (conn != null) {
				conn.close();
			}
		}
		return wfInfo;
	}

	public void endSubWorkflow(WorkflowRelation wr) throws Exception {
		PreparedStatement pstmt = null;
		String SET_STATUS = "UPDATE WORKFLOW_RELATION SET STATUS = '1' WHERE STATUS = '0' AND SUB_OID = ? AND SUB_CLASS = ?";
		try {
			pstmt = getTransactionConn().prepareStatement(SET_STATUS);
			pstmt.setString(1, wr.getSubOid());
			pstmt.setString(2, wr.getSubClass());

			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {

			if (pstmt != null) {
				pstmt.close();
			}

		}
	}
	
	//如果子流不存在,添加yangxl 增 2008-7-9 start
	public void addSuperSubWorkflow(WorkflowRelation wr) throws Exception {
		//添加
		PreparedStatement pstmt = null;
		String sql = "INSERT INTO WORKFLOW_RELATION "
				+ "(ID,SUPER_OID,SUPER_CLASS,SUB_OID,SUB_CLASS,UNLOCK_ID,STATUS,STEP_ID) "
				+ "VALUES(?,?,?,?,?,?,?,?)";

		try {
			pstmt = getTransactionConn().prepareStatement(sql);
			String id = Tools.getPKCode();
			pstmt.setString(1, id);
			pstmt.setString(2, wr.getSuperOid());
			pstmt.setString(3, wr.getSuperClass());
			pstmt.setString(4, wr.getSubOid());
			pstmt.setString(5, wr.getSubClass());
			pstmt.setString(6, wr.getUnlockId());
			pstmt.setString(7, "0");
			pstmt.setString(8, wr.getStepId());//
			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {

			if (pstmt != null) {
				pstmt.close();
			}

		}
	}
	//end
	//如果子流已存在,修改子流状态yangxl 增 2008-7-9 start
	public void updateSuperSubWorkflow(WorkflowRelation wr) throws Exception {
		PreparedStatement pstmt = null;
		String SET_STATUS = "UPDATE WORKFLOW_RELATION SET STATUS = '0',SUB_OID= ? ,SUB_CLASS = ?, UNLOCK_ID = ? WHERE  SUPER_OID = ? AND SUPER_CLASS = ?  AND STEP_ID = ?";
		try {
			pstmt = getTransactionConn().prepareStatement(SET_STATUS);
			pstmt.setString(1, wr.getSubOid());
			pstmt.setString(2, wr.getSubClass());
			pstmt.setString(3, wr.getUnlockId());
			pstmt.setString(4, wr.getSuperOid());
			pstmt.setString(5, wr.getSuperClass());
			pstmt.setString(6, wr.getStepId());

			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {

			if (pstmt != null) {
				pstmt.close();
			}

		}
	}
	//end
	//查询是否有提交到子流的信息 yangxl 增 2008-7-9 start
	public boolean getSubrowWfinfo(WorkflowRelation wr) throws SQLException{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean bool = false;
		String RELATION_ID = "SELECT STATUS FROM WORKFLOW_RELATION WHERE SUPER_OID = ? AND SUPER_CLASS = ?  AND STEP_ID = ?";
		try{
			conn = WfDBConnection.getConnection();
			pstmt = conn.prepareStatement(RELATION_ID);
			pstmt.setString(1, wr.getSuperOid());
			pstmt.setString(2, wr.getSuperClass());
			pstmt.setString(3, wr.getStepId());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				bool = true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return bool;
	}
	//end
	//提交到子流操作workflow_relation表 yangxl 改 2008-7-9 start
	public void doRelation(WorkflowRelation wr) throws Exception {
		
		boolean bool = false;
		bool = getSubrowWfinfo(wr);
		if(bool){
			//修改
			updateSuperSubWorkflow(wr);
		}else{
			//添加
			addSuperSubWorkflow(wr);
		}
		
	}
	//end
	public static String getRoles(String workflowName, String stepId)
			throws Exception {
		if (StringUtils.isBlank(workflowName) || StringUtils.isBlank(stepId)) {
			return "";
		}
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT SUB_T.TBL_USER_ID ROLES FROM TBL_WF_OPERATION_INFO SUP_T ,TBL_WF_PARTICULAR_INFO SUB_T "
				+ "WHERE SUP_T.ID = SUB_T.TBL_WF_OPE_INF_ID "
				+ "AND NAME = ? "
				+ "AND SUB_T.DEL_FLG = '0' "
				+ "AND SUP_T.DEL_FLG = '0' "
				+ "AND SUB_T.STEP_ID = ? "
				+ "AND SUP_T.STATUS = '0' "
				+ "ORDER BY SUP_T.CREATED_TIME DESC";
		try {
			conn = WfDBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, workflowName);
			pstmt.setString(2, stepId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				String roles = rs.getString("ROLES");
				return roles;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return "";
	}

	// 根据empCd取empId
	public static String getEmpId(String empCd) {
		TblCmnUser user = ((UserManageService) ServiceProvider
				.getService(UserManageService.SERVICE_NAME)).getUser(empCd);
		if (user != null && user != null) {
			return user.getId();
		}
		return null;
	}

	// 根据empCd取empName
	public static String getEmpName(String empCd) {
		TblCmnUser user = ((UserManageService) ServiceProvider
				.getService(UserManageService.SERVICE_NAME)).getUser(empCd);
		if (user != null && user != null) {
			return user.getEmpName();
		}
		return null;
	}

	public String getPassRoundNextStepId(String workflowName,
			String passRoundStepId) {
		if (StringUtils.isBlank(workflowName)
				|| StringUtils.isBlank(passRoundStepId)) {
			return "";
		}
		TblWfOperationInfo twoi = new TblWfOperationInfo();
		TblWfOperationInfo twoi2 = new TblWfOperationInfo();
        //	 modifiey by gejianbao;
		CodeWrapperPlus cwp=new CodeWrapperPlus();
		cwp.setCode("0");
		twoi.setName(workflowName);
		twoi.setStatus(cwp);
		List wfList = ((WFService) ServiceProvider
				.getService(WFService.SERVICE_NAME)).findsearch(twoi);
		if (wfList != null && wfList.size() > 0) {
			twoi2 = (TblWfOperationInfo) wfList.get(0);
		}
		Set stepSet = twoi2.getTblWfParticularInfos();
		Iterator it = stepSet.iterator();

		while (it.hasNext()) {
			TblWfParticularInfo step = (TblWfParticularInfo) it.next();
			if (passRoundStepId.equals(String.valueOf(step.getStepId()))) {
				if (StringUtils.isNotBlank(step.getCommitStep())) {
					return step.getCommitStep().split(",")[0];
				}
			}
		}
		return "";
	}

	public String getStepName(String workflowName, String stepId) {
		if (StringUtils.isBlank(workflowName) || StringUtils.isBlank(stepId)) {
			return "";
		}
		TblWfOperationInfo twoi = new TblWfOperationInfo();
		TblWfOperationInfo twoi2 = new TblWfOperationInfo();
        //	 modifiey by gejianbao;
		CodeWrapperPlus cwp=new CodeWrapperPlus();
		cwp.setCode("0");
		twoi.setName(workflowName);
		twoi.setStatus(cwp);
		List wfList = ((WFService) ServiceProvider
				.getService(WFService.SERVICE_NAME)).findsearch(twoi);
		if (wfList != null && wfList.size() > 0) {
			twoi2 = (TblWfOperationInfo) wfList.get(0);
		}
		Set stepSet = twoi2.getTblWfParticularInfos();
		Iterator it = stepSet.iterator();

		while (it.hasNext()) {
			TblWfParticularInfo step = (TblWfParticularInfo) it.next();
			if (stepId.equals(String.valueOf(step.getStepId()))) {
				if (StringUtils.isNotBlank(step.getCommitStep())) {
					return step.getStepName();
				}
			}
		}
		return "";
	}

	public String getPassRoundNextRole(String workflowName,
			String passRoundStepId) throws Exception {
		if (StringUtils.isBlank(workflowName)
				|| StringUtils.isBlank(passRoundStepId)) {
			return "";
		}
		TblWfOperationInfo twoi = new TblWfOperationInfo();
		TblWfOperationInfo twoi2 = new TblWfOperationInfo();
        //	 modifiey by gejianbao;
		CodeWrapperPlus cwp=new CodeWrapperPlus();
		cwp.setCode("0");
		twoi.setName(workflowName);
		twoi.setStatus(cwp);
		List wfList = ((WFService) ServiceProvider
				.getService(WFService.SERVICE_NAME)).findsearch(twoi);
		if (wfList != null && wfList.size() > 0) {
			twoi2 = (TblWfOperationInfo) wfList.get(0);
		}
		Set stepSet = twoi2.getTblWfParticularInfos();
		Iterator it = stepSet.iterator();

		while (it.hasNext()) {
			TblWfParticularInfo step = (TblWfParticularInfo) it.next();
			if (passRoundStepId.equals(String.valueOf(step.getStepId()))) {
				if (StringUtils.isNotBlank(step.getCommitStep())) {
					String roles = getRoles(workflowName, step.getCommitStep()
							.split(",")[0]);
					if (roles != null) {
						return roles.split(",")[0];
					}
				}
			}
		}
		return "";
	}

	public int getEntryCount(WfSearch search) throws Exception {
		boolean flag = false;
		int resultVal = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT COUNT(*) AS CNT FROM ("
				+ "SELECT * FROM (SELECT BASE.*,CALLER.EMP_NAME FROM (SELECT * FROM (SELECT TBL.*,HISTORY.CALLER COMMITER "
				+ "FROM (SELECT T.ID AS ID, T.NAME AS NAME, T.STATE AS STATE,"
				+ "P.STRING_VALUE AS OID, 'oid_' || P.STRING_VALUE AS GLOBALKEY, "
				+ "NVL(C.ID, 0) AS STEPID, NVL(C.ENTRY_ID, 0) AS ENTRY_ID, NVL(C.STEP_ID, 0) AS STEP_ID, "
				+ "C.START_DATE AS COMMIT_TIME, C.FINISH_DATE,NVL(C.STATUS, 0) AS CSTATUS "
				+ "FROM OS_WFENTRY T, OS_CURRENTSTEP C, OS_PROPERTYENTRY P WHERE C.ENTRY_ID = T.ID "
				+ "AND P.GLOBAL_KEY = 'osff_' || T.ID AND P.ITEM_KEY = 'dto.id') TBL, "
				+ "(SELECT H.ENTRY_ID AS ID,H.CALLER CALLER FROM OS_HISTORYSTEP H WHERE H.START_DATE "
				+ "IN (SELECT MIN(START_DATE) FROM OS_HISTORYSTEP GROUP BY ENTRY_ID) AND H.STEP_ID = 1) HISTORY "
				+ "WHERE TBL.ID = HISTORY.ID(+)) TEMP,(SELECT NAME AS NAME2,CLASS_NAME,OPER_NAME "
				+ "FROM TBL_WF_OPERATION_INFO WHERE CREATED_TIME IN (SELECT MAX(CREATED_TIME) "
				+ "FROM (SELECT * FROM TBL_WF_OPERATION_INFO WHERE STATUS = 0 AND DEL_FLG = 0) "
				+ "GROUP BY NAME) AND CLASS_NAME IS NOT NULL) TEMP2 WHERE TEMP.NAME = TEMP2.NAME2(+) )"
				+ "BASE, (SELECT EMP_NAME,EMP_CD FROM TBL_CMN_USER) CALLER WHERE NVL(BASE.COMMITER, '0001') = CALLER.EMP_CD ORDER BY COMMIT_TIME DESC)";
		String postSql = " ) S";
		StringBuffer buf = new StringBuffer(sql);
		buf.append(" WHERE");
		java.sql.Date startTime = null;
		java.sql.Date endTime = null;
		try {
			if (search != null) {
				if (StringUtils.isNotBlank(search.getState())) {
					flag = true;
					buf.append(" CSTATUS = '" + search.getState() + "' AND");
				}
				if (StringUtils.isNotBlank(search.getCommitter())) {
					flag = true;
					buf.append(" EMP_NAME LIKE '%" + search.getCommitter()
							+ "%' AND");
				}
				if (StringUtils.isNotBlank(search.getStartTime())
						&& StringUtils.isNotBlank(search.getEndTime())) {
					flag = true;
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					java.util.Date start = sdf.parse(search.getStartTime());
					java.util.Date end = sdf.parse(search.getEndTime());
					startTime = new java.sql.Date(start.getTime());
					endTime = new java.sql.Date(end.getTime());
					buf.append(" ? < COMMIT_TIME AND COMMIT_TIME < ? AND");
				}
				if (StringUtils.isNotBlank(search.getTitle())) {
					flag = true;
					buf.append(" OPER_NAME LIKE '%" + search.getTitle()
							+ "%' AND");
				}
			}
			if (flag) {
				String sqlWithoutLastAnd = buf.toString();
				sql = sqlWithoutLastAnd.substring(0,
						sqlWithoutLastAnd.length() - 3);
			}
			// System.out.println(sql + postSql);

			conn = WfDBConnection.getConnection();
			// conn = WfDBConnection.getLocalConnection();
			pstmt = conn.prepareStatement(sql + postSql);
			if (startTime != null && endTime != null) {
				pstmt.setDate(1, startTime);
				pstmt.setDate(2, endTime);
			}
			rs = pstmt.executeQuery();
			if (rs.next()) {
				String count = rs.getString("CNT");
				resultVal = Integer.parseInt(count);
				return resultVal;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return resultVal;
	}

	public List findFlowEntry(WfSearch search, int pageNo, int pageSize)
			throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		List resultList = new ArrayList();
		String preSql = "SELECT * FROM (SELECT S.*,ROWNUM R FROM (";
		String postSql = ") S WHERE ROWNUM <= ?) WHERE R >= ?";// <=max(10),>=min(6)
		String sql = "SELECT * FROM (SELECT BASE.*,CALLER.EMP_NAME FROM (SELECT * FROM (SELECT TBL.*,HISTORY.CALLER COMMITER "
				+ "FROM (SELECT T.ID AS ID, T.NAME AS NAME, T.STATE AS STATE,"
				+ "P.STRING_VALUE AS OID, 'oid_' || P.STRING_VALUE AS GLOBALKEY, "
				+ "NVL(C.ID, 0) AS STEPID, NVL(C.ENTRY_ID, 0) AS ENTRY_ID, NVL(C.STEP_ID, 0) AS STEP_ID, "
				+ "C.START_DATE AS COMMIT_TIME, C.FINISH_DATE,NVL(C.STATUS, 0) AS CSTATUS "
				+ "FROM OS_WFENTRY T, OS_CURRENTSTEP C, OS_PROPERTYENTRY P WHERE C.ENTRY_ID = T.ID "
				+ "AND P.GLOBAL_KEY = 'osff_' || T.ID AND P.ITEM_KEY = 'dto.id') TBL, "
				+ "(SELECT H.ENTRY_ID AS ID,H.CALLER CALLER FROM OS_HISTORYSTEP H WHERE H.START_DATE "
				+ "IN (SELECT MIN(START_DATE) FROM OS_HISTORYSTEP GROUP BY ENTRY_ID) AND H.STEP_ID = 1) HISTORY "
				+ "WHERE TBL.ID = HISTORY.ID(+)) TEMP,(SELECT NAME AS NAME2,CLASS_NAME,OPER_NAME "
				+ "FROM TBL_WF_OPERATION_INFO WHERE CREATED_TIME IN (SELECT MAX(CREATED_TIME) "
				+ "FROM (SELECT * FROM TBL_WF_OPERATION_INFO WHERE STATUS = 0 AND DEL_FLG = 0) "
				+ "GROUP BY NAME) AND CLASS_NAME IS NOT NULL) TEMP2 WHERE TEMP.NAME = TEMP2.NAME2(+) )"
				+ "BASE, (SELECT EMP_NAME,EMP_CD FROM TBL_CMN_USER) CALLER WHERE NVL(BASE.COMMITER, '0001') = CALLER.EMP_CD ORDER BY COMMIT_TIME DESC)";
		StringBuffer buf = new StringBuffer(sql);
		buf.append(" WHERE");
		java.sql.Date startTime = null;
		java.sql.Date endTime = null;

		int last = pageSize * pageNo;
		int first = pageSize * (pageNo - 1) + 1;
		try {
			if (search != null) {
				if (StringUtils.isNotBlank(search.getState())) {
					flag = true;
					buf.append(" CSTATUS = '" + search.getState() + "' AND");
				}
				if (StringUtils.isNotBlank(search.getCommitter())) {
					flag = true;
					buf.append(" EMP_NAME LIKE '%" + search.getCommitter()
							+ "%' AND");
				}
				if (StringUtils.isNotBlank(search.getStartTime())
						&& StringUtils.isNotBlank(search.getEndTime())) {
					flag = true;
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					java.util.Date start = sdf.parse(search.getStartTime());
					java.util.Date end = sdf.parse(search.getEndTime());
					startTime = new java.sql.Date(start.getTime());
					endTime = new java.sql.Date(end.getTime());
					buf.append(" ? < COMMIT_TIME AND COMMIT_TIME < ? AND");
				}
				if (StringUtils.isNotBlank(search.getTitle())) {
					flag = true;
					buf.append(" OPER_NAME LIKE '%" + search.getTitle()
							+ "%' AND");
				}
			}
			if (flag) {
				String sqlWithoutLastAnd = buf.toString();
				sql = sqlWithoutLastAnd.substring(0,
						sqlWithoutLastAnd.length() - 3);
			}
			// System.out.println(sql);
			// System.out.println(preSql + sql + postSql);
			conn = WfDBConnection.getConnection();
			// conn = WfDBConnection.getLocalConnection();
			pstmt = conn.prepareStatement(preSql + sql + postSql);
			if (startTime != null && endTime != null) {
				pstmt.setDate(1, startTime);
				pstmt.setDate(2, endTime);
				pstmt.setInt(3, last);
				pstmt.setInt(4, first);
			} else {
				pstmt.setInt(1, last);
				pstmt.setInt(2, first);
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				WfEntry entry = new WfEntry();
				resultList.add(entry);
				// entry.setCommiter(WfUtils.getEmpName(rs.getString("COMMITER")));
				entry.setCommiter(rs.getString("EMP_NAME"));

				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				java.util.Date commitDate = sdf.parse(rs
						.getString("COMMIT_TIME"));
				String commitTime = sdf.format(commitDate);

				entry.setCommitTime(commitTime);
				entry.setTaskId(null);
				String workflowName = rs.getString("OPER_NAME");
				entry.setWfName(workflowName == null ? "没有定义流程名称"
						: workflowName);
                //temp用于封装wfname原始数据 英文名
				String temp  = rs.getString("NAME");
				entry.setTemp(temp);
				
				entry.setWfState(WorkflowUtils.getWorkflowStatusName(rs
						.getString("CSTATUS")));

				entry.setOid(rs.getString("OID"));
				WorkflowInfo wfInfo = null;
				try {
					wfInfo = (WorkflowInfo) ((CommonService) ServiceProvider
							.getService(CommonService.SERVICE_NAME)).load(Class
							.forName(rs.getString("CLASS_NAME")), entry
							.getOid());
					entry.setCurrentStepName(getStepName(wfInfo
							.getWorkflowName(), rs.getString("STEP_ID")));
					//需求对应，增加步骤ID
					entry.setStepId(rs.getString("STEP_ID"));
					
					entry.setTitle(entry.getWfName() + "_"
							+ entry.getCurrentStepName());
					entry.setUrl(wfInfo.getWorkflowEditPath(entry.getOid()));
				} catch (Exception inE) {
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("findFlowEntry.");
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

	public static void main(String[] args) throws Exception {
		WfUtils util = new WfUtils();
		WfSearch search = new WfSearch();
		search.setStartTime("2008-01-22 00:00:00");
		search.setEndTime("2008-12-11 00:00:00");
		search.setState("processing");
		search.setTitle(" 物资 ");

		List list = util.findFlowEntry(search, 2, 10);
		for (int i = 0; i < list.size(); i++) {
			WfEntry entry = (WfEntry) list.get(i);
			System.out.print("名称 : " + entry.getTitle());
			System.out.print("\t提交者 : " + entry.getCommiter());
			System.out.print("\t提交时间 : " + entry.getCommitTime());
			System.out.print("\t当前步骤" + entry.getCurrentStepName());
			System.out.print("\t状态 : " + entry.getWfState());
			System.out.print("\turl : " + entry.getUrl());
			System.out.println();
		}
		System.out.println(util.getEntryCount(search));

		// WorkflowRelation wr = new WorkflowRelation();
		// wr.setSuperOid("12341234123");
		// wr.setSuperClass("com.qware.am.domain.TblAmWoHotmachticket");
		// WorkflowInfo info = util.getSubWfInfo(wr);
	}

	public boolean isPassRoundWithoutSigner(String workflowName, String stepId) {
		if (StringUtils.isBlank(workflowName) || StringUtils.isBlank(stepId)) {
			return false;
		}
		TblWfOperationInfo twoi = new TblWfOperationInfo();
		TblWfOperationInfo twoi2 = new TblWfOperationInfo();
        //	 modifiey by gejianbao;
		CodeWrapperPlus cwp=new CodeWrapperPlus();
		cwp.setCode("0");
		twoi.setName(workflowName);
		twoi.setStatus(cwp);
		List wfList = ((WFService) ServiceProvider
				.getService(WFService.SERVICE_NAME)).findsearch(twoi);
		if (wfList != null && wfList.size() > 0) {
			twoi2 = (TblWfOperationInfo) wfList.get(0);
		}
		Set stepSet = twoi2.getTblWfParticularInfos();
		Iterator it = stepSet.iterator();

		while (it.hasNext()) {
			TblWfParticularInfo step = (TblWfParticularInfo) it.next();
			if (stepId.equals(String.valueOf(step.getStepId()))) {
				if (WorkflowConstants.POINT_ROLE.equals(step.getHelpman())
						&& "Y".equals(step.getTogethersign())) {
					return true;
				}
			}
		}
		return false;
	}

	public static TblWfParticularInfo getStep(String workflowName, String stepId) {
		if (StringUtils.isBlank(workflowName) || StringUtils.isBlank(stepId)) {
			return new TblWfParticularInfo();
		}
		TblWfOperationInfo twoi = new TblWfOperationInfo();
		TblWfOperationInfo twoi2 = new TblWfOperationInfo();
        //	 modifiey by gejianbao;
		CodeWrapperPlus cwp=new CodeWrapperPlus();
		cwp.setCode("0");
		twoi.setName(workflowName);
		twoi.setStatus(cwp);
		List wfList = ((WFService) ServiceProvider
				.getService(WFService.SERVICE_NAME)).findsearch(twoi);
		if (wfList != null && wfList.size() > 0) {
			twoi2 = (TblWfOperationInfo) wfList.get(0);
		}
		Set stepSet = twoi2.getTblWfParticularInfos();
		Iterator it = stepSet.iterator();

		while (it.hasNext()) {
			TblWfParticularInfo step = (TblWfParticularInfo) it.next();
			if (stepId.equals(String.valueOf(step.getStepId()))) {
				return step;
			}
		}
		return new TblWfParticularInfo();
	}

	public boolean hasSubFlow(WorkflowInfo supInfo) throws Exception {
		boolean flag = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String RELATION_ID = "SELECT * FROM WORKFLOW_RELATION WHERE STATUS = '0' AND SUPER_OID = ? AND SUPER_CLASS = ?";
		try {
			conn = WfDBConnection.getConnection();
			pstmt = conn.prepareStatement(RELATION_ID);
			pstmt.setString(1, supInfo.getId());
			pstmt.setString(2, WfUtils.getUnProxyClassName(supInfo.getClass()));

			rs = pstmt.executeQuery();

			if (rs.next()) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("has sub workflow.");
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return flag;
	}

	public boolean hasSupFlow(WorkflowInfo subInfo) throws Exception {
		boolean flag = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String RELATION_ID = "SELECT * FROM WORKFLOW_RELATION WHERE STATUS = '0' AND SUB_OID = ? AND SUB_CLASS = ?";
		try {
			conn = WfDBConnection.getConnection();
			pstmt = conn.prepareStatement(RELATION_ID);
			pstmt.setString(1, subInfo.getId());
			pstmt.setString(2, WfUtils.getUnProxyClassName(subInfo.getClass()));

			rs = pstmt.executeQuery();

			if (rs.next()) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("has sup workflow information.");
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return flag;
	}

	public void setFinishData(WorkflowInfo workflowInfo) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sqlForSelect = new StringBuffer();
		sqlForSelect
				.append("SELECT TASK_ID FROM TBL_WF_USERSIGN WHERE OID = ? AND COMMIT_ID = ? AND PROCESSED = 'N' ");
		try {
			conn = WfDBConnection.getConnection();
			pstmt = conn.prepareStatement(sqlForSelect.toString());
			pstmt.setString(1, workflowInfo.getId());
			pstmt.setString(2, SecurityContextInfo
					.getCurrentUser().getEmpCd());
			rs = pstmt.executeQuery();
			if (!rs.next()) {
				List signerList = workflowInfo.getSignerList();
				FlowTaskInfo fti = new FlowTaskInfo();
				fti.setRoleSignFlag("Y");
				signerList.add(fti);
				workflowInfo.setSignerList(signerList);
				setTransactionConn(conn);
				setSigner(workflowInfo);
				setFinishData(workflowInfo);
			} else {
				StringBuffer sqlForUpdate = new StringBuffer();
				sqlForUpdate
						.append("UPDATE TBL_WF_USERSIGN SET PROCESSED = 'Y' ");
				sqlForUpdate
						.append("WHERE TASK_ID = ?  AND COMMIT_ID = ? AND OID = ? AND PROCESSED = 'N' ");
				pstmt = conn.prepareStatement(sqlForUpdate.toString());
				pstmt.setString(1, rs.getString("TASK_ID"));
				pstmt.setString(2, SecurityContextInfo
						.getCurrentUser().getEmpCd());
				pstmt.setString(3, workflowInfo.getId());

				pstmt.executeUpdate();
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (conn != null)
				conn.close();
		}
	}
}