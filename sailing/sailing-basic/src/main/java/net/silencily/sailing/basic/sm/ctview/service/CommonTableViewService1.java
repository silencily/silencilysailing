package net.silencily.sailing.basic.sm.ctview.service;

import java.util.List;

import net.silencily.sailing.common.crud.domain.CommonTableView;
import net.silencily.sailing.framework.core.ServiceBase;


/**
 * 
 * @author yangxl
 * @version 1.0
 */
public interface CommonTableViewService1 extends ServiceBase{

	String SERVICE_NAME = "common.ViewRows";
	
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
	 * 功能描述 通过 name 查询定制信息表
	 * @param name
	 * @return
	 * 2008-01-28 上午10:26:31
	 * @version 1.0
	 * @author yangxl
	 */
	public CommonTableView findCtViewByName(String name);
	
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
	 * @param id
	 * @return
	 * 2008-01-28 上午10:26:38
	 * @version 1.0
	 * @author yangxl
	 */
	public void deleteCtView(String id);
	
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
}
