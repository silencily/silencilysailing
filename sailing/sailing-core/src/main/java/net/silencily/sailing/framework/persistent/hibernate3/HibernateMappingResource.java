/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project framework
 */
package net.silencily.sailing.framework.persistent.hibernate3;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.xml.transform.TransformerException;

import net.silencily.sailing.framework.core.GlobalParameters;
import net.silencily.sailing.framework.persistent.AbstractResourceProbeBasedResource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.util.DTDEntityResolver;
import org.springframework.beans.factory.InitializingBean;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;

/**
 * @since 2006-7-18
 * @author java2enterprise
 * @version $Id: HibernateMappingResource.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 */
public class HibernateMappingResource extends AbstractResourceProbeBasedResource implements InitializingBean {

	private static transient Log logger = LogFactory.getLog(HibernateMappingResource.class);
	        	
    private String hibernateRootConfig = GlobalParameters.CONFIGURATION_LOCATION + "/framework-hibernate-foo.hbm.xml";
    
    /** 查找 hbm 文件的目标文件 */
	private String hibernateMappingPattern = "net/silencily/**/*.hbm.xml,net/**/domain/*.hbm.xml";

	/** framework-hibernate-foo.hbm.xml 中结尾的字符串 */
	private static final String HBM_LAST_TEXT = "</hibernate-mapping>";
	
	/** Hibernate mapping DTD 中按照固定位置排列的元素 */
	private static List HIBERNATE_MAPPING_DTD_ELEMENTS = new ArrayList() {
		private static final long serialVersionUID = -7001684301651717163L;
		{
			add("meta");
			add("typedef");
			add("import");
			add("class");
			add("subclass");
			add("joined-subclass");
			add("union-subclass");
			add("resultset");
			add("query");
			add("sql-query");
			add("filter-def");
			add("database-object");
		}
	};
	
	/** 用于存储 hibernate mapping 配置内容 */
	private String hibernateMappingConfigContent;
	
	protected EntityResolver getEntityResolver() {
		return new DTDEntityResolver();
	}
	
	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(this.hibernateMappingConfigContent.getBytes(charEncoding));
	}
	
	public String getDescription() {
		return "hibernate mapping resource";
	}

	public void afterPropertiesSet() throws Exception {
        try {
            init();
        } catch (Exception e) {
            logAndThrows("配置错误", e);
        }
	}

    public void init() {
    	if (logger.isInfoEnabled()) {
    		logger.info(" 开始搜索所有满足 [" + hibernateMappingPattern +"] 规则的 hibernate 映射文件  ");
    	}
        Document doc = createDocument();      
        List hbmPaths = Arrays.asList(this.resourceProbe.search(hibernateMappingPattern, null));
        List hbmDocuments = new ArrayList(hbmPaths.size());
        
        for (Iterator iter = hbmPaths.iterator(); iter.hasNext(); ) {
        	String hbmPath = (String) iter.next();
        	Document hbm = populateDocumentFromClassPathResource(hbmPath);
        	hbmDocuments.add(hbm);
        }
        
        StringBuffer initialContent = new StringBuffer();
        
        for (Iterator iter = HIBERNATE_MAPPING_DTD_ELEMENTS.iterator(); iter.hasNext(); ) {
        	String tagName = (String) iter.next();
        	for (Iterator hbmIter = hbmDocuments.iterator(); hbmIter.hasNext(); ) {
        		Document hbm = (Document) hbmIter.next();
    			try {
    				initialContent.append(takeOutNodeContentByTagName(hbm, tagName));
    			} catch (TransformerException t) {
            		logAndThrows(" 解析 hibernate 配置文件出错 ", t);
            	}      		
        	}
        }
                
        String nodeContent = initialContent.toString();        
        
        try {
            String rootDocContent = documentToString(doc);       
            int endPos = rootDocContent.indexOf(HBM_LAST_TEXT);
            String orginalContent = rootDocContent.substring(0, endPos);
            hibernateMappingConfigContent = new StringBuffer(orginalContent).append(nodeContent).append(HBM_LAST_TEXT).toString();
        } catch (Exception e) {
        	throw new RuntimeException(e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug(" hibernate mapping 配置文件加载成功, 内容为 " + System.getProperty("line.separator") + this.hibernateMappingConfigContent);
        }
    }        
    
    /**
     * 从属性<code>hibernateRootConfig</code>创建表现<code>hibernate</code>根配置的<code>Document</code>
     * @return <code>hibernate</code>根配置的<code>Document</code>
     */
    private Document createDocument() {
        return populateDocumentFromClassPathResource(this.hibernateRootConfig);
    }
       
}
