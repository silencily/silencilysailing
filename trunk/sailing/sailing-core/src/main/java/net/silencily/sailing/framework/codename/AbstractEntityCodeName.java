package net.silencily.sailing.framework.codename;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import net.silencily.sailing.exception.UnexpectedException;


/**
 * <p>���ڰ�����ҵ����ֱ���Ϊ<code>CodeName</code>���͵�����, ���͵�������һ��ҵ��ʵ�ּ�¼
 * ���õ�һ���豸ʱ��������<code>Equipment</code>�Ĺ�����ʹ��<code>CodeName</code>����
 * ��¼�豸����չ���豸����</p>
 * @author zhangli
 * @since 2007-3-16
 * @version $Id: AbstractEntityCodeName.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 */
public abstract class AbstractEntityCodeName implements CodeName {
    
    /** ����Ҫӳ��ʵ���<code>id</code> */
    private String id;
    
    /** Ҫӳ���ʵ���<code>code</code>, ���Ҳ�������ʵ�ʵ�ҵ�����, �������ʱ��� */
    private String code;
    
    /** Ҫӳ���ʵ�����ʾ���� */
    private String name;
    
    /** ʵ��ӳ���ʵ������ */
    private Class type;
    
    /**
     * ����Ҫӳ��ʵ������Ժ�ֵ, <code>key</code>����������, <code>value</code>�����
     * ���Ե�ֵ
     */
    private Map properties = new HashMap(30);
    
    public Class getEntityType() {
        if (type == null) {
            String className = entityType();
            if (StringUtils.isBlank(className)) {
                throw new NullPointerException("AbstrctCodeName����������ṩҪӳ���ʵ��ȫ·��������");
            }
            try {
                /* which class loader? */
                type = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new MappedEntityTypeNotFound("û���ҵ�����[" + className + "]", e);
            }
        }
        return type;
    }

    /**
     * ����ʵ��Ҫӳ���ʵ��ȫ·��������, ʵ�ʵ�<code>CodeName</code>���븲���������
     * @return ʵ��Ҫӳ���ʵ��ȫ·��������
     */
    protected abstract String entityType();

    /**
     * ����Ҫӳ���ʵ��<code>code</code>������, ����������ƽ���Ϊ<code>CodeName's code</code>
     * @return Ҫӳ���ʵ��<code>code</code>������
     */
    protected abstract String codePropertyName();
    
    /**
     * ����Ҫӳ���ʵ��<code>name</code>������, ����������ƽ���Ϊ<code>CodeName's name</code>
     * @return Ҫӳ���ʵ��<code>name</code>������
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
     * ����ҵ��ʵ���������<code>propertyName</code>������ֵ
     * @param propertyName Ҫ��������������, �����õ�ҵ��ʵ�����������
     * @return ����ֵ
     * @throws IllegalArgumentException ���ָ�������Բ���ӳ���ҵ��ʵ�������
     */
    public Object getProperty(String propertyName) {
        if (!this.properties.containsKey(propertyName)) {
            throw new IllegalArgumentException("ҵ��ʵ��[" + entityType() + "]û�а���[" + propertyName + "[������");
        }
        return this.properties.get(propertyName);
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
