package net.silencily.sailing.framework.core;

import java.util.Collections;
import java.util.List;

import net.silencily.sailing.framework.persistent.BaseDto;

/**
 * ��ʾ��ǰʹ��ϵͳ���û���Ϣ, ʵ�ʵİ�ȫ��ʵ�ֵ�<code>adapter</code>
 * @author Scott Captain
 * @author java2enterprise
 * @since 2006-6-7
 * @version $Id: User.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 */
public class User extends BaseDto {
	
	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = -7000113272590177501L;
    
	/** ����ǰ���û���½ */
	public static final User EMPTY_USER = new User(null, null, null, null, Collections.EMPTY_LIST);
	
    /** �û���½ϵͳ���ʺ� */
    private String username;
    
    /** �û������� */
    private String chineseName;
    
    /** �û�������֯���� Id  */
    private String organizationId;
    
    /** �û�������֯�������� */
    private String organizationName;
    
    /** ��ǰ�û��Ľ�ɫ code ����, list fill with {@link String} */
    private List roles = Collections.EMPTY_LIST;
    
    /**
     * constructor using all fields
     */
    public User(String username, String chineseName, String organizationId, String organizationName, List roles) {
		this.username = username;
		this.chineseName = chineseName;
		this.organizationId = organizationId;
		this.organizationName = organizationName;
		this.roles = roles;
	}

	public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List getRoles() {
        if (roles == null) {
            return Collections.EMPTY_LIST;
        }
        return Collections.unmodifiableList(roles);
    }
    
    /**
     * ��ǰ�û��Ƿ����ĳ����ɫ
     * @param role ��ɫ����
     * @return �Ƿ����
     */
    public boolean containsRole(String role) {
    	return getRoles().contains(role);
    }

    public void setRoles(List roles) {
        this.roles = roles;
    }

	/**
	 * @return the chineseName
	 */
	public String getChineseName() {
		return chineseName;
	}

	/**
	 * @param chineseName the chineseName to set
	 */
	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	/**
	 * @return the organizationId
	 */
	public String getOrganizationId() {
		return organizationId;
	}

	/**
	 * @param organizationId the organizationId to set
	 */
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	/**
	 * @return the organizationName
	 */
	public String getOrganizationName() {
		return organizationName;
	}

	/**
	 * @param organizationName the organizationName to set
	 */
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	
	
}
