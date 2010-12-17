/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.common.transfer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.framework.transfer.exceptions.TransferException;
import net.silencily.sailing.framework.transfer.meta.FileType;
import net.silencily.sailing.framework.transfer.meta.TransferMetaData;
import net.silencily.sailing.framework.transfer.meta.TransferMetaDataFactory;
import net.silencily.sailing.framework.transfer.strategy.ProcessTransferRowContext;
import net.silencily.sailing.framework.transfer.strategy.ProcessTransferRowStrategy;
import net.silencily.sailing.utils.UUIDGenerator;
import net.silencily.sailing.utils.ZipUtils;

import org.springframework.util.Assert;

import com.power.vfs.FileObject;
import com.power.vfs.FileObjectManager;
import com.power.vfs.FileObjectManagerFactory;


/**
 * 分割导出文件 template, 提供了根据不同策略分组后导出文件, 然后压缩成 zip 文件的功能
 * @since 2006-7-16
 * @author java2enterprise
 * @version $Id: IncisionExportTemplate.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 */
public class IncisionExportTemplate {
	
	/** 用以保存用户导出文件使用的 vfs 根路径, 此目录可能需要做定时任务清除 */
	public static final String EXPORT_FILE_VFS_ROOT = "temporary";
		
	/**
	 * 得到导出文件需要的 vfs FileObjectManager
	 * @return fileObjectManager
	 */
	public static FileObjectManager getFileObjectManager() {
		FileObjectManagerFactory factory = (FileObjectManagerFactory) ServiceProvider.getService(FileObjectManagerFactory.SERVICE_NAME);
		return factory.getFileObjectManager(EXPORT_FILE_VFS_ROOT);
	}
	
	/**
	 * 将数据直接导出为excel（不压缩，因此要注意适应数据不太大情况，<10000行）
	 */
	public String exportExcel(
		List exportRows,
		ExportRowsGrouper exportRowsGrouper,
		String fileName)
		throws TransferException {
		return exportExcel(exportRows, exportRowsGrouper, TransferMetaDataFactory.getDefaultTransferMetaData(), FileType.XLS, fileName);
	}

	public String exportExcel(
		List exportRows, 
		ExportRowsGrouper exportRowsGrouper, 
		TransferMetaData metaData, 
		FileType fileType, 
		String fileName) 
		throws TransferException {
		
		File file = getExportFile(exportRows, exportRowsGrouper,metaData,fileType,fileName);
		try{
			//文件名后有0，因为只分成一组
			String fileFullName = getVfsTemporaryRealPath() + "/" + file.getName()+"/"+ fileName+"0.xls";
			File newFile = new File(fileFullName);
			FileObject fileObject = new FileObject();
			fileObject.setName(fileName + "0.xls");
			fileObject = getFileObjectManager().create(fileObject, new BufferedInputStream(new FileInputStream(newFile)));
			//删除临时文件
			deleteFile(file);
			return fileObject.getFileName();
		} catch (IOException e) {
			throw new TransferException("导出Excel文件时候出现错误", e);
		}
	}
	
	/**
	 * 导出数据并压缩成 zip 包存放到 vfs 中, 必要时将会分成多个文件, 分组的规则由参数 exportRowsGrouper 决定
	 * @param exportRows 需要导出的数据, list fill with {@link TransferExportRow}
	 * @param exportRowsGrouper 分组器
	 * @param distZipFileName 目标 zip 文件的名称, 只需要名字即可, 不需要路径和扩展名 ".zip"
	 * @return 导出后的 zip 文件在 vfs 中的路径
	 * @throws TransferException 如果导出过程中出现异常
	 */
	public String exportAndDoIncisionIfNecessarily(
		List exportRows, 
		ExportRowsGrouper exportRowsGrouper, 
		String distZipFileName) 
		throws TransferException {
		return exportAndDoIncisionIfNecessarily(exportRows, exportRowsGrouper, TransferMetaDataFactory.getDefaultTransferMetaData(), FileType.XLS, "exportFile", distZipFileName);
	}
	
	/**
	 * 导出数据并压缩成 zip 包存放到 vfs 中, 必要时将会分成多个文件, 分组的规则由参数 exportRowsGrouper 决定
	 * @param exportRows 需要导出的数据, list fill with {@link TransferExportRow}
	 * @param exportRowsGrouper 分组器
	 * @param metaData 导出元信息
	 * @param fileType 导出文件类型
	 * @param zipEntryName zip 包中的文件名称
	 * @param distZipFileName 目标 zip 文件的名称, 只需要名字即可, 不需要路径和扩展名 ".zip"
	 * @return 导出后的 zip 文件在 vfs 中的路径
	 * @throws TransferException 如果导出过程中出现异常
	 */
	public String exportAndDoIncisionIfNecessarily(
		List exportRows, 
		ExportRowsGrouper exportRowsGrouper, 
		TransferMetaData metaData, 
		FileType fileType, 
		String zipEntryName,  
		String distZipFileName) 
		throws TransferException {
				
		File file = getExportFile(exportRows, exportRowsGrouper,metaData,fileType,zipEntryName);
		// 将导出的文件压缩成 zip
		try {
			//String uniqueZipDirName = getVfsTemporaryRealPath() + "/" + new UUIDGenerator().generate();
			//File uniqueZipDir = new File(uniqueZipDirName);
			//uniqueZipDir.mkdir();
			String fullDistZipFileName = distZipFileName + ".zip";//uniqueZipDirName + "/" +
			ZipUtils.zipFile(file, fullDistZipFileName);
			File zipFile = new File(fullDistZipFileName);
			
			FileObject fileObject = new FileObject();
			fileObject.setName(distZipFileName + ".zip");
			fileObject = getFileObjectManager().create(fileObject, new BufferedInputStream(new FileInputStream(zipFile)));
			
			// 删除临时文件
			deleteFile(file);
			//deleteFile(uniqueZipDir);
			return fileObject.getFileName();
		} catch (IOException e) {
			throw new TransferException("导出文件错误", e);
		}
	}
	
	//得到导出的文件
	private File getExportFile(
		List exportRows, 
		ExportRowsGrouper exportRowsGrouper, 
		TransferMetaData metaData, 
		FileType fileType, 
		String zipEntryName)
		throws TransferException {		
		List groupedList = exportRowsGrouper.group(exportRows);
		
		File temporaryDir = new File(getVfsTemporaryRealPath());
		if (!temporaryDir.isDirectory()) {
			temporaryDir.mkdir();
		}
		File file = new File(getVfsTemporaryRealPath() + "/" + new UUIDGenerator().generate());
		if (!file.isDirectory()) {
			file.mkdir();
		}

		for (int i = 0; i < groupedList.size(); i++) {
			Object forEach = groupedList.get(i);
			Assert.isInstanceOf(List.class, forEach, " 请正确实现 " + ExportRowsGrouper.class);
			List groupList = (List) forEach;
			ProcessTransferRowStrategy strategy = ProcessTransferRowContext.getStrategy(fileType);
			
			OutputStream out = null;
			try {
				out = new FileOutputStream(file.getPath() + "/" + zipEntryName + i + fileType.getFileSuffix());			
				strategy.writeTransferRows2OutputStream(groupList, metaData, out);			
				out.flush();
				out.close();
			} catch (IOException e) {
				throw new TransferException("导出文件错误", e);
			}
		}
		return file;
	}
	
	private void deleteFile(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteFile(files[i]);
			}
			file.delete();
		} else {
			file.delete();
		}
	}
	
	public static String getVfsTemporaryRealPath() {
		FileObjectManager fileObjectManager  = getFileObjectManager();
		FileObject root = fileObjectManager.find("/");
		return (String) root.getProperty(FileObjectManager.KEY_REAL_PATH);
	}
	
	public static void main(String[] args) {
		System.out.println(getVfsTemporaryRealPath());
	}
}
