package net.silencily.sailing.common.ui.beans;

import java.text.ChoiceFormat;
import java.text.MessageFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;


/**
 * ���ڱ���ʱ��������, ��ʱ������ʾΪ���ܼ���Сʱ����
 * @since 2006-10-13
 * @author scott
 * @version $Id: Time.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 */
public class Time {
    
    /** ����ʱ��<b>���</b>�ĺ�����, ��, ��, ʱ, ��, ���Ҫ����������ݼ������ */
    private long millisecond;
    
    private int flag = -1; 
    
    private String ahead = "���� ";
    
    private String overtime = "���� ";

    private MessageFormat messageFormat = new MessageFormat("{0}{1}{2}{3}{4}");
    
    /**
     * ������������ʱ�����Ķ���, ���ֲ���<code>now</code>����<code>beofre</code>����
     * �೤ʱ������ǳ�������ʱ��
     * @param before    ����ʱ�����Ļ�׼ʱ��
     * @param now       ����ʱ��������ֹʱ��
     * @throws NullPointerException ����κ�һ��������<code>null</code>
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
            new ChoiceFormat(new double[] {0,ChoiceFormat.nextDouble(0)}, new String[] {"","{1}��"}),
            new ChoiceFormat(new double[] {0,ChoiceFormat.nextDouble(0)}, new String[] {"","{2}��"}),
            new ChoiceFormat(new double[] {0,ChoiceFormat.nextDouble(0)}, new String[] {"","{3}Сʱ"}),
            new ChoiceFormat(new double[] {0,ChoiceFormat.nextDouble(0)}, new String[] {"","{4}��"})
        };
    }

    public Time(Date before, Date now, String ahead, String overtime) {
        if (before == null || now == null) {
            throw new NullPointerException("��������ʱ������" + getClass().getName() + "ʱ������null");
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
     * ���ʱ��α��ֵ���"����"? �����"����"��ʾ���캯���Ĳ���<code>before</code>�ڲ���
     * <code>now</code>֮ǰ, ������"����"
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
