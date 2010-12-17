package net.silencily.sailing.basic.sm.role.service.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.silencily.sailing.basic.sm.domain.TblCmnPermission;
import net.silencily.sailing.basic.sm.domain.TblCmnRole;
import net.silencily.sailing.basic.sm.domain.TblCmnRoleOrg;
import net.silencily.sailing.basic.sm.domain.TblCmnRolePermission;
import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.basic.sm.domain.TblCmnUserRole;
import net.silencily.sailing.basic.sm.role.service.RoleService;
import net.silencily.sailing.basic.sm.role.web.RoleForm;
import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.hibernate3.DBHelper;
import net.silencily.sailing.utils.MessageInfo;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.classic.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * 组织机构维护服务类
 * @author gaojing
 * @version $Id: RoleServiceImpl.java,v 1.1 2010/12/10 10:56:30 silencily Exp $
 * @since 2007-8-29
 */
public class RoleServiceImpl implements RoleService {

	
	/**
	 * 仅仅完成初始化根节点的作用, 不做缓存
	 */
	private TblCmnRoleOrg initRoot;

	/**
	 *  HibernateTemplate
	 */
	private HibernateTemplate hibernateTemplate;

	public int addFiledPermissionsForFunPermission(TblCmnRolePermission rp)
	{
		//assert(funPermission.getNodetype().equals("1"));
		TblCmnPermission perm = rp.getTblCmnPermission();
		org.springframework.util.Assert.isTrue("1".equals(perm.getNodetype()),"传入的权限节点不是功能权限!");
		Collection children = perm.getChildren();
		int count = children.size();
		for(Iterator it = children.iterator();it.hasNext();)
		{
			TblCmnPermission child = (TblCmnPermission)it.next();
			if("2".equals(child.getNodetype()))
			{
				TblCmnRolePermission rolePermission = new TblCmnRolePermission();
				rolePermission.setTblCmnRole(rp.getTblCmnRole());
				rolePermission.setTblCmnPermission(child);
				rolePermission.setRwCtrl(rp.getRwCtrl());
				rolePermission.setReadAccessLevel(rp.getReadAccessLevel());
				rolePermission.setWriteAccessLevel(rp.getWriteAccessLevel());
				try {
					hibernateTemplate.save(rolePermission);
					hibernateTemplate.flush();
				}catch(DataIntegrityViolationException e){
					count--;
				}
			}
		}
		return count;
	}
	public int addFolerPermissions(String foderid,
			TblCmnRole role,String rwCtrl,String readAccessLevel,String writeAccessLevel)
	{
		Set perms = new HashSet();
		for(Iterator it=role.getTblCmnRolePermissions().iterator();it.hasNext();)
		{
			TblCmnRolePermission user_perm = (TblCmnRolePermission)it.next();
			perms.add(user_perm.getTblCmnPermission());
		}
		//user.getUserPermissions()
		Session se = hibernateTemplate.getSessionFactory().getCurrentSession();
		StringBuffer sql = new StringBuffer();
		sql.append("select p.* from (select * from tbl_cmn_permission connect by prior id = father_id start with id='");
		sql.append(foderid);
		sql.append("') p where p.nodetype='1' and p.del_flg='0'");
		SQLQuery sq = se.createSQLQuery(sql.toString());
		sq.addEntity(TblCmnPermission.class);
		List result = sq.list();
		int count = result.size();
		Iterator it  = result.iterator();
		while(it.hasNext())
		{
			TblCmnPermission perm = (TblCmnPermission)it.next();
			if(perms.contains(perm))
			{
				count--;
				continue;
			}
			TblCmnRolePermission rolePermission = new TblCmnRolePermission();
			rolePermission.setTblCmnRole(role);
			rolePermission.setTblCmnPermission(perm);
			rolePermission.setRwCtrl(rwCtrl);
			rolePermission.setReadAccessLevel(readAccessLevel);
			rolePermission.setWriteAccessLevel(writeAccessLevel);
			
			hibernateTemplate.save(rolePermission);
			//增加数据项权限
			//addFiledPermissionsForFunPermission(rolePermission);
			
		}
		return count;
		//se.
	}
	
	/**
	 *  
	 * 功能描述 根设定
	 * @param initRoot
	 * 2007-12-7 下午01:17:27
	 * @version 1.0
	 * @author wanghy
	 */
	public void setInitRoot(TblCmnRoleOrg initRoot) {
		this.initRoot = initRoot;
	}
	
	/**
	 * 根取得.
	 */
	public TblCmnRoleOrg getInitRoot() {
		return this.initRoot;
	}
	
	/**
	 * 根保存.
	 */
	public TblCmnRoleOrg getRoot()
	{
		TblCmnRoleOrg root = 
			(TblCmnRoleOrg)hibernateTemplate.load(TblCmnRoleOrg.class, initRoot.getId());
		//如果数据库中没有根节点则创建根节点
		if(null == root)
		{
			root = initRoot;
			hibernateTemplate.save(root);
		}
		return root;
	}


	/**
	 * 
	 * 功能描述  设定Hibernate
	 * @param hibernateTemplate
	 * 2007-12-7 下午01:18:55
	 * @version 1.0
	 * @author wanghy
	 */
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}


	/**
	 * 
	 * 功能描述 取得Hibernate
	 * @param id
	 * @return
	 * 2007-12-7 下午01:19:24
	 * @version 1.0
	 * @author wanghy
	 */
	public TblCmnRoleOrg load(String id) {
		return (TblCmnRoleOrg) this.hibernateTemplate.load(TblCmnRoleOrg.class, id);
	}


	/**
	 * 组织结构data取得
	 */
	public List getChildren(String oid, RoleForm form){
		
		if (StringUtils.isNotBlank(oid)) {
			ContextInfo.recoverQuery();
			TblCmnRoleOrg current = 
				(TblCmnRoleOrg)hibernateTemplate.load(TblCmnRoleOrg.class, oid);
			
			//追加目录下的角色部分信息
			//使用findByCriteria查询以进行分页处理 yushn 2008-03-02
			DetachedCriteria dc = DetachedCriteria.forClass(TblCmnRole.class);
			dc.add(Restrictions.eq("father",current));
			dc.add(Restrictions.eq("delFlg","0"));
			return this.hibernateTemplate.findByCriteria(dc);
			//dc.add(father)
//			Set se  = current.getTblCmnRoles();
//			List list = new ArrayList();
//			for(Iterator iter= se.iterator();iter.hasNext();){
//				Object temp = iter.next();
//				TblCmnRole tempBean = (TblCmnRole)temp;
//				if (null != temp && "0".equals(tempBean.getDelFlg())) {
//					list.add((TblCmnRole)temp);
//				}
//			}		
//			return list;
		} else {
			
			DetachedCriteria dc = DetachedCriteria.forClass(TblCmnRole.class);
			if (!"".equals(form.getFatherName().trim()) && null != form.getFatherName()){
				dc.createAlias("father", "father");	
				dc.add(Restrictions.eq("father.displayName", form.getFatherName()));
			} 
			if (!"".equals(form.getDisplayName().trim()) && null != form.getDisplayName()){
				dc.add(Restrictions.eq("name", form.getDisplayName()));
			} 

			if (!"".equals(form.getDisplayOrder().trim()) && null != form.getDisplayOrder()){
				dc.add(Restrictions.eq("displayOrder", form.getDisplayOrder()));
			} 
			if (!"".equals(form.getRoleCd().trim()) && null != form.getRoleCd()){
				dc.add(Restrictions.eq("roleCd", form.getRoleCd()));
			}
			
			if (!"".equals(form.getSystemRole().trim()) && null != form.getSystemRole()){
				String strRole = "";
				if ("否".equals(form.getSystemRole().trim())){
					strRole = "0";
				} else {
					strRole = "1";
				}
				dc.add(Restrictions.eq("systemRole", strRole));
			} 
			dc.add(Restrictions.eq("delFlg","0"));
			dc.addOrder(Order.asc("displayOrder"));
			return this.hibernateTemplate.findByCriteria(dc);
		}
	}
	
	/**
	 * 
	 * 功能描述 根取得
	 * 2007-12-7 下午01:34:53
	 * @version 1.0
	 * @author wanghy
	 */
	public void init() {
		Object obj = this.hibernateTemplate.get(TblCmnRoleOrg.class, initRoot.getId());
		if (obj == null) {
			this.hibernateTemplate.save(this.initRoot);
		}
	}	
	

	/**
	 * 
	 * 功能描述　取得目录结构.
	 * @param oid
	 * @param form
	 * @return
	 * 2007-11-28 下午06:19:58
	 * @version 1.0
	 * @author wanghy
	 */
	public List getOrgMenu(String oid, RoleForm form) {
		if (StringUtils.isNotBlank(oid)) {
			ContextInfo.recoverQuery();
			TblCmnRoleOrg current = 
				(TblCmnRoleOrg)hibernateTemplate.load(TblCmnRoleOrg.class, oid);
			
			//追加目录下的角色部分信息
			//使用findByCriteria查询以进行分页处理 yushn 2008-03-02
			DetachedCriteria dc = DetachedCriteria.forClass(TblCmnRoleOrg.class);
			dc.add(Restrictions.eq("father",current));
			dc.add(Restrictions.eq("delFlg","0"));
			dc.addOrder(Order.asc("displayOrder"));
			return this.hibernateTemplate.findByCriteria(dc);
			
		} else {
			
			DetachedCriteria dc = DetachedCriteria.forClass(TblCmnRoleOrg.class);
			if (!"".equals(form.getFatherName().trim()) && null != form.getFatherName()){
				dc.createAlias("father", "father");	
				dc.add(Restrictions.eq("father.displayName", form.getFatherName()));
			} 
			if (!"".equals(form.getDisplayName().trim()) && null != form.getDisplayName()){
				dc.add(Restrictions.eq("displayName", form.getDisplayName()));
			} 

			if (!"".equals(form.getDisplayOrder().trim()) && null != form.getDisplayOrder()){
				dc.add(Restrictions.eq("displayOrder", form.getDisplayOrder()));
			}
			dc.add(Restrictions.eq("delFlg","0"));
			dc.addOrder(Order.asc("displayOrder"));
			return this.hibernateTemplate.findByCriteria(dc);
		}
	}
	
	
	/**
	 * 
	 * 功能描述　角色权限信息.
	 * @param role
	 * @param strFlg
	 * @return
	 * 2007-11-28 下午06:19:58
	 * @version 1.0
	 * @author wanghy
	 */
	public List list(TblCmnRole role, String strFlg) {
		ContextInfo.recoverQuery();
		if (("permission").equals(strFlg)){
			DetachedCriteria dc = DetachedCriteria.forClass(TblCmnRolePermission.class);
			dc.add(Restrictions.eq("tblCmnRole", role));
			dc.add(Restrictions.eq("delFlg","0"));
			dc.addOrder(Order.asc("tblCmnRole.id"));
			dc.addOrder(Order.asc("tblCmnPermission.id"));
			return this.hibernateTemplate.findByCriteria(dc);
			
		}else if(("user").equals(strFlg)) {
			ContextInfo.recoverQuery();
			DetachedCriteria dc = DetachedCriteria.forClass(TblCmnUserRole.class);
			dc.add(Restrictions.eq("tblCmnRole", role));
			dc.add(Restrictions.eq("delFlg","0"));
			dc.addOrder(Order.asc("tblCmnUser.id"));
			dc.addOrder(Order.asc("tblCmnRole.id"));
			return this.hibernateTemplate.findByCriteria(dc);
		}else{
			return null;
		}
	}

	/**
	 * 
	 * 功能描述　用户角色取得.
	 * @param consignerBean
	 * @param userBean
	 * @return
	 * 2007-11-28 下午06:19:58
	 * @version 1.0
	 * @author wanghy
	 */
	public List getRole(TblCmnUser tblCmnUser) {
		
		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnUserRole.class);
			dc.add(Restrictions.eq("tblCmnUser", tblCmnUser));
			dc.add(Restrictions.eq("delFlg", "0"));
		return this.hibernateTemplate.findByCriteria(dc);
	}
	
	/**
	 * 
	 * 功能描述　用户角色取得.
	 * @param consignerBean
	 * @param userBean
	 * @return
	 * 2007-11-28 下午06:19:58
	 * @version 1.0
	 * @author wanghy
	 */
	public List getUserRole(TblCmnUser tblCmnUser, TblCmnRole tblCmnRole) {
		
		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnUserRole.class);
			dc.add(Restrictions.eq("tblCmnUser", tblCmnUser));
			dc.add(Restrictions.eq("tblCmnRole", tblCmnRole));
			dc.add(Restrictions.eq("delFlg", "0"));
		return this.hibernateTemplate.findByCriteria(dc);
	}
	
	/**
	 * 
	 * 功能描述　委托角色取得
	 * @param consignerBean
	 * @param userBean
	 * @return
	 * 2007-11-28 下午06:19:58
	 * @version 1.0
	 * @author wanghy
	 */
	public List getUserRoleConsignedUser(TblCmnUser consignerBean, TblCmnUser userBean) {
		
		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnUserRole.class);
			dc.add(Restrictions.eq("tblCmnUserByConsignerId", consignerBean));
			dc.add(Restrictions.eq("tblCmnUser", userBean));
			dc.add(Restrictions.eq("delFlg", "0"));
		return this.hibernateTemplate.findByCriteria(dc);
	}
	/**
	 * 目录查询，根据页面的条件，从父节点开始查询父节点及其所有子节点下的角色目录记录
	 * 支持分页
	 * 功能描述
	 * @param foid
	 * @return
	 * 2007-12-6 下午04:45:44
	 * @version 1.0
	 * @author yushn
	 */
	public List queryOrgStartWithFatherId(String foid)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select * from (select * from tbl_cmn_role_org connect by prior id = father_id start with id='");
		sql.append(foid);
		sql.append("') where id<>'");
		sql.append(foid);
		sql.append("'");		

		return DBHelper.findBySqlExtendFromAutoCondition(this.hibernateTemplate,sql.toString(),TblCmnRoleOrg.class);
		
	}
	/**
	 * 角色查询，根据页面的条件，从父节点开始查询父节点及其所有子节点下的角色记录
	 * 支持分页
	 * 功能描述
	 * @param foid
	 * @return
	 * 2007-12-6 下午04:45:44
	 * @version 1.0
	 * @author yushn
	 */
	public List queryRoleStartWithFatherId(String foid, String fname)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select * from tbl_cmn_role where father_id in (select id from tbl_cmn_role_org where display_name like '%");
		sql.append(fname);
		sql.append("%' ");
		sql.append("connect by prior id = father_id start with id='");		
		sql.append(foid);
		sql.append("')");		
		return DBHelper.findBySqlExtendFromAutoCondition(this.hibernateTemplate,sql.toString(),TblCmnRole.class);

	}
	
	public boolean isSubNode(String fatherid ,String subid){
		StringBuffer basesql = new StringBuffer();
		basesql.append("select rownum");
		basesql.append("  from (select id");
		basesql.append("          from tbl_cmn_role_org");
		basesql.append("        connect by prior id = father_id");
		basesql.append("         start with id = '");
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
	/**
	 * 根据角色标识获取角色列表
	 * @param c
	 * @return
	 */
	public List findRolesByRoleCds(Collection c)
	{
		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnRole.class);
		dc.add(Restrictions.in("roleCd", c));
		return this.hibernateTemplate.findByCriteria(dc);
	}

	public List getRoleOrgChildren() {
			ContextInfo.recoverQuery();
			DetachedCriteria dc = DetachedCriteria.forClass(TblCmnRoleOrg.class);
			return this.hibernateTemplate.findByCriteria(dc);
	}

	public List getRoleChildren() {
		ContextInfo.recoverQuery();
		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnRole.class);
		return this.hibernateTemplate.findByCriteria(dc);
	}
	
	public List queryRoleStartWithFatherId(String foid)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select * from tbl_cmn_role where father_id in (select id from tbl_cmn_role_org connect by prior id = father_id start with id='");
		sql.append(foid);
		sql.append("')");		

		return DBHelper.findBySqlExtendFromAutoCondition(this.hibernateTemplate,sql.toString(),TblCmnRole.class);

	}
	public boolean isAnyRoleInRoleOrg(String orgid,Collection roles)
	{
		if(roles.size()==0) return false;
		
		StringBuffer basesql = new StringBuffer();
		basesql.append("select * from tbl_cmn_role where father_id in (");
		basesql.append("select id from tbl_cmn_role_org connect by prior id = father_id start with id='");
		basesql.append(orgid);
		basesql.append("')");
		basesql.append(" and id in(");
		
		boolean first = true;
		java.util.Iterator it = roles.iterator();
		while(it.hasNext())
		{
			if(first==false)
				basesql.append(",");
			else
				first = false;
			basesql.append("'"+it.next()+"'");
		}
		
		basesql.append(")");

		//this.hibernateTemplate.
		Session se = hibernateTemplate.getSessionFactory().getCurrentSession();
		
		SQLQuery sq = se.createSQLQuery(basesql.toString());
			
		List result = sq.list();
		if(result.size()>0)
			return true;
		else
			return false;	
	}
	public String saveOrg(TblCmnRoleOrg children,RoleForm roleForm,String fatherid)
	{	
		String currentId = "";
		if(children != null){
			currentId = children.getId();
		}
		//避免把当前节点移到自己的子节点中的错误操作，用当前节点的ID和要转移到的节点ID相比较，如果当前节点是
		//要转移节点的父级，就不进行转移操作，页面显示错误信息。
		if (isSubNode(currentId,fatherid)){
			throw new RuntimeException(MessageInfo.factory().getMessage("SM_I060_R_0"));
		}
		if ("c".equals(roleForm.getStrCreate())){
			//新建一条目录数据
			try {
		        hibernateTemplate.save(children);
		        hibernateTemplate.flush();
				roleForm.setOid(children.getId());
				roleForm.setStrCreate("");
				return MessageInfo.factory().getMessage("SM_I038_R_0");
			}catch (Exception ex){
				ex.printStackTrace();
				throw new RuntimeException(MessageInfo.factory().getMessage("SM_I039_R_0"));
			}
		} else {
			try {
				//更新一条目录数据
		        hibernateTemplate.update(children);
		        hibernateTemplate.flush();
		        return MessageInfo.factory().getMessage("SM_I040_R_0");
			} catch (Exception ex){
				ex.printStackTrace();
				throw new RuntimeException(MessageInfo.factory().getMessage("SM_I041_R_0"));
			}
			
		}
	}

}
