package net.silencily.sailing.hibernate3;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.exception.DataAccessDenyException;
import net.silencily.sailing.framework.codename.DepartmentCodeName;
import net.silencily.sailing.framework.codename.UserCodeName;
import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.framework.persistent.hibernate3.EnhancedHibernateTemplate;
import net.silencily.sailing.security.SecurityContextInfo;
import net.silencily.sailing.security.model.CmnEntity;
import net.silencily.sailing.security.model.CmnEntityMember;
import net.silencily.sailing.security.model.CurrentUser;
import net.silencily.sailing.security.model.UserEntityMember;
import net.silencily.sailing.utils.MessageInfo;
import net.silencily.sailing.utils.Tools;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.impl.CriteriaImpl;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;
import org.springframework.orm.hibernate3.HibernateTemplate;


/**
 * @author zhaoyf
 * 
 */
public class EnhancehibernateTemplatePlus extends EnhancedHibernateTemplate {

	/*
	 * public List findByCriteria(DetachedCriteria criteria) throws
	 * DataAccessException { // TODO Auto-generated method stub
	 * AutoConditionsUtil.AddAliasFromConditionInfo(criteria); return
	 * super.findByCriteria(criteria); }
	 */

	// 关于权限过滤代码
	
	@SuppressWarnings("rawtypes")
	public List findByCriteria(DetachedCriteria criteria)
			throws DataAccessException {

		List result = null;
		
//		// 获取参数配置
//		String mainTableClassName = null;
//		try {
//			mainTableClassName = SecurityContextInfo.getMainTableClassName();
//		} catch (NullPointerException e) {
//			mainTableClassName = null;
//		}
//		
//		// 此特殊通道是必须的，否则会导致基础编码查不出来的情况
//		if (null == mainTableClassName || "".equals(mainTableClassName)) {
//			AutoConditionsUtil.AddAliasFromConditionInfo(criteria);
//			result = super.findByCriteria(criteria);
//			if (null == result) {
//				return (new ArrayList(0));
//			} else {
//				return result;
//			}
//		}

		// 判断SecurityContextInfo中mainTableClassName与DetachedCriteria中传入的CLASs参数
		// 如果相等，进行过滤操作，不相等，不过滤
		// 得到当前的SESSION
		Session ss = null;
		HibernateTemplate hibernateTemplate = (HibernateTemplate) ServiceProvider
				.getService("common.hibernateTemplate");
		ss = hibernateTemplate.getSessionFactory().getCurrentSession();
		String className = ((CriteriaImpl) criteria.getExecutableCriteria(ss))
				.getEntityOrClassName();
		String cName = className;
		if ((!("".equals(className))) && (null != className)) {
			// 截断String,去掉包名
			int indexTemp = className.lastIndexOf(".");
			className = className.substring(indexTemp + 1);
		}

		HttpSession session = SecurityContextInfo.getSession();
		CurrentUser currentUser = (CurrentUser) session
				.getAttribute("currentUser");
		if (currentUser == null) {
		    return super.findByCriteria(criteria);
		}
		// 当前用户ID
		String userId = currentUser.getEmpCd();
		// 当前用户部门ID及子部门ID
		String stepID = currentUser.getDeptId();
		HashSet stepIDS = currentUser.getSubDeptIds();
		// 将当前部门添加到部门集合中
		stepIDS.add(stepID);
		// 当前的URL
		String url = SecurityContextInfo.getCurrentPageUrl();
		// 当前数据访问级别判断
		HashMap urlPermissions = currentUser.getUrlPermissions();
		// 这有个BUG,调用以后,基础编码表数据出不来
//		int urlType = ((UrlPermission) urlPermissions.get(url))
//				.getDataAccessLevelRead();
//		// 添加过滤条件
//		if ((!("".equals(mainTableClassName))) && (null != mainTableClassName)
//				&& inArray(mainTableClassName, className)) {
//			// 根据数据访问级别进行处理
//			switch (urlType) {
//			case DataAccessType.DEPT:
//				// 部门级别，添加当前用户的部门列表到查询条件
//				criteria.add(Restrictions.in("creatorDept", stepIDS));
//				//AutoConditionsUtil.AddAliasFromConditionInfo(criteria);
//				break;
//			case DataAccessType.SELF:
//				// 个人级别，添加当前用户的ID为创建人ID
//				criteria.add(Restrictions.eq("creator", userId));
//				// 个人级别，添加当前用户的stepid为创建人creatorDept
//				criteria.add(Restrictions.eq("creatorDept", stepID));
//				//AutoConditionsUtil.AddAliasFromConditionInfo(criteria);
//				break;
//			case DataAccessType.FORBID:
//				return (new ArrayList(0));
//			default:
//				break;
//			}
//		} 
		
		//Map<String, CmnEntity> dataSecurityMap = SecurityContextInfo.getDataSecurity();
		Map<String, CmnEntity> dataSecurityMap = currentUser.getDataSecurityMap();
		CmnEntity cmnEntity = null;
		if (dataSecurityMap != null) {
			if (dataSecurityMap.containsKey(cName)) {
				cmnEntity = dataSecurityMap.get(cName);
				filterData(cmnEntity, criteria, 'S');
			}
		}
		
		AutoConditionsUtil.AddAliasFromConditionInfo(criteria);
		// 根据条件进行查询
		try {
			result = super.findByCriteria(criteria);

		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		if (null == result) {
			result = new ArrayList(0);
		}


		// 遍历查询结果，得到相关权限列表，并保存到SecurityContextInfo
		if (null != result) {
			for (int i = 0; i < result.size(); i++) {
				// 查询当前记录的创建人的DEPTID和USERID 和PK
				String creatorDeptId = null;
				String creatorId = null;
				String dataId = null;
				int type = 0;
				if ((result.toArray()[i]) instanceof EntityPlus) {
					EntityPlus entityPlus = (EntityPlus) (result.toArray()[i]);
					dataId = entityPlus.getId();
					creatorDeptId = entityPlus.getCreatorDept().getCode();
					creatorId = entityPlus.getCreator().getCode();
					// 将该记录的ID，和权限设置到SecurityContextInfo中的rwCtrlTypeList中，ID为KEY值。
					type = ((CurrentUser) session.getAttribute("currentUser"))
							.getRowRWCtrlType(url, creatorDeptId, creatorId);
				    if (dataSecurityMap != null) {
                        if (dataSecurityMap.containsKey(cName)) {
                            entityPlus.setDataAccessLevelD(getDataAccessLevel(entityPlus, 'D'));
                        }
                    }
					
					
				}
				// 判断当前的MAP中是否存在当前数据项目的的KEY值
				if (null != SecurityContextInfo.getRwCtrlTypeMap()) {
					Map temp = new HashMap();
					temp = SecurityContextInfo.getRwCtrlTypeMap();
					if (temp.containsKey(dataId)) {
						temp.remove(dataId);
					}
					temp.put(dataId, new Integer(type));
					SecurityContextInfo.setRwCtrlTypeMap(temp);
				} else {
					Map temp = new HashMap();
					temp.put(dataId, new Integer(type));
					SecurityContextInfo.setRwCtrlTypeMap(temp);
				}
				
			}
		}
		// 返回
		return result;
	}
	/*
	private void setDataAccessLevel(Object o) {
		if (o instanceof EntityPlus) {
			try {
				((EntityPlus)o).setDataAccessLevelC(getDataAccessLevel(o, 'C'));
				((EntityPlus)o).setDataAccessLevelU(getDataAccessLevel(o, 'U'));
				((EntityPlus)o).setDataAccessLevelD(getDataAccessLevel(o, 'D'));
				((EntityPlus)o).setDataAccessLevelS(getDataAccessLevel(o, 'S'));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	*/
	public boolean getDataAccessLevel(Object o, char type) {
		boolean flag = true;
		if (o instanceof EntityPlus) {
			EntityPlus entity = (EntityPlus)o;
		     HttpSession session = SecurityContextInfo.getSession();
		        CurrentUser currentUser = (CurrentUser) session
		                .getAttribute("currentUser");
			Map<String, CmnEntity> dataSecurityMap = null;
			if (currentUser != null) {
			    dataSecurityMap = currentUser.getDataSecurityMap();
			}
			CmnEntity cmnEntity = null;
			if (dataSecurityMap != null) {
				if (dataSecurityMap.containsKey(entity.getClass().getName())) {
					cmnEntity = dataSecurityMap.get(entity.getClass().getName());
					for (CmnEntityMember entityMember : cmnEntity.getEntityMembers()) {
						UserEntityMember userEntityMember = entityMember.getUserEntityMemberId();
						if (StringUtils.isBlank(userEntityMember.getValue(type))) {
							flag = true;
							continue;
						}
//						String getMethod = "get" + entityMember.getName().toUpperCase().charAt(0) + entityMember.getName().substring(1);
//						Method m = entity.getClass().getMethod(getMethod, null);
//						Object objDataValue = m.invoke(entity, null);
						Object objDataValue = "";
						try {
							objDataValue = PropertyUtils.getProperty(entity, entityMember.getName());
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						Object objCondition = getValue(userEntityMember.getValue(type), entityMember, type);;
						
						if (StringUtils.isBlank(entityMember.getChildrenNode())) {							
							String classType = entityMember.getType();
							String oper = userEntityMember.getOper(type);
							if (classType.endsWith("String")) {
								flag = ((String)objDataValue).equals(objCondition);
							} else if (classType.endsWith("Integer") || classType.endsWith("Double")
									|| classType.endsWith("Date")) {
								try {
									int i = -1;
									if (classType.endsWith("Integer")) {
										i = ((Integer)objDataValue).compareTo((Integer)objCondition);
									} else if (classType.endsWith("Double")){
										i = ((Double)objDataValue).compareTo((Double)objCondition);
									} else if (classType.endsWith("Date")){
										i = ((Date)objDataValue).compareTo((Date)objCondition);
									}									
									if (EQUAL.equals(oper)) {
										flag = i == 0;
									} else if (NOT_EQUAL.equals(oper)) {
										flag = i != 0;
									} else if (GREATER_EQUAL.equals(oper)) {
										flag = i >= 0;
									} else if (GREATER.equals(oper)) {
										flag = i > 0;
									} else if (LESS_EQUAL.equals(oper)) {
										flag = i <= 0;
									} else if (LESS.equals(oper)) {
										flag = i < 0;
									}	
								} catch (NumberFormatException e) {
									e.printStackTrace();
								}								
							} else if(classType.endsWith("UserCodeName")) {								
								String strId = "";
								if (objDataValue.getClass().getName().endsWith("UserCodeName")) {
									strId = ((UserCodeName)objDataValue).getCode();
								}
								flag = strId.equals(objCondition);
							} else if(classType.endsWith("CodeWrapperPlus")) {
							    flag = ((CodeWrapperPlus)objDataValue).getCode().equals(objCondition);
							}
						} else {
							if (objCondition instanceof Set) {
								if (((Set)objCondition).isEmpty()) {
									flag = true;
								} else {
									String strId = "";
//									if (objDataValue.getClass().getName().endsWith("UserCodeName")) {
//										strId = ((UserCodeName)objDataValue).getCode();
//									} else 
									if (objDataValue.getClass().getName().endsWith("DepartmentCodeName")) {
										strId = ((DepartmentCodeName)objDataValue).getId();
									} else if (objDataValue instanceof EntityPlus) {
										strId = ((EntityPlus)objDataValue).getId();
									}
									flag = ((Set)objCondition).contains(strId);
								}
							}
						}						
						if (!flag) {
							break;
						}
					}
				}
			}
		}
		return flag;
	}
//	public Set<String> findByDataAccessLevelFiltered(String classType, char type) throws Exception{		
//		DetachedCriteria criteria = DetachedCriteria.forClass(Class.forName(classType));
//		Map<String, CmnEntity> dataSecurityMap = SecurityContextInfo.getDataSecurity();
//		CmnEntity cmnEntity = null;
//		Set<String> filteredIds = new HashSet<String>(0);
//		List list = null;
//		if (dataSecurityMap != null) {
//			if (dataSecurityMap.containsKey(classType)) {
//				cmnEntity = dataSecurityMap.get(classType);
//				filterData(cmnEntity, criteria, type);
//				criteria.add(Restrictions.eq("delFlg", "0"));
//			}
//		}
//		list = super.findByCriteria(criteria);
//		for (Object o : list) {
//			if (o instanceof EntityPlus) {
//				filteredIds.add(((EntityPlus)o).getId());
//			}
//		}
//		return filteredIds;
//	}
	
	/**
	 * 根据类型和ID获取类对象
	 * @param type
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private Object loadObject(String type, String id) throws Exception {
		
		if (id.startsWith("$")) {
			// 拼装过滤字符串
			HttpSession session = SecurityContextInfo.getSession();
			CurrentUser currentUser = (CurrentUser) session.getAttribute("currentUser");
			if (id.equals("$CurrentDept")) {
				id = currentUser.getDeptId();
			}
		}
//		if (type.endsWith("UserCodeName")) {
//			type = "com.qware.hr.domain.TblHrEmpInfo";
//			DetachedCriteria criteria = DetachedCriteria.forClass(Class.forName(type));
//			criteria.add(Restrictions.eq("empCd", id));
//			criteria.add(Restrictions.eq("delFlg", "0"));
//			List list = super.findByCriteria(criteria);
//			if (list.isEmpty()) {
//				return null;
//			} else {
//				return list.get(0);
//			}
//			
//		} else 
		if (type.endsWith("DepartmentCodeName")) {
			type = "com.qware.hr.domain.TblHrDept";			
		}
		return super.load(Class.forName(type), id);
	}
	/**
	 * 获取树节点型的所有子节点IDs
	 * @param pObj
	 * @param childrenNode
	 * @return
	 * @throws Exception
	 */
	private Set<String> getSubIds(Object pObj, String childrenNode) throws Exception{
		Set<String> sub = new HashSet<String>(0);
		if (pObj instanceof EntityPlus && "0".equals(((EntityPlus)pObj).getDelFlg())) {			
//			if (pObj instanceof UserCodeName) {
//				sub.add(((UserCodeName)pObj).getCode());
//			} else {
				sub.add(((EntityPlus)pObj).getId());
//			}
			String getMethod = "get" + childrenNode.toUpperCase().charAt(0) + childrenNode.substring(1);
			Method method = pObj.getClass().getMethod(getMethod, null);
			Object obj = method.invoke(pObj, null);
			if (obj instanceof Collection) {
				Collection c = (Collection)obj;
				if (c.isEmpty()) {
					return sub;
				} else {
					Iterator it = c.iterator();
					if (it.hasNext()) {
						Object o = it.next();
						sub.addAll(getSubIds(o, childrenNode));
					}
				}
			}
		}
		return sub;
	}

	/**
	 * 添加过滤条件
	 * @param cmnEntity 实体BEAN
	 * @param criteria 查询条件
	 * @param type 增:C; 删:D; 改:U; 查:S
	 */
	private void filterData(CmnEntity cmnEntity, DetachedCriteria criteria, char type) {
		for (CmnEntityMember bean : cmnEntity.getEntityMembers()) {
			setCriteria(criteria, bean, type);
		}
	}
	/**
	 * 添加过滤条件
	 * @param criteria 查询条件
	 * @param bean 实体成员BEAN
	 * @param type 增:C; 删:D; 改:U; 查:S
	 */
	private void setCriteria(DetachedCriteria criteria, CmnEntityMember bean, char type) {
		UserEntityMember uem = bean.getUserEntityMemberId();
		String oper = uem.getOper(type);
		String value = uem.getValue(type);
		String attri = bean.getName();
		if (StringUtils.isBlank(value)) {
			return;
		}
		Object objValue = getValue(value, bean, type);

		if (!StringUtils.isBlank(bean.getChildrenNode())) {	
			if (bean.getType().endsWith("CodeName")) {
				criteria.add(Restrictions.in(attri, (Set)objValue));
			} else {
				criteria.add(Restrictions.in(attri + ".id", (Set)objValue));
			}
		} else {
			if (bean.getType().endsWith("CodeName")) {
				criteria.add(Restrictions.in(attri, (Set)objValue));
				return;
			} 
			if (EQUAL.equals(oper)) {
				criteria.add(Restrictions.eq(attri, objValue));
			} else if (NOT_EQUAL.equals(oper)) {
				criteria.add(Restrictions.ne(attri, objValue));
			} else if (GREATER_EQUAL.equals(oper)) {
				criteria.add(Restrictions.ge(attri, objValue));
			} else if (GREATER.equals(oper)) {
				criteria.add(Restrictions.gt(attri, objValue));
			} else if (LESS_EQUAL.equals(oper)) {
				criteria.add(Restrictions.le(attri, objValue));
			} else if (LESS.equals(oper)) {
				criteria.add(Restrictions.lt(attri, objValue));
			} else {
				criteria.add(Restrictions.eq(attri, objValue));
			}
		}
		
		
		
	}

	/**
	 * 将字符串按指定的类型转化
	 * @param value 要转化的字符串
	 * @param bean 实体成员BEAN
	 * @param type 增:C; 删:D; 改:U; 查:S
	 * @return 按指定的类型转化后的对象
	 */
	private Object getValue(String value, CmnEntityMember bean, char type) {
		Object obj = null;
		String classType = bean.getType();
		if (StringUtils.isNotBlank(bean.getChildrenNode())) {
			//obj = bean.getUserEntityMemberId().getSubIds(type);
			//if (((Set)obj).isEmpty()) {
				//obj = getSubIds(bean.getType(), value, bean.getChildrenNode());
				try {
					//数据是一组以","为分隔的ID
					String[] str = value.split(",");
					Set<String> set = new HashSet<String>();
					for (String s : str) {
						set.addAll(getSubIds(loadObject(bean.getType(), s.trim()), bean.getChildrenNode()));
					}
					obj = set;
				} catch (Exception e) {
					e.printStackTrace();
					obj = new HashSet(0);
				}
				bean.getUserEntityMemberId().getSubIds(type).clear();
				bean.getUserEntityMemberId().getSubIds(type).addAll((Set)obj);
			//}
		} else {
			if (classType.endsWith("String")) {
				obj = value;
			} else if (classType.endsWith("Integer")) {
				try {
					obj = Integer.valueOf(value);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			} else if (classType.endsWith("Date")) {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-dd-MM");
				try {
					obj = df.parse(value);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else if (classType.endsWith("Double")){
				try {
					obj = Double.valueOf(value);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			} else if (classType.endsWith("UserCodeName")) {
				Set<String> set = new HashSet<String>();
				String[] str = value.split(",");
				for (String s : str) {
					if (s.trim().equals("$CurrentUser")) {
						HttpSession session = SecurityContextInfo.getSession();
						CurrentUser currentUser = (CurrentUser) session.getAttribute("currentUser");
						set.add(currentUser.getEmpCd());
					} else {
						set.add(s.trim());
					}				
				}
				obj = set;
			} else if (classType.endsWith("CodeWrapperPlus")) {
			    obj = value;
			}
		}
		return obj;	
		
	}
	
	private boolean inArray(String a, String b) {
		String[] temp = a.split(",");
		for (int i = 0; i < temp.length; i++) {
			if (b.equals(temp[i])) {
				return true;
			}
		}
		return false;
	}

	// 注意点1：注意得到的hqls不为空的话，前面必须加上" and "
	// 注意点2：当数据访问级别为DataAccessType.FORBID时，将抛出一个异常

//	public static String getFilterString(String tableName) throws Exception {
//		// 定义返回的字符串
//		String hqls = null;
//		// 拼装过滤字符串
//		HttpSession session = SecurityContextInfo.getSession();
//		CurrentUser currentUser = (CurrentUser) session
//				.getAttribute("currentUser");
//		// 当前用户ID
//		String userId = currentUser.getEmpCd();
//		// 当前用户部门ID及子部门ID
//		String stepID = currentUser.getDeptId();
//		HashSet stepIDS = currentUser.getSubDeptIds();
//		// 将当前部门添加到部门集合中
//		stepIDS.add(stepID);
//		// 当前的URL
//		String url = SecurityContextInfo.getCurrentPageUrl();
//		// 当前数据访问级别判断
//		HashMap urlPermissions = currentUser.getUrlPermissions();
//		// 这有个BUG,调用以后,基础编码表数据出不来
//		int urlType = ((UrlPermission) urlPermissions.get(url))
//				.getDataAccessLevelRead();
//		switch (urlType) {
//		case DataAccessType.DEPT:
//			// 部门级别，添加当前用户的部门列表到查询条件
//			// 拼装集合字符串
//			String temp = " ";
//			for (int i = 0; i < stepIDS.toArray().length; i++) {
//				if (i == 0) {
//					temp = temp + " ' " + (String) (stepIDS.toArray()[i])
//							+ " ' ";
//				} else {
//					temp = temp + " , ' " + (String) (stepIDS.toArray()[i])
//							+ " ' ";
//				}
//			}
//			// 拼装HQL
//			hqls = tableName + "." + "creatorDept IN ( " + temp + " )";
//			return hqls;
//		case DataAccessType.SELF:
//			// 个人级别，添加当前用户的ID为创建人ID，添加当前用户的stepid为创建人creatorDept
//			hqls = tableName + "." + "CREATOR = " + userId + " " + tableName
//					+ "." + "CREATOR_DEPT = " + stepID + " ";
//			return hqls;
//		case DataAccessType.FORBID:
//			// 异常的类型有带修改
//			throw (new Exception());
//		default:
//			return hqls;
//		}
//
//	}

	/**
	 * 
	 * 功能描述：判断新增时关键字段的数值在数据表中的唯一性,可以插入返回TREU。传如的MAP的KEY值为映射实体类的字段名，VALUE为需要做判断的值。
	 * 逻辑删除后的数据行也不在此范围。
	 * 
	 * @param 实体类Class
	 *            关键字段MAP集合 hibernateTemplate
	 * @return 2007-01-22 下午13:06:14
	 * @version 1.0
	 * @author wenjb
	 */
	public static boolean insertDataUniqueCheck(
			HibernateTemplate hibernateTemplate, Class clazz, Map fieldValueMap) {
		ContextInfo.recoverQuery();
		DetachedCriteria dc = DetachedCriteria.forClass(clazz);
		// 没有数据的验证，返回FALSE
		if (null == fieldValueMap || 0 == fieldValueMap.size()) {
			fieldValueMap = new HashMap(0);
			return false;
		}
		Object key[] = fieldValueMap.keySet().toArray();
		for (int i = 0; i < key.length; i++) {
			String keyTemp = (String) key[i];
			dc.add(Restrictions.eq(keyTemp, fieldValueMap.get(keyTemp)));
		}
		dc.add(Restrictions.eq("delFlg", "0"));
		// 返回处理
		List result = hibernateTemplate.findByCriteria(dc);
		if (null == result || 0 == result.size()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * 功能描述：判断更新时关键字段的数值在数据表中的唯一性,可以更新返回TREU。传如的MAP的KEY值为映射实体类的字段名，VALUE为需要做判断的值。
	 * 注意此时的比较范围不包含此数据行,逻辑删除后的数据行也不在此范围
	 * 
	 * @param 实体类Class
	 *            关键字段MAP集合 hibernateTemplate 当前数据行的ID
	 * @return 2007-01-22 下午13:06:14
	 * @version 1.0
	 * @author wenjb
	 */
	public static boolean updateDataUniqueCheck(
			HibernateTemplate hibernateTemplate, Class clazz,
			Map fieldValueMap, String id) {
		ContextInfo.recoverQuery();
		DetachedCriteria dc = DetachedCriteria.forClass(clazz);
		// 没有数据的验证，返回FALSE
		if (null == fieldValueMap || 0 == fieldValueMap.size()) {
			fieldValueMap = new HashMap(0);
			return false;
		}
		Object key[] = fieldValueMap.keySet().toArray();
		for (int i = 0; i < key.length; i++) {
			String keyTemp = (String) key[i];
			dc.add(Restrictions.eq(keyTemp, fieldValueMap.get(keyTemp)));
		}
		dc.add(Restrictions.ne("id", id));
		dc.add(Restrictions.eq("delFlg", "0"));
		// 返回处理
		List result = hibernateTemplate.findByCriteria(dc);
		if (null == result || 0 == result.size()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 功能描述 处理乐观锁定出现的异常,更改异常报错信息
	 * 
	 * @return 2008-2-23 下午04:55:32
	 * @version 1.0
	 * @author wenjb
	 */
	public void flush() throws DataAccessException {
		try {
			super.flush();
		} catch (HibernateOptimisticLockingFailureException e) {
			String errorMsg = MessageInfo.factory().getMessage("CM_I043_C_0");
			//String errorMsg = "您要保存的数据版本已经陈旧，可能有其他用户在您打开本画面之后更改了数据。请从列表画面重新打开本画面，再尝试本次操作！";
			throw new RuntimeException(errorMsg);
		}
	}

	/**
	 * 功能描述 更新和删除时候，判断是否有权限进行更新
	 * 
	 * @return 2008-2-23 下午04:55:32
	 * @version 1.0
	 * @author wenjb
	 */	
	 public void update(Object entity) throws DataAccessException {
		if(SystemPermissionCheckImpl.systemNeedPermissionCheck()){
			if (entity instanceof EntityPlus) {
				batchDeletePermission();
				EntityPlus entityPlus = (EntityPlus) entity;
				int rwCtrlType = entityPlus.getRwCtrlType();
				//setDataAccessLevel(entityPlus);
				//有权限进行删除的情况
				//if(RWCtrlType.EDIT == rwCtrlType){
					if (StringUtils.isBlank(entityPlus.getId())) {	
						
						entityPlus.setDataAccessLevelC(getDataAccessLevel(entityPlus, 'C'));
						
						if (entityPlus.getDataAccessLevelC()) {							
							super.update(entity);
							return;
						} else {
							throw new DataAccessDenyException("您没有权限创建此数据。");	
						}
					} else {
						if (entityPlus.getDelFlg().equals("1")) {
							entityPlus.setDataAccessLevelD(getDataAccessLevel(entityPlus, 'D'));
							if (entityPlus.getDataAccessLevelD()) {
								super.update(entity);
								return;
							} else {
								throw new DataAccessDenyException("您没有权限删除此数据。");	
							}
						} else {
							entityPlus.setDataAccessLevelU(getDataAccessLevel(entityPlus, 'U'));
							if (entityPlus.getDataAccessLevelU()) {
								super.update(entity);
								return;
							} else {
								throw new DataAccessDenyException("您没有权限修改此数据。");	
							}
						}
					}
				}
				//else{
					//无权限的情况下，抛出异常
					String errorMsg = MessageInfo.factory().getMessage("CM_I045_C_0");
					//String errorMsg = "您没有权限修改或者删除此数据。";
					throw new RuntimeException(errorMsg);			
				//}
			//}
		}
		//不需要权限处理的情况
		super.update(entity);
		return;
	 }
	 public void saveOrUpdate(Object entity)throws DataAccessException {
		if(SystemPermissionCheckImpl.systemNeedPermissionCheck()){
			if (entity instanceof EntityPlus) {
				batchDeletePermission();
				EntityPlus entityPlus = (EntityPlus) entity;
				int rwCtrlType = entityPlus.getRwCtrlType();
				//setDataAccessLevel(entityPlus);
				//有权限进行删除的情况
				//if(RWCtrlType.EDIT == rwCtrlType){
					if (StringUtils.isBlank(entityPlus.getId())) {
						
						entityPlus.setDataAccessLevelC(getDataAccessLevel(entityPlus, 'C'));
						
						if (entityPlus.getDataAccessLevelC()) {
							super.saveOrUpdate(entity);
							return;
						} else {
							throw new DataAccessDenyException("您没有权限创建此数据。");	
						}
					} else {
						if (entityPlus.getDelFlg().equals("1")) {
							entityPlus.setDataAccessLevelD(getDataAccessLevel(entityPlus, 'D'));
							if (entityPlus.getDataAccessLevelD()) {
								super.saveOrUpdate(entity);
								return;
							} else {
								throw new DataAccessDenyException("您没有权限删除此数据。");	
							}
						} else {
							entityPlus.setDataAccessLevelU(getDataAccessLevel(entityPlus, 'U'));
							if (entityPlus.getDataAccessLevelU()) {
								super.saveOrUpdate(entity);
								return;
							} else {
								throw new DataAccessDenyException("您没有权限修改此数据。");	
							}
						}
					}
				}
				//else{
					//无权限的情况下，抛出异常
//					String errorMsg = MessageInfo.factory().getMessage("CM_I045_C_0");
//					//String errorMsg = "您没有权限修改或者删除此数据。";
//					throw new DataAccessDenyException(errorMsg);			
				//}
				
			//}
		}
		//不需要权限处理的情况
		super.saveOrUpdate(entity);
		return;
	 }
	 public void delete(Object entity)throws DataAccessException {
		if(SystemPermissionCheckImpl.systemNeedPermissionCheck()){
			if (entity instanceof EntityPlus) {
				batchDeletePermission();
				EntityPlus entityPlus = (EntityPlus) entity;
				//setDataAccessLevel(entityPlus);
				
				entityPlus.setDataAccessLevelD(getDataAccessLevel(entityPlus, 'D'));
				
				int rwCtrlType = entityPlus.getRwCtrlType();
				//有权限进行删除的情况
				//if(RWCtrlType.EDIT == rwCtrlType) {
				if(entityPlus.getDataAccessLevelD()){
					super.delete(entity);
					return;
				} else {
					throw new DataAccessDenyException("您没有权限删除此数据。");	
				}
			}
		}
		//不需要权限处理的情况
		super.delete(entity);
		return;
	 }
	 
	 //需求变更，当读写权限为只读的时候，不能进行删除和批量删除
	 private void batchDeletePermission(){
		 //需求变更，增加新增的处理
		int pageRWCtrlType = 2;
		try {
				// 根据URL默认读写控制权限
				CurrentUser currentUser = SecurityContextInfo.getCurrentUser();
				String url = SecurityContextInfo.getCurrentPageUrl();
				pageRWCtrlType = currentUser.getPageDefaultRWCtrlType(url);	
				if(StringUtils.isBlank(url)){
					pageRWCtrlType = 2;
				}
		} catch (Exception e) {
				pageRWCtrlType = 2;
		}
		
		 if(1 == pageRWCtrlType){
			 //无权限的情况下，抛出异常
			String errorMsgTemp = MessageInfo.factory().getMessage("CM_I045_C_0");
			//String errorMsg = "您没有权限修改或者删除此数据。";
			throw new DataAccessDenyException(errorMsgTemp);	
		 }
	 }
	 public Object load(Class entityClass, Serializable id)
     throws DataAccessException{
		 Object loadTemp = load(entityClass, id, null);
		 try
		 {
			 HttpSession session = SecurityContextInfo.getSession();
			 String loadByTag = session.getAttribute("loadByTag")==null?"":(String)session.getAttribute("loadByTag");
			 if(Class.forName("net.silencily.sailing.basic.wf.WorkflowInfo").isAssignableFrom(entityClass)&&"".equals(loadByTag))
		     {
		     	((EntityPlus)loadTemp).setReflectCopyEntityProperties(EntityPlus.isReflectCopyEntity);
		         Object temp = entityClass.newInstance();
		         //BeanUtils.copyProperties(temp, loadTemp);
		         temp = Tools.depthClone(loadTemp);
		         String idTemp = (String)id;
		         Map historyInfo = session.getAttribute("workFlowHistoryInfo") == null ? 
		        		 			new HashMap(0) : (Map)session.getAttribute("workFlowHistoryInfo");
		         if(historyInfo.containsKey(idTemp)){
		        	 historyInfo.remove(idTemp);
		         }
		         historyInfo.put(idTemp, temp);
		         session.removeAttribute("workFlowHistoryInfo");
		         session.setAttribute("workFlowHistoryInfo", historyInfo);
		         ((EntityPlus)loadTemp).setReflectCopyEntityProperties(EntityPlus.isNotReflectCopyEntity);
		     
		     }
			 ((EntityPlus)loadTemp).setDataAccessLevelU(getDataAccessLevel(loadTemp, 'U'));
		 }
		 catch(Exception e)
		 {
		     e.printStackTrace();
		 }
		 //setDataAccessLevel(loadTemp);		 
		 return loadTemp;
	 }

}
