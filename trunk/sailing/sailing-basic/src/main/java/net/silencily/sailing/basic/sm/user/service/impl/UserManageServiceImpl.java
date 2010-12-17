package net.silencily.sailing.basic.sm.user.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import net.silencily.sailing.basic.sm.domain.TblCmnMsgConfig;
import net.silencily.sailing.basic.sm.domain.TblCmnPermission;
import net.silencily.sailing.basic.sm.domain.TblCmnRole;
import net.silencily.sailing.basic.sm.domain.TblCmnRolePermission;
import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.basic.sm.domain.TblCmnUserMember;
import net.silencily.sailing.basic.sm.domain.TblCmnUserPermission;
import net.silencily.sailing.basic.sm.domain.TblCmnUserRole;
import net.silencily.sailing.basic.sm.user.service.UserManageService;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.hibernate3.DBHelper;
import net.silencily.sailing.utils.SecurityUtils;

import org.hibernate.SQLQuery;
import org.hibernate.classic.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;


/**
 * 
 * 
 * @author baihe
 * @version 1.0
 */
public class UserManageServiceImpl implements UserManageService{

	private HibernateTemplate hibernateTemplate;
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	public int addFiledPermissionsForFunPermission(TblCmnUserPermission up)
	{
		//assert(funPermission.getNodetype().equals("1"));
		TblCmnPermission perm = up.getTblCmnPermission();
		org.springframework.util.Assert.isTrue("1".equals(perm.getNodetype()),"传入的权限节点不是功能权限!");
		Collection children = perm.getChildren();
		int count = children.size();
		for(Iterator it = children.iterator();it.hasNext();)
		{
			TblCmnPermission child = (TblCmnPermission)it.next();
			if("2".equals(child.getNodetype()))
			{
				TblCmnUserPermission userPermission = new TblCmnUserPermission();
				userPermission.setTblCmnUserByUserId(up.getTblCmnUserByUserId());
				userPermission.setTblCmnPermission(perm);
				userPermission.setRwCtrl(up.getRwCtrl());
				userPermission.setReadAccessLevel(up.getReadAccessLevel());
				userPermission.setWriteAccessLevel(up.getWriteAccessLevel());
				try {
					hibernateTemplate.save(userPermission);
					hibernateTemplate.flush();
				}catch(DataIntegrityViolationException e){
					count--;
				}
			}
		}
		return count;
	}
	public int addFolerPermissions(String foderid,
			TblCmnUser user,String rwCtrl,String readAccessLevel,String writeAccessLevel)
	{
		Set perms = new HashSet();
		for(Iterator it=user.getUserPermissions().iterator();it.hasNext();)
		{
			TblCmnUserPermission user_perm = (TblCmnUserPermission)it.next();
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
			TblCmnUserPermission userPermission = new TblCmnUserPermission();
			userPermission.setTblCmnUserByUserId(user);
			userPermission.setTblCmnPermission(perm);
			userPermission.setRwCtrl(rwCtrl);
			userPermission.setReadAccessLevel(readAccessLevel);
			userPermission.setWriteAccessLevel(writeAccessLevel);
			
			hibernateTemplate.save(userPermission);
			
		}
		return count;
		//se.
	}
	/**
	 * 功能描述 物理批量激活状态
	 */
	public List batchjihuo(List str) {
		for(int i=0; i<str.size(); i++){
			String id = (String)str.get(i);
			TblCmnUser user = (TblCmnUser)hibernateTemplate.load(TblCmnUser.class, id);
			user.setStatus("1");	
		}
		return str;
	}

	/**
	 * 功能描述 物理批量禁用状态
	 */
	public List batchjinyong(List str) {
		for(int i=0; i<str.size(); i++){
			String id = (String)str.get(i);
			TblCmnUser user = (TblCmnUser)hibernateTemplate.load(TblCmnUser.class, id);
			user.setStatus("0");	
		}
		return str;
	}

	/**
	 * 功能描述 返回权限列表信息
	 */
	public List getPermissions(TblCmnUser bean) {
		ContextInfo.recoverQuery();
		
		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnUserPermission.class);
		dc.add(Restrictions.eq("tblCmnUserByUserId", bean))
		.add(Restrictions.eq("delFlg", "0"))
		.addOrder(Order.desc("createdTime"));
		return this.hibernateTemplate.findByCriteria(dc);
	}
	
	/**
	 * 功能描述 返回数据权限列表信息
	 */
	public List getDataPermission(TblCmnUser bean) {
		ContextInfo.recoverQuery();
		
		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnUserMember.class);
		dc.add(Restrictions.eq("tblCmnUser", bean))
		.add(Restrictions.eq("delFlg", "0"));
		return this.hibernateTemplate.findByCriteria(dc);
	}

	/**
	 * 功能描述 返回角色列表信息
	 */
	public List getRole(TblCmnUser bean) {
		ContextInfo.recoverQuery();
		
		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnUserRole.class);
		dc.add(Restrictions.eq("tblCmnUser", bean))
		.add(Restrictions.eq("delFlg", "0"))
		.addOrder(Order.desc("createdTime"));
		return this.hibernateTemplate.findByCriteria(dc);
	}

	/**
	 * 功能描述 返回角色与权限关联列表信息
	 */
	public List getRolePermissions(TblCmnRole bean) {
		ContextInfo.recoverQuery();
		
		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnRolePermission.class);
		dc.add(Restrictions.eq("tblCmnRole", bean))
		.add(Restrictions.eq("delFlg", "0"))
		.addOrder(Order.desc("createdTime"));
		return this.hibernateTemplate.findByCriteria(dc);
	}

	/**
	 * 功能描述 返回角色与用户关联列表信息
	 */
	public List getRoleUser(TblCmnRole bean) {
		ContextInfo.recoverQuery();
		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnUserRole.class);
		dc.add(Restrictions.eq("tblCmnRole", bean))
		.add(Restrictions.eq("delFlg", "0"))
		.addOrder(Order.desc("createdTime"));
		return this.hibernateTemplate.findByCriteria(dc);
	}

	/**
	 * 功能描述 逻辑删除用户
	 */
	public void deleteUser(TblCmnUser bean) {
		TblCmnMsgConfig msg=new TblCmnMsgConfig();
//		for(Iterator it = bean.getConsigneUserPermissions().iterator();it.hasNext();)
//		{
//			TblCmnUserPermission up = (TblCmnUserPermission)it.next();
//			up.setDelFlg("1");
//			hibernateTemplate.update(up);
//		}
		for(Iterator it=bean.getConsigneUserRoles().iterator();it.hasNext();)
		{
			TblCmnUserRole up=(TblCmnUserRole)it.next();
			up.setDelFlg("1");
			hibernateTemplate.update(up);
		}
//		for(Iterator it = bean.getUserPermissions().iterator();it.hasNext();)
//		{
//			TblCmnUserPermission up = (TblCmnUserPermission)it.next();
//			TblCmnUser user = null;
//			if(null != (user = up.getTblCmnUserByConsignerId()))
//			{
//				up.setTblCmnUserByUserId(up.getTblCmnUserByConsignerId());
//				up.setTblCmnUserByConsignerId(null);
//			}
//			else
//			{
//				up.setDelFlg("1");
//				hibernateTemplate.update(up);
//			}
//		}
		for(Iterator it=bean.getRoles().iterator();it.hasNext();)
		{
			TblCmnUserRole up=(TblCmnUserRole)it.next();
			TblCmnUser user=null;
			if(null !=(user=up.getTblCmnUserByConsignerId()))
			{
				up.setTblCmnUser(up.getTblCmnUserByConsignerId());
				up.setTblCmnUserByConsignerId(null);
			}
			else
			{
				up.setDelFlg("1");
				hibernateTemplate.update(up);
			}
		}
		if(bean.getMsgConfigs().iterator().hasNext())
		{
			msg=(TblCmnMsgConfig)bean.getMsgConfigs().iterator().next();
			msg.setDelFlg("1");
			hibernateTemplate.update(msg);
		}
		getService().deleteLogic(bean);
	}
	
//	/**
//	 * 功能描述 逻辑删除权限
//	 */
//	public void deletePermission(TblCmnUserPermission userPermission) {
//		try
//		{	
//			userPermission.setDelFlg("1");
//			hibernateTemplate.update(userPermission);
//		}catch(DataIntegrityViolationException e)
//		{	
//			TblCmnUser cmnUser=userPermission.getTblCmnUserByUserId();
//			TblCmnPermission cmnPermission=userPermission.getTblCmnPermission();
//			ContextInfo.recoverQuery();
//			DetachedCriteria dc = DetachedCriteria.forClass(TblCmnUserPermission.class);
//			dc.add(Restrictions.eq("tblCmnPermission", cmnPermission));
//			dc.add(Restrictions.eq("tblCmnUserByUserId", cmnUser));
//			dc.add(Restrictions.eq("delFlg", "1"));
//			List list=hibernateTemplate.findByCriteria(dc);
//			if(list.size()>0)
//			{	
//				TblCmnUserPermission same=(TblCmnUserPermission)list.iterator().next();
//				hibernateTemplate.delete(same);
//				userPermission.setDelFlg("1");
//				hibernateTemplate.update(userPermission);
//			}
//		}
//
//	}

//	public void deleteRole(TblCmnUserRole userRole) {
//		TblCmnUser user=(TblCmnUser)userRole.getTblCmnUserByConsignerId();
//		if(user!=null)
//		{	
//			userRole.setTblCmnUser(userRole.getTblCmnUserByConsignerId());
//			userRole.setTblCmnUserByConsignerId(null);
//			hibernateTemplate.update(userRole);
//		}
//		try
//		{
//			userRole.setDelFlg("1");
//			hibernateTemplate.update(userRole);
//		}catch(DataIntegrityViolationException e)
//		{
//			TblCmnUser cmnUser=userRole.getTblCmnUser();
//			TblCmnRole cmnRole=userRole.getTblCmnRole();
//			ContextInfo.recoverQuery();
//			DetachedCriteria dc = DetachedCriteria.forClass(TblCmnUserRole.class);
//			dc.add(Restrictions.eq("tblCmnUser", cmnUser));
//			dc.add(Restrictions.eq("tblCmnRole", cmnRole));
//			dc.add(Restrictions.eq("delFlg", "1"));
//			List list=hibernateTemplate.findByCriteria(dc);
//			if(list.size()>0)
//			{	
//				TblCmnUserRole same=(TblCmnUserRole)list.iterator().next();
//				hibernateTemplate.delete(same);
//				userRole.setDelFlg("1");
//				hibernateTemplate.update(userRole);
//			}
//		}
//	}
	/**
	 * 功能描述 调用公用接口
	 */
	private CommonService getService() {
		return (CommonService)ServiceProvider.getService(CommonService.SERVICE_NAME);
	}
	
	/**
	 * 功能描述 根据角色id获得用户实例,装载页面条件并分页，慎用！
	 */
	public List getUsersByRolesWithAutoCondtionAndPaginater(List roles) {
		StringBuffer sql = new StringBuffer();
		if(roles.size()>0){
			sql.append("select * from tbl_cmn_user where id in (select user_id from tbl_cmn_user_role where role_id in(");
			Iterator it = roles.iterator();
			sql.append("'"+(String)it.next()+"'");
			while(it.hasNext())
			{
				sql.append(",'"+(String)it.next()+"'");
			}
			sql.append("))");
		}
		else
			sql.append("select * from tbl_cmn_user");	

		return DBHelper.findBySqlExtendFromAutoCondition(this.hibernateTemplate,sql.toString(),TblCmnUser.class);
	}
	/**
	 * 根据角色获取用户列表。
	 * @return
	 */
	public List getUsersByRoles(List roles)
	{
		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnUserRole.class);
		dc.createAlias("tblCmnRole", "tblCmnRole");
		dc.add(Restrictions.in("tblCmnRole", roles));
		List urs = this.hibernateTemplate.findByCriteria(dc);
		List users = new ArrayList();
		Iterator it = urs.iterator();
		while(it.hasNext())
		{
			users.add(((TblCmnUserRole)it.next()).getTblCmnUser());
		}
		return users;
	}
	
	/**
	 * 功能描述 根据角色roleCd获得用户实例
	 */
	public List getUsersByRoleCds(List roleCds) {
		ContextInfo.recoverQuery();
		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnUserRole.class);
		dc.createAlias("tblCmnRole", "tblCmnRole");
		dc.add(Restrictions.in("tblCmnRole.roleCd", roleCds));
		List urs = this.hibernateTemplate.findByCriteria(dc);
		List users = new ArrayList();
		Iterator it = urs.iterator();
		while(it.hasNext())
		{
			users.add(((TblCmnUserRole)it.next()).getTblCmnUser());
		}
		return users;
		
//		StringBuffer sql = new StringBuffer();
//		if(roleCds.size()>0){
//			sql.append("select * from tbl_cmn_user where id in (select user_id from tbl_cmn_user_role where role_cd in(");
//			Iterator it = roleCds.iterator();
//			sql.append("'"+(String)it.next()+"'");
//			while(it.hasNext())
//			{
//				sql.append(",'"+(String)it.next()+"'");
//			}
//			sql.append("))");
//		}
//		else
//			sql.append("select * from tbl_cmn_user");	
//
//		return DBHelper.findBySqlExtendFromAutoCondition(this.hibernateTemplate,sql.toString(),TblCmnUser.class);
	}
	
	/**
	 * 功能描述 根据角色id与部门id获得用户实例
	 */
	public List getUsersByRolesWithAutoCondtionAndPaginater(List roles, String deptId) {
		StringBuffer sql = new StringBuffer();
		if(roles.size()>0){
			sql.append("select * from tbl_cmn_user where emp_cd in (select emp_cd from tbl_hr_emp_info where dept_id='"+deptId + "') and id in (select user_id from tbl_cmn_user_role where role_id in(");
			Iterator it = roles.iterator();
			sql.append("'"+(String)it.next()+"'");
			while(it.hasNext())
			{
				sql.append(",'"+(String)it.next()+"'");
			}
			sql.append("))");
		}
		else
			sql.append("select * from tbl_cmn_user");	

		return DBHelper.findBySqlExtendFromAutoCondition(this.hibernateTemplate,sql.toString(),TblCmnUser.class);
	}
	
	/**
	 * 验证登录用户密码，返回boolean
	 */
	public boolean validateUserInfo(String empCd, String psd) { //empCd为用户编号，psd为用户登录密码（明文）
		StringBuffer sql = new StringBuffer();
		String userPsd = SecurityUtils.passwordHex(psd);
		sql.append("select * from tbl_cmn_user where emp_cd='"+empCd+"' and password='"+userPsd+"'");
		List list = DBHelper.findBySqlExtendFromAutoCondition(this.hibernateTemplate, sql.toString(), TblCmnUser.class);
		return !list.isEmpty();
	}
	
	/**
	 * 返回TblCmnUser实例
	 */
	public TblCmnUser getUser(String empCd){ //empCd为用户编号
		if(null == empCd) return null;
		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnUser.class);
		dc.add(Restrictions.eq("empCd", empCd));
		List list =  this.hibernateTemplate.findByCriteria(dc);
		if(list.iterator().hasNext())
			return (TblCmnUser)list.iterator().next();
		else
			return null;
	}

	public List getUserListForConsign(String currentUserId) {
		ContextInfo.recoverQuery();
		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnUser.class);
		dc.add(Restrictions.ne("id", currentUserId))
		.add(Restrictions.eq("delFlg", "0"))
		.addOrder(Order.desc("createdTime"));
		return this.hibernateTemplate.findByCriteria(dc);
	}

	public TblCmnRole getBaseRole() {
		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnRole.class);
		dc.add(Restrictions.eq("roleCd", "CMN_NORMAL_USER"));
		List list =  this.hibernateTemplate.findByCriteria(dc);
		if(list.iterator().hasNext())
			return (TblCmnRole)list.iterator().next();
		else
			return null;
	}
	
	public TblCmnUser newInstance() {
		TblCmnUser empent = new TblCmnUser();
		empent.setDelFlg(new String("0"));
		return empent;
	}
	

	public List getSubDept(String deptID) throws SQLException {
		List deptIDList = new ArrayList();
		deptIDList.add(deptID);
		DataSource ds=SessionFactoryUtils.getDataSource(hibernateTemplate.getSessionFactory());
		Connection con = ds.getConnection();
		
		String subDeptSQL = "select id from tbl_cmn_dept where dept_state='0' start with parent_id='" + deptID +"' connect by prior id=parent_id";
		Statement st=con.createStatement();
		ResultSet rs=st.executeQuery(subDeptSQL);
		while(rs.next()){
			deptIDList.add(rs.getString("id"));
		}
		
		rs.close();
		st.close();
		con.close();
		return deptIDList;
	}
	
}
