package net.silencily.sailing.framework.persistent.filter;


/**
 * 保存系统中查询数据的信息, 包括检索条件, 翻页等信息. 这个类是通用查询架构中的一部分, 用于
 * 收集参数. 可以检索{@link ConditionInfo#isEmpty()}检查是否设置过条件, 如果这个方法返回
 * <code>true</code>, 这个实例就是一个空条件
 * @author scott
 * @since 2006-4-29 1:58
 * @version $Id: ConditionInfo.java,v 1.1 2010/12/10 10:54:17 silencily Exp $
 *
 */
public class ConditionInfo {
    /**
     * 用于架构的条件, 这个条件通常是安全等控制的条件, 不能由应用设置
     */
    private Condition[] originalConditions;
    
    /**
     * 用于应用的条件, 这些条件是特定的应用设置的
     */
    private Condition[] appendConditions;

    /** 用于应用的持久化层的分页信息 */
    private Paginater paginater;
    
    /** 
     * 是否在当前执行环境下禁止应用检索条件, 常用于在持久化层检索多个业务实体时避免一个业务实体
     * 的条件干扰另一个业务实体的检索, 缺省情况下禁止应用这个条件, 否费显式激活
     */
    private boolean concealQuery = true;
    
    /** 
     * 因为从{@link ContextInfo#getContextCondition()}返回<code>null</code>使它的客
     * 户程序总是判断是否为<code>null</code>, 为了避免这个麻烦, 从前面的方法中返回一个空的
     * 实例, 这个标志就是说明这种空实例的
     */
    private boolean empty = true;

    public ConditionInfo() {
        this(new Condition[0]);
    }
    
    /**
     * 创建一个包含了预先设置了条件的<code>ConditionInfo</code>, 预先设置的条件是配置在安全
     * 系统中的条件, 比如定义一个资源时可以定义基于部门的选择条件, 使登陆用户仅仅看到所属部门的
     * 信息, 这个构造函数应该在安全中调用, 不是应用的<code>API</code>
     * @param conditions
     */
    public ConditionInfo(Condition[] conditions) {
        this.originalConditions = conditions;
        this.appendConditions = new Condition[0];
        this.paginater = Paginater.NOT_PAGINATED;
        empty = false;
    }

    public Condition[] getAppendConditions() {
        if (appendConditions == null) {
            appendConditions = new Condition[0];
        }
        return appendConditions;
    }

    public void setAppendConditions(Condition[] appendConditions) {
        this.appendConditions = appendConditions;
        empty = false;
    }
    
    public void addAppendConditions(Condition condition) {
        if (condition != null) {
            Condition[] newConditions = new Condition[this.getAppendConditions().length + 1];
            System.arraycopy(this.appendConditions, 0, newConditions, 0, this.appendConditions.length);
            newConditions[newConditions.length - 1] = condition;
            this.appendConditions = newConditions;
            empty = false;
        }
    }

    public Condition[] getOriginalConditions() {
        if (originalConditions == null) {
            originalConditions = new Condition[0]; 
        }
        return originalConditions;
    }

    public void setOriginalConditions(Condition[] frameworkConditions) {
        this.originalConditions = frameworkConditions;
        empty = false;
    }

    public Paginater getPaginater() {
        if (this.paginater == null) {
            return Paginater.NOT_PAGINATED;
        }
        return paginater;
    }

    public void setPaginater(Paginater paginater) {
        this.paginater = paginater;
        empty = false;
    }
    
    /**
     * 这个条件的容器是否是<code>empty</code>,就是没有设置过任何条件, 仅仅是标志从未设置过
     * 条件, 而不是说有条件
     * @return 是否设置过条件, 如果这个条件的容器是一个新鲜的实例, 从未设置过条件返回<code>true</code>
     * @see {@link #empty}注释
     */
    public boolean isEmpty() {
        return empty;
    }
    
    /** 
     * 是否禁止应用这个检索条件
     * @return 如果禁止应用条件返回<code>true</code>, 缺省情况下是禁止应用这个条件 
     */
    public boolean isConcealQuery() {
        return concealQuery;
    }
    
    /** 
     * 查询时应用或禁止这个条件
     * @param concealQuery <code>true</code>禁止应用条件, <code>false</code>查询时不要禁止这个条件
     */
    public void setConcealQuery(boolean concealQuery) {
        this.concealQuery = concealQuery;
    }
}
