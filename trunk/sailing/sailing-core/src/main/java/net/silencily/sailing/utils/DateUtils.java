package net.silencily.sailing.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import net.silencily.sailing.exception.UnexpectedException;

import org.apache.commons.lang.StringUtils;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

/**
 * 日期类型操作的<code>utility</code>类
 * @author scott
 * @since 2006-4-16
 * @version $Id: DateUtils.java,v 1.1 2010/12/10 10:54:21 silencily Exp $
 *
 */
public class DateUtils {
    private static final Locale chineseLocale = Locale.CHINESE;

    private static DateUtils utils = new DateUtils();

    private static Pattern dateFormatPattern;
    
    private static final String dateString = "EEE MMM dd HH:mm:ss zzz yyyy";
    
    private static Perl5Matcher matcher = new Perl5Matcher();
    
    private DateUtils() {
        try {
            dateFormatPattern = new Perl5Compiler().compile(
                "([0-9]{4}[^0-9][0-9]{1,2}[^0-9][0-9]{1,2}[^0-9\\s]?)?([\\s]?[0-9]{1,2}:[0-9]{1,2}(:[0-9]{1,2}(\\.[0-9]+)?)?)?", 
                Perl5Compiler.CASE_INSENSITIVE_MASK);
        } catch (MalformedPatternException e) {
            throw new UnexpectedException("不能编译解析日期格式的正则表达式", e);
        }
    }
    
    /**
     * 获取系统当前时间，这里使用服务器时间，如果使用数据库时间，则需要重构
     * @return 
     */
    public static Date getCurrentDate(){
    	return net.silencily.sailing.utils.DBTimeUtil.getDBTime();
    }
    
    /**
	 * 对指定的日期转换为 yyyy-MM-dd 的字符
	 * @param date
	 * @return
	 */
	public static String getSimplateFormat(Date date){
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}
    
    /**
     * 返回指定日期在那个月的第一天
     * @param date 依据这个日期转换
     * @return 参数指定日期所在月份的第一天
     */
    public static Date getFirstDayInMonth(Date date) {
        return utils.getFirstDayInMonthi(date);
    }
    
    private Date getFirstDayInMonthi(Date date) {
        Calendar c = Calendar.getInstance(chineseLocale);
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }
    
    /**
     * 返回指定日期的月份中的最后一天
     * @param date 依据这个日期转换
     * @return 参数指定日期所在月份的最后一天
     */
    public static Date getLastDayInMonth(Date date) {
        return utils.getLastDayInMonthi(date);
    }
    
    private Date getLastDayInMonthi(Date date) {
        Calendar c = Calendar.getInstance(chineseLocale);
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }
    
    /**
     * 从日期的字符串形式转换成<code>Date</code>, 对于<code>Timestamp</code>格式的日期损失
     * 了<code>nano-second</code>部分, 对于当前的应用应该不是问题. 支持使用单个字符(unicode)
     * 或使用连字符分隔的年月日格式以及英文日期格式(例如:<code>Sun Apr 16 22:17:51 CST 2006</code>,
     * 这个格式是<code>Date.toString()</code>的输出,为什么输出这个该死的格式?), <b>特别地</b>
     * 对于输入<code>null</code>或<code>empty</code>返回<code>null</code></b>, 这对于
     * <code>web</code>应用来说是合理的. 可以识别的格式共有<ul>
     * <li>yyyy-MM-dd</li>
     * <li>yyyy-MM-dd HH:mm</li>
     * <li>yyyy年MM月dd日</li>
     * <li>yyyy年MM月dd日 HH:mm</li>
     * <li>EEE MMM HH:mm:ss zzz yyyy(<code>Date.toString()</code>的输出格式)</li>
     * </ul>
     * @param str 要转成日期的字符串
     * @return    转换后的日期, <b>可能返回<code>null</code></b>
     * @throws IllegalArgumentException 如果不能把参数<code>str</code>解析为日期
     */
    public static Date parse(String str) {
        return utils.parsei(str);
    }
    
    private Date parsei(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        
        StringBuffer canonicalFormat = new StringBuffer(25);
        SimpleDateFormat sdf = null;
        Date ret = null;
        try {
            if (matcher.matches(str, dateFormatPattern)) {
                MatchResult result = matcher.getMatch();
                if(result.group(1) == null)
                {
                	canonicalFormat.append("1900-01-01 ");
                }else
                {
                canonicalFormat.append(result.group(1));
                }
                if (result.group(2) == null) {
                    canonicalFormat.append(" 00:00:00");
                } else {
                    canonicalFormat.append(result.group(2));
                }
                
                if (result.group(3) == null) {
                    canonicalFormat.append(":00");
                } else {
                    canonicalFormat.append(result.group(3));
                }
                
                sdf = (SimpleDateFormat) DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, chineseLocale);
                try {
                    ret = sdf.parse(canonicalFormat.toString());
                } catch (ParseException e) {
                    sdf = (SimpleDateFormat) DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.MEDIUM, chineseLocale);
                    ret = sdf.parse(canonicalFormat.toString());
                }
            } else {
                sdf = new SimpleDateFormat(dateString, Locale.US);
                ret = sdf.parse(str);
            }
        } catch (ParseException e) {
            throw new UnexpectedException("不能把[" + str + "]解析为日期", e);
        }

        return ret;
    }
}
