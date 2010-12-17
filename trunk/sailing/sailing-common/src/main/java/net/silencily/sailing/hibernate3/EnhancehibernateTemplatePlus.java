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

	// ����Ȩ�޹��˴���
	
	@SuppressWarnings("rawtypes")
	public List findByCriteria(DetachedCriteria criteria)
			throws DataAccessException {

		List result = null;
		
//		// ��ȡ��������
//		String mainTableClassName = null;
//		try {
//			mainTableClassName = SecurityContextInfo.getMainTableClassName();
//		} catch (NullPointerException e) {
//			mainTableClassName = null;
//		}
//		
//		// ������ͨ���Ǳ���ģ�����ᵼ�»�������鲻���������
//		if (null == mainTableClassName || "".equals(mainTableClassName)) {
//			AutoConditionsUtil.AddAliasFromConditionInfo(criteria);
//			result = super.findByCriteria(criteria);
//			if (null == result) {
//				return (new ArrayList(0));
//			} else {
//				return result;
//			}
//		}

		// �ж�SecurityContextInfo��mainTableClassName��DetachedCriteria�д����CLASs����
		// �����ȣ����й��˲���������ȣ�������
		// �õ���ǰ��SESSION
		Session ss = null;
		HibernateTemplate hibernateTemplate = (HibernateTemplate) ServiceProvider
				.getService("common.hibernateTemplate");
		ss = hibernateTemplate.getSessionFactory().getCurrentSession();
		String className = ((CriteriaImpl) criteria.getExecutableCriteria(ss))
				.getEntityOrClassName();
		String cName = className;
		if ((!("".equals(className))) && (null != className)) {
			// �ض�String,ȥ������
			int indexTemp = className.lastIndexOf(".");
			className = className.substring(indexTemp + 1);
		}

		HttpSession session = SecurityContextInfo.getSession();
		CurrentUser currentUser = (CurrentUser) session
				.getAttribute("currentUser");
		if (currentUser == null) {
		    return super.findByCriteria(criteria);
		}
		// ��ǰ�û�ID
		String userId = currentUser.getEmpCd();
		// ��ǰ�û�����ID���Ӳ���ID
		String stepID = currentUser.getDeptId();
		HashSet stepIDS = currentUser.getSubDeptIds();
		// ����ǰ������ӵ����ż�����
		stepIDS.add(stepID);
		// ��ǰ��URL
		String url = SecurityContextInfo.getCurrentPageUrl();
		// ��ǰ���ݷ��ʼ����ж�
		HashMap urlPermissions = currentUser.getUrlPermissions();
		// ���и�BUG,�����Ժ�,������������ݳ�����
//		int urlType = ((UrlPermission) urlPermissions.get(url))
//				.getDataAccessLevelRead();
//		// ��ӹ�������
//		if ((!("".equals(mainTableClassName))) && (null != mainTableClassName)
//				&& inArray(mainTableClassName, className)) {
//			// �������ݷ��ʼ�����д���
//			switch (urlType) {
//			case DataAccessType.DEPT:
//				// ���ż�����ӵ�ǰ�û��Ĳ����б���ѯ����
//				criteria.add(Restrictions.in("creatorDept", stepIDS));
//				//AutoConditionsUtil.AddAliasFromConditionInfo(criteria);
//				break;
//			case DataAccessType.SELF:
//				// ���˼�����ӵ�ǰ�û���IDΪ������ID
//				criteria.add(Restrictions.eq("creator", userId));
//				// ���˼�����ӵ�ǰ�û���stepidΪ������creatorDept
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
		// �����������в�ѯ
		try {
			result = super.findByCriteria(criteria);

		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		if (null == result) {
			result = new ArrayList(0);
		}


		// ������ѯ������õ����Ȩ���б������浽SecurityContextInfo
		if (null != result) {
			for (int i = 0; i < result.size(); i++) {
				// ��ѯ��ǰ��¼�Ĵ����˵�DEPTID��USERID ��PK
				String creatorDeptId = null;
				String creatorId = null;
				String dataId = null;
				int type = 0;
				if ((result.toArray()[i]) instanceof EntityPlus) {
					EntityPlus entityPlus = (EntityPlus) (result.toArray()[i]);
					dataId = entityPlus.getId();
					creatorDeptId = entityPlus.getCreatorDept().getCode();
					creatorId = entityPlus.getCreator().getCode();
					// ���ü�¼��ID����Ȩ�����õ�SecurityContextInfo�е�rwCtrlTypeList�У�IDΪKEYֵ��
					type = ((CurrentUser) session.getAttribute("currentUser"))
							.getRowRWCtrlType(url, creatorDeptId, creatorId);
				    if (dataSecurityMap != null) {
                        if (dataSecurityMap.containsKey(cName)) {
                            entityPlus.setDataAccessLevelD(getDataAccessLevel(entityPlus, 'D'));
                        }
                    }
					
					
				}
				// �жϵ�ǰ��MAP���Ƿ���ڵ�ǰ������Ŀ�ĵ�KEYֵ
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
		// ����
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
	 * �������ͺ�ID��ȡ�����
	 * @param type
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private Object loadObject(String type, String id) throws Exception {
		
		if (id.startsWith("$")) {
			// ƴװ�����ַ���
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
	 * ��ȡ���ڵ��͵������ӽڵ�IDs
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
	 * ��ӹ�������
	 * @param cmnEntity ʵ��BEAN
	 * @param criteria ��ѯ����
	 * @param type ��:C; ɾ:D; ��:U; ��:S
	 */
	private void filterData(CmnEntity cmnEntity, DetachedCriteria criteria, char type) {
		for (CmnEntityMember bean : cmnEntity.getEntityMembers()) {
			setCriteria(criteria, bean, type);
		}
	}
	/**
	 * ��ӹ�������
	 * @param criteria ��ѯ����
	 * @param bean ʵ���ԱBEAN
	 * @param type ��:C; ɾ:D; ��:U; ��:S
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
	 * ���ַ�����ָ��������ת��
	 * @param value Ҫת�����ַ���
	 * @param bean ʵ���ԱBEAN
	 * @param type ��:C; ɾ:D; ��:U; ��:S
	 * @return ��ָ��������ת����Ķ���
	 */
	private Object getValue(String value, CmnEntityMember bean, char type) {
		Object obj = null;
		String classType = bean.getType();
		if (StringUtils.isNotBlank(bean.getChildrenNode())) {
			//obj = bean.getUserEntityMemberId().getSubIds(type);
			//if (((Set)obj).isEmpty()) {
				//obj = getSubIds(bean.getType(), value, bean.getChildrenNode());
				try {
					//������һ����","Ϊ�ָ���ID
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

	// ע���1��ע��õ���hqls��Ϊ�յĻ���ǰ��������" and "
	// ע���2�������ݷ��ʼ���ΪDataAccessType.FORBIDʱ�����׳�һ���쳣

//	public static String getFilterString(String tableName) throws Exception {
//		// ���巵�ص��ַ���
//		String hqls = null;
//		// ƴװ�����ַ���
//		HttpSession session = SecurityContextInfo.getSession();
//		CurrentUser currentUser = (CurrentUser) session
//				.getAttribute("currentUser");
//		// ��ǰ�û�ID
//		String userId = currentUser.getEmpCd();
//		// ��ǰ�û�����ID���Ӳ���ID
//		String stepID = currentUser.getDeptId();
//		HashSet stepIDS = currentUser.getSubDeptIds();
//		// ����ǰ������ӵ����ż�����
//		stepIDS.add(stepID);
//		// ��ǰ��URL
//		String url = SecurityContextInfo.getCurrentPageUrl();
//		// ��ǰ���ݷ��ʼ����ж�
//		HashMap urlPermissions = currentUser.getUrlPermissions();
//		// ���и�BUG,�����Ժ�,������������ݳ�����
//		int urlType = ((UrlPermission) urlPermissions.get(url))
//				.getDataAccessLevelRead();
//		switch (urlType) {
//		case DataAccessType.DEPT:
//			// ���ż�����ӵ�ǰ�û��Ĳ����б���ѯ����
//			// ƴװ�����ַ���
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
//			// ƴװHQL
//			hqls = tableName + "." + "creatorDept IN ( " + temp + " )";
//			return hqls;
//		case DataAccessType.SELF:
//			// ���˼�����ӵ�ǰ�û���IDΪ������ID����ӵ�ǰ�û���stepidΪ������creatorDept
//			hqls = tableName + "." + "CREATOR = " + userId + " " + tableName
//					+ "." + "CREATOR_DEPT = " + stepID + " ";
//			return hqls;
//		case DataAccessType.FORBID:
//			// �쳣�������д��޸�
//			throw (new Exception());
//		default:
//			return hqls;
//		}
//
//	}

	/**
	 * 
	 * �����������ж�����ʱ�ؼ��ֶε���ֵ�����ݱ��е�Ψһ��,���Բ��뷵��TREU�������MAP��KEYֵΪӳ��ʵ������ֶ�����VALUEΪ��Ҫ���жϵ�ֵ��
	 * �߼�ɾ�����������Ҳ���ڴ˷�Χ��
	 * 
	 * @param ʵ����Class
	 *            �ؼ��ֶ�MAP���� hibernateTemplate
	 * @return 2007-01-22 ����13:06:14
	 * @version 1.0
	 * @author wenjb
	 */
	public static boolean insertDataUniqueCheck(
			HibernateTemplate hibernateTemplate, Class clazz, Map fieldValueMap) {
		ContextInfo.recoverQuery();
		DetachedCriteria dc = DetachedCriteria.forClass(clazz);
		// û�����ݵ���֤������FALSE
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
		// ���ش���
		List result = hibernateTemplate.findByCriteria(dc);
		if (null == result || 0 == result.size()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * �����������жϸ���ʱ�ؼ��ֶε���ֵ�����ݱ��е�Ψһ��,���Ը��·���TREU�������MAP��KEYֵΪӳ��ʵ������ֶ�����VALUEΪ��Ҫ���жϵ�ֵ��
	 * ע���ʱ�ıȽϷ�Χ��������������,�߼�ɾ�����������Ҳ���ڴ˷�Χ
	 * 
	 * @param ʵ����Class
	 *            �ؼ��ֶ�MAP���� hibernateTemplate ��ǰ�����е�ID
	 * @return 2007-01-22 ����13:06:14
	 * @version 1.0
	 * @author wenjb
	 */
	public static boolean updateDataUniqueCheck(
			HibernateTemplate hibernateTemplate, Class clazz,
			Map fieldValueMap, String id) {
		ContextInfo.recoverQuery();
		DetachedCriteria dc = DetachedCriteria.forClass(clazz);
		// û�����ݵ���֤������FALSE
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
		// ���ش���
		List result = hibernateTemplate.findByCriteria(dc);
		if (null == result || 0 == result.size()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * �������� �����ֹ��������ֵ��쳣,�����쳣������Ϣ
	 * 
	 * @return 2008-2-23 ����04:55:32
	 * @version 1.0
	 * @author wenjb
	 */
	public void flush() throws DataAccessException {
		try {
			super.flush();
		} catch (HibernateOptimisticLockingFailureException e) {
			String errorMsg = MessageInfo.factory().getMessage("CM_I043_C_0");
			//String errorMsg = "��Ҫ��������ݰ汾�Ѿ��¾ɣ������������û������򿪱�����֮����������ݡ�����б������´򿪱����棬�ٳ��Ա��β�����";
			throw new RuntimeException(errorMsg);
		}
	}

	/**
	 * �������� ���º�ɾ��ʱ���ж��Ƿ���Ȩ�޽��и���
	 * 
	 * @return 2008-2-23 ����04:55:32
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
				//��Ȩ�޽���ɾ�������
				//if(RWCtrlType.EDIT == rwCtrlType){
					if (StringUtils.isBlank(entityPlus.getId())) {	
						
						entityPlus.setDataAccessLevelC(getDataAccessLevel(entityPlus, 'C'));
						
						if (entityPlus.getDataAccessLevelC()) {							
							super.update(entity);
							return;
						} else {
							throw new DataAccessDenyException("��û��Ȩ�޴��������ݡ�");	
						}
					} else {
						if (entityPlus.getDelFlg().equals("1")) {
							entityPlus.setDataAccessLevelD(getDataAccessLevel(entityPlus, 'D'));
							if (entityPlus.getDataAccessLevelD()) {
								super.update(entity);
								return;
							} else {
								throw new DataAccessDenyException("��û��Ȩ��ɾ�������ݡ�");	
							}
						} else {
							entityPlus.setDataAccessLevelU(getDataAccessLevel(entityPlus, 'U'));
							if (entityPlus.getDataAccessLevelU()) {
								super.update(entity);
								return;
							} else {
								throw new DataAccessDenyException("��û��Ȩ���޸Ĵ����ݡ�");	
							}
						}
					}
				}
				//else{
					//��Ȩ�޵�����£��׳��쳣
					String errorMsg = MessageInfo.factory().getMessage("CM_I045_C_0");
					//String errorMsg = "��û��Ȩ���޸Ļ���ɾ�������ݡ�";
					throw new RuntimeException(errorMsg);			
				//}
			//}
		}
		//����ҪȨ�޴�������
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
				//��Ȩ�޽���ɾ�������
				//if(RWCtrlType.EDIT == rwCtrlType){
					if (StringUtils.isBlank(entityPlus.getId())) {
						
						entityPlus.setDataAccessLevelC(getDataAccessLevel(entityPlus, 'C'));
						
						if (entityPlus.getDataAccessLevelC()) {
							super.saveOrUpdate(entity);
							return;
						} else {
							throw new DataAccessDenyException("��û��Ȩ�޴��������ݡ�");	
						}
					} else {
						if (entityPlus.getDelFlg().equals("1")) {
							entityPlus.setDataAccessLevelD(getDataAccessLevel(entityPlus, 'D'));
							if (entityPlus.getDataAccessLevelD()) {
								super.saveOrUpdate(entity);
								return;
							} else {
								throw new DataAccessDenyException("��û��Ȩ��ɾ�������ݡ�");	
							}
						} else {
							entityPlus.setDataAccessLevelU(getDataAccessLevel(entityPlus, 'U'));
							if (entityPlus.getDataAccessLevelU()) {
								super.saveOrUpdate(entity);
								return;
							} else {
								throw new DataAccessDenyException("��û��Ȩ���޸Ĵ����ݡ�");	
							}
						}
					}
				}
				//else{
					//��Ȩ�޵�����£��׳��쳣
//					String errorMsg = MessageInfo.factory().getMessage("CM_I045_C_0");
//					//String errorMsg = "��û��Ȩ���޸Ļ���ɾ�������ݡ�";
//					throw new DataAccessDenyException(errorMsg);			
				//}
				
			//}
		}
		//����ҪȨ�޴�������
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
				//��Ȩ�޽���ɾ�������
				//if(RWCtrlType.EDIT == rwCtrlType) {
				if(entityPlus.getDataAccessLevelD()){
					super.delete(entity);
					return;
				} else {
					throw new DataAccessDenyException("��û��Ȩ��ɾ�������ݡ�");	
				}
			}
		}
		//����ҪȨ�޴�������
		super.delete(entity);
		return;
	 }
	 
	 //������������дȨ��Ϊֻ����ʱ�򣬲��ܽ���ɾ��������ɾ��
	 private void batchDeletePermission(){
		 //�����������������Ĵ���
		int pageRWCtrlType = 2;
		try {
				// ����URLĬ�϶�д����Ȩ��
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
			 //��Ȩ�޵�����£��׳��쳣
			String errorMsgTemp = MessageInfo.factory().getMessage("CM_I045_C_0");
			//String errorMsg = "��û��Ȩ���޸Ļ���ɾ�������ݡ�";
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
