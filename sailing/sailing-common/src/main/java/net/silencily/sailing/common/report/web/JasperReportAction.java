package net.silencily.sailing.common.report.web;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.HashMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.common.report.service.JasperReportService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.framework.web.struts.BaseDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


/**
 * @author liuduoliang,zhaoyifei
 *
 */
public class JasperReportAction extends BaseDispatchAction {

	
	private JasperReportService getService() {
		return (JasperReportService)ServiceProvider.getService(JasperReportService.SERVICE_NAME);
	}
	public ActionForward report(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) 
			throws Exception {
		String libsPath=(String)request.getSession().getServletContext().getAttribute("org.apache.catalina.jsp_classpath");
		System.setProperty("jasper.reports.compile.class.path", libsPath);
		String filetype=request.getParameter("filetype");
		String file=request.getParameter("file");
		try{
			StringBuffer path=new StringBuffer();
			path.append(request.getSession().getServletContext().getRealPath("/"));
			path.append("WEB-INF/classes/");
			FileInputStream in=new FileInputStream(path.toString()+"jrxml.properties");
//			Properties p=new Properties();
//			p.load(in);
//			String filename=path.toString()+p.getProperty("filename");
			HashMap parameter=new HashMap();
			parameter.put("hp", new Integer(5));
			//获取文件流
			JasperReportService js=getService();
			
			ByteArrayOutputStream os=js.getOutput(file,parameter,filetype);
			if(filetype.equalsIgnoreCase("xls")){
				response.setContentType("application/vnd.ms-excel");
			}else if(filetype.equalsIgnoreCase("pdf")){
				response.setContentType("application/pdf");
			}
			response.setContentLength(os.size()); 
			ServletOutputStream out = response.getOutputStream();
			os.writeTo(out); 
			out.flush();
			out.close();
		}catch(Exception e){
			
		}
			return mapping.findForward("report");
		}
}
