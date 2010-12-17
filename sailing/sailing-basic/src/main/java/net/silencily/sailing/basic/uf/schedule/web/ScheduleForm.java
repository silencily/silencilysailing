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
	 * 描述：日程记录bean
	 * 属性名：bean
	 * 属性类型：TblUfSchedule
	 */
	private TblUfSchedule bean;
	
	/**
	 * 描述：当前的日期currentDay
	 * 属性名：currentDay
	 * 属性类型：String
	 */
	private String currentDay;

	/**
	 * 描述：备忘录列表memo
	 * 属性名：memo
	 * 属性类型：List
	 */
	private List memo = new ArrayList();
	
	/**
	 * 描述：提醒时间alsetList
	 * 属性名：alsetList
	 * 属性类型：ComboSupportList
	 */
	private ComboSupportList alsetList;
	
	/**
	 * 描述：时间间隔reptList
	 * 属性名：reptList
	 * 属性类型：ComboSupportList
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
	 * 功能描述 日程记录bean的get方法
	 * @return
	 * 2007-12-27 上午09:44:53
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
	 * 功能描述 日程记录bean的set方法
	 * @param bean
	 * 2007-12-27 上午09:45:41
	 * @version 1.0
	 * @author yuqian
	 */
	public void setBean(TblUfSchedule bean) {
		this.bean = bean;
	}
	
	/**
	 * 获取服务
	 * @return
	 */
	public ScheduleService service(){
		return (ScheduleService)ServiceProvider.getService(ScheduleService.SERVICE_NAME);
	}
	
	/**
	 * 获取共通服务
	 * @return
	 */
	private CommonService getService() {
		return (CommonService)ServiceProvider.getService(CommonService.SERVICE_NAME);
	}

	/**
	 * 
	 * 功能描述 备忘录列表的get方法
	 * @return
	 * 2007-12-29 下午03:57:20
	 * @version 1.0
	 * @author yuqian
	 */
	public List getMemo() {
		return memo;
	}

	/**
	 * 
	 * 功能描述 备忘录列表的set方法
	 * @param memo
	 * 2007-12-29 下午03:57:23
	 * @version 1.0
	 * @author yuqian
	 */
	public void setMemo(List memo) {
		this.memo = memo;
	}

}
