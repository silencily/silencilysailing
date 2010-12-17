package net.silencily.sailing.framework.codename;

import java.util.ArrayList;
import java.util.List;

import net.silencily.sailing.exception.UnexpectedException;
import net.silencily.sailing.framework.authentication.entity.Principal;
import net.silencily.sailing.framework.authentication.entity.User;
import net.silencily.sailing.framework.utils.AssertUtils;

import org.springframework.util.StringUtils;

/**
 * ����ҵ��ʵ���б������û�������, <code>code</code>��","�ָ����û���¼���б�, <code>name</code>
 * ��","�ָ��������б�, ע��û����Ӧ��<code>Users</code>��
 * @author zhangli
 * @version $Id: UsersCodeName.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 * @since 2007-3-29
 */
public class UsersCodeName implements Principal {
    private String name;
    private String code;
    
    /** ���{@link #code}��{@link UserCodeName} */
    private List userCodeNames = new ArrayList();
    
    public UsersCodeName() {}
    
    public UsersCodeName(String code, String name) {
        setCode(code);
        this.name = name;
    }

    /**
     * �淶","�ָ����б��ֵ, �ų����մ�
     * @param code Ҫ���õ�<code>code</code>, ������<code>null</code>
     */
    public void setCode(String code) {
        this.code = net.silencily.sailing.utils.ArrayUtils.normalizeCommaLimitedString(code);
    }

    public String getCode() throws IllegalStateException {
        return code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() throws IllegalStateException {
        return name;
    }
    
    /**
     * �������{@link #code}��ÿ���û�����Ӧ��{@link UserCodeName}
     * @return ���{@link #code}��ÿ���û�����Ӧ��{@link UserCodeName}
     */
    public List getUserCodeNames() {
        return userCodeNames;
    }

    public void setUserCodeNames(List users) {
        this.userCodeNames = users;
    }

    /**
     * ��鵱ǰ�û��б��Ƿ�����������û���
     * @param username Ҫ����Ƿ����������û��б���û���
     * @return ����ڵ�ǰ���û��б��з���<code>true</code>
     */
    public boolean isContains(String username) {
        return getCode() != null 
            && StringUtils.commaDelimitedListToSet(getCode()).contains(username);
    }
    
    /**
     * ��ǰ���û��б��Ƿ��������ָ�����û�
     * @param user Ҫ�жϵ��û�
     * @return ������������û�����<code>true</code>
     */
    public boolean pass(User user) {
        AssertUtils.notNull(user);
        return isContains(user.getUsername());
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
        UsersCodeName usersCodeName = (UsersCodeName) o;
        return org.apache.commons.lang.StringUtils.equals(getCode(), usersCodeName.getCode());
    }
    
    public int hashCode() {
        if (org.apache.commons.lang.StringUtils.isBlank(getCode())) {
            return System.identityHashCode(this);
        } else {
            return getClass().hashCode() * 29 + getCode().hashCode();
        }
    }
    
    public Object clone() {
        Object ret = null;
        try {
            ret = super.clone();
        } catch (CloneNotSupportedException e) {
            throw new UnexpectedException("����clone UsersCodeName", e);
        }
        return ret;
    }
}
