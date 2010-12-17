package net.silencily.sailing.basic.wf.db;

import java.sql.Connection;

public interface DBConnFactory {
	
	//DBConnFactory buildFactory();

	Connection getConnection();
	
}
