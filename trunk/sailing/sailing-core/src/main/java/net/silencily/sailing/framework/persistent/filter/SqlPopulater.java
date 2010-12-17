package net.silencily.sailing.framework.persistent.filter;

/**
 * 拼装<code>sql</code>语句的<code>utiltity</code>
 * @author scott
 * @since 2006-5-6
 * @version $Id: SqlPopulater.java,v 1.1 2010/12/10 10:54:17 silencily Exp $
 *
 */
public interface SqlPopulater {
    /** following, went without saying */
    String WHERE = "where";
    
    String FROM = "from";
    
    String AND = "and";
    
    String REPLACE_SIGN = "${sql}";
    
    String COUNT = "select count(*) from (" + REPLACE_SIGN + ") t";
    
    /**
     * 把参数指定的条件子句拼接到<code>sql</code>语句的<code>where</code>部分
     * @param sql       <code>sql</code>语句
     * @param clause    要拼接的条件子句
     * @return          拼接后的<code>sql</code>语句
     */
    String where(String sql, String clause);
    
    /**
     * 拼接统计记录数量的<code>sql</code>, 数量的计算在应用了条件子句之后
     * @param sql       查询数据的<code>sql</code>
     * @param clause    条件子句
     * @return          统计基于条件的记录数量的<code>sql</code>
     */
    String count(String sql, String clause);
}
