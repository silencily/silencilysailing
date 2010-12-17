/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project framework
 */
package net.silencily.sailing.framework.persistent.hibernate3;

import java.io.ObjectStreamField;
import java.io.Serializable;
import java.util.Date;

import net.silencily.sailing.framework.codename.DepartmentCodeName;
import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.framework.persistent.Entity;
import net.silencily.sailing.framework.persistent.hibernate3.entity.CodeWrapper;
import net.silencily.sailing.framework.persistent.hibernate3.entity.DepartmentWrapper;
import net.silencily.sailing.framework.persistent.hibernate3.entity.UserWrapper;
import net.silencily.sailing.security.SecurityContextInfo;
import net.silencily.sailing.utils.ArrayUtils;

import org.apache.commons.lang.StringUtils;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.orm.hibernate3.HibernateInterceptor;


/**
 * 重新实现 {@link HibernateInterceptor} 以实现一些自定义操作
 * @since 2006-7-20
 * @author java2enterprise
 * @version $Id: ReworkingHibernateInterceptor.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 */
public class ReworkingHibernateInterceptor extends EmptyInterceptor {

	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = -7593710674374615207L;

    private static final ObjectStreamField[] serialPersistentFields = new ObjectStreamField[0];
    
    
	/**
	 * @see org.hibernate.EmptyInterceptor#onSave(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
	 */
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		if (Entity.class.isAssignableFrom(entity.getClass())) {  
			Entity et = (Entity) entity;
			boolean modified = false;
            if (SecurityContextInfo.getCurrentUser() == null) {
                return modified;
            }
			//设置创建人和创建时间
			for (int i = 0; i < propertyNames.length; i++) {
				if ("creator".equalsIgnoreCase(propertyNames[i])) {
					state[i] = ContextInfo.getContextUser();
					et.setCreator(ContextInfo.getContextUser());
					modified = true;
				}else if ("createdTime".equalsIgnoreCase(propertyNames[i])) {
					Date createdTime = net.silencily.sailing.utils.DBTimeUtil.getDBTime();
					state[i] = createdTime;
					et.setCreatedTime(createdTime);
					modified = true;
				}else if ("creatorDept".equalsIgnoreCase(propertyNames[i])) {
					DepartmentCodeName dep = new DepartmentCodeName();
					dep.setCode(SecurityContextInfo.getCurrentUser().getDeptId());
					dep.setName(SecurityContextInfo.getCurrentUser().getDeptName());
					dep.setId(SecurityContextInfo.getCurrentUser().getDeptId());
					state[i] = dep;
					et.setCreatorDept(dep);
					modified = true;
				}
			}			
			return modified;
		}
		return false;
	}


	/**
	 * @see org.hibernate.EmptyInterceptor#onFlushDirty(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
	 */
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
		if (Entity.class.isAssignableFrom(entity.getClass())) {					
			Entity et = (Entity) entity;
			boolean modified = false;
			//逻辑删除判断标识
			boolean logicDelFlg = false;
            if (SecurityContextInfo.getCurrentUser() == null) {
                return modified;
            }
			//获取逻辑删除标志位的状态，从而分出是更新状态，还是删除状态。
			for (int i = 0; i < propertyNames.length; i++) {
				if("delFlg".equalsIgnoreCase(propertyNames[i])){
					if(currentState[i] != null){
						String currentValue = (String)currentState[i];
						currentValue = (currentValue == null)?"" : currentValue.trim();
						//如果状态为1时，此时为逻辑删除状态。
						if(currentValue.equals("1")){
							logicDelFlg = true;
							break;
						}
					}
				}
			}
			if(!logicDelFlg){
				//更新的时候：设置修改人和时间
				for (int i = 0; i < propertyNames.length; i++) {
					if ("modifier".equals(propertyNames[i])) {
						currentState[i] = ContextInfo.getContextUser();
						et.setModifier(ContextInfo.getContextUser());
						modified = true;
					}else if ("modifiedTime".equals(propertyNames[i])) {
						Date modifiedTime = net.silencily.sailing.utils.DBTimeUtil.getDBTime();
						currentState[i] = modifiedTime;
						et.setModifiedTime(modifiedTime);
						modified = true;
					}else if ("modifierDept".equals(propertyNames[i])) {
						DepartmentCodeName dep = new DepartmentCodeName();
						dep.setCode(SecurityContextInfo.getCurrentUser().getDeptId());
						dep.setName(SecurityContextInfo.getCurrentUser().getDeptName());
						dep.setId(SecurityContextInfo.getCurrentUser().getDeptId());
						currentState[i] = dep;
						et.setCreatorDept(dep);
						modified = true;
					}
				}
			}else{
				//删除的时候：设置删除人和时间
				for (int i = 0; i < propertyNames.length; i++) {
					if ("deleter".equals(propertyNames[i])) {
						currentState[i] = ContextInfo.getContextUser();
						et.setDeleter(ContextInfo.getContextUser());
						modified = true;
					}else if ("deletedTime".equals(propertyNames[i])) {
						Date modifiedTime = net.silencily.sailing.utils.DBTimeUtil.getDBTime();
						currentState[i] = modifiedTime;
						et.setDeletedTime(modifiedTime);
						modified = true;
					}else if ("deleterDept".equals(propertyNames[i])) {
						DepartmentCodeName dep = new DepartmentCodeName();
						dep.setCode(SecurityContextInfo.getCurrentUser().getDeptId());
						dep.setName(SecurityContextInfo.getCurrentUser().getDeptName());
						dep.setId(SecurityContextInfo.getCurrentUser().getDeptId());
						currentState[i] = dep;
						et.setDeleterDept(dep);
						modified = true;
					}
				}
			}
			return modified;
		}
		return false;
	}


	/**
	 * @see org.hibernate.EmptyInterceptor#findDirty(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
	 * @since 2007-11-16
	 * zhaoyifei modify <code>ArrayUtils.isBlank(previousState, i)</code><code>if(currentState[i]==null)</code>
	 */
	public int[] findDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
		
		/**
		 * 重写 currentState 的值以避免 hibernate 认为 {@link CodeWrapper} {@link UserWrapper} {@link DepartmentWrapper} 的空对象为 dirty object
		 */ 
		
		for (int i = 0; i < currentState.length; i++) {
			if(currentState[i]==null)
				continue;
			if (CodeWrapper.class.isAssignableFrom(currentState[i].getClass())) {
				CodeWrapper codeWrapper = (CodeWrapper) currentState[i];
				if (StringUtils.isBlank(codeWrapper.getCode())&& ArrayUtils.isBlank(previousState, i)) {
						currentState[i] = null;
					}
					
			} else if (UserWrapper.class.isInstance(currentState[i])) {
				UserWrapper userWrapper = (UserWrapper) currentState[i];
				if (StringUtils.isBlank(userWrapper.getUsername()) && ArrayUtils.isBlank(previousState, i)) {
					currentState[i] = null;
				}
			} else if (DepartmentWrapper.class.isInstance(currentState[i])) {
				DepartmentWrapper departmentWrapper = (DepartmentWrapper) currentState[i];
				if (StringUtils.isBlank(departmentWrapper.getDepartmentId()) && ArrayUtils.isBlank(previousState, i)) {
					currentState[i] = null;
				}
			}
		}
		
		/**
		 * 注意, 此处返回 null 让 hibernate 自行处理
		 * @see DefaultFlushEntityEventListener#dirtyCheck()
		 */
		return null;
	}
	
	
}
