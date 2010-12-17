package net.silencily.sailing.basic.wf.web;

import java.util.ArrayList;
import java.util.List;

import net.silencily.sailing.basic.wf.domain.TblWfEditInfo;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.struts.BaseFormPlus;

import org.apache.commons.lang.StringUtils;
/**
 * 可编辑项表Form
 * @author yangxl
 * @version 1.0
 */
public class NapeEditForm extends BaseFormPlus{
	/**
	 * 可编辑项实体Bean
	 */
	private TblWfEditInfo tblWfEditInfoBean;

	/**
	 * 可编辑项列表
	 */
	private List list = new ArrayList();
	/**
	 * 
	 * 获取共通服务
	 * @return  共通的service
	 * 2008-5-7上午10:05:51
	 * @version 1.0
	 * @author yangxl
	 */
	private CommonService getService(){
		
		return (CommonService) ServiceProvider.getService(CommonService.SERVICE_NAME);
	}
	
	public List getList() {
		return list;
	}
	
	public void setList(List list) {
		this.list = list;
	}
	
	public TblWfEditInfo getTblWfEditInfo() {
		if (tblWfEditInfoBean == null) {
				
				if (StringUtils.isBlank(getOid())) {
					
					tblWfEditInfoBean = new TblWfEditInfo();
				
				} else {
					
					tblWfEditInfoBean = (TblWfEditInfo)getService().load(TblWfEditInfo.class,getOid());
				}
		}
		return tblWfEditInfoBean;
	}
	
	public void setTblWfEditInfo(TblWfEditInfo tblWfEditInfoBean) {
		this.tblWfEditInfoBean = tblWfEditInfoBean;
	}
	
	public TblWfEditInfo getList(int i)
	{
		TblWfEditInfo tsk = (TblWfEditInfo)list.get(i);
		if(tsk == null)
		{
			tsk = new TblWfEditInfo();
			list.set(i, tsk);
		}
		return tsk;
	}
}
