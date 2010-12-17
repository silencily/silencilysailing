package net.silencily.sailing.resource;

import java.util.Map;

/**
 * <p>当系统启动时需要执行初始化工作的组件需要实现这个接口, 最常用的情景是新部署一个子系统在应用
 * 启动时初始化资源, 比如从其它的位置放置到<code>vfs</code>中的资源, 保存在文件系统的配置信
 * 息写到数据库中, 某些安全数据的初始化等</p>
 * <p>在现有的基于<code>spring framework</code>的架构中, 这个接口的实现配置为子系统服务
 * 描述的<code>ServiceInfo</code>的属性, 而且使用<code>FQN</code>, 以避免实例化<code>
 * ServiceInfo</code>时关联的初始化</p>
 * @author Scott Captain
 * @since 2006-5-17
 * @version $Id: InitializingResource.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 *
 */
public interface InitializingResource {
    
    /**
     * 应用启动时会查询这个组件是否需要执行初始化工作
     * @return 如果需要返回<code>true</code>, 否则<code>false</code>
     */
    boolean requireInitialization();
    
    /**
     * 在应用启动时如果查询{@link #requireInitialization() <code>requireInitialization</code>}
     * 方法返回<code>true</code>就执行初始化工作
     * @param parameters 执行初始化时可以使用的上下文参数, <code>key</code>是类名称, 
     *                   <code>value</code>是类的实例, 比如如果需要<code>javax.servlet.ServletContext</code>
     *                   ,那么<code>key</code>就是"ServletContext", 这个<code>key</code>
     *                   对应的实例就是<code>ServletContext</code>的实例
     */
    void initialize(Map parameters);
}
