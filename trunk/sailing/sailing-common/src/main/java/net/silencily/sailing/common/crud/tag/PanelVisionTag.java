package net.silencily.sailing.common.crud.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import net.silencily.sailing.security.SecurityContextInfo;
import net.silencily.sailing.security.model.CurrentUser;

import org.springframework.web.util.ExpressionEvaluationUtils;


/**
 * 描述：如果传入的selectId为一个javascript方法的时候，当TAB页被过滤掉的话，会出现BUG，建议不用
 * 
 * @author wenjb
 * @version 1.0
 */
public class PanelVisionTag extends TagSupport {
	/**
	 * 描述： 属性名：serialVersionUID 属性类型：long
	 */
	private static final long serialVersionUID = -3192465252187148383L;

	private String panelList = "";
	private String selectId = "";
	private String elementId= "";

	private List objectpanelList = new ArrayList(0);
	private String objectSelectId = "";
	private String objectElementId= "";
	
	//获取当前上下文
	private String qware="";
	
	/**
	 * 初始化解析所有传入值对象值
	 * 
	 * @throws JspException
	 */
	private void objectInit() throws JspException {
		objectpanelList = (List) ExpressionEvaluationUtils.evaluate(
				"panelList", panelList, pageContext);
		objectSelectId = ExpressionEvaluationUtils.evaluateString("selectId",
				selectId, pageContext);
		objectElementId = ExpressionEvaluationUtils.evaluateString("elementId",
				elementId, pageContext);
		//计算${initParam['publicResourceServer']}
		qware =(String) pageContext.getAttribute("publicResourceServer");
	}

	/**
	 * 生成标签主函数
	 */
	public int doStartTag() throws JspException {
		try {
			// 实例化相关参数
			StringBuffer html = new StringBuffer();
			objectInit();
			// 循环处理MAP中的URL，获取权限
			String urlarray = getUrlArray(objectpanelList, objectSelectId,objectElementId);
			html.append(urlarray);
			pageContext.getOut().print(html.toString());
		} catch (Exception e) {
			throw new JspTagException("SimpleTag: " + e.getMessage());
		}
		return SKIP_BODY;
	}

	public int doEndTag() {
		return EVAL_PAGE;
	}

	private String getUrlArray(List objectpanelList,String selectId,String objectElementId){
		StringBuffer html = new StringBuffer();
		html.append("<script type=\"text/javascript\">");
		html.append("var arr = new Array();");
		//定义临时变量
		int arrIndex=0;

		//如果传进来的是一个方法，则直接显示这个方法
		//默认显示页为0
		if("".equals(selectId)||null==selectId){
			selectId="0";
		}
		//默认显示ID
		String id=selectId;
		
		
		// 循环处理MAP中的URL
		for(int i=0;i<objectpanelList.size();i++){
			//得到嵌套的数组
			ArrayList temp=(ArrayList) objectpanelList.get(i);
			String name=(String)(temp.toArray()[0]);
			String url=(String)(temp.toArray()[1]);
			HttpSession session = SecurityContextInfo.getSession();
			CurrentUser currentUser = (CurrentUser) session
					.getAttribute("currentUser");
			HashMap urlPermissions = currentUser.getUrlPermissions();
			//javascript数组显示
			if(urlPermissions.containsKey(url)){
				//有权限, 拼装如下字符串
				//html.append("arr[0] = [\" 列表 \", \"\", \"<c:url value = '/hr/empTraRecAction.do?step=list&paginater.page=0'/>\"];");
				html.append("arr[");
				html.append(arrIndex);
				html.append("] = [\"");
				html.append(name);
				html.append("\", \"\", \"");
				html.append(qware+url);
				html.append("\"];");	
				//修改selectId的值
				//如果传入的是方法，则不处理
				if(selectId.indexOf("(")!=-1){
					if(Integer.parseInt(selectId)==i){
						id=Integer.toString(arrIndex);
					}					
				}
				arrIndex++;
			}
		}
		//拼装如下字符串
		//html.append("var panel = new Panel.panelObject(\"panel\", arr, 0, \"<c:out value = \"${initParam['publicResourceServer']}/image/main/\" />\");");
		html.append("var panel = new Panel.panelObject(\"panel\", arr, document.getElementById(\"index\").value");
		html.append(id);
		html.append(", \"");
		html.append(qware);
		html.append("/image/main/\");");
		//拼装如下字符串
		//html.append("document.getElementById(\"divId_panel\").innerHTML = panel.display();");
		if("".equals(objectElementId)||null==objectElementId){
			objectElementId="divId_panel";
		}
		html.append("document.getElementById(\"");
		html.append(objectElementId);
		html.append("\").innerHTML = panel.display();");		
		html.append("Global.displayOperaButton = function(){ };");
		return html.toString();
	}

	public List getObjectpanelList() {
		return objectpanelList;
	}

	public void setObjectpanelList(List objectpanelList) {
		this.objectpanelList = objectpanelList;
	}

	public String getObjectSelectId() {
		return objectSelectId;
	}

	public void setObjectSelectId(String objectSelectId) {
		this.objectSelectId = objectSelectId;
	}

	public String getpanelList() {
		return panelList;
	}

	public void setpanelList(String panelList) {
		this.panelList = panelList;
	}

	public String getSelectId() {
		return selectId;
	}

	public void setSelectId(String selectId) {
		this.selectId = selectId;
	}

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public String getObjectElementId() {
		return objectElementId;
	}

	public void setObjectElementId(String objectElementId) {
		this.objectElementId = objectElementId;
	}

	public String getPanelList() {
		return panelList;
	}

	public void setPanelList(String panelList) {
		this.panelList = panelList;
	}

}
