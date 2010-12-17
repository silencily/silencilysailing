/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project framework
 */
package net.silencily.sailing.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

/**
 * @since 2006-6-28
 * @author 王政, zhangli,赵一非
 * @version $Id: ArrayUtils.java,v 1.1 2010/12/10 10:54:21 silencily Exp $
 */
public abstract class ArrayUtils {
    
    /**
     * 把","分隔的<code>String</code>规范为按照自然顺序排列, 不包含连续的","和字符串
     * 头尾的空白符, 头尾的",", 参数是<code>,  12, 1,   a,  A  ,,,,,,end, </code>
     * 执行方法后的一个结果<code>1,12,A,a,end</code>
     * @param original 要规范化的字符串
     * @return 规范化后的字符串, 如果参数是<code>null</code>也返回<code>null</code>
     */
    public static String normalizeCommaLimitedString(String original) {
        if (StringUtils.isBlank(original)) {
            return original;
        }
        String[] strs = original.split("\\s*,\\s*");
        List list = new ArrayList(strs.length);
        list.addAll(Arrays.asList(strs));
        list = (List) CollectionUtils.select(list, new Predicate() {
            public boolean evaluate(Object element) {
                String s = org.springframework.util.StringUtils.trimWhitespace(element.toString());
                return s.length() > 0;
            }});
        Collections.sort(list);
        return org.springframework.util.StringUtils.collectionToCommaDelimitedString(list).replaceAll("[\\s\\n\\t]+", "");
    }
	
	/**
	 * 在一个数组末尾位置插入一个对象, 返回新的数组
	 * @param originalArray 原始数组
	 * @param o 欲插入对象
	 * @return 新数组
	 * @throws ArrayStoreException 如果 o 的类型与 originalArray 中元素类型不一致
	 */
	public static Object[] add(Object[] originalArray, Object o) {
		return add(originalArray, o, originalArray.length);
	}
	
	
	/**
	 * 在一个数组某个位置插入一个对象, 返回新的数组
	 * @param originalArray 原始数组
	 * @param o 欲插入对象
	 * @param pos 插入位置
	 * @return 新数组
	 * @throws ArrayStoreException 如果 o 的类型与 originalArray 中元素类型不一致
	 */
	public static Object[] add(Object[] originalArray, Object o, int pos) {
		if (originalArray == null || originalArray.length == 0) {
			return new Object[] {o};
		}
		
		Object[] returnArray = (Object[]) Array.newInstance(originalArray[0].getClass(), originalArray.length + 1);
		System.arraycopy(originalArray, 0, returnArray, 0, pos);
		returnArray[pos] = o;

		System.arraycopy(originalArray, pos, returnArray, pos + 1, originalArray.length - pos);
		return returnArray;
	}
	
    /**
     * 找出两个数组相交的部分, 使用<code>Object.equals</code>方法判断相等
     * @param a1    要取交集的第一个数组
     * @param a2    要取交集的第二个数组
     * @return 返回两个数组中相同的元素, 如果没有相同的元素返回长度为<b>0</b>的数组
     */
    public static Object[] intersect(Object[] a1, Object[] a2) {
        Assert.notNull(a1, "要比较的数组不能是null");
        Assert.notNull(a2, "要比较的数组不能是null");
        Assert.isTrue(a1.getClass().getComponentType() == a2.getClass().getComponentType(), "要比较的数组不是同一种类型");
        
        List ret = new ArrayList(Math.max(a1.length, a2.length));
        for (int i = 0; i < a1.length; i++) {
            int index = org.apache.commons.lang.ArrayUtils.indexOf(a2, a1[i]);
            if (index > -1) {
                ret.add(a2[index]);
            }
        }
        /* 排除掉List中的空元素, 因为在ArrayUtils中如果两个数组中有null仍被认为相等 */
        ret = (List) CollectionUtils.select(ret, new Predicate() {
            public boolean evaluate(Object element) {
                return element != null;
            }});

        return ret.toArray((Object[]) Array.newInstance(a1.getClass().getComponentType(), ret.size()));
    }
    /**
     * 
     * 功能描述 判断数组的内容时候为空
     * @param array
     * @param i
     * @return
     * Nov 16, 2007 10:47:58 AM
     */
    public static boolean isBlank(Object[] array,int i)
    {
    	boolean result=true;
    	if(array==null)
    	return result;
    	if(array[i]==null)
    		return result;
    	return false;
    }
}
