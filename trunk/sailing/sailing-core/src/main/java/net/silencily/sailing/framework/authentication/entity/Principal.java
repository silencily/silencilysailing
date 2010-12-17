package net.silencily.sailing.framework.authentication.entity;

import net.silencily.sailing.framework.codename.CodeName;

/**
 * 一个特殊的{@link CodeName}, 表现一个责任者, 可以是部门,角色和用户, 实现这个接口的类
 * 通常用于统一地展现一个业务实体的责任者, <b>不用于安全控制</b>. 比如一个日程安排可以有
 * 一个这样的属性, 这个日程安排可以是个人, 岗位, 或者是部门级的
 * @author zhangli
 * @version $Id: Principal.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 * @since 2007-4-19
 */
public interface Principal extends CodeName {
    
    /**
     * 给定一个用户, 这个实例是否满足条件, 这个方法通常是由架构代码调用, 同时保证参数不是<code>null</code>
     * @param user 要判断的用户
     * @return 如果满足条件返回<code>true</code>
     */
    boolean pass(User user);

}
