/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.transfer.meta;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <class>FileType</class> 为本系统导入导出支持的所有文件类型, 她是一个 Enum 类型
 * @since 2005-9-25
 * @author 王政
 * @version $Id: FileType.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 */
public class FileType {
	
	//	~ Static fields/initializers =============================================
	
	/** The content type of xls */
	public static final String XLS_CONTENT_TYPE = "application/vnd.ms-excel";
	
	/** The content type of txt */
	public static final String TXT_CONTENT_TYPE = "text/plain";
	
	/** XLS 文件扩展名 */
	public static final String XLS_FILE_SUFFIX = ".xls";
	
	/** TXT 文件扩展名 */
	public static final String TXT_FILE_SUFFIX = ".txt";
	
	public static final String XLS_FILE_CHINESE_TYPE_NAME = "XLS 格式";
	
	public static final String TXT_FILE_CHINESE_TYPE_NAME = "文本格式";
	
	/** XLS 类型 */
	public static final FileType XLS = new FileType(XLS_CONTENT_TYPE, XLS_FILE_SUFFIX, XLS_FILE_CHINESE_TYPE_NAME);
	
	/** TXT 类型 */
	public static final FileType TXT = new FileType(TXT_CONTENT_TYPE, TXT_FILE_SUFFIX, TXT_FILE_CHINESE_TYPE_NAME);

	//	~ Instance fields ========================================================
	
	private String fileContentType;
    
	private String fileSuffix;
	
	private String fileChineseTypeName;
	
	//~ Constructors ===========================================================
	
	private FileType(String fileContentType, String fileSuffix, String fileChineseTypeName) {
		this.fileContentType = fileContentType;
		this.fileSuffix = fileSuffix;
		this.fileChineseTypeName = fileChineseTypeName;
	}
	
	//	~ Methods ================================================================
	
	public static Map getExportFileTypeMap() {
		Map map = new LinkedHashMap();
		map.put(TXT.getFileContentType(), TXT.getFileChineseTypeName());
		map.put(XLS.getFileContentType(), XLS.getFileChineseTypeName());		
		return map;
	}
	
	
	/**
	 * @return Returns the fileContentType.
	 */
	public String getFileContentType() {
		return fileContentType;
	}
	
	/**
	 * @return Returns the fileSuffix.
	 */
	public String getFileSuffix() {
		return fileSuffix;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "文件类型 : " + getFileContentType();
	}

	/**
	 * @return Returns the fileChineseTypeName.
	 */
	public String getFileChineseTypeName() {
		return fileChineseTypeName;
	}
	
	
	
}
