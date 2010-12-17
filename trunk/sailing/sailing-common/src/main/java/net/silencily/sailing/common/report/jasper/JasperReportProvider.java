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
 * ����һ�� jasperreport, �ṩ����������������Դ 
 * @since 2006-9-27
 * @author java2enterprise
 * @version $Id: JasperReportProvider.java,v 1.1 2010/12/10 10:54:17 silencily Exp $
 */
public class JasperReportProvider implements Serializable {
	
	//	~ Static fields/initializers =============================================	
	
	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = -1547794842868171419L;

	//	~ Instance fields ========================================================
	
	/** jasper �ļ�����·�� */
	private String jasperInClassPath;
	
	/** 
	 * detail band �������ɵ���������, ����ģ�������������ʵ���������,
	 * ������� null Ԫ�ص� {@link #beanCollection} ��
	 */
	private int detailSize;
	
	/**
	 * ���� map, ����ʹ����ͨ map �� {@link JavaBeanShadowMap}
	 */
	private Map parameters = new HashMap();
	
	/** ��� detail band �� java bean ���� */
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
