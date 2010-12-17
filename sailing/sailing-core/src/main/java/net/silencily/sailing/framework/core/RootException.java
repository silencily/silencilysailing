package net.silencily.sailing.framework.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

/**
 * <p>系统中所有异常的根, 所有的应用异常都要扩展这个异常, 提供外部化信息的支持等其它通用功能</p>
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
     * 返回这个异常所嵌套的所有异常信息
     * @return 包含异常信息的数组
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
