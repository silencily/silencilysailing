package net.silencily.sailing.common.crud.service.impl;

import java.util.Iterator;
import java.util.List;

import net.silencily.sailing.common.crud.domain.CommonTableScreen;
import net.silencily.sailing.common.crud.domain.CommonTableView;
import net.silencily.sailing.common.crud.domain.CommonTableViewSet;
import net.silencily.sailing.common.crud.service.CommonTableViewService;
import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.utils.Tools;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;



/**
 * @author zhaoyifei
 *
 */
public class CommonTableViewServiceImpl implements CommonTableViewService {

	private boolean setIsNull=false;
	
	private HibernateTemplate hibernateTemplate;
	/**
	 * @return the hibernateTemplate
	 */
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	/**
	 * @param hibernateTemplate the hibernateTemplate to set
	 */
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	/* (non-Javadoc)
	 * @see com.qware.common.crud.service.CommonTableViewService#getRows(java.lang.String, java.lang.Class)
	 */
	public List getRows(String userId, String pageId) {
		// TODO Auto-generated method stub
		
//			String temp[]=c.getName().split("\\.");
//			//System.out.println(c.getName());
//			String s=temp[temp.length-1];
		DetachedCriteria dc = DetachedCriteria.forClass(CommonTableViewSet.class);
		dc.add(Restrictions.eq("userId", userId));
		
		dc.createAlias("commonTableView","commonTableView");
		dc.add(Restrictions.eq("commonTableView.tableName", pageId));
		dc.add(Restrictions.isNull("orderAsc"));
		dc.addOrder(Order.asc("orderNum"));
		List l= hibernateTemplate.findByCriteria(dc);
		if(l.size()==0)
		{
			this.setIsNull=true;
			DetachedCriteria dcc = DetachedCriteria.forClass(CommonTableView.class);
			dcc.add(Restrictions.eq("tableName", pageId));
			dcc.add(Restrictions.isNotNull("orderby"));
			dcc.addOrder(Order.asc("orderby"));
			l= hibernateTemplate.findByCriteria(dcc);
		}
		return l;
	}

	/* (non-Javadoc)
	 * @see com.qware.common.crud.service.CommonTableViewService#getRows(java.lang.Class)
	 */
	public List getRows(String pageId) {
		// TODO Auto-generated method stub
		DetachedCriteria dc = DetachedCriteria.forClass(CommonTableView.class);
		dc.add(Restrictions.eq("tableName", pageId));
		dc.addOrder(Order.asc("orderby"));
		List l= hibernateTemplate.findByCriteria(dc);
		
		return l;
	}

	/* (non-Javadoc)
	 * @see com.qware.common.crud.service.CommonTableViewService#getRowsDict(java.lang.Class, java.lang.String)
	 */
	public String getRowsDict(String pageId, String rowName) {
		// TODO Auto-generated method stub
		String result=null;
		DetachedCriteria dc = DetachedCriteria.forClass(CommonTableView.class);
		
		dc.add(Restrictions.eq("tableName", pageId));
		dc.add(Restrictions.eq("rowName", rowName));
		dc.addOrder(Order.asc("orderby"));
		List l= hibernateTemplate.findByCriteria(dc);
		if(l.size()!=0)
		{
			CommonTableView ctv=(CommonTableView)l.get(0);
			result=ctv.getSubSystem()+"."+ctv.getModule();
		}
		return result;
	}

	public void saveRows(String[] ids, String userId, String pageId) {
		// TODO Auto-generated method stub
		DetachedCriteria dc = DetachedCriteria.forClass(CommonTableViewSet.class);
		dc.add(Restrictions.eq("userId", userId));
		
		dc.createAlias("commonTableView","commonTableView");
		dc.add(Restrictions.eq("commonTableView.tableName", pageId));
		dc.add(Restrictions.isNull("orderAsc"));
		List l= hibernateTemplate.findByCriteria(dc);
		hibernateTemplate.deleteAll(l);
		if(ids==null)
			return;
		for(int i=0;i<ids.length;i++)
		{
			CommonTableViewSet ctvs=new CommonTableViewSet();
			ctvs.setId(Tools.getPKCode());
			ctvs.setUserId(userId);
			ctvs.setOrderNum(new Integer(i));
			CommonTableView commonTableView=(CommonTableView)hibernateTemplate.load(CommonTableView.class,ids[i]);
			ctvs.setCommonTableView(commonTableView);
			hibernateTemplate.save(ctvs);
		}
	}

	public String getSearchTags(String pageId, String user) {
		// TODO Auto-generated method stub
		
		List l= this.getRows(user, pageId);
		StringBuffer re=new StringBuffer();
		Iterator i=l.iterator();
		while(i.hasNext())
		{
			CommonTableViewSet c=(CommonTableViewSet)i.next();
			re.append("\"");
			re.append(c.getCommonTableView().getRowName());
			re.append("\"");
			if(i.hasNext())
				re.append(",");
		}
		return re.toString();
	}

	public List getRows(String userId, String pageId, String asc) {
		// TODO Auto-generated method stub
		DetachedCriteria dc = DetachedCriteria.forClass(CommonTableViewSet.class);
		dc.add(Restrictions.eq("userId", userId));
		
		dc.createAlias("commonTableView","commonTableView");
		dc.add(Restrictions.eq("commonTableView.tableName", pageId));
		dc.add(Restrictions.eq("orderAsc", asc));
		dc.addOrder(Order.asc("orderNum"));
		List l= hibernateTemplate.findByCriteria(dc);
		
		return l;
	}
	public List getRows(String userId, String pageId, boolean asc) {
		// TODO Auto-generated method stub
		DetachedCriteria dc = DetachedCriteria.forClass(CommonTableViewSet.class);
		dc.add(Restrictions.eq("userId", userId));
		
		dc.createAlias("commonTableView","commonTableView");
		dc.add(Restrictions.eq("commonTableView.tableName", pageId));
		if(asc)
		dc.add(Restrictions.isNotNull("orderAsc"));
		else
			dc.add(Restrictions.isNull("orderAsc"));
		dc.addOrder(Order.asc("orderNum"));
		List l= hibernateTemplate.findByCriteria(dc);
		
		return l;
	}

    /**
     * 
     * 功能描述 取得所有默认排序字段
     * @param pageId
     * @return
     * 2008-2-19 下午01:35:55
     * @version 1.0
     * @author tongjq
     */
    public List getDefOrders(String pageId) {
        // TODO Auto-generated method stub
        DetachedCriteria dc = DetachedCriteria.forClass(CommonTableView.class);
        dc.add(Restrictions.eq("tableName", pageId));
        dc.add(Restrictions.isNotNull("orderAsc"));
        dc.add(Restrictions.isNotNull("sortNum"));
        dc.addOrder(Order.asc("sortNum"));
        List l= hibernateTemplate.findByCriteria(dc);
        
        return l;
    }

	public void saveRows(String[] ids, String userId, String pageId, String asc) {
		// TODO Auto-generated method stub
		DetachedCriteria dc = DetachedCriteria.forClass(CommonTableViewSet.class);
		dc.add(Restrictions.eq("userId", userId));
		
		dc.createAlias("commonTableView","commonTableView");
		dc.add(Restrictions.eq("commonTableView.tableName", pageId));
		dc.add(Restrictions.isNotNull(asc));
		List l= hibernateTemplate.findByCriteria(dc);
		hibernateTemplate.deleteAll(l);
		if(ids==null)
			return;
		for(int i=0;i<ids.length;i++)
		{
			CommonTableViewSet ctvs=new CommonTableViewSet();
			ctvs.setId(Tools.getPKCode());
			ctvs.setUserId(userId);
			ctvs.setOrderNum(new Integer(i));
			CommonTableView commonTableView=(CommonTableView)hibernateTemplate.load(CommonTableView.class,ids[i]);
			ctvs.setCommonTableView(commonTableView);
			ctvs.setOrderAsc(asc);
			hibernateTemplate.save(ctvs);
		}
	}

public void saveRows(String[] ids, String userId, String pageId,boolean asc) {
	// TODO Auto-generated method stub
	DetachedCriteria dc = DetachedCriteria.forClass(CommonTableViewSet.class);
	dc.add(Restrictions.eq("userId", userId));
	
	dc.createAlias("commonTableView","commonTableView");
	dc.add(Restrictions.eq("commonTableView.tableName", pageId));
	dc.add(Restrictions.isNotNull("orderAsc"));
	List l= hibernateTemplate.findByCriteria(dc);
	hibernateTemplate.deleteAll(l);
	if(ids==null)
		return;
	for(int i=0;i<ids.length;i++)
	{
		String id=ids[i];
		String asca="asc";
		if(id.split("-").length==2)
		asca=id.split("-")[1];
		
		
		CommonTableViewSet ctvs=new CommonTableViewSet();
		ctvs.setId(Tools.getPKCode());
		ctvs.setUserId(userId);
		ctvs.setOrderNum(new Integer(i));
		CommonTableView commonTableView=(CommonTableView)hibernateTemplate.load(CommonTableView.class,id.split("-")[0]);
		ctvs.setCommonTableView(commonTableView);
		ctvs.setOrderAsc(asca);
		hibernateTemplate.save(ctvs);
	}
}

public boolean isSetIsNull() {
	return setIsNull;
}

//-----------------------定制显示管理------------------------------
// 根据id查询
public CommonTableView findCtViewById(String id) {
	CommonTableView tmp = null;
	ContextInfo.recoverQuery();
	// 设置查询条件
	DetachedCriteria dc = DetachedCriteria.forClass(CommonTableView.class);
	dc.add(Restrictions.eq("id", id));
	List list = hibernateTemplate.findByCriteria(dc);
	if (list.size() > 0) {
		tmp = (CommonTableView) list.get(0);
	}
	return tmp;
}

// 根据name查询
public CommonTableView findCtViewByName(String tableName) {
	CommonTableView tmp = null;
	ContextInfo.recoverQuery();
	// 设置查询条件
	DetachedCriteria dc = DetachedCriteria.forClass(CommonTableView.class);
	dc.add(Restrictions.eq("tableName", tableName));
	List list = hibernateTemplate.findByCriteria(dc);
	if(list.size()>0){
		tmp = (CommonTableView) list.get(0);
	}
	return tmp;
}

// 列表
public List findAllCtView() {
	ContextInfo.recoverQuery();
	// 设置查询条件
	DetachedCriteria dc = DetachedCriteria.forClass(CommonTableView.class);
	List list = hibernateTemplate.findByCriteria(dc);
	return list;
}

// 保存
public void saveCtView(CommonTableView ctview) {
	try {
		getHibernateTemplate().saveOrUpdate(ctview);
	} catch (RuntimeException re) {
		throw re;
	}
}

// 删除
public void deleteCtView(String tableName) {
	CommonTableView ctview = new CommonTableView();
	ctview.setTableName(tableName);
	this.getHibernateTemplate().delete(ctview);

}

// 复合查询
public List search(CommonTableView ctview) {
	// 设置查询条件
	DetachedCriteria dc = DetachedCriteria.forClass(CommonTableView.class);
	ContextInfo.recoverQuery();
	String rowName = ctview.getRowName();
	String tableName = ctview.getTableName();
	if(tableName !=null && !("").equals(tableName)){
		dc.add(Restrictions.eq("tableName", tableName));
	}
	if (rowName != null && !("").equals(rowName)) {
		dc.add(Restrictions.like("rowName", "%"+rowName+"%"));
	}
	dc.addOrder(Order.asc("orderby"));
	List list = hibernateTemplate.findByCriteria(dc);

	return list;
}

// 根据id查询
public CommonTableScreen findCtScreenById(String id) {
	CommonTableScreen tmp = null;
	ContextInfo.recoverQuery();
	// 设置查询条件
	DetachedCriteria dc = DetachedCriteria
			.forClass(CommonTableScreen.class);
	dc.add(Restrictions.eq("id", id));
	List list = hibernateTemplate.findByCriteria(dc);
	if (list.size() > 0) {
		tmp = (CommonTableScreen) list.get(0);
	}
	return tmp;
}

// 根据name查询
public CommonTableScreen findCtScreenByName(String screenName) {
	CommonTableScreen tmp = null;
	ContextInfo.recoverQuery();
	// 设置查询条件
	DetachedCriteria dc = DetachedCriteria
			.forClass(CommonTableScreen.class);
	dc.add(Restrictions.eq("screenName", screenName));
	List list = hibernateTemplate.findByCriteria(dc);
	if (list.size() > 0) {
		tmp = (CommonTableScreen) list.get(0);
	}
	return tmp;
}

// 列表
public List findAllCtScreen() {
	ContextInfo.recoverQuery();
	// 设置查询条件
	DetachedCriteria dc = DetachedCriteria
			.forClass(CommonTableScreen.class);
	List list = hibernateTemplate.findByCriteria(dc);
	return list;
}

// 保存
public void saveCtScreen(String id,String screenName,String tableName) {
	try {
		CommonTableScreen commonTableScreen = new CommonTableScreen();
		if(id!=null && !("").equals(id)){
			commonTableScreen=(CommonTableScreen)hibernateTemplate.load(CommonTableScreen.class,id);
		}
		commonTableScreen.setScreenName(screenName);
		commonTableScreen.setTableName(tableName);
		this.getHibernateTemplate().saveOrUpdate(commonTableScreen);
	} catch (RuntimeException re) {
		throw re;
	}
}

// 删除
public void deleteCtScreen(String id) {
	CommonTableScreen ctscreen = new CommonTableScreen();
	ctscreen.setId(id);
	this.getHibernateTemplate().delete(ctscreen);

}

// 复合查询
public List searchs(CommonTableScreen ctscreen) {
	// 设置查询条件
	DetachedCriteria dc = DetachedCriteria
			.forClass(CommonTableScreen.class);
	ContextInfo.recoverQuery();
	String screenName = ctscreen.getScreenName();
	String tableName = ctscreen.getTableName();
	if (screenName != null && !("").equals(screenName)) {
		dc.add(Restrictions.like("screenName", "%"+screenName+"%"));
	}
	if (tableName != null && !("").equals(tableName)) {
		dc.add(Restrictions.like("tableName", "%"+tableName+"%"));
	}
	List list = hibernateTemplate.findByCriteria(dc);

	return list;
}

//根据tablename查询
public CommonTableScreen findCtTableByName(String tableName) {
	CommonTableScreen tmp = null;
	ContextInfo.recoverQuery();
	// 设置查询条件
	DetachedCriteria dc = DetachedCriteria
			.forClass(CommonTableScreen.class);
	dc.add(Restrictions.eq("tableName", tableName));
	List list = hibernateTemplate.findByCriteria(dc);
	if (list.size() > 0) {
		tmp = (CommonTableScreen) list.get(0);
	}
	return tmp;
}
public void save(List tableViews, CommonTableScreen bean) {
	this.hibernateTemplate.saveOrUpdate(bean);
	for(int i = 0; i < tableViews.size(); i++){
		CommonTableView tableview = (CommonTableView)tableViews.get(i);
		if("1".equals(tableview.getDelFlg())){
			this.hibernateTemplate.delete(tableview);
			tableViews.remove(tableview);
		}else{
			this.hibernateTemplate.saveOrUpdate(tableview);
		}
	}
}

public List getTableViews(String tableName) {
	//屏蔽掉当前查询条件
	ContextInfo.concealQuery();
	DetachedCriteria dc = DetachedCriteria.forClass(CommonTableView.class);
	dc.add(Restrictions.eq("tableName", tableName));
	return this.hibernateTemplate.findByCriteria(dc);
}
}
