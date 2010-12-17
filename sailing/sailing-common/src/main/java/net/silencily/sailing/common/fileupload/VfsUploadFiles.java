package net.silencily.sailing.common.fileupload;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.silencily.sailing.container.ServiceProvider;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

import com.power.vfs.FileObject;
import com.power.vfs.FileObjectManager;
import com.power.vfs.FileObjectManagerFactory;


/**
 * <code>vfs</code>实现的文件上传管理类, 这个类是具有状态的, 通常用于一个上传附件的<code>form</code>
 * 的属性, <b>需要注意</b><code>wangz</code>在重构到<code>public</code>项目时修改了方法
 * {@link #createFileObjectInUploadeingList()}, 导致在上传处理后修改了这个类的内部状态,
 * 在典型的使用这个类作为<code>form-bean</code>的一个属性执行修改操作时, 需要再一次调用
 * {@link #read()}方法刷新状态(编程最佳实践之一:在把一个类作为参数执行一个方法时避免修改
 * 其内部状态)
 * @author Scott Captain
 * @since 2006-6-3
 * @version $Id: VfsUploadFiles.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 * TODO: 修复上述的<code>bug</code>
 */
public class VfsUploadFiles implements UploadFiles {
    
    /** 设置<code>vfs</code>检索路径的标志 */
    private boolean mounted = false;
    
    private String errorMsg = "没有为" + getClass().getName() + "]设置VFS检索路径,无法操作上传的文件";
    
    private String filePath;
    
    private VfsUploadFile[] files;
    
    private FileObjectManager fileObjectManager;
    
    public VfsUploadFiles() {}
    
    public VfsUploadFiles(String filePath) {
        setFilePath(filePath);
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
        fileObjectManager = getFileObjectManager();
        mounted = true;
    }

    public VfsUploadFile[] getFiles() {
        return files;
    }

    public VfsUploadFile getFiles(int index) {
        if (files == null) {
            files = new VfsUploadFile[index + 1];
            files[index] = new VfsUploadFile();
        } else if (files.length <= index) {
            VfsUploadFile[] nf = new VfsUploadFile[index + 1];
            System.arraycopy(files, 0, nf, 0, files.length);
            nf[index] = new VfsUploadFile();
            files = nf;
        }
        return (VfsUploadFile) files[index];
    }
    
    public void setFiles(VfsUploadFile[] files) {
        this.files = files;
    }

    public void setFiles(int index, VfsUploadFile file) {
        getFiles(index);
        if (file != null && file.isUploadFile()) {
            files[index] = file;
        }
    }

    public void read() {
        if (!mounted) {
            throw new IllegalStateException(errorMsg);
        }
        
        if (fileObjectManager.exists("/")) {
            FileObject[] fos = fileObjectManager.list("/");
            files = new VfsUploadFile[fos.length];
            for (int i = 0; i < fos.length; i++) {
                files[i] = new VfsUploadFile(fos[i]);
            }
        }
    }
    
    public UploadFile read(String fileName) {
        if (!mounted) {
            throw new IllegalStateException(errorMsg);
        }
        
        try {
            FileObject fo = fileObjectManager.find(fileName);
            return new VfsUploadFile(fo);
        } catch (Exception e) {
            IllegalStateException ex = new IllegalStateException(e.getMessage());
            ex.initCause(e);
            throw ex;
        }
    }

    public void write() {
        if (!mounted) {
            throw new IllegalStateException(errorMsg);
        }
        if (files == null || files.length == 0) {
            if (fileObjectManager.exists("/")) {
                FileObject[] fos = fileObjectManager.list("/");
                for (int i = 0; i < fos.length; i++) {
                    fileObjectManager.delete(fos[i]);
                }
            }
        } else {
            shrinkNullElements();
            removeFileObjectsNotInUploadedList();
            createFileObjectInUploadeingList();
        }
    }
    
    public FileObjectManager getFileObjectManager() {
        if (fileObjectManager == null) {
        	FileObjectManagerFactory factory = (FileObjectManagerFactory) ServiceProvider.getService("FileObjectManagerFactory");
            fileObjectManager = factory.getFileObjectManager(filePath);
        }
        
        return fileObjectManager;
    }
    
    protected FileObjectManager getFileObjectManager(String root) {
        FileObjectManagerFactory factory = (FileObjectManagerFactory) ServiceProvider.getService("FileObjectManagerFactory");
        return factory.getFileObjectManager(root);
    }
    
    /**
     * 删除不在<code>files</code>的已经上传文件列表中的文件, 已经上传的文件必须通过<code>form</code>
     * 写回来, 已经上传的文件就是<code>UploadFile.isUploadFile() == false</code>的那些
     * 文件
     */
    private void removeFileObjectsNotInUploadedList() {
        FileObject[] fos = null;
        if (fileObjectManager.exists("/")) {
            fos = fileObjectManager.list("/");
        } else {
            return;
        }
        if (fos.length > 0) {
            List list = new ArrayList(files.length);
            list.addAll(Arrays.asList(files));
            Collection uploaded = CollectionUtils.select(list, UPLOADED_FILE_PREDICATE);
            for (int i = 0; i < fos.length; i++) {
                final FileObject file = fos[i];
                if (!CollectionUtils.exists(uploaded, new Predicate() {
                    public boolean evaluate(Object element) {
                        UploadFile uf = (UploadFile) element;
                        return uf.getFileName() != null && uf.getFileName().equals(file.getFileName());
                    }})
                ) {
                    fileObjectManager.delete(file);
                }
            }
        }
    }
    
    /**
     * 把上传的文件中保存到<code>vfs</code>中, 如果出现同名的文件就删除这个文件用新上传的
     * 文件代替
     */
    private void createFileObjectInUploadeingList() {
        List list = new ArrayList(files.length);
    	for (int i = 0; i < files.length; i++) {
    		UploadFile file = files[i];
            if (file.isUploadFile()) {
                String fileName = eliminate(file.getFileName());
                FileObject fo = new FileObject(fileName);
                if (fileObjectManager.exists(fileName)) {
                    fileObjectManager.delete(fo);
                }
                fileObjectManager.create(fo, file.getContent());
                list.add(file);
            }
            file.destory();
    	}
    	files = (VfsUploadFile[]) list.toArray(new VfsUploadFile[list.size()]);
    }
    
    /* 从文件名称中排除 & 符号, 免得下载时错误, 应该在 vfs 中有这样的处理吧 */
    private String eliminate(String str) {
        return str.replace('&', '_');
    }
    
    /**
     * 从上传文件列表中排除掉<code>null</code>, 虽然前端使用了<code>shrink</code>机制,
     * 但是当把这个实例放在业务实体中时仍然可能出现包含<code>null</code>的情况
     */
    private void shrinkNullElements() {
        Collection list = new ArrayList(files.length);
        list.addAll(Arrays.asList(files));
        list = CollectionUtils.select(list, new Predicate() {
            public boolean evaluate(Object element) {
                return element != null;
            }});
        files = (VfsUploadFile[]) list.toArray(new VfsUploadFile[list.size()]);
    }

    private static UploadedFilePredicate UPLOADED_FILE_PREDICATE = new UploadedFilePredicate();
    
    /**
     * 挑选出<b>已经</b>上传的文件的<code>Predicate</code>, 已经上传的文件仅仅有文件名
     * 而没有内容, 同时过滤掉可能存在的空文件名
     */
    private static class UploadedFilePredicate implements Predicate {
        public boolean evaluate(Object element) {
            UploadFile file = (UploadFile) element;
            return file != null && !file.isUploadFile() && StringUtils.isNotBlank(file.getFileName());
        }
    }

    public void output(String fileName, OutputStream out) {
        if (!mounted) {
            throw new IllegalStateException(errorMsg);
        }

        fileObjectManager.read(fileName, out);
    }

    public void delete() {
        if (!mounted) {
            throw new IllegalStateException(errorMsg);
        }
        int pos = this.filePath.lastIndexOf('/');
        if (pos > -1) {
            String path = this.filePath.substring(0, pos);
            String name = this.filePath.substring(pos + 1);
            FileObjectManager fm = getFileObjectManager(path);
            if (fm.exists(name)) {
                fm.delete(fm.find(name), true);
                /* 删除了这个目录后这个实例不能再使用 */
                mounted = false;
            }
        }
    }

    public void delete(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            throw new NullPointerException("没有指定要删除的文件名");
        }
        if (!mounted) {
            throw new IllegalStateException(errorMsg);
        }
        if (fileObjectManager.exists(fileName)) {
            fileObjectManager.delete(fileObjectManager.find(fileName));
        } else {
            throw new IllegalArgumentException("要删除的文件[" + fileName + "]不存在");
        }
    }
}
