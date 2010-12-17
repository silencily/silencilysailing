package net.silencily.sailing.framework.persistent.filter;

import java.util.List;

/**
 * 保存一个<code>DTO</code>在持久化层的元信息, 通常是数据库的表名称及属性与列名称的对应关
 * 系, 每一个<code>DTO</code>都有一个这个实例与之对应
 * @author scott
 * @since 2006-4-19
 * @version $Id: DtoMetadata.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 *
 */
public interface DtoMetadata {
    
    /**
     * 返回提供信息的<code>entry</code>的类型
     * @return
     */
    Class getDtoType();
    
    /**
     * 给定列名称返回相应的属性名称
     * @param columnName 数据库表的列名称
     * @return 这个列对应的属性名称, 可以返回<code>null</code>表示没有属性与之对应
     */
    String getPropertyName(String columnName);
    
    /**
     * 给定属性名称返回相应的列名称
     * @param propertyName <code>domain object</code>的属性名称
     * @return 这个属性对应的列名称, 可以返回<code>null</code>表示没有列与之对应
     */
    String getColumnName(String propertyName);
    
    /**
     * 返回这个<code>domain object</code>对应的数据库的表/视图名称
     * @return <code>domain object</code>对应的数据库的表/视图名称
     */
    String getTableName();
    
    /**
     * 返回组成这个<code>domain object</code>的主键/唯一键的属性名称
     * @return 组成这个<code>domain object</code>的主键/唯一键的属性名称
     */
    String[] getPrimaryKey();

    /**
     * 返回从数据库中的一行组装<code>domain object</code>时要组装的属性名称, 只有在这个列表
     * 中的属性才组装, <b>但如果返回<code>EMPTY_LIST</code>将组装所有的属性</b>
     * @return 要组装的<code>domain object</code>的属性
     */
    List getProperties();
}
