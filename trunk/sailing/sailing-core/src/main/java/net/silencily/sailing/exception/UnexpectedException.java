package net.silencily.sailing.exception;

/**
 * ϵͳ�а�װ������<code>checked</code>�쳣������, �����쳣��Ҫ���ڰ�װ�������쳣ͬʱ�ڳ�����
 * ���ش���Ĵ���. �������ͨ�����ټ���������Ϣ��<code>code</code>, ���ǰѴ����ջ�е���Ϣֱ��
 * ��Ϊ��Ϣ. ����쳣��ʵ��������װ���Ǹ��쳣, ����û��ʵ������
 * @author scott
 * @since 2006-4-13
 * @version $Id: UnexpectedException.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 *
 */
public class UnexpectedException extends BaseSystemException {
    private Throwable actual;

    /**
     * ���ڰ�װ�������͵��쳣, ��˵���쳣�����Ķ����ͻ���, ����<pre>
     * new CannotReadFile("��ȡ��Աxxx������������", IOException)</pre>
     * @param msg    ˵���������Ļ������޷���ɵĶ�����Ϣ, ��������ҳ���<code>log</code>��
     * @param actual ʵ�ʵ��쳣
     */
    public UnexpectedException(String msg, Throwable actual) {
        super(msg, actual);
        this.actual = actual;
    }
    
    /** ����ʵ�ʵ��쳣��Ϣ */
    public Throwable getActual() {
        return this.actual;
    }
}
