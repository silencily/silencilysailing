/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project framework
 */
package net.silencily.sailing.framework.persistent;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.silencily.sailing.container.ConfigurationException;
import net.silencily.sailing.resource.ResourceProbe;
import net.silencily.sailing.resource.impl.AntResourceProbe;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.AbstractResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXException;

/**
 * 使用 {@link ResourceProbe} 查找资源的抽象类, 提供了一些基本的方法
 * @since 2006-7-18
 * @author java2enterprise
 * @version $Id: AbstractResourceProbeBasedResource.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 */
public abstract class AbstractResourceProbeBasedResource extends AbstractResource {
	
	private static transient Log logger = LogFactory.getLog(AbstractResourceProbeBasedResource.class);
	
	protected String charEncoding = "UTF-8";
	
	protected ResourceProbe resourceProbe = new AntResourceProbe();
		
	/**
	 * @param charEncoding the charEncoding to set
	 */
	public void setCharEncoding(String charEncoding) {
		this.charEncoding = charEncoding;
	}

	/**
	 * @param resourceProbe the resourceProbe to set
	 */
	public void setResourceProbe(ResourceProbe resourceProbe) {
		this.resourceProbe = resourceProbe;
	}
	
	/**
	 * 得到 EntityResolver, con-create class 可根据具体情况选用不同的实现
	 * @return the EntityResolver
	 */
	protected abstract org.xml.sax.EntityResolver getEntityResolver();
	
    /**
     * 序列化<code>Document</code>对象到可读的文本格式
     * @param document 要序列化的<code>Document</code>
     * @return 表现<code>xml</code>的<code>string</code>
     */
	protected String documentToString(Document document) {
    	StringWriter sw = new StringWriter();
        try {
            Transformer transformer = getTransformer();         
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, document.getDoctype().getPublicId());
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, document.getDoctype().getSystemId());
            Source source = new DOMSource(document.getDocumentElement());
            Result result = new StreamResult(sw);
            transformer.transform(source, result);
        } catch (Exception e) {
            logAndThrows("由document序列化成文本内容错误", e);
        }
        
        return sw.toString();
    }
	
	
	
    /**
     * @return
     */
	protected DocumentBuilder getDocumentBuilder() {
        DocumentBuilder parser = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        try {
        	parser = factory.newDocumentBuilder();
        	parser.setEntityResolver(getEntityResolver());      	
        } catch (ParserConfigurationException e) {
            logAndThrows("DocumentBuilderFactory 配置错误", e);
        }
        return parser;
    }
	
	/**
	 * 根据类路径资源组装一个 document
	 * @param path 类路径资源, eg /com/coheg/temp.xml
	 * @return the polulated document
	 */
	protected Document populateDocumentFromClassPathResource(String path) {
		Document doc = null;  
        try {
            doc = getDocumentBuilder().parse(Thread.currentThread().getContextClassLoader().getResourceAsStream(path));
        } catch (SAXException e) {
            logAndThrows("配置文件语法错误", e);
        } catch (IOException e) {
            logAndThrows("读取配置文件内容错误", e);
        }
        
        return doc;
	}
	

    protected Transformer getTransformer() throws TransformerConfigurationException {
        TransformerFactory factory = TransformerFactory.newInstance();
        return factory.newTransformer();
    }
    
    /**
     * <code>log</code>错误信息, 抛出异常. 
     */
    protected void logAndThrows(String msg, Exception e) {
        ConfigurationException ex = new ConfigurationException(msg);
        if (e != null) {
            ex.initCause(e);
            if (StringUtils.isNotBlank(e.getMessage())) {
                msg += ":" + e.getMessage();
            }
            logger.error(msg, e);
        } else {
            logger.error(msg);
        }
        
        throw ex;
    }

	/**
	 * 根据节点名称在 document 中取出其实际内容
	 * @param document the document
	 * @param nodeTagName 节点名称
	 * @return 节点内容
	 * @throws TransformerException 如果解析 xml 出错
	 */
	protected String takeOutNodeContentByTagName(Document document, final String nodeTagName) throws TransformerException {        
	    DocumentTraversal traversal = (DocumentTraversal) document;
	    StringBuffer nodeContent = new StringBuffer();
	    NodeIterator it = traversal.createNodeIterator(
	        document.getDocumentElement(), 
	        NodeFilter.SHOW_ELEMENT, 
	        new NodeFilter() {
	            public short acceptNode(Node n) {
	                Element e = (Element) n;
	                if (nodeTagName.equals(e.getTagName())) {
	                    return FILTER_ACCEPT;
	                }
	                return FILTER_REJECT;
	            }}, 
	        false);
	    
	    Node node = null;
	    
	    while ((node = it.nextNode()) != null) {
	        Element e = (Element) node;
	        String fragment = transform(getTransformer(), e);
	        nodeContent.append(fragment);
	    }
	    it.detach();
	    
	    return nodeContent.toString();
	}
	
    protected String transform(Transformer transformer, Node node) throws TransformerException {
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StringWriter sw = new StringWriter();
        
        Source source = new DOMSource(node);
        Result result = new StreamResult(sw);
        transformer.transform(source, result);

        return sw.toString();
    }

}
