package net.silencily.sailing.framework.codename;

import net.silencily.sailing.exception.BaseSystemException;

/**
 * 不能找到映射的业务实体类型, 用于一个系统中业务使用<code>CodeName</code>机制隔离对另外
 * 一个系统中实体的引用时没有找到要引用的实体<code>Class</code>, 使用这种方案的应用一定要
 * 捕获这个异常, 大多数情况下可能是由于没有部署另外的子系统而引起的
 * @author zhangli
 * @version $Id: MappedEntityTypeNotFound.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 * @since 2007-6-9
 */
public class MappedEntityTypeNotFound extends BaseSystemException {
    
    public MappedEntityTypeNotFound(String msg, ClassNotFoundException ex) {
        super(msg, ex);
    }
}
