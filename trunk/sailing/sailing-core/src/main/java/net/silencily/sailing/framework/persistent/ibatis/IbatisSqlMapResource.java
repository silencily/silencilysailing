package net.silencily.sailing.framework.persistent.ibatis;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.silencily.sailing.container.support.ConfigurationResourceTree;
import net.silencily.sailing.framework.core.GlobalParameters;
import net.silencily.sailing.framework.persistent.AbstractResourceProbeBasedResource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.EntityResolver;

import com.ibatis.sqlmap.engine.builder.xml.SqlMapClasspathEntityResolver;

/**
 * <p>���ڼ���<code>sqlmap</code>�����ļ�����Դ, ������<code>springframework's beanfactory</code>
 * ��, ��Ϊ<code>SqlMapClientFactoryBean's configLocation</code>���Ե�ֵ, ������ϵͳ�ĸ�����Ŀ¼
 * {@link net.silencily.sailing.framework.core.coheg.core.GlobalParameters#CONFIGURATION_LOCATION <code>conf</conf>}
 * �¼���<code>sqlmap config</code>�ļ�, Ȼ�����·���в�������������������Դ��Ϊ<code>sqlmap</code>
 * ������<ul>
 * <li>��Դ������<code>-sqlmap.xml</code>��β</li>
 * <li>�ڸ�����Ŀ¼��</li></ul></p>
 * <p><code>sqlmap</code>�����ļ���λ��Ӧ��ͬ<code>bean factory</code>���ò�����ͬ, ����
 * �ּ���ŵĲ���, ����<code>OA</code>��ϵͳ��<code>sqlmap</code>�ļ�Ӧ����<code>oa-sqlmap.xml</code>
 * , ���λ����<code>conf/oa</code>����, ��<code>OA</code>����Ϣ���������ƺ�λ��Ӧ��Ϊ
 * <code>conf/oa/info/info-sqlmap.xml</code></p>
 * <p>���ʵ����һ��<code>Resource</code>�ļ򵥰�, ��Ϊ������������<code>xml bean factory</code>
 * �м���<code>sqlmap</code>�����ļ�, <b>����ע��:һ��*��*Ҫ��<code>sqlmap</code>�����ú�
 * <code>sqlmap</code>����ģʽǰ����ϸ�����Ŀ¼<code>conf</code></b></p>
 * 
 * @author scott
 * @since 2006-3-19
 * @version $Id: IbatisSqlMapResource.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 */
public class IbatisSqlMapResource extends AbstractResourceProbeBasedResource implements InitializingBean {
	
    private final Log logger = LogFactory.getLog(IbatisSqlMapResource.class);
    
    public static final String SQLMAP_ELEMENT_NAME = "sqlMap";
    
    public static final String SQLMAP_ATTRIBUTE_NAME = "resource";
    
    private String sqlmapconfig = GlobalParameters.CONFIGURATION_LOCATION + "/system-sqlmap-config.xml";
    
    private String sqlmapPattern = GlobalParameters.CONFIGURATION_LOCATION + "/**/*-sqlmap.xml";
    
    private String sqlmapconfigcontent;
    
    public void setSqlmapconfig(String sqlmapconfig) {
        this.sqlmapconfig = GlobalParameters.CONFIGURATION_LOCATION + "/" + sqlmapconfig;
    }
        
    public void setSqlmapPattern(String sqlmapPattern) {
        this.sqlmapPattern = GlobalParameters.CONFIGURATION_LOCATION + "/" + sqlmapPattern;
    }
    
    public String getDescription() {
        return "sqlmap-config:[" + this.sqlmapconfig + "]";
    }

    public String getFilename() {
        throw new UnsupportedOperationException("don't supported operations");
    }

    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(this.sqlmapconfigcontent.getBytes(charEncoding));
    }
    
    /**
     * ��֤�Ƿ������˱�Ҫ������, ��Щ���Զ��ṩ��ȱʡֵ
     */
    private void check() {
        if (StringUtils.isBlank(this.sqlmapconfig)) {
            logAndThrows("û��ָ��sqlmap-config�ļ�", null);
        }
        
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(this.sqlmapconfig);
        if (in == null) {
            logAndThrows("û�ҵ�sqlmap�������ļ�[" + this.sqlmapconfig + "]", null);
        }
        
        try {
            in.close();
        } catch (IOException e) {
            logger.warn("��֤sqlmap�����ú��ܹر�������", e);
            in = null;
        }
        
        if (this.resourceProbe == null) {
            logAndThrows("û��ָ����ȡsqlmap��Դ��ResourceProbe", null);
        }
        
        if (StringUtils.isBlank(this.sqlmapPattern)) {
            logAndThrows("û��ָ������sqlmap��Դģʽ", null);
        }
    }
    
    
    /**
     * ʹ����·����Դ��ģʽ����<code>ibatis</code>�ĸ����ú�<code>sqlmap</code>����, ����
     * ���<code>sqlmap</code>��˳�����ģ��ֲ�ĸ���, ����Ŀǰ����<code>ibatis</code>û
     * ������. �������ɵ�<code>xml</code>���л����ַ�����<code>getInputStream()</code>
     * ����ʹ��
     */
    public void init() {
        Document doc = createDocument();
        List sqlmapPaths = Arrays.asList(this.resourceProbe.search(this.sqlmapPattern, this.sqlmapconfig));
        if (sqlmapPaths.size() > 0) {
            List sqlmaps = new ArrayList(sqlmapPaths.size());
            sqlmaps.addAll(sqlmapPaths);
            CollectionUtils.transform(sqlmaps, new Transformer() {
                public Object transform(Object path) {
                    return new ConfigurationResourceTree((String) path);
                }
              });
            Collections.sort(sqlmaps);
            addSqlmaps(doc, sqlmaps);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("����ģʽ" + this.sqlmapPattern + "]û���ҵ��κ�sqlmap����");
            }
        }
        this.sqlmapconfigcontent = documentToString(doc);
        if (logger.isDebugEnabled()) {
            logger.debug("sqlmap-config.xml�ļ�����" + System.getProperty("line.separator") + this.sqlmapconfigcontent);
        }
    }

    public void afterPropertiesSet() throws Exception {
        check();
        try {
            init();
        } catch (Exception e) {
            logAndThrows("���ô���", e);
        }
    }
    
    /**
     * ������<code>sqlmapconfig</code>��������<code>ibatis</code>�����õ�<code>Document</code>
     * @return <code>ibatis</code>�����õ�<code>Document</code>
     */
    private Document createDocument() {        
        return populateDocumentFromClassPathResource(sqlmapconfig);
    }
    
    
    /**
     * ���<code>sqlmap</code>·����<code>sql-map-config</code>�ĵ��ĸ��ڵ�
     * @param doc
     * @param sqlmaps
     */
    private void addSqlmaps(Document doc, List sqlmaps) {
        for (Iterator it = sqlmaps.iterator(); it.hasNext(); ) {
            ConfigurationResourceTree node = (ConfigurationResourceTree) it.next();
            Element ele = doc.createElement(SQLMAP_ELEMENT_NAME);
            ele.setAttribute(SQLMAP_ATTRIBUTE_NAME, node.getResourceFullName());
            doc.getDocumentElement().appendChild(ele);
        }
    }

	protected EntityResolver getEntityResolver() {
		return new SqlMapClasspathEntityResolver();
	}
}
