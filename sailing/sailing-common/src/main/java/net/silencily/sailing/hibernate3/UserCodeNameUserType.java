package net.silencily.sailing.hibernate3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import net.silencily.sailing.framework.codename.UserCodeName;

import org.hibernate.HibernateException;


/**
 * 业务实体的属性引用{@link UserCodeName}作为属性时, 保存和检索时使用的转换器的<code>hibernate</code>
 * 实现, 保存时写入用户登录名, 检索时返回完整的{@link UserCodeName}, 在业务代码中不要直
 * 接使用{@link User}作为属性, 那里的<code>equals</code>方法与这里目的不同, 直接使用影
 * 响性能, 同时无法提供给视图使用的统一方式
 * @author zhangli
 * @version $Id: UserCodeNameUserType.java,v 1.1 2010/12/10 10:54:14 silencily Exp $
 * @since 2007-4-9
 */
public class UserCodeNameUserType extends net.silencily.sailing.framework.codename.impl.hibernate.UserCodeNameUserType {
    


    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
    	UserCodeName cn = (UserCodeName)super.nullSafeGet(rs, names, owner);
        cn.setName(getUsername(cn.getCode()));
        return cn;
    }

    private String getUsername(String id) {
    	Context ctx = null;
		DataSource ds = null;
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null; 
		StringBuffer sql = new StringBuffer();;
		List list = new ArrayList();
		String name = "";
		try {
			ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("jdbc/OracleDS");
			conn = ds.getConnection();
			sql.append("select t.emp_name from tbl_hr_emp_info t where t.emp_cd = '");
			sql.append(id).append("' and t.del_flg = '0'");
			if (conn != null) {
				statement = conn.prepareStatement(sql.toString());
				resultSet = statement.executeQuery();
				while (resultSet.next()) {
					Map map = new HashMap();
					map.put("emp_name", resultSet.getString("emp_name"));
					list.add(map);
				}
			}
		} catch (Exception e) {
			System.out.println("获取员工名称出错。");
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
			name = (String) ((Map) list.get(0)).get("emp_name");
		}
		return name;
	}
}
