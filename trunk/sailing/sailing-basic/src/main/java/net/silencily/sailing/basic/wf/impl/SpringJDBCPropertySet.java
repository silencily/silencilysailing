package net.silencily.sailing.basic.wf.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import net.silencily.sailing.container.ServiceProvider;

import org.springframework.jdbc.datasource.DataSourceUtils;

import com.opensymphony.module.propertyset.database.JDBCPropertySet;


public class SpringJDBCPropertySet extends JDBCPropertySet
{

    public SpringJDBCPropertySet()
    {
    }

    protected Connection getConnection()
        throws SQLException
    {
        return DataSourceUtils.getConnection((DataSource)ServiceProvider.getService("system.datasource"));
    }

    public void init(Map arg0, Map arg1)
    {
        super.init(arg0, arg1);
        super.closeConnWhenDone = false;
    }
}
