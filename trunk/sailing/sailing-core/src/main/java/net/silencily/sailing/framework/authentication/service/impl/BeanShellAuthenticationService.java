package net.silencily.sailing.framework.authentication.service.impl;

import net.silencily.sailing.framework.authentication.entity.Role;
import net.silencily.sailing.framework.authentication.entity.User;
import net.silencily.sailing.framework.authentication.service.AuthenticationService;
import net.silencily.sailing.framework.codename.UserCodeName;

/**
 * ����һ������<code>BeanShell Script</code>��ʵ��, Ŀ���Ǵӱ��뼶��ȥ���԰�ȫʵ�ֵ���
 * ����, �����������ؽ�ź, ������ʵ���ǰѼܹ������<code>API</code>�������, �;��尲ȫ
 * ��ʵ�ֵ�����һ��<code>glue jar</code>, ͨ��<code>sun jar services specification</code>
 * ����������<code>springframework</code>����ʵ��ͬ����Ŀ��
 * @author zhangli
 * @version $Id: BeanShellAuthenticationService.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 * @since 2007-4-8
 */
public class BeanShellAuthenticationService extends BeanShellIntegrationService implements AuthenticationService {
    
    protected AuthenticationService getImplementation() {
        return (AuthenticationService) service;
    }

    public Role loadRole(String roleCode) {
        return getImplementation().loadRole(roleCode);
    }

    public User loadUser(String username) {
        return getImplementation().loadUser(username);
    }

    protected Class getServiceClass() {
        return AuthenticationService.class;
    }

    public UserCodeName getContextUser() {
        return getImplementation().getContextUser();
    }

    public net.silencily.sailing.framework.core.User legacy() {
        return getImplementation().legacy();
    }
}
