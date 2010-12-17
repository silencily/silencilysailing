package net.silencily.sailing.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.common.dbtime.DBTime;



/**
 * 
 * @author wenjb
 * @version 1.0
 */
public class GetDBTimeServlet extends HttpServlet {

	/**
	 * 描述：
	 * 属性名：serialVersionUID
	 * 属性类型：long
	 */
	private static final long serialVersionUID = 3846559974738749586L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Date date = DBTime.getDBTime();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy|MM|dd|HH|mm|ss");
		String dateStr = simpleDateFormat.format(date);		
		resp.setContentType("text/plain; charset=GBK");
		resp.getWriter().write(dateStr);
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

}
