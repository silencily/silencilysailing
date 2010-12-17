/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project framework
 */
package net.silencily.sailing.business;

/**
 * 可被初始化的服务接口, 如果需要初始化一些程序运行期必需的数据或其他资源, 实现此接口并注册到 spring 容器中,
 * 架构在容器初始化完毕时将自动侦测出这些 bean 并执行她们, 注意执行顺序是不固定的, 也就意味着两个 Initialiazble 的实现类
 * 不能互相依赖于对方的执行结果
 * @since 2006-7-28
 * @author java2enterprise
 * @version $Id: Initializable.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 */
public interface Initializable {
	
	/**
	 * spring 容器初始化完毕时调用, do any thing you want do
	 *
	 */
	void init();
	
}
