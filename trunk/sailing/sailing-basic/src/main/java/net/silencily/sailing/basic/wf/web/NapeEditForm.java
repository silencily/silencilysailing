package net.silencily.sailing.basic.wf.web;

import java.util.ArrayList;
import java.util.List;

import net.silencily.sailing.basic.wf.domain.TblWfEditInfo;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.struts.BaseFormPlus;

import org.apache.commons.lang.StringUtils;
/**
 * �ɱ༭���Form
 * @author yangxl
 * @version 1.0
 */
public class NapeEditForm extends BaseFormPlus{
	/**
	 * �ɱ༭��ʵ��Bean
	 */
	private TblWfEditInfo tblWfEditInfoBean;

	/**
	 * �ɱ༭���б�
	 */
	private List list = new ArrayList();
	/**
	 * 
	 * ��ȡ��ͨ����
	 * @return  ��ͨ��service
	 * 2008-5-7����10:05:51
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
