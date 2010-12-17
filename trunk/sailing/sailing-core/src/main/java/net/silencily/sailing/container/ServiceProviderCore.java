package net.silencily.sailing.container;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import net.silencily.sailing.container.support.ConfigFinishedEvent;
import net.silencily.sailing.container.support.ConfigurationResourceTree;
import net.silencily.sailing.framework.core.GlobalParameters;
import net.silencily.sailing.framework.core.ServiceInfo;
import net.silencily.sailing.framework.service.ServiceAutoProxyCreator;
import net.silencily.sailing.resource.ResourceProbe;
import net.silencily.sailing.resource.impl.AntResourceProbe;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * <p><code>ServiceProvider</code>的核心类, 实际的处理都发生这里, 这个类本身不关心用于<code>
 * Singleton</code>还是其他的用法, 主要目的是避免使用<code>static</code>方法和属性的麻烦,
 * 用于方便测试, 不用于应用代码</p>
 * <p>理想情况下, 整个系统中通用的组件都写在根配置文件中, 比如事务,数据源等, 只要与其它子系统的组件
 * 合并到一起时, 彼此之间就可以协作, 没有使用<code>ApplicationContext</code>的原因就是这样,
 * 因为在那里的<code>bean</code>已经完成了实例化的过程</p>
 * <p>处理底层基础组件的过程:<ul>
 * <li><code>BeanFactoryPostProcessor</code>, 这类组件一定要避免不必要地引用同一个工厂中
 * 的其他组件, 避免不必要的实例化. 每个配置的这类组件仅仅本地应用, 不向下传递</li>
 * <li><code>BeanPostProcessor</code>, 这类组件在处理<code>bean factory</code>中要实例
 * 化, 并且, 根配置的<code>bean</code>向下传递</li>
 * <li>所有配置完成后, 开始发布配置完成事件</li>
 * <ul></p>
 * <p>对于这个类的所有的<code>protected</code>的方法和属性都是为了方便测试, 没有用于继承的意图</p>
 * 
 * @author scott
 * @since 2006-3-16
 * @version $Id: ServiceProviderCore.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 * @see com.coheg.container.support.ConfigurationResourceTree
 * @see com.coheg.resource.ResourceProbe
 */
public class ServiceProviderCore {
    /**
     * 用于<code>log</code>配置过程的信息, 注意开关<code>log</code>时使用这个类
     */
    private final Log logger = LogFactory.getLog(ServiceProviderCore.class);
    
    /**
     * 用于查找匹配资源的资源加载类, 没有使用<code>springframework's ResourceLoader</code>
     * 的原因是无法从<code>Resource</code>中解析出<code>class path</code>, 而这是我们所需
     * 要的
     */
    private ResourceProbe resourceProbe = new AntResourceProbe();
    
    /**
     * 用于加载配置的类加载器
     */
    private ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    
    /**
     * 根<code>bean factory</code>
     */
    protected ConfigurableListableBeanFactory rootBeanFactory;
    
    /**
     * 位于配置树结构中的叶子节点的配置, 其中的元素是<code>ConfigurableListableBeanFactory</code>,
     * 用于按照名称来检索一个<code>bean</code>
     * @see {@link org.springframework.beans.factory.BeanFactory#getBean(java.lang.String) getBean}
     * 方法
     */
    protected List leafConfigurations;

    /**
     * 系统中所有的<code>bean</code>的定义, <code>Map's key</code>是<code>bean name</code>,
     * <code>Map's value</code>是<code>BeanDefinition</code>的扩展, 增加了<code>bean definition</code>
     * 的<code>description</code>属性
     */
    protected Map allBeanDefinitions;
    
    /**
     * 系统中所有的<code>bean factory</code>, <code>Map's key</code>是配置信息<code>
     * ServiceInfo</code>, <code>Map's value</code>是相应的<code>bean factory</code>,
     * 其中包含根<code>bean factory</code>
     */
    protected Map allBeanFactories;

    public void setResourceProbe(ResourceProbe resourceProbe) {
        this.resourceProbe = resourceProbe;
    }

    /**
     * 加载系统中所有的服务配置, 加载原则是<ul>
     * <li>加载根配置文件, 如果没有找到根配置文件或找到一个以上的配置文件, 抛出异常</li>
     * <li>按照匹配模式和排除模式分层加载其它的配置, 分层的原则是由
     * {@link com.coheg.container.support.ConfigurationResourceTree 配置节点}决定</li></ul>
     * <li>初始化所有的<code>BeanFactoryPostProcessor</code></li>
     * <li>注册所有的<code>BeanPostProcessor</code></li>
     * <li>注册系统的事件广播, 并在配置结束时发布配置完成事件</li>
     * <li>如果配置中没有包含<code>SerivceInfo</code>或没有定义名称或名称重复抛出配置异常</li></ul>
     * 
     * @param rootConfigurationName 根配置文件名称, <b>不要包含配置文件根路径</b>
     * @param includedPattern   加载配置文件的模式, <b>不要包含配置文件根路径</b>
     * @param excludedPattern   加载配置文件时要排除的模式, <b>不要包含配置文件根路径</b>
     */
    protected void load(String rootConfigurationName, String includedPattern, String excludedPattern) {
        Assert.notNull(includedPattern, "必须指定加载配置文件的模式");

        String[] probed = this.resourceProbe.search(includedPattern, excludedPattern);
        
        List configures = new ArrayList(probed.length);
        configures.addAll(Arrays.asList(probed));
        CollectionUtils.transform(configures, new Transformer() {
            public Object transform(Object element) {
                return new ConfigurationResourceTree((String) element);
            }
        });
        Collections.sort(configures);
        validateConfiguration(rootConfigurationName, configures);
        this.rootBeanFactory = null;
        this.allBeanDefinitions = new LinkedHashMap(configures.size());
        this.leafConfigurations = new ArrayList(configures.size());
        this.allBeanFactories = new HashMap();
        try {
            processConfigures(configures);
        } catch (Exception e) {
            /* todo, release the resources holded by bean factory & one */
            logAndThrows("在分层加载配置信息时", e);
        }
        
        multicaster(new ConfigFinishedEvent());
    }
    
    /**
     * 根据配置分层组织<code>bean factorty</code>
     */
    private void processConfigures(List configures) {
        Map rootBeanPostProcessors = new HashMap(configures.size());
        Map createdBeanFactory = new HashMap(configures.size());
        ConfigurationResourceTree previousNode = null;
        ConfigurableListableBeanFactory previousBeanFactory = null;
        
        for (ListIterator it = configures.listIterator(); it.hasNext(); ) {
            ConfigurationResourceTree curNode = (ConfigurationResourceTree) it.next();
            ConfigurableListableBeanFactory curBeanFactory = new XmlBeanFactoryWithDescription(
                new ClassPathResource(curNode.getResourceFullName()));
            
            /* fail-fast validation */
            ServiceInfo info = validateConfiguration(curBeanFactory, curNode);
            applyBeanFactoryPostProcessors(curBeanFactory);
            allBeanFactories.put(info, curBeanFactory);
            Map beanPostProcessors = curBeanFactory.getBeansOfType(BeanPostProcessor.class);

            if (this.rootBeanFactory == null) {
                this.rootBeanFactory = curBeanFactory;
                rootBeanPostProcessors = beanPostProcessors;
            } else {
                if (!previousNode.hasChild(curNode)) {
                    this.leafConfigurations.add(previousBeanFactory);
                    previousBeanFactory 
                        = fetchNearestParentFactory(createdBeanFactory, configures, curNode);
                }
                
                curBeanFactory.setParentBeanFactory(previousBeanFactory);
            }
            
            applyBeanPostProcessors(curBeanFactory, beanPostProcessors, rootBeanPostProcessors);
            addApplicationListeneres(curBeanFactory);
            
            previousNode = curNode;
            previousBeanFactory = curBeanFactory;
            createdBeanFactory.put(curNode.getResourceFullName(), curBeanFactory);
        }
        
        if (previousBeanFactory != null) {
            this.leafConfigurations.add(previousBeanFactory);
        }
    }

    /**
     * 验证配置文件的正确性
     * @param rootConfigurationName 全路径格式的根配置文件名
     * @param configures            排序后的配置文件<code>ConfigurationResourceTree</code>
     * @throws ConfigurationException 如果没有满足配置要求, 比如在根路径下找到满足查找模式的多个配置文件
     */
    private void validateConfiguration(String rootConfigurationName, List configures) {
        List result = null;
        try {
            result = Collections.list(classLoader.getResources(rootConfigurationName));
        } catch (IOException e) {
            logAndThrows("检索根配置文件[" + rootConfigurationName + "]错误", e);
        }
        
        if (result == null || result.size() == 0) {
            logAndThrows("不能找到根配置文件[" + rootConfigurationName + "]", null);
        }
        
        if (result.size() > 1) {
            logAndThrows("检索到的根配置[" + rootConfigurationName + "]有[" + result.size() + "]个", null);
        }
        
        ConfigurationResourceTree root = (ConfigurationResourceTree) configures.get(0);
        if (!rootConfigurationName.equals(root.getResourceFullName())) {
            logAndThrows("检索到的根配置是[" 
                + root.getResourceFullName() + "],应该是[" + rootConfigurationName + "]", null);
        }
    }
    
    /**
     * 验证<code>bean factory</code>中必须配置的<code>bean</code>
     * @param factory 要验证的<code>bean factory</code>
     * @return 如果验证正确返回这个<code>bean factory</code>的信息<code>bean</code>
     */
    private ServiceInfo validateConfiguration(ConfigurableListableBeanFactory factory, ConfigurationResourceTree node) {
        /* 要求 ServiceInfo 必须是 singleton, 非 FactoryBean, 使用下面的 API 避免在刚开始处理 BeanFactory 时实例化其中的 bean */
        Map map = factory.getBeansOfType(ServiceInfo.class, false, false);
        if (map.size() == 0) {
            logAndThrows("配置[" + node.getResourceFullName() + "]没有定义ServiceInfo", null);
        }
        
        if (map.size() > 1) {
            logAndThrows("配置[" + node.getResourceFullName() + "]定义了多个ServiceInfo", null);
        }
        
        ServiceInfo info = (ServiceInfo) map.values().iterator().next(); 
        if (allBeanFactories.containsKey(info)) {
            logAndThrows("出现同名的配置,名称[" + info.getName() + ",路径是[" + node.getResourceFullName() + "]", null);
        }
        
        return info;
    }
    
    /**
     * 应用当前<code>bean factory</code>中的<code>BeanFactoryPostProcessor</code>
     * @param factory 要配置的<code>bean factory</code>
     */
    private void applyBeanFactoryPostProcessors(ConfigurableListableBeanFactory factory) {
        Map beanFactoryPostProcessors = factory.getBeansOfType(BeanFactoryPostProcessor.class, true, false);
        for (Iterator it = beanFactoryPostProcessors.values().iterator(); it.hasNext(); ) {
            ((BeanFactoryPostProcessor) it.next()).postProcessBeanFactory(factory);
        }
    }
    
    /**
     * 应用根配置的和当前工厂的<code>BeanPostProcessor</code>, 如果后两个参数相同就是正在
     * 配置根<code>bean factory</code>
     * 
     * @param factory                   要配置的<code>bean factory</code>
     * @param beanPostProcessors        应用的当前工厂的<code>BeanPostProcessor</code>
     * @param rootBeanPostProcessors    应用的根工厂的<code>BeanPostProcessor</code>
     */
    private void applyBeanPostProcessors(ConfigurableListableBeanFactory factory, Map beanPostProcessors, Map rootBeanPostProcessors) {
        for (Iterator it = rootBeanPostProcessors.values().iterator(); it.hasNext(); ) {
            Object processor = it.next();
            /* the custom TargetSourceCreator may implements BeanFactoryAware */
            if (ServiceAutoProxyCreator.class.isInstance(processor)) {
                ServiceAutoProxyCreator creator = (ServiceAutoProxyCreator) processor;
                if (creator.getCustomTargetSourceCreator() != null && BeanFactoryAware.class.isInstance(creator.getCustomTargetSourceCreator())) {
                    ((BeanFactoryAware) creator.getCustomTargetSourceCreator()).setBeanFactory(factory);
                }
            }
            factory.addBeanPostProcessor((BeanPostProcessor) processor);
        }
        
        if (beanPostProcessors != rootBeanPostProcessors) {
            for (Iterator it = beanPostProcessors.values().iterator(); it.hasNext(); ) {
                factory.addBeanPostProcessor((BeanPostProcessor) it.next());
            }
        }
    }
    
    /**
     * 注册指定工厂中所有的监听器(实现<code>ApplicationListener</code>接口), 事件源是根配置
     * 中名称为{@link GlobalParameters#APPLICATION_EVENT_MULTICASTER APPLICATION_EVENT_MULTICASTER}
     * 的<code>bean</code>, 这个方法应该在处理过<code>BeanPostProcessor</code>之后进行
     * 
     * @param factory 要检索监听器的<code>bean factory</code>
     */
    private void addApplicationListeneres(ConfigurableListableBeanFactory factory) {
        ApplicationEventMulticaster mc = getApplicationEventMulticaster();
        if (mc != null) {
            Map listeneres = factory.getBeansOfType(ApplicationListener.class);
            for (Iterator it = listeneres.values().iterator(); it.hasNext(); ) {
                mc.addApplicationListener((ApplicationListener) it.next());
            }
        }
    }
    
    void multicaster(ApplicationEvent event) {
        ApplicationEventMulticaster mc = getApplicationEventMulticaster();
        if (mc != null) {
            mc.multicastEvent(event);
        } else {
            if (logger.isInfoEnabled()) {
                logger.info("无法广播事件, 根配置没有注册名册为["
                    + GlobalParameters.APPLICATION_EVENT_MULTICASTER
                    + "]的ApplicationEventMulticaster");
            }
        }
    }
    
    /**
     * 检索根配置中名称是{@link GlobalParameters#APPLICATION_EVENT_MULTICASTER <code>applicationEventMulticaster</code>}
     * 的<code>bean</code>, 这是系统中默认的事件广播
     * 
     * @see net.silencily.sailing.framework.core.coheg.core.GlobalParameters#APPLICATION_EVENT_MULTICASTER
     */
    private ApplicationEventMulticaster getApplicationEventMulticaster() {
        ApplicationEventMulticaster mc = null;
        if (this.rootBeanFactory.containsBean(GlobalParameters.APPLICATION_EVENT_MULTICASTER)) {
            mc = (ApplicationEventMulticaster) this.rootBeanFactory.getBean(
                GlobalParameters.APPLICATION_EVENT_MULTICASTER);
        }
        
        return mc;
    }

    /**
     * 从当前配置向上检索最符合条件的父配置, 因为至少可以找到根配置, 所以如果不能找到任何配置就是
     * 一个程序错误
     * 
     * @param createdFactories 已经创建了的<code>BeanFactory</code>
     * @param nodes            所有的配置节点
     * @param node             要查找父节点的配置节点
     * @return 下一个<code>bean factory</code>的父<code>bean factory</code>
     */
    private ConfigurableListableBeanFactory fetchNearestParentFactory(
        Map createdFactories, List nodes, ConfigurationResourceTree node) {
        ConfigurableListableBeanFactory ret = null;
        
        for (int i = nodes.indexOf(node) - 1; ret == null && i > -1; i--) {
            ConfigurationResourceTree tree = (ConfigurationResourceTree) nodes.get(i);
            if (tree.hasChild(node)) {
                ret = (ConfigurableListableBeanFactory) createdFactories
                    .get(tree.getResourceFullName());
            }
        }
        
        if (ret == null) {
            logAndThrows("没有找到配置信息" + node + "最近的上级配置信息", null);
        }

        return ret;
    }
    
    /**
     * 记录日志, 抛出异常
     * @param msg 错误信息
     * @param e   发生的异常
     */
    private void logAndThrows(String msg, Exception e) {
        if (e != null && StringUtils.isNotBlank(e.getMessage())) {
            msg += ":" + e.getMessage();
        }
        
        ConfigurationException ex = new ConfigurationException(msg);
        if (e != null) {
            ex.initCause(e);
        }
        logger.error(msg, ex);
        throw ex;
    }
    
    /**
     * <code>DefaultListableBeanFactory</code>的<code>xml</code>配置实现, 与<code>
     * XmlBeanFactory</code>所实现的功能完全相同, 只是<code>XmlBeanDefinitionReader</code>
     * 使用了架构自己的对<code>DefaultXmlBeanDefinitionParser</code>的扩展, 以支持<code>
     * bean</code>定义中<code>description</code>的支持
     */
    private static class XmlBeanFactoryWithDescription extends DefaultListableBeanFactory {
        private XmlBeanDefinitionReader reader;
        
        public XmlBeanFactoryWithDescription(Resource resource) {
            this(resource, null);
        }
        
        public XmlBeanFactoryWithDescription(Resource resource, BeanFactory parent) {
            super(parent);
            reader = new XmlBeanDefinitionReader(this);
            //reader.setParserClass(DefaultXmlBeanDefinitionParserWithDescription.class);
            reader.loadBeanDefinitions(resource);
        }
    }

    /**
     * <code>springframework's DefaultXmlBeanDefinitionParser</code>的扩展, 应用于
     * <code>bean</code>配置的扩展描述, 增加了保存<code>description</code>元素的内容,这
     * 些内容对于架构的除了<code>EJB's REQUIRED</code>事务属性外其他属性的声明, 动态脚本技
     * 术(比如<code>BeanShell</code>)支持. 如果一个<code>bean</code>定义了这种描述, 那么
     * <code>BeanDefinition's ResourceDescription</code>就是<code>description</code>
     * 的内容, 否则仍然是本身的<code>resourceDescription</code>
     */
//    public static class DefaultXmlBeanDefinitionParserWithDescription extends DefaultXmlBeanDefinitionParser {
//        public DefaultXmlBeanDefinitionParserWithDescription() {
//            super();
//        }
//        
//        protected BeanDefinition parseBeanDefinitionElement(Element ele, String beanName) {
//            AbstractBeanDefinition definition = (AbstractBeanDefinition) super.parseBeanDefinitionElement(ele, beanName);
//            NodeList nodes = ele.getChildNodes();
//            for (int i = 0; nodes != null && (i < nodes.getLength()); i++) {
//                if (nodes.item(i) instanceof Element) {
//                    Element element = (Element) nodes.item(i);
//                    if (DESCRIPTION_ELEMENT.equals(element.getNodeName())) {
//                        definition.setResourceDescription(fetchDescriptionContent(element));
//                    }
//                }
//            }
//            
//            return definition;
//        }
//        
//        private String fetchDescriptionContent(Element element) {
//            NodeList nodes = element.getChildNodes();
//            String retVal = null;
//            
//            for (int i = 0; i < nodes.getLength(); i++) {
//                if (nodes.item(i) instanceof CDATASection) {
//                    retVal = ((CDATASection) nodes.item(i)).getData();
//                    break;
//                }
//            }
//            
//            return retVal == null ? "" : retVal;
//        }
//    }
    
    /**
     * <p>检索定义在根配置中的基础服务, 通常有事件广播, 客户化信息, 异常处理器等公用组件, 如果
     * 不能获得这些组件正常来讲是一个配置错误</p>
     * 
     * @param name  要检索的基础服务组件名称
     * @return      基础服务组件
     * @throws      IllegalArgumentException 如果参数是<code>null</code>或<code>empty</code>
     * @throws      ConfigurationException   如果没有找到要检索的组件
     */
    Object getBeanFromRootConfiguration(String name) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("没有指定要检索的服务名称");
        }
        
        if (!this.rootBeanFactory.containsBean(name)) {
            throw new ConfigurationException("没有在根配置中定义名称是[" + name + "]的服务");
        }
        
        return this.rootBeanFactory.getBean(name);
    }

}
