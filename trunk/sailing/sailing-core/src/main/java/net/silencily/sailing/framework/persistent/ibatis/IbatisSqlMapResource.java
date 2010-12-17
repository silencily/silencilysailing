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
 * <p>用于检索<code>sqlmap</code>配置文件的资源, 配置在<code>springframework's beanfactory</code>
 * 中, 作为<code>SqlMapClientFactoryBean's configLocation</code>属性的值, 这个类从系统的根配置目录
 * {@link net.silencily.sailing.framework.core.coheg.core.GlobalParameters#CONFIGURATION_LOCATION <code>conf</conf>}
 * 下检索<code>sqlmap config</code>文件, 然后从类路径中查找满足下面条件的资源作为<code>sqlmap</code>
 * 的配置<ul>
 * <li>资源名称以<code>-sqlmap.xml</code>结尾</li>
 * <li>在根配置目录下</li></ul></p>
 * <p><code>sqlmap</code>配置文件的位置应该同<code>bean factory</code>配置策略相同, 采用
 * 分级存放的策略, 比如<code>OA</code>子系统的<code>sqlmap</code>文件应该是<code>oa-sqlmap.xml</code>
 * , 存放位置是<code>conf/oa</code>下面, 而<code>OA</code>中信息发布的名称和位置应该为
 * <code>conf/oa/info/info-sqlmap.xml</code></p>
 * <p>这个实现是一个<code>Resource</code>的简单版, 因为它仅仅用于在<code>xml bean factory</code>
 * 中加载<code>sqlmap</code>配置文件, <b>配置注意:一定*不*要在<code>sqlmap</code>根配置和
 * <code>sqlmap</code>配置模式前面加上根配置目录<code>conf</code></b></p>
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
     * 验证是否设置了必要的属性, 这些属性都提供了缺省值
     */
    private void check() {
        if (StringUtils.isBlank(this.sqlmapconfig)) {
            logAndThrows("没有指定sqlmap-config文件", null);
        }
        
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(this.sqlmapconfig);
        if (in == null) {
            logAndThrows("没找到sqlmap根配置文件[" + this.sqlmapconfig + "]", null);
        }
        
        try {
            in.close();
        } catch (IOException e) {
            logger.warn("验证sqlmap根配置后不能关闭输入流", e);
            in = null;
        }
        
        if (this.resourceProbe == null) {
            logAndThrows("没有指定读取sqlmap资源的ResourceProbe", null);
        }
        
        if (StringUtils.isBlank(this.sqlmapPattern)) {
            logAndThrows("没有指定检索sqlmap资源模式", null);
        }
    }
    
    
    /**
     * 使用类路径资源及模式配置<code>ibatis</code>的根配置和<code>sqlmap</code>配置, 配置
     * 后的<code>sqlmap</code>的顺序符合模块分层的概念, 尽管目前对于<code>ibatis</code>没
     * 有作用. 最后把生成的<code>xml</code>序列化成字符串供<code>getInputStream()</code>
     * 方法使用
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
                logger.debug("根据模式" + this.sqlmapPattern + "]没有找到任何sqlmap配置");
            }
        }
        this.sqlmapconfigcontent = documentToString(doc);
        if (logger.isDebugEnabled()) {
            logger.debug("sqlmap-config.xml文件内容" + System.getProperty("line.separator") + this.sqlmapconfigcontent);
        }
    }

    public void afterPropertiesSet() throws Exception {
        check();
        try {
            init();
        } catch (Exception e) {
            logAndThrows("配置错误", e);
        }
    }
    
    /**
     * 从属性<code>sqlmapconfig</code>创建表现<code>ibatis</code>根配置的<code>Document</code>
     * @return <code>ibatis</code>根配置的<code>Document</code>
     */
    private Document createDocument() {        
        return populateDocumentFromClassPathResource(sqlmapconfig);
    }
    
    
    /**
     * 添加<code>sqlmap</code>路径到<code>sql-map-config</code>文档的根节点
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
