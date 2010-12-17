package net.silencily.sailing.common.crud.tag;

/**
 * @author wuym
 *
 */
public class VisionDefaultInvorkee implements IVisionInvorkee{
	public VisionStatusInfo getVisionStatusInfo(String rwCtrlType,String permissionCode)
	{
		VisionStatusInfo visionStatusInfo = new VisionStatusInfo();

		visionStatusInfo.setPageType(IVisionInvorkee.EDITPAGE);//²é¿´Ò³»ò±à¼­Ò³
		visionStatusInfo.setVisiableStatus(IVisionInvorkee.VISIBLE);//ÊÇ·ñ¿É¼û×´Ì¬
		visionStatusInfo.setEditableStatus(IVisionInvorkee.EDITABLE);//ÊÇ·ñ±à¼­×´Ì¬
		return visionStatusInfo;
	}
}
