<jsp:directive.include file="/decorators/default.jspf" />
<%@page import = "com.qware.security.SecurityContextInfo" %>
<%@page import = "org.hibernate.criterion.DetachedCriteria" %>
<%@page import = "org.hibernate.criterion.Restrictions" %>
<%@page import = "org.hibernate.criterion.MatchMode" %>
<%@page import = "com.qware.sm.domain.TblCmnPermission" %>
<%@page import = "com.coheg.persistent.hibernate3.EnhancedHibernateTemplate" %>
<%@page import = "com.coheg.container.ServiceProvider" %>
<%@page import = "java.util.List" %>
<div align="center" style="color: #FF0000; font-family: "Courier New", Courier, mono">
  <h3>对不起, 您无权限访问此资源</h3>
<%
	String urlName = "";
	try {
		EnhancedHibernateTemplate eht = (EnhancedHibernateTemplate)ServiceProvider.getService("system.hibernateTemplate");
		DetachedCriteria dc = DetachedCriteria.forClass(TblCmnPermission.class)
		.add(Restrictions.like("url", SecurityContextInfo.getCurrentPageUrl(), MatchMode.START));
		List list = eht.findByCriteria(dc);
		for (int i = 0; i < list.size(); i++) {
			String url = ((TblCmnPermission) list.get(i)).getUrl();
			if (url == null || "".equals(url)) {
				continue;
			}
	        String paras[] = url.split("&");
	        url = paras[0];
	        if (paras.length > 1 && paras[1].indexOf("stepType=") != -1) {
	            url = url + "&" + paras[1];
	        }
			if (SecurityContextInfo.getCurrentPageUrl().equals(url)) {
				urlName = ((TblCmnPermission) list.get(i)).getDisplayname();
				break;
			}
		}
	} catch (Exception e){
		e.printStackTrace();
	}
%>
  <h3>页面名称:<%=urlName%></h3>
  <h3>URL:<%=SecurityContextInfo.getCurrentPageUrl()%></h3>
</div>

