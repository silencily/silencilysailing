package net.silencily.sailing.framework.core;

/**
 * 保存核心的配置参数, 这些参数体现了核心的设计方案, 资源存放路径和层次关系, 很少在应用中发生变化
 * 
 * @author scott
 * @since 2005-12-24
 * @version $Id: GlobalParameters.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 *
 */
public interface GlobalParameters {
    
    /**
     * 系统中配置资源的位置, 所有的配置信息必须在这个位置下
     */
    String CONFIGURATION_LOCATION = "conf";
    
    /**
     * 系统中根配置路径及名称, 这个文件在整个系统中仅存在一个. 注意没有资源配置根目录
     */
    String ROOT_CONFIGURATION_NAME = CONFIGURATION_LOCATION + "/system-configuration.xml";
    
    /**
     * 搜索除了系统根配置文件外其他配置文件的模式, 就是说只有根配置文件可以直接在
     * {@link #CONFIGURATION_LOCATION CONFIGURATION_LOCATION}下, 其他
     * 必须在这个位置下的另外一级或多级目录下, 而且后缀必须是<code>-configuration.xml</code>
     */
    String CONFIGURATION_PATTERN = CONFIGURATION_LOCATION + "/**/*-configuration.xml";
    
    /**
     * 架构加载客户化(外部化)信息资源的模式, 这种资源文件必须遵守<code>Properties</code>命名
     * 格式, 这些资源主要用于错误提示信息, 提示信息等消息定义
     */
    String MESSAGE_PATTERN = CONFIGURATION_LOCATION + "/**/*-messages.properties";
    
    /**
     * 架构组件在配置中的名称, 也用于前缀
     */
    String MODULE_NAME = "system";
    
    /**
     * 架构中的系统级事件广播组件的名称, 所有实现了<code>ApplicationListener</code>接口的
     * 组件都可以接受到通知
     */
    String APPLICATION_EVENT_MULTICASTER = MODULE_NAME + "." + "applicationEventMulticaster";
    
    /**
     * 系统中客户化信息资源管理器的<code>bean</code>配置名称, 在系统加载时这个组件负责加载信息
     * 资源
     */
    String APPLICATION_CODED_MESSAGE = MODULE_NAME + "." + "codedMessageSource";
    
    /**
     * 系统中处理异常信息的异常处理组件名称
     */
    String APPLICATION_EXCEPTION_HANDLER = MODULE_NAME + "." + "exceptionHandler";

}
