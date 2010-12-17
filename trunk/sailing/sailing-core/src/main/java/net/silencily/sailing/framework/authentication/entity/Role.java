package net.silencily.sailing.framework.authentication.entity;

import java.io.Serializable;

import net.silencily.sailing.exception.UnexpectedException;

/**
 * 一个用户拥有的角色
 * @author Scott Captain
 * @since 2006-6-18
 * @version $Id: Role.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 */
public class Role implements Serializable, Cloneable {
    /** 一个角色的编码, 通常在程序中引用这个值, 而名称可能会变化 */
    private String code;
    
    /** 角色名称 */
    private String name;
    
    /** 角色所属的部门, 对于系统的功能角色没有这个属性 */
    private Department department;
    
    /** 
     * 这个角色是否表现为一个岗位, 而不是系统的功能角色, 岗位对应着实际生活中具体的岗位, 是
     * 根据实际情况定义的, 而系统功能角色是开发期实现功能后又开发人员定义的
     */
    private boolean job;
    
    /** 
     * 判断这个角色表现为一个岗位还是系统功能角色
     * @return 如果是岗位返回<code>true</code>
     */
    public boolean isJob() {
        return job;
    }

    public void setJob(boolean job) {
        this.job = job;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Object clone() {
        Object ret = null;
        try {
            ret = super.clone();
        } catch (CloneNotSupportedException e) {
            throw new UnexpectedException("不能clone Role", e);
        }
        return ret;
    }
}
