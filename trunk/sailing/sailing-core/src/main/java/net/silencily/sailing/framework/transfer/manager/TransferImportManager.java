/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.transfer.manager;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import net.silencily.sailing.framework.transfer.TransferImportResult;
import net.silencily.sailing.framework.transfer.exceptions.ImportProcessDataCallbackException;
import net.silencily.sailing.framework.transfer.exceptions.TransferException;
import net.silencily.sailing.framework.transfer.meta.FileType;
import net.silencily.sailing.framework.transfer.meta.ImportMetaData;

/**
 * <class>TransferImportManager</class> �ṩ�˵�������ʱ��Ҫ�ķ���, ʵ����Ӧ���� {@link com.coheg.framework.transfer.core.support.TransferImportDaoSupport} ������, 
 * ���е� {@link com.coheg.framework.transfer.core.TransferImportTemplate} �� {@link TransferImportManager} �еķ����ṩ�˾������֧��, ͨ���������ֻ��Ҫ�򵥵�ʵ�ֶ�
 * ������ı������ɾ�Ĳ�������
 * @see com.coheg.framework.transfer.core.support.TransferImportDaoSupport
 * @see com.coheg.framework.transfer.core.TransferImportOperations
 * @see com.coheg.framework.transfer.core.TransferImportTemplate
 * @since 2005-9-26
 * @author ����
 * @version $Id: TransferImportManager.java,v 1.1 2010/12/10 10:54:28 silencily Exp $
 */
public interface TransferImportManager {
	
	/**
	 * �������ݷ���, �� spring �����ļ���Ӧ������������ : <p>
	 * 
	 *    &lt;prop key="importData"&gt;PROPAGATION_REQUIRES_NEW, +com.coheg.framework.transfer.exceptions.TransferException&lt;/prop&gt;
	 * 
	 * <p>
	 * ʵ����ֻ��Ҫ���� {@link com.coheg.framework.transfer.core.TransferImportOperations#importData(InputStream, FileType, Date, TransferImportCallback)}, 
	 * �ټ򵥵�ʵ���������ݹ��ܼ���
	 * 
	 * @param in the input stream
	 * @param fileType the fileType
	 * @param importDate ��������, ������ҳ��
	 * @return the TransferImportResult
	 * @throws TransferException ע��, �׳����쳣ʱ����Ӧ�� commit !!!
	 * @see com.coheg.framework.transfer.core.TransferImportOperations#importData(InputStream, FileType, Date, TransferImportCallback)
	 * @see com.coheg.framework.transfer.callbacks.TransferImportCallback
	 * @see TransferImportResult
	 * @see com.coheg.framework.transfer.TransferImportable
	 */
	TransferImportResult importData(final InputStream in, final FileType fileType, final Date importDate) throws TransferException;
	
	/**
	 * ɾ����������,  �� spring �����ļ���Ӧ������������ : <p>
	 *   
	 *   &lt;prop key="removeWrongData"&gt;PROPAGATION_REQUIRES_NEW, +com.coheg.framework.transfer.exceptions.ImportProcessDataCallbackException&lt;/prop&gt;
	 *  
	 * <p>
	 * ʵ����ֻ��Ҫ���� {@link com.coheg.framework.transfer.core.TransferImportOperations#processDataAfterImported(Serializable[], TransferImportProcessDataCallback)}, 
	 * �ټ򵥵�ʵ��ɾ�����ݹ��ܼ���
	 *   
	 * @param errorIds �� ui ��ȡ�õ�ʵ�� id ����
	 * @return ����Ķ���, ʵ�������������ҵ����Ҫ����Ϣ
	 * @throws ImportProcessDataCallbackException ע��,���׳����쳣ʱ����Ӧ�� commit !!!
	 * @see com.coheg.framework.transfer.core.TransferImportOperations#processDataAfterImported(Serializable[], TransferImportProcessDataCallback)
	 * 
	 */
	Object removeWrongData(Serializable[] errorIds) throws ImportProcessDataCallbackException;
	
	/**
	 * ȷ������,  �� spring �����ļ���Ӧ������������ : <p>
	 *   
	 *   &lt;prop key="confirmCorrectData"&gt;PROPAGATION_REQUIRES_NEW, +com.coheg.framework.transfer.exceptions.ImportProcessDataCallbackException&lt;/prop&gt;
	 *  
	 * <p>
	 * ʵ����ֻ��Ҫ���� {@link com.coheg.framework.transfer.core.TransferImportOperations#processDataAfterImported(Serializable[], TransferImportProcessDataCallback)}, 
	 * �ټ򵥵�ʵ���޸����ݹ��ܼ���
	 * @param errorIds �� ui ��ȡ�õ�ʵ�� id ����
	 *   
	 * @return ����Ķ���, ʵ�������������ҵ����Ҫ����Ϣ
	 * @throws ImportProcessDataCallbackException ע��,���׳����쳣ʱ����Ӧ�� commit !!!
	 * @see com.coheg.framework.transfer.core.TransferImportOperations#processDataAfterImported(Serializable[], TransferImportProcessDataCallback)
	 * 
	 */
	Object confirmCorrectData(Serializable[] correctIds) throws ImportProcessDataCallbackException;
	
	/**
	 * �õ���Ҫ���������, һ���Ǳ��� confirmed �ֶ�Ϊ null ������, ע�� List �е�Ԫ������Ӧ��Ϊ {@link TransferImportable} !!!
	 * @return list fill with {@link TransferImportable}
	 */
	List getRequireToBeProcessedData();
		
	/**
	 * �õ�������������Ҫ��Ԫ��Ϣ, �������ڸ�ʽ��, �ı��������Ҫ�����ʵ�����Լ����������� properteis
	 * @return the ImportMetaData
	 * @see ImportMetaData
	 */
	ImportMetaData getImportMetaData();
	
}
