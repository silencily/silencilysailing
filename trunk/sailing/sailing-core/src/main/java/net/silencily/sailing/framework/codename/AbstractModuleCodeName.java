package net.silencily.sailing.framework.codename;

import org.apache.commons.lang.StringUtils;

import net.silencily.sailing.exception.UnexpectedException;
import net.silencily.sailing.framework.codename.impl.hibernate.ModuleCodeNameUserType;
import net.silencily.sailing.framework.persistent.Entity;

/**
 * ����ͨ������������õĴ���ĳ���, ������뱣�������ݿ�, ����ȱ��ϵͳ��ȱ�ݼ����еĸ����������
 * , һ����ϵͳ�е�û��ҵ�����ʵĴ��붼����ʹ��������һ����չ, ��ȱ�ݹ���Ϊ����һ��ͨ�õ�ģʽ��
 * <pre>
 * protected String getInternalModuleName() {
 *     return FlawConstants.MODULE_NAME;
 * }
 * </pre>, <code>hibernate</code>�����ļ���д��<pre>
 * &lt;property&gt;name="flawLevel" type="code_name" length="4000"/&gt;
 * </pre>
 * @author zhangli
 * @since 2007-3-22
 * @version $Id: AbstractModuleCodeName.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 */
public abstract class AbstractModuleCodeName extends Entity implements CodeName {
    
    /** ���ô���, ���������λ��<code>kg</code>, ��һ��ģ����<code>code</code>����Ψһ */
    private String code;
    
    /** ��������, ���������λ��<code>ǧ��</code> */
    private String name;
    
    /** �������õ�һЩ����, ����ά������ */
    private String description;
    
    /** �������������ķ���, ��һ��ģ�������Ǵ���һ����ģ����Ϊ<code>code</code>�ĸ��ڵ� */
    private AbstractModuleCodeName parent;
    
    /** ���������Ƿ��Ƿ���, һ�������Ƿ��໹�Ǿ�������ý������������, ���������ò�û������ */
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
     * �����������������ģ��(��ϵͳ����), ���������<code>spring's xml</code>�е�<pre>
     *  &lt;bean class="com.coheg.core.ServiceInfo"&gt;
     *      &lt;property name="name" value="common.components"/&gt;
     *      &lt;property name="description" value="common.components"/&gt;
     *  &lt;/bean&gt;</pre>��<code>name</code>����һ��, ͬʱҲ��ÿ����ϵͳ��һ������
     *  <code>MODULE_NAME</code>һ��
     */
    public String getModuleName() {
        return getInternalModuleName();
    }

    /**
     * ʵ������ӿڵ�Ŀ����Ϊ����<code>component</code>���ʹ��<code>CodeName</code>,
     * ��Ϊʵ�ֱϾ�������<code>Hibernate</code>֮��ļܹ�, ��<code>CodeName</code>�Ķ�
     * �ᷴӳ���־ò�, �ⲻ����������Ҫ��, ���Կ��ܵĲ���������<code>clone</code>һ��<code>
     * transient</code>, Ȼ����Ӧ����ʹ����, ���͵�������{@link ModuleCodeNameUserType},
     * ʵ�ʵ�Ӧ�ÿ�����û�б�Ҫ�����������
     */
    public Object clone() {
        Object o = null;
        try {
            o = super.clone();
        } catch (CloneNotSupportedException e) {
            throw new UnexpectedException("����clone " + getClass().getName(), e);
        }
        return o;
    }
    
    /**
     * ����Ĵ���ʵ���������, �̶���ʵ��ģʽ����չ�����, ����ȱ�ݹ�����ϵͳ<pre>
     * protected String getInternalModuleName() {
     *     return FlawConstants.MODULE_NAME;
     * }
     * </pre>,Ȼ�������ϵͳ�е����д���
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
