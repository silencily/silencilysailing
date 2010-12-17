package net.silencily.sailing.framework.authentication.service.impl;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.silencily.sailing.exception.UnexpectedException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import bsh.Interpreter;

/**
 * ����<code>beanshell</code>��̬�ű�ʵ�ּ��ɵĻ���, ����<code>sun jar specification</code>
 * ��<code>META-INF/services</code>�м���ʵ�ֽű�
 * @author zhangli
 * @version $Id: BeanShellIntegrationService.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 * @since 2007-5-1
 */
public abstract class BeanShellIntegrationService implements InitializingBean {
    
    /** ��{@link #logger}�ŵ�<code>beanshell</code>ִ�л�����, ������־ */
    protected static final String KEY_LOGGER = "logger";
    
    /** Ϊ��ͨ��������֪�Ľӿڷ���ؿ�����־, ������ͨ���Ƚ�����ľ���ʵ�� */
    private Log logger = LogFactory.getLog(BeanShellIntegrationService.class);
    
    /** ����ʵ�ֽű���·��ǰ׺ */
    protected static final String IMPL_PATH_PREFIX = "META-INF/services";
    
    protected Interpreter interpreter = new Interpreter();
    
    /**
     * ����ʵ�ֵķ�����, �����������Ϊ����ʵ�ֽű����ļ�����, ���Ǽ��ɷ���ʵ�ֵĹ涨, ����
     * �����ṩ���ֵ
     * @return ʵ�ֵķ�����
     */
    protected abstract Class getServiceClass();
    
    /**
     * �����ʵ�ָ����������, �ṩ�ڽű�����Ҫ�ı���, �����ڽű�ִ������Ҫ<code>JdbcTemplate</code>
     */
    protected void addBeanShellRequiredVariables(Map variables) {
    }
    
    protected Object service;

    public void afterPropertiesSet() throws Exception {
        String className = getServiceClass().getName();
        String path = new StringBuffer(IMPL_PATH_PREFIX).append("/").append(className).toString();
        Resource resource = new ClassPathResource(path);
        if (!resource.exists()) {
            throw new IllegalStateException("û����" 
                + path 
                + "�ҵ�AuthenticationServiceʵ�ֽű�");
        }
        try {
            Map map = new HashMap();
            addBeanShellRequiredVariables(map);
            map.put(KEY_LOGGER, logger);
            for (Iterator it = map.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry entry = (Map.Entry) it.next();
                String name = (String) entry.getKey();
                Object val = entry.getValue();
                this.interpreter.set(name, val);
            }
            /* ����GB2312��д������ļ�,����޸�����ļ���Ҫ����������� */
            this.interpreter.setClassLoader(ClassLoader.getSystemClassLoader());
            service = this.interpreter.eval(new InputStreamReader(resource.getInputStream(), "GB2312"));
        } catch (Exception e) {
            String msg = getServiceClass().getName() + "ʵ�ֽű�����";
            logger.error(msg, e);
            throw new UnexpectedException(msg, e);
        }
    }
}
