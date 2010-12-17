package com.power.vfs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.power.vfs.support.DefaultPathNormalizer;
import com.power.vfs.support.PathNormalizer;

import net.silencily.sailing.exception.UnexpectedException;
import net.silencily.sailing.utils.PlatformSelectorUtils;

public class FileSystemFileObjectManager implements
		ConfigurableFileObjectManager {

	private static Log logger;
	private static final int BUFFER_SIZE = 1024;
	private String rootPath;
	private Map rootPaths;
	protected FileObject root;
	private PathNormalizer pathNormalizer;

	static {
		logger = LogFactory
				.getLog(com.power.vfs.FileSystemFileObjectManager.class);
	}

	private class CharacterReader implements ReadWriteContentCallback {

		public void execute(File file) throws IOException {
			reader = new BufferedReader(new FileReader(file));
		}

		public void release() throws IOException {
		}

		public Reader getReader() {
			return reader;
		}

		Reader reader;

		private CharacterReader() {
			super();
			reader = null;
		}

	}

	private class StreamReader implements ReadWriteContentCallback {

		public void execute(File file) throws IOException {
			in = new BufferedInputStream(new FileInputStream(file));
		}

		public void release() throws IOException {
		}

		public InputStream getInputStream() {
			return in;
		}

		private InputStream in;

		private StreamReader() {
			super();
		}

	}

	private static interface ReadWriteContentCallback {

		public abstract void execute(File file) throws IOException;

		public abstract void release() throws IOException;
	}

	public FileSystemFileObjectManager() {
		pathNormalizer = new DefaultPathNormalizer(logger);
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public void setRootPaths(Map rootPaths) {
		this.rootPaths = rootPaths;
	}

	public FileObject find(String name) {
		File file = existsInternal(name);
		if (file != null) {
			return createFileObject(file);
		} else {
			FileObjectNoExistsException ex = new FileObjectNoExistsException(
					new FileObject(name));
			throw ex;
		}
	}

	public final FileObject[] list(String path) {
		File dir = existsInternal(path);
		if (dir == null) {
			FileObjectNoExistsException ex = new FileObjectNoExistsException(
					new FileObject(path));
			throw ex;
		}
		if (dir.isFile() || !dir.canRead()) {
			IllegalStateException ex = new IllegalStateException(
					"\u8981\u68C0\u7D22\u7684\u76EE\u5F55\u662F\u6587\u4EF6["
							+ path + "]");
			throw ex;
		}
		File files[] = dir.listFiles();
		List list = new ArrayList(files.length);
		for (int i = 0; i < files.length; i++)
			list.add(createFileObject(files[i]));

		return (FileObject[]) list.toArray(new FileObject[list.size()]);
	}

	public FileObject[] listWithResursion(String path) {
		List list = new ArrayList(300);
		FileObject files[] = list(path);
		for (int i = 0; i < files.length; i++)
			if (files[i].isDirectory())
				list.addAll(listResursion(files[i]));
			else
				list.add(files[i]);

		if (logger.isDebugEnabled())
			logger.debug("\u5728\u8F6C\u6210\u6570\u7EC4\u4E4B\u524DList\u4E2D\u7684\u5185\u5BB9"
					+ list);
		return (FileObject[]) list.toArray(new FileObject[list.size()]);
	}

	private List listResursion(FileObject dir) {
		List list = new ArrayList(50);
		FileObject files[] = list(dir.getName());
		for (int i = 0; i < files.length; i++)
			if (files[i].isDirectory())
				list.addAll(listResursion(files[i]));
			else
				list.add(files[i]);

		return list;
	}

	public FileObject create(FileObject object, final InputStream in) {
		validateParameterForNotNull(in,
				"\u6587\u4EF6\u5B9E\u4F53\u8F93\u5165\u6D41");
		ReadWriteContentCallback callback = new ReadWriteContentCallback() {
			public void execute(File file) throws IOException {
				FileOutputStream fout = new FileOutputStream(file, false);
				BufferedOutputStream out = new BufferedOutputStream(fout);
				byte buffer[] = new byte[1024];
				for (int len = 0; (len = in.read(buffer)) != -1;)
					out.write(buffer, 0, len);

				out.flush();
				out.close();
			}

			public void release() throws IOException {
				in.close();
			}
		};
		return writeFile(object, callback);
	}

	public FileObject create(FileObject object, final Reader reader) {
		validateParameterForNotNull(reader,
				"\u6587\u4EF6\u5B9E\u4F53\u8F93\u5165\u6D41");
		ReadWriteContentCallback callback = new ReadWriteContentCallback() {
			public void execute(File file) throws IOException {
				FileWriter fw = new FileWriter(file, false);
				BufferedWriter writer = new BufferedWriter(fw);
				char buffer[] = new char[1024];
				for (int len = 0; (len = reader.read(buffer)) != -1;)
					writer.write(buffer, 0, len);
				writer.flush();
				writer.close();
			}

			public void release() throws IOException {
				reader.close();
			}
		};
		return writeFile(object, callback);
	}

	public FileObject createDirectory(String path) {
		File dir = createFileFromFileObjectName(path);
		if (dir.exists()) {
			FileObjectAlreadyExistsException ex = new FileObjectAlreadyExistsException(
					new FileObject(path));
			if (logger.isDebugEnabled())
				logger.debug(
						"\u521B\u5EFA\u76EE\u5F55[" + dir.getAbsolutePath()
								+ "]\u65F6\u76EE\u5F55\u5DF2\u7ECF\u5B58\u5728",
						ex);
			throw ex;
		}
		if (!dir.mkdirs()) {
			FileObjectException ex = new FileObjectException(
					"\u4E0D\u80FD\u521B\u5EFA\u76EE\u5F55[" + path + "]");
			if (logger.isDebugEnabled())
				logger.debug("\u521B\u5EFA\u76EE\u5F55["
						+ dir.getAbsolutePath()
						+ "]\u65F6, File.mkdirs()\u8FD4\u56DEfalse");
			throw ex;
		} else {
			return createFileObject(dir);
		}
	}

	public final void delete(FileObject object) {
		delete(object, true);
	}

	public void delete(FileObject fo, boolean recursion) {
		validateParameterForNotNull(fo,
				"\u8981\u5220\u9664\u7684\u6587\u4EF6\u5B9E\u4F53");
		File file = existsInternal(fo.getName());
		if (file == null) {
			FileObjectNoExistsException ex = new FileObjectNoExistsException(fo);
			if (logger.isDebugEnabled())
				logger.debug("\u8981\u5220\u9664\u7684\u6587\u4EF6\u5B9E\u4F53["
						+ fo + "]\u4E0D\u5B58\u5728");
			throw ex;
		}
		if (!recursion && file.isDirectory() && file.list().length > 0) {
			FileObjectDirectoryNotEmptyException ex = new FileObjectDirectoryNotEmptyException(
					fo);
			if (logger.isDebugEnabled())
				logger.debug("\u8981\u5220\u9664\u7684\u76EE\u5F55\u975E\u7A7A["
						+ file.getAbsolutePath() + "]");
			throw ex;
		} else {
			deleteWithRecursion(file);
			return;
		}
	}

	public void update(FileObject object) {
		throw new UnsupportedOperationException(
				"\u6CA1\u6709\u5B9E\u73B0\u8FD9\u4E2A\u65B9\u6CD5");
	}

	public boolean exists(String name) {
		return existsInternal(name) != null;
	}

	private File existsInternal(String name) {
		validateParameterForNotNull(name,
				"\u68C0\u7D22\u6587\u4EF6\u5B9E\u4F53\u7684\u6587\u4EF6\u540D");
		File file = createFileFromFileObjectName(name);
		if (!file.exists())
			return null;
		else
			return file;
	}

	private File createFileFromFileObjectName(String name) {
		name = pathNormalizer.normalize(name);
		FileObject fo = new FileObject(name);
		File file = new File(getFileObjectRealPath(fo), fo.getFileName());
		if (logger.isDebugEnabled())
			logger.debug("\u6839\u636E\u6587\u4EF6\u5B9E\u4F53\u540D\u79F0["
					+ name + "]\u521B\u5EFA\u7684File'path["
					+ file.getAbsolutePath() + "]");
		return file;
	}

	public void copy(String source, String target) {
		throw new UnsupportedOperationException(
				"\u6CA1\u6709\u5B9E\u73B0\u8FD9\u4E2A\u65B9\u6CD5");
	}

	public boolean rename(String source, String target) {
		File src = existsInternal(source);
		if (src == null)
			throw new FileObjectNoExistsException(new FileObject(source));
		File dest = existsInternal(target);
		if (dest != null)
			throw new FileObjectAlreadyExistsException(new FileObject(target));
		boolean ret = false;
		if (src.isFile()) {
			FileObject fo = new FileObject(target);
			if (!exists(fo.getPath()))
				createDirectory(fo.getPath());
			ret = src.renameTo(createFileFromFileObjectName(target));
		} else {
			FileObject files[] = listWithResursion(source);
			for (int i = 0; i < files.length; i++) {
				String originalPath = (String) files[i]
						.getProperty("REAL_PATH");
				String targetPath = originalPath.replaceFirst(source, target);
				if (logger.isDebugEnabled())
					logger.debug("\u6539\u540D\u6587\u4EF6[" + originalPath
							+ "]\u5230[" + targetPath + "]");
				File f1 = new File(originalPath);
				File f2 = new File(targetPath);
				if (f1.isDirectory()) {
					f2.mkdirs();
					continue;
				}
				File dir = new File(f2.getParent());
				if (!dir.exists())
					dir.mkdirs();
				f1.renameTo(f2);
			}

		}
		return ret;
	}

	public InputStream getBinaryContent(String path) {
		StreamReader sr = new StreamReader();
		readFile(path, sr);
		return sr.getInputStream();
	}

	public Reader getCharacterContent(String path) {
		CharacterReader reader = new CharacterReader();
		readFile(path, reader);
		return reader.getReader();
	}

	public void read(String fileObjectName, final OutputStream out) {
		readFile(fileObjectName, new ReadWriteContentCallback() {

			public void execute(File file) throws IOException {
				FileInputStream fin = new FileInputStream(file);
				BufferedInputStream in = new BufferedInputStream(fin);
				int len = 0;
				byte buf[] = new byte[1024];
				while ((len = in.read(buf)) != -1)
					out.write(buf, 0, len);
				in.close();
			}

			public void release() throws IOException {
			}
		});
	}

	public void read(String fileObjectName, final Writer writer) {
		readFile(fileObjectName, new ReadWriteContentCallback() {

			public void execute(File file) throws IOException {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				int len = 0;
				char buf[] = new char[1024];
				while ((len = reader.read(buf)) != -1)
					writer.write(buf, 0, len);
				reader.close();
			}

			public void release() throws IOException {
			}
		});
	}

	protected void fetchRootPath() {
		if (rootPaths != null && rootPaths.size() > 0) {
			String name = PlatformSelectorUtils.selectOsName();
			if (!rootPaths.containsKey(name)) {
				String msg = "\u914D\u7F6E\u6587\u4EF6\u4E2D\u6CA1\u6709\u914D\u7F6E\u5F53\u524D\u8FD0\u884C\u7684\u64CD\u4F5C\u7CFB\u7EDF["
						+ name
						+ "]\u7684\u865A\u62DF\u6587\u4EF6\u6839\u76EE\u5F55";
				logger.error(msg);
				throw new FileObjectException("\u914D\u7F6E\u9519\u8BEF:" + msg);
			}
			rootPath = (String) rootPaths.get(name);
		}
		if (rootPath == null) {
			String msg = "\u6CA1\u6709\u914D\u7F6E\u865A\u62DF\u6587\u4EF6\u6839\u76EE\u5F55";
			logger.error(msg);
			throw new FileObjectException("\u914D\u7F6E\u9519\u8BEF:" + msg);
		} else {
			return;
		}
	}

	private String getFileObjectRealPath(FileObject fo) {
		String rootRealPath = (String) root.getProperties().get("REAL_PATH");
		String path = fo.getPath();
		String realPath = null;
		if (!"/".equals(path))
			realPath = rootRealPath + path;
		else
			realPath = rootRealPath;
		if (logger.isDebugEnabled())
			logger.debug("\u6587\u4EF6\u5B9E\u4F53" + fo
					+ "\u5728\u64CD\u4F5C\u7CFB\u7EDF\u5B9E\u9645\u8DEF\u5F84["
					+ realPath + "]");
		return realPath;
	}

	private FileObject createFileObject(File file) {
		FileObject fo = new FileObject();
		String realPath = null;
		try {
			realPath = pathNormalizer.normalize(file.getCanonicalPath());
		} catch (IOException e) {
			String msg = "\u83B7\u53D6\u6587\u4EF6\u8DEF\u5F84\u540D\u79F0\u9519\u8BEF";
			logger.error(msg, e);
			throw new FileObjectException(msg, e);
		}
		String rootRealPath = (String) root.getProperties().get("REAL_PATH");
		if (!realPath.startsWith(rootRealPath))
			throw new IllegalStateException(
					"\u6587\u4EF6["
							+ realPath
							+ "]\u4E0D\u5728\u865A\u62DF\u6587\u4EF6\u7684\u6839\u76EE\u5F55["
							+ rootRealPath + "]\u4E0B");
		String name = realPath.substring(rootRealPath.length());
		fo.setName(name);
		fo.addProperty("REAL_PATH", realPath);
		fo.addProperty("LAST_MODIFIED", new Date(file.lastModified()));
		fo.addProperty("SIZE", new Long(file.length()));
		if (file.isDirectory())
			fo.setType("directory");
		return fo;
	}

	private void validateParameterForNotNull(Object param, String msg) {
		if (param == null || (param instanceof String)
				&& StringUtils.isBlank((String) param))
			throw new IllegalArgumentException("\u8F93\u5165\u53C2\u6570["
					+ msg + "]\u662F\u7A7A\u503C");
		else
			return;
	}

	private void readFile(String path, ReadWriteContentCallback callback) {
		File file = existsInternal(path);
		if (file == null) {
			FileObjectNoExistsException ex = new FileObjectNoExistsException(
					new FileObject(path));
			throw ex;
		}
		if (file.isDirectory()) {
			FileObjectException ex = new FileObjectException(
					"\u8981\u8BFB\u53D6\u7684\u6587\u4EF6\u5B9E\u4F53\u662F\u76EE\u5F55["
							+ path + "]");
			throw ex;
		}
		if (!file.canRead()) {
			FileObjectException ex = new FileObjectException(
					"\u6587\u4EF6\u5B9E\u4F53[" + path
							+ "]\u6CA1\u6709\u8BFB\u6743\u9650");
			throw ex;
		}
		try {
			callback.execute(file);
		} catch (IOException e) {
			String msg = "\u8BFB\u53D6\u6587\u4EF6\u5B9E\u4F53[" + path
					+ "]\u7684\u5185\u5BB9\u53D1\u751F\u9519\u8BEF";
			FileObjectException ex = new FileObjectException(msg, e);
			if (logger.isInfoEnabled())
				logger.info(msg, e);
			throw ex;
		} finally {
			try {
				callback.release();
			} catch (IOException e) {
				if (logger.isWarnEnabled())
					logger.warn(
							"\u8BFB\u53D6\u6587\u4EF6\u5B9E\u4F53\u91CA\u653E\u8D44\u6E90\u9519\u8BEF",
							e);
			}
		}
	}

	private FileObject writeFile(FileObject fo,
			ReadWriteContentCallback callback) {
		File file = createFileFromFileObjectName(fo.getName());
		if (file.exists()) {
			FileObjectAlreadyExistsException ex = new FileObjectAlreadyExistsException(
					fo);
			if (logger.isDebugEnabled())
				logger.debug(
						"\u521B\u5EFA\u6587\u4EF6\u5B9E\u4F53\u9519\u8BEF", ex);
			throw ex;
		}
		try {
			File dir = new File(getFileObjectRealPath(fo));
			if (!dir.exists())
				dir.mkdirs();
			if (file.createNewFile()) {
				callback.execute(file);
			} else {
				if (logger.isInfoEnabled())
					logger.info("\u521B\u5EFA\u6587\u4EF6\u9519\u8BEF: \u8C03\u7528File.createNewFile()\u8FD4\u56DEfalse");
				throw new IOException("\u4E0D\u80FD\u521B\u5EFA\u6587\u4EF6");
			}
		} catch (IOException e) {
			FileObjectException ex = new FileObjectException(fo, e);
			if (logger.isInfoEnabled())
				logger.info("\u521B\u5EFA\u6587\u4EF6[" + fo + "]\u9519\u8BEF",
						e);
			throw ex;
		} finally {
			try {
				callback.release();
			} catch (IOException e) {
				if (logger.isInfoEnabled())
					logger.info(
							"\u5199\u6587\u4EF6\u5B9E\u4F53\u5230\u64CD\u4F5C\u7CFB\u7EDF\u65F6\u9519\u8BEF",
							e);
			}
		}
		return createFileObject(file);
	}

	private void deleteWithRecursion(File dir) {
		if (dir.isFile()) {
			boolean ret = dir.delete();
			if (!ret) {
				FileObjectException ex = new FileObjectException(
						"\u4E0D\u80FD\u5220\u9664\u6587\u4EF6\u5B9E\u4F53["
								+ dir.getName() + "]");
				if (logger.isInfoEnabled())
					logger.info("\u5220\u9664\u6587\u4EF6\u5B9E\u4F53["
							+ dir.getAbsolutePath()
							+ "]\u65F6,\u8C03\u7528File.delete()\u8FD4\u56DEfalse");
				throw ex;
			}
		} else {
			deleteRecursion(dir);
		}
	}

	private void deleteRecursion(File dir) {
		File files[] = dir.listFiles();
		for (int i = 0; i < files.length; i++)
			if (files[i].isDirectory())
				deleteRecursion(files[i]);
			else
				files[i].delete();

		dir.delete();
	}

	public void configRoot() {
		configRoot((String) null);
	}

	public void configRoot(String location) {
		fetchRootPath();
		if (StringUtils.isNotBlank(location)) {
			String subroot = pathNormalizer.normalize(location);
			if (!"/".equals(subroot))
				rootPath += subroot;
			if (logger.isDebugEnabled())
				logger.debug("\u865A\u62DF\u6587\u4EF6\u6839\u76EE\u5F55\u662F["
						+ rootPath + "]");
		}
		File file = new File(rootPath);
		if (!file.exists()) {
			if (!file.mkdirs()) {
				String msg = "\u4E0D\u80FD\u521B\u5EFA\u865A\u62DF\u6587\u4EF6\u7CFB\u7EDF\u6839\u76EE\u5F55["
						+ rootPath + "]";
				logger.error(msg);
				throw new FileObjectException(msg);
			}
		} else if (!file.isDirectory()) {
			String msg = "\u6307\u5B9A\u7684\u865A\u62DF\u6587\u4EF6\u6839\u76EE\u5F55\u662F\u6587\u4EF6["
					+ rootPath + "]";
			logger.error(msg);
			throw new FileObjectException(msg);
		}
		root = new FileObject("/", "directory");
		String path = null;
		try {
			path = file.getCanonicalPath();
			path = pathNormalizer.normalize(path);
		} catch (IOException e) {
			String msg = "\u4E0D\u80FD\u83B7\u5F97\u6587\u4EF6\u5B9E\u4F53\u7684\u64CD\u4F5C\u7CFB\u7EDF\u8DEF\u5F84,"
					+ e.getMessage();
			logger.error(msg, e);
			throw new FileObjectException(msg, e);
		}
		if (logger.isDebugEnabled())
			logger.debug("\u865A\u62DF\u6587\u4EF6\u7CFB\u7EDF\u6839\u76EE\u5F55\u8DEF\u5F84["
					+ path + "]");
		root.addProperty("REAL_PATH", path);
	}

	public int zip(String dirName, OutputStream out) {
		validateParameterForNotNull(dirName,
				"\u8981\u538B\u7F29\u7684\u6587\u4EF6\u5B9E\u4F53\u540D\u79F0\u662F\u7A7A");
		validateParameterForNotNull(out,
				"\u538B\u7F29\u6587\u4EF6\u8F93\u51FA\u6D41\u662Fnull");
		if (!exists(dirName))
			throw new IllegalArgumentException(
					"\u6307\u5B9A\u7684\u6587\u4EF6\u5B9E\u4F53[" + dirName
							+ "]\u4E0D\u5B58\u5728");
		FileObject fo = find(dirName);
		ZipOutputStream zout = new ZipOutputStream(out);
		int count = 0;
		try {
			if (!fo.isDirectory()) {
				zipFile(zout, fo);
				count++;
			} else {
				FileObject files[] = listWithResursion(dirName);
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory())
						zout.putNextEntry(new ZipEntry(files[i].getName() + "/"));
					else
						zipFile(zout, files[i]);
					count++;
				}

			}
			zout.flush();
			zout.close();
		} catch (IOException e) {
			try {
				zout.finish();
			} catch (IOException ex) {
			}
			throw new UnexpectedException(
					"\u538B\u7F29\u6587\u4EF6\u5B9E\u4F53[" + dirName
							+ "]\u9519\u8BEF", e);
		}
		return count;
	}

	private void zipFile(ZipOutputStream zout, FileObject fo)
			throws IOException {
		ZipEntry entry = new ZipEntry(fo.getName());
		zout.putNextEntry(entry);
		read(fo.getName(), zout);
		zout.closeEntry();
	}
}
