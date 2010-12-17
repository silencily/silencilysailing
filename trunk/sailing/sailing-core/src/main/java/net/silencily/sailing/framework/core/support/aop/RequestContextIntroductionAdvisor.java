package net.silencily.sailing.framework.core.support.aop;

import java.util.HashSet;
import java.util.Set;

import net.silencily.sailing.framework.authentication.service.AuthenticationService;
import net.silencily.sailing.framework.core.RequestContext;

import org.springframework.aop.support.DefaultIntroductionAdvisor;
import org.springframework.beans.factory.InitializingBean;

/**
 * 实现动态导入{@link RequestContext}的支持类
 * @author zhangli
 * @version $Id: RequestContextIntroductionAdvisor.java,v 1.1 2010/12/10 10:54:28 silencily Exp $
 * @since 2007-6-12
 */
public class RequestContextIntroductionAdvisor extends DefaultIntroductionAdvisor implements InitializingBean {
    
    /**
     * 创建导入时不应该<code>crosscut</code>的接口或类
     */
    private Set eliminatedClasses;
    
    public void setEliminatedClasses(Set classes) {
        this.eliminatedClasses = classes;
    }

    public RequestContextIntroductionAdvisor() {
        super(new RequestContextIntroductionInterceptor(), RequestContext.class);
    }

    public boolean matches(Class clazz) {
        return super.matches(clazz) && !this.eliminatedClasses.contains(clazz);
    }

    public void afterPropertiesSet() throws Exception {
        this.eliminatedClasses = new HashSet(1);
        this.eliminatedClasses.add(AuthenticationService.class);
    }
}
