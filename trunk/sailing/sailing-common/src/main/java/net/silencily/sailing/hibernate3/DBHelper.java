package net.silencily.sailing.hibernate3;

import java.math.BigDecimal;
import java.util.List;

import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.framework.persistent.filter.Condition;
import net.silencily.sailing.framework.persistent.filter.ConditionConstants;
import net.silencily.sailing.framework.persistent.filter.Paginater;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;



/**
 * 
 * @author yushn
 * @version 1.0
 */
public class DBHelper {
	private static StringBuffer buildSqlConditions()
	{
		//ȡ������
		StringBuffer result = new StringBuffer();
		Condition[] conditions = ContextInfo.getContextCondition().getAppendConditions();
		for (int i = 0; i < conditions.length; i++) {
			Condition condition = conditions[i];
			if (!condition.isPlace()) {
				continue;
			}
			
			if (StringUtils.isNotBlank(condition.getName())) {
				String name = condition.getName();
				String operator = condition.getOperator();
				if (operator == null) {
					operator = ConditionConstants.EQUAL;
				}
				Object value = condition.getValue();
				// ��ȥ���˵Ŀո�
				if (String.class.isInstance(value)) {
					value = ((String) value).trim();
				}
				if (ConditionConstants.LIKE.equals(operator)) {
					value = "%" + value + "%";
				}
				operator = " " + operator + " ";
				result.append(" and "+name+operator+"'"+value+"' ");
			} 
		}
		return result;
	}
	private static void dealPaginater(HibernateTemplate hibernateTemplate,SQLQuery xsq,StringBuffer basesql)
	{
		Paginater paginater = null;
		int firstResult = 0;
		int maxResults = 0;


		
		if (ContextInfo.getContextCondition() != null) {
			paginater = ContextInfo.getContextCondition().getPaginater();
		}
		
		//����ҳ���
		if (paginater != null && paginater != Paginater.NOT_PAGINATED) {

			firstResult = paginater.getPage() * paginater.getPageSize();
			maxResults = paginater.getPageSize(); 

			Session se = hibernateTemplate.getSessionFactory().getCurrentSession();
			
			//se.getEntityName(arg0)
			
			//��ѯ��¼����
			StringBuffer countSql = new StringBuffer();
			countSql.append("select count(0)");
			countSql.append(basesql);

			SQLQuery csq = se.createSQLQuery(countSql.toString());
			List counts = csq.list();
			long count = ((BigDecimal)counts.get(0)).longValue();
			
            paginater.setCount((int)count);
            
            if (firstResult >= 0) {
            	xsq.setFirstResult(firstResult);
            }
            if (maxResults > 0) {
            	xsq.setMaxResults(maxResults);
            }

		}
	}
	/**
	 * ʹ��hibernate����sql��ѯ��֧�ַ�ҳ���Զ�����������
	 * ʹ��������
	 *  StringBuffer sql = new StringBuffer();
		sql.append("select * from tbl_cmn_role where father_id in (select id from tbl_cmn_role_org connect by prior id = father_id start with id='");
		sql.append(foid);
		sql.append("')");		
		return DBHelper.findBySqlExtendFromAutoCondition(this.hibernateTemplate,sql.toString(),TblCmnRole.class);
	 * @param hibernateTemplate
	 * @param usersql
	 * @param clazz
	 * @return
	 */
	public static List findBySqlExtendFromAutoCondition(HibernateTemplate hibernateTemplate,String usersql,Class clazz)
	{
		StringBuffer basesql = new StringBuffer();
		basesql.append(" from (");
		basesql.append(usersql);
		basesql.append(")c where 1=1 ");

		//��������
		basesql.append(buildSqlConditions());
	

		//this.hibernateTemplate.
		Session se = hibernateTemplate.getSessionFactory().getCurrentSession();

		
		StringBuffer sql = new StringBuffer();
		
		sql.append("select {c.*} ");
		//sql.append("select * ");
		sql.append(basesql); 
		//sql.append("and rownum <=100");
		//sql.append(") c where rownum >=1");

		SQLQuery sq = se.createSQLQuery(sql.toString());

		//MessageFormat.format

		
		//�����ҳ
		dealPaginater(hibernateTemplate,sq,basesql);
			
		//se.createSQLQuery(sql.toString(), "c", TblCmnPermission.class);
		sq.addEntity("c", clazz);
		List result = sq.list();
		
		return result;		
	}
	
	/** 
	 * ���û������SQL��ѯ����,������Conditions�е�����
	 * @param hibernateTemplate
	 * @param usersql
	 * @param clazz
	 * @return
	 */
	public static List findBySqlExtendFromUserSql(HibernateTemplate hibernateTemplate,String usersql,Class clazz)
	{
		StringBuffer basesql = new StringBuffer();
		basesql.append(" from (");
		basesql.append(usersql);
		basesql.append(")c where 1=1 ");

		Session se = hibernateTemplate.getSessionFactory().getCurrentSession();
		StringBuffer sql = new StringBuffer();
		
		sql.append("select {c.*} ");
		sql.append(basesql); 
		SQLQuery sq = se.createSQLQuery(sql.toString());

		//�����ҳ
		dealPaginater(hibernateTemplate,sq,basesql);
		sq.addEntity("c", clazz);
		List result = sq.list();
		return result;
	}
}
