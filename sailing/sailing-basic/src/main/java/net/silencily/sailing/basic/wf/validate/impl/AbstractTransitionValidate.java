package net.silencily.sailing.basic.wf.validate.impl;

import java.util.HashMap;
import java.util.Map;

import net.silencily.sailing.basic.wf.WorkflowInfo;
import net.silencily.sailing.basic.wf.util.WfUtils;
import net.silencily.sailing.basic.wf.validate.TransitionValidate;
import net.silencily.sailing.basic.wf.validate.expression.ExpressionValidate;
import net.silencily.sailing.basic.wf.validate.expression.SimpleExpressionValidate;

public class AbstractTransitionValidate implements TransitionValidate{

	private Map params = new HashMap();	

	private static String stepExpressionRegex = "\\;";	
	
	private static String expressionRegex = "\\:";
	
	private static String classExpressionRegex = "class[";			
	
	public boolean validate(WorkflowInfo info, String nextStepId) {
		boolean ret = true;
		Map expressionMap = convert(WfUtils.getStep(info.getWorkflowName(), info.getCurrentStep().getId()).getStepChecks()); 
			//convert("1:[bean.int] == 1 && [bean.string] == '1';2:class[com.qware.wf.validate.expression.impl.Test]");
		ExpressionValidate expressionValidate = null;
		String expre = (String)expressionMap.get(nextStepId);
		if (expre == null || "".equals(expre)) {
			ret = true;
		} else if (this.IsClassExpression(expre)) {
			try {							
				expressionValidate = (ExpressionValidate)Class.forName(expre.replaceFirst("class\\[", "").replaceFirst("\\]", "").trim()).newInstance();
				ret = expressionValidate.validate(info); 
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("类表达式异常");
			}
		} else {
			try {
				expressionValidate = new SimpleExpressionValidate();
				Map map = new HashMap();
				map.put("pageContext", params.get("pageContext"));
				map.put("expre", expre);
				expressionValidate.setParams(map);
				ret = expressionValidate.validate(info);	
			} catch (RuntimeException e) {
				e.printStackTrace();
				throw new RuntimeException("表达式异常");
			}
					}
		return ret;
	}	

	public void setParameters(Map params) {
		this.params = params;		
	}	
	
	private boolean IsClassExpression(String str) {
		boolean ret = false;
		if (str != null && !"".equals(str)) {			
			if (str.startsWith(classExpressionRegex)) {
				ret = true;
			}	
		}
		
		return ret;
	}
	private Map convert(String expressionList) {
		Map ret = new HashMap();
		try {
			if (expressionList != null && !"".equals(expressionList)) {
				String[] expressions = expressionList.split(stepExpressionRegex);
				for (int i=0; i<expressions.length; i++) {
					String[] expression = expressions[i].trim().split(expressionRegex);
					ret.put(expression[0], expression[1]);
				}	
			}
				
		} catch (RuntimeException e) {
			throw new RuntimeException("表达式格式异常");
		}
		return ret;
	}
}
