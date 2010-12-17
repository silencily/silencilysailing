package net.silencily.sailing.common.ui.tree;

/**
 * 做为一棵树的节点，那么他应该有以下的特征
 * 
 */
public interface Treeable extends Comparable {

	/**
	 * 父id
	 * <p>
	 * 顶层节点的父id为空
	 * @return
	 */
	public String getParentId();
	
	/**
	 * 顺序
	 * @return
	 */
	public Integer getSequence();
	
	/**
	 * identity
	 * @return
	 */
	public String getId();
	
	/**
	 * 设置节点的层数
	 * @param level
	 */
	public void setLevel(Integer level);
	
}
