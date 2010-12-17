package net.silencily.sailing.basic.wf.condition;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.spi.Step;

public class AvailableStepsCondition implements Condition {

	public boolean passesCondition(Map tvar, Map args, PropertySet ps) throws WorkflowException {
		 String availableSteps = (String)args.get("availableSteps");
	        if(availableSteps == null)
	            return false;	        
	        else if("all".equals(availableSteps))
	        	return true;
	        else if("nothing".equals(availableSteps))
	        	return false;
	        String avaliableStepArray[] = availableSteps.split(",");
	        for(Iterator currentSteps = ((Collection)tvar.get("currentSteps")).iterator(); currentSteps.hasNext();)
	        {
	            Step step = (Step)currentSteps.next();
	            if(ArrayUtils.indexOf(avaliableStepArray, String.valueOf(step.getStepId())) > -1)
	                return true;
	        }
	        return false;		
	}
}