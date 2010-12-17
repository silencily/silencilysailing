package net.silencily.sailing.servlet;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * @author huxf@bis.com.cn
 *
 */
public class MsgServlet extends HttpServlet {
	// property file name
	private static final String MSG_PROPETY_FILE_NAME = "/WEB-INF/classes/message.properties";
	// js file name which saves message as a hashmap
	private static final String OUTPUT_MSG_JS_FILE_NAME = "/js/msgInfo.js";
	
	public void init() throws ServletException {
		super.init();
		FileOutputStream msgJs = null;
		BufferedOutputStream msgJsBuffer = null;
		try {
			// Context path
			String contextPath = super.getServletContext().getRealPath("/");
			// Fullpath of the message property file
			String msgFile = contextPath + MSG_PROPETY_FILE_NAME;
			// Content of script file which define message's array according to msg property file
			StringBuffer msgScriptBuffer = new StringBuffer();
			// Properties which load msg property file
			Properties p = new Properties();
			msgJs = new FileOutputStream(contextPath + OUTPUT_MSG_JS_FILE_NAME, false);
			msgJsBuffer = new BufferedOutputStream(msgJs);
						
			// Read message property file
			p.load(new FileInputStream(msgFile));
			//p.load(this.getClass().getResourceAsStream(MSG_PROPETY_FILE_NAME));

			/*
			 * Define script hash
			 */
			msgScriptBuffer.append("var msgHash = {");
			// Read message from property file
			for (Enumeration msgId = p.propertyNames(); msgId.hasMoreElements(); ) {
				String tmp = (String)msgId.nextElement();
				msgScriptBuffer.append("\"");
				msgScriptBuffer.append(tmp);
				msgScriptBuffer.append("\"");
				msgScriptBuffer.append(":");
				msgScriptBuffer.append("\"");
				msgScriptBuffer.append(p.getProperty(tmp).replaceAll("\"","\\\\\""));
				msgScriptBuffer.append("\",");
				msgScriptBuffer.append("\r\n");
			}
			// delete the last useless ","
			msgScriptBuffer.deleteCharAt(msgScriptBuffer.length() - 3);
			msgScriptBuffer.append("};");
			msgJsBuffer.write(msgScriptBuffer.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// release resources
			try {
				msgJsBuffer.flush();
				msgJsBuffer.close();
				msgJs.flush();
				msgJs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
