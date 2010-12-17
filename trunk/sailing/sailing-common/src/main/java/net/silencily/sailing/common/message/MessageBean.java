package net.silencily.sailing.common.message;

import java.util.Date;

/**
 * @author zhangwq
 *
 */
public class MessageBean  {
	
	private String messageId;
	private String sendUserId;
	private String receiveUserId;
	private String title;
	private String messageContent;
	private String url;
	private String messageStatus;
	//  ��Ϣ����ʱ��,�浽DB��ʱ��
	private Date   createDate;
	//Ԥ������ʱ��
	private Date   scheduleDate;
	//	��Ϣ����ʱ��
	private Date   sendDate;
    //  ���ʱ��(�Է���Ϊ��λ)
    private int    intervalTime;
	//  ���ʹ���
	private int    sendTime;
	//	���ͼ���
	private int    sendCount;
	//	��Ϣ����ʱ��
	private Date   endDate;
	
	
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getMessageStatus() {
		return messageStatus;
	}
	public void setMessageStatus(String messageStatus) {
		this.messageStatus = messageStatus;
	}
	public String getSendUserId() {
		return sendUserId;
	}
	public String getMessageContent() {
		return messageContent;
	}
	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}
	public Date getSendDate() {
		return sendDate;
	}
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	
	public int getSendCount() {
		return sendCount;
	}
	public void setSendCount(int sendCount) {
		this.sendCount = sendCount;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getReceiveUserId() {
		return receiveUserId;
	}
	public void setReceiveUserId(String receiveUserId) {
		this.receiveUserId = receiveUserId;
	}
	public int getSendTime() {
		return sendTime;
	}
	public void setSendTime(int sendTime) {
		this.sendTime = sendTime;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getScheduleDate() {
		return scheduleDate;
	}
	public void setScheduleDate(Date scheduleDate) {
		this.scheduleDate = scheduleDate;
	}
    public int getIntervalTime() {
        return intervalTime;
    }
    public void setIntervalTime(int intervalTime) {
        this.intervalTime = intervalTime;
    }	
}
