package net.silencily.sailing.common.transfer.importexcel;

import java.io.InputStream;

import net.silencily.sailing.common.CommonConstants;
import net.silencily.sailing.framework.core.ServiceBase;


/**
 * 公共 数据导入服务, 把客户端的<code>dbf</code>,<code>excel</code>等格式的文件中的
 * 数据导入到相应的数据库表中, 实现类负责维护数据文件与数据库表之间的列的对应关系
 * @author Scott Captain
 * @since 2006-8-16
 * @version $Id: CommonTransferService.java,v 1.1 2010/12/10 10:54:15 silencily Exp $
 *
 */
public interface CommonTransferService extends ServiceBase,CommonConstants {
    
    String SERVICE_NAME = MODULE_NAME +".transferService";
    
    /** 支持的导入数据类型: <code>Foxpro's DBF</code>数据格式 */
    String TYPE_DBF = "dbf";
    
    /** 支持的导入数据类型: <code>MS's excel</code>数据格式 */
    String TYPE_XLS = "xls";
    /**excel文件头部声明*/
    String TYPE_EXCEL_CONTEXT = "application/vnd.ms-excel";

    /**
     * 加载指定名称的配置, 这个配置是关于数据文件与数据库表之间列的对应映射, 通常导入名称与
     * 配置文件名称完全相同(不包含后缀.xml)
     * @param name 数据导入名称, 有人事基本信息, 工资等
     * @return 要导入数据的配置信息, 不返回<code>null</code>
     * @throws IllegalArgumentException 如果没有指定名称的配置, 包括参数是<code>null</code>
     */
    CommonTransferConfig loadConfig(String name);

    /**
     * 从输入流读出<code>dbf</code>或<code>excel</code>格式的数据写到数据库表中. 如果
     * 在过程中发生异常, 就撤销所有的已经发生的改变, 即任何插入或更新动作  
     * @param type  要导入的文件类型, 有<code>dbf</code>,<code>excel</code>等, 这个参数
     *              值必须是{@link #TYPE_DBF <code>dbf</code>},{@link #TYPE_XLS <tt>excel</tt>}
     *              常量中的一个
     * @param name  要导入的数据名称, 比如人员基本信息, 工资等. 这个名称与配置文件名称一致
     * @param in    包含导入数据的输入流
     * @return 导入的数据行数
     * @throws IllegalArgumentException 如果任何一个参数是<code>null</code>, <tt>string</tt>
     * 类型是<tt>empty string</tt>
     */
    int importData(String type, String name, InputStream in);
}
