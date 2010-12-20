package net.silencily.sailing.common.crud.domain;

import java.util.HashSet;
import java.util.Set;

import net.silencily.sailing.hibernate3.EntityPlus;

/**
 * CommonTableView generated by MyEclipse Persistence Tools
 */

public class CommonTableView extends EntityPlus implements java.io.Serializable {

	// Fields

	private String id;

	private String tableName;

	private String rowName;

	private String rowDisplayname;

	private Integer orderby;

	private String remark;

	private String subSystem;

	private String module;

	private String popLink;

	private String style;

	private Set commonTableViewSets = new HashSet(0);

	private String sortNum;

	private String orderAsc;

	private String isDbField;

	// Constructors

	public String getIsDbField() {
		return isDbField;
	}

	public void setIsDbField(String isDbField) {
		this.isDbField = isDbField;
	}

	/** default constructor */
	public CommonTableView() {
	}

	/** minimal constructor */
	public CommonTableView(String id) {
		this.id = id;
	}

	// Property accessors

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getRowName() {
		return this.rowName;
	}

	public void setRowName(String rowName) {
		this.rowName = rowName;
	}

	public String getRowDisplayname() {
		return this.rowDisplayname;
	}

	public void setRowDisplayname(String rowDisplayname) {
		this.rowDisplayname = rowDisplayname;
	}

	public Integer getOrderby() {
		return orderby;
	}

	public void setOrderby(Integer orderby) {
		this.orderby = orderby;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSubSystem() {
		return this.subSystem;
	}

	public void setSubSystem(String subSystem) {
		this.subSystem = subSystem;
	}

	public String getModule() {
		return this.module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public Set getCommonTableViewSets() {
		return this.commonTableViewSets;
	}

	public void setCommonTableViewSets(Set commonTableViewSets) {
		this.commonTableViewSets = commonTableViewSets;
	}

	public String getPopLink() {
		return popLink;
	}

	public void setPopLink(String popLink) {
		this.popLink = popLink;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getOrderAsc() {
		return orderAsc;
	}

	public void setOrderAsc(String orderAsc) {
		this.orderAsc = orderAsc;
	}

	public String getSortNum() {
		return sortNum;
	}

	public void setSortNum(String sortNum) {
		this.sortNum = sortNum;
	}

}