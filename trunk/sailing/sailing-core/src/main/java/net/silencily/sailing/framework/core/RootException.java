package net.silencily.sailing.framework.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

/**
 * <p>ϵͳ�������쳣�ĸ�, ���е�Ӧ���쳣��Ҫ��չ����쳣, �ṩ�ⲿ����Ϣ��֧�ֵ�����ͨ�ù���</p>
 * 
 * @author scottcaptain
 * @since 2005-12-19
 * @version $Id: RootException.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 */
public abstract class RootException extends RuntimeException {
    public RootException() {
        super();
    }
    
    public RootException(String msg) {
        super(msg);
    }
    
    public RootException(Exception e) {
        super(e);
    }
    
    public RootException(String msg, Exception e) {
        super(msg, e);
    }
    
    /**
     * ��������쳣��Ƕ�׵������쳣��Ϣ
     * @return �����쳣��Ϣ������
     */
    public String[] getNestedMessage() {
        Throwable t = null;
        List msgs = new ArrayList(10);
        msgs.add(getMessage());
        while (t != getCause() && (t = getCause()) != null) {
            msgs.add(t.getMessage());
        }
        
        CollectionUtils.select(msgs, new Predicate() {
            public boolean evaluate(Object element) {
                return element != null && StringUtils.isNotBlank((String) element);
            }
        });
        
        return (String[]) msgs.toArray(new String[msgs.size()]);
    }
}
