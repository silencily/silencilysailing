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
 * <class>TransferImportActionTemplate</class> 是导入数据 Action 的基类, Use Tempate Pattern 
 * @since 2005-9-26
 * @author 王政
 * @version $Id: TransferImportActionTemplate.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public abstract class TransferImportActionTemplate extends ActionSupport implements Preparable {

	//	~ Static fields/initializers =============================================
	
	/** 导入文件的 Global Result */
	public static final String GLOBAL_TRANSER_IMPORT_RESULT = "globalTransferImportResult";
	
	/** 保存导入结果对象的 Session Key */
	public static final String IMPORT_RESULT_SESSION_KEY = "com.coheg.framework.transfer.web.webwork.TransferImportActionTemplate.importResult";
	
	/** 所有支持的文件类型 */
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
	
	/** 导入日期, 有些模块不需要使用 */
	protected Date importDate = DBTimeUtil.getDBTime();
	
	/** 导入数据后的结果 */
	protected TransferImportResult transferImportResult = new TransferImportResult();
	
	/** 待处理的数据 Id 集合 */
	protected String[] ids = new String[0];
	
	//	~ Methods ================================================================
	
	/**
	 * 得到 TransferImportManager, 子类必须实现此方法
	 * @see TransferImportManager
	 * @see com.coheg.framework.transfer.core.TransferImportTemplate
	 * @return TransferImportManager
	 */
	protected abstract TransferImportManager getTransferImportManager();
	
	/**
	 * 得到 实现类 在 xwork 中的配置名称, 注意是 namespace + actionName, 将被用于 /jsp/transer/import.jsp 中
	 * @return the full action name
	 */
	public abstract String getActionName();
	
	/**
	 * 导入之后的业务方法, 可以做一些操作例如 addActionMessage()
	 * @param transferImportResult the transferImportResult
	 */
	protected abstract void businessAfterImported(TransferImportResult transferImportResult);
	
	/**
	 * 确认数据后的业务方法, 可以做一些操作例如 addActionMessage()
	 * @param confirmedReturnObject the object returned by {@link TransferImportManager#confirmCorrectData(Serializable[])}
	 */
	protected abstract void businessAfterConfirmed(Object confirmedReturnObject);
	
	/**
	 * 删除错误数据后的业务方法, 可以做一些操作例如 addActionMessage()
	 * @param removedReturnObject the object returned by {@link TransferImportManager#removeWrongData(Serializable[])}
	 */
	protected abstract void businessAfterRemoved(Object removedReturnObject);
	
	/**
	 * 页面上是否显示导入日期属性, 默认为 false, Con-Create class 可以覆盖此方法
	 * @return whether require import Date
	 */
	public boolean requireImportDate() {
		return false;
	}
	
	/**
	 * 检查 Action 需要的组件是否注入成功, 子类可以覆盖此方法用于检查组件是否注入成功
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
	 * 得到导入数据的属性元信息
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
	 * 导入数据
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
		message.append(" 导入数据成功, 情况如下 : <br> &nbsp;&nbsp;&nbsp;正常导入数据 [ ");
		message.append(importResult.getImportSuccessedCount());
		message.append(" ] 条 <br> &nbsp;&nbsp;&nbsp;导入期间因为类型错误忽略  [ ");
		message.append(importResult.getImportTypeConversionErrorExceptions().size());
		message.append(" ] 条 <br> &nbsp;&nbsp;&nbsp;导入期间因为其他异常原因忽略 [ ");
		message.append(importResult.getImportCallbackExceptions().size());
		message.append(" ] 条 <br> ");
		
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
			
			addFieldError("file", " 请处理完所有需要待处理的数据后再执行导入动作 ");
			return false;
		}
		
		if (getFile() == null) {
			addFieldError("file", " 请上传文件 ");
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
			addFieldError("file", " 文件格式只能是 .xls 或 .txt ");
			return false;
		}	
		
		if (requireImportDate() && getImportDate() == null) {
			addFieldError("importDate", " 不能为空 ");
			return false;
		}
		
		if (requireImportDate() && getImportDate() != null && getImportDate().after(DBTimeUtil.getDBTime())) {
			addFieldError("importDate", " 请选择一个今天以前的日期 ");
			return false;
		}
		
		
		return true;
	}
	
	
	/**
	 * 删除导入错误的数据
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
	 * 处理已导入的数据
	 * @param isRemove 是否是删除操作
	 * @throws Exception if error happens
	 */
	private Object processData(boolean isRemove, String[] ids) throws Exception {
		TransferImportResult importResult = getRequireToBeProcessedData();		
		if (ids == null || ids.length == 0) {
			addActionError(" 请至少选择一条数据进行处理 ");
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
		message.append(" 成功");
		if (isRemove) {
			message.append("删除");
		} else {
			message.append("确认");
		}
		
		message.append("数据 [ ");
		message.append(ids.length);
		message.append(" ] 条");
		
		addActionMessage(message.toString());
		
		return returnObject;
	}
	
	/**
	 * 确认已导入的数据
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
	 * 显示导入列表
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
		
		throw new UnsupportedOperationException("不支持的文件类型 : " + fileContentType);
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
