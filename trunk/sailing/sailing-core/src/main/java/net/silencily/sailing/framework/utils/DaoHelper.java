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
 * <p>һ��<code>DAO</code>�İ�����, �����������ݷ���ʵ�ֵ�<code>Template</code></p>
 * 
 * @author scott
 * @since 2006-3-10
 * @version $Id: DaoHelper.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 *
 */
public abstract class DaoHelper {
    /** δ֪�����ݿ��Ʒ */
    public static final int UNKNOWN_DBMS = -1;
    
    /** ��ʾ��ǰ<code>DBMS</code>��Ʒ��<code>ORACLE DBMS</code> */
    public static final int ORACLE = 1;
    
    /** ��ʾ��ǰ<code>DBMS</code>��Ʒ��<code>MY SQL</code> */
    public static final int MYSQL = 2;
    
    /** ��ʾ��ǰ<code>DBMS</code>��Ʒ��<code>hsql</code> */
    public static final int HSQL = 3;
    
    private static int currentDbms = UNKNOWN_DBMS;
    
    private static final String HIBERNATE_BEAN_NAME = GlobalParameters.MODULE_NAME + ".hibernateTemplate";
    
    private static final String SQLMAP_BEAN_NAME = GlobalParameters.MODULE_NAME + ".sqlMapClientTemplate";
    
    private static final String JDBC_BEAN_NAME = GlobalParameters.MODULE_NAME + ".jdbcTemplate";
    
    private static final String PRIMARY_KEY_BEAN_NAME = GlobalParameters.MODULE_NAME + ".primaryKey";

    /**
     * ����<code>iBATIS</code>ʵ�ֵ����ݷ���ģ����, ����<code>dao</code>ʵ���н������ݷ��ʲ���,
     * ���ص�<code>SqlMapClientTemplate</code>ʵ�������е�<code>SqlMapClient</code>
     * �ķ���, ���Կ��԰���<code>iBATIS</code>���ṩ��<code>API</code>ִ�����еķ���, ����Ҫ
     * �����˽�<code>SqlMapClientTemplate</code>�����з���. ���ڸ����ӵĲ���, �ο�<code>
     * SqlMapClientCallbak</code>
     * 
     * @return <code>iBATIS</code>ʵ�ֵ����ݷ���ģ��
     */
    public static SqlMapClientTemplate getSqlMapClientTemplate() {
        return (SqlMapClientTemplate) ServiceProvider.getService(SQLMAP_BEAN_NAME);
    }
    
    /**
     * ����<code>jdbc</code>ʵ�ֵ����ݷ���ģ����, ����<code>dao</code>ʵ���н������ݷ��ʲ���
     * @return <code>jdbc</code>ʵ�ֵ����ݷ���ģ����
     */
    public static JdbcTemplate getJdbcTemplate() {
        return (JdbcTemplate) ServiceProvider.getService(JDBC_BEAN_NAME);
    }
    
    /**
     * ����<code>hibernate</code>ʵ�ֵ�ʵ�����ģ����, ����<code>dao</code>ʵ���н������ݷ��ʲ���
     * @return <code>hibernate</code>ʵ�ֵ�ʵ�����ģ����
     */
    public static EnhancedHibernateTemplate getHibernateTemplate() {
    	return (EnhancedHibernateTemplate) ServiceProvider.getService(HIBERNATE_BEAN_NAME);
    }
    
    /**
     * �����µ�����, ���������<code>String</code>����, ����Ϊ<b>32</b>λ. ��֤������Ӧ��
     * �������ϵ�Ψһ��
     * @return �����ɵ�����
     */
    public static String nextPrimaryKey() {
        return (String) ServiceProvider.getService(PRIMARY_KEY_BEAN_NAME);
    }
    
    /**
     * ���������µļ�¼ʱ��������ȱʡ����ֵ�����, ��������������ע���µ�����������
     * @return �����µļ�¼ʱ��������ȱʡ����ֵ�����
     */
    public static PropertyPadding getPropertyPadding() {
        return (PropertyPadding) ServiceProvider.getService(PropertyPadding.SERVICE_NAME);
    }
    
    /**
     * ���ر�ʾ��ǰϵͳ�����õ����ݿ��Ʒ����
     * @return ��ǰϵͳ�����õ����ݿ��Ʒ
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
