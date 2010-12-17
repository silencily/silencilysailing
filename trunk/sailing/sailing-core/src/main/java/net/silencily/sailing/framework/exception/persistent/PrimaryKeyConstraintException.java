package net.silencily.sailing.framework.exception.persistent;

import org.springframework.dao.DataIntegrityViolationException;

/**
 * �־û����쳣, ͨ���ڷ������ݿ�ʱ����������Ψһ���ظ�ʱ�׳�����쳣. ������ԱӦ�ò���ʹ�ø���
 * �������Ϣ��װ
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
