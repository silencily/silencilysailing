package net.silencily.sailing.basic.wf.dto;

public class WfEntry {
	private String wfName;
	private String wfState;
	private String url;
	private String commitTime;
	private String commiter;
	private String oid;	
	private String currentStepName;
	private String title;
    private String taskId;
    private String titleCut;
    private String titleCutForMainPage;
    private String stepId;
    //工作流查询wfname特殊，temp用于封装wfname原始数据
    private String temp;
    
    
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	/**
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
    public String getCurrentStepName() {
		return currentStepName;
	}
	public void setCurrentStepName(String currentStepName) {
		this.currentStepName = currentStepName;
	}
	public String getCommiter() {
		return commiter;
	}
	public void setCommiter(String commiter) {
		this.commiter = commiter;
	}
	public String getCommitTime() {
		return commitTime;
	}
	public void setCommitTime(String commitTime) {
		this.commitTime = commitTime;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getWfName() {
		return wfName;
	}
	public void setWfName(String wfName) {
		this.wfName = wfName;
	}
	public String getWfState() {
		return wfState;
	}
	public void setWfState(String wfState) {
		this.wfState = wfState;
	}
	public String getTitleCut() {
		if(null==title || "".equals(title)){
			return title;
		}else{
			if(this.title.length()> 80 ){
				this.titleCut = this.title.substring(0, 79);
				this.titleCut += "...";
				return titleCut;
			}else{
				return this.title;
			}
		}
	}
	public void setTitleCut(String titleCut) {
		this.titleCut = titleCut;
	}
	public String getTitleCutForMainPage() {
		if(null==title || "".equals(title)){
			return title;
		}else{
			if(this.title.length()> 40 ){
				this.titleCutForMainPage = this.title.substring(0, 39);
				this.titleCutForMainPage += "...";
				return titleCutForMainPage;
			}else{
				return this.title;
			}
		}
	}
	public void setTitleCutForMainPage(String titleCutForMainPage) {
		this.titleCutForMainPage = titleCutForMainPage;
	}
	public String getStepId() {
		return stepId;
	}
	public void setStepId(String stepId) {
		this.stepId = stepId;
	}
	public String getTemp() {
		return temp;
	}
	public void setTemp(String temp) {
		this.temp = temp;
	}
	
}
