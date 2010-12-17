package net.silencily.sailing.framework.authentication.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import net.silencily.sailing.exception.UnexpectedException;

/**
 * ����һ���û���Ϣ, Ŀ���Ǹ��밲ȫ����ʵ��, ��������е������ɰ�ȫע�� 
 * @author zhangli
 * @version $Id: User.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 * @since 2007-4-8
 */
public class User implements Serializable, Cloneable {
    
    /** �û���ϵͳ���˺�, Ҳ���ǵ�¼��, ��<username>username</username>��ͬһ������ */
    private String username;

    /** �û�ʵ������, ͨ����Ϊ��ʾ */
    private String name;
    
    /** �û����ڲ��� */
    private Department department = new Department();
    
    /** �û����ڵĲ���, ����һ�����ڶ�����ŵ����, Ԫ��������{@link Department} */
    private List departments = new ArrayList();
    
    /** �û�ӵ�еĽ�ɫ, Ԫ��������{@link Role} */
    private List roles = new ArrayList();
    
    /**
     * ��������û�ӵ�еĸ�λ, ����<code>{@link Role#isJob()}==true</code>�Ľ�ɫ, ���
     * ����û������˶���������������Ľ�ɫ, �������������ĵ�һ��, ������һ���������޷�ȷ��,
     * ȡ���ڰ�ȫϵͳע���������ʱ��˳��
     * @return ����û��ĸ�λ, <b>���û�ж��������Ľ�ɫ����<code>null</code></b>
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
     * ��һ���û����ڶ������ʱ����������ز����б�, �������һ������, ���س�����<b>1</b>
     * ��<code>List</code>, ���Ԫ����{@link #getDepartment()}�Ľ��һ��
     * @return �û����ڵĲ����б�,Ԫ��������{@link Department}
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
            throw new UnexpectedException("����clone User", e);
        }
        return ret;
    }
    
}
