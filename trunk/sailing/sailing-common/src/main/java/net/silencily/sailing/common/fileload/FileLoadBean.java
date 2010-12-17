package net.silencily.sailing.common.fileload;

import java.util.ArrayList;
import java.util.List;

import net.silencily.sailing.common.fileupload.VfsUploadFile;



/**
 * @author zhaoyf
 *
 */
public class FileLoadBean {

	private String absPath;
	private List files=new ArrayList();
	public List getFiles() {
		return files;
	}

	public void setFiles(List files) {
		this.files = files;
	}
	
	public VfsUploadFile getFiles(int i) {
		
		VfsUploadFile vf =  (VfsUploadFile)files.get(i);
		if(null == vf)
		{
			vf = new VfsUploadFile();
			files.add(vf);
		}
		return vf;
	}
	
	private String fileName;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getAbsPath() {
		return absPath;
	}

	public void setAbsPath(String absPath) {
		this.absPath = absPath;
	}
}
