/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.common.systemcode.spi;

import java.util.List;

import net.silencily.sailing.common.systemcode.SystemCodeService;
import net.silencily.sailing.framework.web.view.ComboSupportList;


/**
 * @since 2006-9-20
 * @author java2enterprise
 * @version $Id: AbstractSystemCodeService.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 */
public abstract class AbstractSystemCodeService implements SystemCodeService {

	/**
	 * @see com.coheg.common.systemcode.SystemCodeService#getCodeList(java.lang.String, java.lang.String)
	 */
	public ComboSupportList getCodeList(String systemModuleName, String parentCode) {	
		List list = findByParentCode(systemModuleName, parentCode);
		ComboSupportList comboSupportList = new ComboSupportList("code", "name");
		comboSupportList.addAll(list);
		return comboSupportList;
	}

}
