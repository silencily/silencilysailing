package net.silencily.sailing.hibernate3;


import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.framework.persistent.filter.Condition;
import net.silencily.sailing.framework.persistent.filter.ConditionConstants;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.SimpleExpression;




//AutoConditionsUtil.AddAliasFromConditionInfo(dc);
/**
 * 代替<code>EnhancedHibernateTemplate</code>处理关联查询条件组装
 * 此过程只影响a.b.fieldname(2级)及更多层次的条件，不能处理复合查询
 * 使用样例：
 * <code>
 * 		ContextInfo.recoverQuery();
 *		DetachedCriteria dc=DetachedCriteria.forClass(TblHrEmpTraReg.class)
 *	    .add(Restrictions.eq("delFlg", "0"));
 *		AutoConditionsUtil.AddAliasFromConditionInfo(dc);
 *		return this.hibernateTemplate.findByCriteria(dc);
 * </code>
 * @author yushn
 * @since 2007-10-12
 */
public class AutoConditionsUtil implements ConditionConstants{
	private static transient Log logger = LogFactory.getLog(AutoConditionsUtil.class);

	public static DetachedCriteria AddAliasFromConditionInfo(DetachedCriteria dc)
	{
		//20071213 PHRXLJY00004 START
		if(ContextInfo.isConcealQuery())
			return null;
		//20071213 PHRXLJY00004 END
		if(null == dc) return null;
		Condition[] cs = ContextInfo.getContextCondition().getAppendConditions();
		if(cs.length==0) {
            ContextInfo.setAliasSet(new HashSet());
            return dc;
        }
		
		//Set as = new HashSet(); 
        Set as = ContextInfo.getAliasSet(); 
		Set oas = new HashSet(); 
		List cslist = new ArrayList();
		for(int i=0;i<cs.length;i++)
		{
			Condition con = cs[i];
			if(null == con ) continue;

			if(StringUtils.isBlank(con.getName()))
			{
				cslist.add(con);
				continue;
			}

			String name = con.getName();
			if(name.indexOf(PROPERTY_SEPARATOR)!=name.lastIndexOf(PROPERTY_SEPARATOR))
			{
				
				if(con.getValue()==null)
					continue;
				if (String.class.isInstance(con.getValue())) {
					String value = (String)con.getValue();
					if(value.trim().length() == 0)
						continue;
				}
				con.setCreateAlias(false);
				int lastindex = name.lastIndexOf(PROPERTY_SEPARATOR);
				if(name.indexOf(".code")==name.length()-5)
					lastindex=name.substring(0,lastindex).lastIndexOf(PROPERTY_SEPARATOR);
				String aliasName = createAliase(dc,as,oas,name.substring(0,lastindex));
				String propertyName = aliasName+name.substring(lastindex);
				
				//构造查询条件
				try {	
					Constructor constructor = SimpleExpression.class.getDeclaredConstructor(new Class[] {String.class, Object.class, String.class});
					constructor.setAccessible(true);
					
					String operator=con.getOperator();
					if (operator == null) {
						operator = EQUAL;
					}
					Object value = con.getValue();
					// 截去两端的空格
					if (String.class.isInstance(value)) {
						value = ((String) value).trim();
					}
					if (LIKE.equals(operator)) {
						value = "%" + value + "%";
					}
					operator = " " + operator + " ";
					
					SimpleExpression expression = 
						(SimpleExpression) constructor.newInstance(
								new Object[] {propertyName, value, operator}
								);					
					dc.add(expression);
				} catch (Exception e) {
					if (logger.isErrorEnabled()) {
						logger.error("动态构造查询条件出错 : " + e);
					}
					throw new RuntimeException("动态构造查询条件出错", e);
				} 	

				
			}
			else
			{
				if(name.indexOf(PROPERTY_SEPARATOR)!=-1)
				{
					String aliase=name.substring(0,name.indexOf(PROPERTY_SEPARATOR));
					if(as.contains(aliase))
					{
						con.setCreateAlias(false);
						
					}
					else
					{
						oas.add(aliase);
					}
				}
				cslist.add(con);
			}
		}
        ContextInfo.setAliasSet(new HashSet());
        Condition[] cs2 = new Condition[cslist.size()];
		for(int i=0;i<cs2.length;i++)
		{
            Condition cd = (Condition)cslist.get(i);
			String name = cd.getName();
			if(StringUtils.isBlank(name))
			{
				cs2[i] = cd;
				continue;
			}
            int lastindex = name.lastIndexOf(PROPERTY_SEPARATOR);
            if(name.indexOf(".code")==name.length()-5 && lastindex > 0) {
                name=name.substring(0,lastindex);
            }
            if(name.indexOf(PROPERTY_SEPARATOR)!=-1) {
                String aliase=name.substring(0,name.indexOf(PROPERTY_SEPARATOR));
                if(as.contains(aliase))
                {
                    cd.setCreateAlias(false);
                }
            }
            cs2[i] = cd;
		}
		ContextInfo.getContextCondition().setAppendConditions(cs2);
		return null;
	}
	/**
	 * 递归方式分层创建alias,已创建过的不再创建
	 * @param dc
	 * @param as
	 * @param path
	 * @return alias名称
	 */
	private static String createAliase(DetachedCriteria dc,Set as ,Set oas ,String path)
	{
		int lastpos = path.lastIndexOf(PROPERTY_SEPARATOR);
		String aliasName = null;
		if(lastpos > -1)
		{
			aliasName = path.substring(lastpos+1);
			if(as.contains(aliasName)||oas.contains(aliasName))
				return aliasName;
			createAliase(dc,as,oas,path.substring(0, lastpos));
			dc.createAlias(path, aliasName);
		}
		else
		{
			aliasName = path;
			if(as.contains(path))
				return path;
			dc.createAlias(path,aliasName);
		}
		as.add(aliasName);
		return aliasName;
	}

}
