/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project framework
 */
package net.silencily.sailing.framework.persistent.hibernate3;

import java.util.List;

import net.silencily.sailing.framework.persistent.filter.ConditionConstants;
import net.silencily.sailing.utils.MiscUtils;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * @since 2006-10-4
 * @author java2enterprise
 * @version $Id: CriterionUtils.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 * @deprecated With HibernateUtils instead of this 
 */
public abstract class CriterionUtils {
	
	/**
	 * 根据 id 集合得到 in 查询条件, 查询时如果 id 集合超过 1000 个, 会自动进行分组
	 * @param ids list fill with id {@link String}
	 * @return 组合后的 in 条件
	 */
	public static Criterion splitIdsConditionIfNecessary(List ids) {
		return splitParamsConditionIfNecessary(ids, "id");
	}
	
	/**
	 * 根据参数集合, 参数名称 得到 in 查询条件, 查询时如果 参数集合超过 1000 个, 会自动进行分组
	 * @param params list fill with param
	 * @param paramName 参数属性名称
	 * @return 组合后的 in 条件
	 */
	public static Criterion splitParamsConditionIfNecessary(List params, String paramName) {
		// Oracle 接受的一个表达式中的最多参数个数
		int maxNumberOfExpression = 1000;
		List splitedList = MiscUtils.splitListBySize(params, maxNumberOfExpression);				
		int size = splitedList.size();
		Criterion[] criterions = new Criterion[size];
		String[] ops = new String[size];
		
		for (int i = 0; i < size; i++) {
			List splitedParams = (List) splitedList.get(i);
			criterions[i] = Restrictions.in(paramName, splitedParams);
			ops[i] = ConditionConstants.OR;
		}
		
		Criterion criterion = new MultiCriterionsExpression(criterions, ops);
		return criterion;
	}
	
}
