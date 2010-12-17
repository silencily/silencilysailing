package net.silencily.sailing.framework.persistent.search.impl;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import net.silencily.sailing.exception.UnexpectedException;
import net.silencily.sailing.framework.persistent.filter.Paginater;
import net.silencily.sailing.framework.persistent.search.MetadataHolder;
import net.silencily.sailing.framework.persistent.search.RowCallback;
import net.silencily.sailing.framework.persistent.search.SearchCondition;
import net.silencily.sailing.framework.persistent.search.SearchConditionAndParameter;
import net.silencily.sailing.framework.persistent.search.SearchConditions;
import net.silencily.sailing.framework.persistent.search.SqlPopulator;
import net.silencily.sailing.framework.utils.DaoHelper;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.jdbc.core.RowMapper;

/**
 * <code>AbstractSearchService</code>的缺省实现, 在实现中只使用了<code>BeanUtils</code>
 * 处理<code>domain object</code>, 没有针对<code>BaseDto</code>做任何处理, 
 * @author scott
 * @since 2006-4-23
 * @version $Id: JdbcSearchService.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 *
 */
public class JdbcSearchService extends AbstractSearchService {
    
    /**
     * 修改<code>sql</code>语句时使用这个类来完成实际的工作, 比如增加查询条件, 翻页
     */
    private SqlPopulator sqlPopulator = new SimpleSqlPopulator();

    public void setSqlPopulater(SqlPopulator sqlPopulator) {
        this.sqlPopulator = sqlPopulator;
    }

    protected MetadataHolder createDefaultMetadataHolder(Class dto) {
        return new IbatisBaseDtoMetadataHolder(dto);
    }

    protected SearchConditions createSearchConditions(SearchCondition[] conditions, MetadataHolder holder) {
        return new JdbcSearchConditions(conditions, holder);
    }

    protected SearchConditionAndParameter populateSearchStatement(String query, SearchConditionAndParameter conds) {
        return this.sqlPopulator.where(query, conds);
    }

    protected SearchConditionAndParameter pagination(Paginater paginater, SearchConditionAndParameter conds) {
        SearchConditionAndParameter scp = sqlPopulator.count(paginater, conds);
        int count = DaoHelper.getJdbcTemplate().queryForInt(scp.getCondition(), scp.getParams());
        paginater.setCount(count);
        
        if (paginater == Paginater.NOT_PAGINATED) {
            return conds;
        }
        
        return sqlPopulator.pagination(paginater, conds);
    }

    protected List execute(MetadataHolder holder, String sql, Object[] params, RowCallback callback) {
        RowMapper rowMapper = null;
        if (callback == null) {
            rowMapper = new ToBeanRowMapperAdapter(holder);
        } else {
            rowMapper = new RowMapperAdapter(callback);
        }
        
        return DaoHelper.getJdbcTemplate().query(sql, params, rowMapper);
    }
    
    private class ToBeanRowMapperAdapter implements RowMapper {
        private MetadataHolder holder;
        private ResultSetMetaData metadata;
        public ToBeanRowMapperAdapter(MetadataHolder holder) {
            this.holder = holder;
        }

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            if (metadata == null) {
                metadata = rs.getMetaData();
            }
            Object bean = null;
            try {
                bean = holder.getDomainType().newInstance();
            } catch (Exception e) {
                throw new UnexpectedException("不能创建[" + holder.getDomainType() + "]的实例", e);
            }
            
            for (int i = 0; i < metadata.getColumnCount(); i++) {
                String columnName = metadata.getColumnName(i + 1);
                String propertyName = holder.getPropertyName(columnName);
                try {
                    BeanUtils.setProperty(bean, propertyName, rs.getObject(i + 1));
                } catch (IllegalAccessException e) {
                    throw new UnexpectedException("不能访问domain属性[" + propertyName + "]", e);
                } catch (InvocationTargetException e) {
                    throw new UnexpectedException("设置[" + holder.getDomainType() + "]的属性[" + propertyName + "]错误", e);
                }
            }

            return bean;
        }        
    }
    
    private class RowMapperAdapter implements RowMapper {
        private RowCallback callback;
        public RowMapperAdapter() {}
        
        public RowMapperAdapter(RowCallback callback) {
            this.callback = callback;
        }

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            return callback.process(rs, rowNum);
        }        
    }
}
