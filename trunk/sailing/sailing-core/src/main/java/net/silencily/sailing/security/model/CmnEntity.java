package net.silencily.sailing.security.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * ҵ��ʵ��
 *
 */
public class CmnEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/** ʵ������ */
	private String entityClassName;
	
	private Set<CmnEntityMember> entityMembers = new HashSet<CmnEntityMember>(0);

	public String getEntityClassName() {
		return entityClassName;
	}

	public void setEntityClassName(String entityClassName) {
		this.entityClassName = entityClassName;
	}

	public Set<CmnEntityMember> getEntityMembers() {
		return entityMembers;
	}

	public void setEntityMembers(Set<CmnEntityMember> entityMembers) {
		this.entityMembers = entityMembers;
	}	
	
}
