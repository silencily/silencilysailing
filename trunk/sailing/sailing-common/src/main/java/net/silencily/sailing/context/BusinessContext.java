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
	 * 当取不到值时候，返回-1
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
	 * 获取当前的SESSION
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
	 * 得到前一个页面的oid
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
	 * 设置前一个页面的oid
	 * 
	 * @param userSetedOid
	 */
	public static void setUserSetedOid(String userSetedOid) {
		HttpSession session = getHttpSession();
		session.setAttribute("userSetedOid", userSetedOid);
	}

	/**
	 * 功能描述 存放当前的URL读取权限的方式
	 * 
	 * @return 2008-1-3 下午04:55:32
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
	 * 功能描述 存放服务器上存放文件的路径
	 * 
	 * @return 2008-1-3 下午04:55:32
	 * @version 1.0
	 * @author wenjb
	 */
	public static String getUploadFilesPath() {
		try {
			HttpSession session = getHttpSession();
			String realPath = session.getServletContext().getRealPath("/")
					+ "/WEB-INF/classes/conf/vfs/vfs-configuration.xml";
			File f = new File(realPath);
			SAXBuilder builder = new SAXBuilder(false);// 注册解析器
			Document doc = builder.build(f);// 创建文档，读入XML文件
			Element rootElement = doc.getRootElement(); // 根节点
			List childlist = rootElement.getChildren(); // 字节点
			for (int i = 0; i < childlist.size(); i++) {
				// 找到<bean id="FileObjectManager"
				Element temp = (Element) (childlist.get(i));
				if (null != (temp.getAttribute("id"))
						&& "FileObjectManager".equals(temp
								.getAttributeValue("id"))) {
					// 找到 <property name="rootPaths">
					List secondChildlist = temp.getChildren();
					for (int j = 0; j < secondChildlist.size(); j++) {
						Element secondTemp = (Element) (secondChildlist.get(j));
						if (null != (secondTemp.getAttribute("name"))
								&& "rootPaths".equals(secondTemp
										.getAttributeValue("name"))) {
							// 找到 <map>节点
							Element mapElement = ( (Element) secondTemp
									.getChildren().get(0) );
							// 取出第一个子节点的值
							String path = ((Element) mapElement.getChildren()
									.get(0)).getAttributeValue("value");
							return path;
						}
					}
				}
			}
			throw new RuntimeException("上传文件存储的地址出现错误，请与系统管理员联系");
		} catch (Exception e) {
			throw new RuntimeException("上传文件存储的地址出现错误，请与系统管理员联系");
		}
	}

	
	/**
	 * 功能描述 资产报表专用。
	 * 
	 * @return 2008-1-3 下午04:55:32
	 * @version 1.0
	 * @author wenjb
	 */
	public static String getRptPath() {	
		return ConstCommon.RPTPATH;
	}
	
}

