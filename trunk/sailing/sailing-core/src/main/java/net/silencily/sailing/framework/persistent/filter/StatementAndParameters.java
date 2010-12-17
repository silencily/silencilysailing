package net.silencily.sailing.framework.persistent.filter;


/**
 * 保存<code>PreparedStatement</code>的<code>sql</code>或<code>where clause</code>
 * 和实际的参数值
 * @author scott
 * @since 2006-4-30
 * @version $Id: StatementAndParameters.java,v 1.1 2010/12/10 10:54:17 silencily Exp $
 *
 */
public class StatementAndParameters {
    private String statement;
    private Object[] params;
    private static final String EMPTY_STRING = new String();
    private static final Object[] EMPTY_PARAM = new Object[0];
        
    public StatementAndParameters() {
        super();
    }
    
    public StatementAndParameters(String statement, Object[] params) {
        this.statement = statement;
        this.params = params;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }
    
    public static final StatementAndParameters EMPTY_CONDITION = new StatementAndParameters() {
        public String getStatement() {
            return EMPTY_STRING;
        }

        public void setStatement(String condition) {
            throw new UnsupportedOperationException("不能为空条件设置语句");
        }

        public Object[] getParams() {
            return EMPTY_PARAM;
        }

        public void setParams(Object[] params) {
            throw new UnsupportedOperationException("不能为空条件设置值");
        }
    };
}
