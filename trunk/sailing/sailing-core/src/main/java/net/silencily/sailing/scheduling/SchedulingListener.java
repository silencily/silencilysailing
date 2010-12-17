package net.silencily.sailing.scheduling;

import java.util.Iterator;
import java.util.List;
import java.util.Timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import net.silencily.sailing.container.ServiceProviderUtils;
import net.silencily.sailing.container.support.ConfigFinishedEvent;

/**
 * <p>用于启动定时执行的任务的监听器, 当接收到配置加载完成的通知后, 启动实现了接口
 * {@link com.coheg.scheduling.Schedulable Schedulable}的任务, 在应用关闭时
 * 要在这里清除掉所有的调度任务</p>
 * 
 * @author scott
 * @since 2006-2-28
 * @version $Id: SchedulingListener.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 *
 */
public class SchedulingListener implements ApplicationListener, DisposableBean {
    private Log logger = LogFactory.getLog(SchedulingListener.class);
    
    /**
     * 用于执行调度任务的定时器
     */
    private Timer timer = new Timer();
    
    /**
     * 被调度的任务
     */
    private List scheduledList;

    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ConfigFinishedEvent) {
            this.scheduledList = ServiceProviderUtils.getServicesOfType(Schedulable.class);
            SchedulingIterator iterator = new SchedulingIterator() {
                public void callback(Schedulable schedulable) {
                    schedulable.schedules(timer);
                }
            };
            
            iterator.iterator(scheduledList);
        }
    }

    public void destroy() throws Exception {
        timer.cancel();
        timer = null;
        
        if (this.scheduledList != null) {
            SchedulingIterator iterator = new SchedulingIterator() {
                public void callback(Schedulable schedulable) {
                    schedulable.cancel();
                }
            };
            
            iterator.iterator(scheduledList);
            scheduledList = null;
        }
    }

    private abstract class SchedulingIterator {
        public void iterator(List scheduledList) {
            for (Iterator it = scheduledList.iterator(); it.hasNext(); ) {
                Schedulable schedulable = (Schedulable) it.next();
                try {
                    callback(schedulable);
                } catch (Exception e) {
                    if (logger.isInfoEnabled()) {
                        logger.info("执行调度任务时发生错误", e);
                    }
                }
            }
        }
        
        public abstract void callback(Schedulable schedulable);
    }
}
