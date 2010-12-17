package net.silencily.sailing.framework.persistent.filter;

/**
 * ƴװ<code>sql</code>����<code>utiltity</code>
 * @author scott
 * @since 2006-5-6
 * @version $Id: SqlPopulater.java,v 1.1 2010/12/10 10:54:17 silencily Exp $
 *
 */
public interface SqlPopulater {
    /** following, went without saying */
    String WHERE = "where";
    
    String FROM = "from";
    
    String AND = "and";
    
    String REPLACE_SIGN = "${sql}";
    
    String COUNT = "select count(*) from (" + REPLACE_SIGN + ") t";
    
    /**
     * �Ѳ���ָ���������Ӿ�ƴ�ӵ�<code>sql</code>����<code>where</code>����
     * @param sql       <code>sql</code>���
     * @param clause    Ҫƴ�ӵ������Ӿ�
     * @return          ƴ�Ӻ��<code>sql</code>���
     */
    String where(String sql, String clause);
    
    /**
     * ƴ��ͳ�Ƽ�¼������<code>sql</code>, �����ļ�����Ӧ���������Ӿ�֮��
     * @param sql       ��ѯ���ݵ�<code>sql</code>
     * @param clause    �����Ӿ�
     * @return          ͳ�ƻ��������ļ�¼������<code>sql</code>
     */
    String count(String sql, String clause);
}
