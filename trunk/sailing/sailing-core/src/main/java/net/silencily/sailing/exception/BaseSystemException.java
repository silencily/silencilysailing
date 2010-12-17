package net.silencily.sailing.exception;


/**
 * <p>ϵͳ������ϵͳ�쳣(<code>UncheckedException</code>)�ĳ���, ���ڴ�����ֿ��ܵĴ�����
 * ��, ��<code>BaseAppException</code>��ʹ�����, �����쳣�ǿ��������е�����. �����쳣��û
 * �к���<code>IllegalArgumentException</code>,<code>IllegalStateException</code>��
 * �����������쳣, ���ڳ־û������Ͳ���Ȼ����ʹ��<code>java</code>�����ṩ����Щ�쳣, ����Ӧ
 * ����ҵ���߼���ͱ��ֲ�, ����Щ����¾�Ӧ��ʹ������쳣���������������쳣, ������֤ʧ�ܵ�ֱ��
 * �׳�, �ɼܹ�����չʾ�ʹ���</p>
 * <p>Ӧ��ע�⹹����<code>BaseSystemException(Object param)</code>, ���������ڿ���ʱ��
 * ���ǲ���ʱ�����쳣��Ϣ. ��ʱ�쳣��Ϣ����ֱ�Ӵ�<code>param</code>����������</p>
 * @author scott
 * @since 2006-3-21
 * @version $Id: BaseSystemException.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 *
 */
public abstract class BaseSystemException extends RuntimeException implements ExceptionInfo {
    private String key;
    private Object[] params;
    private String errorInformation;
    
    /** ���ڴ�����Ϣ��û��ռλ��������´��ݲ��� */
    public static final Object EMPTY_PARAMETER = ExceptionInfo.NULL_PARAM;
    
    /**
     * ȱʡ������, û��ָ���κδ�����Ϣ, ��õ�������ڴ�����������쳣����(����)�����˴�����Ϣ.
     * ��ʾ�����Ϣû�и��������Ҫ�ṩ, ����������������:<tt>CannotConnectToDatabase=��
     * ���ж�,�����������ݿ�</tt>. �������쳣����Ҫ�����κζ���˵��,����ʹ�����������
     */
    protected BaseSystemException() {
        super();
    }

    /**
     * �����ڴ�����Ϣ�ж����˰���ռλ����<code>code</code>, �����������ָ����̬��ʾ����. ���
     * ����������ĳ��ȷ�����쳣����, ����ͬ��ֻ������쳣�����������Ļ����Ĳ���. ���緢���ڼ�������
     * �ļ�ʱû�а���Ҫ���д�����ļ��Ĵ���, ��������������������<code>code</code>: <tt>
     * ConfigurationException=����{0}��������:{1}</tt>, ʵ�ʵĵ���������<code>
     * new ConfigurationException(new String[] {"Spring xml bean", "û�ж���ServiceInfo���"})</code>
     * 
     * @param params �滻ռλ������Ϣ ��������Ϣû��ռλ��ʹ��{@link #EMPTY_PARAMETER <code>EMPTY_PARAMETER}
     */
    protected BaseSystemException(Object[] params) {
        super();
        this.params = params;
    }
    
    /**
     * ���ڲ�ʹ���쳣���������ɴ���<code>code</code>�Ļ���, �ڴ�����ֱ��ָ��<code>code</code>,
     * ȱʡ������쳣���������ɼ���������Ϣ��<code>code</code>ͨ����<code>�׳��쳣��ȫ�޶�����
     * .������.�쳣����</code>, ��һ���������ж��ͬ���쳣�������������ڲ�ͬ�Ļ�����ʱ���������
     * Ԥ�ȶ��������Ϣ�Ĳ���
     * @param key       ����������Ϣ��<code>code</code>
     * @param params    ������Ϣռλ��, ��������Ϣû��ռλ��ʹ��{@link #EMPTY_PARAMETER <code>EMPTY_PARAMETER}
     */
    protected BaseSystemException(String key, Object[] params) {
        super();
        this.key = key;
        this.params = params;
    }
    
    /**
     * ���ڰ�װ�������쳣, ��ָ���쳣�����Ļ�����Ϣ. �����������ϵͳ��<code>UnexpectedException</code>
     * ��ȱʡ������. ���ڰ�װ<code>Unchecked</code>�쳣���޷�������쳣
     * @param msg    �����쳣�Ļ���˵��
     * @param cause  ʵ�ʵ��쳣
     */
    protected BaseSystemException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public String getKey() {
        return key;
    }

    public Object[] getParams() {
        if (this.params == null) {
            return NULL_PARAM;
        }
        
        return this.params;
    }

    public String getErrorInformation() {
        return this.errorInformation;
    }

    public void setErrorInformation(String msg) {
        this.errorInformation = msg;
    }

    public Throwable getThrowable() {
        return ExceptionUtils.getActualThrowable(this);
    }
}
