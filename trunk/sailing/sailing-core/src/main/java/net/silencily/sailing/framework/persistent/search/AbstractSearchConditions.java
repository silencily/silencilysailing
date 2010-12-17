package net.silencily.sailing.framework.persistent.search;

import org.apache.commons.lang.StringUtils;

/**
 * <code>SearchCondtion</code>�ĳ���ʵ��, �ṩ�˶����ھ���ʵ�ֵĹ��÷���
 * 
 * @author scott
 * @since 2006-4-18
 * @version $Id: AbstractSearchConditions.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 *
 */
public abstract class AbstractSearchConditions implements SearchConditions {

    /** Ҫ��װ��ѯ�Ӿ������, ���ǵ�˳����������ɽ�� */
    protected SearchCondition[] conds;
    
    /** ��������ʱʹ�õ�Ԫ��Ϣ */
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
            throw new NullPointerException("�ϲ�����AbstractSearchConditionsʱ������null");
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
     * ����һ�������Ĳ�����, ��һ������<code>SearchCondtion</code>û��ָ��������ʱ����ȱʡ
     * �Ĳ�����, ʵ����ʲô��������<code>SearchConditionConstant</code>����
     * @param cond ����
     * @return     ��������Ĳ�����
     */
    protected String defaultOperation(SearchCondition cond) {
        String oper = cond.getOperator();
        if (StringUtils.isBlank(oper)) {
            oper = SearchConditionConstant.DEFAULT_OPERATOR;
        }
        
        return oper;
    }

    /**
     * ����һ�������������������߼���ϵ, ��һ������<code>SearchCondtion</code>û��ָ����ϵ��
     * ʱ����ȱʡ�Ĺ�ϵ��, ʵ����ʲô��ϵ����<code>SearchConditionConstant</code>����
     * @param cond ����
     * @return     ��������Ĺ�ϵ��
     */
    protected String defaultRelation(SearchCondition cond) {
        String relation = cond.getPrepend();
        if (StringUtils.isBlank(relation)) {
            relation = SearchConditionConstant.DEFAULT_RELATION;
        }
        return relation;
    }
}
