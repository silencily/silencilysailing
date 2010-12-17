/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 * Project cardAssistant
 */
package net.silencily.sailing.framework.web.webwork;

import net.silencily.sailing.framework.dao.PaginationSupport;

/**
 * ��ҳ�ӿ�, ������Ҫ��ҳ���ܵ� action ����ʵ�ִ˽ӿ�
 * 
 * @see com.coheg.framework.dao.PaginationSupport
 * @since 2005-9-7
 * @author ����
 * @version $Id: Paginationable.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 */
public interface Paginationable {
	
	/**
	 * �õ���ҳʵ����, ������ /common/pager.jsp ����ʾ
	 * @return ��ҳʵ����
	 */
	PaginationSupport getPaginationSupport();
	
}
