package net.silencily.sailing.framework.persistent.search;

import net.silencily.sailing.framework.persistent.filter.Paginater;

/**
 * �Ѳ�ѯ�����ϲ���<code>sql</code>����<code>where</code>����
 * @author scott
 * @since 2006-4-23
 * @version $Id: SqlPopulator.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 *
 */
public interface SqlPopulator {
    /**
     * �������ϲ�<code>sql</code>���Ͳ���
     * @param sql   Ҫ�ϲ���<code>sql</code>
     * @param sp    ����
     * @return      �ϲ����<code>SearchCondtionAndParameter</code>, ��������е�
     *              <code>condition</code>�������Ѿ��ϲ����<code>sql</code>���, 
     *              <code>params</code>���ּ����������Ĳ���
     */
    SearchConditionAndParameter where(String sql, SearchConditionAndParameter sp);
    
    /**
     * ��<code>sql</code>�Ļ�����ʵ�ַ�ҳ����
     * @param sql   Ҫ�ϲ���<code>sql</code>
     * @param sp    ����
     * @return      �ϲ����<code>SearchCondtionAndParameter</code>, ��������е�
     *              <code>condition</code>�������Ѿ��ϲ����<code>sql</code>���, 
     *              <code>params</code>���ּ����������Ĳ���
     */
    SearchConditionAndParameter pagination(Paginater paginater, SearchConditionAndParameter sp);
    
    /**
     * ��ԭ����<code>update</code>���������Ҫ���µ��м�����
     * @param sql Ҫ�޸ĵ�<code>update</code>���
     * @param sp  Ҫ���ӵĸ������
     * @return �޸ĺ��<code>sql</code>������
     */
    SearchConditionAndParameter update(String sql, SearchConditionAndParameter sp);
    
    /**
     * ����ͳ�����������ļ�¼����<code>SearchConditionAndParameter</code>, ���е�<code>
     * condition</code>�Ǽ���������<code>sql</code>, <code>params</code>��Ȼ��ԭ���Ĳ���
     * @param paginater ���ü�¼������{@link Paginater <code>Paginater</code>}
     * @param sp        <code>sql</code>������������
     * @return          ������¼������<code>sql</code>�Ͳ���
     */
    SearchConditionAndParameter count(Paginater paginater, SearchConditionAndParameter sp);
}
