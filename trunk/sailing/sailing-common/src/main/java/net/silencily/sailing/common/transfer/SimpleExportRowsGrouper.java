/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.common.transfer;

import java.util.ArrayList;
import java.util.List;

/**
 * ������, ֱ�ӽ� exoprtRows ������ list �ĵ�һ��Ԫ��
 * @since 2006-7-16
 * @author java2enterprise
 * @version $Id: SimpleExportRowsGrouper.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 */
public class SimpleExportRowsGrouper implements ExportRowsGrouper {

	public List group(List exportRows) {
		List groupedList = new ArrayList(1);
		groupedList.add(exportRows);
		return groupedList;
	}

}
