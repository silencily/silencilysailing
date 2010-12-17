package com.power.vfs;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public interface FileObjectManager {
	public abstract FileObject find(String s);

    public abstract FileObject[] list(String s);

    public abstract FileObject[] listWithResursion(String s);

    public abstract FileObject create(FileObject fileobject, InputStream inputstream);

    public abstract FileObject create(FileObject fileobject, Reader reader);

    public abstract FileObject createDirectory(String s);

    public abstract void delete(FileObject fileobject);

    public abstract void delete(FileObject fileobject, boolean flag);

    public abstract void update(FileObject fileobject);

    public abstract boolean exists(String s);

    public abstract void copy(String s, String s1);

    public abstract boolean rename(String s, String s1);

    public abstract InputStream getBinaryContent(String s);

    public abstract Reader getCharacterContent(String s);

    public abstract void read(String s, OutputStream outputstream);

    public abstract void read(String s, Writer writer);

    public abstract int zip(String s, OutputStream outputstream);

    public static final String OS_WINDOWS = "windows";
    public static final String OS_LINUX = "linux";
    public static final String KEY_REAL_PATH = "REAL_PATH";
    public static final String KEY_TYPE = "TYPE";
    public static final String KEY_SIZE = "SIZE";
    public static final String KEY_LAST_MODIFIED = "LAST_MODIFIED";
    public static final String TYPE_DIRECTORY = "directory";

}
