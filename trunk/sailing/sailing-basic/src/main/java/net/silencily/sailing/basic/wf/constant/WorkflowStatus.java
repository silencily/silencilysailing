package net.silencily.sailing.basic.wf.constant;

import java.util.HashMap;
import java.util.Map;

public interface WorkflowStatus {
	
	//定义工作流的流程状态
	public static final Map WF_STATUS = new HashMap() {
		
		private static final long serialVersionUID = -1841814371685403017L;
		{
			 put("scratch", "\u8349\u7A3F");
             put("processing", "\u5904\u7406\u4E2D");
             put("finish", "\u5DF2\u5B8C\u6210");
             put("suspend", "\u6302\u8D77");             
             put("killed", "\u88AB\u53D6\u6D88");
             put("untread", "\u88AB\u9000\u56DE");
             put("retake", "\u91CD\u65B0\u7F16\u5236");
             put("lockup", "\u8F6C\u5165\u5B50\u6D41\u7A0B");
		}
	};	
	
	//定义查询的任务类型
	public static final Map WF_SERARCH_STATUS = new HashMap() {
		
		private static final long serialVersionUID = -1841814371685403027L;
		{
			 put("waitWF", "\u5f85\u529e\u4efb\u52a1");        //待办任务
             put("alreadyWF", "\u5df2\u529e\u4efb\u52a1");     //已办任务
             put("recieveWF", "\u53d7\u6258\u4efb\u52a1");     //受托任务
             put("entrustWF", "\u59d4\u6258\u4efb\u52a1");     //委托任务
		}
	};	
	
	//工作流管理使用的工作流状态
	public static final Map WF_SM_STATUS = new HashMap() {
		
		private static final long serialVersionUID = -4381106769351256132L;
		{
             put("processing", "\u5904\u7406\u4E2D");
             put("untread", "\u88AB\u9000\u56DE");
             put("retake", "\u91CD\u65B0\u7F16\u5236");
		}
	};	
}
