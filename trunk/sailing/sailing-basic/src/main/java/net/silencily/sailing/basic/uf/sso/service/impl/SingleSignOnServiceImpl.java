package net.silencily.sailing.basic.uf.sso.service.impl;

import net.silencily.sailing.basic.uf.domain.TblUfSsoEntry;
import net.silencily.sailing.basic.uf.sso.service.SingleSignOnService;

import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * 
 * @author tongjq
 * @version 1.0
 */
public class SingleSignOnServiceImpl implements SingleSignOnService {
    private HibernateTemplate hibernateTemplate; 

    /* (non-Javadoc)
     * @see com.qware.uf.sso.service.SingleSignOnService#load(java.lang.String)
     */
    public TblUfSsoEntry load(String cid) {
        // TODO Auto-generated method stub
        return (TblUfSsoEntry)this.hibernateTemplate.load(TblUfSsoEntry.class , cid);
    }

    /* (non-Javadoc)
     * @see com.qware.uf.sso.service.SingleSignOnService#save(com.qware.uf.domain.TblUfSsoEntry)
     */
    public void save(TblUfSsoEntry tuse) {
        // TODO Auto-generated method stub
        this.hibernateTemplate.saveOrUpdate(tuse);
    }

    /* (non-Javadoc)
     * @see com.qware.uf.sso.service.SingleSignOnService#update(com.qware.uf.domain.TblUfSsoEntry)
     */
    public void update(TblUfSsoEntry tuse) {
        // TODO Auto-generated method stub
        this.hibernateTemplate.update(tuse);
    }

    /**
     * @return hibernateTemplate
     */
    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    /**
     * @param hibernateTemplate ÒªÉèÖÃµÄ hibernateTemplate
     */
    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

}
