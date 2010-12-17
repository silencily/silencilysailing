package net.silencily.sailing.resource.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import net.silencily.sailing.utils.DebugUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.DirectoryScanner;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 * <p>采用<code>AntPathMatcher</code>实现查找匹配资源名称</p>
 * 
 * @author scottcaptain
 * @since 2005-12-21
 * @version $Id: AntResourceProbe.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 */
public class AntResourceProbe extends AbstractResourceProbe {
    private PathMatcher pathMatcher = new AntPathMatcher();

    protected Set findResourcesInFileSystem(String rootDir, String pattern) {
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(rootDir);
        scanner.setIncludes(new String[] {pattern});
        scanner.setCaseSensitive(true);
        scanner.scan();
        
        Set result = new TreeSet();
        String[] files = scanner.getIncludedFiles(); 
        List list = Arrays.asList(files);
        Transformer transformer = new Transformer() {
            public Object transform(Object element) {
                if (element != null) {
                    String str = (String) element;
                    return str.replace('\\', '/');
                }

                return element;
            }
        };
        
        CollectionUtils.transform(list, transformer);
        result.addAll(list);
        
        if (logger.isDebugEnabled()) {
            logger.debug("使用模式["
                + pattern
                + "在目录["
                + rootDir
                + "]下找到资源"
                + DebugUtils.convertMultiLines(list));
            }
        
        return result;
    }

    protected Set findResourcesInJar(String jarFile, String pattern) {
        JarFile file = null;
        Set result = new TreeSet();
        try {
            file = new JarFile(jarFile);
        } catch (IOException e) {
            String msg = "打开jar文件[" + jarFile + "]错误";
            
            if (logger.isInfoEnabled()) {
                logger.info(msg + "," + e.getMessage(), e);
            }
            
            if (isThrowsException()) {
                IllegalStateException ex = new IllegalStateException(msg);
                ex.initCause(e);
                throw ex;
            }
        }
        
        Enumeration enumeration = file.entries();
        while (enumeration.hasMoreElements()) {
            JarEntry entry = (JarEntry) enumeration.nextElement();
            String name = entry.getName();
            if (pathMatcher.match(pattern, name)) {
                result.add(name);
                if (logger.isDebugEnabled()) {
                    logger.debug("在文件["
                        +  jarFile
                        + "]中找到匹配模式["
                        + pattern 
                        + "]的资源["
                        + name
                        + "]");
                }
            }
        }

        return result;
    }

    public boolean isPattern(String resourceName) {
        if (StringUtils.isBlank(resourceName)) {
            return false;
        }

        return pathMatcher.isPattern(resourceName);
    }
    
    protected boolean matches(String resourceName, String pattern) {
        String[] patterns = pattern.split("\\s*,\\s*");
        boolean match = false;
        for (int i = 0; !match && (i < patterns.length); i++) {
            match ^= pathMatcher.match(patterns[i], resourceName);
        }
        
        return match;
    }
}
