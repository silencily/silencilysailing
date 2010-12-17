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

	/* （非 Javadoc）
	 * @see com.qware.uf.column.service.ColumnManageService#list()
	 */
	public List list() {
		// TODO 自动生成方法存根
		ContextInfo.recoverQuery();
		DetachedCriteria dc  = DetachedCriteria.forClass(TblUfColumn.class)
			.add(Restrictions.eq("delFlg", "0"))
			.addOrder(Order.desc("columnFlg"));
	    return this.hibernateTemplate.findByCriteria(dc); 
	}

	/* （非 Javadoc）
	 * @see com.qware.uf.column.service.ColumnManageService#load(java.lang.String)
	 */
	public TblUfColumn load(String cid) {
		// TODO 自动生成方法存根
		return (TblUfColumn)this.hibernateTemplate.load(TblUfColumn.class , cid);
	}

	/* （非 Javadoc）
	 * @see com.qware.uf.column.service.ColumnManageService#save(com.qware.uf.domain.TblUfColumn)
	 */
	public void save(TblUfColumn tuc) {
		// TODO 自动生成方法存根
        Set set = tuc.getTblUfPubPermission();
        Set deleteSet = new HashSet();
        //删除从表数据
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
	 * @param hibernateTemplate 要设置的 hibernateTemplate
	 */
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public void update(TblUfColumn tuc) {
		// TODO 自动生成方法存根
		this.hibernateTemplate.update(tuc);
	}

    /* (non-Javadoc)
     * @see com.qware.uf.column.service.ColumnManageService#delete(com.qware.uf.domain.TblUfColumn)
     */
    public void delete(TblUfColumn tuc) {
        // TODO 自动生成方法存根
        hibernateTemplate.delete(tuc);
    }

    public boolean deleteAllRel(Class mTbl, Class sTbl, String mTblName, List oids) {
        int num = oids.size();
        for (int i = 0; i < num; i++) {
            //取得要删除的主表的ID
            String id = oids.get(i).toString().trim();
            //检索新闻表数据
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
            //检索从表数据
            dc = DetachedCriteria.forClass(sTbl);
//            dc.add(Restrictions.eq("delFlg", "0"));
            dc.add(Restrictions.eq(mTblName + ".id", id));
            sRow=this.hibernateTemplate.findByCriteria(dc);
            //删除从表数据
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
            //检索主表数据
            TblUfColumn masterData = this.load(id);
            //删除主表数据
            this.delete(masterData);
        }

        return true;
    }

    /**新建一条记录
     * @return TblHrRate
     */
    public TblUfColumn newInstance() {
        TblUfColumn tuc = new TblUfColumn();
        tuc.setColumnFlg("UF_LMQF_01");
        return tuc;
    }

    /**创建栏目下拉框
     * @return ComboSupportList
     */
    public ComboSupportList getColumnList(boolean chkAuth) {
        // TODO 自动生成方法存根
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
