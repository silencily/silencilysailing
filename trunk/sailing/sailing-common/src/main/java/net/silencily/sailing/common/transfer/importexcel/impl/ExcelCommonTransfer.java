package net.silencily.sailing.common.transfer.importexcel.impl;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import jxl.Cell;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;

import net.silencily.sailing.common.transfer.importexcel.CommonTransfer;
import net.silencily.sailing.common.transfer.importexcel.CommonTransferCallback;

import org.apache.commons.lang.StringUtils;

/**
 * ���¹���������ϵͳ<code>excel</code>���ݵ�ʵ����, Ҫ��<code>excel</code>�ļ��б���
 * ���ݵĸ�ʽ<ul>
 * <li>�ڵ�һ��<code>sheet</code>����Ҫ���������</li>
 * <li>���ݵĵ�һ��������������, ��������������ļ��е�<code>fieldName</code>��Ӧ</li> 
 * </ul>
 * @author Scott Captain
 * @since 2006-8-18
 * @version $Id: ExcelCommonTransfer.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 *
 */
public class ExcelCommonTransfer implements CommonTransfer {
    private String EMPTY_STRING = new String();

    public int transfer(InputStream in, CommonTransferCallback callback) throws Exception {
        Workbook workbook = Workbook.getWorkbook(in);
        int count = 0;
        try {
            if (workbook.getNumberOfSheets() == 0) {
                return 0;
//            throw new IllegalStateException("excel�ļ�û������");
            }
            Sheet sheet = workbook.getSheet(0);
            int countOfColumns = sheet.getColumns();
            int countOfRows = sheet.getRows();
            Map fields = new LinkedHashMap(countOfColumns);
            for (int i = 0; i < countOfRows; i++) {
                /* ��һ�������������� */
                if (i > 0) {
                    Map data = makeData(fields, sheet.getRow(i));
                    if (data != Collections.EMPTY_MAP) {
                        /* ���д�����ݽ�� + 1 */
                        count += (callback.executePerRow(data) ? 1 : 0);
                    }
                } else {
                    makeFieldNames(fields, sheet.getRow(i));
                }
            }
        } finally {
            workbook.close();
        }

        return count;
    }
    
    /**
     * �޼���Ԫ���ֵ, �Ѿ��пհ��ַ���յ����ڸ�ʽ(��excel����"   .  ."����ʽ)����Ϊ�մ�
     * @param value <code>excel</code>�ļ��б�����ֵ
     * @return
     */
    private String prune(String value) {
        if (value != null) {
            value = value.replaceAll("^\\s$", EMPTY_STRING).replaceAll("\\s+\\.\\s*\\.", EMPTY_STRING);
        }
        return value != null ? value : EMPTY_STRING;
    }
    
    /** ���<code>excel</code>�����Ƿ��ǿ���, ����ǿ��з���<code>true</code> */
    private boolean isEmptyRow(Cell[] cells) {
        boolean empty = true;
        for (int i = 0; empty && i < cells.length; i++) empty &= StringUtils.isBlank(prune(cells[i].getContents()));
        return empty;
    }
    
    /** ��װ������, ��������������������ƶ�Ӧ */
    private void makeFieldNames(Map map, Cell[] cells) {
        for (int i = 0; i < cells.length; i++) {
            map.put(prune(cells[i].getContents()), null);
        }
    }
    
    /**
     * ��<code>excel</code>һ������һ��<code>Map</code>����, <code>key</code>�Ǵ�<code>excel</code>
     * �ļ��ĵ�һ�н�������������Ϣ, <code>value</code>���������ϢΪ��׼������������д,
     * ����кϲ���Ԫ����������Ĳ��־���<code>null</code>
     * 
     * @param map
     * @param cells
     * @return
     */
    private Map makeData(Map map, Cell[] cells) {
        if (isEmptyRow(cells)) {
            return Collections.EMPTY_MAP;
        }

        Map data = new LinkedHashMap(map.size());
        data.putAll(map);
        int i = 0;
        for (Iterator it = data.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            if (i < cells.length) {
                entry.setValue(toString(cells[i++]));
            }
        }

        return data;
    }
    
    protected SimpleDateFormat to = new SimpleDateFormat("yyyy-MM-dd");
    protected String toString(Cell cell) {
        if (cell instanceof DateCell) {
            DateCell dc = (DateCell) cell;
            return to.format(dc.getDate());
        }

        return prune(cell.getContents());
    }
}
