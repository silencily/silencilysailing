package net.silencily.sailing.basic.wf.web;

import net.silencily.sailing.basic.wf.domain.TblWfOperationInfo;
import net.silencily.sailing.basic.wf.domain.WorkFlowMetaOperator;
import net.silencily.sailing.basic.wf.domain.mate.WorkFlowMeta;

public class CreateXml {

	private static WorkFlowMetaOperator workFlowMetaOperator = WorkFlowMetaOperator
	.getInstance();
	
	public void saveWfInfo(TblWfOperationInfo wfinfo,String dir) {
		try{
			WorkFlowMeta wfm = new WorkFlowMeta();
			wfm = workFlowMetaOperator.getWorkFlowMeta(wfinfo);
			String dirs = dir;
			workFlowMetaOperator.save(wfm,dirs);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
