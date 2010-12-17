package net.silencily.sailing.utils;

import java.net.URL;
import java.util.Locale;

/**
 * ѡ��ǰӦ�����еĲ���ϵͳ��<code>utility</code>
 * 
 * @author scott
 * @since 2005-12-29
 * @version $Id: PlatformSelectorUtils.java,v 1.1 2010/12/10 10:54:21 silencily Exp $
 *
 */
public abstract class PlatformSelectorUtils {
    
    /** <code>Windows</code>����ϵͳ */
    public static final int OS_WINDOWS = 0;
    
    /** <code>Linux</code>����ϵͳ */
    public static final int OS_LINUX = 1;
    
    /** ��ϵͳ���Լ���������<code>Windows</code>����ϵͳ���� */
    public static final String WINDOWS = "windows";
    
    /** ��ϵͳ���Լ���������<code>Linux</code>����ϵͳ���� */
    public static final String LINUX = "linux";
    
    private static final String[] oss = {WINDOWS, LINUX};
    
    /** ��ϵͳ���Լ���������ʵ�ʲ���ϵͳ���� */
    private static final String os = System.getProperty("os.name").toLowerCase(Locale.US);
    
    /**
     * ѡ��ǰӦ��������ʲô����ϵͳ��, ����ĳЩ�����ڲ���ϵͳ�Ĳ���, ���紴��Ŀ¼��ʵ��λ�õ�,
     * <b>ע��</b>: û�и�ϸ�µ��жϲ���ϵͳ������, �����ж��������<code>Windows</code>��
     * ��Ϊ<code>unix style</code>�Ĳ���ϵͳ
     * 
     * @return ���ر�ʾ����ϵͳ�ı���
     */
    public final static int selectOs() {
        if (os.indexOf(WINDOWS) > -1) {
            return OS_WINDOWS;
        }
        
        return OS_LINUX;
    }
    
    /**
     * ���ص�ǰ����ϵͳ����������, �������<code>Win9x, Windows NT, XP, etc.</code>����
     * <code>windows</code>, ����<code>Linux, Unix, etc.</code>�ȷ���<code>linux</code>
     * 
     * @return ��ǰ����ϵͳ����������
     */
    public static String selectOsName() {
        return oss[selectOs()];
    }
    
    /**
     * �жϵ�ǰӦ�������ڶ�����Ӧ�û�����Ӧ�÷�����������, ��Ҫ���ڲ��Ի����µ������������
     * @return ���Ӧ�������ڶ����Ļ����·���<code>true</code>
     */
    public static boolean isIndependent() {
        URL url = Thread.currentThread().getContextClassLoader().getResource("/");
        return (url == null);
    }
}
