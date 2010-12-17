package net.silencily.sailing.framework.core;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.silencily.sailing.framework.codename.UserCodeName;
import net.silencily.sailing.framework.persistent.filter.ConditionInfo;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 系统上下文信息, 包含当前执行线程的信息, 比如当前用户, 安全认证, 检索条件等信息. 这个信息
 * 将跟随用户的一次完整的请求过程, 比如从<code>web</code>页面请求到响应结束
 * 
 * @author scott
 * @author java2enterprise
 * @since 2006-2-26
 * @version $Id: ContextInfo.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 *
 */
public class ContextInfo implements Serializable {
    
    private static Log logger = LogFactory.getLog(ContextInfo.class);
	
	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = -3966764145229699433L;

    /**
     * 保存当前执行上下文中的检索,更新数据的条件
     */
    private static ThreadLocal contextCondition = new ThreadLocal();
    /**
     * 当前用户
     * 旧系统中的当前用户。不推荐使用
     */
    private static ThreadLocal currentUser = new ThreadLocal();
    /**
     * 用户上下文
     * 更新数据库时自动更新创建者等信息时以供基盘程序使用。不推荐在其它地方使用。
     */
    private static ThreadLocal contextUser = new ThreadLocal();
    /**
     * 数据库别名
     * 在Viewbean自动排序处理中，将已生成的数据库别名存放到该属性中，在之后的自动生成查询条件处理中不再生成已生成过的别名
     */
    private static ThreadLocal aliasSet = new ThreadLocal();

    /**
     * 从当前执行环境中检索保存条件的
     * @return
     */
    public static ConditionInfo getContextCondition() {
        ConditionInfo info = (ConditionInfo) contextCondition.get();
        if (info == null) {
            info = new ConditionInfo();
            contextCondition.set(info);
        }

        return info;
    }

    public static void setContextCondition(ConditionInfo contextCondition) {
        ContextInfo.contextCondition.set(contextCondition);
    }

    /**
     * 从当前执行环境中检索已经建立的表关联
     * @return
     */
    public static Set getAliasSet() {
        Set info = (Set) aliasSet.get();
        if (info == null) {
            info = new HashSet();
            aliasSet.set(info);
        }

        return info;
    }

    public static void setAliasSet(Set set) {
        ContextInfo.aliasSet.set(set);
    }

    /**
     * 屏蔽掉当前的条件, 避免在<code>AOP</code>组件中使用. 所有需要修改条件的组件都要查询
     * 这个标志, 以决定是否应用这些条件. 安全中的条件应用不适用于这个规则. 如果当前执行线程
     * 没有条件信息, 什么也不做
     */
    public static void concealQuery() {
        ConditionInfo info = getContextCondition();
        info.setConcealQuery(true);
    }
    
    /**
     * 如果当前执行线程屏蔽了条件信息, 调用这个方法再次恢复条件信息. 当执行<code>DAO</code>
     * 查询时仍要应用这个条件. 如果当前执行线程没有条件信息就什么也不做
     */
    public static void recoverQuery() {
        ConditionInfo info = getContextCondition();
        info.setConcealQuery(false);
    }

    /**
     * 查询当前执行线程是否屏蔽掉了应用于<code>DAO</code>操作的条件, 所有的针对查询操作的
     * <code>AOP</code>组件在应用方法前都要查询这个标志, 以明确是否应用条件.如果当前执行线
     * 程没有条件信息, 什么也不做
     * @return 是否屏蔽掉了查询条件, 如果返回<code>true</code>, 就不要应用这些条件
     */
    public static boolean isConcealQuery() {
        return getContextCondition().isConcealQuery();
    }
    
    public static net.silencily.sailing.framework.codename.UserCodeName getContextUser() {
    	SecurityContext securityContext = SecurityContextHolder.getContext();
    	
		Authentication authentication = securityContext.getAuthentication();
		if(authentication != null){
			Object principal = authentication.getPrincipal();
			UserDetails ud = (UserDetails)principal;
			UserCodeName cn = new UserCodeName();
            cn.setName(ud.getUsername());
            cn.setUsername(ud.getUsername());
            contextUser.set(cn);
		}
        return (UserCodeName) ContextInfo.contextUser.get();
    }
    
    public static void clear() {
        currentUser.set(null);
        contextUser.set(null);
        contextCondition.set(null);
    }

    public static User getCurrentUser() {
        if (currentUser.get() == null) {
//            AuthenticationService service = (AuthenticationService) ServiceProvider.getService(AuthenticationService.SERVICE_NAME);
//            User user = null;
//            try {
//                user = service.legacy();
//                if (logger.isInfoEnabled()) {
//                    logger.info("检索当前登录用户:" + user.getUsername());
//                }
//            } catch (Exception e) {
//                /* 仅仅处理用户没有登录时的情况, 适用于网站请求 */
//                user = User.EMPTY_USER;
//                if (logger.isInfoEnabled()) {
//                    logger.info("检索当前登录用户,AuthenticationService.legacy()方法", e);
//                }
//            }
//            return user;
            
            //User user = service.legacy();
        	SecurityContext securityContext = SecurityContextHolder.getContext();
    		Authentication authentication = securityContext.getAuthentication();
    		Object principal = authentication.getPrincipal();
    		UserDetails ud = (UserDetails)principal;
    		
            List list = new LinkedList();
            User user = new User("admin","Test","Admin","Administrator",list);
            list.add("flaw.equipment.dqzg");
            list.add("flaw.watch");
            list.add("flaw.launch.stop.mainMachine.safety");
            list.add("admin");
            list.add("hr.department");
            list.add("hr.superior");
            list.add("material.manager");
            
            user.setChineseName("");
            user.setUsername(ud.getUsername());
            
            currentUser.set(user);
            if (logger.isDebugEnabled()) {
                logger.debug("ContextInfo.getCurrentUser,线程" + Thread.currentThread().getName() + ",从Authentication返回:" + user.getUsername() + "," + user.getChineseName());
            }
        }
        return (User) currentUser.get();
    }
}
