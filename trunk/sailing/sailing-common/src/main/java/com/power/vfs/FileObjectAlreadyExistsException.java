package com.power.vfs;

public class FileObjectAlreadyExistsException extends FileObjectException{
	public FileObjectAlreadyExistsException(FileObject fo)
    {
        super("\u6587\u4EF6\u5B9E\u4F53[" + fo.getName() + "]\u5DF2\u7ECF\u5B58\u5728", new IllegalStateException(fo.getName()));
    }
}
