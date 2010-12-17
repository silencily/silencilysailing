/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.common.persistent.hibernate3.spi;

import net.silencily.sailing.common.CommonConstants;
import net.silencily.sailing.common.CommonServiceLocator;
import net.silencily.sailing.common.systemcode.SystemCode;
import net.silencily.sailing.common.systemcode.SystemCodeService;
import net.silencily.sailing.framework.persistent.hibernate3.entity.CodeWrapper;
import net.silencily.sailing.framework.persistent.hibernate3.usertype.CodeWrapperType.CodeWrapperCallbackHanler;


/**
 * This is a spi class
 * @since 2006-9-20
 * @author java2enterprise
 * @version $Id: ReformCodeWrapperCallbackHandler.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 */
public class ReformCodeWrapperCallbackHandler implements CodeWrapperCallbackHanler {

	public static final String SERVICE_NAME = CommonConstants.MODULE_NAME + ".persistent.reformCodeWrapperCallbackHandler";
	
	public void afterNullSafeGet(CodeWrapper codeWrapperLoaded)
		throws Throwable {
		
		SystemCodeService systemCodeService = CommonServiceLocator.getSystemCodeService();
		String[] moduleNames = systemCodeService.getRegisteredSystemModuleNames();
		
		for (int i = 0; i < moduleNames.length; i++) {
			SystemCode systemCode = systemCodeService.load(moduleNames[i], codeWrapperLoaded.getCode());
			if (systemCode == null) {
				continue;
			}
			codeWrapperLoaded.setName(systemCode.getName());
			codeWrapperLoaded.setDescription(systemCode.getDescription());
			break;
		}		
	}

}
