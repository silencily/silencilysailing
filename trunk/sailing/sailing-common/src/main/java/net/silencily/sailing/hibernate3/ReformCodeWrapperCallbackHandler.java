/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.hibernate3;

import java.util.Map;

import net.silencily.sailing.common.dict.service.BasicCodeService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.framework.persistent.hibernate3.entity.CodeWrapper;
import net.silencily.sailing.hibernate3.CodeWrapperType.CodeWrapperCallbackHanler;
import net.silencily.sailing.security.SecurityContextInfo;




/**
 * This is a spi class
 * @since 2006-9-20
 * @author java2enterprise
 * @version $Id: ReformCodeWrapperCallbackHandler.java,v 1.1 2010/12/10 10:54:14 silencily Exp $
 */
public class ReformCodeWrapperCallbackHandler implements CodeWrapperCallbackHanler  {

	
	
	public ReformCodeWrapperCallbackHandler() {
		super();
		// TODO Auto-generated constructor stub
	}
	public void afterNullSafeGet(CodeWrapper codeWrapperLoaded)
		throws Throwable {
			
		//codeWrapperLoaded.setName(service().getNameByCode(codeWrapperLoaded.getCode()));
		Map<String, String> map = (Map<String, String>)SecurityContextInfo.getSession().getAttribute("initBaseCode");
		if (map != null) {
		    codeWrapperLoaded.setName(map.get(codeWrapperLoaded.getCode()));
		} else {
		    codeWrapperLoaded.setName(service().getNameByCode(codeWrapperLoaded.getCode()));  
		}
		
	}
	static BasicCodeService service() {
		return (BasicCodeService) ServiceProvider.getService(BasicCodeService.SERVICE_NAME);
	}
}
