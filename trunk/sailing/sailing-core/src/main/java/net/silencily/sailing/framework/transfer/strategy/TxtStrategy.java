/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.transfer.strategy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import net.silencily.sailing.framework.transfer.DefaultTransferImportRow;
import net.silencily.sailing.framework.transfer.TransferExportRow;
import net.silencily.sailing.framework.transfer.exceptions.ErrorWhenReadInputStreamException;
import net.silencily.sailing.framework.transfer.exceptions.ErrorWhenWriteOutputStreamException;
import net.silencily.sailing.framework.transfer.exceptions.TransferException;
import net.silencily.sailing.framework.transfer.meta.ImportMetaData;
import net.silencily.sailing.framework.transfer.meta.TransferMetaData;

import org.springframework.util.Assert;

/**
 * @since 2005-9-28
 * @author 王政
 * @version $Id: TxtStrategy.java,v 1.1 2010/12/10 10:54:17 silencily Exp $
 */
public class TxtStrategy extends AbstractProcessTransferRowStrategy  implements ProcessTransferRowStrategy {
	
	/**
	 * 
	 * @see com.coheg.framework.transfer.strategy.ProcessTransferRowStrategy#populateTransferRows(java.io.InputStream, com.coheg.framework.transfer.meta.ImportMetaData)
	 */
	public List populateTransferRows(InputStream in, ImportMetaData importMetaData) throws TransferException {	
		prepareImport(in);
		
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));	
		String string = null;		
		List importRows = new LinkedList();
		int rowNumber = 0;
		
		try {
			while ((string = bufferedReader.readLine()) != null) {
				DefaultTransferImportRow importRow = new DefaultTransferImportRow();
				
				if (importMetaData.getDateFormat() != null) {
					importRow.setDateFormat(importMetaData.getDateFormat());
				}
				importRow.setRowNumber(rowNumber ++);
				
				int currentRowColumnCount = 0;
				List columnContents = new LinkedList();
				StringTokenizer tokenizer = new StringTokenizer(string, importMetaData.getTxtSeparator());
				
				while (tokenizer.hasMoreElements()) {
					String content = tokenizer.nextToken();
					columnContents.add(content);
					currentRowColumnCount++;
				}
				
				importRow.setImportColumnContents(columnContents);
				importRow.setColumnCounts(currentRowColumnCount);
				
				importRows.add(importRow);
			}
		} catch (IOException e) {
			throw new ErrorWhenReadInputStreamException(" can't read inputstream ", e);
		}
		
		int size = importRows.size();
		if (size > 0) {
			((DefaultTransferImportRow) importRows.get(size - 1)).setLast(true);
		}
		
		return importRows;
	}
	

	public void writeTransferRows2OutputStream(List exportRows, TransferMetaData transferMetaData, OutputStream out) throws TransferException {
		Assert.notNull(exportRows, " exportRows must be specified ");
		
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
		
		try {
			for (Iterator iter = exportRows.iterator(); iter.hasNext(); ) {
				TransferExportRow exportRow = (TransferExportRow) iter.next();
				Assert.notNull(exportRow, " exportRow is required. ");
				int columnCount = exportRow.getColumnCounts();
				for (int j = 0; j < columnCount; j++) {
					StringBuffer sb = new StringBuffer();
					sb.append(exportRow.getFormatedString(j));
					
					if (j < columnCount - 1) {
						sb.append(transferMetaData.getTxtSeparator());
					}
					
					writer.write(sb.toString());
				}
				writer.write("\r\n");
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			throw new ErrorWhenWriteOutputStreamException("写数据发生异常 ", e);
		}
	}
	

	
	
}
