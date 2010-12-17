package net.silencily.sailing.framework.persistent.search;

import net.silencily.sailing.framework.persistent.filter.Paginater;

/**
 * 把查询条件合并到<code>sql</code>语句的<code>where</code>部分
 * @author scott
 * @since 2006-4-23
 * @version $Id: SqlPopulator.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 *
 */
public interface SqlPopulator {
    /**
     * 用条件合并<code>sql</code>语句和参数
     * @param sql   要合并的<code>sql</code>
     * @param sp    条件
     * @return      合并后的<code>SearchCondtionAndParameter</code>, 这个对象中的
     *              <code>condition</code>部分是已经合并后的<code>sql</code>语句, 
     *              <code>params</code>部分加入了条件的参数
     */
    SearchConditionAndParameter where(String sql, SearchConditionAndParameter sp);
    
    /**
     * 在<code>sql</code>的基础上实现翻页功能
     * @param sql   要合并的<code>sql</code>
     * @param sp    条件
     * @return      合并后的<code>SearchCondtionAndParameter</code>, 这个对象中的
     *              <code>condition</code>部分是已经合并后的<code>sql</code>语句, 
     *              <code>params</code>部分加入了条件的参数
     */
    SearchConditionAndParameter pagination(Paginater paginater, SearchConditionAndParameter sp);
    
    /**
     * 在原来的<code>update</code>语句中增加要更新的列及参数
     * @param sql 要修改的<code>update</code>语句
     * @param sp  要增加的更新语句
     * @return 修改后的<code>sql</code>及参数
     */
    SearchConditionAndParameter update(String sql, SearchConditionAndParameter sp);
    
    /**
     * 返回统计满足条件的记录数的<code>SearchConditionAndParameter</code>, 其中的<code>
     * condition</code>是检索行数的<code>sql</code>, <code>params</code>仍然是原来的参数
     * @param paginater 设置记录行数的{@link Paginater <code>Paginater</code>}
     * @param sp        <code>sql</code>语句和条件参数
     * @return          检索记录行数的<code>sql</code>和参数
     */
    SearchConditionAndParameter count(Paginater paginater, SearchConditionAndParameter sp);
}
