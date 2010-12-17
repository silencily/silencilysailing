package com.power.vfs.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

import com.power.vfs.FileObjectException;

public class DefaultPathNormalizer implements PathNormalizer {
	private Log logger;

	public DefaultPathNormalizer() {
	}

	public DefaultPathNormalizer(Log logger) {
		this.logger = logger;
	}

	public String normalize(String name) throws FileObjectException {
		if (StringUtils.isBlank(name))
			throw new IllegalArgumentException(
					"\u89C4\u8303\u5316\u8DEF\u5F84\u540D\u79F0\u65F6\u53C2\u6570\u662F\u7A7A\u503C");
		if (logger != null && logger.isDebugEnabled())
			logger.debug("\u89C4\u8303\u5316\u524D\u7684\u8DEF\u5F84[" + name
					+ "]");
		String result = name.replace('\\', '/');
		result = result.replaceAll("/{2,}", "/");
		if (result.charAt(0) != '/')
			result = "/" + result;
		if (!"/".equals(result) && result.endsWith("/"))
			result = result.substring(0, result.length() - 1);
		if (result.indexOf('.') > -1 || result.indexOf("..") > -1)
			result = parsePathWithDot(result);
		if (logger != null && logger.isDebugEnabled())
			logger.debug("\u89C4\u8303\u5316\u540E\u7684\u8DEF\u5F84[" + result
					+ "]");
		return result;
	}

	private String parsePathWithDot(String orig) {
		if (orig.length() == 1)
			return orig;
		orig = orig.substring(1);
		String strs[] = orig.split("\\s*/\\s*");
		List paths = new ArrayList(strs.length);
		List result = new ArrayList(strs.length);
		paths.addAll(Arrays.asList(strs));
		Iterator it = paths.iterator();
		do {
			if (!it.hasNext())
				break;
			String path = (String) it.next();
			if (!".".equals(path))
				if ("..".equals(path)) {
					int priorLevelPathIndex = result.size() - 1;
					if (priorLevelPathIndex < 0)
						throw new FileObjectException(
								"\u4F7F\u7528'..'\u8868\u793A\u7684\u4E0A\u7EA7\u76EE\u5F55\u8D85\u8FC7\u4E86\u865A\u62DF\u6587\u4EF6\u7CFB\u7EDF\u6839\u76EE\u5F55["
										+ orig + "]",
								new IllegalArgumentException());
					result.remove(result.size() - 1);
				} else {
					result.add(path);
				}
		} while (true);
		StringBuffer sb = new StringBuffer();
		for (Iterator it1 = result.iterator(); it1.hasNext(); sb.append("/")
				.append(it1.next()))
			;
		return sb.toString();
	}

}
