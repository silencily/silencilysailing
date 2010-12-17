package net.silencily.sailing.common.fileupload;

import java.io.InputStream;

import javax.servlet.ServletContext;

import net.silencily.sailing.exception.UnexpectedException;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.upload.FormFile;

import com.power.vfs.FileObject;
import com.power.vfs.FileObjectManager;



/**
 * <code>UploadFile</code>��<code>Commons FileUpload</code>��<code>vfs</code>��ʵ��
 * @author Scott Captain
 * @since 2006-6-3
 * @version $Id: VfsUploadFile.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 *
 */
public class VfsUploadFile implements UploadFile {
    
    /** �����ʵ����һ���ո��ϴ����ļ�ʱ, ������Ծͱ�ʾ�Ǹ��ļ� */
    private FormFile formFile;
    
    /** �����ʵ����һ���Ѿ��ϴ����ļ�ʱ, ������Ծͱ�ʾ�Ǹ��ļ� */
    private FileObject fileObject;
    
    /** ���ʵ���ʾ���ļ����� */
    private String fileName;
    
    /** ���ʵ���ʾ���ļ����� */
    private String fileDesc;
    
    /** ���ʵ���ʾ��<code>MIME</code>���� */
    private String contentType;
    
    /** ���ʵ���ʾ�����ֽڼ���Ĵ�С */
    private int fileSize = -1;
    
    /**��ʾ�ļ��ĺ�׺��������ǰ����ʾ��Ӧ����ͼ�꣺���Դ�contentType�жϣ�Ŀǰ���ý�ȡ�ļ���"."���ַ�*/
    private String suffName = "";
    
    public VfsUploadFile() {}
    
    public VfsUploadFile(FileObject fileObject) {
        this.fileObject = fileObject;
    }
    
    public FileObject getFileObject() {
        return this.fileObject;
    }

    public FormFile getFormFile() {
        return formFile;
    }

    public void setFormFile(FormFile formFile) {
        this.formFile = formFile;
    }
    
    public String getFileName() {
        String fn = null;
        if (formFile != null) {
            fn = formFile.getFileName();
        } else if (fileObject != null) {
            fn = fileObject.getFileName();
        }
        
        if (StringUtils.isBlank(fn)) {
            fn = fileName;
        }
        
        return fn;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        if (contentType != null) {
            return contentType;
        }

        if (fileObject != null) {
            contentType = fileObject.getType();
        } else if (formFile != null) {
            contentType = formFile.getContentType();
        }
        
        return contentType;
    }

    public String getContentType(ServletContext sc) {
        String mime = sc.getMimeType(getFileName());
        return (mime == null ? "application/oct-stream" : mime);
    }
    
    public int getFileSize() {
        if (fileSize != -1) {
            return fileSize;
        }
        
        if (formFile != null) {
            fileSize = formFile.getFileSize();
        } else if (fileObject != null) {
            Long l = (Long) fileObject.getProperty(FileObjectManager.KEY_SIZE);
            fileSize = l.intValue();
        }
        
        return fileSize;
    }

    public boolean isUploadFile() {
        return formFile != null && StringUtils.isNotBlank(formFile.getFileName()) && formFile.getFileSize() > 0;
    }

    public InputStream getContent() {
        InputStream in = null;
        if (isUploadFile()) {
            try {
                in = formFile.getInputStream();
            } catch (Exception e) {
                throw new UnexpectedException("��ȡ�ϴ��ļ�[" + getFileName() + "]ʱ����", e);
            }
        }

        return in;
    }

    public void destory() {
        if (isUploadFile()) {
            formFile.destroy();
        }
    }

	public String getSuffName() {
		if(getFileName().indexOf(".")>0){
			suffName = getFileName().split("\\.")[1].toLowerCase();
		}
		return suffName;
	}

	public void setSuffName(String suffName) {
		this.suffName = suffName;
	}

	/**
	 * @return the fileDesc
	 */
	public String getFileDesc() {
		return fileDesc;
	}

	/**
	 * @param fileDesc the fileDesc to set
	 */
	public void setFileDesc(String fileDesc) {
		this.fileDesc = fileDesc;
	}
}
