package net.silencily.sailing.exception;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 用于把一个<code>exception</code>适配为异常处理的<code>ExceptionInfo</code>
 * 
 * @author scott
 * @since 2006-3-24
 * @version $Id: ExceptionInfoAdapter.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 *
 */
public abstract class ExceptionInfoAdapter {
    /** 这是使用<code>log</code>的几个基础类之一 */
    private static final Log logger = LogFactory.getLog(ExceptionInfoAdapter.class);
    
    private final static Class[] interfaces = new Class[] {ExceptionInfo.class};
    
    public static Exception adapter(Exception ex) {
        Enhancer enhancer = new Enhancer();        
        enhancer.setSuperclass(ex.getClass());
        enhancer.setInterfaces(interfaces);
        enhancer.setUseFactory(false);
        enhancer.setCallback(new ExceptionInfoCallback(ex));

        return create(enhancer, ex);
    }
    
    /**
     * 按照三种类型创建代理, 缺省, 一个参数(String/Exception), 两个参数(String, Exception),
     * 仅找出第一个构造器就执行创建过程
     */
    private static Exception create(Enhancer enhancer, Exception ex) {
        Constructor[] conses = ex.getClass().getDeclaredConstructors();
        Object ret = null;

        for (int i = 0; ret == null && i < conses.length; i++) {
            if (conses[i].getModifiers() == Modifier.PUBLIC) {
                Class[] types = conses[i].getParameterTypes();
                
                try {
                    if (types.length == 0) {
                        ret = enhancer.create();
                    } else if (types.length == 1 && types[0] == String.class) {
                        ret = enhancer.create(types, new String[] {ex.getMessage()});
                    } else if (types.length == 1) {
                        ret = enhancer.create(types, new Object[] {ex.getCause()});
                    } else if (types.length == 2) {
                        ret = enhancer.create(types, new Object[] {ex.getMessage(), ex.getCause()});
                    }
                } catch (Throwable e) {
                    ret = null;
                    if (logger.isDebugEnabled()) {
                        logger.debug("创建代理时构造器参数类型错误", e);
                    }
                }
            }
        }

        if (ret == null) {
            ret = ex;
            if (logger.isWarnEnabled()) {
                logger.warn("不能为这个类创建代理:" + ex);
            }
        }
        
        return (Exception) ret;
    }
    
    private static class ExceptionInfoCallback implements MethodInterceptor {
        private static final String getter = "getErrorInformation";
        
        private static final String setter = "setErrorInformation";
        
        private static final String getParams = "getParams";
        
        private String errorInformation;
        
        private Exception ex;
        
        public ExceptionInfoCallback(Exception ex) {
            this.ex = ex;
        }
        
        public Object intercept(Object orig, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            String name = method.getName();
            Object ret = null;
            if (getter.equals(name)) {
                ret = errorInformation;
            } else if (setter.equals(name)) {
                errorInformation = (String) args[0];
            } else if (getParams.equals(name)) {
                ret = ExceptionInfo.NULL_PARAM;
            } else {
                ret = method.invoke(ex, args);
            }

            return ret;
        }        
    }
}
