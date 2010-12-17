package net.silencily.sailing.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author wenjb
 * @version 1.0
 */
public class ClearWorkFlowPermissionServlet extends HttpServlet {

	/**
	 * 描述： 属性名：serialVersionUID 属性类型：long
	 */
	private static final long serialVersionUID = -4014169861891359280L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//清空SESSION的内容
		HttpSession session = req.getSession();
		if (null != session.getAttribute("tag")){
			session.removeAttribute("tag");
		}
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}
}
