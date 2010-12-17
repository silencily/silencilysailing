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
 * <code>ResourceProbe</code>�ĳ���ʵ��, ����<code>PlatformSelector</code>ѡ������ƽ̨
 * �ǵ���Ӧ�û��ǻ���Ӧ�÷�������<code>web</code>Ӧ��, �ֱ���ò�ͬ�ķ�ʽ����ƥ�����Դ
 * 
 * @author scottcaptain
 * @since 2005-12-21
 * @version $Id: AbstractResourceProbe.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 */
public abstract class AbstractResourceProbe implements ResourceProbe {
    protected Log logger = LogFactory.getLog(AbstractResourceProbe.class);
    
    /**
     * �����������Դʱ���ڷָ�ÿ����Դ����
     */
    private static final String RESOURCE_NAME_SEPERATOR = ",";
    
    /**
     * �ڲ�����Դ��Դʱ��������쳣�Ƿ��׳�, ������ܼ���ĳЩ��Դ��Ӱ�����ִ�п��Թص��������
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
            throw new IllegalArgumentException("������Դʱ�����ǿ�ֵ");
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
     * �ڵ�����Ӧ�û���������ƥ��ָ��ģʽ����Դ, ���ؿ��Դ���·�����ص���Դ����
     * 
     * @param resourceName Ҫ��������Դ����ģʽ
     * @return ƥ��ָ��ģʽ����·����Դ����, ���û���ҵ����س���Ϊ<b>0</b>��<code>Set</code>
     */
    private Set findResourceInSingleApplication(String resourceName) {
        String systemPath = System.getProperty("java.class.path");
        String seperator = System.getProperty("path.separator");

        String[] paths = systemPath.split(seperator);
        Set result = new TreeSet();
        
        if (logger.isDebugEnabled()) {
            logger.debug("�ڵ�����Ӧ�û�������������["
                + resourceName
                + "]����Դ, ������·����"
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
     * ��Ӧ�÷����������в���ƥ��ָ��ģʽ����Դ����
     * 
     * @param resourceName Ҫ���ҵ���Դ����ģʽ
     * @return �ҵ�����Դ����, ���û���ҵ��κ���Դ���س���Ϊ<b>0</b>�ļ���
     */
    private Set findResourceInAppServer(String resourceName) {
        URL url = Thread.currentThread().getContextClassLoader().getResource("/");
        if (url == null) {
            IllegalStateException ex = new IllegalStateException("��Ӧ�÷����������¼�����Դ["
                + resourceName
                + "]ʱ����ʹ��'/'��λ��·��");
            throw ex;
        }

        Set result = new TreeSet();
        String rootDir = url.getPath();
        if (logger.isDebugEnabled()) {
            logger.debug("Ӧ�÷�����classesĿ¼��["
                + rootDir
                + "]");
        }
        
        result.addAll(findResourcesInFileSystem(rootDir, resourceName));
        
        String libDir = webLibDirectory(rootDir);
        if (logger.isDebugEnabled()) {
            logger.debug("Ӧ�÷�����libĿ¼��["
                + libDir
                + "]");
        }
        
        File lib = new File(libDir);
        if (!lib.exists()) {
            IllegalStateException ex = new IllegalStateException(
                "����Ӧ�÷�����libĿ¼��jar�ļ�ʱĿ¼["
                + libDir
                + "]������");
            if (logger.isInfoEnabled()) {
                logger.info("�����ļ�����," + ex.getMessage(), ex);
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
     * ����Ӧ��(web)��������<code>lib</code>Ŀ¼��·��
     * 
     * @param webClassesDir Ӧ��(web)��������<code>classes</code>Ŀ¼��·��
     * @return Ӧ��(web)��������<code>lib</code>Ŀ¼��·��
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
     * ����Ƿ��������Դ����ƥ��ָ����ģʽ, �������<code>pattern</code>����","�ָ��Ķ��
     * ģʽ���, �����Ƿ�ƥ������֮һ
     * 
     * @param resourceName Ҫƥ�����Դ����
     * @param pattern      ���ƥ���ģʽ, ������","�ָ��Ķ��ģʽ
     * @return             �����Դ����ƥ��ģʽ����<code>true</code>, ����<code>false</code>
     */
    protected abstract boolean matches(String resourceName, String pattern);
}
