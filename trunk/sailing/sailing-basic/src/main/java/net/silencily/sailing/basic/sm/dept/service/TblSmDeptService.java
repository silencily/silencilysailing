package net.silencily.sailing.basic.sm.dept.service;

import java.util.List;

import net.silencily.sailing.basic.sm.domain.TblCmnDept;
import net.silencily.sailing.framework.core.ServiceBase;



/**
 * 组织机构管理, 所有的方法不返回<code>null</code>
 * 
 * @author gaojing
 * @version $Id: TblSmDeptService.java,v 1.1 2010/12/10 10:56:45 silencily Exp $
 * @since 2007-8-29
 */
public interface TblSmDeptService extends ServiceBase {

	String SERVICE_NAME = "sm.deptService";

	/**
	 * 列出指定节点下一级所有配置信息, 参数可以是<code>null</code>, 在这种情况下返回人事 编码根节点下一级的所有节点
	 * 
	 * @param config 列出这个节点下的配置, 如果是<code>null</code>表示列出配置根节点
	 * @return 指定节点下一级所有配置信息, 元素类型是{@link TblCmnDept}
	 */
	List list(TblCmnDept config, String radio);

	/**
	 * 保存或创建一个配置信息
	 * 
	 * @param config 要保存或创建的配置信息
	 
	 void saveOrUpdate(TblCmnDept config);
     */
	/**
	 * 删除指定的配置信息, 如果这个节点下有子节点做级联删除
	 * 
	 * @param config 要删除的节点
	 void delete(TblCmnDept config);

	 */
	/**
	 * 加载指定编码的节点
	 * 
	 * @param id 要加载的节点编码
	 * @return 节点信息
	 
	TblCmnDept load(String id);
	*/
	/**
	 * 在指定的节点下创建新的节点信息
	 * 
	 * @param parentId 指定节点的<code>code</code>
	 * @return 新创建的节点信息
	 */
	TblCmnDept newInstance(String parentId);

	/** 创建自己的无参list()方法,显示所有的节点 
	List list();*/

	/** 获取父id */
	public String getParentId(String id);
}
