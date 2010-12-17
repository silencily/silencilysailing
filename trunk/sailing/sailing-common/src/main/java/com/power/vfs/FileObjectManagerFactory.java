package com.power.vfs;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class FileObjectManagerFactory {
	public static final String SERVICE_NAME = "FileObjectManagerFactory";
	private Map managers;

	public FileObjectManagerFactory() {
		managers = Collections.synchronizedMap(new HashMap());
	}

	public FileObjectManager getFileObjectManager(String location) {
		if (managers.containsKey(location))
			return (FileObjectManager) managers.get(location);
		else
			return getFileObjectManagerInternal(location);
	}

	protected synchronized FileObjectManager getFileObjectManagerInternal(
			String location) {
		ConfigurableFileObjectManager fom = lookupFileObjectManager();
		fom.configRoot(location);
		managers.put(location, fom);
		return fom;
	}

	protected abstract ConfigurableFileObjectManager lookupFileObjectManager();
}
