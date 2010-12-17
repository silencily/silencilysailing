package net.silencily.sailing.common.crud.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import net.silencily.sailing.framework.web.view.taglibs.ExtendComboCompositeTag;
import net.silencily.sailing.utils.UUIDGenerator;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.ExpressionEvaluationUtils;


/**
 * @author wuym
 *
 */
public class VisionChooseTag extends ExtendComboCompositeTag {

	private String rwCtrlType = "";					
	private String permissionCode = "";
	private String wfPermissionCode = "";			//工作流PermissionCode
	private String bisname = "";					//效验提示名称
	private String readonly = "false";				//readonly状态
	private String textStyle = "false";				//文本框样式
	private String required = "false";				//是否为必填项*
	private String mustInputHintName = "";			//为必填项* 提示
	private String mustInputHintAction = "";		//为必填项* 事件
	private String canDelete = "false";				//橡皮擦
	private String deleteAction = "";				//橡皮擦事件
	private String onchange = "";					//文本值改变事件
	private String dropMeanNotrequired = "false";	//是否去掉置空选项
	private String comInvorkeeClassFullName = "";	//设定值所属类绝对路径(服务器端处理)

	private String objectRwCtrlType = "";					
	private String objectPermissionCode = "";
	private String objectWfPermissionCode = "";				//工作流PermissionCode
	private String objectTextName = "";
	private String objectValueName = "";
	private String objectBisname = "";						//效验提示名称
	private String objectReadonly = "";						//readonly状态
	private String objectTextStyle = "";					//文本框样式
	private String objectRequired = "false";				//是否为必填项*
	private String objectMustInputHintName = "";			//为必填项* 提示
	private String objectMustInputHintAction = "";			//为必填项* 事件
	private String objectCanDelete = "false";				//橡皮擦
	private String objectDeleteAction = "";					//橡皮擦事件
	private String objectOnchange = "";						//文本值改变事件
	private String objectDropMeanNotrequired = "false";		//是否去掉置空选项
	private String objectComInvorkeeClassFullName = "";		//设定值所属类绝对路径(服务器端处理)
	
	VisionStatusInfo statusInfo;
	String valueObjectHtml;
	boolean wfRequried = false;

	private void objectInit() throws JspException {
		objectRwCtrlType=ExpressionEvaluationUtils.evaluateString("rwCtrlType", rwCtrlType, pageContext);
		objectPermissionCode=ExpressionEvaluationUtils.evaluateString("permissionCode", permissionCode, pageContext);
		objectWfPermissionCode=ExpressionEvaluationUtils.evaluateString("wfPermissionCode", wfPermissionCode, pageContext);
		objectTextName = ExpressionEvaluationUtils.evaluateString("textName", textName, pageContext);
		objectValueName = ExpressionEvaluationUtils.evaluateString("valueName", valueName, pageContext);
		objectBisname=ExpressionEvaluationUtils.evaluateString("bisname", bisname, pageContext);
		objectReadonly = ExpressionEvaluationUtils.evaluateString("readonly", readonly, pageContext);
		objectTextStyle = ExpressionEvaluationUtils.evaluateString("textStyle", textStyle, pageContext);
		objectRequired=ExpressionEvaluationUtils.evaluateString("required", required, pageContext);
		objectMustInputHintName=ExpressionEvaluationUtils.evaluateString("mustInputHintName", mustInputHintName, pageContext);
		objectMustInputHintAction=ExpressionEvaluationUtils.evaluateString("mustInputHintAction", mustInputHintAction, pageContext);
		objectCanDelete=ExpressionEvaluationUtils.evaluateString("canDelete", canDelete, pageContext);
		objectDeleteAction=ExpressionEvaluationUtils.evaluateString("deleteAction", deleteAction, pageContext);
		objectOnchange=ExpressionEvaluationUtils.evaluateString("onchange", onchange, pageContext);
		objectDropMeanNotrequired=ExpressionEvaluationUtils.evaluateString("dropMeanNotrequired", dropMeanNotrequired, pageContext);
		objectComInvorkeeClassFullName=ExpressionEvaluationUtils.evaluateString("comInvorkeeClassFullName", comInvorkeeClassFullName, pageContext);
	}
	
	/**
	 * 生成标签主函数
	 */
	public int doStartTag() throws JspException {
		try {
			//实例化相关参数
			StringBuffer html = new StringBuffer();
			valueObjectHtml = getValueObjectHtml();
			objectInit();
			ITagSecurityPolicy invorkee = (ITagSecurityPolicy) Class.forName(comInvorkeeClassFullName).newInstance();
			statusInfo = invorkee.compomentPermission(this.objectRwCtrlType,this.objectPermissionCode,this.objectWfPermissionCode,pageContext);
			wfRequried = invorkee.workFlowIsNeedData(this.objectWfPermissionCode);
			//查看页
			if (statusInfo.getPageType() == ITagSecurityPolicy.VIEWPAGE) {
				/*
				//增加temp隐藏域的处理
				getHiddenInit(html);
				html.append("<input type=\"hidden\" id=\""+ objectTextName +"TempValueDivId\" value=\""+ getTextValue() +"\"/>");
				html.append("\n<input type = \"hidden\" ");
				html.append(" name = \"");
				html.append(objectTextName);
				html.append("\" value = \"");
				html.append(getTextValue());
				html.append("\"");
				html.append(" id = \""+ INPUT_TEXT_ID +"\" ");
				html.append("/>");
				//增加temp隐藏域的处理
				 */
				getVisible(html);
			}
			//或编辑页
			else if (statusInfo.getPageType() == ITagSecurityPolicy.EDITPAGE) {
				getHiddenInit(html);
				getEditPage(html);
				try {
					pageContext.getOut().write(html.toString());
				} catch (IOException e) {
					throw new JspException(e);
				}
			} else {
				throw new JspException("");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SKIP_BODY;

	}
	
	/**
	 * 得到查看状态tag
	 * @param html
	 * @param comStatusInfo
	 */
	public void getVisible(StringBuffer html){
		try {
			if (statusInfo.getVisiableStatus() == ITagSecurityPolicy.VISIBLE) {
				//html.append(valueObjectHtml);
				html.append(getTextValue());

			} else if (statusInfo.getVisiableStatus() == ITagSecurityPolicy.UNVISIBLE) {
				html.append("-");
			} else {
				throw new JspException("");
			}
			pageContext.getOut().print(html.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 得到可编辑状态tag
	 * @param html
	 * @param comStatusInfo
	 */
	public void getEditPage(StringBuffer html){
		try {
			html.append(valueObjectHtml);

//			String evaledTextName = evaluateAndEscapeHtml(TEXT_NAME_ATTRIBUTE,
//					textName, pageContext);
			//加入保存临时值DIV
			if(!objectOnchange.equals("")){
				//html.append("<div id=\""+ objectTextName +"TempValueDivId\" value=\""+ getTextValue() +"\"></div>");
				html.append("<input type=\"hidden\" id=\""+ objectTextName +"TempValueDivId\" value=\""+ getTextValue() +"\"/>");
			}
			html.append("\n<input type = \"text\" ");
			html.append(" name = \"");
			html.append(objectTextName);
			html.append("\" value = \"");
			html.append(getTextValue());
			html.append("\"");
			
			//文本框样式
			if(!objectTextStyle.equals("")){
				html.append(" style=\""+ objectTextStyle +"\"");
			}
			getOnchange(html,objectTextName);
			html.append(" id = \""+ INPUT_TEXT_ID +"\" ");//下拉框去ID以适应样式
			//html.append(" class = \""+ INPUT_TEXT_ID +"\" ");//下拉框去class以适应样式

			//必输入项提示
			getBisname(html);
			

			//工作流表单编辑项必须入力属性
			getWfRequried(html);
			

			String uuid = new UUIDGenerator().generate().toString();
			String javascriptArrayName = "ExtendComboText.source" + uuid;

			StringBuffer eventHandler = new StringBuffer();
			eventHandler
					.append("if (event.srcElement.readOnly) return false;ExtendCombo.getOptionStatic(document.getElementsByName('");
			eventHandler.append(objectTextName);
			eventHandler.append("')[0], this, ");
			if (StringUtils.isNotBlank(getValueObject())) {
				eventHandler.append(getValueObject());
			} else {
				eventHandler.append("null");
			}
			eventHandler.append(", ");
			eventHandler.append(javascriptArrayName);
			eventHandler.append(",'','',"+ objectDropMeanNotrequired);
			eventHandler.append(")");

			html.append(" onkeyup = \"");
			html.append(eventHandler);
			html.append("\" ");
			
			//是否为只读
			if (isTextReadonly() || objectReadonly.equals("true")) {
				html.append(" class = \"readonly\" readonly = \"readonly\" ");
			}
			if (StringUtils.isNotBlank(textExtra)) {
				String evaledExtra = evaluateAndEscapeHtml(
						TEXT_EXTRA_ATTRIBUTE, textExtra, pageContext);
				html.append(evaledExtra);
			}
			getDisabled(html);
//			if (statusInfo.getEditableStatus() == ITagSecurityPolicy.UNEDITABLE) {
//				html.append("disabled");
//			}
			html.append(" />");

			//下拉按钮
			html.append("<input type=\"button\" id=\"");
			html.append(INPUT_SELECT_ID);
			html.append("\" name = \"");
			html.append(evaluateAndEscapeHtml(BUTTON_NAME_ATTRIBUTE,
					buttonName, pageContext));

			//提示功能
//			if(!objectMustInputHintAction.equals("")){
//				html.append(" onchange=\""+ objectMustInputHintAction +"\"");
//			}
//			else if(!objectMustInputHintName.equals("")){
//				html.append(" onchange=\"alert(\'必须输入\'+"+ objectMustInputHintName +");\"");
//			}

			html.append("\" title=\"" + "点击这里弹出下拉选择框" + "\" onclick =\"");
			html.append(eventHandler + "\" ");
			getDisabled(html);
//			if (statusInfo.getEditableStatus() == ITagSecurityPolicy.UNEDITABLE) {
//				html.append("disabled ");
//			}
			html.append("/>");
			//是否为必填项
			if (objectRequired.equals("true")) {
				html.append(" <span class=\"font_request\">*</span>");
			}
			
			//下拉框map加载
			html.append("\n<script language=\"javascript\">\n");
			html.append("if (ExtendComboText == null) { var  ExtendComboText = {}; } \n");

			html.append(javascriptArrayName);
			html.append(" = [");
			html.append(getExtendComboSourceJavaScriptArrayContent());
			html.append("]; \n");

			html.append("</script>\n");
			if (objectCanDelete.equals("true") && !objectRequired.equals("true")) {
				// html
				// .append("<input id=input_date type=button name=timebutton");
				// if (comStatusInfo.getEditableStatus() ==
				// IComInvorkee.UNEDITABLE) {
				// html.append(" disabled");
				// }
				// html.append(" onclick=" + canDelete + "/>");
				getDelete(html);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 自定义属性bisname提示信息名称,必输入项提示
	 * @param html
	 */
	private void getBisname(StringBuffer html){
		if(!this.objectBisname.equals("")){
			html.append(" bisname=\""+ this.objectBisname +"\"");
		}
	}

	/**
	 * 工作流表单编辑项必须入力属性
	 * @param html
	 */
	private void getWfRequried(StringBuffer html){
		if(wfRequried){
			html.append(" wfRequried=\"true\"");
		}
	}
	
	/**
	 * 得到传入对象的值
	 * @return
	 */
	public String getValueKey() throws JspException {
		String key;
		//value = "     " />
		key = valueObjectHtml.substring(valueObjectHtml.indexOf("value = \"")+9, valueObjectHtml.indexOf("\" />"));
		return key;
	}
	/**
	 * 隐藏域,用于保存初始值
	 * @param html
	 */
	private void getHiddenInit(StringBuffer html){
		try{
		html.append("<input type=\"hidden\" id=\"hiddenValueDefault"+ this.objectValueName +"\"" +
				" name=\"hiddenValueDefault"+ this.objectValueName +"\"" +
				" value=\"" + this.getValueKey() + "\"/>\n");
		}catch(Exception e){e.printStackTrace();}
	}

	/**
	 * 文本域值改变事件
	 * @param html
	 */
	private void getOnchange(StringBuffer html,String objectTextName){
		if(!this.objectOnchange.equals("")){
			html.append(" onpropertychange=\"if(!(this.value==document.getElementById('"+ objectTextName +"TempValueDivId').value)){" + this.objectOnchange + "" +
					"document.getElementById('"+ objectTextName +"TempValueDivId').value=this.value;}\"");
		}
	}
	
	/**
	 * 得到清除功能
	 * @param html
	 */
	private void getDelete(StringBuffer html){
		html.append("<input id=\"opera_clear\" type=\"button\" name=\"delbutton\" title=\"点击清除\"");
		getDisabled(html);
//		if (statusInfo.getEditableStatus() == ITagSecurityPolicy.UNEDITABLE) {
//			html.append(" disabled");
//		}
//		html.append(" onclick=\"FormUtils.cleanValues($('" + this.objectTextName + "'))" + "\"" +
//				"/>\n");
		if(!this.objectDeleteAction.equals("")){
			//手动实现橡皮擦事件
			html.append(" onclick=\"" + this.objectDeleteAction + "\"");
		}else{
			html.append(" onclick=\"FormUtils.cleanValues($('" + this.objectTextName + "'));\"");
		}
		
		html.append("/>\n");
	}
	
	/**
	 * 功能按钮不可编辑状态
	 * @param html
	 */
	private void getDisabled(StringBuffer html){
		if (statusInfo.getEditableStatus() == ITagSecurityPolicy.UNEDITABLE || objectReadonly.equals("true")) {
			html.append(" disabled");
		}
	}
	
	public String getRwCtrlType() {
		return rwCtrlType;
	}

	public void setRwCtrlType(String rwCtrlType) {
		this.rwCtrlType = rwCtrlType;
	}

	public String getPermissionCode() {
		return permissionCode;
	}

	public void setPermissionCode(String permissionCode) {
		this.permissionCode = permissionCode;
	}

	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public String getCanDelete() {
		return canDelete;
	}

	public void setCanDelete(String canDelete) {
		this.canDelete = canDelete;
	}

	public String getOnchange() {
		return onchange;
	}

	public void setOnchange(String onchange) {
		this.onchange = onchange;
	}

	public String getComInvorkeeClassFullName() {
		return comInvorkeeClassFullName;
	}

	public void setComInvorkeeClassFullName(String comInvorkeeClassFullName) {
		this.comInvorkeeClassFullName = comInvorkeeClassFullName;
	}

	public String getReadonly() {
		return readonly;
	}

	public void setReadonly(String readonly) {
		this.readonly = readonly;
	}

	public String getDeleteAction() {
		return deleteAction;
	}

	public void setDeleteAction(String deleteAction) {
		this.deleteAction = deleteAction;
	}

	public String getMustInputHintAction() {
		return mustInputHintAction;
	}

	public void setMustInputHintAction(String mustInputHintAction) {
		this.mustInputHintAction = mustInputHintAction;
	}

	public String getMustInputHintName() {
		return mustInputHintName;
	}

	public void setMustInputHintName(String mustInputHintName) {
		this.mustInputHintName = mustInputHintName;
	}

	public String getBisname() {
		return bisname;
	}

	public void setBisname(String bisname) {
		this.bisname = bisname;
	}

	public String getWfPermissionCode() {
		return wfPermissionCode;
	}

	public void setWfPermissionCode(String wfPermissionCode) {
		this.wfPermissionCode = wfPermissionCode;
	}

	public String getDropMeanNotrequired() {
		return dropMeanNotrequired;
	}

	public void setDropMeanNotrequired(String dropMeanNotrequired) {
		this.dropMeanNotrequired = dropMeanNotrequired;
	}

	public String getTextStyle() {
		return textStyle;
	}

	public void setTextStyle(String textStyle) {
		this.textStyle = textStyle;
	}
	
}
