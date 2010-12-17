package net.silencily.sailing.hibernate3;

import net.silencily.sailing.security.SecurityContextInfo;

import org.apache.commons.lang.StringUtils;


/**
 * @author wenjb
 *
 */
public class SystemPermissionCheckImpl implements SystemPermissionCheck {

	/* (non-Javadoc)
	 * @see com.qware.hibernate3.SystemPermissionCheck#systemNeedPermissionCheck()
	 */
	public static boolean systemNeedPermissionCheck() {
		// ���ݵ�ǰ��URL���õ���ϵͳ�����ƣ��ж��Ƿ���Ҫ���ι���
		String url = SecurityContextInfo.getCurrentPageUrl();
		if(StringUtils.isBlank(url)){
			return false;
		}else{
			if(url.indexOf("/hr/") != -1 || url.indexOf("/rm/") != -1){
				return true;
			}
		}
		return false;
	}

}
