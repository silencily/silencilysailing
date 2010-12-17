package net.silencily.sailing.framework.codename.impl.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.silencily.sailing.framework.codename.UserCodeName;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

/**
 * ҵ��ʵ�����������{@link UserCodeName}��Ϊ����ʱ, ����ͼ���ʱʹ�õ�ת������<code>hibernate</code>
 * ʵ��, ����ʱд���û���¼��, ����ʱ����������{@link UserCodeName}, ��ҵ������в�Ҫֱ
 * ��ʹ��{@link User}��Ϊ����, �����<code>equals</code>����������Ŀ�Ĳ�ͬ, ֱ��ʹ��Ӱ
 * ������, ͬʱ�޷��ṩ����ͼʹ�õ�ͳһ��ʽ
 * @author zhangli
 * @version $Id: UserCodeNameUserType.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 * @since 2007-4-9
 */
public class UserCodeNameUserType implements UserType {
    
    private int[] TYPES = new int[] {Hibernate.STRING.sqlType()};
    
    private Log logger = LogFactory.getLog(UserCodeNameUserType.class);
    
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    public Object deepCopy(Object value) throws HibernateException {
        if (value == null) {
            return value;
        }
        return ((UserCodeName) value).clone();
    }

    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == y) {
            return true;
        }
        if (x == null || y == null) {
            return false;
        }
        /* UserCodeName has implemented this method proper */
        return x.equals(y);
    }

    public int hashCode(Object x) throws HibernateException {
        if (x == null) {
            return UserCodeName.class.hashCode();
        }
        return x.hashCode();
    }

    public boolean isMutable() {
        return true;
    }

    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
        String username = rs.getString(names[0]);
        UserCodeName cn = new UserCodeName();
        if (StringUtils.isBlank(username)) {
            return cn;
        }
        /*
        AuthenticationService service = (AuthenticationService) ServiceProvider
            .getService(AuthenticationService.SERVICE_NAME);
        try {
            User user = service.loadUser(username);
            cn.copyFrom(user);
        } catch (Exception e) {
            if (logger.isInfoEnabled()) {
                logger.info("ҵ��ʵ��" 
                    + owner.getClass().getName() 
                    + "������username=" + username
                    + "���û�,�����ݿ���û���ҵ�", e);
            }
            cn.setCode(username);
            cn.setName(username);
        }
        */
        ///////////////////////
        cn.setCode(username);
        cn.setName(username);
        /////////////////////////
        
        return cn;
    }

    public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
        if (value != null && value.getClass() == String.class) {
            st.setString(index, (String)value);
        	return;
        }
    	UserCodeName cn = (UserCodeName) value;
        if (cn == null || cn.getCode() == null) {
            st.setNull(index, TYPES[0]);
        } else {
            st.setString(index, cn.getCode());
        }
    }

    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    public Class returnedClass() {
        return UserCodeName.class;
    }

    public int[] sqlTypes() {
        return TYPES;
    }
}
