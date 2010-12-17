package net.silencily.sailing.framework.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;

/**
 * ����<code>Hibernate</code>�����һ�鷽��
 * @author zhangli
 * @version $Id: HibernateUtils.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 * @since 2007-4-14
 */
public abstract class HibernateUtils {
    
    public static final int MAX_SIZE_IN_LIST = 500;
    
    /** ��<code>sql's in</code>������ֵ�б��ǿ��б�ʱ�����ֵ����ֵ�б� */
    public static final String[] EMPTY_LIST = new String[] {""};

    /**
     * ��ȫִ�����ݿ�<code>in</code>����, �������ݿ�δ����,��<code>ORACLE</code>��<code>in</code>
     * �����������б��ܶ���<code>1000</code>
     * @param propertyName ����<code>in</code>������������
     * @param list         ����<code>in</code>�������б�
     * @return ���԰�ȫִ�е�<code>Restrictions.IN</code>
     * @throws NullPointerException �κ�һ��������<code>null</code>��<code>empty string</code>
     */
    public static Criterion safeIn(String propertyName, Object[] list) {
        return safeIn(propertyName, Arrays.asList(list));
    }
    
    /**
     * ��ȫִ�����ݿ�<code>in</code>����, �������ݿ�δ����,��<code>ORACLE</code>��<code>in</code>
     * �����������б��ܶ���<code>1000</code>
     * @param propertyName ����<code>in</code>������������
     * @param list         ����<code>in</code>�������б�
     * @return ���԰�ȫִ�е�<code>Restrictions.IN</code>
     * @throws NullPointerException �κ�һ��������<code>null</code>��<code>empty string</code>
     */
    public static Criterion safeIn(String propertyName, Collection list) {
        if (StringUtils.isBlank(propertyName) || list == null) {
            throw new NullPointerException("�����ǿ�ֵ");
        }
        if (list.size() == 0) {
            return Restrictions.in(propertyName, EMPTY_LIST);
        }
        List lists = new ArrayList(list.size());
        lists.addAll(list);
        Collection criterions = new ArrayList();
        splitInList(criterions, propertyName, lists);
        Disjunction ret = Restrictions.disjunction();
        for (Iterator it = criterions.iterator(); it.hasNext(); ) {
            ret.add((Criterion) it.next());
        }
        return ret;
    }
    
    private static void splitInList(Collection criterions, String propertyName, List list) {
        if (list.size() > MAX_SIZE_IN_LIST) {
            criterions.add(Restrictions.in(propertyName, list.subList(0, MAX_SIZE_IN_LIST)));
            splitInList(criterions, propertyName, list.subList(MAX_SIZE_IN_LIST, list.size()));
        } else {
            criterions.add(Restrictions.in(propertyName, list));
        }
    }
}
