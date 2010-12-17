package net.silencily.sailing.common.report.service;

import java.util.Map;

import net.silencily.sailing.common.CommonConstants;
import net.silencily.sailing.framework.service.ServiceBaseWithNotAllowedNullParamters;


/**
 * ����������, ���ݸ��������ݺ�ģ����ɱ�����, �Ѵ�����ı���д���������, ʵ����Ҫ����
 * �������Ĵ�С, ��������ڴ����. �κ�һ��ʵ���ڴ����̶������ı�������˶�ݱ���ʱ, (����
 * �����{@link ReportConfig#isFixedRow()}����<code>true</code>)�����������Ϊ�����ļ�
 * ����ѹ��Ϊһ��<code>zip</code>���
 * @author zhangli
 * @version $Id: ReportService.java,v 1.1 2010/12/10 10:54:17 silencily Exp $
 * @since 2007-5-6
 */
public interface ReportService extends ServiceBaseWithNotAllowedNullParamters {
    
    String SERVICE_NAME = CommonConstants.MODULE_NAME + ".reportService";
    
    /**
     * �������ݺ����ô�������, �ѽ��д���������, ���������ѱ������ú����ɵı��������д
     * ������<code>reportSource</code>��
     * @param data      ����, ��ν���������ʵ��, ͨ��<code>key</code>��ģ���е�ռλ��
     * @param reportSource ���պ�д�����ݵĴ�����, �������ȡ<code>code</code>�������
     * @throws IllegalArgumentException �������û�����ñ��������
     */
    void process(Map data, ReportSource reportSource);
}
