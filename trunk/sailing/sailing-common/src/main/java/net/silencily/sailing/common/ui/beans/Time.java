package net.silencily.sailing.common.ui.beans;

import java.text.ChoiceFormat;
import java.text.MessageFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;


/**
 * 用于表现时间间隔的类, 把时间间隔显示为几周几个小时几分
 * @since 2006-10-13
 * @author scott
 * @version $Id: Time.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 */
public class Time {
    
    /** 表现时间<b>间隔</b>的毫秒数, 月, 天, 时, 分, 秒等要基于这个数据计算出来 */
    private long millisecond;
    
    private int flag = -1; 
    
    private String ahead = "还有 ";
    
    private String overtime = "超过 ";

    private MessageFormat messageFormat = new MessageFormat("{0}{1}{2}{3}{4}");
    
    /**
     * 创建表现两个时间间隔的对象, 表现参数<code>now</code>距离<code>beofre</code>还有
     * 多长时间或者是超过多少时间
     * @param before    表现时间间隔的基准时间
     * @param now       表现时间间隔的终止时间
     * @throws NullPointerException 如果任何一个参数是<code>null</code>
     */
    public Time(Date before, Date now) {
        this(before, now, null, null);
    }
    
    public String getAhead() {
        return this.ahead;
    }
    
    public String getOvertime() {
        return this.overtime;
    }
    
    private ChoiceFormat[] getInterval() {
        return new ChoiceFormat[] {
            new ChoiceFormat(new double[] {-1, 1}, new String[] {getAhead(), getOvertime()}),
            new ChoiceFormat(new double[] {0,ChoiceFormat.nextDouble(0)}, new String[] {"","{1}周"}),
            new ChoiceFormat(new double[] {0,ChoiceFormat.nextDouble(0)}, new String[] {"","{2}天"}),
            new ChoiceFormat(new double[] {0,ChoiceFormat.nextDouble(0)}, new String[] {"","{3}小时"}),
            new ChoiceFormat(new double[] {0,ChoiceFormat.nextDouble(0)}, new String[] {"","{4}分"})
        };
    }

    public Time(Date before, Date now, String ahead, String overtime) {
        if (before == null || now == null) {
            throw new NullPointerException("创建表现时间间隔的" + getClass().getName() + "时参数是null");
        }
        this.millisecond = now.getTime() - before.getTime();
        flag = this.millisecond < 0 ? -1 : 1;
        this.millisecond = Math.abs(this.millisecond);
        if (StringUtils.isNotBlank(ahead)) {
            this.ahead = ahead;
        }
        if (StringUtils.isNotBlank(overtime)) {
            this.overtime = overtime;
        }
    }
    
    public Time(long millisecond) {
        this.millisecond = millisecond;
    }

    public long getWeek() {
        return (long) getSecond() / 60 / 60 / 24 / 7;  // 7 * 24
    }
    
    public int getDay() {
        return (int) (getSecond() / 60 / 60 / 24) % 7;
    }
    
    public long getHour() {
        return (int) (getSecond() / 60 / 60) % 24;
    }
    
    public int getMinute() {
        return (int) (getSecond() / 60) % 60;
    }
    
    public long getSecond() {
        long second = 0;
        if (this.millisecond != 0) {
            second = Math.round((double) this.millisecond / (double) 1000);
        }
        return second;
    }
    
    /**
     * 这个时间段表现的是"超过"? 如果是"还有"表示构造函数的参数<code>before</code>在参数
     * <code>now</code>之前, 否则是"超过"
     * @return
     */
    public boolean isOverstep() {
        return flag == -1;
    }
    
    public long getMillisecond() {
        return this.millisecond;
    }
    
    public void setMillisecond(long millisecond) {
        this.millisecond = millisecond;
    }
    
    public String toString() {
        messageFormat.setFormats(getInterval());
        return messageFormat.format(new Long[] {new Long(flag), new Long(getWeek()), new Long(getDay()), new Long(getHour()), new Long(getMinute())});
    }
}
