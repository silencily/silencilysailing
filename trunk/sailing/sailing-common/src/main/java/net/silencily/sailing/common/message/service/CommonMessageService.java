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
 * ������Ϣ�Ľӿ�
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
	
	/**�õ���ǰ�û����͵���Ϣ
	 * �Է���ʱ�併������
	 */
	List findSendMessage();
	
	/**
	 * ��״̬��ѯ���͵���Ϣ
	 * @param type
	 * @return
	 */
	List findSendMessageForStatus(String status);
	
	/**�õ���ǰ�û����ܵ���Ϣ
	 * �Խ���ʱ�併������(ע��,�ڻ�û�н���ǰ,�ǰ�����ʱ������)
	 */
	List findAcceptMessage();
	
	/**
	 * ��״̬��ѯ���ܵ���Ϣ
	 * @param type
	 * @return
	 */
	List findAcceptMessageForStatus(String status);

	/**����Ϣ�����ߵĽ���״̬ΪoldStatus�����޸�ΪnewStatus״̬
	 * 
	 * @param oldStatus  �޸�ǰ��״̬
	 * @param newStatus  �޸ĺ��״̬
	 */
	CommonMessage updateAcceptStatue(CommonMessage message,String oldStatus,String newStatus);
	
	/**
	 * �����������޸�Ϊ��ɾ����
	 * @param message
	 * @return
	 */
	CommonMessage removeAcceptType(CommonMessage message);
	
	/**�õ�������Ϣ�ķ�״̬ͳ��*/
	List statForAcceptStatus();
	
	/**�õ�������Ϣ�ķ�״̬ͳ��*/
	List statForSendStatus();
	
}
