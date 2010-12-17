package net.silencily.sailing.basic.wf.entry;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.basic.sm.user.service.UserManageService;
import net.silencily.sailing.basic.wf.WorkflowConstants;
import net.silencily.sailing.basic.wf.WorkflowDataCallback;
import net.silencily.sailing.basic.wf.service.WFService;
import net.silencily.sailing.container.ServiceProvider;

import org.apache.commons.lang.StringUtils;

public class WorkflowStep implements WorkflowDataCallback {

	private String id;
	private String name;
	private String actionId;
	private WorkflowAction action;
	private List principal;
	private Boolean hasPermission;
	private Map metadata;
	private Date startTime;
	private Date finishTime;
	private String username;
	private String opinion;
	private List availableActions;
	private Map historyData;
	private String description;
	private String pointType;
	private String specialObject;
	private String roles;

	// 历史步骤信息，工作流实例步骤主键
	private long entryStepId;

	public long getEntryStepId() {
		return entryStepId;
	}

	public void setEntryStepId(long entryStepId) {
		this.entryStepId = entryStepId;
	}

	public Map getHistoryData() {
		return historyData;
	}

	public void setHistoryData(Map historyData) {
		this.historyData = historyData;
	}

	public String getSpecialObject() {
		return specialObject;
	}

	public int getIntId() {
		return Integer.parseInt(id);
	}

	public void setSpecialObject(String specialObject) {
		this.specialObject = specialObject;
	}

	public String getPointType() {
		return pointType;
	}

	public void setPointType(String pointType) {
		this.pointType = pointType;
	}

	public WorkflowStep() {
		principal = Collections.EMPTY_LIST;
		historyData = new HashMap(5);
	}

	public List getAvailableActions() {
		if (availableActions == null)
			return Collections.EMPTY_LIST;
		else
			return availableActions;
	}

	public String getUserChineseName() {
		if (username == null)
			return null;
		else {
			TblCmnUser user = ((UserManageService) ServiceProvider
					.getService(UserManageService.SERVICE_NAME)).getUser(this
					.getUsername());
			if (user != null) {
				return user.getEmpName();
			}
			return null;
		}

	}

	public void setAvailableActions(List availableActions) {
		this.availableActions = availableActions;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map getMetadata() {
		if (metadata == null)
			metadata = new HashMap(10);
		return metadata;
	}

	public void setMetadata(Map metadata) {
		this.metadata = metadata;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setData(Map map) {
		historyData = map;
	}

	public Map getData() throws Exception {
		// 取工作流的步骤信息
		String stepChangeContent = "";
		WFService service = (WFService) ServiceProvider
				.getService(WFService.SERVICE_NAME);
		stepChangeContent = service.searchHistoryEntryData(String.valueOf(this
				.getEntryStepId()));
		if (StringUtils.isNotBlank(stepChangeContent) && null != historyData) {
			historyData.put(WorkflowConstants.KEY_STEP_CHANGE_CONTENT,
					stepChangeContent);
		}
		return historyData;
	}

	public List getPrincipal() {
		return principal;
	}

	public void setPrincipal(List principal) {
		this.principal = principal;
	}

	public Boolean isHasPermission() {
		return hasPermission;
	}

	public boolean isAllowed() {
		return hasPermission != null && hasPermission.booleanValue();
	}

	public Boolean getHasPermission() {
		return isHasPermission();
	}

	public void setHasPermission(Boolean hasPermission) {
		this.hasPermission = hasPermission;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public WorkflowAction getAction() {
		return action;
	}

	public void setAction(WorkflowAction action) {
		this.action = action;
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

}
