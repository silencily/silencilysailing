package net.silencily.sailing.common.transfer.importexcel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.lang.StringUtils;

/**
 * ���� ��������ϵͳ���ݵĵ����������, ������ð����ж��������<code>HrTransferConfig</code>
 * , ÿ���������ʾһ�������ļ��е��������ݿ�����֮��Ķ�Ӧ��ϵ
 * @author Scott Captain
 * @since 2006-8-17
 * @version $Id: CommonTransferConfig.java,v 1.1 2010/12/10 10:54:15 silencily Exp $
 *
 */
public class CommonTransferConfig {
    
    /** Ҫ�������������, ����Ա������Ϣ, ���ʱ�� */
    private String name;
    
    /** Ҫ�������ݵ���ʾ����, ���ڷ����쳣 */
    private String label;
    
    /** ���浼�����ݵ����ݿ������, ��ʹ��<code>JDBC</code>����ʱ���Ǳ���� */
    private String tableName;
    
    /** 
     * Ψһ��ʶһ�����ݵ�������, ��ҵ����Ҫ���¶������������������ʱ, ���ֵ����������ֵ
     */
    private String key;
    
    /**
     * ���������ļ��������ݿ��������֮��Ķ�Ӧ����, Ԫ��������
     * {@link CommonTransferConfigItem <code>HrTransferConfigItem</code>}
     */
    private List items = new ArrayList();

    public List getItems() {
        return items;
    }

    public void setItems(List items) {
        this.items = items;
    }

    /* 
     * the method conviences that find HrTransferConfigItem from the field name, the 
     * method are not designed for frequent invocation
     */
    public Map getKeyItemMapping() {
        Map map = new CaseInsensitiveMap(items.size());
        for (Iterator it = items.iterator(); it.hasNext(); ) {
            CommonTransferConfigItem item = (CommonTransferConfigItem) it.next();
            map.put(item.getFieldName(), item);
        }
        return map;
    }

    /* the method convenience digester operation */
    public void addItem(CommonTransferConfigItem item) {
        items.add(item);
    }

    public String getKey() {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }    
}
