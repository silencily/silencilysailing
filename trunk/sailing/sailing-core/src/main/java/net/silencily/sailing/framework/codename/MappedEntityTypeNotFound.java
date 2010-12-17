package net.silencily.sailing.framework.codename;

import net.silencily.sailing.exception.BaseSystemException;

/**
 * �����ҵ�ӳ���ҵ��ʵ������, ����һ��ϵͳ��ҵ��ʹ��<code>CodeName</code>���Ƹ��������
 * һ��ϵͳ��ʵ�������ʱû���ҵ�Ҫ���õ�ʵ��<code>Class</code>, ʹ�����ַ�����Ӧ��һ��Ҫ
 * ��������쳣, ���������¿���������û�в����������ϵͳ�������
 * @author zhangli
 * @version $Id: MappedEntityTypeNotFound.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 * @since 2007-6-9
 */
public class MappedEntityTypeNotFound extends BaseSystemException {
    
    public MappedEntityTypeNotFound(String msg, ClassNotFoundException ex) {
        super(msg, ex);
    }
}
