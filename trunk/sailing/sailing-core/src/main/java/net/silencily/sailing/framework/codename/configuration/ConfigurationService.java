package net.silencily.sailing.framework.codename.configuration;

import java.util.List;
import java.util.Map;

import net.silencily.sailing.framework.codename.AbstractModuleCodeName;
import net.silencily.sailing.framework.service.ServiceBaseWithNotAllowedNullParamters;

/**
 * 系统配置服务, 提供了分层配置的概念, 层是针对模块以及模块下的功能而言, 整个系统全局的配置位于
 * 根节点中, 而各个模块的配置可以通过模块名获得. 
 * @author zhangli
 * @version $Id: ConfigurationService.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 * @since 2007-6-17
 */
public interface ConfigurationService extends ServiceBaseWithNotAllowedNullParamters {
    
    /**
     * 各层配置之间的分隔符, 注意不要使用这个字符作为<code>code</code>, 如果使用了可能出现
     * 不能检索出所需的配置的情况, 比如使用"flaw.specialty"作为配置<code>code</code>,你
     * 就不能使用"flaw."检索出任何配置
     */
    String SEPARATOR = ".";
    
    /**
     * 检索以参数指定代码的配置为根配置的配置服务, 这个配置服务仅仅可以看到这个配置以及以下的
     * 配置
     * @param prefix 返回一个{@link ConfigurationService}, 从返回的实例可以操作以这个
     * 参数指定的代码的配置及其子配置
     * @return 可以操作参数指定前缀的所有配置的{@link ConfigurationService}
     * @throws NullPointerException 如果参数是<code>null</code>或<code>empty</code>
     */
    ConfigurationService configure(String prefix);
    
    /**
     * 列出所有的配置, 元素类型是{@link AbstractModuleCodeName}
     * @return 所有的配置, 如果没有结果返回<code>Collections.EMPTY_LIST</code>
     */
    List list();
    
    /**
     * 返回一个<code>Map</code>, <code>key</code>是代码, <code>value</code>是{@link AbstractModuleCodeName}
     * 其中代码就是{@link AbstractModuleCodeName#getCode()}的值
     * @param type 要检索的配置类型, 这个类一定是{@link AbstractModuleCodeName}的子类
     * @return <code>Map</code>形式的所有的配置, 如果没有任何配置返回<code>Collections.EMPTY_MAP</code>
     */
    Map map();
    
    /**
     * 检索指定代码的配置, 在没有对应的配置时返回<code>null</code>
     * @param code 配置的代码
     * @return 以参数为代码的配置, <b>如果没有指定代码的配置返回<code>null</code></b>
     * @throws NullPointerException 如果参数是<code>null</code>或<code>empty</code>
     */
    AbstractModuleCodeName load(String code);
    
    /**
     * 查询这个配置服务作用的配置类型, 配置的类型一定是{@link AbstractModuleCodeName}的
     * 子类, 这个组件的实现使用这个方法检索实际的配置类型
     * @return 这个配置服务操作的配置类型
     */
    Class type();
}
