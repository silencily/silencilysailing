/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.web.webwork.converter;

import java.util.Iterator;
import java.util.Map;

import net.silencily.sailing.framework.transfer.meta.FileType;
import net.silencily.sailing.framework.transfer.web.webwork.TransferImportActionTemplate;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.webwork.util.WebWorkTypeConverter;
import com.opensymphony.xwork.util.TypeConversionException;

/**
 * <class>TransferFileTypeConverter</class> 是 {@link com.coheg.framework.transfer.meta.FileType} 的转换器
 * @since 2005-9-29
 * @author 王政
 * @version $Id: TransferFileTypeConverter.java,v 1.1 2010/12/10 10:54:23 silencily Exp $
 */
public class TransferFileTypeConverter extends WebWorkTypeConverter {

	/**
	 * 
	 * @see com.opensymphony.webwork.util.WebWorkTypeConverter#convertFromString(java.util.Map, java.lang.String[], java.lang.Class)
	 */
	public Object convertFromString(Map context, String[] values, Class toClass) {
		if (toClass != FileType.class) {
			throw new TypeConversionException(new UnsupportedOperationException(getClass() + " only support " + FileType.class));
		}

		if (values == null || values.length == 0) {
			throw new TypeConversionException(" values is required ");
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < values.length; i++) {
			sb.append(values[i]);
		}
		String value = sb.toString().trim();
			
		for (Iterator iter = TransferImportActionTemplate.ALL_SUPPORTED_FILE_TYPES.iterator(); iter.hasNext(); ) {
			FileType fileType = (FileType) iter.next();
			if (StringUtils.equals(value, fileType.getFileContentType())) {
				return fileType;
			}
		}
			
		throw new TypeConversionException("Can't convert " + value + " to " + FileType.class);
	}

	public String convertToString(Map context, Object o) {
		if (o == null) {
			return null;
		}
		
		if (! (o instanceof FileType)) {
			throw new TypeConversionException(new UnsupportedOperationException(getClass() + " only support " + FileType.class));
		}
		return ((FileType) o).getFileContentType();
	}

}
