package net.silencily.sailing.framework.persistent.search;

/**
 * 表示执行一次特定查询时的检索条件, 这个条件由多个<code>SearchCondition</code>组成, 通过
 * <code>toString</code>方法可以转换成特定持久化策略的<code>where</code>条件
 * @author scott
 * @since 2006-4-18
 * @version $Id: SearchConditions.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 *
 */
public interface SearchConditions {
    /** 生成的检索串中需要替换参数的占位符, 比如<code>JDBC</code>中的"?", <code>IBATIS's #id#</code> */
    String PLACE_HOLDER = "?";
    
    /**
     * 返回这个实例要处理的查询条件
     * @return 这个实例要处理的查询条件, 如果没有返回长度为<b>0</b>的数组
     */
    SearchCondition[] getSearchCondition();
    
    /** 
     * 返回处理后的结果, 包括条件和条件的取值
     * @return 条件和条件的取值, 永远不返回<code>null</code>
     */
    SearchConditionAndParameter getResult();
    
    /**
     * 合并其它的条件到这个组合条件中, 如果其他的条件没有包含任何检索条件就不做任何处理. 忽略要合
     * 并的<code>SearchConditions</code>中的<code>MetadataHolder</code>, 这样的结果就
     * 是合并进来的条件作用于这个实例的<code>domain object</code>
     * @param conditions 其它的条件
     * @throws NullPointerException 如果参数是<code>null</code>
     */
    void join(SearchConditions conditions);
}
