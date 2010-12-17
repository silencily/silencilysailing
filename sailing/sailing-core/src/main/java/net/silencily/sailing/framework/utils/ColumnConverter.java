package net.silencily.sailing.framework.utils;

import net.silencily.sailing.framework.persistent.filter.DtoMetadata;
import net.silencily.sailing.framework.persistent.filter.impl.DefaultDtoMetadata;

import org.apache.commons.beanutils.Converter;

/**
 * 
 * @author Scott Captain
 * @since 2006-7-19
 * @version $Id: ColumnConverter.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 *
 */
public class ColumnConverter implements Converter {
    private DtoMetadata dtoMetadata = new DefaultDtoMetadata();

    public Object convert(Class clazz, Object value) {
        if (value != null) {
            String columnName = (String) value;
            return dtoMetadata.getColumnName(columnName);
        }

        return null;
    }

}
