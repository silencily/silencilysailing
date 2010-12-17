package net.silencily.sailing.common.transfer.importexcel.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.silencily.sailing.common.transfer.importexcel.CommonTransfer;
import net.silencily.sailing.common.transfer.importexcel.CommonTransferCallback;
import net.silencily.sailing.common.transfer.importexcel.CommonTransferConfig;
import net.silencily.sailing.common.transfer.importexcel.CommonTransferConfigItem;
import net.silencily.sailing.common.transfer.importexcel.CommonTransferService;
import net.silencily.sailing.exception.UnexpectedException;

import org.apache.commons.digester.Digester;
import org.apache.commons.lang.StringUtils;

/**
 * <p>������Դ������Ա������Ϣ,���ʵ����ݵķ���, �������ʵ������˼������������õķ���, д������
 * �����ݵķ���. ʵ�ֵ�ҵ���߼�˵��<ul>
 * <li>����������Ϣ:ÿ��Ҫ�����ҵ�񶼶�����һ������, ��Ա������Ϣ��<code>basisinfo</code>,
 * ������<code>pay</code>, �����ļ��������������������. �������һ����������, �Ͱ���ͬ����
 * ����. ��������ʱ���ȴ�ϵͳĬ�ϵ�����Ŀ¼��(<tt>conf/hr</tt>, �μ�������<code>defaultLocation</code>
 * ����)����, ���û������ļ��ʹӵ�ǰ���·��������. ÿ�����ݵĵ��붼��Ӧ��һ�������������ļ�,
 * Ҫ�������ļ������ɷ���<code>loadConfig(String)</code>�Ĳ������</li>
 * <li>�������ݵĹ涨:��������ļ����������ݿ�����û�����ö�Ӧ��ϵ, ��ô��������е����ݾͲ�
 * ��ִ�е���, ��������ʱ����ִ�и���, ���������ݵļ�������<code>HrTransferConfig's key</code>
 * ����, ���û���������ж���������ԾͲ���ִ�и��²���, ������²���û��ִ�л�û�и����κ�����
 * ��ִ����������</li>
 * <li>�����ļ���<code>schema</code>:<pre>
 * &lt;mapping&gt;
 *   &lt;columns name="basisinfo" key="code" tableName="hr_employee"&gt;
 *      &lt;column fieldName="d10" columnName="native" label="����"/&gt;
 *   &lt;/columns&gt;
 * &lt;/mapping&gt;
 * </pre>���е�����˵��<ul>
 * <li><code>columns' name</code>:��������, ��������ǵ���<code>importData</code>ʱ��������
 * ��ֵ, Ҳ�������ļ�����, �������Ʊ���һ��</li>
 * <li><code>columns' key</code>:Ψһ������, ����������ʱʹ��</li>
 * <li><code>columns' tableName</code>:���յ������ݵı�����</li>
 * <li><code>columns' label</code>:��������������Ϣ, �����ڷ�������ʱ����ϸ���ṩ��Ϣ</li>
 * </ul>
 * </li>
 * </ul></p>
 * @author Scott Captain
 * @since 2006-8-16
 * @version $Id: AbstractCommonTransferService.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 *
 */
public abstract class AbstractCommonTransferService implements CommonTransferService {
    protected String defaultLocation = "conf/" + MODULE_NAME + "/";

    private Map transfers = Collections.EMPTY_MAP;
    
    public void setTransfers(Map transfers) {
        this.transfers = transfers;
    }

    public CommonTransferConfig loadConfig(String name) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("�������µ�������ʱû��ָ��Ҫ���ص���������");
        }
        InputStream in = getResource(configFileName(name));
        Digester digester = new Digester();
        digester.addObjectCreate("mapping/columns", CommonTransferConfig.class);
        digester.addSetProperties("mapping/columns");
        digester.addObjectCreate("mapping/columns/column", CommonTransferConfigItem.class);
        digester.addSetProperties("mapping/columns/column");
        digester.addSetNext("mapping/columns/column", "addItem");
        CommonTransferConfig conf = null;
        try {
            conf = (CommonTransferConfig) digester.parse(in);
        } catch (Exception e) {
            throw new UnexpectedException("�������µ������������ļ�[" + name + "]����", e);
        }

        return conf;
    }
    
    /* ������ѭ�������� batch ����͸������ݵķ�����Ϊ��֧�ֿ��ܵ�"�ܵ�����پ͵�����ٵ����" */
    public int importData(String type, String name, InputStream in) {
        CommonTransfer trans = (CommonTransfer) transfers.get(naming(type));
        if (trans == null) {
            throw new IllegalArgumentException("û��Ϊ[" + type + "]ָ��������");
        }
        final CommonTransferConfig conf = loadConfig(name);
        final Map fieldItemMappings = conf.getKeyItemMapping();
        try {
            return trans.transfer(in, new CommonTransferCallback() {
                public boolean executePerRow(Map map) {
                    return writeRow(map, fieldItemMappings, conf);
                }
            });
        } catch (Exception e) {
            throw new UnexpectedException("�����ʽ" + type + ",����" + name + "�����ݴ���", e);
        }
    }

    /**
     * ����������˴������ļ����뵽���ݿ�����, ���������޸����ݿ�����ʱ, ֻ������Щ������
     * ���е���, ÿһ��Ҫִ�и��²����ĵ����������һ��<code>key</code>������, �����������
     * �������ƾͲ�ִ�и��¶���, ֻ����������. ֻ������������ʱ�ŷ���<code>true</code>
     */
    protected boolean writeRow(Map data, Map mappings, CommonTransferConfig conf) {
        List paramNames = new ArrayList(data.size());
        List paramValues = new ArrayList(data.size());
        for (Iterator it = data.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            String fieldName = (String) entry.getKey();
            /* �����ݿ��б���ʲô����ȡ�������ݷ��ʴ���ʵ�� */
            Object value = entry.getValue();
            /* ֻ���������ļ������õ���, ���û�ж��������ƾ�ʹ�������ļ�����������Ϊ���� */
            if (mappings.containsKey(fieldName)) {
                CommonTransferConfigItem item = (CommonTransferConfigItem) mappings.get(fieldName);
                paramNames.add(StringUtils.isNotBlank(item.getColumnName()) ? item.getColumnName() : fieldName);
                paramValues.add(value);
            }
        }
        if (paramNames.size() == 0) {
            throw new IllegalStateException("û��Ҫ�����������,�����ļ�û�ж����������ļ���ƥ�����");
        }
        int count = 0;
        boolean inserted = false;
        String keyColumnName = conf.getKey();
        if (StringUtils.isNotBlank(keyColumnName)) {
            count = updateData(paramNames, paramValues, conf);
        }
        if (count == 0) {
            insertData(paramNames, paramValues, conf);
            inserted = true;
        }

        return inserted;
    }

    protected InputStream getResource(String name) {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(defaultLocation + name);
        if (in == null) {
            in = getClass().getResourceAsStream(name);
        }
        if (in == null) {
            throw new IllegalStateException("û�ҵ����������ļ�[" + name + "]");
        }
        return in;
    }
    
    protected String configFileName(String name) {
        return name + ".xml";
    }
    
    private String naming(String type) {
        StringBuffer sb = new StringBuffer();
        sb.append(MODULE_NAME).append(".").append(type).append(".").append(CommonTransfer.SERVICE_NAME);
        return sb.toString();
    }
    
    /**
     * �������ݵ����ݿ��
     * @param paramNames    Ҫ�������ݵ�������
     * @param paramValues   ÿ���ж�Ӧ��ֵ
     */
    protected abstract void insertData(List paramNames, List paramValues, CommonTransferConfig conf);
    
    /**
     * ��ָ����Ψһ��������ʱ�������ļ���һ�и������ݿ��е���������
     * @param paramNames    Ҫ�������ݵ�������
     * @param paramValues   ÿ���ж�Ӧ��ֵ
     * @param keyColumnName Ψһ��������������
     * @return ���������һ�����ݷ���<b>1</b>,��û�и��µ�����ʱ����<b>0</b>
     */
    protected abstract int updateData(List paramNames, List paramValues, CommonTransferConfig conf);
}
