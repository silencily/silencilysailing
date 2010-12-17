package net.silencily.sailing.common.transfer.importexcel;

import java.io.InputStream;

/**
 * 读写<code>dbf</code>,<code>excel</code>文件的组件, 从输入流中读出数据并调用
 * {@link CommonTransferService#writeRow(java.util.Map, HrTransferConfigItem[]) writeRow}
 * 方法. 这个接口的实现不需要处理异常
 * @author Scott Captain
 * @since 2006-8-17
 * @version $Id: CommonTransfer.java,v 1.1 2010/12/10 10:54:15 silencily Exp $
 *
 */
public interface CommonTransfer {
    
    /**
     * 这个名称用于使用者检索实现类, 比如一个<code>excel</code>的实现的名称应该是:<code>
     * hr.excel.hrTransfer</code>, 使用者(HrTransferServicde)就使用这个名称查找实现类
     */
    String SERVICE_NAME = "commonTransfer";
    
    /**
     * 从输入流读出<code>dbf</code>, <code>excel</code>等格式的数据, 每读一行都调用
     * {@link CommonTransferService#writeRow(java.util.Map) writeRow}
     * 方法
     * @param in        包含要导入数据的输入流
     * @param serivce   回调这个类的方法
     * @return          导入到数据库的行数
     */
    int transfer(InputStream in, CommonTransferCallback callback) throws Exception;
}
