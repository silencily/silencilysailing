package net.silencily.sailing.resource;

/**
 * ����·����Ŀ¼�»�<code>jar</code>�ļ�������ָ�����Ƶ���Դ, ��Դ���ƿ��԰���<code>ant</code>
 * ����ƥ��ģʽָ��, ����:
 * <pre>
 *   <code>/com/88/person*</code> --> com/coheg/person.xml, com/power/personal, etc.
 * </pre>
 * ��Щ��Դ��������<class>ClassLoader.getResources()</class>������Դ������, ��Ҫ��������·��
 * ����Դ�в�����Դ
 *  
 * @author scottcaptain
 * @since 2005-12-20
 * @version $Id: ResourceProbe.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 */
public interface ResourceProbe {
    
    /**
     * �ж�һ����Դ�����Ƿ���һ��<code>ant-style</code>��ƥ��ģʽ
     * 
     * @param resourceName Ҫ�жϵ���Դ����
     * @return �����<code>ant-style</code>ƥ��ģʽ����<code>true</code>, ���ڿ�ֵ����
     * <code>false</code>
     */
    boolean isPattern(String resourceName);
        
    /**
     * ����ָ��ģʽ����ƥ�����Դ��, ��Щ��Դ���ƿ���ʹ��<code>ClassLoader</code>
     * ����, ����ǰ���<code>String</code>�ıȽ�˳�������
     * 
     * @param resourceName Ҫ��������Դ����ģʽ, ��������","�ָ��Ķ��ģʽ
     * @return ����������Դ��������, ���û�ҵ���Դ���س���Ϊ<b>0</b>������
     * @throws IllegalArgumentException ���������<code>null</code>��<code>empty</code>
     */
    String[] search(String resourceName);
    
    /**
     * ����ָ��ģʽ����ƥ�����Դ��, ��Щ��Դ���ƿ���ʹ��<code>ClassLoader</code>����, ���
     * �ǰ�����ĸ�Ƚ�˳�������, �ڶ�����������ָ���ų�ģʽ, ���Ƿ������ģʽ����Դ�����ų���
     * 
     * @param resourceName Ҫ��������Դ����ģʽ, ��������","�ָ��Ķ��ģʽ
     * @param elimination  �ӽ�����ų�����Դģʽ, ��������","�ָ��Ķ��ģʽ
     * @return ����������Դ��������, ���û�ҵ���Դ���س���Ϊ<b>0</b>������
     * @throws IllegalArgumentException ���������<code>null</code>��<code>empty</code>
     */
    String[] search(String resourceName, String elimination);
}
