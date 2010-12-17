package net.silencily.sailing.basic.wf.search.web;

import java.util.List;

import net.silencily.sailing.basic.wf.constant.WorkflowStatus;
import net.silencily.sailing.basic.wf.dto.WfEntry;
import net.silencily.sailing.basic.wf.dto.WfSearch;
import net.silencily.sailing.framework.web.view.ComboSupportList;
import net.silencily.sailing.struts.BaseFormPlus;

public class PersonWfSearchForm extends BaseFormPlus {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8041257730802106529L;

	// 具体工作流实例信息
	private WfEntry wfEntry;

	// 待办工作流列表
	private List waitWfList;

	private List alreadyList;

	private List recieveList;

	private List entrustList;

	private List allList;
	
	private List passroundList;

	// 查询条件
	private WfSearch wfSearch = null;

	// 存放工作流状态信息
	private ComboSupportList comboSupportList = null;

	public List getWaitWfList() {
		return waitWfList;
	}

	public void setWaitWfList(List waitWfList) {
		this.waitWfList = waitWfList;
	}

	public WfEntry getWfEntry() {
		return wfEntry;
	}

	public void setWfEntry(WfEntry wfEntry) {
		this.wfEntry = wfEntry;
	}

	public List getAllList() {
		return allList;
	}

	public void setAllList(List allList) {
		this.allList = allList;
	}

	public List getAlreadyList() {
		return alreadyList;
	}

	public void setAlreadyList(List alreadyList) {
		this.alreadyList = alreadyList;
	}

	public List getEntrustList() {
		return entrustList;
	}

	public void setEntrustList(List entrustList) {
		this.entrustList = entrustList;
	}

	public List getRecieveList() {
		return recieveList;
	}

	public void setRecieveList(List recieveList) {
		this.recieveList = recieveList;
	}

	public ComboSupportList getComboSupportList() {
		if (null == comboSupportList) {
			comboSupportList = new ComboSupportList(WorkflowStatus.WF_SERARCH_STATUS);
		}
		return comboSupportList;
	}

	public void setComboSupportList(ComboSupportList comboSupportList) {
		this.comboSupportList = comboSupportList;
	}

	public WfSearch getWfSearch() {
		if (null == wfSearch) {
			wfSearch = new WfSearch();
		}
		return wfSearch;
	}

	public void setWfSearch(WfSearch wfSearch) {
		this.wfSearch = wfSearch;
	}

	public List getPassroundList() {
		return passroundList;
	}

	public void setPassroundList(List passroundList) {
		this.passroundList = passroundList;
	}

}
