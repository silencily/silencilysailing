package net.silencily.sailing.common.fileload;

import java.util.ArrayList;
import java.util.List;

import net.silencily.sailing.common.fileupload.VfsUploadFile;


/**
 * @author wenjb
 *
 */
public class FileLoadBeanPlus extends FileLoadBean{
	

	private List files=new ArrayList();
	public List getFiles() {
		return files;
	}

	public void setFiles(List files) {
		this.files = files;
	}
	
	public VfsUploadFile getFiles(int i) {
		
		while (files.size() < i + 1) {
			files.add(null);
		}
		VfsUploadFile vf =  (VfsUploadFile)files.get(i);
		if(null == vf)
		{
			vf = new VfsUploadFile();
			files.add(vf);
		}
		return vf;
	}
}
