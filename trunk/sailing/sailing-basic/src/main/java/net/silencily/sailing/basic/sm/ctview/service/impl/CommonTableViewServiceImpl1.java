package net.silencily.sailing.basic.sm.ctview.service.impl;

import java.util.List;

import net.silencily.sailing.basic.sm.ctview.service.CommonTableViewService1;
import net.silencily.sailing.basic.wf.domain.TblWfOperationInfo;
import net.silencily.sailing.common.crud.domain.CommonTableView;
import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.hibernate3.EnhancehibernateTemplatePlus;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;



public class CommonTableViewServiceImpl1 implements CommonTableViewService1{

	private HibernateTemplate hibernateTemplate;

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	//根据id查询
	public CommonTableView findCtViewById(String id) {
		CommonTableView tmp = null;		
		ContextInfo.recoverQuery();
		// 设置查询条件
		DetachedCriteria dc = DetachedCriteria.forClass(CommonTableView.class);
		dc.add(Restrictions.eq("id", id));
		List list = hibernateTemplate.findByCriteria(dc);
		if(list.size()>0){
			tmp = (CommonTableView) list.get(0);
		}
		return tmp;
	}
	//根据name查询
	public CommonTableView findCtViewByName(String name) {
		CommonTableView tmp = null;		
		ContextInfo.recoverQuery();
		// 设置查询条件
		DetachedCriteria dc = DetachedCriteria.forClass(CommonTableView.class);
		dc.add(Restrictions.eq("name", name));
		//dc.addOrder(Order.asc("sortNum"));
		List list = hibernateTemplate.findByCriteria(dc);
		tmp = (CommonTableView) list.get(0);
		return tmp;
	}

	//列表
	public List findAllCtView() {
		ContextInfo.recoverQuery();
		// 设置查询条件
		DetachedCriteria dc = DetachedCriteria.forClass(CommonTableView.class);
		List list = ((EnhancehibernateTemplatePlus) hibernateTemplate).findByCriteria(dc);
		return list;
	}

	// 保存
	public void saveCtView(CommonTableView ctview){
		try {
			getHibernateTemplate().saveOrUpdate(ctview);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	// 删除
	public void deleteCtView(String id){
		CommonTableView ctview = new CommonTableView();
		ctview.setId(id);
		this.getHibernateTemplate().update(ctview);

	}
	
	//复合查询
	public List search(CommonTableView ctview) {	
		//设置查询条件
		DetachedCriteria dc = DetachedCriteria.forClass(TblWfOperationInfo.class);
		/*ContextInfo.recoverQuery();
		String name = wfInfo.getName();
		String operName = wfInfo.getOperName();
		String status = wfInfo.getStatus();
		Double edition = wfInfo.getEdition();
		if(operName!=null && !("").equals(operName)){
			dc.add(Restrictions.eq("operName", operName));
		}
		if(name!=null && !("").equals(name)){
			dc.add(Restrictions.eq("name", name));
		}
		if(edition!=null && !("").equals(edition)){
			dc.add(Restrictions.eq("edition", edition));
		}
		if(status!=null && !("").equals(status)){
			dc.add(Restrictions.eq("status", status));
		}
		dc.add(Restrictions.eq("delFlg", "0"));
		dc.addOrder(Order.desc("createdTime"));*/
		List list = hibernateTemplate.findByCriteria(dc);
		
		return list;	
	}
}
