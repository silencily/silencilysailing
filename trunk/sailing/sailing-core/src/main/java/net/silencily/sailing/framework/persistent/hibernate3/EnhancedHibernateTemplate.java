/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project framework
 */
package net.silencily.sailing.framework.persistent.hibernate3;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.framework.persistent.filter.Condition;
import net.silencily.sailing.framework.persistent.filter.ConditionConstants;
import net.silencily.sailing.framework.persistent.filter.ConditionInfo;
import net.silencily.sailing.framework.persistent.filter.Conditions;
import net.silencily.sailing.framework.persistent.filter.Paginater;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.SimpleExpression;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.impl.CriteriaImpl.OrderEntry;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * 增强的 HibernateTemplate, 重载 {@link HibernateTemplate} 的一些方法以实现一些增强功能,
 * 目前已经实现的功能有 : 
 * <ul>
 * <li>重载 findByCriteria 方法实现自动翻页, 自动查询</li>
 * </ul>
 * 目前在 Hibernate 3.0.5, 3.1.2, 3.1.3 中测试通过, 不保证其他版本可用, 需要实现自动查询分页功能的
 * 业务类必须使用 {@link #findByCriteria(DetachedCriteria)} 进行查询
 * @see Condition
 * @see Conditions
 * @since 2006-8-19
 * @author java2enterprise
 * @version $Id: EnhancedHibernateTemplate.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 */
public class EnhancedHibernateTemplate extends HibernateTemplate implements ConditionConstants {

	private static transient Log logger = LogFactory.getLog(EnhancedHibernateTemplate.class);
	
	private static final String CRITERIA_ASSERT_ERROR_MESSAGE = " 's type is not " + CriteriaImpl.class + ", please make sure you are using Hibernate3.0.5!!! ";

	/**
	 * 重载以实现自动查询、 翻页功能
	 * @see Paginater
	 * @see org.springframework.orm.hibernate3.HibernateTemplate#findByCriteria(org.hibernate.criterion.DetachedCriteria)
	 */
	public List findByCriteria(final DetachedCriteria criteria) throws DataAccessException {
		Assert.notNull(criteria, "DetachedCriteria required");        
		EnhancedCriteriaCallback callback = new EnhancedCriteriaCallback(criteria);
		return (List) executeWithNativeSession(callback);
	}
		
	private void rebuildCriterial(DetachedCriteria criteria) {
		ConditionInfo info = ContextInfo.getContextCondition();
		if (info == null) {
			return;
		}
		
		Condition[] globalConditions = ContextInfo.getContextCondition().getOriginalConditions();
		Condition[] appendConditions = ContextInfo.getContextCondition().getAppendConditions();
		
		// 根据 order 排序
		Arrays.sort(appendConditions, new Comparator() {
			public int compare(Object o1, Object o2) {
				Condition condition1 = (Condition) o1;
				Condition condition2 = (Condition) o2;				
				return condition1.getOrder() - condition2.getOrder();
			}
		});
		
		// 记录构造查询条件中的所有 alias
		List aliasList = new ArrayList();
		
		appendConditions(criteria, globalConditions, aliasList);
		appendConditions(criteria, appendConditions, aliasList);	
	}

	private void appendConditions(DetachedCriteria criteria, Condition[] conditions, List aliasList) {
		List criterions = new ArrayList();
		List prepends = new ArrayList();
		for (int i = 0; i < conditions.length; i++) {
			Condition condition = conditions[i];
			if (!condition.isPlace()) {
				continue;
			}
			
			if (StringUtils.isNotBlank(condition.getName())) {
				Criterion criterion = getSingleCondition(criteria, condition, aliasList);
				criterions.add(criterion);
				prepends.add(condition.getPrepend());
			} else {
				// 处理复合查询
				if (condition.getCompositeConditions().length > 0) {				
					appendConditions(criteria, condition.getCompositeConditions(), aliasList);
				} else {
					throw new RuntimeException("查询条件配置错误, 基本属性和复合条件必须包含一项");
				}
			} 
		}
		
		if (!CollectionUtils.isEmpty(criterions)) {
			Criterion criterion = new MultiCriterionsExpression((Criterion[]) criterions.toArray(new Criterion[0]), (String[]) prepends.toArray(new String[0]));
			criteria.add(criterion);
		}
	}
	
	private Criterion getSingleCondition(DetachedCriteria criteria, Condition condition, List aliasList) {
		String name = condition.getName();
		String operator = condition.getOperator();
		if (operator == null) {
			operator = EQUAL;
		}
		Object value = condition.getValue();
		// 截去两端的空格
		if (String.class.isInstance(value)) {
			value = ((String) value).trim();
		}
		if (LIKE.equals(operator)) {
			value = "%" + value + "%";
		}
		operator = " " + operator + " ";
		
		// 处理关联查询
		String lastAlias = null;		
		if (name.indexOf(PROPERTY_SEPARATOR) > -1 && condition.isCreateAlias()) {
			StringTokenizer tokenizer = new StringTokenizer(name, PROPERTY_SEPARATOR, false);
			StringBuffer createdAlias = new StringBuffer();
			while (tokenizer.hasMoreTokens()) {
				String alias = tokenizer.nextToken();
				if (tokenizer.hasMoreTokens()) {
					lastAlias = alias;
					createdAlias.append(alias);
					String associationPath = createdAlias.toString();
					createdAlias.append(PROPERTY_SEPARATOR);
					
					if (!aliasList.contains(associationPath)) {
						criteria.createAlias(associationPath, alias);
						aliasList.add(associationPath);
					}
				}
			}			
		}
		
		String propertyName = name;
		if (lastAlias != null) {
			propertyName = lastAlias + name.substring(name.lastIndexOf(PROPERTY_SEPARATOR));
		}
				
		try {	
			Constructor constructor = SimpleExpression.class.getDeclaredConstructor(new Class[] {String.class, Object.class, String.class});
			constructor.setAccessible(true);
			SimpleExpression expression = (SimpleExpression) constructor.newInstance(new Object[] {propertyName, value, operator});					
			return expression;
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("动态构造查询条件出错 : " + e);
			}
			throw new RuntimeException("动态构造查询条件出错", e);
		} 	
	} 
	
	public static void main(String[] args) {
		String string = "a.b.c.d";
		StringTokenizer tokenizer = new StringTokenizer(string, ".", false);
		while (tokenizer.hasMoreTokens()) {
			System.out.println("Next Token -> " + tokenizer.nextToken());
		}
	}
	
	
	/**
     * 从 criteria 中取得 {@link Projection}, 接口中没有公开此方法, 因此从 {@link CriteriaImpl} 中取得
     * @see CriteriaImpl#getProjection()
     * @param criteria the criteria
     * @return the Projection
     */
	private Projection getProjection(Criteria criteria) {
    	assertType(criteria);
    	CriteriaImpl impl = (CriteriaImpl) criteria;
    	return impl.getProjection();
    }
   
	private void assertType(Criteria criteria) {
		Assert.notNull(criteria, " criteria is required. ");
		String message = criteria + CRITERIA_ASSERT_ERROR_MESSAGE;
		if (!CriteriaImpl.class.isInstance(criteria)) {
    		if (logger.isDebugEnabled()) {
    			logger.debug(message);
    		}
    		throw new RuntimeException(message);
    	}
	}
    
	/**
	 * 得到 criteria 中的 OrderEntry[]
	 * @param criteria the criteria
	 * @return the OrderEntry[]
	 */
	private OrderEntry[] getOrders(Criteria criteria) {
    	assertType(criteria);
		CriteriaImpl impl = (CriteriaImpl) criteria;
		Field field = getOrderEntriesField(criteria);
		try {
			return (OrderEntry[]) ((List) field.get(impl)).toArray(new OrderEntry[0]);
		} catch (Exception e) {
    		logAndThrowException(criteria, e);
    		throw new InternalError(" Runtime Exception impossibility can't throw ");
		} 
    }
    
	/**
	 * 移除 criteria 中的 OrderEntry[]
	 * @param criteria the criteria
	 * @return the criteria after removed OrderEntry[]
	 */
	private Criteria removeOrders(Criteria criteria) {
    	assertType(criteria);
    	CriteriaImpl impl = (CriteriaImpl) criteria;
    	
    	try {
        	Field field = getOrderEntriesField(criteria);
        	field.set(impl, new ArrayList());
        	return impl;
    	} catch (Exception e) {
    		logAndThrowException(criteria, e);
    		throw new InternalError(" Runtime Exception impossibility can't throw ");
    	}	
    }

    /**
     * 为 criteria 增加 OrderEntry[]
     * @param criteria the criteria
     * @param orderEntries the OrderEntry[]
     * @return the criteria after add OrderEntry[]
     */
	private Criteria addOrders(Criteria criteria, OrderEntry[] orderEntries) {
    	assertType(criteria);
    	CriteriaImpl impl = (CriteriaImpl) criteria;
    	try {
        	Field field = getOrderEntriesField(criteria);
        	for (int i = 0; i < orderEntries.length; i++) {
        		List innerOrderEntries = (List) field.get(criteria);
        		innerOrderEntries.add(orderEntries[i]);
        	}
        	return impl;
    	} catch (Exception e) {
    		logAndThrowException(criteria, e);
    		throw new InternalError(" Runtime Exception impossibility can't throw ");
    	}
    }

	private void logAndThrowException(Criteria criteria, Exception e) {
		String message = criteria + CRITERIA_ASSERT_ERROR_MESSAGE;
		if (logger.isDebugEnabled()) {
			logger.debug(message, e);
		}
		throw new RuntimeException(message, e);
	}
    
	private Field getOrderEntriesField(Criteria criteria) {
		Assert.notNull(criteria, " criteria is requried. " );
		try {
			Field field = CriteriaImpl.class.getDeclaredField("orderEntries");
			field.setAccessible(true);
			return field;
		} catch (Exception e) {
			logAndThrowException(criteria, e);
    		throw new InternalError();
		}
	}
	
	/**
	 * inner class , core implments of enchanced criteria
	 *
	 */
	private class EnhancedCriteriaCallback implements HibernateCallback {
		
		private DetachedCriteria criteria;

		public EnhancedCriteriaCallback(DetachedCriteria criteria) {
			this.criteria = criteria;
		}

		public Object doInHibernate(Session session) throws HibernateException, SQLException {
			Criteria executableCriteria = criteria.getExecutableCriteria(session);				
			prepareCriteria(executableCriteria);
			
			Paginater paginater = null;
			int firstResult = 0;
			int maxResults = 0;
			
			// 添加 ContextInfo 中的查询条件
			if (!ContextInfo.isConcealQuery()) {
				rebuildCriterial(criteria);			
			
				if (ContextInfo.getContextCondition() != null) {
					paginater = ContextInfo.getContextCondition().getPaginater();
				}
				if (paginater != null) {
					firstResult = paginater.getPage() * paginater.getPageSize();
					maxResults = paginater.getPageSize(); 
				}
				
				// 查询结束后屏蔽当前条件
		        ContextInfo.concealQuery();
			}
			
			// 构造分页查询
			if (paginater != null && paginater != Paginater.NOT_PAGINATED) {
				// Get the orginal orderEntries
				OrderEntry[] orderEntries = getOrders(executableCriteria);
				// Remove the orders
				executableCriteria = removeOrders(executableCriteria);				
				// get the original projection
				Projection projection = getProjection(executableCriteria);
				
				Integer iCount = (Integer) executableCriteria.setProjection(Projections.rowCount()).uniqueResult();
                if (iCount == null) {
                	throw new RuntimeException("无法执行 count 统计, 很可能是 [ " + criteria.getClass() + " ] 没有相应的 hbm 配置文件");
                }				
				int totalCount = iCount == null ? 0 : iCount.intValue();
                paginater.setCount(totalCount);
                
                executableCriteria.setProjection(projection);
                if (projection == null) {
                	// Set the ResultTransformer to get the same object structure with hql
                	executableCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
                }              
                // Add the orginal orderEntries
                executableCriteria = addOrders(executableCriteria, orderEntries);
                // 追加排序项目id(如果不进行排序,翻页处理无法正常显示)
                Order order = Order.asc("id");//new Order("id", true);
                executableCriteria.addOrder(order);
                // 修正查询后查询结果不在第一页无法显示的问题, 自动定向到第一页
                if (firstResult >= totalCount) {
                	firstResult = 0;
                	paginater.setPage(0);
                }   
                
                if (firstResult >= 0) {
                	executableCriteria.setFirstResult(firstResult);
                }
                if (maxResults > 0) {
                	executableCriteria.setMaxResults(maxResults);
                }
			}
			
            return executableCriteria.list();
		}
	}
}
