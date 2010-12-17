package net.silencily.sailing.basic.uf.column.service;

import java.util.List;

import net.silencily.sailing.basic.uf.domain.TblUfColumn;
import net.silencily.sailing.framework.core.ServiceBase;
import net.silencily.sailing.framework.web.view.ComboSupportList;


/**
 * @author wangchc
 *
 */
public interface ColumnManageService extends ServiceBase {
	public static String SERVICE_NAME = "uf.ColumnManageService";
	
	List list(); 
		
	TblUfColumn load(String cid);
	
	void save(TblUfColumn tuc);
	
	void update(TblUfColumn tuc);

    void delete(TblUfColumn tuc);
    
    TblUfColumn newInstance();

    /**
     * 
     * 功能描述 主从表关联删除
     * @param mTbl 要删除的主表的持久化对象
     * @param sTbl 要删除的从表的持久化对象
     * @param mTblName 主表名称
     * @param oid 要删除的主表记录的ID
     * @return 成功true;失败false
     * 2007-12-24
     * @version 1.0
     * @author tongjq
     */
    public boolean deleteAllRel(Class mTbl, Class sTbl, String mTblName, List oids);

    public ComboSupportList getColumnList(boolean chkAuth);

}
