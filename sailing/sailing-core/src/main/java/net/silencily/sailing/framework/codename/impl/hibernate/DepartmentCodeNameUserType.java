package net.silencily.sailing.framework.codename.impl.hibernate;

import java.io.Serializable;
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

import net.silencily.sailing.framework.codename.DepartmentCodeName;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

/**
 * 业务实体使用{@link DepartmentCodeName}作为属性, 当保存检索时这个属性的转换器的<code>hibernate</code>
 * 实现, 保存时仅保存部门<code>id</code>, 检索时返回一个完整的<code>Department</code>实例
 * @author zhangli
 * @version $Id: DepartmentCodeNameUserType.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 * @since 2007-4-1
 */
public class DepartmentCodeNameUserType implements UserType {
    
    private Log logger = LogFactory.getLog(DepartmentCodeNameUserType.class);
    
    private int[] TYPES = new int[] {Hibernate.STRING.sqlType()};

    public Object assemble(Serializable serial, Object obj) throws HibernateException {
        return serial;
    }

    public Object deepCopy(Object obj) throws HibernateException {
        if (obj == null) {
            return obj;
        }
        return ((DepartmentCodeName) obj).clone();
    }

    public Serializable disassemble(Object obj) throws HibernateException {
        return (Serializable) obj;
    }

    public boolean equals(Object o1, Object o2) throws HibernateException {
        if (o1 == o2) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }
        /* DepartmentCodeName implements proper equals */
        return o1.equals(o2);
    }

    public int hashCode(Object obj) throws HibernateException {
        if (obj == null) {
            /* 这个 hash 不够精确, 但是对于这个类已经足够了 */
            return DepartmentCodeName.class.hashCode();
        }
        return obj.hashCode();
    }

    public boolean isMutable() {
        return true;
    }

    public Object nullSafeGet(ResultSet resultSet, String[] columns, Object owner) throws HibernateException, SQLException {
        String id = resultSet.getString(columns[0]);
        DepartmentCodeName cn = new DepartmentCodeName();
        if (StringUtils.isBlank(id)) {
            return cn;
        }
        
        /*
        DepartmentService service = (DepartmentService) ServiceProvider
            .getService(DepartmentService.SERVICE_NAME);
        try {
            Department d = service.loadDepartment(id);
            cn.copyFrom(d);
        } catch (IllegalStateException e) {
            if (logger.isInfoEnabled()) {
                logger.info("业务实体" 
                    + owner.getClass().getName() 
                    + "引用了id=" + id 
                    + "的部门,在数据库中没有找到");
            }
            cn.setCode(id);
            cn.setName(id);
        }
        */
        ////////////////////////////
        /*
        if(SecurityContextInfo.getCurrentUser() != null){
	        cn.setCode(SecurityContextInfo.getCurrentUser().getDeptId());
	        cn.setName(SecurityContextInfo.getCurrentUser().getDeptName());
        }
        */
        cn.setCode(id);
        String name = "";
        name = getDepartmentInfo(id);
        cn.setName(name);
        ////////////////////////////      
        return cn;
    }

    public void nullSafeSet(PreparedStatement ps, Object value, int index) throws HibernateException, SQLException {
        if (value != null && value.getClass() == String.class) {
            ps.setString(index, (String)value);
        	return;
        }

    	DepartmentCodeName d = (DepartmentCodeName) value;
        if (d == null || d.getCode() == null) {
            ps.setNull(index, TYPES[0]);
        } else {
            ps.setString(index, d.getId());
        }
    }

    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    public Class returnedClass() {
        return DepartmentCodeName.class;
    }

    public int[] sqlTypes() {
        return TYPES;
    }
    
    
    private String getDepartmentInfo(String id) {
		Context ctx = null;
		DataSource ds = null;
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String sql = null;
		List list = new ArrayList();
		String name = "";
		try {
			ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/OracleDS");
			conn = ds.getConnection();
			sql = "select t.dept_name from tbl_cmn_dept t where id = '";
			sql = sql + id +"' and t.del_flg = '0'";
			if (conn != null) {
				statement = conn.prepareStatement(sql);
				resultSet = statement.executeQuery();
				while (resultSet.next()) {
					Map map = new HashMap();
					map.put("dept_name", resultSet.getString("dept_name"));
					list.add(map);
				}
			}
		} catch (Exception e) {
			System.out.println("获取部门名称出错。");
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
			name = (String) ((Map) list.get(0)).get("dept_name");
		}
		return name;
	}
}
