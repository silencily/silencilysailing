/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.security.app.appliction.dao;

import java.util.List;

import net.silencily.sailing.security.entity.Application;


/**
 * 此接口只是为了给 {@link MultiApplicationHttpInvokerProxyFactoryBean} 提供所有应用的信息, 不使用 {@link ApplicationDao}
 * 的原因是以下 3 个 bean 出现了循环引用 <p/>
 * {@link MultiApplicationHttpInvokerProxyFactoryBean}
 * {@link RemoteCachesProviderSerivceImpl}
 * {@link ApplicationDao}
 * <p/>
 * @since 2006-7-31
 * @author java2enterprise
 * @version $Id: MultiApplicationHttpInvokerDao.java,v 1.1 2010/12/10 10:54:28 silencily Exp $
 */
public interface MultiApplicationHttpInvokerDao {
	
	Class ENTITY_TYPE = Application.class;
	
	List findAll();
	
}
