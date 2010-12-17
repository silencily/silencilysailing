package net.silencily.sailing.framework.persistent.search.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.silencily.sailing.framework.persistent.filter.Paginater;
import net.silencily.sailing.framework.persistent.search.MetadataHolder;
import net.silencily.sailing.framework.persistent.search.RowCallback;
import net.silencily.sailing.framework.persistent.search.SearchCondition;
import net.silencily.sailing.framework.persistent.search.SearchConditionAndParameter;
import net.silencily.sailing.framework.persistent.search.SearchConditions;
import net.silencily.sailing.framework.persistent.search.SearchService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ��������ĳ���ʵ��, ʵ����ע��/ȡ��ע��<code>MetadataHolder</code>�����Ƶ��߼�
 * @author scott
 * @since 2006-4-19
 * @version $Id: AbstractSearchService.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 *
 */
public abstract class AbstractSearchService implements SearchService {
    protected final Log logger = LogFactory.getLog(this.getClass());

    /** �Ѿ�ע���<code>MetadataHolder</code> */
    private Map metadataHolders = Collections.synchronizedMap(new HashMap(200));
    
    /** 
     * ��һ��<code>domain object</code>û����ص�<code>MetadataHolder</code>ʱ��ָ��
     * ���ȱʡ�Ĵ������ṩԪ��Ϣ
     */
    protected MetadataHolder defaultMetadataHolder;

    /**
     * ����ȱʡ��<code>domain object</code>Ԫ��Ϣ������
     * @param holder ȱʡ��Ԫ��Ϣ������
     */
    public void setMetadataHolder(MetadataHolder holder) {
        this.defaultMetadataHolder = holder;
    }

    /**
     * �ṩ�˿����õ�ע��<code>MetadataHolder</code>�ķ���, ������ϵͳ����ʱԤ��ע���Ҫ��
     * <code>MetadataHolder</code>
     * @param metadataHolder Ҫע���<code>MetadataHolder</code>�б�
     */
    public void setMetadataHolders(List metadataHolder) {
        if (metadataHolder != null) {
            for (Iterator it = metadataHolder.iterator(); it.hasNext(); ) {
                MetadataHolder holder = (MetadataHolder) it.next();
                registry(holder);
            }
        }
    }
    
    public synchronized boolean deregistry(Class type) {
        if (type == null) {
            throw new NullPointerException("ȡ��ע��domain objectԪ��Ϣʱ������null");
        }
        
        return metadataHolders.remove(type) != null;
    }

    public synchronized MetadataHolder registry(MetadataHolder holder) {
        if (holder == null) {
            throw new NullPointerException("ע��domain objectԪ��Ϣʱ������null");
        }
        
        if (holder.getDomainType() == null) {
            throw new IllegalArgumentException("ע��domain objectԪ��Ϣʱû��ָ��domain����");
        }
        
        MetadataHolder h = (MetadataHolder) metadataHolders.put(holder.getDomainType(), holder);
        return h;
    }

    public List search(final Class dto, SearchCondition[] conditions, Paginater paginater) {
        return search(dto, conditions, paginater, null);
    }

    public List search(final Class dto, SearchCondition[] conditions, Paginater paginater, RowCallback callback) {
        if (dto == null) {
            throw new NullPointerException("��������ʱ����domain object������null");
        }
        
        MetadataHolder holder = (MetadataHolder) this.metadataHolders.get(dto);
        if (holder == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("domain[" + dto.getName() + "]û�ж���MetadataHolder,ʹ��ȱʡ����");
            }
            /* ���ַ�ʽ����û������ */            
            synchronized (this.metadataHolders) {
                if (holder == null) {
                    registry(createDefaultMetadataHolder(dto));
                }
            }
            holder = (MetadataHolder) this.metadataHolders.get(dto);
        }
        
        if (conditions == null) {
            conditions = new SearchCondition[0];
        }
        
        if (paginater == null) {
            paginater = Paginater.NOT_PAGINATED;
        }
        
        SearchConditions searchConditions = createSearchConditions(conditions, holder);
        SearchConditionAndParameter condsAndParameters = searchConditions.getResult();
        if (logger.isDebugEnabled()) {
            logger.debug("���ݲ�ѯ��������:[" + condsAndParameters.getCondition() + "]");
        }
        String query = holder.getQueryStatement();
        if (logger.isDebugEnabled()) {
            logger.debug("���ɵļ����������ݵ�sql[" + query + "]");
        }
        SearchConditionAndParameter scp = populateSearchStatement(query, condsAndParameters);

        if (logger.isDebugEnabled()) {
            logger.debug("���ݼ����������ɵ�sql[" + scp.getCondition() + "]");
        }
        scp = pagination(paginater, scp);
        if (logger.isDebugEnabled()) {
            logger.debug("������ҳ���sql[" + scp.getCondition() + "]");
        }
        
        return execute(holder, scp.getCondition(), scp.getParams(), callback);
    }
    
    /**
     * ���ݲ�ѯ���н����<code>sql</code>�Ͳ�ѯ�������ɵ�<code>clause</code>���������<code>
     * select</code>���, ע������<code>condition</code>�Ѿ���������<code>sql</code>
     * @param query     �ɲ�ѯ���н����<code>sql</code>,���԰���������Ԫ��,����:<code>order</code>��
     * @param conds     ��ѯ������ɵ��Ӿ�Ͳ���
     * @return          ������ɵ�<code>sql</code>���Ͳ�����<code>SearchConditionAndParameter</code>
     */
    abstract protected SearchConditionAndParameter populateSearchStatement(String query, SearchConditionAndParameter conds);
    
    /**
     * ��һ��<code>domain object</code>û��ע��<code>MetadataHolder</code>ʱ, Ϊ������
     * ȱʡ��<code>MetadataHolder</code>
     * @param dto <code>domain object</code>����
     * @return Ϊ���<code>domain object</code>������<code>MetadataHolder</code>
     */
    abstract protected MetadataHolder createDefaultMetadataHolder(Class dto);

    /**
     * ����������������<code>SearchConditions</code>��ʵ��, ���ʵ�����ڴ���<code>where</code>
     * �����Ͳ���
     * @param conditions ����ʹ�õ�����
     * @param holder     ���<code>domain object</code>��Ԫ��Ϣ
     * @return ����<code>where</code>�����Ͳ�����<code>SearchConditions</code>
     */
    abstract protected SearchConditions createSearchConditions(SearchCondition[] conditions, MetadataHolder holder);
    
    /**
     * �����ҳ��<code>select</code>���, �����ҳ��{@link Paginater#NOT_PAGINATED <code>NOT_PAGINATED</code>}
     * �Ͳ�ִ�з�ҳ, ���ձ�����<code>select</code>���, ע���������Ҫ�Բ���<code>paginater</code>
     * ����, �����������ļ�¼������д��<code>Paginater</code>��
     * @param sql       ������<code>sql</code>���
     * @param paginater ��ҳ��Ϣ
     * @param conds     ���ݲ�ѯ�������ɵ��Ӿ�Ͳ���
     * @return          �����ҳ���<code>select</code>���Ͳ���
     */
    abstract SearchConditionAndParameter pagination(Paginater paginater, SearchConditionAndParameter conds);
    
    /**
     * ִ�����ļ���
     * @param sql       Ҫִ�е�<code>sql</code>
     * @param params    <code>sql</code>���Ĳ���
     * @param callback  ÿ����һ��ʱ�ص��������
     * @return          ��ѯ���, ���û�н�����س���Ϊ���<code>List</code>
     */
    abstract List execute(MetadataHolder holder, String sql, Object[] params, RowCallback callback);
}
