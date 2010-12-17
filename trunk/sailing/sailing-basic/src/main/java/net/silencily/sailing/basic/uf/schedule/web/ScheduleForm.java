package net.silencily.sailing.basic.uf.schedule.web;

import java.util.ArrayList;
import java.util.List;

import net.silencily.sailing.basic.uf.domain.TblUfSchedule;
import net.silencily.sailing.basic.uf.schedule.service.ScheduleService;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.framework.web.view.ComboSupportList;
import net.silencily.sailing.struts.BaseFormPlus;

import org.apache.commons.lang.StringUtils;

/**
 * @author zhangwenqi
 *
 */
public class ScheduleForm extends BaseFormPlus {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9188966848380489L;

	/**
	 * �������ճ̼�¼bean
	 * ��������bean
	 * �������ͣ�TblUfSchedule
	 */
	private TblUfSchedule bean;
	
	/**
	 * ��������ǰ������currentDay
	 * ��������currentDay
	 * �������ͣ�String
	 */
	private String currentDay;

	/**
	 * ����������¼�б�memo
	 * ��������memo
	 * �������ͣ�List
	 */
	private List memo = new ArrayList();
	
	/**
	 * ����������ʱ��alsetList
	 * ��������alsetList
	 * �������ͣ�ComboSupportList
	 */
	private ComboSupportList alsetList;
	
	/**
	 * ������ʱ����reptList
	 * ��������reptList
	 * �������ͣ�ComboSupportList
	 */
	private ComboSupportList reptList;
	
	public ComboSupportList getAlsetList() {
		if(alsetList == null)
		{
			alsetList = service().getAlseList();
		}
		return alsetList;
	}

	public void setAlsetList(ComboSupportList alsetList) {
		this.alsetList = alsetList;
	}

	public ComboSupportList getReptList() {
		if(reptList == null)
		{
			reptList = service().getReptList();
		}
		return reptList;
	}

	public void setReptList(ComboSupportList reptList) {
		this.reptList = reptList;
	}

	public String getCurrentDay() {
		return currentDay;
	}

	public void setCurrentDay(String currentDay) {
		this.currentDay = currentDay;
	}

	/**
	 * 
	 * �������� �ճ̼�¼bean��get����
	 * @return
	 * 2007-12-27 ����09:44:53
	 * @version 1.0
	 * @author yuqian
	 */
	public TblUfSchedule getBean() {
		if (null == bean) {
			if(StringUtils.isBlank(getOid())){
				bean=new TblUfSchedule();
			}else{
				bean = (TblUfSchedule)getService().load(TblUfSchedule.class, getOid());
			}
		}
		return bean;
	}

	/**
	 * 
	 * �������� �ճ̼�¼bean��set����
	 * @param bean
	 * 2007-12-27 ����09:45:41
	 * @version 1.0
	 * @author yuqian
	 */
	public void setBean(TblUfSchedule bean) {
		this.bean = bean;
	}
	
	/**
	 * ��ȡ����
	 * @return
	 */
	public ScheduleService service(){
		return (ScheduleService)ServiceProvider.getService(ScheduleService.SERVICE_NAME);
	}
	
	/**
	 * ��ȡ��ͨ����
	 * @return
	 */
	private CommonService getService() {
		return (CommonService)ServiceProvider.getService(CommonService.SERVICE_NAME);
	}

	/**
	 * 
	 * �������� ����¼�б��get����
	 * @return
	 * 2007-12-29 ����03:57:20
	 * @version 1.0
	 * @author yuqian
	 */
	public List getMemo() {
		return memo;
	}

	/**
	 * 
	 * �������� ����¼�б��set����
	 * @param memo
	 * 2007-12-29 ����03:57:23
	 * @version 1.0
	 * @author yuqian
	 */
	public void setMemo(List memo) {
		this.memo = memo;
	}

}
