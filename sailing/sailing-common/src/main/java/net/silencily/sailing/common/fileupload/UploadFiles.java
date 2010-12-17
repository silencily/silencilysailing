package net.silencily.sailing.common.fileupload;

import java.io.OutputStream;


/**
 * 管理上传文件<code>UploadFile</code>的接口, 负责为使用这个接口的组件读取和写入文件, 这个
 * 接口的实现类是有状态的. 在使用这个组件之前首先应该设置<code>filePath</code>的属性
 * @author Scott Captain
 * @since 2006-6-3
 * @version $Id: UploadFiles.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 *
 */
public interface UploadFiles {
    
    /**
     * 设置保存上传文件的根路径
     */
    void setFilePath(String filePath);

    /**
     * 从保存上传文件的位置读取所有已上传的文件
     */
    void read();
    
    /**
     * 从保存上传文件的位置读取指定名称<code>UploadFile</code>
     * @param fileName 要读取的文件名
     * @throws IllegalStateException 如果指定名称的文件没找到
     */
    UploadFile read(String fileName);
    
    /**
     * 一个方便输出文件内容的<code>utility</code>方法, 把已经上传的文件的内容输出到指定
     * 的输出流中, 注意是已经上传的文件, <b>不能对刚刚上传的文件执行这个方法</b>
     * @param file 要输出的<code>UploadFile</code>
     * @param out  要输出的目的
     */
    void output(String fileName, OutputStream out);

    /**
     * 把所有的上传文件写到<code>filePath</code>指定的位置
     */
    void write();
    
    /**
     * 删除保存上传文件目录(通过{@link #setFilePath(String)}方法设置)目录下所有的文件, 通常
     * 目录的格式是"系统名/业务实体<tt>id</tt>", 比如信息发布中保存的一条信息的附件的目录是
     * "info/8f56tt4rddf676783243", 执行这个方法后将删除目录"info/8f56tt4rddf676783243"
     * 及下面所有的文件
     */
    void delete();
    
    /**
     * 删除保存在删除保存上传文件目录(通过{@link #setFilePath(String)}方法设置)下指定名称的文件
     * @param fileName 要删除的文件名称
     * @throws NullPointerException 如果文件名是<code>null</code>或<code>empty</code>
     * @throws IllegalArgumentException 如果没有指定名称的文件
     */
    void delete(String fileName);
}
