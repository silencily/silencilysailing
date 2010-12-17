package net.silencily.sailing.resource.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import net.silencily.sailing.container.support.ConfigurationResourceTree;
import net.silencily.sailing.framework.core.GlobalParameters;
import net.silencily.sailing.framework.exception.NoSuchMessageException;
import net.silencily.sailing.resource.CodedMessageSource;
import net.silencily.sailing.resource.ResourceProbe;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.util.Assert;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

/**
 * {@link com.coheg.resource.CodedMessageSource CodedMessageSource}的实现类, 支持分层
 * 加载编码的信息, 与<code>spring's AbstractMessageSource</code>不同的是所有的方法在参数
 * <code>code</code>是<code>null</code>或<code>empty</code>时抛出异常, 另外一点是当没有
 * 参数化的信息时就返回消息本身, 而不是通过<code>MessageFormat</code>解决
 * 
 * @author scott
 * @since 2006-1-9
 * @version $Id: HierarchyCodedMessageSource.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 *
 */
public class HierarchyCodedMessageSource extends AbstractMessageSource implements CodedMessageSource {

    /**
     * 系统中使用的缺省<code>Locale</code>, 目前我们暂时不考虑向国外销售我们的<code>MIS</code>
     */
    private static Locale CHINESE = Locale.CHINESE;
    
    /**
     * 读配置文件时使用的缺省编码
     */
    private static String ENCODING = "GBK";
    
    
    /**
     * 用于在系统启动时缓存已经加载的信息
     */
    private Properties cachedMessages = new Properties();
    
    private String messagePattern = GlobalParameters.MESSAGE_PATTERN;

    /**
     * 读取属性文件的缺省组件
     */
    private PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();
    
    public void setPropertiesPersistenter(PropertiesPersister propertiesPersister) {
        this.propertiesPersister = propertiesPersister;
    }
    
    public void setMessagePattern(String messagePattern) {
        this.messagePattern = messagePattern;
    }

    public String getMessage(String code) {
        String msg = null;
        try {
            msg = getMessage(code, null, CHINESE);
        } catch (org.springframework.context.NoSuchMessageException e) {
            throw new NoSuchMessageException(code);
        }
        
        return msg;
    }

    public String getMessage(String code, String defaultMsg) {
        return getMessage(code, null, defaultMsg, CHINESE);
    }

    public String getMessage(String code, Object[] args) {
        String msg = null;
        try {
            msg = getMessage(code, args, CHINESE);
        } catch (org.springframework.context.NoSuchMessageException e) {
            throw new NoSuchMessageException(code);
        }
        
        return msg;
    }

    public String getMessage(String code, Object[] args, String defaultMessage) {
        return getMessage(code, args, defaultMessage, CHINESE);
    }

    public boolean exists(String code) {
        return cachedMessages.containsKey(code);
    }

    public Object getAdapter(Class clazz) {
        return null;
    }

    protected String getMessageInternal(String code, Object[] args, Locale locale) {
        Assert.notNull(code, "检索信息时编码是空值");
        if (args == null || args.length == 0) {
            return resolveCodeWithoutArguments(code, locale);
        } else {
            MessageFormat mf = resolveCode(code, locale);
            if (mf != null) {
                return mf.format(args);
            }
        }
        
        return null;

    }
    
    /**
     * 当没有格式参数时返回信息本身, 这个实现避免第一次使用带有格式参数检索信息后, 使用
     * <code>getProperty(String)</code>不能检索到信息的问题
     */
    protected String resolveCodeWithoutArguments(String code, Locale locale) {
        Object obj = cachedMessages.get(code);
        String ret = null;
        if (obj != null) {
            if (obj instanceof MessageFormat) {
                ret = ((MessageFormat) obj).toPattern();
            } else {
                ret = String.valueOf(obj);
            }
        }
        
        return ret;
    }

    protected synchronized MessageFormat resolveCode(String code, Locale locale) {
        Object obj = cachedMessages.get(code);
        if (obj == null) {
            return null;
        }
        
        if (obj instanceof MessageFormat) {
            return (MessageFormat) obj;
        }
        
        MessageFormat mf = new MessageFormat((String) obj, locale);
        cachedMessages.put(code, mf);

        return mf;
    }
    
    public final CodedMessageSource init() throws IllegalStateException {
        List resources = fetchResources();

        for (Iterator it = resources.iterator(); it.hasNext(); ) {
            Properties p = new Properties();
            ConfigurationResourceTree node = (ConfigurationResourceTree) it.next();
            InputStream in = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream(node.getResourceFullName());
            
            if (in == null) {
                String msg = "加载编码信息时没找到名称是[" + node.getResourceFullName() + "]的资源";
                logger.error(msg);
                throw new IllegalStateException(msg);
            }
            
            try {
                InputStreamReader reader = new InputStreamReader(in, ENCODING);
                propertiesPersister.load(p, reader);
                addProperties(p);
            } catch (UnsupportedEncodingException e) {
                String msg = "加载配置文件[" + node.getResourceFullName() + "]编码错误";
                logger.error(msg, e);
                IllegalStateException ex = new IllegalStateException(msg);
                ex.initCause(e);
                throw ex;
            } catch (IOException e) {
                String msg = "读配置文件[" + node.getResourceFullName() + "]错误";
                logger.error(msg, e);
                IllegalStateException ex = new IllegalStateException(msg);
                ex.initCause(e);
                throw ex;
            } 
        }
        
        return this;
    }
    
    protected List fetchResources() {
        ResourceProbe probe = new AntResourceProbe();
        String[] resourceNames = probe.search(this.messagePattern);
        final List results = new ArrayList(resourceNames.length);
        /* 把资源类路径变成 ConfigurationResourceTree, 便于按照层次关系组织 */
        CollectionUtils.forAllDo(Arrays.asList(resourceNames), new Closure() {
            public void execute(Object element) {
                ConfigurationResourceTree node = new ConfigurationResourceTree((String) element);
                results.add(node);
            }
        });
        Collections.sort(results);

        return results;
    }
    
    /**
     * 合并资源, 如果同名的信息已经在<code>cachedMessages</code>中就不再加载
     * @param p 要合并的信息
     */
    private void addProperties(Properties p) {
        Enumeration enumeration = p.keys();
        while (enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();
            if (!cachedMessages.containsKey(key)) {
                cachedMessages.put(key, p.getProperty(key));
            }
        }
    }
}
