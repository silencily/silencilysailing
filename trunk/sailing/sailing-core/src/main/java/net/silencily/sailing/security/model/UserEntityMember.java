package net.silencily.sailing.security.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.silencily.sailing.framework.persistent.filter.ConditionConstants;

import org.apache.commons.lang.StringUtils;


public class UserEntityMember implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String searchScope;
	
	private String updateScope;
	
	private String createScope;
	
	private String deleteScope;
	
	private Set<String> searchIds = new HashSet<String>(0);
	private Set<String> updateIds = new HashSet<String>(0);
	private Set<String> createIds = new HashSet<String>(0);
	private Set<String> deleteIds = new HashSet<String>(0);
	
	public String getOper(char type) {
		String scope = null;
		switch (type) {
			case 'S':scope = getSearchScope();break;
			case 'U':scope = getUpdateScope();break;
			case 'C':scope = getCreateScope();break;
			case 'D':scope = getDeleteScope();break;
			default:break;
		}
		return getOper(scope);
	}
	private String getOper(String scope) {
		if (scope != null) {
			String regex = ">=?|<=?|!?=";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(scope);
			while (m.find()) {
				if (!"".equals(m.group())) {
					return m.group();
				}
			}
		}
		return ConditionConstants.DEFAULT_OPERATOR;
	}
	public String getValue(char type) {
		String scope = null;
		switch (type) {
			case 'S':scope = getSearchScope();break;
			case 'U':scope = getUpdateScope();break;
			case 'C':scope = getCreateScope();break;
			case 'D':scope = getDeleteScope();break;
			default:break;
		}
		return getValue(scope);
	}
	public String getValue(String scope) {
		if (scope != null) {
			return scope.replace(getOper(scope), "").trim();
		} else {
			return "";
		}		
	}
	public Set<String> getSubIds(char type) {
		switch (type) {
			case 'S':return getSearchIds();
			case 'U':return getUpdateIds();
			case 'C':return getCreateIds();
			case 'D':return getDeleteIds();
		default:return new HashSet<String>(0);
	}
	}
	public String getSearchScope() {
		return searchScope;
	}
	public void setSearchScope(String searchScope) {
		this.searchScope = searchScope;
	}
	public String getUpdateScope() {
        if (StringUtils.isBlank(updateScope)) {
            return searchScope;
        } else {
            return updateScope;
        }
	}
	public void setUpdateScope(String updateScope) {
		this.updateScope = updateScope;
	}
	public String getCreateScope() {
	    if (StringUtils.isBlank(createScope)) {
	        return searchScope;
	    } else {
	        return createScope;
	    }
	}
	public void setCreateScope(String createScope) {
		this.createScope = createScope;
	}
	public String getDeleteScope() {
        if (StringUtils.isBlank(deleteScope)) {
            return searchScope;
        } else {
            return deleteScope;
        }
	}
	public void setDeleteScope(String deleteScope) {
		this.deleteScope = deleteScope;
	}
	public Set<String> getSearchIds() {
		return searchIds;
	}
	public void setSearchIds(Set<String> searchIds) {
		this.searchIds = searchIds;
	}
	public Set<String> getUpdateIds() {
		return updateIds;
	}
	public void setUpdateIds(Set<String> updateIds) {
		this.updateIds = updateIds;
	}
	public Set<String> getCreateIds() {
		return createIds;
	}
	public void setCreateIds(Set<String> createIds) {
		this.createIds = createIds;
	}
	public Set<String> getDeleteIds() {
		return deleteIds;
	}
	public void setDeleteIds(Set<String> deleteIds) {
		this.deleteIds = deleteIds;
	}

}
