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
public class SearchDeptTag extends TagSupport {

	private String  code;
	private String  name;
	private String  valueDefault;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getValueDefault() {
		return valueDefault;
	}
	public void setValueDefault(String valueDefault) {
		this.valueDefault = valueDefault;
	}
	public int doEndTag() throws JspException {
		// TODO Auto-generated method stub
		JspWriter out = pageContext.getOut();
		BaseActionForm form = (BaseActionForm)ExpressionEvaluationUtils.evaluate("valueDefault",valueDefault, pageContext);
		String codeDefault=(String)form.getConditionValue(code);
		String nameDefault=(String)form.getConditionValue(name);
		if(StringUtils.isBlank(codeDefault))
			codeDefault="";
		if(StringUtils.isBlank(nameDefault))
			nameDefault="";
		StringBuffer tr=new StringBuffer();
		tr.append("<input name=\"conditions({0}).name\" type=\"hidden\" value=\"{0}\"/>");
		tr.append("<input name=\"conditions({0}).operator\" type=\"hidden\" value=\"=\"/>");
		tr.append("<input name=\"conditions({0}).type\" type=\"hidden\" value=\"java.lang.String\"/>");
		tr.append("<input id=\"{0}\" name=\"conditions({0}).value\" type=\"hidden\" value=\"{1}\" />");
		tr.append("<input name=\"conditions({2}).name\" type=\"hidden\" value=\"\"/>");
		tr.append("<input name=\"conditions({2}).operator\" type=\"hidden\" value=\"=\"/>");
		tr.append("<input name=\"conditions({2}).type\" type=\"hidden\" value=\"java.lang.String\"/>");
		tr.append("<input name=\"conditions({2}).value\" type=\"text\" value=\"{3}\" readonly=\"readonly\"  class=\"readonly\"/>");
		//tr.append("<input type=\"button\" id=\"select_fromtree\" title=\" 点击这里选择部门 \" onClick=\"TreeUtils.selectAndSetDepartment($({4}conditions({0}).value{4}), $({4}conditions({2}).value{4}), TreeUtils.treeTypeSingle)\">");	
		tr.append("<input type=\"button\" id=\"select_fromtree\" title=\" 点击这里选择部门 \" onClick=\"showCgmlTreeWindow({4}{0}{4},{4}conditions({0}).value{4},{4}conditions({2}).value{4},{4}{4},{4}1{4})\">");
		tr.append("<input type=\"button\" id=\"opera_clear\" title=\" 点击清除 \" onclick=\"FormUtils.cleanValues($({4}conditions({0}).value{4}), $({4}conditions({2}).value{4}))\"/> ");
		tr.append("<input name=\"conditions({0}).createAlias\" type=\"hidden\" value=\"false\"/>");
		tr.append("<input name=\"conditions({2}).createAlias\" type=\"hidden\" value=\"false\"/>");
		try {
			out.print(MessageFormat.format(tr.toString(), new Object[]{code,codeDefault,name,nameDefault,"'"}));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return super.doEndTag();
	}
	
}
