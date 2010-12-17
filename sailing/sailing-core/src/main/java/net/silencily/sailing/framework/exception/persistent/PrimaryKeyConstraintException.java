package net.silencily.sailing.framework.exception.persistent;

import org.springframework.dao.DataIntegrityViolationException;

/**
 * 持久化层异常, 通常在访问数据库时出现主键或唯一键重复时抛出这个异常. 开发人员应该捕获并使用更加
 * 具体的信息包装
 * 
 * @author scott
 * @since 2006-2-9
 * @version $Id: PrimaryKeyConstraintException.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 *
 */
public class PrimaryKeyConstraintException extends DataIntegrityViolationException implements DatabaseException {
    
    private String sql;
    
    private String code = "1";

    public PrimaryKeyConstraintException(String msg, String sql, Throwable ex) {
        super(msg, ex);
        this.sql = sql;
    }
    
    public String getSql() {
        return this.sql;
    }

    public String getSqlCode() {
        return code;
    }
}
