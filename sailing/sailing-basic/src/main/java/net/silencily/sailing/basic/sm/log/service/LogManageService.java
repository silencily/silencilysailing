package net.silencily.sailing.basic.sm.log.service;

import java.sql.SQLException;

import net.silencily.sailing.framework.core.ServiceBase;


public interface LogManageService extends ServiceBase{
	/**
	 * 
	 */
	public static final String SERVICE_NAME = "sm.logManageService";

	public void delete(String startTime, String endTime) throws SQLException;

}
