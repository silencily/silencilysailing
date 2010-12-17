package net.silencily.sailing.basic.sm.dept.service.impl;

import java.util.List;

import net.silencily.sailing.basic.sm.dept.service.TblSmDeptPareRecService;
import net.silencily.sailing.basic.sm.domain.TblCmnDeptPareRec;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateTemplate;


/**
 * 上级部门变更服务类
 * @author gaojing
 * @version $Id: TblCmnDeptPareRecServiceImpl.java,v 1.2 2007/09/27 13:45:29 gaoj
 * Exp $
 * @since 2007-8-29
 */
public class TblCmnDeptPareRecServiceImpl implements TblSmDeptPareRecService {

	/**
	 * 仅仅完成初始化根节点的作用, 不做缓存
	 */
	private HibernateTemplate hibernateTemplate;

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

//	public void saveOrUpdate(TblCmnDeptPareRec config) {
//		this.hibernateTemplate.saveOrUpdate(config);
//	}

//	public List list() {
//		ContextInfo.recoverQuery();
//		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnDeptPareRec.class);
//		dc.addOrder(Order.asc("id"));
//		return this.hibernateTemplate.findByCriteria(dc);
//	}

	public TblCmnDeptPareRec load(String id) {
		TblCmnDeptPareRec pare = null;
		try {
			pare = (TblCmnDeptPareRec) this.hibernateTemplate.load(TblCmnDeptPareRec.class, id);
		}
		catch (DataAccessException e) {
			e.printStackTrace();
		}
		return pare;
	}

	public TblCmnDeptPareRec newInstance (String parentId) {
		TblCmnDeptPareRec parent = load(parentId);
		TblCmnDeptPareRec r = new TblCmnDeptPareRec();
		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnDeptPareRec.class).add(
				Restrictions.eq("parent", "changeid")).setProjection(Projections.max("changeid"));
		List list = this.hibernateTemplate.findByCriteria(dc);
		int no = 1;
		if (list.size() > 0 && list.get(0) != null) {
			Integer i = (Integer) list.get(0);
			no = i.intValue() + 1;
		}
		r.setSequenceNo(new Integer(no));
		r.setDelFlg("0");
		return r;
	}

	public void init() {

	}
}
