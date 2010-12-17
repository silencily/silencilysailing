package net.silencily.sailing.scheduling;

import java.util.Timer;

/**
 * <p>һ������ִ��ʱ���������Ľӿ�, ʵ������ӿڵ��������ָ����ʱ��ִ��ĳ������. �����Ϊ��ʱ
 * ִ�л���ִ����������<ul>
 * <li>ָ����<code>period</code>����:ÿ��<code>period</code><b>��</b>��ִ��һ��<code>
 * execute</code>����, �������Ҫ�������, ����{@link #getPeriod() <code>getPeriod</code>}
 * Ӧ�÷���{@link #NON_PERIOD NON_PERIOD}. ����Ҫÿ��10��ִ��һ��<code>execute</code>��
 * ��������<code>getPeriod</code>��������<code>10</code></li>
 * <li>ָ����<code>delay</code>����:����<code>delay</code><b>��</b>��ִ��<code>execute</code>
 * ����, �������Ҫ��ʱִ�з���{@link #getDelay() <code>getDelay</code>}Ӧ�÷���{@link #NON_DELAY
 * <code>NON_DELAY</code></li>
 * <li>ָ����<code>specified time</code>, ÿ��ָ����ʱ��͵���<code>execute</code>����,
 * ��������ĸ�ʽ��<code>HH:mm:ss</code>��<code>HH:mm</code>��ʽ, ����:<code>HH</code>
 * ��<code>24</code>Сʱ���ӵ�, <code>mm</code>�Ƿ���, <code>ss</code>������, ���������
 * �������������<code>period</code>������ֵ, ����Ҫ��ÿ���<code>21:30</code>����<code>
 * execute</code>����������<code>getSpecifiedTime</code>����<code>21:30</code></li></ul>
 * 
 * @author scott
 * @since 2006-2-26
 * @version $Id: Schedulable.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 *
 */
public interface Schedulable {
    /**
     * ��ϵͳ����ʱ, �ܹ������������, �Ա�ִ��ĳ�������Ե�����. �ܹ���������׳����κ��쳣����
     * ���������׳�, ����<code>log</code>������Ϣ. Ӧ�ô���Ӧ�������ﴦ����ֿ��ܵĴ������.
     * <b>ע��</b>: ��Ҫ�Բ���<code>timer</code>�����ص�����, Ҳ��Ҫֱ�ӽ���<code>cancel</code>
     * ���ͷ���Դ�Ĳ���
     * 
     * @param timer ִ�������������<code>Timer</code>
     */
    void schedules(Timer timer);
    
    /**
     * ��ϵͳ�ر�ʱ�ɼܹ������������, Ӧ�ÿ��������ﴦ���ռ�õ���Դ, �־û�����������ĵ�����Ϣ
     * ��, �ܹ���������׳����κ��쳣���ǲ��������׳�, ����<code>log</code>������Ϣ. Ӧ�ô���
     * Ӧ�������ﴦ����ֿ��ܵĴ������. ͬʱע������������ʱϵͳ�Ѿ�׼���رն����ǲ������, ��
     * �������׳��쳣û�кܴ��������
     */
    void cancel();
}
