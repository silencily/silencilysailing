package net.silencily.sailing.basic.uf.column.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.silencily.sailing.basic.uf.column.service.ColumnManageService;
import net.silencily.sailing.basic.uf.domain.TblUfColumn;
import net.silencily.sailing.basic.uf.domain.TblUfColumnOrder;
import net.silencily.sailing.basic.uf.domain.TblUfNews;
import net.silencily.sailing.basic.uf.domain.TblUfPubPermission;
import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.framework.web.view.ComboSupportList;
import net.silencily.sailing.hibernate3.CodeWrapperPlus;
import net.silencily.sailing.security.SecurityContextInfo;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Projection;
import org.springframework.orm.hibernate3.HibernateTemplate;


/**
 * @author wangchc
 *
 */
public class ColumnManageServiceImpl implements ColumnManageService {
	
	private HibernateTemplate hibernateTemplate; 

	/* ���� Javadoc��
	 * @see com.qware.uf.column.service.ColumnManageService#list()
	 */
	public List list() {
		// TODO �Զ����ɷ������
		ContextInfo.recoverQuery();
		DetachedCriteria dc  = DetachedCriteria.forClass(TblUfColumn.class)
			.add(Restrictions.eq("delFlg", "0"))
			.addOrder(Order.desc("columnFlg"));
	    return this.hibernateTemplate.findByCriteria(dc); 
	}

	/* ���� Javadoc��
	 * @see com.qware.uf.column.service.ColumnManageService#load(java.lang.String)
	 */
	public TblUfColumn load(String cid) {
		// TODO �Զ����ɷ������
		return (TblUfColumn)this.hibernateTemplate.load(TblUfColumn.class , cid);
	}

	/* ���� Javadoc��
	 * @see com.qware.uf.column.service.ColumnManageService#save(com.qware.uf.domain.TblUfColumn)
	 */
	public void save(TblUfColumn tuc) {
		// TODO �Զ����ɷ������
        Set set = tuc.getTblUfPubPermission();
        Set deleteSet = new HashSet();
        //ɾ���ӱ�����
        for(Iterator iter = set.iterator(); iter.hasNext();){
            TblUfPubPermission tupp = (TblUfPubPermission) iter.next();
            if ("1".equals(tupp.getDelFlg())) {
                hibernateTemplate.delete(tupp);
                deleteSet.add(tupp);
            }
        }
        set.removeAll(deleteSet);

        this.hibernateTemplate.saveOrUpdate(tuc);
	}

	/**
	 * @return hibernateTemplate
	 */
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	/**
	 * @param hibernateTemplate Ҫ���õ� hibernateTemplate
	 */
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public void update(TblUfColumn tuc) {
		// TODO �Զ����ɷ������
		this.hibernateTemplate.update(tuc);
	}

    /* (non-Javadoc)
     * @see com.qware.uf.column.service.ColumnManageService#delete(com.qware.uf.domain.TblUfColumn)
     */
    public void delete(TblUfColumn tuc) {
        // TODO �Զ����ɷ������
        hibernateTemplate.delete(tuc);
    }

    public boolean deleteAllRel(Class mTbl, Class sTbl, String mTblName, List oids) {
        int num = oids.size();
        for (int i = 0; i < num; i++) {
            //ȡ��Ҫɾ���������ID
            String id = oids.get(i).toString().trim();
            //�������ű�����
            DetachedCriteria dc = null;
            dc = DetachedCriteria.forClass(TblUfNews.class);
            dc.add(Restrictions.eq("tblUfColumn.id", id));
            dc.setProjection(Projections.rowCount());
            
            List sRow=this.hibernateTemplate.findByCriteria(dc);
            if (((Integer)sRow.get(0)).intValue() > 0) {
                return false;
            }
            dc = DetachedCriteria.forClass(TblUfColumnOrder.class);
            dc.add(Restrictions.eq("tblUfColumn.id", id));
            dc.setProjection(Projections.rowCount());
            sRow=this.hibernateTemplate.findByCriteria(dc);
            if (((Integer)sRow.get(0)).intValue() > 0) {
                return false;
            }
            //�����ӱ�����
            dc = DetachedCriteria.forClass(sTbl);
//            dc.add(Restrictions.eq("delFlg", "0"));
            dc.add(Restrictions.eq(mTblName + ".id", id));
            sRow=this.hibernateTemplate.findByCriteria(dc);
            //ɾ���ӱ�����
            for(int row=0; row<sRow.size();row++){
                Object tmp = sRow.get(row);
//                Tools.setProperty(tmp,"delFlg","1");
//                try{
//                    hibernateTemplate.update(tmp);
                  hibernateTemplate.delete(tmp);
//                }catch (RuntimeException e) {
//                    e.printStackTrace();
//                    return false;
//                }
            }
            //������������
            TblUfColumn masterData = this.load(id);
            //ɾ����������
            this.delete(masterData);
        }

        return true;
    }

    /**�½�һ����¼
     * @return TblHrRate
     */
    public TblUfColumn newInstance() {
        TblUfColumn tuc = new TblUfColumn();
        tuc.setColumnFlg("UF_LMQF_01");
        return tuc;
    }

    /**������Ŀ������
     * @return ComboSupportList
     */
    public ComboSupportList getColumnList(boolean chkAuth) {
        // TODO �Զ����ɷ������
        DetachedCriteria dc  = DetachedCriteria.forClass(TblUfColumn.class)
        .add(Restrictions.eq("delFlg", "0"))
        .add(Restrictions.eq("columnFlg", new CodeWrapperPlus("UF_LMQF_01")));
        List list = this.hibernateTemplate.findByCriteria(dc); 

        ComboSupportList csl = new ComboSupportList("id","columnNm");

        for(int i=0; i<list.size();i++){
            TblUfColumn tuc = (TblUfColumn)list.get(i);
            if (chkAuth) {
                boolean chkOk = false;
                Set set = tuc.getTblUfPubPermission();
                for(Iterator iter = set.iterator(); iter.hasNext();){
                    TblUfPubPermission tupp = (TblUfPubPermission) iter.next();
                    if (tupp.getTblHrEmpInfo()!= null && tupp.getTblHrEmpInfo().getEmpCd().equals(SecurityContextInfo.getCurrentUser().getEmpCd())) {
                        chkOk = true;
                        break;
                    }
                }
                if (!chkOk) {
                    continue;
                }
            }
            csl.add(tuc);
        }
        return csl;
    }

}
