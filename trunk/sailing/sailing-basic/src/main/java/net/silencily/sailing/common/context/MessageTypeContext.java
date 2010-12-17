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
	 * �������� ���ص�ǰ��ҳ���Ӧ��ҳ������.
	 *        ����:��ǰ��ACTION�е�REQUEST����ʵ��BEAN��rwCtrlType 
	 * 2008-1-27 ����02:19:13
	 * 
	 * @param request
	 *            rwCtrlType
	 * @version 1.0
	 * @author wenjb
	 */
	public static int messageType(HttpServletRequest request, int rwCtrlType) {

		// ����û�������ֲ����������������,����ҵ������
		try {
			if (!BusinessContext.isNull()) {
				if (BusinessContext.getOperType() == OperType.NotAddForNotData) {
					return OperType.NotAddForNotData;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ����ҳ��
		if (request.getParameter("oid") == null
				|| request.getParameter("oid").toString().length() == 0) {
			return OperType.ADD;
		}
		String tag = WorkFlowFormContext.getTag();
		if ("1".equals(tag)) {
			return OperType.EDIT;
		} else {
			// �������ⲿ�����
			try {
				if (!BusinessContext.isNull()) {
					if (BusinessContext.getOperType() == OperType.EDIT) {
						return OperType.EDIT;
					} else if (BusinessContext.getOperType() == OperType.VIEW) {
						return OperType.VIEW;
					} else {
						throw new Exception("���������趨����!");
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
					throw new Exception("�޷��жϻ�������!");
				}
			} catch (Exception e) {
				e.printStackTrace();
				return OperType.EDIT;
			}
		}
	}
}
