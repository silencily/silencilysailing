package net.silencily.sailing.framework.persistent.filter.impl;

import java.util.regex.Pattern;

import net.silencily.sailing.framework.persistent.filter.SqlPopulater;

import org.apache.commons.lang.StringUtils;

/**
 * <code>SqlPopulator</code>�ļ�ʵ��, ���ʵ���Ժ����Ҫʹ��<code>antlr</code>�滻
 * @author scott
 * @since 2006-5-6
 * @version $Id: SimpleSqlPopulater.java,v 1.1 2010/12/10 10:54:21 silencily Exp $
 *
 */
public class SimpleSqlPopulater implements SqlPopulater {
    
    /**
     * ��ԭ��<code>sql</code>�������������������, Ҫ������Ʋ��ܳ��ֱ���, �����Ǹ��Ӳ�ѯ
     * ���, ֻ��������<code>select .... from xxx ...</code>�Ļ�������ȷ����
     */
    public String where(String sql, String clause) {
        String ret = null;
        int pos = -1;
        if (Pattern.matches(".+\\s+where\\s+.+", sql)) {
            pos = sql.indexOf("where");
            String back = sql.substring(0, pos);
            String forward = sql.substring(pos + "where".length());
            ret = new StringBuffer(back).append(" where ").append(clause).append(" and ").append(forward).toString();
        } else if (Pattern.matches(".+\\s+order\\s+by\\s+.+", sql)) {
            String[] flagments = Pattern.compile("\\s+order\\s+by\\s+").split(sql);
            if (flagments.length == 2) {
                ret = new StringBuffer(flagments[0]).append(" where ").append(clause).append(" order by ").append(flagments[1]).toString();
            } else {
                throw new IllegalStateException("�޷���������������order by��sql[" + sql + "]");
            }
        } else {
            ret = new StringBuffer(sql).append(" where ").append(clause).toString();
        }
        
        return ret;
    }

    public String count(String sql, String clause) {
        String result = null;
        if (StringUtils.isNotBlank(clause)) {
            result = COUNT.replaceAll("\\$\\{sql\\}", where(sql, clause));
        } else {
            result = COUNT.replaceAll("\\$\\{sql\\}", sql);
        }
        return result;
    }
}
