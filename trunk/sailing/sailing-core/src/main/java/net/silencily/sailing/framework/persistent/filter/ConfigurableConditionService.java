package net.silencily.sailing.framework.persistent.filter;

/**
 * 可配置的条件服务, 可以修改,合并架构的安全等条件和应用的条件到当前执行线程. 这个接口是架构
 * 用于控制数据访问的, 不用于通常的应用
 * @author scott
 * @since 2006-5-3
 * @version $Id: ConfigurableConditionService.java,v 1.1 2010/12/10 10:54:17 silencily Exp $
 * @see Conditions
 * @see ConditionService
 */
public interface ConfigurableConditionService extends ConditionService {
    /**
     * 把当前应用的条件合并到系统本身的条件中
     * @param conditions    特定应用的条件
     * @param paginater     分页信息
     * @throws NullPointerException 如果任何一个条件是<code>null</code>
     */
    void applyAppendCondition(Condition[] conditions, Paginater paginater);
    
    /**
     * 取消合并到系统条件中的当前应用的条件
     */
    void cancelAppendCondition();
    
    /**
     * 组装当前执行线程中的条件为<code>Conditions</code>, 参数可以是<code>null</code>,
     * 执行组装条件子句时使用缺省的机制, 无法使用特定的元信息
     * @param  dto 要操作的<code>DTO</code>类型
     * @return 组装后的组合条件, 如果当前没有任何条件返回
     *         {@link Conditions#EMPTY_CONDITIONS <code>EMPTY_CONDITIONS</code>}
     * @see DefaultDtoMetadata#INDEPENDENT_DTO_METADATA
     */
    Conditions populateCondition(Class dto);
    
    /**
     * 在参数中的<code>sql</code>或其他的语句和参数中加入分页部分, 这种处理绝大多数情况下
     * 是为<code>UI</code>提供支持
     * @param sp        要加入分页的完整的<code>sql</code>或其它的语句(比如:<code>hql</code>)和参数
     * @return   加入分页后的<code>sql</code>语句和参数
     */
    StatementAndParameters populateSqlWithPaginater(StatementAndParameters sp);
}
