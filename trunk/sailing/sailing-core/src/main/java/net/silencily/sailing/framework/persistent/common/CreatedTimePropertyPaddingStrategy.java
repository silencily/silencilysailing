package net.silencily.sailing.framework.persistent.common;

import java.beans.PropertyDescriptor;
import java.util.Date;

/**
 * 用于在创建新的记录时填写<code>createdTime</code>的属性, 注意这个时间是<code>JVM</code>
 * 的时间, 如果有更严格的要求, 比如使用数据库时间, 覆盖<code>fetchCurrentTime</code>方法
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
