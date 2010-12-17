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
 * <p>人力资源导入人员基本信息,工资等数据的服务, 这个抽象实现完成了检索数据列配置的方法, 写入数据
 * 库数据的方法. 实现的业务逻辑说明<ul>
 * <li>导入配置信息:每个要导入的业务都定义了一个名称, 人员基本信息是<code>basisinfo</code>,
 * 工资是<code>pay</code>, 配置文件就是以这个名字命名的. 如果新增一个导入数据, 就按照同样的
 * 规则. 加载配置时首先从系统默认的配置目录下(<tt>conf/hr</tt>, 参见这个类的<code>defaultLocation</code>
 * 属性)搜索, 如果没有这个文件就从当前类的路径下搜索. 每类数据的导入都对应着一个独立的配置文件,
 * 要搜索的文件名是由方法<code>loadConfig(String)</code>的参数组成</li>
 * <li>导入数据的规定:如果数据文件的列与数据库表的列没有配置对应关系, 那么这个数据列的数据就不
 * 会执行导入, 导入数据时首先执行更新, 更新所依据的键是配置<code>HrTransferConfig's key</code>
 * 属性, 如果没有在配置中定义这个属性就不会执行更新操作, 如果更新操作没有执行或没有更新任何数据
 * 就执行新增操作</li>
 * <li>配置文件的<code>schema</code>:<pre>
 * &lt;mapping&gt;
 *   &lt;columns name="basisinfo" key="code" tableName="hr_employee"&gt;
 *      &lt;column fieldName="d10" columnName="native" label="民族"/&gt;
 *   &lt;/columns&gt;
 * &lt;/mapping&gt;
 * </pre>其中的属性说明<ul>
 * <li><code>columns' name</code>:导入名称, 这个名称是调用<code>importData</code>时导入名称
 * 的值, 也是配置文件名称, 三个名称必须一致</li>
 * <li><code>columns' key</code>:唯一键名称, 当更新数据时使用</li>
 * <li><code>columns' tableName</code>:接收导入数据的表名称</li>
 * <li><code>columns' label</code>:这个导入的描述信息, 常用于发生错误时更详细地提供信息</li>
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
            throw new IllegalArgumentException("加载人事导入配置时没有指定要加载的配置名称");
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
            throw new UnexpectedException("加载人事导入数据配置文件[" + name + "]错误", e);
        }

        return conf;
    }
    
    /* 采用了循环而不是 batch 插入和更新数据的方案是为了支持可能的"能导入多少就导入多少的情况" */
    public int importData(String type, String name, InputStream in) {
        CommonTransfer trans = (CommonTransfer) transfers.get(naming(type));
        if (trans == null) {
            throw new IllegalArgumentException("没有为[" + type + "]指定导入类");
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
            throw new UnexpectedException("导入格式" + type + ",名称" + name + "的数据错误", e);
        }
    }

    /**
     * 配置项决定了从数据文件导入到数据库表的列, 当新增或修改数据库表的行时, 只包含那些在配置
     * 项中的列, 每一个要执行更新操作的导入必须配置一个<code>key</code>列名称, 如果不配置这
     * 个列名称就不执行更新动作, 只有增量导入. 只有新增数据行时才返回<code>true</code>
     */
    protected boolean writeRow(Map data, Map mappings, CommonTransferConfig conf) {
        List paramNames = new ArrayList(data.size());
        List paramValues = new ArrayList(data.size());
        for (Iterator it = data.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            String fieldName = (String) entry.getKey();
            /* 在数据库中保存什么类型取决于数据访问代码实现 */
            Object value = entry.getValue();
            /* 只导入配置文件中配置的列, 如果没有定义列名称就使用数据文件的域名称作为列名 */
            if (mappings.containsKey(fieldName)) {
                CommonTransferConfigItem item = (CommonTransferConfigItem) mappings.get(fieldName);
                paramNames.add(StringUtils.isNotBlank(item.getColumnName()) ? item.getColumnName() : fieldName);
                paramValues.add(value);
            }
        }
        if (paramNames.size() == 0) {
            throw new IllegalStateException("没有要导入的数据列,配置文件没有定义与数据文件相匹配的列");
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
            throw new IllegalStateException("没找到导入配置文件[" + name + "]");
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
     * 插入数据到数据库表
     * @param paramNames    要插入数据的列名称
     * @param paramValues   每个列对应的值
     */
    protected abstract void insertData(List paramNames, List paramValues, CommonTransferConfig conf);
    
    /**
     * 当指定了唯一键或主键时用数据文件的一行更新数据库中的已有数据
     * @param paramNames    要更新数据的列名称
     * @param paramValues   每个列对应的值
     * @param keyColumnName 唯一键或主键列名称
     * @return 如果更新了一行数据返回<b>1</b>,当没有更新到数据时返回<b>0</b>
     */
    protected abstract int updateData(List paramNames, List paramValues, CommonTransferConfig conf);
}
