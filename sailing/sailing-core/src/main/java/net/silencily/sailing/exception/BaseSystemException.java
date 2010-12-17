package net.silencily.sailing.exception;


/**
 * <p>系统中所有系统异常(<code>UncheckedException</code>)的超类, 用于处理各种可能的错误情
 * 况, 与<code>BaseAppException</code>的使用相对, 这种异常是开发过程中的主流. 这种异常并没
 * 有涵盖<code>IllegalArgumentException</code>,<code>IllegalStateException</code>等
 * 几个基础的异常, 对于持久化层或最低层仍然可以使用<code>java</code>本身提供的那些异常, 但不应
 * 该在业务逻辑层和表现层, 在这些情况下就应该使用这个异常做基类的有意义的异常, 比如验证失败等直接
 * 抛出, 由架构负责展示和处理</p>
 * <p>应该注意构造器<code>BaseSystemException(Object param)</code>, 经常处理在开发时而
 * 不是部署时创建异常信息. 这时异常信息可以直接从<code>param</code>参数传进来</p>
 * @author scott
 * @since 2006-3-21
 * @version $Id: BaseSystemException.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 *
 */
public abstract class BaseSystemException extends RuntimeException implements ExceptionInfo {
    private String key;
    private Object[] params;
    private String errorInformation;
    
    /** 用于错误信息中没有占位符的情况下传递参数 */
    public static final Object EMPTY_PARAMETER = ExceptionInfo.NULL_PARAM;
    
    /**
     * 缺省构造器, 没有指定任何错误信息, 最常用的情况是在错误代码中用异常名称(短名)配置了错误信息.
     * 表示这个信息没有更多的内容要提供, 比如错误代码配置是:<tt>CannotConnectToDatabase=网
     * 络中断,不能连接数据库</tt>. 像这类异常不需要进行任何额外说明,可以使用这个构造器
     */
    protected BaseSystemException() {
        super();
    }

    /**
     * 用于在错误信息中定义了包含占位符的<code>code</code>, 从这个构造器指定动态显示内容. 这个
     * 构造器用于某种确定的异常现象, 而不同的只是这个异常所处的上下文环境的差异. 比如发生在加载配置
     * 文件时没有按照要求编写配置文件的错误, 可以像后面这样定义错误<code>code</code>: <tt>
     * ConfigurationException=加载{0}发生错误:{1}</tt>, 实际的调用类似于<code>
     * new ConfigurationException(new String[] {"Spring xml bean", "没有定义ServiceInfo组件"})</code>
     * 
     * @param params 替换占位符的信息 如果这个信息没有占位符使用{@link #EMPTY_PARAMETER <code>EMPTY_PARAMETER}
     */
    protected BaseSystemException(Object[] params) {
        super();
        this.params = params;
    }
    
    /**
     * 用于不使用异常处理器生成错误<code>code</code>的机制, 在代码中直接指定<code>code</code>,
     * 缺省情况下异常处理器生成检索错误信息的<code>code</code>通常是<code>抛出异常的全限定类名
     * .方法名.异常类名</code>, 在一个方法内有多个同名异常或这个组件被用于不同的环境下时多采用这种
     * 预先定义错误信息的策略
     * @param key       检索错误信息的<code>code</code>
     * @param params    错误信息占位符, 如果这个信息没有占位符使用{@link #EMPTY_PARAMETER <code>EMPTY_PARAMETER}
     */
    protected BaseSystemException(String key, Object[] params) {
        super();
        this.key = key;
        this.params = params;
    }
    
    /**
     * 用于包装其它的异常, 并指明异常发生的环境信息. 这个构造器是系统中<code>UnexpectedException</code>
     * 的缺省构造器. 用于包装<code>Unchecked</code>异常或无法处理的异常
     * @param msg    发生异常的环境说明
     * @param cause  实际的异常
     */
    protected BaseSystemException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public String getKey() {
        return key;
    }

    public Object[] getParams() {
        if (this.params == null) {
            return NULL_PARAM;
        }
        
        return this.params;
    }

    public String getErrorInformation() {
        return this.errorInformation;
    }

    public void setErrorInformation(String msg) {
        this.errorInformation = msg;
    }

    public Throwable getThrowable() {
        return ExceptionUtils.getActualThrowable(this);
    }
}
