package net.silencily.sailing.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class SecurityUtils {
	/**
	 * 加密密码
	 * @param 密码明文,可以为null,表示要获取默认口令的密文
	 * @return 密码密文
	 * 2007-11-23 下午04:04:38
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
