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
     * �������� ���ӱ����ɾ��
     * @param mTbl Ҫɾ��������ĳ־û�����
     * @param sTbl Ҫɾ���Ĵӱ�ĳ־û�����
     * @param mTblName ��������
     * @param oid Ҫɾ���������¼��ID
     * @return �ɹ�true;ʧ��false
     * 2007-12-24
     * @version 1.0
     * @author tongjq
     */
    public boolean deleteAllRel(Class mTbl, Class sTbl, String mTblName, List oids);

    public ComboSupportList getColumnList(boolean chkAuth);

}
