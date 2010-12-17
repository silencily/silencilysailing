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
 * <code>TreeNode</code>�ĳ���ʵ��, ʵ���˱���Ͳ����ӽڵ�ķ���. ���ʵ��Ҫ��ÿһ��<code>id</code>
 * ��ӦΨһ��<code>TreeNode</code>, ÿ��<code>TreeNode</code>��һ��Ψһ��<code>id</code>,
 * @author scott
 * @since 2006-4-9
 * @version $Id: AbstractTreeNode.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 *
 */
public abstract class AbstractTreeNode implements TreeNode {

    protected List children = Collections.synchronizedList(new LinkedList());

    public void add(TreeNode node) {
        if (node == null) {
            throw new NullPointerException("�����ӽڵ�ʱ������null,��ǰ�ڵ�id=[" + getId() + "],name=[" + getCaptain() + "]");
        }
        
        if (StringUtils.isBlank(node.getId())) {
            throw new NullPointerException("�����ӽڵ�ʱ�ӽڵ�id��null,��ǰ�ڵ�id=[" + getId() + "],name=[" + getCaptain() + "]");
        }
        
        if (node.getId().equals(getId()) || find(getHierarchyChildren(), node.getId()) != null) {
            throw new IllegalStateException("Ҫ���ӵĽڵ��Ѵ���,id=[" + node.getId() + "],��ǰ�ڵ�id=[" + getId() + "],name=[" + getCaptain() + "]");
        }
        
        synchronized (children) {
            children.add(node);
            sort(children);
        }
    }

    /* ����δ�� synchronizing, but shoule be */
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
            throw new NullPointerException("ɾ���ڵ�["
                + this.getCaptain()
                + "]���ӽڵ�ʱҪɾ���Ľڵ���null,��ǰ�ڵ�id=[" + getId() + "]");
        }
        /* ʹ�� id �ж�, �����ڽ��������� id ����ʱɾ���ڵ� */
        TreeNode t = null;
        if (StringUtils.isNotBlank(node.getId())) {
            t = find(children, node.getId());
        }
        
        if (t == null) {
            throw new IllegalArgumentException("Ҫɾ���Ľڵ�id=["
                + node.getId()
                + "]������,��ǰ�ڵ�id=["
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
            throw new NullPointerException("����ָ��id�Ľڵ�ʱ������null");
        }
        
        TreeNode node = find(getHierarchyChildren(), id);
        if (node == null) {
            throw new IllegalArgumentException("Ҫ�����Ľڵ�id=["
                + id
                + "]������, ��ǰ�ڵ�id=["
                + getId()
                + "],name=["
                + getCaptain()
                + "]");
        }

        return node;
    }
    
    /**
     * ��ֱ���ӽڵ�����в�ε��ӽڵ��м���ָ��<code>id</code>�Ľڵ�
     * @param nodes ֱ���ӽڵ�����в�ε��ӽڵ�Ļ���, ͨ��{@link #getChildren() getChildren}
     *              ��{@link #getHierarchyChildren() getHierarchyChildren}�������
     * @param id    Ҫ�����Ľڵ��<code>id</code>
     * @return      ����ҵ���������ڵ�, ���򷵻�<code>null</code>
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
     * �����������<code>add(TreeNode)</code>�������֮�󷽷�����֮ǰ����, ������һ����������
     * ��ʽ�����¼�����ӽڵ�Ļ���, �������������������, ���Բ�Ҫ������ִ����ڵ���ص��κνṹ��
     * �Ĳ���. �����ʵ���������ȱʡ������ǰ��ռ���˳�����е�
     * @param children �����ӽڵ��Ĵ洢�ӽڵ��<code>List</code>
     */
    protected void sort(List children) {}    
}
