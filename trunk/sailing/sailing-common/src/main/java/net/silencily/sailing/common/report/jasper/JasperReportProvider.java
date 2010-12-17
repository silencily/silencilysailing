/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.common.report.jasper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述一个 jasperreport, 提供基本特征和数据来源 
 * @since 2006-9-27
 * @author java2enterprise
 * @version $Id: JasperReportProvider.java,v 1.1 2010/12/10 10:54:17 silencily Exp $
 */
public class JasperReportProvider implements Serializable {
	
	//	~ Static fields/initializers =============================================	
	
	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = -1547794842868171419L;

	//	~ Instance fields ========================================================
	
	/** jasper 文件的类路径 */
	private String jasperInClassPath;
	
	/** 
	 * detail band 可以容纳的数据条数, 画完模板后根据数据填充实际情况得来,
	 * 用于填充 null 元素到 {@link #beanCollection} 中
	 */
	private int detailSize;
	
	/**
	 * 参数 map, 可以使用普通 map 或 {@link JavaBeanShadowMap}
	 */
	private Map parameters = new HashMap();
	
	/** 填充 detail band 的 java bean 集合 */
	private Collection beanCollection = new ArrayList();
	
	//	~ Constructors ===========================================================	
	
	public JasperReportProvider() {
	}
	
	public JasperReportProvider(
		String jasperInClassPath, 
		int detailSize, 
		Map parameters, 
		Collection beanCollection) {
		this.jasperInClassPath = jasperInClassPath;
		this.detailSize = detailSize;
		this.parameters = parameters;
		this.beanCollection = beanCollection;
	}
	
	//  ~ Methods ================================================================	
	
	/**
	 * @return the beanCollection
	 */
	public Collection getBeanCollection() {
		if (beanCollection == null) {
			beanCollection = new ArrayList();
		}
		return beanCollection;
	}

	/**
	 * @param beanCollection the beanCollection to set
	 */
	public void setBeanCollection(Collection beanCollection) {
		this.beanCollection = beanCollection;
	}

	/**
	 * @return the detailSize
	 */
	public int getDetailSize() {
		return detailSize;
	}

	/**
	 * @param detailSize the detailSize to set
	 */
	public void setDetailSize(int detailSize) {
		this.detailSize = detailSize;
	}

	/**
	 * @return the jasperInClassPath
	 */
	public String getJasperInClassPath() {
		return jasperInClassPath;
	}

	/**
	 * @param jasperInClassPath the jasperInClassPath to set
	 */
	public void setJasperInClassPath(String jasperInClassPath) {
		this.jasperInClassPath = jasperInClassPath;
	}

	/**
	 * @return the parameters
	 */
	public Map getParameters() {
		if (parameters == null) {
			parameters = new HashMap();
		}
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Map parameters) {
		this.parameters = parameters;
	}
	
	
}
