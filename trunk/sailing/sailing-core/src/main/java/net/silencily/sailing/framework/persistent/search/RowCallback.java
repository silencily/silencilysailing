package net.silencily.sailing.framework.persistent.search;

import java.sql.ResultSet;

/**
 * 用于按检索条件执行查询的行回调接口
 * @author scott
 * @since 2006-4-19
 * @version $Id: RowCallback.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 *
 */
public interface RowCallback {
    Object process(ResultSet resultSet, int rowNum);
}
