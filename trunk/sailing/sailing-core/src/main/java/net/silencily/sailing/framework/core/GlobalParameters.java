package net.silencily.sailing.framework.core;

/**
 * ������ĵ����ò���, ��Щ���������˺��ĵ���Ʒ���, ��Դ���·���Ͳ�ι�ϵ, ������Ӧ���з����仯
 * 
 * @author scott
 * @since 2005-12-24
 * @version $Id: GlobalParameters.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 *
 */
public interface GlobalParameters {
    
    /**
     * ϵͳ��������Դ��λ��, ���е�������Ϣ���������λ����
     */
    String CONFIGURATION_LOCATION = "conf";
    
    /**
     * ϵͳ�и�����·��������, ����ļ�������ϵͳ�н�����һ��. ע��û����Դ���ø�Ŀ¼
     */
    String ROOT_CONFIGURATION_NAME = CONFIGURATION_LOCATION + "/system-configuration.xml";
    
    /**
     * ��������ϵͳ�������ļ������������ļ���ģʽ, ����˵ֻ�и������ļ�����ֱ����
     * {@link #CONFIGURATION_LOCATION CONFIGURATION_LOCATION}��, ����
     * ���������λ���µ�����һ����༶Ŀ¼��, ���Һ�׺������<code>-configuration.xml</code>
     */
    String CONFIGURATION_PATTERN = CONFIGURATION_LOCATION + "/**/*-configuration.xml";
    
    /**
     * �ܹ����ؿͻ���(�ⲿ��)��Ϣ��Դ��ģʽ, ������Դ�ļ���������<code>Properties</code>����
     * ��ʽ, ��Щ��Դ��Ҫ���ڴ�����ʾ��Ϣ, ��ʾ��Ϣ����Ϣ����
     */
    String MESSAGE_PATTERN = CONFIGURATION_LOCATION + "/**/*-messages.properties";
    
    /**
     * �ܹ�����������е�����, Ҳ����ǰ׺
     */
    String MODULE_NAME = "system";
    
    /**
     * �ܹ��е�ϵͳ���¼��㲥���������, ����ʵ����<code>ApplicationListener</code>�ӿڵ�
     * ��������Խ��ܵ�֪ͨ
     */
    String APPLICATION_EVENT_MULTICASTER = MODULE_NAME + "." + "applicationEventMulticaster";
    
    /**
     * ϵͳ�пͻ�����Ϣ��Դ��������<code>bean</code>��������, ��ϵͳ����ʱ���������������Ϣ
     * ��Դ
     */
    String APPLICATION_CODED_MESSAGE = MODULE_NAME + "." + "codedMessageSource";
    
    /**
     * ϵͳ�д����쳣��Ϣ���쳣�����������
     */
    String APPLICATION_EXCEPTION_HANDLER = MODULE_NAME + "." + "exceptionHandler";

}
