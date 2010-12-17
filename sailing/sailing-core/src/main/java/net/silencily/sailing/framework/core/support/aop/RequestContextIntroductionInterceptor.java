package net.silencily.sailing.framework.core.support.aop;

import net.silencily.sailing.framework.authentication.entity.Function;
import net.silencily.sailing.framework.codename.UserCodeName;
import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.framework.core.RequestContext;
import net.silencily.sailing.framework.persistent.filter.ConditionInfo;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.DelegatingIntroductionInterceptor;

/**
 * 动态实现{@link RequestContext}的支持类
 * @author zhangli
 * @version $Id: RequestContextIntroductionInterceptor.java,v 1.1 2010/12/10 10:54:28 silencily Exp $
 * @since 2007-6-12
 */
public class RequestContextIntroductionInterceptor extends DelegatingIntroductionInterceptor implements RequestContext {

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Object ret = null;
        if (methodInvocation.getMethod().getDeclaringClass() == RequestContext.class) {
            ret = methodInvocation.getMethod().invoke(this, methodInvocation.getArguments());
        } else {
            ret = methodInvocation.proceed();
        }
        return ret;
    }

    public ConditionInfo getConditionInfo() {
        return ContextInfo.getContextCondition();
    }

    public Function getCurrentFunction() {
        throw new UnsupportedOperationException("don't be implemented");
    }

    public UserCodeName getCurrentUser() {
        return ContextInfo.getContextUser();
    }
}
