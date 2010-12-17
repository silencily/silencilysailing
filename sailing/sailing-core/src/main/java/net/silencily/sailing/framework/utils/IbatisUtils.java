package net.silencily.sailing.framework.utils;


import com.ibatis.sqlmap.engine.impl.ExtendedSqlMapClient;
import com.ibatis.sqlmap.engine.mapping.sql.Sql;

/**
 * 用于方便操作<code>IBATIS</code>的<code>utility</code>
 * @author scott
 * @since 2006-4-20
 * @version $Id: IbatisUtils.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 *
 */
public abstract class IbatisUtils {
    /**
     * 指定<code>IBATIS sql mapping id</code>返回定义的<code>sql</code>语句. 
     * @param id    查找语句的<code>mapping id</code>
     * @param param 这个<code>sql</code>语句使用的参数
     * @return   返回检索到的语句
     */
    public static String getSql(String id, Object param) {
        ExtendedSqlMapClient sqlMapClient = (ExtendedSqlMapClient) DaoHelper.getSqlMapClientTemplate().getSqlMapClient();
        Sql sql = sqlMapClient.getMappedStatement(id).getSql();
        return sql.getSql(null, param);
    }
}
