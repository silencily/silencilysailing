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
 * <class>DataAccessExceptionTranslator</class> ���ڴ� {@link org.springframework.dao.DataAccessException}
 * �õ�ԭʼ�� {@link java.sql.SQLException}, ��� Exception Chain �Ķ��㲻�� SQLException, ������ null,
 * �� Solution �� {@link org.springframework.jdbc.support.SQLExceptionTranslator} �������
 * @see org.springframework.jdbc.support.SQLExceptionTranslator
 * @see org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator
 * @see org.springframework.jdbc.support.SQLStateSQLExceptionTranslator
 * @see org.summerfragrance.util.ExceptionChainRootCauseAwareSupport
 * @since 2005-8-1
 * @author ����
 * @version $Id: DataAccessExceptionTranslator.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 */
public class DataAccessExceptionTranslator {
        
    /**
     * ��������� SQLException, ��� rootCause ���� SQLException, �򷵻� null
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



