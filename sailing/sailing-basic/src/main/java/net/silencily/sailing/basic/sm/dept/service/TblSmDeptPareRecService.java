package net.silencily.sailing.basic.sm.dept.service;

import java.util.List;

import net.silencily.sailing.basic.sm.domain.TblCmnDeptPareRec;
import net.silencily.sailing.framework.core.ServiceBase;


/**
 * 组织机构管理, 所有的方法不返回<code>null</code>
 * 
 * @author gaojing
 * @version $Id: TblSmDeptPareRecService.java,v 1.1 2010/12/10 10:56:45 silencily Exp $
 * @since 2007-8-29
 */
public interface TblSmDeptPareRecService extends ServiceBase {

	String SERVICE_NAME = "sm.deptParentService";

	/**
	 * 保存或创建一个配置信息
	 * 
	 * @param config 要保存或创建的配置信息
	 
	void saveOrUpdate(TblHrDeptPareRec config);
	*/
	/** 用于显示状态变更履历 */
//	List list();

	/**
	 * 加载指定编码的节点
	 * 
	 * @param id 要加载的节点编码
	 * @return 节点信息
	 */
//	TblHrDeptPareRec load(String id);

	/**
	 * 在指定的节点下创建新的节点信息
	 * 
	 * @param parentId 指定节点的<code>code</code>
	 * @return 新创建的节点信息
	 */
	TblCmnDeptPareRec newInstance(String parentCode);

}
