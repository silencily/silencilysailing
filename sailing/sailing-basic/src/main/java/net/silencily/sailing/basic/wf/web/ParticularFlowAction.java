package net.silencily.sailing.basic.wf.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.basic.sm.domain.TblCmnRole;
import net.silencily.sailing.basic.wf.domain.TblWfFormInfo;
import net.silencily.sailing.basic.wf.domain.TblWfOperationInfo;
import net.silencily.sailing.basic.wf.domain.TblWfParticularInfo;
import net.silencily.sailing.basic.wf.service.WFService;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.utils.MessageInfo;
import net.silencily.sailing.utils.MessageUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

/**
 * @author gejb time 2009-05-08
 * @deprecated   Use OperationAction instead
 * @see com.qware.wf.web.OperationAction
 */
public class ParticularFlowAction extends DispatchAction {

	WFService wFService = null;

	public static final String FORWARD_LIST = "list";// 总业务信息列表

	public static final String FLOWNAME_LIST = "edits";// 业务名称列表

	public static final String FORWARD_TOPLIST = "toplist";// 返回总业务信息列表

	public static final String FOEWARD_MAIN = "main";// 返回工作流信息表

	private String upId = "";// 步骤信息表id

	String operNameSelect;
	
	String nameSelect;
	
	String opeditionSelect;
	/**
	 * @author yangxl time 2007-11-30 功能描述 列表
	 * 
	 */
	public ActionForward list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		List list = new ArrayList();
		Set set = new HashSet();
		DynaActionForm Form = (DynaActionForm) form;
		String ooid = Form.get("ooid").toString();// 主表ID1
		String id = request.getParameter("idw");// 主表ID2
		TblWfOperationInfo tblWfOperationInfo = new TblWfOperationInfo();
		wFService = (WFService) ServiceProvider
				.getService(WFService.SERVICE_NAME);
		if (ooid != null && !("").equals(ooid)) {
			tblWfOperationInfo = wFService.findWfOperInfoById(ooid);
			set = tblWfOperationInfo.getTblWfParticularInfos();
			Object[] array = set.toArray();
			for (int i = 0; i < array.length; i++) {
				TblWfParticularInfo tbl = (TblWfParticularInfo) array[i];
				//角色
				TblCmnRole tcr = new TblCmnRole();
				String roles = tbl.getTblUserId();
				String[] ro = null;
				StringBuffer lro = new StringBuffer();
				if (roles != null && !("").equals(roles)) {
					ro = roles.split(",");
					for (int g = 0; g < ro.length; g++) {
						String roleCd = ro[g].toString();
						tcr = wFService.findroleCdRole(roleCd);
						if(tcr!=null && !tcr.equals("")){
							lro.append(tcr.getName());
							lro.append(",");
						}
					}
				}
				tbl.setTblUserId(lro.toString());
				list.add(tbl);
			}
			request.setAttribute("ooid", ooid);
		}
		if (id != null && !("").equals(id)) {
			tblWfOperationInfo = wFService.findWfOperInfoById(id);
			set = tblWfOperationInfo.getTblWfParticularInfos();
			Object[] array = set.toArray();
			for (int i = 0; i < array.length; i++) {
				TblWfParticularInfo tbl = (TblWfParticularInfo) array[i];
				//角色
				TblCmnRole tcr = new TblCmnRole();
				String roles = tbl.getTblUserId();
				String[] ro = null;
				StringBuffer lro = new StringBuffer();
				if (roles != null && !("").equals(roles)) {
					ro = roles.split(",");
					for (int g = 0; g < ro.length; g++) {
						String roleCd = ro[g].toString();
						tcr = wFService.findroleCdRole(roleCd);
						if(tcr!=null && !tcr.equals("")){
							lro.append(tcr.getName());
							lro.append(",");
						}
					}
				}
				tbl.setTblUserId(lro.toString());
				list.add(tbl);
			}
			request.setAttribute("ooid", id);
		}
		request.setAttribute("lists", list);
		operNameSelect = request.getParameter("operNames");
		nameSelect = request.getParameter("names");
		opeditionSelect = request.getParameter("opeditions");
		request.setAttribute("operNameSelect", operNameSelect);
		request.setAttribute("nameSelect", nameSelect);
		request.setAttribute("opeditionSelect", opeditionSelect);
		return mapping.findForward(FORWARD_LIST);
	}

	/**
	 * @author yangxl time 2007-11-30 功能描述 编辑页面（业务名称列表）
	 * 
	 */
	public ActionForward edit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {

		TblWfParticularInfo tblWfParticularInfo = new TblWfParticularInfo();
		TblWfOperationInfo tblWfOperationInfo = new TblWfOperationInfo();
		Set qxli = new HashSet();
		Set set = new HashSet();
		Set set1 = new HashSet();
		String fid = "";
		String fname = "";
		String fcode = "";
		String feditor ="";
		DynaActionForm Form = (DynaActionForm) form;

		upId = request.getParameter("idd");
		String ooid= "";
		ooid = Form.get("ooid").toString();
		if(ooid.equals("") || ooid.equals(null)){
			ooid = request.getAttribute("ooid").toString();
		}
		if (upId != null && !("").equals(upId)) {
			if (ooid != null && !("").equals(ooid)) {
				tblWfOperationInfo = wFService.findWfOperInfoById(ooid);
				set = tblWfOperationInfo.getTblWfParticularInfos();
				for (int i = 0; i < set.size(); i++) {
					TblWfParticularInfo temp = (TblWfParticularInfo) set
							.toArray()[i];
					if (temp.getId().equals(upId)) {
						tblWfParticularInfo = temp;
						break;
					}
				}
				request.setAttribute("wfpinfo", tblWfParticularInfo);

				set1 = tblWfParticularInfo.getTblWfFormInfos();

				if (set1.size() != '0') {
					for (int j = 0; j < set1.size(); j++) {

						TblWfFormInfo tblWfFormInfo = (TblWfFormInfo) set1
								.toArray()[j];
						if (tblWfFormInfo.getId() != null
								&& !("").equals(tblWfFormInfo.getId())) {
							fid = tblWfFormInfo.getId();
						}
						if (tblWfFormInfo.getFieldName() != null
								&& !("").equals(tblWfFormInfo.getFieldName())) {
							fname = tblWfFormInfo.getFieldName();
						}

						if (tblWfFormInfo.getFieldCode() != null
								&& !("").equals(tblWfFormInfo.getFieldCode())) {
							fcode = tblWfFormInfo.getFieldCode();
						}
						if (tblWfFormInfo.getEditorFlg() != null
								&& !("").equals(tblWfFormInfo.getEditorFlg())) {
							feditor = tblWfFormInfo.getEditorFlg();
						}
						qxli.add(new Column(fid, fname, fcode,feditor));
					}
				}
			}

			// 生成提交退回列表
			String stepNmu = tblWfOperationInfo.getStepNum();
			int sN = 0;
			if(stepNmu!=null && !("").equals(stepNmu)){
				sN = new Integer(stepNmu).intValue();
			}else{
				sN = 50;
			}
			List ln = new ArrayList();
			for (int i = 1; i < sN; i++) {
				String num = new Integer(i).toString();
				ln.add(num);
			}
			ln.add("99");
			// 提交
			String stepOn = tblWfParticularInfo.getCommitStep();
			String[] sO = null;
			List lo = new ArrayList();// 选中提交步骤
			List ls = new ArrayList();// 所有提交步骤
			if (stepOn != null && !("").equals(stepOn)) {
				sO = stepOn.split(",");
				for (int j = 0; j < sO.length; j++) {
					lo.add(sO[j].toString());
				}
				for (int m = 0; m < ln.size(); m++) {

					int vN = m + 1;
					String vnum = new Integer(vN).toString();
					String stName = wFService.findTblWfParticularInfoName(ooid, vnum);
					if (lo.contains(ln.toArray()[m].toString())) {
						String vsel = "SELECTED";// 选中状态
						if(vN==ln.size()){
							ls.add(new StepsVo("END"+"结束", vsel,vnum));
						}else{
							ls.add(new StepsVo(vnum+stName, vsel,vnum));
						}
					} else {
						String vsel = "";
						if(vN==ln.size()){
							ls.add(new StepsVo("END"+"结束", vsel,vnum));
						}else{
							ls.add(new StepsVo(vnum+stName, vsel,vnum));
						}
					}
				}
			} else {
				for (int i = 1; i <= sN; i++) {
					String num = new Integer(i).toString();
					String stName = wFService.findTblWfParticularInfoName(ooid, num);
					String vsel = "";
					if(i==sN){
						ls.add(new StepsVo("END"+"结束", vsel,num));
					}else{
						ls.add(new StepsVo(num+stName, vsel,num));
					}
				}
			}
			// 退回
			String stepOff = tblWfParticularInfo.getGobackStep();
			List lt = new ArrayList();// 所有退回步骤
			for (int n = 0; n < ln.size(); n++) {
				int vN = n + 1;
				String vnum = new Integer(vN).toString();
				String stName = wFService.findTblWfParticularInfoName(ooid, vnum);
				if (ln.toArray()[n].toString().equals(stepOff)) {
					String vsel = "SELECTED";// 选中状态
					if(vN==ln.size()){
						lt.add(new StepsVo("END"+"结束", vsel,vnum));
					}else{
						lt.add(new StepsVo(vnum+stName, vsel,vnum));
					}
				} else {
					String vsel = "";
					if(vN==ln.size()){
						lt.add(new StepsVo("END"+"结束", vsel,vnum));
					}else{
						lt.add(new StepsVo(vnum+stName, vsel,vnum));
					}
				}
			}

			request.setAttribute("gobacklist", lt);
			request.setAttribute("commitlist", ls);
			// 角色
			TblCmnRole tcr = new TblCmnRole();
			String roles = tblWfParticularInfo.getTblUserId();
			String[] ro = null;
			List lro = new ArrayList();
			if (roles != null && !("").equals(roles)) {
				ro = roles.split(",");
				for (int g = 0; g < ro.length; g++) {
					String roleCd = ro[g].toString();
					tcr = wFService.findroleCdRole(roleCd);
					if(tcr!=null && !tcr.equals("")){
						lro.add(tcr);
					}
				}
			}
			if(lro.size()>0){
				request.setAttribute("roleli", lro);
			}
			// 权限
			if (qxli == null ||qxli.size()<=0) {
				List colums = new ArrayList();
				colums.add(new Column("", "", "",""));
				request.setAttribute("list", colums);
			} else {
				request.setAttribute("list", qxli);
			}
			MessageUtils.addMessage(request, MessageInfo.factory().getMessage(
			"WF_I010_p_0"));
			request.setAttribute("ooid", ooid);
		} else {
			// 生成提交退回列表
			tblWfOperationInfo = wFService.findWfOperInfoById(ooid);
			
			String stepNmu = tblWfOperationInfo.getStepNum();
			int sN = 0;
			if(stepNmu!=null && !("").equals(stepNmu)){
				sN = new Integer(stepNmu).intValue();
			}else{
				sN = 50;
			}
			List l1 = new ArrayList();
			for (int i = 1; i <= sN; i++) {
				String num = new Integer(i).toString();
				String stName = wFService.findTblWfParticularInfoName(ooid, num);
				String vsel = "";
				if(i==sN){
					l1.add(new StepsVo("END"+"结束", vsel,num));
				}else{
					l1.add(new StepsVo(num+stName, vsel,num));
				}
				
			}
			request.setAttribute("gobacklist", l1);
			request.setAttribute("commitlist", l1);

			List colums = new ArrayList();
			colums.add(new Column("", "", "",""));
			request.setAttribute("list", colums);
			request.setAttribute("ooid", ooid);
			//yangxl 2008-5-27 新加 用来控制步骤id自动加一 start
			Set sets = tblWfOperationInfo.getTblWfParticularInfos();
			int maxStepId = 0;
			List listStepId = new ArrayList();
			if(sets.size()>0){
				for(Iterator it=sets.iterator();it.hasNext();){
					TblWfParticularInfo tblWfPar = (TblWfParticularInfo)it.next();
					if(tblWfPar!=null){
						if(tblWfPar.getStepId()!=null){
							listStepId.add(tblWfPar.getStepId());
						}
					}
				}
				for(int i = 0; i < listStepId.size(); i++){
					for( int j = 0; j < listStepId.size(); j++){
						int a = new Long(listStepId.get(i).toString()).intValue();
						int b = new Long(listStepId.get(j).toString()).intValue();
						if(b>=a){
							if(b != 99){
								maxStepId = b + 1;
							}
						}
					}
				}
				tblWfParticularInfo.setStepId(new Long(maxStepId));
				request.setAttribute("wfpinfo", tblWfParticularInfo);
			}else{
				tblWfParticularInfo.setStepId(new Long("1"));
				request.setAttribute("wfpinfo", tblWfParticularInfo);
			}
			//end
			MessageUtils.addMessage(request, MessageInfo.factory().getMessage(
			"WF_I009_p_0"));
		}
		operNameSelect = request.getParameter("operNames");
		nameSelect = request.getParameter("names");
		opeditionSelect = request.getParameter("opeditions");
		request.setAttribute("operNameSelect", operNameSelect);
		request.setAttribute("nameSelect", nameSelect);
		request.setAttribute("opeditionSelect", opeditionSelect);
		String nameoper = tblWfOperationInfo.getName();//流程名称
		request.setAttribute("nameoper", nameoper);
		return mapping.findForward(FLOWNAME_LIST);

	}

	/**
	 * @author yangxl time 2007-11-30 功能描述 保存
	 * 
	 */
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		
		DynaActionForm Form = (DynaActionForm) form;
		String ooid = Form.get("ooid").toString();//流程信息id
		//2008-5-29 yangxl stepId唯一性验证 Start
		boolean boolStepId = false;
		boolStepId = stepIdCheck(Form,request,ooid);
		if(!boolStepId){
			return mapping.findForward(FLOWNAME_LIST);
		}
		//stepId唯一性验证end
		//2008-6-30 yangxl stepNamw唯一性验证 Start
		boolean boolStepName = false;
		boolStepName = stepNameCheck(Form,request,ooid);
		if(!boolStepName){
			return mapping.findForward(FLOWNAME_LIST);
		}
		//stepName唯一性验证end
		String fid = null;// id
		String fieldName = null;// 表单项
		String fieldCode = null;// 表单KEY
		String fowardEdit = null;// 提交
		String editorFlg = null;//是否可入例
		//2008-6-2 yangxl通过ooid获得步骤总数Strat 
		TblWfOperationInfo tblWfOperNum = new TblWfOperationInfo();
		tblWfOperNum = wFService.findWfOperInfoById(ooid);
		String stepIdSum = null;//步骤总数
		if(tblWfOperNum!=null){
			stepIdSum = tblWfOperNum.getStepNum();
		}
		//end
		TblWfParticularInfo tblWfParticularInfosave = new TblWfParticularInfo();
		if(StringUtils.isNotBlank(upId))
			tblWfParticularInfosave = wFService.findWfParticularInfoById(upId);
		else
			tblWfParticularInfosave = new TblWfParticularInfo();
		tblWfParticularInfosave.setStepId(new Long(Form.get("stepId").toString().trim()));
		tblWfParticularInfosave.setStepName(Form.get("stepName").toString().trim());
		tblWfParticularInfosave.setDealInfo(Form.get("dealInfo").toString().trim());
		tblWfParticularInfosave.setPointStep(Form.get("pointStep").toString().trim());
		//2008-5-28yangxl加步骤检查 start
		tblWfParticularInfosave.setStepChecks(Form.get("stepChecks").toString().trim());
		//end
		if (Form.get("goback").toString() != null
				&& !("").equals(Form.get("goback").toString())) {
			tblWfParticularInfosave.setGoback("Y");
		} else {
			tblWfParticularInfosave.setGoback("N");
		}
		tblWfParticularInfosave.setGobackStep(Form.get("gobackStep").toString());
		if (Form.get("cancel").toString() != null
				&& !("").equals(Form.get("cancel").toString())) {
			tblWfParticularInfosave.setCancel("Y");
		} else {
			tblWfParticularInfosave.setCancel("N");
		}
		if (Form.get("fetch").toString() != null
				&& !("").equals(Form.get("fetch").toString())) {
			tblWfParticularInfosave.setFetch("Y");
		} else {
			tblWfParticularInfosave.setFetch("N");
		}
		if (Form.get("suspend").toString() != null
				&& !("").equals(Form.get("suspend").toString())) {
			tblWfParticularInfosave.setSuspend("Y");
		} else {
			tblWfParticularInfosave.setSuspend("N");
		}
		if (Form.get("passround").toString() != null
				&& !("").equals(Form.get("passround").toString())) {
			tblWfParticularInfosave.setPassround("Y");
		} else {
			tblWfParticularInfosave.setPassround("N");
		}
		if (Form.get("commit").toString() != null
				&& !("").equals(Form.get("commit").toString())) {
			tblWfParticularInfosave.setCommit("Y");
		} else {
			tblWfParticularInfosave.setCommit("N");
		}
		//2008-6-2yangxl 验证提交步骤是否是最后一步
		String commitStepChecks = Form.get("commitStep").toString();
		StringBuffer commitStepChecksnew = new StringBuffer();
		if(StringUtils.isNotBlank(commitStepChecks)){
			String[] commitStepsz = commitStepChecks.split(",");
			for(int t = 0; t < commitStepsz.length; t++){
				if(commitStepsz[t].toString().equals(stepIdSum)){
					commitStepsz[t] = "99";
				}
				commitStepChecksnew.append(commitStepsz[t]);
				commitStepChecksnew.append(",");
			}

		}
		//end
		tblWfParticularInfosave.setCommitStep(commitStepChecksnew.toString());
		if (Form.get("togethersign").toString() != null
				&& !("").equals(Form.get("togethersign").toString())) {
			tblWfParticularInfosave.setTogethersign("Y");
		} else {
			tblWfParticularInfosave.setTogethersign("N");
		}
		
		tblWfParticularInfosave.setHelpman(Form.get("helpman").toString());
		if (Form.get("message").toString() != null
				&& !("").equals(Form.get("message").toString())) {
			tblWfParticularInfosave.setMessage("Y");
		} else {
			tblWfParticularInfosave.setMessage("N");
		}
		tblWfParticularInfosave.setDelFlg("0");
		//String ooid = Form.get("ooid").toString();
		TblWfOperationInfo tblWfOperationInfo = new TblWfOperationInfo();

		wFService = (WFService) ServiceProvider
				.getService(WFService.SERVICE_NAME);
		//角色
		StringBuffer tblUserId = new StringBuffer();
		String userId = Form.get("tblUserId").toString().trim();
		if(userId!=null && !("").equals(userId)){
			String[] userIdsz =userId.split(",");
			if(userIdsz.length>0){
				TblCmnRole tcr = new TblCmnRole();
				for(int u = 0; u < userIdsz.length; u++){
					tcr=wFService.findidRole(userIdsz[u]);
					if(tcr!=null){
						if(tcr.getRoleCd()!=null && !("").equals(tcr.getRoleCd())){
							tblUserId.append(tcr.getRoleCd());
							tblUserId.append(",");
						}
					}
				}
			}
		}
		tblWfParticularInfosave.setTblUserId(tblUserId.toString());
		if (ooid != null && !("").equals(ooid)) {
			tblWfOperationInfo = wFService.findWfOperInfoById(ooid);
			tblWfParticularInfosave.setTblWfOperationInfo(tblWfOperationInfo);
		}

		try {
				String fieldStatus = Form.get("fieldStatus").toString();
				tblWfParticularInfosave.setFieldStatus(fieldStatus);
				// 取得权限
				if (fieldStatus.equals("3")) {
					//tblWfParticularInfosave findPar = wFService.findWfParticularInfoById(upId);
					Set formSet = tblWfParticularInfosave.getTblWfFormInfos();
					List listForm = new ArrayList();
					String[] popedom = request.getParameterValues("row");
					//2008-3-12关于删除第一条的问题
					if(popedom[0].equals("N") || popedom[0].equals("Y")){
						String[] po = new String[popedom.length-1];
						for(int i = 1; i < popedom.length; i++){
							po[i-1]=popedom[i].toString();
						}
						for(int j = 0; j < po.length; j++){
							popedom=po;
						}
					}
					for(Iterator it=formSet.iterator();it.hasNext();){
						
						boolean delflg = true;
						TblWfFormInfo tbl = (TblWfFormInfo) it.next();
						//判断是否删除
						int i=0;
						if (popedom != null && !("").equals(popedom))
						{
							for(i=0;i<popedom.length;i+=4)
							{
								if(popedom[i].equalsIgnoreCase(tbl.getId()))
								{
										delflg = false;
										break;
								}
							}
							if(!delflg)//更新
							{
								fid = popedom[i];
								fieldName = popedom[i + 1];
								fieldCode = popedom[i + 2];
								editorFlg = popedom[i + 3];
								tbl.setFieldName(fieldName);
								tbl.setFieldCode(fieldCode);
								tbl.setTblWfOpeInfId(ooid);
								tbl.setEditorFlg(editorFlg);
								tbl.setDelFlg("0");
							}
						}
						//删除
						if(delflg){
							listForm.add(tbl);
						}
					}
					for(Iterator it = listForm.iterator();it.hasNext();){
						TblWfFormInfo tbl = (TblWfFormInfo)it.next();
						formSet.remove(tbl);
						tbl.setTblWfParticularInfo(null);
						tbl.setDelFlg("1");
						//getService().delete(tbl);
					}
					//新增
					if (popedom != null && !("").equals(popedom)) {
						for (int i = 0; i < popedom.length; i+=4) {
							fid = popedom[i];
							if(StringUtils.isBlank(fid))
							{
								fieldName = popedom[i + 1];
								fieldCode = popedom[i + 2];
								editorFlg = popedom[i + 3];
								TblWfFormInfo tblWfFormInfo = new TblWfFormInfo();
								tblWfFormInfo.setFieldName(fieldName);
								tblWfFormInfo.setFieldCode(fieldCode);
								tblWfFormInfo.setEditorFlg(editorFlg);
								tblWfFormInfo.setTblWfOpeInfId(ooid);
								tblWfFormInfo.setDelFlg("0");
								tblWfFormInfo
										.setTblWfParticularInfo(tblWfParticularInfosave);
								tblWfParticularInfosave.getTblWfFormInfos().add(tblWfFormInfo);
								
							}
						}
					}

				}
				//验证stepName是否重复
				/*String stepName = Form.get("stepName").toString().trim();
				boolean bool = false ;
				bool = wFService.findbooleanOpinstepName(stepName, tblWfOperationInfo);
				if(bool){*/
				tblWfOperationInfo.getTblWfParticularInfos().add(tblWfParticularInfosave);
					wFService.updateWfParInfo(tblWfParticularInfosave);
				//}
				
				fowardEdit = "main";

		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("WF_I028_p_0"));
		}
		String stepNmu = tblWfOperationInfo.getStepNum();
		int sN = new Integer(stepNmu).intValue();
		List l1 = new ArrayList();
		for (int i = 1; i <= sN; i++) {
			String num = new Integer(i).toString();
			String vsel = "";
			l1.add(new StepsVo(num, vsel,num));
		}
		request.setAttribute("ooid", ooid);
		MessageUtils.addMessage(request, MessageInfo.factory().getMessage(
		"WF_I027_p_0"));
		return mapping.findForward(fowardEdit);
	}
	
	/**
	 * @author yangxl time 2007-11-30 功能描述 保存全部并生成XML
	 * 
	 */
	public ActionForward createXML(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {

		boolean boolStepId = false;
		String dir = this.getServlet().getServletContext().getRealPath("")
				+"\\"+"WEB-INF"+"\\"+"classes"+"\\"+"META-INF";
		DynaActionForm Form = (DynaActionForm) form;
		String ooid = Form.get("ooid").toString();
		TblWfOperationInfo tblWfOperationInfo = new TblWfOperationInfo();
		wFService = (WFService) ServiceProvider
				.getService(WFService.SERVICE_NAME);
		tblWfOperationInfo = wFService.findWfOperInfoById(ooid);
		//2008-5-29yangxl 判断流程中是否有步骤结束语句 start
		operNameSelect = request.getParameter("operNames");
		nameSelect = request.getParameter("names");
		opeditionSelect = request.getParameter("opeditions");
		Set set = tblWfOperationInfo.getTblWfParticularInfos();
		for(Iterator it =set.iterator(); it.hasNext();){
			TblWfParticularInfo tblWfParticularInfo = (TblWfParticularInfo)it.next();
			if(tblWfParticularInfo!=null){
				String stepId = tblWfParticularInfo.getStepId().toString();
				if(!stepId.equals("99")){
					boolStepId = false;
				}else{
					boolStepId = true;
				}
			}
		}
		if(!boolStepId){
			request.setAttribute("operNameSelect", operNameSelect);
			request.setAttribute("nameSelect", nameSelect);
			request.setAttribute("opeditionSelect", opeditionSelect);
			MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("WF_I003_p_0"));
			return mapping.findForward(FOEWARD_MAIN);
		}else{
			
			CreateXml createXml = new CreateXml();
			createXml.saveWfInfo(tblWfOperationInfo, dir);
			request.setAttribute("operNameSelect", operNameSelect);
			request.setAttribute("nameSelect", nameSelect);
			request.setAttribute("opeditionSelect", opeditionSelect);
			MessageUtils.addMessage(request, MessageInfo.factory().getMessage(
			"WF_I004_p_0"));
		}
		//判断流程中是否有步骤结束语句 end
		return mapping.findForward(FORWARD_TOPLIST);
	}

	/**
	 * @author yangxl time 2007-11-30 功能描述 删除
	 * 
	 */
	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String[] deleteId = request.getParameterValues("id");
		wFService = (WFService) ServiceProvider
				.getService(WFService.SERVICE_NAME);
		for (int i = 0; i < deleteId.length; i++) {
			wFService.deleteWfParInfo(deleteId[i]);
		}
		operNameSelect = request.getParameter("operNames");
		nameSelect = request.getParameter("names");
		opeditionSelect = request.getParameter("opeditions");
		request.setAttribute("operNameSelect", operNameSelect);
		request.setAttribute("nameSelect", nameSelect);
		request.setAttribute("opeditionSelect", opeditionSelect);
		MessageUtils.addMessage(request, MessageInfo.factory().getMessage(
		"WF_I008_p_0"));
		return mapping.findForward(FOEWARD_MAIN);
	}
	
	/**
	 * @author yangxl time 2008-05-28 功能描述 检查stepId是否唯一
	 * 
	 */
	public boolean stepIdCheck(DynaActionForm Form,
			HttpServletRequest request,String ooid){
		
		boolean bool = false;
		String oldstepId = ""; //步骤原id
		if(StringUtils.isNotBlank(upId)){
			TblWfParticularInfo tblWfParupId=(TblWfParticularInfo)getService().load(TblWfParticularInfo.class, upId);
			if(tblWfParupId!=null){
				if(tblWfParupId.getStepId()!=null){
					oldstepId = tblWfParupId.getStepId().toString();
				}
			}
		}
		String stepId = Form.get("stepId").toString().trim();
		//判断如果id没有改变就不进行唯一性验证
		if(!oldstepId.equals(stepId)){
			TblWfOperationInfo tblWfOperChecks = new TblWfOperationInfo();
			TblWfParticularInfo tblWfParChecks = new TblWfParticularInfo();
			tblWfOperChecks = wFService.findWfOperInfoById(ooid);
			if(tblWfOperChecks!=null){
				Set set = tblWfOperChecks.getTblWfParticularInfos();
				String numStep = tblWfOperChecks.getStepNum();//流程定义的步骤
				if(set.size()>0){
					for(Iterator it=set.iterator();it.hasNext();){
						tblWfParChecks = (TblWfParticularInfo)it.next();
						if(tblWfParChecks!=null){
							String stepIdInfo = tblWfParChecks.getStepId().toString();
							if(stepId.equals(stepIdInfo)){
								MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("WF_I002_p_0"));
								String stepNameCh = Form.get("stepName").toString().trim();
								String dealInfoCh = Form.get("dealInfo").toString().trim();
								String pointStepCh = Form.get("pointStep").toString().trim();
								String stepChecksCh = Form.get("stepChecks").toString().trim();
								String gobackStepCh = Form.get("gobackStep").toString().trim();
								String commitStepCh = Form.get("commitStep").toString().trim();
								String helpmanCh = Form.get("helpman").toString().trim();
								String fieldStatusCh = Form.get("fieldStatus").toString().trim();
								tblWfParChecks.setStepId(new Long(stepId));
								tblWfParChecks.setStepName(stepNameCh);
								tblWfParChecks.setDealInfo(dealInfoCh);
								tblWfParChecks.setPointStep(pointStepCh);
								tblWfParChecks.setStepChecks(stepChecksCh);
								//tblWfParChecks.setGobackStep(gobackStepCh);
								//tblWfParChecks.setCommitStep(commitStepCh);
								tblWfParChecks.setHelpman(helpmanCh);
								tblWfParChecks.setFieldStatus(fieldStatusCh);
								if (Form.get("goback").toString() != null
										&& !("").equals(Form.get("goback").toString())) {
									tblWfParChecks.setGoback("Y");
								} else {
									tblWfParChecks.setGoback("N");
								}
								if (Form.get("cancel").toString() != null
										&& !("").equals(Form.get("cancel").toString())) {
									tblWfParChecks.setCancel("Y");
								} else {
									tblWfParChecks.setCancel("N");
								}
								if (Form.get("fetch").toString() != null
										&& !("").equals(Form.get("fetch").toString())) {
									tblWfParChecks.setFetch("Y");
								} else {
									tblWfParChecks.setFetch("N");
								}
								if (Form.get("suspend").toString() != null
										&& !("").equals(Form.get("suspend").toString())) {
									tblWfParChecks.setSuspend("Y");
								} else {
									tblWfParChecks.setSuspend("N");
								}
								if (Form.get("passround").toString() != null
										&& !("").equals(Form.get("passround").toString())) {
									tblWfParChecks.setPassround("Y");
								} else {
									tblWfParChecks.setPassround("N");
								}
								if (Form.get("commit").toString() != null
										&& !("").equals(Form.get("commit").toString())) {
									tblWfParChecks.setCommit("Y");
								} else {
									tblWfParChecks.setCommit("N");
								}
								if (Form.get("togethersign").toString() != null
										&& !("").equals(Form.get("togethersign").toString())) {
									tblWfParChecks.setTogethersign("Y");
								} else {
									tblWfParChecks.setTogethersign("N");
								}
								if (Form.get("message").toString() != null
										&& !("").equals(Form.get("message").toString())) {
									tblWfParChecks.setMessage("Y");
								} else {
									tblWfParChecks.setMessage("N");
								}
								
								
								if(StringUtils.isNotBlank(numStep)){
									int numStepInt = new Integer(numStep).intValue();
									//退回
									List lt = new ArrayList();// 所有退回步骤
									for (int n = 0; n < numStepInt; n++) {
										int vN = n + 1;
										String vnum = new Integer(vN).toString();
										String stName = wFService.findTblWfParticularInfoName(ooid, vnum);
										if (vnum.equals(gobackStepCh)) {
											String vsel = "SELECTED";// 选中状态
											if(vN==numStepInt){
												lt.add(new StepsVo("END"+"结束", vsel,vnum));
											}else{
												lt.add(new StepsVo(vnum+stName, vsel,vnum));
											}
										} else {
											String vsel = "";
											if(vN==numStepInt){
												lt.add(new StepsVo("END"+"结束", vsel,vnum));
											}else{
												lt.add(new StepsVo(vnum+stName, vsel,vnum));
											}
										}
									}
									request.setAttribute("gobacklist", lt);
									
									//提交
									String stepOn = commitStepCh;
									String[] sO = null;
									List lo = new ArrayList();// 选中提交步骤
									List ls = new ArrayList();// 所有提交步骤
									if (stepOn != null && !("").equals(stepOn)) {
										sO = stepOn.split(",");
										for (int j = 0; j < sO.length; j++) {
											lo.add(sO[j].toString());
										}
										for (int m = 0; m < numStepInt; m++) {

											int vN = m + 1;
											String vnum = new Integer(vN).toString();
											String stName = wFService.findTblWfParticularInfoName(ooid, vnum);
											if (lo.contains(vnum)) {
												String vsel = "SELECTED";// 选中状态
												if(vN==numStepInt){
													ls.add(new StepsVo("END"+"结束", vsel,vnum));
												}else{
													ls.add(new StepsVo(vnum+stName, vsel,vnum));
												}
											} else {
												String vsel = "";
												if(vN==numStepInt){
													ls.add(new StepsVo("END"+"结束", vsel,vnum));
												}else{
													ls.add(new StepsVo(vnum+stName, vsel,vnum));
												}
											}
										}
									} else {
										for (int i = 1; i <= numStepInt; i++) {
											String num = new Integer(i).toString();
											String stName = wFService.findTblWfParticularInfoName(ooid, num);
											String vsel = "";
											ls.add(new StepsVo(num+stName, vsel,num));
										}
									}
									request.setAttribute("commitlist", ls);
								}
								
								//角色
								TblCmnRole tcr = new TblCmnRole();
								String roles = Form.get("tblUserId").toString().trim();
								if(StringUtils.isNotBlank(roles)){
									String[] ro = null;
									List lro = new ArrayList();
									if (roles != null && !("").equals(roles)) {
										ro = roles.split(",");
										for (int g = 0; g < ro.length; g++) {
											String roleCd = ro[g].toString();
											tcr = wFService.findidRole(roleCd);
											if(tcr!=null && !tcr.equals("")){
												lro.add(tcr);
											}
										}
									}
									if(lro.size()>0){
										request.setAttribute("roleli", lro);
									}
								}
								//权限控制信息
								if (fieldStatusCh.equals("3")) {
									String fidCh = null;//权限控制信息id
									String fieldNameCh = null;
									String fieldCodeCh = null;
									String editorFlgCh = null;
									Set qxli = new HashSet();
									String[] popedom = request.getParameterValues("row");
									//新增
									if (popedom != null && !("").equals(popedom)) {
										for (int i = 0; i < popedom.length; i+=4) {
											TblWfFormInfo tblWfFormInfo = new TblWfFormInfo();
												fidCh = popedom[i];
												fieldNameCh = popedom[i + 1];
												fieldCodeCh = popedom[i + 2];
												editorFlgCh = popedom[i + 3];
												tblWfFormInfo.setFieldName(fieldNameCh);
												tblWfFormInfo.setFieldCode(fieldCodeCh);
												tblWfFormInfo.setEditorFlg(editorFlgCh);
												qxli.add(new Column(fidCh, fieldNameCh, fieldCodeCh,editorFlgCh));
										}
									}
									// 权限
									if (qxli == null ||qxli.size()<=0) {
										List colums = new ArrayList();
										colums.add(new Column("", "", "",""));
										request.setAttribute("list", colums);
									} else {
										request.setAttribute("list", qxli);
									}
									
								}
								request.setAttribute("wfpinfo", tblWfParChecks);
								request.setAttribute("ooid", ooid);
								bool = false;
								break;
							}else{
								bool = true;
							}
						}
					}
				}else{
					bool = true;
				}
			}
		}else{
			bool = true;
		}
		return bool;
	}
	/**
	 * @author yangxl time 2008-06-26 功能描述 检查stepName是否唯一
	 * 
	 */
	public boolean stepNameCheck(DynaActionForm Form,
			HttpServletRequest request,String ooid){
		
		boolean bool = false;
		String oldstepName = ""; //步骤原id
		if(StringUtils.isNotBlank(upId)){
			TblWfParticularInfo tblWfParupId=(TblWfParticularInfo)getService().load(TblWfParticularInfo.class, upId);
			if(tblWfParupId!=null){
				if(tblWfParupId.getStepName()!=null){
					oldstepName = tblWfParupId.getStepName().toString();
				}
			}
		}
		String stepName = Form.get("stepName").toString().trim();
		//判断如果id没有改变就不进行唯一性验证
		if(!oldstepName.equals(stepName)){
			TblWfOperationInfo tblWfOperChecks = new TblWfOperationInfo();
			TblWfParticularInfo tblWfParChecks = new TblWfParticularInfo();
			tblWfOperChecks = wFService.findWfOperInfoById(ooid);
			if(tblWfOperChecks!=null){
				Set set = tblWfOperChecks.getTblWfParticularInfos();
				String numStep = tblWfOperChecks.getStepNum();//流程定义的步骤
				if(set.size()>0){
					for(Iterator it=set.iterator();it.hasNext();){
						tblWfParChecks = (TblWfParticularInfo)it.next();
						if(tblWfParChecks!=null){
							String stepNameInfo = tblWfParChecks.getStepName().toString();
							if(stepName.equals(stepNameInfo)){
								MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("WF_I029_p_0"));
								String stepId = Form.get("stepId").toString().trim();
								String dealInfoCh = Form.get("dealInfo").toString().trim();
								String pointStepCh = Form.get("pointStep").toString().trim();
								String stepChecksCh = Form.get("stepChecks").toString().trim();
								String gobackStepCh = Form.get("gobackStep").toString().trim();
								String commitStepCh = Form.get("commitStep").toString().trim();
								String helpmanCh = Form.get("helpman").toString().trim();
								String fieldStatusCh = Form.get("fieldStatus").toString().trim();
								tblWfParChecks.setStepId(new Long(stepId));
								tblWfParChecks.setStepName(stepName);
								tblWfParChecks.setDealInfo(dealInfoCh);
								tblWfParChecks.setPointStep(pointStepCh);
								tblWfParChecks.setStepChecks(stepChecksCh);
								tblWfParChecks.setHelpman(helpmanCh);
								tblWfParChecks.setFieldStatus(fieldStatusCh);
								if (Form.get("goback").toString() != null
										&& !("").equals(Form.get("goback").toString())) {
									tblWfParChecks.setGoback("Y");
								} else {
									tblWfParChecks.setGoback("N");
								}
								if (Form.get("cancel").toString() != null
										&& !("").equals(Form.get("cancel").toString())) {
									tblWfParChecks.setCancel("Y");
								} else {
									tblWfParChecks.setCancel("N");
								}
								if (Form.get("fetch").toString() != null
										&& !("").equals(Form.get("fetch").toString())) {
									tblWfParChecks.setFetch("Y");
								} else {
									tblWfParChecks.setFetch("N");
								}
								if (Form.get("suspend").toString() != null
										&& !("").equals(Form.get("suspend").toString())) {
									tblWfParChecks.setSuspend("Y");
								} else {
									tblWfParChecks.setSuspend("N");
								}
								if (Form.get("passround").toString() != null
										&& !("").equals(Form.get("passround").toString())) {
									tblWfParChecks.setPassround("Y");
								} else {
									tblWfParChecks.setPassround("N");
								}
								if (Form.get("commit").toString() != null
										&& !("").equals(Form.get("commit").toString())) {
									tblWfParChecks.setCommit("Y");
								} else {
									tblWfParChecks.setCommit("N");
								}
								if (Form.get("togethersign").toString() != null
										&& !("").equals(Form.get("togethersign").toString())) {
									tblWfParChecks.setTogethersign("Y");
								} else {
									tblWfParChecks.setTogethersign("N");
								}
								if (Form.get("message").toString() != null
										&& !("").equals(Form.get("message").toString())) {
									tblWfParChecks.setMessage("Y");
								} else {
									tblWfParChecks.setMessage("N");
								}
								
								
								if(StringUtils.isNotBlank(numStep)){
									int numStepInt = new Integer(numStep).intValue();
									//退回
									List lt = new ArrayList();// 所有退回步骤
									for (int n = 0; n < numStepInt; n++) {
										int vN = n + 1;
										String vnum = new Integer(vN).toString();
										String stName = wFService.findTblWfParticularInfoName(ooid, vnum);
										if (vnum.equals(gobackStepCh)) {
											String vsel = "SELECTED";// 选中状态
											if(vN==numStepInt){
												lt.add(new StepsVo("END"+"结束", vsel,vnum));
											}else{
												lt.add(new StepsVo(vnum+stName, vsel,vnum));
											}
										} else {
											String vsel = "";
											if(vN==numStepInt){
												lt.add(new StepsVo("END"+"结束", vsel,vnum));
											}else{
												lt.add(new StepsVo(vnum+stName, vsel,vnum));
											}
										}
									}
									request.setAttribute("gobacklist", lt);
									
									//提交
									String stepOn = commitStepCh;
									String[] sO = null;
									List lo = new ArrayList();// 选中提交步骤
									List ls = new ArrayList();// 所有提交步骤
									if (stepOn != null && !("").equals(stepOn)) {
										sO = stepOn.split(",");
										for (int j = 0; j < sO.length; j++) {
											lo.add(sO[j].toString());
										}
										for (int m = 0; m < numStepInt; m++) {

											int vN = m + 1;
											String vnum = new Integer(vN).toString();
											String stName = wFService.findTblWfParticularInfoName(ooid, vnum);
											if (lo.contains(vnum)) {
												String vsel = "SELECTED";// 选中状态
												if(vN==numStepInt){
													ls.add(new StepsVo("END"+"结束", vsel,vnum));
												}else{
													ls.add(new StepsVo(vnum+stName, vsel,vnum));
												}
											} else {
												String vsel = "";
												if(vN==numStepInt){
													ls.add(new StepsVo("END"+"结束", vsel,vnum));
												}else{
													ls.add(new StepsVo(vnum+stName, vsel,vnum));
												}
											}
										}
									} else {
										for (int i = 1; i <= numStepInt; i++) {
											String num = new Integer(i).toString();
											String stName = wFService.findTblWfParticularInfoName(ooid, num);
											String vsel = "";
											ls.add(new StepsVo(num+stName, vsel,num));
										}
									}
									request.setAttribute("commitlist", ls);
								}
								
								//角色
								TblCmnRole tcr = new TblCmnRole();
								String roles = Form.get("tblUserId").toString().trim();
								if(StringUtils.isNotBlank(roles)){
									String[] ro = null;
									List lro = new ArrayList();
									if (roles != null && !("").equals(roles)) {
										ro = roles.split(",");
										for (int g = 0; g < ro.length; g++) {
											String roleCd = ro[g].toString();
											tcr = wFService.findidRole(roleCd);
											if(tcr!=null && !tcr.equals("")){
												lro.add(tcr);
											}
										}
									}
									if(lro.size()>0){
										request.setAttribute("roleli", lro);
									}
								}
								//权限控制信息
								if (fieldStatusCh.equals("3")) {
									String fidCh = null;//权限控制信息id
									String fieldNameCh = null;
									String fieldCodeCh = null;
									String editorFlgCh = null;
									Set qxli = new HashSet();
									String[] popedom = request.getParameterValues("row");
									//新增
									if (popedom != null && !("").equals(popedom)) {
										for (int i = 0; i < popedom.length; i+=4) {
											TblWfFormInfo tblWfFormInfo = new TblWfFormInfo();
												fidCh = popedom[i];
												fieldNameCh = popedom[i + 1];
												fieldCodeCh = popedom[i + 2];
												editorFlgCh = popedom[i + 3];
												tblWfFormInfo.setFieldName(fieldNameCh);
												tblWfFormInfo.setFieldCode(fieldCodeCh);
												tblWfFormInfo.setEditorFlg(editorFlgCh);
												qxli.add(new Column(fidCh, fieldNameCh, fieldCodeCh,editorFlgCh));
										}
									}
									// 权限
									if (qxli == null ||qxli.size()<=0) {
										List colums = new ArrayList();
										colums.add(new Column("", "", "",""));
										request.setAttribute("list", colums);
									} else {
										request.setAttribute("list", qxli);
									}
									
								}
								request.setAttribute("wfpinfo", tblWfParChecks);
								request.setAttribute("ooid", ooid);
								bool = false;
								break;
							}else{
								bool = true;
							}
						}
					}
				}else{
					bool = true;
				}
			}
		}else{
			bool = true;
		}
		return bool;
	}
	private CommonService getService() {
		return (CommonService) ServiceProvider
				.getService(CommonService.SERVICE_NAME);
	}
	
}
