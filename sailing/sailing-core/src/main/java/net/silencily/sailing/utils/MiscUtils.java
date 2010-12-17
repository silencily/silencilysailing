package net.silencily.sailing.utils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.util.Assert;

/**
 * 一些公用的方法
 * @author Scott Captain
 * @author java2enterprise
 * @since 2006-6-1
 * @version $Id: MiscUtils.java,v 1.1 2010/12/10 10:54:21 silencily Exp $
 *
 */
public abstract class MiscUtils {
    /**
     * 从参数数组中排除掉<code>null</code>元素, 如果参数是<code>null</code>或数组没有元素
     * 就返回参数对象, 不做任何处理
     * @param arrayWithNullElements 要排除<code>null</code>元素的数组
     * @return 不包含<code>null</code>元素的数组
     */
    public static Object[] removeNullElements(Object[] arrayWithNullElements) {
        if (arrayWithNullElements == null || arrayWithNullElements.length == 0) {
            return arrayWithNullElements;
        }
        
        List list = new ArrayList(arrayWithNullElements.length);
        list.addAll(Arrays.asList(arrayWithNullElements));
        removeNullElements(list);
        return list.toArray((Object[]) Array.newInstance(arrayWithNullElements.getClass().getComponentType(), list.size()));
    }
    
    /**
     * 从集合中排出<code>null</code>元素, 如果参数是<code>null</code>或集合没有元素
     * 不做任何处理
     * @param collectionWithNullElements
     */
    public static void removeNullElements(Collection collectionWithNullElements) {
        if (collectionWithNullElements == null || collectionWithNullElements.size() == 0) {
            return;
        }
        
        CollectionUtils.filter(collectionWithNullElements, new Predicate() {
            public boolean evaluate(Object element) {
                return (element != null);
            }});
    }
    
    /**
     * 把<code>ISO-8859-1</code>转成<code>GBK</code>编码的字符串, 常用于<code>http</code>
     * 中通过<code>GET</code>提交包含的中文数据
     * @param str 要转换编码的字符串
     * @param logger 当发生错误时要输出日志的<code>Log</code>
     * @return 转换后的编码
     */
    public static String recode(String str, Log logger) {
        if (StringUtils.isNotBlank(str)) {
            try {
                str = new String(str.getBytes("ISO-8859-1"), "GBK");
            } catch (UnsupportedEncodingException e) {
                if (logger != null && logger.isDebugEnabled()) {
                    logger.debug("不能转换字符串[" + str + "]的编码", e);
                }
            }
        }
        
        return str;
    }
    
    /**
     * 处理 null 的 toString 方法
     * @param o the object
     * @return 如果 o == null, 返回空字符串, 否则返回 o.toString()
     */
    public static String nullSafeToString(Object o) {
    	return o == null ? "" : o.toString();
    }
    
    /**
     * 把具有行分隔符的字符串拆分为数组, 如果参数是空串返回长度为<b>0</b>的数组, 其中的元素
     * 不包含空串
     * @param str
     * @return 字符串数组
     * @throws NullPointerException 如果参数是<code>null</code>
     */
    public static String[] splitWithLineSeparator(String str) {
        if (str == null) {
            throw new NullPointerException("splitWithLineSeparator's 参数是null");
        }

        String[] ret = str.split("[\\r\\n]");
        List list = new ArrayList(ret.length);
        for (int i = 0; i < ret.length; i++) {
            if (StringUtils.isNotBlank(ret[i])) {
                list.add(ret[i]);
            }
        }
        
        return (String[]) list.toArray(new String[list.size()]);
    }
    
    /**
     * 排除 list 中为 null 的元素, 如果 list == null, 返回空 List
     * @param list the orignal list
     * @return list after shrink
     */
    public static List nullSafeShrinkNullElements(List list) {
    	if (list == null) {
    		return Collections.EMPTY_LIST;
    	}
    	return (List) CollectionUtils.select(list, new Predicate() {
			public boolean evaluate(Object object) {
				return object != null;
			}
    	});
    }
    
    
    /**
     * 根据一个指定的 size 拆分一个 List 为多个 List, 再将这些拆分后的 list 放在一个 list 中返回
     * @param list 原 list
     * @param size 拆分 list 的大小
     * @return list fill with list
     */
    public static List splitListBySize(List list, int size) {
    	Assert.notNull(list, "list required");
		int fromIndex = 0;
		int splitedCount = list.size() / size + 1;
		List splitedList = new ArrayList(splitedCount);
		
		for (int i = 0; i < splitedCount; i++) {
			int toIndex = fromIndex + size;
			if (toIndex > list.size()) {
				toIndex = list.size();
			}
			List element = list.subList(fromIndex, toIndex);
			fromIndex += size;
			splitedList.add(element);
		}
					
		return splitedList;
    }
    
}
