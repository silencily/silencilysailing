/*
 * Copyright 2004-2005 wangz.
 * Project shufe_newsroom
 */
package net.silencily.sailing.framework.spring;

import java.sql.SQLException;

import net.silencily.sailing.framework.utils.ExceptionChainRootCauseAware;
import net.silencily.sailing.framework.utils.ExceptionChainRootCauseAwareSupport;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * <class>DataAccessExceptionTranslator</class> 用于从 {@link org.springframework.dao.DataAccessException}
 * 得到原始的 {@link java.sql.SQLException}, 如果 Exception Chain 的顶层不是 SQLException, 将返回 null,
 * 此 Solution 是 {@link org.springframework.jdbc.support.SQLExceptionTranslator} 的逆操作
 * @see org.springframework.jdbc.support.SQLExceptionTranslator
 * @see org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator
 * @see org.springframework.jdbc.support.SQLStateSQLExceptionTranslator
 * @see org.summerfragrance.util.ExceptionChainRootCauseAwareSupport
 * @since 2005-8-1
 * @author 王政
 * @version $Id: DataAccessExceptionTranslator.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 */
public class DataAccessExceptionTranslator {
        
    /**
     * 返回最初的 SQLException, 如果 rootCause 不是 SQLException, 则返回 null
     * @param dataAccessException 
     * @return root cause in SQLException
     */
    public static SQLException getOrignalSQLException(DataAccessException dataAccessException) {
        ExceptionChainRootCauseAware aware = new ExceptionChainRootCauseAwareSupport();
        Throwable t = aware.getRootCause(dataAccessException);
        return t instanceof SQLException ? (SQLException) t : null;
    }
    
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        DataAccessException e = new DataIntegrityViolationException("Error", new SQLException("Sql Exception", "22", 100));
        SQLException exception = DataAccessExceptionTranslator.getOrignalSQLException(e);
        if (exception == null) {
            return;
        }
        System.out.println("Error Code : " + exception.getErrorCode());
        System.out.println("Message : " + exception.getMessage());
        System.out.println("SQL State : " + exception.getSQLState());
        System.out.println("Next Exception : " + exception.getNextException());
    }

}



