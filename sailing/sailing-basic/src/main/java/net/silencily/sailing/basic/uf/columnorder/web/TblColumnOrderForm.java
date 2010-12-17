package net.silencily.sailing.basic.uf.columnorder.web;

import java.util.ArrayList;
import java.util.List;

import net.silencily.sailing.basic.uf.domain.TblUfColumn;
import net.silencily.sailing.basic.uf.domain.TblUfColumnOrder;
import net.silencily.sailing.struts.BaseFormPlus;

import org.apache.log4j.Logger;


public class TblColumnOrderForm extends BaseFormPlus {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(TblColumnOrderForm.class);

     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List listDesktop;
	private String displayNo;
	private String loginCd;
	private List desk = new ArrayList();
	private List parentid = new ArrayList(); 

	 

	/**
	 * @return parentid
	 */
	public List getParentid() {
		return parentid;
	}

	/**
	 * @param parentid 要设置的 parentid
	 */
	public void setParentid(List parentid) {
		this.parentid = parentid;
	}

	public TblUfColumn getParentid(int i) {
		TblUfColumn pid = (TblUfColumn)parentid.get(i);
		if (pid == null) {
			pid = new TblUfColumn();
			desk.add(i, pid);

		}
		return pid;
	}
	/**
	 * @return desk
	 */
	public List getDesk() {
		return desk;
	}

	/**
	 * @param desk 要设置的 desk
	 */
	public void setDesk(List desk) {
		this.desk = desk;
	}

	/**
	 * @return listDesktop
	 */
	public List getListDesktop() {
		return listDesktop;
	}

	public TblUfColumnOrder getDesk(int i) {
		TblUfColumnOrder tcdt = (TblUfColumnOrder) desk.get(i);
		if (tcdt == null) {
			tcdt = new TblUfColumnOrder();
			desk.add(i, tcdt);

		}

		logger.info("getDesk(" + i + ") - TblUfColumnOrder ["+ tcdt.getId() +"] tcdt.getDisplaySort=" + tcdt.getDisplaySort());
		
		return tcdt;
	}
	
	/**
	 * @param listDesktop 要设置的 listDesktop
	 */
	public void setListDesktop(List listDesktop) {
		this.listDesktop = listDesktop;
	}

	/**
	 * @return displayNo
	 */
	public String getDisplayNo() {
		return displayNo;
	}

	/**
	 * @param displayNo 要设置的 displayNo
	 */
	public void setDisplayNo(String sortNo) {
		this.displayNo = sortNo;
	}

	public String getLoginCd() {
		return loginCd;
	}

	public void setLoginCd(String loginCd) {
		this.loginCd = loginCd;
	}

	 
}
