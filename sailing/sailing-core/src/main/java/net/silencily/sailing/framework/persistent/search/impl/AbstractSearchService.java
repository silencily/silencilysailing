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
 * 检索服务的抽象实现, 实现了注册/取消注册<code>MetadataHolder</code>及控制的逻辑
 * @author scott
 * @since 2006-4-19
 * @version $Id: AbstractSearchService.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 *
 */
public abstract class AbstractSearchService implements SearchService {
    protected final Log logger = LogFactory.getLog(this.getClass());

    /** 已经注册的<code>MetadataHolder</code> */
    private Map metadataHolders = Collections.synchronizedMap(new HashMap(200));
    
    /** 
     * 当一个<code>domain object</code>没有相关的<code>MetadataHolder</code>时将指定
     * 这个缺省的处理器提供元信息
     */
    protected MetadataHolder defaultMetadataHolder;

    /**
     * 设置缺省的<code>domain object</code>元信息处理器
     * @param holder 缺省的元信息处理器
     */
    public void setMetadataHolder(MetadataHolder holder) {
        this.defaultMetadataHolder = holder;
    }

    /**
     * 提供了可配置的注册<code>MetadataHolder</code>的方法, 用于在系统启动时预先注册必要的
     * <code>MetadataHolder</code>
     * @param metadataHolder 要注册的<code>MetadataHolder</code>列表
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
            throw new NullPointerException("取消注册domain object元信息时参数是null");
        }
        
        return metadataHolders.remove(type) != null;
    }

    public synchronized MetadataHolder registry(MetadataHolder holder) {
        if (holder == null) {
            throw new NullPointerException("注册domain object元信息时参数是null");
        }
        
        if (holder.getDomainType() == null) {
            throw new IllegalArgumentException("注册domain object元信息时没有指定domain类型");
        }
        
        MetadataHolder h = (MetadataHolder) metadataHolders.put(holder.getDomainType(), holder);
        return h;
    }

    public List search(final Class dto, SearchCondition[] conditions, Paginater paginater) {
        return search(dto, conditions, paginater, null);
    }

    public List search(final Class dto, SearchCondition[] conditions, Paginater paginater, RowCallback callback) {
        if (dto == null) {
            throw new NullPointerException("检索数据时参数domain object类型是null");
        }
        
        MetadataHolder holder = (MetadataHolder) this.metadataHolders.get(dto);
        if (holder == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("domain[" + dto.getName() + "]没有定义MetadataHolder,使用缺省处理");
            }
            /* 这种方式可能没有作用 */            
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
            logger.debug("根据查询条件生成:[" + condsAndParameters.getCondition() + "]");
        }
        String query = holder.getQueryStatement();
        if (logger.isDebugEnabled()) {
            logger.debug("生成的检索所有数据的sql[" + query + "]");
        }
        SearchConditionAndParameter scp = populateSearchStatement(query, condsAndParameters);

        if (logger.isDebugEnabled()) {
            logger.debug("根据检索条件生成的sql[" + scp.getCondition() + "]");
        }
        scp = pagination(paginater, scp);
        if (logger.isDebugEnabled()) {
            logger.debug("创建分页后的sql[" + scp.getCondition() + "]");
        }
        
        return execute(holder, scp.getCondition(), scp.getParams(), callback);
    }
    
    /**
     * 根据查询所有结果的<code>sql</code>和查询条件生成的<code>clause</code>组成完整的<code>
     * select</code>语句, 注意结果的<code>condition</code>已经是完整的<code>sql</code>
     * @param query     可查询所有结果的<code>sql</code>,可以包含其他的元素,比如:<code>order</code>等
     * @param conds     查询条件组成的子句和参数
     * @return          包含组成的<code>sql</code>语句和参数的<code>SearchConditionAndParameter</code>
     */
    abstract protected SearchConditionAndParameter populateSearchStatement(String query, SearchConditionAndParameter conds);
    
    /**
     * 当一个<code>domain object</code>没有注册<code>MetadataHolder</code>时, 为它创建
     * 缺省的<code>MetadataHolder</code>
     * @param dto <code>domain object</code>类型
     * @return 为这个<code>domain object</code>创建的<code>MetadataHolder</code>
     */
    abstract protected MetadataHolder createDefaultMetadataHolder(Class dto);

    /**
     * 给定检索条件创建<code>SearchConditions</code>的实例, 这个实例用于创建<code>where</code>
     * 条件和参数
     * @param conditions 检索使用的条件
     * @param holder     相关<code>domain object</code>的元信息
     * @return 创建<code>where</code>条件和参数的<code>SearchConditions</code>
     */
    abstract protected SearchConditions createSearchConditions(SearchCondition[] conditions, MetadataHolder holder);
    
    /**
     * 加入分页的<code>select</code>语句, 如果分页是{@link Paginater#NOT_PAGINATED <code>NOT_PAGINATED</code>}
     * 就不执行分页, 按照本来的<code>select</code>语句, 注意这个方法要对参数<code>paginater</code>
     * 操作, 把满足条件的记录行数填写到<code>Paginater</code>中
     * @param sql       完整的<code>sql</code>语句
     * @param paginater 分页信息
     * @param conds     根据查询条件生成的子句和参数
     * @return          加入分页后的<code>select</code>语句和参数
     */
    abstract SearchConditionAndParameter pagination(Paginater paginater, SearchConditionAndParameter conds);
    
    /**
     * 执行最后的检索
     * @param sql       要执行的<code>sql</code>
     * @param params    <code>sql</code>语句的参数
     * @param callback  每处理一行时回调这个方法
     * @return          查询结果, 如果没有结果返回长度为零的<code>List</code>
     */
    abstract List execute(MetadataHolder holder, String sql, Object[] params, RowCallback callback);
}
