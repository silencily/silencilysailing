package net.silencily.sailing.framework.exception;

import net.silencily.sailing.exception.BaseBusinessException;

/**
 * 表示上传文件时文件大小超过限制的异常, 这个信息配置在基础架构中. 可能的信息格式是"上传文件{0}时
 * 大小超过限制, 最大限制是{1}M"
 * @author scott
 * @since 2006-4-1
 * @version $Id: FileuploadExceededException.java,v 1.1 2010/12/10 10:54:28 silencily Exp $
 *
 */
public class FileuploadExceededException extends BaseBusinessException {
    /**
     * 文件大小超过限制异常
     * @param fileName 引起错误的文件名
     */
    public FileuploadExceededException(String fileName) {
        super(new String[] {fileName});
    }
}
