package net.silencily.sailing.common.fileload.service;

import java.io.OutputStream;

import net.silencily.sailing.common.fileload.FileLoadBean;


/**
 * @author zhaoyf
 *
 */
public interface FileLoadService {

	public static final String SERVICE_NAME = "common.FileLoadService";
	
	public String uploadFile(FileLoadBean flb);
	
	public String remove(FileLoadBean flb);
	
	public String download(FileLoadBean flb,OutputStream os);
}
