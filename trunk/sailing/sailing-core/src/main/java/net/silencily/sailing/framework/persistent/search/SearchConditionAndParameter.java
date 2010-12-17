package net.silencily.sailing.framework.persistent.search;

/**
 * 用于保存解析后的查询条件及查询条件的值, 其中的<code>condition</code>部分可能是完整的<code>
 * sql</code>或<code>where</code>部分, 取决于使用者
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
            throw new UnsupportedOperationException("不能为空条件设置值");
        }

        public Object[] getParams() {
            return EMPTY_PARAM;
        }

        public void setParams(Object[] params) {
            throw new UnsupportedOperationException("不能为空条件设置值");
        }
    };
}
