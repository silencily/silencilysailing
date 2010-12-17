/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.common.message.service.impl;

import java.io.Serializable;
import java.util.List;

import net.silencily.sailing.common.message.dto.CommonMessage;
import net.silencily.sailing.common.message.dto.CommonMessageAccept;
import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.framework.utils.DaoHelper;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;



/**
 * 消息的 hibernate 实现
 * @since 2006-10-21
 * @author pillarliu 
 * @version $Id: HibernateCommonMessageService.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 */
public class HibernateCommonMessageService extends AbstractCommonMessageService{
	protected CommonMessage loadEntity(Serializable id) {
		 CommonMessage message =  (CommonMessage) DaoHelper.getHibernateTemplate().load(ENTITY_CLASS, id);
		 return message;
	}

	protected void removeEntity(CommonMessage message) {
		DaoHelper.getHibernateTemplate().delete(message);
	}

	protected void saveEntity(CommonMessage message) {
		DaoHelper.getHibernateTemplate().save(message);
	}

	protected void updateEntity(CommonMessage message) {
		DaoHelper.getHibernateTemplate().update(message);
	}

	protected List findAllEntities() {
		ContextInfo.recoverQuery();
		DetachedCriteria criteria = getCritera();
		return DaoHelper.getHibernateTemplate().findByCriteria(criteria);
	}
	
	private DetachedCriteria getCritera(){
		return DetachedCriteria.forClass(ENTITY_CLASS).addOrder(Order.desc(ORDER_PROPERTY));
	}
	
	public List findAcceptEntitiesForCurrentUser(String username){
		ContextInfo.recoverQuery();
		DetachedCriteria criteria = DetachedCriteria.forClass(ENTITY_ACCEPT_CLASS).addOrder(Order.desc(ORDER_ACCEPT));
		criteria.add(Restrictions.eq("acceptPerson.username", username));
		criteria.add(Restrictions.eq("acceptType.code", CommonMessageAccept.ACCEPT_TYPE_NORMAL));
		criteria.createAlias("message", "m");
		criteria.add(Restrictions.ne("m.status.code", CommonMessage.STEND_STATUS_SCRATCH));
		return DaoHelper.getHibernateTemplate().findByCriteria(criteria);
	}
	
	public List findSendEntitiesForCurrentUser(String username){
		ContextInfo.recoverQuery();
		DetachedCriteria criteria = getCritera();
		criteria.add(Restrictions.eq("author.username", username));
		criteria.add(Restrictions.ne("status.code",CommonMessage.STEND_STATUS_KILLED));
		return DaoHelper.getHibernateTemplate().findByCriteria(criteria);
	}
	
	public List findSendEntitiesForStatus(String username,String status){
		ContextInfo.recoverQuery();
		DetachedCriteria criteria = getCritera();
		criteria.add(Restrictions.eq("author.username", username));
		criteria.add(Restrictions.eq("status.code",status));
		return DaoHelper.getHibernateTemplate().findByCriteria(criteria);
	}
	
	public List findAcceptEntitiesForStatus(String username,String status){
		ContextInfo.recoverQuery();
		DetachedCriteria criteria = DetachedCriteria.forClass(ENTITY_ACCEPT_CLASS).addOrder(Order.desc(ORDER_ACCEPT));
		criteria.add(Restrictions.eq("acceptPerson.username", username));
		criteria.add(Restrictions.eq("status.code", status));
		criteria.add(Restrictions.ne("acceptType.code",CommonMessageAccept.ACCEPT_TYPE_KILLED));
		//criteria.add(Restrictions.eq("acceptType.code", CommonMessageAccept.ACCEPT_TYPE_NORMAL));
		criteria.createAlias("message", "m");
		criteria.add(Restrictions.ne("m.status.code", CommonMessage.STEND_STATUS_SCRATCH));
		return DaoHelper.getHibernateTemplate().findByCriteria(criteria);
	}
	
	
	/**
	 * 统计采用的ibatis实现
	 */
	
	//注意 草稿的消息不能统计在内
	public List statEntityForAcceptStatus(String username){
		List result = (List) DaoHelper.getSqlMapClientTemplate().queryForList("common.message.statAccept",username);
		return result;
	}
	
	public List statEntityForSendStatus(String username){
		/*
		List result = ((List) DaoHelper.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
	                String hqlQuery = "select status,count(*) num from platform_message where author = '"+username+"'  group by status ";
	                List map = (List) session.createSQLQuery(hqlQuery);
	                return map;
            }}));
		*/
		List result = (List) DaoHelper.getSqlMapClientTemplate().queryForList("common.message.statSend",username);
		return result;
	}
}
