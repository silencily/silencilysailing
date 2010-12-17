package net.silencily.sailing.basic.sm.search.web;

import java.util.List;
import java.util.Set;

import net.silencily.sailing.basic.sm.domain.TblCmnDept;
import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.struts.BaseFormPlus;

import org.apache.commons.lang.StringUtils;



/**
 * @author zhaoyifei
 *
 */
public class SmSearchForm extends BaseFormPlus {
	
	private TblCmnDept deptRoot;

	private TblCmnUser result;
	
	private String parentCode;
	
	private List infoList;
	
	/* checkbox or radio type */
	private String checkType;

	public List getInfoList() {
		return infoList;
	}
	public void setInfoList(List infoList) {
		this.infoList = infoList;
	}
	/**
	 * @return the result
	 */
	public TblCmnUser getResult() {
		if(result==null)
		{
			if(!StringUtils.isBlank(getOid()))
			{
				result=result=(TblCmnUser) getService().load(TblCmnUser.class,oid);
			}
			else
			{
				result=new TblCmnUser();
			}
		}
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(TblCmnUser result) {
		this.result = result;
	}

	

	/**
	 * @return the parentCode
	 */
	public String getParentCode() {
		return parentCode;
	}

	/**
	 * @param parentCode the parentCode to set
	 */
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public TblCmnDept getDeptRoot() {
		return deptRoot;
	}

	public void setDeptRoot(TblCmnDept deptRoot) {
		this.deptRoot = deptRoot;
	}
	private CommonService getService() {
		return (CommonService)ServiceProvider.getService(CommonService.SERVICE_NAME);
	}
	public String getCheckType() {
		return checkType;
	}
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

}
