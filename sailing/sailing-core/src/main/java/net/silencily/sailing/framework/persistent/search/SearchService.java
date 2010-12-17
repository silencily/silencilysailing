package net.silencily.sailing.framework.persistent.search;

import java.util.List;

import net.silencily.sailing.framework.core.GlobalParameters;
import net.silencily.sailing.framework.core.ServiceBase;
import net.silencily.sailing.framework.persistent.filter.Paginater;

/**
 * 支持以<code>SearchCondition</code>组成的查询条件检索数据的服务, 条件的属性名表示<code>
 * domain object</code>的属性名(也可以是列名称), 这个服务需要一个已经定义的列出所有数据的语句
 * (不管它是什么,<code>HQL</code>,<code>SQL</code>,<code>IBATIS with inner-place</code>
 * 等等, 检索条件连同翻页信息将增加到这个语句的相当于<code>WHERE</code>部分, 如果不能提供这个
 * 信息, 那么至少要提供表名称
 * @author scott
 * @since 2006-4-19
 * @version $Id: SearchService.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 *
 */
public interface SearchService extends ServiceBase {
    String SERVICE_NAME = GlobalParameters.MODULE_NAME + ".searchService";
    /**
     * 根据条件检索数据, 检索出来的每行数据要组装成参数<code>dto</code>所指定的类型, 检索执行
     * 后满足条件的记录行数填写在参数<code>paginater's count</code>中
     * @param dto           检索数据库相关的元信息的<code>key</code>
     * @param conditions    检索条件, 如果是<code>null</code>将列出所有的数据
     * @param paginater     每次返回指定范围的结果集, 范围定义在这个参数中
     * @return              满足条件的结果集
     */
    List search(Class dto, SearchCondition[] conditions, Paginater paginater);
    
    /**
     * 根据条件检索数据, 检索出来的每行数据要组装成参数<code>dto</code>所指定的类型, 同时这个
     * 类型也是检索数据库表相关的元信息的<code>key</code>, 每检索一行数据都要调用一次<code>
     * RowCallback.process</code>方法, 检索执行后满足条件的记录行数填写在参数<code>paginater's count
     * </code>中
     * @param dto           检索数据库相关的元信息的<code>key</code>
     * @param conditions    检索条件, 如果是<code>null</code>将列出所有的数据
     * @param paginater     每次返回指定范围的结果集, 范围定义在这个参数中
     * @return              满足条件的结果集
     */
    List search(Class dto, SearchCondition[] conditions, Paginater paginater, RowCallback callback);
    
    /**
     * 为一个<code>domain object</code>注册一个元数据处理器, 如果已经存在相同类型的处理器,
     * 就用新的替换旧的. 这个<code>MetadataHolder</code>必须提供是什么类型的<code>Metadata
     * </code>
     * @param holder    要注册的<code>MetadataHolder</code>
     * @return <b>如果类型<code>dto</code>还没有注册就返回<code>null</code></b>, 否则
     *         返回原有的<code>MetadataHolder</code>
     * @throws NullPointerException 如果任何一个参数是<code>null</code>         
     * @throws IllegalArgumentException 如果这个<code>holder</code>没有提供要服务的<code>
     *         domain object</code>的类型, (<code>MetadataHolder.getDomainTpye() == null</code>)
     */
    MetadataHolder registry(MetadataHolder holder);
    
    /**
     * 取消类型为<code>type</code>的<code>MetadataHolder</code>的注册
     * @param type 要取消元信息注册的<code>domain object</code>类型
     * @return 如果成功取消返回<code>true</code>, 如果没有这个类型的源信息返回<code>false</code>
     * @throws NullPointerException 如果参数是<code>null</code>
     */
    boolean deregistry(Class type);
}
