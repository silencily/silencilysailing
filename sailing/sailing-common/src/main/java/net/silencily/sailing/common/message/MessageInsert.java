package net.silencily.sailing.common.message;

import java.util.Date;
import java.util.List;

import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.common.message.domain.TblUfMessage;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.utils.Tools;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class MessageInsert  {
	public static final String SYSTEM_USER = "systemUser";
	
	public static TblUfMessage createMessage(MessageBean messageBean) throws Exception {
		//将消息创建日期，发送计数，赋值
		
        Date date = net.silencily.sailing.common.dbtime.DBTime.getDBTime();
        
        messageBean.setCreateDate(date);
        //发送计数，赋值为0
        messageBean.setSendCount(0);
        //消息状态，赋值为0
        messageBean.setMessageStatus("0");
        
        Date endDate = net.silencily.sailing.common.dbtime.DBTime.getDBTime();
        endDate.setYear(endDate.getYear()+1);
        
        //映射，赋值
        TblUfMessage tblUfMessage = new TblUfMessage();
    	if (StringUtils.isBlank(messageBean.getSendUserId())) {
    		tblUfMessage.setSendUserid(SYSTEM_USER);
    	} else {
    		tblUfMessage.setSendUserid(messageBean.getSendUserId());
    	}
        tblUfMessage.setReceiverUserid(messageBean.getReceiveUserId());
        tblUfMessage.setTitle(messageBean.getTitle());
        tblUfMessage.setMessageContent(messageBean.getMessageContent());
        tblUfMessage.setMessageStatus(messageBean.getMessageStatus());
        tblUfMessage.setCreateDate(messageBean.getCreateDate());
        tblUfMessage.setUrl(messageBean.getUrl());
        //如果预定发送开始时间不填写，则赋值为系统当前时间
        if ( messageBean.getScheduleDate() != null) {
        	tblUfMessage.setScheduleDate(messageBean.getScheduleDate());
        } else {
        	tblUfMessage.setScheduleDate(messageBean.getCreateDate());
        }
        //如果发送结束时间不填写，则赋值为系统当前时间加一年
        if ( messageBean.getEndDate() != null) {
        	tblUfMessage.setEndDate(messageBean.getEndDate());
        } else {
        	tblUfMessage.setEndDate(endDate);
        }
        tblUfMessage.setSendDate(messageBean.getSendDate());
        //如果发送次数不填写，则赋值为1
        if ( messageBean.getSendTime() != 0) {
        	tblUfMessage.setSendTime(messageBean.getSendTime());
        } else {
        	tblUfMessage.setSendTime(1);
        }
        tblUfMessage.setSendCount(messageBean.getSendCount());
        tblUfMessage.setIntervalTime(messageBean.getIntervalTime());
        
        //消息ID赋值
        if (StringUtils.isBlank(messageBean.getMessageId())) {
        	tblUfMessage.setMessageId(Tools.getPKCode());
        } else {
        	tblUfMessage.setMessageId(messageBean.getMessageId());
        }
        //共通字段赋值
        tblUfMessage.setDelFlg("0");
        tblUfMessage.setVersion(Integer.valueOf("0"));
        
        //插入到数据库
        return insertData(tblUfMessage);
	}
	/**
	 * 功能描述:插入到数据库
	 * @param args
	 * 2007-11-29 上午10:02:49
	 * @version 1.0
	 * @author zhangwq
	 */
	private static TblUfMessage insertData(TblUfMessage tblUfMessage) {
		getService().saveOrUpdate(tblUfMessage);
		return tblUfMessage;
	}

    /**
     * 功能描述:更新到数据库
     * @param args
     * 2007-11-29 上午10:02:49
     * @version 1.0
     * @author zhangwq
     */
    public static TblUfMessage modifyMessage(TblUfMessage tblUfMessage) {
    	getService().update(tblUfMessage);
        return tblUfMessage;
    }

    /**
     * 功能描述:读取数据
     * @param args
     * 2007-11-29 上午10:02:49
     * @version 1.0
     * @author zhangwq
     */
    public static TblUfMessage loadMessage(String id) {
    	return (TblUfMessage)getService().load(TblUfMessage.class, id);
    }

    /**
     * 功能描述:删除数据
     * @param args
     * 2007-11-29 上午10:02:49
     * @version 1.0
     * @author zhangwq
     */
    public static TblUfMessage delete(String id) {
    	TblUfMessage tblUfMessage;
    	
    	tblUfMessage = loadMessage(id);
    	if(tblUfMessage != null){
    		getService().delete(tblUfMessage);
    	}
        return tblUfMessage;
    }

    /**
     * 功能描述:删除数据
     * @param args
     * 2007-11-29 上午10:02:49
     * @version 1.0
     * @author zhangwq
     */
    public static TblUfMessage delete(TblUfMessage tblUfMessage) {
    	getService().delete(tblUfMessage);
        return tblUfMessage;
    }

    /**
     * 功能描述:关闭消息
     * @param args
     * 2007-11-29 上午10:02:49
     * @version 1.0
     * @author zhangwq
     */
    public static void endMessage(TblUfMessage tblUfMessage) {
    	tblUfMessage.setMessageStatus("1");
    	getService().update(tblUfMessage);
    }

    /**
     * 功能描述:关闭消息
     * @param args
     * 2007-11-29 上午10:02:49
     * @version 1.0
     * @author zhangwq
     */
    public static void endMessage(String messageId) {
        ContextInfo.concealQuery();
        DetachedCriteria dc = DetachedCriteria.forClass(TblUfMessage.class)
        .add(Restrictions.eq("messageId", messageId))
        .add(Restrictions.eq("messageStatus", "0"));
        List list = getService().findByCriteria(dc);
        for (int i = 0; i < list.size(); i++) {
        	endMessage((TblUfMessage)list.get(i));
        }
    }

    /**
     * 功能描述:关闭消息
     * @param args
     * 2007-11-29 上午10:02:49
     * @version 1.0
     * @author zhangwq
     */
    public static void endMessage(String messageId,String receiverUserid) {
        ContextInfo.concealQuery();
        DetachedCriteria dc = DetachedCriteria.forClass(TblUfMessage.class)
        .add(Restrictions.eq("messageId", messageId))
        .add(Restrictions.eq("receiverUserid", receiverUserid))
        .add(Restrictions.eq("messageStatus", "0"));
        List list = getService().findByCriteria(dc);
        for (int i = 0; i < list.size(); i++) {
        	endMessage((TblUfMessage)list.get(i));
        }
    }

    /**
     * 共通Service调用接口
     */
    private static CommonService getService() {
		return (CommonService) ServiceProvider
				.getService(CommonService.SERVICE_NAME);
	}
}