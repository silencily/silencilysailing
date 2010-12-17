package net.silencily.sailing.context;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpSession;

import net.silencily.sailing.security.SecurityContextInfo;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;


public class BusinessContext {

	/*
	 * ��ȡ����ֵʱ�򣬷���-1
	 */

	public static int getOperType() {
		HttpSession session = getHttpSession();
		if (null == session || null == session.getAttribute("operType")) {
			return -1;
		} else {
			return ((Integer) session.getAttribute("operType")).intValue();
		}
	}

	public static void setOperType(int operType) {
		HttpSession session = getHttpSession();
		Integer operTypeInteger = new Integer(operType);
		if (null == session.getAttribute("operType")) {
			session.setAttribute("operType", operTypeInteger);
		} else {
			session.removeAttribute("operType");
			session.setAttribute("operType", operTypeInteger);
		}
	}

	/*
	 * ��ȡ��ǰ��SESSION
	 */
	private static HttpSession getHttpSession() {
		return SecurityContextInfo.getSession();
	}

	public static boolean isNull() {
		HttpSession session = getHttpSession();
		if (session.getAttribute("operType") == null) {
			return true;
		}
		return false;
	}

	/**
	 * �õ�ǰһ��ҳ���oid
	 * 
	 * @return String
	 */
	public static String getUserSetedOid() {
		HttpSession session = getHttpSession();
		return (session.getAttribute("userSetedOid") == null || ""
				.equals(session.getAttribute("userSetedOid"))) ? null
				: (String) session.getAttribute("userSetedOid");
	}

	/**
	 * ����ǰһ��ҳ���oid
	 * 
	 * @param userSetedOid
	 */
	public static void setUserSetedOid(String userSetedOid) {
		HttpSession session = getHttpSession();
		session.setAttribute("userSetedOid", userSetedOid);
	}

	/**
	 * �������� ��ŵ�ǰ��URL��ȡȨ�޵ķ�ʽ
	 * 
	 * @return 2008-1-3 ����04:55:32
	 * @version 1.0
	 * @author wenjb
	 */
	public static String getCalculateRwCtrlTypeByID() {
		HttpSession session = getHttpSession();
		String temp = null;
		try {
			temp = session.getAttribute("calculateRwCtrlTypeByID") == null ? null
					: (String) session.getAttribute("calculateRwCtrlTypeByID");
		} catch (Exception e) {
			temp = null;
		}

		return temp;
	}

	public static void setCalculateRwCtrlTypeByID(String calculateRwCtrlTypeByID) {
		HttpSession session = getHttpSession();
		session
				.setAttribute("calculateRwCtrlTypeByID",
						calculateRwCtrlTypeByID);
	}

	/**
	 * �������� ��ŷ������ϴ���ļ���·��
	 * 
	 * @return 2008-1-3 ����04:55:32
	 * @version 1.0
	 * @author wenjb
	 */
	public static String getUploadFilesPath() {
		try {
			HttpSession session = getHttpSession();
			String realPath = session.getServletContext().getRealPath("/")
					+ "/WEB-INF/classes/conf/vfs/vfs-configuration.xml";
			File f = new File(realPath);
			SAXBuilder builder = new SAXBuilder(false);// ע�������
			Document doc = builder.build(f);// �����ĵ�������XML�ļ�
			Element rootElement = doc.getRootElement(); // ���ڵ�
			List childlist = rootElement.getChildren(); // �ֽڵ�
			for (int i = 0; i < childlist.size(); i++) {
				// �ҵ�<bean id="FileObjectManager"
				Element temp = (Element) (childlist.get(i));
				if (null != (temp.getAttribute("id"))
						&& "FileObjectManager".equals(temp
								.getAttributeValue("id"))) {
					// �ҵ� <property name="rootPaths">
					List secondChildlist = temp.getChildren();
					for (int j = 0; j < secondChildlist.size(); j++) {
						Element secondTemp = (Element) (secondChildlist.get(j));
						if (null != (secondTemp.getAttribute("name"))
								&& "rootPaths".equals(secondTemp
										.getAttributeValue("name"))) {
							// �ҵ� <map>�ڵ�
							Element mapElement = ( (Element) secondTemp
									.getChildren().get(0) );
							// ȡ����һ���ӽڵ��ֵ
							String path = ((Element) mapElement.getChildren()
									.get(0)).getAttributeValue("value");
							return path;
						}
					}
				}
			}
			throw new RuntimeException("�ϴ��ļ��洢�ĵ�ַ���ִ�������ϵͳ����Ա��ϵ");
		} catch (Exception e) {
			throw new RuntimeException("�ϴ��ļ��洢�ĵ�ַ���ִ�������ϵͳ����Ա��ϵ");
		}
	}

	
	/**
	 * �������� �ʲ�����ר�á�
	 * 
	 * @return 2008-1-3 ����04:55:32
	 * @version 1.0
	 * @author wenjb
	 */
	public static String getRptPath() {	
		return ConstCommon.RPTPATH;
	}
	
}

