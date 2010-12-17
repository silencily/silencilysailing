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
 * ��������������selectIdΪһ��javascript������ʱ�򣬵�TABҳ�����˵��Ļ��������BUG�����鲻��
 * 
 * @author wenjb
 * @version 1.0
 */
public class PanelVisionTag extends TagSupport {
	/**
	 * ������ ��������serialVersionUID �������ͣ�long
	 */
	private static final long serialVersionUID = -3192465252187148383L;

	private String panelList = "";
	private String selectId = "";
	private String elementId= "";

	private List objectpanelList = new ArrayList(0);
	private String objectSelectId = "";
	private String objectElementId= "";
	
	//��ȡ��ǰ������
	private String qware="";
	
	/**
	 * ��ʼ���������д���ֵ����ֵ
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
		//����${initParam['publicResourceServer']}
		qware =(String) pageContext.getAttribute("publicResourceServer");
	}

	/**
	 * ���ɱ�ǩ������
	 */
	public int doStartTag() throws JspException {
		try {
			// ʵ������ز���
			StringBuffer html = new StringBuffer();
			objectInit();
			// ѭ������MAP�е�URL����ȡȨ��
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
		//������ʱ����
		int arrIndex=0;

		//�������������һ����������ֱ����ʾ�������
		//Ĭ����ʾҳΪ0
		if("".equals(selectId)||null==selectId){
			selectId="0";
		}
		//Ĭ����ʾID
		String id=selectId;
		
		
		// ѭ������MAP�е�URL
		for(int i=0;i<objectpanelList.size();i++){
			//�õ�Ƕ�׵�����
			ArrayList temp=(ArrayList) objectpanelList.get(i);
			String name=(String)(temp.toArray()[0]);
			String url=(String)(temp.toArray()[1]);
			HttpSession session = SecurityContextInfo.getSession();
			CurrentUser currentUser = (CurrentUser) session
					.getAttribute("currentUser");
			HashMap urlPermissions = currentUser.getUrlPermissions();
			//javascript������ʾ
			if(urlPermissions.containsKey(url)){
				//��Ȩ��, ƴװ�����ַ���
				//html.append("arr[0] = [\" �б� \", \"\", \"<c:url value = '/hr/empTraRecAction.do?step=list&paginater.page=0'/>\"];");
				html.append("arr[");
				html.append(arrIndex);
				html.append("] = [\"");
				html.append(name);
				html.append("\", \"\", \"");
				html.append(qware+url);
				html.append("\"];");	
				//�޸�selectId��ֵ
				//���������Ƿ������򲻴���
				if(selectId.indexOf("(")!=-1){
					if(Integer.parseInt(selectId)==i){
						id=Integer.toString(arrIndex);
					}					
				}
				arrIndex++;
			}
		}
		//ƴװ�����ַ���
		//html.append("var panel = new Panel.panelObject(\"panel\", arr, 0, \"<c:out value = \"${initParam['publicResourceServer']}/image/main/\" />\");");
		html.append("var panel = new Panel.panelObject(\"panel\", arr, document.getElementById(\"index\").value");
		html.append(id);
		html.append(", \"");
		html.append(qware);
		html.append("/image/main/\");");
		//ƴװ�����ַ���
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
