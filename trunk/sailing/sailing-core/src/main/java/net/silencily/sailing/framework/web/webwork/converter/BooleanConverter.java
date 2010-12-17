package net.silencily.sailing.framework.web.webwork.converter;

import java.util.Map;

import com.opensymphony.webwork.util.WebWorkTypeConverter;
import com.opensymphony.xwork.util.TypeConversionException;

/**
 * 对Boolean和String进行类型转化。如果value等于""，则转换为null对象。
 * @since 2005-9-19
 * @author 钱安川
 * @version $Id: BooleanConverter.java,v 1.1 2010/12/10 10:54:23 silencily Exp $
 */
public class BooleanConverter extends WebWorkTypeConverter{

	public Object convertFromString(Map context, String[] values, Class toClass) {
		if (toClass != Boolean.class) {
			throw new TypeConversionException(new UnsupportedOperationException(getClass() + " only support java.lang.Boolean Type!"));
		}

		if (values == null || values.length == 0 || "".equals(values[0])) {
			return null;
		}
		return Boolean.valueOf(values[0]);
	}

	public String convertToString(Map context, Object o) {
		if (o == null) {
			return null;
		}
		
		if (! (o instanceof Boolean)) {
			throw new TypeConversionException(new UnsupportedOperationException(getClass() + " only support java.lang.Boolean Type!"));
		}
		return ((Boolean)o).toString();
	}

}
