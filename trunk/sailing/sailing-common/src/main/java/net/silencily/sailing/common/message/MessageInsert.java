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
		//����Ϣ�������ڣ����ͼ�������ֵ
		
        Date date = net.silencily.sailing.common.dbtime.DBTime.getDBTime();
        
        messageBean.setCreateDate(date);
        //���ͼ�������ֵΪ0
        messageBean.setSendCount(0);
        //��Ϣ״̬����ֵΪ0
        messageBean.setMessageStatus("0");
        
        Date endDate = net.silencily.sailing.common.dbtime.DBTime.getDBTime();
        endDate.setYear(endDate.getYear()+1);
        
        //ӳ�䣬��ֵ
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
        //���Ԥ�����Ϳ�ʼʱ�䲻��д����ֵΪϵͳ��ǰʱ��
        if ( messageBean.getScheduleDate() != null) {
        	tblUfMessage.setScheduleDate(messageBean.getScheduleDate());
        } else {
        	tblUfMessage.setScheduleDate(messageBean.getCreateDate());
        }
        //������ͽ���ʱ�䲻��д����ֵΪϵͳ��ǰʱ���һ��
        if ( messageBean.getEndDate() != null) {
        	tblUfMessage.setEndDate(messageBean.getEndDate());
        } else {
        	tblUfMessage.setEndDate(endDate);
        }
        tblUfMessage.setSendDate(messageBean.getSendDate());
        //������ʹ�������д����ֵΪ1
        if ( messageBean.getSendTime() != 0) {
        	tblUfMessage.setSendTime(messageBean.getSendTime());
        } else {
        	tblUfMessage.setSendTime(1);
        }
        tblUfMessage.setSendCount(messageBean.getSendCount());
        tblUfMessage.setIntervalTime(messageBean.getIntervalTime());
        
        //��ϢID��ֵ
        if (StringUtils.isBlank(messageBean.getMessageId())) {
        	tblUfMessage.setMessageId(Tools.getPKCode());
        } else {
        	tblUfMessage.setMessageId(messageBean.getMessageId());
        }
        //��ͨ�ֶθ�ֵ
        tblUfMessage.setDelFlg("0");
        tblUfMessage.setVersion(Integer.valueOf("0"));
        
        //���뵽���ݿ�
        return insertData(tblUfMessage);
	}
	/**
	 * ��������:���뵽���ݿ�
	 * @param args
	 * 2007-11-29 ����10:02:49
	 * @version 1.0
	 * @author zhangwq
	 */
	private static TblUfMessage insertData(TblUfMessage tblUfMessage) {
		getService().saveOrUpdate(tblUfMessage);
		return tblUfMessage;
	}

    /**
     * ��������:���µ����ݿ�
     * @param args
     * 2007-11-29 ����10:02:49
     * @version 1.0
     * @author zhangwq
     */
    public static TblUfMessage modifyMessage(TblUfMessage tblUfMessage) {
    	getService().update(tblUfMessage);
        return tblUfMessage;
    }

    /**
     * ��������:��ȡ����
     * @param args
     * 2007-11-29 ����10:02:49
     * @version 1.0
     * @author zhangwq
     */
    public static TblUfMessage loadMessage(String id) {
    	return (TblUfMessage)getService().load(TblUfMessage.class, id);
    }

    /**
     * ��������:ɾ������
     * @param args
     * 2007-11-29 ����10:02:49
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
     * ��������:ɾ������
     * @param args
     * 2007-11-29 ����10:02:49
     * @version 1.0
     * @author zhangwq
     */
    public static TblUfMessage delete(TblUfMessage tblUfMessage) {
    	getService().delete(tblUfMessage);
        return tblUfMessage;
    }

    /**
     * ��������:�ر���Ϣ
     * @param args
     * 2007-11-29 ����10:02:49
     * @version 1.0
     * @author zhangwq
     */
    public static void endMessage(TblUfMessage tblUfMessage) {
    	tblUfMessage.setMessageStatus("1");
    	getService().update(tblUfMessage);
    }

    /**
     * ��������:�ر���Ϣ
     * @param args
     * 2007-11-29 ����10:02:49
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
     * ��������:�ر���Ϣ
     * @param args
     * 2007-11-29 ����10:02:49
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
     * ��ͨService���ýӿ�
     */
    private static CommonService getService() {
		return (CommonService) ServiceProvider
				.getService(CommonService.SERVICE_NAME);
	}
}