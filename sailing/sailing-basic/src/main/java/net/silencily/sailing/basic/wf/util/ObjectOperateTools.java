/**
 * Name: ObjectOperateTools.java
 * Author: Bis liyan
 */
package net.silencily.sailing.basic.wf.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.silencily.sailing.basic.wf.dto.ClonedObject;
import net.silencily.sailing.framework.persistent.Entity;
import net.silencily.sailing.utils.Tools;

/**
 * A tools for object's operation 反射工具
 * 
 * @author Bis liyan
 */
public class ObjectOperateTools {
	
	/**
	 * 判断两个对象是否不相等
	 * 
	 * @param obj1             
	 * @param obj2
	 * @return true or false
	 */
	public static boolean isNotEqual(Object obj1, Object obj2) {
		boolean isNotEqual = false;
		if (obj1 == obj2) {
			isNotEqual = false;
		} else if (obj1 != null && obj1.equals(obj2)) {
			isNotEqual = false;
		} else if((obj1==null||"".equals(obj1))&&(obj2==null||"".equals(obj2))){
			isNotEqual = false;
		} else if((obj1 == null && obj2 != null) || (obj1 != null && obj2 == null)){
			isNotEqual = true;
		} else if (Date.class.isAssignableFrom(obj1.getClass())&&Date.class.isAssignableFrom(obj2.getClass())) {  //日期处理
			String temp1 = Tools.getTimeStyle(((Date) obj1));
			String temp2 = Tools.getTimeStyle(((Date) obj2));
			if(temp1.equals(temp2)){
				isNotEqual = false;
			}else{
				isNotEqual = true;
			}
		}else {
			isNotEqual = true;
		} 
		return isNotEqual;
	}
	/**
	 * 伪克隆一个综合对象含主从关系的
	 * 
	 * @param src
	 * @throws Exception	
	 * @return clonedObj
	 */
	public static ClonedObject clone(Entity src) throws Exception {
		ClonedObject cloneObj = falseClone(src);
		Map subListMap = new HashMap();
		cloneObj.setSubListMap(subListMap);
		Field[] fields = src.getClass().getDeclaredFields();
		for (int i=0; i<fields.length; i++) {
			if (java.util.Set.class.isAssignableFrom(fields[i].getType())) {
				subListMap.put(fields[i].getName(),falseCloneList(toList((Set)src.getClass()
						.getDeclaredMethod(ClassOperateTools.getGetMethodStr(fields[i].getName()), null)
						.invoke(src, null))));
			}
		}
		return cloneObj;
	}
	/**
	 * 伪克隆一个对象
	 * 
	 * @param src
	 * @throws Exception	
	 * @return clonedObj
	 */
	public static ClonedObject falseClone(Entity src) throws Exception {
		ClonedObject cloneObj = new ClonedObject();
		if (src != null) {			
			cloneObj.setId(src.getId());
			Map properties = new HashMap();
			cloneObj.setProperties(properties);
			
			Class srcCls = Tools.getUnProxyClass(src.getClass());
			Field[] srcFields = srcCls.getDeclaredFields();
			
			for (int i=0; i<srcFields.length; i++) {				
				Field srcField = srcFields[i];					
				String srcFieldName = srcField.getName();
				Method getXxx = srcCls.getDeclaredMethod(
								ClassOperateTools.getGetMethodStr(srcFieldName), null);
				Object returnValue = getXxx.invoke(src, null);
				if (returnValue == null || !ClassOperateTools.isSpecialType(srcField))
					continue;
			
				properties.put(srcFieldName, returnValue);	
						
			}
		}		
		return cloneObj;
		
	}
	/**
	 * 把一个对象身上的值，copy到另一个对象上去
	 * 
	 * @param dest             
	 * @param src
	 * @throws Exception
	 */
	public static void copyProperties(Object dest, Object src) throws Exception {
		if (dest != null && src != null) {
			Class destCls = Tools.getUnProxyClass(dest.getClass());
			Class srcCls = Tools.getUnProxyClass(src.getClass());
			Field[] srcFields = srcCls.getDeclaredFields();
			Field[] destFields = destCls.getDeclaredFields();
			for (int i=0; i<srcFields.length; i++) {
				for (int j=0; j<destFields.length; j++) {
					Field srcField = srcFields[i];
					Field destField = destFields[j];
					String srcFieldName = srcField.getName();
					if (srcFieldName != null && srcFieldName.equals(destField.getName())) {
						Method getXxx = srcCls.getDeclaredMethod(
								ClassOperateTools.getGetMethodStr(srcFieldName), null);
						Object returnValue = getXxx.invoke(src, null);
						if (returnValue == null || !ClassOperateTools.isSpecialType(srcField))
							continue;
						Method setXxx = destCls.getDeclaredMethod(
								ClassOperateTools.getSetMethodStr(srcFieldName), new Class[]{returnValue.getClass()});
						setXxx.invoke(dest, new Object[]{returnValue});
					}
				}				
			}
		}		
	}
	/**
	 * 伪克隆一个List    
	 *        
	 * @param src
	 * @throws Exception
	 */
	public static List falseCloneList(List src) throws Exception {
		List result = new ArrayList();
		if (src != null) {
			for (int i=0; i<src.size(); i++) {				
				Entity srcObj = (Entity)src.get(i);
				result.add(falseClone(srcObj));
			}
		}		
		return result;
	}
	/**
	 * 克隆一个List    
	 *        
	 * @param src
	 * @throws Exception
	 */
	public static List cloneList(List src) throws Exception {
		List result = new ArrayList();
		if (src != null) {
			for (int i=0; i<src.size(); i++) {				
				Object srcObj = src.get(i);
				Class cls = Tools.getUnProxyClass(srcObj.getClass());
				Object destObj = cls.newInstance();
				Entity oldEnt = (Entity)srcObj;
				Entity newEnt = (Entity)destObj;
				newEnt.setId(oldEnt.getId());
				copyProperties(destObj, srcObj);
				result.add(destObj);
			}
		}		
		return result;
	}
	/**
	 * 把一个Set转成List    
	 *       
	 * @param set
	 */
	public static List toList(Set set) {
		List result = new ArrayList();
		if (set == null) 
			return result;
		Iterator it = set.iterator();
		while (it.hasNext()) {
			result.add(it.next());
		}
		return result;
	}
	/**
	 * 把一个List转成Set    
	 *       
	 * @param list
	 */
	public static Set toSet(List list) {
		Set resultSet = new HashSet();
		if (list == null) {
			return resultSet; 
		}
		for (int i=0; i<list.size(); i++) {
			resultSet.add(list.get(i));
		}
		return resultSet;
				
	}
	public static void main(String[] args) throws Exception{
//		String obj1 = new String ("123");
//		String obj2 = "123";
//		String obj3 = "23";
//		System.out.println(isNotEqual(null, null));
//		System.out.println(isNotEqual(null, obj1));
//		System.out.println(isNotEqual(obj1, null));
//		System.out.println(isNotEqual(obj1, obj2));
//		System.out.println(isNotEqual(obj1, obj3));
		
//		com.qware.gr.domain.TblGrVpQltTplPara src = new com.qware.gr.domain.TblGrVpQltTplPara();
//		src.setTitle("hello");
//		com.qware.gr.domain.TblGrVpQltTplPara dest = new com.qware.gr.domain.TblGrVpQltTplPara();
//		ObjectOperateTools.copyProperties(dest, src);
//		System.out.println(dest.getTitle());
//		System.out.println("------------------------");
//		List list = new ArrayList();
//		list.add(src);
//		list.add(dest);
//		List cloneList = ObjectOperateTools.cloneList(list);
//		for (int i=0; i<cloneList.size(); i++) {
//			com.qware.gr.domain.TblGrVpQltTplPara obj = (com.qware.gr.domain.TblGrVpQltTplPara)cloneList.get(i);
//			System.out.println(obj.getTitle());
//		}
//		
//		com.qware.gr.domain.TblGrVpQltTplPara src = new com.qware.gr.domain.TblGrVpQltTplPara();
//		src.setId("123");
//		src.setTitle("hello");
//		
//		ClonedObject clonedObject = ObjectOperateTools.falseClone(src);
//		System.out.println(clonedObject.getId());

	}
	
}
