package net.silencily.sailing.common.report.service.excelimpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import net.silencily.sailing.common.report.config.ReportConfig;
import net.silencily.sailing.common.report.config.ReportConfigImpl;
import net.silencily.sailing.common.report.service.ReportConfigDao;
import net.silencily.sailing.common.report.service.ReportManagementService;
import net.silencily.sailing.common.report.service.ReportSource;
import net.silencily.sailing.exception.UnexpectedException;

import org.apache.commons.collections.Buffer;
import org.apache.commons.collections.buffer.UnboundedFifoBuffer;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.power.vfs.FileObject;
import com.power.vfs.FileObjectManager;

import bsh.EvalError;
import bsh.Interpreter;


/**
 * <p><code>excel</code>����ʵ��, ÿ�������Ӧһ��<code>excel</code>ģ���ļ�, ��������д
 * ռλ��, ��ʵ�ʵ����������ռλ����λ��, ģ�屣����<code>vfs</code>�в���ͨ��{@link VfsUploadFiles}
 * ��ʵ�ʵĶ�д����</p>
 * ʵ��֧�����ָ�ʽ�ı���, һ��<code>free</code>��ʽ��, ������ʽ�ǵ�������, ����糧ĳ��
 * �������ձ�, �����б���ʽ��, ��糧һ��Ľ�ų́��, ���ָ�ʽ���Ի��ʹ��, ��Ҫչ�ֵ����ݷ�
 * ��<code>Map</code>��, ������Ա����: <code>Map's key</code>��<code>user</code>, ֵ
 * ����<code>User</code>����, ģ���������������������ڲ���<pre>
 * +-----------------------------+
 * |����  |${user.name}           |
 * +-----------------------------+
 * |����  |${user.department.name}|
 * +-----------------------------+
 * </pre>ʵ�ʵ�ֵ���滻<code>${xxx}</code>��ʽ��ռλ����ֵ, ѭ���ĸ�ʽ��<code>{item : items} {</code>
 * , ����<code>item</code>Ϊÿ��ѭ����ֵ, <code>items</code>������򼯺�,����
 * ��һ������ѭ��������, ע��ѭ��ռλ�������в��������Ŀ��<code>excel</code>��, ����ζ������
 * �����������������,����ռλ��Ҳ������ͺ����<pre>
 * |   ϵͳ�˺�      |   ����      |   ְ��      |
 * |<b>${user : department.users} {</b>               |
 * |${user.username}|${user.name} |${user.job} |   
 * |                |             |            |
 * |                |             |            |
 * |                |             |            |
 * |                |             |            |
 * |                |             |            |
 * |<b>}</b>               |             |            |
 * </pre>, ���ѭ��ÿ��<code>excel</code>���<b>7</b>��, ע��ѭ��������־Ҫ��д�ڵ�һ��
 * <code>cell</code>��,ʵ�ֶ����˼���ȱʡֵ, �������ڱ���������ݵ�<code>Map</code>����
 * ��ͬ��<code>key</code>����, ���ǿ�����ģ����ʹ��<ul>
 * <li>${date}:��ǰ����,�Ѿ�ת��"yyyy-MM-dd"��<code>string</code></li>
 * <li>${datetime}:��ǰʱ��,�Ѿ�ת��"yyyy-MM-dd HH:mm"��<code>string</code></li>
 * <li>${user}, ��ǰ��¼�û�,������{@link User �û�ʵ��}</li>
 * </ul>���ڲ��ܽ��͵ı���, �Ͱ���ģ���б����Ķ������, ���׳��쳣, Ҫ��ϸ�µؼ��ִ�����
 * ����<code>log4j.logger.common.report.excel=DEBUG</code>
 * @author zhangli
 * @version $Id: ExcelReportService.java,v 1.1 2010/12/10 10:54:15 silencily Exp $
 * @since 2007-5-6
 */
public class ExcelReportService implements ReportManagementService {
    
    /** <code>excel</code>����������� */
    public static final int MAX_ROW_COUNT = 500;
    
    public static final String DATE_KEY = "date";
    
    public static final String DATETIME_KEY = "datetime";

    public static final String VFS_TMP_DIR = "tmp/report";
    
    public static final Pattern PLACE_HOLDER_PATTERN = Pattern.compile("\\$\\{\\s*([a-zA-Z][^\\$\\{\\}\\s]*)\\}");

    /** ���ռλ���������ģʽ����ѭ��������ݵĿ�ʼλ��, �����￪ʼѭ��ֱ��ѭ������ */
    public static final Pattern LOOP_PATTERN = Pattern.compile("\\$\\{\\s*([a-zA-Z][a-zA-Z0-9]*)\\s*:\\s*([a-zA-Z][a-zA-Z0-9\\.]*)\\s*}\\s*\\{\\s*");
    
    public static final String LOOP_END = "}";
    
    private SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    private SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private FileObjectManager fileObjectManager;
    
    private ReportConfigDao reportConfigDao;
    
    protected Log logger = LogFactory.getLog("common.report.excel");
    
    public void setFileObjectManager(FileObjectManager fileObjectManager) {
        this.fileObjectManager = fileObjectManager;
    }
    
    public void setReportConfigDao(ReportConfigDao reportConfigDao) {
        this.reportConfigDao = reportConfigDao;
    }

    public void deleteReportConfig(ReportConfig reportConfig) {
        reportConfigDao.deleteEntity(reportConfig);
    }

    public List listReportConfigs(String code) {
        return reportConfigDao.listEntities(code);
    }

    public ReportConfig loadReportConfig(String code) {
        return reportConfigDao.loadEntityById(code);
    }

    public void saveReportConfig(ReportConfig reportConfig) {
        reportConfigDao.saveEntity(reportConfig);
    }

    public void process(Map data, ReportSource reportSource) {
        if (reportSource.getOutput() == null) {
            throw new IllegalStateException("����ReportSourceû��ָ�������");
        }
        ReportConfig config = loadReportConfig(reportSource.getCode());
        reportSource.setReportConfig(config);
        /* ��������һ��sheet������ */
        try {
            Workbook sourceWorkbook = readTemplate(config);
            Sheet sourceSheet = sourceWorkbook.getSheet(0);
            int rows = sourceSheet.getRows();
            sourceWorkbook.close();
            if (rows == 0) {
                throw new IllegalStateException("����" 
                    + config.getCode() 
                    + "," 
                    + config.getName() 
                    + "��ģ��û������");
            }
            /* �����ʱĿ¼���� */
            String tempDir = tmpDir();
            if (fileObjectManager.exists(tempDir)) {
                fileObjectManager.delete(new FileObject(tempDir), true);
            }
            /* 
             * ����Ƕ�ױ���(�ֶ�����ʾ)��һҳ��ʾ��ָ��, ʵ�������һ�ű���������������ʾ����
             * ÿ��������ÿҳ��ʾ����û������, ��������Ŀ������ǿ�ݴ���
             */
            Buffer fifo = new UnboundedFifoBuffer(10);
            data = preprocess(data);
            /* TODO:����VELOCITYʵ�ֱ��ʽ������ */
            Interpreter interpreter = new Interpreter();
            for (Iterator it = data.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry entry = (Map.Entry) it.next();
                interpreter.set(entry.getKey().toString(), entry.getValue());
            }
            /* �Ƿ��Ƕ�ҳ����ı�־ */
            boolean reports = false;
            int page = 0;
            String fullName = null;
            do {
                ByteArrayOutputStream output = readWrite(config, interpreter, fifo);
                fullName = writeToTmpFile(reportSource, output, ++page);
            } while (reports = (fifo.size() > 0));
            if (reports) {
                zip(reportSource.getOutput());
            } else {
                fileObjectManager.read(fullName, reportSource.getOutput());
            }
        } catch (Exception ex) {
            throw new UnexpectedException("���ܴ�������", ex);
        }
    }
    
    private Workbook readTemplate(ReportConfig config) {
        try {
            ReportConfigImpl impl = (ReportConfigImpl) config;
            return Workbook.getWorkbook(impl.getTemplate(this.fileObjectManager));
        } catch (Exception e) {
            throw new UnexpectedException("������ģ�����," + config.getCode() + "," + config.getName(), e);
        }
    }
    
    private ByteArrayOutputStream readWrite(ReportConfig config, Interpreter interpreter, Buffer fifo) {
        Workbook sourceWorkbook = readTemplate(config);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            Sheet sheet = sourceWorkbook.getSheet(0);
            WritableWorkbook target = createReportWorkbook(output, sourceWorkbook);
            WritableSheet targetSheet = target.getSheet(0);
            /* û��ѭ��, ��ʼѭ��, ����ѭ��, ����ѭ���ֱ��� -1, 1, 2, 3 */
            int flag = -1;
            /* ѭ������е�����һ��, ��ѭ���� */
            int loopStartRow = -1;
            /* ѭ��λ��, ����һҳʱ�����λ�õ����濪ʼ, ������Listĩβʱ������� */
            int loopIndex = -1;
            /* ѭ������ */
            int size = -1;
            
            if (fifo.size() > 0) {
                Integer pointer = (Integer) fifo.remove();
                loopIndex = pointer.intValue();
            }
            /* ѭ��������, ${item : items}, loops[0]���� item, loops[1]���� items */
            String[] loops = new String[2];
            String script = null;
            for (int i = 0; i < sheet.getRows(); i++) {
                flag = rowType(flag, sheet.getRow(i), loops);
                if (flag == -1) {
                    writeToTargetSheet(sheet.getRow(i), i, targetSheet, interpreter);
                } else {
                    if (flag == 1) {
                        script = readScriptAndReplacePlaceholders(loops);
                        loopStartRow = i + 1;
                        flag = 2;
                    }
                    if (flag > 1) {
                        loopIndex++;
                    }
                    interpreter.set("loopIndex", new Integer(loopIndex));
                    size = ((Integer) interpreter.eval(script)).intValue();
                    if (loopIndex >= size) {
                        writeEmptyToTargetSheet(sheet.getRow(loopStartRow), i, targetSheet);
                    } else {
                        writeLoopToTargetSheet(loops, sheet.getRow(loopStartRow), i, targetSheet, interpreter, fifo);
                    }
                }
                if (flag == 3) {
                    flag = -1;
                }
            }
            if (loopIndex < size) {
                fifo.add(new Integer(loopIndex));
            }
            target.write();
            target.close();
        } catch (EvalError e) {
            clean(output);
            throw new UnexpectedException("excelģ������������", e);
        } catch (RowsExceededException e) {
            clean(output);
            throw new UnexpectedException("excelģ�嶨�����,ѭ��������Ѿ������һ��", e);
        } catch (Exception e) {
            clean(output);
            throw new UnexpectedException("����дexcel���ݵ������", e);
        } finally {
            sourceWorkbook.close();
        }
        return output;
    }
    
    private void clean(OutputStream output) {
        try {
            if (output != null) {
                output.close();
            }
            output = null;
        } catch (IOException ex) {
            /* ignores this exception, throw outer one */
        }
    }
    
    private String writeToTmpFile(ReportSource reportSource, ByteArrayOutputStream output, int serialNo) {
        String fileName = new StringBuffer(reportSource.getReportConfig().getName())
            .append('_').append(serialNo).append(".xls").toString();
        String fullName = new StringBuffer(tmpDir()).append('/').append(fileName).toString();
        FileObject fo = new FileObject(fullName);
        if (fileObjectManager.exists(fullName)) {
            fileObjectManager.delete(new FileObject(fullName));
        }
        fileObjectManager.create(fo, new ByteArrayInputStream(output.toByteArray()));
        if (logger.isDebugEnabled()) {
            logger.debug("������ʱ�ļ�[" + fullName + "]�����Ѵ����ı���");
        }
        return fullName;
    }
    
    private String tmpDir() {
        return new StringBuffer(VFS_TMP_DIR)
            .append('/').append(Thread.currentThread().getName()).toString();
    }

    private WritableWorkbook createReportWorkbook(OutputStream output, Workbook sourceWorkbook) {
        try {
            return Workbook.createWorkbook(output, sourceWorkbook);
        } catch (IOException e) {
            throw new UnexpectedException("���ܴ����������", e);
        }
    }
    
    private void writeEmptyToTargetSheet(Cell[] cells, int row, WritableSheet targetSheet) throws RowsExceededException, WriteException {
        String value = new String();
        for (int i = 0; i < cells.length; i++) {
            targetSheet.addCell(format(row, i, value));
        }
    }
    
    private void writeToTargetSheet(Cell[] cells, int row, WritableSheet targetSheet, Interpreter interpreter) throws Exception {
        for (int i = 0; i < cells.length; i++) {
            String content = cells[i].getContents();
            Object value = content;
            Matcher matcher = PLACE_HOLDER_PATTERN.matcher(content);
            if (matcher.matches()) {
                String key = matcher.group(1);
                try {
                    value = interpreter.eval(key);
                } catch (EvalError e) {
                    value = content;
                    logger.info("���ܽ��ͱ���[" + key + "],ģ���еĸ�ʽ[" + content + "]", e);
                }
            }
            targetSheet.addCell(format(row, i, value));
        }
    }
    
    private void writeLoopToTargetSheet(
        final String[] loops, 
        final Cell[] cells, 
        final int row, 
        final WritableSheet targetSheet, 
        final Interpreter interpreter, 
        Buffer fifo) throws Exception {
        
        for (int i = 0; i < cells.length; i++) {
            String content = cells[i].getContents();
            Object value = content;
            Matcher matcher = PLACE_HOLDER_PATTERN.matcher(content);
            if (matcher.matches()) {
                String key = matcher.group(1);
                value = interpreter.eval(key);
            }
            targetSheet.addCell(format(row, i, value));
        }
    }
    
    private Map preprocess(Map data) {
        Map map = new HashMap(data.size() + 10);
        Date d = net.silencily.sailing.utils.DBTimeUtil.getDBTime();
        map.put(DATE_KEY, DATE_FORMAT.format(d));
        map.put(DATETIME_KEY, DATETIME_FORMAT.format(d));
        map.putAll(data);
        return map;
    }
    
    private WritableCell format(int row, int column, Object value) {
        String label = null;
        if (value == null) {
            label = new String();
        } else {
            label = value.toString();
        }
        WritableCell cell = new Label(column, row, label);
        return cell;
    }
    
    /* �Ƿ�����ѭ��, ֻҪһ���г�����${item : items,30}���Ƶĸ�ʽ, ���е�����cellȫ������ */
    private int rowType(int flag, Cell[] cells, String[] loops) {
        /* �����ѭ����, �����ڴ�ѭ��������־ */
        if (flag == 2) {
            if (cells.length != 0 && cells[0].getContents().trim().equals(LOOP_END)) {
                return 3;
            }
            return flag;
        }
        for (int i = 0; i < cells.length; i++) {
            Matcher matcher = LOOP_PATTERN.matcher(cells[i].getContents());
            if (matcher.matches()) {
                loops[0] = matcher.group(1);
                loops[1] = matcher.group(2);
                return 1;
            }
        }
        return -1;
    }
    
    private String readScriptAndReplacePlaceholders(String[] loops) {
        String fileName = new StringBuffer(ClassUtils.getShortClassName(getClass()))
            .append(".bsh").toString();
        InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(fileName));
        char[] buf = new char[1024];
        int len = 0;
        StringBuffer sbuf = new StringBuffer(500);
        try {
            while ((len = reader.read(buf)) != -1) {
                sbuf.append(buf, 0, len);
            }
        } catch (IOException e) {
            throw new UnexpectedException("���ܶ�ȡbsh script", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    /* ignore */
                }
            }
        }
        return sbuf.toString().replaceAll("\\$\\{item\\}", loops[0]).replaceAll("\\$\\{items\\}", loops[1]);
    }
    
    private void zip(OutputStream output) {
        String tempDir = tmpDir();
        this.fileObjectManager.zip(tempDir, output);
    }

    public ReportConfig createReportConfig(String code, String name) {
        ReportConfigImpl impl = new ReportConfigImpl();
        impl.setCode(code);
        impl.setName(name);
        return impl;
    }
}
