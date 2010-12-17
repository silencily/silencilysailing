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
 * <code>vfs</code>ʵ�ֵ��ļ��ϴ�������, ������Ǿ���״̬��, ͨ������һ���ϴ�������<code>form</code>
 * ������, <b>��Ҫע��</b><code>wangz</code>���ع���<code>public</code>��Ŀʱ�޸��˷���
 * {@link #createFileObjectInUploadeingList()}, �������ϴ�������޸����������ڲ�״̬,
 * �ڵ��͵�ʹ���������Ϊ<code>form-bean</code>��һ������ִ���޸Ĳ���ʱ, ��Ҫ��һ�ε���
 * {@link #read()}����ˢ��״̬(������ʵ��֮һ:�ڰ�һ������Ϊ����ִ��һ������ʱ�����޸�
 * ���ڲ�״̬)
 * @author Scott Captain
 * @since 2006-6-3
 * @version $Id: VfsUploadFiles.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 * TODO: �޸�������<code>bug</code>
 */
public class VfsUploadFiles implements UploadFiles {
    
    /** ����<code>vfs</code>����·���ı�־ */
    private boolean mounted = false;
    
    private String errorMsg = "û��Ϊ" + getClass().getName() + "]����VFS����·��,�޷������ϴ����ļ�";
    
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
     * ɾ������<code>files</code>���Ѿ��ϴ��ļ��б��е��ļ�, �Ѿ��ϴ����ļ�����ͨ��<code>form</code>
     * д����, �Ѿ��ϴ����ļ�����<code>UploadFile.isUploadFile() == false</code>����Щ
     * �ļ�
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
     * ���ϴ����ļ��б��浽<code>vfs</code>��, �������ͬ�����ļ���ɾ������ļ������ϴ���
     * �ļ�����
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
    
    /* ���ļ��������ų� & ����, �������ʱ����, Ӧ���� vfs ���������Ĵ���� */
    private String eliminate(String str) {
        return str.replace('&', '_');
    }
    
    /**
     * ���ϴ��ļ��б����ų���<code>null</code>, ��Ȼǰ��ʹ����<code>shrink</code>����,
     * ���ǵ������ʵ������ҵ��ʵ����ʱ��Ȼ���ܳ��ְ���<code>null</code>�����
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
     * ��ѡ��<b>�Ѿ�</b>�ϴ����ļ���<code>Predicate</code>, �Ѿ��ϴ����ļ��������ļ���
     * ��û������, ͬʱ���˵����ܴ��ڵĿ��ļ���
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
                /* ɾ�������Ŀ¼�����ʵ��������ʹ�� */
                mounted = false;
            }
        }
    }

    public void delete(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            throw new NullPointerException("û��ָ��Ҫɾ�����ļ���");
        }
        if (!mounted) {
            throw new IllegalStateException(errorMsg);
        }
        if (fileObjectManager.exists(fileName)) {
            fileObjectManager.delete(fileObjectManager.find(fileName));
        } else {
            throw new IllegalArgumentException("Ҫɾ�����ļ�[" + fileName + "]������");
        }
    }
}
