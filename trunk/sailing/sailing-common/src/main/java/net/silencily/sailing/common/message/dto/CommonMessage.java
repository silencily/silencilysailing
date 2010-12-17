/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.common.message.dto;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import net.silencily.sailing.common.CommonConstants;
import net.silencily.sailing.common.fileupload.UploadFiles;
import net.silencily.sailing.common.fileupload.VfsUploadFiles;
import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.framework.core.User;
import net.silencily.sailing.framework.persistent.BaseDto;
import net.silencily.sailing.framework.persistent.hibernate3.entity.CodeWrapper;
import net.silencily.sailing.framework.persistent.hibernate3.entity.UserWrapper;
import net.silencily.sailing.utils.DateUtils;

/**
 * ���õ���Ϣʵ�壬Ϊ��ʵ�������ʼ�����Ĺ��ܣ����������һЩ�ʼ�����
 * @hibernate.class table="platform_message"
 * @since 2006-10-21
 * @author pillarliu 
 * @version $Id: CommonMessage.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public class CommonMessage  extends BaseDto{

	private static final long serialVersionUID = 1L;

	/**����״̬���ݸ� */
	public static String STEND_STATUS_SCRATCH = "message.sendStatus.00";
	/**����״̬���ѷ���*/
	public static String STEND_STATUS_FINSITH = "message.sendStatus.01";
	/**����״̬��ɾ��*/
	public static String STEND_STATUS_KILLED  = "message.sendStatus.02";

	/**��Ϣ����*/
	private CodeWrapper type;
	
	/**��Ϣ����*/
	private String title;
	
	/**��Ϣ���ݣ�TODO û��ʹ��clob*/
	private String content;
	
	/**��Ϣ���ߣ���������*/
	private UserWrapper author = new UserWrapper();
	
	/**��������*/
	private Date sendDate;
	
	/**���ӵ�ַ���������ӵ���������*/
	private String url;
	
	/**״̬�� */
	private CodeWrapper status;
	
	/**��Ҫ��*/
	private CodeWrapper messageLevel;

	/**�����ˣ���Ӧ������Ϣ���ܼ�¼*/
	private Set acceptPersons = new LinkedHashSet();
		
	/** ��Ϣ�а����ĸ��� */
	private UploadFiles uploadFiles = new VfsUploadFiles();
	
	/** �Ƿ��и���*/
	private boolean attachement = false;
	
	/**
	 * @hibernate.property  column="author" type="user_wrapper" 
	 * @return the author
	 */
	public UserWrapper getAuthor() {
		return author;
	}

	public void setAuthor(UserWrapper author) {
		this.author = author;
	}

	/**
	 * @hibernate.property  column="content" type="string"  length="4000"
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @hibernate.property  column="message_level" type="code_wrapper"  
	 * @return the messageLevel
	 */
	public CodeWrapper getMessageLevel() {
		if(messageLevel == null){
			messageLevel = new CodeWrapper();
		}
		return messageLevel;
	}

	public void setMessageLevel(CodeWrapper level) {
		this.messageLevel = level;
	}

	/**
	 * @hibernate.property column="send_date" type="timestamp" 
	 * @return the sendDate
	 */	
	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
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
	
	public boolean isScratch(){
		if(STEND_STATUS_SCRATCH.equals(getStatus().getCode())){
			return true;
		}
		return false;
	}

	/**
	 * @hibernate.property  column="title" type="string"  
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @hibernate.property  column="type" type="code_wrapper"  
	 * @return the type
	 */
	public CodeWrapper getType() {
		if(type == null){
			type = new CodeWrapper();
		}
		return type;
	}

	public void setType(CodeWrapper type) {
		this.type = type;
	}

	/**
	 * @hibernate.property  column="url" type="string"  
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @hibernate.set table = "platform_message_accept" cascade = "all-delete-orphan" inverse = "true" order-by="accept_person"
	 * @hibernate.key column = "message_id" not-null="true"
	 * @hibernate.one-to-many class = "com.coheg.common.message.dto.CommonMessageAccept"
	 * @return the acceptPersons
	 */
	public Set getAcceptPersons() {
		return acceptPersons;
	}
	public void setAcceptPersons(Set acceptPersons) {
		this.acceptPersons = acceptPersons;
	}
	public void addMessageAccepts(CommonMessageAccept item) {
		if (acceptPersons == null) {
			acceptPersons = new LinkedHashSet();
		}
		item.setMessage(this);
		CodeWrapper status = new CodeWrapper();
		status.setCode(CommonMessageAccept.ACCEPT_STATUS_NONE);
		item.setStatus(status);
		CodeWrapper type = new CodeWrapper();
		type.setCode(CommonMessageAccept.ACCEPT_TYPE_NORMAL);
		item.setAcceptType(type);
		acceptPersons.add(item);
	}	
	public void removeAllAccept() {
		getAcceptPersons().clear();
	}
	
	/**
	 * @return the uploadFiles
	 */
	public UploadFiles getUploadFiles() {
		return uploadFiles;
	}

	/**
	 * @param uploadFiles the uploadFiles to set
	 */
	public void setUploadFiles(UploadFiles uploadFiles) {
		this.uploadFiles = uploadFiles;
	}
	
	public static CommonMessage prototype(){
		CommonMessage message = new CommonMessage();
		User user = ContextInfo.getCurrentUser();
		UserWrapper userWrapper = new UserWrapper();
    	userWrapper.setUsername(user.getUsername());
    	userWrapper.setChineseName(user.getChineseName());
    	message.setAuthor(userWrapper);
    	message.setSendDate(DateUtils.getCurrentDate());
		return message;
	}
	
    /**
     * �õ��ϴ������� vfs �е�·��
     * 1.ÿ���û�����һ��Ŀ¼
     * 2.���û�ÿһ�꽨��һ��Ŀ¼
     * 3.���û���ÿһ���ÿһ���½���һ��Ŀ¼
     * @return upload files path
     */
	public String getUploadFilesPath() {
		Calendar c = Calendar.getInstance(Locale.CHINESE);
        c.setTime(getSendDate());        
		StringBuffer url = new StringBuffer();
		url.append(CommonConstants.MESSAGE_VFS_ROOT);
		url.append(getAuthor().getUsername());
		url.append("/");
		url.append(c.get(Calendar.YEAR));
		url.append("/");
		url.append(c.get(Calendar.MONTH));
		url.append("/");
		url.append(getId());
		return  url.toString();
	}

	public boolean isAttachement() {
		return attachement;
	}

	public void setAttachement(boolean attachement) {
		this.attachement = attachement;
	}
	
}
