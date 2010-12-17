package net.silencily.sailing.framework.persistent.filter;

/**
 * 基于架构安全,通用查询目的修改一个<code>sql</code>参数的回调接口, 实现作为<code>SqlMapProxy</code>
 * 的属性配置
 * 
 * @author scott
 * @since 2006-5-6
 * @version $Id: ReworkingParameter.java,v 1.1 2010/12/10 10:54:17 silencily Exp $
 *
 */
public interface ReworkingParameter {
    /**
     * 提供了可修改的参数, 具体的内容取决于这个实例作用的方法 
     * @param params
     */
    void rework(Object[] params);
    
    /**
     * 恢复修改了的参数
     */
    void restore();
}
