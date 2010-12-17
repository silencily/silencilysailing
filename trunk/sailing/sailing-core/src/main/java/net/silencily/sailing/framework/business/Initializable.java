package net.silencily.sailing.framework.business;

/**
 * 架构的一个回调接口, 用于在系统启动时执行一些初始化工作, 比如预先创建一些必需的数据以保证服务
 * 可以正常执行, 这个情形经常是一个子系统第一次投入使用时初始化系统中必要的编码. 比如电厂中的专
 * 业编码, 简单地说一个子系统或一个服务要开始投入运行至少要具备什么数据, 就是通过这个接口来实现
 * , 实现了这个接口的服务架构保证在初始化线程结束之前不能调用这个服务的任何方法, 对这个接口的调
 * 用发生在整个系统初始化工作完成之后, 就是<code>spring framework bean factory</code>初始
 * 化结束准备投入使用
 * 
 * @author zhangli
 * @version $Id: Initializable.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 * @since 2007-6-4
 */
public interface Initializable {
    
    /**
     * 组件所需的数据, 状态已经初始化了吗? 如果是就不执行初始化方法, 否则将执行初始化方法
     * @return 如果初始化完成返回<code>true</code>, 否则是<code>false</code>
     */
    boolean isInitialized();
    
    /**
     * 系统初始化工作结束后开始调用这个方法, 如果在初始化过程中发生错误那么这个服务的所有方法都被
     * 锁定
     * @exception InitializableException 如果在初始化过程中发生错误
     * @exception InitializableInProcessException 如果在初始化过程中对服务的方法进行调用
     */
    void initialize() throws InitializableException, InitializableInProcessException;
}
