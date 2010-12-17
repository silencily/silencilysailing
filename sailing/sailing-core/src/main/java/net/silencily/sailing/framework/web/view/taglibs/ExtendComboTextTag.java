/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project framework
 */
package net.silencily.sailing.framework.web.view.taglibs;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.ExpressionEvaluationUtils;
import org.springframework.web.util.HtmlUtils;

import net.silencily.sailing.utils.UUIDGenerator;

/**
 * 只包含文本域的扩展下拉框支持 Tag, 需要与 /publicrsource/web/public/scripts/extendCombo.js 配合使用
 * @since 2006-7-21
 * @author java2enterprise
 * @version $Id: ExtendComboTextTag.java,v 1.1 2010/12/10 10:54:28 silencily Exp $
 */
public class ExtendComboTextTag extends TagSupport {

	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = 8750195491419428712L;
		
	public static final String TEXT_NAME_ATTRIBUTE = "textName";
	public static final String VALUE_ATTRIBUTE = "value";
	public static final String SOURCE_ATTRIBUTE = "source";
	public static final String TEXT_EXTRA_ATTRIBUTE = "textExtra";
	public static final String TEXT_READONLY_ATTRIBUTE = "textReadonly";
	public static final String BUTTON_NAME_ATTRIBUTE = "buttonName";
	
	
	protected static final String INPUT_TEXT_ID = "input_text";
	protected static final String INPUT_SELECT_ID = "input_select";
	
	protected String textName = "";
	protected String value = "";
	protected String source = "";
	protected String textExtra = "";
	protected String textReadonly = "";
	protected String buttonName = "";
	
	
	/**
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	public int doStartTag() throws JspException {
		StringBuffer html = new StringBuffer();
		// 增加 hidden 域
		html.append(getValueObjectHtml());
		
		html.append("\n<input type = \"text\" ");
		html.append(" name = \"");
		String evaledTextName = evaluateAndEscapeHtml(TEXT_NAME_ATTRIBUTE, textName, pageContext);
		html.append(evaledTextName);
		html.append("\" value = \""); 
		html.append(getTextValue());
		html.append("\" id = \"");
		html.append(INPUT_TEXT_ID);
		html.append("\" ");

		String uuid = new UUIDGenerator().generate().toString();
		String javascriptArrayName = "ExtendComboText.source" + uuid;
		
		// 构造 javascript 函数
		StringBuffer eventHandler = new StringBuffer();
		eventHandler.append("ExtendCombo.getOptionStatic(document.getElementsByName('");
		eventHandler.append(evaledTextName);
		eventHandler.append("')[0], this, ");
		if (StringUtils.isNotBlank(getValueObject())) {
			eventHandler.append(getValueObject());
		} else {
			eventHandler.append("null");
		}
		eventHandler.append(", ");
		eventHandler.append(javascriptArrayName);
		eventHandler.append(")");
		
		html.append(" onkeyup = \"");
		html.append(eventHandler);
		html.append("\" ");
		if (isTextReadonly()) {
			html.append(" class = \"readonly\" readonly = \"readonly\" ");
		}
		if (StringUtils.isNotBlank(textExtra)) {
			String evaledExtra = evaluateAndEscapeHtml(TEXT_EXTRA_ATTRIBUTE, textExtra, pageContext);
			html.append(evaledExtra);
		}
		html.append(" />");
		
		html.append("<input type=\"button\" id=\"");
		html.append(INPUT_SELECT_ID);
		html.append("\" name = \"");
		html.append(evaluateAndEscapeHtml(BUTTON_NAME_ATTRIBUTE, buttonName, pageContext));
		html.append("\" title=\"点击这里弹出下拉选择框\" onclick =\"");
		html.append(eventHandler);
		html.append("\" />");
		
		// 构造下拉框需要的数据, 是一个 Javascript Array
		html.append("\n<script language=\"javascript\">\n");
		html.append("if (ExtendComboText == null) { var  ExtendComboText = {}; } \n");
		
		html.append(javascriptArrayName);
		html.append(" = [");
		html.append(getExtendComboSourceJavaScriptArrayContent());
		html.append("]; \n");
		
		html.append("</script>\n");
				
		try {
			pageContext.getOut().write(html.toString());
		} catch (IOException e) {
			throw new JspException(e);
		}
		
		return SKIP_BODY;
	}
	
	protected String evaluateAndEscapeHtml(
		String attributeName, 
		String attributeValue, 
		PageContext pageContext) 
		throws JspException {
		String escapedHtml = HtmlUtils.htmlEscape(ExpressionEvaluationUtils.evaluateString(attributeName, attributeValue, pageContext));
		return StringUtils.trimToEmpty(escapedHtml);
	}
	
	/**
	 * 得到文本域的值
	 * @return text value
	 * @throws JspException  if any exception happend
	 */
	protected String getTextValue() throws JspException {
		return evaluateAndEscapeHtml(VALUE_ATTRIBUTE, value, pageContext);
	}
	
	/**
	 * 文本域是否只读
	 * @return
	 * @throws JspException
	 */
	protected boolean isTextReadonly() throws JspException {
		String isTextReadonly = evaluateAndEscapeHtml(VALUE_ATTRIBUTE, textReadonly, pageContext);
		return Boolean.valueOf(isTextReadonly).booleanValue();
	}
	
	/**
	 * 得到存储值域的 html
	 * @return the html
	 * @throws JspException if any exception happend
	 */
	protected String getValueObjectHtml() throws JspException {
		return "";
	}
	
	/**
	 * 得到存储值的对象
	 * @return the object
	 * @throws JspException  if any exception happend
	 */
	protected String getValueObject() throws JspException {
		return "";
	}
			
	/**
	 * 得到下拉框的数据来源
	 * @return the string content
	 * @throws JspException if any exception happend
	 */
	protected String getExtendComboSourceJavaScriptArrayContent() throws JspException {
		StringBuffer javaScriptArrayContent = new StringBuffer();
		
		Collection c = new ArrayList();
		Object evaledSource = ExpressionEvaluationUtils.evaluate(SOURCE_ATTRIBUTE, source, pageContext);		
		if (evaledSource == null) {
			c.add("");
		}else if (Collection.class.isInstance(evaledSource)) {
			c.addAll((Collection) evaledSource); 
		} else if (evaledSource.getClass().isArray()) {
			for (int i = 0; i < Array.getLength(evaledSource); i++) {
				c.add(Array.get(evaledSource, i));
			}
		} else {
			c.add(evaledSource.toString());
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
	 * @param textExtra the textExtra to set
	 */
	public void setTextExtra(String extra) {
		this.textExtra = extra;
	}


	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}


	/**
	 * @param textName the textName to set
	 */
	public void setTextName(String textName) {
		this.textName = textName;
	}


	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @param buttonName the buttonName to set
	 */
	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}

	/**
	 * @return the textReadonly
	 */
	public String getTextReadonly() {
		return textReadonly;
	}

	/**
	 * @param textReadonly the textReadonly to set
	 */
	public void setTextReadonly(String textReadonly) {
		this.textReadonly = textReadonly;
	}
	
	
	
	
}
