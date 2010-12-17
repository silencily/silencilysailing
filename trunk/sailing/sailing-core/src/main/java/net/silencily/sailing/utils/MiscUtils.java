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
 * һЩ���õķ���
 * @author Scott Captain
 * @author java2enterprise
 * @since 2006-6-1
 * @version $Id: MiscUtils.java,v 1.1 2010/12/10 10:54:21 silencily Exp $
 *
 */
public abstract class MiscUtils {
    /**
     * �Ӳ����������ų���<code>null</code>Ԫ��, ���������<code>null</code>������û��Ԫ��
     * �ͷ��ز�������, �����κδ���
     * @param arrayWithNullElements Ҫ�ų�<code>null</code>Ԫ�ص�����
     * @return ������<code>null</code>Ԫ�ص�����
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
     * �Ӽ������ų�<code>null</code>Ԫ��, ���������<code>null</code>�򼯺�û��Ԫ��
     * �����κδ���
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
     * ��<code>ISO-8859-1</code>ת��<code>GBK</code>������ַ���, ������<code>http</code>
     * ��ͨ��<code>GET</code>�ύ��������������
     * @param str Ҫת��������ַ���
     * @param logger ����������ʱҪ�����־��<code>Log</code>
     * @return ת����ı���
     */
    public static String recode(String str, Log logger) {
        if (StringUtils.isNotBlank(str)) {
            try {
                str = new String(str.getBytes("ISO-8859-1"), "GBK");
            } catch (UnsupportedEncodingException e) {
                if (logger != null && logger.isDebugEnabled()) {
                    logger.debug("����ת���ַ���[" + str + "]�ı���", e);
                }
            }
        }
        
        return str;
    }
    
    /**
     * ���� null �� toString ����
     * @param o the object
     * @return ��� o == null, ���ؿ��ַ���, ���򷵻� o.toString()
     */
    public static String nullSafeToString(Object o) {
    	return o == null ? "" : o.toString();
    }
    
    /**
     * �Ѿ����зָ������ַ������Ϊ����, ��������ǿմ����س���Ϊ<b>0</b>������, ���е�Ԫ��
     * �������մ�
     * @param str
     * @return �ַ�������
     * @throws NullPointerException ���������<code>null</code>
     */
    public static String[] splitWithLineSeparator(String str) {
        if (str == null) {
            throw new NullPointerException("splitWithLineSeparator's ������null");
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
     * �ų� list ��Ϊ null ��Ԫ��, ��� list == null, ���ؿ� List
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
     * ����һ��ָ���� size ���һ�� List Ϊ��� List, �ٽ���Щ��ֺ�� list ����һ�� list �з���
     * @param list ԭ list
     * @param size ��� list �Ĵ�С
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
