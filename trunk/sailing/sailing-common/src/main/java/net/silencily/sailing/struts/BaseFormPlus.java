package net.silencily.sailing.struts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.silencily.sailing.code.SysCodeModule;
import net.silencily.sailing.code.SysCodeModuleMap;
import net.silencily.sailing.code.SysCodeSubSysMap;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.common.dict.domain.CommonBasicCode;
import net.silencily.sailing.common.dict.service.BasicCodeService;
import net.silencily.sailing.common.fileload.FileLoadBean;
import net.silencily.sailing.common.fileload.FileLoadBeanPlus;
import net.silencily.sailing.common.fileload.domain.TblCmnUpLoadFile;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.framework.web.struts.BaseForm;
import net.silencily.sailing.framework.web.view.ComboSupportList;
import net.silencily.sailing.utils.Tools;

import org.apache.commons.lang.StringUtils;

/**
 * @author zhaoyf
 * 
 */
public class BaseFormPlus extends BaseForm {
	private static Map sysCodes = new SysCodeSubSysMap();

	private String searchTag;

	private String popSelectType;

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getPopSelectType() {
		return popSelectType;
	}

	public void setPopSelectType(String popSelectType) {
		this.popSelectType = popSelectType;
	}

	public String getSearchTag() {
		return searchTag;
	}

	public void setSearchTag(String searchTag) {
		this.searchTag = searchTag;
	}

	public String getProperties(String propertyname) {
		Object o = Tools.getProperty(this, propertyname);
		if (null == o)
			return null;
		return o.toString();
	}

	public Map getSysCodes() {
		if (sysCodes == null || sysCodes.isEmpty()) {
		    initSysCodes();
		}
		return sysCodes;
	}

	public void setSysCodes(Map sysCodes) {
		BaseFormPlus.sysCodes = sysCodes;
	}

	public static Map getCodes(String key) {
		return (Map) sysCodes.get(key);
	}

	public BaseFormPlus() {
		super();
		// this.initSysCodes();
	}

	private void initSysCodes() {
		//sysCodes = new SysCodeSubSysMap();
		ComboSupportList csl = getBasicCodeService().getComboListAll();
		for (Object o : csl) {
			CommonBasicCode c = (CommonBasicCode) o;
			SysCodeModuleMap scm = (SysCodeModuleMap) sysCodes
					.get(c.getSubid());
			if (scm == null) {
				scm = new SysCodeModuleMap();
			}

			ComboSupportList value = (ComboSupportList) scm
					.get(c.getTypeCode());
			if (value == null) {
				Map contentMap = new HashMap();
				contentMap.put(c.getCode(), c.getName());
				value = new ComboSupportList(contentMap);
			} else {
				Map contentMap = value.getContentMap();
				if (contentMap == null) {
					contentMap = new HashMap();
				}
				contentMap.put(c.getCode(), c.getName());
			}
			scm.setSubSysCode(c.getSubid());
			scm.put(c.getTypeCode(), value);
			sysCodes.put(c.getSubid(), scm);
		}
	}

	public SysCodeModule getSysCodes(String key) {
		SysCodeModule value = (SysCodeModule) sysCodes.get(key);
		if (value == null) {
			SysCodeModule scm = new SysCodeModule();
			scm.setSubSysCode(key.toString());
			value = scm;
			sysCodes.put(key, value);
		}
		return value;
	}

	/* 共通附件上传存储文件信息 */
	private List tblCmnUpLoadFiles = new ArrayList();

	public List getTblCmnUpLoadFiles() {
		return tblCmnUpLoadFiles;
	}

	public TblCmnUpLoadFile getTblCmnUpLoadFiles(int i) {
		while (tblCmnUpLoadFiles.size() < i + 1) {
			tblCmnUpLoadFiles.add(null);
		}
		TblCmnUpLoadFile tblCmnUpLoadFile = (TblCmnUpLoadFile) tblCmnUpLoadFiles
				.get(i);
		if (tblCmnUpLoadFiles.get(i) == null) {
			String id = request.getParameter("tblCmnUpLoadFiles[" + i + "].id");
			if (StringUtils.isBlank(id)) {
				tblCmnUpLoadFile = new TblCmnUpLoadFile();
			} else {
				tblCmnUpLoadFile = (TblCmnUpLoadFile) ((CommonService) ServiceProvider
						.getService(CommonService.SERVICE_NAME)).load(
						TblCmnUpLoadFile.class, id);
			}
			tblCmnUpLoadFiles.set(i, tblCmnUpLoadFile);
		} else {
			if (StringUtils.isBlank(tblCmnUpLoadFile.getId())) {
				String id = request.getParameter("tblCmnUpLoadFiles[" + i
						+ "].id");
				if (StringUtils.isNotBlank(id)) {
					tblCmnUpLoadFile = (TblCmnUpLoadFile) ((CommonService) ServiceProvider
							.getService(CommonService.SERVICE_NAME)).load(
							TblCmnUpLoadFile.class, id);
					tblCmnUpLoadFiles.set(i, tblCmnUpLoadFile);
				}
			}
		}
		return (TblCmnUpLoadFile) tblCmnUpLoadFiles.get(i);
	}

	public void setTblCmnUpLoadFiles(List tblCmnUpLoadFiles) {
		this.tblCmnUpLoadFiles = tblCmnUpLoadFiles;
	}
	
	/* 共通附件上传存储文件 */
	private FileLoadBean filesBean = new FileLoadBeanPlus();

	public FileLoadBean getFilesBean() {
		return filesBean;
	}

	public void setFilesBean(FileLoadBean filesBean) {
		this.filesBean = filesBean;
	}
	
	private BasicCodeService getBasicCodeService() {
		return (BasicCodeService) ServiceProvider.getService(BasicCodeService.SERVICE_NAME);
	}
	
}
