package net.silencily.sailing.framework.persistent.search;

import java.util.List;

/**
 * ����һ��<code>domain object</code>�ڳ־û����Ԫ��Ϣ, ͨ�������ݿ�ı����Ƽ�������������
 * �Ķ�Ӧ��ϵ, ÿһ��<code>domain object</code>����һ�����ʵ����֮��Ӧ, Ҫע�⵽<code>
 * domain object</code>��һЩ���Ա�����ʵ�����͵�ֵ, ����<code>listStatement</code>
 * @author scott
 * @since 2006-4-19
 * @version $Id: MetadataHolder.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 *
 */
public interface MetadataHolder {
    
    /**
     * �����ṩ��Ϣ��<code>entry</code>������
     * @return
     */
    Class getDomainType();
    
    /**
     * ���������Ʒ�����Ӧ����������
     * @param columnName ���ݿ���������
     * @return ����ж�Ӧ����������, ���Է���<code>null</code>��ʾû��������֮��Ӧ
     */
    String getPropertyName(String columnName);
    
    /**
     * �����������Ʒ�����Ӧ��������
     * @param propertyName <code>domain object</code>����������
     * @return ������Զ�Ӧ��������, ���Է���<code>null</code>��ʾû������֮��Ӧ
     */
    String getColumnName(String propertyName);

    /**
     * ���ش����ݿ��е�һ����װ<code>domain object</code>ʱҪ��װ����������, ֻ��������б�
     * �е����Բ���װ, <b>���������<code>EMPTY_LIST</code>����װ���е�����</b>
     * @return Ҫ��װ��<code>domain object</code>������
     */
    List getProperties();
    
    /**
     * ���ؼ����������ݵ�<code>select</code>(����ȵ����, ����<code>HSQL:select * from userDto
     * </code>,ͨ���ڷ�������ж���Ҫ�������<code>API</code>, ����������ṩ����֧��. <b>�������
     * ����в�Ҫ���ֲ���ռλ��</b>
     * @return �����������ݵ�<code>select</code>���
     */
    String getQueryStatement();

}
