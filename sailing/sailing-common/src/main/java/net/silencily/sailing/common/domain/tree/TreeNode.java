package net.silencily.sailing.common.domain.tree;

import java.util.List;

/**
 * <p>用于表示可以表示成树形结构中的树节点, 这个组件最常用的情形是在<code>jsp</code>中表现数据量
 * 不是很大的树形结构</p>
 * <p>可以参考信息发布栏目管理服务的<code>API</code>及实现恰当地使用这个接口的例子</p>
 * @author scott
 * @since 2006-4-6
 * @version $Id: TreeNode.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 */
public interface TreeNode {
    
    /**
     * 检索这个节点的标题
     * @return 这个节点的标题
     */
    String getCaptain();
    
    /**
     * 检索唯一标识这个节点的<code>id</code>, 在一些实现中可能是<code>url</code>, 具体的值
     * 取决于实现
     * @return 标识这个节点的<code>id</code>
     */
    String getId();
    
    /**
     * 检索这个节点的所有子节点, 子节点的顺序是实现特定的, 可能按照某种顺序, 也可能是按照插入顺序
     * @return 当前节点的所有子节点, <code>List</code>的元素类型是<code>TreeNode</code>,
     *         如果没有子节点返回<code>EMPTY_LIST</code>
     */
    List getChildren();
    
    /**
     * 一个<code>utility</code>方法, 返回这个节点下所有的子节点和子节点的子节点, 一直递归下去
     * 直到叶子节点为止. 返回的数组元素的顺序是以这个节点为根把所有的节点都展开, 从根开始深度优先
     * 遍历的顺序
     * @return 这个节点的所有子节点, <code>List</code>的元素类型是<code>TreeNode</code>,
     *         如果没有子节点返回<code>EMPTY_LIST</code>
     */
    List getHierarchyChildren();
    
    /**
     * 当前节点是否是<code>leaf</code>节点, 通常一个节点不允许有子节点才返回<code>true</code>,
     * 如果一个非叶子节点没有子节点这个方法返回<code>false</code>, 只是<code>getChildren().length == 0</code>
     * @return 如果是<code>leaf</code>节点返回<code>true</code>, 否则<code>false</code>
     */
    boolean isLeaf();
    
    /**
     * 返回当前节点表示的数据信息, 经常是一个<code>domain object</code>, 可以返回<code>null</code>
     * @return 当前节点表示的数据信息
     */
    Object getData();

    /**
     * 为当前节点增加一个子节点, 在已有子节点中的顺序取决于实现, 可能是末尾, 或者是按照某种排序方法
     * 设定的顺序
     * @param node 要增加的子节点
     * @throws NullPointerException 如果参数是<code>null</code>
     * @throws IllegalStateException 如果参数违反了实现的约定, 比如增加重复的节点等
     */
    void add(TreeNode node);
    
    /**
     * 删除当前节点的直接子节点
     * @param node 要删除的子节点
     * @throws NullPointerException 如果参数是<code>null</code>
     * @throws BaseBusinessException 如果参数违反了实现的约定, 比如当前节点没有参数指定的子节点
     */
    void remove(TreeNode node);
    
    /**
     * 当前节点是否存在<code>id</code>是给定值的子节点, 检查递归到子节点和子节点的子节点. 注意
     * 要判断整个层次结构中是否存在这样的节点, 当前节点必须是整个树的根节点
     * @param id 要检查的<code>id</code>
     * @return 如果当前节点存在这样的子节点, 不一定是直接子节点, 返回<code>true</code>, 否则
     *         <code>false</code>
     */
    boolean existWithId(String id);
    
    /**
     * <p>在当前节点下找出<code>id</code>是给定值的子节点, 递归到子节点和子节点的子节点. 注意
     * 要在整个层次结构中是找出具有这个<code>id</code>的节点, 当前节点必须是整个树的根节点
     * , 检索不包括本身, 就是说从子节点开始检索</p>
     * <p>这个方法经常误用在获得根节点后使用根节点<code>id</code>作为参数而发生错误, 这就要在
     * 调用这个方法之前判断是否是根节点的<code>id</code></p>
     * @param id 要检索的节点的<code>id</code>
     * @return 当前节点下具有这个<code>id</code>值的节点
     * @throws NullPointerException 如果参数<code>id</code>是<code>null</code>或
     *         <code>empty</code>
     * @throws IllegalArgumentException 如果没有找到具有这个<code>id</code>值的节点
     */
    TreeNode findById(String id);
    
    /**
     * 返回表示这个节点的字符串形式, 具体的实现可能针对展示有特定的表现形式
     * @return 表现这个节点的字符串 
     */
    String toString();
}
