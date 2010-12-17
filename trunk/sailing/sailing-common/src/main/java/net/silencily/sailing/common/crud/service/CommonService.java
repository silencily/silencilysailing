package net.silencily.sailing.common.crud.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.silencily.sailing.framework.core.ServiceBase;
import net.silencily.sailing.struts.BaseFormPlus;

import org.hibernate.criterion.DetachedCriteria;


/**
 * @author zhaoyifei
 *
 */
public interface CommonService extends ServiceBase{

	String SERVICE_NAME = "common.CommonCURD";
	
	/**
	 * 
	 * 功能描述 新增数据库数据，参数为新建的持久化对象
	 * @param e 持久化对象
	 * Oct 31, 2007 10:33:10 AM
	 */
	public void add(Object e);
	
	/**
	 * 
	 * 功能描述 修改数据库数据。
	 * @param e 持久化对象
	 * Oct 31, 2007 10:34:12 AM
	 */
	public void update(Object e);
	
	/**
	 * 
	 * 功能描述	物理删除数据库数据
	 * @param e 持久化对象
	 * Oct 31, 2007 10:52:01 AM
	 */
	public void delete(Object e);
	
	/**
	 * 
	 * 功能描述 逻辑删除数据库数据，修改持久化对象的delFlg字段为"1"
	 * @param e 持久化对象
	 * Oct 31, 2007 10:52:36 AM
	 */
	public void deleteLogic(Object e);
	/**
	 * 
	 * 功能描述 保存或修改数据库数据
	 * @param e 持久化对象
	 * Oct 31, 2007 10:53:33 AM
	 */
	public void saveOrUpdate(Object e);
	
	/**
	 * 
	 * 功能描述 查询数据，条件自动增加
	 * @param c 数据库数据类型
	 * @param user 查询用户
	 * @return List 返回符合条件的数据库数据
	 * Oct 31, 2007 10:53:54 AM
	 */
	public List getList(Class c,String user);
	
	/**
	 * 
	 * 功能描述 查询数据，条件自动增加
	 * @param c 数据库数据类型
	 * @return List 返回符合条件的数据库数据
	 * Oct 31, 2007 10:56:28 AM
	 */
	public List getList(Class c);
	/**
	 * 
	 * 功能描述  查询数据，条件自动增加
	 * @param c  数据库数据类型
	 * @param user 查询用户
	 * @param pageId 页面id
	 * @param orderAsc 排序集 升序
	 * @param orderDesc 排序集 降序
	 * @return ViewBean 定制显示列表的对象
	 * Oct 31, 2007 10:57:11 AM
	 */
	public ViewBean fetchAll(Class c,String user,String pageId,List orderAsc,List orderDesc);
	
	/**
	 * 
	 * 功能描述  查询数据，条件自动增加
	 * @param c 数据数据类型
	 * @param user 查询用户
	 * @param pageId 页面id
	 * @return ViewBean 定制显示列表的对象
	 * Oct 31, 2007 10:59:11 AM
	 */
	public ViewBean fetchAll(Class c,String user,String pageId);
	
	/**
	 * 
	 * 功能描述  查询数据，条件自动增加
	 * @param c 数据库数据类型
	 * @param pageId 页面id
	 * @return ViewBean 定制显示列表的对象
	 * Oct 31, 2007 10:59:33 AM
	 */
	public ViewBean fetchAll(Class c,String pageId);
	
	/**
	 * 
	 * 功能描述 加载数据库中的一条数据
	 * @param c 数据库数据类型
	 * @param id 数据的id
	 * @return 数据库数据
	 * Oct 31, 2007 11:00:00 AM
	 */
	public Object load(Class c,String id);
	/**
	 * 
	 * 功能描述 加载数据库中的一条数据
	 * @param c 数据库数据类型
	 * @param id 数据的id
	 * @return 数据库数据
	 * Oct 31, 2007 11:00:00 AM
	 */
	public Object writeLoad(Class c,String id);
	
	/**
	 * 
	 * 功能描述 根据条件查询数据条数，主要用于唯一性验证
	 * @param dc 查询条件
	 * @return 查询数目
	 * Oct 31, 2007 11:01:04 AM
	 */
	public int getSize(DetachedCriteria dc);
	
	/**
	 * 
	 * 功能描述 批量逻辑删除数据
	 * @param c 数据库数据类型
	 * @param oids id集合
	 * @return 删除条数
	 * Oct 31, 2007 6:10:35 PM
	 */
	public int deleteAllLogic(Class c,List oids);
	
	/**
	 * 
	 * 功能描述 批量物理删除数据
	 * @param c 数据库数据类型
	 * @param oids id集合
	 * @return 删除条数
	 * Oct 31, 2007 6:10:35 PM
	 */
	public int deleteAll(Class c,List oids);
	
	/**
	 * 
	 * 功能描述 根据条件返回子集
	 * @param s 集合
	 * @param bfp form
	 * @return 符合条件子集
	 * Nov 6, 2007 10:13:16 AM
	 */
	public Set getSubSet(Set s,BaseFormPlus bfp);
	
	
	/**
	 * 
	 * 功能描述 批量逻辑删除数据
	 * @param c 数据集合
	 * @return 删除条数
	 * Oct 31, 2007 6:10:35 PM
	 */
	public int deleteAllLogic(Collection c);
	
	/**
	 * 
	 * 功能描述 批量物理删除数据
	 * @param c 数据集合
	 * @return 删除条数
	 * Oct 31, 2007 6:10:35 PM
	 */
	public int deleteAll(Collection c);
	
	/**
	 * 注意 此方法不推荐使用!!!请使用listSetToVB代替此方法
	 * 功能描述 按照传送条件设置viewbean
	 * @param c 数据库数据类型
	 * @param pageId 页面id
	 * @param data 要设置在viewbean中的数据list
	 * @return ViewBean 定制显示列表的对象
	 * 2007-11-26 上午11:34:09
	 * @version 1.0
	 * @author lihe
	 */
	public ViewBean setVBByList(String pageId,List data);
    
	/**
	 *
	 * 功能描述 按照传送条件设置viewbean
	 * @param c 数据库数据类型
	 * @param pageId 页面id
	 * @param data 要设置在viewbean中的数据list
	 * @return ViewBean 定制显示列表的对象
	 * 2008-02-15 上午11:34:09
	 * @version 1.0
	 * @author tongjq
	 */
	public ViewBean listSetToVB(String pageId,List data);
    /**
     *
     * 功能描述 按照条件读取数据
     * @param dc 条件
     * @return List 对象数据
     * 2008-01-05 13:09:11
     * @version 1.0
     * @author tongjq
     */
    public List findByCriteria(DetachedCriteria dc);
}
