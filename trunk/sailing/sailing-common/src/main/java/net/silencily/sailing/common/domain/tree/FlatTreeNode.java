/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.common.domain.tree;

/**
 * 扁平树节点, 与 {@link TreeNode} 不同的是, 它只描述节点本身信息, 而没有父子信息, 
 * 父子信息一般需要通过 Service 得到, 典型应用场景是缓式加载树
 * @since 2006-7-25
 * @author java2enterprise
 * @version $Id: FlatTreeNode.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 */
public interface FlatTreeNode {

    /**
     * 检索唯一标识这个节点的<code>identity</code>, 在一些实现中可能是<code>url</code>, 具体的值
     * 取决于实现
     * @return 标识这个节点的<code>identity</code>
     */
	String getIdentity();
	
    /**
     * 检索这个节点的标题
     * @return 这个节点的标题
     */
    String getCaptain();
	
    /**
     * 当前节点是否是<code>leaf</code>节点, 通常一个节点不允许有子节点才返回<code>true</code>, 这个描述一般是业务含义上的,
     * 如果一个非叶子节点没有子节点这个方法返回<code>false</code>, 只是<code>hasChildren() == true</code>
     * @return 如果是<code>leaf</code>节点返回<code>true</code>, 否则<code>false</code>
     */
    boolean isLeaf();
    
    /**
     * 当前节点是否有子节点, 与 {@link #isLeaf()} 不同, 它真正描述了该节点下是否有子节点, 与业务无关
     * @return whether has children
     */
    boolean isHasChildren();
	
    /**
     * 返回当前节点表示的数据信息, 经常是一个<code>domain object</code>, 可以返回<code>null</code>
     * @return 当前节点表示的数据信息
     */
    Object getData();
    
    /**
     * 得到 ui 上显示的图片类型 ?
     * @return the image Type
     */
    String getImageType();

    /**
     * 此节点展示到 ui 上时是否可选, 默认应该为 true
     * @return whether this node can be selected from ui
     */
    boolean isCanbeSelected();
}
