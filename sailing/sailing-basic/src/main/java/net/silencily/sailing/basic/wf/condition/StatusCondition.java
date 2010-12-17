package net.silencily.sailing.basic.wf.condition;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.StoreException;
import com.opensymphony.workflow.spi.*;
import java.util.*;

import org.apache.commons.lang.StringUtils;

public class StatusCondition implements Condition {

	public StatusCondition() {
	}

	public boolean passesCondition(Map transientVars, Map args, PropertySet ps) throws StoreException {
	        String status = (String)args.get("status");
	    	String[] statusArray = null; 
	        if(StringUtils.isBlank(status)){
	    		return false;
	    	}else{
	    		statusArray = status.split(",");
	    	}	       
	        if(statusArray == null){
	        	return false;
	        }	        
	        WorkflowEntry entry = (WorkflowEntry)transientVars.get("entry");
	        WorkflowStore store = (WorkflowStore)transientVars.get("store");
	        List currentSteps = store.findCurrentSteps(entry.getId());
	        for(int i=0; i<currentSteps.size(); i++){
	        	Step step = (Step)currentSteps.get(i);
	        	for(int j=0; j<statusArray.length; j++){
	        		if(step != null && step.getStatus().equals(statusArray[j])){
	        			return true;
	        		}
	        	}
	        }	        
	        return false;
	    }
}
