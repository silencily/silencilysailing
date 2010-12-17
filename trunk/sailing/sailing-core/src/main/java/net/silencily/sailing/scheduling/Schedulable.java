package net.silencily.sailing.scheduling;

import java.util.Timer;

/**
 * <p>一个用于执行时间调度任务的接口, 实现这个接口的类可以在指定的时间执行某种任务. 任务分为延时
 * 执行或定期执行两种类型<ul>
 * <li>指定了<code>period</code>参数:每过<code>period</code><b>秒</b>就执行一个<code>
 * execute</code>方法, 如果不需要这个参数, 方法{@link #getPeriod() <code>getPeriod</code>}
 * 应该返回{@link #NON_PERIOD NON_PERIOD}. 比如要每过10秒执行一次<code>execute</code>方
 * 法可以让<code>getPeriod</code>方法返回<code>10</code></li>
 * <li>指定了<code>delay</code>参数:过了<code>delay</code><b>秒</b>后执行<code>execute</code>
 * 方法, 如果不需要延时执行方法{@link #getDelay() <code>getDelay</code>}应该返回{@link #NON_DELAY
 * <code>NON_DELAY</code></li>
 * <li>指定了<code>specified time</code>, 每到指定的时间就调用<code>execute</code>方法,
 * 这个参数的格式是<code>HH:mm:ss</code>或<code>HH:mm</code>形式, 其中:<code>HH</code>
 * 是<code>24</code>小时的钟点, <code>mm</code>是分钟, <code>ss</code>是秒钟, 如果设置了
 * 这个参数将忽略<code>period</code>参数的值, 比如要在每天的<code>21:30</code>调用<code>
 * execute</code>方法可以让<code>getSpecifiedTime</code>返回<code>21:30</code></li></ul>
 * 
 * @author scott
 * @since 2006-2-26
 * @version $Id: Schedulable.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 *
 */
public interface Schedulable {
    /**
     * 当系统启动时, 架构调用这个方法, 以便执行某种周期性的任务. 架构捕获这个抛出的任何异常但是
     * 不再重新抛出, 仅仅<code>log</code>错误信息. 应用代码应该在这里处理各种可能的错误情况.
     * <b>注意</b>: 不要对参数<code>timer</code>做本地的引用, 也不要直接进行<code>cancel</code>
     * 等释放资源的操作
     * 
     * @param timer 执行周期性任务的<code>Timer</code>
     */
    void schedules(Timer timer);
    
    /**
     * 当系统关闭时由架构调用这个方法, 应用可以在这里处理掉占用的资源, 持久化周期性任务的调度信息
     * 等, 架构捕获这个抛出的任何异常但是不再重新抛出, 仅仅<code>log</code>错误信息. 应用代码
     * 应该在这里处理各种可能的错误情况. 同时注意调用这个方法时系统已经准备关闭而且是不可逆的, 在
     * 这里再抛出异常没有很大的意义了
     */
    void cancel();
}
