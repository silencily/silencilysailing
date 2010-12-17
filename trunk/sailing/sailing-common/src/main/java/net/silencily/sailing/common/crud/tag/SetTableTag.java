package net.silencily.sailing.common.crud.tag;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import net.silencily.sailing.common.crud.domain.CommonTableView;
import net.silencily.sailing.common.crud.domain.CommonTableViewSet;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.ExpressionEvaluationUtils;

/**
 * @author zhaoyf
 *
 */
public class SetTableTag extends TagSupport {

	private String all;
	private String select;
	private String isSel;
	public String getIsSel() {
		return isSel;
	}
	public void setIsSel(String isSel) {
		this.isSel = isSel;
	}
	
	
	public String getAll() {
		return all;
	}
	public void setAll(String all) {
		this.all = all;
	}
	public String getSelect() {
		return select;
	}
	public void setSelect(String select) {
		this.select = select;
	}
	public int doEndTag() throws JspException {
		// TODO Auto-generated method stub
		JspWriter out = pageContext.getOut();
		List l=this.getAllList();
		List l1=this.getSelectList();
		for(int i=0,size=l1.size();i<size;i++)
		{
			CommonTableViewSet ctv=(CommonTableViewSet)l1.get(i);
			if(l.contains(ctv.getCommonTableView()))
				l.remove(ctv.getCommonTableView());
		}
		try {
			if("select".equals(isSel))
			{
				
				for(int i=0,size=l1.size();i<size;i++)
				{
					CommonTableViewSet ctv=(CommonTableViewSet)l1.get(i);
					if(!StringUtils.isBlank(ctv.getOrderAsc()))
					out.println("<option value='"+ctv.getCommonTableView().getId()+"-"+ctv.getOrderAsc()+"'>"+ctv.getCommonTableView().getRowDisplayname()+"-"+change(ctv.getOrderAsc())+"</option>");
					else
						out.println("<option value='"+ctv.getCommonTableView().getId()+"'>"+ctv.getCommonTableView().getRowDisplayname()+"</option>");
				}
			}
			if("all".equals(isSel))
			{
				
				for(int i=0,size=l.size();i<size;i++)
				{
					CommonTableView ctv=(CommonTableView)l.get(i);
					out.println("<option value='"+ctv.getId()+"'>"+ctv.getRowDisplayname()+"</option>");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.doEndTag();
	}
	public List getAllList() throws JspException
	{
		Object allList = ExpressionEvaluationUtils.evaluate("all",all, pageContext);
		if(List.class.isInstance( allList))
			return (List)allList;
		else
			return null;
	}
	
	public List getSelectList() throws JspException
	{
		Object selectList = ExpressionEvaluationUtils.evaluate("select",select, pageContext);
		if(List.class.isInstance( selectList))
			return (List)selectList;
		else
			return null;
	}
	private String change(String code)
	{
		if("asc".equals(code))
			return "ÉýÐò";
		else
			return "½µÐò";
	}
}
