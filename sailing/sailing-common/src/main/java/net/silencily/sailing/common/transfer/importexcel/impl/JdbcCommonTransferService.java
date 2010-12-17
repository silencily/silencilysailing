package net.silencily.sailing.common.transfer.importexcel.impl;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.silencily.sailing.common.transfer.importexcel.CommonTransferConfig;
import net.silencily.sailing.framework.utils.DaoHelper;


/**
 * <code>JDBC</code>实现的人事数据导入服务
 * @author Scott Captain
 * @since 2006-8-17
 * @version $Id: JdbcCommonTransferService.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 */
public class JdbcCommonTransferService extends AbstractCommonTransferService {

    protected void insertData(List paramNames, List paramValues, CommonTransferConfig conf) {
        /* 插入数据时需要填写 id, version 两个列 */
        StringBuffer sb = new StringBuffer("insert into ").append(conf.getTableName()).append(" (");
        StringBuffer placeholder = new StringBuffer();
        int[] types = new int[paramNames.size() + 2];
        Arrays.fill(types, Types.VARCHAR);
        types[1] = Types.INTEGER;
        
        List val = new ArrayList(paramValues.size());
        val.add(DaoHelper.nextPrimaryKey());
        val.add(new Integer(0));
        val.addAll(paramValues);

        /* 不需要检查 paramNames.size() == 0 的情况, 在这个方法之前已经作了检查 */
        sb.append("id, version");
        placeholder.append("?, ?");
        for (int i = 0; i < paramNames.size(); i++) {
            //String col = (String) paramNames.get(i);
            sb.append(", ").append((String) paramNames.get(i));
            placeholder.append(", ").append("?");
        }
        sb.append(") values (").append(placeholder).append(")");
        DaoHelper.getJdbcTemplate().update(sb.toString(), (Object[]) val.toArray(), types);
    }

    protected int updateData(List paramNames, List paramValues, CommonTransferConfig conf) {
        StringBuffer sql = new StringBuffer("update ").append(conf.getTableName()).append(" set ");
        Object value = null;
        for (int i = 0; i < paramNames.size(); i++) {
            String col = (String) paramNames.get(i);
            if (col.equalsIgnoreCase(conf.getKey())) {
                value = paramValues.get(i);
            }
            if (i > 0) {
                sql.append(",");
            }
            sql.append(col).append(" = ?");
        }
        sql.append(" where ").append(conf.getKey()).append(" = ?");
        /* 在要更新的参数数组中再加上 where 条件的值 */
        Object[] params = paramValues.toArray();
        Object[] _params = new Object[params.length + 1];
        System.arraycopy(params, 0, _params, 0, params.length);
        _params[params.length] = value;
        /* 使 null 参数值正确工作 */
        int[] types = new int[_params.length];
        Arrays.fill(types, Types.VARCHAR);
        
        return DaoHelper.getJdbcTemplate().update(sql.toString(), _params, types);
    }
}
