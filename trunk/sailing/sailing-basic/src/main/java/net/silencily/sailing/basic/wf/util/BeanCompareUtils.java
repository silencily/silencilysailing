/*
 * Main.java
 *
 * Created on 2008年6月21日, 上午11:51
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.silencily.sailing.basic.wf.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import net.silencily.sailing.basic.wf.service.WFService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.framework.persistent.Entity;
import net.silencily.sailing.framework.persistent.hibernate3.entity.CodeWrapper;
import net.silencily.sailing.utils.Tools;


/**
 * 
 * @author 刘卓
 */
public class BeanCompareUtils {

	public static String compare(Object oldObj, Object newObj, String[] loop,
			String[] uncompare, String[] displayField, boolean isMaster) {

		if (null == oldObj && isMaster) {
			return "";
		}
//		 主表的类型
		Class entryClass = getObjClass(oldObj, newObj);
		if (entryClass == null) {
			throw new RuntimeException("对象类型不一致");
		}
		

		Field oldFieldlist[] = entryClass.getDeclaredFields();

		// Method oldMethlist[] = oldObj.getClass().getDeclaredMethods();
		StringBuffer result = null;
		StringBuffer sb = new StringBuffer();
		for (int j = 0; j < oldFieldlist.length; j++) {

			boolean umc = false;
			if (uncompare != null)
				for (int l = 0; l < uncompare.length; l++) {
					if (oldFieldlist[j].getName().toUpperCase().equals(
							uncompare[l].toUpperCase())) {
						umc = true;
						break;
					}
				}
			if (umc) {
				continue;
			}
			try {
				String fieldName = oldFieldlist[j].getName();
				Method getMethod = null;
				try {
					getMethod = Tools.getGetMethod(fieldName, getObjClass(oldObj,
							newObj));
				} catch (Exception e) {
					continue;
				}
				String[] loopStr = compareLoop(fieldName, loop);

				Object oldV = getMethod.invoke(oldObj, null);
				Object newV = getMethod.invoke(newObj, null);
				compareObject(newV, oldV, fieldName, loopStr, uncompare, displayField, sb, entryClass);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println(sb.toString());
		if (sb.length() != 0) {
			result = new StringBuffer();
			try {
				if (isMaster) {
					result.append("基本信息")
							/*.append(" ID=")
							.append(((Entity) oldObj).getId())*/
							.append(" _br_");
				}
				result.append(sb);
				if (isMaster) {
					result.append("_br_");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result == null ? null : result.toString();
	}

	/**
	 * 比较两个对象
	 */
	private static void compareObject(Object newV, Object oldV,
			String fieldName, String[] loopStr,String[] uncompare, String[] displayField,
			StringBuffer sb, Class entryClass) throws Exception {
		Class cls = getObjClass(oldV, newV);
		if (cls == null) {
			return;
		}
		// 判断List或者Set
		if (hasSuperClass(cls, Collection.class)
				|| cls.equals(org.hibernate.collection.PersistentSet.class)) {
			try {
				Object[] oldCol = ((Collection) oldV).toArray();
				Object[] newCol = ((Collection) newV).toArray();

				/*if (oldCol.length != newCol.length) {
					printChange(sb, getBisFieldName(entryClass, fieldName)
							+ ".size", new Integer(oldCol.length), new Integer(
							newCol.length), entryClass);
				}*/
				Hashtable colTable = new Hashtable();
				for (int i = 0; i < oldCol.length; i++) {
					Object oldIDObj = Entity.class.getMethod("getId",
							null).invoke(oldCol[i], null);
					
					if (oldIDObj == null) {
						continue;
					}
					String oldID = oldIDObj == null ? null : oldIDObj
							.toString();
					for (int j = 0; j < newCol.length; j++) {
						Object newIDObj = Entity.class.getMethod(
								"getId", null).invoke(newCol[j], null);
						String newID = newIDObj == null ? null
								: newIDObj.toString();

						if (oldID.equals(newID)) {
							colTable.put(oldCol[i], newCol[j]);
						}
					}
				}

				ArrayList delList = getDeletedItem(newCol);
				ArrayList newList = getNewItem(oldCol, newCol);
				if(delList.size()>0){
					sb.append("_br_----明细列表信息:").append("删除的记录----");
					for(int i = 0 ; i < delList.size() ; i++)
					{
						Object obj = delList.get(i);
						if(obj == null){
							continue;
						}
						//sb.append("ID=").append(((Entity)obj).getId());
						sb.append("_br_ ").append(getFieldString(displayField, delList.get(i), getObjClass(obj,null)));
					}
					sb.append("_br_--------------------------------------------------------------------_br_");
				}
				if(newList.size()>0){
					
					sb.append("_br_----明细列表信息:").append("新增的记录----_br_");
					for(int i = 0 ; i < newList.size() ; i++) {
						Object obj = newList.get(i);
						if(obj == null){
							continue;
						}
						//sb.append("ID=").append(((Entity)obj).getId());
						sb.append("_br_ ").append(getFieldString(displayField, newList.get(i), getObjClass(obj,null)));
					}
					sb.append("_br_--------------------------------------------------------------------_br_");
				}
				if (loopStr != null) {
						Enumeration enumer = colTable.keys();
						String[] fieldArr = null;
						if (loopStr.length - 1 > 0) {
							fieldArr = new String[loopStr.length - 1];
						}
						while (enumer.hasMoreElements()) {
							Object oldObj = enumer.nextElement();
							Object newObj = colTable.get(oldObj);

							for (int i = 1; i < loopStr.length; i++) {
								fieldArr[i - 1] = loopStr[i];
							}
							// String newFieldName = fieldName;
							if (fieldArr != null) {
								Method getM = Tools.getGetMethod(fieldArr[0],
										getObjClass(newObj, oldObj));
								newObj = getM.invoke(newObj, null);
								oldObj = getM.invoke(oldObj, null);
								// newFieldName = fieldArr[0];
							}
							String result = compare(oldObj, newObj, fieldArr,
									uncompare, displayField, false);
							if (result != null && result.length() > 0) {
								sb.append(
										"_br_----明细列表信息:")
										/*.append("ID=")
										.append(oldID)*/
										.append(" ")
										.append(getFieldString(displayField, oldObj, getObjClass(oldObj, newObj)))
										.append("---- _br_").append(result)
										.append("--------------------------------------------------------------------_br_");
							}
						}
				}
			} catch (org.hibernate.LazyInitializationException e) {
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 不是基础类型并且继续向下一层判断
		else if (loopStr != null) {
			if (!compareWithLoop(loopStr, newV, oldV, uncompare, displayField, entryClass, sb)) {
				printChange(sb, fieldName, uncompare, oldV, newV, entryClass);
			}
		}
		// 基础类型
		else {
			if (!primitiveEquals(oldV, newV)) {
				if (oldV != null && Date.class.isAssignableFrom(oldV.getClass())){
					oldV = Tools.getTimeStyle(((Date) oldV));
				}
				if (newV != null && Date.class.isAssignableFrom(newV.getClass())) {
					newV = Tools.getTimeStyle(((Date) newV));
				}
				Object oldStr = oldV;
				Object newStr = newV;
				if((oldStr == null || oldStr instanceof CodeWrapper) && 
						(newStr == null || newStr instanceof CodeWrapper)){
					if(oldStr != null){
						oldStr = ((CodeWrapper)oldStr).getName();
					}
					if(newStr != null){
						newStr = ((CodeWrapper)newStr).getName();
					}
				}
				printChange(sb, fieldName, uncompare, oldStr, newStr, entryClass);
			}
		}
	}

	private static ArrayList getNewItem(Object[] oldCol, Object[] newCol) throws Exception {
		ArrayList result = new ArrayList();
		Hashtable oldTable = getObjTable(oldCol);
		for(int i=0;i<newCol.length;i++){
			Object newIDObj = Entity.class.getMethod("getId",
					null).invoke(newCol[i], null);
			
			if (newIDObj == null) {
				continue;
			}
			String newID = newIDObj == null ? null : newIDObj
					.toString();
			if(!oldTable.containsKey(newID)){
				result.add(newCol[i]);
			}
		}
		return result;
	}

	private static ArrayList getDeletedItem(Object[] newCol) throws Exception{
		ArrayList result = new ArrayList();
		for(int i = 0; i<newCol.length ;i++){
			Object obj = newCol[i];
			if(obj == null){
				continue;
			}
			if("1".equals(((Entity)obj).getDelFlg())){
				result.add(newCol[i]);
			}
		}
		return result;
	}

	private static Hashtable getObjTable(Object[] oldCol) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Hashtable oldTable = new Hashtable();
		for(int i=0;i<oldCol.length;i++){
			Object oldIDObj = Entity.class.getMethod("getId",
					null).invoke(oldCol[i], null);
			if (oldIDObj == null) {
				continue;
			}
			String oldID = oldIDObj == null ? null : oldIDObj
					.toString();
			if(!oldTable.contains(oldID)){
				oldTable.put(oldID, oldCol[i]);
			}
		}
		return oldTable;
	}
	
	private static String getFieldString(String[] fields,Object obj, Class cls){
		StringBuffer result = new StringBuffer();
		for(int i=0;i<fields.length;i++){
			try{
				String[] subFields = fields[i].split("\\.");
				Class pCls = cls;
				Object value = obj;
				String field = null;
				for(int j = 0 ; j < subFields.length; j++){
					try{
						field = subFields[j];
						Method m = Tools.getGetMethod(field, pCls);
						if(value != null){
							value = m.invoke(value, null);
							if(j != (subFields.length-1)){
								pCls = m.getReturnType();
							}
						}
					}catch(NoSuchMethodException ne){
						continue;
					}
				}
				result.append(" [")
				.append(getBisFieldName(pCls, field))
				.append("]")
				.append("为")
				.append("[")
				.append(value==null?"":value.toString())
				.append("] ");
			}catch(NoSuchMethodException ne){
				continue;
			}catch(Exception e){
				
			}
		}
		return result.toString();
	}
	
	/**
	 * 得到类
	 */
	public static boolean hasSuperClass(Class cls, Class superCls) {
		List itfs = getAllSuperClass(cls);
		for (int i = 0; i < itfs.size(); i++) {
			if (itfs.get(i).equals(superCls)) {
				return true;
			}
		}
		return false;
	}

	private static List getAllSuperClass(Class cls) {
		List result = new ArrayList();
		Class cur = cls.getSuperclass();
		while (!cls.equals(Object.class)) {
			if (cur == null) {
				break;
			}
			result.add(cur);
			result.addAll(getAllSuperClass(cur));
			Class[] clss = cur.getInterfaces();
			for (int i = 0; i < clss.length; i++) {
				result.add(clss[i]);
				result.addAll(getAllSuperClass(clss[i]));
			}
			cur = cur.getSuperclass();
		}
		return result;
	}

	/**
	 * 得到实例的类型
	 */
	private static Class getObjClass(Object oldV, Object newV) {
		Class cls = null;
		if (oldV == null) {
			if (newV != null) {
				cls = Tools.getUnProxyClass(newV.getClass());
			}
		} else {
			cls = Tools.getUnProxyClass(oldV.getClass());
		}
		return cls;
	}

	/**
	 * 对两个基础类型进行比较
	 */
	private static boolean primitiveEquals(Object oldV, Object newV) {
		boolean result = true;
		Class cls = getObjClass(oldV, newV);
		if (cls == null || hasSuperClass(cls, Entity.class)) {
			return result;
		}
		result = !ObjectOperateTools.isNotEqual(oldV, newV);
		// if (oldV == null || newV == null) {
		// result = false;
		// } else {
		// // 如果是字符串类型null==""
		// Class cls = getObjClass(oldV, newV);
		// if (cls == null) {
		// return true;
		// }
		// if (cls.equals(String.class)) {
		// if (oldV == null) {
		// oldV = "";
		// }
		// if (newV == null) {
		// newV = "";
		// }
		// }
		// if (oldV != null) {
		// if (!oldV.equals(newV)) {
		// result = false;
		// }
		// } else if (newV != null) {
		// if (!newV.equals(oldV)) {
		// result = false;
		// }
		// }
		// }
		return result;
	}

	/**
	 * 比较与字符串匹配的属性
	 * 
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	private static boolean compareWithLoop(String[] loopStr, Object newV,
			Object oldV,String[] uncompare,String[] displayField,
			Class entryClass, StringBuffer sb) throws Exception {
		Object oldO = oldV;
		Object newO = newV;
		String subField = null;
		String[] l = null;
		if (loopStr != null && loopStr.length > 2) {
			l = new String[loopStr.length - 1];
		}

		for (int li = 1; li < loopStr.length; li++) {
			subField = loopStr[li];
			l[li - 1] = subField;
			Field f = oldO.getClass().getField(subField);
			if (f == null) {
				throw new RuntimeException("[" + loopStr + "]中找不到属性["
						+ subField + "]");
			}
			oldO = f.get(oldO);
			newO = f.get(newO);
		}
		Class cls = getObjClass(oldO, newO);
		if (cls == null) {
			return true;
		}
		/*
		 * if(!cls.isPrimitive()){ throw new
		 * RuntimeException("属性["+loopStr+"]不是基础类型"); }
		 */
		// 一致true
		StringBuffer result = new StringBuffer();
		compareObject(newV, oldV, subField, l, uncompare, displayField, result, getObjClass(oldV, newV));
		sb.append(result);
		return result.length() > 0;
	}

	/**
	 * 如果有找到属性对应的字符串
	 */
	private static String[] compareLoop(String fieldName, String[] loop) {
		if (loop != null)
			for (int i = 0; i < loop.length; i++) {
				String[] sub = loop[i].split("\\.");
				for(int j=0;j<sub.length;j++){
					if (sub[j].equals(fieldName)) {
						return sub;
					}
				}
			}
		return null;
	}

	/**
	 * 向字符串追加
	 */
	private static void printChange(StringBuffer sb, String fieldName, String[] uncompare,
			Object oldV, Object newV, Class classTemp) throws Exception {
		if (compareLoop(fieldName, uncompare) == null) {
			sb.append(" [")
			.append(getBisFieldName(classTemp, fieldName))
			.append("] 由 [")
			.append(getObjString(oldV))
			.append("] 变为 [")
			.append(getObjString(newV))
			.append("] _br_");
		}
	}
	
	private static String getObjString(Object obj){
		String result = obj == null ? "" : obj.toString();
		return result;
	}

	// 需要预留的,通过字段名获得业务显示名的方法
	private static String getBisFieldName(Class cls, String fieldName)
			throws Exception {
		try {
			WFService serviceTemp = (WFService) ServiceProvider
					.getService(WFService.SERVICE_NAME);
			return serviceTemp.searchStepFieldComment(cls, fieldName);
		} catch (Exception e) {
			e.printStackTrace();
			return fieldName;
		}

	}

	public static void main(String[] args) {
		/*TblRmArrivalMaster a = new TblRmArrivalMaster();
		a.setCheckAddr("aa");
		TblRmEntermtMaster tm = new TblRmEntermtMaster();
		tm.setId("aaa");
		tm.setEntertblCd("11");
		a.getTblRmEntermtMasters().add(tm);
		TblRmArrivalMaster b = new TblRmArrivalMaster();
		b.setCheckAddr("bb");
		tm = new TblRmEntermtMaster();
		tm.setId("aaa");
		tm.setEntertblCd("12");
		b.getTblRmEntermtMasters().add(tm);*/
		/*System.out.println(BeanCompareUtils.compare(a, b,
				new String[] { "tblRmEntermtMasters.entertblCd" }, null, true));
		System.out.println("tblRmEntermtMasters.entertblCd".split("\\.")[0]);*/
	}

}
