package net.silencily.sailing.framework.authentication.service;

import net.silencily.sailing.framework.authentication.entity.Role;
import net.silencily.sailing.framework.authentication.entity.User;
import net.silencily.sailing.framework.codename.UserCodeName;
import net.silencily.sailing.framework.service.ServiceBaseWithNotAllowedNullParamters;

/**
 * <p>系统中使用的用户, 角色, 部门以及相关服务, 在大多数情况下, 不应该使用到这个服务, 因为
 * 运行时当前用户的角色信息已经由安全注入到{@link ContextInfo}中, 而且应用可能用到的选择
 * 部门, 选择用户等公用服务也由系统统一提供, 这个服务最常用在{@link User}, {@link Department}等
 * 系统资源的<code>converter</code>中</p>
 * @author zhangli
 * @version $Id: AuthenticationService.java,v 1.1 2010/12/10 10:54:23 silencily Exp $
 * @since 2007-4-8
 */
public interface AuthenticationService extends ServiceBaseWithNotAllowedNullParamters {
    
    /* 这个接口连同集成安全的entity要单独打包,所以不再引用SystemConstants */
    String SERVICE_NAME = "system.authenticationService";
    
    /**
     * 根据用户登录名检索用户信息, 检索到的用户信息包含部门,角色等全部的信息
     * @param username 用户在系统的注册(登录)账号
     * @return 用户信息, 不返回<code>null</code>
     * @throws NullPointerException 如果参数是<code>null</code>
     * @throws IllegalStateException 如果指定名称的用户没找到, 这应该是一个开发期错误 
     */
    User loadUser(String username);
    
    /**
     * 根据角色<code>code</code>检索角色信息
     * @param roleCode 角色<code>code</code>,是为了方便记忆定义的一些代码
     * @return 指定<code>code</code>角色, 不返回<code>null</code>
     * @throws NullPointerException 如果参数是<code>null</code>
     * @throws IllegalStateException 如果指定名称的角色没找到, 这应该是一个开发期错误 
     */
    Role loadRole(String roleCode);
    
    /**
     * 检索当前登录用户, 总是返回一个当前执行环境下的用户, 是否支持<code>guest</code>形式
     * 的用户取决于具体的安全实现, 即使支持也要返回一个{@link User}的实例
     * @return 当前登录用户, 不返回<code>null</code>
     */
    UserCodeName getContextUser();
    
    net.silencily.sailing.framework.core.User legacy();
}
