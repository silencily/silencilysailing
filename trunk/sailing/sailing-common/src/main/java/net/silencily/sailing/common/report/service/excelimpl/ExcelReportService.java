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
 * <p><code>excel</code>报表实现, 每个报表对应一个<code>excel</code>模板文件, 在其中填写
 * 占位符, 把实际的数据输出到占位符的位置, 模板保存在<code>vfs</code>中并且通过{@link VfsUploadFiles}
 * 做实际的读写工作</p>
 * 实现支持两种格式的报表, 一个<code>free</code>形式的, 这种形式是单行数据, 例如电厂某天
 * 的生产日报, 二是列表形式的, 像电厂一天的进煤台帐, 两种格式可以混合使用, 把要展现的数据放
 * 到<code>Map</code>中, 比如人员姓名: <code>Map's key</code>是<code>user</code>, 值
 * 就是<code>User</code>对象, 模板中这样定义姓名和所在部门<pre>
 * +-----------------------------+
 * |姓名  |${user.name}           |
 * +-----------------------------+
 * |部门  |${user.department.name}|
 * +-----------------------------+
 * </pre>实际的值将替换<code>${xxx}</code>格式的占位符的值, 循环的格式是<code>{item : items} {</code>
 * , 其中<code>item</code>为每次循环的值, <code>items</code>是数组或集合,下面
 * 是一个定义循环的例子, 注意循环占位符所在行不再输出到目标<code>excel</code>中, 这意味着这行
 * 上如果有其它的文字,变量占位符也不会解释和输出<pre>
 * |   系统账号      |   姓名      |   职务      |
 * |<b>${user : department.users} {</b>               |
 * |${user.username}|${user.name} |${user.job} |   
 * |                |             |            |
 * |                |             |            |
 * |                |             |            |
 * |                |             |            |
 * |                |             |            |
 * |<b>}</b>               |             |            |
 * </pre>, 这个循环每个<code>excel</code>输出<b>7</b>行, 注意循环结束标志要求写在第一个
 * <code>cell</code>中,实现定义了几个缺省值, 除非你在保存输出数据的<code>Map</code>中用
 * 相同的<code>key</code>覆盖, 总是可以在模板中使用<ul>
 * <li>${date}:当前日期,已经转成"yyyy-MM-dd"的<code>string</code></li>
 * <li>${datetime}:当前时间,已经转成"yyyy-MM-dd HH:mm"的<code>string</code></li>
 * <li>${user}, 当前登录用户,类型是{@link User 用户实体}</li>
 * </ul>对于不能解释的变量, 就按照模板中本来的定义输出, 不抛出异常, 要更细致地检查执行情况
 * 设置<code>log4j.logger.common.report.excel=DEBUG</code>
 * @author zhangli
 * @version $Id: ExcelReportService.java,v 1.1 2010/12/10 10:54:15 silencily Exp $
 * @since 2007-5-6
 */
public class ExcelReportService implements ReportManagementService {
    
    /** <code>excel</code>报表最大行数 */
    public static final int MAX_ROW_COUNT = 500;
    
    public static final String DATE_KEY = "date";
    
    public static final String DATETIME_KEY = "datetime";

    public static final String VFS_TMP_DIR = "tmp/report";
    
    public static final Pattern PLACE_HOLDER_PATTERN = Pattern.compile("\\$\\{\\s*([a-zA-Z][^\\$\\{\\}\\s]*)\\}");

    /** 如果占位符满足这个模式就是循环输出数据的开始位置, 从这里开始循环直到循环结束 */
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
            throw new IllegalStateException("参数ReportSource没有指定输出流");
        }
        ReportConfig config = loadReportConfig(reportSource.getCode());
        reportSource.setReportConfig(config);
        /* 仅操作第一个sheet的内容 */
        try {
            Workbook sourceWorkbook = readTemplate(config);
            Sheet sourceSheet = sourceWorkbook.getSheet(0);
            int rows = sourceSheet.getRows();
            sourceWorkbook.close();
            if (rows == 0) {
                throw new IllegalStateException("报表" 
                    + config.getCode() 
                    + "," 
                    + config.getName() 
                    + "的模板没有内容");
            }
            /* 清除临时目录内容 */
            String tempDir = tmpDir();
            if (fileObjectManager.exists(tempDir)) {
                fileObjectManager.delete(new FileObject(tempDir), true);
            }
            /* 
             * 保存嵌套报表(分多行显示)在一页显示的指针, 实际中如果一张报表包含多个分行显示并且
             * 每个都限制每页显示行数没有意义, 这样做的目的是增强容错性
             */
            Buffer fifo = new UnboundedFifoBuffer(10);
            data = preprocess(data);
            /* TODO:采用VELOCITY实现表达式及解析 */
            Interpreter interpreter = new Interpreter();
            for (Iterator it = data.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry entry = (Map.Entry) it.next();
                interpreter.set(entry.getKey().toString(), entry.getValue());
            }
            /* 是否是多页报表的标志 */
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
            throw new UnexpectedException("不能创建报表", ex);
        }
    }
    
    private Workbook readTemplate(ReportConfig config) {
        try {
            ReportConfigImpl impl = (ReportConfigImpl) config;
            return Workbook.getWorkbook(impl.getTemplate(this.fileObjectManager));
        } catch (Exception e) {
            throw new UnexpectedException("读报表模板错误," + config.getCode() + "," + config.getName(), e);
        }
    }
    
    private ByteArrayOutputStream readWrite(ReportConfig config, Interpreter interpreter, Buffer fifo) {
        Workbook sourceWorkbook = readTemplate(config);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            Sheet sheet = sourceWorkbook.getSheet(0);
            WritableWorkbook target = createReportWorkbook(output, sourceWorkbook);
            WritableSheet targetSheet = target.getSheet(0);
            /* 没有循环, 开始循环, 正在循环, 结束循环分别是 -1, 1, 2, 3 */
            int flag = -1;
            /* 循环标记行的下面一行, 是循环体 */
            int loopStartRow = -1;
            /* 循环位置, 当下一页时从这个位置的下面开始, 当到达List末尾时补充空行 */
            int loopIndex = -1;
            /* 循环次数 */
            int size = -1;
            
            if (fifo.size() > 0) {
                Integer pointer = (Integer) fifo.remove();
                loopIndex = pointer.intValue();
            }
            /* 循环变量名, ${item : items}, loops[0]保存 item, loops[1]保存 items */
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
            throw new UnexpectedException("excel模板变量定义错误", e);
        } catch (RowsExceededException e) {
            clean(output);
            throw new UnexpectedException("excel模板定义错误,循环标记行已经是最后一行", e);
        } catch (Exception e) {
            clean(output);
            throw new UnexpectedException("不能写excel内容到输出流", e);
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
            logger.debug("创建临时文件[" + fullName + "]保存已创建的报表");
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
            throw new UnexpectedException("不能创建输出报表", e);
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
                    logger.info("不能解释变量[" + key + "],模板中的格式[" + content + "]", e);
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
    
    /* 是否定义了循环, 只要一行中出现了${item : items,30}类似的格式, 这行的其他cell全部忽略 */
    private int rowType(int flag, Cell[] cells, String[] loops) {
        /* 如果是循环中, 仅仅期待循环结束标志 */
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
            throw new UnexpectedException("不能读取bsh script", e);
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
