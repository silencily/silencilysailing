package net.silencily.sailing.basic.sm.domain;

import java.util.HashSet;
import java.util.Set;

import net.silencily.sailing.common.domain.tree.FlatTreeNode;
import net.silencily.sailing.hibernate3.EntityPlus;

public class TblCmnEntity extends EntityPlus implements FlatTreeNode {

	private String fatherModule;

	private String name;

	private String className;

	private Set tblCmnEntityMember = new HashSet(0);

	/** 当前节点的直接子节点 */
	private Set children = new HashSet(0);

	// - utility methods
	/**
	 * 判断当前配置节点是否是叶子节点
	 * 
	 * @return 如果是叶子节点返回<code>true</code>
	 */
	public boolean isLeaf() {
		return getChildren().isEmpty();
	}

	public TblCmnEntity() {

	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getFatherModule() {
		return fatherModule;
	}

	public void setFatherModule(String fatherModule) {
		this.fatherModule = fatherModule;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set getTblCmnEntityMember() {
		return tblCmnEntityMember;
	}

	public void setTblCmnEntityMember(Set tblCmnEntityMember) {
		this.tblCmnEntityMember = tblCmnEntityMember;
	}

	public Set getChildren() {
		return children;
	}

	public void setChildren(Set children) {
		this.children = children;
	}

	public String getCaptain() {
		return getName();
	}

	public Object getData() {
		return this;
	}

	public String getIdentity() {
		return getId();
	}

	public String getImageType() {
		return "1";
	}

	public boolean isCanbeSelected() {
		return true;
	}

	public boolean isHasChildren() {
		return getChildren().size() > 0;
	}

}
