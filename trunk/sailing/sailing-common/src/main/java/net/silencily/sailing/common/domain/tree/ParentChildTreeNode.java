package net.silencily.sailing.common.domain.tree;


public class ParentChildTreeNode extends TigraTreeNode {
    private String id;
    private String parentId;
    private String name;
    private String url;

    public ParentChildTreeNode(String id, String parentId, String name, String url) {
        super();
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.url = url;
    }

    public String getCaptain() {
        return name;
    }

    public String getId() {
        return id;
    }

    public Object getData() {
        return url;
    }
    
    public String getParentId() {
        return parentId;
    }
}
