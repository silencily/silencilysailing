/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.common.systemcode;

import java.util.List;

import net.silencily.sailing.framework.core.ServiceBase;
import net.silencily.sailing.framework.web.view.ComboSupportList;


/**
 * 系统代码向业务子系统提供的 Service 
 * @since 2006-9-19
 * @author java2enterprise
 * @version $Id: SystemCodeService.java,v 1.1 2010/12/10 10:54:20 silencily Exp $
 */
public interface SystemCodeService extends ServiceBase {
	
	/**
	 * 根据 子系统模块名, 代码 parent code 得到下级代码集合, 如果没有下级代码, 返回空 list
	 * @param systemModuleName 子系统模块名
	 * @param parentCode parent code
	 * @return list fill with {@link SystemCode}
	 */
	List findByParentCode(String systemModuleName, String parentCode);
	
	/**
	 * 根据 子系统模块名, 代码 parent code 得到 {@link ComboSupportList}, 用于在视图上显示
	 * @param systemModuleName 子系统模块名
	 * @param parentCode parent code
	 * @return {@link ComboSupportList}
	 */
	ComboSupportList getCodeList(String systemModuleName, String parentCode);
	
	/**
	 * 根据 子系统模块名, 代码 code 得到系统代码, 如果查找不到, return null, 
	 * 返回 null 而不抛异常的原因是避免 spring 回滚事务
	 * @param systemModuleName 子系统模块名
	 * @param code 代码 code
	 * @return system code, 如果查找不到, return null
	 */
	SystemCode load(String systemModuleName, String code);

	/**
	 * 得到所有已经注册的子系统模块名, 如果还没有子系统注册, 返回 new String[0]
	 * @return registered system module names
	 */
	String[] getRegisteredSystemModuleNames();
	
}
