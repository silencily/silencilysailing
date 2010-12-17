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
 * 基于<code>beanshell</code>动态脚本实现集成的基类, 按照<code>sun jar specification</code>
 * 在<code>META-INF/services</code>中检索实现脚本
 * @author zhangli
 * @version $Id: BeanShellIntegrationService.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 * @since 2007-5-1
 */
public abstract class BeanShellIntegrationService implements InitializingBean {
    
    /** 把{@link #logger}放到<code>beanshell</code>执行环境中, 方便日志 */
    protected static final String KEY_LOGGER = "logger";
    
    /** 为了通过众所周知的接口方便地开关日志, 而不是通过比较生疏的具体实现 */
    private Log logger = LogFactory.getLog(BeanShellIntegrationService.class);
    
    /** 检索实现脚本的路径前缀 */
    protected static final String IMPL_PATH_PREFIX = "META-INF/services";
    
    protected Interpreter interpreter = new Interpreter();
    
    /**
     * 检索实现的服务类, 这个类名称作为检索实现脚本的文件名称, 这是集成服务实现的规定, 子类
     * 必须提供这个值
     * @return 实现的服务类
     */
    protected abstract Class getServiceClass();
    
    /**
     * 具体的实现覆盖这个方法, 提供在脚本中需要的变量, 比如在脚本执行中需要<code>JdbcTemplate</code>
     */
    protected void addBeanShellRequiredVariables(Map variables) {
    }
    
    protected Object service;

    public void afterPropertiesSet() throws Exception {
        String className = getServiceClass().getName();
        String path = new StringBuffer(IMPL_PATH_PREFIX).append("/").append(className).toString();
        Resource resource = new ClassPathResource(path);
        if (!resource.exists()) {
            throw new IllegalStateException("没有在" 
                + path 
                + "找到AuthenticationService实现脚本");
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
            /* 我用GB2312编写的这个文件,如果修改这个文件仍要保持这个编码 */
            this.interpreter.setClassLoader(ClassLoader.getSystemClassLoader());
            service = this.interpreter.eval(new InputStreamReader(resource.getInputStream(), "GB2312"));
        } catch (Exception e) {
            String msg = getServiceClass().getName() + "实现脚本错误";
            logger.error(msg, e);
            throw new UnexpectedException(msg, e);
        }
    }
}
