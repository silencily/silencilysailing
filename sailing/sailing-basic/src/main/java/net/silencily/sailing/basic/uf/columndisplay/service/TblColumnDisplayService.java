package net.silencily.sailing.basic.uf.columndisplay.service;

import java.util.List;

import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.basic.uf.domain.TblUfColumn;
import net.silencily.sailing.basic.uf.domain.TblUfNews;
import net.silencily.sailing.framework.core.ServiceBase;

public interface TblColumnDisplayService extends ServiceBase {
	
	public static String SERVICE_NAME = "uf.columndisplayservice";
	
	List list(Class c); 
	List list();
	 
	List listNews(TblUfColumn tblUfColumn);
	
	TblUfNews loadNews(String id);
	TblCmnUser getEmpInfo(String emp_cd);
}
