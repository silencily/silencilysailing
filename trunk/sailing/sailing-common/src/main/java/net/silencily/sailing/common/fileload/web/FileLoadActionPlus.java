package net.silencily.sailing.common.fileload.web;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.common.fileload.domain.TblCmnUpLoadFile;
import net.silencily.sailing.common.fileupload.VfsUploadFile;
import net.silencily.sailing.common.fileupload.VfsUploadFiles;
import net.silencily.sailing.container.ServiceProvider;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author wenjb
 *
 */
public class FileLoadActionPlus extends FileLoadAction {

	/*
	 * 根据路径和文件名称，处理下载
	 * */
	public ActionForward download(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		//id
		String id = request.getParameter("id");
		TblCmnUpLoadFile tblCmnUpLoadFile = (TblCmnUpLoadFile) ((CommonService) ServiceProvider
				.getService(CommonService.SERVICE_NAME)).load(TblCmnUpLoadFile.class, id);
		
		//文件名称和路径
		String imgName = tblCmnUpLoadFile.getName();
		String filepath = tblCmnUpLoadFile.getSavePath();
		
		
		if(null != imgName && null != filepath) {
			VfsUploadFiles files = new VfsUploadFiles(filepath);
			files.read();
			VfsUploadFile[] vfsfiles = files.getFiles();
			for(int i=0;i<vfsfiles.length;i++)
			{
				VfsUploadFile file = vfsfiles[i];
				if(file.getFileName().equalsIgnoreCase(imgName))
				{
					response.setContentType("application/octet-stream"); 
					response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(file.getFileName(), "utf-8"));
					files.output(file.getFileName(), response.getOutputStream());
					break;
				}
			}
		}
		return null;
	}
}
