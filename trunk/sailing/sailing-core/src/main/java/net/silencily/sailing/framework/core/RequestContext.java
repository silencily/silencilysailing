package net.silencily.sailing.framework.core;


import net.silencily.sailing.framework.authentication.entity.Function;
import net.silencily.sailing.framework.codename.UserCodeName;
import net.silencily.sailing.framework.persistent.filter.ConditionInfo;

/**
 * <p>ϵͳ����ӿ�, ͨ��<code>AOP</code>��ʵ��, �ṩ�����ĵ�ͨ����Ϣ, ��ǰ��¼�û�, ������
 * ����, �κ�һ��������<code>spring bean factory</code>��<code>ServiceBase</code>����
 * ������<code>cast</code>����������Ի�ȡ������Ϣ</p>
 * <p>����ڷ�����ʹ������Щ��Ϣ, ��ô��<code>junit</code>ʱ�����Եķ���ʵ��Ҫ��ʵ������ӿ�
 * ͬʱ<code>mock</code>��ʹ�õ���Ϣ</p>
 * @author zhangli
 * @version $Id: RequestContext.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 * @since 2007-6-11
 */
public interface RequestContext {
    
    /**
     * ������ǰ��¼���û�
     * @return ��ǰ��¼���û�
     */
    UserCodeName getCurrentUser();
    
    /**
     * ������ǰ�����е�ִ������
     * @return ��ǰ�����е�ִ������
     */
    ConditionInfo getConditionInfo();
    
    /**
     * ������ǰ����Ĺ���
     * @return ��ǰ����Ĺ���
     */
    Function getCurrentFunction();
}
