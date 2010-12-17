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
 * <p>�쳣����ĳ���ʵ��, ����ҵ���쳣ʱ��̬��չ��ʵ��<code>ExceptionInfo</code>�ṩ�쳣��Ϣ,����
 * ��Ҫ�ṩ����������Ϣ�ķ���{@link #getMessage(String, ExceptionInfo) getMessage}����ʹ
 * ��ָ����<code>key</code>������Ϣ</p>
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
        /* һ����������ڴ����쳣ʱָ���� key, �μ� BaseSystemException */
        if (StringUtils.isNotBlank(ret.getKey())) {
            ret.setErrorInformation(getMessage(ret.getKey(), ret));
            return ret;
        }

        String classAndMethodName = clazz.getName() + "." + method;
//        String throwableName = ClassUtils.getShortClassName(ret.getThrowable().getClass());
        /* Java2Enterprise �����쳣Ҳʹ��FQN, ����ͬ���쳣��ͻ */
        String throwableName = ret.getThrowable().getClass().getName();
        /* ����FQN.methodName.�쳣���� ���� key */
        String msg = getMessage(classAndMethodName + "." + throwableName, (ExceptionInfo) ex);
        
        /* ����FQN.methodName ���� key */
        if (StringUtils.isBlank(msg)) {
            msg = getMessage(classAndMethodName, (ExceptionInfo) ex);
        }
        
        /* �����쳣�������� key */
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
     * ʹ��ָ����<code>key</code>����Ԥ���������ļ��еĴ�����Ϣ
     * @param key       ���õ�<code>key</code>
     * @param info      �쳣��Ϣ
     * @return ���ʹ��<code>key</code>�ҵ���Ԥ�����õ���Ϣ�ͷ����������������Ϣ, ���򷵻�<code>null</code>
     */
    abstract protected String getMessage(String key, ExceptionInfo info);

    /**
     * ��鲢�������, �������쳣û��ʵ��<code>ExceptionInfo</code>, �ʹ���һ��ʵ����
     * <code>ExceptionInfo</code>�Ĵ���. ����쳣������Ƕ����<code>ExceptionInfo</code>
     * ���쳣, ����������ڴ��������Ҫ���뵽������͵��쳣Ϊֹ
     * @param ex Ҫ������쳣
     * @return ��������쳣, ������һ��������
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
            logger.debug("����ExceptionInfo֮ǰ���쳣," + ex.getMessage(), ex);
        }

        try {
            ex.getClass().newInstance();
        } catch (Exception e) {
            /* �������ʵ��������쳣������InvocationTargetException */
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
     * ���ڴ��������쳣������̱�����쳣: ʹ��<code>null</code>����ص��������ķ���
     */
    private static class NullParameterException extends BaseSystemException {
        private String msg = "BaseExceptionHandler's handle()�����쳣ʱ���������null";

        public String getErrorInformation() {
            return msg;
        }        
    };
    
    /**
     * ������ϵͳ�쳣ʱͨ����������������쳣ʵ��<code>ExceptionInfo</code>, �����쳣������
     * ͳһ����
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
