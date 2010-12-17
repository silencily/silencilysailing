package net.silencily.sailing.resource;

import java.util.Map;

/**
 * <p>��ϵͳ����ʱ��Ҫִ�г�ʼ�������������Ҫʵ������ӿ�, ��õ��龰���²���һ����ϵͳ��Ӧ��
 * ����ʱ��ʼ����Դ, �����������λ�÷��õ�<code>vfs</code>�е���Դ, �������ļ�ϵͳ��������
 * Ϣд�����ݿ���, ĳЩ��ȫ���ݵĳ�ʼ����</p>
 * <p>�����еĻ���<code>spring framework</code>�ļܹ���, ����ӿڵ�ʵ������Ϊ��ϵͳ����
 * ������<code>ServiceInfo</code>������, ����ʹ��<code>FQN</code>, �Ա���ʵ����<code>
 * ServiceInfo</code>ʱ�����ĳ�ʼ��</p>
 * @author Scott Captain
 * @since 2006-5-17
 * @version $Id: InitializingResource.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 *
 */
public interface InitializingResource {
    
    /**
     * Ӧ������ʱ���ѯ�������Ƿ���Ҫִ�г�ʼ������
     * @return �����Ҫ����<code>true</code>, ����<code>false</code>
     */
    boolean requireInitialization();
    
    /**
     * ��Ӧ������ʱ�����ѯ{@link #requireInitialization() <code>requireInitialization</code>}
     * ��������<code>true</code>��ִ�г�ʼ������
     * @param parameters ִ�г�ʼ��ʱ����ʹ�õ������Ĳ���, <code>key</code>��������, 
     *                   <code>value</code>�����ʵ��, ���������Ҫ<code>javax.servlet.ServletContext</code>
     *                   ,��ô<code>key</code>����"ServletContext", ���<code>key</code>
     *                   ��Ӧ��ʵ������<code>ServletContext</code>��ʵ��
     */
    void initialize(Map parameters);
}
