/**
 * Name: WorkflowBacthTools.java
 * Author: Bis liyan
 */
package net.silencily.sailing.basic.wf.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.silencily.sailing.basic.wf.db.WfDBConnection;

/**
 * The batch tools for workflow 工作流批处理工具
 * 
 * @author Bis liyan
 */
public class WorkflowBatchTools {
	
	public static void main(String[] args) throws Exception{	
		updateTaskNameToTbl_usersign(WfDBConnection.getLocalConnection());
	}
	/**
	 * Update task name to tbl_usersign 更新任务名称到tbl_usersign表里
	 * 
	 * @param conn
	 *            Database's connection
	 * @throws Exception	
	 */
	public static void updateTaskNameToTbl_usersign(Connection conn) throws Exception {
		try {
			conn.setAutoCommit(false);
			String sql = "select id,nvl(workflow_name, '') as workflow_name,nvl(to_step_name, '') as to_step_name from tbl_wf_usersign";
			String update = "update tbl_wf_usersign set task_name = ? , workflow_cnname = ? where id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			PreparedStatement pstmt2 =conn.prepareStatement(update);
			while (rs.next()) {
				String id = rs.getString("id");
				
				String workflowCnName = WfCommonTools.getWorkflowCnName(rs.getString("workflow_name"));
				pstmt2.setString(1, workflowCnName + "_" + (rs.getString("to_step_name") == null ? "" : rs.getString("to_step_name")));
				pstmt2.setString(2, workflowCnName);
				pstmt2.setString(3, id);
				pstmt2.addBatch();					
			}
			pstmt2.executeBatch();
			conn.commit();
		} catch(Exception e) {
			conn.rollback();
			e.printStackTrace();
			throw new Exception();
		} finally {
			conn.setAutoCommit(true);
			if (conn != null)
				conn.close();
		}
	}
	/**
	 * Update pojo's class name to tbl_oper 更新pojo的类名到tbl_oper表里
	 * 
	 * @param conn
	 *            Database's connection
	 * @throws Exception	
	 */
	public static void updateClassNameToTblOper(Connection conn) throws Exception {
		try {
			conn.setAutoCommit(false);
			PreparedStatement pstmt1 = conn.prepareStatement("select workflow_name,class_name from tbl_wf_usersign group by workflow_name,class_name");
			ResultSet rs1 = pstmt1.executeQuery();
			List list1 = new ArrayList();
			while (rs1.next()) {
				Map map = new HashMap();
				map.put(rs1.getString("workflow_name"), rs1.getString("class_name"));
				list1.add(map);
			}
			PreparedStatement pstmt2 = conn.prepareStatement("select id,name from tbl_wf_operation_info t where t.status = 0 and del_flg = 0");
			ResultSet rs2 = pstmt2.executeQuery();
			List needUpdateIds = new ArrayList();
			
			while (rs2.next()) {
				Map map = new HashMap();
				map.put(rs2.getString("name"), rs2.getString("id"));
				needUpdateIds.add(map);
			}
			for (int i=0; i<needUpdateIds.size(); i++){
				Map map1 = (Map)needUpdateIds.get(i);
				Set set1 = map1.keySet();
				Iterator it = set1.iterator();
				if (it.hasNext()) {
					String wf_name = (String)it.next();
					String id = (String)map1.get(wf_name);
					for (int j=0; j<list1.size(); j++){
						Map map2 = (Map)list1.get(j);
						Set set2 = map2.keySet();
						Iterator it2 = set2.iterator();
						if (it2.hasNext()) {
							String wfname = (String)it2.next();
							String className = (String)map2.get(wfname);
							if (wf_name.equals(wfname)){
								//System.out.println(className + " ; " + id + " ; " + wfname);
								PreparedStatement pstmt3 = conn.prepareStatement("update tbl_wf_operation_info set class_name = ? where id = ?");
								pstmt3.setString(1, className);
								pstmt3.setString(2, id);
								pstmt3.executeUpdate();
							}
						}
					}
				}
			}			
			conn.commit();
		} catch(Exception e) {
			conn.rollback();
			e.printStackTrace();
			throw new Exception("update class name to tbl_oper");
		} finally {
			conn.setAutoCommit(true);
			if (conn != null) 
				conn.close();
		}	
	}

}
