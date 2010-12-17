package net.silencily.sailing.basic.sm.workflow.web;

import java.util.List;

import net.silencily.sailing.basic.wf.constant.WorkflowStatus;
import net.silencily.sailing.basic.wf.dto.WfSearch;
import net.silencily.sailing.framework.web.view.ComboSupportList;
import net.silencily.sailing.struts.BaseFormPlus;


/**
 * 
 * @author wenjb
 * @version 1.0
 */
public class WFManageForm extends BaseFormPlus {
	
	/**
	 * ������
	 * ��������serialVersionUID
	 * �������ͣ�long
	 */
	private static final long serialVersionUID = 7382181830839466639L;
	// �������б�
	private List allWfList;
	// ��Ź�����״̬��Ϣ
	private ComboSupportList comboSupportList = null;

	// ��ѯ����
	private WfSearch wfSearch = null;


	public List getAllWfList() {
		return allWfList;
	}

	public void setAllWfList(List allWfList) {
		this.allWfList = allWfList;
	}

	public ComboSupportList getComboSupportList() {
		if (null == comboSupportList) {
			comboSupportList = new ComboSupportList(WorkflowStatus.WF_SM_STATUS);
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


}
