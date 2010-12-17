package net.silencily.sailing.basic.uf.sso.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SingleSignOnServlet extends HttpServlet {

	//** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = 6345844655530595349L;
	
	/**
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    doPost(req, resp);
	}

	/**
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SingleSignOnForm theForm = (SingleSignOnForm)req.getAttribute("theForm");
        String url = req.getContextPath() + "/j_security_check.auth?j_username=";
        url = url + theForm.getUsername();
        resp.setHeader("Pragma", "No-cache");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setDateHeader("Expires", 0);
        resp.sendRedirect(url);
    }

}