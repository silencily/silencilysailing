package net.silencily.sailing.framework.persistent.common;

import java.beans.PropertyDescriptor;

import net.silencily.sailing.framework.utils.DaoHelper;

import org.apache.commons.lang.StringUtils;

/**
 * ���ڴ����µļ�¼ʱ�Զ���<code>GUID</code>��д<code>BaseDto's id</code>����ֵ, ���ٿ���
 * ʱ����Ҫ���鷳
 * @author scott
 * @since 2006-4-13
 * @version $Id: IdPropertyPaddingStrategy.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 *
 */
public class IdPropertyPaddingStrategy extends AbstractPropertyPaddingStrategy {
    private static final String PROPERTY_NAME = "id";
    
    /** ���<code>BaseDto's id</code>��<code>null</code>��<code>empty</code>����д */
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
