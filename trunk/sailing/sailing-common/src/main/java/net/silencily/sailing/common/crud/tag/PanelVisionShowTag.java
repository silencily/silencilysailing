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
	 * ������
	 * ��������serialVersionUID
	 * �������ͣ�long
	 */
	private static final long serialVersionUID = 1706524858073367851L;
	
	/**
	 * ��������LIST�洢ѡ��Ķ�����Ϣ������ArrayListǶ��ArrayList�ķ������洢ѡ���Ϣ
	 * ��������panelList
	 * �������ͣ�List
	 */
	private List panelList = new ArrayList();

	/**
	 * �������洢�û����������
	 * ��������entryName
	 * �������ͣ�String
	 */
	private String entryName = new String();
	
	/**
	 * �������趨ֵ���������·��(�������˴���)
	 * ��������comInvorkeeClassFullName
	 * �������ͣ�String
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
	 * ���ɱ�ǩ������
	 */
	public int doStartTag() throws JspException {

		
		
		
		
		/*
		  
		 <div class="main_title"> <div>������˹�����</div></div>
<div class="main_body">
	<div id="divId_panel">
		<!-- ��������ʾѡ�-->
	</div>	
</div>
<script type="text/javascript">
//����ѡ�
var arr = new Array();
arr[0] = [" �������� ", "", "<c:url value = '/wf/personWfSearchAction.do?step=waitList'/>"];
arr[1] = [" �Ѱ����� ", "", "<c:url value = '/wf/personWfSearchAction.do?step=alreadyList'/>"];
arr[2] = [" �������� ", "", "<c:url value = '/wf/personWfSearchAction.do?step=recieveList'/>"];
arr[3] = [" ί������ ", "", "<c:url value = '/wf/personWfSearchAction.do?step=entrustList'/>"];
arr[4] = [" ȫ������ ", "", "<c:url value = '/wf/personWfSearchAction.do?step=allList'/>"];
arr[5] = [" �������� ", "", "<c:url value = '/wf/personWfSearchAction.do?step=searchList'/>"];
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
		//��ʼjavascript����ƴװ
		html.append("<script type=\"text/javascript\">");
		html.append("var arr = new Array();");
		List listTemp = new ArrayList();
		for(int i=0;i<listTemp.size();i++){
			String s1=new String("");
			String s2=new String("");
			//TODO
			
			
			html.append("arr[5] = [\" �������� \", \"\", \"<c:url value = \'/wf/personWfSearchAction.do?step=searchList\'/>\"];");
		}
		html.append("var panel = new Panel.panelObject(\"panel\", arr, 0, \"<c:out value = \"${initParam[\'publicResourceServer\']}/image/main/\" />\");");
		html.append("document.getElementById(\"divId_panel\").innerHTML = panel.display();");
		html.append("Global.displayOperaButton = function(){ };");
		html.append("</script>");
		
		//���ƴװ�õ�String
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
