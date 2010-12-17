package net.silencily.sailing.framework.exception;

import net.silencily.sailing.exception.BaseBusinessException;

/**
 * ��ʾ�ϴ��ļ�ʱ�ļ���С�������Ƶ��쳣, �����Ϣ�����ڻ����ܹ���. ���ܵ���Ϣ��ʽ��"�ϴ��ļ�{0}ʱ
 * ��С��������, ���������{1}M"
 * @author scott
 * @since 2006-4-1
 * @version $Id: FileuploadExceededException.java,v 1.1 2010/12/10 10:54:28 silencily Exp $
 *
 */
public class FileuploadExceededException extends BaseBusinessException {
    /**
     * �ļ���С���������쳣
     * @param fileName ���������ļ���
     */
    public FileuploadExceededException(String fileName) {
        super(new String[] {fileName});
    }
}
