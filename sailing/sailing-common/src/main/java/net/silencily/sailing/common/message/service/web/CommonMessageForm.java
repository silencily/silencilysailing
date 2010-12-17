/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.common.message.service.web;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.silencily.sailing.common.CommonServiceLocator;
import net.silencily.sailing.common.message.dto.CommonMessage;
import net.silencily.sailing.common.message.dto.CommonMessageAccept;
import net.silencily.sailing.framework.web.struts.BaseActionForm;

import org.apache.commons.lang.StringUtils;

/**
 * 消息 form bean
 * @since 2006-9-29
 * @author pillarliu 
 * @version $Id: CommonMessageForm.java,v 1.1 2010/12/10 10:54:17 silencily Exp $
 */
public class CommonMessageForm  extends BaseActionForm{

	private static final long serialVersionUID = 1L;
	
	private CommonMessage message;
	
	private CommonMessageAccept[] acceptPersons = new CommonMessageAccept[0];
	
	protected String PARAM_OID = "oid";
	
	/**用于列表list*/
	private Collection results = Collections.EMPTY_LIST;
	
	/** 下载的文件名 */
	private String fileName;
	
	/** 分状态查询*/
	private String status;
	
	/**各状态统计*/
	private List messageStatus;
	
	/**为了避免中文问题：传输接受人名*/
	private String personName;
	/***/
	private String personChinese;
		
	public String getStatus() {
		return status;
	}

	public void setStatus(String type) {
		this.status = type;
	}

	public CommonMessage getMessage() {		
		if (message == null) {
			String activeOid = request.getParameter(PARAM_OID);
			if(StringUtils.isNotBlank(activeOid)){
				message = CommonServiceLocator.getMessageService().load(activeOid);
			}else{
				message = CommonMessage.prototype();
			}
		}		
		return message;
	}
	
	public void setMessage(CommonMessage coal){
		this.message = coal;
	}

	public CommonMessageAccept[] getAcceptPersons(){
		return acceptPersons;
	}	
	public void setAcceptPersons(CommonMessageAccept[] truck){
		this.acceptPersons = truck;
	}
	public CommonMessageAccept getAcceptPersons(int index) 
		throws IndexOutOfBoundsException {
		CommonMessageAccept item = acceptPersons[index];
		return item;
	}
	public void setAcceptPersons(int index, CommonMessageAccept item)
		throws IndexOutOfBoundsException {
		this.acceptPersons[index] = item;
	}
		
	/**
	 * @return the results
	 */
	public Collection getResults() {
		return results;
	}

	/**
	 * @param results the results to set
	 */
	public void setResults(Collection results) {
		this.results = results;
	}
	
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List getMessageStatus() {
		return messageStatus;
	}

	public void setMessageStatus(List messageStatus) {
		this.messageStatus = messageStatus;
	}

	public String getPersonChinese() {
		return personChinese;
	}

	public void setPersonChinese(String personChinese) {
		this.personChinese = personChinese;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}
}
