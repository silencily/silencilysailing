package net.silencily.sailing.common.ui.selector.service;

import java.util.List;

/**
 * 公用选择树方法
 * @author liuz
 *
 */
public interface SelectorService {
	
	String SERVICE_NAME = "common.ui.selectorService";

	/**列出部门树下用户
	 * 这里的<code>currentId</code>是当前部门ID
	 * 默认的根节点为部门=<code>0</code>，如果指定这个currentId,则显示这个部门下所有层次的子部门.
	 */
	List listDeptTree(String currentId);
	
	/**列出部门树下用户
	 * currentId 同上
	 */
	List listUserTree(String currentId);
	
	/**
	 * 指定某个部门下的用户list
	 * @param orgId
	 * @param Level 节点层次
	 * @return
	 */
	List listUserByOrgId(String orgId,int Level);
	
	/** 显式显示指定代码<code>parentId</code>下所属代码节点
	 *  注意这里是显式（一次性）加栽，因此parentId下节点数目不能太多
	 *  默认为根节点0
	 * @param parentId
	 * @return
	 */
	List listAllCodeByParentId(String parentId);
	
	/** 缓式显示指定代码<code>codeId</code>下根层次下代码节点
	 *  与上面不同，这里是缓式加栽，这里的节点数目不受限制
	 *  TODO 这里应该返回本身节点和子节点数据，目前只返回子节点
	 *  注意默认的code=0
	 * @param parentId
	 * @return
	 */
	List listCodeByParentId(String codeId);
}
