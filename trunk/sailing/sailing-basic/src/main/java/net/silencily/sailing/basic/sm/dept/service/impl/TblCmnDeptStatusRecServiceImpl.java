package net.silencily.sailing.basic.sm.dept.service.impl;

import java.util.List;

import net.silencily.sailing.basic.sm.dept.service.TblSmDeptStatusRecService;
import net.silencily.sailing.basic.sm.domain.TblCmnDeptStatusRec;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * 部门状态变更类
 * @author gaojing
 * @version $Id: TblCmnDeptStatusRecServiceImpl.java,v 1.1 2010/12/10 10:56:44 silencily Exp $
 * @since 2007-8-29
 */
public class TblCmnDeptStatusRecServiceImpl implements TblSmDeptStatusRecService {

	/**
	 * 仅仅完成初始化根节点的作用, 不做缓存
	 */
	private HibernateTemplate hibernateTemplate;

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

//	public void saveOrUpdate(TblCmnDeptStatusRec config) {
//		this.hibernateTemplate.saveOrUpdate(config);
//	}

//	public List list() {
//		ContextInfo.recoverQuery();
//		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnDeptStatusRec.class);
//		dc.addOrder(Order.asc("id"));
//		return this.hibernateTemplate.findByCriteria(dc);
//	}

	public TblCmnDeptStatusRec load(String id) {
		TblCmnDeptStatusRec status = null;
		try {
			status = (TblCmnDeptStatusRec) this.hibernateTemplate.load(TblCmnDeptStatusRec.class, id);
		}
		catch (DataAccessException e) {
			e.printStackTrace();
		}
		return status;
	}

	public TblCmnDeptStatusRec newInstance(String parentId) {
		TblCmnDeptStatusRec parent = load(parentId);
		TblCmnDeptStatusRec r = new TblCmnDeptStatusRec();
		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnDeptStatusRec.class).add(
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
}
