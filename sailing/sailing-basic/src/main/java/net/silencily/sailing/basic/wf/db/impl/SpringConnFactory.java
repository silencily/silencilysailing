package net.silencily.sailing.basic.wf.db.impl;

import java.sql.Connection;
import java.sql.SQLException;

import net.silencily.sailing.basic.wf.db.DBConnFactory;
import net.silencily.sailing.container.ServiceProvider;

import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.util.Assert;


public class SpringConnFactory implements DBConnFactory {
	
	private static SpringConnFactory itself = null;

	private SpringConnFactory() {
	}

	public static synchronized DBConnFactory buildFactory() {
		if (itself == null) {
			itself = new SpringConnFactory();
		}
		
		return itself;
	}

	public Connection getConnection() {
		Connection conn = null;
		HibernateTemplate hibernateTemplate = (HibernateTemplate) ServiceProvider
				.getService("common.hibernateTemplate");
		Session session = SessionFactoryUtils.getSession(hibernateTemplate
				.getSessionFactory(), false);
		
		conn = session.connection();
		try {
			if (conn.getAutoCommit())
					conn.setAutoCommit(false);
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		
		Assert.isTrue(conn != null,
				"Exception : get connection form database connection factory");		
		
		return conn;
	}

}
