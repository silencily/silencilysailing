package net.silencily.sailing.basic.wf.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.silencily.sailing.basic.wf.db.impl.SpringConnFactory;
import net.silencily.sailing.basic.wf.proxy.ProxyConnection;


public class WfDBConnection {
	public static Connection getConnection()throws SQLException{
		DBConnFactory dbcf = SpringConnFactory.buildFactory();
		Connection conn = dbcf.getConnection();		
		ProxyConnection pc = new ProxyConnection();			
		return pc.proxy(conn);
	}
	public static Connection getLocalConnection()throws Exception{
		Class.forName("oracle.jdbc.driver.OracleDriver");
		return java.sql.DriverManager.getConnection("jdbc:oracle:thin:@172.16.2.142:1521:qware", "qware5", "qware5");
	}
	public static void main(String[] args)throws Exception{
		//update class name.
		Connection conn = WfDBConnection.getLocalConnection();
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
		conn.close();		
	}
}
