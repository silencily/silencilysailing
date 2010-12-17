package net.silencily.sailing.framework.persistent.search;

import java.util.List;

import net.silencily.sailing.framework.core.GlobalParameters;
import net.silencily.sailing.framework.core.ServiceBase;
import net.silencily.sailing.framework.persistent.filter.Paginater;

/**
 * ֧����<code>SearchCondition</code>��ɵĲ�ѯ�����������ݵķ���, ��������������ʾ<code>
 * domain object</code>��������(Ҳ������������), ���������Ҫһ���Ѿ�������г��������ݵ����
 * (��������ʲô,<code>HQL</code>,<code>SQL</code>,<code>IBATIS with inner-place</code>
 * �ȵ�, ����������ͬ��ҳ��Ϣ�����ӵ���������൱��<code>WHERE</code>����, ��������ṩ���
 * ��Ϣ, ��ô����Ҫ�ṩ������
 * @author scott
 * @since 2006-4-19
 * @version $Id: SearchService.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 *
 */
public interface SearchService extends ServiceBase {
    String SERVICE_NAME = GlobalParameters.MODULE_NAME + ".searchService";
    /**
     * ����������������, ����������ÿ������Ҫ��װ�ɲ���<code>dto</code>��ָ��������, ����ִ��
     * �����������ļ�¼������д�ڲ���<code>paginater's count</code>��
     * @param dto           �������ݿ���ص�Ԫ��Ϣ��<code>key</code>
     * @param conditions    ��������, �����<code>null</code>���г����е�����
     * @param paginater     ÿ�η���ָ����Χ�Ľ����, ��Χ���������������
     * @return              ���������Ľ����
     */
    List search(Class dto, SearchCondition[] conditions, Paginater paginater);
    
    /**
     * ����������������, ����������ÿ������Ҫ��װ�ɲ���<code>dto</code>��ָ��������, ͬʱ���
     * ����Ҳ�Ǽ������ݿ����ص�Ԫ��Ϣ��<code>key</code>, ÿ����һ�����ݶ�Ҫ����һ��<code>
     * RowCallback.process</code>����, ����ִ�к����������ļ�¼������д�ڲ���<code>paginater's count
     * </code>��
     * @param dto           �������ݿ���ص�Ԫ��Ϣ��<code>key</code>
     * @param conditions    ��������, �����<code>null</code>���г����е�����
     * @param paginater     ÿ�η���ָ����Χ�Ľ����, ��Χ���������������
     * @return              ���������Ľ����
     */
    List search(Class dto, SearchCondition[] conditions, Paginater paginater, RowCallback callback);
    
    /**
     * Ϊһ��<code>domain object</code>ע��һ��Ԫ���ݴ�����, ����Ѿ�������ͬ���͵Ĵ�����,
     * �����µ��滻�ɵ�. ���<code>MetadataHolder</code>�����ṩ��ʲô���͵�<code>Metadata
     * </code>
     * @param holder    Ҫע���<code>MetadataHolder</code>
     * @return <b>�������<code>dto</code>��û��ע��ͷ���<code>null</code></b>, ����
     *         ����ԭ�е�<code>MetadataHolder</code>
     * @throws NullPointerException ����κ�һ��������<code>null</code>         
     * @throws IllegalArgumentException ������<code>holder</code>û���ṩҪ�����<code>
     *         domain object</code>������, (<code>MetadataHolder.getDomainTpye() == null</code>)
     */
    MetadataHolder registry(MetadataHolder holder);
    
    /**
     * ȡ������Ϊ<code>type</code>��<code>MetadataHolder</code>��ע��
     * @param type Ҫȡ��Ԫ��Ϣע���<code>domain object</code>����
     * @return ����ɹ�ȡ������<code>true</code>, ���û��������͵�Դ��Ϣ����<code>false</code>
     * @throws NullPointerException ���������<code>null</code>
     */
    boolean deregistry(Class type);
}
