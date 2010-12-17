package net.silencily.sailing.common.crud.tag;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import net.silencily.sailing.common.crud.service.ViewBean;
import net.silencily.sailing.common.crud.service.ViewBean.ViewAssi;
import net.silencily.sailing.framework.persistent.hibernate3.entity.CodeWrapper;
import net.silencily.sailing.hibernate3.EntityPlus;
import net.silencily.sailing.struts.BaseFormPlus;
import net.silencily.sailing.utils.Tools;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.web.util.ExpressionEvaluationUtils;


/**
 * @author zhaoyifei
 *
 */
public class TableTag extends TagSupport {
	
	/**
	 * 自动回应弹出选择请求
	 */
	
	boolean isSelect=false;
	private static String script1="<script language=\"javascript\">\r\n"+
	"var listObject = new Object();\r\n"+
	"CurrentPage.onLoadSelect = function(){\r\n"+
	"listObject = new ListUtil.Listing('listObject', 'listtable');\r\n"+
	"listObject.init();\r\n"+
	"top.definedWin.selectListing = function(inum) {\r\n";
	
		//<c:if test="${theForm.popSelectType == 'radio'}">listObject.selectWindow(1);</c:if>
		//<c:if test="${theForm.popSelectType == 'checkbox'}">listObject.selectWindow(2);</c:if>
	private static String script2="}\r\n"+
	"top.definedWin.closeListing = function(inum) {\r\n"+
	"listObject.selectWindow();\r\n"+
	"}\r\n"+
"}\r\n"+
"CurrentPage.onLoadSelect();\r\n"+
	"</script>"; 
	private static String selectType1="<input type='hidden' name='popSelectType' value='";
	private static String selectType2="'/>"; 
	/**************************************/
	
	
	private String script;
	private String linkScript="<div onclick=\"javascript:definedWin.openUrl(''{0}'', ''{1}'')\" class=\"font_link\">{2}</div>";
    private String downloadScript="<a href=\"{0}\" class=\"font_link\">{1}</a>";
    private String hiddenScript="<input type=\"hidden\" value=\"{0}\" />";
    private String name=null;
	private String del=null;
	private String box=null;
	private String publicResourceServer=null;
	private String formName;
	
	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}
	//private String qwareServer=null;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	public int doEndTag() throws JspException {
		// TODO Auto-generated method stub
		publicResourceServer=this.pageContext.getServletContext().getInitParameter("publicResourceServer");
		script="<script type=\"text/javascript\">"+
				" if (CurrentPage == null) { var CurrentPage = {}; } "+
				"CurrentPage.settable =function(pageid,asc){"+
				"var url = ContextInfo.fetchServerAddr()+'"+publicResourceServer+"/curd/curdAction.do?step=setTable&pageId=';"+
				"url+=pageid;"+
				"if(asc!=null)"+
				"url+='&asc=asc';"+
				"definedWin.openListingUrl(\"setTable\",url);"+
				"}"+
				"</script>";
		JspWriter out = pageContext.getOut();
		String selcetFuction=null;
		String popSelectType=null;
		if(StringUtils.isNotBlank(formName))
		{
			BaseFormPlus form = (BaseFormPlus)ExpressionEvaluationUtils.evaluate("formName",formName, pageContext);
			popSelectType=form.getPopSelectType();
			if("only".equals(form.getPopSelectType()))
				this.box="radio";
			if("multi".equals(form.getPopSelectType()))
				this.box="check";
			if("only".equals(form.getPopSelectType()))
				selcetFuction="listObject.selectWindow(1);";
			if("multi".equals(form.getPopSelectType()))
				selcetFuction="listObject.selectWindow(2);";
			if(!StringUtils.isBlank(form.getPopSelectType()))
			{
			isSelect=true;
			this.del="false";
			}
		}
		ViewBean vb=this.getViewBean();
		StringBuffer idgetMethod=new StringBuffer();
		idgetMethod.append("get");
		String idName=vb.getIdName();
		String idfirst=idName.substring(0,1);
		idgetMethod.append(idfirst.toUpperCase());
		idgetMethod.append(idName.substring(1));
		//-----Start修改于2008.01.10 by het--------//
		//publicResourceServer=getPublicResourceServer();
		//-----End----------------------------//
		try {
				List rl=vb.getViewRow();
				List resultl=vb.getViewList();
				Iterator rli=rl.iterator();
				out.println("<thead name=\"tabtitle\">");
				out.println("<tr>");
				out.print("<td" + ("nobox".equals(this.box)?" style='display:none'":"") + " width=\"25px\" field=\"id\" nowrap=\"nowrap\">");
				if(!"radio".equals(this.box))
				out.print("<input  width='15px' id='detailIdsForPrintAll' type='checkbox' onclick=\"FormUtils.checkAll(this,document.getElementsByName('oid'))\" title=\"是否全选\"/>");
				else
					out.print("&nbsp;");
				out.print("</td>");
				while(rli.hasNext())
				{
					ViewAssi va=(ViewAssi)rli.next();
					Class cp=null;
					for(int i=0;i<vb.getViewList().size();i++)
					{
						try {
							cp=PropertyUtils.getPropertyType(vb.getViewList().get(0), va.getRow());
							break;
						} catch (RuntimeException e) {
							// TODO Auto-generated catch block
							continue;
						}
					}
					
					if((null!=cp)&&isSelect&&(vb.getViewList().size()!=0)&&(CodeWrapper.class.isAssignableFrom(cp)))
					{
						out.print("<td field=\"" + va.getRow() +
						"Code\"  style='display:none'/>");
					}
					String dataType="";
					if((null!=cp)&&(Number.class.isAssignableFrom(cp)))
					{
						dataType=" type=\"Number\" ";
					}
					out.print("<td field=\"" + StringUtils.replaceChars(va.getRow(), '.','_') + "\" " +dataType+
						" nowrap=\"nowrap\" >");
					out.print(va.getRowName());
					out.print("</td>");
				
				}
				if(!"false".equals(this.del))
				out.println("<td  width=\"40px\" nowrap=\"nowrap\" type=\"operate\">操作</td>");
				out.println("</tr>");
				out.println("</thead>");
				
			
		
		Iterator resultli=resultl.iterator();
		
		out.println("<tbody id='tablist'>");
		
		while(resultli.hasNext())
		{
			Object object=resultli.next();
			String id=(String)MethodUtils.invokeExactMethod(object,idgetMethod.toString(),null);
			if (object != null 
					&& object.getClass().toString().indexOf("com.qware.am.domain.TblAmWoDefectclear") != -1){
				Object tempValue = PropertyUtils.getNestedProperty(object, "colourContl");
				out.print("<tr class=\"\" STYLE=\"" );
				out.print(tempValue );
				out.print("\" />" );
			}else{
				out.print("<tr class=\"\" />" );
			}
			out.print("<td" + ("nobox".equals(this.box)?" style='display:none'":"") + " class=\"list_first\" nowrap=\"nowrap\" align='center'>" );
			if(!"radio".equals(this.box))
			{
			out.print("<input type=\"checkbox\" name=\"oid\" onclick=\"FormUtils.check($('detailIdsForPrintAll'),document.getElementsByName('oid'))\" value=\""+id+"\"");
			//out.print(id);
			out.print("/>" );
			}
			else
			{
				out.print("<input type=\"radio\" name=\"oid\"  value=\""+id+"\"/>");
			}
//			out.print("<input type=\"hidden\" name=\"oid\" value=\"");
//			out.print(id);
//			out.print("\" />" );
			out.print("</td>");
			Iterator i=rl.iterator();
			while(i.hasNext())
			{
				ViewAssi va=(ViewAssi)i.next();
				String r=va.getRow();
				String link=va.getLink();
				
				Object value;
				try {
					value = PropertyUtils.getNestedProperty(object, r);
                    if(value==null) {
                        //当value为null时需要判断该项目是否是CodeWrapper，如果是则需要生成code列
                        Class cp=PropertyUtils.getPropertyType(vb.getViewList().get(0), va.getRow());
                        if((null!=cp)&&isSelect&&(vb.getViewList().size()!=0)&&(CodeWrapper.class.isAssignableFrom(cp)))
                        {
                            out.print("<td style='display:none'></td>");
                        }
                    }
				} catch (RuntimeException e) {
					// TODO Auto-generated catch block
					out.print("<td nowrap=\"nowrap\" align='left'> &nbsp;" );
					out.print("</td>");
					continue;
				}
				if(value==null)
				{
					out.print("<td nowrap=\"nowrap\" align='left'> &nbsp;" );
					out.print("</td>");
					continue;
				}
				if(Date.class.isAssignableFrom(value.getClass()))
				{
					out.print("<td nowrap=\"nowrap\" align='center'>" );
					String style="yyyy-MM-dd";
					if(StringUtils.isNotBlank(va.getStyle()))
						style=va.getStyle();
					try {
						out.print(this.changeScript(va.getRowName(), link,Tools.getTheTime((Date)value,style) ,object,va.getStyle()));
					} catch (RuntimeException e) {
						// TODO Auto-generated catch block
						out.print("&nbsp;");
					}
				}
				if(Number.class.isAssignableFrom(value.getClass()))
				{
					out.print("<td nowrap=\"nowrap\" align='right'>" );
					String style="#.##";
					if(StringUtils.isNotBlank(va.getStyle()))
						style=va.getStyle();
					try{
					if(isSelect) {
                        //if (StringUtils.isBlank(link)) {
                        //    out.print(this.makeHidden(value.toString()));
                        //}
						out.print(this.changeScript(va.getRowName(), link,new DecimalFormat(style).format(value),object,va.getStyle()));
                    } else {
						out.print(this.changeScript(va.getRowName(), link,new DecimalFormat(style).format(value),object,va.getStyle()));
                    }
				} catch (RuntimeException e) {
					// TODO Auto-generated catch block
					out.print("&nbsp;");
				}
				}
				if(value.getClass().equals(String.class))
				{
					out.print("<td nowrap=\"nowrap\" align='left'>" );
					String style="14";
					if(StringUtils.isNotBlank(va.getStyle()))
						style=va.getStyle();
					try{
					    if ("download".equals(style)) {
                            out.print(this.changeScript(va.getRowName(), link,Tools.changeHtml(value.toString()),object,va.getStyle()));
                        } else {
                            if (isSelect && StringUtils.isBlank(link) && StringUtils.isNotBlank(value.toString())) {
                                out.print(this.makeHidden(Tools.changeHtml(value.toString())));
                            }
                            out.print(this.changeScript(va.getRowName(), link,Tools.changeHtml(Tools.abbreviate(value.toString(),Integer.parseInt(style))),object,va.getStyle()));
                        }
    				} catch (RuntimeException e) {
    					// TODO Auto-generated catch block
    					out.print("&nbsp;");
    				}
				}
				if(CodeWrapper.class.isAssignableFrom(value.getClass()))
				{
					if(isSelect)
					{
					out.print("<td style='display:none'>" );
					out.print(((CodeWrapper)value).getCode());
					out.print("</td>");
					}
					out.print("<td nowrap=\"nowrap\" align='left'>" );
					out.print(this.changeScript(va.getRowName(), link,Tools.changeHtml(((CodeWrapper)value).getName()),object,va.getStyle()));
				}
				
//				}
//				else
//				{
//					out.print("<td nowrap=\"nowrap\" align='left'>" );
//					out.print(dict.get(r));
//				}
				//
				if(link != null && value.getClass().equals(String.class) && !StringUtils.isBlank(value.toString())){
					out.print("</td>");
				}
				else{
					out.print("&nbsp;</td>");
				}
			
			}
			if(!"false".equals(this.del))
			{
				out.print("<td nowrap='nowrap' align=\"center\">");
				//这段程序加入权限，不好使的话可能与权限有关
				try{
					//需求变更，只读权限就不能删除
//					String url = SecurityContextInfo.getCurrentPageUrl();
//					CurrentUser currentUser = SecurityContextInfo.getCurrentUser();
//					int pageRWCtrlType = currentUser.getPageDefaultRWCtrlType(url);
//					
//					if(1 == pageRWCtrlType){
					if(!((EntityPlus)object).getDataAccessLevelD()){
						out.print("&nbsp;");
					}else{
						//Integer integer=(Integer)(SecurityContextInfo.getRwCtrlTypeMap().get(id));
						//if(integer.intValue()!=1)
							out.print("<input type=\"button\" class=\"list_delete\" onclick=\"CurrentPage.remove('"+id+"')\" title=\"点击删除\"/>");
						//else
						//	out.print("&nbsp;");
					}
				}catch(Exception e){
					//当获取不到权限的时候，因为目前系统中存在不配置权限过滤的可能行，采取放开政策
					out.print("<input type=\"button\" class=\"list_delete\" onclick=\"CurrentPage.remove('"+id+"')\" title=\"点击删除\"/>");
				}
				out.print("</td>");
			}
			out.println("</tr>");
		}
		out.print("</tbody>");	
			out.print(script);
			if(isSelect)
			{
				out.print(script1);
				out.print(selcetFuction);
				out.print(script2);
				out.print(selectType1);
				out.print(popSelectType);
				out.print(selectType2);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new JspException();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.doEndTag();
	}
	private ViewBean getViewBean()
	{
		ViewBean vb=(ViewBean)pageContext.getAttribute(name,
                PageContext.PAGE_SCOPE);
		if(vb==null)
		{
			vb=(ViewBean)pageContext.getAttribute(name,
	                PageContext.REQUEST_SCOPE);
		}
		return vb;
	}
	private String getPublicResourceServer()
	{
		String publicResourceServer=(String) pageContext.getAttribute("initParam['publicResourceServer']",PageContext.PAGE_SCOPE);
		if(publicResourceServer==null||publicResourceServer.equals(""))
			publicResourceServer=(String) pageContext.getAttribute("initParam['publicResourceServer']",PageContext.REQUEST_SCOPE);
		if(publicResourceServer==null||publicResourceServer.equals(""))
			publicResourceServer=(String) pageContext.getAttribute("initParam['publicResourceServer']",PageContext.SESSION_SCOPE);
		
		return publicResourceServer;
	}

	public String getDel() {
		return del;
	}

	public void setDel(String del) {
		this.del = del;
	}

	public String getBox() {
		return box;
	}

	public void setBox(String box) {
		this.box = box;
	}
	private String changeScript(String popName,String link,String view,Object bean,String style)
	{
        if(StringUtils.isBlank(view))
            return "&nbsp;";
//		if(isSelect)
//			return view;
		if(StringUtils.isBlank(link))
			return view;
        if("download".equals(style))
            return MessageFormat.format(downloadScript,new Object[]{changeLink(link,bean),view});
		return MessageFormat.format(linkScript,new Object[]{popName,changeLink(link,bean),view});
	}
	private String changeLink(String link,Object bean)
	{
		try {
			Velocity.init();
			VelocityContext context=new VelocityContext();
			context.put("bean",bean);
			StringWriter out=new StringWriter();
			boolean a=Velocity.evaluate(context, out, "",publicResourceServer+link);
			String s=out.toString();
			return s;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    private String makeHidden(String val) {
        return MessageFormat.format(hiddenScript,new Object[]{val});
    }
}
