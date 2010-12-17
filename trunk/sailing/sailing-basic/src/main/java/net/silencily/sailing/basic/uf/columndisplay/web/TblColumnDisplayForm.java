package net.silencily.sailing.basic.uf.columndisplay.web;

import java.util.ArrayList;
import java.util.List;

import net.silencily.sailing.basic.uf.domain.TblUfColumn;
import net.silencily.sailing.basic.uf.domain.TblUfColumnOrder;
import net.silencily.sailing.basic.uf.domain.TblUfNews;
import net.silencily.sailing.basic.uf.domain.TblUfNewsFdbk;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.struts.BaseFormPlus;

import org.apache.commons.lang.StringUtils;


public class TblColumnDisplayForm extends BaseFormPlus {
     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List listDesktop;
	private String displayNo;
	private List desk = new ArrayList();
	private List parentid = new ArrayList();
	private TblUfNews tblufNews;
	private String sortno;
	private String displayTitle;
	private String today;
	private TblUfNewsFdbk tblufNewsFdbk;
    private List wfList;
    private List scheduleList;
    private List missionList;
    private List mailList;
    private int wfListSize;
	/**
	 * @return displayTitle
	 */
	public String getDisplayTitle() {
		return displayTitle;
	}

	/**
	 * @param displayTitle 要设置的 displayTitle
	 */
	public void setDisplayTitle(String displayTitle) {
		this.displayTitle = displayTitle;
	}

	/**
	 * @return sortno
	 */
	public String getSortno() {
		return sortno;
	}

	/**
	 * @param sortno 要设置的 sortno
	 */
	public void setSortno(String sortno) {
		this.sortno = sortno;
	}

	/**
	 * @return tblufNews
	 */
	public TblUfNews getTblufNews() {
		return tblufNews;
	}

	/**
	 * @param tblufNews 要设置的 tblufNews
	 */
	public void setTblufNews(TblUfNews tblufNews) {
		this.tblufNews = tblufNews;
	}

	/**
	 * @return parentid
	 */
	public List getParentid() {
		return parentid;
	}

	/**
	 * @param parentid 要设置的 parentid
	 */
	public void setParentid(List parentid) {
		this.parentid = parentid;
	}

	public TblUfColumn getParentid(int i) {
		TblUfColumn pid = (TblUfColumn)parentid.get(i);
		if (pid == null) {
			pid = new TblUfColumn();
			desk.add(i, pid);

		}
		return pid;
	}
	/**
	 * @return desk
	 */
	public List getDesk() {
		return desk;
	}

	/**
	 * @param desk 要设置的 desk
	 */
	public void setDesk(List desk) {
		this.desk = desk;
	}

	/**
	 * @return listDesktop
	 */
	public List getListDesktop() {
		return listDesktop;
	}

	public TblUfColumnOrder getDesk(int i) {
		TblUfColumnOrder tcdt = (TblUfColumnOrder) desk.get(i);
		if (tcdt == null) {
			tcdt = new TblUfColumnOrder();
			desk.add(i, tcdt);

		}
		return tcdt;
	}
	
	/**
	 * @param listDesktop 要设置的 listDesktop
	 */
	public void setListDesktop(List listDesktop) {
		this.listDesktop = listDesktop;
	}

	/**
	 * @return displayNo
	 */
	public String getDisplayNo() {
		return displayNo;
	}

	/**
	 * @param displayNo 要设置的 displayNo
	 */
	public void setDisplayNo(String sortNo) {
		this.displayNo = sortNo;
	}

	public String getToday() {
		return today;
	}

	public void setToday(String today) {
		this.today = today;
	}	
	
	public TblUfNewsFdbk getTblufNewsFdbk() {
		if(tblufNewsFdbk==null){
			if(StringUtils.isBlank(getOid())){
				tblufNewsFdbk=new TblUfNewsFdbk();
			}
			else{
				tblufNewsFdbk=(TblUfNewsFdbk)getService().load(TblUfNewsFdbk.class, getOid());
			}
		}
		return tblufNewsFdbk;
	}

	public void setTblufNewsFdbk(TblUfNewsFdbk tblufNewsFdbk) {
		this.tblufNewsFdbk = tblufNewsFdbk;
	}

	private CommonService getService() {
		return (CommonService) ServiceProvider
				.getService(CommonService.SERVICE_NAME);
	}

    /**
     * @return the mailList
     */
    public List getMailList() {
        return mailList;
    }

    /**
     * @param mailList the mailList to set
     */
    public void setMailList(List mailList) {
        this.mailList = mailList;
    }

    /**
     * @return the missionList
     */
    public List getMissionList() {
        return missionList;
    }

    /**
     * @param missionList the missionList to set
     */
    public void setMissionList(List missionList) {
        this.missionList = missionList;
    }

    /**
     * @return the scheduleList
     */
    public List getScheduleList() {
        return scheduleList;
    }

    /**
     * @param scheduleList the scheduleList to set
     */
    public void setScheduleList(List scheduleList) {
        this.scheduleList = scheduleList;
    }

    /**
     * @return the wfList
     */
    public List getWfList() {
        return wfList;
    }

    /**
     * @param wfList the wfList to set
     */
    public void setWfList(List wfList) {
        this.wfList = wfList;
    }

    /**
     * @return the wfListSize
     */
    public int getWfListSize() {
        return wfListSize;
    }

    /**
     * @param wfListSize the wfListSize to set
     */
    public void setWfListSize(int wfListSize) {
        this.wfListSize = wfListSize;
    }
}
