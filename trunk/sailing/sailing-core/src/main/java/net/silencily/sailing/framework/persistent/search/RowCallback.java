package net.silencily.sailing.framework.persistent.search;

import java.sql.ResultSet;

/**
 * ���ڰ���������ִ�в�ѯ���лص��ӿ�
 * @author scott
 * @since 2006-4-19
 * @version $Id: RowCallback.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 *
 */
public interface RowCallback {
    Object process(ResultSet resultSet, int rowNum);
}
