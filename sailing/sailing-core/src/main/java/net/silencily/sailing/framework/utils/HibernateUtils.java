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
 * 方便<code>Hibernate</code>方便的一组方法
 * @author zhangli
 * @version $Id: HibernateUtils.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 * @since 2007-4-14
 */
public abstract class HibernateUtils {
    
    public static final int MAX_SIZE_IN_LIST = 500;
    
    /** 当<code>sql's in</code>操作的值列表是空列表时用这个值代替值列表 */
    public static final String[] EMPTY_LIST = new String[] {""};

    /**
     * 安全执行数据库<code>in</code>操作, 其它数据库未测试,在<code>ORACLE</code>中<code>in</code>
     * 操作的数据列表不能多余<code>1000</code>
     * @param propertyName 用于<code>in</code>操作的属性名
     * @param list         用于<code>in</code>操作的列表
     * @return 可以安全执行的<code>Restrictions.IN</code>
     * @throws NullPointerException 任何一个参数是<code>null</code>或<code>empty string</code>
     */
    public static Criterion safeIn(String propertyName, Object[] list) {
        return safeIn(propertyName, Arrays.asList(list));
    }
    
    /**
     * 安全执行数据库<code>in</code>操作, 其它数据库未测试,在<code>ORACLE</code>中<code>in</code>
     * 操作的数据列表不能多余<code>1000</code>
     * @param propertyName 用于<code>in</code>操作的属性名
     * @param list         用于<code>in</code>操作的列表
     * @return 可以安全执行的<code>Restrictions.IN</code>
     * @throws NullPointerException 任何一个参数是<code>null</code>或<code>empty string</code>
     */
    public static Criterion safeIn(String propertyName, Collection list) {
        if (StringUtils.isBlank(propertyName) || list == null) {
            throw new NullPointerException("参数是空值");
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
