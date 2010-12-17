/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.common.systemcode.spi;

import java.util.List;

import net.silencily.sailing.common.CommonConstants;
import net.silencily.sailing.common.systemcode.SystemCode;
import net.silencily.sailing.common.systemcode.SystemCodeService;


/**
 * @since 2006-9-20
 * @author java2enterprise
 * @version $Id: SystemCodeCRUDService.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 */
public interface SystemCodeCRUDService extends SystemCodeService {
	
	String SERVICE_NAME = CommonConstants.MODULE_NAME + ".systemCodeCRUDService";
	
	void save(String systemModuleName, SystemCode systemCode);
	
	void update(String systemModuleName, SystemCode systemCode);
	
	void delete(String systemModuleName, String code);
	
	/**
	 * 注意此方法与 {@link SystemCodeService#findByParentCode(String, String)} 的不同是
	 * 此方法需要考虑查询与分页
	 * @see SystemCodeService#findByParentCode(String, String)
	 */
	List findByParentCodeWithContextInfo(String systemModuleName, String parentCode);
	
}
