package net.silencily.sailing.common.transfer.importexcel;

import java.util.Map;

/**
 * 用于{@link CommonTransfer <code>HrTransfer</code>}中把读到数据写到数据库
 * @author Scott Captain
 * @since 2006-8-17
 * @version $Id: CommonTransferCallback.java,v 1.1 2010/12/10 10:54:15 silencily Exp $
 */
public interface CommonTransferCallback {
    /**
     * {@link CommonTransfer <code>HrTransfer</code>}每读到一行数据就调用一个这个方法
     * @param map   从<code>dbf</code>,<code>excel</code>数据文件读到的一行数据, 
     * <code>key</code>是数据文件中的域名称, <code>value</code>这个域在这行的值
     * @return 如果把这行数据写到了数据库就返回<code>true</code>, 否则返回<code>false</code>
     */
    boolean executePerRow(Map map);
}
