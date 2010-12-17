/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.common.report.jasper;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 实现来自于 com.opensymphony.webwork.views.jasperreports.JasperReportsResult
 * @since 2006-9-27
 * @author java2enterprise
 * @version $Id: ServletJasperReportUtils.java,v 1.1 2010/12/10 10:54:17 silencily Exp $
 */
public abstract class ServletJasperReportUtils {
	
	private static transient Log logger = LogFactory.getLog(ServletJasperReportUtils.class);
	
	public static String CONTENT_DISPOSITION_INLINE = "inline";	
	public static String CONTENT_DISPOSITION_ATTACHMENT = "attachment";
	
	public static void exportReport(
		JasperReportProvider reportProvider, 
		HttpServletRequest request, 
		HttpServletResponse response) 
		throws ServletException {
	
		exportReport(
			reportProvider, 
			request, 
			response, 
			request.getParameter("jasper.contentDisposition"), 
			request.getParameter("jasper.format"), 
			null
		);
	}
	
	public static void exportReport(
		JasperReportProvider reportProvider, 
		HttpServletRequest request, 
		HttpServletResponse response,
		String fileName) 
		throws ServletException {
		
		exportReport(
			reportProvider, 
			request, 
			response, 
			request.getParameter("jasper.contentDisposition"), 
			request.getParameter("jasper.format"), 
			fileName
		);
	}
	
	
	public static void exportReport(
		JasperReportProvider reportProvider, 
		HttpServletRequest request, 
		HttpServletResponse response,
		String contentDisposition,
		String format,
		String fileName) 
		throws ServletException {

        try {
			if (!"contype".equals(request.getHeader("User-Agent"))) {
				String format2use = format == null ? JasperReportConstants.FORMAT_PDF : format;
				StringBuffer header = new StringBuffer();
                header.append((contentDisposition == null) ? CONTENT_DISPOSITION_ATTACHMENT : contentDisposition);
            	header.append("; filename=\"");
            	header.append(URLEncoder.encode(getFileName(fileName), "UTF8"));
            	header.append(".");
            	header.append(format2use.toLowerCase());
            	header.append("\"");
            	response.setHeader("Content-disposition", header.toString());
                
            	byte[] report = new byte[0];			
				if (JasperReportConstants.FORMAT_PDF.equals(format2use)) {
					response.setContentType("application/pdf");
					report = JasperReportUtils.exportReportToPdfBytes(reportProvider);
				} else if (JasperReportConstants.FORMAT_XLS.equals(format2use)) {
					response.setContentType("application/vnd.ms-excel");
					report = JasperReportUtils.exportReportToXlsBytes(reportProvider);
				} else {
					throw new ServletException("Unknown report format: " + format2use);
				}
				
	            response.setContentLength(report.length);
            	ServletOutputStream ouputStream = response.getOutputStream();
                ouputStream.write(report);
                ouputStream.flush();
                ouputStream.close();
			} else {
	            // Code to handle "contype" request from IE	
                response.setContentType("application/pdf");
                response.setContentLength(0);
                ServletOutputStream out = response.getOutputStream();
                out.close(); 
			}
		} catch (IOException e) {
        	logger.error("Error writing report output", e);
            throw new ServletException(e.getMessage(), e);
        }
	}

	private static String getFileName(String fileName) {
		if (fileName == null) {
			return "导出文件" + new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss").format(net.silencily.sailing.utils.DBTimeUtil.getDBTime());
		}
		return fileName;
	}
	
}
