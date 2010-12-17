package net.silencily.sailing.basic.wf.util;

import java.util.List;

import net.silencily.sailing.basic.wf.domain.TblWfOperationInfo;


/**
 * @author jianghl
 * Struts分页显示逻辑Bean
 */
public class PageBean {
	
	int currentPage=1; //当前页数
	public int totalPages=0; //总页数
    int pageRecorders=10; //每页显示的行数（这里即每页显示3条数据）
	int totalRows=0; //总行数
	int pageStartRow=0; //每页显示数据的起始行数
	int pageEndRow=0; //每页显示数据的终止行数
	boolean hasNextPage=false; //是否有下一页
	boolean hasPreviousPage=false; //是否有前一页
	List arrayList;
	//Iterator it;
	

	public PageBean(){}  //空的构造函数
	
	public PageBean(List list,int pns){ //构造函数
		
		this.arrayList=list; 
		totalRows=arrayList.size(); //获得总行数
	    //it=arrayList.iterator();  
	    hasPreviousPage=false; //是否有前一页
	    currentPage=1; //当前页数
	    if(pns!=0){
	    	pageRecorders = pns;
	    }
	    //获得总页数
	    if((totalRows%pageRecorders)==0){ 
	    	totalPages=totalRows/pageRecorders; 
	    }else{
	    	totalPages=totalRows/pageRecorders+1; 
	    } 
	    
	    //判断是否有下一页
	    if(currentPage>=totalPages){ 
	    	hasNextPage=false; 
	    }else{
	    	hasNextPage=true;
	    }

	    //判断每页所显示的数据
	    if(totalRows<pageRecorders){ 
	    	this.pageStartRow=0;           
	    	this.pageEndRow=totalRows;   
	    }else{
	    	this.pageStartRow=0;         
	    	this.pageEndRow=pageRecorders;   
	    }

	}
	
	public PageBean(int totalRows,int totalPages,int currentPage){ //构造函数
		this.totalRows = totalRows;
		this.totalPages = totalPages;
		this.currentPage = currentPage;
		hasPreviousPage = false;
		hasNextPage = false; 
		this.pageStartRow = 0;  
		this.pageEndRow = 0; 
	}

	public String getCurrentPage() { //get当前页数
		return this.toString(currentPage);
	}
	
	public void setCurrentPage(int currentPage) { //set当前页数
		this.currentPage = currentPage;
	}

	public int getPageRecorders() { //get页显示的行数
		return pageRecorders;
	}
	
	public void setPageRecorders(int pageRecorders) { //set每页显示的行数
		this.pageRecorders = pageRecorders;
	}

	public int getPageEndRow() { //get每页显示数据的终止行数
		return pageEndRow;
	}
	
	public int getPageStartRow() { //get每页显示数据的起始行数
		return pageStartRow;
	}

	public String getTotalPages() { //get总页数
		return this.toString(totalPages);
	}

	public String getTotalRows() { //get总行数
		return this.toString(totalRows);
	}

	public boolean getHasNextPage() { //get是否有下一页 
		return hasNextPage;
	}
	
	public void setHasNextPage(boolean hasNextPage) { //set是否有下一页
		this.hasNextPage = hasNextPage;
	}

	public boolean getHasPreviousPage() { //get是否有上一页
		return hasPreviousPage;
	}

	public void setHasPreviousPage(boolean hasPreviousPage) { //set是否有上一页
		this.hasPreviousPage = hasPreviousPage;
	}
	
	public List getArrayList() {
		return arrayList;
	}
	
	/**
	 * 获得首页要显示的数据
	 * @return Message数组类型 
	 */
	public Object[] getFirstPage(){
		
		currentPage = 1;
		hasPreviousPage=false; 
		hasNextPage=true;

		Object[] objects=getMessages(); //获得显示的数据行
	 
		return objects;
	}
	
	/**
	 * 获得下一页要显示的数据
	 * @return Message数组类型 
	 */
	public Object[] getNextPage(){
		
		if(currentPage<totalPages){
			currentPage=currentPage+1; //当前页加1
		}else{
			currentPage=totalPages;
		}
	 
		//判断是否有上一页
		if((currentPage-1)>0){ 
			hasPreviousPage=true; 
		}else{
			hasPreviousPage=false; 
		}
		
		//判断是否有下一页
		if(currentPage>=totalPages){ 
			hasNextPage=false; 
		}else{
			hasNextPage=true;
		}
		
		Object[] objects=getMessages(); //获得显示的数据行
	 
		return objects;
	}
	
	/**
	 * 获得上一页要显示的数据
	 * @return Message数组类型 
	 */
	public Object[] getPreviousPage(){
	 
		if(currentPage>=2){
			currentPage=currentPage-1; //当前页减1
		}else{
			currentPage=1;
		}
	   
	    //判断是否有下一页
	    if(currentPage>=totalPages){  
	    	hasNextPage=false; 
	    }else{
	    	hasNextPage=true;
	    }
	    
	    //判断是否有上一页
	    if((currentPage-1)>0){ 
	    	hasPreviousPage=true; 
	    }else{
	    	hasPreviousPage=false; 
	    }
	  
	    Object[] objects=getMessages(); //获得显示的数据行
	    
	    return objects;
	}
	
	/**
	 * 获得尾页要显示的数据
	 * @return Message数组类型 
	 */
	public Object[] getLastPage(){
		
		currentPage = totalPages;
		hasPreviousPage=true; 
		hasNextPage=false;

		Object[] objects=getMessages(); //获得显示的数据行
	 
		return objects;
	}
	
	public Object[] getTargetPage(int targetPage){ //获得目标页要显示的数据
		
		this.setCurrentPage(targetPage); //将目标页设置为当前页
		
		//判断是否有下一页
	    if(currentPage>=totalPages){  
	    	hasNextPage=false; 
	    }else{
	    	hasNextPage=true;
	    }
	    
	    //判断是否有上一页
	    if((currentPage-1)>0){ 
	    	hasPreviousPage=true; 
	    }else{
	    	hasPreviousPage=false; 
	    }
	    
	    Object[] objects = getMessages(); //获得显示的数据行
		
		return objects;
	}
	
	/**
	 * 获得当前要显示的数据行
	 * @return 返回一个要显示的数据数组
	 */
	public Object[] getMessages(){
	
		//设置当前页的起始行数和终止行数
		if(currentPage*pageRecorders<totalRows){ //当前页不是最后一页
			pageEndRow=currentPage*pageRecorders; //每页显示的终止行数=当前页数*每页显示的行数
			pageStartRow=pageEndRow-pageRecorders; //每页显示的起始行数=该页的终止行数-每页显示的行数
		}
		else{ //当前页是最后一页
			pageEndRow=totalRows; //每页显示的终止行数=总行数
			pageStartRow=pageRecorders*(totalPages-1); //每页显示的起始行数=每页显示的行数*（总页数-1）
		}
	 	
		Object[] objects=new TblWfOperationInfo[pageEndRow-pageStartRow]; //实例化一个TblWfOperationInfo类的对象数组，把要显示的数据放进去
		
		//把要显示的数据从集合中取出来放入messages数组里
		int j=0; 
		for(int i=pageStartRow;i<pageEndRow;i++)
		{
			objects[j++]=arrayList.get(i);
		}
		
		return objects;
	}
	
	/**
	 * 把int类型数据转换成字符串类型
	 */
	public String toString(int temp)
	{
		String str=Integer.toString(temp);
		return str;
	}
	
}
