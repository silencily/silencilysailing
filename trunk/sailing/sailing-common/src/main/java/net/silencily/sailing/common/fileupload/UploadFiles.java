package net.silencily.sailing.common.fileupload;

import java.io.OutputStream;


/**
 * �����ϴ��ļ�<code>UploadFile</code>�Ľӿ�, ����Ϊʹ������ӿڵ������ȡ��д���ļ�, ���
 * �ӿڵ�ʵ��������״̬��. ��ʹ��������֮ǰ����Ӧ������<code>filePath</code>������
 * @author Scott Captain
 * @since 2006-6-3
 * @version $Id: UploadFiles.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 *
 */
public interface UploadFiles {
    
    /**
     * ���ñ����ϴ��ļ��ĸ�·��
     */
    void setFilePath(String filePath);

    /**
     * �ӱ����ϴ��ļ���λ�ö�ȡ�������ϴ����ļ�
     */
    void read();
    
    /**
     * �ӱ����ϴ��ļ���λ�ö�ȡָ������<code>UploadFile</code>
     * @param fileName Ҫ��ȡ���ļ���
     * @throws IllegalStateException ���ָ�����Ƶ��ļ�û�ҵ�
     */
    UploadFile read(String fileName);
    
    /**
     * һ����������ļ����ݵ�<code>utility</code>����, ���Ѿ��ϴ����ļ������������ָ��
     * ���������, ע�����Ѿ��ϴ����ļ�, <b>���ܶԸո��ϴ����ļ�ִ���������</b>
     * @param file Ҫ�����<code>UploadFile</code>
     * @param out  Ҫ�����Ŀ��
     */
    void output(String fileName, OutputStream out);

    /**
     * �����е��ϴ��ļ�д��<code>filePath</code>ָ����λ��
     */
    void write();
    
    /**
     * ɾ�������ϴ��ļ�Ŀ¼(ͨ��{@link #setFilePath(String)}��������)Ŀ¼�����е��ļ�, ͨ��
     * Ŀ¼�ĸ�ʽ��"ϵͳ��/ҵ��ʵ��<tt>id</tt>", ������Ϣ�����б����һ����Ϣ�ĸ�����Ŀ¼��
     * "info/8f56tt4rddf676783243", ִ�����������ɾ��Ŀ¼"info/8f56tt4rddf676783243"
     * ���������е��ļ�
     */
    void delete();
    
    /**
     * ɾ��������ɾ�������ϴ��ļ�Ŀ¼(ͨ��{@link #setFilePath(String)}��������)��ָ�����Ƶ��ļ�
     * @param fileName Ҫɾ�����ļ�����
     * @throws NullPointerException ����ļ�����<code>null</code>��<code>empty</code>
     * @throws IllegalArgumentException ���û��ָ�����Ƶ��ļ�
     */
    void delete(String fileName);
}
