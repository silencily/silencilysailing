package net.silencily.sailing.common.fileupload;

import net.silencily.sailing.framework.persistent.Entity;

/**
 * �ϴ��ļ�·��������, ������{@link FileObject}���Ե�ҵ��ʵ����ϴ�ʱ��ʱ�����ɱ����ļ�
 * ��·��, ������һ��Ŀ¼����̫����ļ�, ����һ�������м����ȱ�ݵ�, ���һ��ȱ�ݵ�ʹ����
 * �ϴ��ļ�, ��ô�������һ��Ŀ¼���м�����ļ�
 * @author zhangli
 * @version $Id: UploadFilePathGenerator.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 * @since 2007-4-25
 */
public interface UploadFilePathGenerator {
    
    /** ����·���ĸ�Ŀ¼ */
    String FIRST_LEVEL_PATH = "entity";
    
    /**
     * ����ҵ��ʵ�����ɱ����ϴ��ļ���·��, ���ɵ�·��û��ǰ���ͺ�׺��"/"
     * @param entity Ҫ�����ϴ��ļ���ҵ��ʵ��
     * @return ���ɵı���{@link FileObject}��·����, �����·���¿��Ա������ҵ��ʵ���
     * ���е��ϴ��ļ�, ������<code>null</code>
     * @throws NullPointerException ���������<code>null</code>
     * @throws IllegalArgumentException ������ݲ����޷�����·����
     */
    String path(Entity entity);
    
}
