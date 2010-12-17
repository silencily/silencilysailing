package com.power.vfs.support;

import com.power.vfs.FileObjectException;

public interface PathNormalizer {
	public abstract String normalize(String s)
    throws FileObjectException;

public static final char separator = 47;
}
