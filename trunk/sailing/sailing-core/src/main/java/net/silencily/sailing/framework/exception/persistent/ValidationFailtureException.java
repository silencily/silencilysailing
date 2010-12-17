package net.silencily.sailing.framework.exception.persistent;

import net.silencily.sailing.exception.BaseBusinessException;
import net.silencily.sailing.framework.persistent.Validatable;

/**
 * {@link Validatable}��֤ҵ��ʵ��״̬����ʱ�׳�����쳣
 * @author zhangli
 * @version $Id: ValidationFailtureException.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 * @since 2007-4-7
 */
public class ValidationFailtureException extends BaseBusinessException {
    
    public ValidationFailtureException(String key, Object[] params) {
        super(key, params);
    }
    
    public ValidationFailtureException(Object[] params) {
        super(params);
    }
}
