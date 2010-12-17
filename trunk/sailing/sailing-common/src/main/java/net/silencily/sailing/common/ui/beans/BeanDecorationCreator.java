package net.silencily.sailing.common.ui.beans;

import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;
import net.sf.cglib.transform.impl.UndeclaredThrowableStrategy;
import net.silencily.sailing.exception.UnexpectedException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ClassUtils;


/**
 * 为用于视图的业务实体创建基于业务实体实例的代理, 简化手动实现<code>decorator</code>繁琐
 * , 要求满足的约定是
 * <dl>用于视图的业务实体</dl>
 * <dd>扩展业务实体类</dd>
 * <dd>有一个构造器, 其参数接受所扩展的业务实体的实例</li>
 * <dl><code>package</code>,<code>class</code>命名约定</dl>
 * <dd>用于视图的扩展类包名在其扩展的业务实体包名下加上<code>dto</code>
 * 例如业务实体类
 *   <code>com.coheg.flaw.Flaw</code>
 * 用于视图的扩展类
 *   <code>com.coheg.flaw.dto.FlawDto</code>
 * </dd>, 更多的说明参见<a href="package.html">包描述</a>
 * @author zhangli
 * @version $Id: BeanDecorationCreator.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 * @since 2007-3-29
 */
public abstract class BeanDecorationCreator {
    
    private static final int BEAN = 0;
    private static final int VIEW_CLASS = 1;
    private static final Log logger = LogFactory.getLog(BeanDecorationCreator.class);
    
    /**
     * 把业务实体的一个实例装饰为用于视图的类型, 可惜损失了类型安全. It's time for set
     * <code>mustang</code>
     * @param viewClass 用于实体的类型
     * @param target    实际的业务实体实例
     * @return  装饰后的用于视图的实例
     */
    public static Object decorate(Object target) {
        if (target == null) {
            throw new NullPointerException("生成业务实体视图帮助类参数业务实体是null");
        }
        Class viewClass = dtoClass(target);
        if (viewClass == null) {
            /* don't proxy without view class */
            return target;
        }
        Enhancer enhancer = new Enhancer();
        enhancer.setCallbackFilter(new MethodDelegationCallbackFilter(viewClass));
        enhancer.setCallbacks(new Callback[] {new BeanDelegationCallback(target), NoOp.INSTANCE});
        enhancer.setSuperclass(viewClass);
        enhancer.setStrategy(new UndeclaredThrowableStrategy(UndeclaredThrowableException.class));        
        
        /* debug code */
        Runtime.getRuntime().gc();
        long m1 = Runtime.getRuntime().freeMemory();
        Object obj = enhancer.create(new Class[] {className(target)}, new Object[] {target});
        long m2 = Runtime.getRuntime().freeMemory();
        if (logger.isDebugEnabled()) {
            logger.debug("创建Dto Proxy使用内存[" + ((m1 - m2) / 1024) + "]K,当前空闲[" + (m2 / 1024 / 1024) + "M]");
        }
        return obj;
    }

    private static class MethodDelegationCallbackFilter implements CallbackFilter {
        private Class viewClass;
        
        public MethodDelegationCallbackFilter(Class viewClass) {
            this.viewClass = viewClass;
        }

        public int accept(Method method) {
            Class clazz = method.getDeclaringClass();
            if (clazz == viewClass) {
                return VIEW_CLASS;
            }
            return BEAN;
        }
    }
    
    private static class BeanDelegationCallback implements MethodInterceptor {
        private Object bean;
        
        public BeanDelegationCallback(Object bean) {
            this.bean = bean;
        }
        
        public Object intercept(Object target, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            return method.invoke(bean, args);
        }
    }
    
    private static final String PACKAGE_SUFFIX = ".dto.";
    private static final String CLASS_SUFFIX = "Dto";
    
    /**
     * 按照这个类的使用约定, 即包名在业务实体包下加<code>dto</code>, 类名加<code>Dto</code>
     * 后缀, 检索这个业务实体的视图帮助类, 如果没有这样的类返回<code>null</code>
     * @return 业务实体的视图帮助类, 就是一个<code>dto</code>传值类
     */
    private static Class dtoClass(Object dto) {
        String className = ClassUtils.getShortName(dto.getClass());
        String dtoClassName = new StringBuffer(dto.getClass().getPackage().getName())
            .append(PACKAGE_SUFFIX)
            .append(className)
            .append(CLASS_SUFFIX)
            .toString();
        Class clazz = null;
        try {
            clazz = Class.forName(dtoClassName);
        } catch (ClassNotFoundException e) {
            /* 支持这种情况是为了架构可能统一处理视图帮助类 */
            if (logger.isDebugEnabled()) {
                logger.debug("业务实体[" + dto.getClass().getName()
                    + "]没有视图帮助类["
                    + clazz.getName() + "]");
            }
        }
        return clazz;
    }
    
    /**
     * 解析被代理过的本来的类名称
     * @return 代理之前的类名称
     */
    private static Class className(Object bean) {
        Class clazz = bean.getClass();
        String className = ClassUtils.getShortName(clazz);
        String beanClassName = new StringBuffer(clazz.getPackage().getName())
            .append(".")
            .append(className)
            .toString();
        Class ret = null;
        try {
            ret = ClassUtils.forName(beanClassName);
        } catch (ClassNotFoundException e) {
            throw new UnexpectedException("从代理类解析业务实体类错误", e);
        }
        return ret;
    }
}
