package net.silencily.sailing.common.report.service;

import java.util.List;

import net.silencily.sailing.common.report.config.ReportConfig;


/**
 * 报表配置管理服务, 维护报表配置
 * @author zhangli
 * @version $Id: ReportManagementService.java,v 1.1 2010/12/10 10:54:17 silencily Exp $
 * @since 2007-5-6
 */
public interface ReportManagementService extends ReportService {
    
    /**
     * 创建一个新的报表配置
     * @param code 报表配置编码
     * @param name 报表名称
     * @return 新的报表配置
     */
    ReportConfig createReportConfig(String code, String name);
    
    /**
     * 保存一个新建或对已有报表配置的修改
     * @param reportConfig 报表配置
     */
    void saveReportConfig(ReportConfig reportConfig);
    
    /**
     * 加载一个报表配置
     * @param code 报表配置编码
     * @return 报表配置
     * @throws IllegalStateException 如果没有这样的报表配置
     */
    ReportConfig loadReportConfig(String code);
    
    /**
     * 删除已有报表配置
     * @param reportConfig 报表配置
     */
    void deleteReportConfig(ReportConfig reportConfig);
    
    /**
     * 列出类似指定报表配置编码的所有报表配置, 可以这样管理报表配置编码, 首先以子系统命名,
     * 就是各个模块的<code>MODULE_NAME</code>, 接着是功能名称, 再下来是报表名称, 之间
     * 以","分隔
     * @return 满足条件的报表配置, 元素类型是{@link ReportConfig}, 如果没有数据返回
     * <code>Collections.EMPTY_LIST</code>
     */
    List listReportConfigs(String code);

}
