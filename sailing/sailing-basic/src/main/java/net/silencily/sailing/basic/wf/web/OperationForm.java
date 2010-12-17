package net.silencily.sailing.basic.wf.web;

import java.util.ArrayList;
import java.util.List;

import net.silencily.sailing.basic.wf.domain.TblWfOperationInfo;
import net.silencily.sailing.basic.wf.domain.TblWfParticularInfo;
import net.silencily.sailing.struts.BaseFormPlus;

import org.apache.commons.lang.StringUtils;

public class OperationForm extends BaseFormPlus {
	private TblWfOperationInfo wfoperinfo;

	private TblWfParticularInfo wfpinfo;

	private List particulars = new ArrayList();

	private List gobacklist = new ArrayList();

	private List commitlist = new ArrayList();

	private List roleli = new ArrayList();

	//private List wfForminfoList = new ArrayList();

	/*
	 * WFService wFService = (WFService) ServiceProvider
	 * .getService(WFService.SERVICE_NAME);
	 */

	/**
	 * @return the wfoperinfo
	 */
	public TblWfOperationInfo getWfoperinfo() {
		if (wfoperinfo == null) {
			if (StringUtils.isBlank(getOid())) {
				wfoperinfo = new TblWfOperationInfo();
			} else {
				wfoperinfo = (TblWfOperationInfo) OperationAction.service()
						.load(TblWfOperationInfo.class, getOid());
			}
		}
		return wfoperinfo;
	}

	/**
	 * @param wfoperinfo
	 *            the wfoperinfo to set
	 */
	public void setWfoperinfo(TblWfOperationInfo wfoperinfo) {
		this.wfoperinfo = wfoperinfo;
	}

	/**
	 * @return the particulars
	 */
	public List getParticulars() {
		return particulars;
	}

	/**
	 * @param particulars
	 *            the particulars to set
	 */
	public void setParticulars(List particulars) {
		this.particulars = particulars;
	}

	/**
	 * @return the wfpinfo
	 */
	public TblWfParticularInfo getWfpinfo() {
		if (null == wfpinfo) {
			if (StringUtils.isBlank(getOid())) {
				wfpinfo = new TblWfParticularInfo();
			} else {
				wfpinfo = (TblWfParticularInfo) OperationAction.service().load(
						TblWfParticularInfo.class, getOid());
			}

		}
		return wfpinfo;
	}

	/**
	 * @param wfpinfo
	 *            the wfpinfo to set
	 */
	public void setWfpinfo(TblWfParticularInfo wfpinfo) {
		this.wfpinfo = wfpinfo;
	}

	/**
	 * @return the gobacklist
	 */
	public List getGobacklist() {
		return gobacklist;
	}

	/**
	 * @param gobacklist
	 *            the gobacklist to set
	 */
	public void setGobacklist(List gobacklist) {
		this.gobacklist = gobacklist;
	}

	/**
	 * @return the commitlist
	 */
	public List getCommitlist() {
		return commitlist;
	}

	/**
	 * @param commitlist
	 *            the commitlist to set
	 */
	public void setCommitlist(List commitlist) {
		this.commitlist = commitlist;
	}

	/**
	 * @return the roleli
	 */
	public List getRoleli() {
		return roleli;
	}

	/**
	 * @param roleli
	 *            the roleli to set
	 */
	public void setRoleli(List roleli) {
		this.roleli = roleli;
	}

	/**
	 * @return the wfForminfoList
	 *//*
	public List getWfForminfoList() {
		return wfForminfoList;
	}

	*//**
	 * @param wfForminfoList
	 *            the wfForminfoList to set
	 *//*
	public void setWfForminfoList(List wfForminfoList) {
		this.wfForminfoList = wfForminfoList;
	}

	public TblWfFormInfo getWfForminfoList(int i)
			throws IndexOutOfBoundsException {
		while (wfForminfoList.size() <= i) {
			wfForminfoList.add(null);
		}
		TblWfFormInfo detail = (TblWfFormInfo) wfForminfoList.get(i);
		String forminfoId = request
				.getParameter("wfForminfoList[" + i + "].id");
		if (StringUtils.isNotBlank(forminfoId)) {
			detail = (TblWfFormInfo) getService().load(TblWfFormInfo.class,
					forminfoId);
		}
		if (detail == null) {
			detail = new TblWfFormInfo();
		}
		wfForminfoList.set(i, detail);
		return detail;

	}*/

	/*private CommonService getService() {
		return (CommonService) ServiceProvider
				.getService(CommonService.SERVICE_NAME);
	}*/
}
