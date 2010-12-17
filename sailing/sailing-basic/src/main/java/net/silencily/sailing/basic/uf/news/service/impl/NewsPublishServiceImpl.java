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
     * 功能描述 信息发布列表
     */
    public List list() {
        // TODO 自动生成方法存根
        ContextInfo.recoverQuery();
        DetachedCriteria dc = DetachedCriteria.forClass(TblUfNews.class)
        .add(Restrictions.eq("delFlg", "0"))
        .add(Restrictions.eq("publisherId", SecurityContextInfo.getCurrentUser().getEmpCd()))
        .addOrder(Order.desc("publishTime"));
        return this.hibernateTemplate.findByCriteria(dc); 
    }

    public TblUfNews load(String oid) {
        // TODO 自动生成方法存根
        return (TblUfNews)this.hibernateTemplate.load(TblUfNews.class, oid);
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

    /**
     * 
     * 功能描述 信息保存
     * @param o 要保存的信息
     */
    public void save(Object o) {
        // TODO 自动生成方法存根
        Set set = ((TblUfNews)o).getTblUfNewsFdbks();
        Set deleteSet = new HashSet();
        //删除反馈表数据
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
        //删除附件表数据
        for(Iterator iter = set.iterator(); iter.hasNext();){
            TblUfNewsAttach tuna = (TblUfNewsAttach) iter.next();
            if ("1".equals(tuna.getDelFlg())) {
                hibernateTemplate.delete(tuna);
                deleteSet.add(tuna);
            }
        }
        set.removeAll(deleteSet);

        //保存数据
        this.hibernateTemplate.saveOrUpdate(o);

        //删除附件文件
        for(Iterator iter = deleteSet.iterator(); iter.hasNext();){
            TblUfNewsAttach tuna = (TblUfNewsAttach) iter.next();
            this.deleteFiles(tuna);
        }
    }

    /**
     * 功能描述 信息浏览列表以及首页的更多新闻
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
        // TODO 自动生成方法存根
        hibernateTemplate.delete(tuc);
    }

    
    /**
     * 功能描述 信息删除
     * @param oids 要删除信息的oid
     * @return 
     */
    public boolean deleteAllRel(List oids) {
        int num = oids.size();
        for (int i = 0; i < num; i++) {
            //取得要删除的主表的ID
            String id = oids.get(i).toString().trim();
            //检索信息反馈表数据
            DetachedCriteria dc = DetachedCriteria.forClass(TblUfNewsFdbk.class);
            dc.add(Restrictions.eq("tblUfNews.id", id));
            List sRow=this.hibernateTemplate.findByCriteria(dc);
            for(int row=0; row<sRow.size();row++){
                Object tmp = sRow.get(row);
                hibernateTemplate.delete(tmp);
            }

            //检索信息附件表数据
            dc = DetachedCriteria.forClass(TblUfNewsAttach.class);
            dc.add(Restrictions.eq("tblUfNews.id", id));
            sRow=this.hibernateTemplate.findByCriteria(dc);
            //删除从表数据
            for(int row=0; row<sRow.size();row++){
                Object tmp = sRow.get(row);
                hibernateTemplate.delete(tmp);
            }

            //检索主表数据
            TblUfNews masterData = this.load(id);
            //删除主表数据
            this.delete(masterData);

            //删除附件文件
            for(int row=0; row<sRow.size();row++){
                TblUfNewsAttach tmp = (TblUfNewsAttach)sRow.get(row);
                this.deleteFiles(tmp);
            }
        }

        return true;
    }

    /**
     * 
     * 功能描述 文件删除
     * @param tuna 要删除的附件
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
