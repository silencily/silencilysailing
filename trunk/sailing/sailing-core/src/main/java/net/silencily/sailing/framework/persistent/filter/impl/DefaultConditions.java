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
 * <code>Conditions</code>��ȱʡʵ��, ������<code>iBATIS</code>/<code>JDBC</code>��
 * �־û�����
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

    /** Ҫ��װ��ѯ�Ӿ������, ���ǵ�˳����������ɽ�� */
    protected Condition[] conds;
    
    protected List appliedConds;
    
    protected DtoMetadata dtoMetadata = DefaultDtoMetadata.INDEPENDENT_DTO_METADATA;
    
    /**
     * ȱʡ������, ����<code>candidates</code>��<code>conds</code>����ͨ��<code>setter</code>
     * ����
     */
    public DefaultConditions() {
        super();
    }

    public DefaultConditions(Condition[] conds) {
        this(conds, (Class) null);
    }
    
    /**
     * ����û���ض���Ԫ��Ϣ�Ĺ�����, ����Ҫ������������<code>DTO</code>����, <code>DTO</code>
     * ��Ԫ��Ϣʹ��ȱʡ��<code>DefaultDtoMetadata</code>, 
     * @param conds Ҫ����������
     * @param dto   Ҫ������<code>DTO</code>����
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
        /* Condition's compositeConditions ������ܳ��� null Ԫ�� */
        condi = (Condition[]) MiscUtils.removeNullElements(condi);
        for (int i = 0; i < condi.length; i++) {
            if (StringUtils.isBlank(condi[i].getName()) && condi[i].getCompositeConditions().length == 0) {
                /* occurs eorror when populated the condition */ 
                throw new IllegalArgumentException("����SearchCondition��������:��û����������Ҳû��������,�Ѿ������Ľ��[" + sb.toString() + "]");
            }
            
            if (!condi[i].isPlace()) {
                continue;
            }

            if (sb.length() > 0 && !(Character.toString(sb.charAt(sb.length() - 1)).equals(COMPOSITION_LEFT_FLAG))) {
                /* ���ڵ�һ���������Թ�ϵ��, ������������û����д��ϵ��ʹ��ȱʡ�� and */
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
                        + "������["
                        + condi[i].getName()
                        + "]�Ҳ�����Ӧ����");
                }
                if (Condition.IN.equals(condi[i].getOperator())) {
                    /* ����IN,������Condition.getType()ͳͳ��ΪString, value Ӧ���� array, Collection ����, �������ʹ�� toString ��Ϊ��� */
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
            throw new NullPointerException("�ϲ�����Conditionsʱ������null");
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
            throw new NullPointerException("Ҫ���ӵ�������null");
        }
        if (other.length > 0) {
            DefaultConditions dc = new DefaultConditions();
            dc.setConds(other);
            join(dc);
        }
    }

    /**
     * ����һ�������Ĳ�����, ��һ������<code>Condtion</code>û��ָ��������ʱ����ȱʡ
     * �Ĳ�����, ʵ����ʲô��������<code>ConditionConstant</code>����
     * @param cond ����
     * @return     ��������Ĳ�����
     */
    public String defaultOperation(Condition cond) {
        String oper = cond.getOperator();
        if (StringUtils.isBlank(oper)) {
            oper = Condition.DEFAULT_OPERATOR;
        }
        
        return oper;
    }

    /**
     * ����һ�������������������߼���ϵ, ��һ������<code>Condtion</code>û��ָ����ϵ��
     * ʱ����ȱʡ�Ĺ�ϵ��, ʵ����ʲô��ϵ����<code>ConditionConstant</code>����
     * @param cond ����
     * @return     ��������Ĺ�ϵ��
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
