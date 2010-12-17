package net.silencily.sailing.framework.core;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.silencily.sailing.framework.codename.UserCodeName;
import net.silencily.sailing.framework.persistent.filter.ConditionInfo;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * ϵͳ��������Ϣ, ������ǰִ���̵߳���Ϣ, ���統ǰ�û�, ��ȫ��֤, ������������Ϣ. �����Ϣ
 * �������û���һ���������������, �����<code>web</code>ҳ��������Ӧ����
 * 
 * @author scott
 * @author java2enterprise
 * @since 2006-2-26
 * @version $Id: ContextInfo.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 *
 */
public class ContextInfo implements Serializable {
    
    private static Log logger = LogFactory.getLog(ContextInfo.class);
	
	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = -3966764145229699433L;

    /**
     * ���浱ǰִ���������еļ���,�������ݵ�����
     */
    private static ThreadLocal contextCondition = new ThreadLocal();
    /**
     * ��ǰ�û�
     * ��ϵͳ�еĵ�ǰ�û������Ƽ�ʹ��
     */
    private static ThreadLocal currentUser = new ThreadLocal();
    /**
     * �û�������
     * �������ݿ�ʱ�Զ����´����ߵ���Ϣʱ�Թ����̳���ʹ�á����Ƽ��������ط�ʹ�á�
     */
    private static ThreadLocal contextUser = new ThreadLocal();
    /**
     * ���ݿ����
     * ��Viewbean�Զ��������У��������ɵ����ݿ������ŵ��������У���֮����Զ����ɲ�ѯ���������в������������ɹ��ı���
     */
    private static ThreadLocal aliasSet = new ThreadLocal();

    /**
     * �ӵ�ǰִ�л����м�������������
     * @return
     */
    public static ConditionInfo getContextCondition() {
        ConditionInfo info = (ConditionInfo) contextCondition.get();
        if (info == null) {
            info = new ConditionInfo();
            contextCondition.set(info);
        }

        return info;
    }

    public static void setContextCondition(ConditionInfo contextCondition) {
        ContextInfo.contextCondition.set(contextCondition);
    }

    /**
     * �ӵ�ǰִ�л����м����Ѿ������ı����
     * @return
     */
    public static Set getAliasSet() {
        Set info = (Set) aliasSet.get();
        if (info == null) {
            info = new HashSet();
            aliasSet.set(info);
        }

        return info;
    }

    public static void setAliasSet(Set set) {
        ContextInfo.aliasSet.set(set);
    }

    /**
     * ���ε���ǰ������, ������<code>AOP</code>�����ʹ��. ������Ҫ�޸������������Ҫ��ѯ
     * �����־, �Ծ����Ƿ�Ӧ����Щ����. ��ȫ�е�����Ӧ�ò��������������. �����ǰִ���߳�
     * û��������Ϣ, ʲôҲ����
     */
    public static void concealQuery() {
        ConditionInfo info = getContextCondition();
        info.setConcealQuery(true);
    }
    
    /**
     * �����ǰִ���߳�������������Ϣ, ������������ٴλָ�������Ϣ. ��ִ��<code>DAO</code>
     * ��ѯʱ��ҪӦ���������. �����ǰִ���߳�û��������Ϣ��ʲôҲ����
     */
    public static void recoverQuery() {
        ConditionInfo info = getContextCondition();
        info.setConcealQuery(false);
    }

    /**
     * ��ѯ��ǰִ���߳��Ƿ����ε���Ӧ����<code>DAO</code>����������, ���е���Բ�ѯ������
     * <code>AOP</code>�����Ӧ�÷���ǰ��Ҫ��ѯ�����־, ����ȷ�Ƿ�Ӧ������.�����ǰִ����
     * ��û��������Ϣ, ʲôҲ����
     * @return �Ƿ����ε��˲�ѯ����, �������<code>true</code>, �Ͳ�ҪӦ����Щ����
     */
    public static boolean isConcealQuery() {
        return getContextCondition().isConcealQuery();
    }
    
    public static net.silencily.sailing.framework.codename.UserCodeName getContextUser() {
    	SecurityContext securityContext = SecurityContextHolder.getContext();
    	
		Authentication authentication = securityContext.getAuthentication();
		if(authentication != null){
			Object principal = authentication.getPrincipal();
			UserDetails ud = (UserDetails)principal;
			UserCodeName cn = new UserCodeName();
            cn.setName(ud.getUsername());
            cn.setUsername(ud.getUsername());
            contextUser.set(cn);
		}
        return (UserCodeName) ContextInfo.contextUser.get();
    }
    
    public static void clear() {
        currentUser.set(null);
        contextUser.set(null);
        contextCondition.set(null);
    }

    public static User getCurrentUser() {
        if (currentUser.get() == null) {
//            AuthenticationService service = (AuthenticationService) ServiceProvider.getService(AuthenticationService.SERVICE_NAME);
//            User user = null;
//            try {
//                user = service.legacy();
//                if (logger.isInfoEnabled()) {
//                    logger.info("������ǰ��¼�û�:" + user.getUsername());
//                }
//            } catch (Exception e) {
//                /* ���������û�û�е�¼ʱ�����, ��������վ���� */
//                user = User.EMPTY_USER;
//                if (logger.isInfoEnabled()) {
//                    logger.info("������ǰ��¼�û�,AuthenticationService.legacy()����", e);
//                }
//            }
//            return user;
            
            //User user = service.legacy();
        	SecurityContext securityContext = SecurityContextHolder.getContext();
    		Authentication authentication = securityContext.getAuthentication();
    		Object principal = authentication.getPrincipal();
    		UserDetails ud = (UserDetails)principal;
    		
            List list = new LinkedList();
            User user = new User("admin","Test","Admin","Administrator",list);
            list.add("flaw.equipment.dqzg");
            list.add("flaw.watch");
            list.add("flaw.launch.stop.mainMachine.safety");
            list.add("admin");
            list.add("hr.department");
            list.add("hr.superior");
            list.add("material.manager");
            
            user.setChineseName("");
            user.setUsername(ud.getUsername());
            
            currentUser.set(user);
            if (logger.isDebugEnabled()) {
                logger.debug("ContextInfo.getCurrentUser,�߳�" + Thread.currentThread().getName() + ",��Authentication����:" + user.getUsername() + "," + user.getChineseName());
            }
        }
        return (User) currentUser.get();
    }
}
