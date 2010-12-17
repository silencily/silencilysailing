package net.silencily.sailing.common.domain.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;


/**
 * <code>TreeNode</code>的抽象实现, 实现了保存和操作子节点的方案. 这个实现要求每一个<code>id</code>
 * 对应唯一的<code>TreeNode</code>, 每个<code>TreeNode</code>有一个唯一的<code>id</code>,
 * @author scott
 * @since 2006-4-9
 * @version $Id: AbstractTreeNode.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 *
 */
public abstract class AbstractTreeNode implements TreeNode {

    protected List children = Collections.synchronizedList(new LinkedList());

    public void add(TreeNode node) {
        if (node == null) {
            throw new NullPointerException("增加子节点时参数是null,当前节点id=[" + getId() + "],name=[" + getCaptain() + "]");
        }
        
        if (StringUtils.isBlank(node.getId())) {
            throw new NullPointerException("增加子节点时子节点id是null,当前节点id=[" + getId() + "],name=[" + getCaptain() + "]");
        }
        
        if (node.getId().equals(getId()) || find(getHierarchyChildren(), node.getId()) != null) {
            throw new IllegalStateException("要增加的节点已存在,id=[" + node.getId() + "],当前节点id=[" + getId() + "],name=[" + getCaptain() + "]");
        }
        
        synchronized (children) {
            children.add(node);
            sort(children);
        }
    }

    /* 这里未做 synchronizing, but shoule be */
    public List getHierarchyChildren() {
        List list = new ArrayList(children.size() + 1);
        for (ListIterator it = children.listIterator(); it.hasNext(); ) {
            list.addAll(getHierarchyChildrenWithRecursive((TreeNode) it.next()));
        }

        return list;
    }
    
    private List getHierarchyChildrenWithRecursive(TreeNode node) {
        List list = new ArrayList(node.getChildren().size() + 1);
        list.add(node);
        for (ListIterator it = node.getChildren().listIterator(); it.hasNext(); ) {
            list.addAll(getHierarchyChildrenWithRecursive((TreeNode) it.next()));
        }

        return list;
    }

    public List getChildren() {
        return Collections.unmodifiableList(children);
    }
    
    public boolean isLeaf() {
        return children.size() == 0;
    }

    public void remove(TreeNode node) {
        if (node == null) {
            throw new NullPointerException("删除节点["
                + this.getCaptain()
                + "]的子节点时要删除的节点是null,当前节点id=[" + getId() + "]");
        }
        /* 使用 id 判断, 方便在仅仅设置了 id 属性时删除节点 */
        TreeNode t = null;
        if (StringUtils.isNotBlank(node.getId())) {
            t = find(children, node.getId());
        }
        
        if (t == null) {
            throw new IllegalArgumentException("要删除的节点id=["
                + node.getId()
                + "]不存在,当前节点id=["
                + getId()
                + "], name=[" + getCaptain() + "]");
        }
        
        children.remove(t);
    }

    public boolean existWithId(String id) {
        TreeNode node = null;
        if (id != null) {
            node = find(getHierarchyChildren(), id);
        }

        return (node != null);
    }

    public TreeNode findById(String id) {
        if (StringUtils.isBlank(id)) {
            throw new NullPointerException("检索指定id的节点时参数是null");
        }
        
        TreeNode node = find(getHierarchyChildren(), id);
        if (node == null) {
            throw new IllegalArgumentException("要检索的节点id=["
                + id
                + "]不存在, 当前节点id=["
                + getId()
                + "],name=["
                + getCaptain()
                + "]");
        }

        return node;
    }
    
    /**
     * 在直接子节点或所有层次的子节点中检索指定<code>id</code>的节点
     * @param nodes 直接子节点或所有层次的子节点的缓存, 通过{@link #getChildren() getChildren}
     *              或{@link #getHierarchyChildren() getHierarchyChildren}方法获得
     * @param id    要检索的节点的<code>id</code>
     * @return      如果找到返回这个节点, 否则返回<code>null</code>
     */
    final protected TreeNode find(Collection nodes, final String id) {
        return (TreeNode) CollectionUtils.find(nodes, new Predicate() {
            public boolean evaluate(Object element) {
                return ((TreeNode) element).getId().equals(id);
            }
        });
    }

    public abstract String getCaptain();
    
    public abstract String getId();
    
    public abstract Object getData();
    
    public abstract String toString();
    
    /**
     * 这个方法就在<code>add(TreeNode)</code>操作完成之后方法返回之前调用, 给子类一个按照其他
     * 方式排序新加入的子节点的机会, 这个方法仅仅用于排序, 绝对不要在这里执行与节点相关的任何结构性
     * 的操作. 如果不实现这个方法缺省情况下是按照加入顺序排列的
     * @param children 加入子节点后的存储子节点的<code>List</code>
     */
    protected void sort(List children) {}    
}
