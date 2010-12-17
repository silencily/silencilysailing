package net.silencily.sailing.basic.uf.im.service.impl;

import java.util.List;

import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.basic.uf.im.service.IMService;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * 
 * @author tongjq
 * @version 1.0
 */
public class IMServiceImpl implements IMService {
    private HibernateTemplate hibernateTemplate;

    /**
     *
     * @return hibernateTemplate the hibernateTemplate to get
     */
    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    /**
     *
     * @param hibernateTemplate
     *            the hibernateTemple to set
     */
    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    /* (non-Javadoc)
     * @see com.qware.uf.im.service.IMService#list(java.lang.String)
     */
    public List list() {
        // TODO Auto-generated method stub
        DetachedCriteria criteria = DetachedCriteria.forClass(TblCmnUser.class);
        criteria.add(Restrictions.eq("delFlg","0"));
        criteria.createAlias("emp","emp");
        criteria.add(Restrictions.eq("emp.delFlg", "0"));
        return this.hibernateTemplate.findByCriteria(criteria);
    }

    /* (non-Javadoc)
     * @see com.qware.uf.im.service.IMService#load(java.lang.String)
     */
    public TblCmnUser find(String empCd) {
        // TODO Auto-generated method stub
        DetachedCriteria criteria = DetachedCriteria.forClass(TblCmnUser.class);
        criteria.add(Restrictions.eq("delFlg","0"));
        criteria.createAlias("emp","emp");
        criteria.add(Restrictions.eq("emp.empCd", empCd));
        criteria.add(Restrictions.eq("emp.delFlg", "0"));

        List list = this.hibernateTemplate.findByCriteria(criteria);
        if (list.isEmpty()) {
            return null;
        }
        return (TblCmnUser) list.get(0);
    }

    /*
     * (non-Javadoc)
     * @see com.qware.uf.im.service.IMService#update(java.lang.Object)
     */
    public void update(Object obj) {
        hibernateTemplate.update(obj);
    }

}
