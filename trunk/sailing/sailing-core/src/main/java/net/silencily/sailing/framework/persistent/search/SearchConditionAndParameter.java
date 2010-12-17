package net.silencily.sailing.framework.persistent.search;

/**
 * ���ڱ��������Ĳ�ѯ��������ѯ������ֵ, ���е�<code>condition</code>���ֿ�����������<code>
 * sql</code>��<code>where</code>����, ȡ����ʹ����
 * @author scott
 * @since 2006-4-19
 * @version $Id: SearchConditionAndParameter.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 *
 */
public class SearchConditionAndParameter {
    private String condition;
    private Object[] params;
    private static final String EMPTY_STRING = new String();
    private static final Object[] EMPTY_PARAM = new Object[0];
        
    public SearchConditionAndParameter() {
        super();
    }
    
    public SearchConditionAndParameter(String condition, Object[] params) {
        this.condition = condition;
        this.params = params;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }
    
    public static final SearchConditionAndParameter EMPTY_CONDITION = new SearchConditionAndParameter() {
        public String getCondition() {
            return EMPTY_STRING;
        }

        public void setCondition(String condition) {
            throw new UnsupportedOperationException("����Ϊ����������ֵ");
        }

        public Object[] getParams() {
            return EMPTY_PARAM;
        }

        public void setParams(Object[] params) {
            throw new UnsupportedOperationException("����Ϊ����������ֵ");
        }
    };
}
