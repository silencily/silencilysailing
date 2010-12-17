package net.silencily.sailing.common.dbtime;

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
public class DBTimeServiceImpl implements DBTimeService {

	public String getDBTime(){
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

	public static Connection getLocalConnection() throws Exception {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		return java.sql.DriverManager
				.getConnection("jdbc:oracle:thin:@172.16.2.142:1521:qware",
						"qware5", "qware5");
	}

	public static void main(String[] args) throws Exception {
		Connection conn = getLocalConnection();
		PreparedStatement pstmt1 = conn
				.prepareStatement("select sysdate from dual");
		ResultSet rs1 = pstmt1.executeQuery();
		List list1 = new ArrayList();
		while (rs1.next()) {
			Map map = new HashMap();
			map.put("sysdate", rs1.getString("sysdate"));
			list1.add(map);
		}
		String dateStr = (String) ((Map) list1.get(0)).get("sysdate");
		System.out.println(dateStr);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = simpleDateFormat.parse(dateStr);
		System.out.println(d);
	}
}
