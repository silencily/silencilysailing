/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.common.transfer;

import java.util.List;


/**
 * 为将要导出的数据分组, 目的有两个
 * <ul>
 * <li>防止内存溢出</li>
 * <li>Excel 文件的最大行数是 65535, 如果导出数据超出此行数, 必须分组</li>
 * </ul>
 * @see SimpleExportRowsGrouper
 * @see ByCountExportRowsGrouper
 * @since 2006-7-16
 * @author java2enterprise
 * @version $Id: ExportRowsGrouper.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 */
public interface ExportRowsGrouper {
	
	/**
	 * 实现分组功能, 分组结果是一个 List, 其中的每个元素也应该是 List, 每个 List 中的元素是 {@link TransferExportRow}
	 * @param exportRows exportRows list fill with {@link TransferExportRow}
	 * @return list of list
	 */
	List group(List exportRows);
}
