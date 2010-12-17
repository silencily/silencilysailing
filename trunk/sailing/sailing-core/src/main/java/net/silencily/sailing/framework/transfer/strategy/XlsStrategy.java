/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.transfer.strategy;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jxl.BooleanCell;
import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.LabelCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.CellFormat;
import jxl.format.Colour;
import jxl.read.biff.BiffException;
import jxl.write.Blank;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import net.silencily.sailing.framework.transfer.DefaultTransferImportRow;
import net.silencily.sailing.framework.transfer.TransferExportRow;
import net.silencily.sailing.framework.transfer.exceptions.ErrorWhenReadInputStreamException;
import net.silencily.sailing.framework.transfer.exceptions.ErrorWhenWriteOutputStreamException;
import net.silencily.sailing.framework.transfer.exceptions.TransferException;
import net.silencily.sailing.framework.transfer.meta.ImportMetaData;
import net.silencily.sailing.framework.transfer.meta.TransferMetaData;

import org.springframework.util.Assert;

/**
 * @since 2005-9-27
 * @author 王政
 * @version $Id: XlsStrategy.java,v 1.1 2010/12/10 10:54:17 silencily Exp $
 */
public class XlsStrategy extends AbstractProcessTransferRowStrategy implements ProcessTransferRowStrategy {

	/**
	 * 
	 * @see com.coheg.framework.transfer.strategy.ProcessTransferRowStrategy#populateTransferRows(java.io.InputStream, com.coheg.framework.transfer.meta.ImportMetaData)
	 */
	public List populateTransferRows(InputStream in, ImportMetaData importMetaData) throws TransferException {
		prepareImport(in);		
		Workbook workbook = null;
		try {
			workbook = Workbook.getWorkbook(in);
		} catch (BiffException e) {
			throw new ErrorWhenReadInputStreamException("can't read xls", e);
		} catch (IOException e) {
			throw new ErrorWhenReadInputStreamException("can't read xls", e);
		}
		
		Sheet sheet = workbook.getSheet(0);
		int rowCount = sheet.getRows();
		int columnCount = sheet.getColumns();
				
		List importRows = new LinkedList();
		
		for (int i = 0; i < rowCount; i++) {
			DefaultTransferImportRow importRow = new DefaultTransferImportRow();
			
			if (importMetaData.getDateFormat() != null) {
				importRow.setDateFormat(importMetaData.getDateFormat());
			}
			
			importRow.setRowNumber(i);
			importRow.setColumnCounts(columnCount);
			
			if (i == rowCount - 1) {
				importRow.setLast(true);
			}
			
			Cell[] cells = sheet.getRow(i);
			List columnContents = new LinkedList();
			
			for (int j = 0; j < cells.length; j++) {
				CellType cellType = cells[j].getType();		
				columnContents.add(getCustomCellValueByCellType(cellType, cells[j]));
			}
				
			importRow.setImportColumnContents(columnContents);
			
			importRows.add(importRow);
		}
		
		return importRows;
	}
	
	/**
	 * 
	 * @throws WriteException 
	 * @throws RowsExceededException 
	 * @see com.coheg.framework.transfer.strategy.ProcessTransferRowStrategy#writeTransferRows2OutputStream(com.coheg.framework.transfer.TransferExportRow[], com.coheg.framework.transfer.meta.TransferMetaData, java.io.OutputStream)
	 */
	public void writeTransferRows2OutputStream(List exportRows, TransferMetaData transferMetaData, OutputStream out) throws TransferException {
		Assert.notNull(exportRows, " exportRows must be specified ");
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(out);
		
		try {
			WritableWorkbook workbook = Workbook.createWorkbook(bufferedOutputStream);
			WritableSheet sheet = workbook.createSheet("导出数据", 0);
			
			int rowIndex = 0;
			for (Iterator iter = exportRows.iterator(); iter.hasNext(); ) {
				TransferExportRow exportRow = (TransferExportRow) iter.next();
				Assert.notNull(exportRow, " exportRow is required. ");
				int columnCounts = exportRow.getColumnCounts();
				
				for (int j = 1; j <= columnCounts; j++) {
					WritableCell cell = createCellByType(j - 1, rowIndex, exportRow.get(j), exportRow.getDateFormat());
					sheet.addCell(cell);
				}
				
				rowIndex ++;
			}
			workbook.write();
			workbook.close();
		} catch (RowsExceededException e) {
			throw new ErrorWhenWriteOutputStreamException(" 不能写 excel ", e);
		} catch (WriteException e) {
			throw new ErrorWhenWriteOutputStreamException(" 不能写 excel ", e);
		} catch (IOException e) {
			throw new ErrorWhenWriteOutputStreamException(" 不能写 excel ", e);
		}
	}
	
	private WritableCell createCellByType(int col, int row, Object o, DateFormat dateFormat) throws WriteException {
		WritableCell cell = null;
		if (o == null) {
			cell = new Blank(col, row);
		} else if (java.lang.Number.class.isInstance(o)) {
			cell = new Number(col, row, ((java.lang.Number) o).doubleValue());
		} else if (Date.class.isInstance(o)) {
			cell = new Label(col, row, dateFormat.format((Date) o));
		} else {
			cell = new Label(col, row, String.valueOf(o));
		}
		
		
		cell.setCellFormat(getCellFormat(cell, row));
		return cell;
	}
	
	public static CellFormat getCellFormat(WritableCell cell, int row) throws WriteException {
		WritableCellFormat cellFormat = new WritableCellFormat();
		cellFormat.setWrap(false);
		cellFormat.setLocked(true);
		cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.LIGHT_BLUE);
		
		if (Number.class.isInstance(cell)) {
			cellFormat.setAlignment(Alignment.RIGHT);
		} else {
			cellFormat.setAlignment(Alignment.LEFT);
		}
		
		// 为不同行设置不同的背景色
		if (row == 0) {
			cellFormat.setBackground(Colour.AQUA);
		} else if (row % 2 == 0) {
			cellFormat.setBackground(Colour.LIGHT_TURQUOISE2);
		} else {
			cellFormat.setBackground(Colour.WHITE);
		}
		
		return cellFormat;
	}
	
	public static Object getCustomCellValueByCellType(CellType cellType, Cell cell) {
		Assert.notNull(cellType, " cellType is required. ");
		if (cellType == CellType.BOOLEAN || cellType == CellType.BOOLEAN_FORMULA) {
			return Boolean.valueOf(((BooleanCell) cell).getValue());
		} else if (cellType == CellType.DATE || cellType == CellType.DATE_FORMULA) {
			return ((DateCell) cell).getDate();
		}  else if (cellType == CellType.LABEL || cellType == CellType.STRING_FORMULA) {
			return ((LabelCell) cell).getString();
		} else if (cellType == CellType.NUMBER || cellType == CellType.NUMBER_FORMULA) {
			return new Double(((NumberCell) cell).getValue());
		} else if (cellType == CellType.EMPTY || cellType == CellType.ERROR) {
			return null;
		}
		
		throw new IllegalArgumentException("不识别的单元格格式 : " + cellType);
	}
	

}
