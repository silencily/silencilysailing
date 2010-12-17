package net.silencily.sailing.framework.authentication.service.impl;

import net.silencily.sailing.framework.authentication.entity.Role;
import net.silencily.sailing.framework.authentication.entity.User;
import net.silencily.sailing.framework.authentication.service.AuthenticationService;
import net.silencily.sailing.framework.codename.UserCodeName;

/**
 * 这是一个基于<code>BeanShell Script</code>的实现, 目的是从编译级别去除对安全实现的依
 * 赖性, 以做到真正地解藕, 将来的实现是把架构所需的<code>API</code>单独打包, 和具体安全
 * 的实现单独作一个<code>glue jar</code>, 通过<code>sun jar services specification</code>
 * 或者依赖于<code>springframework</code>容器实现同样的目的
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
