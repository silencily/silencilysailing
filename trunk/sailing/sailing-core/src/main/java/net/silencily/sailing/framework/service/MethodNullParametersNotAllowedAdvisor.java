package net.silencily.sailing.framework.service;

import java.lang.reflect.Method;

import net.silencily.sailing.framework.utils.AssertUtils;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.beans.factory.InitializingBean;

/**
 * 不允许方法调用时参数是空值, 这个代理作用于{@link ServiceBaseWithNotAllowedNullParamters}
 * , 省去了在每个方法调用时判断参数是否为空的代码
 * @author zhangli
 * @version $Id: MethodNullParametersNotAllowedAdvisor.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 * @since 2007-4-22
 */
public class MethodNullParametersNotAllowedAdvisor extends StaticMethodMatcherPointcutAdvisor
    implements MethodInterceptor, InitializingBean {

    public boolean matches(Method method, Class targetClass) {
        return method.getParameterTypes().length > 0; 
    }

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Object[] args = methodInvocation.getArguments();
        String msg = new StringBuffer(methodInvocation.getMethod().getName())
            .append("执行时参数是空值").toString();
        for (int i = 0; i < args.length; i++) {
            AssertUtils.notNull(args[0], msg);
        }
        return methodInvocation.proceed();
    }

    public void afterPropertiesSet() throws Exception {
        setAdvice(this);
        setOrder(1);
        setClassFilter(new ClassFilter() {
            public boolean matches(Class clazz) {
                return ServiceBaseWithNotAllowedNullParamters.class.isAssignableFrom(clazz);
            }});
    }
}
