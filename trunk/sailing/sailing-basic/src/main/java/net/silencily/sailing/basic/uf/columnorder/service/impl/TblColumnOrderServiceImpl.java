package net.silencily.sailing.basic.uf.columnorder.service.impl;

import java.util.List;

import net.silencily.sailing.basic.uf.columnorder.service.TblColumnOrderService;
import net.silencily.sailing.basic.uf.domain.TblUfColumn;
import net.silencily.sailing.basic.uf.domain.TblUfColumnOrder;
import net.silencily.sailing.framework.core.ContextInfo;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;


public class TblColumnOrderServiceImpl implements TblColumnOrderService {

	private HibernateTemplate hibernateTemplate;
	
	public List list() {
		// TODO 自动生成方法存根
		ContextInfo.recoverQuery();
		DetachedCriteria dc = DetachedCriteria.forClass(TblUfColumn.class)
			.add(Restrictions.eq("delFlg", "0"))
			.addOrder(Order.asc("columnFlg.code"))
		    .addOrder(Order.asc("columnNm"));
		
	    return this.hibernateTemplate.findByCriteria(dc); 
	}

	/**
	 * @return hibernateTemplate
	 */
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	/**
	 * @param hibernateTemplate 要设置的 hibernateTemplate
	 */
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	


	public void update(TblUfColumnOrder tuco) {
		// TODO 自动生成方法存根
		this.hibernateTemplate.update(tuco);
	}

	
	public TblUfColumn load(String cid){
		return (TblUfColumn)this.hibernateTemplate.load(TblUfColumn.class , cid);
	}
	
	public TblUfColumnOrder loadOrder(String cid){
		return (TblUfColumnOrder)this.hibernateTemplate.get(TblUfColumnOrder.class , cid);
	}

	public void save(TblUfColumnOrder tuco) {
		// TODO 自动生成方法存根
		this.hibernateTemplate.save(tuco);
	}
}
