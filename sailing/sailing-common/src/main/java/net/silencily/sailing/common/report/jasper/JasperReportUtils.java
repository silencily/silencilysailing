/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.common.report.jasper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.springframework.util.ClassUtils;

/**
 * @since 2006-9-27
 * @author java2enterprise
 * @version $Id: JasperReportUtils.java,v 1.1 2010/12/10 10:54:17 silencily Exp $
 */
public abstract class JasperReportUtils {
	
	public static void exportReportToPdfStream(JasperReportProvider reportProvider, OutputStream out) {
		JasperReport jasperReport = getJasperReport(reportProvider);
		JasperPrint jasperPrint = fillReport(jasperReport, reportProvider);
		try {
			JasperExportManager.exportReportToPdfStream(jasperPrint, out);
		} catch (Exception e) {
			throw new RuntimeException("导出 jasper report 失败", e);
		}
	}
	
	public static void exportReportToPdfFile(JasperReportProvider reportProvider, String pdfFileName) {
		try {
			exportReportToPdfStream(reportProvider, new FileOutputStream(pdfFileName));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("导出 jasper report 失败", e);
		}
	}
	
	public static byte[] exportReportToPdfBytes(JasperReportProvider reportProvider) {
		JasperReport jasperReport = getJasperReport(reportProvider);
		JasperPrint jasperPrint = fillReport(jasperReport, reportProvider);
		try {
			return JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (JRException e) {
			throw new RuntimeException("导出 jasper report 失败", e);
		}
	}
	
	public static byte[] exportReportToXlsBytes(JasperReportProvider reportProvider) {		
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        exportReportToXlsStream(reportProvider, baos);
        return baos.toByteArray();
	} 
	
	public static void exportReportToXlsStream(JasperReportProvider reportProvider, OutputStream out) {
		JasperReport jasperReport = getJasperReport(reportProvider);
		JasperPrint jasperPrint = fillReport(jasperReport, reportProvider);
		JRExporter exporter = new JRXlsExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
		try {
			exporter.exportReport();
		} catch (JRException e) {
			throw new RuntimeException("导出 jasper report 失败", e);
		}
	}
	
	public static void exportReportToXlsFile(JasperReportProvider reportProvider, String xlsFileName) {
		try {
			exportReportToXlsStream(reportProvider, new FileOutputStream(xlsFileName));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("导出 jasper report 失败", e);
		}
	}
	
	private static JasperReport getJasperReport(JasperReportProvider reportProvider) {
		try {
			URL url = ClassUtils.getDefaultClassLoader().getResource(reportProvider.getJasperInClassPath());			
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(url);
			File reportDirectory = new File(url.getPath().substring(url.getPath().indexOf("/") + 1, url.getPath().lastIndexOf("/")));
			reportProvider.getParameters().put("reportDirectory", reportDirectory);
			return jasperReport;
		} catch (Exception e) {
			throw new RuntimeException("从路径 [" + reportProvider.getJasperInClassPath() + "] 读取 jasper 失败", e);
		}
	}
	
	private static JasperPrint fillReport(JasperReport report, JasperReportProvider reportProvider) {
		try {
			return JasperFillManager.fillReport(report, reportProvider.getParameters(), new JRBeanCollectionDataSource(fillCollectionToAdaptDetailBand(reportProvider)));
		} catch (Exception e) {
			throw new RuntimeException("填充 jasper report 失败", e);
		}
	}
	
	private static Collection fillCollectionToAdaptDetailBand(JasperReportProvider reportProvider) {
		int fillCount = reportProvider.getDetailSize();
		if (reportProvider.getBeanCollection().size() != 0) {
			int modResult = reportProvider.getBeanCollection().size() % reportProvider.getDetailSize();
			if (modResult == 0) {
				return reportProvider.getBeanCollection();
			}
			fillCount = reportProvider.getDetailSize() - modResult;
		} 

		List nullElements = new ArrayList(fillCount);
		for (int i = 0; i < fillCount; i++) {
			nullElements.add(null);
		}
		List filledCollection = new ArrayList(reportProvider.getBeanCollection().size() + nullElements.size());
		filledCollection.addAll(reportProvider.getBeanCollection());
		filledCollection.addAll(nullElements);
		return filledCollection;
	}
	
	public static void main(String[] args) {
		URL url = ClassUtils.getDefaultClassLoader().getResource("jdbc.properties");
		System.out.println(url.getPath().substring(url.getPath().indexOf("/") + 1, url.getPath().lastIndexOf("/")));
	}
	
	
}
