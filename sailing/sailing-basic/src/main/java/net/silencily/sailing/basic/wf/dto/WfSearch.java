
package net.silencily.sailing.basic.wf.dto;

/**
 * 功能描述：封装查询条件，个人工作流和系统管理工作流管理共用
 * 
 * @author wenjb
 * @version 1.0
 */
public class WfSearch {

	// 说明：业务名称
	private String title;

	// 说明：任务状态,存储的值为WorkflowStatus中对应的KEY值
	private String state;

	// 说明：提交用户
	private String committer;

	// 说明：开始时间
	private String startTime;

	// 说明：结束时间
	private String endTime;

	public String getCommitter() {
		return committer;
	}

	public void setCommitter(String committer) {
		this.committer = committer;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTitle() {
		return title == null ? null : title.trim();
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
