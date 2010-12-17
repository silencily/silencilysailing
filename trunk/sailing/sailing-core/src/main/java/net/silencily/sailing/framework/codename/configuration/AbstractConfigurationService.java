package net.silencily.sailing.framework.codename.configuration;

import java.util.List;
import java.util.Map;

import net.silencily.sailing.framework.business.Initializable;
import net.silencily.sailing.framework.business.InitializableException;
import net.silencily.sailing.framework.business.InitializableInProcessException;
import net.silencily.sailing.framework.codename.AbstractModuleCodeName;

import org.springframework.beans.factory.InitializingBean;

/**
 * 配置服务的业务实现, 没有持久化部分, 实现了{@link Initializable}接口, 在系统启动时从<code>metadata</code>
 * 中初始化配置
 * @author zhangli
 * @version $Id: AbstractConfigurationService.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 * @since 2007-6-19
 */
public class AbstractConfigurationService implements ConfigurationService, Initializable, InitializingBean {
    
    /** 这个配置服务所操作的配置类型, 一定是{@link AbstractModuleCodeName}的子类 */
    private Class type;

    public ConfigurationService configure(String prefix) {
        // TODO Auto-generated method stub
        return null;
    }

    public List list() {
        // TODO Auto-generated method stub
        return null;
    }

    public AbstractModuleCodeName load(String code) {
        // TODO Auto-generated method stub
        return null;
    }

    public Map map() {
        // TODO Auto-generated method stub
        return null;
    }

    public Class type() {
        // TODO Auto-generated method stub
        return null;
    }

    public void initialize() throws InitializableException, InitializableInProcessException {
        // TODO Auto-generated method stub
        
    }

    public boolean isInitialized() {
        // TODO Auto-generated method stub
        return false;
    }

    public void afterPropertiesSet() throws Exception {
        if (type == null) {
            throw new IllegalArgumentException();
        }
    }
}
