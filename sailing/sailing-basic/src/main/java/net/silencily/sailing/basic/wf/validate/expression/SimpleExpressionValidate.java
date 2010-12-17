package net.silencily.sailing.basic.wf.validate.expression;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import net.silencily.sailing.basic.wf.WorkflowInfo;
import net.silencily.sailing.basic.wf.validate.expression.function.Function;
import net.silencily.sailing.basic.wf.validate.expression.function.FunctionPlus;
import net.silencily.sailing.basic.wf.validate.expression.function.impl.FunctionImpl;

public class SimpleExpressionValidate extends AbstractExpressionValidate {

	private static String rightParenthesis = "\\]";
	
	private static String leftParenthesis = "\\[";
	
	protected boolean doValidate(WorkflowInfo info) {
		String expre = (String)this.getParams().get("expre");		
		List targets = this.convert(expre);
		Map values = this.values(info, targets);
		String finalExpre = this.replace(expre, values);
		return execute(finalExpre);
	}

	private boolean execute(String expre) {
		boolean ret = true;
		PageContext pageContext = (PageContext)this.getParams().get("pageContext");
		try {
			ret = org.springframework.web.util.ExpressionEvaluationUtils.evaluateBoolean("", expre, pageContext);
		} catch (JspException e) {			
			e.printStackTrace();
			throw new RuntimeException("表达式不能被解析");
		}
		return ret;
	}
	private String replace(String expre, Map values) {
		Set keySet = values.keySet();
		Iterator it = keySet.iterator();
		while (it.hasNext()) {
			String key = (String)it.next();
			Object value = values.get(key);
			String oldS = this.leftParenthesis + key + this.rightParenthesis;
			String newS = String.valueOf(value);
			if (value.getClass() == String.class) {
				newS = "'" + value + "'";
			}
			expre = expre.replaceAll(oldS, newS);	
		}
		
		return "${" + expre + "}";
	}
	
	private Map values(WorkflowInfo info, List targets) {
		Map ret= new HashMap();
		for (int i=0; i<targets.size(); i++) {
			String key = (String)targets.get(i);
			
			Object value = null;
			List list = FunctionUtils.isFunction((String)targets.get(i));
			if (list != null) {
				key = FunctionUtils.convert(key);
				Method method = (Method)list.get(0);
				String target = (String)list.get(1);
				Function function = null;
				Object v = null;
				try {
					if (method.getName().startsWith("bt")) {
						String[] vars = target.split("\\.");
						function = new FunctionImpl(vars[vars.length - 1]);						
						v = this.getValue(info, target.replaceFirst(vars[vars.length - 1], ""));
					} else {
						function = new FunctionImpl();
						v = this.getValue(info, target);
					}
					
					if (!Collection.class.isAssignableFrom(v.getClass()))
						throw new RuntimeException("表达式函数的参数必须是一个集合");
					value = method.invoke(function, new Object[] {v});
				} catch (Exception e) {					
					e.printStackTrace();
					throw new RuntimeException("表达式函数计算时出错");
				}				
			} else {
				value = (this.getValue(info, (String)targets.get(i)));
			}
			if (value == null)
				throw new RuntimeException("表达式" + (String)targets.get(i) + "时出错");
			ret.put(key, value);
		}
		
		return ret;
	}
	private Object getValue(WorkflowInfo info, String str) {
		return invoke(info, getGetMethods(str));
	}
	private String[] getGetMethods(String str) {
		String[] tvar = str.split("\\.");
		String[] methods = new String[tvar.length - 1];
		for (int i=1; i<tvar.length; i++) {
			String result = "get";
			String first = tvar[i].substring(0, 1);
			String last = tvar[i].substring(1);
			first = first.toUpperCase();				
			methods[i - 1] = result + first + last; 			
		}
		return methods;
	}
	private Object invoke(Object obj, String[] method) {
		
		Object ret = null;				
		ret = invoke(obj, method[0]);
		for (int i=1; i<method.length; i++) {			
			ret = invoke(ret, method[i]);
		}		
		return ret;
	}
	private Object invoke(Object obj, String method) {
		Object ret = null;
		Method m = null;	
		try {									
			m = obj.getClass().getDeclaredMethod(method, null);			
		} catch (NoSuchMethodException e) {
			try {
				m = obj.getClass().getSuperclass().getDeclaredMethod(method, null);
			} catch (Exception inE) {
				inE.printStackTrace();
			}
		}
		try {			
			ret = m.invoke(obj, null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("请检查表达式项");
		}
		return ret;
	}	
	
	private List convert(String expre) {
		List ret = new ArrayList();		
		String[] first = expre.split(rightParenthesis);
		for (int i=0; i<first.length - 1; i++) {			
			String[] second = first[i].split(leftParenthesis);
			String content = second[second.length - 1];
			String target = content.trim();			
			ret.add(target);
		}
		return ret;
	}
	private static class FunctionUtils {
		private static List func = new ArrayList();
		
		static {
			func.add("sum");
			func.add("avg");
			func.add("max");
			func.add("min");
			func.add("btsum");
			func.add("btavg");
			func.add("btmax");
			func.add("btmin");
		}
		public static List isFunction(String target) {
			List ret = null;
			for (int i=0; i<func.size(); i++) {
				String f = (String)func.get(i);
				if (target.startsWith(f + "(")) {
					ret = new ArrayList();
					if (!target.endsWith(")")) {
						throw new RuntimeException("表达式函数没有右括号");
					}
					Method method = null;
					try {
						method = Function.class.getDeclaredMethod(f, new Class[]{Collection.class});
						
					} catch (Exception e) {
						try {
							method = FunctionPlus.class.getDeclaredMethod(f, new Class[]{Collection.class});
						} catch (Exception e2) {
							e2.printStackTrace();						
							throw new RuntimeException("表达式函数类有异常, 请与管理员联系");
						}
					}
					if (method == null) 
						throw new RuntimeException("method is null.");
					ret.add(method);
					String temp = target.replaceFirst(f + "\\(", "");
					temp = temp.substring(0, temp.length() - 1);
					if (temp == null || "".equals(temp.trim())) 
						throw new RuntimeException("表达式函数" + f + "里必须写参数");
					ret.add(temp);
					break;
				}
			}
			return ret;
		}
		
		public static String convert(String target) {
			return target.replaceFirst("\\(", "\\\\(").substring(0, target.length()).concat("\\)");
		}
	}	
}
