package net.silencily.sailing.framework.codename;

import java.util.ArrayList;
import java.util.List;

import net.silencily.sailing.exception.UnexpectedException;
import net.silencily.sailing.framework.authentication.entity.Principal;
import net.silencily.sailing.framework.authentication.entity.User;
import net.silencily.sailing.framework.utils.AssertUtils;

import org.springframework.util.StringUtils;

/**
 * 用于业务实体中保存多个用户的属性, <code>code</code>是","分隔的用户登录名列表, <code>name</code>
 * 是","分隔的姓名列表, 注意没有相应的<code>Users</code>类
 * @author zhangli
 * @version $Id: UsersCodeName.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 * @since 2007-3-29
 */
public class UsersCodeName implements Principal {
    private String name;
    private String code;
    
    /** 组成{@link #code}的{@link UserCodeName} */
    private List userCodeNames = new ArrayList();
    
    public UsersCodeName() {}
    
    public UsersCodeName(String code, String name) {
        setCode(code);
        this.name = name;
    }

    /**
     * 规范","分隔的列表的值, 排除掉空串
     * @param code 要设置的<code>code</code>, 可以是<code>null</code>
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
     * 检索组成{@link #code}的每个用户名对应的{@link UserCodeName}
     * @return 组成{@link #code}的每个用户名对应的{@link UserCodeName}
     */
    public List getUserCodeNames() {
        return userCodeNames;
    }

    public void setUserCodeNames(List users) {
        this.userCodeNames = users;
    }

    /**
     * 检查当前用户列表是否包含参数的用户名
     * @param username 要检查是否存在在这个用户列表的用户名
     * @return 如果在当前的用户列表中返回<code>true</code>
     */
    public boolean isContains(String username) {
        return getCode() != null 
            && StringUtils.commaDelimitedListToSet(getCode()).contains(username);
    }
    
    /**
     * 当前的用户列表是否包含参数指定的用户
     * @param user 要判断的用户
     * @return 如果包含参数用户返回<code>true</code>
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
            throw new UnexpectedException("不能clone UsersCodeName", e);
        }
        return ret;
    }
}
