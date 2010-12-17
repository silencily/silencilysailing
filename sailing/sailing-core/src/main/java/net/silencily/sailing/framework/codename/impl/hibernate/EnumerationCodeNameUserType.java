package net.silencily.sailing.framework.codename.impl.hibernate;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.silencily.sailing.exception.UnexpectedException;
import net.silencily.sailing.framework.codename.EnumerationCodeName;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

/**
 * 工器具状态转换类
 * @author zhangli
 * @version $Id: EnumerationCodeNameUserType.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 * @since 2007-4-26
 */
public class EnumerationCodeNameUserType extends AbstractParameterizedCodeNameUserType implements UserType {
    
    private static final int[] TYPES = new int[] {Hibernate.STRING.sqlType()};

    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    public boolean equals(Object x, Object y) throws HibernateException {
        return x == y;
    }

    public int hashCode(Object x) throws HibernateException {
        if (x == null) {
            return EnumerationCodeName.class.hashCode();
        }
        return x.hashCode();
    }

    public boolean isMutable() {
        return false;
    }

    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
        String code = rs.getString(names[0]);
        return enumerationCodeName(code);
    }

    public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
        EnumerationCodeName state = (EnumerationCodeName) value;
        if (state == null) {
            st.setNull(index, TYPES[0]);
        } else {
            st.setString(index, state.getCode());
        }
    }

    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    public Class returnedClass() {
        return EnumerationCodeName.class;
    }
    
    private EnumerationCodeName enumerationCodeName(String code) {
        try {
            Method method = clazz.getMethod(
                EnumerationCodeName.METHOD_NAME, EnumerationCodeName.METHOD_PARAMETERS);
            return (EnumerationCodeName) method.invoke(null, new String[] {code});
        } catch (Exception e) {
            throw new UnexpectedException("EnumerationCodeName的子类要求定义静态方法" 
                + EnumerationCodeName.METHOD_NAME 
                + "(String),检查这个方法", e);
        }
    }

    public int[] sqlTypes() {
        return TYPES;
    }
}
