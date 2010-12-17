package net.silencily.sailing.container;

import net.silencily.sailing.exception.BaseSystemException;

/**
 * <p>�����쳣, ˵��<code>spring framework xml bean factory</code>û�ж�������<code>
 * bean</code>���û���û�а���Ҫ��������, ͨ����Ҫ��������쳣</p>
 * 
 * @author scott
 * @since 2006-3-16
 * @version $Id: ConfigurationException.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 */
public class ConfigurationException extends BaseSystemException {
    public ConfigurationException(String msg) {
        super(new Object[] {msg});
    }
}
