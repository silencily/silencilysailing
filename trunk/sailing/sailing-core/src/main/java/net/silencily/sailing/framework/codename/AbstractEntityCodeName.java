package net.silencily.sailing.framework.codename;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import net.silencily.sailing.exception.UnexpectedException;


/**
 * <p>用于把其它业务表现表现为<code>CodeName</code>类型的属性, 典型的例子是一个业务实现记录
 * 引用的一个设备时不建立与<code>Equipment</code>的关联而使用<code>CodeName</code>机制
 * 记录设备编码展现设备名称</p>
 * @author zhangli
 * @since 2007-3-16
 * @version $Id: AbstractEntityCodeName.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 */
public abstract class AbstractEntityCodeName implements CodeName {
    
    /** 保存要映射实体的<code>id</code> */
    private String id;
    
    /** 要映射的实体的<code>code</code>, 这个也许会用在实际的业务操作, 比如物资编码 */
    private String code;
    
    /** 要映射的实体的显示名称 */
    private String name;
    
    /** 实际映射的实体类型 */
    private Class type;
    
    /**
     * 保存要映射实体的属性和值, <code>key</code>是属性名称, <code>value</code>是这个
     * 属性的值
     */
    private Map properties = new HashMap(30);
    
    public Class getEntityType() {
        if (type == null) {
            String className = entityType();
            if (StringUtils.isBlank(className)) {
                throw new NullPointerException("AbstrctCodeName的子类必须提供要映射的实体全路径类名称");
            }
            try {
                /* which class loader? */
                type = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new MappedEntityTypeNotFound("没有找到类型[" + className + "]", e);
            }
        }
        return type;
    }

    /**
     * 检索实际要映射的实体全路径类名称, 实际的<code>CodeName</code>必须覆盖这个方法
     * @return 实际要映射的实体全路径类名称
     */
    protected abstract String entityType();

    /**
     * 检索要映射的实体<code>code</code>属性名, 这个属性名称将作为<code>CodeName's code</code>
     * @return 要映射的实体<code>code</code>属性名
     */
    protected abstract String codePropertyName();
    
    /**
     * 检索要映射的实体<code>name</code>属性名, 这个属性名称将作为<code>CodeName's name</code>
     * @return 要映射的实体<code>name</code>属性名
     */
    protected abstract String namePropertyName();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Map getProperties() {
        return properties;
    }

    public void setProperties(Map properties) {
        this.properties = properties;
        setCode((String) this.properties.get(codePropertyName()));
        setName((String) this.properties.get(namePropertyName()));
    }
    
    /**
     * 检索业务实体的名称是<code>propertyName</code>的属性值
     * @param propertyName 要检索的属性名称, 是引用的业务实体的属性名称
     * @return 属性值
     * @throws IllegalArgumentException 如果指定的属性不是映射的业务实体的属性
     */
    public Object getProperty(String propertyName) {
        if (!this.properties.containsKey(propertyName)) {
            throw new IllegalArgumentException("业务实体[" + entityType() + "]没有包含[" + propertyName + "[的属性");
        }
        return this.properties.get(propertyName);
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
    
    public int hashCode() {
        if (StringUtils.isBlank(getId())) {
            return super.hashCode();
        }
        return getClass().hashCode() * 29 + getId().hashCode();
    }
    
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (StringUtils.isBlank(getId())) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        AbstractEntityCodeName a = (AbstractEntityCodeName) o;
        return getId().equals(a.getId());
    }
}
