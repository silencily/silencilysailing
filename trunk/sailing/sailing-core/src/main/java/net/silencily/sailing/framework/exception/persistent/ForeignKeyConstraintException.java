package net.silencily.sailing.framework.exception.persistent;

import org.springframework.dao.DataIntegrityViolationException;

/**
 * 持久化层异常, 通常用于访问数据库时, 创建子表记录找不到主表的键或操作主表记录时因为从表外键约束
 * 而无法删除/修改数据
 * 
 * @author scott
 * @since 2006-2-9
 * @version $Id: ForeignKeyConstraintException.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 *
 */
public class ForeignKeyConstraintException extends DataIntegrityViolationException implements DatabaseException {
    
    private String sql;
    
    private String code = "2292";

    public ForeignKeyConstraintException(String msg, String sql, Throwable ex) {
        super(msg, ex);
        this.sql = sql;
    }
    
    public String getSql() {
        return sql;
    }

    public String getSqlCode() {
        return code;
    }
}
