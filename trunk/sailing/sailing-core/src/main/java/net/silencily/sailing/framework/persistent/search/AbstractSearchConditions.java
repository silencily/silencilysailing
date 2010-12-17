package net.silencily.sailing.framework.persistent.search;

import org.apache.commons.lang.StringUtils;

/**
 * <code>SearchCondtion</code>的抽象实现, 提供了独立于具体实现的公用方法
 * 
 * @author scott
 * @since 2006-4-18
 * @version $Id: AbstractSearchConditions.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 *
 */
public abstract class AbstractSearchConditions implements SearchConditions {

    /** 要组装查询子句的条件, 它们的顺序决定了生成结果 */
    protected SearchCondition[] conds;
    
    /** 处理属性时使用的元信息 */
    protected MetadataHolder metadataHolder;
    
    public void setSearchCondition(SearchCondition[] conditions) {
        this.conds = conditions;
    }
    
    public SearchCondition[] getSearchCondition() {
        if (this.conds == null) {
            return new SearchCondition[0];
        }
        return this.conds;
    }

    public MetadataHolder getMetadataHolder() {
        return metadataHolder;
    }

    public void setMetadataHolder(MetadataHolder metadataHolder) {
        this.metadataHolder = metadataHolder;
    }

    public void join(SearchConditions conditions) {
        if (conditions == null) {
            throw new NullPointerException("合并两个AbstractSearchConditions时参数是null");
        }

        if (getSearchCondition().length > 0) {
            SearchCondition[] cond 
                = new SearchCondition[getSearchCondition().length + conditions.getSearchCondition().length];
            System.arraycopy(getSearchCondition(), 0, cond, 0, getSearchCondition().length);
            System.arraycopy(conditions.getSearchCondition(), 0, cond, getSearchCondition().length, conditions.getSearchCondition().length);
            conds = cond;
        }
        
    }
    
    /**
     * 检索一个条件的操作符, 当一个条件<code>SearchCondtion</code>没有指定操作符时返回缺省
     * 的操作符, 实际是什么操作符由<code>SearchConditionConstant</code>决定
     * @param cond 条件
     * @return     这个条件的操作符
     */
    protected String defaultOperation(SearchCondition cond) {
        String oper = cond.getOperator();
        if (StringUtils.isBlank(oper)) {
            oper = SearchConditionConstant.DEFAULT_OPERATOR;
        }
        
        return oper;
    }

    /**
     * 检索一个条件与其他条件的逻辑关系, 当一个条件<code>SearchCondtion</code>没有指定关系符
     * 时返回缺省的关系符, 实际是什么关系符由<code>SearchConditionConstant</code>决定
     * @param cond 条件
     * @return     这个条件的关系符
     */
    protected String defaultRelation(SearchCondition cond) {
        String relation = cond.getPrepend();
        if (StringUtils.isBlank(relation)) {
            relation = SearchConditionConstant.DEFAULT_RELATION;
        }
        return relation;
    }
}
