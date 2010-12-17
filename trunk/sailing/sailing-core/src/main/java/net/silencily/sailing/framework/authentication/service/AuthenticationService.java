package net.silencily.sailing.framework.authentication.service;

import net.silencily.sailing.framework.authentication.entity.Role;
import net.silencily.sailing.framework.authentication.entity.User;
import net.silencily.sailing.framework.codename.UserCodeName;
import net.silencily.sailing.framework.service.ServiceBaseWithNotAllowedNullParamters;

/**
 * <p>ϵͳ��ʹ�õ��û�, ��ɫ, �����Լ���ط���, �ڴ���������, ��Ӧ��ʹ�õ��������, ��Ϊ
 * ����ʱ��ǰ�û��Ľ�ɫ��Ϣ�Ѿ��ɰ�ȫע�뵽{@link ContextInfo}��, ����Ӧ�ÿ����õ���ѡ��
 * ����, ѡ���û��ȹ��÷���Ҳ��ϵͳͳһ�ṩ, ������������{@link User}, {@link Department}��
 * ϵͳ��Դ��<code>converter</code>��</p>
 * @author zhangli
 * @version $Id: AuthenticationService.java,v 1.1 2010/12/10 10:54:23 silencily Exp $
 * @since 2007-4-8
 */
public interface AuthenticationService extends ServiceBaseWithNotAllowedNullParamters {
    
    /* ����ӿ���ͬ���ɰ�ȫ��entityҪ�������,���Բ�������SystemConstants */
    String SERVICE_NAME = "system.authenticationService";
    
    /**
     * �����û���¼�������û���Ϣ, ���������û���Ϣ��������,��ɫ��ȫ������Ϣ
     * @param username �û���ϵͳ��ע��(��¼)�˺�
     * @return �û���Ϣ, ������<code>null</code>
     * @throws NullPointerException ���������<code>null</code>
     * @throws IllegalStateException ���ָ�����Ƶ��û�û�ҵ�, ��Ӧ����һ�������ڴ��� 
     */
    User loadUser(String username);
    
    /**
     * ���ݽ�ɫ<code>code</code>������ɫ��Ϣ
     * @param roleCode ��ɫ<code>code</code>,��Ϊ�˷�����䶨���һЩ����
     * @return ָ��<code>code</code>��ɫ, ������<code>null</code>
     * @throws NullPointerException ���������<code>null</code>
     * @throws IllegalStateException ���ָ�����ƵĽ�ɫû�ҵ�, ��Ӧ����һ�������ڴ��� 
     */
    Role loadRole(String roleCode);
    
    /**
     * ������ǰ��¼�û�, ���Ƿ���һ����ǰִ�л����µ��û�, �Ƿ�֧��<code>guest</code>��ʽ
     * ���û�ȡ���ھ���İ�ȫʵ��, ��ʹ֧��ҲҪ����һ��{@link User}��ʵ��
     * @return ��ǰ��¼�û�, ������<code>null</code>
     */
    UserCodeName getContextUser();
    
    net.silencily.sailing.framework.core.User legacy();
}
