package net.silencily.sailing.framework.core.support;

import net.silencily.sailing.framework.core.RequestContext;

/**
 * 绑定/解除{@link RequestContext}到当前执行线程
 * @author zhangli
 * @version $Id: RequestContextBinding.java,v 1.1 2010/12/10 10:54:21 silencily Exp $
 * @since 2007-5-4
 */
public interface RequestContextBinding {
    
    /**
     * 这个命名特殊的原因是这个实现分别用于应用代码检索{@link RequestContext}和架构代码
     * 绑定/解除{@link RequestContext}, 为了方便应用代码采用了倾向应用的名称
     */
    String SERVICE_NAME = "system.requestContext";
    
    boolean hasRequestContext();
    
    void bindRequestContext(RequestContext requestContext);
    
    void unbindRequestContext();

}
