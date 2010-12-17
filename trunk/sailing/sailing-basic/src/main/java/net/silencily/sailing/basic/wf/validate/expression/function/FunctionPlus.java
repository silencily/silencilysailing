package net.silencily.sailing.basic.wf.validate.expression.function;

import java.util.Collection;

public interface FunctionPlus extends Function {

	Object btsum(Collection collection);

	Object btavg(Collection collection);

	Object btmax(Collection collection);

	Object btmin(Collection collection);
}
