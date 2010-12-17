package net.silencily.sailing.security.entity;

import java.io.Serializable;
import java.util.Date;
/**
 * @author qian
 * @hibernate.class table="security_user_transaction"
 */
public class UserTransaction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8570040065570472625L;

	private String userId;

	private Date operateDate;

	private String userIp;

	private String requestUrl;

	private String operateName;

	private String id;
	
	private Integer version;
	/**
	 * @hibernate.id unsaved-value="null" generator-class="uuid.hex" length = "32"
	 */
	public String getId() {
		return id;
	}

	public void setId(String property1) {
		this.id = property1;
	}
	/**
	 * @hibernate.property type = "com.coheg.framework.hibernate3.CStringType" length = "100"
	 */
	public String getOperateName() {
		return operateName;
	}

	public void setOperateName(String property1) {
		this.operateName = property1;
	}
	/**
	 * @hibernate.property type = "com.coheg.framework.hibernate3.CStringType" length = "200"
	 */
	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String property1) {
		this.requestUrl = property1;
	}
	/**
	 * @hibernate.property type = "com.coheg.framework.hibernate3.CStringType" length = "100"
	 */
	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String property1) {
		this.userIp = property1;
	}
	/**
	 * @hibernate.property type = "date" 
	 */
	public Date getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(Date property1) {
		this.operateDate = property1;
	}
	/**
	 * @hibernate.property type = "com.coheg.framework.hibernate3.CStringType" length = "32"
	 */
	public String getUserId() {
		return userId;
	}

	public void setUserId(String property1) {
		this.userId = property1;
	}
	/**
	 * @hibernate.version unsaved-value="null"
	 */
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}
