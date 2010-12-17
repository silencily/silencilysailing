package net.silencily.sailing.common.crud.tag;

import java.io.IOException;
import java.text.MessageFormat;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import net.silencily.sailing.struts.BaseFormPlus;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.ExpressionEvaluationUtils;


/**
 * 
 * @author zhaoyf
 * @version 1.0
 */
public class PopSelectTag extends TagSupport {

	
	private String formName;
	
	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}
	private static String script1="<script language=\"javascript\">\r\n"+
	"var listObject = new Object();\r\n"+
	"CurrentPage.onLoadSelect = function(){\r\n"+
	"listObject = new ListUtil.Listing('listObject', 'listtable');\r\n"+
	"listObject.init();\r\n"+
	"top.definedWin.selectListing = function(inum) {\r\n";
	
		//<c:if test="${theForm.popSelectType == 'radio'}">listObject.selectWindow(1);</c:if>
		//<c:if test="${theForm.popSelectType == 'checkbox'}">listObject.selectWindow(2);</c:if>
	private static String script2="}\r\n"+
	"top.definedWin.closeListing = function(inum) {\r\n"+
	"listObject.selectWindow();\r\n"+
	"}\r\n"+
"}\r\n"+
"CurrentPage.onLoadSelect();\r\n"+
	"</script>"; 
	public int doEndTag() throws JspException {
		// TODO Auto-generated method stub
		JspWriter out = pageContext.getOut();
		BaseFormPlus form = (BaseFormPlus)ExpressionEvaluationUtils.evaluate("formName",formName, pageContext);
		String selcetFuction=null;
		if(StringUtils.isBlank(form.getPopSelectType()))
			return super.doEndTag();
		if("only".equals(form.getPopSelectType()))
			selcetFuction="listObject.selectWindow(1);";
		if("multi".equals(form.getPopSelectType()))
			selcetFuction="listObject.selectWindow(2);";
		try {
			out.print(script1);
			out.print(selcetFuction);
			out.print(script2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.doEndTag();
	}

}
