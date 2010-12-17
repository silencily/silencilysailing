package net.silencily.sailing.basic.wf.service.impl;

import net.silencily.sailing.basic.wf.service.OperationService;

import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * @author ¸ð½¨±¦
 *
 */
public class OperationServiceImpl implements OperationService {
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
	 * @see com.qware.wf.service.OperationService#load(java.lang.Class, java.lang.String)
	 */
	public Object load(Class c, String id) {
		
		return this.hibernateTemplate.load(c, id);
	}

}
