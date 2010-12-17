package net.silencily.sailing.basic.uf.schedule.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.silencily.sailing.basic.uf.domain.TblUfSchedule;
import net.silencily.sailing.basic.uf.schedule.service.ScheduleService;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.common.dbtime.DBTime;
import net.silencily.sailing.common.message.MessageBean;
import net.silencily.sailing.common.message.MessageInsert;
import net.silencily.sailing.common.message.domain.TblUfMessage;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.framework.web.view.ComboSupportList;
import net.silencily.sailing.security.SecurityContextInfo;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;


/**
 * @author zhangwenqi
 *
 */
public class ScheduleServiceImpl implements ScheduleService {
	
	private HibernateTemplate hibernateTemplate;
    
	public List list(String currentDay) {
		DetachedCriteria dc = DetachedCriteria.forClass(TblUfSchedule.class);
		ContextInfo.recoverQuery();
		if(!StringUtils.isBlank(currentDay))
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
			Date begTime = DBTime.getDBTime();
			Date endTime = DBTime.getDBTime();
			try {
				begTime = sdf.parse(currentDay + " 00:00:00");
				endTime = sdf.parse(currentDay + " 23:59:59");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			dc.add(Restrictions.ge("begTime", begTime));
			dc.add(Restrictions.le("begTime", endTime));
		}
		dc.add(Restrictions.eq("delFlg", "0"));
		dc.add(Restrictions.eq("empCd", SecurityContextInfo.getCurrentUser().getEmpCd()));
		dc.addOrder(Order.asc("begTime"));
	    return this.hibernateTemplate.findByCriteria(dc); 
	}

	public TblUfSchedule load(String oid) {
		return (TblUfSchedule)this.hibernateTemplate.load(TblUfSchedule.class, oid);
	}

	public void save(Object obj) {
		this.hibernateTemplate.saveOrUpdate(obj);
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public String getCurrentDay() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(DBTime.getDBTime());
	}

	public List getMemo(String currentDay) {
		List memo = new ArrayList();
		List list = new ArrayList();
		String sDate = currentDay.substring(0,7);
		for(int i = 1; i <= 31; i++)
		{
			String sDay = String.valueOf(i);
			if(i<10)
			{
				sDay = "0" + sDay;
			}
			List tem = list(sDate + "-" + sDay);
			if(tem.size() <= 4)
			{
				list.addAll(tem);
			}else
			{
				for(int j = 0; j < 4; j++)
				{
					list.add(tem.get(j));
				}
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		TblUfSchedule ufSchedule = new TblUfSchedule();
		for(int i = 0; i < list.size(); i++)
		{
			ufSchedule = (TblUfSchedule)list.get(i);
			memo.add(sdf.format(ufSchedule.getBegTime()) + " " + ufSchedule.getTitle());
		}
		return memo;
	}

	public ComboSupportList getAlseList() {
		Map map = new LinkedHashMap();
		map.put("0", "不提醒");
		map.put("1", "一分钟");
		map.put("5", "五分钟");
		map.put("10", "十分钟");
		map.put("60", "一小时");
		ComboSupportList csl = new ComboSupportList(map);
		return csl;
	}

	public ComboSupportList getReptList() {
		Map map = new LinkedHashMap();
		map.put("5", "五分钟");
		map.put("10", "十分钟");
		map.put("60", "一小时");
		ComboSupportList csl = new ComboSupportList(map);
		return csl;
	}

	public TblUfMessage saveMseeage(TblUfSchedule bean) throws Exception {
		String intervalTime = "";
		MessageBean messageBean = new MessageBean();
		long time = bean.getBegTime().getTime() - Long.parseLong(bean.getAlertTime()) * 60000;
		messageBean.setScheduleDate(new Date(time));
		messageBean.setEndDate(bean.getEndTime());
		messageBean.setMessageContent(getGreetingWords(bean));
		messageBean.setTitle(bean.getTitle());
		messageBean.setSendUserId(SecurityContextInfo.getCurrentUser().getUserId());
		messageBean.setReceiveUserId(SecurityContextInfo.getCurrentUser().getUserId());
		
		//设置再次提醒的时间间隔
		intervalTime = bean.getReptAlert();
		intervalTime = (intervalTime == null)? "" : intervalTime.trim();
		if(!"".equals(intervalTime)){
			messageBean.setIntervalTime(Integer.parseInt(bean.getReptAlert()));
		}
		
		if(!StringUtils.isBlank(bean.getReptAlert()))
		{
			long alertTime = Long.parseLong(bean.getReptAlert()) * 60000;
			messageBean.setSendTime((int)((bean.getEndTime().getTime() - time) / alertTime));
		}
		return MessageInsert.createMessage(messageBean);
	}

	public void updataMessage(TblUfSchedule bean) {
		TblUfMessage tblUfMessage = (TblUfMessage)getService().load(TblUfMessage.class, bean.getMessageId());
		long time = bean.getBegTime().getTime() - Long.parseLong(bean.getAlertTime()) * 60000;
		tblUfMessage.setScheduleDate(new Date(time));
		tblUfMessage.setEndDate(bean.getEndTime());
		tblUfMessage.setMessageContent(getGreetingWords(bean));
		tblUfMessage.setTitle(bean.getTitle());
		tblUfMessage.setSendUserid(SecurityContextInfo.getCurrentUser().getUserId());
		tblUfMessage.setReceiverUserid(SecurityContextInfo.getCurrentUser().getUserId());
		if(StringUtils.isBlank(bean.getReptAlert()))
		{
			tblUfMessage.setSendTime(1);
		}else
		{
			long alertTime = Long.parseLong(bean.getReptAlert()) * 60000;
			tblUfMessage.setSendTime((int)((bean.getEndTime().getTime() - time) / alertTime));
		}
		getService().saveOrUpdate(tblUfMessage);
	} 
	
	public List listNowAfter(String currentDay) {
		DetachedCriteria dc = DetachedCriteria.forClass(TblUfSchedule.class);
		ContextInfo.recoverQuery();
		if (!StringUtils.isBlank(currentDay)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
			Date begTime = DBTime.getDBTime();
			try {
				begTime = sdf.parse(currentDay + " 00:00:00");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			dc.add(Restrictions.ge("begTime", begTime));
		}
		dc.add(Restrictions.eq("delFlg", "0"));
		dc.add(Restrictions.eq("empCd", SecurityContextInfo.getCurrentUser()
				.getEmpCd()));
		dc.addOrder(Order.asc("begTime"));
		return this.hibernateTemplate.findByCriteria(dc);
	}
	
    private String getGreetingWords(TblUfSchedule bean) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return "您好!您在" + sdf.format(bean.getEndTime()) + "有如下安排:\r\n" + bean.getContent();
    }
	private CommonService getService() {
		return (CommonService) ServiceProvider
				.getService(CommonService.SERVICE_NAME);
	}
}
