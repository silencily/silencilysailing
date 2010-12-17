package net.silencily.sailing.common.report.config;

import net.silencily.sailing.framework.codename.CodeName;


/**
 * 报表配置
 * @author zhangli
 * @version $Id: ReportConfig.java,v 1.1 2010/12/10 10:54:17 silencily Exp $
 * @since 2007-5-6
 */
public interface ReportConfig extends CodeName {
    
    /** 保存报表模板的根目录, 这段路径对应用透明, 以便从全局管理不同应用的<code>vfs</code>路径 */
    public static final String TEMPLATE_ROOT_PATH = "report/template";
}
