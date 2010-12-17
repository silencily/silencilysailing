/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.common.transfer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.power.vfs.FileObjectManager;

/**
 * @since 2006-8-14
 * @author java2enterprise
 * @version $Id: VfsHttpClientResultUtils.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 */
public abstract class VfsHttpClientResultUtils {
	
	/** 以附件形式下载 */
	public static String CONTENT_DISPOSITION_ATTACHMENT = "attachment";
	
	/** 在浏览器中打开 */
	public static String CONTENT_DISPOSITION_INLINE = "inline";
	
	/**
	 * 从 vfs 中下载文件到 http 客户端
	 * @param fileObjectManager the fileObjectManager
	 * @param request the request
	 * @param response the response
	 * @param contentDisposition the contentDisposition, valid value is {@value #CONTENT_DISPOSITION_ATTACHMENT} or {@value #CONTENT_DISPOSITION_INLINE}
	 * @param fileName 文件在 vfs 系统中的名称
	 * @throws IOException if any exception happens
	 * @throws ServletException if any exception happens
	 */
	public static void downloadFromVfs(
			FileObjectManager fileObjectManager,
			HttpServletRequest request, 
			HttpServletResponse response, 
			String contentDisposition, 
			String fileName) 
			throws IOException, ServletException {
			
			OutputStream out = response.getOutputStream();
	        
	        //为客户端下载指定默认的下载文件名称
			/**如果文件名长度超过17则需要另外转码，否则报错 */
			String newFileName = URLEncoder.encode(fileName, "UTF-8");
	        if (newFileName.length() > 150) {
	        	/**根据request的locale 得出可能的编码，中文操作系统通常是gb2312*/
	            String guessCharset = "gb2312"; 
	            newFileName = new String(fileName.getBytes(guessCharset), "ISO8859-1"); 
	        }
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", contentDisposition + ";filename=\"" + newFileName + "\"");       
	        
			if (!"contype".equals(request.getHeader("User-Agent"))) {						
				fileObjectManager.read(fileName, out);
			} else {
	            // Code to handle "contype" request from IE
	            try {               
	                response.setContentLength(0);
	                out.close();
	            } catch (IOException e) {
	                throw new ServletException(e.getMessage(), e);
	            }
			}
		}
	
}
