package net.silencily.sailing.framework.codename.impl.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Set;

import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.framework.authentication.entity.Department;
import net.silencily.sailing.framework.authentication.service.DepartmentService;
import net.silencily.sailing.framework.codename.DepartmentsCodeName;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

/**
 * 用于业务实体的{@link DepartmentsCodeName}类型的属性检索和读取的转换器<code>Hibernate</code>
 * 实现
 * @author zhangli
 * @version $Id: DepartmentsCodeNameUserType.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 * @since 2007-4-9
 */
public class DepartmentsCodeNameUserType implements UserType {
    
    private Log logger = LogFactory.getLog(DepartmentsCodeNameUserType.class);
    
    private static final int[] TYPES = new int[] {Hibernate.STRING.sqlType()};

    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    public Object deepCopy(Object value) throws HibernateException {
        if (value == null) {
            return value;
        }
        return ((DepartmentsCodeName) value).clone();
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
        return x.equals(y);
    }

    public int hashCode(Object x) throws HibernateException {
        if (x == null) {
            return DepartmentsCodeName.class.hashCode();
        }
        return x.hashCode();
    }

    public boolean isMutable() {
        return true;
    }

    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
        String codes = rs.getString(names[0]);
        DepartmentsCodeName cn = new DepartmentsCodeName();
        if (StringUtils.isBlank(codes)) {
            return cn;
        }
        cn.setCode(codes);
        Set s = org.springframework.util.StringUtils.commaDelimitedListToSet(cn.getCode());
        final DepartmentService service = (DepartmentService) ServiceProvider
            .getService(DepartmentService.SERVICE_NAME);
        Collection n = CollectionUtils.collect(s, new Transformer() {
            public Object transform(Object element) {
                String id = (String) element;
                String ret = null;
                try {
                    Department d = service.loadDepartment(id);
                    ret = d.getName();
                } catch (IllegalStateException e) {
                    if (logger.isInfoEnabled()) {
                        logger.info("检索DepartmentsCodeName时,从数据库中没有检索到[" + id + "]的部门信息");
                    }
                    ret = id;
                }
                return ret;
            }});
        String cns = org.springframework.util.StringUtils.collectionToCommaDelimitedString(n);
        cn.setName(cns);
        return cn;
    }

    public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
        DepartmentsCodeName cn = (DepartmentsCodeName) value;
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
        return DepartmentsCodeName.class;
    }

    public int[] sqlTypes() {
        return TYPES;
    }
}
