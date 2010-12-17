package net.silencily.sailing.basic.uf.schedule.service;

import java.util.List;

import net.silencily.sailing.basic.uf.domain.TblUfSchedule;
import net.silencily.sailing.common.message.domain.TblUfMessage;
import net.silencily.sailing.framework.core.ServiceBase;
import net.silencily.sailing.framework.web.view.ComboSupportList;


/**
 * @author zhangwenqi
 *
 */
public interface ScheduleService extends ServiceBase {
	public static String SERVICE_NAME = "uf.ScheduleService";
	
	/**
	 * 
	 * 功能描述 根据当前时间或查询条件取得列表
	 * @param 当前时间currentDay
	 * @return list
	 * 2007-12-26 下午02:40:55
	 * @version 1.0
	 * @author yuqian
	 */
	List list(String currentDay);
	
	
	TblUfSchedule load(String oid);
	
	
    void save(Object obj);


    /**
     * 
     * 功能描述 得到当前日期的字符串
     * @return
     * 2007-12-29 下午03:43:24
     * @version 1.0
     * @author yuqian
     */
	String getCurrentDay();


	/**
	 * 
	 * 功能描述 得到当前月的备忘录列表
	 * @param currentDay
	 * @return
	 * 2007-12-29 下午04:02:09
	 * @version 1.0
	 * @author yuqian
	 */
	List getMemo(String currentDay);

	/**
	 * 
	 * 功能描述 得到提醒时间列表
	 * @return
	 * 2008-1-3 下午04:21:35
	 * @version 1.0
	 * @author yuqian
	 */
	ComboSupportList getAlseList();

	/**
	 * 
	 * 功能描述 得到时间间隔列表
	 * @return
	 * 2008-1-3 下午04:21:38
	 * @version 1.0
	 * @author yuqian
	 */
	ComboSupportList getReptList();

	/**
	 * 
	 * 功能描述 插入发送消息
	 * @param bean
	 * 2008-1-7 上午09:51:17
	 * @version 1.0
	 * @author yuqian
	 * @throws Exception 
	 */
	TblUfMessage saveMseeage(TblUfSchedule bean) throws Exception;
	
	/**
	 * 
	 * 功能描述 修改发送消息
	 * @param bean
	 * 2008-1-7 下午02:24:39
	 * @version 1.0
	 * @author yuqian
	 */
	void updataMessage(TblUfSchedule bean);
	/**
	 * 返回从今天开始的所有List
	 * @param currentDay
	 * @return
	 */
	List listNowAfter(String currentDay);
}