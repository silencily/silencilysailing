package net.silencily.sailing.framework.utils;

import java.util.Date;

import net.silencily.sailing.utils.DateUtils;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang.StringUtils;

/**
 * ȱʡ������ת����, ʵ���˴��ַ���ת����<code>Date</code>����, ���������ʵ�ִ�<code>Date</code>
 * ����<code>java.sql.Date</code>,<code>java.sql.Timestamp</code>����
 * @author scott
 * @since 2006-4-17
 * @version $Id: DateConverter.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 *
 */
public class DateConverter implements Converter {
    public final Object convert(Class type, Object value) {
        Date d = toDate(value);
        return cast(d);
    }
    
    protected Date cast(Date d) {
        return d;
    }
    
    private Date toDate(Object value) {
        if (value == null || (value instanceof String) && StringUtils.isBlank((String) value)) {
            return null;
        }
        
        if (value instanceof Date) {
            return (Date) value;
        }
        
        if (!(value instanceof String)) {
            throw new IllegalArgumentException(value + "����DateҲ����String");
        }
        
        String str = (String) value;
        return DateUtils.parse(str);
    }
}
