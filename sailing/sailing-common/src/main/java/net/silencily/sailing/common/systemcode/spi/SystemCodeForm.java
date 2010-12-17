/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.common.systemcode.spi;

import java.util.Collection;
import java.util.Collections;

import net.silencily.sailing.common.CommonServiceLocator;
import net.silencily.sailing.common.systemcode.SystemCode;
import net.silencily.sailing.framework.web.struts.BaseActionForm;

import org.apache.commons.lang.StringUtils;


/**
 * @since 2006-9-20
 * @author java2enterprise
 * @version $Id: SystemCodeForm.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 */
public class SystemCodeForm extends BaseActionForm {

	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = -8428459784160051834L;
	
	private SystemCode systemCode;
	
	private SystemCode parent;
	
	private Collection results = Collections.EMPTY_LIST;
	
	private String systemModuleName;

	private String parentCode;
	
	private String entryStep = "entry";
	
	/**
	 * @return the systemCode
	 */
	public SystemCode getSystemCode() {
		if (systemCode == null) {
			String activeId = request.getParameter("oid");
			String systemModuleName = request.getParameter("systemModuleName");
			if (StringUtils.isNotBlank(activeId)) {
				systemCode = CommonServiceLocator.getSystemCodeCRUDService().load(systemModuleName, activeId);
			} else {
				systemCode = new SystemCode();
			}
		}
		return systemCode;
	}
	
	public SystemCode getParent() {
		if (parent == null) {
			String parentCode = request.getParameter("parentCode");
			String systemModuleName = request.getParameter("systemModuleName");
			if (StringUtils.isNotBlank(parentCode)) {
				parent = CommonServiceLocator.getSystemCodeCRUDService().load(systemModuleName, parentCode);
			} else {
				parent = new SystemCode();
			}
		}
		return parent;
	}
	
	/**
	 * @param systemCode the systemCode to set
	 */
	public void setSystemCode(SystemCode systemCode) {
		this.systemCode = systemCode;
	}

	/**
	 * @return the results
	 */
	public Collection getResults() {
		return results;
	}

	/**
	 * @param results the results to set
	 */
	public void setResults(Collection results) {
		this.results = results;
	}

	/**
	 * @return the systemModuleName
	 */
	public String getSystemModuleName() {
		return systemModuleName;
	}

	/**
	 * @param systemModuleName the systemModuleName to set
	 */
	public void setSystemModuleName(String systemModuleName) {
		this.systemModuleName = systemModuleName;
	}

	/**
	 * @return the parentCode
	 */
	public String getParentCode() {
		return parentCode;
	}

	/**
	 * @param parentCode the parentCode to set
	 */
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	/**
	 * @return the entryStep
	 */
	public String getEntryStep() {
		return entryStep;
	}

	/**
	 * @param entryStep the entryStep to set
	 */
	public void setEntryStep(String entryStep) {
		this.entryStep = entryStep;
	}
	
	
	
}
