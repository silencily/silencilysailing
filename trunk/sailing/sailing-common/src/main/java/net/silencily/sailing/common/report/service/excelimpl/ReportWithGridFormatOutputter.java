package net.silencily.sailing.common.report.service.excelimpl;

import java.util.List;

/**
 * �б��ʽ��<code>excel</code>���������
 * @author zhangli
 * @version $Id: ReportWithGridFormatOutputter.java,v 1.1 2010/12/10 10:54:15 silencily Exp $
 * @since 2007-5-8
 */
public interface ReportWithGridFormatOutputter {
    
    void execute(int start, int end, List cells, RowCallback callback);
}
