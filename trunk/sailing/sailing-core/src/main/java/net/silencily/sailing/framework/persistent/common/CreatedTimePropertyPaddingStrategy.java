package net.silencily.sailing.framework.persistent.common;

import java.beans.PropertyDescriptor;
import java.util.Date;

/**
 * �����ڴ����µļ�¼ʱ��д<code>createdTime</code>������, ע�����ʱ����<code>JVM</code>
 * ��ʱ��, ����и��ϸ��Ҫ��, ����ʹ�����ݿ�ʱ��, ����<code>fetchCurrentTime</code>����
 * @author scott
 * @since 2006-4-13
 * @version $Id: CreatedTimePropertyPaddingStrategy.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 *
 */
public class CreatedTimePropertyPaddingStrategy extends AbstractPropertyPaddingStrategy {
    private static final String PROPERTY_NAME = "createdTime";
    
    protected boolean isShouldSet(Object bean, PropertyDescriptor desc, Object result) {
        return result == null;
    }

    protected Object settingWith() {
        return fetchCurrentTime();
    }

    protected boolean isDesirableProperty(String propertyName) {
        return PROPERTY_NAME.equals(propertyName);
    }
    
    protected Date fetchCurrentTime() {
        return net.silencily.sailing.utils.DBTimeUtil.getDBTime();
    }

}
