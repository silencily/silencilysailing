/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.common;

import java.util.List;

import net.silencily.sailing.framework.core.ContextInfo;


/**
 * 有关安全的公共类，需要加入到框架中。
 * @since 2007-4-27
 * @author pillarliu 
 * @version $Id: SecurityUtils.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 */
public class SecurityUtils {

	/**比较当前用户是否有 role 的角色*/
	public static boolean isContains(String role){
		boolean flag = false;
		List roles = ContextInfo.getCurrentUser().getRoles();		
		if(roles.contains(role)){
			flag = true;
		}
		return flag;
	}
}
