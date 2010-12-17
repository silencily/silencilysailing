package net.silencily.sailing.basic.sm.domain;

import net.silencily.sailing.hibernate3.EntityPlus;


public class TblCmnUserMember extends EntityPlus implements java.io.Serializable {
	
	private String searchScope;
	
	private String updateScope;
	
	private String createScope;
	
	private String deleteScope;
	
	private TblCmnUser tblCmnUser;
	
	private TblCmnEntityMember tblCmnEntityMember;
	
	public TblCmnUserMember()  {
		
	}

	public String getCreateScope() {
		return createScope;
	}

	public void setCreateScope(String createScope) {
		this.createScope = createScope;
	}

	public String getDeleteScope() {
		return deleteScope;
	}

	public void setDeleteScope(String deleteScope) {
		this.deleteScope = deleteScope;
	}

	public String getSearchScope() {
		return searchScope;
	}

	public void setSearchScope(String searchScope) {
		this.searchScope = searchScope;
	}

	public String getUpdateScope() {
		return updateScope;
	}

	public void setUpdateScope(String updateScope) {
		this.updateScope = updateScope;
	}

	public TblCmnEntityMember getTblCmnEntityMember() {
		return tblCmnEntityMember;
	}

	public void setTblCmnEntityMember(TblCmnEntityMember tblCmnEntityMember) {
		this.tblCmnEntityMember = tblCmnEntityMember;
	}

	public TblCmnUser getTblCmnUser() {
		return tblCmnUser;
	}

	public void setTblCmnUser(TblCmnUser tblCmnUser) {
		this.tblCmnUser = tblCmnUser;
	}

}
