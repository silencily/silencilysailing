package net.silencily.sailing.basic.security.acegi.userdetails.hibernate;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.silencily.sailing.basic.security.acegi.userdetails.User;
import net.silencily.sailing.basic.sm.domain.TblCmnDept;
import net.silencily.sailing.basic.sm.domain.TblCmnEntity;
import net.silencily.sailing.basic.sm.domain.TblCmnEntityMember;
import net.silencily.sailing.basic.sm.domain.TblCmnPermission;
import net.silencily.sailing.basic.sm.domain.TblCmnRole;
import net.silencily.sailing.basic.sm.domain.TblCmnRolePermission;
import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.basic.sm.domain.TblCmnUserMember;
import net.silencily.sailing.basic.sm.domain.TblCmnUserRole;
import net.silencily.sailing.security.acegi.userdetails.DisabledUserException;
import net.silencily.sailing.security.acegi.userdetails.RepeatFailureException;
import net.silencily.sailing.security.model.CmnEntity;
import net.silencily.sailing.security.model.CmnEntityMember;
import net.silencily.sailing.security.model.DataAccessType;
import net.silencily.sailing.security.model.DefaultCurrentUser;
import net.silencily.sailing.security.model.Dept;
import net.silencily.sailing.security.model.FieldPermission;
import net.silencily.sailing.security.model.RWCtrlType;
import net.silencily.sailing.security.model.UrlPermission;
import net.silencily.sailing.security.model.UserEntityMember;
import net.silencily.sailing.utils.SecurityUtils;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


/**
 * 实现自己的 {@link org.acegisecurity.userdetails.UserDetailsService} 以便组装
 * {@link net.silencily.sailing.security.acegi.userdetails.qware.security.acegi.userdetails.User}
 * 
 * @see org.acegisecurity.userdetails.UserDetailsService
 * @see net.silencily.sailing.security.acegi.userdetails.qware.security.acegi.userdetails.User
 * @author tongjq
 * @version 1.0
 */
public class HibernateAuthenticationDao extends HibernateDaoSupport implements
        UserDetailsService {
    private static final int FOLDER_NODE = 0;
    private static final int URL_NODE = 1;
    private static final int FIELD_NODE = 2;
    public  static final int ATTMEPT_LIMIT = 5;
    private boolean isSingleSignOn = false;
    
    /**
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @see net.sf.acegisecurity.providers.dao.AuthenticationDao#loadUserByUsername(java.lang.String)
     */
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException, RepeatFailureException, DisabledUserException, DataAccessException {
        TblCmnUser tcu = getTblCmnUser(username);
        User user = new User();
        if (!tcu.getStatus().equals("1")) {
            throw new DisabledUserException("User " + username
                    + " has been disabled");
        }
        if (tcu.getFailedTimes() != null && Integer.parseInt(tcu.getFailedTimes()) >= ATTMEPT_LIMIT) {
            throw new RepeatFailureException("User " + username
                    + " has been locked");
        }

        // 设定acegi user
        user.setUsername(tcu.getEmpCd());
        if (!isSingleSignOn) {
//      非单点登录的时候用用户表的密码作比较
            user.setPassword(tcu.getPassword());
        } else {
//      单点登录的时候用""作比较（因为认证已经在前处理通过，这里只是取登录信息）
            user.setPassword(SecurityUtils.passwordHex(""));
            this.isSingleSignOn = false;
        }
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        // 设定Dept
        Dept dept = new Dept();
        fetchDept(dept,tcu.getTblCmnDept());

        // 设定CurrentUser
        DefaultCurrentUser dcu = new DefaultCurrentUser();
        dcu.setUserId(tcu.getId());
        dcu.setUserName(tcu.getEmpName());
        dcu.setEmpCd(tcu.getEmpCd());
        dcu.setDept(dept);
        dcu.setEmpId(tcu.getId());
        
        // 设定部门以及子部门
        HashSet hs = new HashSet();
        this.fetchSubDept(hs, tcu.getTblCmnDept());
        dcu.setSubDeptIds(hs);
        
        // 设定CurrentUser的权限(业务逻辑用)
        HashMap urlPermissions = this.getUrlPermissions(tcu);
        dcu.setUrlPermissions(urlPermissions);
        // 设定角色
        Set roles = tcu.getRoles();
        HashMap HsRoles = new HashMap();
        for (Iterator iter = roles.iterator(); iter.hasNext();) {
            TblCmnUserRole uRole = (TblCmnUserRole) iter.next();
            HsRoles.put(uRole.getTblCmnRole().getId(), uRole.getTblCmnRole().getRoleCd());
        }
        dcu.setRoles(HsRoles);
        user.setCurrentUser(dcu);
        
        // 设定User的权限(acegi)
        List dbAuths = new LinkedList();
        for (Iterator iter = urlPermissions.keySet().iterator(); iter.hasNext();) {
            String url = (String) iter.next();
            GrantedAuthority authority = new GrantedAuthorityImpl(url);
            dbAuths.add(authority);
        }
        user.setAuthorities((GrantedAuthority[]) dbAuths
                .toArray(new GrantedAuthority[0]));
        // // 强制初始化 lazy load 对象, 因为 user 对象要使用在客户端应用中, 有可能不设置 OpenSessionInView
        // Hibernate.initialize(user.getOrganization());
        // Hibernate.initialize(user.getUserProps());
//***********************************************************************************************
		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnUserMember.class);
		dc.add(Restrictions.eq("delFlg", "0"));
        dc.createAlias("tblCmnUser", "tblCmnUser")
				.add(Restrictions.eq("tblCmnUser.empCd", username));
		//dc.add(Restrictions.eq("userId.emp.empCd", tcu.getId()));        
		List dataSecurityList = getHibernateTemplate().findByCriteria(dc);
//		TblCmnUserMember temp = new TblCmnUserMember();
//		List dataSecurityList = new ArrayList();
//		dataSecurityList.add(temp);

		Map<String, CmnEntity> dataSecurityMap = new HashMap<String, CmnEntity>();
		for (int i = 0; i < dataSecurityList.size(); i++) {
			TblCmnUserMember cum = (TblCmnUserMember) dataSecurityList.get(i);
			TblCmnEntityMember cem = cum.getTblCmnEntityMember();	
			TblCmnEntity ce = cem.getTblCmnEntity();
			
			UserEntityMember userEntityMember = new UserEntityMember();
			CmnEntityMember cmnEntityMember = new CmnEntityMember();
			CmnEntity cmnEntity = dataSecurityMap.get(ce.getClassName());
			if (cmnEntity == null) {
				cmnEntity = new CmnEntity();
				cmnEntity.setEntityClassName(ce.getClassName());
			}
			userEntityMember.setCreateScope(cum.getCreateScope());
			userEntityMember.setDeleteScope(cum.getDeleteScope());
			userEntityMember.setUpdateScope(cum.getUpdateScope());
			userEntityMember.setSearchScope(cum.getSearchScope());
			
			cmnEntityMember.setType(cem.getType());
			cmnEntityMember.setName(cem.getMemberName());
			cmnEntityMember.setChildrenNode(cem.getChildrensName());			
			cmnEntityMember.setUserEntityMemberId(userEntityMember);			
			
			cmnEntity.getEntityMembers().add(cmnEntityMember);
			
//			if (StringUtils.isNotBlank(cem.getChildrenNode())) {
//				cmnEntityMember.setChildrenNode(cem.getChildrenNode());
//				if (cem.getType().endsWith("TblHrDept") || cem.getType().endsWith("DepartmentCodeName")) {
//			        // 设定部门以及子部门
//			        HashSet set = new HashSet(0);
//			        if (!StringUtils.isBlank(userEntityMember.getSearchScope())) {
//			        	this.fetchSubDept(set, (TblHrDept)getHibernateTemplate().load(TblHrDept.class, userEntityMember.getValue('S')));
//			        	userEntityMember.setSearchIds(set);
//			        }			        
//			        if (!StringUtils.isBlank(userEntityMember.getCreateScope())) {
//			        	set = new HashSet(0);
//			        	this.fetchSubDept(set, (TblHrDept)getHibernateTemplate().load(TblHrDept.class, userEntityMember.getValue('C')));
//			        	userEntityMember.setCreateIds(set);
//				    }			        
//			        if (!StringUtils.isBlank(userEntityMember.getUpdateScope())) {
//			        	set = new HashSet(0);
//			        	this.fetchSubDept(set, (TblHrDept)getHibernateTemplate().load(TblHrDept.class, userEntityMember.getValue('U')));
//			        	userEntityMember.setUpdateIds(set);
//				    }			       
//			        if (!StringUtils.isBlank(userEntityMember.getDeleteScope())) {
//			        	 set = new HashSet(0);
//			        	this.fetchSubDept(set, (TblHrDept)getHibernateTemplate().load(TblHrDept.class, userEntityMember.getValue('D')));
//			        	userEntityMember.setDeleteIds(set);
//			        }			        
//				} 
//			}
			dataSecurityMap.put(ce.getClassName(), cmnEntity);
		}
		//SecurityContextInfo.setDataSecurity(dataSecurityMap);
		dcu.setDataSecurityMap(dataSecurityMap);
//***********************************************************************************************       
        return user;
    }

    /**
     * 
     * 功能描述 取得合并当前用户权限
     * @param empInfo
     * @return 用户权限
     * 2007-11-28 上午10:42:11
     * @version 1.0
     * @author tongjq
     */
    private HashMap getUrlPermissions(TblCmnUser tcu){
        HashMap urlPermissions = new HashMap();

        Calendar nowDate = Calendar.getInstance();
        nowDate.clear(Calendar.MILLISECOND);
        nowDate.clear(Calendar.SECOND);
        nowDate.clear(Calendar.MINUTE);
        nowDate.clear(Calendar.HOUR);
        nowDate.clear(Calendar.HOUR_OF_DAY);
        Date dt = nowDate.getTime();

        //取得用户角色集合
        Set roles = tcu.getRoles();
        for (Iterator iter = roles.iterator(); iter.hasNext();) {
            //取得用户角色
            TblCmnUserRole uRole = (TblCmnUserRole) iter.next();
            if (uRole.getTblCmnUserByConsignerId() != null 
            	&& !StringUtils.isBlank(uRole.getTblCmnUserByConsignerId().getId())
                && ( uRole.getBeginTime().after(dt) || uRole.getInvalidTime().before(dt))){
            //有效期外的授权角色不处理
                continue;
            }
            
            //取得角色
            TblCmnRole tcRole = uRole.getTblCmnRole();
            //取得角色权限集合
            Set permissions = tcRole.getTblCmnRolePermissions();
            for (Iterator iterp = permissions.iterator(); iterp.hasNext();) {
                //取得角色权限
                TblCmnRolePermission tcrp = (TblCmnRolePermission) iterp.next();
                //取得权限
                TblCmnPermission tcp = tcrp.getTblCmnPermission();
                this.addUrlPermissions(urlPermissions, tcp, tcrp.getRwCtrl(),
                        tcrp.getReadAccessLevel(), tcrp.getWriteAccessLevel());
            }
        }

//        //取得用户权限集合
//        Set permissions = tcu.getUserPermissions();
//        for (Iterator iter = permissions.iterator(); iter.hasNext();) {
//            //取得用户权限
//            TblCmnUserPermission tcup = (TblCmnUserPermission) iter.next();
//            if (tcup.getTblCmnUserByConsignerId() != null 
//            	&& !StringUtils.isBlank(tcup.getTblCmnUserByConsignerId().getId())
//                && ( tcup.getBeginTime().after(dt) || tcup.getInvalidTime().before(dt))){
//            //有效期外的授权权限不处理
//                continue;
//            }
//            //取得权限
//            TblCmnPermission tcp = tcup.getTblCmnPermission();
//            this.addUrlPermissions(urlPermissions, tcp, tcup.getRwCtrl(),
//                    tcup.getReadAccessLevel(), tcup.getWriteAccessLevel());
//        }
        return urlPermissions;
    }
    /**
     * 
     * 功能描述
     * @param urlPermissions
     * @param tcp
     * @param accessLevel
     * 2007-11-28 下午05:54:13
     * @version 1.0
     * @author tongjq
     */
    private void addUrlPermissions(HashMap urlPermissions,
            TblCmnPermission tcp,
            String rwCtrl,
            String readAccessLevel,
            String writeAccessLevel) {
 /*****************************************************************
  * 去掉数据项的权限*/    	
		rwCtrl = "2";
		readAccessLevel = "0";
		writeAccessLevel = "0";
 /*****************************************************************/
        String url = tcp.getUrl();
        int nodeType = Integer.parseInt(tcp.getNodetype());//节点类型
        if (nodeType == FIELD_NODE) {
            url = tcp.getFather().getUrl();
        }
        if (org.apache.commons.lang.StringUtils.isBlank(url)){
        //url空的时候不处理
            return;
        }
        //取得权限比较用URL
        String paras[] = url.split("&");
        //取得&之前(第二个参数之前)的字符串作
        url = paras[0];
        //如果第二个参数是stepType则也拼到权限比较用URL上
        if (paras.length > 1 && paras[1].indexOf("stepType=") != -1) {
            url = url + "&" + paras[1];
        }
        
        int fieldPermissionType;//读取控制类型
        fieldPermissionType = Integer.parseInt(rwCtrl);
//        if (org.apache.commons.lang.StringUtils.isBlank(tcp.getFieldpermissionType())){
//        //FieldpermissionType空的时候默认为可编辑
//            fieldPermissionType = RWCtrlType.EDIT;
//        } else {
//            fieldPermissionType = Integer.parseInt(tcp.getFieldpermissionType());
//        }

        if (urlPermissions.containsKey(url)){
        //存在url的时候
            UrlPermission up = (UrlPermission)urlPermissions.get(url);

            switch (nodeType){
            case FOLDER_NODE:
            case URL_NODE:
                if (Integer.parseInt(readAccessLevel) < up.getDataAccessLevelRead()){
                    up.setDataAccessLevelRead(Integer.parseInt(readAccessLevel));
                }
                if (Integer.parseInt(writeAccessLevel) < up.getDataAccessLevelWrite()){
                    up.setDataAccessLevelWrite(Integer.parseInt(writeAccessLevel));
                }
                if (fieldPermissionType > up.getRwCtrlType()){
                    up.setRwCtrlType(fieldPermissionType);
                }
                break;
            case FIELD_NODE:
                HashMap fieldPerms = up.getFieldPerms();
                if (fieldPerms.containsKey(tcp.getPermissionCd())){
                //权限标识存在时
                    FieldPermission fp = (FieldPermission)fieldPerms.get(tcp.getPermissionCd());
                    if (fieldPermissionType > fp.getRwCtrlType()){
                        fp.setRwCtrlType(fieldPermissionType);
                    }
                } else {
                    FieldPermission fp = new FieldPermission();
                    fp.setRwCtrlType(fieldPermissionType);
                    fieldPerms.put(tcp.getPermissionCd(), fp);
                }
                break;
            }
        } else {
        //不存在url的时候
            UrlPermission up = new UrlPermission();
            HashMap fieldPerms = new HashMap();
            switch (nodeType){
                case FOLDER_NODE:
                case URL_NODE:
                    up.setDataAccessLevelRead(Integer.parseInt(readAccessLevel));
                    up.setDataAccessLevelWrite(Integer.parseInt(writeAccessLevel));
                    up.setRwCtrlType(fieldPermissionType);
                    up.setFieldPerms(fieldPerms);
                    urlPermissions.put(url, up);
                    break;
                case FIELD_NODE:
                    up.setDataAccessLevelRead(DataAccessType.SELF);
                    up.setDataAccessLevelWrite(DataAccessType.FORBID);
                    up.setRwCtrlType(RWCtrlType.SIGHTLESS);
                    FieldPermission fp = new FieldPermission();
                    fp.setRwCtrlType(fieldPermissionType);
                    fieldPerms.put(tcp.getPermissionCd(), fp);
                    up.setFieldPerms(fieldPerms);
                    urlPermissions.put(url, up);
                    break;
            }
        }

    }

    /**
     * 
     * 功能描述 取得所有子部门
     * @param hs
     * @param parent
     * 2008-1-10 下午05:32:17
     * @version 1.0
     * @author tongjq
     */
    private void fetchSubDept(HashSet hs, TblCmnDept parent) {
        if (hs.contains(parent.getId())) {
            return;
        }
        hs.add(parent.getId());
        DetachedCriteria criteria = DetachedCriteria.forClass(TblCmnDept.class);
        criteria.add(Restrictions.eq("parent", parent));
        criteria.add(Restrictions.eq("delFlg","0"));
        List list = getHibernateTemplate().findByCriteria(criteria);
        for (Iterator iter = list.iterator(); iter.hasNext();) {
        	TblCmnDept thd = (TblCmnDept) iter.next();
            this.fetchSubDept(hs, thd);
        }
    }

    /**
     * 
     * 功能描述 根据是否是班组来取得相应的部门
     * @param dept
     * @param son
     * 2008-1-10 下午05:32:57
     * @version 1.0
     * @author tongjq
     */
    private void fetchDept(Dept dept,TblCmnDept son) {
        dept.setId(son.getId());
        dept.setName(son.getDeptName());
        if (son.getIsGroup().getCode().equals("SHI") && !son.isRootNode()) {
            dept.setDeptId(son.getParent().getId());
            dept.setDeptName(son.getParent().getDeptName());
        } else {
            dept.setDeptId(son.getId());
            dept.setDeptName(son.getDeptName());
        }
        return;
    }

    /**
     * Allows subclasses to add their own granted authorities to the list to be
     * returned in the <code>User</code>.
     * 
     * @param username
     *            the username, for use by finder methods
     * @param authorities
     *            the current granted authorities, as populated from the
     *            <code>authoritiesByUsername</code> mapping
     */
    protected void addCustomAuthorities(String username, List authorities) {
    }

    public Class getEntityClass() {
        return User.class;
    }

    public void setIsSingleSignOn(boolean isSingleSignOn) {
        this.isSingleSignOn = isSingleSignOn;
    }

    public TblCmnUser getTblCmnUser(String username) 
                        throws UsernameNotFoundException {
        DetachedCriteria criteria = DetachedCriteria.forClass(TblCmnUser.class);
        criteria.add(Restrictions.eq("delFlg","0"));
        //criteria.createAlias("emp","emp");
        criteria.add(Restrictions.eq("empCd", username));
        //criteria.add(Restrictions.eq("emp.delFlg", "0"));
        List list = getHibernateTemplate().findByCriteria(criteria);
        if (list.isEmpty()) {
            throw new UsernameNotFoundException("User " + username
                    + " not found");
        }
//        if (list.size() > 1) {
//            // tongjq throw new UsernameRepeatException("User " + username + "
//            // repeats ");
//            throw new BadCredentialsException("User " + username + " repeats ");
//        }

        return (TblCmnUser) list.get(0);
    }

}
