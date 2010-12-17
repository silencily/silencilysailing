package net.silencily.sailing.basic.wf.util;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.silencily.sailing.basic.wf.WorkflowInfo;
import net.silencily.sailing.basic.wf.db.WfDBConnection;
import net.silencily.sailing.basic.wf.dto.ClonedObject;

import org.apache.commons.lang.StringUtils;

public class ChangeRecordTools {
	public static Map getSubListMap(ClonedObject oldBean, WorkflowInfo newBean) throws Exception{
		Map subListMap = new HashMap();
		Class cls = newBean.getClass();
		Field[] fields = cls.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			if (java.util.Set.class.isAssignableFrom(fields[i].getType())) {
				Map oldSubListMap = oldBean.getSubListMap();
				Set oldSubListSet = oldSubListMap.keySet();
				Iterator it = oldSubListSet.iterator();
				while (it.hasNext()) {
					String key = (String) it.next();
					if (key.equals(fields[i].getName())) {
						subListMap.put(oldSubListMap.get(key), ObjectOperateTools.toList((Set)ClassOperateTools.getValueByName(fields[i].getName(), newBean)));
					}
				}
			}
		}
		return subListMap;
	}

	public static String getRecords(Object oldBean, Object newBean)
			throws Exception {
		StringBuffer changeBuf = new StringBuffer();
		String stepChangeContent = "";

		boolean flg = false;
		if (oldBean != null && newBean != null) {
			changeBuf.append(MainBeanChangeRecord.getChangeRecord(
					(ClonedObject) oldBean, (WorkflowInfo) newBean));
			flg = true;
		}

		Map subListMap = getSubListMap((ClonedObject) oldBean, (WorkflowInfo) newBean);
		if (subListMap != null) {
			Set set = subListMap.keySet();
			Iterator it = set.iterator();
			while (it.hasNext()) {
				List oldList = (List) it.next();
				List newList = (List) subListMap.get(oldList);
				String subRecord = SubListChangeRecord.getChangeRecord(oldList,
						newList);
				changeBuf.append(flg ? "<br>" + subRecord : subRecord);
				flg = true;
			}
		}

		if (flg) {
			stepChangeContent = changeBuf.toString();
			if (stepChangeContent.length() > 4000) {
				stepChangeContent = stepChangeContent.substring(0, 3999);
			}
		}
		return stepChangeContent;
	}

	public static void saveOrUpdate(String pk, String record) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String update = "UPDATE WORKFLOW_STEP_RECORD SET RECORD = ? WHERE ID = ?";
		String insert = "INSERT WORKFLOW_STEP_RECORD VALUES(?,?)";
		try {
			conn = WfDBConnection.getConnection();
			if (StringUtils.isNotBlank(getRecord(pk))) {
				// do update
				pstmt = conn.prepareStatement(update);
				pstmt.setString(1, record);
				pstmt.setString(2, pk);
				pstmt.executeUpdate();
			} else {
				// do insert
				pstmt = conn.prepareStatement(insert);
				pstmt.setString(1, pk);
				pstmt.setString(2, record);
				pstmt.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();

			if (pstmt != null)
				pstmt.close();

			if (conn != null)
				conn.close();
		}

	}

	public static String getRecord(String pk) throws Exception {
		if (StringUtils.isBlank(pk)) {
			throw new Exception("the parm pk is null");
		}
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT RECORD FROM WORKFLOW_STEP_RECORD WHERE ID = ?";
		try {
			conn = WfDBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pk);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getString("RECORD");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();

			if (pstmt != null)
				pstmt.close();

			if (conn != null)
				conn.close();
		}
		return "";
	}
}
