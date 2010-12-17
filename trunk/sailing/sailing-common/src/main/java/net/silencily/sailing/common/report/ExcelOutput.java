package net.silencily.sailing.common.report;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class ExcelOutput {
	
	/**
	 * �½�excel����
	 * 
	 * @param map ��ά���������Ϣ
	 * @param str ��ά����Ϣ
	 * @throws IOException
	 */
	public void createExcel(HashMap map,String[][] str) throws IOException{

	    HSSFWorkbook wb = new HSSFWorkbook();
	    HSSFSheet sheet = wb.createSheet("new sheet");   
	    sheet.setDisplayFormulas(true);
	    sheet.setDisplayGridlines(false);
	    sheet.setPrintGridlines(true);
	    sheet.setMargin((short)1, (short)1);
	    
	    //����
	    short colNum = (short)(str[0].length-1);
	    String titleName = map.get("title").toString();
	    HSSFCell rtnCell = this.setTitle(sheet, colNum, titleName,0);
	    this.style_16(wb, rtnCell, "");
	    
	    //header�趨 
	    String headName1 = map.get("header1").toString();
	    HSSFCell headerCell01 = this.setHeader(sheet, colNum, headName1, 1);
	    this.style_10(wb, headerCell01, "left");
	    
	    String headName2 = map.get("header2").toString();
	    HSSFCell headerCell02 = this.setHeader(sheet, colNum, headName2, 2);
	    this.style_10(wb, headerCell02, "");
	    
	    for(short i= 0;i<str.length;i++){
	    	HSSFRow row = sheet.createRow(i+3); 

	    	//row.setHeightInPoints((float)20.6);
	    	for (short j =0 ;j <str[0].length;j++){
	    		HSSFCell cell = row.createCell(j);
	    		//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
	    		String tempValue = str[i][j];
	    		//�Ƿ�ϲ���Ԫ��
	    		String strValue = this.combine(sheet, tempValue);
	    		cell.setCellValue(strValue);
	    		
	    		//����styleȡ�á�
	    		int location = tempValue.indexOf("style");
	    		if(location != -1){
		    		String tmpVal = tempValue.substring(location);
		    		int styleVal = tmpVal.lastIndexOf("=");
	    			String styleId = tmpVal.substring(styleVal+1);
	    			this.willUseStyle(wb, cell, "", styleId);
	    		}else{
	    			this.willUseStyle(wb, cell, "", "999");
	    		}
	    	}
	    }
	    FileOutputStream fileOut = new FileOutputStream(map.get("fileName").toString().concat(".xls"));
	    wb.write(fileOut);
	    fileOut.close();

	}
	
	/**
	 * �޸�excel����
	 * 
	 * @param map ��ά���������Ϣ
	 * @param str ��ά����Ϣ
	 * @throws IOException
	 */
	public void updateExcel(List lst,FileInputStream fInput,String outputFileName) throws IOException{
		//��ȡ�ļ���
		POIFSFileSystem fs  =  new POIFSFileSystem(fInput);
		//������������
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		
		//����װ��
		for (Iterator iter = lst.iterator(); iter.hasNext();) {
			UpdatePrintBean bean = (UpdatePrintBean)iter.next();
			HSSFSheet sheet = wb.getSheetAt(0);

			HSSFRow row = sheet.getRow(bean.getYLocation());
			HSSFCell cell = row.getCell((short)bean.getXLocation());
			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			
			//�趨�����ʽ
			this.willUseStyle(wb, cell, bean.getAlign(), 
					bean.getStyle()==null ? "999" : bean.getStyle());
			//�趨������ݡ�
			cell.setCellValue(bean.getValue());
			
			
		}
		
		//�����ļ�
	    FileOutputStream fileOut = new FileOutputStream(outputFileName.concat(".xls"));
	    wb.write(fileOut);
	    fileOut.close();
	}
	
	
	/**
	 * �ϲ��б��
	 * @param sheet
	 * @param value
	 * @return
	 */
	private String combine(HSSFSheet sheet,String value){
		String[] arr = value.split(";");
		if (value.indexOf("curPos")!= -1){
			Region reggion = new Region();
			int x = 0;
			int y = 0;
			if (arr.length>2){
				for (int i = 0;i < arr.length; i++){
					String str = arr[i].substring(arr[i].lastIndexOf("=")+1);
					String numArray[] = str.split(":");
					if(numArray.length > 1){
						x = Integer.parseInt(numArray[0]);
						y = Integer.parseInt(numArray[1]);
						reggion.setColumnFrom((short)y);
						reggion.setColumnTo((short)(y));
						reggion.setRowFrom(x);
						reggion.setRowTo(x);						
						continue;
					}else{
						if (i==0){
							continue;
						}
						int num = Integer.parseInt(str)-1;
						//�кϲ��趨��
						if (arr[i].indexOf("col")!=-1){
							reggion.setColumnFrom((short)y);
							reggion.setColumnTo((short)(y+num));
						}
						
						//�кϲ��趨
						if (arr[i].indexOf("row")!=-1){
							reggion.setRowFrom(x);
							reggion.setRowTo(x+num);					
						}
					}
				}
			}
			sheet.addMergedRegion(reggion);
		}
		
		return arr[0];
	}
	
	/**
	 * TITLE�����趨��
	 * @param sheet
	 * @param colNum
	 * @param titleName
	 */
	private HSSFCell setTitle(HSSFSheet sheet, short colNum, String titleName, int i){
		
	    //titleλ�á�
	    HSSFRow rowTitle = sheet.createRow(0);
	    rowTitle.setHeightInPoints((float)64.6);

	    //���кϲ�
	    this.allCombine(sheet, colNum, i);

	    HSSFCell cellTitle = rowTitle.createCell((short)0);
	    //cellTitle.setEncoding(HSSFCell.ENCODING_UTF_16);
	    String titleValue = this.combine(sheet, titleName);
	    cellTitle.setCellValue(titleValue);
	    
	    return cellTitle;
	    
	}
	
	/**
	 * ��ͷ�趨��
	 * @param sheet
	 * @param colNum
	 * @param titleName
	 * @return
	 */
	private HSSFCell setHeader(HSSFSheet sheet, short colNum, String headerName,int i){
	    //titleλ�á�
	    HSSFRow rowTitle = sheet.createRow(i);
	    //�и��趨��
	    rowTitle.setHeightInPoints((float)30.6);
	    //���кϲ�
	    this.allCombine(sheet, colNum,i);
	    HSSFCell cellTitle = rowTitle.createCell((short)0);
	    //cellTitle.setEncoding(HSSFCell.ENCODING_UTF_16);
	    String titleValue = this.combine(sheet, headerName);
	    //�����趨
	    cellTitle.setCellValue(titleValue);

	    
	    return cellTitle;
	    
	}	
	
	/**
	 * 9������������
	 * ��ά�����ݲ�����ʽ
	 * @param wb
	 * @param cell
	 * @param align
	 */
	private void setCellStyle(HSSFWorkbook wb,HSSFCell cell,String align){
	    HSSFCellStyle style = wb.createCellStyle();
	    HSSFFont font = wb.createFont();
	    font.setFontName("����");
	    font.setFontHeightInPoints((short)9);

	    style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	    style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	    style.setBorderRight(HSSFCellStyle.BORDER_THIN);
	    style.setBorderTop(HSSFCellStyle.BORDER_THIN);
	    
	    if ("left".equals(align)){
	    	style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
	    }else if ("right".equals(align)){
	    	style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
	    }else{
	    	style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	    }
	    //style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);	
	    style.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);	
	    style.setFont(font);
	    
	    //�趨�Զ�����
	    style.setWrapText( true );
	    
	    //���Ӧ����ʽ
	    cell.setCellStyle(style);   

	}
	
	/**
	 * ��ʽ24������������
	 * @param wb
	 * @param cell
	 * @param align
	 */
	private void style_24(HSSFWorkbook wb,HSSFCell cell,String align){
	    HSSFCellStyle style = wb.createCellStyle();
	    HSSFFont font = wb.createFont();
	    font.setFontName("����");
	    font.setFontHeightInPoints((short)24);
	    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	    if ("right".equals(align)){
	    	 style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
	    }else if(("left".equals(align))){
	    	 style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
	    }else{
	    	style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	    }
	    style.setBorderBottom(HSSFCellStyle.BORDER_NONE);
	    style.setBorderTop(HSSFCellStyle.BORDER_NONE);
	    style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	    style.setFont(font);
	    cell.setCellStyle(style);
	}

	/**
	 * ��ʽ20������������
	 * @param wb
	 * @param cell
	 * @param align
	 */
	private void style_20(HSSFWorkbook wb,HSSFCell cell,String align){
	    HSSFCellStyle style = wb.createCellStyle();
	    HSSFFont font = wb.createFont();
	    font.setFontName("����");
	    font.setFontHeightInPoints((short)24);
	    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	    if ("right".equals(align)){
	    	 style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
	    }else if(("left".equals(align))){
	    	 style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
	    }else{
	    	style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	    }
	    style.setBorderBottom(HSSFCellStyle.BORDER_NONE);
	    style.setBorderTop(HSSFCellStyle.BORDER_NONE);
	    style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	    style.setFont(font);
	    cell.setCellStyle(style);
	}
	
	/**
	 * ��ʽ16������������
	 * @param wb
	 * @param cell
	 * @param align
	 */
	private void style_16(HSSFWorkbook wb,HSSFCell cell,String align){
	    HSSFCellStyle style = wb.createCellStyle();
	    HSSFFont font = wb.createFont();
	    font.setFontName("����");
	    font.setFontHeightInPoints((short)16);
	    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	    style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	    style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	    style.setBorderBottom(HSSFCellStyle.NO_FILL);
	    style.setBorderTop(HSSFCellStyle.NO_FILL);
	    style.setFont(font);
	    cell.setCellStyle(style);
	}

	/**
	 * header��ʽ10������������
	 * @param wb
	 * @param cell
	 * @param align
	 */
	private void style_10(HSSFWorkbook wb,HSSFCell cell,String align){
	    HSSFCellStyle style = wb.createCellStyle();
	    HSSFFont font = wb.createFont();
	    font.setFontName("����");
	    font.setFontHeightInPoints((short)10);
	    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	    if ("right".equals(align)){
	    	 style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
	    }else if(("left".equals(align))){
	    	 style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
	    }else{
	    	style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	    }
	    style.setBorderBottom(HSSFCellStyle.BORDER_NONE);
	    style.setBorderTop(HSSFCellStyle.BORDER_NONE);
	    style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	    style.setFont(font);
	    cell.setCellStyle(style);
	}

	
	/**
	 * ���кϲ�
	 * @param sheet
	 * @param colNum
	 */
	private void allCombine(HSSFSheet sheet, short colNum,int i){
	    Region region = new Region(i,(short)0,i, colNum);
	    sheet.addMergedRegion(region);
	}
	
	
	/**
	 * ʹ����ʽ��ѡ�ۡ�
	 * 
	 * @param wb
	 * @param cell
	 * @param align
	 * @param str
	 */
	private void willUseStyle(HSSFWorkbook wb,HSSFCell cell,String align,String str){
		
		int styleId = Integer.parseInt(str);
		
		switch (styleId){
		case 0:
			this.style_10(wb, cell, align);
			break;
		case 1:	
			this.style_16(wb, cell, align);
			break;
		case 2:	
			this.style_20(wb, cell, align);
			break;
		case 3:	
			this.style_24(wb, cell, align);
			break;
		default:
			//Ĭ��9����
			this.setCellStyle(wb, cell, align);
			break;
		}		
	}
}
