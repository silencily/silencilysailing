package net.silencily.sailing.basic.sm.dept.service.impl;

import java.util.List;

import net.silencily.sailing.basic.sm.dept.service.TblSmDeptService;
import net.silencily.sailing.basic.sm.domain.TblCmnDept;
import net.silencily.sailing.framework.core.ContextInfo;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;
/**
 * 组织机构维护服务类
 * @author gaojing
 * @version $Id: TblCmnDeptServiceImpl.java,v 1.1 2010/12/10 10:56:44 silencily Exp $
 * @since 2007-8-29
 */
public class TblCmnDeptServiceImpl implements TblSmDeptService {

	/**
	 * 仅仅完成初始化根节点的作用, 不做缓存
	 */
	private TblCmnDept root;

	private HibernateTemplate hibernateTemplate;

	public void setRoot(TblCmnDept root) {
		this.root = root;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

//	public void delete(TblCmnDept config) {
//		this.hibernateTemplate.delete(config);
//	}

	public List list(TblCmnDept config, String radio) {
		ContextInfo.recoverQuery();
		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnDept.class);
		dc.add(Restrictions.eq("parent", config));
		if (radio.equalsIgnoreCase("2")) {
		}
		else {
			dc.add(Restrictions.eq("deptState", radio));
		}
		dc.addOrder(Order.asc("showSequence"));
		return this.hibernateTemplate.findByCriteria(dc);
	}

//	protected List listChange() {
//		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnDept.class).add(Restrictions.eq("deptState", "0"))
//				.addOrder(Order.asc("deptCd"));
//		return this.hibernateTemplate.findByCriteria(dc);
//	}

	public TblCmnDept load(String id) {
		return (TblCmnDept) this.hibernateTemplate.load(TblCmnDept.class, id);
	}

//	public void saveOrUpdate(TblCmnDept config) {
//		this.hibernateTemplate.saveOrUpdate(config);
//	}

	public TblCmnDept newInstance(String parentId) {
		TblCmnDept parent = load(parentId);
		TblCmnDept r = new TblCmnDept();
		//20071219 PHRJGBM01009 START
		//DetachedCriteria dc = DetachedCriteria.forClass(TblCmnDept.class);
		//dc.add(Restrictions.eq("parent", parent)).setProjection(Projections.max("showSequence"));
		//List list = this.hibernateTemplate.findByCriteria(dc);
		//int no = 1;
		//if (list.size() > 0 && list.get(0) != null) {
		//	Integer i = (Integer) list.get(0);
		//	no = i.intValue() + 1;
		//}
		r.setParent(parent);
		//r.setShowSequence(new Integer(no));
		//20071219 PHRJGBM01009 END
		r.setDelFlg("0");
		return r;
	}

	public void init() {
		Object obj = this.hibernateTemplate.get(TblCmnDept.class, TblCmnDept.ROOT_NODE_CODE);
		if (obj == null) {
			this.hibernateTemplate.save(this.root);
		}
	}

//	protected TblCmnDept deptEdit() {
//		return (TblCmnDept) this.hibernateTemplate.loadAll(TblCmnDept.class);
//	}

	public String getParentId(String id) {
		String result = "";
		ContextInfo.recoverQuery();
		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnDept.class);
		dc.add(Restrictions.eq("id", id));
		List l = this.hibernateTemplate.findByCriteria(dc);
		if (l.size() > 0) {
			TblCmnDept hd = (TblCmnDept) l.get(0);
			result = hd.getParent().getId();
		}
		return result;
	}

	public List list() {
		return null;
	}
}
