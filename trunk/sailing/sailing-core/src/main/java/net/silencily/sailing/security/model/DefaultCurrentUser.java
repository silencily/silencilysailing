package net.silencily.sailing.security.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;


public class DefaultCurrentUser implements Serializable,CurrentUser  {
	/**
	 * �û���
	 */
	private String userName;
	/**
	 * �û�id���û���������
	 */
	private String userId;
	/**
	 * Ա����Ϣ��id
	 */
	private String empId;
	/**
	 * �û����ڲ���
	 */
	private Dept dept;
	/**
	 * �û�ӵ�еĹ���Ȩ��,keyΪҳ��url��valueΪ{@link UrlPermission}���͡�
	 */
	private HashMap urlPermissions;
	/**
	 * �û�ӵ�еĽ�ɫ��,keyΪ��ɫID,valueΪ��ɫ��ʶ��
	 */
	private HashMap roles;
	/**
	 * �û����ڲ��ż��Ӳ���ID��������ΪString ���͵Ĳ���ID
	 */
	private HashSet subDeptIds;
	
	/**
	 * ��¼�û���Ӧ��Ա�����(��¼��)
	 */
	private String empCd;
	
	private Map<String, CmnEntity> dataSecurityMap = null;
	
    /**
     * 
     * ȡ�õ�ǰ�û������н�ɫ
     * @return keyΪroleId,valueΪroleCd
     * 2007-11-20 ����03:06:14
     * @version 1.0
     * @author yushn
     */
    public HashMap getRoles() {
        return roles;
    }
    /**
     * 
     * �жϵ�ǰ�û��Ƿ�ӵ��ָ���Ľ�ɫ
     * @param roleID ��ɫID
     * @return
     * 2007-11-20 ����03:06:14
     * @version 1.0
     * @author yushn
     */
	public boolean hasRole(String roleId)
	{
		return roles.containsKey(roleId);
	}
    /**
     * 
     * �жϵ�ǰ�û��Ƿ�ӵ�д���Ľ�ɫ���еĽ�ɫ
     * @param roleId ��ɫID����Ԫ��ΪString���ͣ�HashSet������Ϊ�˿��ٱȶ�
     * @return ��ǰ�û�ӵ�еĽ�ɫID���봫��Ľ�ɫ���Ľ�����Ϊ�վͷ���true,���򷵻�false
     * 2007-11-20 ����02:49:32
     * @version 1.0
     * @author yushn
     */
	public boolean hasRole(HashSet roleId)
	{
		Set base;
		Set loop;
		if(null == roles||null == roleId)
			return false;
		if(roles.size() > roleId.size())
		{
			base = roles.keySet();
			loop = roleId;
		}
		else
		{
			base = roleId;
			loop = roles.keySet();
		}
		for(Iterator it = loop.iterator();it.hasNext();)
		{
			if(base.contains(it.next()))
			{
				return true;
			}
		}
		return false;
	}
    /**
     * 
     * �жϵ�ǰ�û��Ƿ�ӵ��ָ���Ľ�ɫ
     * @param roleCode ��ɫ��ʶ����
     * @return
     * 2007-11-20 ����03:06:14
     * @version 1.0
     * @author tongjq
     */
    public boolean hasRoleCd(String roleCode)
    {
        return roles.containsValue(roleCode);
    }
    /**
     * 
     * �жϵ�ǰ�û��Ƿ�ӵ�д���Ľ�ɫ���еĽ�ɫ
     * @param roleCodes �û����뼯��Ԫ��ΪString���ͣ�HashSet������Ϊ�˿��ٱȶ�
     * @return ��ǰ�û�ӵ�еĽ�ɫ���봫��Ľ�ɫ���Ľ�����Ϊ�վͷ���true,���򷵻�false
     * 2007-11-20 ����02:49:32
     * @version 1.0
     * @author tongjq
     */
    public boolean hasRoleCd(HashSet roleCodes)
    {
        Set base;
        Set loop;
        if(null == roles||null == roleCodes)
            return false;
        if(roles.size() > roleCodes.size())
        {
            base = new HashSet(roles.values());
            loop = roleCodes;
        }
        else
        {
            base = roleCodes;
            loop = new HashSet(roles.values());
        }
        for(Iterator it = loop.iterator();it.hasNext();)
        {
            if(base.contains(it.next()))
            {
                return true;
            }
        }
        return false;
    }
	/**
	 * 
	 * ��ȡ��ǰ�û���ָ��ҳ���µ�ָ���������Ȩ������
	 * ��������˾����������Ȩ�����ͣ��򷵻����õ�Ȩ�����ͣ����򷵻�ҳ���Ĭ��Ȩ�����͡�
	 * @param pageUrl ҳ��url
	 * @param permissionCode ������Ȩ�ޱ�ʶ����
	 * @return ������Ŀ�Ȩ������
	 * 		���������ɼ���ֻ�����ɱ༭
	 * 		���ʱ��ʹ��{@link RWCtrlType}�еĳ������Խ����ж�
	 * 2007-11-20 ����03:35:48
	 * @version 1.0
	 * @author yushn
	 */
	public int getFieldRWCtrlType(String pageUrl,String permissionCode)
	{
		UrlPermission urlp = (UrlPermission)urlPermissions.get(pageUrl);
		if(null == urlp)
			return RWCtrlType.SIGHTLESS;
		HashMap fps = urlp.getFieldPerms();
		if(null == fps || StringUtils.isBlank(permissionCode))
			return urlp.getRwCtrlType();
		FieldPermission fp = (FieldPermission)fps.get(permissionCode);
		if(null == fp)//δ����������Ȩ����ʹ��ҳ��Ĭ�ϵĶ�д��������
			return urlp.getRwCtrlType();
		return fp.getRwCtrlType();
	}
	/**
	 * ��ȡָ��ҳ���Ĭ�϶�д�������͡�
	 * ��������
	 * @param pageUrl
	 * @return ������Ŀ�Ȩ������
	 * 		���������ɼ���ֻ�����ɱ༭
	 * 		���ʱ��ʹ��{@link RWCtrlType}�еĳ������Խ����ж�
	 * 2007-11-30 ����11:19:30
	 * @version 1.0
	 * @author yushn
	 */
	public int getPageDefaultRWCtrlType(String pageUrl)
	{
		UrlPermission urlp = (UrlPermission)urlPermissions.get(pageUrl);
		if(null == urlp)
			return RWCtrlType.READ_ONLY;
		return urlp.getRwCtrlType()==RWCtrlType.EDIT ? RWCtrlType.EDIT : RWCtrlType.READ_ONLY;
	}
	/**
	 * ��ȡ��ǰ�û���ָ����¼�Ķ�д��������
	 * ��������
	 * @param deptId ��¼�Ĵ����߲���Id
	 * @param empCd	 ��¼�Ĵ�����ϵͳ��¼����
	 * @return ��д��������
	 * 		���ʱ��ʹ��{@link RWCtrlType}�еĳ������Խ����ж�
	 * 2007-11-28 ����07:19:12
	 * @version 1.0
	 * @author yushn
	 */
	public int getRowRWCtrlType(String pageUrl,String deptId,String empCd)
	{
		UrlPermission urlp = (UrlPermission)urlPermissions.get(pageUrl);
		if(null == urlp)
			return RWCtrlType.SIGHTLESS;
		int wt = urlp.getDataAccessLevelWrite();
		switch(wt)
		{
		case DataAccessType.SYSTEM:
			return RWCtrlType.EDIT;
		case DataAccessType.DEPT:
			//���ڲ��ŵıȽϻ�Ҫ�жϴ���Ĳ����Ƿ��ǵ�ǰ�û����ڲ��ŵ��Ӳ���!!!
			if(this.subDeptIds.contains(deptId))
			{
				return RWCtrlType.EDIT;
			}
		case DataAccessType.SELF:
			//���ڲ��ŵıȽϻ�Ҫ�жϴ���Ĳ����Ƿ��ǵ�ǰ�û����ڲ��ŵ��Ӳ���!!!
			if(this.subDeptIds.contains(deptId)&&StringUtils.equals(this.empCd, empCd))
			{
				return RWCtrlType.EDIT;
			}
		default://���ݷ��ʼ���λ����ֹ��
			return RWCtrlType.READ_ONLY;
		}
	}
	/**
	 * 
	 * ��ȡָ��ҳ�������������Ĭ��Ȩ������
	 * @param pageUrl
	 * @return ҳ�������������Ĭ��Ȩ������
	 * 		ֻ��ֻ���Ϳɱ༭��������
	 * 		���ʱ��ʹ��{@link RWCtrlType}�еĳ������Խ����ж�
	 * 2007-11-20 ����05:04:41
	 * @version 1.0
	 * @author yushn
	 */
//	public int getPagePermission(String pageUrl)
//	{
//		UrlPermission urlp = (UrlPermission)urlPermissions.get(pageUrl);
//		if(null == urlp)
//			return RWCtrlType.SIGHTLESS;
//		return urlp.getDataAccessLevel();
//	}
	public String getUserId(){
		return this.userId;
	}
	/**
	 * ��ȡ��¼�û���Ӧ��Ա�����(��¼��)
	 * ��������
	 * @return
	 * 2007-11-28 ����07:04:27
	 * @version 1.0
	 * @author yushn
	 */
	public String getEmpCd(){
		return this.empCd;
	}
	/**
	 * ��ȡ��¼�û�������
	 * ������ʾ
	 * @return
	 * 2007-11-26 ����07:25:17
	 * @version 1.0
	 * @author yushn
	 */
	public String getUserName(){
		return this.userName;
	}
	/**
	 * ��ȡ����id��������
	 * �˽ӿ���Ϊ�˷����жϲ��ż�����Ȩ��
	 * @return
	 * 2007-11-26 ����07:25:46
	 * @version 1.0
	 * @author yushn
	 */
	public String getDeptId(){
		return this.dept.getId();
	}
	/**
	 * ��ȡ��¼�û���������
	 * ������ʾ
	 * @return
	 * 2007-11-26 ����07:30:19
	 * @version 1.0
	 * @author yushn
	 */
	public String getDeptName(){
		return this.dept.getName();
	}
	
	/**
	 * 
	 * ��ȡ��¼��
	 * @return
	 * 2007-11-30 ����05:10:00
	 * @version 1.0
	 * @author yushn
	 */
	public String getLoginName()
	{
		return empCd;
	}

	/**
	 * ��ȡȨ��
	 * @return
	 * 2007-12-05 ����04:30:00
	 * @version 1.0
	 * @author tongjq
	 */
	public HashMap getUrlPermissions()
	{
		return urlPermissions;
	}
	
	public Dept getDept() {
		return dept;
	}
	public void setDept(Dept dept) {
		this.dept = dept;
	}
	public void setRoles(HashMap roles) {
		this.roles = roles;
	}
	public void setUrlPermissions(HashMap urlPermissions) {
		this.urlPermissions = urlPermissions;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setEmpCd(String empCd) {
		this.empCd = empCd;
	}
	public HashSet getSubDeptIds() {
		return subDeptIds;
	}
	public void setSubDeptIds(HashSet subDeptIds) {
		this.subDeptIds = subDeptIds;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	/**
	 * ��ȡ����ID
	 * @return ����ǰ��鷵�ص��ǰ���id������Ϊ��ֵ��
	 */
	public String getGroupId(){
		//�趨����ֵ�ĳ�ʼֵ��
		String groupId = "";
		//��ȡid�������ǲ���idҲ�����ǰ���id
		String id = this.dept.getId();
		
		//��ȡ����id�����id���ǰ���ʱ���ð��������Ĳ���id���������
		String deptId = this.dept.getDeptId();
		
		//��ΪId�п����ǲ���id��Ҳ�����ǰ���id����deptId���ǲ���id�����Ե��ǰ���ʱ��id��deptId���ȡ�
		if(!"".equals(id) && !"".equals(deptId) && !id.equalsIgnoreCase(deptId)){
			groupId = id;
		}
		
		return groupId;
	}
	/**
	 * ��ȡ��ʵ�Ĳ���id,���ǵ������ǰ�������
	 * @return
	 */
	public String getRealDeptId()
	{
		//��ȡid�������ǲ���idҲ�����ǰ���id
		String id = this.dept.getId();
		
		//��ȡ����id�����id���ǰ���ʱ���ð��������Ĳ���id���������
		String deptId = this.dept.getDeptId();
	
		if(!"".equals(id) && !"".equals(deptId) && !id.equalsIgnoreCase(deptId)){
			return deptId;
		}else{
			return id;
		}
	}
    public Map<String, CmnEntity> getDataSecurityMap() {
        return dataSecurityMap;
    }
    public void setDataSecurityMap(Map<String, CmnEntity> dataSecurityMap) {
        this.dataSecurityMap = dataSecurityMap;
    }
	
}
