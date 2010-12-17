package net.silencily.sailing.framework.authentication.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import net.silencily.sailing.exception.UnexpectedException;

/**
 * 表现一个用户信息, 目的是隔离安全具体实现, 这个类所有的属性由安全注入 
 * @author zhangli
 * @version $Id: User.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 * @since 2007-4-8
 */
public class User implements Serializable, Cloneable {
    
    /** 用户在系统的账号, 也就是登录名, 与<username>username</username>是同一个东西 */
    private String username;

    /** 用户实际姓名, 通常作为显示 */
    private String name;
    
    /** 用户所在部门 */
    private Department department = new Department();
    
    /** 用户所在的部门, 用于一个人在多个部门的情况, 元素类型是{@link Department} */
    private List departments = new ArrayList();
    
    /** 用户拥有的角色, 元素类型是{@link Role} */
    private List roles = new ArrayList();
    
    /**
     * 检索这个用户拥有的岗位, 就是<code>{@link Role#isJob()}==true</code>的角色, 如果
     * 这个用户定义了多个满足这样条件的角色, 返回满足条件的第一个, 返回哪一个在这里无法确定,
     * 取决于安全系统注入这个属性时的顺序
     * @return 这个用户的岗位, <b>如果没有定义这样的角色返回<code>null</code></b>
     */
    public Role findJob() {
        if (getRoles() != null && getRoles().size() > 0) {
            return (Role) CollectionUtils.find(getRoles(), new Predicate() {
                public boolean evaluate(Object element) {
                    Role role = (Role) element;
                    return role != null && role.isJob();
                }});
        }
        return null;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
    
    /**
     * 当一个用户属于多个部门时这个方法返回部门列表, 如果就是一个部门, 返回长度是<b>1</b>
     * 的<code>List</code>, 这个元素与{@link #getDepartment()}的结果一样
     * @return 用户所在的部门列表,元素类型是{@link Department}
     */
    public List getDepartments() {
        return departments;
    }

    public void setDepartments(List departments) {
        this.departments = departments;
    }

    public List getRoles() {
        return roles;
    }

    public void setRoles(List roles) {
        this.roles = roles;
    }
    
    public Object clone() {
        Object ret = null;
        try {
            ret = super.clone();
        } catch (CloneNotSupportedException e) {
            throw new UnexpectedException("不能clone User", e);
        }
        return ret;
    }
    
}
