/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.transfer.meta;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * @since 2005-9-26
 * @author ÍõÕþ
 * @version $Id: DefaultImportMetaData.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 */
public class DefaultImportMetaData extends DefaultTransferMetaData implements ImportMetaData, InitializingBean {
	
	private Map entityMap = new LinkedHashMap();
	
	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		Assert.isTrue(! getEntityMap().isEmpty(), " entityMap must be specified ");
	}

	/**
	 * @return Returns the entityProperties.
	 */
	public Map getEntityMap() {
		return entityMap;
	}

	/**
	 * @param entityMap The entityMap to set.
	 */
	public void setEntityMap(Map entityMap) {
		this.entityMap = entityMap;
	}

	



}
