package net.silencily.sailing.security.model;

import java.io.Serializable;

public class CmnEntityMember implements Serializable{

	private static final long serialVersionUID = 1L;
		
	private String type;
	
	private String name;
	
	private String childrenNode;
	
	private UserEntityMember userEntityMemberId;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getChildrenNode() {
		return childrenNode;
	}

	public void setChildrenNode(String childrenNode) {
		this.childrenNode = childrenNode;
	}

	public UserEntityMember getUserEntityMemberId() {
		return userEntityMemberId;
	}

	public void setUserEntityMemberId(UserEntityMember userEntityMemberId) {
		this.userEntityMemberId = userEntityMemberId;
	}

}
