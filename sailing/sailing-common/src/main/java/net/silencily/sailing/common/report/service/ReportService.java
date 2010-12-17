package net.silencily.sailing.common.report.service;

import java.util.Map;

import net.silencily.sailing.common.CommonConstants;
import net.silencily.sailing.framework.service.ServiceBaseWithNotAllowedNullParamters;


/**
 * 报表创建服务, 根据给定的数据和模板完成报表创建, 把创建后的报表写到输出流中, 实现类要限制
 * 输出报表的大小, 以免造成内存溢出. 任何一种实现在创建固定行数的报表输出了多份报表时, (这种
 * 情况下{@link ReportConfig#isFixedRow()}返回<code>true</code>)把输出流创建为几个文件
 * 并且压缩为一个<code>zip</code>输出
 * @author zhangli
 * @version $Id: ReportService.java,v 1.1 2010/12/10 10:54:17 silencily Exp $
 * @since 2007-5-6
 */
public interface ReportService extends ServiceBaseWithNotAllowedNullParamters {
    
    String SERVICE_NAME = CommonConstants.MODULE_NAME + ".reportService";
    
    /**
     * 根据数据和配置创建报表, 把结果写到输出流中, 处理结束后把报表配置和生成的报表输出流写
     * 到参数<code>reportSource</code>中
     * @param data      数据, 如何解释依赖于实现, 通常<code>key</code>是模板中的占位符
     * @param reportSource 接收和写入数据的传输类, 从这里读取<code>code</code>和输出流
     * @throws IllegalArgumentException 如果参数没有设置必须的数据
     */
    void process(Map data, ReportSource reportSource);
}
