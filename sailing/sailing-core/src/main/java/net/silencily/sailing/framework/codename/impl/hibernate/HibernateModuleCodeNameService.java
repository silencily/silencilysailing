package net.silencily.sailing.framework.codename.impl.hibernate;

import java.util.Collection;
import java.util.Collections;

import net.silencily.sailing.framework.codename.CodeName;
import net.silencily.sailing.framework.codename.impl.AbstractModuleCodeNameService;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * 代码名称服务的<code>hibernate</code>实现
 * @author zhangli
 * @since 2007-3-21
 * @version $Id: HibernateModuleCodeNameService.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 */
public class HibernateModuleCodeNameService extends AbstractModuleCodeNameService {
    private HibernateTemplate hibernateTemplate;
    
    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    protected Collection listEntity(Class clazz) {
        Collection c = hibernateTemplate.findByCriteria(order(clazz));
        return c.size() > 0 ? c : Collections.EMPTY_LIST;
    }

    protected CodeName loadEntity(Class clazz, String code) {
        return (CodeName) hibernateTemplate.get(clazz, code);
    }

    protected Collection listEntity(Class clazz, String parentCode) {
        DetachedCriteria dc = order(clazz)
            .createCriteria("parent")
            .add(Restrictions.eq("code", parentCode));
        Collection c = hibernateTemplate.findByCriteria(dc);
        return c.size() > 0 ? c : Collections.EMPTY_LIST;
    }
    
    private DetachedCriteria order(Class clazz) {
        return DetachedCriteria.forClass(clazz)
        .addOrder(Order.asc("sequenceNo"));
    }
}
