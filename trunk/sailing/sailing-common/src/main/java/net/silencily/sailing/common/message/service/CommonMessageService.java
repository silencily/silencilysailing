/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.common.message.service;


import java.io.Serializable;
import java.util.List;

import net.silencily.sailing.common.CommonConstants;
import net.silencily.sailing.common.message.dto.CommonMessage;
import net.silencily.sailing.common.message.dto.CommonMessageAccept;
import net.silencily.sailing.framework.core.ServiceBase;


/**
 * 公共消息的接口
 * @since 2006-10-03
 * @author pillar
 * @version $Id: CommonMessageService.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public interface CommonMessageService extends ServiceBase, CommonConstants{
	
	String SERVICE_NAME = MODULE_NAME + ".messageService";
	
	Class ENTITY_CLASS = CommonMessage.class;
	
	String ORDER_PROPERTY = "sendDate";
	
	Class ENTITY_ACCEPT_CLASS = CommonMessageAccept.class;
	
	String ORDER_ACCEPT  = "acceptDate";
	
	void save(CommonMessage message);
	
	void update(CommonMessage message);
	
	void remove(CommonMessage message);
	
	CommonMessage load(Serializable id);	
		
	List findAll();	
	
	/**得到当前用户发送的消息
	 * 以发送时间降序排列
	 */
	List findSendMessage();
	
	/**
	 * 分状态查询发送的消息
	 * @param type
	 * @return
	 */
	List findSendMessageForStatus(String status);
	
	/**得到当前用户接受的消息
	 * 以接受时间降序排列(注意,在还没有接受前,是按发送时间排列)
	 */
	List findAcceptMessage();
	
	/**
	 * 分状态查询接受的消息
	 * @param type
	 * @return
	 */
	List findAcceptMessageForStatus(String status);

	/**当消息接受者的接受状态为oldStatus，则修改为newStatus状态
	 * 
	 * @param oldStatus  修改前的状态
	 * @param newStatus  修改后的状态
	 */
	CommonMessage updateAcceptStatue(CommonMessage message,String oldStatus,String newStatus);
	
	/**
	 * 将接受类型修改为“删除”
	 * @param message
	 * @return
	 */
	CommonMessage removeAcceptType(CommonMessage message);
	
	/**得到接受消息的分状态统计*/
	List statForAcceptStatus();
	
	/**得到发送消息的分状态统计*/
	List statForSendStatus();
	
}
