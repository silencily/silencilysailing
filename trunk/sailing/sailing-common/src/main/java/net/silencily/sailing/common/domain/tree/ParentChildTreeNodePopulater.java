package net.silencily.sailing.common.domain.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;


public abstract class ParentChildTreeNodePopulater {
    public static TreeNode populater() {
        ParentChildTreeNode n0 = new ParentChildTreeNode("root", "root1", "root name", "root url");
        ParentChildTreeNode n1 = new ParentChildTreeNode("1000", "root", "1000 name", "1000 url");
        ParentChildTreeNode n2 = new ParentChildTreeNode("1001", "1000", "1001 name", "1001 url");
        ParentChildTreeNode n3 = new ParentChildTreeNode("1002", "1000", "1002 name", "1002 url");
        ParentChildTreeNode n4 = new ParentChildTreeNode("2000", "1000", "2000 name", "2000 url");
        ParentChildTreeNode n5 = new ParentChildTreeNode("2001", "2000", "2001 name", "2001 url");
        ParentChildTreeNode n6 = new ParentChildTreeNode("2002", "2000", "2002 name", "2002 url");
        ParentChildTreeNode n7 = new ParentChildTreeNode("2003", "2002", "2003 name", "2003 url");
        ParentChildTreeNode n8 = new ParentChildTreeNode("2004", "2003", "2004 name", "2004 url");
    
        
        List list = new ArrayList();
        list.add(n0);
        list.add(n1);
        list.add(n2);
        list.add(n3);
        list.add(n4);
        list.add(n5);
        list.add(n6);
        list.add(n7);
        list.add(n8);
    
        
        return populater(list, "root");
    }
    
    public static TreeNode populater(List nodes, String parentId) {
        ParentChildTreeNode root = findNodeById(nodes, parentId);
        List children = findChildrenByParentId(nodes, parentId);
        
        for (int i = 0; i < children.size(); i++) {
            ParentChildTreeNode node = (ParentChildTreeNode) children.get(i);
            node = (ParentChildTreeNode) populater(nodes, node.getId());
            root.add(node);
        }

        return root;
    }
    
    /**
     * 
     * @param nodes
     * @param id
     * @return
     * @throws IllegalStateException 
     */
    public static ParentChildTreeNode findNodeById(List nodes, final String id) {
        ParentChildTreeNode node = (ParentChildTreeNode) CollectionUtils.find(nodes, new Predicate() {
            public boolean evaluate(Object element) {
                return id.equals(((ParentChildTreeNode) element).getId());
            }});
        if (node == null) {
            throw new IllegalStateException("指定id[" + id + "]的节点没找到");
        }
        return node;
    }

    /**
     * 
     * @param parentId
     * @return
     */
    public static List findChildrenByParentId(List nodes,final String parentId) {
        Collection c = CollectionUtils.select(nodes, new Predicate() {
            public boolean evaluate(Object element) {
                return parentId.equals(((ParentChildTreeNode) element).getParentId());
            }});
        List ret = new ArrayList(c.size());
        ret.addAll(c);
        return ret;
    }
    
    public static void main(String[] args) {
        ParentChildTreeNode n0 = new ParentChildTreeNode("root", "root1", "root name", "root url");
        ParentChildTreeNode n1 = new ParentChildTreeNode("1000", "root", "1000 name", "1000 url");
        ParentChildTreeNode n2 = new ParentChildTreeNode("1001", "1000", "1001 name", "1001 url");
        ParentChildTreeNode n3 = new ParentChildTreeNode("1002", "1000", "1002 name", "1002 url");
        ParentChildTreeNode n4 = new ParentChildTreeNode("2000", "1000", "2000 name", "2000 url");
        ParentChildTreeNode n5 = new ParentChildTreeNode("2001", "2000", "2001 name", "2001 url");
        ParentChildTreeNode n6 = new ParentChildTreeNode("2002", "2000", "2002 name", "2002 url");
        ParentChildTreeNode n7 = new ParentChildTreeNode("2003", "2002", "2003 name", "2003 url");
        ParentChildTreeNode n8 = new ParentChildTreeNode("2004", "2003", "2004 name", "2004 url");
    
        
        List list = new ArrayList();
        list.add(n0);
        list.add(n1);
        list.add(n2);
        list.add(n3);
        list.add(n4);
        list.add(n5);
        list.add(n6);
        list.add(n7);
        list.add(n8);
    
        
        TreeNode node = populater(list, "root");
        System.out.println(node);
    }
    
    
}
