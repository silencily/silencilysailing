package net.silencily.sailing.framework.persistent.filter.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.framework.persistent.filter.Condition;
import net.silencily.sailing.framework.persistent.filter.ConditionInfo;
import net.silencily.sailing.framework.persistent.filter.Conditions;
import net.silencily.sailing.framework.persistent.filter.ConfigurableConditionService;
import net.silencily.sailing.framework.persistent.filter.DtoMetadata;
import net.silencily.sailing.framework.persistent.filter.Paginater;
import net.silencily.sailing.framework.persistent.filter.StatementAndParameters;
import net.silencily.sailing.framework.utils.DaoHelper;

import org.springframework.util.Assert;

/**
 * 持久化操作条件服务实现类, 注意这个类并没有严格地解决多线程的问题
 * @author scott
 * @since 2006-5-3
 * @version $Id: ConditionServiceImpl.java,v 1.1 2010/12/10 10:54:21 silencily Exp $
 *
 */
public class ConditionServiceImpl implements ConfigurableConditionService {
    protected Map registriedDtoMetadatas = Collections.synchronizedMap(new HashMap());
    
    private DtoMetadata defaultDtoMetadata = new DefaultDtoMetadata();

    /** {@inheritDoc} */
    public DtoMetadata registry(DtoMetadata metadata) {
        
        Assert.notNull(metadata, "调用ConditionService.registry参数是null");
        Assert.notNull(metadata.getDtoType(), "调用ConditionService.registry没有指定dto类型");

        synchronized (registriedDtoMetadatas) {
            return (DtoMetadata) registriedDtoMetadatas.put(metadata.getDtoType(), metadata);
        }
    }

    /** {@inheritDoc} */
    public boolean deregistry(Class type) {
        Assert.notNull(type, "调用ConditionService.deregistry没有指定dto类型");

        synchronized (registriedDtoMetadatas) {
            return (registriedDtoMetadatas.remove(type) != null);
        }
    }
    
    /** {@inheritDoc} */
    public DtoMetadata getDtoMetadata(Class dto) {
        if (!registriedDtoMetadatas.containsKey(dto)) {
            return defaultDtoMetadata;
        }
        
        return (DtoMetadata) registriedDtoMetadatas.get(dto);
    }

    public void applyAppendCondition(Condition[] conditions, Paginater paginater) {
        Assert.notNull(conditions, "调用ConfigurableConditionSerivce.applyAppendCondition方法时参数Condition[]是null");
        Assert.notNull(conditions, "调用ConfigurableConditionSerivce.applyAppendCondition方法时参数Paginater是null");
        ConditionInfo info = getConditionInfo();
        info.setAppendConditions(conditions);
        info.setPaginater(paginater);
        ContextInfo.setContextCondition(info);
    }

    public void cancelAppendCondition() {
        ConditionInfo info = getConditionInfo();
        info.setAppendConditions(null);
        info.setPaginater(null);
        ContextInfo.setContextCondition(info);
    }

    public Conditions populateCondition(Class dto) {
        ConditionInfo info = getConditionInfo();
        DefaultConditions orig = new DefaultConditions();
        orig.setConds(info.getOriginalConditions());
        orig.setDtoMetadata(getDtoMetadata(dto));
        orig.appendCondition(info.getAppendConditions());
        if (orig.getConds().length == 0) {
            return Conditions.EMPTY_CONDITIONS;
        }
        
        return orig;
    }
    
    private ConditionInfo getConditionInfo() {
        ConditionInfo info = ContextInfo.getContextCondition();
        if (info == null) {
            /* there may be this case, yes. it happen just at standalone */
            info = new ConditionInfo();
            ContextInfo.setContextCondition(info);
        }
        return info;
    }
    
    /**
     * 使用当前执行线程的<code>ContextInfo</code>中<code>Paginater</code>组装<code>sql</code>
     * , 如果存在附加的<code>Paginater</code>,首先使用这个<code>Paginater</code>
     */
    public StatementAndParameters populateSqlWithPaginater(StatementAndParameters sp) {
        Paginater p = ContextInfo.getContextCondition().getPaginater();
        if (p == Paginater.NOT_PAGINATED) {
            return sp;
        }
        
        StatementAndParameters sap = null;
        int dbms = DaoHelper.getDbmsProduction();
        if (dbms == DaoHelper.MYSQL) {
            sap = paginationForMysql(sp, p);
        } else if (dbms == DaoHelper.ORACLE) {
            sap = paginationForOracle(sp);
        }
        
        return sap;
    }
    
    private StatementAndParameters paginationForMysql(StatementAndParameters sp, Paginater p) {
        String sql = sp.getStatement().concat(" limit ?, ?");
        Object[] params = sp.getParams();
        Object[] paramsWithPagination = new Object[params.length + 2];
        System.arraycopy(params, 0, paramsWithPagination, 0, params.length);
        paramsWithPagination[params.length] = new Integer(p.getPage() * p.getPageSize());
        paramsWithPagination[params.length + 1] = new Integer(p.getPageSize());
        
        return new StatementAndParameters(sql, paramsWithPagination);
    }
    
    private StatementAndParameters paginationForOracle(StatementAndParameters sp) {
        throw new UnsupportedOperationException("fuck");
    }
}
