package net.silencily.sailing.framework.exception.persistent;

import java.sql.SQLException;

import org.springframework.dao.DataAccessException;

/**
 * @author zhangli
 * @version $Id: InsertedValueTooLongException.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 * @since 2007-5-28
 */
public class InsertedValueTooLongException extends DataAccessException implements DatabaseException {
    public static final String CODE = "1461";
    private String sql;
    
    public InsertedValueTooLongException(String msg, String sql, SQLException ex) {
        super(msg, ex);
        this.sql = sql;
    }
    
    public String getSql() {
        return sql;
    }

    public String getSqlCode() {
        return CODE;
    }
}
