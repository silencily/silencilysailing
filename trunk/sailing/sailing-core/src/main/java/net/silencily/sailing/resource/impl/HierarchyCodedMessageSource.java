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
 * {@link com.coheg.resource.CodedMessageSource CodedMessageSource}��ʵ����, ֧�ֲַ�
 * ���ر������Ϣ, ��<code>spring's AbstractMessageSource</code>��ͬ�������еķ����ڲ���
 * <code>code</code>��<code>null</code>��<code>empty</code>ʱ�׳��쳣, ����һ���ǵ�û��
 * ����������Ϣʱ�ͷ�����Ϣ����, ������ͨ��<code>MessageFormat</code>���
 * 
 * @author scott
 * @since 2006-1-9
 * @version $Id: HierarchyCodedMessageSource.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 *
 */
public class HierarchyCodedMessageSource extends AbstractMessageSource implements CodedMessageSource {

    /**
     * ϵͳ��ʹ�õ�ȱʡ<code>Locale</code>, Ŀǰ������ʱ������������������ǵ�<code>MIS</code>
     */
    private static Locale CHINESE = Locale.CHINESE;
    
    /**
     * �������ļ�ʱʹ�õ�ȱʡ����
     */
    private static String ENCODING = "GBK";
    
    
    /**
     * ������ϵͳ����ʱ�����Ѿ����ص���Ϣ
     */
    private Properties cachedMessages = new Properties();
    
    private String messagePattern = GlobalParameters.MESSAGE_PATTERN;

    /**
     * ��ȡ�����ļ���ȱʡ���
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
        Assert.notNull(code, "������Ϣʱ�����ǿ�ֵ");
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
     * ��û�и�ʽ����ʱ������Ϣ����, ���ʵ�ֱ����һ��ʹ�ô��и�ʽ����������Ϣ��, ʹ��
     * <code>getProperty(String)</code>���ܼ�������Ϣ������
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
                String msg = "���ر�����Ϣʱû�ҵ�������[" + node.getResourceFullName() + "]����Դ";
                logger.error(msg);
                throw new IllegalStateException(msg);
            }
            
            try {
                InputStreamReader reader = new InputStreamReader(in, ENCODING);
                propertiesPersister.load(p, reader);
                addProperties(p);
            } catch (UnsupportedEncodingException e) {
                String msg = "���������ļ�[" + node.getResourceFullName() + "]�������";
                logger.error(msg, e);
                IllegalStateException ex = new IllegalStateException(msg);
                ex.initCause(e);
                throw ex;
            } catch (IOException e) {
                String msg = "�������ļ�[" + node.getResourceFullName() + "]����";
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
        /* ����Դ��·����� ConfigurationResourceTree, ���ڰ��ղ�ι�ϵ��֯ */
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
     * �ϲ���Դ, ���ͬ������Ϣ�Ѿ���<code>cachedMessages</code>�оͲ��ټ���
     * @param p Ҫ�ϲ�����Ϣ
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
