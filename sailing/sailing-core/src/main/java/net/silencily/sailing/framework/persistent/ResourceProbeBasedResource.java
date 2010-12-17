/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project framework
 */
package net.silencily.sailing.framework.persistent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.xml.sax.EntityResolver;

/**
 * 基于 {@link ResourceProbe} 的 Resource 实现, 与 {@link ClassPathResource} 的区别在于它可以搜索 jar 中的配置文件,
 * 如果 classpath 中有同名的配置文件, 则以 classpath 为准
 * @see ResourceProbe
 * @see ClassPathResource
 * @since 2006-9-8
 * @author java2enterprise
 * @version $Id: ResourceProbeBasedResource.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 */
public class ResourceProbeBasedResource extends AbstractResourceProbeBasedResource implements InitializingBean {
	
	/** 配置文件的路径, 从 classpath 根路径开始 */
	private String path;
	
	private String[] realPath;
	
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String pattern) {
		this.path = pattern;
	}

	protected EntityResolver getEntityResolver() {
		return null;
	}

	public String getDescription() {
		return "resource probe based resource [" + this.path + "]";
	}
	
	/**
	 * @see org.springframework.core.io.AbstractResource#getFilename()
	 */
	public String getFilename() throws IllegalStateException {
		if (realPath == null || realPath.length == 0) {
			return null;
		}
		return StringUtils.getFilename(realPath[0]);
	}
	
	/**
	 * 
	 * @see org.springframework.core.io.InputStreamSource#getInputStream()
	 */
	public InputStream getInputStream() throws IOException {
		InputStream is = null;
		if (realPath != null && realPath.length > 0) {
			is = ClassUtils.getDefaultClassLoader().getResourceAsStream(realPath[0]);
		}		
		if (is == null) {
			throw new FileNotFoundException(getDescription() + " cannot be opened because it does not exist");
		}
		return is;
	}

	
	
	/**
	 * @see org.springframework.core.io.AbstractResource#getFile()
	 */
	public File getFile() throws IOException {
		return ResourceUtils.getFile(getURL(), getDescription());
	}

	/**
	 * @see org.springframework.core.io.AbstractResource#getURL()
	 */
	public URL getURL() throws IOException {
		URL url = null;
		if (realPath != null && realPath.length > 0) {
			url = ClassUtils.getDefaultClassLoader().getResource(realPath[0]);
		}	
		if (url == null) {
			throw new FileNotFoundException(getDescription() + " cannot be resolved to URL because it does not exist");
		}
		return url;
	}

	private void init() {
		realPath = this.resourceProbe.search(path, null);
	}

	public void afterPropertiesSet() throws Exception {
        try {
            init();
        } catch (Exception e) {
            logAndThrows("配置错误", e);
        }
	}	
}
