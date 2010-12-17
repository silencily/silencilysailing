package net.silencily.sailing.framework.persistent.search;

import java.io.Serializable;


/**
 * ��ʾ��������ʱҪ���������֮һ, ���������͵�ʵ�����һ���ض�����������. �����ʾ�����ƻ�
 * <code>DTO</code>��������������ʹ�����Ĳ������ݵĲ��Զ���, �ڼܹ��Ĳ�ѯ���������������
 * ����Ϊ����<code>DTO</code>������
 * @author scott
 * @since 2006-4-18
 * @version $Id: SearchCondition.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 * @see {@link AbstractSearchConditions}
 */
public class SearchCondition implements SearchConditionConstant, Serializable {
    
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
    
    /**
     * ������������ֵ��<code>null</code>������ַ���������<code>empty</code>����װ��
     * ѯ����ʱ�Ƿ�����������. ���ڴ���������<code>true</code>, ���ǵ�һ������û��ֵ
     * ʱ�����������. ���͵���������<code>UI</code>�е���������, ���û����д�κ�ֵ�Ͳ�
     * �����������
     */
    private boolean ignoreEmpty = true;
    
    /** ��ص�һ������, �������<code>... and (cond1 or cond2)</code> */
    private SearchCondition[] compositeConditions;

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

    public SearchCondition[] getCompositeConditions() {
        if (this.compositeConditions == null) {
            this.compositeConditions = new SearchCondition[0];
        }
        
        return compositeConditions;
    }

    public void setCompositeConditions(SearchCondition[] compositeConditions) {
        this.compositeConditions = compositeConditions;
    }

    public boolean isIgnoreEmpty() {
        return ignoreEmpty;
    }

    public void setIgnoreEmpty(boolean ignoreEmpty) {
        this.ignoreEmpty = ignoreEmpty;
    }
}
