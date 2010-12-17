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
 * ��֯����ά��������
 * @author gaojing
 * @version $Id: RoleServiceImpl.java,v 1.1 2010/12/10 10:56:30 silencily Exp $
 * @since 2007-8-29
 */
public class RoleServiceImpl implements RoleService {

	
	/**
	 * ������ɳ�ʼ�����ڵ������, ��������
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
		org.springframework.util.Assert.isTrue("1".equals(perm.getNodetype()),"�����Ȩ�޽ڵ㲻�ǹ���Ȩ��!");
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
			//����������Ȩ��
			//addFiledPermissionsForFunPermission(rolePermission);
			
		}
		return count;
		//se.
	}
	
	/**
	 *  
	 * �������� ���趨
	 * @param initRoot
	 * 2007-12-7 ����01:17:27
	 * @version 1.0
	 * @author wanghy
	 */
	public void setInitRoot(TblCmnRoleOrg initRoot) {
		this.initRoot = initRoot;
	}
	
	/**
	 * ��ȡ��.
	 */
	public TblCmnRoleOrg getInitRoot() {
		return this.initRoot;
	}
	
	/**
	 * ������.
	 */
	public TblCmnRoleOrg getRoot()
	{
		TblCmnRoleOrg root = 
			(TblCmnRoleOrg)hibernateTemplate.load(TblCmnRoleOrg.class, initRoot.getId());
		//������ݿ���û�и��ڵ��򴴽����ڵ�
		if(null == root)
		{
			root = initRoot;
			hibernateTemplate.save(root);
		}
		return root;
	}


	/**
	 * 
	 * ��������  �趨Hibernate
	 * @param hibernateTemplate
	 * 2007-12-7 ����01:18:55
	 * @version 1.0
	 * @author wanghy
	 */
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}


	/**
	 * 
	 * �������� ȡ��Hibernate
	 * @param id
	 * @return
	 * 2007-12-7 ����01:19:24
	 * @version 1.0
	 * @author wanghy
	 */
	public TblCmnRoleOrg load(String id) {
		return (TblCmnRoleOrg) this.hibernateTemplate.load(TblCmnRoleOrg.class, id);
	}


	/**
	 * ��֯�ṹdataȡ��
	 */
	public List getChildren(String oid, RoleForm form){
		
		if (StringUtils.isNotBlank(oid)) {
			ContextInfo.recoverQuery();
			TblCmnRoleOrg current = 
				(TblCmnRoleOrg)hibernateTemplate.load(TblCmnRoleOrg.class, oid);
			
			//׷��Ŀ¼�µĽ�ɫ������Ϣ
			//ʹ��findByCriteria��ѯ�Խ��з�ҳ���� yushn 2008-03-02
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
				if ("��".equals(form.getSystemRole().trim())){
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
	 * �������� ��ȡ��
	 * 2007-12-7 ����01:34:53
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
	 * ����������ȡ��Ŀ¼�ṹ.
	 * @param oid
	 * @param form
	 * @return
	 * 2007-11-28 ����06:19:58
	 * @version 1.0
	 * @author wanghy
	 */
	public List getOrgMenu(String oid, RoleForm form) {
		if (StringUtils.isNotBlank(oid)) {
			ContextInfo.recoverQuery();
			TblCmnRoleOrg current = 
				(TblCmnRoleOrg)hibernateTemplate.load(TblCmnRoleOrg.class, oid);
			
			//׷��Ŀ¼�µĽ�ɫ������Ϣ
			//ʹ��findByCriteria��ѯ�Խ��з�ҳ���� yushn 2008-03-02
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
	 * ������������ɫȨ����Ϣ.
	 * @param role
	 * @param strFlg
	 * @return
	 * 2007-11-28 ����06:19:58
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
	 * �����������û���ɫȡ��.
	 * @param consignerBean
	 * @param userBean
	 * @return
	 * 2007-11-28 ����06:19:58
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
	 * �����������û���ɫȡ��.
	 * @param consignerBean
	 * @param userBean
	 * @return
	 * 2007-11-28 ����06:19:58
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
	 * ����������ί�н�ɫȡ��
	 * @param consignerBean
	 * @param userBean
	 * @return
	 * 2007-11-28 ����06:19:58
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
	 * Ŀ¼��ѯ������ҳ����������Ӹ��ڵ㿪ʼ��ѯ���ڵ㼰�������ӽڵ��µĽ�ɫĿ¼��¼
	 * ֧�ַ�ҳ
	 * ��������
	 * @param foid
	 * @return
	 * 2007-12-6 ����04:45:44
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
	 * ��ɫ��ѯ������ҳ����������Ӹ��ڵ㿪ʼ��ѯ���ڵ㼰�������ӽڵ��µĽ�ɫ��¼
	 * ֧�ַ�ҳ
	 * ��������
	 * @param foid
	 * @return
	 * 2007-12-6 ����04:45:44
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
	 * ���ݽ�ɫ��ʶ��ȡ��ɫ�б�
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
		//����ѵ�ǰ�ڵ��Ƶ��Լ����ӽڵ��еĴ���������õ�ǰ�ڵ��ID��Ҫת�Ƶ��Ľڵ�ID��Ƚϣ������ǰ�ڵ���
		//Ҫת�ƽڵ�ĸ������Ͳ�����ת�Ʋ�����ҳ����ʾ������Ϣ��
		if (isSubNode(currentId,fatherid)){
			throw new RuntimeException(MessageInfo.factory().getMessage("SM_I060_R_0"));
		}
		if ("c".equals(roleForm.getStrCreate())){
			//�½�һ��Ŀ¼����
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
				//����һ��Ŀ¼����
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
