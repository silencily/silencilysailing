package net.silencily.sailing.framework.persistent.search;

/**
 * ��ʾִ��һ���ض���ѯʱ�ļ�������, ��������ɶ��<code>SearchCondition</code>���, ͨ��
 * <code>toString</code>��������ת�����ض��־û����Ե�<code>where</code>����
 * @author scott
 * @since 2006-4-18
 * @version $Id: SearchConditions.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 *
 */
public interface SearchConditions {
    /** ���ɵļ���������Ҫ�滻������ռλ��, ����<code>JDBC</code>�е�"?", <code>IBATIS's #id#</code> */
    String PLACE_HOLDER = "?";
    
    /**
     * �������ʵ��Ҫ����Ĳ�ѯ����
     * @return ���ʵ��Ҫ����Ĳ�ѯ����, ���û�з��س���Ϊ<b>0</b>������
     */
    SearchCondition[] getSearchCondition();
    
    /** 
     * ���ش����Ľ��, ����������������ȡֵ
     * @return ������������ȡֵ, ��Զ������<code>null</code>
     */
    SearchConditionAndParameter getResult();
    
    /**
     * �ϲ�������������������������, �������������û�а����κμ��������Ͳ����κδ���. ����Ҫ��
     * ����<code>SearchConditions</code>�е�<code>MetadataHolder</code>, �����Ľ����
     * �Ǻϲ��������������������ʵ����<code>domain object</code>
     * @param conditions ����������
     * @throws NullPointerException ���������<code>null</code>
     */
    void join(SearchConditions conditions);
}
