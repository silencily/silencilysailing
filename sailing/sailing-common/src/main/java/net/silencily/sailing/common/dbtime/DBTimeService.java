package net.silencily.sailing.common.dbtime;


/**
 * 
 * @author wenjb
 * @version 1.0
 */
public interface DBTimeService {
	
	public static final String SERVICE_NAME = "common.DBTimeService";
	
	/**
	 * 
	 * 功能描述 获取当前数据库的时间
	 * 
	 * @param URL
	 * @return 构造好的List，可直接设置到Tree的list里 2007-11-28 上午09:46:14
	 * @version 1.0
	 * @author lihe
	 * @throws Exception 
	 */
	public String getDBTime();
	
}
