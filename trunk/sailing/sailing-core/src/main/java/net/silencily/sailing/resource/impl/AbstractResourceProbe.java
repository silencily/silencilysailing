package net.silencily.sailing.resource.impl;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.silencily.sailing.resource.ResourceProbe;
import net.silencily.sailing.utils.DebugUtils;
import net.silencily.sailing.utils.PlatformSelectorUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <code>ResourceProbe</code>的抽象实现, 根据<code>PlatformSelector</code>选择运行平台
 * 是单独应用还是基于应用服务器的<code>web</code>应用, 分别采用不同的方式加载匹配的资源
 * 
 * @author scottcaptain
 * @since 2005-12-21
 * @version $Id: AbstractResourceProbe.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 */
public abstract class AbstractResourceProbe implements ResourceProbe {
    protected Log logger = LogFactory.getLog(AbstractResourceProbe.class);
    
    /**
     * 当检索多个资源时用于分隔每个资源名称
     */
    private static final String RESOURCE_NAME_SEPERATOR = ",";
    
    /**
     * 在查找资源资源时如果发生异常是否抛出, 如果不能加载某些资源不影响程序执行可以关掉这个开关
     */
    private boolean throwsException = true;
    
    public void setThrowsException(boolean throwsException) {
        this.throwsException = throwsException;
    }
    
    public boolean isThrowsException() {
        return this.throwsException;
    }

    public String[] search(String resourceName) {
        if (StringUtils.isBlank(resourceName)) {
            throw new IllegalArgumentException("搜索资源时参数是空值");
        }
        
        Set result = new TreeSet();
        String[] resourceNames = resourceName.split("\\s*" + RESOURCE_NAME_SEPERATOR + "\\s*");
        for (int i = 0; i < resourceNames.length; i++) {
            if (!isPattern(resourceNames[i])) {
                result.add(resourceNames[i]);
            } else if (PlatformSelectorUtils.isIndependent()) {
                result.addAll(findResourceInSingleApplication(resourceNames[i]));
            } else {
                result.addAll(findResourceInAppServer(resourceNames[i]));
            }
        }

        return (String[]) result.toArray(new String[result.size()]);
    }

    public String[] search(String resourceName, String elimination) {
        String[] resources = search(resourceName);
        if (StringUtils.isBlank(elimination)) {
            return resources;
        }
        
        List result = new ArrayList();
        
        for (int i = 0; i < resources.length; i++) {
            if (!matches(resources[i], elimination)) {
                result.add(resources[i]);
            }
        }

        return (String[]) result.toArray(new String[result.size()]);
    }
    
    /**
     * 在单独的应用环境下搜索匹配指定模式的资源, 返回可以从类路径加载的资源名称
     * 
     * @param resourceName 要搜索的资源名称模式
     * @return 匹配指定模式的类路径资源名称, 如果没有找到返回长度为<b>0</b>的<code>Set</code>
     */
    private Set findResourceInSingleApplication(String resourceName) {
        String systemPath = System.getProperty("java.class.path");
        String seperator = System.getProperty("path.separator");

        String[] paths = systemPath.split(seperator);
        Set result = new TreeSet();
        
        if (logger.isDebugEnabled()) {
            logger.debug("在单独的应用环境下搜索名称["
                + resourceName
                + "]的资源, 搜索的路径是"
                + DebugUtils.convertMultiLines(paths));
        }
        
        for (int i = 0; i < paths.length; i++) {
            if (paths[i].endsWith(".jar")) {
                result.addAll(findResourcesInJar(paths[i], resourceName));
            } else {
                File file = new File(paths[i]);
                if (file.exists() && file.isDirectory()) {
                    result.addAll(findResourcesInFileSystem(paths[i], resourceName));
                }
            }
        }
        
        return result;
    }

    /**
     * 在应用服务器环境中查找匹配指定模式的资源名称
     * 
     * @param resourceName 要查找的资源名称模式
     * @return 找到的资源名称, 如果没有找到任何资源返回长度为<b>0</b>的集合
     */
    private Set findResourceInAppServer(String resourceName) {
        URL url = Thread.currentThread().getContextClassLoader().getResource("/");
        if (url == null) {
            IllegalStateException ex = new IllegalStateException("在应用服务器环境下检索资源["
                + resourceName
                + "]时不能使用'/'定位根路径");
            throw ex;
        }

        Set result = new TreeSet();
        String rootDir = url.getPath();
        if (logger.isDebugEnabled()) {
            logger.debug("应用服务器classes目录是["
                + rootDir
                + "]");
        }
        
        result.addAll(findResourcesInFileSystem(rootDir, resourceName));
        
        String libDir = webLibDirectory(rootDir);
        if (logger.isDebugEnabled()) {
            logger.debug("应用服务器lib目录是["
                + libDir
                + "]");
        }
        
        File lib = new File(libDir);
        if (!lib.exists()) {
            IllegalStateException ex = new IllegalStateException(
                "检索应用服务器lib目录的jar文件时目录["
                + libDir
                + "]不存在");
            if (logger.isInfoEnabled()) {
                logger.info("检索文件错误," + ex.getMessage(), ex);
            }
            
            throw ex;
        }
        
        File[] files = lib.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".jar");
            }
        });
        
        for (int i = 0; i <  files.length; i++) {
            result.addAll(findResourcesInJar(files[i].getAbsolutePath(), resourceName));
        }

        return result;
    }
    
    /**
     * 生成应用(web)服务器的<code>lib</code>目录的路径
     * 
     * @param webClassesDir 应用(web)服务器的<code>classes</code>目录的路径
     * @return 应用(web)服务器的<code>lib</code>目录的路径
     */
    private String webLibDirectory(String webClassesDir) {
        if (!webClassesDir.endsWith("/")) {
            webClassesDir += "/";
        }
        
        String webLibDir = webClassesDir + "../lib"; 
        
        return webLibDir;
    }
    
    protected abstract Set findResourcesInFileSystem(String rootDir, String pattern);
    
    protected abstract Set findResourcesInJar(String jarFile, String pattern);
    
    /**
     * 检查是否给定的资源名称匹配指定的模式, 如果参数<code>pattern</code>是由","分隔的多个
     * 模式组成, 返回是否匹配其中之一
     * 
     * @param resourceName 要匹配的资源名称
     * @param pattern      检查匹配的模式, 可以是","分隔的多个模式
     * @return             如果资源名称匹配模式返回<code>true</code>, 否则<code>false</code>
     */
    protected abstract boolean matches(String resourceName, String pattern);
}
