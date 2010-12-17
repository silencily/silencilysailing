/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.common.message.dto;

import java.util.Date;

import net.silencily.sailing.framework.persistent.BaseDto;
import net.silencily.sailing.framework.persistent.hibernate3.entity.CodeWrapper;
import net.silencily.sailing.framework.persistent.hibernate3.entity.UserWrapper;

/**
 * ��Ϣ����ʵ�壺��һ����Ϣ��Ӧһ�����߶�������ˣ�ÿ������һ����¼
 * @hibernate.class table="platform_message_accept"
 * @since 2006-10-21
 * @author pillarliu 
 * @version $Id: CommonMessageAccept.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public class CommonMessageAccept extends BaseDto{

	private static final long serialVersionUID = 1L;

	/**�Ķ�״̬��δ�Ķ� */
	public static String ACCEPT_STATUS_NONE    = "message.acceptStatus.00";
	/**�Ķ�״̬�����Ķ�*/
	public static String ACCEPT_STATUS_FINSITH = "message.acceptStatus.01";
	/**�Ķ�״̬���ѻظ�*/
	public static String ACCEPT_STATUS_REPLAY  = "message.acceptStatus.02";
	/**�Ķ�״̬����ת��*/
	public static String ACCEPT_STATUS_MOVE    = "message.acceptStatus.03";
	
	/**�������ͣ�����*/
	public static String ACCEPT_TYPE_NORMAL    = "message.acceptType.01";
	/**�������ͣ�ɾ��*/
	public static String ACCEPT_TYPE_KILLED    = "message.acceptType.02";
	
	/**��Ӧ����Ϣʵ��*/
	private CommonMessage message;
	
	/**������*/
	private UserWrapper acceptPerson = new UserWrapper();
	
	/**�������ͣ�ֱ�ӷ���=0������=1������=2*/
	private CodeWrapper sendType;
	
	/**�Ķ�״̬*/
	private CodeWrapper status;
	
	/**�������ͣ�����/������������ɾ��������Ϣ������ʾ�ڽ����б��У�*/
	private CodeWrapper acceptType;

	/**��������*/
	private Date acceptDate;
	
	private String remark;

	
	/**
	 * @hibernate.property column="accept_date" type="timestamp" 
	 * @return the acceptDate
	 */	
	public Date getAcceptDate() {
		return acceptDate;
	}
	public void setAcceptDate(Date acceptDate) {
		this.acceptDate = acceptDate;
	}

	/**
	 * @hibernate.property  column="accept_person" type="user_wrapper" 
	 * @return the acceptPerson
	 */
	public UserWrapper getAcceptPerson() {
		return acceptPerson;
	}
	public void setAcceptPerson(UserWrapper acceptPerson) {
		this.acceptPerson = acceptPerson;
	}

	/**
	 * @hibernate.property  column="accept_type" type="code_wrapper"  
	 * @return the acceptType
	 */
	public CodeWrapper getAcceptType() {
		if(acceptType == null){
			acceptType = new CodeWrapper();
		}
		return acceptType;
	}
	public void setAcceptType(CodeWrapper acceptType) {
		this.acceptType = acceptType;
	}

	/**
	 * @hibernate.many-to-one class = "com.coheg.common.message.dto.CommonMessage" column = "message_id" not-null = "true"
	 * @return the message
	 */
	public CommonMessage getMessage() {
		return message;
	}

	public void setMessage(CommonMessage message) {
		this.message = message;
	}

	/**
	 * @hibernate.property  column="remark" type="string"  length="4000"
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @hibernate.property  column="send_type" type="code_wrapper" 
	 * @return the sendType
	 */
	public CodeWrapper getSendType() {
		if(sendType == null){
			sendType = new CodeWrapper();
		}
		return sendType;
	}
	public void setSendType(CodeWrapper sendType) {
		this.sendType = sendType;
	}

	/**
	 * @hibernate.property  column="status" type="code_wrapper"  
	 * @return the status
	 */
	public CodeWrapper getStatus() {
		if(status == null){
			status = new CodeWrapper();
		}
		return status;
	}
	public void setStatus(CodeWrapper status) {
		this.status = status;
	}
	



}
