/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.security.acegi.ui;

import javax.servlet.http.HttpServletRequest;

import net.sf.navigator.menu.MenuRepository;

import org.acegisecurity.Authentication;
import org.dom4j.Document;

/**
 * @since 2006-1-19
 * @author ÍõÕþ
 * @version $Id: UserMenuAware.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 */
public interface UserMenuAware {
	
	MenuRepository buildStrutsMenu(HttpServletRequest request, Authentication authResult);
	
	Document buildXmlMenu(HttpServletRequest request, Authentication authResult);
}
