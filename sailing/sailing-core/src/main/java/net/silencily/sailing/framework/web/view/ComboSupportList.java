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
 * 下拉框支持 List, 使用时构造此类型或使用 map 做为构造子, 在页面上这样使用 <p/>
 *  &lt;select name="combo" comboSupportList="&lt;c:out value = "${requestScope['comboSupportList']}" /&gt;"&gt;&lt;/select&gt;	
 * <p/> 同时导入 /web/public/scripts/global.js, global.js 负责解析生成下拉框选项 
 * @since 2006-7-14
 * @author java2enterprise
 * @version $Id: ComboSupportList.java,v 1.1 2010/12/10 10:54:28 silencily Exp $
 */
public class ComboSupportList extends ArrayList {
	
	/** 各元素之间的分隔符 */
	public static final String ELEMENT_SEPARATOR = "|||";
	
	/** text 和 value 之间的分隔符 */
	public static final String TEXT_VALUE_SEPARATOR = "###";

	
	private static final long serialVersionUID = -7130798048652042608L;
	
	/** 是否可多选 */
	private boolean multiple = false;
	
	/** 下拉框 value 在 list 元素中的属性名 */
	private String valueProperty;
	
	/** 下拉框 text 在 list 元素中的属性名 */
	private String textProperty;
	
	/** 下拉框第一个选项的 value, 可选 */
	private String headerValue;
	
	/** 下拉框第一个选项的 text, 可选 */
	private String headerText;
	
	/** 用于存储下拉框内容的 map, 是另一种可选方案 */
	private Map contentMap = new HashMap();
	
	/** 需要选中的值集合 */
	private List selectedValues = new ArrayList();
	
	/** 选中的文本集合, 用于回显到页面上 */
	private List selectedTexts = new ArrayList();
	
	/**
	 * 构造一个下拉框支持 List
	 * @param valueProperty 下拉框 value 在 list 元素中的属性名
	 * @param textProperty 下拉框 text 在 list 元素中的属性名
	 */
	public ComboSupportList(String valueProperty, String textProperty) {
		Assert.notNull(valueProperty);
		Assert.notNull(textProperty);		
		this.valueProperty = valueProperty;
		this.textProperty = textProperty;
	}
	
	/**
	 * 使用 map 构造出一个下拉框, 使用 map 的 key 作为 option's value, map 的 value 作为 option's text
	 * @param contentMap the contentMap
	 */
	public ComboSupportList(Map contentMap) {
		Assert.notNull(contentMap, " content Map required. ");
		this.contentMap = contentMap;
	}
	
	
	
	/**
	 * 重载 toString 方法以便 javascript 解析
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
	 * 将数据转换为 {@link KeyValue} 的 List
	 * @return list fill with {@link KeyValue}
	 */
	public List convertData2KeyValues() {		
		List keyValues = new ArrayList();
		if (!CollectionUtils.isEmpty(getContentMap())) {				
			// 使用 map 构造
			Iterator iter = getContentMap().entrySet().iterator();
			while (iter.hasNext()) {					
				Map.Entry entry = (Entry) iter.next();
				KeyValue keyValue = new DefaultKeyValue(entry.getKey(), entry.getValue());
				keyValues.add(keyValue);
			}
		} else {
			// 使用 list 中的元素构造
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
	 * 得到 list 大小
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
	 * 得的选中选项的 text 集合, 此集合由 {@link #selectedValues} 计算得到
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
	 * FIXME view 层尚未实现
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
