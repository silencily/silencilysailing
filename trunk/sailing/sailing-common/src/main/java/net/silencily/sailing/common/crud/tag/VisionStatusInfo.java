package net.silencily.sailing.common.crud.tag;

/**
 * @author wuym
 *
 */
public class VisionStatusInfo {
	
	private int pageType = IVisionInvorkee.EDITPAGE;
	private int visiableStatus = IVisionInvorkee.VISIBLE;
	private int editableStatus = IVisionInvorkee.EDITABLE;
	
	protected int getPageType() {
		return pageType;
	}
	protected void setPageType(int pageType) {
		this.pageType = pageType;
	}
	protected int getVisiableStatus() {
		return visiableStatus;
	}
	protected void setVisiableStatus(int visiableStatus) {
		this.visiableStatus = visiableStatus;
	}
	protected int getEditableStatus() {
		return editableStatus;
	}
	protected void setEditableStatus(int editableStatus) {
		this.editableStatus = editableStatus;
	}
	
	
	
}
