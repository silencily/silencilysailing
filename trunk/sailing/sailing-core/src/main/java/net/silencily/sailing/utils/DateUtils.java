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
 * �������Ͳ�����<code>utility</code>��
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
            throw new UnexpectedException("���ܱ���������ڸ�ʽ��������ʽ", e);
        }
    }
    
    /**
     * ��ȡϵͳ��ǰʱ�䣬����ʹ�÷�����ʱ�䣬���ʹ�����ݿ�ʱ�䣬����Ҫ�ع�
     * @return 
     */
    public static Date getCurrentDate(){
    	return net.silencily.sailing.utils.DBTimeUtil.getDBTime();
    }
    
    /**
	 * ��ָ��������ת��Ϊ yyyy-MM-dd ���ַ�
	 * @param date
	 * @return
	 */
	public static String getSimplateFormat(Date date){
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}
    
    /**
     * ����ָ���������Ǹ��µĵ�һ��
     * @param date �����������ת��
     * @return ����ָ�����������·ݵĵ�һ��
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
     * ����ָ�����ڵ��·��е����һ��
     * @param date �����������ת��
     * @return ����ָ�����������·ݵ����һ��
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
     * �����ڵ��ַ�����ʽת����<code>Date</code>, ����<code>Timestamp</code>��ʽ��������ʧ
     * ��<code>nano-second</code>����, ���ڵ�ǰ��Ӧ��Ӧ�ò�������. ֧��ʹ�õ����ַ�(unicode)
     * ��ʹ�����ַ��ָ��������ո�ʽ�Լ�Ӣ�����ڸ�ʽ(����:<code>Sun Apr 16 22:17:51 CST 2006</code>,
     * �����ʽ��<code>Date.toString()</code>�����,Ϊʲô�����������ĸ�ʽ?), <b>�ر��</b>
     * ��������<code>null</code>��<code>empty</code>����<code>null</code></b>, �����
     * <code>web</code>Ӧ����˵�Ǻ����. ����ʶ��ĸ�ʽ����<ul>
     * <li>yyyy-MM-dd</li>
     * <li>yyyy-MM-dd HH:mm</li>
     * <li>yyyy��MM��dd��</li>
     * <li>yyyy��MM��dd�� HH:mm</li>
     * <li>EEE MMM HH:mm:ss zzz yyyy(<code>Date.toString()</code>�������ʽ)</li>
     * </ul>
     * @param str Ҫת�����ڵ��ַ���
     * @return    ת���������, <b>���ܷ���<code>null</code></b>
     * @throws IllegalArgumentException ������ܰѲ���<code>str</code>����Ϊ����
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
            throw new UnexpectedException("���ܰ�[" + str + "]����Ϊ����", e);
        }

        return ret;
    }
}
