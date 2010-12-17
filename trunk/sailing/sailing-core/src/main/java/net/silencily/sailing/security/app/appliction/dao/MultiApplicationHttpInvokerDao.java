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
 * �˽ӿ�ֻ��Ϊ�˸� {@link MultiApplicationHttpInvokerProxyFactoryBean} �ṩ����Ӧ�õ���Ϣ, ��ʹ�� {@link ApplicationDao}
 * ��ԭ�������� 3 �� bean ������ѭ������ <p/>
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
