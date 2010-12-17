package net.silencily.sailing.common.report.service;

import java.util.List;

import net.silencily.sailing.common.report.config.ReportConfig;


/**
 * 报表配置<code>DAO</code>, 因为同一个{@link ReportManagementService}存在多个实现,
 * 可能有<code>excel</code>和<code>velocity</code>等, 所以就把数据访问对象单独做出来
 * , 以便不同的实现重用这个数据访问逻辑
 * @author zhangli
 * @version $Id: ReportConfigDao.java,v 1.1 2010/12/10 10:54:17 silencily Exp $
 * @since 2007-5-8
 */
public interface ReportConfigDao {
    
    /** 删除一个报表配置 */
    void deleteEntity(ReportConfig reportConfig);
    
    /** 更新已有的或者保存一个新建的报表配置 */
    void saveEntity(ReportConfig reportConfig);
    
    /** 
     * 根据参数加载已保存的报表配置, 因为是以<code>code</code>为主键所以也就是根据
     * <code>code</code>加载
     */
    ReportConfig loadEntityById(String code);
    
    /**
     * 列出类似指定<code>code</code>的所有配置, 这个方法主要是针对分层管理报表配置的情况
     * , <code>code</code>如何分层在{@link ReportManagementService}上叙述
     * @param code 要查找的报表配置<code>code</code>模式
     * @return 满足条件的结果
     */
    List listEntities(String code);
}
