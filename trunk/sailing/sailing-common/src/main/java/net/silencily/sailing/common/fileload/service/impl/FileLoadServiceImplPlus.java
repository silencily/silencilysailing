package net.silencily.sailing.common.fileload.service.impl;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.common.fileload.FileLoadBean;
import net.silencily.sailing.common.fileload.domain.TblCmnUpLoadFile;
import net.silencily.sailing.common.fileload.service.FileLoadServicePlus;
import net.silencily.sailing.common.fileupload.VfsUploadFile;
import net.silencily.sailing.common.fileupload.VfsUploadFiles;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.context.BusinessContext;
import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.struts.BaseFormPlus;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;


/**
 * @author wenjb
 * 
 */
public class FileLoadServiceImplPlus implements FileLoadServicePlus {

	public String uploadFile(FileLoadBean flb) {
		VfsUploadFiles f = new VfsUploadFiles(flb.getAbsPath());
		if (null != flb.getFiles()) {
			VfsUploadFile[] vfsfiles = new VfsUploadFile[flb.getFiles().size()];
			int index = 0;
			for (Iterator it = flb.getFiles().iterator(); it.hasNext();) {
				VfsUploadFile file = (VfsUploadFile) it.next(); // 获取上传文件的值
				vfsfiles[index++] = file;

			}
			f.setFiles(vfsfiles);
			f.write();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.qware.common.fileload.service.FileLoadService#download(com.qware.common.fileload.FileLoadBean,
	 *      java.io.OutputStream)
	 */
	public String download(FileLoadBean flb, OutputStream os) {
		// 暂不提供此方法，文件下载的方法，在FileLoadActionPlus中已经提供
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.qware.common.fileload.service.FileLoadService#remove(com.qware.common.fileload.FileLoadBean)
	 */
	public String remove(FileLoadBean flb) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * 根据当前类路径和OID，查询得到全部的存储文件
	 */
	public String initFileList(BaseFormPlus baseFormPlus, Class c, String oid) {
		List tblCmnUpLoadFilesTemp = new ArrayList();
		ContextInfo.concealQuery();
		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnUpLoadFile.class);
		String pathTemp = "";
		pathTemp = c.getName().replace('.', '/') + "/" + oid;
		dc.add(Restrictions.eq("savePath", pathTemp));
		dc.add(Restrictions.eq("delFlg", "0"));
		dc.addOrder(Order.desc("createdTime"));
		tblCmnUpLoadFilesTemp = ((CommonService) ServiceProvider
				.getService(CommonService.SERVICE_NAME)).findByCriteria(dc);
		// 循环处理LIST，使LIST中不出现文件不在的对象
		// 获取文件绝对路径
		String filepath = "";
		filepath = BusinessContext.getUploadFilesPath();
		filepath = filepath + "/" + pathTemp;
		List result = new ArrayList();
		for (int i = 0; i < tblCmnUpLoadFilesTemp.size(); i++) {
			try {
				String temp = filepath
						+ "/"
						+ ((TblCmnUpLoadFile) tblCmnUpLoadFilesTemp.get(i))
								.getName();
				File file = new File(temp);
				if (file.exists()) {
					result.add((TblCmnUpLoadFile) tblCmnUpLoadFilesTemp.get(i));
				}
			} catch (Exception e) {
				//
			}
		}
		baseFormPlus.setTblCmnUpLoadFiles(result);
		return null;
	}

	public String saveFileList(BaseFormPlus baseFormPlus, Class c, String oid) {

		// 保存可编辑列表中的文件

		// 设置上传路径，存储文件描述信息
		String pathTemp = c.getName().replace('.', '/') + "/" + oid;
		List tblCmnUpLoadFilesTemp = baseFormPlus.getTblCmnUpLoadFiles();
		FileLoadBean flb = baseFormPlus.getFilesBean();
		flb.setAbsPath(pathTemp);

		for (int i = 0; i < tblCmnUpLoadFilesTemp.size(); i++) {
			//处理空白行保存异常
			if(tblCmnUpLoadFilesTemp.get(i) == null){
				continue;
			}
			TblCmnUpLoadFile tblCmnUpLoadFiletmep = (TblCmnUpLoadFile) tblCmnUpLoadFilesTemp
					.get(i);
			tblCmnUpLoadFiletmep.setSavePath(pathTemp);
			// 防止老的附件丢失
			((VfsUploadFile) flb.getFiles(i)).setFileName(tblCmnUpLoadFiletmep
					.getName());
			((VfsUploadFile) flb.getFiles(i)).setFileDesc(tblCmnUpLoadFiletmep
					.getDescrible());

			// 新增情况下的空行处理
			if (StringUtils.isNotBlank(tblCmnUpLoadFiletmep.getId())
					|| StringUtils.isNotBlank(tblCmnUpLoadFiletmep.getName())
					|| StringUtils.isNotBlank(tblCmnUpLoadFiletmep
							.getDescrible())) {
				// 保存文件描述信息
				if (StringUtils.isBlank(tblCmnUpLoadFiletmep.getId())) {
					tblCmnUpLoadFiletmep.setDelFlg("0");
				}
				((CommonService) ServiceProvider
						.getService(CommonService.SERVICE_NAME))
						.saveOrUpdate(tblCmnUpLoadFiletmep);
			}

		}

		// 开始上传文件
		if (flb.getFiles().size() > 0) {
			VfsUploadFiles f = new VfsUploadFiles(flb.getAbsPath());
			if (null != flb.getFiles()) {
				VfsUploadFile[] vfsfiles = new VfsUploadFile[flb.getFiles()
						.size()];
				int index = 0;
				for (Iterator it = flb.getFiles().iterator(); it.hasNext();) {
					VfsUploadFile file = (VfsUploadFile) it.next(); // 获取上传文件的值
					vfsfiles[index++] = file;

				}
				f.setFiles(vfsfiles);
				f.write();
			}
		}
		return initFileList(baseFormPlus, c, oid);
	}

	public String deleteLogicFileList(Class c,
			String oid) {

		List tblCmnUpLoadFilesTemp = new ArrayList();
		ContextInfo.concealQuery();
		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnUpLoadFile.class);
		String pathTemp = "";
		pathTemp = c.getName().replace('.', '/') + "/" + oid;
		dc.add(Restrictions.eq("savePath", pathTemp));
		dc.add(Restrictions.eq("delFlg", "0"));
		dc.addOrder(Order.desc("createdTime"));
		tblCmnUpLoadFilesTemp = ((CommonService) ServiceProvider
				.getService(CommonService.SERVICE_NAME)).findByCriteria(dc);
		// 循环处理LIST，使LIST中不出现文件不在的对象
		// 获取文件绝对路径
		String filepath = "";
		filepath = BusinessContext.getUploadFilesPath();
		filepath = filepath + "/" + pathTemp;
		for (int i = 0; i < tblCmnUpLoadFilesTemp.size(); i++) {
			((TblCmnUpLoadFile) tblCmnUpLoadFilesTemp.get(i)).setDelFlg("1");
			((CommonService) ServiceProvider
					.getService(CommonService.SERVICE_NAME))
					.saveOrUpdate((TblCmnUpLoadFile) tblCmnUpLoadFilesTemp
							.get(i));
		}
		return null;
	}
}
