/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.common.web;

import javax.servlet.ServletException;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

/**
 * 注册公用组件的 plug in
 * @since 2006-7-27
 * @author java2enterprise
 * @version $Id: InitializingCommonComponentsPlugIn.java,v 1.1 2010/12/10 10:54:20 silencily Exp $
 */
public class InitializingCommonComponentsPlugIn implements PlugIn {
	
	
	public void init(ActionServlet servlet, ModuleConfig config)
		throws ServletException {

	}

	
	public void destroy() {
	}


}
