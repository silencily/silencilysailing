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
 * Ϊ������ͼ��ҵ��ʵ�崴������ҵ��ʵ��ʵ���Ĵ���, ���ֶ�ʵ��<code>decorator</code>����
 * , Ҫ�������Լ����
 * <dl>������ͼ��ҵ��ʵ��</dl>
 * <dd>��չҵ��ʵ����</dd>
 * <dd>��һ��������, �������������չ��ҵ��ʵ���ʵ��</li>
 * <dl><code>package</code>,<code>class</code>����Լ��</dl>
 * <dd>������ͼ����չ�����������չ��ҵ��ʵ������¼���<code>dto</code>
 * ����ҵ��ʵ����
 *   <code>com.coheg.flaw.Flaw</code>
 * ������ͼ����չ��
 *   <code>com.coheg.flaw.dto.FlawDto</code>
 * </dd>, �����˵���μ�<a href="package.html">������</a>
 * @author zhangli
 * @version $Id: BeanDecorationCreator.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 * @since 2007-3-29
 */
public abstract class BeanDecorationCreator {
    
    private static final int BEAN = 0;
    private static final int VIEW_CLASS = 1;
    private static final Log logger = LogFactory.getLog(BeanDecorationCreator.class);
    
    /**
     * ��ҵ��ʵ���һ��ʵ��װ��Ϊ������ͼ������, ��ϧ��ʧ�����Ͱ�ȫ. It's time for set
     * <code>mustang</code>
     * @param viewClass ����ʵ�������
     * @param target    ʵ�ʵ�ҵ��ʵ��ʵ��
     * @return  װ�κ��������ͼ��ʵ��
     */
    public static Object decorate(Object target) {
        if (target == null) {
            throw new NullPointerException("����ҵ��ʵ����ͼ���������ҵ��ʵ����null");
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
            logger.debug("����Dto Proxyʹ���ڴ�[" + ((m1 - m2) / 1024) + "]K,��ǰ����[" + (m2 / 1024 / 1024) + "M]");
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
     * ����������ʹ��Լ��, ��������ҵ��ʵ����¼�<code>dto</code>, ������<code>Dto</code>
     * ��׺, �������ҵ��ʵ�����ͼ������, ���û���������෵��<code>null</code>
     * @return ҵ��ʵ�����ͼ������, ����һ��<code>dto</code>��ֵ��
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
            /* ֧�����������Ϊ�˼ܹ�����ͳһ������ͼ������ */
            if (logger.isDebugEnabled()) {
                logger.debug("ҵ��ʵ��[" + dto.getClass().getName()
                    + "]û����ͼ������["
                    + clazz.getName() + "]");
            }
        }
        return clazz;
    }
    
    /**
     * ������������ı�����������
     * @return ����֮ǰ��������
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
            throw new UnexpectedException("�Ӵ��������ҵ��ʵ�������", e);
        }
        return ret;
    }
}
