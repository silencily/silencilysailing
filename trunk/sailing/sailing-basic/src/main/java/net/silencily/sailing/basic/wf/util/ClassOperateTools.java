/**
 * Name: ClassOperateTools.java
 * Author: Bis liyan
 */
package net.silencily.sailing.basic.wf.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import net.silencily.sailing.utils.Tools;

/**
 * A tools for the reflect operation ���乤��
 * 
 * @author Bis liyan
 */
public class ClassOperateTools {
	/**
	 * �õ�obj��name��ֵ
	 * 
	 * @param name
	 *            fieldName
	 * @param obj
	 * @throws Exception
	 * @return result
	 */
	public static Object getValueByName(String name, Object obj)
			throws Exception {
		Object result = new Object();
		if (obj != null) {			
			Class cls = Tools.getUnProxyClass(obj.getClass());		
			Method method = cls.getDeclaredMethod(ClassOperateTools.getGetMethodStr(name), null);
			result = method.invoke(obj, null);
		}
		return result;
	}

	/**
	 * �õ�Fileldӳ��
	 * 
	 * @param obj
	 * @throws Exception
	 * @return resultMap
	 */
	/**
	 * �������� map (key = fieldName, value = ChineseScription)
	 */
	public static Map getFieldMap(Object obj) throws Exception {
		Class cls = Tools.getUnProxyClass(obj.getClass());
		Map resultMap = new HashMap();		
		Field[] fields = cls.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			if (isSpecialType(fields[i])) {
				resultMap.put(fields[i].getName(), fields[i].getName());
			}
		}

		return resultMap;
	}
	/**
	 * �õ�get�������� 
	 * @param fieldName	
	 * @return getXxx
	 */
	public static String getGetMethodStr(String fieldName){
		String result = "get";
		String first = fieldName.substring(0, 1);
		String last = fieldName.substring(1);
		first = first.toUpperCase();
		return result + first + last;
	}
	/**
	 * �õ�set�������� 
	 * @param fieldName	
	 * @return setXxx
	 */
	public static String getSetMethodStr(String fieldName){
		String result = "set";
		String first = fieldName.substring(0, 1);
		String last = fieldName.substring(1);
		first = first.toUpperCase();
		return result + first + last;
	}
	public static void main(String[] args){
		String hello = "hello";
		System.out.println(getGetMethodStr(hello));
		System.out.println(getSetMethodStr(hello));
	}
	public static boolean isSpecialType(Field field) {
		Class cls = field.getType();
		boolean flag = false;
		if (byte.class.equals(cls)) {
			flag = true;
		} else if (short.class.equals(cls)) {
			flag = true;
		} else if (char.class.equals(cls)) {
			flag = true;
		} else if (int.class.equals(cls)) {
			flag = true;
		} else if (float.class.equals(cls)) {
			flag = true;
		} else if (double.class.equals(cls)) {
			flag = true;
		} else if (boolean.class.equals(cls)) {
			flag = true;
		} else if (long.class.equals(cls)) {
			flag = true;
		} else if (Byte.class.equals(cls)) {
			flag = true;
		} else if (Short.class.equals(cls)) {
			flag = true;
		} else if (Integer.class.equals(cls)) {
			flag = true;
		} else if (Character.class.equals(cls)) {
			flag = true;
		} else if (Float.class.equals(cls)) {
			flag = true;
		} else if (Double.class.equals(cls)) {
			flag = true;
		} else if (Boolean.class.equals(cls)) {
			flag = true;
		} else if (BigDecimal.class.equals(cls)) {
			flag = true;
		} else if (java.util.Date.class.equals(cls)) {
			flag = true;
		} else if (java.sql.Date.class.equals(cls)) {
			flag = true;
		} else if (String.class.equals(cls)) {
			flag = true;
		}
		return flag;
	}
}
