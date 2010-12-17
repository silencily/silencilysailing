/**
 * 
 */
package net.silencily.sailing.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * 
 * @author wenjb
 * @version 1.0
 */
public class DBTimeUtil {

	public static String getDBTimeStr() {
		Context ctx = null;
		DataSource ds = null;
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String sql = null;
		List list = new ArrayList();
		String dateStr = null;
		try {
			ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/OracleDS");
			conn = ds.getConnection();
			sql = "select sysdate FROM dual";
			if (conn != null) {
				statement = conn.prepareStatement(sql);
				resultSet = statement.executeQuery();
				while (resultSet.next()) {
					Map map = new HashMap();
					map.put("sysdate", resultSet.getString("sysdate"));
					list.add(map);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("获取数据库时间出错，请与系统管理员联系。");
		} finally {
			if (null != resultSet) {
				try {
					resultSet.close();
				} catch (Exception e) {
					;
				}
				resultSet = null;
			}
			if (null != statement) {
				try {
					statement.close();
				} catch (Exception e) {
					;
				}
				statement = null;
			}
			if (null != conn) {
				try {
					conn.close();
				} catch (Exception e) {
					;
				}
				conn = null;
			}
		}
		if (list.size() != 0) {
			dateStr = (String) ((Map) list.get(0)).get("sysdate");
		}
		return dateStr;
	}
	
	public static Date getDBTime() {
		String dateStr = getDBTimeStr();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = simpleDateFormat.parse(dateStr);
		} catch (Exception e) {
			throw new RuntimeException("获取数据库时间出错，请与系统管理员联系。");
		}
		return date;
	}
}