package net.silencily.sailing.common.fileupload;

import java.io.InputStream;

import javax.servlet.ServletContext;

/**
 * <p>表现一个基于<code>web</code>上传文件, 当实际上传了文件时可以从这里读到文件内容, 当列出
 * 已上传的文件时, 这里可以得到文件名以及读取文件的路径. 当使用了上传的文件后, 要明确地调用
 * <code>destory</code>方法释放资源</p>
 * <p>这个接口有一个缺省的基于<code>jakarta commons fileupload</code>的实现, 它的实际
 * 意义是为了掩藏具体的实现策略, (我们可能要采用<code>webwork</code>)</p>
 * <p>任何操作都应该首先判断是否是刚刚上传的文件, 或者是已经上传的文件</p>
 * @author Scott Captain
 * @since 2006-6-3
 * @version $Id: UploadFile.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 *
 */
public interface UploadFile {
    
    /**
     * 返回文件名称, 不包含路径部分. 这个方法典型用于页面显示已上传的文件, 同时也用于与页面
     * 表单交互检查删除了哪些已上传的文件(没有提交上来的就是删除的文件). 如果检索刚上传的
     * 文件名称使用{@link #getUploadFileName() <code>getUploadFileName</code>}代替.
     * 不能从这里检索到刚刚上传的文件名称
     * 
     * @return 文件名称, 不返回<code>null</code>或<code>empty</code>
     */
    String getFileName();
    
    /**
     * 设置已经上传到服务器的文件名称
     * @param fileName 已经上传到服务器的文件名称
     */
    void setFileName(String fileName);

    /**
     * 返回上传资源的<code>MIME</code>类型, 实现可能仅仅根据文件后缀名了判断它的类型. 这个
     * 方法在处理上传文件时, 或处理已经保存在服务器的文件时都可以使用
     * @return 上传资源的<code>MIME</code>类型
     */
    String getContentType();
    
    /**
     * 返回<code>web</code>应用定义的<code>MIME</code>类型
     * @param sc 当前应用的<code>ServletContext</code>
     * @return <code>web</code>应用定义的<code>MIME</code>类型
     */
    String getContentType(ServletContext sc);
    
    /**
     * 以字节报告刚刚或已经上传的文件的大小, 对于已经上传的文件调用这个方法可能是否可以得到
     * 结果取决于页面是否提交了这个值
     * @return 刚刚或已经上传的文件的大小
     */
    int getFileSize();
    
    /**
     * 判断这个实体是否是刚刚上传的文件, 当处理刚刚上传的文件时一定要首先调用这个方法判断是否
     * 是所需要的文件
     * @return 如果这个实体表示一个刚刚上传的文件返回<code>true</code>,否则<code>false</code>
     */
    boolean isUploadFile();
    
    /**
     * 检索刚刚上传或已经上传的文件的内容
     * @return 保存刚刚上传或已经上传的文件的内容的输入流
     */
    InputStream getContent();

    /**
     * 当对文件执行过操作,主要是{@link #getContent() <code>getContent</code>}操作后一定
     * 要调用这个方法释放所用的资源
     */
    void destory();
}
