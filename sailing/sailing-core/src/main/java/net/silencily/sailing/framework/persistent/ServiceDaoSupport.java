package net.silencily.sailing.framework.persistent;

/**
 * 一个标识<code>DAO</code>模式的接口, 所有的<code>DAO</code>类必须扩展这个标记接口, 以便
 * 架构加入通用的行为. 比如在增加事务行为时, 排除<code>DAO's getter/setter</code>方法
 * 
 * @author scott
 * @since 2006-3-20
 * @version $Id: ServiceDaoSupport.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 *
 */
public interface ServiceDaoSupport {

}
