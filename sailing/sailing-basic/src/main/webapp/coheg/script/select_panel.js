/*
 * 选项卡控件对象
 * 2006-04-06
 * 
 * @name     控件名字，用于应用出现多组选项卡情况。
 * @arr      所有选项的数据二维数组 [显示名称,点击事件]，
             例如：arr[[name1,function1],[name2,function2],[...]];
 * @selectId 默认选中的项的序号，int，（从0开始）
 */
function panelObject(name,arr,selectId){
	this.name = name;	
	this.imagePath = getContextPath() +"/image/main/";
	this.bgCss  = "panel_bg";//背景css
	this.tabCss = "panel_tab";//选择卡背景css
	//this.focusCssv=  "";       //聚焦css
	//this.blurCss  =  "";       //普通css
	this.dataArr   = arr;
	this.selectId  = selectId ? selectId : 0;
	//this.address   = address;//显示位置

	/** 为防止加载图片时候导致异常，加入缓存*/
    this.oneImage1 = new Image();
	this.oneImage1.src = this.imagePath+"/panel_one_1.gif";
	this.oneImage2 = new Image();
	this.oneImage2.src = this.imagePath+"/panel_one_2.gif";
	this.oneImage3 = new Image();
	this.oneImage3.src = this.imagePath+"/panel_one_3.gif";
	this.twoImage1 = new Image();
	this.twoImage1.src = this.imagePath+'/panel_two_1.gif';
	this.twoImage2 = new Image();
	this.twoImage2.src = this.imagePath+'/panel_two_2.gif';
	this.twoImage3 = new Image();
	this.twoImage3.src = this.imagePath+'/panel_two_3.gif';
	this.twoImage31 = new Image();
	this.twoImage31.src = this.imagePath+'/panel_two_3_1.gif';

	//function
	this.display = panelDisplay;
	this.click   = panelClick;
	this.show    = panelShow;
}

function panelDisplay(){
	var arr = this.dataArr;
	var selid = this.selectId;
	var str = "<div class='"+this.bgCss+"'>";
	for(var i=0;i<arr.length;i++){
		str += "<div onclick='"+arr[i][1]+"' class='"+this.tabCss+"'>";
		if(i == selid)
			str += this.show(i, arr[i][0]);
		else
			str += this.show(i, arr[i][0]);
		str += "</div>";
	}
	//str += "<div background='"+golobal_image_path+"/panel_end.jpg' class='panel_end' id='tdId_panel_end'></div>";
	//str += "<div class='panel_line'>&nbsp;</div>";
	str += "</div>";
	return str;
}

function panelShow(index, str, inum) {
	var tab = "";
	inum = this.dataArr.length
	if(index == this.selectId) {
		tab += "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">";
		tab += "<tr id=\""+this.name+".tabTrId"+index+"\" style=\"cursor:hand\" onclick=\""+this.name+".click("+index+","+inum+")\">";
		tab += "<td background = '"+this.oneImage1.src+"' height=\"30\" width=\"5\">&nbsp;</td>";//class=\"tab_selected_panel1\"
		tab += "<td background = '"+this.oneImage2.src+"' class=''> &nbsp; "+str+" &nbsp; </td>";
		tab += "<td background = '"+this.oneImage3.src+"' width=\"5\">&nbsp;</td>";
		tab += "</tr>";
		tab += "</table>";
	}
	else {
		tab += "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">";
		tab += "<tr id=\""+this.name+".tabTrId"+index+"\" style=\"cursor:hand\" onclick=\""+this.name+".click("+index+","+inum+")\">";
		if((index-1) == this.selectId)
			tab += "<td background=\""+this.twoImage2.src+"\" height=\"30\" width=\"5\">&nbsp;</td>";
		else
			tab += "<td background=\""+this.twoImage1.src+"\" height=\"30\" width=\"5\">&nbsp;</td>";
		tab += "<td background=\""+this.twoImage2.src+"\" disabled> &nbsp; "+str+" &nbsp; </td>";
		if(index == (inum-1))
			tab += "<td background=\""+this.twoImage31.src+"\" width=\"5\"></td>";
		else
			tab += "<td background=\""+this.twoImage3.src+"\" width=\"5\"></td>";
		tab += "</tr>";
		tab += "</table>";
	}
	return tab;
	//document.write(tab);
	//document.close();
}
/*
 * 点击事件，可以自己加入其它函数
 */
function panelClick(index,inum) {
	var tdList;

	for(var i = 0; i < inum; i++) {
		if(i==index || document.getElementById(this.name+".tabTrId"+i)==null) 
			continue;
		tdList = document.getElementById(this.name+".tabTrId"+i).children;
		if(i == (index+1))
			tdList[0].background = this.twoImage2.src;
		else
			tdList[0].background = this.twoImage1.src;
		tdList[1].className  = "";
		tdList[1].disabled   = true;
		tdList[1].background = this.twoImage2.src;
		if(i == inum-1)
			tdList[2].background = this.twoImage31.src;				
		else
			tdList[2].background = this.twoImage3.src;
	}
	tdList = document.getElementById(this.name+".tabTrId"+index).children;
	tdList[0].background = this.oneImage1.src;
	tdList[1].className  = "";
	tdList[1].disabled   = false;
	tdList[1].background = this.oneImage2.src;	
	tdList[2].background = this.oneImage3.src;
}
