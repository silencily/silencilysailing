/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 * Project cardAssistant
 */
package net.silencily.sailing.framework.web.webwork;

import net.silencily.sailing.framework.dao.PaginationSupport;

/**
 * 翻页接口, 所有需要翻页功能的 action 必须实现此接口
 * 
 * @see com.coheg.framework.dao.PaginationSupport
 * @since 2005-9-7
 * @author 王政
 * @version $Id: Paginationable.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 */
public interface Paginationable {
	
	/**
	 * 得到翻页实体类, 用于在 /common/pager.jsp 中显示
	 * @return 翻页实体类
	 */
	PaginationSupport getPaginationSupport();
	
}
