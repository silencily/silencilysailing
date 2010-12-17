package net.silencily.sailing.basic.wf.impl;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StopWatch;

public class OsWorkflowTraceInterceptor
    implements MethodInterceptor
{

    public OsWorkflowTraceInterceptor()
    {
    }

    public Object invoke(MethodInvocation methodInvocation)
        throws Throwable
    {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("workflow");
        String className = methodInvocation.getMethod().getDeclaringClass().getName();
        String methodName = methodInvocation.getMethod().getName();
        if(logger.isDebugEnabled())
            logger.debug("enter '" + methodName + "' of class " + className);
        Object ret = methodInvocation.proceed();
        stopWatch.stop();
        if(logger.isDebugEnabled())
            logger.debug(stopWatch.prettyPrint());
        return ret;
    }

    private static Log logger;

    static 
    {
        logger = LogFactory.getLog(OsWorkflowTraceInterceptor.class);
    }
}

