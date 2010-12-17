package net.silencily.sailing.utils;

import java.net.URL;
import java.util.Locale;

/**
 * 选择当前应用运行的操作系统的<code>utility</code>
 * 
 * @author scott
 * @since 2005-12-29
 * @version $Id: PlatformSelectorUtils.java,v 1.1 2010/12/10 10:54:21 silencily Exp $
 *
 */
public abstract class PlatformSelectorUtils {
    
    /** <code>Windows</code>操作系统 */
    public static final int OS_WINDOWS = 0;
    
    /** <code>Linux</code>操作系统 */
    public static final int OS_LINUX = 1;
    
    /** 从系统属性检索出来的<code>Windows</code>操作系统名称 */
    public static final String WINDOWS = "windows";
    
    /** 从系统属性检索出来的<code>Linux</code>操作系统名称 */
    public static final String LINUX = "linux";
    
    private static final String[] oss = {WINDOWS, LINUX};
    
    /** 从系统属性检索出来的实际操作系统名称 */
    private static final String os = System.getProperty("os.name").toLowerCase(Locale.US);
    
    /**
     * 选择当前应用运行在什么操作系统下, 用于某些依赖于操作系统的操作, 比如创建目录的实际位置等,
     * <b>注意</b>: 没有更细致地判断操作系统的特征, 仅仅判断如果不是<code>Windows</code>就
     * 作为<code>unix style</code>的操作系统
     * 
     * @return 返回表示操作系统的变量
     */
    public final static int selectOs() {
        if (os.indexOf(WINDOWS) > -1) {
            return OS_WINDOWS;
        }
        
        return OS_LINUX;
    }
    
    /**
     * 返回当前操作系统代表性名称, 比如对于<code>Win9x, Windows NT, XP, etc.</code>返回
     * <code>windows</code>, 对于<code>Linux, Unix, etc.</code>等返回<code>linux</code>
     * 
     * @return 当前操作系统代表性名称
     */
    public static String selectOsName() {
        return oss[selectOs()];
    }
    
    /**
     * 判断当前应用运行在独立的应用还是在应用服务器环境下, 主要用于测试环境下的类的正常运行
     * @return 如果应用运行在独立的环境下返回<code>true</code>
     */
    public static boolean isIndependent() {
        URL url = Thread.currentThread().getContextClassLoader().getResource("/");
        return (url == null);
    }
}
