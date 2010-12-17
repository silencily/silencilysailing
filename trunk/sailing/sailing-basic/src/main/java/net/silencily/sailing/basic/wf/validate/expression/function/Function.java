package net.silencily.sailing.basic.wf.validate.expression.function;

import java.util.Collection;

public interface Function {
	
	Object sum(Collection collection);
	
	Object avg(Collection collection);
	
	Object max(Collection collection);
	
	Object min(Collection collection);
}
