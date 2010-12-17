package net.silencily.sailing.basic.uf.column.web;

import java.util.ArrayList;
import java.util.List;

import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.basic.uf.domain.TblUfColumn;
import net.silencily.sailing.basic.uf.domain.TblUfPubPermission;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.struts.BaseFormPlus;

import org.apache.commons.lang.StringUtils;

public class ColumnManageForm extends BaseFormPlus {
    private TblUfColumn bean;

    private List listDesktop;
	
	private List desk = new ArrayList();

    private List pubPermission = new ArrayList();

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

	/**
	 * @param listDesktop 要设置的 listDesktop
	 */
	public void setListDesktop(List listDesktop) {
		this.listDesktop = listDesktop;
	}
	
	public TblUfColumn getDesk(int i) {
		TblUfColumn tcdt = (TblUfColumn) desk.get(i);
		if (tcdt == null) {
			tcdt = new TblUfColumn();
			desk.add(i, tcdt);

		}
		
		return tcdt;
	}

    /**
     * 
     * @return bean
     */
    public TblUfColumn getBean() {
        if (bean == null) {
            if (StringUtils.isBlank(getOid())) {
                bean = new TblUfColumn();
            }
            else {
                bean = (TblUfColumn)getService().load(TblUfColumn.class, getOid());
            }
        }
        return bean;        
    }

    /**
     * 设置持久化类
     * @param bean
     */
    public void setBean(TblUfColumn bean) {
        this.bean = bean;
    }

    /**
     * @return pubPermission
     */
    public List getPubPermission() {
        return pubPermission;
    }

    public TblUfPubPermission getPubPermission(int i){
        TblUfPubPermission tupp = (TblUfPubPermission) pubPermission.get(i);
        if (tupp == null) {
            tupp = new TblUfPubPermission();
//            pubPermission.add(i, tupp);
        } else {
            if(tupp.getTblHrEmpInfo()==null){
                //加入职工信息
                String empId=request.getParameter("experEmpInfo[" + i + "].id");
                if (StringUtils.isNotBlank(empId)) {
                	TblCmnUser emp=(TblCmnUser)getService().load(TblCmnUser.class,empId);
                    tupp.setTblHrEmpInfo(emp);
                }
            }
        }
        
        return tupp;
    }

    public void setPubPermission(TblUfPubPermission tupp,int i) throws IndexOutOfBoundsException {
        this.pubPermission.set(i, tupp);
    }

    /**
     * @param pubPermission 要设置的 pubPermission
     */
    public void setPubPermission(List pubPermission) {
        this.pubPermission = pubPermission;
    }

    /**
     * 获取共通服务
     * @return
     */
    private CommonService getService() {
        return (CommonService)ServiceProvider.getService(CommonService.SERVICE_NAME);
    }

}
