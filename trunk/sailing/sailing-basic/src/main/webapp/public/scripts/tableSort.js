
/*
 * 前端Table排序
 * 列表JS对象化
 * 调节列表宽度
 *
 */

var TableSort = {};

TableSort.global_isByTagName = (document.getElementsByTagName) ? true : false;
TableSort.global_isMSIE5 = (document.getElementsByTagName && document.all) ? true : false;
TableSort.arrowUp = null;
TableSort.arrowDown = null;
//点击列表事件
TableSort.lastClick = null;
TableSort.preClick  = null;
TableSort.uuid = "oid";
//初始选中行
TableSort.initRow = false;

TableSort.initSortTable = function() {
	TableSort.arrowUp = document.createElement("span");
	//var tn = document.createTextNode("    ");//↓
	//TableSort.arrowUp.appendChild(tn);
	TableSort.arrowUp.innerHTML = " &nbsp; &nbsp; ";
	TableSort.arrowUp.className = "arrow_up";
	TableSort.arrowDown = document.createElement("span");
	//var tn = document.createTextNode("    ");//↑
	//TableSort.arrowDown.appendChild(tn);
	TableSort.arrowDown.innerHTML = " &nbsp; &nbsp; ";
	TableSort.arrowDown.className = "arrow_down";
}

if (TableSort.global_isMSIE5 || TableSort.global_isByTagName) {
	TableSort.initSortTable();
}

//这个方法不但排序还有选中效果
TableSort.sortColumn = function (e) {
	TableSort.clickListing(e);
	TableSort.sortColumnOnly(e);
}

TableSort.sortColumnOnly = function (e) {	
	var tmp, el, tHeadParent;
	if (TableSort.global_isMSIE5) {
		tmp = e.srcElement;
	} else if (TableSort.global_isByTagName) {
		tmp = e.target;
	}
	
	tHeadParent = TableSort.getParentTagName(tmp, "THEAD");
	el = TableSort.getParentTagName(tmp, "TD");
	if(null == el) return;
	if (el.getAttribute("type") == "operate") {
		return;
	}
	if (tHeadParent == null) {
		return;
	}
	if(!el || el.innerHTML.indexOf('checkbox') >= 0) {
		return;
	}
	
	if (el != null) {
		var p = el.parentNode;
		var i;
		if (el._descending) {// catch the null
			el._descending = false;
		} else {
			el._descending = true;
		}
		
		if (tHeadParent.arrow != null) {
			if (tHeadParent.arrow.parentNode != el) {
				tHeadParent.arrow.parentNode._descending = null;   
			}
			tHeadParent.arrow.parentNode.removeChild(tHeadParent.arrow);
		}
		if (el._descending) {			
			tHeadParent.arrow = TableSort.arrowDown.cloneNode(true);
			//TableSort.arrowDown.style.display = "";
		} else {			
			tHeadParent.arrow = TableSort.arrowUp.cloneNode(true);
			//TableSort.arrowUp.style.display = "";
		}
			
		el.appendChild(tHeadParent.arrow);
			
		// get the index of the td
		for (i=0; i<p.cells.length; i++) {
			if (p.cells[i] == el) {
				break;
			}
		}
		var table = TableSort.getParentTagName(el, "TABLE");
		// can't fail	  
		TableSort.sortTable(table,i,el._descending, el.getAttribute("type"));
		//alert("type:"+el.getAttribute("type"));
		//alert("nowrap:"+el.getAttribute("nowrap"));
		//alert("field:"+el.getAttribute("field"));
		//alert(el.innerHTML);
	}
}



TableSort.sortTable = function(tableNode, nCol, bDesc, sType) {
	var tBody = tableNode.tBodies[0];
	var trs = tBody.rows;
	var a = new Array();
	
	for (var i=0; i<trs.length; i++) {
		a[i] = trs[i];
	}
	
	a.sort(TableSort.compareByColumn(nCol,bDesc,sType));
	
	for (var i=0; i<a.length; i++) {
		tBody.appendChild(a[i]);
	}
	//将上次选择的选中
	if(TableSort.lastClick){
		//var boxObj = document.getElementsByName(TableSort.uuid)[.rowIndex-1];
		var boxObj = TableSort.lastClick.childNodes[0].childNodes[0];
		if(boxObj && boxObj.tagName == 'input'){
			boxObj.checked = true;boxObj.onfocus();
		}
		TableSort.isPreLine();
		TableSort.isNextLine();
	}
}

TableSort.CaseInsensitiveString = function(s) {
	return String(s).toUpperCase();
}

TableSort.parseDate = function(s) {
	return Date.parse(s.replace(/\-/g, '/'));
}

TableSort.toNumber = function(s) {
	return Number(s.replace(/[^0-9\.]/g, ""));
}

TableSort.compareByColumn = function(nCol, bDescending, sType) {
	//alert(nCol+"/"+ bDescending+"/"+ sType);
	var c = nCol;
	var d = bDescending;
	
	var fTypeCast = String;
	
	if (sType == "Number") {
		fTypeCast = TableSort.toNumber;
	} else if (sType == "Date") {
		fTypeCast = TableSort.parseDate;
	} else if (sType == "CaseInsensitiveString") {
		fTypeCast = TableSort.CaseInsensitiveString;
	}
	
	return function (n1, n2) {
		if(n1.cells && n1.cells.length){
			if (fTypeCast(TableSort.getInnerText(n1.cells[c])) < fTypeCast(TableSort.getInnerText(n2.cells[c]))) {
				return d ? -1 : +1;
			}
				
			if (fTypeCast(TableSort.getInnerText(n1.cells[c])) > fTypeCast(TableSort.getInnerText(n2.cells[c]))) {
				return d ? +1 : -1;
			}
		}
		return 0;
	};
}

TableSort.getInnerText = function(el) {
	if (TableSort.global_isMSIE5) {
		return el.innerText; //Not needed but it is faster 
	}
	var str = "";
	
	for (var i=0; i<el.childNodes.length; i++) {
		switch (el.childNodes.item(i).nodeType) {
			case 1: //ELEMENT_NODE
				str += TableSort.getInnerText(el.childNodes.item(i));
				break;
			case 3: //TEXT_NODE
				str += el.childNodes.item(i).nodeValue;
				break;
		}  
	} 
	return str;
}

TableSort.getParentTagName = function(el, pTagName) {
	if (el == null) {
		return null;
	} else if (el.nodeType == 1 && el.tagName.toLowerCase() == pTagName.toLowerCase()) {
		return el;
	} else {
		return TableSort.getParentTagName(el.parentNode, pTagName);
	}	
}
TableSort.mainTable = null;//当前操作的Table对象
//选中列表的某一行
TableSort.clickListing =  function(el) {
    
	var tmp;
	if (TableSort.global_isMSIE5) {
		tmp = el.srcElement;
	} else if (TableSort.global_isByTagName) {
		tmp = el.target;
	}
	/**为了CheckBox机能与高亮机能分离
	var flag = tmp.type == "checkbox" ? true : false;
	*/
	var trObj = TableSort.getParentTagName(tmp, "TR");
	
	//tmp == trObj.childNodes[0] ||
	if(null == trObj || trObj.parentNode.tagName== "THEAD") {
		return;
	}
	var tblObj = TableSort.getParentTagName(tmp, "TABLE");
	this.mainTable = tblObj;
	this.setNoSelect();
	
	var array = trObj.getElementsByTagName("input");
	var ctrlArray = new Array();
	for(var i=0;i<array.length;i++)
	{
		var ctrl = array[i];
		
		if(ctrl.name == this.uuid)
		{
			ctrl.selectedNo = null;
			ctrlArray.push(ctrl);
		}
	}
	
	if(ctrlArray.length == 0)
		return;
	var chkbox = ctrlArray[0];
	//document.getElementById("detailIdsForPrintAll").value = chkbox.value;
	chkbox.selectedNo = chkbox.value;
	//if(TableSort.initRow && trObj == TableSort.lastClick && flag){
	/**
	if(TableSort.initRow && trObj == TableSort.lastClick ){// && flag
		TableSort.setNoSelect();
		//TableSort.setClickTrObj(TableSort.lastClick,flag)
		return;
	}
	*/
	
	TableSort.setClickTrObj(trObj);//,flag
	trObj.ondblclick = TableSort.dblClick;
}
TableSort.setClickTrObj = function(trObj,flag){

	if(!trObj){
		return;
	}
	TableSort.initRow = true;
	TableSort.setAbleLineButton();	
	if(Global.topOpera && trObj.rowIndex ==1){		
		TableSort.setUnabledPreLine();
	}
	if(Global.topOpera && trObj.rowIndex == trObj.parentNode.childNodes.length){
		TableSort.setUnabledNextLine();
	}
		//为了CheckBox机能与高亮机能分离
	TableSort.setLastClick(TableSort.lastClick,flag);

	TableSort.setClick(trObj);
	//将对应的TableSort.uuid选中，如果为hidden域也一样效果
	var boxObj = trObj.cells[0].childNodes[0];
	//var boxObj = document.getElementsByName(TableSort.uuid)[trObj.rowIndex-1];

	if(boxObj && boxObj.name == TableSort.uuid){
		if(boxObj.disabled){//设置为不可选择
			trObj.title = boxObj.title;
			return false;
		}
		
		if (boxObj.type.toUpperCase() == "CHECKBOX") {
			if (window.event) {
				if (window.event.srcElement != boxObj) {
					boxObj.checked = !boxObj.checked;
				}
			} else {
				boxObj.checked = !boxObj.checked;
			}
		} else {
			boxObj.checked = true;
		}
		boxObj.onfocus();
	}

	trObj.lastClassName = trObj.className;

	trObj.className = "Alt";

	if(TableSort.preClick != TableSort.lastClick){
		TableSort.preClick = TableSort.lastClick;
	}
	TableSort.lastClick = trObj;
	TableSort.afterFocusClick(trObj);
	
}
TableSort.setLastClick = function(obj,flag){
	if(obj){
		obj.className = obj.lastClassName;
		var boxObj = obj.cells[0].childNodes[0];
		//为了CheckBox机能与高亮机能分离
		//if(boxObj && boxObj.name == TableSort.uuid && !flag){
		if(boxObj && boxObj.name == TableSort.uuid){
			//boxObj.checked = false;
			boxObj.onblur();
		}
		TableSort.afterBlurClick(obj);
	}	
}
TableSort.setNoSelect = function(){
	if(this.mainTable){
		try{
			var oidArray = this.mainTable.getElementsByTagName("input");
			for(var i=0;i<oidArray.length;i++)
			{
				var ctrl = oidArray[i];
				
				if(ctrl.name == this.uuid)
				{
					ctrl.selectedNo = null;
				}
			}
		}catch(e){}
	}
	TableSort.setLastClick(TableSort.lastClick,false);
	TableSort.lastClick = null;//TableSort.preClick;
	return;
}

//注意这时 TableSort.lastClick != trObj,发生在 ondblclick前
TableSort.setClick = function(trObj){
	//用于页面重载，
}
TableSort.afterFocusClick = function(trObj){}
TableSort.afterBlurClick = function(trObj){}

//重复点击就跳到下一个Tab
TableSort.dblClick = function(){
	if( parent.panel && parent.panel.selectId >=0 ){
		var nextNum =  parseInt(parent.panel.selectId+1,10);
		if( nextNum < parent.panel.dataArr.length){

			parent.panel.click(nextNum);
		}
	}
}

//是否使用顶部操作按钮，可以在global.js中置值
TableSort.isUseLineButton = true;
TableSort.preNum  = 5; //上一页在 TopOpera 中序号
TableSort.nextNum = 6; //下一页

//设置上下行按钮可用或屏蔽
TableSort.setAbleLineButton = function(){
	if(Global.topOpera && !Global.isDefinedWindow()){
		Global.topOpera.setAbledBtn(TableSort.preNum);
		Global.topOpera.setAbledBtn(TableSort.nextNum);
		//设置按钮事件
		Global.displayOperaButton();
	}
}
TableSort.setUnabledPreLine = function(){
	Global.topOpera.setUnabledBtn(TableSort.preNum);
}
TableSort.setUnabledNextLine = function(){
	Global.topOpera.setUnabledBtn(TableSort.nextNum);
}
//判断操作按钮是否可用
TableSort.isPreLine = function(){
	if(TableSort.lastClick && TableSort.isUseLineButton){
		var trObj = TableSort.lastClick.previousSibling;
		if(trObj)
			Global.topOpera.setAbledBtn(TableSort.preNum);
		else
			Global.topOpera.setUnabledBtn(TableSort.preNum);
	}else if(parent.panel && self.name != parent.panel.listFrame && parent.frames[parent.panel.listFrame].TableSort
		&& parent.frames[parent.panel.listFrame].TableSort.isUseLineButton){
		TableSort.preLine = function(){
			//TODO link panel.js
			parent.frames[parent.panel.listFrame].TableSort.preLine();
			parent.panel.click(parent.panel.selectId);
		}
		parent.frames[parent.panel.listFrame].TableSort.isPreLine();
		//Global.topOpera.setAbledBtn(TableSort.preNum);		
	}else
		Global.topOpera.setUnabledBtn(TableSort.preNum);
}
//提供点击上下行的的事件
TableSort.preLine = function(){	
	if(TableSort.lastClick){
		var trObj = TableSort.lastClick.previousSibling;
		TableSort.setClickTrObj(trObj,false);
		trObj = TableSort.lastClick.previousSibling;
		if(!trObj){
			TableSort.setUnabledPreLine();
		}
	}else{
		TableSort.setUnabledPreLine();
	}
}
TableSort.isNextLine = function(){
	if(TableSort.lastClick && TableSort.isUseLineButton){
		var trObj = TableSort.lastClick.nextSibling;
		if(trObj)
			Global.topOpera.setAbledBtn(TableSort.nextNum);
		else
			Global.topOpera.setUnabledBtn(TableSort.nextNum);
	}else if(parent.panel && self.name != parent.panel.listFrame && parent.frames[parent.panel.listFrame].TableSort 
		&& parent.frames[parent.panel.listFrame].TableSort.isUseLineButton){
		TableSort.nextLine = function(){
			parent.frames[parent.panel.listFrame].TableSort.nextLine();
			parent.panel.click(parent.panel.selectId);
		}
		parent.frames[parent.panel.listFrame].TableSort.isNextLine();
	}else
		Global.topOpera.setUnabledBtn(TableSort.nextNum);
}
TableSort.nextLine = function(){
	if(TableSort.lastClick){
		var trObj = TableSort.lastClick.nextSibling;
		TableSort.setClickTrObj(trObj,false);
		trObj = TableSort.lastClick.nextSibling;
		if(!trObj){
			TableSort.setUnabledNextLine();
		}
	}else{
		TableSort.setUnabledNextLine();
	}
}

/**
 * 以下方法将listing装载为JS Object,应用于选择列表中某行值返回到父页面，这样通过object来传递数据
 * 注意这里遵守列表有关约定：选择域名为oid（唯一键）,表头部分要使用field字段名为对象属性,
 */
ListUtil = {};

//selfName 是初始化的对象名，tabId是表格ID
ListUtil.Listing = function(selfName,tabId){
	this.name  = selfName;
	this.tabId  = tabId;
	this.selfObj = document.getElementById(this.tabId); 
	this.tabHead = new Object();
	this.tabBody = new Array();
	this.head = new Object();
	this.data = new Array();
	this.uuid = "oid";
	
	//初始化，将列表序列化为Object
	this.init = function(){
		var obj = this.selfObj;
		this.tabHead = obj.rows[0].cells;
		this.tabBody = obj.rows;
		this.getHead();
		this.getData();
	}
	//返回列表中数据，兼容Firefox要求
	this.getDataValue = function(obj){
		var str = "";
		//获取 checkbox / radio / hidden 域的值日
		if (!obj) {
			return str;
		}
		if(obj.childNodes[0] && obj.childNodes[0].tagName == "INPUT" ){
			str = obj.childNodes[0].value;
		}else if(obj.innerText)
			str = obj.innerText;
		else if(obj)
			str = obj.innerHTML.replace(/\<[^>]*>/gi,"");
		str = str.replace(/(^(\s|\u3000)*)|((\s|\u3000)*$)/g, '');
		return str;
	}
	//得到列表头部定义
	this.getHead = function(){
		var temp;
		for(var i=0;i<this.tabHead.length;i++){
			temp = this.getDataValue(this.tabHead[i]);
			if(this.tabHead[i].getAttribute("field")){
			eval(this.name+".head."+this.tabHead[i].getAttribute("field") +" = '"+temp+"'");
				
			//	eval("if(typeof "+this.name+".head."+this.tabHead[i].getAttribute("field")+" == 'object')"+this.name+".head."+this.tabHead[i].getAttribute("field") +" = '"+temp+"'");
			}
			//obj.setAttribute(this.tabHead[i].field,);
		}
	}
	//如果字符串最后一个字是'\',则再拼一个'\'
	this.appendSlash = function(str){
		if (str.length > 1) {
			if (str.substr(str.length-2,1) != '\\' && str.substr(str.length-1,1) == '\\') {
				str += '\\';
			}
		} else if (str.length > 0) {
			if (str.substr(str.length-1,1) == '\\') {
				str += '\\';
			}
		}
		return str;
	}
	//得到列表数据定义
	this.getData = function(){
		var boxObj;
		var n = 0;
		var temp;
		var h = 0;
		for(var i=1;i<this.tabBody.length;i++){
			this.data[n] = new Object();
			h = 0;
			for(var j in this.head){	
				temp = this.getDataValue(this.tabBody[i].cells[h]);
				temp = this.appendSlash(temp);
				var obj = eval(this.name);
				if(obj instanceof Object){
					obj.data[n][j] = temp;
					//alert("____1______"+obj.data[n][j]);
					//alert("____2______"+eval(this.name+".data["+n+"]."+j));
				}
				//eval(this.name+".data["+n+"]."+j+"='"+ temp+"'");
				h++;
				//eval(this.name+".data["+n+"]."+this.tabHead[j].field +"= "+ this.name+".tabBody["+i+"].cells["+j+"].innerText");
			}
			//boxObj = document.getElementsByName(this.uuid)
			boxObj = this.tabBody[i].cells[0].childNodes[0];
			if(boxObj)
				eval(this.name+".data["+n+"]."+this.uuid +" =  '"+boxObj.value+"'");
			n++;
		}
	}
	
	//选择单个
	this.selectOne = function(){
		var boxObj = document.getElementsByName(this.uuid);
		var arr = new Array();
		if(boxObj && TableSort.lastClick){
			boxObj = boxObj[TableSort.lastClick.rowIndex-1];
			if(boxObj && boxObj.disabled == false){
				var uid = "";
				for(var i=0;i<this.data.length;i++){
					uid = eval(this.name+".data["+i+"]."+this.uuid);
					//uid = this.data[i].getAttribute(this.uuid);
					if(uid == boxObj.value){
						arr[0] = this.data[i];
						break;
					}
				}
			}
		}
		return arr;
	}
	//选择多个
	this.selectMult = function(){
		var boxObj = document.getElementsByName(this.uuid);
		var arr = new Array();
		if(boxObj){
			var uid = "";
			for(var i=0;i<this.data.length;i++){
				uid = eval(this.name+".data["+i+"]."+this.uuid);
				for(var j=0;j<boxObj.length;j++){
					if(boxObj[j] && boxObj[j].disabled == false && boxObj[j].checked && uid == boxObj[j].value){
						arr[arr.length] = this.data[i];
						break;
					}
				}
			}
		}
		return arr;
	}

	this.selectWindow = function(flag){
		var arr = new Array();
		if(flag == 1){
			arr = this.selectOne();
			if(arr.length == 0 && this.data.length > 0){
				arr[0] = this.data[0];
			}
			if(arr.length == 0){
				alert("请选择列表中一行!");return;
			}
		}else if(flag == 2){
			arr = this.selectMult();
			if(arr.length == 0){
				alert("请点击选中列表前的复选框！");return;
			}
		}
		//top.returnValue = arr;
		//top.close();
		top.definedWin.listObject(arr);
	}
}

/*以下方法定义表格拖拉宽度功能*/
//定义对象，如果有多个列表，可以使用其他变量
ListUtil.cell = new Array();
ListUtil.fixCell = function(objName,divObj){
	this.name    = objName;
	this.selfObj = divObj;
	this.Grid   = this.selfObj.childNodes[0];
	this.tabCol = this.Grid.childNodes(0).childNodes;
	this.lineV = null;
	this.lineH = null;
	this.tabLeft = parseInt(this.selfObj.offsetLeft);

	this.sWidth1 = 0;
	this.midVar  = new Object();
	this.midVar.blnAdjust       = false;
	this.midVar.blnAdjustH      = false;
	this.midVar.mAdjustColWidth = true;
	this.midVar.mAdjustRowHeight= true;
	this.midVar.AdjustRow       = 0;
	this.midVar.AdjustCol       = 0;
	this.midVar.mautosize       = false;

	this.eventX;
	this.eventY;

	//onmousdown
	this.onDown = function(event){
		event = event ? event : (window.event ? window.event : null);
		this.eventX = event.x ? event.x : event.pageX;
		this.eventY = event.y ? event.y : event.pageY;
		if (event.button != 1) 
			return;
		var curObj=document.elementFromPoint(this.eventX,this.eventY);
		if(curObj && curObj.tagName=="TD"){
			var curCol=curObj.cellIndex;
			curObj=curObj.parentNode;
			var curRow=curObj.rowIndex;
		}else {
			return;
		}

		if (this.Grid.rows(curRow).style.cursor == "col-resize"){
			this.lineV.style.display ="";
			this.selfObj.setCapture();
			this.sWidth1=this.eventX +document.body.offsetLeft;
			this.lineV.style.left = this.sWidth1;
			this.lineV.style.top  = parseInt(this.selfObj.offsetTop);//+parseInt(this.Grid.offsetTop);
			this.lineV.style.height = this.selfObj.offsetHeight;
			this.midVar.blnAdjust=true;
			return;
		}else {
			//this.lineV.style.display ="none";
			this.midVar.blnAdjust=false;
		}
		if (this.tabCol(curCol).style.cursor == "n-resize"){
			this.lineH.style.display = "";
			this.selfObj.setCapture();
			this.sWidth1=this.eventY+document.body.scrollTop;
			this.lineH.style.top =this.sWidth1;
			this.lineH.style.left=this.tabLeft+parseInt(this.Grid.offsetLeft);
			this.lineH.style.width =this.selfObj.offsetWidth;
			this.midVar.blnAdjustH=true;
			return;
		}else {
			//this.lineH.style.display ="none";
			this.midVar.blnAdjustH=false;
		}
	}

	//onmousemove
	this.onMove = function(event){
		event = event ? event : (window.event ? window.event : null);
		this.eventX = event.x ? event.x : event.pageX;
		this.eventY = event.y ? event.y : event.pageY;
		this.tabCol(0).style.cursor="default";
		if (this.midVar.blnAdjust==true){
			this.lineV.style.left =this.eventX+document.body.offsetLeft;
		}else if(this.midVar.blnAdjustH==true){
			this.lineH.style.top =event.y+document.body.offsetTop;
		}else {	
			var curObj=document.elementFromPoint(this.eventX,this.eventY);
			if(curObj && curObj.tagName=="TD"){
				var curCol=curObj.cellIndex;
				curObj=curObj.parentNode;
				var curRow=curObj.rowIndex;
			}else {
				return;
			}
			if(curRow==0){
				this.Grid.rows(curRow).style.cursor="default";
				if(this.midVar.mAdjustColWidth==false){
					return;
				}
				var xLeft   = this.eventX+document.body.offsetLeft;
				var wLeft = 0;
				var iTmpLeft = 0;
				for (var i=0;i<this.Grid.rows(curRow).cells.length;i++){
					//if(this.tabCol(i).style.display!="none"){
						tCell = this.Grid.rows[curRow].cells[i];
						wLeft = this.tabLeft+parseInt(tCell.offsetLeft)+parseInt(tCell.offsetWidth);				
						if (xLeft >= wLeft-4 && xLeft <= wLeft+4 ){
							iTmpLeft=parseInt(tCell.offsetLeft);
							if(this.Grid.style.borderLeftStyle=="none"){
								iTmpLeft ++;
							}
							var offsetCol=this.LeftToCol(iTmpLeft);
							if(xLeft>=this.tabLeft+iTmpLeft+parseInt(tCell.offsetWidth)) {
								var bFind=false;
								for (var j=offsetCol+1;j<this.Grid.rows(curRow).cells.length;j++){
									if(this.tabCol(j).style.pixelWidth>1){
										break;
									}
									bFind=true;
								}
								if(bFind){
									this.midVar.AdjustCol=j-1;
								}
								else {
									this.midVar.AdjustCol=offsetCol;
								}
							}else
								this.midVar.AdjustCol=offsetCol	;			
							this.Grid.rows(curRow).style.cursor = "col-resize";
							break;
						}
					//}
				}//end for
				return
			}	
			if(curCol==0){
				this.tabCol(curCol).style.cursor ="default";
				if(this.midVar.mAdjustRowHeight==false){
					return;
				}
				var yTop  = this.eventY+document.body.offsetTop;
				var tRows = null;
				var tTop  = 0;
				var bFind;
				for (var i=0;i<this.Grid.rows.length;i++){
					if(this.Grid.rows(i).style.display!="none"){
						tRows = this.Grid.rows[i].cells[0];
						tTop  = parseInt(this.selfObj.offsetTop)+parseInt(tRows.offsetTop)+parseInt(tRows.offsetHeight);
						if (yTop >= tTop-4 && yTop <= tTop+4 ){									
							if(yTop >= tTop){
								bFind=false;
								for (var j=i+1;j<this.Grid.rows.length;j++){
									if(this.Grid.rows(j).style.display != "none"){
										break;
									}
									bFind=true;
								}
								if(bFind){
									this.midVar.AdjustRow = j-1;
								}else{
									this.midVar.AdjustRow = i;
								}
							}else{
								this.midVar.AdjustRow = i;
							}				
							this.tabCol(curCol).style.cursor = "n-resize";
							break;
						}
					}
				}//end for
			}//end if
		
		}//
	}
	
	//onmouseup
	this.onUp = function(){
		if (this.lineV.style.display=="" ){
			var lngRange=parseInt(this.lineV.style.left) - parseInt(this.sWidth1);
			var afterWidth=this.tabCol[this.midVar.AdjustCol].style.pixelWidth+ lngRange;
			if(afterWidth<=0){
				this.tabCol[this.midVar.AdjustCol].style.width = 50;
			}else {
				this.tabCol[this.midVar.AdjustCol].style.width=afterWidth;
			}
			if(this.midVar.mautosize){
				this.selfObj.style.width=this.Grid.offsetWidth;
			}
			//this.Grid.style.width = this.Grid.offsetWidth + 10;
			this.midVar.AdjustCol=0;
			this.sWidth1=0;
			this.Grid.style.cursor="default";
			this.lineV.style.display ="none";
			this.selfObj.releaseCapture ();
			this.midVar.blnAdjust=false;
		}
		if (this.lineH.style.display=="" && this.sWidth1!=0 ){
			var lngRange=parseInt(this.lineH.style.top) - parseInt(this.sWidth1);
			var afterWidth=this.Grid.rows(this.midVar.AdjustRow).style.pixelHeight+ lngRange;
			if(afterWidth<=0){
				this.Grid.rows(this.midVar.AdjustRow).style.height= 20;
			}
			else {
				if(this.Grid.rows(this.midVar.AdjustRow).style.display=="none") {
					this.Grid.rows(this.midVar.AdjustRow).style.display="";
				}
				this.Grid.rows(this.midVar.AdjustRow).style.height=afterWidth;
			}
			if(this.midVar.mautosize){
				this.selfObj.style.height=this.Grid.offsetHeight;
			}
			this.midVar.AdjustRow=0;
			this.sWidth1=0;
			this.Grid.style.cursor="default";
			this.lineH.style.display ="none";
			this.selfObj.releaseCapture();
			this.midVar.blnAdjustH=false;			
		}
		Global.setHeight();
		//TableSort.resizeListing(this.selfObj);
	}

	this.LeftToCol = function(lngLeft) {
		var lngWidth=0;
		var temp = 0;
		for(var ii=0;ii<this.tabCol.length;ii++) {
			temp = ii;
			lngWidth=lngWidth+this.tabCol(ii).style.pixelWidth;
			if(lngWidth>=lngLeft){
				break;
			}			
		}
		return temp;
	}

	//初始化
	this.init = function(){
		//如果没有col自动补充上
		if(this.Grid.childNodes.length < 2){
			return;
		}else if(this.Grid.childNodes.length < 3){
			var group = document.createElement("colgroup");
			var node = this.Grid.childNodes[0];
			this.Grid.insertBefore(group,node);
			var col;
			for(var i=0;i<this.Grid.rows(0).cells.length;i++){
				col = document.createElement("col");
				group.appendChild(col);
			}
			this.tabCol = this.Grid.childNodes(0).childNodes;
		}
		this.writeDivLine();
	}
	this.writeDivLine = function(){
		this.selfObj.listObj = this.name;
		this.selfObj.onmousedown = function(event){eval(this.listObj).onDown(event)};
		this.selfObj.onmousemove = function(event){eval(this.listObj).onMove(event)};
		this.selfObj.onmouseup   = function(){eval(this.listObj).onUp()};

		var node = document.createElement("div");
		node.id = this.name+'.lineV';
		node.style.cssText = 'border-left:1px solid #ddd;display:none; height: 28px;width: 1px; left:0px;top:0px; position:absolute;cursor:col-resize';
		this.selfObj.parentNode.appendChild(node);
		node = document.createElement("div");
		node.id = this.name+'.lineH';
		node.style.cssText = 'display: none; height: 1px; width: 35px;left:0px;top:0px; position:absolute;cursor:n-resize';
		this.selfObj.parentNode.appendChild(node);
		this.lineV = document.getElementById(this.name+".lineV");
		this.lineH = document.getElementById(this.name+".lineH");
		for (var i=0;i<this.tabCol.length;i++){
			this.tabCol(i).style.width = (this.Grid.rows[0].cells[i].offsetWidth ? this.Grid.rows[0].cells[i].offsetWidth - 2 : this.Grid.rows[0].cells[i].offsetWidth)+"px";
		}
		this.Grid.style.tableLayout = "fixed";
	}
	this.init();
}

/*TODO 列表的方法，在缩小区域的时候有不调节的bug*/
//为了出现横向滚动条，调节列表宽度
TableSort.resizeListing = function(list) {
	//列表的div的ID
	if(typeof list == "string"){
		list = document.getElementById(list);
	}else{
		list = list ? list : document.getElementById("divId_scrollLing");
	}
	if(list){
		//list.className = "list_scroll";
		//list.style.width = (document.body.scrollWidth) + "px";
	}
}

//window.onresize  = TableSort.resizeListing;


/* 点击每一行，展开其详细信息
 * table 当前表格对象
 */
TableSort.displayDetail = function(url,obj){
	obj = obj ? obj : event.srcElement;
	var src =  event.srcElement;
	var row = 0;
	while(src!=null){
	  if(src.nodeName=="TR"){   
		  row = src.rowIndex;
		  break;
	  }
	  src=src.parentElement;
	}   
	var table = src.parentElement.parentElement;
	//注意row属性
	var detailId = "";
	if(src.rowNumber){
		detailId = table.id+"_tr_"+src.rowNumber;
	}else{
		src.rowNumber = src.rowIndex;
		detailId = table.id+"_tr_"+src.rowNumber;
	}
	if(!document.getElementById(detailId)){
	   TableSort.addChildPage(table,detailId,row+1,url); 
	}
	var tr = document.getElementById(detailId);
	StyleControl.switchDivListing(obj,tr)
}
//在指定的table位置加入子表页面
TableSort.addChildPage = function(table,detailId,row,url){
	TableSort.beforeAddChildPage(table,row);
	url = url.indexOf("?")>0 ? url : url +"?txt="+row;
	row = row ? row : table.rows.length;
	var tr = table.insertRow(row);   
	tr.id = detailId;  
	var td = document.createElement("TD");
	tr.appendChild(td);
	if(top.definedWin){
		top.definedWin.openLoading();
	}
	var td = document.createElement("TD");
	td.innerHTML = "&nbsp;";
	tr.appendChild(td);
	td = document.createElement("TD");
	td.colSpan = table.rows[0].cells.length-2;
	td.innerHTML = '<iframe border=0 frameborder="0" name="definedWin_'+detailId+'" src="'+url+'" height="100%" width="100%" scrolling="no"></iframe>';
	tr.appendChild(td);	
	TableSort.afterAddChildPage(table,row);
}

/**
*用于调整可定制列表的列宽
*@param 列宽定义对象
*       对象示例:var rowWidth = {
*				sortNo:"100px",//列的字段名,列宽设定值
*				supplyCopeNo:"10%",
*				supplyCorpName:"500px"
*			   };
*/
TableSort.setTableColWidth = function(colWidth){
	var tdArr = document.getElementsByTagName("td");
	for(var i = 0 ; i < tdArr.length ; i++){
		var td = tdArr[i];
		if(td.field != null && td.field != ""){
			var width = colWidth[td.field];
			if(width != null && width != ""){
				td.style.width=width;
			}
		}
	}
} 
//回调
TableSort.beforeAddChildPage = function(table,row){}
TableSort.afterAddChildPage  = function(table,row){}
