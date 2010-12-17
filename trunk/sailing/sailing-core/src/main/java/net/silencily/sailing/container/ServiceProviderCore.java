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
 * <p><code>ServiceProvider</code>�ĺ�����, ʵ�ʵĴ�����������, ����౾����������<code>
 * Singleton</code>�����������÷�, ��ҪĿ���Ǳ���ʹ��<code>static</code>���������Ե��鷳,
 * ���ڷ������, ������Ӧ�ô���</p>
 * <p>���������, ����ϵͳ��ͨ�õ������д�ڸ������ļ���, ��������,����Դ��, ֻҪ��������ϵͳ�����
 * �ϲ���һ��ʱ, �˴�֮��Ϳ���Э��, û��ʹ��<code>ApplicationContext</code>��ԭ���������,
 * ��Ϊ�������<code>bean</code>�Ѿ������ʵ�����Ĺ���</p>
 * <p>����ײ��������Ĺ���:<ul>
 * <li><code>BeanFactoryPostProcessor</code>, �������һ��Ҫ���ⲻ��Ҫ������ͬһ��������
 * ���������, ���ⲻ��Ҫ��ʵ����. ÿ�����õ����������������Ӧ��, �����´���</li>
 * <li><code>BeanPostProcessor</code>, ��������ڴ���<code>bean factory</code>��Ҫʵ��
 * ��, ����, �����õ�<code>bean</code>���´���</li>
 * <li>����������ɺ�, ��ʼ������������¼�</li>
 * <ul></p>
 * <p>�������������е�<code>protected</code>�ķ��������Զ���Ϊ�˷������, û�����ڼ̳е���ͼ</p>
 * 
 * @author scott
 * @since 2006-3-16
 * @version $Id: ServiceProviderCore.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 * @see com.coheg.container.support.ConfigurationResourceTree
 * @see com.coheg.resource.ResourceProbe
 */
public class ServiceProviderCore {
    /**
     * ����<code>log</code>���ù��̵���Ϣ, ע�⿪��<code>log</code>ʱʹ�������
     */
    private final Log logger = LogFactory.getLog(ServiceProviderCore.class);
    
    /**
     * ���ڲ���ƥ����Դ����Դ������, û��ʹ��<code>springframework's ResourceLoader</code>
     * ��ԭ�����޷���<code>Resource</code>�н�����<code>class path</code>, ��������������
     * Ҫ��
     */
    private ResourceProbe resourceProbe = new AntResourceProbe();
    
    /**
     * ���ڼ������õ��������
     */
    private ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    
    /**
     * ��<code>bean factory</code>
     */
    protected ConfigurableListableBeanFactory rootBeanFactory;
    
    /**
     * λ���������ṹ�е�Ҷ�ӽڵ������, ���е�Ԫ����<code>ConfigurableListableBeanFactory</code>,
     * ���ڰ�������������һ��<code>bean</code>
     * @see {@link org.springframework.beans.factory.BeanFactory#getBean(java.lang.String) getBean}
     * ����
     */
    protected List leafConfigurations;

    /**
     * ϵͳ�����е�<code>bean</code>�Ķ���, <code>Map's key</code>��<code>bean name</code>,
     * <code>Map's value</code>��<code>BeanDefinition</code>����չ, ������<code>bean definition</code>
     * ��<code>description</code>����
     */
    protected Map allBeanDefinitions;
    
    /**
     * ϵͳ�����е�<code>bean factory</code>, <code>Map's key</code>��������Ϣ<code>
     * ServiceInfo</code>, <code>Map's value</code>����Ӧ��<code>bean factory</code>,
     * ���а�����<code>bean factory</code>
     */
    protected Map allBeanFactories;

    public void setResourceProbe(ResourceProbe resourceProbe) {
        this.resourceProbe = resourceProbe;
    }

    /**
     * ����ϵͳ�����еķ�������, ����ԭ����<ul>
     * <li>���ظ������ļ�, ���û���ҵ��������ļ����ҵ�һ�����ϵ������ļ�, �׳��쳣</li>
     * <li>����ƥ��ģʽ���ų�ģʽ�ֲ��������������, �ֲ��ԭ������
     * {@link com.coheg.container.support.ConfigurationResourceTree ���ýڵ�}����</li></ul>
     * <li>��ʼ�����е�<code>BeanFactoryPostProcessor</code></li>
     * <li>ע�����е�<code>BeanPostProcessor</code></li>
     * <li>ע��ϵͳ���¼��㲥, �������ý���ʱ������������¼�</li>
     * <li>���������û�а���<code>SerivceInfo</code>��û�ж������ƻ������ظ��׳������쳣</li></ul>
     * 
     * @param rootConfigurationName �������ļ�����, <b>��Ҫ���������ļ���·��</b>
     * @param includedPattern   ���������ļ���ģʽ, <b>��Ҫ���������ļ���·��</b>
     * @param excludedPattern   ���������ļ�ʱҪ�ų���ģʽ, <b>��Ҫ���������ļ���·��</b>
     */
    protected void load(String rootConfigurationName, String includedPattern, String excludedPattern) {
        Assert.notNull(includedPattern, "����ָ�����������ļ���ģʽ");

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
            logAndThrows("�ڷֲ����������Ϣʱ", e);
        }
        
        multicaster(new ConfigFinishedEvent());
    }
    
    /**
     * �������÷ֲ���֯<code>bean factorty</code>
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
     * ��֤�����ļ�����ȷ��
     * @param rootConfigurationName ȫ·����ʽ�ĸ������ļ���
     * @param configures            �����������ļ�<code>ConfigurationResourceTree</code>
     * @throws ConfigurationException ���û����������Ҫ��, �����ڸ�·�����ҵ��������ģʽ�Ķ�������ļ�
     */
    private void validateConfiguration(String rootConfigurationName, List configures) {
        List result = null;
        try {
            result = Collections.list(classLoader.getResources(rootConfigurationName));
        } catch (IOException e) {
            logAndThrows("�����������ļ�[" + rootConfigurationName + "]����", e);
        }
        
        if (result == null || result.size() == 0) {
            logAndThrows("�����ҵ��������ļ�[" + rootConfigurationName + "]", null);
        }
        
        if (result.size() > 1) {
            logAndThrows("�������ĸ�����[" + rootConfigurationName + "]��[" + result.size() + "]��", null);
        }
        
        ConfigurationResourceTree root = (ConfigurationResourceTree) configures.get(0);
        if (!rootConfigurationName.equals(root.getResourceFullName())) {
            logAndThrows("�������ĸ�������[" 
                + root.getResourceFullName() + "],Ӧ����[" + rootConfigurationName + "]", null);
        }
    }
    
    /**
     * ��֤<code>bean factory</code>�б������õ�<code>bean</code>
     * @param factory Ҫ��֤��<code>bean factory</code>
     * @return �����֤��ȷ�������<code>bean factory</code>����Ϣ<code>bean</code>
     */
    private ServiceInfo validateConfiguration(ConfigurableListableBeanFactory factory, ConfigurationResourceTree node) {
        /* Ҫ�� ServiceInfo ������ singleton, �� FactoryBean, ʹ������� API �����ڸտ�ʼ���� BeanFactory ʱʵ�������е� bean */
        Map map = factory.getBeansOfType(ServiceInfo.class, false, false);
        if (map.size() == 0) {
            logAndThrows("����[" + node.getResourceFullName() + "]û�ж���ServiceInfo", null);
        }
        
        if (map.size() > 1) {
            logAndThrows("����[" + node.getResourceFullName() + "]�����˶��ServiceInfo", null);
        }
        
        ServiceInfo info = (ServiceInfo) map.values().iterator().next(); 
        if (allBeanFactories.containsKey(info)) {
            logAndThrows("����ͬ��������,����[" + info.getName() + ",·����[" + node.getResourceFullName() + "]", null);
        }
        
        return info;
    }
    
    /**
     * Ӧ�õ�ǰ<code>bean factory</code>�е�<code>BeanFactoryPostProcessor</code>
     * @param factory Ҫ���õ�<code>bean factory</code>
     */
    private void applyBeanFactoryPostProcessors(ConfigurableListableBeanFactory factory) {
        Map beanFactoryPostProcessors = factory.getBeansOfType(BeanFactoryPostProcessor.class, true, false);
        for (Iterator it = beanFactoryPostProcessors.values().iterator(); it.hasNext(); ) {
            ((BeanFactoryPostProcessor) it.next()).postProcessBeanFactory(factory);
        }
    }
    
    /**
     * Ӧ�ø����õĺ͵�ǰ������<code>BeanPostProcessor</code>, ���������������ͬ��������
     * ���ø�<code>bean factory</code>
     * 
     * @param factory                   Ҫ���õ�<code>bean factory</code>
     * @param beanPostProcessors        Ӧ�õĵ�ǰ������<code>BeanPostProcessor</code>
     * @param rootBeanPostProcessors    Ӧ�õĸ�������<code>BeanPostProcessor</code>
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
     * ע��ָ�����������еļ�����(ʵ��<code>ApplicationListener</code>�ӿ�), �¼�Դ�Ǹ�����
     * ������Ϊ{@link GlobalParameters#APPLICATION_EVENT_MULTICASTER APPLICATION_EVENT_MULTICASTER}
     * ��<code>bean</code>, �������Ӧ���ڴ����<code>BeanPostProcessor</code>֮�����
     * 
     * @param factory Ҫ������������<code>bean factory</code>
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
                logger.info("�޷��㲥�¼�, ������û��ע������Ϊ["
                    + GlobalParameters.APPLICATION_EVENT_MULTICASTER
                    + "]��ApplicationEventMulticaster");
            }
        }
    }
    
    /**
     * ������������������{@link GlobalParameters#APPLICATION_EVENT_MULTICASTER <code>applicationEventMulticaster</code>}
     * ��<code>bean</code>, ����ϵͳ��Ĭ�ϵ��¼��㲥
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
     * �ӵ�ǰ�������ϼ�������������ĸ�����, ��Ϊ���ٿ����ҵ�������, ������������ҵ��κ����þ���
     * һ���������
     * 
     * @param createdFactories �Ѿ������˵�<code>BeanFactory</code>
     * @param nodes            ���е����ýڵ�
     * @param node             Ҫ���Ҹ��ڵ�����ýڵ�
     * @return ��һ��<code>bean factory</code>�ĸ�<code>bean factory</code>
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
            logAndThrows("û���ҵ�������Ϣ" + node + "������ϼ�������Ϣ", null);
        }

        return ret;
    }
    
    /**
     * ��¼��־, �׳��쳣
     * @param msg ������Ϣ
     * @param e   �������쳣
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
     * <code>DefaultListableBeanFactory</code>��<code>xml</code>����ʵ��, ��<code>
     * XmlBeanFactory</code>��ʵ�ֵĹ�����ȫ��ͬ, ֻ��<code>XmlBeanDefinitionReader</code>
     * ʹ���˼ܹ��Լ��Ķ�<code>DefaultXmlBeanDefinitionParser</code>����չ, ��֧��<code>
     * bean</code>������<code>description</code>��֧��
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
     * <code>springframework's DefaultXmlBeanDefinitionParser</code>����չ, Ӧ����
     * <code>bean</code>���õ���չ����, �����˱���<code>description</code>Ԫ�ص�����,��
     * Щ���ݶ��ڼܹ��ĳ���<code>EJB's REQUIRED</code>�����������������Ե�����, ��̬�ű���
     * ��(����<code>BeanShell</code>)֧��. ���һ��<code>bean</code>��������������, ��ô
     * <code>BeanDefinition's ResourceDescription</code>����<code>description</code>
     * ������, ������Ȼ�Ǳ����<code>resourceDescription</code>
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
     * <p>���������ڸ������еĻ�������, ͨ�����¼��㲥, �ͻ�����Ϣ, �쳣�������ȹ������, ���
     * ���ܻ����Щ�������������һ�����ô���</p>
     * 
     * @param name  Ҫ�����Ļ��������������
     * @return      �����������
     * @throws      IllegalArgumentException ���������<code>null</code>��<code>empty</code>
     * @throws      ConfigurationException   ���û���ҵ�Ҫ���������
     */
    Object getBeanFromRootConfiguration(String name) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("û��ָ��Ҫ�����ķ�������");
        }
        
        if (!this.rootBeanFactory.containsBean(name)) {
            throw new ConfigurationException("û���ڸ������ж���������[" + name + "]�ķ���");
        }
        
        return this.rootBeanFactory.getBean(name);
    }

}
