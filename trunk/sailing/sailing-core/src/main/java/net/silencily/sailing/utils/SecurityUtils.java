package net.silencily.sailing.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class SecurityUtils {
	/**
	 * ��������
	 * @param ��������,����Ϊnull,��ʾҪ��ȡĬ�Ͽ��������
	 * @return ��������
	 * 2007-11-23 ����04:04:38
	 * @version 1.0
	 * @author yushn
	 */
	public static String passwordHex(String plain)
	{
		if(null == plain)
			return DigestUtils.md5Hex("1");
		else
			return DigestUtils.md5Hex(plain);
	}
}
