/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.web.webwork.dispatcher;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.framework.transfer.exceptions.TransferException;
import net.silencily.sailing.framework.transfer.strategy.ProcessTransferRowContext;
import net.silencily.sailing.framework.transfer.strategy.ProcessTransferRowStrategy;
import net.silencily.sailing.framework.transfer.web.webwork.TransferExportable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.webwork.dispatcher.WebWorkResultSupport;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ActionInvocation;

/**
 * <class>TransferExportResult</class> �����ṩ�����ļ������, ֻ��ʵ���� {@link com.coheg.framework.transfer.web.webwork.TransferExportable} �ӿڵ�
 * Action ����ʹ�ô� Result, ʹ��ʱ����Ҫ�κβ���, �򵥵����������ü��� : <p>
 * 
 * result-type ����: <br>
 * 
 * &lt;result-types&gt;<br>
 * &nbsp;&nbsp;&lt;result-type name="transfer" class="com.coheg.framework.web.webwork.dispatcher.TransferExportResult" /&gt;<br>
 * &lt;/result-types&gt;<br>
 * 
 * <br>
 * Action ���� : <br>
 * 
 * &lt;result name="success" type="transfer" /&gt;
 * <p>
 * 
 * @see com.coheg.framework.transfer.web.webwork.TransferExportable
 * @since 2005-9-29
 * @author ����
 * @version $Id: TransferExportResult.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 */
public class TransferExportResult extends WebWorkResultSupport {

	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = 5170563589835611114L;

	private static Log logger = LogFactory.getLog(TransferExportResult.class);
	
	private String documentName;
	
	private String contentDisposition;
	
	/**
	 * 
	 * @see com.opensymphony.webwork.dispatcher.WebWorkResultSupport#doExecute(java.lang.String, com.opensymphony.xwork.ActionInvocation)
	 */
	protected void doExecute(String finalLocation, ActionInvocation invocation) throws Exception {
		Action action = (Action) invocation.getAction();
		
		if (! TransferExportable.class.isInstance(action)) {
			throw new RuntimeException(" To use  " + TransferExportResult.class + " , your action must implements " + TransferExportable.class);
		}
		
		TransferExportable exportable = (TransferExportable) action;
		Assert.notNull(exportable.getFileType(), " fileType is requried. ");
		Assert.notNull(exportable.getTransferMetaData(), " transferMetaData is required. ");
		Assert.notNull(exportable.getTransferMetaData().getDateFormat(), " transferMetaData#dateFormat is required. ");
		Assert.notNull(exportable.getTransferMetaData().getTxtSeparator(), " transferMetaData#txtSeparator is required. ");
		
		// �Ƿ���������Ҫ����
		boolean transferRowsExist = exportable.getTransferExportRows() != null && ! exportable.getTransferExportRows().isEmpty();
			
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        
        if (StringUtils.isBlank(documentName)) {
        	documentName = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(net.silencily.sailing.utils.DBTimeUtil.getDBTime()) + exportable.getFileType().getFileSuffix();
        } else {
        	documentName = conditionalParse(documentName, invocation) + exportable.getFileType().getFileSuffix();
        }
        
        if (StringUtils.isBlank(contentDisposition)) {
        	contentDisposition = "attachment";
        } else {
        	contentDisposition = conditionalParse(contentDisposition, invocation);
        }
        
        //Ϊ�ͻ�������ָ��Ĭ�ϵ������ļ�����
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", contentDisposition + ";filename=\"" + documentName + "\"");       
        
		OutputStream out = response.getOutputStream();
		
		if (! "contype".equals(request.getHeader("User-Agent")) && transferRowsExist) {						
			ProcessTransferRowStrategy strategy = ProcessTransferRowContext.getStrategy(exportable.getFileType());
			try {
				strategy.writeTransferRows2OutputStream(exportable.getTransferExportRows(), exportable.getTransferMetaData(), out);
			} catch (TransferException e) {
            	logger.error(" Error writing transfer export output ", e);
                throw new ServletException(e.getMessage(), e);
            }
		} else {
            // Code to handle "contype" request from IE
            try {               
                response.setContentLength(0);
                out.close();
            } catch (IOException e) {
            	logger.error(" Error writing transfer export output ", e);
                throw new ServletException(e.getMessage(), e);
            }
		}
	}


	/**
	 * @return Returns the contentDisposition.
	 */
	public String getContentDisposition() {
		return contentDisposition;
	}


	/**
	 * @param contentDisposition The contentDisposition to set.
	 */
	public void setContentDisposition(String contentDisposition) {
		this.contentDisposition = contentDisposition;
	}


	/**
	 * @return Returns the documentName.
	 */
	public String getDocumentName() {
		return documentName;
	}


	/**
	 * @param documentName The documentName to set.
	 */
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}


}
