package net.silencily.sailing.basic.uf.columnorder.service;

import java.util.List;

import net.silencily.sailing.basic.uf.domain.TblUfColumn;
import net.silencily.sailing.basic.uf.domain.TblUfColumnOrder;
import net.silencily.sailing.framework.core.ServiceBase;

public interface TblColumnOrderService extends ServiceBase {
	
	public static String SERVICE_NAME = "uf.columnorderservice";
	
	List list(); 
		
	TblUfColumn load(String cid);
	
	TblUfColumnOrder loadOrder(String cid);

	void update(TblUfColumnOrder tuco);
	
	void save(TblUfColumnOrder tuco);
}
