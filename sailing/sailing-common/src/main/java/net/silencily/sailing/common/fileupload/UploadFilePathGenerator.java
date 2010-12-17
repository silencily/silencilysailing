package net.silencily.sailing.common.fileupload;

import net.silencily.sailing.framework.persistent.Entity;

/**
 * 上传文件路径生成器, 根据有{@link FileObject}属性的业务实体和上传时的时间生成保存文件
 * 的路径, 避免在一个目录下有太多的文件, 比如一年下来有几万个缺陷单, 如果一个缺陷单使用了
 * 上传文件, 那么可能造成一个目录下有几万个文件
 * @author zhangli
 * @version $Id: UploadFilePathGenerator.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 * @since 2007-4-25
 */
public interface UploadFilePathGenerator {
    
    /** 生成路径的根目录 */
    String FIRST_LEVEL_PATH = "entity";
    
    /**
     * 根据业务实体生成保存上传文件的路径, 生成的路径没有前导和后缀的"/"
     * @param entity 要保存上传文件的业务实体
     * @return 生成的保存{@link FileObject}的路径名, 在这个路径下可以保存这个业务实体的
     * 所有的上传文件, 不返回<code>null</code>
     * @throws NullPointerException 如果参数是<code>null</code>
     * @throws IllegalArgumentException 如果根据参数无法构建路径名
     */
    String path(Entity entity);
    
}
