/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.transfer.web.webwork;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.silencily.sailing.framework.transfer.TransferImportResult;
import net.silencily.sailing.framework.transfer.TransferImportable;
import net.silencily.sailing.framework.transfer.exceptions.ImportProcessDataCallbackException;
import net.silencily.sailing.framework.transfer.manager.TransferImportManager;
import net.silencily.sailing.framework.transfer.meta.FileType;
import net.silencily.sailing.utils.DBTimeUtil;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.ActionSupport;
import com.opensymphony.xwork.Preparable;

/**
 * <class>TransferImportActionTemplate</class> �ǵ������� Action �Ļ���, Use Tempate Pattern 
 * @since 2005-9-26
 * @author ����
 * @version $Id: TransferImportActionTemplate.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public abstract class TransferImportActionTemplate extends ActionSupport implements Preparable {

	//	~ Static fields/initializers =============================================
	
	/** �����ļ��� Global Result */
	public static final String GLOBAL_TRANSER_IMPORT_RESULT = "globalTransferImportResult";
	
	/** ���浼��������� Session Key */
	public static final String IMPORT_RESULT_SESSION_KEY = "com.coheg.framework.transfer.web.webwork.TransferImportActionTemplate.importResult";
	
	/** ����֧�ֵ��ļ����� */
	public static final List ALL_SUPPORTED_FILE_TYPES = Collections.unmodifiableList(
			new LinkedList() {
				private static final long serialVersionUID = 3501728977902315867L;
				{
					add(FileType.XLS);
					add(FileType.TXT);
				}
			}
	);
	
	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = -3423275101058820553L;
	
	//	~ Instance fields ========================================================
	
	protected File file;
	
	protected String fileFileName;
	
	protected String fileContentType;
	
	/** ��������, ��Щģ�鲻��Ҫʹ�� */
	protected Date importDate = DBTimeUtil.getDBTime();
	
	/** �������ݺ�Ľ�� */
	protected TransferImportResult transferImportResult = new TransferImportResult();
	
	/** ����������� Id ���� */
	protected String[] ids = new String[0];
	
	//	~ Methods ================================================================
	
	/**
	 * �õ� TransferImportManager, �������ʵ�ִ˷���
	 * @see TransferImportManager
	 * @see com.coheg.framework.transfer.core.TransferImportTemplate
	 * @return TransferImportManager
	 */
	protected abstract TransferImportManager getTransferImportManager();
	
	/**
	 * �õ� ʵ���� �� xwork �е���������, ע���� namespace + actionName, �������� /jsp/transer/import.jsp ��
	 * @return the full action name
	 */
	public abstract String getActionName();
	
	/**
	 * ����֮���ҵ�񷽷�, ������һЩ�������� addActionMessage()
	 * @param transferImportResult the transferImportResult
	 */
	protected abstract void businessAfterImported(TransferImportResult transferImportResult);
	
	/**
	 * ȷ�����ݺ��ҵ�񷽷�, ������һЩ�������� addActionMessage()
	 * @param confirmedReturnObject the object returned by {@link TransferImportManager#confirmCorrectData(Serializable[])}
	 */
	protected abstract void businessAfterConfirmed(Object confirmedReturnObject);
	
	/**
	 * ɾ���������ݺ��ҵ�񷽷�, ������һЩ�������� addActionMessage()
	 * @param removedReturnObject the object returned by {@link TransferImportManager#removeWrongData(Serializable[])}
	 */
	protected abstract void businessAfterRemoved(Object removedReturnObject);
	
	/**
	 * ҳ�����Ƿ���ʾ������������, Ĭ��Ϊ false, Con-Create class ���Ը��Ǵ˷���
	 * @return whether require import Date
	 */
	public boolean requireImportDate() {
		return false;
	}
	
	/**
	 * ��� Action ��Ҫ������Ƿ�ע��ɹ�, ������Ը��Ǵ˷������ڼ������Ƿ�ע��ɹ�
	 * @throws Exception if any error happens
	 */
	protected void checkActionConfig() throws Exception {	
	}
	
	/**
	 * @see com.opensymphony.xwork.ActionSupport#doDefault()
	 */
	public String doDefault() throws Exception {
		return INPUT;
	}

	
	/**
	 * �õ��������ݵ�����Ԫ��Ϣ
	 * @see com.coheg.framework.transfer.meta.ImportMetaData#getEntityMap()
	 * @return the map of entity attributes
	 */
	public Map getEntityMap() {
		return getTransferImportManager().getImportMetaData().getEntityMap();
	}
		
	/**
	 * @see com.opensymphony.xwork.Preparable#prepare()
	 */
	public void prepare() throws Exception {
		Assert.notNull(getTransferImportManager(), " transferImportManager is required.  ");
		checkActionConfig();
	}
	
	
	/**
	 * ��������
	 */
	public String doImport() throws Exception {	
		if (! validateBeforeImport()) {
			setTransferImportResult(getRequireToBeProcessedData());
			return GLOBAL_TRANSER_IMPORT_RESULT;
		}
		
		InputStream in = new BufferedInputStream(new FileInputStream(file));
		TransferImportResult importResult = getTransferImportManager().importData(in, lookupFileType(getFileContentType()), getImportDate());
		setTransferImportResult(importResult);
		
		StringBuffer message = new StringBuffer();
		message.append(" �������ݳɹ�, ������� : <br> &nbsp;&nbsp;&nbsp;������������ [ ");
		message.append(importResult.getImportSuccessedCount());
		message.append(" ] �� <br> &nbsp;&nbsp;&nbsp;�����ڼ���Ϊ���ʹ������  [ ");
		message.append(importResult.getImportTypeConversionErrorExceptions().size());
		message.append(" ] �� <br> &nbsp;&nbsp;&nbsp;�����ڼ���Ϊ�����쳣ԭ����� [ ");
		message.append(importResult.getImportCallbackExceptions().size());
		message.append(" ] �� <br> ");
		
		addActionMessage(message.toString());
		
		businessAfterImported(getTransferImportResult());
		
		return GLOBAL_TRANSER_IMPORT_RESULT;
	}
	
	private TransferImportResult getRequireToBeProcessedData() {
		TransferImportResult transferImportResult = new TransferImportResult();
		List requireToBeProcessedData = getTransferImportManager().getRequireToBeProcessedData();
		if (requireToBeProcessedData == null) {
			requireToBeProcessedData = new LinkedList();
		}
		
		for (Iterator iter = requireToBeProcessedData.iterator(); iter.hasNext(); ) {
			((TransferImportable) iter.next()).setImportSuccessed(true);
		}
		
		transferImportResult.setTransferImportables(requireToBeProcessedData);
		transferImportResult.setImportSuccessedCount(requireToBeProcessedData.size());
		return transferImportResult;
	}

	private boolean validateBeforeImport() {
		if (getTransferImportManager().getRequireToBeProcessedData() != null 
			&& getTransferImportManager().getRequireToBeProcessedData().size() > 0) {
			
			addFieldError("file", " �봦����������Ҫ����������ݺ���ִ�е��붯�� ");
			return false;
		}
		
		if (getFile() == null) {
			addFieldError("file", " ���ϴ��ļ� ");
			return false;
		}
		
		boolean exist = false;
		
		for (Iterator iter = ALL_SUPPORTED_FILE_TYPES.iterator(); iter.hasNext(); ) {
			String conentType = ((FileType) iter.next()).getFileContentType();
			if (StringUtils.equals(conentType, getFileContentType())) {
				exist = true;
				break;
			}
		}
		
		if (! exist) {
			addFieldError("file", " �ļ���ʽֻ���� .xls �� .txt ");
			return false;
		}	
		
		if (requireImportDate() && getImportDate() == null) {
			addFieldError("importDate", " ����Ϊ�� ");
			return false;
		}
		
		if (requireImportDate() && getImportDate() != null && getImportDate().after(DBTimeUtil.getDBTime())) {
			addFieldError("importDate", " ��ѡ��һ��������ǰ������ ");
			return false;
		}
		
		
		return true;
	}
	
	
	/**
	 * ɾ��������������
	 * @return
	 * @throws Exception
	 */
	public String doRemove() throws Exception {	    
	    Object returnObject = processData(true, getIds());
		
		if (! hasActionErrors()) {
			businessAfterRemoved(returnObject);
		}
		
		return GLOBAL_TRANSER_IMPORT_RESULT;
	}
	
	/**
	 * �����ѵ��������
	 * @param isRemove �Ƿ���ɾ������
	 * @throws Exception if error happens
	 */
	private Object processData(boolean isRemove, String[] ids) throws Exception {
		TransferImportResult importResult = getRequireToBeProcessedData();		
		if (ids == null || ids.length == 0) {
			addActionError(" ������ѡ��һ�����ݽ��д��� ");
			setTransferImportResult(importResult);
			return null;
		}
		
		Object returnObject = null;
		try {
			if (isRemove) {
				returnObject = getTransferImportManager().removeWrongData(ids);
			} else {
				returnObject = getTransferImportManager().confirmCorrectData(ids);
			}
		} catch (ImportProcessDataCallbackException e) {
			addActionError(e.getParticularDescription());
			return null;
		}
		
		setTransferImportResult(getRequireToBeProcessedData());
			
		StringBuffer message = new StringBuffer();
		message.append(" �ɹ�");
		if (isRemove) {
			message.append("ɾ��");
		} else {
			message.append("ȷ��");
		}
		
		message.append("���� [ ");
		message.append(ids.length);
		message.append(" ] ��");
		
		addActionMessage(message.toString());
		
		return returnObject;
	}
	
	/**
	 * ȷ���ѵ��������
	 * @return
	 * @throws Exception
	 */
	public String doConfirm() throws Exception {
		Object returnObject = processData(false, getIds());
		
		if (! hasActionErrors()) {
			businessAfterConfirmed(returnObject);
		}
		
		return GLOBAL_TRANSER_IMPORT_RESULT;
	}
	
	
	/**
	 * ��ʾ�����б�
	 * @return the list of {@link TransferImportResult}
	 * @throws Exception
	 */
	public String doList() throws Exception {
		setTransferImportResult(getRequireToBeProcessedData());
		return GLOBAL_TRANSER_IMPORT_RESULT;
	}
	
	/**
	 * 
	 * @param fileContentType
	 * @return
	 */
	public static FileType lookupFileType(String fileContentType) {
		if (FileType.XLS_CONTENT_TYPE.equals(fileContentType)) {
			return FileType.XLS;
		} else if (FileType.TXT_CONTENT_TYPE.equals(fileContentType)) {
			return FileType.TXT;
		}
		
		throw new UnsupportedOperationException("��֧�ֵ��ļ����� : " + fileContentType);
	}
	
	
	public static String getContextPath(){
		return ServletActionContext.getRequest().getContextPath();
	}

	/**
	 * @return Returns the file.
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file The file to set.
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * @return Returns the fileContentType.
	 */
	public String getFileContentType() {
		return fileContentType;
	}

	/**
	 * @param fileContentType The fileContentType to set.
	 */
	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

	/**
	 * @return Returns the fileFileName.
	 */
	public String getFileFileName() {
		return fileFileName;
	}

	/**
	 * @param fileFileName The fileFileName to set.
	 */
	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	/**
	 * @return Returns the transferImportResult.
	 */
	public TransferImportResult getTransferImportResult() {
		return transferImportResult;
	}

	/**
	 * @param transferImportResult The transferImportResult to set.
	 */
	public void setTransferImportResult(TransferImportResult transferImportResult) {
		this.transferImportResult = transferImportResult;
	}

	/**
	 * @return Returns the ids.
	 */
	public String[] getIds() {
		return ids;
	}

	/**
	 * @param ids The ids to set.
	 */
	public void setIds(String[] toBeRemovedIds) {
		this.ids = toBeRemovedIds;
	}

	/**
	 * @return Returns the importDate.
	 */
	public Date getImportDate() {
		return importDate;
	}

	/**
	 * @param importDate The importDate to set.
	 */
	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}





	
}
