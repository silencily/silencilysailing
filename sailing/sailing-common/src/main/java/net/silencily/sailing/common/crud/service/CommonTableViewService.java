package net.silencily.sailing.common.crud.service;

import java.util.List;

import net.silencily.sailing.common.crud.domain.CommonTableScreen;
import net.silencily.sailing.common.crud.domain.CommonTableView;
import net.silencily.sailing.framework.core.ServiceBase;


/**
 * @author zhaoyifei
 *
 */
public interface CommonTableViewService extends ServiceBase{

	String SERVICE_NAME = "common.ViewRows";
	/**
	 * 
	 * 功能描述 得到用户定制的显示列
	 * @param userId 用户id
	 * @param c 表名（持久化类名）
	 * @return 列名列表
	 * 2007-8-22上午10:51:14
	 */
	public List getRows(String userId,String pageId);
	
	/**
	 * 
	 * 功能描述 得到用户定制的排序条件
	 * @param userId 用户id
	 * @param c 表名（持久化类名）
	 * @param asc 排序方式 asc，desc
	 * @return 列名列表
	 * 2007-8-22上午10:51:14
	 */
	public List getRows(String userId,String pageId,String asc);
	/**
	 * 
	 * 功能描述 得到用户定制的排序条件
	 * @param userId 用户id
	 * @param c 表名（持久化类名）
	 * @param asc 是否排序条件
	 * @return 列名列表
	 * 2007-8-22上午10:51:14
	 */
	public List getRows(String userId, String pageId, boolean asc);
	
	/**
	 * 
	 * 功能描述 所有可以显示的列名
	 * @param c 表名（持久化类名）
	 * @return 列名列表
	 * 2007-8-22上午10:54:05
	 */
	public List getRows(String pageId);
	
    /**
     * 
     * 功能描述 得到默认的排序条件
     * @param c 表名（持久化类名）
     * @return 列名列表
     * 2007-8-22上午10:51:14
     */
    public List getDefOrders(String pageId);
	/**
	 * 
	 * 功能描述 得到列的数据字典
	 * @param c
	 * @param rowName
	 * @return
	 * 2007-8-22上午11:06:12
	 */
	public String getRowsDict(String pageId,String rowName);
	
	/**
	 * 
	 * 功能描述 保存
	 * @param l
	 * Oct 10, 2007 11:11:15 AM
	 */
	public void saveRows(String[] ids,String userId,String pageId);
	
	/**
	 * 
	 * 功能描述 保存
	 * @param l
	 * Oct 10, 2007 11:11:15 AM
	 */
	public void saveRows(String[] ids,String userId,String pageId,String asc);
	
	/**
	 * 
	 * 功能描述 查询画面个性化定制数据
	 * @param pageId 页面id
	 * @param user 用户
	 * @return <code>"'aa','bb','dd'"</code>
	 * Nov 28, 2007 4:57:48 PM
	 * @version 1.0
	 * @author zhaoyf
	 */
	public String getSearchTags(String pageId,String user);
	
	/**
	 * 
	 * 功能描述 保存
	 * @param l
	 * Oct 10, 2007 11:11:15 AM
	 */
	public void saveRows(String[] ids, String userId, String pageId,boolean asc);
	
	/**
	 * 
	 * 功能描述 是否有设置项
	 * @return
	 * Jan 9, 2008 1:27:23 PM
	 * @version 1.0
	 * @author zhaoyf
	 */
	public boolean isSetIsNull();
	
//	------------------------定制显示管理---------------------
	/**
	 * 
	 * 功能描述 通过 id 查询定制信息表
	 * @param id
	 * @return
	 * 2008-01-28 上午10:26:31
	 * @version 1.0
	 * @author yangxl
	 */
	public CommonTableView findCtViewById(String id);
	/**
	 * 
	 * 功能描述 通过 tableName 查询定制信息表
	 * @param tableName
	 * @return
	 * 2008-01-28 上午10:26:31
	 * @version 1.0
	 * @author yangxl
	 */
	public CommonTableView findCtViewByName(String tableName);
	
	/**
	 * 
	 * 功能描述 所有定制信息 
	 * @param 
	 * @return
	 * 2008-01-28 上午10:26:32
	 * @version 1.0
	 * @author yangxl
	 */
	public List findAllCtView();
	
	/**
	 * 
	 * 功能描述 存储定制信息 
	 * @param ctview
	 * @return
	 * 2008-01-28 上午10:26:35
	 * @version 1.0
	 * @author yangxl
	 */
	public void saveCtView(CommonTableView ctview);

	/**
	 * 
	 * 功能描述 删除定制信息 
	 * @param tableName
	 * @return
	 * 2008-01-28 上午10:26:38
	 * @version 1.0
	 * @author yangxl
	 */
	public void deleteCtView(String tableName);
	
	/**
	 * 
	 * 功能描述  复合查询
	 * @param ctview
	 * @return
	 * 2008-01-28 上午11:08:40
	 * @version 1.0
	 * @author yangxl
	 */
	public List search(CommonTableView ctview);
	
	/**
	 * 
	 * 功能描述 通过 id 查询页面信息
	 * @param id
	 * @return
	 * 2008-01-28 上午10:26:31
	 * @version 1.0
	 * @author yangxl
	 */
	public CommonTableScreen findCtScreenById(String id);
	/**
	 * 
	 * 功能描述 通过 name 查询页面信息
	 * @param name
	 * @return
	 * 2008-01-28 上午10:26:31
	 * @version 1.0
	 * @author yangxl
	 */
	public CommonTableScreen findCtScreenByName(String name);
	
	/**
	 * 
	 * 功能描述 所有页面信息 
	 * @param 
	 * @return
	 * 2008-01-28 上午10:26:32
	 * @version 1.0
	 * @author yangxl
	 */
	public List findAllCtScreen();
	
	/**
	 * 
	 * 功能描述 存储页面信息 
	 * @param ctscreen
	 * @return
	 * 2008-01-28 上午10:26:35
	 * @version 1.0
	 * @author yangxl
	 */
	public void saveCtScreen(String id,String screenName,String tableName);

	/**
	 * 
	 * 功能描述 删除页面信息 
	 * @param id
	 * @return
	 * 2008-01-28 上午10:26:38
	 * @version 1.0
	 * @author yangxl
	 */
	public void deleteCtScreen(String id);
	
	/**
	 * 
	 * 功能描述  复合查询
	 * @param ctscreen
	 * @return
	 * 2008-01-28 上午11:08:40
	 * @version 1.0
	 * @author yangxl
	 */
	public List searchs(CommonTableScreen ctscreen);
	
	/**
	 * 
	 * 功能描述  通过tableName查询（不能同名）
	 * @param tableName
	 * @return
	 * 2008-01-28 上午11:08:40
	 * @version 1.0
	 * @author yangxl
	 */
	public CommonTableScreen findCtTableByName(String tableName);
	/**
	 * tableview by baihe
	 * @param tableViews
	 * @param bean
	 */
	public void save(List tableViews, CommonTableScreen bean);
	public List getTableViews(String tableName);
}
