/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @since 2006-7-16
 * @author java2enterprise
 * @version $Id: ZipUtils.java,v 1.1 2010/12/10 10:54:21 silencily Exp $
 */
public abstract class ZipUtils {
	
	/**
	 * 压缩一个文件或目录到 zip 文件中, 如果 file 是一个目录, 将递归读取下面所有文件或目录并压缩到目标文件中
	 * @param file 来源目录或文件
	 * @param zipFileName 目的文件名, 应该是一个完整的路径名称, 例如 d:/test.zip
	 * @throws IOException 如果压缩过程中出现异常
	 */
	public static void zipFile(File file, String zipFileName) throws IOException {
		ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFileName)));
		innerZipFile(file, zipOutputStream, file.getName());
		zipOutputStream.finish();
		zipOutputStream.flush();
		zipOutputStream.close();
	}
	
	private static void innerZipFile(File file, ZipOutputStream zipOutputStream, String baseEntryName) throws IOException {
		if (file.isDirectory()) {
			baseEntryName += "/";
			zipOutputStream.putNextEntry(new ZipEntry(baseEntryName));
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				innerZipFile(files[i], zipOutputStream, baseEntryName + files[i].getName());
			}
		} else {
			zipOutputStream.putNextEntry(new ZipEntry(baseEntryName));
			InputStream in = new BufferedInputStream(new FileInputStream(file));
			byte[] bytes = new byte[1024];
			while (in.read(bytes) != -1) {
				zipOutputStream.write(bytes);
			}
			in.close();
		}
	}
}
