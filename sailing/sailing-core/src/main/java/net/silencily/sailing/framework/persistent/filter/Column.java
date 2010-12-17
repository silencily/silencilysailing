package net.silencily.sailing.framework.persistent.filter;

/**
 * 用于查询条件中表示两个列之间关系的值类型, 比如数据库中有两列, <code>finishedTime</code>, 
 * <code>deadline</code>, 这种类型的条件就是类似于<code>finishedTime < deadline</code>
 * 表示值是<code>dto</code>的属性名称
 * @author Scott Captain
 * @since 2006-7-19
 * @version $Id: Column.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 *
 */
public class Column {
    private String columnName;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
}
