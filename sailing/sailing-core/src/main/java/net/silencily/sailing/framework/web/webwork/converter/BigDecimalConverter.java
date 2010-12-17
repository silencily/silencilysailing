package net.silencily.sailing.framework.web.webwork.converter;

import java.math.BigDecimal;
import java.util.Map;

import com.opensymphony.webwork.util.WebWorkTypeConverter;
import com.opensymphony.xwork.util.TypeConversionException;

/**
 * @since 2005-9-28
 * @author Ç®°²´¨
 * @version $Id: BigDecimalConverter.java,v 1.1 2010/12/10 10:54:21 silencily Exp $
 */
public class BigDecimalConverter extends WebWorkTypeConverter{

	public Object convertFromString(Map context, String[] values, Class toClass) {
		if (toClass != BigDecimal.class) {
			throw new TypeConversionException(new UnsupportedOperationException(getClass() + " only support java.math.BigDecimal Type!"));
		}

		if (values == null || values.length == 0 || "".equals(values[0])) {
			return null;
		}
		try {
			return new BigDecimal(values[0]);
		} catch (NumberFormatException e) {
			throw new TypeConversionException("Can't convert " + values[0] + " to java.math.BigDecimal " );
		}	
	}

	public String convertToString(Map context, Object o) {
		if (o == null) {
			return null;
		}
		
		if (! (o instanceof BigDecimal)) {
			throw new TypeConversionException(new UnsupportedOperationException(getClass() + " only support java.math.BigDecimal!"));
		}
		return ((BigDecimal)o).toString();
	}

}
