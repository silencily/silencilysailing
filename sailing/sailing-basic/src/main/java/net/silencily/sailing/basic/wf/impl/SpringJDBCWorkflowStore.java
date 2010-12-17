package net.silencily.sailing.basic.wf.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import net.silencily.sailing.container.ServiceProvider;

import org.springframework.jdbc.datasource.DataSourceUtils;

import com.opensymphony.workflow.StoreException;
import com.opensymphony.workflow.spi.jdbc.JDBCWorkflowStore;

public class SpringJDBCWorkflowStore extends JDBCWorkflowStore
{

    public SpringJDBCWorkflowStore()
    {
    }

    protected Connection getConnection()
        throws SQLException
    {
        return DataSourceUtils.getConnection((DataSource)ServiceProvider.getService("system.datasource"));
    }

    @SuppressWarnings("unchecked")
	public void init(Map props)
        throws StoreException
    {
        super.init(props);
        closeConnWhenDone = false;
    }
}
