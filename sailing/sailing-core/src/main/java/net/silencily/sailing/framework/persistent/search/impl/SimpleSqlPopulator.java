package net.silencily.sailing.framework.persistent.search.impl;

import java.sql.SQLException;

import net.silencily.sailing.exception.UnexpectedException;
import net.silencily.sailing.framework.persistent.filter.Paginater;
import net.silencily.sailing.framework.persistent.search.SearchConditionAndParameter;
import net.silencily.sailing.framework.persistent.search.SqlPopulator;
import net.silencily.sailing.framework.utils.DaoHelper;

import org.apache.commons.lang.StringUtils;

/**
 * <code>SqlPopulator</code>的简单实现, 仅仅根据判断字符串来执行合并工作,将来可能采用<code>
 * antlr</code>的实现来替换, 要替换的<code>sql</code>不支持别名, 不支持子查询. 必须是简单的
 * <code>select ... from xxx where xxx order by xxx</code>类似的格式, 这个实现时一个临
 * 时措施
 * @author scott
 * @since 2006-4-23
 * @version $Id: SimpleSqlPopulator.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 *
 */
public class SimpleSqlPopulator implements SqlPopulator {
    private static final String WHERE = "where";
    private static final String FROM = "from";
    private static final String AND = "and";
    private static final String REPLACE_SIGN = "${sql}";
    private static final String COUNT = "select count(*) from (" + REPLACE_SIGN + ") t";
    private static final String MYSQL = "mysql";
    private static final String ORACLE = "oracle";
    private String vendor;
    
    public SimpleSqlPopulator() {
        try {
            //TODO: transfer the fllowing code to SearchService, it becomes more than clean
            vendor = DaoHelper.getJdbcTemplate().getDataSource().getConnection().getMetaData().getDatabaseProductName().toLowerCase();
        } catch (SQLException e) {
            throw new UnexpectedException("不能获得数据库厂商名称", e);
        }
    }

    public SearchConditionAndParameter where(String sql, SearchConditionAndParameter sp) {
        SearchConditionAndParameter scp = copy(sp);
        sql = sql.toLowerCase();
        if (sp == SearchConditionAndParameter.EMPTY_CONDITION) {
            scp.setCondition(sql);
            return scp;
        }
        
        String[] elements = StringUtils.split(sql, ' ');
        StringBuffer sb = new StringBuffer();
        int fromPos = findIndexOfElement(elements, FROM);
        for (int i = 0; i < fromPos + 2; i++) {
            sb.append(elements[i]).append(" ");
        }
        int wherePos = findIndexOfElement(elements, WHERE);
        int remainderPos = -1;
        sb.append(WHERE).append(" ").append(scp.getCondition()).append(" ");
        if (wherePos == -1) {
            remainderPos = fromPos + 2;
        } else {
            sb.append(AND).append(" ");
            remainderPos = wherePos + 1;
        }
        for (int i = remainderPos; i < elements.length; i++) {
            sb.append(elements[i]).append(" ");
        }
        scp.setCondition(sb.toString().trim());
        
        return scp;
    }
    
    private SearchConditionAndParameter copy(SearchConditionAndParameter sp) {
        SearchConditionAndParameter scp = new SearchConditionAndParameter();
        scp.setCondition(sp.getCondition());
        Object[] params = new Object[sp.getParams().length];
        System.arraycopy(sp.getParams(), 0, params, 0, sp.getParams().length);
        scp.setParams(params);
        return scp;
    }

    public SearchConditionAndParameter pagination(Paginater paginater, SearchConditionAndParameter sp) {
        if (paginater == Paginater.NOT_PAGINATED) {
            return copy(sp);
        }
        SearchConditionAndParameter scp = null;
        if (vendor.indexOf(MYSQL) > -1) {
            scp = paginationInMysql(paginater, sp);
        } else if (vendor.indexOf(ORACLE) > -1) {
            scp = paginationInOracle(paginater, sp);
        } else {
            throw new IllegalStateException("不支持这个数据库[" + vendor + "]");
        }

        return scp;
    }

    public SearchConditionAndParameter update(String sql, SearchConditionAndParameter sp) {
        throw new UnsupportedOperationException("don't implements update");
    }

    private int findIndexOfElement(String[] elements, String elementName) {
        for (int i = 0; i < elements.length; i++) {
            if (elementName.equals(elements[i])) {
                return i;
            }
        }
        
        return -1;
    }
    
    private SearchConditionAndParameter paginationInOracle(Paginater paginater, SearchConditionAndParameter sp) {
        throw new UnsupportedOperationException("don't implements paginationInOracle");
    }
    
    private SearchConditionAndParameter paginationInMysql(Paginater paginater, SearchConditionAndParameter sp) {
        String sql = sp.getCondition();
        sql += " LIMIT ?, ?";
        int countOfParams = sp.getParams().length;
        Object[] params = new Object[countOfParams + 2];
        System.arraycopy(sp.getParams(), 0, params, 0, countOfParams);
        params[countOfParams] = new Integer(paginater.getPage() * paginater.getPageSize());
        params[countOfParams + 1] = new Integer(paginater.getPageSize());
        SearchConditionAndParameter scp = new SearchConditionAndParameter();
        scp.setCondition(sql);
        scp.setParams(params);

        return scp;
    }

    public SearchConditionAndParameter count(Paginater paginater, SearchConditionAndParameter sp) {
        String sql = sp.getCondition();
        sql = COUNT.replaceAll("\\$\\{sql\\}", sql);
        SearchConditionAndParameter scp = copy(sp);
        scp.setCondition(sql);
        return scp;
    }
}
