package net.silencily.sailing.basic.sm.datapermission.service.impl;

import java.util.List;

import net.silencily.sailing.basic.sm.datapermission.service.TblDataPermissionService;
import net.silencily.sailing.basic.sm.domain.TblCmnEntity;
import net.silencily.sailing.framework.core.ContextInfo;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;


public class TblDataPermissionServiceImpl implements  TblDataPermissionService{
	
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
	
	
	public List list(Class c, String oid) {
		ContextInfo.recoverQuery();
		DetachedCriteria dc = DetachedCriteria.forClass(c);
		dc.add(Restrictions.eq("tblCmnEntity.id", oid));
		dc.add(Restrictions.eq("delFlg", "0"));
		dc.addOrder(Order.asc("id"));
		List list = this.hibernateTemplate.findByCriteria(dc);
		return list;
	}
	
	public List getList(Class c, String oid) {
		ContextInfo.recoverQuery();
		DetachedCriteria dc = DetachedCriteria.forClass(c);
		dc.add(Restrictions.eq("tblCmnEntityMember.id", oid));
		dc.add(Restrictions.eq("delFlg", "0"));
		dc.addOrder(Order.asc("id"));
		List list = this.hibernateTemplate.findByCriteria(dc);
		return list;
	}
	
    public List list(TblCmnEntity config, String radio) {
        // TODO Auto-generated method stub
        ContextInfo.recoverQuery();
        DetachedCriteria dc = DetachedCriteria.forClass(TblCmnEntity.class);
//        dc.add(Restrictions.eq("fatherModule", config));
        dc.addOrder(Order.asc("name"));
        dc.add(Restrictions.eq("delFlg","0"));
        
        return hibernateTemplate.findByCriteria(dc);
    }
    
}
