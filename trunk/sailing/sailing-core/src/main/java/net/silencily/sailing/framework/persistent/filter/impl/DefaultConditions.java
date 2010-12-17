package net.silencily.sailing.framework.persistent.filter.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.silencily.sailing.container.CommonServiceProvider;
import net.silencily.sailing.framework.persistent.filter.Column;
import net.silencily.sailing.framework.persistent.filter.Condition;
import net.silencily.sailing.framework.persistent.filter.Conditions;
import net.silencily.sailing.framework.persistent.filter.DtoMetadata;
import net.silencily.sailing.framework.persistent.filter.StatementAndParameters;
import net.silencily.sailing.utils.MiscUtils;

import org.apache.commons.lang.StringUtils;

/**
 * <code>Conditions</code>的缺省实现, 适用于<code>iBATIS</code>/<code>JDBC</code>的
 * 持久化策略
 * @author scott
 * @since 2006-5-2
 * @version $Id: DefaultConditions.java,v 1.1 2010/12/10 10:54:21 silencily Exp $
 *
 */
public class DefaultConditions implements Conditions {
    private static final String PLACE_HOLDER = "?";
    
    private static final String PADDING_SIGN = " ";
    
    private static final String COMPOSITION_LEFT_FLAG = "(";
    
    private static final String COMPOSITION_RIGHT_FLAG = ")";

    /** 要组装查询子句的条件, 它们的顺序决定了生成结果 */
    protected Condition[] conds;
    
    protected List appliedConds;
    
    protected DtoMetadata dtoMetadata = DefaultDtoMetadata.INDEPENDENT_DTO_METADATA;
    
    /**
     * 缺省构造器, 属性<code>candidates</code>和<code>conds</code>必须通过<code>setter</code>
     * 设置
     */
    public DefaultConditions() {
        super();
    }

    public DefaultConditions(Condition[] conds) {
        this(conds, (Class) null);
    }
    
    /**
     * 用于没有特定的元信息的构造器, 设置要操作的条件和<code>DTO</code>类型, <code>DTO</code>
     * 的元信息使用缺省的<code>DefaultDtoMetadata</code>, 
     * @param conds 要操作的条件
     * @param dto   要操作的<code>DTO</code>类型
     */
    public DefaultConditions(Condition[] conds, Class dto) {
        super();
        this.conds = conds;
        this.dtoMetadata = CommonServiceProvider.getConditionService().getDtoMetadata(dto);
    }
    
    public DtoMetadata getDtoMetadata() {
        return this.dtoMetadata;
    }
    
    public void setDtoMetadata(DtoMetadata dtoMetadata) {
        this.dtoMetadata = dtoMetadata;
    }

    public Condition[] getConds() {
        if (conds == null) {
            return new Condition[0];
        }
        return conds;
    }

    public void setConds(Condition[] conds) {
        this.conds = conds;
    }

    /** {@inheritDoc} */
    public StatementAndParameters getResult() {
        if (getConds().length == 0) {
            return StatementAndParameters.EMPTY_CONDITION;
        }

        StringBuffer sb = new StringBuffer();
        List params = new ArrayList();
        appliedConds = new ArrayList();
        reentry(sb, params, getConds());
        
        StatementAndParameters sp = null;
        if (StringUtils.isBlank(sb.toString())) {
            sp = StatementAndParameters.EMPTY_CONDITION;
        } else {
            sp = new StatementAndParameters(sb.toString(), params.toArray(new Object[params.size()]));
        }

        return sp;
    }
    
    private void reentry(StringBuffer sb, List params, Condition[] condi) {
        /* Condition's compositeConditions 数组可能出现 null 元素 */
        condi = (Condition[]) MiscUtils.removeNullElements(condi);
        for (int i = 0; i < condi.length; i++) {
            if (StringUtils.isBlank(condi[i].getName()) && condi[i].getCompositeConditions().length == 0) {
                /* occurs eorror when populated the condition */ 
                throw new IllegalArgumentException("解析SearchCondition条件错误:既没有属性名称也没有子条件,已经解析的结果[" + sb.toString() + "]");
            }
            
            if (!condi[i].isPlace()) {
                continue;
            }

            if (sb.length() > 0 && !(Character.toString(sb.charAt(sb.length() - 1)).equals(COMPOSITION_LEFT_FLAG))) {
                /* 对于第一个条件忽略关系符, 其它的情况如果没有填写关系符使用缺省的 and */
                sb.append(PADDING_SIGN).append(defaultRelation(condi[i])).append(PADDING_SIGN);
            }
            
            if (condi[i].getCompositeConditions().length > 0) {
                sb.append(COMPOSITION_LEFT_FLAG);
                StringBuffer s = new StringBuffer();
                reentry(s, params, condi[i].getCompositeConditions());
                if (s.length() == 0) {
                    sb.append("1 = 1");
                } else {
                    sb.append(s);
                }
                sb.append(COMPOSITION_RIGHT_FLAG);
//            } else if (!isEmpty(condi[i].getValue())) {
            } else {
                String columnName = dtoMetadata.getColumnName(condi[i].getName());
                
                if (columnName == null) {
                    throw new IllegalStateException(dtoMetadata.getDtoType()
                        + "的属性["
                        + condi[i].getName()
                        + "]找不到对应的列");
                }
                if (Condition.IN.equals(condi[i].getOperator())) {
                    /* 对于IN,其类型Condition.getType()统统作为String, value 应该是 array, Collection 类型, 如果不是使用 toString 作为结果 */
                    Collection values = null;
                    if (condi[i].getValue() instanceof String) {
                        values = org.springframework.util.StringUtils.commaDelimitedListToSet((String) condi[i].getValue());
                    } else if (condi[i].getValue() instanceof Collection) {
                        values = (Collection) condi[i].getValue();
                    } else if (condi[i].getValue().getClass().isArray()) {
                        values = new ArrayList(((Object[]) condi[i].getValue()).length);
                        values.addAll(Arrays.asList((Object[]) condi[i].getValue()));
                    }
                    
                } else if (condi[i].getType() == Column.class) {
                    sb.append(columnName).append(PADDING_SIGN).append(defaultOperation(condi[i])).append(PADDING_SIGN).append(condi[i].getValue());
                } else {
                    sb.append(columnName).append(PADDING_SIGN).append(defaultOperation(condi[i])).append(PADDING_SIGN).append(PLACE_HOLDER);
                    params.add(defaultValue(condi[i]));
                    appliedConds.add(condi[i]);
                }
            }
        }
    }
    
    /** {@inheritDoc} */
    public void join(Conditions other) {
        if (other == null) {
            throw new NullPointerException("合并两个Conditions时参数是null");
        }

        DefaultConditions dec = (DefaultConditions) other;
        Condition[] cond = new Condition[getConds().length + dec.getConds().length];
        System.arraycopy(getConds(), 0, cond, 0, getConds().length);
        System.arraycopy(dec.getConds(), 0, cond, getConds().length, dec.getConds().length);
        conds = cond;
    }
    
    /** {@inheritDoc} */
    public void appendCondition(Condition[] other) {
        if (other == null) {
            throw new NullPointerException("要增加的条件是null");
        }
        if (other.length > 0) {
            DefaultConditions dc = new DefaultConditions();
            dc.setConds(other);
            join(dc);
        }
    }

    /**
     * 检索一个条件的操作符, 当一个条件<code>Condtion</code>没有指定操作符时返回缺省
     * 的操作符, 实际是什么操作符由<code>ConditionConstant</code>决定
     * @param cond 条件
     * @return     这个条件的操作符
     */
    public String defaultOperation(Condition cond) {
        String oper = cond.getOperator();
        if (StringUtils.isBlank(oper)) {
            oper = Condition.DEFAULT_OPERATOR;
        }
        
        return oper;
    }

    /**
     * 检索一个条件与其他条件的逻辑关系, 当一个条件<code>Condtion</code>没有指定关系符
     * 时返回缺省的关系符, 实际是什么关系符由<code>ConditionConstant</code>决定
     * @param cond 条件
     * @return     这个条件的关系符
     */
    public String defaultRelation(Condition cond) {
        String relation = cond.getPrepend();
        if (StringUtils.isBlank(relation)) {
            relation = Condition.DEFAULT_RELATION;
        }
        return relation;
    }
    
    public Object defaultValue(Condition cond) {
        if (cond.getType() == String.class && Condition.LIKE.equals(cond.getOperator())) {
            return "%" + cond.getValue() + "%";
        }
        return cond.getValue();
    }

    public Condition[] getAppliedConditions() {
        if (appliedConds == null) {
            return new Condition[0];
        }
        return (Condition[]) appliedConds.toArray(new Condition[appliedConds.size()]);
    }
}
