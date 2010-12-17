package net.silencily.sailing.basic.wf.service.impl;

import java.util.List;

import net.silencily.sailing.basic.wf.service.NapeEditService;
import net.silencily.sailing.framework.core.ContextInfo;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class NapeEditServiceImpl implements NapeEditService{

	private HibernateTemplate hibernateTemplate;
	
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		
		this.hibernateTemplate = hibernateTemplate;
	}

   	public HibernateTemplate getHibernateTemplate() {
		
   		return hibernateTemplate;
	}
   	
   	public List getList(Class c, String nameoper) {
   		ContextInfo.recoverQuery();
        DetachedCriteria d=DetachedCriteria.forClass(c);
        d.add(Restrictions.eq("delFlg","0"));
        d.add(Restrictions.eq("tblWfName",nameoper));
        return hibernateTemplate.findByCriteria(d);
    }
}
