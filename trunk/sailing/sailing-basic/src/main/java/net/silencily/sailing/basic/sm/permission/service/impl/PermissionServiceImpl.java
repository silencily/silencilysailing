package net.silencily.sailing.basic.sm.permission.service.impl;

import java.util.List;

import net.silencily.sailing.basic.sm.domain.TblCmnPermission;
import net.silencily.sailing.basic.sm.domain.TblCmnRole;
import net.silencily.sailing.basic.sm.domain.TblCmnRolePermission;
import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.basic.sm.domain.TblCmnUserPermission;
import net.silencily.sailing.basic.sm.domain.TblCmnUserRole;
import net.silencily.sailing.basic.sm.permission.service.PermissionService;
import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.hibernate3.DBHelper;

import org.hibernate.SQLQuery;
import org.hibernate.classic.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;


public class PermissionServiceImpl implements PermissionService {
	
	/**
	 * 仅仅完成初始化根节点的作用, 不做缓存
	 */
	private TblCmnPermission initRoot;

	private HibernateTemplate hibernateTemplate;

	public void setInitRoot(TblCmnPermission initRoot) {
		this.initRoot = initRoot;
	}
	public TblCmnPermission getInitRoot() {
		return this.initRoot;
	}
	
	public TblCmnPermission getRoot()
	{
		TblCmnPermission root = 
			(TblCmnPermission)hibernateTemplate.load(TblCmnPermission.class, initRoot.getId());
		//如果数据库中没有根节点则创建根节点
		if(null == root)
		{
			root = initRoot;
			hibernateTemplate.save(root);
		}
		return root;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}


	public List getChildren(String oid){
		ContextInfo.recoverQuery();
		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnPermission.class);
		dc.createCriteria("father", "father");
		dc.add(Restrictions.eq("father.id", oid));
		dc.add(Restrictions.eq("delFlg", "0"));
		dc.addOrder(Order.asc("displayOrder"));
		return this.hibernateTemplate.findByCriteria(dc);
	}
	
	public List getChildren(String oid,String[] nodetypes){
		ContextInfo.recoverQuery();
		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnPermission.class);
		dc.createCriteria("father", "father");
		dc.add(Restrictions.eq("father.id", oid));
		dc.add(Restrictions.eq("delFlg", "0"));
		dc.add(Restrictions.in("nodetype", nodetypes));
		dc.addOrder(Order.asc("displayOrder"));
		
		return this.hibernateTemplate.findByCriteria(dc);
	}

	/**
	 * 根据页面的条件，从父节点开始查询父节点及其所有子节点下的记录
	 * 支持分页
	 * 功能描述
	 * @param foid
	 * @return
	 * 2007-12-6 下午04:45:44
	 * @version 1.0
	 * @author yushn
	 */
	public List queryStartWithFatherId(String foid,String fname)
	{	
		StringBuffer sql = new StringBuffer();
		sql.append("select * from TBL_CMN_PERMISSION where father_id in(");
		sql.append("select id from TBL_CMN_PERMISSION where CONNECT_BY_ISLEAF = 0");
		sql.append(" and displayname like '%");
		sql.append(fname);
		sql.append("%'");
		sql.append(" connect by prior id=father_id start with id='");
		sql.append(foid);
		sql.append("')");
		return DBHelper.findBySqlExtendFromAutoCondition(this.hibernateTemplate,sql.toString(),TblCmnPermission.class);
	}

	public TblCmnPermission load(String id) {
		return (TblCmnPermission) this.hibernateTemplate.load(TblCmnPermission.class, id);
	}


	public void init() {
		Object obj = this.hibernateTemplate.get(TblCmnPermission.class, initRoot.getId());
		if (obj == null) {
			this.hibernateTemplate.save(this.initRoot);
		}
	}


	public List list() {
		return null;
	}
	
	/**
	 * 
	 * 功能描述　委托权限取得
	 * @param consignerBean
	 * @param userBean
	 * @return
	 * 2007-11-28 下午06:19:58
	 * @version 1.0
	 * @author wanghy
	 */
	public List getUserPermissionConsignedUser(TblCmnUser consignerBean, TblCmnUser userBean) {
		
		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnUserPermission.class);
			dc.add(Restrictions.eq("tblCmnUserByConsignerId", consignerBean));
			dc.add(Restrictions.eq("tblCmnUserByUserId", userBean));
			dc.add(Restrictions.eq("delFlg", "0"));
		return this.hibernateTemplate.findByCriteria(dc);
	}
	
	/**
	 * 
	 * 功能描述 取角色权限关联
	 * @param role
	 * @param strFlg
	 * @return
	 * 2007-12-5 下午06:06:27
	 * @version 1.0
	 * @author wanghy
	 */
	public List getRolePermision(TblCmnPermission bean) {
			DetachedCriteria dc = DetachedCriteria.forClass(TblCmnRolePermission.class);
			dc.add(Restrictions.eq("tblCmnPermission", bean));
			dc.add(Restrictions.eq("delFlg","0"));
			return this.hibernateTemplate.findByCriteria(dc);
		}
	
	/**
	 * 
	 * 功能描述 取用户权限关联
	 * @param role
	 * @param strFlg
	 * @return
	 * 2007-12-5 下午06:06:27
	 * @version 1.0
	 * @author wanghy
	 */
	public List getUserPermision(TblCmnPermission bean) {
		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnUserPermission.class);
		dc.add(Restrictions.eq("tblCmnPermission", bean));
		dc.add(Restrictions.eq("delFlg","0"));
		return this.hibernateTemplate.findByCriteria(dc);
	}
	
	/**
	 * 
	 * 功能描述 判定同一个父节点下数据项权限标识唯一
	 * @param role
	 * @param strFlg
	 * @return
	 * 2007-12-5 下午06:06:27
	 * @version 1.0
	 * @author wanghy
	 */
	public List onlyOneFlg(String permissionCd, String fatherId) {
		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnPermission.class);
			dc.add(Restrictions.eq("permissionCd", permissionCd));
			dc.add(Restrictions.eq("delFlg", "0"));
			dc.createAlias("father", "father");
			dc.add(Restrictions.eq("father.id", fatherId));
		return this.hibernateTemplate.findByCriteria(dc);
	}

	
	public boolean isSubNode(String fatherid ,String subid){
		StringBuffer basesql = new StringBuffer();
		basesql.append("select rownum");
		basesql.append(" from (select id");
		basesql.append(" from tbl_cmn_permission");
		basesql.append(" connect by prior id = father_id");
		basesql.append(" start with id = '");
		basesql.append(fatherid);
		basesql.append("')");
		basesql.append(" where id = '");
		basesql.append(subid);
		basesql.append("'");


		//this.hibernateTemplate.
		Session se = hibernateTemplate.getSessionFactory().getCurrentSession();
		
		SQLQuery sq = se.createSQLQuery(basesql.toString());
			
		List result = sq.list();
		if(result.size()>0)
			return true;
		else
			return false;
	}
	public boolean existUrl(String url)
	{
		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnPermission.class);
		dc.add(Restrictions.eq("url", url));
		dc.add(Restrictions.eq("nodetype", "1"));
		dc.add(Restrictions.eq("delFlg", "0"));
		
		List list = this.hibernateTemplate.findByCriteria(dc);
		if(list.size()>0)
			return true;
		return false;
	}
	public List getPerRole(TblCmnPermission bean) {
		ContextInfo.recoverQuery();
		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnRolePermission.class);
		dc.add(Restrictions.eq("tblCmnPermission", bean))
		.add(Restrictions.eq("delFlg", "0"))
		.addOrder(Order.desc("createdTime"));
		return this.hibernateTemplate.findByCriteria(dc);
		
	}
	public List getPerUser(TblCmnPermission bean) {
		ContextInfo.recoverQuery();
		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnUserPermission.class);
		dc.add(Restrictions.eq("tblCmnPermission", bean))
		.add(Restrictions.eq("delFlg", "0"))
		.addOrder(Order.desc("createdTime"));
		return this.hibernateTemplate.findByCriteria(dc);
	}
	public List getRoleUser(TblCmnRole role) {
			ContextInfo.recoverQuery();
			DetachedCriteria dc = DetachedCriteria.forClass(TblCmnUserRole.class);
			dc.add(Restrictions.eq("tblCmnRole", role))
			.add(Restrictions.eq("delFlg", "0"))
			.addOrder(Order.desc("createdTime"));
			return this.hibernateTemplate.findByCriteria(dc);
	}
}
