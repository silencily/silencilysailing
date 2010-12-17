package net.silencily.sailing.basic.sm.domain;

import java.util.HashSet;
import java.util.Set;

import net.silencily.sailing.common.domain.tree.FlatTreeNode;
import net.silencily.sailing.hibernate3.EntityPlus;

public class TblCmnEntityMember extends EntityPlus implements
		java.io.Serializable, FlatTreeNode {

	private TblCmnEntity tblCmnEntity;

	private String memberName;

	private String type;

	private String name;

	private String childrensName;

	private Set tblCmnUserMember = new HashSet(0);

	public TblCmnEntityMember() {

	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public TblCmnEntity getTblCmnEntity() {
		return this.tblCmnEntity;
	}

	public void setTblCmnEntity(TblCmnEntity tblCmnEntity) {
		this.tblCmnEntity = tblCmnEntity;
	}

	public String getChildrensName() {
		return childrensName;
	}

	public void setChildrensName(String childrensName) {
		this.childrensName = childrensName;
	}

	public Set getTblCmnUserMember() {
		return tblCmnUserMember;
	}

	public void setTblCmnUserMember(Set tblCmnUserMember) {
		this.tblCmnUserMember = tblCmnUserMember;
	}

	public String getCaptain() {
		// TODO Auto-generated method stub
		return getName();
	}

	public Object getData() {
		// TODO Auto-generated method stub
		return this;
	}

	public String getIdentity() {
		// TODO Auto-generated method stub
		return getId();
	}

	public String getImageType() {
		// TODO Auto-generated method stub
		return "2";
	}

	public boolean isCanbeSelected() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isHasChildren() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isLeaf() {
		// TODO Auto-generated method stub
		return false;
	}

}
