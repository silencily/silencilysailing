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
 * Ϊ��Ҫ���������ݷ���, Ŀ��������
 * <ul>
 * <li>��ֹ�ڴ����</li>
 * <li>Excel �ļ������������ 65535, ����������ݳ���������, �������</li>
 * </ul>
 * @see SimpleExportRowsGrouper
 * @see ByCountExportRowsGrouper
 * @since 2006-7-16
 * @author java2enterprise
 * @version $Id: ExportRowsGrouper.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 */
public interface ExportRowsGrouper {
	
	/**
	 * ʵ�ַ��鹦��, ��������һ�� List, ���е�ÿ��Ԫ��ҲӦ���� List, ÿ�� List �е�Ԫ���� {@link TransferExportRow}
	 * @param exportRows exportRows list fill with {@link TransferExportRow}
	 * @return list of list
	 */
	List group(List exportRows);
}
