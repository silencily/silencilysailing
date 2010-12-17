package net.silencily.sailing.basic.uf.sso.service;

import net.silencily.sailing.basic.uf.domain.TblUfSsoEntry;
import net.silencily.sailing.framework.core.ServiceBase;



/**
 * 
 * @author tongjq
 * @version 1.0
 */
public interface SingleSignOnService extends ServiceBase {
    public static String SERVICE_NAME = "uf.SingleSignOnService";
    
//    List list(); 
        
    TblUfSsoEntry load(String cid);
    
    void save(TblUfSsoEntry tuse);
    
    void update(TblUfSsoEntry tuse);

}
