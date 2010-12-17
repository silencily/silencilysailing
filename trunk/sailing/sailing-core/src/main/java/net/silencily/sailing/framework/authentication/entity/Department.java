package net.silencily.sailing.framework.authentication.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import net.silencily.sailing.exception.UnexpectedException;
import net.silencily.sailing.framework.core.User;

/**
 * ����һ�����ŵ���, �����Ĵ�����Ϊ��ʵ�ֻ�����ҵ��, �����밲ȫʵ�ֵ�<code>adapter</code>,
 * ��Ϣ��ȫ�ɰ�ȫ����ע��, ������ǲ��־û��������(����<code>Hibernate</code>��˵��),����
 * ��<code>immutable</code>, ������<code>setter</code>��Ϊ��֧��ҳ�潻��ʱ�Ѳ��ŵ�<code>id</code>
 * ���������������ڸı����ʵ��������, ��������������ͬһ�����������ڲ��ŵ�<code>id</code>��ͬ, ������
 * �������κ�����
 * @author scott
 * @since $Date: 2010/12/10 10:54:18 $
 * @version $Id: Department.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 * @see User
 */
public class Department implements Serializable, Cloneable {
    private static final long serialVersionUID = 6650226178019664820L;
    
    /** ��ϵͳ��Ψһ�ر�ʶ������ŵ�ֵ */
    private String id;
    
    /** �������� */
    private String name;
    
    /** һ�����ž��е�����, ����糧���鸺���רҵ���ض���ҵ����Ϣ */
    private Map properties = new LinkedHashMap();
    
    /** ������ŵ��ϼ����ܲ���, ������ֵ��<code>null</code>, ����������֯�����ĸ��ڵ� */
    private Department parent;
    
    /** ��������Ƿ����������� */
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
     * ����Ƿ����ʵ��������֯�����ĸ��ڵ�
     * @return ����Ǹ��ڵ㷵��<code>true</code>
     */
    public boolean isRoot() {
        return parent == null;
    }
    
    /**
     * ���������ڲ��������е�ָ��<code>key</code>��Ӧ��ֵ
     * @param key ��������
     * @return ���ƶ�Ӧ��ֵ, ���ܷ���<code>null</code>
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
            throw new UnexpectedException("���ܸ���Department", e);
        }
        return o;
    }
}
