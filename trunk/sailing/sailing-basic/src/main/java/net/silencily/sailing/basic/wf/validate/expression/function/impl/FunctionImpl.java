package net.silencily.sailing.basic.wf.validate.expression.function.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import net.silencily.sailing.basic.wf.validate.expression.function.FunctionPlus;

public class FunctionImpl implements FunctionPlus{

	private String item;
	
	public FunctionImpl(String item) {
		this.item = item;
	}
	public FunctionImpl() {
		
	}
	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public Object btavg(Collection collection) {
		
		return avg(convert(collection, item));
	}

	public Object btmax(Collection collection) {
		
		return max(convert(collection, item));
	}

	public Object btmin(Collection collection) {
		
		return min(convert(collection, item));
	}

	public Object btsum(Collection collection) {		
		return sum(convert(collection, item));
	}

	private Collection convert(Collection collection, String item) {
		
		Collection coll = new ArrayList();
		Iterator it = collection.iterator();
		Method method = null;
		boolean first = true;
		
		String methodName = "get";
		String pre = item.substring(0, 1);
		String post = item.substring(1);
		pre = pre.toUpperCase();		
		methodName = methodName + pre + post;
		
		while (it.hasNext()) {
			Object obj = it.next();
			try {
				if (first) {					
					method = obj.getClass().getDeclaredMethod(methodName, null);
					first = false;
				}
				coll.add(method.invoke(obj, null));
			} catch (Exception e) {				
				e.printStackTrace();
				throw new RuntimeException("表达式解析" + item + "时出错");
			}
			
		}
		return coll;
	}
	public Object avg(Collection collection) {
		Double size = new Double(collection.size());
		Number sum = (Number)sum(collection);
		return new Double(sum.doubleValue() / size.doubleValue());
	}

	public Object max(Collection collection) {		
		return Collections.max(collection);
	}

	public Object min(Collection collection) {
		return Collections.min(collection);
	}

	public Object sum(Collection collection) {
		double ret = 0;
		Iterator it = collection.iterator();
		while (it.hasNext()) {
			Number number = (Number)it.next();
			ret += number.doubleValue();
		}
		return new Double(ret);
	}

}
