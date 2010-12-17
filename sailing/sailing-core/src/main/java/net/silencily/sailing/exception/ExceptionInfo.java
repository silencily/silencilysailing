package net.silencily.sailing.exception;

/**
 * <p>每个异常都应该包含的信息, 比如用于客户端的错误提示信息. 架构中最基础的两种异常都实现了这个
 * 接口, 以提供一致的异常表现行为</p>
 * 
 * @author scott
 * @since 2006-3-22
 * @version $Id: ExceptionInfo.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 *
 * @see BaseAppException
 * @see BaseSystemException
 */
public interface ExceptionInfo {
    
    /**
     * 如果没有用于<code>MessageFormat</code>替换的参数, 返回这个对象
     */
    Object[] NULL_PARAM = new Object[0];

    /**
     * 生成有意义的错误信息, 这个信息用于客户端的显示或<code>log</code>, 不应该用于开发中判断
     * 的依据. 
     * @return 具体的异常信息, 不返回<code>empty</code>或<code>null</code>
     */
    String getErrorInformation();
    
    /**
     * <p>用于配置的设置显示信息的方法, 应用在正常情况下不要使用这个方法, 客户化参数应该配置在资源文
     * 件中, <b>实现注意:</b>如果参数指定了<code>empty</code>或<code>null</code>就忽略这
     * 个传进来的值</p>
     * <p><code>BaseExceptionHandler</code>用这个方法处理客户化信息</p>
     * @param msg 提供<code>log</code>和显示的信息
     */
    void setErrorInformation(String msg);
    
    /**
     * 返回用于异常参数替换的参数值
     * @return 如果没有参数替换返回{@link #NULL_PARAM <code>NULL_PARAM</code>}
     */
    Object[] getParams();
    
    /**
     * 返回一个异常信息对应的<code>key</code>, 这个<code>key</code>用来检索对应的外部化信息
     * @return 如果直接设置了信息(通过构造器)或没有<code>key</code>返回<code>null</code>
     */
    String getKey();
    
    /**
     * 检索这个<code>ExceptionInfo</code>表示的异常
     * @return 创建这个实例时的异常, 不返回<code>null</code>
     */
    Throwable getThrowable();
}
