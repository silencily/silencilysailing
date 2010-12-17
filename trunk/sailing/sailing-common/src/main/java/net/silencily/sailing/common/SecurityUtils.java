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
 * �йذ�ȫ�Ĺ����࣬��Ҫ���뵽����С�
 * @since 2007-4-27
 * @author pillarliu 
 * @version $Id: SecurityUtils.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 */
public class SecurityUtils {

	/**�Ƚϵ�ǰ�û��Ƿ��� role �Ľ�ɫ*/
	public static boolean isContains(String role){
		boolean flag = false;
		List roles = ContextInfo.getCurrentUser().getRoles();		
		if(roles.contains(role)){
			flag = true;
		}
		return flag;
	}
}
