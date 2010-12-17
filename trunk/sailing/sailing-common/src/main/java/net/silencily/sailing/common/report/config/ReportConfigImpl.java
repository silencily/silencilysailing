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
 * ��������ʵ����, ��Ϊ������һЩά���õķ���, Ϊ��ʹ�ñ���ĳ��������ؿ����������õ���Ϣ
 * , ���Բ����˽ӿ�. ����ά������Ҫע��: ģ���ǻ���{@link #code �������}���ɵ�<code>vfs</code>
 * ·��, ����Ҫ��һ�����ô���������ı����, Ҫ�����޸ı���ʱ�ƶ�ģ����<code>vfs</code>��
 * ��λ��
 * @author zhangli
 * @version $Id: ReportConfigImpl.java,v 1.1 2010/12/10 10:54:17 silencily Exp $
 * @since 2007-5-7
 * @see ExcelReportService �������ñ������ñ���Ĳ���
 */
public class ReportConfigImpl extends Entity implements ReportConfig, Validatable {

    /**
     * ������, ͨ����ģ������.��������.������������, ��"technology.supervise.report01"
     * , ������Ե�ֵ����<code>id</code>, �μ�{@link ReportManagementService}�������
     * һ�������<code>code</code>
     */
    private String code;
    
    /**
     * ������ʾ����, ���ṩά������ʱ������ʾ�������û�
     */
    private String name;
    
    /**
     * ģ������, �����������
     * {@link ReportManagementService#saveReportConfig(ReportConfig) <code>����ģ��</code>}
     * ʱά����
     */
    private String templateName;
    
    /** �����ڱ������ö���ʱ�ϴ�����ģ����ʹ�õ����� */
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
            errorMsg.add("û����д�������");
        }
        if (StringUtils.isBlank(getName())) {
            errorMsg.add("û����д��������");
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
            throw new IllegalStateException("��������[" + getCode() + "]û�ж���ģ��");
        }
        String name = vfsTemplateName();
        if (fileObjectManager.exists(name)) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            fileObjectManager.read(name, output);
            return new ByteArrayInputStream(output.toByteArray());
        }
        throw new IllegalStateException("��������[" 
            + getCode() + "]û��[" + name + "]ģ��");
    }
}
