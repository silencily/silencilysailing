package net.silencily.sailing.basic.uf.funtree.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import net.silencily.sailing.basic.uf.funtree.service.DisplayFunTreeService;

import org.apache.commons.lang.StringUtils;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;


/**
 * 一体化办公功能树显示逻辑实现
 * @author huxf
 * @version $Id: DisplayFunTreeServiceImpl.java,v 1.1 2010/12/10 10:56:48 silencily Exp $
 * @since 2007-11-23
 */
public class DisplayFunTreeServiceImpl implements DisplayFunTreeService{	
	// permission map for merging user's permission and role's permission
	private HibernateTemplate hibernateTemplate;

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	/**
	 * Get the function by user and role which user belongs to
	 * @param userId
	 * @return
	 */
	private class PermNode implements Comparable
	{
		private String id;
		private String father_id;
		private int level;
		private String name;
		private String type;
		private String url;
		private int order;
		boolean valid;
		private List childs;
		
		public int compareTo(Object o) {
		    // Implicitly tests for correct type:
		    int argi = ((PermNode)o).order;
		    if(order == argi) return 0;
		    if(order < argi) return -1;
		    return 1;
		}
		  
		public int getLevel() {
			return level;
		}
		public void setLevel(int level) {
			this.level = level;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public int getOrder() {
			return order;
		}
		public void setOrder(int order) {
			this.order = order;
		}

		public String getFather_id() {
			return father_id;
		}
		public void setFather_id(String father_id) {
			this.father_id = father_id;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}

		public boolean isValid() {
			return valid;
		}

		public void setValid(boolean valid) {
			this.valid = valid;
		}

		public List getChilds() {
			return childs;
		}

		public void setChilds(List childs) {
			this.childs = childs;
		}
		public boolean isFoder()
		{
			if(type.equals("0"))
				return true;
			return false;
		}
	}

	public List getFunTree(String userId, String prefix) throws Exception{
		// for saving js' node by user's permission
		List userFunList = new ArrayList();
		Connection con = null;
		Statement stFolders = null;
		Statement stPerms = null;
		ResultSet rsFolders = null;
		ResultSet rsPerms = null;
		
		Map folders = new HashMap();

		try {
			// get the data source
			DataSource ds = SessionFactoryUtils.getDataSource(hibernateTemplate.getSessionFactory());

			// wrong data source
			if (ds == null) {
				throw new Exception ("Data source error");
			}
			// get connection
			con = ds.getConnection();

			//查询出root节点下所有的目录
			stFolders = con.createStatement();
			rsFolders = stFolders.executeQuery(makeAllfolderSQL().toString());
			//TODO:构造目录树（复合表）
			while (rsFolders.next()) {
				PermNode pn = extractPermNode(rsFolders,true);
				folders.put(pn.getId(), pn);
				//addTreeNod(rsUserPer, userFunList, userFunList_, prefix);
			}
			for(Iterator it = folders.entrySet().iterator();it.hasNext();)
			{
				Map.Entry entry = (Map.Entry)it.next();
				PermNode pn = (PermNode)entry.getValue();
				if(StringUtils.isNotBlank(pn.getFather_id()))
				{
					PermNode father = (PermNode)folders.get(pn.getFather_id());
					if(null != father)
					{
						if(null == father.getChilds())
							father.setChilds(new ArrayList());
						father.getChilds().add(pn);
					}
				}
			}
				

			//查询出用户关联的所有菜单权限(包括通过角色拥有的权限)
			stPerms = con.createStatement();
			rsPerms = stPerms.executeQuery(makeUserPermSQL(userId).toString());
			//TODO:填充菜单权限到目录树(同时对父级目录置有效标记)
			while (rsPerms.next()) {
				PermNode pn = extractPermNode(rsPerms,false);
				if(StringUtils.isNotBlank(pn.father_id))
				{
					PermNode father = (PermNode)folders.get(pn.getFather_id());
					if(null != father)
					{
						if(null == father.getChilds())
							father.setChilds(new ArrayList());
						pn.setLevel(father.level+1);
						father.getChilds().add(pn);
						setFatherValid(folders,father);
					}
				}
				//addTreeNod(rsRolePer, userFunList, userFunList_, prefix);
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			// release DB resource
			try {
				rsFolders.close();
				rsPerms.close();
				stFolders.close();
				stPerms.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		//TODO:对目录树同层节点按显示顺序进行排序
		//TODO:填充返回列表(不包括没有置有效标记的目录节点)	
		PermNode root = (PermNode)folders.get("root");
		if(null == root)
			return userFunList;
		addTreeNod(userFunList,root,prefix);
		return userFunList;
	}

	private void setFatherValid(Map folders,PermNode foder)
	{
		if(foder.valid)
			return;
		foder.valid = true;
		PermNode father = (PermNode)folders.get(foder.getFather_id());
		if(null != father)
			setFatherValid(folders,father);
	}
	private PermNode extractPermNode(ResultSet rs,boolean folder) throws Exception
	{
		String id_ = rs.getString("ID");
		String father_id = rs.getString("FATHER_ID");
		int level = 0;
		if(folder)
			level = rs.getInt("LEVEL");
		int order = rs.getInt("DISPLAY_ORDER");
		String name = rs.getString("DISPLAYNAME");
		String url = rs.getString("URL");
		String type = rs.getString("NODETYPE");
		
		PermNode pn = new PermNode();
		pn.setId(id_);
		pn.setFather_id(father_id);
		pn.setLevel(level);
		pn.setName(name);
		pn.setOrder(order);
		pn.setType(type);
		pn.setUrl(url);
		
		return pn;
	}
	/**
	 * Make query SQL for permission according to user's role
	 * @param userId
	 * @return
	 */
	private StringBuffer makeAllfolderSQL() {
		// user's permission SQL
		StringBuffer buffer = new StringBuffer();
		
		/*
		 * make the query SQL for user's permission
		 */
		buffer.append("select level, t.id,t.url,t.displayname,t.display_order,t.father_id,t.nodetype ");
		buffer.append("from (select * ");
		buffer.append("from tbl_cmn_permission cp ");
		buffer.append("where cp.nodetype = '0' and cp.del_flg = '0') t ");
		buffer.append("connect by prior id = father_id ");
		buffer.append("start with id = 'root'  ");
		
		return buffer;
	}
	
	/**
	 * Make query SQL for permission according to user id
	 * @param userId
	 * @return
	 */
	private StringBuffer makeUserPermSQL(String userId) {
		// user's permission SQL
		StringBuffer buffer = new StringBuffer();
		
		/*
		 * make the query SQL for user's permission
		 */
		buffer.append("select distinct t.id,t.url,t.displayname,t.display_order,t.father_id,t.nodetype ");
		buffer.append("from tbl_cmn_permission t ");
		buffer.append("where (t.id in (select up.permission_id ");
		buffer.append("from tbl_cmn_user_permission up ");
		buffer.append("where up.user_id = '");
		buffer.append(userId);
		buffer.append("' and up.del_flg = '0') or ");
		buffer.append("t.id in (select rp.permission_id ");
		buffer.append("from tbl_cmn_role_permission rp ");
		buffer.append("where rp.role_id in ");
		buffer.append("(select ur.role_id ");
		buffer.append("from tbl_cmn_user_role ur ");
		buffer.append("where ur.user_id = '" );
		buffer.append(userId);
		buffer.append("' and ur.del_flg = '0') and ");
		buffer.append("rp.del_flg = '0')) and t.nodetype = '1' and ");
		buffer.append("t.urltype = '0' and t.del_flg = '0' ");
			
		return buffer;
	}

	/**
	 * Add tree node, put each node under its father id
	 * @param rs
	 * @param userFunList
	 * @param userFunList_
	 * @throws Exception
	 */
	private void addTreeNod(List userFunList,PermNode node, String prefix) 
		throws Exception {
		if(null == node) return;
		if(node.isFoder())
			if(!node.valid) return;
		StringBuffer perBuffer = new StringBuffer();
		String id_ = node.getId();
		int level_ = node.getLevel();

		if(!node.getId().equals("root"))
		{
			perBuffer.append(level_-1);
			perBuffer.append(", ");
			perBuffer.append(node.getName());
			perBuffer.append(",");
			perBuffer.append(makeUrl(node.getUrl(),prefix));
			perBuffer.append(",, ");
			perBuffer.append(id_);
			perBuffer.append(", ");
			perBuffer.append(Integer.parseInt(node.getType())+2);
			
			// add tree node
			userFunList.add(perBuffer.toString());
		}
		List subFolders = node.getChilds();
		if(subFolders!=null){
			Collections.sort(subFolders);
			for(Iterator it = subFolders.iterator();it.hasNext();)
			{
				addTreeNod(userFunList,(PermNode)it.next(),prefix);
			}
		}

		
	}
	
	/**
	 * If url in DB is empty(folder type) returns "#"
	 * @param url
	 * @return
	 */
	private String makeUrl(String url,String prefix) {
		String urlTmp;
		/*
		 * no url, then do not refresh page
		 */
		if (url == null) {
			return "#";
		} else {
			urlTmp = url.trim();
		}
		
		if ("".equals(urlTmp) || "#".equals(urlTmp)) {
			return "#";
		}
		
		/*
		 * format to a regular url format
		 */
		if (!urlTmp.startsWith("/")) {
			urlTmp = "/" + urlTmp;
		}
		urlTmp = prefix + urlTmp;
		return urlTmp;
	}
}
