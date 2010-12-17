package net.silencily.sailing.basic.wf.validate.expression.evaluation.impl;

import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import net.silencily.sailing.basic.wf.validate.expression.evaluation.ExpressionEvaluation;

public class EvaluationDefaultAdapter implements ExpressionEvaluation{

	public boolean evaluateBoolean(Map map) {
		String expre = (String)map.get("expre");
		PageContext pageContext = (PageContext)map.get("pageContext");
		boolean ret = false;
		try {
			ret = org.springframework.web.util.ExpressionEvaluationUtils.evaluateBoolean("", expre, pageContext);
		} catch (JspException e) {			
			e.printStackTrace();
			throw new RuntimeException("表达式不能被解析");
		}
		return ret;
	}

}
