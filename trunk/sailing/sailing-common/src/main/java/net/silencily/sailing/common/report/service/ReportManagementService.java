package net.silencily.sailing.common.report.service;

import java.util.List;

import net.silencily.sailing.common.report.config.ReportConfig;


/**
 * �������ù������, ά����������
 * @author zhangli
 * @version $Id: ReportManagementService.java,v 1.1 2010/12/10 10:54:17 silencily Exp $
 * @since 2007-5-6
 */
public interface ReportManagementService extends ReportService {
    
    /**
     * ����һ���µı�������
     * @param code �������ñ���
     * @param name ��������
     * @return �µı�������
     */
    ReportConfig createReportConfig(String code, String name);
    
    /**
     * ����һ���½�������б������õ��޸�
     * @param reportConfig ��������
     */
    void saveReportConfig(ReportConfig reportConfig);
    
    /**
     * ����һ����������
     * @param code �������ñ���
     * @return ��������
     * @throws IllegalStateException ���û�������ı�������
     */
    ReportConfig loadReportConfig(String code);
    
    /**
     * ɾ�����б�������
     * @param reportConfig ��������
     */
    void deleteReportConfig(ReportConfig reportConfig);
    
    /**
     * �г�����ָ���������ñ�������б�������, �����������������ñ���, ��������ϵͳ����,
     * ���Ǹ���ģ���<code>MODULE_NAME</code>, �����ǹ�������, �������Ǳ�������, ֮��
     * ��","�ָ�
     * @return ���������ı�������, Ԫ��������{@link ReportConfig}, ���û�����ݷ���
     * <code>Collections.EMPTY_LIST</code>
     */
    List listReportConfigs(String code);

}
