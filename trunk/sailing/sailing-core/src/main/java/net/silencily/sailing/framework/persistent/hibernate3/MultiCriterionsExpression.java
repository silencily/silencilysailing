/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project framework
 */
package net.silencily.sailing.framework.persistent.hibernate3;

import java.util.ArrayList;
import java.util.List;

import net.silencily.sailing.framework.persistent.filter.ConditionConstants;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.engine.TypedValue;
import org.springframework.util.Assert;

/**
 * 将多个 Criterion 合并成一个 Criterion 的 Expression, 与 {@link Junction} 不同的是
 * 每个子 Expression 都可以有不同的连接符 and 或 or
 * @see Junction
 * @see Conjunction
 * @see Disjunction
 * @since 2006-8-20
 * @author java2enterprise
 * @version $Id: MultiCriterionsExpression.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 */
public class MultiCriterionsExpression implements Criterion {
	
	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = -6153626155195309794L;

	private Criterion[] criterions = new Criterion[0];
	
	private String[] ops = new String[0];
	
	public MultiCriterionsExpression(Criterion[] criterions, String[] ops) {
		Assert.notNull(criterions, " criterions required. ");
		Assert.notNull(ops, " ops required. ");
		Assert.isTrue(criterions.length == ops.length);
		this.criterions = criterions;
		this.ops = ops;
		validateOps(ops);
	}

	private void validateOps(String[] ops) {
		for (int i = 0; i < ops.length; i++) {
			Assert.isTrue(ops[i] == null || ConditionConstants.AND.equals(ops[i]) || ConditionConstants.OR.equals(ops[i])); 
		}
	}
	
	public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery) 
		throws HibernateException {
		
		List result = new ArrayList();
		
		for (int i = 0; i < criterions.length; i++) {
			TypedValue[] typedValues = criterions[i].getTypedValues(criteria, criteriaQuery);
			for (int j = 0; j < typedValues.length; j++) {
				result.add(typedValues[j]);
			}
		}
		
		return (TypedValue[]) result.toArray(new TypedValue[0]);
	}

	public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery)
		throws HibernateException {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" ( " );
		for (int i = 0; i < criterions.length; i++) {
			if (i != 0) {
				buffer.append(ops[i] == null ? ConditionConstants.AND : ops[i]);
			}			
			buffer.append(" ");
			buffer.append(criterions[i].toSqlString(criteria, criteriaQuery));
			buffer.append(" ");
		}
		buffer.append(" ) ");
		return buffer.toString();
	}

}
