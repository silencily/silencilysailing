package net.silencily.sailing.framework.persistent.search;

import java.io.Serializable;


/**
 * 表示操作数据时要满足的条件之一, 多个这个类型的实例组成一次特定操作的条件. 具体表示列名称或
 * <code>DTO</code>属性名称依赖于使用它的操作数据的策略而定, 在架构的查询操作中这个条件被
 * 解释为基于<code>DTO</code>的属性
 * @author scott
 * @since 2006-4-18
 * @version $Id: SearchCondition.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 * @see {@link AbstractSearchConditions}
 */
public class SearchCondition implements SearchConditionConstant, Serializable {
    
    /** 作为检索条件的<code>domain object</code>的属性名称 */
    private String name;
    
    /** 属性要满足的值 */
    private Object value;
    
    /** 属性与值之间的关系, 有"=", ">", "<"等, 基本遵循<code>SQL</code>的标准 */
    private String operator;
    
    /** 属性的类型 */
    private Class type;
    
    /** 如果存在多个属性时这个属性与前一个的关系, 这些属性的第一个不解释这个值 */
    private String prepend;
    
    /**
     * 如果这个条件的值是<code>null</code>或对于字符串类型是<code>empty</code>在组装查
     * 询条件时是否忽略这个条件. 对于大多数情况是<code>true</code>, 就是当一个条件没有值
     * 时忽略这个条件. 典型的情形是在<code>UI</code>中的搜索条件, 如果没有填写任何值就不
     * 考虑这个条件
     */
    private boolean ignoreEmpty = true;
    
    /** 相关的一组条件, 比如组成<code>... and (cond1 or cond2)</code> */
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
