package net.silencily.sailing.basic.uf.news.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.silencily.sailing.basic.uf.domain.TblUfNews;
import net.silencily.sailing.basic.uf.domain.TblUfNewsAttach;
import net.silencily.sailing.basic.uf.domain.TblUfNewsFdbk;
import net.silencily.sailing.basic.uf.news.service.NewsPublishService;
import net.silencily.sailing.common.dbtime.DBTime;
import net.silencily.sailing.common.fileupload.VfsUploadFiles;
import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.security.SecurityContextInfo;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * @author wangchc
 *
 */
public class NewsPublishServiceImpl implements NewsPublishService {

    private HibernateTemplate hibernateTemplate;

    /**
     * �������� ��Ϣ�����б�
     */
    public List list() {
        // TODO �Զ����ɷ������
        ContextInfo.recoverQuery();
        DetachedCriteria dc = DetachedCriteria.forClass(TblUfNews.class)
        .add(Restrictions.eq("delFlg", "0"))
        .add(Restrictions.eq("publisherId", SecurityContextInfo.getCurrentUser().getEmpCd()))
        .addOrder(Order.desc("publishTime"));
        return this.hibernateTemplate.findByCriteria(dc); 
    }

    public TblUfNews load(String oid) {
        // TODO �Զ����ɷ������
        return (TblUfNews)this.hibernateTemplate.load(TblUfNews.class, oid);
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

    /**
     * 
     * �������� ��Ϣ����
     * @param o Ҫ�������Ϣ
     */
    public void save(Object o) {
        // TODO �Զ����ɷ������
        Set set = ((TblUfNews)o).getTblUfNewsFdbks();
        Set deleteSet = new HashSet();
        //ɾ������������
        for(Iterator iter = set.iterator(); iter.hasNext();){
            TblUfNewsFdbk tunf = (TblUfNewsFdbk) iter.next();
            if ("1".equals(tunf.getDelFlg())) {
                hibernateTemplate.delete(tunf);
                deleteSet.add(tunf);
            }
        }
        set.removeAll(deleteSet);

        set = ((TblUfNews)o).getTblUfNewsAttach();
        deleteSet = new HashSet();
        //ɾ������������
        for(Iterator iter = set.iterator(); iter.hasNext();){
            TblUfNewsAttach tuna = (TblUfNewsAttach) iter.next();
            if ("1".equals(tuna.getDelFlg())) {
                hibernateTemplate.delete(tuna);
                deleteSet.add(tuna);
            }
        }
        set.removeAll(deleteSet);

        //��������
        this.hibernateTemplate.saveOrUpdate(o);

        //ɾ�������ļ�
        for(Iterator iter = deleteSet.iterator(); iter.hasNext();){
            TblUfNewsAttach tuna = (TblUfNewsAttach) iter.next();
            this.deleteFiles(tuna);
        }
    }

    /**
     * �������� ��Ϣ����б��Լ���ҳ�ĸ�������
     */
    public List listMore() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date today = null;
        try {
            today = sdf.parse(sdf.format(DBTime.getDBTime()));
        } catch (Exception e) {
            today = DBTime.getDBTime();
        }
//        ContextInfo.recoverQuery();
        ContextInfo.concealQuery();

        DetachedCriteria dc = DetachedCriteria.forClass(TblUfNews.class)
        .add(Restrictions.eq("delFlg", "0"))
        .add(Restrictions.eq("published.code", "1"))
        .add(Restrictions.ge("invalidTime", today))
        .createCriteria("tblUfNewsFdbks")
        .add(Restrictions.eq("tblHrEmpInfo.empCd", SecurityContextInfo.getCurrentUser().getEmpCd()));

        List myNews = this.hibernateTemplate.findByCriteria(dc);
        List newsIds = new ArrayList();
        for (int i = 0;i < myNews.size();i++) {
            newsIds.add(((TblUfNews)myNews.get(i)).getId());
        }
        //start inserted by lanxingang 20080225
        if(newsIds.size() < 1){
        	newsIds.add("NONE");
        }
        //end inserted by lanxingang 20080225
        
        ContextInfo.recoverQuery();
        dc = DetachedCriteria.forClass(TblUfNews.class)
        .add(Restrictions.eq("delFlg", "0"))
        .add(Restrictions.eq("published.code", "1"))
        .add(Restrictions.ge("invalidTime", today))
        .addOrder(Order.desc("publishTime"))
        .createAlias("tblUfColumn","tblUfColumn")
        .add(Restrictions.or(Restrictions.eq("tblUfColumn.feedbackFlg.code", "0"), 
                             Restrictions.in("id", newsIds)));

//        DetachedCriteria dc = DetachedCriteria.forClass(TblUfNews.class)
//        .add(Restrictions.eq("delFlg", "0"))
//        .add(Restrictions.eq("published.code", "1"))
//        .addOrder(Order.desc("publishTime"))
//
//        .createAlias("tblUfColumn","tblUfColumn")
//        
////        .createCriteria("tblUfNewsFdbks")
//        .setFetchMode("tblUfNewsFdbks", FetchMode.JOIN)
//        .add(Restrictions.eq("tblHrEmpInfo.empCd", SecurityContextInfo.getCurrentUser().getEmpCd()));
////        .add(Restrictions.or(Restrictions.eq("tblHrEmpInfo.empCd", SecurityContextInfo.getCurrentUser().getEmpCd()), 
////                             Restrictions.eq("tblUfColumn.feedbackFlg.code", "0")));

        List news = this.hibernateTemplate.findByCriteria(dc);

        return news; 
    }

    /* (non-Javadoc)
     * @see com.qware.uf.column.service.ColumnManageService#delete(com.qware.uf.domain.TblUfColumn)
     */
    public void delete(TblUfNews tuc) {
        // TODO �Զ����ɷ������
        hibernateTemplate.delete(tuc);
    }

    
    /**
     * �������� ��Ϣɾ��
     * @param oids Ҫɾ����Ϣ��oid
     * @return 
     */
    public boolean deleteAllRel(List oids) {
        int num = oids.size();
        for (int i = 0; i < num; i++) {
            //ȡ��Ҫɾ���������ID
            String id = oids.get(i).toString().trim();
            //������Ϣ����������
            DetachedCriteria dc = DetachedCriteria.forClass(TblUfNewsFdbk.class);
            dc.add(Restrictions.eq("tblUfNews.id", id));
            List sRow=this.hibernateTemplate.findByCriteria(dc);
            for(int row=0; row<sRow.size();row++){
                Object tmp = sRow.get(row);
                hibernateTemplate.delete(tmp);
            }

            //������Ϣ����������
            dc = DetachedCriteria.forClass(TblUfNewsAttach.class);
            dc.add(Restrictions.eq("tblUfNews.id", id));
            sRow=this.hibernateTemplate.findByCriteria(dc);
            //ɾ���ӱ�����
            for(int row=0; row<sRow.size();row++){
                Object tmp = sRow.get(row);
                hibernateTemplate.delete(tmp);
            }

            //������������
            TblUfNews masterData = this.load(id);
            //ɾ����������
            this.delete(masterData);

            //ɾ�������ļ�
            for(int row=0; row<sRow.size();row++){
                TblUfNewsAttach tmp = (TblUfNewsAttach)sRow.get(row);
                this.deleteFiles(tmp);
            }
        }

        return true;
    }

    /**
     * 
     * �������� �ļ�ɾ��
     * @param tuna Ҫɾ���ĸ���
     * @return 
     */
    public boolean deleteFiles(TblUfNewsAttach tuna) {
        try {
            VfsUploadFiles files = new VfsUploadFiles(tuna.getSavePath());
            files.delete(tuna.getFileName());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
