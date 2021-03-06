package net.silencily.sailing.basic.sm.domain;

// Generated 2007-11-14 16:40:27 by Hibernate Tools 3.2.0.b9

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.silencily.sailing.basic.sm.permission.service.PermissionService;
import net.silencily.sailing.common.domain.tree.FlatTreeNode;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.hibernate3.EntityPlus;

/**
 * TblCmnPermission generated by hbm2java
 */
public class TblCmnPermission extends EntityPlus implements FlatTreeNode {

	private static Map nodeTypeMap = new HashMap();
	private static Map systemPermissionMap = new HashMap();
	private static Map urltypeMap = new HashMap();
	static {
		nodeTypeMap.put("0", "目录");
		nodeTypeMap.put("1", "功能权限");
		nodeTypeMap.put("2", "数据项权限");
	}
	static {
		systemPermissionMap.put("1", "是");
		systemPermissionMap.put("0", "否");
	}
	static {
		urltypeMap.put("0", "菜单");
		urltypeMap.put("1", "其他");
	}

	// public interface TreeNodeType
	// {
	// int FOLDER = 0;
	// int FOLDER_AND_FUNCTION = 1;
	// int ALL = 3;
	// }
	// private int treeNodeType = TreeNodeType.ALL;

	private String[] nodeTypes = null;

	private TblCmnPermission father;

	private String url;

	private String displayname;

	private String nodetype;

	private String nodeTypeName;

	private String urltype;

	private String urltypeCh;

	private Short displayOrder;

	private String permissionCd;

	private String note;

	private String permissionRoute;

	private String systemPermission;
	private String systemPermissionCh;

	private Set tblCmnRolePermissions = new HashSet(0);

	private Set children = new HashSet(0);

	private Set tblCmnUserPermissions = new HashSet(0);

	public TblCmnPermission() {
	}

	private static PermissionService service() {
		return (PermissionService) ServiceProvider
				.getService(PermissionService.SERVICE_NAME);
	}

	/**
	 * 判断当前配置节点是否是叶子节点
	 * 
	 * @return 如果是叶子节点返回<code>true</code>
	 */
	public boolean isLeaf() {
		return !this.isHasChildren();

	}

	// private boolean isLeaf(Collection c,)

	/**
	 * 当前节点是否是根节点
	 * 
	 * @return 如果这个实例是根节点返回<code>true</code>
	 */
	public boolean isRootNode() {
		return service().getInitRoot().getId().equals(getId());
	}

	public String getCaptain() {
		return this.getDisplayname();
	}

	public Object getData() {
		return this;
	}

	public String getIdentity() {
		return getId();
	}

	public String getImageType() {
		int imgtype = Integer.parseInt(this.nodetype) + 1;
		return String.valueOf(imgtype);
		// return "0".equals(getNodetype())? "1" : "1".equals(getNodetype())?
		// "2" : "3";
	}

	public boolean isCanbeSelected() {
		return true;
	}

	public boolean isHasChildren() {
		Iterator it = null;
		if (null == this.nodeTypes)
			return !getChildren().isEmpty();
		else {
			it = getChildren().iterator();
			while (it.hasNext()) {
				TblCmnPermission perm = (TblCmnPermission) it.next();
				for (int i = 0; i < this.nodeTypes.length; i++) {
					if (this.nodeTypes[i].equals(perm.nodetype))
						return true;
				}
			}

		}
		return false;
		// return getChildren().size() > 0;
	}

	public TblCmnPermission getFather() {
		return this.father;
	}

	public void setFather(TblCmnPermission tblCmnPermission) {
		this.father = tblCmnPermission;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDisplayname() {
		return this.displayname;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}

	public String getNodetype() {
		return this.nodetype;
	}

	public void setNodetype(String nodetype) {
		this.nodetype = nodetype;
	}

	public String getUrltype() {
		return this.urltype;
	}

	public void setUrltype(String urltype) {
		this.urltype = urltype;
	}

	public Short getDisplayOrder() {
		return this.displayOrder;
	}

	public void setDisplayOrder(Short displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getPermissionCd() {
		return this.permissionCd;
	}

	public void setPermissionCd(String permissionCd) {
		this.permissionCd = permissionCd;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getSystemPermission() {
		return this.systemPermission;
	}

	public void setSystemPermission(String systemPermission) {
		this.systemPermission = systemPermission;
	}

	public Set getTblCmnRolePermissions() {
		return this.tblCmnRolePermissions;
	}

	public void setTblCmnRolePermissions(Set tblCmnRolePermissions) {
		this.tblCmnRolePermissions = tblCmnRolePermissions;
	}

	public Set getTblCmnUserPermissions() {
		return this.tblCmnUserPermissions;
	}

	public void setTblCmnUserPermissions(Set tblCmnUserPermissions) {
		this.tblCmnUserPermissions = tblCmnUserPermissions;
	}

	public Set getChildren() {
		return children;
	}

	public void setChildren(Set children) {
		this.children = children;
	}

	public String getNodeTypeName() {
		return (String) nodeTypeMap.get(nodetype);
	}

	/**
	 * 
	 * 功能描述 取得权限路径
	 * 
	 * @return 2007-11-23 下午08:09:33
	 * @version 1.0
	 * @author baihe
	 */
	public String getPermissionRoute() {
		if (father != null)
			return father.getPermissionRoute() + "/" + this.displayname;
		else
			return this.displayname;
	}

	public void setPermissionRoute(String permissionRoute) {
		this.permissionRoute = permissionRoute;
	}

	public static Map getNodeTypeMap() {
		return nodeTypeMap;
	}

	public static void setNodeTypeMap(Map nodeTypeMap) {
		TblCmnPermission.nodeTypeMap = nodeTypeMap;
	}

	public String getSystemPermissionCh() {
		return (String) systemPermissionMap.get(systemPermission);

	}

	public String getUrltypeCh() {
		return (String) urltypeMap.get(urltype);
	}

	public static Map getSystemPermissionMap() {
		return systemPermissionMap;
	}

	public static void setSystemPermissionMap(Map systemPermissionMap) {
		TblCmnPermission.systemPermissionMap = systemPermissionMap;
	}

	public static Map getUrltypeMap() {
		return urltypeMap;
	}

	public static void setUrltypeMap(Map urltypeMap) {
		TblCmnPermission.urltypeMap = urltypeMap;
	}

	public String[] getNodeTypes() {
		return nodeTypes;
	}

	public void setNodeTypes(String[] nodeTypes) {
		this.nodeTypes = nodeTypes;
	}

}
