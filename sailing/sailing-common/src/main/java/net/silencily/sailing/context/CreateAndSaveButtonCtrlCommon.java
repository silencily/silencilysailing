package net.silencily.sailing.context;

import javax.servlet.http.HttpSession;

import net.silencily.sailing.security.SecurityContextInfo;
import net.silencily.sailing.security.model.CurrentUser;


/**
 * 功能说明：此类的存在，完全是因为业务共通没有。
 * 
 * @author wenjb
 * @version 1.0
 */
public class CreateAndSaveButtonCtrlCommon {

	/**
	 * 判断当前的页面是否能启用新增按钮 功能描述
	 * 
	 * @return 2008-1-3 下午04:55:32
	 * @version 1.0
	 * @author wenjb
	 */
	public static boolean disableCreateButton() {

		// 新增按钮标志
		boolean disableCreateButton = false;
		boolean formInTheWorkFlow = false;
		// 流内外判定
		if ("1".equals(getFormInTheWorkFlowForCommonButton())) {
			formInTheWorkFlow = true;
		}

		// 获取不到currentUser,权限放开
		int pageRWCtrlType = getPageRWCtrlType();

		// 新增处理
		if (formInTheWorkFlow) {
			// 流内，禁止
			disableCreateButton = true;
		} else {
			// 根据URL默认读写控制权限
			if (2 != pageRWCtrlType) {
				disableCreateButton = true;
			}
			// 从已办来的URL，禁用
			// 从已办URL，传阅，工作流管理的URL，禁用
			if ("alreadyList".equals(WorkFlowFormContextCommon.getUrlKey())
					|| "system".equals(WorkFlowFormContextCommon.getUrlKey())
					|| "passroundList".equals(WorkFlowFormContextCommon
							.getUrlKey())) {
				disableCreateButton = true;
			}
		}
		return disableCreateButton;

	}

	/**
	 * 判断当前的页面是否能启用保存按钮 功能描述
	 * 
	 * @return 2008-1-3 下午04:55:32
	 * @version 1.0
	 * @author wenjb
	 */
	public static boolean disableSaveButton() {
		// 保存按钮标志
		boolean disableSaveButton = false;
		boolean formInTheWorkFlow = false;

		// 流内外判定
		if ("1".equals(getFormInTheWorkFlowForCommonButton())) {
			formInTheWorkFlow = true;
		}
		// 获取不到currentUser,权限放开
		int rowRWCtrlType = getRowRWCtrlType();

		// 当业务自己设置成浏览页的情况，封闭掉保存
		if (BusinessContext.getOperType() == 3) {
			disableSaveButton = true;
		}
		// 保存处理
		if (formInTheWorkFlow) {
			// 流内，判断是不是全部不可编辑
			String fieldStatus = WorkFlowFormContextCommon.getFieldStatus();
			if ("2".equals(fieldStatus)) {
				disableSaveButton = true;
			}
		} else {
			// 根据URL默认读写控制权限
			if (2 != rowRWCtrlType) {
				disableSaveButton = true;
			}
			// 从已办URL，传阅，工作流管理的URL，禁用
			if ("alreadyList".equals(WorkFlowFormContextCommon.getUrlKey())
					|| "system".equals(WorkFlowFormContextCommon.getUrlKey())
					|| "passroundList".equals(WorkFlowFormContextCommon
							.getUrlKey())) {
				disableSaveButton = true;
			}
		}
		return disableSaveButton;
	}

	/**
	 * 功能描述： 判断当前的URL的PageRWCtrlType
	 * 
	 * @return 2008-2-27 下午04:55:32
	 * @version 1.0
	 * @author wenjb
	 */
	public static int getPageRWCtrlType() {
		// 获取不到currentUser,权限放开
		int pageRWCtrlType = 2;
		try {
			// 根据URL默认读写控制权限
			CurrentUser currentUser = SecurityContextInfo.getCurrentUser();
			String url = SecurityContextInfo.getCurrentPageUrl();
			pageRWCtrlType = currentUser.getPageDefaultRWCtrlType(url);
		} catch (Exception e) {
			pageRWCtrlType = 2;
		}
		return pageRWCtrlType;
	}

	/**
	 * 功能描述： 判断当前的数据行的的ROWRWCtrlType
	 * 
	 * @return 2008-2-27 下午04:55:32
	 * @version 1.0
	 * @author wenjb
	 */
	public static int getRowRWCtrlType() {
		// 获取不到currentUser,权限放开
		int rowRWCtrlType = 2;
		// 判断当前页面，采用什么样的权限机制，和主表一致，还是采用从表自身的权限
		String calculateRwCtrlTypeByID = BusinessContext
				.getCalculateRwCtrlTypeByID();
		if ("".equals(calculateRwCtrlTypeByID)) {
			try {
				// 根据oid默认读写控制权限
				String oid = BusinessContext.getUserSetedOid();
				if (null == oid) {
					// 不存在OID的情况，根据PAGERWCTRLTYPE判定
					return getPageRWCtrlType();
				}
				// 当前存在SESSION中的权限信息
				Integer integer = (Integer) (SecurityContextInfo
						.getRwCtrlTypeMap().get(oid));
				rowRWCtrlType = integer.intValue();
				//需求变更 ,流外权限：读写控制权限，只读的话，所有数据的保存不能用。
				int temp = getPageRWCtrlType();
				if(1 == temp){
					rowRWCtrlType = 1;
				}
			} catch (Exception e) {
				rowRWCtrlType = 2;
			}
		}
		return rowRWCtrlType;
	}

	/**
	 * 功能描述： 重新计算流内还是流外,仅用于共通按钮和画面控制情况不一致的情况。
	 * 
	 * @return 2008-2-27 下午04:55:32
	 * @version 1.0
	 * @author wenjb
	 */
	public static String getFormInTheWorkFlowForCommonButton() {
		String formInTheWorkFlow = "2";
		HttpSession session = SecurityContextInfo.getSession();
		if(null==session){
			return "2";
		}else{
			try{
				formInTheWorkFlow = session.getAttribute("formInTheWorkFlowForCommonButton") == null ? null
				: (String) session.getAttribute("formInTheWorkFlowForCommonButton");
			}catch(Exception e){
				//捕获webLogic本身存在的进程抢占，导致session无效问题
			}
		}
		return formInTheWorkFlow;
	}

	public static void setFormInTheWorkFlowForCommonButton(
			String formInTheWorkFlowForCommonButton) {
		HttpSession session = SecurityContextInfo.getSession();
		if(null==session){
			return;
		}
		session.setAttribute("formInTheWorkFlowForCommonButton",
				formInTheWorkFlowForCommonButton);
	}

}
