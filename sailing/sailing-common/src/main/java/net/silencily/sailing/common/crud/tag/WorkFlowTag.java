package net.silencily.sailing.common.crud.tag;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import net.silencily.sailing.hibernate3.EntityPlus;
import net.silencily.sailing.security.SecurityContextInfo;
import net.silencily.sailing.utils.Tools;

import org.springframework.web.util.ExpressionEvaluationUtils;


public class WorkFlowTag extends TagSupport {
	/**
	 * 描述： 属性名：serialVersionUID 属性类型：long
	 */
	private static final long serialVersionUID = 1485175067093016503L;

	private String mainEntry = "";

	private String subStr = "";
	
	private String msRelation = "";
	
	private String uncompare = "";
	
	private String displayField = "";

	private Object objectMainEntry;

	private String objectSubStr = "";
	
	private Hashtable objMsRelation;
	
	private String objUncompare = "";
	
	private String objDisplayField = "";

	public String getDisplayField() {
		return displayField;
	}

	public void setDisplayField(String displayField) {
		this.displayField = displayField;
	}

	public String getObjDisplayField() {
		return objDisplayField;
	}

	public void setObjDisplayField(String objDisplayField) {
		this.objDisplayField = objDisplayField;
	}

	public String getObjUncompare() {
		return objUncompare;
	}

	public void setObjUncompare(String objUncompare) {
		this.objUncompare = objUncompare;
	}

	public String getUncompare() {
		return uncompare;
	}

	public void setUncompare(String uncompare) {
		this.uncompare = uncompare;
	}

	/**
	 * 初始化解析所有传入值对象值
	 * 
	 * @throws JspException
	 */
	private void objectInit() throws JspException {
		objectMainEntry = (Object) ExpressionEvaluationUtils.evaluate(
				"mainEntry", mainEntry, pageContext);
		
		objectSubStr = ExpressionEvaluationUtils.evaluateString("subStr",
				subStr, pageContext);
		
		objMsRelation = getMsRelationMap(msRelation.replaceAll(" ", ""));
		
		objUncompare = ExpressionEvaluationUtils.evaluateString("uncompare",
				uncompare, pageContext);
		
		objDisplayField = ExpressionEvaluationUtils.evaluateString("displayField",
				displayField, pageContext);
	}

	private Hashtable getMsRelationMap(String relation) throws JspException {
		Hashtable result = new Hashtable();
		if("".equals(relation)){
			return result;
		}
		relation = relation.substring(2,relation.length()-2);
		String[] relations = relation.split("\\},\\{");
		for(int i=0;i<relations.length;i++){
			String[] subRel = relations[i].split(",");
			if(subRel.length != 2){
				throw new RuntimeException("property: msRelation format invalid");
			}
			result.put(subRel[0], ExpressionEvaluationUtils.evaluate("", subRel[1], pageContext));
		}
		return result;
	}

	public int doStartTag() throws JspException {
		try {
			objectInit();
			// 得到的MAINENTRY进行COPY，保存到SESSION
			HttpSession session = SecurityContextInfo.getSession();
			session.setAttribute("loadByTag", "true");
			saveMainEntry(objectMainEntry);
			// 处理STRING字符串，保存到SESSION
			saveSubString(objectSubStr,objectMainEntry);
			saveUncompare(objUncompare);
			saveDisplayField(objDisplayField);
		} catch (Exception je) {
			throw new JspTagException("SimpleTag: " + je.getMessage());
		}
		return 0;
	}

	private void saveDisplayField(String objDisplayField2) {
		HttpSession session = SecurityContextInfo.getSession();
		Map historyInfo = (Map) session.getAttribute("workFlowHistoryInfo");
		String str[] = objDisplayField2.split(",");
		if(null == historyInfo){
			historyInfo = new HashMap(0);
		}
		historyInfo.put("displayField", str);
		session.setAttribute("workFlowHistoryInfo", historyInfo);
	}

	private void saveUncompare(String objUncompare2) {
		HttpSession session = SecurityContextInfo.getSession();
		Map historyInfo = (Map) session.getAttribute("workFlowHistoryInfo");
		String str[] = objUncompare2.split(",");
		if(null == historyInfo){
			historyInfo = new HashMap(0);
		}
		historyInfo.put("uncompare", str);
		session.setAttribute("workFlowHistoryInfo", historyInfo);
	}

	public void saveMainEntry(Object mainEntry) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		if (Class.forName("com.qware.wf.WorkflowInfo").isAssignableFrom(
				mainEntry.getClass())) {
			((EntityPlus) mainEntry)
					.setReflectCopyEntityProperties(EntityPlus.isReflectCopyEntity);
			HttpSession session = SecurityContextInfo.getSession();
			Object temp = Tools.getUnProxyClass(mainEntry.getClass()).newInstance();
			temp = 	Tools.depthClone(mainEntry);
			setMsRelationShip(temp, objMsRelation);
			String idTemp = ((EntityPlus) mainEntry).getId();
			Map historyInfo = new HashMap(0);
			historyInfo.put(idTemp, temp);
			session.setAttribute("workFlowHistoryInfo", historyInfo);
			((EntityPlus) mainEntry)
					.setReflectCopyEntityProperties(EntityPlus.isNotReflectCopyEntity);
		}
	}

	private void setMsRelationShip(Object temp, Hashtable relation) {
		Enumeration e = relation.keys();
		while(e.hasMoreElements()){
			String key = (String)e.nextElement();
			try{
				Method getM = Tools.getGetMethod(key, Tools.getUnProxyClass(temp.getClass()));
				Method setM = Tools.getSetMethod(key, Tools.getUnProxyClass(temp.getClass()),new Class[]{getM.getReturnType()});
				Object obj = getM.invoke(temp, null);
				Object value = relation.get(key);
				if(obj instanceof Set && value instanceof Collection)
				{
					HashSet valueSet = new HashSet();
					valueSet.addAll((Collection)value);
					value = valueSet;
				}
				setM.invoke(temp, new Object[]{value});
			}catch(Exception ex){
				ex.printStackTrace();
				throw new RuntimeException("property: " 
											+ key + " of class: " 
											+ Tools.getUnProxyClass(temp.getClass()).getName() 
											+ " invalid");
			}
		}
	}

	public void saveSubString(String objectSubStr,Object mainEntry )
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		if (Class.forName("com.qware.wf.WorkflowInfo").isAssignableFrom(
				mainEntry.getClass())) {
			HttpSession session = SecurityContextInfo.getSession();
			Map historyInfo = (Map) session.getAttribute("workFlowHistoryInfo");
			String str[] = objectSubStr.split(",");
			if(null == historyInfo){
				historyInfo = new HashMap(0);
			}
			historyInfo.put("subStr", str);
			session.setAttribute("workFlowHistoryInfo", historyInfo);
	    }
	}

	public String getMainEntry() {
		return mainEntry;
	}

	public void setMainEntry(String mainEntry) {
		this.mainEntry = mainEntry;
	}

	public Object getObjectMainEntry() {
		return objectMainEntry;
	}

	public void setObjectMainEntry(Object objectMainEntry) {
		this.objectMainEntry = objectMainEntry;
	}

	public void setObjectMainEntry(String objectMainEntry) {
		this.objectMainEntry = objectMainEntry;
	}

	public String getObjectSubStr() {
		return objectSubStr;
	}

	public void setObjectSubStr(String objectSubStr) {
		this.objectSubStr = objectSubStr;
	}

	public String getSubStr() {
		return subStr;
	}

	public void setSubStr(String subStr) {
		this.subStr = subStr;
	}

	public String getMsRelation() {
		return msRelation;
	}

	public void setMsRelation(String msRelation) {
		this.msRelation = msRelation;
	}

	public Hashtable getObjMsRelation() {
		return objMsRelation;
	}

	public void setObjMsRelation(Hashtable objMsRelation) {
		this.objMsRelation = objMsRelation;
	}

}
