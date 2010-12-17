package net.silencily.sailing.basic.wf.util;

import java.util.List;

import net.silencily.sailing.basic.wf.domain.TblWfOperationInfo;


/**
 * @author jianghl
 * Struts��ҳ��ʾ�߼�Bean
 */
public class PageBean {
	
	int currentPage=1; //��ǰҳ��
	public int totalPages=0; //��ҳ��
    int pageRecorders=10; //ÿҳ��ʾ�����������Ｔÿҳ��ʾ3�����ݣ�
	int totalRows=0; //������
	int pageStartRow=0; //ÿҳ��ʾ���ݵ���ʼ����
	int pageEndRow=0; //ÿҳ��ʾ���ݵ���ֹ����
	boolean hasNextPage=false; //�Ƿ�����һҳ
	boolean hasPreviousPage=false; //�Ƿ���ǰһҳ
	List arrayList;
	//Iterator it;
	

	public PageBean(){}  //�յĹ��캯��
	
	public PageBean(List list,int pns){ //���캯��
		
		this.arrayList=list; 
		totalRows=arrayList.size(); //���������
	    //it=arrayList.iterator();  
	    hasPreviousPage=false; //�Ƿ���ǰһҳ
	    currentPage=1; //��ǰҳ��
	    if(pns!=0){
	    	pageRecorders = pns;
	    }
	    //�����ҳ��
	    if((totalRows%pageRecorders)==0){ 
	    	totalPages=totalRows/pageRecorders; 
	    }else{
	    	totalPages=totalRows/pageRecorders+1; 
	    } 
	    
	    //�ж��Ƿ�����һҳ
	    if(currentPage>=totalPages){ 
	    	hasNextPage=false; 
	    }else{
	    	hasNextPage=true;
	    }

	    //�ж�ÿҳ����ʾ������
	    if(totalRows<pageRecorders){ 
	    	this.pageStartRow=0;           
	    	this.pageEndRow=totalRows;   
	    }else{
	    	this.pageStartRow=0;         
	    	this.pageEndRow=pageRecorders;   
	    }

	}
	
	public PageBean(int totalRows,int totalPages,int currentPage){ //���캯��
		this.totalRows = totalRows;
		this.totalPages = totalPages;
		this.currentPage = currentPage;
		hasPreviousPage = false;
		hasNextPage = false; 
		this.pageStartRow = 0;  
		this.pageEndRow = 0; 
	}

	public String getCurrentPage() { //get��ǰҳ��
		return this.toString(currentPage);
	}
	
	public void setCurrentPage(int currentPage) { //set��ǰҳ��
		this.currentPage = currentPage;
	}

	public int getPageRecorders() { //getҳ��ʾ������
		return pageRecorders;
	}
	
	public void setPageRecorders(int pageRecorders) { //setÿҳ��ʾ������
		this.pageRecorders = pageRecorders;
	}

	public int getPageEndRow() { //getÿҳ��ʾ���ݵ���ֹ����
		return pageEndRow;
	}
	
	public int getPageStartRow() { //getÿҳ��ʾ���ݵ���ʼ����
		return pageStartRow;
	}

	public String getTotalPages() { //get��ҳ��
		return this.toString(totalPages);
	}

	public String getTotalRows() { //get������
		return this.toString(totalRows);
	}

	public boolean getHasNextPage() { //get�Ƿ�����һҳ 
		return hasNextPage;
	}
	
	public void setHasNextPage(boolean hasNextPage) { //set�Ƿ�����һҳ
		this.hasNextPage = hasNextPage;
	}

	public boolean getHasPreviousPage() { //get�Ƿ�����һҳ
		return hasPreviousPage;
	}

	public void setHasPreviousPage(boolean hasPreviousPage) { //set�Ƿ�����һҳ
		this.hasPreviousPage = hasPreviousPage;
	}
	
	public List getArrayList() {
		return arrayList;
	}
	
	/**
	 * �����ҳҪ��ʾ������
	 * @return Message�������� 
	 */
	public Object[] getFirstPage(){
		
		currentPage = 1;
		hasPreviousPage=false; 
		hasNextPage=true;

		Object[] objects=getMessages(); //�����ʾ��������
	 
		return objects;
	}
	
	/**
	 * �����һҳҪ��ʾ������
	 * @return Message�������� 
	 */
	public Object[] getNextPage(){
		
		if(currentPage<totalPages){
			currentPage=currentPage+1; //��ǰҳ��1
		}else{
			currentPage=totalPages;
		}
	 
		//�ж��Ƿ�����һҳ
		if((currentPage-1)>0){ 
			hasPreviousPage=true; 
		}else{
			hasPreviousPage=false; 
		}
		
		//�ж��Ƿ�����һҳ
		if(currentPage>=totalPages){ 
			hasNextPage=false; 
		}else{
			hasNextPage=true;
		}
		
		Object[] objects=getMessages(); //�����ʾ��������
	 
		return objects;
	}
	
	/**
	 * �����һҳҪ��ʾ������
	 * @return Message�������� 
	 */
	public Object[] getPreviousPage(){
	 
		if(currentPage>=2){
			currentPage=currentPage-1; //��ǰҳ��1
		}else{
			currentPage=1;
		}
	   
	    //�ж��Ƿ�����һҳ
	    if(currentPage>=totalPages){  
	    	hasNextPage=false; 
	    }else{
	    	hasNextPage=true;
	    }
	    
	    //�ж��Ƿ�����һҳ
	    if((currentPage-1)>0){ 
	    	hasPreviousPage=true; 
	    }else{
	    	hasPreviousPage=false; 
	    }
	  
	    Object[] objects=getMessages(); //�����ʾ��������
	    
	    return objects;
	}
	
	/**
	 * ���βҳҪ��ʾ������
	 * @return Message�������� 
	 */
	public Object[] getLastPage(){
		
		currentPage = totalPages;
		hasPreviousPage=true; 
		hasNextPage=false;

		Object[] objects=getMessages(); //�����ʾ��������
	 
		return objects;
	}
	
	public Object[] getTargetPage(int targetPage){ //���Ŀ��ҳҪ��ʾ������
		
		this.setCurrentPage(targetPage); //��Ŀ��ҳ����Ϊ��ǰҳ
		
		//�ж��Ƿ�����һҳ
	    if(currentPage>=totalPages){  
	    	hasNextPage=false; 
	    }else{
	    	hasNextPage=true;
	    }
	    
	    //�ж��Ƿ�����һҳ
	    if((currentPage-1)>0){ 
	    	hasPreviousPage=true; 
	    }else{
	    	hasPreviousPage=false; 
	    }
	    
	    Object[] objects = getMessages(); //�����ʾ��������
		
		return objects;
	}
	
	/**
	 * ��õ�ǰҪ��ʾ��������
	 * @return ����һ��Ҫ��ʾ����������
	 */
	public Object[] getMessages(){
	
		//���õ�ǰҳ����ʼ��������ֹ����
		if(currentPage*pageRecorders<totalRows){ //��ǰҳ�������һҳ
			pageEndRow=currentPage*pageRecorders; //ÿҳ��ʾ����ֹ����=��ǰҳ��*ÿҳ��ʾ������
			pageStartRow=pageEndRow-pageRecorders; //ÿҳ��ʾ����ʼ����=��ҳ����ֹ����-ÿҳ��ʾ������
		}
		else{ //��ǰҳ�����һҳ
			pageEndRow=totalRows; //ÿҳ��ʾ����ֹ����=������
			pageStartRow=pageRecorders*(totalPages-1); //ÿҳ��ʾ����ʼ����=ÿҳ��ʾ������*����ҳ��-1��
		}
	 	
		Object[] objects=new TblWfOperationInfo[pageEndRow-pageStartRow]; //ʵ����һ��TblWfOperationInfo��Ķ������飬��Ҫ��ʾ�����ݷŽ�ȥ
		
		//��Ҫ��ʾ�����ݴӼ�����ȡ��������messages������
		int j=0; 
		for(int i=pageStartRow;i<pageEndRow;i++)
		{
			objects[j++]=arrayList.get(i);
		}
		
		return objects;
	}
	
	/**
	 * ��int��������ת�����ַ�������
	 */
	public String toString(int temp)
	{
		String str=Integer.toString(temp);
		return str;
	}
	
}
