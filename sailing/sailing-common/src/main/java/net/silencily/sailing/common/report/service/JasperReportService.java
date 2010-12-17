package net.silencily.sailing.common.report.service;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.silencily.sailing.framework.core.ServiceBase;



/**
 * @author liuduoliang,zhaoyifei
 *
 */
public interface JasperReportService extends ServiceBase{

	public final static String jasper="jasper";
	public final static String jrxml="jrxml";
	public final static String path="com/qware/common/report/jasper/";
	public final static String xls="xls";
	public final static String pdf="pdf";
	public final static String SERVICE_NAME="common.JasperReportService";
	/**
	 * 
	 * 功能描述：报表不通过数据库获取数据
	 * @param filename
	 * @param parameter
	 * @param filetype
	 * @return
	 * 2007-8-27下午09:13:03
	 */
	public ByteArrayOutputStream getOutput(String filename,Map parameter,String filetype);
    /**
     * 
     * 功能描述：报表不通过数据库获取数据(本方法支持多个报表作为一个文档输出)
     * @param filenames 文件名list
     * @param parameter 参数list
     * @param filetype
     * @return
     * 2007-8-27下午09:13:03
     */
    public ByteArrayOutputStream getOutput(List filenames,List parameter,String filetype);
	/**
	 * 
	 * 功能描述：报表不通过数据库但通过List(JavaBean集合)获取数据
	 * @param filename
	 * @param parameter
	 * @param filetype
	 * @return
	 * 2007-8-27下午09:13:03
	 */
	public ByteArrayOutputStream getOutput(String filename,Map parameter,String filetype,JRDataSource jcds);
    /**
     * 
     * 功能描述：报表不通过数据库但通过List(JavaBean集合)获取数据(本方法支持多个报表作为一个文档输出)
     * @param filenames 文件名list
     * @param parameter 参数list
     * @param filetype
     * @return
     * 2007-8-27下午09:13:03
     */
    public ByteArrayOutputStream getOutput(List filenames,List parameter,String filetype,JRDataSource jcds);
	 
	/**
	 * 
	 * 功能描述：报表需要通过数据库获取数据
	 * @param filename
	 * @param parameter
	 * @param filetype
	 * @return
	 * 2007-10-18下午3:43:20
	 */
	public ByteArrayOutputStream getOutput(String filename,Map parameter,String filetype,Connection con);

    /**
     * 
     * 功能描述：报表需要通过数据库获取数据(本方法支持多个报表作为一个文档输出)
     * @param filenames 文件名list
     * @param parameter
     * @param filetype
     * @return
     * 2007-10-18下午3:43:20
     */
    public ByteArrayOutputStream getOutput(List filenames,List parameter,String filetype,Connection con);
	
	public void setPath(String servletPath);
}
