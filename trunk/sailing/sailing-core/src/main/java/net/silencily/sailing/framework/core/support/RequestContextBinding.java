package net.silencily.sailing.framework.core.support;

import net.silencily.sailing.framework.core.RequestContext;

/**
 * ��/���{@link RequestContext}����ǰִ���߳�
 * @author zhangli
 * @version $Id: RequestContextBinding.java,v 1.1 2010/12/10 10:54:21 silencily Exp $
 * @since 2007-5-4
 */
public interface RequestContextBinding {
    
    /**
     * ������������ԭ�������ʵ�ֱַ�����Ӧ�ô������{@link RequestContext}�ͼܹ�����
     * ��/���{@link RequestContext}, Ϊ�˷���Ӧ�ô������������Ӧ�õ�����
     */
    String SERVICE_NAME = "system.requestContext";
    
    boolean hasRequestContext();
    
    void bindRequestContext(RequestContext requestContext);
    
    void unbindRequestContext();

}
