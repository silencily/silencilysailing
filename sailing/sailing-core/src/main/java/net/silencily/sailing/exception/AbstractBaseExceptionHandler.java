package net.silencily.sailing.exception;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>异常处理的抽象实现, 当非业务异常时动态扩展以实现<code>ExceptionInfo</code>提供异常信息,子类
 * 需要提供检索错误信息的方法{@link #getMessage(String, ExceptionInfo) getMessage}处理使
 * 用指定的<code>key</code>检索信息</p>
 * @author scott
 * @since 2006-4-12
 * @version $Id: AbstractBaseExceptionHandler.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 */
public abstract class AbstractBaseExceptionHandler implements BaseExceptionHandler {
    private Class[] interfaces = new Class[] {ExceptionInfo.class};
    protected Log logger = LogFactory.getLog(AbstractBaseExceptionHandler.class);

    public ExceptionInfo handle(Exception info, Class clazz, String method) {
        Exception ex = checkParameter(info);
        ExceptionInfo ret = (ExceptionInfo) ex;
        /* 一种情况就是在创建异常时指定了 key, 参见 BaseSystemException */
        if (StringUtils.isNotBlank(ret.getKey())) {
            ret.setErrorInformation(getMessage(ret.getKey(), ret));
            return ret;
        }

        String classAndMethodName = clazz.getName() + "." + method;
//        String throwableName = ClassUtils.getShortClassName(ret.getThrowable().getClass());
        /* Java2Enterprise 建议异常也使用FQN, 避免同名异常冲突 */
        String throwableName = ret.getThrowable().getClass().getName();
        /* 根据FQN.methodName.异常名称 生成 key */
        String msg = getMessage(classAndMethodName + "." + throwableName, (ExceptionInfo) ex);
        
        /* 根据FQN.methodName 生成 key */
        if (StringUtils.isBlank(msg)) {
            msg = getMessage(classAndMethodName, (ExceptionInfo) ex);
        }
        
        /* 根据异常名称生成 key */
        if (StringUtils.isBlank(msg)) {
            msg = getMessage(throwableName, (ExceptionInfo) ex);
        }
        
        if (StringUtils.isNotBlank(msg)) {
            ((ExceptionInfo) ex).setErrorInformation(msg);
        } else {
            ((ExceptionInfo) ex).setErrorInformation(ExceptionUtils.getCauseMessage(ex));
        }

        return (ExceptionInfo) ex;
    }
    
    /**
     * 使用指定的<code>key</code>检索预先配置在文件中的错误信息
     * @param key       配置的<code>key</code>
     * @param info      异常信息
     * @return 如果使用<code>key</code>找到了预先配置的信息就返回这个检索到的信息, 否则返回<code>null</code>
     */
    abstract protected String getMessage(String key, ExceptionInfo info);

    /**
     * 检查并处理参数, 如果这个异常没有实现<code>ExceptionInfo</code>, 就创建一个实现了
     * <code>ExceptionInfo</code>的代理. 这个异常可能是嵌套了<code>ExceptionInfo</code>
     * 的异常, 如果是这样在处理过程中要深入到这个类型的异常为止
     * @param ex 要处理的异常
     * @return 处理过的异常, 可能是一个代理类
     */
    protected Exception checkParameter(Exception ex) {
        if (ex == null) {
            return NULL_PARAM;
        }

        if (ExceptionInfo.class.isAssignableFrom(ex.getClass())) {
            return ex;
        }
        
        ex = (Exception) ExceptionUtils.getActualThrowable(ex);
        
        if (logger.isDebugEnabled()) {
            logger.debug("生成ExceptionInfo之前的异常," + ex.getMessage(), ex);
        }

        try {
            ex.getClass().newInstance();
        } catch (Exception e) {
            /* 如果不能实例化这个异常就做成InvocationTargetException */
            ex = new InvocationTargetException(ex);
        }
        
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(interfaces);
        enhancer.setSuperclass(ex.getClass());
        enhancer.setUseFactory(false);
        enhancer.setCallback(new ExceptionInfoMethodInterceptor(ex));
        return (Exception) enhancer.create();
    }

    private static NullParameterException NULL_PARAM = new NullParameterException();
    
    /**
     * 用于处理发生在异常处理过程本身的异常: 使用<code>null</code>错误地调用这个类的方法
     */
    private static class NullParameterException extends BaseSystemException {
        private String msg = "BaseExceptionHandler's handle()处理异常时输入参数是null";

        public String getErrorInformation() {
            return msg;
        }        
    };
    
    /**
     * 当发生系统异常时通过这个处理器代理异常实现<code>ExceptionInfo</code>, 便于异常处理器
     * 统一处理
     */
    private class ExceptionInfoMethodInterceptor implements MethodInterceptor, ExceptionInfo {
        private String errorMessage;
        private String key;
        private Object[] params = ExceptionInfo.NULL_PARAM;
        private Throwable cause;

        public ExceptionInfoMethodInterceptor(Throwable cause) {
            this.cause = cause;
            this.errorMessage = ExceptionUtils.getCauseMessage(cause);
        }
        
        public Object intercept(Object target, Method method, Object[] params, MethodProxy methodProxy) throws Throwable {
            if (method.getDeclaringClass() == ExceptionInfo.class) {
                return method.invoke(this, params);
            } else {
                return method.invoke(cause, params);
            }
        }

        public String getErrorInformation() {
            return this.errorMessage;
        }

        public void setErrorInformation(String msg) {
            this.errorMessage = msg;
        }

        public Object[] getParams() {
            return this.params;
        }

        public String getKey() {
            return this.key;
        }

        public Throwable getThrowable() {
            return ExceptionUtils.getActualThrowable(cause);
        }
    }
}
