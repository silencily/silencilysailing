package net.silencily.sailing.common.transfer.importexcel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.lang.StringUtils;

/**
 * 公共 导入其他系统数据的导入操作配置, 这个配置包含有多个配置项<code>HrTransferConfig</code>
 * , 每个配置项表示一个数据文件中的域与数据库表的列之间的对应关系
 * @author Scott Captain
 * @since 2006-8-17
 * @version $Id: CommonTransferConfig.java,v 1.1 2010/12/10 10:54:15 silencily Exp $
 *
 */
public class CommonTransferConfig {
    
    /** 要导入的数据名称, 有人员基本信息, 工资表等 */
    private String name;
    
    /** 要导入数据的显示名称, 用于发生异常 */
    private String label;
    
    /** 保存导入数据的数据库表名称, 在使用<code>JDBC</code>方案时这是必须的 */
    private String tableName;
    
    /** 
     * 唯一标识一行数据的列名称, 当业务需要更新而不是新增导入的数据时, 这个值用来做条件值
     */
    private String key;
    
    /**
     * 导入数据文件域与数据库表列名称之间的对应配置, 元素类型是
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
