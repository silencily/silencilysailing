package net.silencily.sailing.framework.codename;

import java.util.Iterator;

import net.silencily.sailing.exception.UnexpectedException;
import net.silencily.sailing.framework.authentication.entity.Principal;
import net.silencily.sailing.framework.authentication.entity.User;
import net.silencily.sailing.framework.utils.AssertUtils;
import net.silencily.sailing.utils.ArrayUtils;

import org.apache.commons.lang.StringUtils;

/**
 * 用于保存多个部门的列表的业务实体属性
 * @author zhangli
 * @version $Id: DepartmentsCodeName.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 * @since 2007-4-8
 */
public class DepartmentsCodeName implements Principal {
    private String code;
    private String name;
    
    public DepartmentsCodeName() {
        super();
    }
    
    public DepartmentsCodeName(String code, String name) {
        setCode(code);
        this.name = name;
    }

    public void setCode(String code) {
        this.code = ArrayUtils.normalizeCommaLimitedString(code);
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
    
    public boolean isContains(String departmentId) {
        return code != null && org.springframework.util.StringUtils.commaDelimitedListToSet(code)
            .contains(departmentId);
    }

    /**
     * 参数指定的用户所在部门是否在这个部门列表中, 如果部门编码是按照层次码定义的, 那么参数
     * 用户在任何一个部门列表的下属部门也返回<code>true</code>, 比如部门列表是<code>0801,0802</code>
     * , 如果参数<code>user</code>的部门编码是<code>080101</code>也满足这个方法
     * @param user 要判断的用户
     * @return 如果参数用户的部门在当前部门列表中
     * @throws NullPointerException 如果参数是<code>null</code>
     */
    public boolean pass(User user) {
        if (code == null) {
            return false;
        }
        AssertUtils.notNull(user);
        Iterator it = org.springframework.util.StringUtils.commaDelimitedListToSet(code).iterator();
        boolean ret = false;
        String dept = user.getDepartment().getId();
        while (!ret && it.hasNext()) {
            ret = dept.startsWith((String) it.next());
        }
        return ret;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        DepartmentsCodeName d = (DepartmentsCodeName) o;
        return StringUtils.equals(code, d.code);
    }
    
    public int hashCode() {
        if (code != null) {
            return DepartmentsCodeName.class.hashCode() * 29 + code.hashCode();
        } else {
            return System.identityHashCode(this);
        }
    }

    public Object clone() {
        Object o = null;
        try {
            o = super.clone();
        } catch (CloneNotSupportedException e) {
            throw new UnexpectedException("不能clone " + getClass().getName(), e);
        }
        return o;
    }
}
