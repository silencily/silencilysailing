package net.silencily.sailing.framework.codename;

import java.util.Iterator;

import net.silencily.sailing.exception.UnexpectedException;
import net.silencily.sailing.framework.authentication.entity.Principal;
import net.silencily.sailing.framework.authentication.entity.User;
import net.silencily.sailing.framework.utils.AssertUtils;
import net.silencily.sailing.utils.ArrayUtils;

import org.apache.commons.lang.StringUtils;

/**
 * ���ڱ��������ŵ��б��ҵ��ʵ������
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
     * ����ָ�����û����ڲ����Ƿ�����������б���, ������ű����ǰ��ղ���붨���, ��ô����
     * �û����κ�һ�������б����������Ҳ����<code>true</code>, ���粿���б���<code>0801,0802</code>
     * , �������<code>user</code>�Ĳ��ű�����<code>080101</code>Ҳ�����������
     * @param user Ҫ�жϵ��û�
     * @return ��������û��Ĳ����ڵ�ǰ�����б���
     * @throws NullPointerException ���������<code>null</code>
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
            throw new UnexpectedException("����clone " + getClass().getName(), e);
        }
        return o;
    }
}
