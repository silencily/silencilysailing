package net.silencily.sailing.common.fileload.web;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.common.fileupload.VfsUploadFile;
import net.silencily.sailing.common.fileupload.VfsUploadFiles;
import net.silencily.sailing.struts.DispatchActionPlus;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


/**
 * @author zhaoyf
 *
 */
public class FileLoadAction extends DispatchActionPlus {

	public ActionForward upload(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return null;
	}

	public ActionForward list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try
		{
			VfsUploadFiles f = new VfsUploadFiles("mysample");
			f.read();
			VfsUploadFile[] vufs = f.getFiles();
			//UpLoadForm theForm = (UpLoadForm) form;
			//Map files = new HashMap();
			String[] files = new String[vufs.length];
			for(int i=0;i<vufs.length;i++)
			{
				files[i] = vufs[i].getFileName();
			}
			//theForm.setOldFiles(files);
			request.setAttribute("files", files);
			//request.setAttribute("test", "ok");
			//request.g
			
		}
		catch(Exception e)
		{
			System.err.print(e);
		}
		
		return mapping.findForward("display");
		//return null;
	}
	public ActionForward download(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		////VfsUploadFiles files = new VfsUploadFiles("");
		////files.read();
		
		String filename = request.getParameter("filename");
		if(null != filename) {
			int index = filename.lastIndexOf("/");
			if (index != -1) {
				String filepath = filename.substring(0, index);
				String imgName = filename.substring(index + 1);
				VfsUploadFiles files = new VfsUploadFiles(filepath);
				files.read();
//				BASE64Decoder decoder = new BASE64Decoder(); 
//				byte[] b = decoder.decodeBuffer(filename); 
//				filename = new String(b); 

				VfsUploadFile[] vfsfiles = files.getFiles();
				for(int i=0;i<vfsfiles.length;i++)
				{
					VfsUploadFile file = vfsfiles[i];
					//System.out.print(file.getFileName());
					if(file.getFileName().equalsIgnoreCase(imgName))
					{
						//response.setContentType(file.getContentType(getServlet().getServletContext()));
						response.setContentType("application/octet-stream"); 
						response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(file.getFileName(), "utf-8"));
						files.output(file.getFileName(), response.getOutputStream());
						break;
					}
				}
			}
		}
		return null;
	
	}
}
