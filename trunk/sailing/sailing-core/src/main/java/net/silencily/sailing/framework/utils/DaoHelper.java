package net.silencily.sailing.framework.utils;

import java.sql.Connection;
import java.sql.SQLException;

import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.framework.core.GlobalParameters;
import net.silencily.sailing.framework.persistent.common.PropertyPadding;
import net.silencily.sailing.framework.persistent.hibernate3.EnhancedHibernateTemplate;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

/**
 * <p>一个<code>DAO</code>的帮助类, 检索各种数据访问实现的<code>Template</code></p>
 * 
 * @author scott
 * @since 2006-3-10
 * @version $Id: DaoHelper.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 *
 */
public abstract class DaoHelper {
    /** 未知的数据库产品 */
    public static final int UNKNOWN_DBMS = -1;
    
    /** 表示当前<code>DBMS</code>产品是<code>ORACLE DBMS</code> */
    public static final int ORACLE = 1;
    
    /** 表示当前<code>DBMS</code>产品是<code>MY SQL</code> */
    public static final int MYSQL = 2;
    
    /** 表示当前<code>DBMS</code>产品是<code>hsql</code> */
    public static final int HSQL = 3;
    
    private static int currentDbms = UNKNOWN_DBMS;
    
    private static final String HIBERNATE_BEAN_NAME = GlobalParameters.MODULE_NAME + ".hibernateTemplate";
    
    private static final String SQLMAP_BEAN_NAME = GlobalParameters.MODULE_NAME + ".sqlMapClientTemplate";
    
    private static final String JDBC_BEAN_NAME = GlobalParameters.MODULE_NAME + ".jdbcTemplate";
    
    private static final String PRIMARY_KEY_BEAN_NAME = GlobalParameters.MODULE_NAME + ".primaryKey";

    /**
     * 检索<code>iBATIS</code>实现的数据访问模板类, 用在<code>dao</code>实现中进行数据访问操作,
     * 返回的<code>SqlMapClientTemplate</code>实现了所有的<code>SqlMapClient</code>
     * 的方法, 所以可以按照<code>iBATIS</code>所提供的<code>API</code>执行所有的方法, 不需要
     * 另外了解<code>SqlMapClientTemplate</code>的所有方法. 对于更复杂的操作, 参考<code>
     * SqlMapClientCallbak</code>
     * 
     * @return <code>iBATIS</code>实现的数据访问模板
     */
    public static SqlMapClientTemplate getSqlMapClientTemplate() {
        return (SqlMapClientTemplate) ServiceProvider.getService(SQLMAP_BEAN_NAME);
    }
    
    /**
     * 检索<code>jdbc</code>实现的数据访问模板类, 用在<code>dao</code>实现中进行数据访问操作
     * @return <code>jdbc</code>实现的数据访问模板类
     */
    public static JdbcTemplate getJdbcTemplate() {
        return (JdbcTemplate) ServiceProvider.getService(JDBC_BEAN_NAME);
    }
    
    /**
     * 检索<code>hibernate</code>实现的实体访问模板类, 用在<code>dao</code>实现中进行数据访问操作
     * @return <code>hibernate</code>实现的实体访问模板类
     */
    public static EnhancedHibernateTemplate getHibernateTemplate() {
    	return (EnhancedHibernateTemplate) ServiceProvider.getService(HIBERNATE_BEAN_NAME);
    }
    
    /**
     * 检索新的主键, 这个主键是<code>String</code>类型, 长度为<b>32</b>位. 保证在整个应用
     * 服务器上的唯一性
     * @return 新生成的主键
     */
    public static String nextPrimaryKey() {
        return (String) ServiceProvider.getService(PRIMARY_KEY_BEAN_NAME);
    }
    
    /**
     * 检索创建新的记录时管理设置缺省属性值的组件, 获得这个组件后可以注册新的属性设置类
     * @return 创建新的记录时管理设置缺省属性值的组件
     */
    public static PropertyPadding getPropertyPadding() {
        return (PropertyPadding) ServiceProvider.getService(PropertyPadding.SERVICE_NAME);
    }
    
    /**
     * 返回表示当前系统中配置的数据库产品常量
     * @return 当前系统中配置的数据库产品
     * @see #ORACLE
     * @see #MYSQL
     * @see #HSQL
     */
    public static int getDbmsProduction() {
        if (currentDbms == UNKNOWN_DBMS) {
            JdbcTemplate jt = getJdbcTemplate();
            String prod = (String) jt.execute(new ConnectionCallback() {
                public Object doInConnection(Connection con) throws SQLException, DataAccessException {
                    return con.getMetaData().getDatabaseProductName();
                }
            });

            if (prod != null) {
                if (prod.toLowerCase().indexOf("oracle") > -1) {
                    currentDbms = ORACLE;
                } else if (prod.toLowerCase().indexOf("mysql") > -1) {
                    currentDbms = MYSQL;
                }
            }
        }
        
        return currentDbms;
    }
}
