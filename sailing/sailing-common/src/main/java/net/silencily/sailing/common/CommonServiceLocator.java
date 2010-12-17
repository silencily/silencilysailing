/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.common;

import net.silencily.sailing.common.message.service.CommonMessageService;
import net.silencily.sailing.common.systemcode.SystemCodeService;
import net.silencily.sailing.common.systemcode.spi.SystemCodeCRUDService;
import net.silencily.sailing.container.ServiceProvider;



/**
 * @since 2006-9-20
 * @author java2enterprise
 * @version $Id: CommonServiceLocator.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 */
public abstract class CommonServiceLocator {
	
	public static SystemCodeService getSystemCodeService() {
		return (SystemCodeService) ServiceProvider.getService(SystemCodeCRUDService.SERVICE_NAME);
	}
	
	public static SystemCodeCRUDService getSystemCodeCRUDService() {
		return (SystemCodeCRUDService) ServiceProvider.getService(SystemCodeCRUDService.SERVICE_NAME);
	}
	
//	public static CommonOrganizationService getOrganizationService() {
//		return (CommonOrganizationService) ServiceProvider.getService(CommonOrganizationService.SERVICE_NAME);
//	}
//	
//	public static ExtensionalOrganizationService getExtensionalOrganizationService() {
//		return (ExtensionalOrganizationService) ServiceProvider.getService(ExtensionalOrganizationService.SERVICE_NAME);
//	}
//	
//	public static ExtensionalUserService getExtensionalUserService() {
//		return (ExtensionalUserService) ServiceProvider.getService(ExtensionalUserService.SERVICE_NAME);
//	}
	
	public static CommonMessageService getMessageService() {
		return (CommonMessageService) ServiceProvider.getService(CommonMessageService.SERVICE_NAME);
	}
	
//	public static CommonBookPersonService getBookService() {
//		return (CommonBookPersonService) ServiceProvider.getService(CommonBookPersonService.SERVICE_NAME);
//	}
	
}
