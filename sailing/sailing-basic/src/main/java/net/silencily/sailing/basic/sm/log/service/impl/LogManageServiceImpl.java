package net.silencily.sailing.basic.sm.log.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.silencily.sailing.basic.sm.log.service.LogManageService;

import org.springframework.orm.hibernate3.HibernateTemplate;

public class LogManageServiceImpl implements LogManageService{
	
	private HibernateTemplate hibernateTemplate;

	public void delete(String startTime, String endTime) throws SQLException {
		
		String tableName="TBL_CMN_SYS_LOG";
		Connection con=hibernateTemplate.getSessionFactory().getCurrentSession().connection();
		String codeSQL="delete from " + tableName + " where TO_CHAR(time,'YYYY-MM-DD') between '" + startTime + "' and '" + endTime+"'";
		Statement st=con.createStatement();
		st.execute(codeSQL);
		st.close();
		con.close();
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

}
