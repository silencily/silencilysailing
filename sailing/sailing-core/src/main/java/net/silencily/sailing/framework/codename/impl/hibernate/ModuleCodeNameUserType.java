package net.silencily.sailing.framework.codename.impl.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.framework.codename.AbstractModuleCodeName;
import net.silencily.sailing.framework.codename.ModuleCodeNameService;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

/**
 * 各子系统的业务实体使用了<code>AbstractModuleCodeName</code>的子类, 在<code>hibernate</code>
 * 配置文件中这样定义类型<pre>
 * &lt;property name="xxx" type="com.coheg.framework.codename.impl.hibernate.ModuleCodeNameUserType" length="4000"&gt;
 *   &lt;param name="class.name"&gt;子系统的<code>CodeName</code>类名&lt;/param&gt;
 * &lt;/property&gt;</pre>
 * @author zhangli
 * @version $Id: ModuleCodeNameUserType.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 * @since 2007-3-25
 */
public class ModuleCodeNameUserType extends AbstractParameterizedCodeNameUserType implements UserType {
    public static final int[] TYPES = new int[] {Hibernate.STRING.sqlType()};
    private static Log logger = LogFactory.getLog(ModuleCodeNameUserType.class);

    public Object deepCopy(Object obj) throws HibernateException {
        if (obj == null) {
            return obj;
        }
        return ((AbstractModuleCodeName)obj).clone();
    }

    public boolean equals(Object o1, Object o2) throws HibernateException {
        if (o1 == o2) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }
        /* AbstractModuleCodeName has proper implemented this method */
        return o1.equals(o2);
    }

    public int hashCode(Object obj) throws HibernateException {
        AbstractModuleCodeName cn = (AbstractModuleCodeName) obj;
        if (cn == null) {
            return AbstractModuleCodeName.class.hashCode();
        }
        return cn.hashCode();
    }

    public boolean isMutable() {
        return true;
    }

    public Class returnedClass() {
        return clazz;
    }

    public Object assemble(Serializable serializable, Object obj) throws HibernateException {
        return serializable;
    }

    public Serializable disassemble(Object obj) throws HibernateException {
        return (Serializable) obj;
    }

    public Object nullSafeGet(ResultSet resultSet, String[] columns, Object obj) throws HibernateException, SQLException {
        String code = resultSet.getString(columns[0]);
        if (!resultSet.wasNull()) {
            ModuleCodeNameService service = (ModuleCodeNameService) ServiceProvider.getService(ModuleCodeNameService.SERVICE_NAME);
            AbstractModuleCodeName cn = (AbstractModuleCodeName) service.load(clazz, code);
            if (cn == null) {
                cn = (AbstractModuleCodeName) instance();
                cn.setCode(code);
                cn.setName(code);
                logger.warn("代码表中没有" + clazz.getName() + "的代码[" + code + "]");
            }
            return cn;
        }
        return null;
    }

    public void nullSafeSet(PreparedStatement ps, Object value, int index) throws HibernateException, SQLException {
        AbstractModuleCodeName cn = (AbstractModuleCodeName) value;
        if (cn != null && StringUtils.isNotBlank(cn.getCode())) {
            ps.setString(index, cn.getCode());
        } else {
            ps.setNull(index, TYPES[0]);
        }
    }

    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    public int[] sqlTypes() {
        return TYPES;
    }
}
