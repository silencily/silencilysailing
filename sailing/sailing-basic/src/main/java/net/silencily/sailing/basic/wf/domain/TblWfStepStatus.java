/**
 * 
 */
package net.silencily.sailing.basic.wf.domain;

/**
 * @author yushn
 *
 */
public class TblWfStepStatus implements java.io.Serializable{
	private String id;
	private String beanId;
	private String stepId;
	private String nullFieldName;
	public String getBeanId() {
		return beanId;
	}
	public void setBeanId(String beanId) {
		this.beanId = beanId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNullFieldName() {
		return nullFieldName;
	}
	public void setNullFieldName(String nullFieldName) {
		this.nullFieldName = nullFieldName;
	}
	public String getStepId() {
		return stepId;
	}
	public void setStepId(String stepId) {
		this.stepId = stepId;
	}
	
}
