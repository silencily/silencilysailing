package net.silencily.sailing.framework.core;

/**
 * <p>系统中应用组件的基接口, 这是一个标志性接口, 用以说明一个业务组件需要, 事务, 安全, 审计等
 * 通用服务, 实现这个接口的组件的每一个方法缺省都是事务性的, 符合<code>EJB's REQUIRED</code>
 * 事务标准, 同时当方法抛出<code>RuntimeException</code>时回滚事务, 而<code>CheckedException</code>
 * 不回滚事务</p>
 * 
 * @author scott
 * @since 2006-3-1
 * @version $Id: ServiceBase.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 *
 */
public interface ServiceBase {

}
