package net.silencily.sailing.common.report.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.silencily.sailing.common.fileupload.VfsUploadFiles;
import net.silencily.sailing.framework.persistent.Entity;
import net.silencily.sailing.framework.persistent.Validatable;

import org.apache.commons.lang.StringUtils;

import com.power.vfs.FileObjectManager;



/**
 * 报表配置实现类, 因为包含了一些维护用的方法, 为了使用报表的程序更清楚地看到关于配置的信息
 * , 所以采用了接口. 配置维护程序要注意: 模板是基于{@link #code 报表编码}生成的<code>vfs</code>
 * 路径, 所以要不一个配置创建后不允许改变编码, 要不在修改编码时移动模板在<code>vfs</code>中
 * 的位置
 * @author zhangli
 * @version $Id: ReportConfigImpl.java,v 1.1 2010/12/10 10:54:17 silencily Exp $
 * @since 2007-5-7
 * @see ExcelReportService 关于设置报表配置编码的策略
 */
public class ReportConfigImpl extends Entity implements ReportConfig, Validatable {

    /**
     * 报表编号, 通常以模块名称.功能名称.报表名称命名, 像"technology.supervise.report01"
     * , 这个属性的值用作<code>id</code>, 参见{@link ReportManagementService}如何生成
     * 一个报表的<code>code</code>
     */
    private String code;
    
    /**
     * 报表显示名称, 当提供维护界面时用于显示给最终用户
     */
    private String name;
    
    /**
     * 模板名称, 这个属性是在
     * {@link ReportManagementService#saveReportConfig(ReportConfig) <code>保存模板</code>}
     * 时维护的
     */
    private String templateName;
    
    /** 用于在报表配置定义时上传下载模板所使用的属性 */
    private VfsUploadFiles vfsUploadFiles;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateNameWithPath) {
        this.templateName = templateNameWithPath;
    }
    
    public VfsUploadFiles getVfsUploadFiles() {
        if (this.vfsUploadFiles == null) {
            this.vfsUploadFiles = new VfsUploadFiles();
        }
        return this.vfsUploadFiles;
    }

    public void validate() {
        List errorMsg = new ArrayList();
        if (StringUtils.isBlank(getCode())) {
            errorMsg.add("没有填写报表编码");
        }
        if (StringUtils.isBlank(getName())) {
            errorMsg.add("没有填写报表名称");
        }
        if (errorMsg.size() > 0) {
            throw new IllegalStateException(errorMsg.toString());
        }
    }

    public String vfsTemplatePath() {
        if (getCode() == null) {
            return null;
        }
        return new StringBuffer(TEMPLATE_ROOT_PATH)
            .append('/').append(getCode().replace('.', '/')).toString();
    }
    
    public String vfsTemplateName() {
        return new StringBuffer(vfsTemplatePath())
            .append('/').append(getTemplateName()).toString();
    }

    public InputStream getTemplate(FileObjectManager fileObjectManager) {
        if (vfsTemplatePath() == null) {
            throw new IllegalStateException("报表配置[" + getCode() + "]没有定义模板");
        }
        String name = vfsTemplateName();
        if (fileObjectManager.exists(name)) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            fileObjectManager.read(name, output);
            return new ByteArrayInputStream(output.toByteArray());
        }
        throw new IllegalStateException("报表配置[" 
            + getCode() + "]没有[" + name + "]模板");
    }
}
