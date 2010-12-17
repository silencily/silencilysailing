package net.silencily.sailing.framework.persistent.common;

import java.beans.PropertyDescriptor;

import net.silencily.sailing.framework.utils.DaoHelper;

import org.apache.commons.lang.StringUtils;

/**
 * 用于创建新的记录时自动用<code>GUID</code>填写<code>BaseDto's id</code>属性值, 减少开发
 * 时不必要的麻烦
 * @author scott
 * @since 2006-4-13
 * @version $Id: IdPropertyPaddingStrategy.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 *
 */
public class IdPropertyPaddingStrategy extends AbstractPropertyPaddingStrategy {
    private static final String PROPERTY_NAME = "id";
    
    /** 如果<code>BaseDto's id</code>是<code>null</code>或<code>empty</code>就填写 */
    protected boolean isShouldSet(Object bean, PropertyDescriptor desc, Object result) {
        boolean ret = true;
        if (result != null) {
            String id = String.valueOf(result);
            ret = StringUtils.isBlank(id);
        }
        
        return ret;
    }

    protected Object settingWith() {
        return DaoHelper.nextPrimaryKey();
    }

    protected boolean isDesirableProperty(String propertyName) {
        return PROPERTY_NAME.equals(propertyName);
    }
}
