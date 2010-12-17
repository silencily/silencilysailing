package net.silencily.sailing.security.model;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;

/**
 * ��ǰ��¼�û����ṩ��ǰ��¼�û���صİ�ȫ��Ϣ
 * @author yushn
 * @version 1.0
 */
public interface CurrentUser {
    /**
     * 
     * ȡ�õ�ǰ�û������н�ɫ
     * @return keyΪroleId,valueΪroleCd
     * 2007-11-20 ����03:06:14
     * @version 1.0
     * @author yushn
     */
    public HashMap getRoles();
	/**
	 * 
	 * �жϵ�ǰ�û��Ƿ�ӵ��ָ���Ľ�ɫ
	 * @param roleID ��ɫID
	 * @return
	 * 2007-11-20 ����03:06:14
	 * @version 1.0
	 * @author yushn
	 */
	public boolean hasRole(String roleId);
	/**
	 * 
	 * �жϵ�ǰ�û��Ƿ�ӵ�д���Ľ�ɫ���еĽ�ɫ
	 * @param roleId ��ɫID����Ԫ��ΪString���ͣ�HashSet������Ϊ�˿��ٱȶ�
	 * @return ��ǰ�û�ӵ�еĽ�ɫID���봫��Ľ�ɫ���Ľ�����Ϊ�վͷ���true,���򷵻�false
	 * 2007-11-20 ����02:49:32
	 * @version 1.0
	 * @author yushn
	 */
	public boolean hasRole(HashSet roleId);
    /**
     * 
     * �жϵ�ǰ�û��Ƿ�ӵ��ָ���Ľ�ɫ
     * @param roleCode ��ɫ��ʶ����
     * @return
     * 2007-11-20 ����03:06:14
     * @version 1.0
     * @author tongjq
     */
    public boolean hasRoleCd(String roleCode);
    /**
     * 
     * �жϵ�ǰ�û��Ƿ�ӵ�д���Ľ�ɫ���еĽ�ɫ
     * @param roleCodes �û����뼯��Ԫ��ΪString���ͣ�HashSet������Ϊ�˿��ٱȶ�
     * @return ��ǰ�û�ӵ�еĽ�ɫ���봫��Ľ�ɫ���Ľ�����Ϊ�վͷ���true,���򷵻�false
     * 2007-11-20 ����02:49:32
     * @version 1.0
     * @author tongjq
     */
    public boolean hasRoleCd(HashSet roleCodes);
	/**
	 * 
	 * ��ȡ��ǰ�û���ָ��ҳ���µ�ָ��������Ķ�д��������
	 * ��������˾���������Ķ�д�������ͣ��򷵻����õĶ�д�������ͣ����򷵻�ҳ���Ĭ��Ȩ�����͡�
	 * @param pageUrl ҳ��url
	 * @param permissionCode ������Ȩ�ޱ�ʶ����
	 * @return ������Ŀ�Ȩ������
	 * 		���������ɼ���ֻ�����ɱ༭
	 * 		���ʱ��ʹ��{@link RWCtrlType}�еĳ������Խ����ж�
	 * 2007-11-20 ����03:35:48
	 * @version 1.0
	 * @author yushn
	 */
	public int getFieldRWCtrlType(String pageUrl,String permissionCode);
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
	public int getPageDefaultRWCtrlType(String pageUrl);
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
	//public int getPagePermission(String pageUrl);
	/**
	 * ��ȡ��ǰ�û���ָ����¼�Ķ�д��������
	 * ��������
	 * @param deptId ��¼�Ĵ����߲���
	 * @param userId	��¼�Ĵ�����
	 * @return ��д��������
	 * 		���ʱ��ʹ��{@link RWCtrlType}�еĳ������Խ����ж�
	 * 2007-11-28 ����07:19:12
	 * @version 1.0
	 * @author yushn
	 */
	public int getRowRWCtrlType(String pageUrl,String deptId,String userId);
	/**
	 * ��ȡ��¼�û�id(����)
	 * �˽ӿ���Ϊ�˷���ϵͳ����ί��Ȩ�޺ͶԼ�¼�����ݷ���Ȩ�޵��ж�
	 * @return
	 * 2007-11-26 ����07:23:56
	 * @version 1.0
	 * @author yushn
	 */
	public String getUserId();
	/**
	 * ��ȡ��¼�û���Ӧ��Ա�����(��¼��)
	 * ��������
	 * @return
	 * 2007-11-28 ����07:04:27
	 * @version 1.0
	 * @author yushn
	 */
	public String getEmpCd();
	/**
	 * ��ȡ��¼�û�������
	 * ������ʾ
	 * @return
	 * 2007-11-26 ����07:25:17
	 * @version 1.0
	 * @author yushn
	 */
	public String getUserName();
	/**
	 * ��ȡ����id��������
	 * �˽ӿ���Ϊ�˷����жϲ��ż�����Ȩ��
	 * @return
	 * 2007-11-26 ����07:25:46
	 * @version 1.0
	 * @author yushn
	 */
	public String getDeptId();
	/**
	 * ��ȡ��¼�û���������
	 * ������ʾ
	 * @return
	 * 2007-11-26 ����07:30:19
	 * @version 1.0
	 * @author yushn
	 */
	public String getDeptName();
	/**
	 * 
	 * ��ȡ��¼��
	 * @return
	 * 2007-11-30 ����05:10:00
	 * @version 1.0
	 * @author yushn
	 */
	public String getLoginName();
	/**
	 * ��ȡȨ��
	 * @return
	 * 2007-12-05 ����04:30:00
	 * @version 1.0
	 * @author tongjq
	 */
	public HashMap getUrlPermissions();
    /**
     * ��ȡ�����Լ��Ӳ���
     * @return
     * 2007-12-05 ����04:30:00
     * @version 1.0
     * @author tongjq
     */
    public HashSet getSubDeptIds();

    /**
     * ��ȡ��¼Ա����Ϣ��id
     * @return
     * 2007-11-26 ����07:30:19
     * @version 1.0
     * @author yushn
     */
    public String getEmpId();
    /**
     * ��ȡ����ID�����ǰ����ʱ�򷵻ص��ǿ�ֵ��""����
     */
    public String getGroupId();
    
    public Dept getDept();
    
	/**
	 * ��ȡ��ʵ�Ĳ���id,���ǵ������ǰ�������
	 * @return
	 */
	public String getRealDeptId();
	
	public Map<String, CmnEntity> getDataSecurityMap();
}