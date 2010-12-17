package net.silencily.sailing.common.crud.tag;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;
/**
 * 
 * @author wenjb
 * @version 1.0
 */
public class PanelVisionShowTag  extends TagSupport{

	/**
	 * 描述：
	 * 属性名：serialVersionUID
	 * 属性类型：long
	 */
	private static final long serialVersionUID = 1706524858073367851L;
	
	/**
	 * 描述：此LIST存储选项卡的定义信息，采用ArrayList嵌套ArrayList的方法，存储选项卡信息
	 * 属性名：panelList
	 * 属性类型：List
	 */
	private List panelList = new ArrayList();

	/**
	 * 描述：存储用户输入的名称
	 * 属性名：entryName
	 * 属性类型：String
	 */
	private String entryName = new String();
	
	/**
	 * 描述：设定值所属类绝对路径(服务器端处理)
	 * 属性名：comInvorkeeClassFullName
	 * 属性类型：String
	 */
	private String comInvorkeeClassFullName = "";
	
	
	
	public String getComInvorkeeClassFullName() {
		return comInvorkeeClassFullName;
	}

	public void setComInvorkeeClassFullName(String comInvorkeeClassFullName) {
		this.comInvorkeeClassFullName = comInvorkeeClassFullName;
	}

	public String getEntryName() {
		return entryName;
	}

	public void setEntryName(String entryName) {
		this.entryName = entryName;
	}

	/**
	 * 生成标签主函数
	 */
	public int doStartTag() throws JspException {

		
		
		
		
		/*
		  
		 <div class="main_title"> <div>浏览个人工作流</div></div>
<div class="main_body">
	<div id="divId_panel">
		<!-- 在这里显示选项卡-->
	</div>	
</div>
<script type="text/javascript">
//定义选项卡
var arr = new Array();
arr[0] = [" 待办任务 ", "", "<c:url value = '/wf/personWfSearchAction.do?step=waitList'/>"];
arr[1] = [" 已办任务 ", "", "<c:url value = '/wf/personWfSearchAction.do?step=alreadyList'/>"];
arr[2] = [" 受托任务 ", "", "<c:url value = '/wf/personWfSearchAction.do?step=recieveList'/>"];
arr[3] = [" 委托任务 ", "", "<c:url value = '/wf/personWfSearchAction.do?step=entrustList'/>"];
arr[4] = [" 全部任务 ", "", "<c:url value = '/wf/personWfSearchAction.do?step=allList'/>"];
arr[5] = [" 检索任务 ", "", "<c:url value = '/wf/personWfSearchAction.do?step=searchList'/>"];
var panel = new Panel.panelObject("panel", arr, 0, "<c:out value = "${initParam['publicResourceServer']}/image/main/" />");
document.getElementById("divId_panel").innerHTML = panel.display();
Global.displayOperaButton = function(){ };
</script> 
		  */
		
		StringBuffer html = new StringBuffer();
		html.append("<div class=\"main_title\"> <div>"+this.getEntryName()+"</div></div>");
		html.append("<div class=\"main_body\">");
		html.append("<div id=\"divId_panel\">");
		html.append("</div>");
		html.append("</div>");
		//开始javascript代码拼装
		html.append("<script type=\"text/javascript\">");
		html.append("var arr = new Array();");
		List listTemp = new ArrayList();
		for(int i=0;i<listTemp.size();i++){
			String s1=new String("");
			String s2=new String("");
			//TODO
			
			
			html.append("arr[5] = [\" 检索任务 \", \"\", \"<c:url value = \'/wf/personWfSearchAction.do?step=searchList\'/>\"];");
		}
		html.append("var panel = new Panel.panelObject(\"panel\", arr, 0, \"<c:out value = \"${initParam[\'publicResourceServer\']}/image/main/\" />\");");
		html.append("document.getElementById(\"divId_panel\").innerHTML = panel.display();");
		html.append("Global.displayOperaButton = function(){ };");
		html.append("</script>");
		
		//输出拼装好的String
		try{
			pageContext.getOut().print(html.toString());			
		}catch(Exception e){
			throw new JspTagException("SimpleTag: " + e.getMessage());
		}
		return SKIP_BODY;
	}
	
	public List getPanelList() {
		return panelList;
	}

	public void setPanelList(List panelList) {
		this.panelList = panelList;
	}	
	
}
