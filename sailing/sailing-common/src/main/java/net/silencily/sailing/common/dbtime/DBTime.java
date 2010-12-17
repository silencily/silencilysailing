package net.silencily.sailing.common.dbtime;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.silencily.sailing.container.ServiceProvider;


/**
 * 
 * @author wenjb
 * @version 1.0
 */
public class DBTime {

	/**
	 * 功能描述
	 * 
	 * @param 2008-4-14
	 *            下午04:25:25
	 * @version 1.0
	 * @author wenjb
	 * @return
	 */
	public static Date getDBTime() {
		DBTimeService dBTimeService = (DBTimeService) ServiceProvider
				.getService(DBTimeService.SERVICE_NAME);
		String dateStr = dBTimeService.getDBTime();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = simpleDateFormat.parse(dateStr);
		} catch (Exception e) {
			throw new RuntimeException("获取数据库时间出错，请与系统管理员联系。");
		}
		return date;
	}
}
