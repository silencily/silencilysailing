package com.power.vfs;

import net.silencily.sailing.exception.BaseSystemException;

public class FileObjectException extends BaseSystemException{
	public FileObjectException()
    {
    }

    public FileObjectException(String msg)
    {
        super(msg, new IllegalStateException());
    }

    public FileObjectException(FileObject fo, Exception e)
    {
        super("\u5904\u7406\u6587\u4EF6\u5B9E\u4F53[" + fo.getName() + "]\u53D1\u751F\u5F02\u5E38", e);
        this.fo = fo;
    }

    public FileObjectException(FileObject fo, Exception e, String msg)
    {
        super(msg, e);
        this.fo = fo;
    }

    public FileObjectException(String msg, Exception e)
    {
        super(msg, e);
    }

    protected void setFileObject(FileObject fo)
    {
        this.fo = fo;
    }

    public FileObject getFileObject()
    {
        return fo;
    }

    private FileObject fo;

}
