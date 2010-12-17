package net.silencily.sailing.common.report.service;

import java.io.OutputStream;
import java.io.Writer;

import net.silencily.sailing.common.report.config.ReportConfig;


/**
 * 报表输出源, 当创建一个报表时, 用这个实例作为参数调用报表服务, 服务处理后填写输出流和报表
 * 配置, 报表服务要求的属性是"报表编码"和"输出流", 对于<code>excel</code>报表服务要求输出
 * 流是<code>OutputStream</code>, 对于<code>velocity</code>要求是<code>Writer</code>
 * @author zhangli
 * @version $Id: ReportSource.java,v 1.1 2010/12/10 10:54:17 silencily Exp $
 * @since 2007-5-6
 */
public class ReportSource {
    
    public ReportSource() {}
    
    public ReportSource(String code, OutputStream output) {
        this.code = code;
        this.output = output;
    }
    
    public ReportSource(String code, Writer writer) {
        this.code = code;
        this.writer = writer;
    }
    
    /** 报表配置编码, 调用报表服务时必须填写 */
    private String code;
    
    /** 报表配置, 调用报表服务后由报表服务设置 */
    private ReportConfig reportConfig;
    
    /** 输出流, 生成的报表写到这里 */
    private OutputStream output;
    
    /** 字符输出流, 生成的报表写到这里 */
    private Writer writer;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public OutputStream getOutput() {
        return output;
    }

    public void setOutput(OutputStream output) {
        this.output = output;
    }

    public ReportConfig getReportConfig() {
        return reportConfig;
    }

    public void setReportConfig(ReportConfig reportConfig) {
        this.reportConfig = reportConfig;
    }

    public Writer getWriter() {
        return writer;
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }
}
