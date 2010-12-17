package net.silencily.sailing.framework.persistent.filter;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;


/**
 * <p>表示操作数据时要满足的条件之一, 多个这个类型的实例组成一次特定操作的条件. 具体表示列名
 * 称或<code>DTO</code>属性名称依赖于使用它的操作数据的策略而定, 在架构的查询操作中这个条件被
 * 解释为基于<code>DTO</code>的属性</p>
 * <p>这个类的用法如下<ul>
 * <li><code>prepend</code>使用{@link ConditionConstants#DEFAULT_RELATION <code>and</code>}</li>
 * <li><code>operator</code>使用{@link ConditionConstants#DEFAULT_OPERATOR <code>=</code>}</li>
 * <li><code>type</code>使用{@link ConditionConstants#DEFAULT_TYPE <code>String</code>}</li>
 * <li>属性名和组合条件(<code>compositeCondition</code>至少选择一个</li>
 * <li>如果设置了组合条件,那么忽略条件中的属性名<code>name</code>优先使用组合条件</li></ul>
 * </p>
 * @author scott
 * @since 2006-4-18
 * @version $Id: Condition.java,v 1.1 2010/12/10 10:54:17 silencily Exp $
 * @see {@link AbstractSearchConditions}
 */
public class Condition implements ConditionConstants, Serializable, Comparable {
    
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
    
    /** 如果存在多个条件, 这个属性决定了组成条件表达式时各个条件的顺序 */
    private int order;
    
    /**
     * 这个条件是否被列入执行查询的条件中, 比如按照目前的统一规范, 不输入值的条件不作为查询
     * 条件, 对于某些情况, 比如查询人员时条件的值是人员<code>id</code>, 但还有姓名作为显示,
     * 在这种情况下保存人员姓名的<code>condition</code>是不作为查询条件的. 如果没有指定这个
     * 值, 缺省值是<code>true</code>, 说明要考虑这个条件. 这个属性与<code>ignoreEmpty</code>
     * 显著区别是这个条件的值不是空, 但在执行查询时仍然要忽略这个条件, 这个属性实际上更多地为表现
     * 层作了支持
     */
    private boolean place = true;
    
    /**
     * 如果这个条件的值是<code>null</code>或对于字符串类型是<code>empty</code>在组装查
     * 询条件时是否忽略这个条件. 对于大多数情况是<code>true</code>, 就是当一个条件没有值
     * 时忽略这个条件. 典型的情形是在<code>UI</code>中的搜索条件, 如果没有填写任何值就不
     * 考虑这个条件
     */
    private boolean ignoreEmpty = true;
    
    /**
     * 当属性名中出现 "." 时是否创建别名, 在两种情况下可能出现 "."
     * <ul>
     * <li>此属性是一个关联属性, 此时 createAlias 应该为 true, 这也是默认情况</li>
     * <li>此属性是一个复合类型, 即 org.hibernate.usertype.CompositeUserType 的实现,  此时 createAlias 应该为 false</li>
     * </ul>
     */ 
    private boolean createAlias = true;
    
    /** 相关的一组条件, 比如组成<code>... and (cond1 or cond2)</code> */
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
            throw new NullPointerException("比较Condition顺序时,参数是null");
        }
        if (!(o instanceof Condition)) {
            throw new ClassCastException("比较Condition顺序时,参数不是Condition类型");
        }
        Condition c = (Condition) o;
        int ret = 0;
        if (this.equals(c)) {
            ret = 0;
        } else {
            ret = this.order - c.order;
            /* 这里忽略了 ret == 0 的情况, 因为在 ret == 0 时, 与 equals 方法不一致 */
            ret = (ret > 0) ? 1 : -1;
        }
        return ret;
    }
    
    /**
     * <p>执行查询时是否考虑这个条件, 如果明确设置了<code>false</code>或者这个条件的值是空
     * 并且<code>ignoreEmpty</code>返回<code>true</code>,执行查询时就不考虑这个条件</p>
     * <p>当执行查询时这个方法作为是否考虑这个条件的唯一标准</p>
     * @return 执行查询时是否考虑这个条件
     */
    public boolean isPlace() {
        if (place) {
            /* value 不是空值或 value 是空而且不忽略空值 */
            return (isEmpty() && !ignoreEmpty || !isEmpty() || (getCompositeConditions().length > 0));
        } else {
            /* 明确指定了不作为查询条件 */
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
