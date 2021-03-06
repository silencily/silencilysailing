package net.silencily.sailing.basic.wf.domain;

import java.util.HashSet;
import java.util.Set;

import net.silencily.sailing.hibernate3.EntityPlus;

/**
 * TblWfParticularInfo generated by MyEclipse Persistence Tools
 */

public class TblWfParticularInfo extends EntityPlus
		implements java.io.Serializable {

	// Fields

	private TblWfOperationInfo tblWfOperationInfo;

	private Long stepId;

	private String stepName;

	private String dealInfo;

	private String goback;

	private String gobackStep;

	private String cancel;

	private String suspend;

	private String togethersign;

	private String helpman;

	private String fetch;

	private String passround;

	private String commit;

	private String commitStep;

	private String tblUserId;

	private String message;

	private String fieldStatus;

	private Set tblWfFormInfos = new HashSet(0);

	private String pointStep;

	private String stepChecks;

	// Constructors

	public String getStepChecks() {
		return stepChecks;
	}

	public void setStepChecks(String stepChecks) {
		this.stepChecks = stepChecks;
	}

	// Property accessors

	public TblWfOperationInfo getTblWfOperationInfo() {
		return this.tblWfOperationInfo;
	}

	public void setTblWfOperationInfo(TblWfOperationInfo tblWfOperationInfo) {
		this.tblWfOperationInfo = tblWfOperationInfo;
	}

	public Long getStepId() {
		return this.stepId;
	}

	public void setStepId(Long stepId) {
		this.stepId = stepId;
	}

	public String getStepName() {
		return this.stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public String getDealInfo() {
		return this.dealInfo;
	}

	public void setDealInfo(String dealInfo) {
		this.dealInfo = dealInfo;
	}

	public String getGoback() {
		return this.goback;
	}

	public void setGoback(String goback) {
		this.goback = goback;
	}

	public String getGobackStep() {
		return this.gobackStep;
	}

	public void setGobackStep(String gobackStep) {
		this.gobackStep = gobackStep;
	}

	public String getCancel() {
		return this.cancel;
	}

	public void setCancel(String cancel) {
		this.cancel = cancel;
	}

	public String getSuspend() {
		return this.suspend;
	}

	public void setSuspend(String suspend) {
		this.suspend = suspend;
	}

	public String getTogethersign() {
		return this.togethersign;
	}

	public void setTogethersign(String togethersign) {
		this.togethersign = togethersign;
	}

	public String getHelpman() {
		return this.helpman;
	}

	public void setHelpman(String helpman) {
		this.helpman = helpman;
	}

	public String getFetch() {
		return this.fetch;
	}

	public void setFetch(String fetch) {
		this.fetch = fetch;
	}

	public String getPassround() {
		return this.passround;
	}

	public void setPassround(String passround) {
		this.passround = passround;
	}

	public String getCommit() {
		return this.commit;
	}

	public void setCommit(String commit) {
		this.commit = commit;
	}

	public String getCommitStep() {
		return this.commitStep;
	}

	public void setCommitStep(String commitStep) {
		this.commitStep = commitStep;
	}

	public String getTblUserId() {
		return this.tblUserId;
	}

	public void setTblUserId(String tblUserId) {
		this.tblUserId = tblUserId;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFieldStatus() {
		return this.fieldStatus;
	}

	public void setFieldStatus(String fieldStatus) {
		this.fieldStatus = fieldStatus;
	}

	public Set getTblWfFormInfos() {
		return this.tblWfFormInfos;
	}

	public void setTblWfFormInfos(Set tblWfFormInfos) {
		this.tblWfFormInfos = tblWfFormInfos;
	}

	public String getAllRoleCd() {

		return tblUserId;

	}

	public String getPointStep() {
		return pointStep;
	}

	public void setPointStep(String pointStep) {
		this.pointStep = pointStep;
	}
}