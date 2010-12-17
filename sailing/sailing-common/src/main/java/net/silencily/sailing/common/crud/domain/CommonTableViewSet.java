package net.silencily.sailing.common.crud.domain;

import net.silencily.sailing.hibernate3.EntityPlus;

/**
 * CommonTableViewSet generated by MyEclipse Persistence Tools
 */

public class CommonTableViewSet extends EntityPlus implements java.io.Serializable {

	// Fields

	private String id;

	private CommonTableView commonTableView;

	private String userId;

	private Integer orderNum;
	private String orderAsc;
	// Constructors

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	/** default constructor */
	public CommonTableViewSet() {
	}

	/** minimal constructor */
	public CommonTableViewSet(String id) {
		this.id = id;
	}

	/** full constructor */
	public CommonTableViewSet(String id, CommonTableView commonTableView,
			String userId) {
		this.id = id;
		this.commonTableView = commonTableView;
		this.userId = userId;
	}

	// Property accessors

	public CommonTableView getCommonTableView() {
		return this.commonTableView;
	}

	public void setCommonTableView(CommonTableView commonTableView) {
		this.commonTableView = commonTableView;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	
	public String getOrderAsc() {
		return orderAsc;
	}

	public void setOrderAsc(String orderAsc) {
		this.orderAsc = orderAsc;
	}

}