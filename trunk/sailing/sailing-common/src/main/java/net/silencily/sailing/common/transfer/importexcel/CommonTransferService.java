package net.silencily.sailing.common.transfer.importexcel;

import java.io.InputStream;

import net.silencily.sailing.common.CommonConstants;
import net.silencily.sailing.framework.core.ServiceBase;


/**
 * ���� ���ݵ������, �ѿͻ��˵�<code>dbf</code>,<code>excel</code>�ȸ�ʽ���ļ��е�
 * ���ݵ��뵽��Ӧ�����ݿ����, ʵ���ฺ��ά�������ļ������ݿ��֮����еĶ�Ӧ��ϵ
 * @author Scott Captain
 * @since 2006-8-16
 * @version $Id: CommonTransferService.java,v 1.1 2010/12/10 10:54:15 silencily Exp $
 *
 */
public interface CommonTransferService extends ServiceBase,CommonConstants {
    
    String SERVICE_NAME = MODULE_NAME +".transferService";
    
    /** ֧�ֵĵ�����������: <code>Foxpro's DBF</code>���ݸ�ʽ */
    String TYPE_DBF = "dbf";
    
    /** ֧�ֵĵ�����������: <code>MS's excel</code>���ݸ�ʽ */
    String TYPE_XLS = "xls";
    /**excel�ļ�ͷ������*/
    String TYPE_EXCEL_CONTEXT = "application/vnd.ms-excel";

    /**
     * ����ָ�����Ƶ�����, ��������ǹ��������ļ������ݿ��֮���еĶ�Ӧӳ��, ͨ������������
     * �����ļ�������ȫ��ͬ(��������׺.xml)
     * @param name ���ݵ�������, �����»�����Ϣ, ���ʵ�
     * @return Ҫ�������ݵ�������Ϣ, ������<code>null</code>
     * @throws IllegalArgumentException ���û��ָ�����Ƶ�����, ����������<code>null</code>
     */
    CommonTransferConfig loadConfig(String name);

    /**
     * ������������<code>dbf</code>��<code>excel</code>��ʽ������д�����ݿ����. ���
     * �ڹ����з����쳣, �ͳ������е��Ѿ������ĸı�, ���κβ������¶���  
     * @param type  Ҫ������ļ�����, ��<code>dbf</code>,<code>excel</code>��, �������
     *              ֵ������{@link #TYPE_DBF <code>dbf</code>},{@link #TYPE_XLS <tt>excel</tt>}
     *              �����е�һ��
     * @param name  Ҫ�������������, ������Ա������Ϣ, ���ʵ�. ��������������ļ�����һ��
     * @param in    �����������ݵ�������
     * @return �������������
     * @throws IllegalArgumentException ����κ�һ��������<code>null</code>, <tt>string</tt>
     * ������<tt>empty string</tt>
     */
    int importData(String type, String name, InputStream in);
}
