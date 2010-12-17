package net.silencily.sailing.framework.persistent.search;

import java.util.List;

/**
 * 保存一个<code>domain object</code>在持久化层的元信息, 通常是数据库的表名称及属性与列名称
 * 的对应关系, 每一个<code>domain object</code>都有一个这个实例与之对应, 要注意到<code>
 * domain object</code>的一些属性必须是实例类型的值, 比如<code>listStatement</code>
 * @author scott
 * @since 2006-4-19
 * @version $Id: MetadataHolder.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 *
 */
public interface MetadataHolder {
    
    /**
     * 返回提供信息的<code>entry</code>的类型
     * @return
     */
    Class getDomainType();
    
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
     * 返回从数据库中的一行组装<code>domain object</code>时要组装的属性名称, 只有在这个列表
     * 中的属性才组装, <b>但如果返回<code>EMPTY_LIST</code>将组装所有的属性</b>
     * @return 要组装的<code>domain object</code>的属性
     */
    List getProperties();
    
    /**
     * 返回检索所有数据的<code>select</code>(或相等的语句, 比如<code>HSQL:select * from userDto
     * </code>,通常在服务组件中都需要定义这个<code>API</code>, 以满足对外提供各种支持. <b>但是这个
     * 语句中不要出现参数占位符</b>
     * @return 检索所有数据的<code>select</code>语句
     */
    String getQueryStatement();

}
