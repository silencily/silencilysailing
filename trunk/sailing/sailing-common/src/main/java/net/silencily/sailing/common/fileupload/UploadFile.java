package net.silencily.sailing.common.fileupload;

import java.io.InputStream;

import javax.servlet.ServletContext;

/**
 * <p>����һ������<code>web</code>�ϴ��ļ�, ��ʵ���ϴ����ļ�ʱ���Դ���������ļ�����, ���г�
 * ���ϴ����ļ�ʱ, ������Եõ��ļ����Լ���ȡ�ļ���·��. ��ʹ�����ϴ����ļ���, Ҫ��ȷ�ص���
 * <code>destory</code>�����ͷ���Դ</p>
 * <p>����ӿ���һ��ȱʡ�Ļ���<code>jakarta commons fileupload</code>��ʵ��, ����ʵ��
 * ������Ϊ���ڲؾ����ʵ�ֲ���, (���ǿ���Ҫ����<code>webwork</code>)</p>
 * <p>�κβ�����Ӧ�������ж��Ƿ��Ǹո��ϴ����ļ�, �������Ѿ��ϴ����ļ�</p>
 * @author Scott Captain
 * @since 2006-6-3
 * @version $Id: UploadFile.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 *
 */
public interface UploadFile {
    
    /**
     * �����ļ�����, ������·������. ���������������ҳ����ʾ���ϴ����ļ�, ͬʱҲ������ҳ��
     * ���������ɾ������Щ���ϴ����ļ�(û���ύ�����ľ���ɾ�����ļ�). ����������ϴ���
     * �ļ�����ʹ��{@link #getUploadFileName() <code>getUploadFileName</code>}����.
     * ���ܴ�����������ո��ϴ����ļ�����
     * 
     * @return �ļ�����, ������<code>null</code>��<code>empty</code>
     */
    String getFileName();
    
    /**
     * �����Ѿ��ϴ������������ļ�����
     * @param fileName �Ѿ��ϴ������������ļ�����
     */
    void setFileName(String fileName);

    /**
     * �����ϴ���Դ��<code>MIME</code>����, ʵ�ֿ��ܽ��������ļ���׺�����ж���������. ���
     * �����ڴ����ϴ��ļ�ʱ, �����Ѿ������ڷ��������ļ�ʱ������ʹ��
     * @return �ϴ���Դ��<code>MIME</code>����
     */
    String getContentType();
    
    /**
     * ����<code>web</code>Ӧ�ö����<code>MIME</code>����
     * @param sc ��ǰӦ�õ�<code>ServletContext</code>
     * @return <code>web</code>Ӧ�ö����<code>MIME</code>����
     */
    String getContentType(ServletContext sc);
    
    /**
     * ���ֽڱ���ոջ��Ѿ��ϴ����ļ��Ĵ�С, �����Ѿ��ϴ����ļ�����������������Ƿ���Եõ�
     * ���ȡ����ҳ���Ƿ��ύ�����ֵ
     * @return �ոջ��Ѿ��ϴ����ļ��Ĵ�С
     */
    int getFileSize();
    
    /**
     * �ж����ʵ���Ƿ��Ǹո��ϴ����ļ�, ������ո��ϴ����ļ�ʱһ��Ҫ���ȵ�����������ж��Ƿ�
     * ������Ҫ���ļ�
     * @return ������ʵ���ʾһ���ո��ϴ����ļ�����<code>true</code>,����<code>false</code>
     */
    boolean isUploadFile();
    
    /**
     * �����ո��ϴ����Ѿ��ϴ����ļ�������
     * @return ����ո��ϴ����Ѿ��ϴ����ļ������ݵ�������
     */
    InputStream getContent();

    /**
     * �����ļ�ִ�й�����,��Ҫ��{@link #getContent() <code>getContent</code>}������һ��
     * Ҫ������������ͷ����õ���Դ
     */
    void destory();
}
