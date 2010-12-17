package net.silencily.sailing.framework.authentication.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import net.silencily.sailing.exception.UnexpectedException;
import net.silencily.sailing.framework.core.User;

/**
 * 表现一个部门的类, 这个类的存在是为了实现基础的业务, 而隔离安全实现的<code>adapter</code>,
 * 信息完全由安全管理注入, 这个类是不持久化的组件类(按照<code>Hibernate</code>的说法),而且
 * 是<code>immutable</code>, 增加了<code>setter</code>是为了支持页面交户时把部门的<code>id</code>
 * 传回来而不是用于改变这个实例的属性, 所以两个部门是同一个仅仅依赖于部门的<code>id</code>相同, 而忽略
 * 其它的任何属性
 * @author scott
 * @since $Date: 2010/12/10 10:54:18 $
 * @version $Id: Department.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 * @see User
 */
public class Department implements Serializable, Cloneable {
    private static final long serialVersionUID = 6650226178019664820L;
    
    /** 在系统中唯一地标识这个部门的值 */
    private String id;
    
    /** 部门名称 */
    private String name;
    
    /** 一个部门具有的属性, 例如电厂班组负责的专业等特定的业务信息 */
    private Map properties = new LinkedHashMap();
    
    /** 这个部门的上级主管部门, 如果这个值是<code>null</code>, 这个类表现组织机构的根节点 */
    private Department parent;
    
    /** 这个部门是否有下属部门 */
    private boolean hasChildren = false;
    
    public Department() {
        super();
    }
    
    public Department(String id, String name, Department parent, Map properties, boolean hasChildren) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.properties = properties;
        this.hasChildren = hasChildren;
    }
    
    //- utilities 
    
    /**
     * 检查是否这个实例表现组织机构的根节点
     * @return 如果是根节点返回<code>true</code>
     */
    public boolean isRoot() {
        return parent == null;
    }
    
    /**
     * 检索保存在部门属性中的指定<code>key</code>对应的值
     * @param key 属性名称
     * @return 名称对应的值, 可能返回<code>null</code>
     */
    public Object getProperties(String key) {
        return getProperties().get(key);
    }
    
    //- getters & setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Department getParent() {
        return parent;
    }

    public void setParent(Department parent) {
        this.parent = parent;
    }

    public Map getProperties() {
        if (properties == null) {
            properties = new HashMap();
        }
        return properties;
    }

    public void setProperties(Map properties) {
        this.properties = properties;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public Object clone() {
        Object o = null;
        try {
            o = super.clone();
        } catch (CloneNotSupportedException e) {
            throw new UnexpectedException("不能复制Department", e);
        }
        return o;
    }
}
