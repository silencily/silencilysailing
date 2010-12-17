/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.common.message.service.impl;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.silencily.sailing.common.CommonServiceLocator;
import net.silencily.sailing.common.message.dto.CommonMessage;
import net.silencily.sailing.common.message.dto.CommonMessageAccept;
import net.silencily.sailing.common.message.dto.CommonMessageStat;
import net.silencily.sailing.common.message.service.CommonMessageService;
import net.silencily.sailing.common.systemcode.SystemCode;
import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.framework.core.User;
import net.silencily.sailing.framework.persistent.hibernate3.entity.CodeWrapper;

/**
 * 实现消息的服务
 * @since 2006-10-21
 * @author pillarliu 
 * @version $Id: AbstractCommonMessageService.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 */
public abstract class AbstractCommonMessageService implements CommonMessageService{
	
	protected abstract CommonMessage loadEntity(Serializable id);
	protected abstract void saveEntity(CommonMessage message);
	protected abstract void removeEntity(CommonMessage message);
	protected abstract void updateEntity(CommonMessage message);
	protected abstract List findAllEntities();
	protected abstract List findSendEntitiesForCurrentUser(String username);
	protected abstract List findAcceptEntitiesForCurrentUser(String username);
	protected abstract List findSendEntitiesForStatus(String username,String status);
	protected abstract List findAcceptEntitiesForStatus(String username,String status);
	protected abstract List statEntityForAcceptStatus(String username);
	protected abstract List statEntityForSendStatus(String username);
    
	public List findAll() {
		return findAllEntities();
	}

	public void remove(CommonMessage message) {
		if(message.isScratch()){
			removeEntity(message);
		}else{
			CodeWrapper status = new CodeWrapper();
			status.setCode(CommonMessage.STEND_STATUS_KILLED);
			message.setStatus(status);
		}
	}
	
	public void save(CommonMessage message) {
		saveEntity(message);
		//这里要将路径中oid替换为生成的id
		String content = message.getContent();
		String regexOid = "#oid#";
		content = content.replaceAll(regexOid, message.getId());
		message.setContent(content);
	}
	
	public void update(CommonMessage message) {
		updateEntity(message);
	}

	public CommonMessage load(Serializable id){
		return loadEntity(id);
	}
	
	public List findAcceptMessage(){
		User user = ContextInfo.getCurrentUser();
		List result = findAcceptEntitiesForCurrentUser(user.getUsername());
		return result;
	}
	
	public List findSendMessage(){
		User user = ContextInfo.getCurrentUser();
		List result = findSendEntitiesForCurrentUser(user.getUsername());
		return result;
	}
	
	public List findSendMessageForStatus(String status){
		User user = ContextInfo.getCurrentUser();
		List result = findSendEntitiesForStatus(user.getUsername(),status);
		return result;
	}
	
	public List findAcceptMessageForStatus(String status){
		User user = ContextInfo.getCurrentUser();
		List result = findAcceptEntitiesForStatus(user.getUsername(),status);
		return result;
	}
	
	
	public CommonMessage updateAcceptStatue(CommonMessage message,String oldStatus,String newStatus){
		User user = ContextInfo.getCurrentUser();
		Set set = message.getAcceptPersons();
		CodeWrapper status = new CodeWrapper();
		status.setCode(newStatus);
		for (Iterator iter = set.iterator(); iter.hasNext(); ) {
			CommonMessageAccept accept = (CommonMessageAccept) iter.next();
			if(user.getUsername().equals(accept.getAcceptPerson().getUsername())){
				if(accept.getStatus().getCode().equals(oldStatus)){
					accept.setStatus(status);
				}
				break;
			}
		}
		return message;
	}
	
	public CommonMessage removeAcceptType(CommonMessage message){
		User user = ContextInfo.getCurrentUser();
		Set set = message.getAcceptPersons();
		CodeWrapper status = new CodeWrapper();
		status.setCode(CommonMessageAccept.ACCEPT_TYPE_KILLED);
		for (Iterator iter = set.iterator(); iter.hasNext(); ) {
			CommonMessageAccept accept = (CommonMessageAccept) iter.next();
			if(user.getUsername().equals(accept.getAcceptPerson().getUsername())){
				accept.setAcceptType(status);
				break;
			}
		}
		return message;
	}
	
	//TODO 目前采用ibatis不提供codeWapper填入，只能手工load
	public List statForAcceptStatus(){
		User user = ContextInfo.getCurrentUser();
		List list = statEntityForAcceptStatus(user.getUsername());
		for (Iterator iter = list.iterator(); iter.hasNext(); ) {
			CommonMessageStat stat = (CommonMessageStat) iter.next();
			SystemCode code = CommonServiceLocator.getSystemCodeService().load(MODULE_NAME, stat.getCode());
			stat.setStatus(code);
		}		
		return list;
	}
	
	public List statForSendStatus(){
		User user = ContextInfo.getCurrentUser();
		List list = statEntityForSendStatus(user.getUsername());
		for (Iterator iter = list.iterator(); iter.hasNext(); ) {
			CommonMessageStat stat = (CommonMessageStat) iter.next();
			SystemCode code = CommonServiceLocator.getSystemCodeService().load(MODULE_NAME, stat.getCode());
			stat.setStatus(code);
		}		
		return list;
	}
}
