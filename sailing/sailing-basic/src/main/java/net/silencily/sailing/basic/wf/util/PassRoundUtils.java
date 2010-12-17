/**
 * Name: PassRoundUtils.java
 * Author: Bis liyan
 */
package net.silencily.sailing.basic.wf.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import net.silencily.sailing.basic.wf.WorkflowInfo;
import net.silencily.sailing.basic.wf.db.WfDBConnection;
import net.silencily.sailing.basic.wf.dto.FlowTaskInfo;
import net.silencily.sailing.basic.wf.dto.WfEntry;
import net.silencily.sailing.basic.wf.dto.WfSearch;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.security.SecurityContextInfo;
import net.silencily.sailing.security.model.CurrentUser;
import net.silencily.sailing.utils.Tools;

import org.apache.commons.lang.StringUtils;

/**
 * The utiltools for workflow's passround 工作流传阅工具
 * 
 * @author Bis liyan
 */
public class PassRoundUtils {
	/**
	 * Find current user's passround task 获得当前用户的传阅任务
	 * 
	 * @param user
	 *            current's user
	 * @param wfsearch
	 *            search's condition
	 * @param pageNo
	 *            page's no
	 * @param pageSize
	 *            one page' size
	 * @throws Exception
	 * @return passround task's list
	 */
	public static List findPassRoundTask(CurrentUser user, WfSearch wfsearch,
			int pageNo, int pageSize) throws Exception {
		List resultList = new ArrayList();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String pre = "SELECT * FROM (SELECT S.*,ROWNUM R FROM(";
		String sql = "SELECT * FROM TBL_WF_USERSIGN WHERE TASK_TYPE = 'PASSROUND' AND PASSROUNDER_ID = ? ORDER BY COMMIT_TIME DESC";
		String post = ") S WHERE ROWNUM <= ? ) WHERE R >= ?";
		try {
			conn = WfDBConnection.getConnection();
			pstmt = conn.prepareStatement(pre + sql + post);
			pstmt.setString(1, user.getEmpCd());
			pstmt.setInt(2, pageNo * pageSize);
			pstmt.setInt(3, pageSize * (pageNo - 1) + 1);
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
					java.util.Date commitDate = sdf.parse(rs.getString("COMMIT_TIME"));
					String commitTime = sdf.format(commitDate);
					
					wfEntry.setCommiter(WfUtils.getEmpName(rs.getString("COMMIT_ID")));
					wfEntry.setCommitTime(commitTime);
					String className = rs.getString("CLASS_NAME");
					String title = rs.getString("TASK_NAME");
					
					WorkflowInfo wfInfo = null;
					wfInfo = (WorkflowInfo) ((CommonService) ServiceProvider
							.getService(CommonService.SERVICE_NAME)).load(Class
							.forName(className), oid);
					wfEntry.setCurrentStepName(wfInfo.getCurrentStep()
							.getName());
					wfInfo.setTaskId(taskId);
					String url = wfInfo.getWorkflowEditPath(oid);
					StringBuffer buf = new StringBuffer(
							null == url ? "not found url" : url);
					//buf.append("&taskId=" + taskId);
					wfEntry.setUrl(buf.toString());
					wfEntry.setWfName(wfInfo.getWorkflowName());
					wfEntry.setWfState(wfInfo.getWorkflowStatusName());
					//wfEntry.setTitle(WorkflowTaskTools.getTaskName(wfInfo, title)); 08-07-08 by liuzhuo
					wfEntry.setTitle(wfInfo.getTaskName(title));
				} catch (Exception e) {
					continue;
				}
			
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("find passround task");
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

	/**
	 * get passround task's count 获得当前用户的传阅任务个数
	 * 
	 * @param user
	 *            current's user
	 * @param wfsearch
	 *            search's condition
	 * @throws Exception
	 * @return passround task's count
	 */
	public static int getPassRoundTaskCount(CurrentUser user, WfSearch wfsearch)
			throws Exception {
		int count = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String pre = "SELECT COUNT(*) AS CNT FROM (";
		String sql = "SELECT * FROM TBL_WF_USERSIGN WHERE TASK_TYPE = 'PASSROUND' AND PASSROUNDER_ID = ? ";
		String post = ")";
		try {
			conn = WfDBConnection.getConnection();
			pstmt = conn.prepareStatement(pre + sql + post);
			pstmt.setString(1, user.getEmpCd());
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				count = rs.getInt("CNT");
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw new Exception("get passround task count");
		} finally {
			if (rs != null)
				rs.close();
			
			if (pstmt != null)
				pstmt.close();
			
			if (conn != null)
				conn.close();
		}
		return count;
	}

	/**
	 * get signer's list by step's id 得到特定步骤上的最新已办人列表
	 * 
	 * @param stepId
	 *            want passround step's id
	 * @param wfInfo
	 *            instance of workflow
	 * @throws Exception
	 * @return signer's list
	 */
	public static List getSignerListByStepId(String stepId, WorkflowInfo wfInfo)
			throws Exception {
		List resultList = new ArrayList();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String unWhenTs = "SELECT * FROM TBL_WF_USERSIGN WHERE TO_STEP_ID = ? AND OID = ? "
			+ "AND COMMIT_TIME = (SELECT MAX(COMMIT_TIME) FROM TBL_WF_USERSIGN "
			+ "WHERE TO_STEP_ID = ? AND OID = ?) ";
	    String whenTs = "SELECT * FROM TBL_WF_USERSIGN WHERE TASK_ID IN ("
			+ "SELECT TASK_ID FROM("
			+ unWhenTs + "))";
	    String sql = WfUtils.isTogetherSign(wfInfo.getWorkflowName(), stepId) ? whenTs
			: unWhenTs;
		try {
			conn = WfDBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, stepId);
			pstmt.setString(2, wfInfo.getId());
			pstmt.setString(3, stepId);
			pstmt.setString(4, wfInfo.getId());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				FlowTaskInfo fti = new FlowTaskInfo();
				fti.setProcessId(rs.getString("PROCESS_ID"));
				resultList.add(fti);
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw new Exception("get signer's list by step's id");
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

	/**
	 * clear one passround's task 清除当前用户的传阅任务
	 * 
	 * @param user
	 *            current's user
	 * @param taskId
	 *            task's id
	 * @throws Exception
	 */
	public static void clearPassRoundTask(CurrentUser user, String taskId)
			throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String clear = "DELETE FROM TBL_WF_USERSIGN "
				+ "WHERE TASK_ID = ? AND TASK_TYPE = 'PASSROUND' AND PASSROUNDER_ID = ?";
		try {
			conn = WfDBConnection.getConnection();
			pstmt = conn.prepareStatement(clear);
			pstmt.setString(1, taskId);
			pstmt.setString(2, user.getEmpCd());

			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("clear passround task");
		} finally {
			if (pstmt != null)
				pstmt.close();

			if (conn != null)
				conn.close();
		}
	}

	/**
	 * send passround's task 发送传阅任务
	 * 
	 * @param workflowInfo
	 *            instance of workflow
	 * @param signerList
	 *            signer's list
	 * @throws Exception
	 */
	public static void sendPassRoundTask(WorkflowInfo wfInfo, List signerList) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;	
		if (signerList == null) {
			signerList = new ArrayList();
		}
		String sql = "INSERT INTO TBL_WF_USERSIGN "
				+ "(ID,OID,WORKFLOW_NAME,CLASS_NAME,TASK_ID,FROM_TASK_ID,TASK_TYPE,PASSROUNDER_ID,COMMIT_ID,TASK_NAME) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?)";

		try {
			String taskId = Tools.getPKCode();
			String taskType = "PASSROUND";				
			conn = WfDBConnection.getConnection();
			conn.setAutoCommit(false);
			for (int i = 0; i < signerList.size(); i++) {
				FlowTaskInfo fti = (FlowTaskInfo) signerList.get(i);
				pstmt = conn.prepareStatement(sql);

				pstmt.setString(1, Tools.getPKCode());
				pstmt.setString(2, wfInfo.getId());
				pstmt.setString(3, wfInfo.getWorkflowName());			

				String className = wfInfo.getClass().getName();
				int x = className.indexOf(("$"));
				if (-1 != x) {
					className = className.substring(0, x);
				}
				pstmt.setString(4, className);
				pstmt.setString(5, taskId);
				if (StringUtils.isBlank(wfInfo.getTaskId())) {
					pstmt.setString(6, Tools.getPKCode());
				} else {
					pstmt.setString(6, wfInfo.getTaskId());
				}
				pstmt.setString(7, taskType);
				pstmt.setString(8, fti.getProcessId());
				pstmt.setString(9, SecurityContextInfo.getCurrentUser().getEmpCd());
				
				String taskName = WfCommonTools.getWorkflowCnName(wfInfo.getWorkflowName());				
				pstmt.setString(10, taskName);
				pstmt.executeUpdate();
			}
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace();
			throw new Exception();
		} finally {
			conn.setAutoCommit(true);
			if (pstmt != null) {
				pstmt.close();
			}
			
			if (conn != null) {
				conn.close();
			}
		}
	}
}
