package net.silencily.sailing.framework.business;

/**
 * �ܹ���һ���ص��ӿ�, ������ϵͳ����ʱִ��һЩ��ʼ������, ����Ԥ�ȴ���һЩ����������Ա�֤����
 * ��������ִ��, ������ξ�����һ����ϵͳ��һ��Ͷ��ʹ��ʱ��ʼ��ϵͳ�б�Ҫ�ı���. ����糧�е�ר
 * ҵ����, �򵥵�˵һ����ϵͳ��һ������Ҫ��ʼͶ����������Ҫ�߱�ʲô����, ����ͨ������ӿ���ʵ��
 * , ʵ��������ӿڵķ���ܹ���֤�ڳ�ʼ���߳̽���֮ǰ���ܵ������������κη���, ������ӿڵĵ�
 * �÷���������ϵͳ��ʼ���������֮��, ����<code>spring framework bean factory</code>��ʼ
 * ������׼��Ͷ��ʹ��
 * 
 * @author zhangli
 * @version $Id: Initializable.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 * @since 2007-6-4
 */
public interface Initializable {
    
    /**
     * ������������, ״̬�Ѿ���ʼ������? ����ǾͲ�ִ�г�ʼ������, ����ִ�г�ʼ������
     * @return �����ʼ����ɷ���<code>true</code>, ������<code>false</code>
     */
    boolean isInitialized();
    
    /**
     * ϵͳ��ʼ������������ʼ�����������, ����ڳ�ʼ�������з���������ô�����������з�������
     * ����
     * @exception InitializableException ����ڳ�ʼ�������з�������
     * @exception InitializableInProcessException ����ڳ�ʼ�������жԷ���ķ������е���
     */
    void initialize() throws InitializableException, InitializableInProcessException;
}
