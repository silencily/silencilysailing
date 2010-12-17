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
 * ʹ�� {@link ResourceProbe} ������Դ�ĳ�����, �ṩ��һЩ�����ķ���
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
	 * �õ� EntityResolver, con-create class �ɸ��ݾ������ѡ�ò�ͬ��ʵ��
	 * @return the EntityResolver
	 */
	protected abstract org.xml.sax.EntityResolver getEntityResolver();
	
    /**
     * ���л�<code>Document</code>���󵽿ɶ����ı���ʽ
     * @param document Ҫ���л���<code>Document</code>
     * @return ����<code>xml</code>��<code>string</code>
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
            logAndThrows("��document���л����ı����ݴ���", e);
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
            logAndThrows("DocumentBuilderFactory ���ô���", e);
        }
        return parser;
    }
	
	/**
	 * ������·����Դ��װһ�� document
	 * @param path ��·����Դ, eg /com/coheg/temp.xml
	 * @return the polulated document
	 */
	protected Document populateDocumentFromClassPathResource(String path) {
		Document doc = null;  
        try {
            doc = getDocumentBuilder().parse(Thread.currentThread().getContextClassLoader().getResourceAsStream(path));
        } catch (SAXException e) {
            logAndThrows("�����ļ��﷨����", e);
        } catch (IOException e) {
            logAndThrows("��ȡ�����ļ����ݴ���", e);
        }
        
        return doc;
	}
	

    protected Transformer getTransformer() throws TransformerConfigurationException {
        TransformerFactory factory = TransformerFactory.newInstance();
        return factory.newTransformer();
    }
    
    /**
     * <code>log</code>������Ϣ, �׳��쳣. 
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
	 * ���ݽڵ������� document ��ȡ����ʵ������
	 * @param document the document
	 * @param nodeTagName �ڵ�����
	 * @return �ڵ�����
	 * @throws TransformerException ������� xml ����
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
