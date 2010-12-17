package net.silencily.sailing.common.fileload.service.impl;

import java.io.OutputStream;
import java.util.Iterator;

import net.silencily.sailing.common.fileload.FileLoadBean;
import net.silencily.sailing.common.fileload.service.FileLoadService;
import net.silencily.sailing.common.fileupload.VfsUploadFile;
import net.silencily.sailing.common.fileupload.VfsUploadFiles;


/**
 * @author zhaoyf
 *
 */
public class FileLoadServiceImpl implements FileLoadService {

	/* (non-Javadoc)
	 * @see com.qware.common.fileload.service.impl.FileLoadService#uploadFile(com.qware.common.fileload.FileLoadBean)
	 */
	public String uploadFile(FileLoadBean flb) {
		// TODO Auto-generated method stub
		VfsUploadFiles f = new VfsUploadFiles(flb.getAbsPath());
		if(null!=flb.getFiles()){				
			VfsUploadFile[] vfsfiles = new VfsUploadFile[flb.getFiles().size()];
			int index= 0;
			for(Iterator it = flb.getFiles().iterator();it.hasNext();)
			{
				VfsUploadFile file = (VfsUploadFile)it.next();   //获取上传文件的值
				vfsfiles[index++]=file;
				
			}
			f.setFiles(vfsfiles);
			f.write();
		}		
		return null;
	}

	public String download(FileLoadBean flb,OutputStream os) {
		// TODO Auto-generated method stub
		return null;
	}

	public String remove(FileLoadBean flb) {
		// TODO Auto-generated method stub
		return null;
	}

}
