package net.silencily.sailing.common.context;

import javax.servlet.http.HttpServletRequest;

import net.silencily.sailing.context.BusinessContext;
import net.silencily.sailing.context.OperType;
import net.silencily.sailing.security.model.RWCtrlType;


/**
 * 
 * @author wenjb
 * @version 1.0
 */
public class MessageTypeContext {

	/**
	 * 功能描述 返回当前的页面对应的页面类型.
	 *        参数:当前的ACTION中的REQUEST请求，实体BEAN的rwCtrlType 
	 * 2008-1-27 下午02:19:13
	 * 
	 * @param request
	 *            rwCtrlType
	 * @version 1.0
	 * @author wenjb
	 */
	public static int messageType(HttpServletRequest request, int rwCtrlType) {

		// 处理没有数据又不能新增的特殊情况,特殊业务需求
		try {
			if (!BusinessContext.isNull()) {
				if (BusinessContext.getOperType() == OperType.NotAddForNotData) {
					return OperType.NotAddForNotData;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 新增页面
		if (request.getParameter("oid") == null
				|| request.getParameter("oid").toString().length() == 0) {
			return OperType.ADD;
		}
		String tag = WorkFlowFormContext.getTag();
		if ("1".equals(tag)) {
			return OperType.EDIT;
		} else {
			// 工作流外部的情况
			try {
				if (!BusinessContext.isNull()) {
					if (BusinessContext.getOperType() == OperType.EDIT) {
						return OperType.EDIT;
					} else if (BusinessContext.getOperType() == OperType.VIEW) {
						return OperType.VIEW;
					} else {
						throw new Exception("画面类型设定错误!");
					}
				} else if (rwCtrlType != -1) {
					switch (rwCtrlType) {
					case RWCtrlType.EDIT:
						return OperType.EDIT;
					case RWCtrlType.READ_ONLY:
						return OperType.VIEW;
					default:
						return OperType.VIEW;
					}
				} else {
					throw new Exception("无法判断画面类型!");
				}
			} catch (Exception e) {
				e.printStackTrace();
				return OperType.EDIT;
			}
		}
	}
}
