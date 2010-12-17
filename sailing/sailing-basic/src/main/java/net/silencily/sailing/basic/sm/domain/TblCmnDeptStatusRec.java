package net.silencily.sailing.basic.sm.domain;

import java.util.Date;

import net.silencily.sailing.hibernate3.EntityPlus;

public class TblCmnDeptStatusRec extends EntityPlus implements
		java.io.Serializable {

	// Fields

	private TblCmnDept tblCmnDept;

	private String beginState;

	private String endState;

	private String operator;

	private Date operateTime;

	private Date changeTime;

	private String remark;

	private String field1;

	private String field2;

	private String field3;

	private String field4;

	private String field5;

	// Constructors

	/** default constructor */
	public TblCmnDeptStatusRec() {
	}

	/**
	 * @return the beginState
	 */
	public String getBeginState() {
		return beginState;
	}

	/**
	 * @param beginState
	 *            the beginState to set
	 */
	public void setBeginState(String beginState) {
		this.beginState = beginState;
	}

	/**
	 * @return the changeTime
	 */
	public Date getChangeTime() {
		return changeTime;
	}

	/**
	 * @param changeTime
	 *            the changeTime to set
	 */
	public void setChangeTime(Date changeTime) {
		this.changeTime = changeTime;
	}

	/**
	 * @return the endState
	 */
	public String getEndState() {
		return endState;
	}

	/**
	 * @param endState
	 *            the endState to set
	 */
	public void setEndState(String endState) {
		this.endState = endState;
	}

	/**
	 * @return the field1
	 */
	public String getField1() {
		return field1;
	}

	/**
	 * @param field1
	 *            the field1 to set
	 */
	public void setField1(String field1) {
		this.field1 = field1;
	}

	/**
	 * @return the field2
	 */
	public String getField2() {
		return field2;
	}

	/**
	 * @param field2
	 *            the field2 to set
	 */
	public void setField2(String field2) {
		this.field2 = field2;
	}

	/**
	 * @return the field3
	 */
	public String getField3() {
		return field3;
	}

	/**
	 * @param field3
	 *            the field3 to set
	 */
	public void setField3(String field3) {
		this.field3 = field3;
	}

	/**
	 * @return the field4
	 */
	public String getField4() {
		return field4;
	}

	/**
	 * @param field4
	 *            the field4 to set
	 */
	public void setField4(String field4) {
		this.field4 = field4;
	}

	/**
	 * @return the field5
	 */
	public String getField5() {
		return field5;
	}

	/**
	 * @param field5
	 *            the field5 to set
	 */
	public void setField5(String field5) {
		this.field5 = field5;
	}

	/**
	 * @return the operateTime
	 */
	public Date getOperateTime() {
		return operateTime;
	}

	/**
	 * @param operateTime
	 *            the operateTime to set
	 */
	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	/**
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @param operator
	 *            the operator to set
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark
	 *            the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the TblCmnDept
	 */
	public TblCmnDept getTblCmnDept() {
		return tblCmnDept;
	}

	/**
	 * @param tblHrDept
	 *            the tblHrDept to set
	 */
	public void setTblCmnDept(TblCmnDept tblCmnDept) {
		this.tblCmnDept = tblCmnDept;
	}

}