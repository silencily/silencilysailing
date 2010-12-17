package net.silencily.sailing.container;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationEvent;

import net.silencily.sailing.exception.BaseExceptionHandler;
import net.silencily.sailing.framework.core.GlobalParameters;
import net.silencily.sailing.resource.CodedMessageSource;

public class ServiceProvider {
    private static ServiceProviderCore core;
    
    static {
        core = new ServiceProviderCore();
        String excludedPattern = GlobalParameters.CONFIGURATION_LOCATION + "/**/test/**/*";
        core.load(
            GlobalParameters.ROOT_CONFIGURATION_NAME, 
            GlobalParameters.CONFIGURATION_PATTERN, 
            excludedPattern);
    }
    
    /**
     * ����ָ�����Ƶķ���, ���Ʊ�������<code>��������.��������</code>�Ĺ淶, ������ƾ�������
     * �������ļ��е�<code>id</code>����.
     * @param name Ҫ�����ķ�������
     * @return ָ�����Ƶķ���, ������<code>null</code>
     * @throws IllegalArgumentException ���ָ�����Ƶķ��񲻴���
     */
    public static Object getService(String name) {
        if (StringUtils.isBlank(name)) {
            throw new NullPointerException("��������ʱ�������ǿ�[" + name + "]");
        }
        
        Object bean = null;
        for (Iterator it = core.leafConfigurations.iterator(); bean == null && it.hasNext(); ) {
            BeanFactory beanFactory = (BeanFactory) it.next();
            /* Ϊ��Ч��, û�е��� hasService(String) ���� */
            if (beanFactory.containsBean(name)) {
                bean = beanFactory.getBean(name);
            }
        }
        
        if (bean == null) {
            throw new IllegalArgumentException("û�ж�������[" + name + "]�ķ���");
        }
        
        return bean;
    }
    
    /**
     * ��ѯ�Ƿ���ָ�����Ƶķ���
     * @param name Ҫ��ѯ�ķ�����
     * @return ���������������Ƶķ��񷵻�<code>true</code>, ����<code>false</code>
     * @throws NullPointerException ���������<code>empty</code>��<code>null</code>
     */
    public static boolean hasService(String name) {
        if (StringUtils.isBlank(name)) {
            throw new NullPointerException("��������ʱ�������ǿ�[" + name + "]");
        }
        
        boolean hasSpecifiedService = false;
        for (Iterator it = core.leafConfigurations.iterator(); !hasSpecifiedService && it.hasNext(); ) {
            BeanFactory beanFactory = (BeanFactory) it.next();
            hasSpecifiedService = beanFactory.containsBean(name);
        }
        
        return hasSpecifiedService;
    }
    /**
     * <p>�㲥�¼�, �Ա�ʹ����Ȥ������ܹ����յ�֪ͨ, �������б���ע��������{@link GlobalParameters#APPLICATION_EVENT_MULTICASTER <code>APPLICATION_EVENT_MULTICASTER</code>}
     * ��<code>ApplicationEventMulticaster</code>���</p>
     * 
     * @param event Ҫ�㲥���¼�, ��<code>ApplicationEvent</code>������
     * @throws NullPointerException �������<code>event</code>��<code>null</code>
     */
    public static void multicaster(ApplicationEvent event) {
        if (event == null) {
            throw new NullPointerException("�㲥�¼�ʱ��Ϊ�¼��Ĳ�����null");
        }
        
        core.multicaster(event);
    }
    
    /**
     * �����ͻ�����Ϣ����, �����������Լ�����Ԥ�����õ���Ϣ. ��������Ǽܹ����õ�, ��������
     * ���׳��쳣������������
     * @return �ͻ�����Ϣ����
     */
    public static CodedMessageSource getCodedMessageSource() {
        return (CodedMessageSource) core.getBeanFromRootConfiguration(GlobalParameters.APPLICATION_CODED_MESSAGE);
    }
    
    public static BaseExceptionHandler getExceptionHandler() {
        return (BaseExceptionHandler) core.getBeanFromRootConfiguration(GlobalParameters.APPLICATION_EXCEPTION_HANDLER);
    }

    /**
     * ���ڼܹ���<code>utility</code>��ķ���, ������Ӧ�ô���
     * @return ϵͳ���ĵ����ù�����
     */
    static ServiceProviderCore getServiceProviderCore() {
        return core;
    }
}
