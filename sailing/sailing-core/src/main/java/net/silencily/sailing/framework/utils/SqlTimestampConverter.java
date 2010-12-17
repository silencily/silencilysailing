package net.silencily.sailing.framework.utils;

import java.sql.Timestamp;
import java.util.Date;


/**
 * 实现从字符串转换成<code>java.sql.Timestamp</code>, 但是损失了<code>nano-second</code>
 * @author scott
 * @since 2006-4-17
 * @version $Id: SqlTimestampConverter.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 *
 */
public class SqlTimestampConverter extends DateConverter {
    protected Date cast(Date d) {
        return new Timestamp(d.getTime());
    }
}
