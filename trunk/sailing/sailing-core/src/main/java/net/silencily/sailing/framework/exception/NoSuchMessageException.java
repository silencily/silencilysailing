package net.silencily.sailing.framework.exception;

/**
 * 当使用指定的<code>code</code>检索编码信息时, 没有找到对应的信息抛出这个异常
 * 
 * @author scott
 * @since 2006-1-9
 * @version $Id: NoSuchMessageException.java,v 1.1 2010/12/10 10:54:28 silencily Exp $
 *
 */
@SuppressWarnings("serial")
public class NoSuchMessageException extends RuntimeException {
    private String code;
    
    public NoSuchMessageException(String code) {
        super("编码[" + code + "]没有定义信息");
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
}
