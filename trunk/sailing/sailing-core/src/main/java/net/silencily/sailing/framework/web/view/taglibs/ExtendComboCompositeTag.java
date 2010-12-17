/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project framework
 */
package net.silencily.sailing.framework.web.view.taglibs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;

import net.silencily.sailing.framework.web.view.ComboSupportList;
import net.silencily.sailing.utils.MiscUtils;

import org.apache.commons.collections.KeyValue;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.keyvalue.DefaultKeyValue;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.ExpressionEvaluationUtils;


/**
 * �����ı�����ֵ�����չ������֧�� Tag, ��Ҫ�� /publicrsource/web/public/scripts/extendCombo.js ���ʹ��.
 * Note : Ϊ����ԭ���������򱣳ּ�����, �������ֵ�������� {@link ComboSupportList#getSelectedValues()} �л�ȡ,
 * ��� {@link ComboSupportList#getSelectedValues()} ��Ϊ��, value ���ý�ʧЧ, ��Ϊȡ {@link ComboSupportList#getSelectedValues()} �е����һ��Ԫ��
 * @see #getValue()
 * @since 2006-7-21
 * @author java2enterprise
 * @version $Id: ExtendComboCompositeTag.java,v 1.1 2010/12/10 10:54:28 silencily Exp $
 */
public class ExtendComboCompositeTag extends ExtendComboTextTag {

	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = -8263510149618967094L;
	
	public static final String VALUE_NAME_ATTRIBUTE = "valueName";
	public static final String VALUE_TYPE_ATTRIBUTE = "valueType";
	public static final String VALIDATE_ATTRIBUTE = "validate";
	
	protected String valueName;
	protected String valueType;
	protected String validate;
	
	
	/**
	 * @see net.silencily.sailing.framework.web.view.taglibs.coheg.web.view.taglibs.ExtendComboTextTag#getExtendComboSourceJavaScriptArrayContent()
	 */
	protected String getExtendComboSourceJavaScriptArrayContent() throws JspException {
		return getJavaScriptArrayFromComboSupportList(getSourceAsComboSupportList());
	}
	
	public static String getJavaScriptArrayFromComboSupportList(ComboSupportList comboSupportList) {
		StringBuffer javaScriptArrayContent = new StringBuffer();			
		Collection c = new ArrayList();
			
		List keyValues = comboSupportList.convertData2KeyValues();
		for (Iterator iter = keyValues.iterator(); iter.hasNext(); ) {
			KeyValue keyValue = (KeyValue) iter.next();
			StringBuffer buffer = new StringBuffer();
			buffer.append(ComboSupportList.null2empty(keyValue.getKey()));
			buffer.append(ComboSupportList.ELEMENT_SEPARATOR);
			buffer.append(ComboSupportList.null2empty(keyValue.getValue()));		
			c.add(buffer.toString());
		}
				
		for (Iterator iter = c.iterator(); iter.hasNext(); ) {
			String forEach = String.valueOf(iter.next());
			javaScriptArrayContent.append("\"");
			javaScriptArrayContent.append(forEach);
			javaScriptArrayContent.append("\"");
			if (iter.hasNext()) {
				javaScriptArrayContent.append(", ");
			}
		}
			
		return javaScriptArrayContent.toString();
	}
		
	
	/**
	 * 
	 * @return list fill with {@link KeyValue}
	 * @throws JspException
	 */
	private List convertData2KeyValues() throws JspException {
		ComboSupportList comboSupportList = getSourceAsComboSupportList();
		return comboSupportList.convertData2KeyValues();
	}
	
	/**
	 * �� source ת��Ϊ {@link ComboSupportList} ���, ��ʵ��, source Ҳֻ֧�� {@link ComboSupportList} ����
	 */
	private ComboSupportList getSourceAsComboSupportList() throws JspException {
		Object evaledSource = ExpressionEvaluationUtils.evaluate(SOURCE_ATTRIBUTE, source, pageContext);			
		if (evaledSource == null) {
			return new ComboSupportList(new HashMap());
		} 
		if (!ComboSupportList.class.isInstance(evaledSource)) {
			throw new JspException(" �Բ���, ��̬������� source ����Ŀǰֻ֧�� [" + ComboSupportList.class + "] ����");
		}
		return (ComboSupportList) evaledSource;		
	}
	
	/**
	 * @see net.silencily.sailing.framework.web.view.taglibs.coheg.web.view.taglibs.ExtendComboTextTag#getValueObject()
	 */
	protected String getValueObject() throws JspException {
		StringBuffer html = new StringBuffer();		
		html.append("document.getElementsByName('");
		String evaledValueName = evaluateAndEscapeHtml(VALUE_NAME_ATTRIBUTE, valueName, pageContext);
		html.append(evaledValueName);
		html.append("')[0]");
		return html.toString();
	}
	
	/**
	 * @see net.silencily.sailing.framework.web.view.taglibs.coheg.web.view.taglibs.ExtendComboTextTag#getValueObjectHtml()
	 */
	protected String getValueObjectHtml() throws JspException {
		StringBuffer html = new StringBuffer();
		html.append("<input name = \"");
		String evaledValueName = evaluateAndEscapeHtml(VALUE_NAME_ATTRIBUTE, valueName, pageContext);
		html.append(evaledValueName);
		html.append("\" type = \"");
		
		if (StringUtils.isNotBlank(valueType)) {
			String evaledValueType = evaluateAndEscapeHtml(VALUE_TYPE_ATTRIBUTE, valueType, pageContext);
			html.append(evaledValueType);
		} else {
			html.append("hidden");
		}
		
		html.append("\" value = \"");
		html.append(getValue());
		html.append("\" />");
		return html.toString();
	}
	
	private KeyValue lookUpKeyValue() throws JspException {
		String evaledValue = ExpressionEvaluationUtils.evaluateString(VALUE_ATTRIBUTE, value, pageContext);		
		List keyValues = convertData2KeyValues();
		for (Iterator iter = keyValues.iterator(); iter.hasNext(); ) {
			KeyValue keyValue = (KeyValue) iter.next();
			if (StringUtils.equals(evaledValue, String.valueOf(keyValue.getKey()))) {
				return keyValue;
			}
		}
		
		// �������Ҫ��֤, ��ֱ�ӷ��� value
		String evaledValidate = ExpressionEvaluationUtils.evaluateString(VALUE_ATTRIBUTE, value, pageContext);
		if ("false".equals(evaledValidate)) {
			KeyValue keyValue = new DefaultKeyValue(evaledValue, null);
			return keyValue;
		}
		
		return null;
	}
	
	/**
	 * �õ��������ֵ, ע��, Ϊ����ԭ���������򱣳ּ�����, ֵ�������� {@link ComboSupportList#getSelectedValues()} �л�ȡ,
	 * ��� {@link ComboSupportList#getSelectedValues()} ��Ϊ��, value ���ý�ʧЧ, ��Ϊȡ {@link ComboSupportList#getSelectedValues()} �е����һ��Ԫ��
	 */
	private String getValue() throws JspException {
		ComboSupportList comboSupportList = getSourceAsComboSupportList();
		List selectedValues = comboSupportList.getSelectedValues();
		List filteredSelectedValues = filterNullElements(selectedValues);
		if (!CollectionUtils.isEmpty(comboSupportList) && !CollectionUtils.isEmpty(filteredSelectedValues)) {
			Object lastElement = selectedValues.get(filteredSelectedValues.size() - 1);
			return MiscUtils.nullSafeToString(lastElement);
		}
		
		KeyValue keyValue = lookUpKeyValue();
		return keyValue == null ? "" : MiscUtils.nullSafeToString(keyValue.getKey());
	}

	private List filterNullElements(List list) {
		return (List) org.apache.commons.collections.CollectionUtils.select(list, new Predicate() {
			public boolean evaluate(Object object) {
				return object != null;
			}
		});
	}
	
	/**
	 * @see net.silencily.sailing.framework.web.view.taglibs.coheg.web.view.taglibs.ExtendComboTextTag#getTextValue()
	 */
	protected String getTextValue() throws JspException {
		ComboSupportList comboSupportList = getSourceAsComboSupportList();
		List selectedTexts = comboSupportList.getSelectedTexts();
		List filteredSelectedTexts = comboSupportList.getSelectedTexts();
		
		if (!CollectionUtils.isEmpty(comboSupportList) && !CollectionUtils.isEmpty(filteredSelectedTexts)) {
			Object lastElement = selectedTexts.get(filteredSelectedTexts.size() - 1);
			return MiscUtils.nullSafeToString(lastElement);
		}
		
		KeyValue keyValue = lookUpKeyValue();
		return keyValue == null ? "" : MiscUtils.nullSafeToString(keyValue.getValue());
	}
	
	/**
	 * @see net.silencily.sailing.framework.web.view.taglibs.coheg.web.view.taglibs.ExtendComboTextTag#isTextReadonly()
	 */
	protected boolean isTextReadonly() throws JspException {
		return true;
	}

	/**
	 * @see net.silencily.sailing.framework.web.view.taglibs.coheg.web.view.taglibs.ExtendComboTextTag#setValue2TextObject()
	 */
	protected boolean setValue2TextObject() {
		return false;
	}
	
	
	/**
	 * @param validate the validate to set
	 */
	public void setValidate(String validate) {
		this.validate = validate;
	}
	/**
	 * @param valueName the valueName to set
	 */
	public void setValueName(String valueName) {
		this.valueName = valueName;
	}
	/**
	 * @param valueType the valueType to set
	 */
	public void setValueType(String valueType) {
		this.valueType = valueType;
	}
	
	
	
	
	
}
