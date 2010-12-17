package net.silencily.sailing.basic.wf.service.impl;

import java.beans.PropertyDescriptor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import net.silencily.sailing.basic.sm.domain.TblCmnRole;
import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.basic.wf.WorkflowInfo;
import net.silencily.sailing.basic.wf.db.WfDBConnection;
import net.silencily.sailing.basic.wf.domain.TblWfFormInfo;
import net.silencily.sailing.basic.wf.domain.TblWfOperationInfo;
import net.silencily.sailing.basic.wf.domain.TblWfParticularInfo;
import net.silencily.sailing.basic.wf.domain.TblWfStepStatus;
import net.silencily.sailing.basic.wf.service.WFService;
import net.silencily.sailing.hibernate3.CodeWrapperPlus;
import net.silencily.sailing.hibernate3.EnhancehibernateTemplatePlus;
import net.silencily.sailing.utils.Tools;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * 
 * @author wenjb
 * @version 1.0
 */
public class WFServiceImpl implements WFService {

	private HibernateTemplate hibernateTemplate;

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	//功能描述 通过工作流信息表的PK，查找工作流的具体实例,有级联关系
	public List findWfOperInfoAll() {		
		// 设置查询条件
		DetachedCriteria dc = DetachedCriteria.forClass(TblWfOperationInfo.class);
		CodeWrapperPlus cwp= new CodeWrapperPlus("0","启用",null);
		cwp.setModify(true);
		dc.add(Restrictions.eq("delFlg", "0"));
		dc.add(Restrictions.eq("status", cwp));
		dc.addOrder(Order.desc("createdTime"));
		List list = hibernateTemplate.findByCriteria(dc);
		return list;
	}
	// 功能描述 通过工作流信息表的PK，查找工作流的具体实例,有级联关系
	public TblWfOperationInfo findWfOperInfoById(String id) {
		
		TblWfOperationInfo tmp = null;		
		//ContextInfo.recoverQuery();
		// 设置查询条件
		DetachedCriteria dc = DetachedCriteria.forClass(TblWfOperationInfo.class);
		dc.add(Restrictions.eq("id", id));
		dc.add(Restrictions.eq("delFlg", "0"));
		dc.addOrder(Order.desc("createdTime"));
		List list = hibernateTemplate.findByCriteria(dc);
		if(list.size()>0){
			tmp = (TblWfOperationInfo) list.get(0);
		}
		return tmp;
	}
	
	public TblWfOperationInfo findWfOperInfoByName(String name) {
		TblWfOperationInfo tmp = null;		
		//ContextInfo.recoverQuery();
		// 设置查询条件
		DetachedCriteria dc = DetachedCriteria.forClass(TblWfOperationInfo.class);
		dc.add(Restrictions.eq("name", name));
		dc.add(Restrictions.eq("delFlg", "0"));
		dc.addOrder(Order.desc("createdTime"));
		List list = hibernateTemplate.findByCriteria(dc);
		if(list.size()>0){
			tmp = (TblWfOperationInfo) list.get(0);
		}
		return tmp;
	}

	// 功能描述 从数据库中导入所有的工作流实例。
	public List findAllWfOper() {
		//ContextInfo.recoverQuery();
		// 设置查询条件
		DetachedCriteria dc = DetachedCriteria.forClass(TblWfOperationInfo.class);
		dc.add(Restrictions.eq("delFlg", "0"));
		dc.addOrder(Order.desc("createdTime"));
		List list = ((EnhancehibernateTemplatePlus) hibernateTemplate).findByCriteria(dc);
		return list;
	}

	// 功能描述 存储一个工作流实例,目前只存储工作流信息表，不进行XML文件生成
	public void saveWfOperInfo(TblWfOperationInfo wfinfo) {
		try {
			getHibernateTemplate().save(wfinfo);
			// getHibernateTemplate().flush();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	// 功能描述 删除工作流实例信息，只是修改工作流信息表的删除标志字段
	public void deleteWfOperInfo(String id) {
		TblWfOperationInfo wfInfo = this.findWfOperInfoById(id);
		wfInfo.setDelFlg("1");
		this.getHibernateTemplate().update(wfInfo);

	}

	// 功能描述 更新修改的工作流实例信息。
	public void updateWfOperInfo(TblWfOperationInfo wfInfo) {
		try{
			this.getHibernateTemplate().update(wfInfo);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	// 功能描述 存储一个步骤定义信息,同时存储表单权限信息，有级连关系
	public void saveWfParInfo(TblWfParticularInfo wfinfo) {
		try {
			getHibernateTemplate().save(wfinfo);
			// getHibernateTemplate().flush();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	// 删除工作流实例信息,同时删除对应的权限信息
	public void deleteWfParInfo(String id) {
		TblWfFormInfo tbl = null ;
		try{
			TblWfParticularInfo wfInfo = this.findWfParticularInfoById(id);
			DetachedCriteria dc = DetachedCriteria.forClass(TblWfFormInfo.class);
			dc.add(Restrictions.eq("tblWfParticularInfo", wfInfo));
			List list = hibernateTemplate.findByCriteria(dc);
			for( int i = 0; i < list.size(); i++){
				tbl = (TblWfFormInfo) list.get(i);
				TblWfFormInfo tblefForm = tbl;
				tblefForm.setDelFlg("1");
				this.getHibernateTemplate().update(tblefForm);
			}
			wfInfo.setDelFlg("1");
			this.getHibernateTemplate().update(wfInfo);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	// 功能描述 更新具体流程定义信息表
	public void updateWfParInfo(TblWfParticularInfo wfInfo) {
		this.getHibernateTemplate().saveOrUpdate(wfInfo);
	}
	
	//功能描述 获得所有角色 yangxl
	public List findRole() {
		//ContextInfo.recoverQuery();
		// 设置查询条件
		List list = ((EnhancehibernateTemplatePlus) hibernateTemplate).loadAll(TblCmnRole.class);
		return list;
	}
	
	//功能描述 通过标识号获得角色
	public TblCmnRole findroleCdRole(String roleCd){
		TblCmnRole tr = null;
		//ContextInfo.recoverQuery();
		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnRole.class);
		dc.add(Restrictions.eq("roleCd", roleCd));
		dc.add(Restrictions.eq("delFlg", "0"));
		List list = hibernateTemplate.findByCriteria(dc);
		try{
			if(list.size()>0){
				tr = (TblCmnRole)list.get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return tr;
	}
	
	//功能描述 通过id获得角色
	public TblCmnRole findidRole(String id){
		TblCmnRole tr = null;
		//ContextInfo.recoverQuery();
		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnRole.class);
		dc.add(Restrictions.eq("id", id));
		dc.add(Restrictions.eq("delFlg", "0"));
		List list = hibernateTemplate.findByCriteria(dc);
		try{
			if(list.size()>0){
				tr = (TblCmnRole)list.get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return tr;
	}
	//功能描述 通过步骤Id查找工作流步骤信息 yangxl time: 2007-12-20
	public TblWfParticularInfo findWfParticularInfoById(String id) {
		TblWfParticularInfo tmp = null;		
		//ContextInfo.recoverQuery();
		// 设置查询条件
		DetachedCriteria dc = DetachedCriteria.forClass(TblWfParticularInfo.class);
		dc.add(Restrictions.eq("id", id));
		dc.add(Restrictions.eq("delFlg", "0"));
		List list = hibernateTemplate.findByCriteria(dc);
		try{
			if(list.size()>0){
				tmp = (TblWfParticularInfo) list.get(0);
			}
		}catch(Exception e){
			System.out.println("imp");
			e.printStackTrace();
		}
		
		return tmp;		
	}
	
	//功能描述 通过步骤Id查找工作流步骤信息 yangxl time: 2007-12-20
	public boolean findbooleanOpinstepName(String stepName,TblWfOperationInfo tblWfOperationInfo) {
		boolean bool = false;	
		//ContextInfo.recoverQuery();
		// 设置查询条件
		DetachedCriteria dc = DetachedCriteria.forClass(TblWfParticularInfo.class);
		dc.add(Restrictions.eq("stepName", stepName));
		dc.add(Restrictions.eq("delFlg", "0"));
		dc.add(Restrictions.eq("tblWfOperationInfo", tblWfOperationInfo));
		List list = hibernateTemplate.findByCriteria(dc);
		try{
			if(list.size()>0){
				bool = false;
			}else{
				bool = true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return bool;		
	}
	
	//查找可能的结果，仅限制2个变量（版本号eversion，工作流名称name）
	public List findsearch(TblWfOperationInfo wfInfo) {	
		//设置查询条件
		DetachedCriteria dc = DetachedCriteria.forClass(TblWfOperationInfo.class);
		//ContextInfo.recoverQuery();
		String name = wfInfo.getName();
		String operName = wfInfo.getOperName();
		String status = wfInfo.getStatus().toString();
		Double edition = wfInfo.getEdition();
		if(operName!=null && !("").equals(operName)){
			dc.add(Restrictions.like("operName", "%"+operName+"%"));
		}
		if(name!=null && !("").equals(name)){
			dc.add(Restrictions.like("name", "%"+name+"%"));
		}
		if(edition!=null && !("").equals(edition)){
			dc.add(Restrictions.eq("edition", edition));
		}
		if(status!=null && !("").equals(status)){
			dc.add(Restrictions.eq("status", new CodeWrapperPlus(status)));
		}
		dc.add(Restrictions.eq("delFlg", "0"));
		dc.addOrder(Order.desc("createdTime"));
		List list = hibernateTemplate.findByCriteria(dc);
		
		return list;	
	}
	
	//删除工作流信息
	public void deleteWfFormInfo(String id) {
		TblWfFormInfo tbl =  new TblWfFormInfo();
		tbl.setId(id);
		tbl.setDelFlg("1");
		this.getHibernateTemplate().update(tbl);
		
	}
	
	public TblWfOperationInfo findWfOperInfoByEdition(String name) {
		TblWfOperationInfo tmp = null;		
		//ContextInfo.recoverQuery();
		// 设置查询条件
		DetachedCriteria dc = DetachedCriteria.forClass(TblWfOperationInfo.class);
		dc.add(Restrictions.eq("name", name));
		dc.add(Restrictions.eq("delFlg", "0"));
		dc.addOrder(Order.desc("edition"));
		List list = hibernateTemplate.findByCriteria(dc);
		if(list.size()>0){
			tmp = (TblWfOperationInfo) list.get(0);
		}
		return tmp;		
	}
	
	public void recAppDataStatus(WorkflowInfo appBean,String stepId) throws Exception
	{
		PropertyDescriptor[] a=PropertyUtils.getPropertyDescriptors(appBean.getClass());
		StringBuffer sb = new StringBuffer();
		boolean first = true;
		for(int i=0;i<a.length;i++)
		{
			if(null == a[i].getReadMethod())
				continue;
			if(StringUtils.isBlank(a[i].getName()))
				continue;
			if("version".equals(a[i].getName()) || "sequenceNo".equals(a[i].getName()))
				continue;
			try{
				PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(appBean, a[i].getName());
				if(null == pd ) continue;
				boolean isString = pd.getPropertyType().isAssignableFrom(java.lang.String.class);
				
				Object obj = Tools.getProperty(appBean, a[i].getName());
				if(isString)
				{
					if(StringUtils.isNotBlank((String)obj))
						continue;
				}
				else if(null != obj)
				{
					continue;
				}
				if(first==true){
					first = false;
				}else
				{
					sb.append(",");
				}
				sb.append(a[i].getName());
			}catch(Exception e)
			{
				continue;
			}
		}
		//查询已有记录
		/*
		DetachedCriteria dc = DetachedCriteria.forClass(TblWfStepStatus.class);
		dc.add(Restrictions.eq("beanId", appBean.getId()));
		dc.add(Restrictions.eq("stepId", stepId));
		List list = hibernateTemplate.findByCriteria(dc);
		//删除已有记录
		for(java.util.Iterator it=list.iterator();it.hasNext();)
		{
			this.hibernateTemplate.delete(it.next());
			//this.hibernateTemplate.flush();
			
		}
		*/
		
		StringBuffer sql1 = new StringBuffer();
		sql1.append("delete from tbl_wf_step_status where bean_id = '");
		sql1.append(appBean.getId());
		sql1.append("' and step_id = '");
		sql1.append(stepId);
		sql1.append("'");
		System.out.println(sql1);
		Connection con1 = WfDBConnection.getConnection();
		try{
			Statement st1 = con1.createStatement();
			st1.execute(sql1.toString());
		}catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		finally
		{
			try{
				con1.close();
			}catch(Exception e)
			{
				
			}
		}

		StringBuffer sql = new StringBuffer();
		sql.append("insert into tbl_wf_step_status (id,bean_id,step_id,null_field_name) values('");
		sql.append(Tools.getPKCode());
		sql.append("','");
		sql.append(appBean.getId());
		sql.append("','");
		sql.append(stepId);
		sql.append("','");
		sql.append(sb.toString());
		sql.append("')");
		System.out.println(sql);
		Connection con = WfDBConnection.getConnection();
		try{
			Statement st = con.createStatement();
			st.execute(sql.toString());
		}catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		finally
		{
			try{
				con.close();
			}catch(Exception e)
			{
				
			}
		}
//		TblWfStepStatus ss = new TblWfStepStatus();
//		ss.setBeanId(appBean.getId());
//		ss.setStepId(stepId);
//		if(sb.length()>1000)
//		{
//			ss.setNullFieldName(sb.substring(0, 1000));
//			ss.setNullFieldName2(sb.substring(1000));
//			
//		}else
//			ss.setNullFieldName(sb.toString());
//		this.hibernateTemplate.save(ss);
//		this.hibernateTemplate.flush();
		
	}
	public void resetAppDataStatus(WorkflowInfo appBean,String stepId)
	{
		DetachedCriteria dc = DetachedCriteria.forClass(TblWfStepStatus.class);
		dc.add(Restrictions.eq("beanId", appBean.getId()));
		dc.add(Restrictions.eq("stepId", stepId));
		List list = hibernateTemplate.findByCriteria(dc);
		if(list.size()>0)
		{
			TblWfStepStatus status = (TblWfStepStatus)list.get(0);
			if(StringUtils.isBlank(status.getNullFieldName()))
				return;
			String[] tokens = status.getNullFieldName().split(",");
			for(int i=0;i<tokens.length;i++)
			{
				if("version".equals(tokens[i]) || "sequenceNo".equals(tokens[i]))
					continue;
				try{
					PropertyUtils.setNestedProperty(appBean,tokens[i], null);
				}catch(Exception e)
				{
					
				}
			}
		}
	}
	public void cleanAppDataStatus(String beanId)
	{
		DetachedCriteria dc = DetachedCriteria.forClass(TblWfStepStatus.class);
		dc.add(Restrictions.eq("beanId", beanId));
		List list = hibernateTemplate.findByCriteria(dc);
		hibernateTemplate.deleteAll(list);
		//hibernateTemplate.flush();
	}
	//功能描述 通过标识号获得管理员
	public TblCmnUser findempCdUser(String empCd){
		TblCmnUser the = null;
		//ContextInfo.recoverQuery();
		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnUser.class);
		dc.add(Restrictions.eq("empCd", empCd));
		dc.add(Restrictions.eq("delFlg", "0"));
		List list = hibernateTemplate.findByCriteria(dc);
		try{
			the = (TblCmnUser)list.get(0);
		}catch(Exception e){
			e.printStackTrace();
		}
		return the;
	}
	//功能描述 获得最新的流程
	public List findWfOperInfo() {
		List list = new ArrayList();
		//ContextInfo.recoverQuery();
		// 设置查询条件
		DetachedCriteria dc = DetachedCriteria.forClass(TblWfOperationInfo.class);
		dc.add(Restrictions.eq("status", new CodeWrapperPlus("0")));
		dc.add(Restrictions.eq("delFlg", "0"));
		list = hibernateTemplate.findByCriteria(dc);
		return list;
	}
	//功能描述 通过步骤查询步骤名称
	public String findTblWfParticularInfoName(String ooid,String stid){
		String stepName = "";
		List list = new ArrayList();
		TblWfParticularInfo tblWfParticularInfo = null;
		//ContextInfo.recoverQuery();
		DetachedCriteria dc = DetachedCriteria.forClass(TblWfParticularInfo.class);
		dc.createAlias("tblWfOperationInfo","tblWfOperationInfo");
		dc.add(Restrictions.eq("tblWfOperationInfo.id", ooid));
		dc.add(Restrictions.eq("delFlg", "0"));
		if(stid!=null && !("").equals(stid)){
			Long stepId = Long.valueOf(stid);
			dc.add(Restrictions.eq("stepId", stepId));
		}
		list = hibernateTemplate.findByCriteria(dc);
		if(list.size()>0){
			tblWfParticularInfo = (TblWfParticularInfo)list.get(0);
			stepName = tblWfParticularInfo.getStepName().toString();
		}
		return stepName;
		
	}
	
	
	
	public void saveHistoryEntryData(String entryStepId,String stepChangeContent,String masterOid) throws Exception{
		Connection con = WfDBConnection.getConnection();
		StringBuffer sql = new StringBuffer();
		sql.append("insert into tbl_wf_propertyentry (entry_step_id,history_step_value,master_oid) values(?,?,?)");
		System.out.println(sql);
		try{
			PreparedStatement st = con.prepareStatement(sql.toString());
			st.setString(1, entryStepId);
			st.setString(2, stepChangeContent);
			st.setString(3, masterOid);
			st.execute();
		}catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		finally
		{
			try{
				con.close();
			}catch(Exception e)
			{	
			}
		}
		
	}
	
	public String searchHistoryEntryData(String entryStepId) throws Exception{
		StringBuffer sql = new StringBuffer();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String result = new String("");
		sql.append("select history_step_value from tbl_wf_propertyentry where entry_step_id = '");
		sql.append(entryStepId);
		sql.append("'");
		System.out.println(sql);
		Connection con = WfDBConnection.getConnection();
		try{
			statement = con.prepareStatement(sql.toString());
			resultSet = statement.executeQuery();
            while (resultSet.next()) {
            	    result  = resultSet.getString("history_step_value");
            }
            return result;
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		finally
		{
			try{
				con.close();
			}catch(Exception e)
			{	
			}
		}
	}
	
	/*
	 * 功能说明：更加实体BEAN的CLASS和属性名称，等到相应的注释。
	 * 
	 * */
	public String searchStepFieldComment(Class entry,String entryField) throws Exception{
		Connection con = null;
		try{
			String tableFieldName = getTableFieldByEntryField(entry,entryField);
			String tableName = getTableNameByEntryClass(entry);
			StringBuffer sql = new StringBuffer();
			PreparedStatement statement = null;
			ResultSet resultSet = null;
			String result = new String("");
			sql.append("select column_name, comments from user_col_comments where table_name = '");
			sql.append(tableName);
			sql.append("' and column_name = '");
			sql.append(tableFieldName);
			sql.append("'");
			System.out.println(sql);
			con = WfDBConnection.getConnection();
			statement = con.prepareStatement(sql.toString());
			resultSet = statement.executeQuery();
            while (resultSet.next()) {
            	    result  = resultSet.getString("comments");
            }
            if(StringUtils.isBlank(result)){
            	return entryField;
            }else{
            	return result;
            }
		}catch(Exception e){
			e.printStackTrace();
			return entryField;
		}
		finally
		{
			try{
				if(con != null){
					con.close();
				}
			}catch(Exception e)
			{	
			}
		}
	}
	
	public String getTableNameByEntryClass(Class entry){
	    Configuration hibernateConf = new Configuration();
        PersistentClass pc = hibernateConf.getClassMapping(entry.getName());
        if (pc == null) {
        	hibernateConf = hibernateConf.addClass(entry);
        	pc = hibernateConf.getClassMapping(entry.getName());
        }
        return pc.getTable().getName();
	}
	
	public String getTableFieldByEntryField (Class entrySrc,String entryField){
		//Class entry = WfUtils.getUnProxyClass(entrySrc.getClass());
		Class entry = entrySrc;
	    Configuration hibernateConf = new Configuration();
        PersistentClass pc = hibernateConf.getClassMapping(entry.getName());
        if (pc == null) {
        	hibernateConf = hibernateConf.addClass(entry);
        	pc = hibernateConf.getClassMapping(entry.getName());
        }
       Property prop = pc.getProperty(entryField);  
       return ((Column) prop.getValue().getColumnIterator().next()).getName();
	}
	
	public static void main(String ap[]){
//		Table t = new WFServiceImpl().getTableByEntryClass(TblAmCostProject.class);
//		System.out.print(t.getName());
//		List  temp = new WFServiceImpl().getTableFiledName(TblAmCostProject.class);
//		System.out.print(t.getColumnIterator().next().toString());
		//String te = new WFServiceImpl().getTableFieldByEntryField(TblAmCostProject.class,"projectCd");
		//System.out.print(te);
	}
}
