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
 * @author ����, zhangli,��һ��
 * @version $Id: ArrayUtils.java,v 1.1 2010/12/10 10:54:21 silencily Exp $
 */
public abstract class ArrayUtils {
    
    /**
     * ��","�ָ���<code>String</code>�淶Ϊ������Ȼ˳������, ������������","���ַ���
     * ͷβ�Ŀհ׷�, ͷβ��",", ������<code>,  12, 1,   a,  A  ,,,,,,end, </code>
     * ִ�з������һ�����<code>1,12,A,a,end</code>
     * @param original Ҫ�淶�����ַ���
     * @return �淶������ַ���, ���������<code>null</code>Ҳ����<code>null</code>
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
	 * ��һ������ĩβλ�ò���һ������, �����µ�����
	 * @param originalArray ԭʼ����
	 * @param o ���������
	 * @return ������
	 * @throws ArrayStoreException ��� o �������� originalArray ��Ԫ�����Ͳ�һ��
	 */
	public static Object[] add(Object[] originalArray, Object o) {
		return add(originalArray, o, originalArray.length);
	}
	
	
	/**
	 * ��һ������ĳ��λ�ò���һ������, �����µ�����
	 * @param originalArray ԭʼ����
	 * @param o ���������
	 * @param pos ����λ��
	 * @return ������
	 * @throws ArrayStoreException ��� o �������� originalArray ��Ԫ�����Ͳ�һ��
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
     * �ҳ����������ཻ�Ĳ���, ʹ��<code>Object.equals</code>�����ж����
     * @param a1    Ҫȡ�����ĵ�һ������
     * @param a2    Ҫȡ�����ĵڶ�������
     * @return ����������������ͬ��Ԫ��, ���û����ͬ��Ԫ�ط��س���Ϊ<b>0</b>������
     */
    public static Object[] intersect(Object[] a1, Object[] a2) {
        Assert.notNull(a1, "Ҫ�Ƚϵ����鲻����null");
        Assert.notNull(a2, "Ҫ�Ƚϵ����鲻����null");
        Assert.isTrue(a1.getClass().getComponentType() == a2.getClass().getComponentType(), "Ҫ�Ƚϵ����鲻��ͬһ������");
        
        List ret = new ArrayList(Math.max(a1.length, a2.length));
        for (int i = 0; i < a1.length; i++) {
            int index = org.apache.commons.lang.ArrayUtils.indexOf(a2, a1[i]);
            if (index > -1) {
                ret.add(a2[index]);
            }
        }
        /* �ų���List�еĿ�Ԫ��, ��Ϊ��ArrayUtils�����������������null�Ա���Ϊ��� */
        ret = (List) CollectionUtils.select(ret, new Predicate() {
            public boolean evaluate(Object element) {
                return element != null;
            }});

        return ret.toArray((Object[]) Array.newInstance(a1.getClass().getComponentType(), ret.size()));
    }
    /**
     * 
     * �������� �ж����������ʱ��Ϊ��
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
