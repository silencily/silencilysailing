package net.silencily.sailing.framework.codename;

import org.apache.commons.lang.StringUtils;

import net.silencily.sailing.exception.UnexpectedException;
import net.silencily.sailing.framework.codename.impl.hibernate.ModuleCodeNameUserType;
import net.silencily.sailing.framework.persistent.Entity;

/**
 * 用于通过界面可以配置的代码的超类, 这类代码保存在数据库, 比如缺陷系统中缺陷级别中的各个级别代码
 * , 一个子系统中的没有业务性质的代码都可以使用这个类的一个扩展, 以缺陷管理为例的一个通用的模式是
 * <pre>
 * protected String getInternalModuleName() {
 *     return FlawConstants.MODULE_NAME;
 * }
 * </pre>, <code>hibernate</code>配置文件的写法<pre>
 * &lt;property&gt;name="flawLevel" type="code_name" length="4000"/&gt;
 * </pre>
 * @author zhangli
 * @since 2007-3-22
 * @version $Id: AbstractModuleCodeName.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 */
public abstract class AbstractModuleCodeName extends Entity implements CodeName {
    
    /** 配置代码, 比如计量单位的<code>kg</code>, 在一个模块中<code>code</code>必须唯一 */
    private String code;
    
    /** 配置名称, 比如计量单位的<code>千克</code> */
    private String name;
    
    /** 关于配置的一些描述, 用于维护界面 */
    private String description;
    
    /** 这项配置所属的分类, 在一个模块中总是存在一个以模块名为<code>code</code>的根节点 */
    private AbstractModuleCodeName parent;
    
    /** 这项配置是否是分类, 一个配置是分类还是具体的配置仅仅看这个属性, 分类与配置并没有区别 */
    private boolean hasChildren = false;
    
    private Boolean creatable = Boolean.TRUE;
    private Boolean updatable = Boolean.TRUE;
    private Boolean deletable = Boolean.TRUE;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AbstractModuleCodeName getParent() {
        return parent;
    }

    public void setParent(AbstractModuleCodeName parent) {
        this.parent = parent;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public Boolean getCreatable() {
        return creatable;
    }

    public void setCreatable(Boolean creatable) {
        this.creatable = creatable;
    }
    
    public boolean isCreatable() {
        return getParent() == null || getParent().getCreatable() == null || getParent().getCreatable().booleanValue();
    }

    public Boolean getDeletable() {
        return deletable;
    }

    public void setDeletable(Boolean deletable) {
        this.deletable = deletable;
    }
    
    public boolean isDeletable() {
        return getDeletable() == null || getDeletable().booleanValue();
    }

    public Boolean getUpdatable() {
        return updatable;
    }

    public void setUpdatable(Boolean updatable) {
        this.updatable = updatable;
    }
    
    public boolean isUpdatable() {
        return getUpdatable() == null || getUpdatable().booleanValue();
    }

    /**
     * 检索这个代码所属的模块(子系统名称), 这个名称与<code>spring's xml</code>中的<pre>
     *  &lt;bean class="com.coheg.core.ServiceInfo"&gt;
     *      &lt;property name="name" value="common.components"/&gt;
     *      &lt;property name="description" value="common.components"/&gt;
     *  &lt;/bean&gt;</pre>的<code>name</code>属性一致, 同时也与每个子系统中一个常量
     *  <code>MODULE_NAME</code>一致
     */
    public String getModuleName() {
        return getInternalModuleName();
    }

    /**
     * 实现这个接口的目的是为了以<code>component</code>风格使用<code>CodeName</code>,
     * 因为实现毕竟可能是<code>Hibernate</code>之类的架构, 对<code>CodeName</code>改动
     * 会反映到持久层, 这不是我们所需要的, 所以可能的操作就是先<code>clone</code>一个<code>
     * transient</code>, 然后在应用中使用它, 典型的例子是{@link ModuleCodeNameUserType},
     * 实际的应用开发中没有必要关心这个方法
     */
    public Object clone() {
        Object o = null;
        try {
            o = super.clone();
        } catch (CloneNotSupportedException e) {
            throw new UnexpectedException("不能clone " + getClass().getName(), e);
        }
        return o;
    }
    
    /**
     * 具体的代码实现这个方法, 固定的实现模式是扩展这个类, 比如缺陷管理子系统<pre>
     * protected String getInternalModuleName() {
     *     return FlawConstants.MODULE_NAME;
     * }
     * </pre>,然后这个子系统中的所有代码
     * @return
     */
    protected abstract String getInternalModuleName();
    
    /* equals & hashCode don't apply to Set, Comparator, Comparable */
    
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
        return StringUtils.equals(getCode(), ((AbstractModuleCodeName) o).getCode());
    }
    
    public int hashCode() {
        if (StringUtils.isBlank(getCode())) {
            return getClass().hashCode();
        } else {
            return getClass().hashCode() * 29 + getCode().hashCode();
        }
    }
}
