package net.silencily.sailing.basic.uf.news.service;

import java.util.List;

import net.silencily.sailing.basic.uf.domain.TblUfNews;
import net.silencily.sailing.framework.core.ServiceBase;

/**
 * @author wangchc
 *
 */
public interface NewsPublishService extends ServiceBase {
    public static final String SERVICE_NAME = "uf.NewsPublishService"; 
    List list();
    List listMore();
    TblUfNews load(String oid);
    void save(Object o);
    void delete(TblUfNews tun);
    public boolean deleteAllRel(List oids);

}
