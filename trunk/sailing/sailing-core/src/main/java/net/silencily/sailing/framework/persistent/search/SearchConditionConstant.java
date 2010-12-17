package net.silencily.sailing.framework.persistent.search;

/**
 * 保存查询条件中使用到的常量
 * @author scott
 * @since 2006-4-21
 * @version $Id: SearchConditionConstant.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 *
 */
public interface SearchConditionConstant {
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
    
    /** 表示属性之间的"and"关系 */
    String AND = "and";
    
    /** 表示属性之间的"or"关系 */
    String OR = "or";
    
    /** 表示属性之间的"not"关系 */
    /*
    String NOT = "not";
    */

    /** 当不指定属性与值之间的操作符时使用的缺省操作符 */
    String DEFAULT_OPERATOR = EQUAL;
    
    /** 当不指定属性之间的关系时使用这个缺省的关系 */
    String DEFAULT_RELATION = AND;
}
