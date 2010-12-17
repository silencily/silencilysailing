package net.silencily.sailing.framework.persistent.search.impl;

import java.util.ArrayList;
import java.util.List;

import net.silencily.sailing.framework.persistent.search.AbstractSearchConditions;
import net.silencily.sailing.framework.persistent.search.MetadataHolder;
import net.silencily.sailing.framework.persistent.search.SearchCondition;
import net.silencily.sailing.framework.persistent.search.SearchConditionAndParameter;

import org.apache.commons.lang.StringUtils;

/**
 * 查询条件<code>JDBC</code>实现, 把查询条件解析为满足<code>SQL</code>标准语法的子句
 * @author scott
 * @since 2006-4-21
 * @version $Id: JdbcSearchConditions.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 *
 */
public class JdbcSearchConditions extends AbstractSearchConditions {
    private static final String PADDING_SIGN = " ";
    
    private static final String COMPOSITION_LEFT_FLAG = "(";
    
    private static final String COMPOSITION_RIGHT_FLAG = ")";
    
    public JdbcSearchConditions() {
        this(null, null);
    }
    
    public JdbcSearchConditions(SearchCondition[] conditions) {
        this(conditions, null);
    }
    
    public JdbcSearchConditions(SearchCondition[] conditions, MetadataHolder metadataHolder) {
        super();
        this.conds = conditions;
        this.metadataHolder = metadataHolder;
    }

    public SearchConditionAndParameter getResult() {
        if (getSearchCondition().length == 0) {
            return SearchConditionAndParameter.EMPTY_CONDITION;
        }
        
        StringBuffer sb = new StringBuffer();
        List params = new ArrayList();
        reentry(sb, params, getSearchCondition());
        SearchConditionAndParameter sp = new SearchConditionAndParameter(sb.toString(), params.toArray(new Object[params.size()]));

        return sp;
    }
    
    private void reentry(StringBuffer sb, List params, SearchCondition[] condi) {
        for (int i = 0; i < condi.length; i++) {
            if (StringUtils.isBlank(condi[i].getName()) && condi[i].getCompositeConditions().length == 0) {
                throw new IllegalArgumentException("解析SearchCondition条件错误:既没有属性名称也没有子条件,已经解析的结果[" + sb.toString() + "]");
            }
            
            if (i > 0) {
                /* 对于第一个条件忽略关系符, 其它的情况如果没有填写关系符使用缺省的 and */
                sb.append(PADDING_SIGN).append(defaultRelation(condi[i])).append(PADDING_SIGN);
            }
            
            if (condi[i].getCompositeConditions().length > 0) {
                sb.append(COMPOSITION_LEFT_FLAG);
                reentry(sb, params, condi[i].getCompositeConditions());
                sb.append(COMPOSITION_RIGHT_FLAG);
            } else {
                String columnName = metadataHolder.getColumnName(condi[i].getName());
                if (columnName == null) {
                    throw new IllegalStateException(metadataHolder.getDomainType()
                        + "的属性["
                        + condi[i].getName()
                        + "]找不到对应的列");
                }
                sb.append(columnName).append(PADDING_SIGN).append(defaultOperation(condi[i])).append(PADDING_SIGN).append(PLACE_HOLDER);
                params.add(condi[i].getValue());
            }
        }
    }
}
