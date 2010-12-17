package net.silencily.sailing.common.crud.tag;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import net.silencily.sailing.utils.DateUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.ExpressionEvaluationUtils;
import org.springframework.web.util.HtmlUtils;

/**
 * @author wuym
 * 
 */
public class VisionTextboxTag extends TagSupport { 
	private String rwCtrlType = "";					//rwCtrlType
	private String permissionCode = "";				//permissionCode
	private String wfPermissionCode = "";			//������PermissionCode
	private String id = "";							//�ı�ID
	private String name = "";						//�ı�����
	private String value = "";						//�ı���ֵ
	private String bisname = "";					//Ч����ʾ����
	private String maxlength = "";					//�ı���������볤��
	private String maxviewlength = "0";				//���ı��������ʾ����
	private String format = "";						//��ʽ����ʽ
	private String style = "";						//style����ֵ
	private String clazz = "";						//class����ֵ
	private String readonly = "";					//ֻ��
	private String required = "false";				//�Ƿ�Ϊ������*
	private String mustInputHintName = "";			//Ϊ������* ��ʾ
	private String mustInputHintAction = "";		//Ϊ������* �¼�
	private String dataType = "1";					//0����/1�ַ�/2���ڿ�/3���ڼ�ʱ���
	private String haveDateButton = "true";			//ֻ��״̬���Ƿ���ʾ����button,true:��ʾ,false:����ʾ,Ĭ��true
	private String link = "";						//�Ƿ�Ϊ����
	private String target = "";						//�Ƿ�Ϊ��������
	private String longTextType = "false";			//�Ƿ�Ϊ���ı���
	private String longTextSaveFormat = "false";	//���ı���ʾ��ʽ����
	private String rows = "1";						//�ı�������
	private String signatureButtonAction = "";		//ǩ�ְ�ťjs����
	private String selButtonAction = "";			//�鿴��ťjs����
	private String canDelete = "false";				//��Ƥ��
	private String deleteAction = "";				//��Ƥ���¼�
	private String onchange = "";					//�ı�ֵ�ı��¼�
	private String searchButtonAction = "";			//������ťjs����
	private String comInvorkeeClassFullName = "";	//�趨ֵ���������·��(�������˴���)

	private String objectRwCtrlType = "";					//rwCtrlType
	private String objectPermissionCode = "";				//permissionCode
	private String objectWfPermissionCode = "";				//������PermissionCode
	private String objectId = "";							//�ı�ID
	private String objectName = "";							//�ı������������
	private String objectValue = "";						//�ı����������ֵ
	private String truncationValue = "";					//��ʾ״̬�Ľض��ַ�
	private String objectBisname = "";						//Ч����ʾ����
	private String objectMaxlength = "";					//�ı���������볤��
	private String objectMaxviewlength = "0";				//���ı��������ʾ����
	private String objectFormat = "";						//��ʽ����ʽ
	private String objectStyle = "";						//style����ֵ
	private String objectClazz = "";						//class����ֵ
	private String objectReadonly = "";						//ֻ��
	private String objectRequired = "false";				//�Ƿ�Ϊ������*
	private String objectMustInputHintName = "";			//Ϊ������* ��ʾ
	private String objectMustInputHintAction = "";			//Ϊ������* �¼�
	private String objectDataType = "1";					//����/�ַ�/���ڿ�
	private String objectHaveDateButton = "true";			//ֻ��״̬���Ƿ���ʾ����button,true:��ʾ,false:����ʾ,Ĭ��true
	private String objectLink = "";							//�Ƿ�Ϊ����
	private String objectTarget = "";						//�Ƿ�Ϊ��������
	private String objectLongTextType = "false";			//�Ƿ�Ϊ���ı���
	private String objectLongTextSaveFormat = "false";	//���ı���ʾ��ʽ����
	private String objectRows = "1";						//�ı�������
	private String objectSignatureButtonAction = "";		//ǩ�ְ�ťjs����
	private String objectSelButtonAction = "";				//�鿴��ťjs����
	private String objectCanDelete = "false";				//��Ƥ��
	private String objectDeleteAction = "";					//��Ƥ���¼�
	private String objectOnchange = "";						//�ı�ֵ�ı��¼�
	private String objectSearchButtonAction = "";			//������ťjs����
	private String objectComInvorkeeClassFullName = "";		//�趨ֵ���������·��(�������˴���)

	VisionStatusInfo statusInfo;
	boolean wfRequried = false;

	/**
	 * ��ʼ���������д���ֵ����ֵ
	 * @throws JspException
	 */
	private void objectInit() throws JspException {
		objectRwCtrlType=ExpressionEvaluationUtils.evaluateString("rwCtrlType", rwCtrlType, pageContext);
		objectPermissionCode=ExpressionEvaluationUtils.evaluateString("permissionCode", permissionCode, pageContext);
		objectWfPermissionCode=ExpressionEvaluationUtils.evaluateString("wfPermissionCode", wfPermissionCode, pageContext);
		objectId=ExpressionEvaluationUtils.evaluateString("id", id, pageContext);
		objectName=ExpressionEvaluationUtils.evaluateString("name", name, pageContext);
		
		objectBisname=ExpressionEvaluationUtils.evaluateString("bisname", bisname, pageContext);
		objectMaxlength=ExpressionEvaluationUtils.evaluateString("maxlength", maxlength, pageContext);
		objectMaxviewlength=ExpressionEvaluationUtils.evaluateString("maxviewlength", maxviewlength, pageContext);
		objectFormat=ExpressionEvaluationUtils.evaluateString("format", format, pageContext);
		objectStyle=ExpressionEvaluationUtils.evaluateString("style", style, pageContext);
		objectClazz=ExpressionEvaluationUtils.evaluateString("clazz", clazz, pageContext);
		objectReadonly=ExpressionEvaluationUtils.evaluateString("readonly", readonly, pageContext);
		objectRequired=ExpressionEvaluationUtils.evaluateString("required", required, pageContext);
		objectMustInputHintName=ExpressionEvaluationUtils.evaluateString("mustInputHintName", mustInputHintName, pageContext);
		objectMustInputHintAction=ExpressionEvaluationUtils.evaluateString("mustInputHintAction", mustInputHintAction, pageContext);
		objectDataType=ExpressionEvaluationUtils.evaluateString("dataType", dataType, pageContext);
		objectHaveDateButton=ExpressionEvaluationUtils.evaluateString("haveDateButton", haveDateButton, pageContext);
		objectLink=ExpressionEvaluationUtils.evaluateString("link", link, pageContext);
		objectTarget=ExpressionEvaluationUtils.evaluateString("target", target, pageContext);
		objectLongTextType=ExpressionEvaluationUtils.evaluateString("longTextType", longTextType, pageContext);
		objectLongTextSaveFormat=ExpressionEvaluationUtils.evaluateString("longTextSaveFormat", longTextSaveFormat, pageContext);
		objectRows=ExpressionEvaluationUtils.evaluateString("rows", rows, pageContext);
		objectSignatureButtonAction=ExpressionEvaluationUtils.evaluateString("signatureButtonAction", signatureButtonAction, pageContext);
		objectSelButtonAction=ExpressionEvaluationUtils.evaluateString("selButtonAction", selButtonAction, pageContext);
		objectCanDelete=ExpressionEvaluationUtils.evaluateString("canDelete", canDelete, pageContext);
		objectDeleteAction=ExpressionEvaluationUtils.evaluateString("deleteAction", deleteAction, pageContext);
		objectOnchange=ExpressionEvaluationUtils.evaluateString("onchange", onchange, pageContext);
		objectSearchButtonAction=ExpressionEvaluationUtils.evaluateString("searchButtonAction", searchButtonAction, pageContext);
		objectComInvorkeeClassFullName=ExpressionEvaluationUtils.evaluateString("comInvorkeeClassFullName", comInvorkeeClassFullName, pageContext);

		try{
			objectValue=ExpressionEvaluationUtils.evaluateString("value", value, pageContext);
			Object objectDoubleValue = ExpressionEvaluationUtils.evaluate("value", value, pageContext);
			if(Double.class.isAssignableFrom(ExpressionEvaluationUtils.evaluate("value", value, pageContext).getClass())){
				objectValue=(String)(new DecimalFormat(objectFormat.equals("")?"#.##":objectFormat).format(objectDoubleValue));
				//objectValue=formatDoubleStr(objectValue);
			}
			else if(Number.class.isAssignableFrom(ExpressionEvaluationUtils.evaluate("value", value, pageContext).getClass())
					&& !objectFormat.equals("")){
				this.objectValue = (String)(new DecimalFormat(objectFormat).format(objectDoubleValue));
				//objectValue=formatNumberStr(objectValue);
			}
			else if(Date.class.isAssignableFrom(ExpressionEvaluationUtils.evaluate("value", value, pageContext).getClass())
					&& !objectFormat.equals("") && !objectValue.equals("") && objectDataType.equals("7")){
				objectValue = getDatetime(objectValue);
			}
			else{
				objectValue=ExpressionEvaluationUtils.evaluateString("value", value, pageContext);
			}
		}catch(Exception e){objectValue="";}

		try{
			if(Date.class.isAssignableFrom(ExpressionEvaluationUtils.evaluate("value", value, pageContext).getClass())){
				this.objectValue = getTextDateOrDatetimeValue();
			}
		}catch(Exception e){}

		//���ڿ�ֵ���ʽ��
		if (objectDataType.equals("2") || objectDataType.equals("3") || objectDataType.equals("4")||objectDataType.equals("12")||objectDataType.equals("13")||objectDataType.equals("14")||objectDataType.equals("15")) {
			this.objectValue = getDateOrDatetimeValue();
		}

		//�Գ��ı���ʾ״̬�Ľضϲ���
		if(Integer.parseInt(objectMaxviewlength)>0 && Integer.parseInt(objectMaxviewlength) < objectValue.length()){
			this.truncationValue = objectValue.substring(0, Integer.parseInt(objectMaxviewlength));
		}else{
			this.truncationValue = objectValue;
		}
		
		//ת��html�ַ�
		this.objectValue = this.objectValue.replaceAll("&", "&amp;");
		this.objectValue = this.objectValue.replaceAll("<", "&lt;");
		this.objectValue = this.objectValue.replaceAll(">", "&gt;");
		//this.objectValue = this.objectValue.replaceAll("'", "&apos;");
		this.objectValue = this.objectValue.replaceAll("\"", "&quot;");
	}
	
	/**
	 * ���ɱ�ǩ������
	 * @return int
	 * @throws JspException
	 */
	public int doStartTag() throws JspException {
		try {
			//ʵ������ز���
			StringBuffer html = new StringBuffer();
			objectInit();
			ITagSecurityPolicy invorkee = (ITagSecurityPolicy) Class.forName(objectComInvorkeeClassFullName).newInstance();
			statusInfo = invorkee.compomentPermission(this.objectRwCtrlType,this.objectPermissionCode,this.objectWfPermissionCode,pageContext);
			wfRequried = invorkee.workFlowIsNeedData(this.objectWfPermissionCode);
			//�鿴ҳ
			if (statusInfo.getPageType() == ITagSecurityPolicy.VIEWPAGE) {
				//�Ƿ�ɼ�
				if (statusInfo.getVisiableStatus() == ITagSecurityPolicy.VISIBLE) {
					//����LABEL��ʾʱ��ҵ�����ҳ����ʱ������JAVASCRIPT�Ĵ���ÿ��LABEL��������һ��������
					
					if(this.objectLongTextType.equals("true")){
//						if(Integer.parseInt(objectMaxviewlength)>0){
//							String truncationValue = objectValue.substring(0, Integer.parseInt(objectMaxviewlength));
//						}
						if(this.objectLongTextSaveFormat.equals("true")){
							objectValue = objectValue.replaceAll("\r\n","<br>").replaceAll(" ", "&nbsp;");
						}
						if(Integer.parseInt(objectMaxviewlength)>0){
							truncationValue = truncationValue.replaceAll("\r\n","<br>").replaceAll(" ", "&nbsp;");
							if(Integer.parseInt(objectMaxviewlength) < objectValue.length()){
								objectValue = "<span style=\"word-break:break-all\" title=\""+this.objectValue+"\">"+this.truncationValue+"...</span>";
							}
						}
						//���ı������ʽ�������Զ�����
						if(Integer.parseInt(objectMaxviewlength)==0 && this.objectLongTextSaveFormat.equals("true")){
							html.append("<div style=\"word-break:break-all\">"+objectValue+"&nbsp;</div>");
						}else {
							html.append("<span style=\"word-break:break-all\">"+objectValue+"&nbsp;</span>");
						}
					}
					else if(objectSignatureButtonAction.trim().length() > 0 || objectDataType == "7"){
						html.append("<u>");
						html.append(objectValue);
						html.append("</u>");
					}
					else if(!this.objectLink.equals("") && !this.objectValue.equals("")){
						if(this.objectTarget.equals("")) {
							html.append("<a href=\""+ this.objectLink +"\" style=\""+ this.objectStyle +"\" class=\""+ this.objectClazz +"\">");
							html.append(objectValue);
							html.append("</a>");
						}
						else
							html.append("<span style=\"word-break:break-all\" onclick=\"javascript:definedWin.openUrl('', '"+this.objectLink+"')\" class=\"font_link\">"+objectValue+"</span>");
						
					}else{
						//������ʾ״̬���Ҷ���
						if(objectDataType.equals("0")){
							html.append("<span style=\"word-break:break-all\" class=\"font_money\">");
							html.append("&nbsp;"+objectValue);
							html.append("</span>");
						}else{
							if(objectDataType.equals("6")){
								if(Integer.parseInt(objectMaxviewlength)>0){
									truncationValue = truncationValue.replaceAll("\r\n","<br>").replaceAll(" ", "&nbsp;");
									if(Integer.parseInt(objectMaxviewlength) < objectValue.length()){
										objectValue = "<span style=\"word-break:break-all\" title=\""+this.objectValue+"\">"+this.truncationValue+"...</span>";
									}
								}
								//����ע�ı��򱣴��ʽ�������Զ�����
								if(Integer.parseInt(objectMaxviewlength)==0 && this.objectLongTextSaveFormat.equals("true")){
									objectValue = "<div style=\"word-break:break-all\">"+objectValue+"</div>";
								}
							}
							html.append(objectValue+"&nbsp;");
						}
					}
					/*
					//���Ӵ�����֤������ȡ�������ڹ�������ѯ������JAVASCRIPT����wenjb@bis.com.cn
					html.append("<input type=\"hidden\"");
					html.append(" id=\"" + this.objectId + "\"");
					html.append(" name=\"" + this.objectName + "\"");
					html.append(" value=\"" +"123"+ "\"");
					html.append( "/>");
					html.append("<input type=\"hidden\"");
					html.append(" id=\"" +"temp."+ this.objectId + "\"");
					html.append(" name=\""  +"temp."+  this.objectName + "\"");
					html.append(" value=\""+"123" +"\"");
					html.append( "/>");
					//���Ӵ�����֤������ȡ�������ڹ�������ѯ������JAVASCRIPT����wenjb@bis.com.cn
					*/
				} else if (statusInfo.getVisiableStatus() == ITagSecurityPolicy.UNVISIBLE) {
					html.append("&nbsp;");
				} else {
					throw new JspException("");
				}
			}
			//��༭ҳ
			else if (statusInfo.getPageType() == ITagSecurityPolicy.EDITPAGE) {
				//�Ƿ�ɼ�
				if (statusInfo.getVisiableStatus() == ITagSecurityPolicy.UNVISIBLE) {
					html.append("-");
				} else if (statusInfo.getVisiableStatus() == ITagSecurityPolicy.VISIBLE) {
					//�Ƿ���������
//					if (!dateType && ((selButtonAction != null && selButtonAction.trim().length() > 0)
//						|| (searchButtonAction != null && searchButtonAction.trim().length() > 0))){
//						getHiddenFieldInit(html);
//						html.append(getHiddenField());
//					}else{
						getHiddenInit(html);
//					}
					
						//�Ƿ�Ϊ���ı�����ļ�ѡ���
					if (this.objectLongTextType.equals("true")) {
						getLongText(html);
					}
					else if(objectDataType == "7"){
						if(objectValue.equals("")){
							html.append("<input style=\"background:;text-decoration:none;BORDER: 0px; WIDTH: 0px;\" type=\"text\" name=\""+objectName+"\" id=\""+objectId+"\" value=\""+objectValue+"\" readonly/>");
						}else{
							html.append("<u>");
							html.append(objectValue);
							html.append("</u>");
						}
					}
					else {
						getTextbox(html);
					}
				}
			} else {
				throw new JspException("");
			}
			pageContext.getOut().print(html.toString());
		} catch (Exception e) {
			throw new JspTagException("SimpleTag: " + e.getMessage());
		}
		return SKIP_BODY;
	}

	public int doEndTag() {
		return EVAL_PAGE;
	}
	
	/**
	 * �õ����ı���
	 * @param html
	 */
	private void getLongText(StringBuffer html){
		//���뱣����ʱֵDIV
		if(!objectOnchange.equals("")){
			html.append("<div value=\""+ objectValue +"\" id=\""+ objectName +"TempValueDivId\"/>");
		}
		html.append("<textarea id=\"textArea\"" +
				" name=\""+ this.objectName +"\"");
		
		//�ı�������
		html.append(" rows=\""+ this.objectRows +"\"");

		//���������༭�������������
		getWfRequried(html);
		
		//�ı������������
		if(!this.objectMaxlength.equals("")){
			if(!this.objectBisname.equals("")){
				//ȥ����������
//				html.append(" onkeydown=\"this.value = this.value.substring(0,"+ this.objectMaxlength +");\"");
//			}else{
				html.append(" onkeyup=\"if(this.value.length > "+ this.objectMaxlength +
						"){Validator.clearValidateInfo();Validator.warnMessage('"+ 
						this.objectBisname +"���Ȳ��ܴ���"+ this.objectMaxlength +"');Validator.isClearMessage=true;}" +
								"else if(Validator.isClearMessage){Validator.clearValidateInfo();Validator.isClearMessage=false;}\"");
			}
		}

		//�Զ�������bisname��ʾ��Ϣ����
		getBisname(html);
		
		//��󳤶���ʾ
		isMaxlengthHint(html);

		//��������ʾ
		if (objectRequired.equals("true")) {
			html.append(" bisRequired=\"true\"");
		}
		
		//�ı�Ϊ���ֿ���ı���
		html.append(" class=\"");
		if(objectDataType.equals("0")){
			html.append("font_money");
		}else if(!objectClazz.equals("")){
			html.append(this.objectClazz);
		}else{
			html.append("input_long");
		}
		if (this.objectReadonly.equals("true") || statusInfo.getEditableStatus() == ITagSecurityPolicy.UNEDITABLE) {
			html.append(" readonly");
		}
		html.append("\"");
		
		getOnchange(html,this.objectName);
		
		//���ı����޹�����dataType="6"
		if(!objectStyle.equals("")){
			html.append(" style=\""+ (this.objectDataType.equals("6")?"overflow:hidden;":"") + this.objectStyle +"\"");
		}
		
		//getOnchange(html);
//		if(objectReadonly.equals("true") || statusInfo.getEditableStatus() == ITagSecurityPolicy.UNEDITABLE){
			html.append(getDisableAttrStr(statusInfo));
//		}
//		else if (statusInfo.getEditableStatus() == ITagSecurityPolicy.UNEDITABLE) {
//			html.append(" readonly");
//		}
		html.append(">" + this.objectValue + "</textarea>");
		isRequired(html);

		if(!this.objectDataType.equals("8")){
			remarkButton(html);
		}
	}
	
	/**
	 * �õ��ı���
	 * @param html
	 */
	private void getTextbox(StringBuffer html){
		//���뱣����ʱֵDIV
		if(!objectOnchange.equals("")){
			html.append("<div value=\""+ objectValue +"\" id=\""+ objectName +"TempValueDivId\"/>");
		}

		//�Ƿ�Ϊ�ļ�ѡ������
		if(this.objectDataType.equals("5")){
			html.append("<input type=\"file\"");
		}else{
			html.append("<input type=\"text\"");
		}
		
		//���������༭�������������
		getWfRequried(html);
		
		html.append(" name=\"" + this.objectName + "\"");
		html.append(" value=\"" + this.objectValue + "\"");
		
		//�Զ�������bisname��ʾ��Ϣ����
		getBisname(html);
		
		//��󳤶���ʾ
		isMaxlengthHint(html);

		//��������ʾ
		if (objectRequired.equals("true")) {
			html.append(" bisRequired=\"true\"");
		}
		
		//�ı������������
//		if(!this.objectMaxlength.equals("")){
//			html.append(" maxlength=\""+ this.objectMaxlength +"\"");
//		}
		
		getOnchange(html,this.objectName);
		
		if (objectDataType.equals("2") || objectDataType.equals("3") || objectDataType.equals("4")||objectDataType.equals("12")||objectDataType.equals("13")||objectDataType.equals("14")||objectDataType.equals("15")) {
			if(!objectHaveDateButton.equals("false")){
				html.append(" id=\"input_text\"");
			}
		}
//		if ((selButtonAction != null && selButtonAction.trim().length() > 0)
//				|| (searchButtonAction != null && searchButtonAction.trim().length() > 0)) {
//			html.append(" id=\"" + this.id + "\"");
//		}
		if (!this.objectId.equals("")) {
			html.append(" id=\"" + this.objectId + "\"");
		}
		if (objectStyle != null && objectStyle.trim().length() > 0) {
			html.append(" style=\"" + this.objectStyle + "\"");
		}
		
		String disableAttrStr = getDisableAttrStr(statusInfo);
		html.append(" class=\"");
//		if (disableAttrStr.equals("")) {
			//���ı�����ʽ
			if (!objectHaveDateButton.equals("false") && (objectDataType.equals("2") || objectDataType.equals("3") || objectDataType.equals("4") ||objectDataType.equals("12")||objectDataType.equals("13")||objectDataType.equals("14")|| objectDataType.equals("6")||objectDataType.equals("15"))){
				html.append("input_text");
			}
			//�ı�Ϊ���ֿ�
			else if(objectDataType.equals("0")){
				html.append("font_money");
			}
			if (objectClazz != null && objectClazz.trim().length() > 0) {
				html.append(" "+this.objectClazz);
			}
//		} else {
//			html.append(disableAttrStr);
//		}
		
		html.append(disableAttrStr + "\"");
		
		html.append(disableAttrStr + "/>");
		//�ɼ�״̬
		if (statusInfo.getVisiableStatus() == ITagSecurityPolicy.VISIBLE) {
			//���ڿ�
			if (!(objectHaveDateButton.equals("false") && objectReadonly.equals("true")) && 
					(objectDataType.equals("2") || objectDataType.equals("3") || objectDataType.equals("4")
							||objectDataType.equals("12")||objectDataType.equals("13")||objectDataType.equals("14")||objectDataType.equals("15"))) {
				html.append("<input id=\"input_date\" type=\"button\" name=\"button\"");
				getDisabled(html);
				//buttonTitle(html);
				if(statusInfo.getEditableStatus() != ITagSecurityPolicy.UNEDITABLE || objectReadonly.equals("false")){
					if(objectDataType.equals("2")){
						html.append(" title=\""+"����ѡ������"+" \"");	
					}
					if(objectDataType.equals("3")){
						html.append(" title=\""+"����ѡ������ʱ��"+" \"");	
					}
					if(objectDataType.equals("4")){
						html.append(" title=\""+"����ѡ��ʱ��"+" \"");	
					}
					if(objectDataType.equals("12")){
						html.append(" title=\""+"����ѡ���·�"+" \"");	
					}
					if(objectDataType.equals("13")){
						html.append(" title=\""+"����ѡ�񼾶�"+" \"");	
					}
					if(objectDataType.equals("14")){
						html.append(" title=\""+"����ѡ�����"+" \"");	
					}
					if(objectDataType.equals("15")){
						html.append(" title=\""+"����ѡ������ʱ��"+" \"");	
					}
					//html.append(" title=\" "+(objectDataType.equals("2")?"����ѡ������":(objectDataType.equals("3")?"����ѡ������ʱ��":"����ѡ��ʱ��"))+" \"");
				}
				if(objectDataType.equals("2") || objectDataType.equals("3") || objectDataType.equals("4")){
					html.append(" onclick=\""
							+ "DateComponent.setSpecialDay(1,this,document.getElementsByName('"
							+ this.objectName + "')[0]"+ 
							(objectDataType.equals("2")?"":(objectDataType.equals("3")?",''":",document.getElementsByName('"
							+ this.objectName + "')[0]")) + ((!this.objectFormat.equals("") && this.objectFormat.indexOf("ss")==-1)?",false":"")+")"
							+ "\"/>");
				}else{
					if(objectDataType.equals("15")){
						html.append(" onclick=\""
								+ "DateComponent.setSpecialDay("
								+ "1"
								+","
								+"this,document.getElementsByName('"
								+ this.objectName + "')[0]"
								+","
								+"''"
								+","
								+"false"
								+")"
								+ "\"/>");		
					}else{
						html.append(" onclick=\""
								+ "DateComponent.setSpecialDay("
								+ objectDataType
								+","
								+"this,document.getElementsByName('"
								+ this.objectName + "')[0]"
								+")"
								+ "\"/>");
					}
				}
			}
			isRequired(html);

			if(this.objectDataType.equals("6")){
				remarkButton(html);
			}

			//ǩ�ְ�ť
			if(statusInfo.getEditableStatus() != ITagSecurityPolicy.UNEDITABLE){
				if (objectSignatureButtonAction.trim().length() > 0) {
					html.append("<input id=\"signature_query\" type=\"button\" name=\"signaturebutton_"+this.objectName+"\"");
					getDisabled(html);
					html.append(" title=\" ǩ�� \"");
					html.append(" onclick=\"" + objectSignatureButtonAction
							+ "\"/>");
				}
			}
			
			//����������ѡ��ť
			if (!(objectDataType.equals("2") || objectDataType.equals("3") || objectDataType.equals("4") 
					||objectDataType.equals("12")||objectDataType.equals("13")||objectDataType.equals("14")||objectDataType.equals("15"))) {
				if (objectSelButtonAction.trim().length() > 0) {
					html.append("<input id=\"select_fromtree\" type=\"button\" name=\"selbutton_"+this.objectName+"\"");
					getDisabled(html);
					//buttonTitle(html);
					html.append(" title=\"����ѡ��\"");
					html.append(" onclick=\"" + objectSelButtonAction
							+ "\"/>");
				}
			}
			
			//������������ť
			if (!(objectDataType.equals("2") || objectDataType.equals("3") || objectDataType.equals("4")
					||objectDataType.equals("12")||objectDataType.equals("13")||objectDataType.equals("14")||objectDataType.equals("15"))) {
				if (objectSearchButtonAction.trim().length() > 0) {
					html.append("<input id=\"edit_query\" type=\"button\" name=\"edibutton_"+this.objectName+"\"");
					getDisabled(html);
					//buttonTitle(html);
					html.append(" title=\"����ѡ���鿴\"");
					html.append(" onclick=\"" + objectSearchButtonAction
							+ "\"/>");
				}
			}

			//�Ƿ�����Ƥ��
			if (objectCanDelete.equals("true") && ((objectSelButtonAction.trim().length() > 0)
					|| (objectSearchButtonAction.trim().length() > 0))
					|| objectCanDelete.equals("true") && (objectDataType.equals("2") || objectDataType.equals("3") || objectDataType.equals("4")||objectDataType.equals("12")||objectDataType.equals("13")||objectDataType.equals("14")||objectDataType.equals("15"))) {
				html.append("<input id=\"opera_clear\" type=\"button\" name=\"delbutton_"+this.objectName+"\" title=\"������\"");
				getDisabled(html);
				
				if(!this.objectDeleteAction.equals("")){
					//�ֶ�ʵ����Ƥ���¼�
					html.append(" onclick=\"" + this.objectDeleteAction + "\"");
				}else{
					html.append(" onclick=\"FormUtils.cleanValues($('" + this.objectName + "'));\"");
				}
				
				html.append("/>");
			}
			
		}
	}

	/**
	 * С����λ0��˭�и��õ�ʵ�ַ�ʽ��
	 * @param numberStr
	 * @return
	 */
	private String formatDoubleStr(String doubleStr) throws JspException{
		/*if(!Integer.class.isAssignableFrom(ExpressionEvaluationUtils.evaluate("value", value, pageContext).getClass())){
			if(doubleStr.indexOf(".") == -1){
				doubleStr += ".00";
			}else if(doubleStr.equals("")){
				doubleStr += "0.00";
			}
		}*/
		if(!objectFormat.equals("") && objectFormat.indexOf(".") != -1 ){
			int decimalDigits = objectFormat.substring(objectFormat.indexOf(".")+1).length();
			int beDecimalDigits = 0;
			if(doubleStr.indexOf(".") == -1){
				doubleStr += ".";
			}else{
				beDecimalDigits = doubleStr.substring(doubleStr.indexOf(".")+1).length();
			}
			for(int i=beDecimalDigits;i<decimalDigits;i++){
				doubleStr += "0";
			}
		}
		return doubleStr;
	}
	
	/**
	 * ������,���ڱ����ʼֵ
	 * @param html
	 */
	private void getHiddenInit(StringBuffer html){
		html.append("<input type=\"hidden\" id=\"hiddenValueDefault"+ this.objectId +"\"" +
				" name=\"hiddenValueDefault"+ this.objectName +"\"" +
				" value=\"" + this.objectValue + "\"/>");
	}

	/**
	 * �ı���ֵ�ı��¼�
	 * @param html
	 */
	private void getOnchange(StringBuffer html,String evaledTextName){
		if(!this.objectOnchange.equals("")){
			html.append(" onpropertychange=\"if(!(this.value==document.getElementById('"+ evaledTextName +"TempValueDivId').value)){" + this.objectOnchange + "" +
					"document.getElementById('"+ evaledTextName +"TempValueDivId').value=this.value;}\"");
		}
	}

	/**
	 * �Զ�������bisname��ʾ��Ϣ����,����������ʾ
	 * @param html
	 */
	private void getBisname(StringBuffer html){
		if(!this.objectBisname.equals("")){
			html.append(" bisname=\""+ this.objectBisname +"\"");
		}
	}

	/**
	 * ������
	 * @param html
	 */
	private void isRequired(StringBuffer html){
		if (objectRequired.equals("true")) {
			html.append(" <span class=\"font_request\">*</span>");
		}
	}

	/**
	 * ��󳤶���ʾ��Ϣ
	 * @param html
	 */
	private void isMaxlengthHint(StringBuffer html){
		if (!objectMaxlength.equals("")) {
			html.append(" maxlength=\""+ this.objectMaxlength +"\"");
		}
	}

	/**
	 * ���������༭�������������
	 * @param html
	 */
	private void getWfRequried(StringBuffer html){
		if(wfRequried){
			html.append(" wfRequried=\"true\"");
		}
	}
	
	/**
	 * ���������,����KEYֵ
	 * @param html
	 */
//	private void getHiddenFieldInit(StringBuffer html){
//		html.append("<input type=\"hidden\" id=\""+ this.hiddenFieldId +"HiddenValueDefault\"" +
//				" name=\""+ this.hiddenFieldName +"HiddenValueDefault\"" +
//				" value=\"" + this.hiddenFieldValue + "\"/>");
//	}
	
	/**
	 * ���ܰ�ť������״̬
	 * @param html
	 */
	private void getDisabled(StringBuffer html){
		if (statusInfo.getEditableStatus() == ITagSecurityPolicy.UNEDITABLE || objectReadonly.equals("true")) {
			html.append(" disabled");
		}
	}
	
	/**
	 * �����Ƿ��а�ť����������
	 * @return String
	 */
//	private String getHiddenField() {
//			return "<input id=\"" + this.hiddenFieldId
//					+ "\" type=\"hidden\" name=\"" + this.hiddenFieldName
//					+ "\" value=\"" + this.hiddenFieldValue + "\" readonly/>";
//		return "";
//	}

	/**
	 * ֻ��״̬
	 * @param comStatusInfo
	 * @return String
	 */
	private String getDisableAttrStr(VisionStatusInfo comStatusInfo) {
		if ((comStatusInfo.getEditableStatus() == ITagSecurityPolicy.UNEDITABLE)
				|| (!objectReadonly.equals("writeDate") && (objectDataType.equals("2") || objectDataType.equals("3") || objectDataType.equals("4")||objectDataType.equals("12")||objectDataType.equals("13")||objectDataType.equals("14")||objectDataType.equals("15")))
				|| objectSignatureButtonAction.trim().length() > 0
				|| (objectSelButtonAction.trim().length() > 0)
				//|| (canDelete)
				|| (objectSearchButtonAction.trim().length() > 0)
				|| (!objectReadonly.equals("writeDate") && objectReadonly.equals("true"))) {
//			if (objectDataType.equals("2") || objectDataType.equals("3") || objectDataType.equals("4") 
//					 //|| this.objectDataType.equals("6")
//					 || objectDataType.equals("0")) {
//				return " readonly";
//			} else {
				return " readonly";
//			}
		}
		return "";
	}
	
	/**
	 * ��ť��ʾ��ʼֵ
	 * @param html
	 */
	private void buttonTitle(StringBuffer html){
		if(!this.objectValue.equals("")){
			html.append(" title=\""+this.objectValue+"\"");
		}
	}

	/**
	 * ���ı��༭�򵯳���ע��ť
	 * @param html
	 */
	private void remarkButton(StringBuffer html){

		html.append("<span title=''>");
		html.append("<input type=\"button\"" +
				" id=\"edit_longText\"");
		buttonTitle(html);
		html.append(" onClick=\"definedWin.openLongTextWin(document.getElementsByName('"+ this.objectName +"')[0],'',");
		if (statusInfo.getEditableStatus() == ITagSecurityPolicy.UNEDITABLE || objectReadonly.equals("true")) {
			html.append("true");
		}else{
			html.append("false");
		}
		//ȥ��������������
//		if(!this.objectMaxlength.equals("")){
//			html.append(","+this.objectMaxlength);
//		}
		html.append(");\" /></span>");
	}

	/**
	 * �õ�������/���ڼ�ʱ��/��ʱ��
	 * @return String
	 */
	private String getDateOrDatetimeValue(){
		try{
			if(objectDataType.equals("2")){
				return getDate(objectValue);
			}
			else if(objectDataType.equals("3")) {
				return getDatetime(objectValue);
			}
			else if(objectDataType.equals("15")) {
				return getDatetimeWithoutSec(objectValue);
			}
			else if(objectDataType.equals("4")) {
				return getTime(objectValue);
			}else {
				return objectValue;
			}
		}catch(Exception pe){
			return objectValue;
		}
	}

	/**
	 * �õ�������/���ڼ�ʱ��/��ʱ��
	 * @return String
	 */
	private String getTextDateOrDatetimeValue(){
		try{
			if(objectDataType.equals("9")){
				return getDate(objectValue);
			}
			else if(objectDataType.equals("10")) {
				return getDatetime(objectValue);
			}
			else if(objectDataType.equals("11")){
				return getTime(objectValue);
			}else{
				return objectValue;
			}
		}catch(Exception pe){
			return objectValue;
		}
	}

	/**
	 * �õ�����������
	 * @param datetime
	 * @return String
	 * @throws Exception
	 */
	private String getDate(String datetime) throws Exception{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(DateUtils.parse(datetime));
//		return datetime.equals("")?"":(datetime.substring(0,datetime.length()<10?9:datetime.length()-1));
	}

	/**
	 * �õ����ڼ�ʱ������
	 * @param datetime
	 * @return String
	 * @throws Exception
	 */
	private String getDatetime(String datetime) throws Exception{
		SimpleDateFormat sdf=new SimpleDateFormat(this.objectFormat.equals("")?"yyyy-MM-dd HH:mm:ss":this.objectFormat);
		return sdf.format(DateUtils.parse(datetime));
	}

	/**
	 * �õ����ڼ�ʱ��ȥ������
	 * @param datetime
	 * @return String
	 * @throws Exception
	 */
	private String getDatetimeWithoutSec(String datetime) throws Exception{
		SimpleDateFormat sdf=new SimpleDateFormat(this.objectFormat.equals("")?"yyyy-MM-dd HH:mm":this.objectFormat);
		return sdf.format(DateUtils.parse(datetime));
	}
	
	
	
	/**
	 * �õ���ʱ������
	 * @param datetime
	 * @return String
	 * @throws Exception
	 */
	private String getTime(String datetime) throws Exception{
		SimpleDateFormat sdf=new SimpleDateFormat(this.objectFormat.equals("")?"HH:mm:ss":this.objectFormat);
        try {
            SimpleDateFormat trySdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
            trySdf.parse(datetime);
            datetime = datetime.substring(11, 19);
        } catch (Exception e) {
            
        }
		return sdf.format(DateUtils.parse(datetime));
	}

	/**
	 * 
	 * @param attributeName
	 * @param attributeValue
	 * @param pageContext
	 * @return String
	 * @throws JspException
	 */
	protected String evaluateAndEscapeHtml(
		String attributeName, 
		String attributeValue, 
		PageContext pageContext) 
		throws JspException {
		String escapedHtml = HtmlUtils.htmlEscape(ExpressionEvaluationUtils.evaluateString(attributeName, attributeValue, pageContext));
		return StringUtils.trimToEmpty(escapedHtml);
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getLongTextType() {
		return longTextType;
	}

	public void setLongTextType(String longTextType) {
		this.longTextType = longTextType;
	}

	public String getSelButtonAction() {
		return selButtonAction;
	}

	public void setSelButtonAction(String selButtonAction) {
		this.selButtonAction = selButtonAction;
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

	public String getSearchButtonAction() {
		return searchButtonAction;
	}

	public void setSearchButtonAction(String searchButtonAction) {
		this.searchButtonAction = searchButtonAction;
	}

	public String getComInvorkeeClassFullName() {
		return comInvorkeeClassFullName;
	}

	public void setComInvorkeeClassFullName(String comInvorkeeClassFullName) {
		this.comInvorkeeClassFullName = comInvorkeeClassFullName;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public String getMaxlength() {
		return maxlength;
	}

	public void setMaxlength(String maxlength) {
		this.maxlength = maxlength;
	}

	public String getReadonly() {
		return readonly;
	}

	public void setReadonly(String readonly) {
		this.readonly = readonly;
	}

	public String getHaveDateButton() {
		return haveDateButton;
	}

	public void setHaveDateButton(String haveDateButton) {
		this.haveDateButton = haveDateButton;
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

	public String getSignatureButtonAction() {
		return signatureButtonAction;
	}

	public void setSignatureButtonAction(String signatureButtonAction) {
		this.signatureButtonAction = signatureButtonAction;
	}

	public String getWfPermissionCode() {
		return wfPermissionCode;
	}

	public void setWfPermissionCode(String wfPermissionCode) {
		this.wfPermissionCode = wfPermissionCode;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getLongTextSaveFormat() {
		return longTextSaveFormat;
	}

	public void setLongTextSaveFormat(String longTextSaveFormat) {
		this.longTextSaveFormat = longTextSaveFormat;
	}

	public String getMaxviewlength() {
		return maxviewlength;
	}

	public void setMaxviewlength(String maxviewlength) {
		this.maxviewlength = maxviewlength;
	}
/**
	public String getNewLineOfView() {
		return newLineOfView;
	}

	public void setNewLineOfView(String newLineOfView) {
		this.newLineOfView = newLineOfView;
	}
*/
}
