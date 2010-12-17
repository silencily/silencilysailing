package net.silencily.sailing.common.crud.tag;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import net.silencily.sailing.framework.web.struts.BaseActionForm;
import net.silencily.sailing.utils.Tools;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.ExpressionEvaluationUtils;


/**
 * @author zhaoyf
 *
 */
public class SearchTimeTag extends TagSupport {

	private static String enter="\r\n";
	private String name;
	private String elName;
	private String valueDefault;
	private String oper;
	private String pattern;
	
	private String type;
	private String range;
	private String dataType;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValueDefault() {
		return valueDefault;
	}
	public void setValueDefault(String valueDefault) {
		this.valueDefault = valueDefault;
	}
	public String getOper() {
		return oper;
	}
	public void setOper(String oper) {
		this.oper = oper;
	}
	
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public int doEndTag() throws JspException {
		// TODO Auto-generated method stub
		JspWriter out = pageContext.getOut();
		BaseActionForm form = (BaseActionForm)ExpressionEvaluationUtils.evaluate("valueDefault",valueDefault, pageContext);
		if(StringUtils.isBlank(name))
			name=(String)ExpressionEvaluationUtils.evaluate("elName",elName, pageContext);		
		//range = null||true and type=null||date
		//区间
		if(StringUtils.isBlank(range)||"true".equals(range)){
			
			StringBuffer tr=new StringBuffer();
			tr.append("<input name=\"conditions({0}{1}).name\" type=\"hidden\" value=\"{0}\"/>"+enter);
			tr.append("<input name=\"conditions({0}{1}).operator\" type=\"hidden\" value=\"{1}\"/>"+enter);
			//追加字符串类型
			if(StringUtils.isBlank(type)||"Date".equals(type)){
				tr.append("<input name=\"conditions({0}{1}).type\" type=\"hidden\" value=\"java.util.Date\"/>"+enter);
			}else{
				tr.append("<input name=\"conditions({0}{1}).type\" type=\"hidden\" value=\"java.lang.String\"/>"+enter);
			}
			tr.append("<input name=\"conditions({0}{1}).value\"  type=\"text\" id=\"input_text\" value=\"{2}\" class=\"readonly\" readonly=\"readonly\" style=\"text-align:left\"/>");
			//追加时间控件类型
			if((!StringUtils.isBlank(dataType))&&("12".equals(dataType)||"13".equals(dataType) ||"1".equals(dataType))){
				tr.append("<input type=\"button\" id=\"input_date\" name=\"powerWorkTime_button\" ");
				if("12".equals(dataType)){
					tr.append(" title=\" 点击这里选择月份 \" ");
				}
				if("13".equals(dataType)){
					tr.append(" title=\" 点击这里选择季度 \" ");
				}
				if("14".equals(dataType)){
					tr.append(" title=\" 点击这里选择年份 \" ");
				}
				tr.append(" onclick=\""
						+ "DateComponent.setSpecialDay("
						+ dataType
						+","
						+"this, $({3}conditions({0}{1}).value{3}));\" />"
						+enter);
			}else{
				if("yyyy-MM-dd".equals(this.pattern)){
					tr.append("<input type=\"button\" id=\"input_date\" name=\"powerWorkTime_button\" title=\" 点击这里选择日期 \" onclick=\"DateComponent.setSpecialDay(1,this, $({3}conditions({0}{1}).value{3}));\" />"+enter);
				}
				if("yyyy-MM-dd hh:mm:ss".equals(this.pattern) || "yyyy-MM-dd HH:mm:ss".equals(this.pattern)){
					this.pattern = "yyyy-MM-dd HH:mm:ss";	//小时12进制改24进制
					tr.append("<input type=\"button\" id=\"input_date\" name=\"powerWorkTime_button\" title=\" 点击这里选择日期时间 \" onclick=\"DateComponent.setSpecialDay(1,this, $({3}conditions({0}{1}).value{3}),{3}{3});\" />"+enter);
				}
				if("yyyy-MM-dd hh:mm".equals(this.pattern) || "yyyy-MM-dd HH:mm".equals(this.pattern)){
					this.pattern = "yyyy-MM-dd HH:mm";	//小时12进制改24进制
					tr.append("<input type=\"button\" id=\"input_date\" name=\"powerWorkTime_button\" title=\" 点击这里选择日期时间 \" onclick=\"DateComponent.setSpecialDay(1,this, $({3}conditions({0}{1}).value{3}),{3}{3},false);\" />"+enter);
				}
				if(" hh:mm:ss".equals(this.pattern) || " HH:mm:ss".equals(this.pattern)){
					this.pattern = " HH:mm:ss";				//小时12进制改24进制
					tr.append("<input type=\"button\" id=\"input_date\" name=\"powerWorkTime_button\" title=\" 点击这里选择时间 \" onclick=\"DateComponent.setSpecialDay(1,this,{3}{3},$({3}conditions({0}{1}).value{3}));\" />"+enter);
				}
			}
			try {
				String date1 = null;
				String date2 = null;
				if(form.getConditionValue(name+">=") instanceof Date){
					date1 = Tools.getTheTime((Date)form.getConditionValue(name+">="), pattern);
				}else{
					date1 = toString(form.getConditionValue(name+">="));
				}
				if(form.getConditionValue(name+"<=") instanceof Date){
					date2 = Tools.getTheTime((Date)form.getConditionValue(name+"<="), pattern);
				}else{
					date2 = toString(form.getConditionValue(name+"<="));
				}
				out.print(MessageFormat.format(tr.toString(), new Object[]{name,">=",date1,"'"}));
				out.print(" 至 ");
				out.print(MessageFormat.format(tr.toString(), new Object[]{name,"<=",date2,"'"}));
				out.print(" <input type=\"button\" id=\"opera_clear\" title=\" 点击清除 \" onclick=\"FormUtils.cleanValues($('conditions("+name+">="+").value'), $('conditions("+name+">="+").value')); FormUtils.cleanValues($('conditions("+name+"<="+").value'), $('conditions("+name+"<="+").value'))\"/>");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			//非区间处理
			StringBuffer tr=new StringBuffer();
			tr.append("<input name=\"conditions({0}{1}).name\" type=\"hidden\" value=\"{0}\"/>"+enter);
			tr.append("<input name=\"conditions({0}{1}).operator\" type=\"hidden\" value=\"{1}\"/>"+enter);
			//追加字符串类型
			if(StringUtils.isBlank(type)||"Date".equals(type)){
				tr.append("<input name=\"conditions({0}{1}).type\" type=\"hidden\" value=\"java.util.Date\"/>"+enter);
			}else{
				tr.append("<input name=\"conditions({0}{1}).type\" type=\"hidden\" value=\"java.lang.String\"/>"+enter);
			}
			tr.append("<input name=\"conditions({0}{1}).value\"  type=\"text\" id=\"input_text\" value=\"{2}\" class=\"readonly\" readonly=\"readonly\" style=\"text-align:left\"/>");
			//追加时间控件类型
			if((!StringUtils.isBlank(dataType))&&("12".equals(dataType)||"13".equals(dataType) ||"1".equals(dataType))){
				tr.append("<input type=\"button\" id=\"input_date\" name=\"powerWorkTime_button\" ");
				if("12".equals(dataType)){
					tr.append(" title=\" 点击这里选择月份 \" ");
				}
				if("13".equals(dataType)){
					tr.append(" title=\" 点击这里选择季度 \" ");
				}
				if("14".equals(dataType)){
					tr.append(" title=\" 点击这里选择年份 \" ");
				}
				tr.append(" onclick=\""
						+ "DateComponent.setSpecialDay("
						+ dataType
						+","
						+"this, $({3}conditions({0}{1}).value{3}));\" />"
						+enter);
			}else{
				if("yyyy-MM-dd".equals(this.pattern)){
					tr.append("<input type=\"button\" id=\"input_date\" name=\"powerWorkTime_button\" title=\" 点击这里选择日期 \" onclick=\"DateComponent.setSpecialDay(1,this, $({3}conditions({0}{1}).value{3}));\" />"+enter);
				}
				if("yyyy-MM-dd hh:mm:ss".equals(this.pattern) || "yyyy-MM-dd HH:mm:ss".equals(this.pattern)){
					this.pattern = "yyyy-MM-dd HH:mm:ss";	//小时12进制改24进制
					tr.append("<input type=\"button\" id=\"input_date\" name=\"powerWorkTime_button\" title=\" 点击这里选择日期时间 \" onclick=\"DateComponent.setSpecialDay(1,this, $({3}conditions({0}{1}).value{3}),{3}{3});\" />"+enter);
				}
				if("yyyy-MM-dd hh:mm".equals(this.pattern) || "yyyy-MM-dd HH:mm".equals(this.pattern)){
					this.pattern = "yyyy-MM-dd HH:mm";	//小时12进制改24进制
					tr.append("<input type=\"button\" id=\"input_date\" name=\"powerWorkTime_button\" title=\" 点击这里选择日期时间 \" onclick=\"DateComponent.setSpecialDay(1,this, $({3}conditions({0}{1}).value{3}),{3}{3},false);\" />"+enter);
				}
				if(" hh:mm:ss".equals(this.pattern) || " HH:mm:ss".equals(this.pattern)){
					this.pattern = " HH:mm:ss";				//小时12进制改24进制
					tr.append("<input type=\"button\" id=\"input_date\" name=\"powerWorkTime_button\" title=\" 点击这里选择时间 \" onclick=\"DateComponent.setSpecialDay(1,this,{3}{3},$({3}conditions({0}{1}).value{3}));\" />"+enter);
				}
			}
			try {
				String date1 = null;
				if(form.getConditionValue(name+"=") instanceof Date){
					date1 = Tools.getTheTime((Date)form.getConditionValue(name+"="), pattern);
				}else{
					date1 = toString(form.getConditionValue(name+"="));
				}
				out.print(MessageFormat.format(tr.toString(), new Object[]{name,"=",date1 ,"'"}));
				out.print(" <input type=\"button\" id=\"opera_clear\" title=\" 点击清除 \" onclick=\"FormUtils.cleanValues($('conditions("+name+"="+").value'), $('conditions("+name+"="+").value'));\"/>");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return super.doEndTag();
	}
	public String getElName() {
		return elName;
	}
	public void setElName(String elName) {
		this.elName = elName;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	private String toString(Object obj){
		return obj == null ? "" : obj.toString();
	}
}
