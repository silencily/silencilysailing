package net.silencily.sailing.framework.authentication.entity;

import net.silencily.sailing.framework.codename.CodeName;

/**
 * һ�������{@link CodeName}, ����һ��������, �����ǲ���,��ɫ���û�, ʵ������ӿڵ���
 * ͨ������ͳһ��չ��һ��ҵ��ʵ���������, <b>�����ڰ�ȫ����</b>. ����һ���ճ̰��ſ�����
 * һ������������, ����ճ̰��ſ����Ǹ���, ��λ, �����ǲ��ż���
 * @author zhangli
 * @version $Id: Principal.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 * @since 2007-4-19
 */
public interface Principal extends CodeName {
    
    /**
     * ����һ���û�, ���ʵ���Ƿ���������, �������ͨ�����ɼܹ��������, ͬʱ��֤��������<code>null</code>
     * @param user Ҫ�жϵ��û�
     * @return ���������������<code>true</code>
     */
    boolean pass(User user);

}
