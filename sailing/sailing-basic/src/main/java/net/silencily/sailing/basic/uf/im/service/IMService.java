package net.silencily.sailing.basic.uf.im.service;

import java.util.List;

import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.framework.core.ServiceBase;


/**
 * 
 * @author tongjq
 * @version 1.0
 */
public interface IMService extends ServiceBase {
    public static String SERVICE_NAME = "uf.IMService";
    
    List list(); 
        
    TblCmnUser find(String empCd);
    
    void update(Object obj);

}
