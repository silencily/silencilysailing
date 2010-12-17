package com.power.vfs.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import com.power.vfs.FileObject;

public abstract class VfsUtils {
	public VfsUtils() {
	}

	public static FileObject[] filterOutDirectories(FileObject files[]) {
		return filter(files, true);
	}

	public static FileObject[] filterOutFiles(FileObject files[]) {
		return filter(files, false);
	}

	private static FileObject[] filter(FileObject files[],
			final boolean directory) {
		List result = new ArrayList(files.length);
		result.addAll(Arrays.asList(files));
		CollectionUtils.filter(result, new Predicate() {
			public boolean evaluate(Object element) {
				FileObject fo = (FileObject) element;
				return fo != null && fo.isDirectory() != directory;
			}
		});
		return (FileObject[]) result.toArray(new FileObject[result.size()]);
	}
}
