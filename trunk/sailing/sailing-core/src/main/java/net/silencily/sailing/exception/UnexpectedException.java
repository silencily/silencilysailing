package net.silencily.sailing.exception;

/**
 * 系统中包装其它的<code>checked</code>异常的类型, 这类异常主要用于包装其它的异常同时在程序中
 * 不必处理的错误. 这类错误通常不再检索错误信息的<code>code</code>, 而是把错误堆栈中的信息直接
 * 作为信息. 这个异常的实质是它包装的那个异常, 本身并没有实际意义
 * @author scott
 * @since 2006-4-13
 * @version $Id: UnexpectedException.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 *
 */
public class UnexpectedException extends BaseSystemException {
    private Throwable actual;

    /**
     * 用于包装其他类型的异常, 并说明异常发生的动作和环境, 比如<pre>
     * new CannotReadFile("读取人员xxx档案附件错误", IOException)</pre>
     * @param msg    说明程序错误的环境和无法完成的动作信息, 将表现在页面或<code>log</code>中
     * @param actual 实际的异常
     */
    public UnexpectedException(String msg, Throwable actual) {
        super(msg, actual);
        this.actual = actual;
    }
    
    /** 返回实际的异常信息 */
    public Throwable getActual() {
        return this.actual;
    }
}
