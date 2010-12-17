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
 * 人事管理导入其他系统<code>excel</code>数据的实现类, 要求<code>excel</code>文件中保存
 * 数据的格式<ul>
 * <li>在第一个<code>sheet</code>保存要导入的数据</li>
 * <li>数据的第一行是数据域名称, 这个名称与配置文件中的<code>fieldName</code>对应</li> 
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
//            throw new IllegalStateException("excel文件没有数据");
            }
            Sheet sheet = workbook.getSheet(0);
            int countOfColumns = sheet.getColumns();
            int countOfRows = sheet.getRows();
            Map fields = new LinkedHashMap(countOfColumns);
            for (int i = 0; i < countOfRows; i++) {
                /* 这一行是数据域名称 */
                if (i > 0) {
                    Map data = makeData(fields, sheet.getRow(i));
                    if (data != Collections.EMPTY_MAP) {
                        /* 如果写入数据结果 + 1 */
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
     * 修剪单元格的值, 把具有空白字符或空的日期格式(在excel中是"   .  ."的形式)设置为空串
     * @param value <code>excel</code>文件中本来的值
     * @return
     */
    private String prune(String value) {
        if (value != null) {
            value = value.replaceAll("^\\s$", EMPTY_STRING).replaceAll("\\s+\\.\\s*\\.", EMPTY_STRING);
        }
        return value != null ? value : EMPTY_STRING;
    }
    
    /** 检查<code>excel</code>内容是否是空行, 如果是空行返回<code>true</code> */
    private boolean isEmptyRow(Cell[] cells) {
        boolean empty = true;
        for (int i = 0; empty && i < cells.length; i++) empty &= StringUtils.isBlank(prune(cells[i].getContents()));
        return empty;
    }
    
    /** 组装域名称, 这个域名称用于与列名称对应 */
    private void makeFieldNames(Map map, Cell[] cells) {
        for (int i = 0; i < cells.length; i++) {
            map.put(prune(cells[i].getContents()), null);
        }
    }
    
    /**
     * 从<code>excel</code>一行生成一个<code>Map</code>数据, <code>key</code>是从<code>excel</code>
     * 文件的第一行解析出来的列信息, <code>value</code>以这个列信息为标准从左到右依次填写,
     * 如果有合并单元格的情况不足的部分就是<code>null</code>
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
