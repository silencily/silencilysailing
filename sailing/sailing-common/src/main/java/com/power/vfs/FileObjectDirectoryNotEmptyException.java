package com.power.vfs;

public class FileObjectDirectoryNotEmptyException extends FileObjectException{
	public FileObjectDirectoryNotEmptyException(FileObject fo)
    {
        super("\u8981\u5220\u9664\u7684\u76EE\u5F55[" + fo.getName() + "]\u975E\u7A7A", new IllegalStateException(fo.getName()));
    }
}
