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
	 * ��������������ͨ�����ݿ��ȡ����
	 * @param filename
	 * @param parameter
	 * @param filetype
	 * @return
	 * 2007-8-27����09:13:03
	 */
	public ByteArrayOutputStream getOutput(String filename,Map parameter,String filetype);
    /**
     * 
     * ��������������ͨ�����ݿ��ȡ����(������֧�ֶ��������Ϊһ���ĵ����)
     * @param filenames �ļ���list
     * @param parameter ����list
     * @param filetype
     * @return
     * 2007-8-27����09:13:03
     */
    public ByteArrayOutputStream getOutput(List filenames,List parameter,String filetype);
	/**
	 * 
	 * ��������������ͨ�����ݿ⵫ͨ��List(JavaBean����)��ȡ����
	 * @param filename
	 * @param parameter
	 * @param filetype
	 * @return
	 * 2007-8-27����09:13:03
	 */
	public ByteArrayOutputStream getOutput(String filename,Map parameter,String filetype,JRDataSource jcds);
    /**
     * 
     * ��������������ͨ�����ݿ⵫ͨ��List(JavaBean����)��ȡ����(������֧�ֶ��������Ϊһ���ĵ����)
     * @param filenames �ļ���list
     * @param parameter ����list
     * @param filetype
     * @return
     * 2007-8-27����09:13:03
     */
    public ByteArrayOutputStream getOutput(List filenames,List parameter,String filetype,JRDataSource jcds);
	 
	/**
	 * 
	 * ����������������Ҫͨ�����ݿ��ȡ����
	 * @param filename
	 * @param parameter
	 * @param filetype
	 * @return
	 * 2007-10-18����3:43:20
	 */
	public ByteArrayOutputStream getOutput(String filename,Map parameter,String filetype,Connection con);

    /**
     * 
     * ����������������Ҫͨ�����ݿ��ȡ����(������֧�ֶ��������Ϊһ���ĵ����)
     * @param filenames �ļ���list
     * @param parameter
     * @param filetype
     * @return
     * 2007-10-18����3:43:20
     */
    public ByteArrayOutputStream getOutput(List filenames,List parameter,String filetype,Connection con);
	
	public void setPath(String servletPath);
}
