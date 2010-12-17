/*
 * 选项卡控件对象
 * 2006-04-06
 * 
 * @name     控件名字，用于应用出现多组选项卡情况。
 * @arr      所有选项的数据二维数组 [显示名称,点击事件]，
             例如：arr[[name1,function1,url1],[name2,function2,url2],[...]];
 * @selectId 默认选中的项的序号，int，（从0开始）
 */

if(Panel == null){
	var Panel = {};
}


Panel.panelObject = function (name,arr,selectId,imagePath,oneTimeUrl){
	this.checktaglib = false;
	this.checkallvalue = false;
	this.beforeindex = selectId;
	this.beforeFrameValues = new Array();
	this.beforeSearchTermList = new Array();
	this.afterSearchTermList = new Array();
	this.ispanelclick = false;
	this.name = name;	
	this.imagePath = imagePath ? imagePath : ContextInfo.contextPath+"/image/tree/";
	this.bgCss  = "panel_bg";		//背景css
	this.tabCss = "panel_tab";		//选择卡背景css
	//this.focusCssv=  "";			//聚焦css
	//this.blurCss  =  "";			//普通css
	this.dataArr   = arr;
	this.selectId  = selectId ? selectId : 0;//当前选中的选项卡序号
	this.newPanel  = new Array();   //建立一个新选项卡，当前序号是修改对应序号的卡内容
	this.disablePanel = new Array();//指定Tab为禁用，例如new Array(1,2)
	//this.address   = address;//显示位置
	this.lastUrl   = "";					 //上一次选中url
	this.lastDivId = "divId_"+this.name+"_0";//上一次选中divId
	this.focusFont = "";
	this.blurFont  = "font_blur";
	this.uuid      = "oid";					 //定义的列表的唯一键名，用于传递参数
	this.listFrame = "frame_"+this.name+"_0";//定义列表frame名
	this.isPass    = true;			//是否允许切换，如果被子页面指定false,则会出现message提示
	this.message   = "迁移页面可能会丢失现有未保存的数据，确实要迁移吗？";//以前的提示信息："切换Tab可能会丢失现有未保存的数据，确实要切换吗？";
	this.holdPage  = null;          //如果要保留原页面（不刷新），则这里赋予tab中序号
	this.reloadPage= null;          //如果只对页面进行reload,不加载新url,则这里赋予tab中序号
	this.isUseOid  = true;          //是否使用oid做参数传递，默认是进行的
	this.oneTimeUrl = oneTimeUrl ? oneTimeUrl : "";   //工作流等处理用来显示entry下的某一具体页面的URL

	/** 为防止加载图片时候导致异常，加入缓存*/
    this.oneImage1 = new Image();
	this.oneImage1.src = this.imagePath+"panel_one_1.gif";
	this.oneImage2 = new Image();
	this.oneImage2.src = this.imagePath+"panel_one_2.gif";
	this.oneImage3 = new Image();
	this.oneImage3.src = this.imagePath+"panel_one_3.gif";
	this.twoImage1 = new Image();
	this.twoImage1.src = this.imagePath+'panel_two_1.gif';
	this.twoImage2 = new Image();
	this.twoImage2.src = this.imagePath+'panel_two_2.gif';
	this.twoImage3 = new Image();
	this.twoImage3.src = this.imagePath+'panel_two_3.gif';
	this.twoImage31 = new Image();
	this.twoImage31.src = this.imagePath+'panel_two_3_1.gif';

	//public
	this.display       = Panel.display;		 //显示选项卡
	this.click         = Panel.click;		 //点击选项卡
	this.displayFrame  = Panel.displayFrame; //加载iframe中路径
	this.setUrl        = Panel.setUrl;		 //提供重载url路径
	this.setContent    = Panel.setContent ;  //如果是切换显示tab，这个方法是定义显示其内容
	this.beforeClick   = Panel.beforeClick;  //在切换tab之前的自定义操作
	this.afterClick    = Panel.afterClick;   //在切换tab之后的自定义操作
	this.selfClick     = Panel.selfClick;    //业务页面自定义操作
	//private
	this.setTab     = Panel.setTab;		 //显示某个项	
	this.switchTab  = Panel.switchTab;
	this.displayDiv = Panel.displayDiv;
	this.setDisable = Panel.setPanelDisable;
	this.setAble    = Panel.setPanelAble;
	this.setProperies = Panel.setPanelProperies;
	
	this.checkValues = Panel.checkValues;
	this.checkTaglibValues = Panel.checkTaglibValues;
	this.confirmMessage = Panel.confirmMessage;
	this.movePage = Panel.movePage;
	this.saveSearchTerm = Panel.saveSearchTerm;
	this.setSearchTerm = Panel.setSearchTerm;
}

Panel.display = function (){
	var arr = this.dataArr;
	var selid = this.selectId;
	var str = "<table border=0 cellspacing='0' cellpadding='0' width='100%'><tr><td nowrap ><div class='"+this.bgCss+"'>";
	var frm = "";
	var i = 0;
	if (this.oneTimeUrl != "") {
		while(i<arr.length){
			if (this.oneTimeUrl.indexOf(arr[i][2]) != -1) {
				this.selectId = i;
				selid = i;
				break;
			}
			i++;
		}
		if (i >= arr.length) {
		//panel里没有与oneTimeUrl相似的url
			this.oneTimeUrl = "";
		}
		i = 0;
	}
	try{
	while(i<arr.length){
		this.lastUrl   = "about:blank";
		str += "<div onclick='"+arr[i][1]+"' class='"+this.tabCss+"'>";
		str += this.setTab(i);
		str += "</div>";
		frm += ' <tr';
		if(i != selid )
			frm += ' style="display:none" ';
		else{
			this.lastDivId = 'divId_'+this.name+'_'+i;
			if (this.oneTimeUrl != ""){
				this.lastUrl   =  this.oneTimeUrl;
				this.oneTimeUrl = "";
			} else if(arr[i][2]){
				this.lastUrl   =  arr[i][2];
			}
		}
		frm += ' id="divId_'+this.name+'_'+i+'"><td nowrap  ';//divId_panel_0
		frm += ' class="panel_content">';
		if(arr[i][2]){//frame_panel_0
			frm +='	<iframe frameborder="0" scrolling="no" src="'+this.lastUrl+'" height="100%" width="100%" '
		       +'    name="frame_'+this.name+'_'+i+'" id="frame_'+this.name+'_'+i+'"></iframe> ';
			 arr[i][3] = 'frame_'+this.name+'_'+i;
		}
		frm += '</td></tr>';
		i++;
	}
	}catch(e){}
	frm += '</table>';
	this.dataArr = arr;
	//str += "<div background='"+golobal_image_path+"/panel_end.jpg' class='panel_end' id='tdId_panel_end'></div>";
	//str += "<div class='panel_line'>&nbsp;</div>";	
	str += "</div></td></tr>";	
	str += frm;
	this.beforeindex = this.selectId; //yushn 2008-03-04
	return str;
}

Panel.setTab = function(index) {
	var tab = "";
	inum = this.dataArr.length
	var str = this.dataArr[index][0]; 
	if(index == this.selectId) {
		tab += "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" >";
		tab += "<tr id=\""+this.name+".tabTrId"+index+"\" style=\"cursor:pointer\" onclick=\""+this.name+".click("+index+",true)\">";
		tab += "<td nowrap background = '"+this.oneImage1.src+"' height=\"23\" width=\"5\">&nbsp;</td>";//class=\"tab_selected_panel1\"
		tab += "<td nowrap background = '"+this.oneImage2.src+"' class='"+this.focusFont+"' id='"+this.name+"_tdId_panel_"+index+"'> &nbsp; "+str+" &nbsp; </td>";
		tab += "<td nowrap background = '"+this.oneImage3.src+"' width=\"5\">&nbsp;</td>";
		tab += "</tr>";
		tab += "</table>";
	}
	else {
		tab += "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" >";
		tab += "<tr id=\""+this.name+".tabTrId"+index+"\" style=\"cursor:pointer\" onclick=\""+this.name+".click("+index+",true)\">";
		if((index-1) == this.selectId)
			tab += "<td nowrap background=\""+this.twoImage2.src+"\" height=\"23\" width=\"5\">&nbsp;</td>";
		else
			tab += "<td nowrap background=\""+this.twoImage1.src+"\" height=\"23\" width=\"5\">&nbsp;</td>";
		tab += "<td nowrap background=\""+this.twoImage2.src+"\" class='"+this.blurFont+"' id='"+this.name+"_tdId_panel_"+index+"'> &nbsp; "+str+" &nbsp; </td>";
		if(index == (inum-1))
			tab += "<td nowrap background=\""+this.twoImage31.src+"\" width=\"5\"></td>";
		else
			tab += "<td nowrap background=\""+this.twoImage3.src+"\" width=\"5\"></td>";
		tab += "</tr>";
		tab += "</table>";
	}
	return tab;
	//document.write(tab);
	//document.close();
}

//切换tab
Panel.switchTab = function(index){
	var tdList;
	var inum = this.dataArr.length;
	for(var i = 0; i < inum; i++) {
		if(i==index || document.getElementById(this.name+".tabTrId"+i)==null) 
			continue;
		tdList = document.getElementById(this.name+".tabTrId"+i).childNodes;
		if(i == (index+1))
			tdList[0].style.backgroundImage = "url("+this.twoImage2.src+")";
		else
			tdList[0].style.backgroundImage = "url("+this.twoImage1.src+")";
		tdList[1].className  = this.blurFont;
		//tdList[1].disabled   = true;
		tdList[1].style.backgroundImage = "url("+this.twoImage2.src+")";
		if(i == inum-1)
			tdList[2].style.backgroundImage = "url("+this.twoImage31.src+")";				
		else
			tdList[2].style.backgroundImage = "url("+this.twoImage3.src+")";
	}
	tdList = document.getElementById(this.name+".tabTrId"+index).childNodes;
	tdList[0].style.backgroundImage = "url("+this.oneImage1.src+")";
	tdList[1].style.backgroundImage = "url("+this.oneImage2.src+")";	
	tdList[2].style.backgroundImage = "url("+this.oneImage3.src+")";
	tdList[1].className  = this.focusFont;

	var frmNa = "frame_"+this.name+"_"+index;
	var divId = "divId_"+this.name+"_"+index;
	var frm  = document.getElementById(frmNa);
	if(frm)
		frm.parentNode.parentNode.style.display = "";
	if(this.lastDivId && divId != this.lastDivId){
		document.getElementById(this.lastDivId).style.display = "none";
	}
	this.lastDivId = divId;
	this.selectId = index;	
}
//如果不用iframe,直接切换div
Panel.displayDiv = function(index){
	var divStr  = "divId_"+this.name+"_"+index;
	var divObj  = document.getElementById(divStr);
	divObj.style.display = "";		
	this.setContent(divObj,index)
	//this.lastDivId = divStr;
}

/* divObj tr的对象，divObj.childNodes[0]即可以填入的内容
 * index  当前选择的序号
 */
Panel.setContent = function (divObj,index){
	divObj.childNodes[0].innerHTML = "当前选择项为空";
}

/*
 * 点击事件，可以自己加入其它函数
 *
 * commitFlg是否提交标记
 */
Panel.click = function(index,commitFlg) {
	//不知道这里怎么有错误 ？ 有方法重载了这个实现 ?
	try{
		if(!this.beforeClick(index))
			return;
		if(!this.selfClick(index))
			return;
		if(!this.isPass){
			if(!confirm(this.message))
				return;
		}
	}catch(e){}
	/*
	//修改了流内外的判定方法
	try{
		//如果是TAB页的切换，那么工作流的权限就不用清空
		top.document.frames["under"].document.frames["mainFrame"].document.getElementById("mustClearFormPermission").value="false";
	}catch(e){}
	*/
	this.switchTab(index);
	var url =  this.dataArr[index][2];
	if(url){
		this.displayFrame(index,url,commitFlg);			
	}else{
		this.displayDiv(index);
	}
	this.afterClick(index);
}

Panel.displayFrame = function (index,url,commitFlg){
	var frmNa = "frame_"+this.name+"_"+index;
	var divId = "divId_"+this.name+"_"+index;
	var frm  = document.getElementById(frmNa);	
	var formTemp = frames[frmNa].document.forms[0];
	if(this.reloadPage == index && formTemp.step && formTemp.step.value.indexOf('remove') == -1){
		//document.frames[frmNa].location.reload(true);
		//TODO 这里对页面提交，有可能存在问题		
		frames[frmNa].FormUtils.post(formTemp, frames[frmNa].location.href);
	}else if(this.holdPage != index){
		frm.src = "about:blank";
		//open loading
		if(top.definedWin){
			top.definedWin.openLoading();
		}
		var inum = 0,newName = this.dataArr[index][0];newUrl = url;
		if(this.newPanel.length > 0 && this.newPanel[0] == index){
			inum    = this.newPanel[0];
			newName = this.newPanel[1] ? this.newPanel[1] : newName;
			newUrl  = this.newPanel[2] ? this.newPanel[2] : newUrl;
		}		
		if(document.frames(frmNa).CurrentPage && document.frames(frmNa).CurrentPage.query && commitFlg==true){
			//如果为查询页面,重新查询
			try{
				var searchDiv = frames[frmNa].document.getElementById("supplierQuery");
				if(searchDiv){
					this.afterSearchTermList[this.selectId] = new Array();
					this.saveSearchTerm(this.afterSearchTermList[this.selectId],searchDiv);
					this.setSearchTerm(this.beforeSearchTermList[this.selectId],searchDiv);
					this.ispanelclick = true;
				}
			}catch(e){}
			document.frames(frmNa).CurrentPage.query();
		}else{
			switch(index){
				case inum:
						frm.src = newUrl;
					break;
				default:
					frm.src = this.setUrl(index,newUrl);
					break;
			}
		}
	}
		
	if(this.disablePanel.length > 0){
		this.setDisable();
	}else
		this.setAble();
	//重新设置高度
	Global.setHeight();
}

//设置Tab的名字、路径等
Panel.setPanelProperies = function(arr){
	for(var i=0;i<arr.length;i++){
		if(arr[i]){
			this.dataArr[i][0] = arr[i][0];
			this.dataArr[i][2] = arr[i][2];
			document.getElementById(this.name+"_tdId_panel_"+i).innerHTML = " &nbsp; "+arr[i][0]+" &nbsp; ";
		}
	}
}

/* 动态扩展点击panel的时候，加载自己url
 * 这个方法在根据自己使用来重构,
 * index 是当前选项卡序号（从0开始）
 * 这里是一个默认实现，要求列表放在第一个tab,列表中唯一键名为 this.uuid
 *
 * 返回为空，则使用原本的url
 * 返回一个url,则就加载这个url
 */
Panel.setUrl = function(index,url){
	var frmNa = this.listFrame;
	var beforefrmNa = "frame_"+this.name+"_"+this.beforeindex;
	try{
		//有可能这个frame出错，取不到其内容???你在说什么不清楚!!!
		var oid= frames[beforefrmNa].document.getElementsByName(this.uuid);
		//当前页为新增oid为空，则取列表页oid
		if(oid.length==1 && (oid[0].value == null || oid[0].value == "" || oid[0].value == "undefined")){
			oid= frames[frmNa].document.getElementsByName(this.uuid);
		}
		if(oid.length && this.isUseOid){
			if(url.indexOf("?")>0)
				url += "&";
			else
				url += "?";
			
			//如果没有选中，则默认取第一行的oid值
			//默认取值有问题,注掉
			var temp = null;
			for(var i=0;i<oid.length;i++){
				if(oid[i].selectedNo){
					temp = oid[i].selectedNo;
					break;
				}
			}
			//var temp = frames[frmNa].document.getElementById("detailIdsForPrintAll");
			
			//if(temp != null && temp.value != ""){
			if(temp == null || temp == "" || temp == "undefined"){
				temp = oid[0].value;
			}
			
			if(url.indexOf(this.uuid+"=")>0){
				if(Global.URLParams[this.uuid])
					return url;
				else{
					url = url.replace(this.uuid+"=",this.uuid+"="+temp);
				}
			}else{
				url += this.uuid+"="+temp;
			}
		}
	}catch(e){}
	return url;
}

//将指定的Tab设置为禁用
Panel.setPanelDisable = function(arr){
	arr = arr ? arr : this.disablePanel;
	var tab;
	for(var j=0;j<arr.length;j++){
		tab  = document.getElementById(this.name+".tabTrId"+arr[j]);
		tab.disabled = true;
	}
}
//将所有Tab设置为 使用
Panel.setPanelAble = function(){
	var tab;
	for(var i=0;i<this.dataArr.length;i++){
		tab  = document.getElementById(this.name+".tabTrId"+i);
		tab.disabled = false;
	}
}

//在切换tab之前的自定义操作
Panel.beforeClick = function(index){
	//alert(this.checktaglib);
	//alert(this.checkallvalue);
	if(this.checktaglib){
		return this.checkTaglibValues();
	}
	else if(this.checkallvalue){
		return this.checkValues();
	}else{return true;}
}
//在切换tab之后的自定义操作
Panel.afterClick = function(index){
	this.beforeindex = index;
}

Panel.selfClick = function(index){
	return true;
}

Panel.movePage = function () {
	try{
		if(this.checktaglib){
			return this.checkTaglibValues();
		}
		else if(this.checkallvalue){
			return this.checkValues();
		}else{return true;}
	}catch(e){
		return true;
	}
}

//检测form值是否改变
Panel.checkValues = function (temp){
	//var checkValues = new frames[beforefrmNa].document.CheckValues.ValuesObject("panelaaaaa");
	//alert(checkBalues.name);
	//checkValues.thisname();
	//alert(this.beforeFrameValues.length);
	if(this.beforeFrameValues.length==0){
		return true;
	}
	var beforefrmNa = "frame_"+this.name+"_"+this.beforeindex;
	var dfs = frames[beforefrmNa].document.forms;
	//遍历所有form
	for(i=0;i<dfs.length;i++){
		var df = dfs[i].getElementsByTagName("*");
		//遍历单个form的所有对象
		for(j=0;j<df.length;j++){
			/* 对可编辑列表置删除位元素跳过 */
			if(isDelFlg(df[j])){
				return this.confirmMessage(temp);
			}
			//过滤掉hidden项,hidden不做比对
			if(!(df[j].type == "hidden")){
				//tagName为"IMG"的项
				if(df[j].tagName == "IMG"){
					//比对src属性
					if(df[j].src != this.beforeFrameValues[i][j]){
						return this.confirmMessage(temp);
					}
				}
				//tagName为"INPUT"的项
				else if(df[j].tagName == "INPUT" && df[j].type == "checkbox"){
					//比对checked属性
					if(df[j].checked != this.beforeFrameValues[i][j]){
						//alert(df[j].checked+"///"+this.beforeFrameValues[i][j]);
						return this.confirmMessage(temp);
					}
				}
				//tagName为"OPTION"的项
				else if(df[j].tagName == "OPTION"){
					//比对value属性
					if(df[j].value != this.beforeFrameValues[i][j]){
						//alert(df[j].value+"///"+this.beforeFrameValues[i][j]);
						return this.confirmMessage(temp);
					}
					/*去除select选择项
					var selectTag = df[j];
					if(selectTag.getElementsByTagName("*").length != this.beforeFrameValues[i][j].length){
						return this.confirmMessage(temp);
					}
					for(var k = 0;0 < selectTag[k].length;k++){
						if(df[j].src != this.beforeFrameValues[i][j]){
							if(!confirm(this.message)){
								return false;
							}else{
								this.checkallvalue = false;
								return true;
							}
						}
					}*/
				}
				//其他项直接比对value属性
				else if(df[j].name != undefined){
					if(df[j].value != this.beforeFrameValues[i][j]){
						//alert(df[j].name);
						//alert("beforeValue:"+df[j].value+"======"+this.beforeFrameValues[i][j]);
						return this.confirmMessage(temp);
					}
				}
			}
		}
	}
	this.checkallvalue = false;
	return true;
}

Panel.confirmMessage = function(temp){
if(temp){
	return false;
}else{
	if(!confirm(this.message)){
		return false;
	}else{
		this.checkallvalue = false;
		return true;
	}
}
}

//检测form值是否改变,此方法已被废除
Panel.checkTaglibValues = function (){
	//alert("begin");//比对开始
	var beforefrmNa = "frame_"+this.name+"_"+this.beforeindex;
	//遍历所有form
	for(i=0;i<frames[beforefrmNa].document.forms.length;i++){
		//遍历单个form的所有tag对象
		for(j=0;j<frames[beforefrmNa].document.forms[i].length;j++){
			//得到以hiddenValueDefault为后缀的标签自定义初始hidden值
			hiddenID = frames[beforefrmNa].document.forms[i].elements[j].name;
			if(!(hiddenID == null) && !(hiddenID.search("hiddenValueDefault") == -1)){
				name = hiddenID.substr(18,hiddenID.length);
				//比较页面tag初始值与当前值是否相等
				if(frames[beforefrmNa].document.forms[i].elements[j].value != frames[beforefrmNa].document.getElementsByName(name)[0].value){
					if(!confirm(this.message)){
						return false;
					}else{this.checktaglib = false;return true;}
				}
			}
		}
	}
	this.checktaglib = false;
	//alert("end");//比对结束
	return true;
}

Panel.saveValues = function (index){
	var noncefrmNa = "frame_"+this.name+"_"+this.index;
	var noncefrm  = document.getElementById(noncefrmNa);
	var allformvaluelist = new Array();
	for(i=0;i<dfs.length;i++){
		var df = dfs[i];
		var formvaluelist = new Array();
		for(j=0;j<df.length;j++){
			formvaluelist[j] = dfs[i].elements[j].value;
		}
		allformvaluelist[i] = formvaluelist;
	}
}

Panel.getLineClassName = function (obj){
	if (obj.parentNode.tagName == "TR") {
		return obj.parentNode.className;
	} else if (obj.parentNode.tagName != "HTML") {
		return Panel.getLineClassName(obj.parentNode);
	} else {
		return "";
	}
}

Panel.saveSearchTerm = function(searchTermList,searchDiv){
	var searchTag = searchDiv.getElementsByTagName("*");
	for(i=0;i<searchTag.length;i++){
		if(searchTag[i].name && (searchTag[i].name).indexOf("conditions(") != -1){
			searchTermList[i] = searchTag[i].value;
		}
	}
}

Panel.setSearchTerm = function(searchTermList,searchDiv){
	var searchTag = searchDiv.getElementsByTagName("*");
	for(j=0;j<searchTag.length;j++){
		if(searchTag[j].name && (searchTag[j].name).indexOf("conditions(") != -1){
			searchTag[j].value = searchTermList[j];
		}
	}
}
