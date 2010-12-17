package net.silencily.sailing.framework.persistent.filter;

/**
 * 保存条件中使用到的常量
 * @author scott
 * @since 2006-4-21
 * @version $Id: ConditionConstants.java,v 1.1 2010/12/10 10:54:17 silencily Exp $
 *
 */
public interface ConditionConstants {
    /** 表示属性等于值 */
    String EQUAL = "=";
    
    /** 表示属性不等于值 */
    String NOT_EQUAL = "!=";
    
    /** 表示属性大于或等于值 */
    String GREATER_EQUAL = ">=";
    
    /** 表示属性大于值 */
    String GREATER = ">";
    
    /** 表示属性小于或等于值 */
    String LESS_EQUAL = "<=";
    
    /** 表示属性小于值 */
    String LESS = "<";
    
    /** 表示属性满足匹配的值 */
    String LIKE = "like";
    
    /** 表示<code>sql</code>语句中的<code>in</code>关系操作符 */
    String IN = "in";
    
    /** 表示属性之间的"and"关系 */
    String AND = "and";
    
    /** 表示属性之间的"or"关系 */
    String OR = "or";
    
    /** 嵌套属性之间的分隔符 */
    String PROPERTY_SEPARATOR = ".";
    
    /** 表示属性之间的"not"关系 */
    /*
    String NOT = "not";
    */

    /** 当不指定属性与值之间的操作符时使用的缺省操作符 */
    String DEFAULT_OPERATOR = EQUAL;
    
    /** 当不指定属性之间的关系时使用这个缺省的关系 */
    String DEFAULT_RELATION = AND;
    
    /** 当不指定条件的类型时缺省的条件值类型 */
    Class DEFAULT_TYPE = String.class;
}
