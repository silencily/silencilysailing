package net.silencily.sailing.framework.persistent.filter.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import com.ibatis.sqlmap.engine.mapping.parameter.BasicParameterMap;
import com.ibatis.sqlmap.engine.mapping.parameter.BasicParameterMapping;
import com.ibatis.sqlmap.engine.mapping.parameter.ParameterMap;
import com.ibatis.sqlmap.engine.mapping.parameter.ParameterMapping;
import com.ibatis.sqlmap.engine.scope.RequestScope;
import com.ibatis.sqlmap.engine.type.TypeHandler;

import net.silencily.sailing.container.CommonServiceProvider;
import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.framework.persistent.filter.Condition;
import net.silencily.sailing.framework.persistent.filter.ConditionInfo;
import net.silencily.sailing.framework.persistent.filter.Conditions;
import net.silencily.sailing.framework.persistent.filter.ConfigurableConditionService;
import net.silencily.sailing.framework.persistent.filter.Paginater;
import net.silencily.sailing.framework.persistent.filter.ReworkingParameter;
import net.silencily.sailing.framework.persistent.filter.SqlPopulater;
import net.silencily.sailing.framework.persistent.filter.StatementAndParameters;
import net.silencily.sailing.framework.utils.DaoHelper;
import net.silencily.sailing.utils.MiscUtils;

/**
 * 添加安全,通用查询的条件到要执行的<code>iBATIS's mapping</code>中, 这个类用于<code>
 * SqlExecutorInvocationHandler</code>的<code>executeQuery</code>, 在一个操作中又
 * 执行了其它的查询操作的情况下, 查询条件和分页操作基本上是不需要的. 在这种情况下需要调用
 * <code>ContextInfo.concealQuery</code>方法屏蔽掉查询条件和分页信息. 一个例子是当检索
 * 一个客户信息时, 在<code>DAO</code>中有这样的方法<code>listCustomers</code>, 而在这个
 * 方法中还调用了<code>listOrderByCustomer</code>的方法, 那么在调用第二个方法之前应该明确
 * 地调用<code>ContextInfo.concealQuery</code>以屏蔽掉这个执行线程的条件, 是否恢复取决于
 * 实际的应用
 * @author scott
 * @since 2006-5-6
 * @version $Id: ReworkingParameterForCondition.java,v 1.1 2010/12/10 10:54:21 silencily Exp $
 *
 */
public class ReworkingParameterForCondition implements ReworkingParameter {
	
    private static final ThreadLocal definedParameterMappings = new ThreadLocal();

    private SqlPopulater sqlPopulater = new SimpleSqlPopulater();
    
    public void setSqlPopulater(SqlPopulater sqlPopulater) {
        this.sqlPopulater = sqlPopulater;
    }
    
    /**
     * 修改参数, 参数<code>params</code>依次由下列数组组成<ol>
     * <li>RequestScope</li>
     * <li>Connection</li>
     * <li>sql statement</li>
     * <li>parameters array</li>
     * <li>skipResults</li>
     * <li>maxResults</li>
     * <li>RowHandlerCallback</li>
     * </ol>
     */
    public void rework(Object[] params) {
        if (ContextInfo.isConcealQuery()) {
            return;
        } else {
            ContextInfo.concealQuery();
        }
        
        Conditions conds = ((ConfigurableConditionService) CommonServiceProvider.getConditionService())
            .populateCondition(null);
        if (conds != Conditions.EMPTY_CONDITIONS && ((DefaultConditions) conds).getResult() != StatementAndParameters.EMPTY_CONDITION) {
            DefaultConditions dc = (DefaultConditions) conds;
            StatementAndParameters sp = dc.getResult();
            Condition[] con = dc.getAppliedConditions();
            
            /* set parameterMap */
            RequestScope request = (RequestScope) params[0];
            BasicParameterMap parameterMap = (BasicParameterMap) request.getParameterMap();
            ParameterMapping[] parameterMappings = parameterMap.getParameterMappings();
            Object[] saved = new Object[2];
            saved[0] = request;
            saved[1] = parameterMap;
            definedParameterMappings.set(saved);
            
            parameterMap = reconstruct(parameterMap);
            request.setParameterMap(parameterMap);
            /* saves original parameter mappings */
            List mappings = new ArrayList(parameterMappings.length);
            mappings.addAll(Arrays.asList(parameterMappings));

            ParameterMapping[] newMappings = new ParameterMapping[parameterMappings.length + con.length];
            for (int i = 0; i < con.length; i++) {
                BasicParameterMapping mapping = new BasicParameterMapping();
                mapping.setPropertyName(dc.getDtoMetadata().getColumnName(con[i].getName()));
                mapping.setJavaType(con[i].getType());
                TypeHandler typeHandler = parameterMap.getDelegate().getTypeHandlerFactory().getTypeHandler(con[i].getType());
                if (typeHandler == null) {
                    typeHandler = parameterMap.getDelegate().getTypeHandlerFactory().getUnkownTypeHandler();
                }
                mapping.setTypeHandler(typeHandler);
                newMappings[i] = mapping;
            }
            System.arraycopy(parameterMappings, 0, newMappings, con.length, parameterMappings.length);
            List list = Arrays.asList(newMappings);
            parameterMap.setParameterMappingList(list);
            request.setParameterMap(parameterMap);
            
            /* set sql */
            String sql = sqlPopulater.where((String) params[2], sp.getStatement());
            params[2] = sql;
            
            /* set parameters */
            Object[] argus = sp.getParams();
            Object[] orig = (Object[]) params[3];
            Object[] newParams = new Object[argus.length + orig.length];
            System.arraycopy(argus, 0, newParams, 0, argus.length);
            System.arraycopy(orig, 0, newParams, argus.length, orig.length);
            params[3] = newParams;
        }
        
        /* set pagination */
        ConditionInfo info = ContextInfo.getContextCondition();
        Paginater p = info.getPaginater();
        if (p != Paginater.NOT_PAGINATED) {
            params[4] = new Integer(p.getPage() * p.getPageSize());
            params[5] = new Integer(p.getPageSize());
            /* if paginated, counting to the count of row */
            String countSql = sqlPopulater.count((String) params[2], null);
            int rowcount = 0;
            /*
             * 在运行过程中发现这样的问题, 每次参数params[3]中的参数都会多出一个null元素,
             * 即使没有查询参数是依旧有一个null, 这样导致 queryForInt 方法执行错误. 所以
             * 这里过滤掉null元素
             */
            Object[] param = null;
            if (params[3] == null || ((Object[]) params[3]).length == 0) {
                param = new Object[0];
            } else {
                param = new Object[((Object[]) params[3]).length];
                System.arraycopy(params[3], 0, param, 0, param.length);
                param = MiscUtils.removeNullElements(param);
            }
            if (param.length == 0) {
                rowcount = DaoHelper.getJdbcTemplate().queryForInt(countSql);
            } else {
                rowcount = DaoHelper.getJdbcTemplate().queryForInt(countSql, param);
            }
            p.setCount(rowcount);
        }
    }
    
    /**
     * 重新构建<code>BasicParameterMap</code>类, 因为这个类堆积<code>parameterMappingIndex</code>
     * 的属性. 在接下来的执行中要调用<code>setParameterMappingList</code>方法设置其中的参数
     */
    private BasicParameterMap reconstruct(BasicParameterMap oldp) {
        BasicParameterMap newp = new BasicParameterMap(oldp.getDelegate());
        newp.setId(oldp.getId());
        newp.setParameterClass(oldp.getParameterClass());
        newp.setResource(oldp.getResource());
        return newp;
    }

    public void restore() {
        if (definedParameterMappings.get() != null) {
            Object[] saved = (Object[]) definedParameterMappings.get();
            RequestScope request = (RequestScope) saved[0];
            ParameterMap parameterMap = (ParameterMap) saved[1];
            request.setParameterMap(parameterMap);
            definedParameterMappings.set(null);
        }
    }
}
