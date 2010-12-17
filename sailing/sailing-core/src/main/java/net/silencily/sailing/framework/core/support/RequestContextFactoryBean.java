package net.silencily.sailing.framework.core.support;

import java.util.HashMap;
import java.util.Map;

import net.silencily.sailing.framework.authentication.entity.Function;
import net.silencily.sailing.framework.authentication.service.AuthenticationService;
import net.silencily.sailing.framework.codename.UserCodeName;
import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.framework.core.RequestContext;
import net.silencily.sailing.framework.persistent.filter.ConditionInfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 当前登录用户请求上下文的提供类, 也就是{@link RequestContext}的工厂类, 这个类配置在
 * <code>springframework's container</code>, 以便让应用程序以<code>IoC</code>的风
 * 格获得{@link RequestContext}实例, 在业务逻辑实现层典型的使用方法是<pre>
 * public abstract class BusinessServiceImpl implements ServiceBase {
 *     
 *     public List businessMethod() {
 *         ...
 *         User user = getRequestContext().getCurrentUser();
 *         ...
 *     }
 *     
 *     protected abstract RequestContext getRequestContext();
 * } 
 * </pre>只要实现了{@link ServiceBase}基础服务接口并且有方法签名<pre>
 * protected abstract RequestContext getRequestContext();</pre>在运行时即可获得当前
 * 执行环境下保存用户, 检索条件等信息的{@link RequestContext}实例, 在<code>web</code>
 * 层环境下可以直接从<code>HttpServletRequest</code>中获得{@link RequestContext}实例,
 * <pre>
 * RequestContext ctx = (RequestContext) request.getAttribute(RequestContext.SERVICE_NAME);
 * </pre>关于为什么以这种实现方式的详细说明见架构设计文档
 * @author scott
 */
public class RequestContextFactoryBean implements FactoryBean, RequestContextBinding, DisposableBean, InitializingBean {
    
    private Log logger = LogFactory.getLog(RequestContextFactoryBean.class);
    
    private AuthenticationService authenticationService;
    
    private ThreadLocal requestContexts = new ThreadLocal();
    
    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public Object getObject() throws Exception {
        return new RequestContextImpl();
    }

    public Class getObjectType() {
        return RequestContext.class;
    }

    public boolean isSingleton() {
        return false;
    }
    
    class RequestContextImpl implements RequestContext {
        private UserCodeName user;
        
        RequestContextImpl() {
            user = authenticationService.getContextUser();
        }

        public Function getCurrentFunction() {
            throw new UnsupportedOperationException("don't ever implementation");
        }

        public UserCodeName getCurrentUser() {
            return this.user;
        }

        public ConditionInfo getConditionInfo() {
            return ContextInfo.getContextCondition();
        }
    }

    public boolean hasRequestContext() {
        Map map = (Map) this.requestContexts.get();
        return map.containsKey(getKey());
    }

    public void bindRequestContext(RequestContext requestContext) {
        Map map = (Map) this.requestContexts.get();
        map.put(getKey(), requestContext);
        if (logger.isDebugEnabled()) {
            logger.debug("在线程" + getKey() + "绑定" + requestContext);
        }
    }

    public void unbindRequestContext() {
        Map map = (Map) this.requestContexts.get();
        if (map != null && map.containsKey(getKey())) {
            Object o = map.remove(getKey());
            if (logger.isDebugEnabled()) {
                logger.debug("从线程" + getKey() + "清除" + o);
            }
        }
    }

    public void destroy() throws Exception {
        this.requestContexts.set(null);
        this.requestContexts = null;
    }

    public void afterPropertiesSet() throws Exception {
        this.requestContexts.set(new HashMap(64));
    }
    
    private String getKey() {
        return Thread.currentThread().getName();
    }
}
