package net.silencily.sailing.framework.utils;

import java.util.Date;


/**
 * 实现从字符串转换成<code>java.sql.Date</code>
 * @author scott
 * @since 2006-4-17
 * @version $Id: SqlDateConverter.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 *
 */
public class SqlDateConverter extends DateConverter {
    protected Date cast(Date d) {
        return new java.sql.Date(d.getTime());
    }
}
