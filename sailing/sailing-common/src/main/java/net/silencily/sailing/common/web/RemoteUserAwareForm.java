/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project flaw
 */
package net.silencily.sailing.common.web;

import net.silencily.sailing.framework.web.struts.BaseActionForm;


/**
 * 得到当前登录用户的 Form, 便于从 UI 上获取
 * @deprecated 方法已经 pull up 到 {@link BaseActionForm} 中, 直接使用 BaseActionForm 即可
 * @since 2006-6-19
 * @author 王政
 * @version $Id: RemoteUserAwareForm.java,v 1.1 2010/12/10 10:54:20 silencily Exp $
 */
public class RemoteUserAwareForm extends BaseActionForm {

	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = 3518192737397735556L;
	
	
}
