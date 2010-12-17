package net.silencily.sailing.basic.uf.columndisplay.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.basic.uf.columndisplay.service.TblColumnDisplayService;
import net.silencily.sailing.basic.uf.domain.TblUfColumn;
import net.silencily.sailing.basic.uf.domain.TblUfNews;
import net.silencily.sailing.common.dbtime.DBTime;
import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.security.SecurityContextInfo;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;


public class TblColumnDisplayServiceImpl implements TblColumnDisplayService {

	private HibernateTemplate hibernateTemplate;
	
	public List list(Class c) {
		// TODO 自动生成方法存根
		ContextInfo.recoverQuery();
		DetachedCriteria dc  = DetachedCriteria.forClass(c)
			.add(Restrictions.eq("delFlg", "0"))
			.add(Restrictions.eq("isSelect", "1"))
			.add(Restrictions.eq("empCd", SecurityContextInfo.getCurrentUser().getEmpCd()))
			.addOrder(Order.asc("displaySort"));
	    return this.hibernateTemplate.findByCriteria(dc); 
	}

	public List list() {
		// TODO 自动生成方法存根
		ContextInfo.recoverQuery();
		DetachedCriteria dc  = DetachedCriteria.forClass(TblUfColumn.class)
			.add(Restrictions.eq("delFlg", "0"))
			.add(Restrictions.ne("columnFlg.code", "UF_LMQF_01"))
			.addOrder(Order.asc("columnFlg"));
	    return this.hibernateTemplate.findByCriteria(dc); 
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

	public List listNews(TblUfColumn tblUfColumn) {
		// TODO 自动生成方法存根
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date today = null;
        try {
            today = sdf.parse(sdf.format(DBTime.getDBTime()));
        } catch (Exception e) {
            today = DBTime.getDBTime();
        }
		ContextInfo.concealQuery();
        DetachedCriteria dc = null;
        if (tblUfColumn.getFeedbackFlg().getCode().equals("1")) {
            dc = DetachedCriteria.forClass(TblUfNews.class)
            .add(Restrictions.eq("tblUfColumn.id", tblUfColumn.getId()))
            .add(Restrictions.eq("delFlg", "0"))
            .add(Restrictions.eq("published.code", "1"))
            .add(Restrictions.ge("invalidTime", today))
            .createCriteria("tblUfNewsFdbks")
            .add(Restrictions.eq("tblHrEmpInfo.empCd", SecurityContextInfo.getCurrentUser().getEmpCd()));
        } else {
            dc = DetachedCriteria.forClass(TblUfNews.class)
            .add(Restrictions.eq("tblUfColumn.id", tblUfColumn.getId()))
            .add(Restrictions.eq("delFlg", "0"))
            .add(Restrictions.eq("published.code", "1"))
            .add(Restrictions.ge("invalidTime", today))
            .addOrder(Order.desc("publishTime"));
        }
        return this.hibernateTemplate.findByCriteria(dc); 
	}

	public TblUfNews loadNews(String id) {
		// TODO 自动生成方法存根
		return (TblUfNews)this.hibernateTemplate.load(TblUfNews.class , id);
	}

	public TblCmnUser getEmpInfo(String emp_cd) {
		DetachedCriteria dc  = DetachedCriteria.forClass(TblCmnUser.class)
			.add(Restrictions.eq("delFlg", "0"))
			.add(Restrictions.eq("empCd", emp_cd));
	    return (TblCmnUser)this.hibernateTemplate.findByCriteria(dc).iterator().next(); 
	}

	 
			
}
