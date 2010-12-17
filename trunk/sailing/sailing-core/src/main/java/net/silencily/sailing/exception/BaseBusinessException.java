package net.silencily.sailing.exception;


/**
 * ϵͳ������<b>ҵ���쳣</b>�ĳ���, ҵ���쳣����ƿ��������п���Ԥ�ϵ��Ĵ������. ���������쳣��
 * �������̵�һ����. �ܹ��쳣�������Ϊ�����쳣����ǡ���Ĵ������, ����������ڴ���֮��Ĵ�����Ϣ
 * @author scott
 * @since 2006-4-1
 * @version $Id: BaseBusinessException.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 *
 */
public abstract class BaseBusinessException extends BaseSystemException {
    public BaseBusinessException() {
        super();
    }
    
    public BaseBusinessException(String key, Object[] params) {
        super(key, params);
    }
    
    public BaseBusinessException(Object[] params) {
        super(params);
    }
}
