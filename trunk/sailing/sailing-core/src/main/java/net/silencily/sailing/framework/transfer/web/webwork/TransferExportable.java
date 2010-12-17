/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.transfer.web.webwork;

import java.util.List;

import net.silencily.sailing.framework.transfer.meta.FileType;
import net.silencily.sailing.framework.transfer.meta.TransferMetaData;

/**
 * ʹ�õ�������ʱ action ��Ҫʵ�ֵĽӿ�, ʵ�ִ˽ӿڵ� action ��ֱ��ʹ�� {@link com.coheg.framework.web.webwork.dispatcher.TransferExportResult} ������ļ����ͻ���
 * @see com.coheg.framework.web.webwork.dispatcher.TransferExportResult
 * @since 2005-9-28
 * @author ����
 * @version $Id: TransferExportable.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public interface TransferExportable {
	
	/**
	 * �õ���������
	 * @return the list fill with {@link com.coheg.framework.transfer.TransferExportRow}
	 */
	List getTransferExportRows();
	
	/**
	 * �õ�����������Ҫ��Ԫ��Ϣ
	 * @return the transferMetaData
	 */
	TransferMetaData getTransferMetaData();
	
	/**
	 * �õ��ļ�����, �˲���Ӧ���� {@link com.coheg.framework.web.webwork.converter.TransferFileTypeConverter} ת���õ�
	 * @see FileType
	 * @see com.coheg.framework.web.webwork.converter.TransferFileTypeConverter
	 * @return the FileType
	 */
	FileType getFileType();
	
}
