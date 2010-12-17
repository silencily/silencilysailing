package net.silencily.sailing.framework.persistent.filter;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;


/**
 * <p>��ʾ��������ʱҪ���������֮һ, ���������͵�ʵ�����һ���ض�����������. �����ʾ����
 * �ƻ�<code>DTO</code>��������������ʹ�����Ĳ������ݵĲ��Զ���, �ڼܹ��Ĳ�ѯ���������������
 * ����Ϊ����<code>DTO</code>������</p>
 * <p>�������÷�����<ul>
 * <li><code>prepend</code>ʹ��{@link ConditionConstants#DEFAULT_RELATION <code>and</code>}</li>
 * <li><code>operator</code>ʹ��{@link ConditionConstants#DEFAULT_OPERATOR <code>=</code>}</li>
 * <li><code>type</code>ʹ��{@link ConditionConstants#DEFAULT_TYPE <code>String</code>}</li>
 * <li>���������������(<code>compositeCondition</code>����ѡ��һ��</li>
 * <li>����������������,��ô���������е�������<code>name</code>����ʹ���������</li></ul>
 * </p>
 * @author scott
 * @since 2006-4-18
 * @version $Id: Condition.java,v 1.1 2010/12/10 10:54:17 silencily Exp $
 * @see {@link AbstractSearchConditions}
 */
public class Condition implements ConditionConstants, Serializable, Comparable {
    
    /** ��Ϊ����������<code>domain object</code>���������� */
    private String name;
    
    /** ����Ҫ�����ֵ */
    private Object value;
    
    /** ������ֵ֮��Ĺ�ϵ, ��"=", ">", "<"��, ������ѭ<code>SQL</code>�ı�׼ */
    private String operator;
    
    /** ���Ե����� */
    private Class type;
    
    /** ������ڶ������ʱ���������ǰһ���Ĺ�ϵ, ��Щ���Եĵ�һ�����������ֵ */
    private String prepend;
    
    /** ������ڶ������, ������Ծ���������������ʽʱ����������˳�� */
    private int order;
    
    /**
     * ��������Ƿ�����ִ�в�ѯ��������, ���簴��Ŀǰ��ͳһ�淶, ������ֵ����������Ϊ��ѯ
     * ����, ����ĳЩ���, �����ѯ��Աʱ������ֵ����Ա<code>id</code>, ������������Ϊ��ʾ,
     * ����������±�����Ա������<code>condition</code>�ǲ���Ϊ��ѯ������. ���û��ָ�����
     * ֵ, ȱʡֵ��<code>true</code>, ˵��Ҫ�����������. ���������<code>ignoreEmpty</code>
     * �������������������ֵ���ǿ�, ����ִ�в�ѯʱ��ȻҪ�����������, �������ʵ���ϸ����Ϊ����
     * ������֧��
     */
    private boolean place = true;
    
    /**
     * ������������ֵ��<code>null</code>������ַ���������<code>empty</code>����װ��
     * ѯ����ʱ�Ƿ�����������. ���ڴ���������<code>true</code>, ���ǵ�һ������û��ֵ
     * ʱ�����������. ���͵���������<code>UI</code>�е���������, ���û����д�κ�ֵ�Ͳ�
     * �����������
     */
    private boolean ignoreEmpty = true;
    
    /**
     * ���������г��� "." ʱ�Ƿ񴴽�����, ����������¿��ܳ��� "."
     * <ul>
     * <li>��������һ����������, ��ʱ createAlias Ӧ��Ϊ true, ��Ҳ��Ĭ�����</li>
     * <li>��������һ����������, �� org.hibernate.usertype.CompositeUserType ��ʵ��,  ��ʱ createAlias Ӧ��Ϊ false</li>
     * </ul>
     */ 
    private boolean createAlias = true;
    
    /** ��ص�һ������, �������<code>... and (cond1 or cond2)</code> */
    private Condition[] compositeConditions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getPrepend() {
        return prepend;
    }

    public void setPrepend(String prepend) {
        this.prepend = prepend;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Condition[] getCompositeConditions() {
        if (this.compositeConditions == null) {
            this.compositeConditions = new Condition[0];
        }
        
        return compositeConditions;
    }

    public Condition getCompositeConditions(int index) {
        return compositeConditions[index];
    }

    public void setCompositeConditions(Condition[] compositeConditions) {
        this.compositeConditions = compositeConditions;
    }

    public void setCompositeConditions(int index, Condition compositeConditions) {
        this.compositeConditions[index] = compositeConditions;
    }

    public boolean isIgnoreEmpty() {
        return ignoreEmpty;
    }

    public void setIgnoreEmpty(boolean ignoreEmpty) {
        this.ignoreEmpty = ignoreEmpty;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int compareTo(Object o) {
        if (o == null) {
            throw new NullPointerException("�Ƚ�Condition˳��ʱ,������null");
        }
        if (!(o instanceof Condition)) {
            throw new ClassCastException("�Ƚ�Condition˳��ʱ,��������Condition����");
        }
        Condition c = (Condition) o;
        int ret = 0;
        if (this.equals(c)) {
            ret = 0;
        } else {
            ret = this.order - c.order;
            /* ��������� ret == 0 �����, ��Ϊ�� ret == 0 ʱ, �� equals ������һ�� */
            ret = (ret > 0) ? 1 : -1;
        }
        return ret;
    }
    
    /**
     * <p>ִ�в�ѯʱ�Ƿ����������, �����ȷ������<code>false</code>�������������ֵ�ǿ�
     * ����<code>ignoreEmpty</code>����<code>true</code>,ִ�в�ѯʱ�Ͳ������������</p>
     * <p>��ִ�в�ѯʱ���������Ϊ�Ƿ������������Ψһ��׼</p>
     * @return ִ�в�ѯʱ�Ƿ����������
     */
    public boolean isPlace() {
        if (place) {
            /* value ���ǿ�ֵ�� value �ǿն��Ҳ����Կ�ֵ */
            return (isEmpty() && !ignoreEmpty || !isEmpty() || (getCompositeConditions().length > 0));
        } else {
            /* ��ȷָ���˲���Ϊ��ѯ���� */
            return false;
        }
    }

    public void setPlace(boolean place) {
        this.place = place;
    }
    
    private boolean isEmpty() {
        if (value == null) {
            return true;
        } else if (value instanceof String) {
            String str = (String) value;
            return StringUtils.isBlank(str);
        } else {
            return false;
        }
    }

	/**
	 * @return the createAlias
	 */
	public boolean isCreateAlias() {
		return createAlias;
	}

	/**
	 * @param createAlias the createAlias to set
	 */
	public void setCreateAlias(boolean createAlias) {
		this.createAlias = createAlias;
	}
}
