package net.silencily.sailing.common.crud.tag;

import java.io.IOException;
import java.text.MessageFormat;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import net.silencily.sailing.framework.web.struts.BaseActionForm;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.ExpressionEvaluationUtils;


/**
 * @author zhaoyf
 *
 */
public class SearchTextTag extends TagSupport {

	private static String enter="\r\n";
	private String id;
	private String name;
	private String elName;
	private String oper;
	private String type;
	private String valueDefault="";
	private String style="";
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = "style=\""+style+"\"";
	}
	public String getValueDefault() {
		return valueDefault;
	}
	public void setValueDefault(String valueDefault) {
		this.valueDefault = valueDefault;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOper() {
		return oper;
	}
	public void setOper(String oper) {
		this.oper = oper;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int doStartTag() throws JspException {
		// TODO Auto-generated method stub
		JspWriter out = pageContext.getOut();
		BaseActionForm form = (BaseActionForm)ExpressionEvaluationUtils.evaluate("valueDefault",valueDefault, pageContext);
		if(StringUtils.isBlank(name))
			name=(String)ExpressionEvaluationUtils.evaluate("elName",elName, pageContext);
		Object aa=form.getConditionValue(name+oper);
		if(aa==null)
			aa="";
		if(StringUtils.isBlank(style))
		{
		if(type.indexOf("Long")!=-1)
			style="style=\"text-align:right\"";
		if(type.indexOf("Double")!=-1)
			style="style=\"text-align:right\"";
		if(type.indexOf("Integer")!=-1)
			style="style=\"text-align:right\"";
		if(Number.class.isAssignableFrom(aa.getClass()))
			style="style=\"text-align:right\"";
		}
		if(!StringUtils.isBlank(id))
			id="id=\""+id+"\"";
		else
			id="";
		
		
		StringBuffer tr=new StringBuffer();
		tr.append("<input name=\"conditions({0}{1}).name\" type=\"hidden\" value=\"{0}\"/>"+enter);
		tr.append("<input name=\"conditions({0}{1}).operator\" type=\"hidden\" value=\"{1}\"/>"+enter);
		tr.append("<input name=\"conditions({0}{1}).type\" type=\"hidden\" value=\"{2}\"/>"+enter);
		tr.append("<input {5} name=\"conditions({0}{1}).value\" value=\"{3}\" {4}/>");
//		if(name.indexOf(".")!=name.lastIndexOf("."))
//			tr.append("<input name=\"conditions({0}{1}).createAlias\" value=\"false\" type=\"hidden\"/>"+enter);
		try {
			out.print(MessageFormat.format(tr.toString(), new Object[]{name,oper,type,aa.toString(),style,id}));
			style="";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		name = null;
		return SKIP_BODY;//super.doEndTag();
	}

	public int doEndTag() {
		return EVAL_PAGE;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getElName() {
		return elName;
	}
	public void setElName(String elName) {
		this.elName = elName;
	}

}
