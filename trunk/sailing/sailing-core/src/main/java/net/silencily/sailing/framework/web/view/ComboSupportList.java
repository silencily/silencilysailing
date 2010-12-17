/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project framework
 */
package net.silencily.sailing.framework.web.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.KeyValue;
import org.apache.commons.collections.keyvalue.DefaultKeyValue;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * ������֧�� List, ʹ��ʱ��������ͻ�ʹ�� map ��Ϊ������, ��ҳ��������ʹ�� <p/>
 *  &lt;select name="combo" comboSupportList="&lt;c:out value = "${requestScope['comboSupportList']}" /&gt;"&gt;&lt;/select&gt;	
 * <p/> ͬʱ���� /web/public/scripts/global.js, global.js �����������������ѡ�� 
 * @since 2006-7-14
 * @author java2enterprise
 * @version $Id: ComboSupportList.java,v 1.1 2010/12/10 10:54:28 silencily Exp $
 */
public class ComboSupportList extends ArrayList {
	
	/** ��Ԫ��֮��ķָ��� */
	public static final String ELEMENT_SEPARATOR = "|||";
	
	/** text �� value ֮��ķָ��� */
	public static final String TEXT_VALUE_SEPARATOR = "###";

	
	private static final long serialVersionUID = -7130798048652042608L;
	
	/** �Ƿ�ɶ�ѡ */
	private boolean multiple = false;
	
	/** ������ value �� list Ԫ���е������� */
	private String valueProperty;
	
	/** ������ text �� list Ԫ���е������� */
	private String textProperty;
	
	/** �������һ��ѡ��� value, ��ѡ */
	private String headerValue;
	
	/** �������һ��ѡ��� text, ��ѡ */
	private String headerText;
	
	/** ���ڴ洢���������ݵ� map, ����һ�ֿ�ѡ���� */
	private Map contentMap = new HashMap();
	
	/** ��Ҫѡ�е�ֵ���� */
	private List selectedValues = new ArrayList();
	
	/** ѡ�е��ı�����, ���ڻ��Ե�ҳ���� */
	private List selectedTexts = new ArrayList();
	
	/**
	 * ����һ��������֧�� List
	 * @param valueProperty ������ value �� list Ԫ���е�������
	 * @param textProperty ������ text �� list Ԫ���е�������
	 */
	public ComboSupportList(String valueProperty, String textProperty) {
		Assert.notNull(valueProperty);
		Assert.notNull(textProperty);		
		this.valueProperty = valueProperty;
		this.textProperty = textProperty;
	}
	
	/**
	 * ʹ�� map �����һ��������, ʹ�� map �� key ��Ϊ option's value, map �� value ��Ϊ option's text
	 * @param contentMap the contentMap
	 */
	public ComboSupportList(Map contentMap) {
		Assert.notNull(contentMap, " content Map required. ");
		this.contentMap = contentMap;
	}
	
	
	
	/**
	 * ���� toString �����Ա� javascript ����
	 * @see /web/public/scripts/Global.js#Global.populateCombos
	 * @see java.util.AbstractCollection#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();	
		buffer.append(multiple);
		buffer.append(ELEMENT_SEPARATOR);
		
		if (headerValue != null || headerText != null) {
			buffer.append(null2empty(headerValue));
			buffer.append(TEXT_VALUE_SEPARATOR);
			buffer.append(null2empty(headerText));
			buffer.append(TEXT_VALUE_SEPARATOR);
			buffer.append(true);
			buffer.append(ELEMENT_SEPARATOR);
		}
		
		List keyValues = convertData2KeyValues();
		for (Iterator iter = keyValues.iterator(); iter.hasNext(); ) {
			KeyValue keyValue = (KeyValue) iter.next();
			buffer.append(null2empty(keyValue.getKey()));
			buffer.append(TEXT_VALUE_SEPARATOR);
			buffer.append(null2empty(keyValue.getValue()));
			buffer.append(TEXT_VALUE_SEPARATOR);
			
			boolean selected = selectedValues.contains(keyValue.getKey());
			buffer.append(selected);						
			
			buffer.append(ELEMENT_SEPARATOR);
		}
				
		String string = buffer.toString();
		return string.substring(0, string.length() - ELEMENT_SEPARATOR.length());
	}
	
	/**
	 * ������ת��Ϊ {@link KeyValue} �� List
	 * @return list fill with {@link KeyValue}
	 */
	public List convertData2KeyValues() {		
		List keyValues = new ArrayList();
		if (!CollectionUtils.isEmpty(getContentMap())) {				
			// ʹ�� map ����
			Iterator iter = getContentMap().entrySet().iterator();
			while (iter.hasNext()) {					
				Map.Entry entry = (Entry) iter.next();
				KeyValue keyValue = new DefaultKeyValue(entry.getKey(), entry.getValue());
				keyValues.add(keyValue);
			}
		} else {
			// ʹ�� list �е�Ԫ�ع���
			Iterator i = iterator();
			boolean hasNext = i.hasNext();
			while (hasNext) {
				Object forEach = i.next();
				try {
					if (forEach == this) {
						continue;
					}
					hasNext = i.hasNext();
					Object key = PropertyUtils.getProperty(forEach, getValueProperty());
					Object value = PropertyUtils.getProperty(forEach, getTextProperty());
					KeyValue keyValue = new DefaultKeyValue(key, value);
					keyValues.add(keyValue);
				} catch (Exception e) {
					throw new RuntimeException(e);
				} 
			}
		}

		return keyValues;
	}
	
	/**
	 * �õ� list ��С
	 * @return size of list
	 */
	public int getSize() {
		return size();
	}
	
	/**
	 * @see java.util.ArrayList#isEmpty()
	 */
	public boolean isEmpty() {
		return super.isEmpty() && (contentMap == null || contentMap.isEmpty()) ;
	}

	public static String null2empty(Object o) {
		return o == null ? "" : o.toString();
	}
	
	/**
	 * �õ�ѡ��ѡ��� text ����, �˼����� {@link #selectedValues} ����õ�
	 * @return the selectedTexts
	 */
	public List getSelectedTexts() {
		return selectedTexts;
	}

	/**
	 * @return the headValue
	 */
	public String getHeaderValue() {
		return headerValue;
	}

	/**
	 * @param headValue the headValue to set
	 */
	public void setHeaderValue(String headValue) {
		this.headerValue = headValue;
	}

	/**
	 * @return the textProperty
	 */
	public String getTextProperty() {
		return textProperty;
	}

	/**
	 * @return the textValue
	 */
	public String getHeaderText() {
		return headerText;
	}

	/**
	 * @param textValue the textValue to set
	 */
	public void setHeaderText(String textValue) {
		this.headerText = textValue;
	}

	/**
	 * @return the valueProperty
	 */
	public String getValueProperty() {
		return valueProperty;
	}

	/**
	 * @return the selectedValues
	 */
	public List getSelectedValues() {
		return selectedValues;
	}

	/**
	 * @param selectedValues the selectedValues to set
	 */
	public void setSelectedValues(List selectedValues) {
		this.selectedValues = selectedValues;
		// set selected texts
		List keyValues = convertData2KeyValues();
		for (Iterator iter = keyValues.iterator(); iter.hasNext(); ) {
			KeyValue keyValue = (KeyValue) iter.next();
			if (selectedValues.contains(keyValue.getKey())) {
				selectedTexts.add(keyValue.getValue());
			}
		}
	}

	/**
	 * @return the multiple
	 */
	public boolean isMultiple() {
		return multiple;
	}

	/**
	 * FIXME view ����δʵ��
	 * @param multiple the multiple to set
	 */
	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	/**
	 * @return the contentMap
	 */
	public Map getContentMap() {
		return contentMap;
	}

}
