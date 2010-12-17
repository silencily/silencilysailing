package net.silencily.sailing.framework.codename.configuration;

import net.silencily.sailing.framework.codename.AbstractModuleCodeName;

/**
 * 配置管理服务, 这个接口提供给用户维护配置信息, 也包括可编程地维护配置信息, 每个配置有可删除
 * 和可修改两个属性, 用于控制用户是否可以更新配置
 * @author zhangli
 * @version $Id: ConfigurationManagementService.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 * @since 2007-6-18
 */
public interface ConfigurationManagementService extends ConfigurationService {
    
    /**
     * 创建一个新的配置, 调用这个方法时要检查参数上级节点的"可创建属性", 如果这个可创建属性是
     * <code>false</code>抛出异常, 如果没有这样的节点存在或者不是<code>false</code>就可
     * 以正常执行
     * @param codename 要创建的配置
     * @throws IllegalStateException 如果不能创建这个配置
     */
    void create(AbstractModuleCodeName codename);
    
    /**
     * 更新已有的配置信息, 调用这个方法要检查"可更新属性", 如果不是<code>true</code>就不能
     * 执行这个操作
     * @param codename 要更新的配置信息
     * @throws IllegalStateException 如果不能更新这个配置, 就是"可更新属性"不是<code>true</code>
     */
    void update(AbstractModuleCodeName codename);
    
    /**
     * 删除已有的配置信息, 调用这个方法要检查"可删除属性", 如果不是<code>true</code>就不能
     * 执行删除操作
     * @param codename 要删除的配置信息
     * @throws IllegalStateException 如果不能删除这个配置, 就是"可删除属性"不是<code>true</code>
     */
    void delete(AbstractModuleCodeName codename);

}
