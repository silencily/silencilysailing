package net.silencily.sailing.basic.sm.perameter.service.impl;

import net.silencily.sailing.basic.sm.domain.TblCmnSysParameter;
import net.silencily.sailing.basic.sm.perameter.service.ParameterManageService;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;


public class ParameterManageServiceImpl implements ParameterManageService{
	
	private HibernateTemplate hibernateTemplate;
	
	/**
	 * �������� ���ݲ�����ʶ���ϵͳ����ʵ��
	 */
	public TblCmnSysParameter getTblSysParameter(String parameterSign) {
		DetachedCriteria dc  = DetachedCriteria.forClass(TblCmnSysParameter.class)
			.add(Restrictions.eq("delFlg", "0"))
			.add(Restrictions.eq("parameterSign", parameterSign))
			.addOrder(Order.desc("createdTime"));
		if(this.hibernateTemplate.findByCriteria(dc).iterator().hasNext()){
			 return (TblCmnSysParameter)this.hibernateTemplate.findByCriteria(dc).iterator().next(); 
		}else{
			return null;
		}
	}
	
	
    /**
     * 
     * �������� ���ݲ�����ʶ��ò���ֵ.
     * @param parameterSign
     * @return
     * 2008-1-14 ����01:55:49
     * @version 1.0
     * @author wanghy
     */
	public String getSysParameter(String parameterSign) {
		DetachedCriteria dc  = DetachedCriteria.forClass(TblCmnSysParameter.class)
			.add(Restrictions.eq("delFlg", "0"))
			.add(Restrictions.eq("parameterSign", parameterSign))
			.addOrder(Order.desc("createdTime"));
		if(this.hibernateTemplate.findByCriteria(dc).iterator().hasNext()){
			TblCmnSysParameter sysPar= (TblCmnSysParameter)this.hibernateTemplate.findByCriteria(dc).iterator().next();
		    return  sysPar.getParameterValue();
		}else{
			return null;
		}

	}


	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}


	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
}
