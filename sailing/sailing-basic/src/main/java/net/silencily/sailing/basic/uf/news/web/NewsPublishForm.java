package net.silencily.sailing.basic.uf.news.web;

import java.util.ArrayList;
import java.util.List;

import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.basic.uf.domain.TblUfNews;
import net.silencily.sailing.basic.uf.domain.TblUfNewsAttach;
import net.silencily.sailing.basic.uf.domain.TblUfNewsFdbk;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.common.fileload.FileLoadBean;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.framework.web.view.ComboSupportList;
import net.silencily.sailing.struts.BaseFormPlus;

import org.apache.commons.lang.StringUtils;

/**
 * @author wangchc
 *
 */
public class NewsPublishForm extends BaseFormPlus {
    private TblUfNews bean;
    private String tblUfColumnid;
//    private List listNewsFdbk = new ArrayList(); 
    private List newsFdbk = new ArrayList();
//    private List experEmpInfo = new ArrayList();
    private ComboSupportList columnList;
    private FileLoadBean flb = new FileLoadBean();
    private List attachement = new ArrayList(); //项目验收报告
    private String resourceUrl;

    /**
     * @return the flb
     */
    public FileLoadBean getFlb() {
        return flb;
    }

    /**
     * @param flb the flb to set
     */
    public void setFlb(FileLoadBean flb) {
        this.flb = flb;
    }

//    /**
//	 * @return experEmpInfo
//	 */
//	public List getExperEmpInfo() {
//		return experEmpInfo;
//	}
//
//	/**
//	 * @param experEmpInfo 要设置的 experEmpInfo
//	 */
//	public void setExperEmpInfo(List experEmpInfo) {
//		this.experEmpInfo = experEmpInfo;
//	}
//
//	public TblHrEmpInfo getExperEmpInfo(int i){
//		TblHrEmpInfo thei = (TblHrEmpInfo) experEmpInfo.get(i);
//		if (thei == null) {
//			thei = new TblHrEmpInfo();
//			newsFdbk.add(i, thei);
//		}
//		
//		return thei;
//	}
	/**
	 * @return newsFdbk
	 */
	public List getNewsFdbk() {
		return newsFdbk;
	}

	public TblUfNewsFdbk getNewsFdbk(int i){
        TblUfNewsFdbk tunf = (TblUfNewsFdbk) newsFdbk.get(i);
        if (tunf == null) {
            tunf = new TblUfNewsFdbk();
//            pubPermission.add(i, tupp);
        } else {
            if(tunf.getTblHrEmpInfo()==null){
                //加入职工信息
                String empId=request.getParameter("experEmpInfo[" + i + "].id");
                if (StringUtils.isNotBlank(empId)) {
                	TblCmnUser emp=(TblCmnUser)getService().load(TblCmnUser.class,empId);
                    tunf.setTblHrEmpInfo(emp);
                }
            }
        }
		
		return tunf;
	}
	/**
	 * @param newsFdbk 要设置的 newsFdbk
	 */
	public void setNewsFdbk(List newsFdbk) {
		this.newsFdbk = newsFdbk;
	}

//	/**
//	 * @return listNewsFdbk
//	 */
//	public List getListNewsFdbk() {
//		return listNewsFdbk;
//	}
//	 
//	/**
//	 * @param listNewsFdbk 要设置的 listNewsFdbk
//	 */
//	public void setListNewsFdbk(List listNewsFdbk) {
//		this.listNewsFdbk = listNewsFdbk;
//	}

	/**
	 * @return tblUfColumnid
	 */
	public String getTblUfColumnid() {
		return tblUfColumnid;
	}

	/**
	 * @param tblUfColumnid 要设置的 tblUfColumnid
	 */
	public void setTblUfColumnid(String tblUfColumnid) {
		this.tblUfColumnid = tblUfColumnid;
	}

	/**
	 * @return bean
	 */
	public TblUfNews getBean() {
        if (bean == null) {
            if (StringUtils.isBlank(getOid())) {
                bean = new TblUfNews();
            }
            else {
                bean = (TblUfNews)getService().load(TblUfNews.class, getOid());
            }
        }
        return bean;        
	}

	/**
	 * @param bean 要设置的 bean
	 */
	public void setBean(TblUfNews bean) {
		this.bean = bean;
	}

    /**
     * @return the columnList
     */
    public ComboSupportList getColumnList() {
        return columnList;
    }

    /**
     * @param columnList the columnList to set
     */
    public void setColumnList(ComboSupportList columnList) {
        this.columnList = columnList;
    }

    /**
     * @return the attachement
     */
    public List getAttachement() {
        return attachement;
    }

    /**
     * @return the TblUfNewsAttach
     */
    public TblUfNewsAttach getAttachement(int i) {
        TblUfNewsAttach tuna = (TblUfNewsAttach)attachement.get(i);
        if (tuna == null){
            tuna = new TblUfNewsAttach();
//            tuna.setTblUfNews(new TblUfNews());
            attachement.add(i,tuna);
        }
        return tuna;
    }

    /**
     * @param attachement the attachement to set
     */
    public void setAttachement(List attachement) {
        this.attachement = attachement;
    }

    /**
     * 获取共通服务
     * @return
     */
    private CommonService getService() {
        return (CommonService)ServiceProvider.getService(CommonService.SERVICE_NAME);
    }

    /**
     * @return the resourceUrl
     */
    public String getResourceUrl() {
        return resourceUrl;
    }

    /**
     * @param resourceUrl the resourceUrl to set
     */
    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

}
