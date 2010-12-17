package net.silencily.sailing.framework.codename.impl.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.silencily.sailing.exception.UnexpectedException;
import net.silencily.sailing.framework.codename.AbstractEntityCodeName;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;

/**
 * �����ϵͳ֮������ʵ���<code>UserType</code>, �������ͱ���������������, ���ⲻ��Ҫ��
 * ���뼶����, �������<code>converter</code>������{@link AbstractEntityCodeName}��
 * ����, ʵ�����ÿ���������:<pre>
 * &lt;class name="" table=""&gt;
 *    ......
 *   &lt;property 
 *       name="laborDress" 
 *       type="com.coheg.framework.codename.impl.hibernate.EntityCodeNameUserType" 
 *       column="labor_dress"&gt;
 *       &lt;param name="class.name"&gt;LaborDressMateril&lt;/param&gt;
 *   &lt;/property&gt;
 * &lt;/class&gt;
 * </pre>���е�<code>LaborDressMateril</code>������չ��{@link AbstractEntityCodeName}
 * �ṩ�˱�Ҫ����Ϣ������
 * <p>���ź���֪ͨ��:���ֻ��Ʋ�֧��<code>HQL</code>, ����<code>Hibernate.custom()</code></p>
 * @author zhangli
 * @version $Id: EntityCodeNameUserType.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 * @since 2007-6-9
 */
public class EntityCodeNameUserType extends AbstractParameterizedCodeNameUserType implements CompositeUserType {
    
    private static Log logger = LogFactory.getLog(EntityCodeNameUserType.class);
    
    public static final String[] PROPERTY_NAMES = {"id"};
    
    public static final Type[] TYPES = {Hibernate.STRING};

    public Object assemble(Serializable cached, SessionImplementor session, Object owner) throws HibernateException {
        return cached;
    }

    public Object deepCopy(Object obj) throws HibernateException {
        if (obj == null) {
            return obj;
        }
        return ((AbstractEntityCodeName)obj).clone();
    }

    public Serializable disassemble(Object value, SessionImplementor session) throws HibernateException {
        return (Serializable) value;
    }

    public boolean equals(Object o1, Object o2) throws HibernateException {
        if (o1 == o2) {
            return true;
        }
        if (o1 == null && o2 == null) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }
        return o1.equals(o2);
    }

    public String[] getPropertyNames() {
        return PROPERTY_NAMES;
    }

    public Type[] getPropertyTypes() {
        return TYPES;
    }

    public Object getPropertyValue(Object component, int index) throws HibernateException {
        AbstractEntityCodeName cn = (AbstractEntityCodeName) component;
        if (index == 0) {
            return cn.getId();
        }
        throw new IllegalArgumentException("getPropertyValue's index=" + index);
    }

    public int hashCode(Object obj) throws HibernateException {
        if (obj == null) {
            return clazz.hashCode();
        }
        return obj.hashCode();
    }

    public boolean isMutable() {
        return true;
    }

    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        String id = rs.getString(names[0]);
        AbstractEntityCodeName cn = (AbstractEntityCodeName) instance();
        if (!rs.wasNull()) {
            Object entity = session.getFactory().openTemporarySession().get(cn.getEntityType(), id);
            if (entity == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("û���ҵ�[" + cn.getEntityType().getName() + "]ӳ���ҵ��ʵ��,id=[" + id + "]");
                }
            } else {
                try {
                    cn.setCode(BeanUtilsBean.getInstance().getProperty(entity, "code"));
                    cn.setName(BeanUtilsBean.getInstance().getProperty(entity, "name"));
//                    cn.setProperties(BeanUtilsBean.getInstance().describe(entity));
                } catch (Exception e) {
                    throw new UnexpectedException("��[" + cn.getEntityType().getName() + "]����ΪMapʱ����", e);
                }
            }
        }
        return cn;
    }

    public void nullSafeSet(PreparedStatement ps, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        AbstractEntityCodeName cn = (AbstractEntityCodeName) value;
        if (cn != null && StringUtils.isNotBlank(cn.getId())) {
            ps.setString(index, cn.getId());
        } else {
            ps.setNull(index, Hibernate.STRING.sqlType());
        }
    }

    public Object replace(Object original, Object target, SessionImplementor session, Object owner) throws HibernateException {
        return original;
    }

    public Class returnedClass() {
        return clazz;
    }

    public void setPropertyValue(Object component, int property, Object value) throws HibernateException {
        if (value != null) {
            AbstractEntityCodeName cn = (AbstractEntityCodeName) component;
            cn.setId(value.toString());
        }
    }
}
