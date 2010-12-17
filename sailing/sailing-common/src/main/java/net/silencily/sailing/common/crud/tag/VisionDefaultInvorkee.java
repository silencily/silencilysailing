package net.silencily.sailing.common.crud.tag;

/**
 * @author wuym
 *
 */
public class VisionDefaultInvorkee implements IVisionInvorkee{
	public VisionStatusInfo getVisionStatusInfo(String rwCtrlType,String permissionCode)
	{
		VisionStatusInfo visionStatusInfo = new VisionStatusInfo();

		visionStatusInfo.setPageType(IVisionInvorkee.EDITPAGE);//�鿴ҳ��༭ҳ
		visionStatusInfo.setVisiableStatus(IVisionInvorkee.VISIBLE);//�Ƿ�ɼ�״̬
		visionStatusInfo.setEditableStatus(IVisionInvorkee.EDITABLE);//�Ƿ�༭״̬
		return visionStatusInfo;
	}
}
