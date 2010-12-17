package com.power.vfs;

public class FileObjectNoExistsException extends FileObjectException {
	public FileObjectNoExistsException(FileObject fo) {
		super("\u6587\u4EF6\u5B9E\u4F53[" + fo.getName()
				+ "]\u4E0D\u5B58\u5728", new FileObjectException(fo.getName()));
		setFileObject(fo);
	}
}
