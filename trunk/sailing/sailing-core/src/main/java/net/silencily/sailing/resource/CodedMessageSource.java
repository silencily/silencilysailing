package net.silencily.sailing.resource;

import net.silencily.sailing.framework.exception.NoSuchMessageException;



/**
 * <p>����ӿ�ʹ�ñ������д�������ļ��е���Ϣ, ʵ������Ϣ�����ķ���, Ŀ���������쳣��
 * Ϣ, ��ʾ��Ϣ�ȿͻ�����Ϣ. �����ļ��Ǳ�׼��<code>java.util.Properties</code>�ļ��ĸ�ʽ,
 * ������Ҫ������ת��(escape)����, ���е������ļ�<b>һ��</b>Ҫ��<code>conf</code>��·����,
 * �����ļ���һ��Ҫƥ��{@link GlobalParameters#MESSAGE_CODED_CONFIGURATION_LOCATION<code>��׺��</code>}���ģʽ. ������Ϣ��
 * ����Ŀ¼��ηֲ���ص�, Ŀ¼�ṹӦ�����ֳ��ܹ�����Ӧ����ϵͳ���߼��ṹ, һ�����ò�ε���֯����
 * ����������
 * <pre>
 *   + conf
 *     - root-messagecoded.properties
 *     + oa
 *       - oa-messagecoded.properties
 *       + senddoc
 *         - oa-senddoc-messagecoded.properties
 *       + receivedoc
 *         - oa-receive-messagecoded.properties
 *   ...
 * </pre>
 * ���ͬһ�����ó�����ͬ<code>key</code>����Ϣ�Ͱ����ļ�������Ȼ˳��(Ӣ����ĸ˳��, �ļ�����Ȼ
 * Ӧ����Ӣ����ĸ������)�ȼ��ص����κ���ص�, �������ͬ��Ŀ¼����������ͻ, ��һ��������һ����ͬ��
 * ��Ϣ, ��������Ŀ���Ƿ�ֹ�²�ӽ�����ҵ����ϵͳ���ǵ��Ѿ�ʹ�õ���ϵͳ����Ϣ</p>
 * <p>ʵ��Ӧ�û����Ѿ����ص���Ϣ, ���ڲ���ʲô����ˢ�»���Ӧ�������������ڵ�����������</p>
 * 
 * @author scottcaptain
 * @since 2005-12-19
 * @version $Id: CodedMessageSource.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 */
public interface CodedMessageSource {
    String SERVICE_NAME = "CodedMessageSource";
    
    /**
     * ����ָ���������Ϣ, ��������������ʽ���ʹ�ÿɶ��Ժõ��ַ���, ������ʾ��Ա��Ϣ�ı����
     * <code>user.folk</code>
     * 
     * @param code Ҫ������Ϣ��<code>code</code>
     * @return �����Ӧ����Ϣ
     * @throws IllegalArgumentException ���������<code>null</code>��<code>empty</code>
     * @throws NoSuchMessageException ���û���ҵ���Ӧ����Ϣ
     */
    String getMessage(String code);

    /**
     * ����ָ���������Ϣ, ��������������ʽ���ʹ�ÿɶ��Ժõ��ַ���, ������ʾ��Ա��Ϣ�ı����
     * <code>user.folk</code>
     * 
     * @param code Ҫ������Ϣ��<code>code</code>
     * @return �����Ӧ����Ϣ, ���û���ҵ���Ϣ, ���صڶ�������, �������������ֵ��ʲô
     * @throws IllegalArgumentException ���������<code>null</code>��<code>empty</code>
     */    
    String getMessage(String code, String defaultMsg);
    
    /**
     * ����ָ���������Ϣ, ʹ�ò����滻ռλ��, �滻����μ�{@link java.text.MessageFormat MessageFormat}
     * ��
     * 
     * @param code
     * @param args
     * @return ��������Ϣ
     * @throws IllegalArgumentException ���������<code>null</code>��<code>empty</code>
     * @throws NoSuchMessageException ���û���ҵ���Ӧ����Ϣ
     */
    String getMessage(String code, Object[] args);
    
    /**
     * ����ָ���������Ϣ, ʹ�ò����滻ռλ��, �滻����μ�{@link java.text.MessageFormat MessageFormat}
     * ��
     * 
     * @param code
     * @param args
     * @return ��������Ϣ, ���û�ҵ���Ӧ����Ϣ, ����ȱʡ����Ϣ
     * @throws IllegalArgumentException ���������<code>null</code>��<code>empty</code>
     */
    String getMessage(String code, Object[] args, String defaultMessage);
    
    /**
     * ����Ƿ����ָ���������Ϣ, �����������ϸ�µؿ��Ƴ������Ϣ, ���ֱ��ʹ��{@link #getMessage(String) getMessage}
     * , ��û��Ҫ������<code>code</code>ʱ���׳��쳣, ʹ������������Ա����������
     * 
     * @param code Ҫ����Ƿ����<code>code</code>��ֵ
     * @return ��������������ⲿ����Ϣ����<code>true</code>, ����<code>false</code>
     */
    boolean exists(String code);
}
