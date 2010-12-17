
/* 
 * Author: Qware company zhejiang college & coheg group  
 * Site  : http://groups.google.com/group/coheg
 * 
 * NOTICE: If You use this code for any purpose, commercial or 
 * private, Please get permission from the Qware company zhejiang college & coheg group.
 * And You can not remove this notice from your final code.
 * ===================================================================
 */


var definedWin = {}


/**
**加入EditPage
**/
definedWin.editPageIns;
definedWin.overlay  = "definedWin_overlay";
definedWin.lightbox = "definedWin_lightbox";
definedWin.content  = "definedWin_content"; 
definedWin.titleName= "definedWin_titleName";
definedWin.loading  = "definedWin_loading";
definedWin.operaButton = "definedWin_button";
definedWin.buttonHeight = 30; 
definedWin.width  = 800;      
definedWin.height = 540;
definedWin.txtName= null;
definedWin.isLoading = false; 
definedWin.isModal   = false; 
definedWin.isOpen    = false; 
definedWin.modalNum  = -1;
definedWin.returnArray = new Object();

definedWin.idx  = 0;
definedWin.xidx = 0;
definedWin.cascade = 0;
definedWin.cOffset = 25;
definedWin.cWins   = new Array();
definedWin.foo = null;

definedWin.ok = "确定";
definedWin.cancel = "取消";
definedWin.oldOk = "确定";
definedWin.oldCancel = "取消";
definedWin.curWidth  = null;      
definedWin.curHeight = null;

definedWin.openWindow = function(txtName,winUrl,indexNum,okTxt,cancelTxt,curWidth,curHeight) {
//2008-02-21 change by wangshuo start
	//window.open(winUrl,txtName,"height=700, width=800, scrollbars=yes, top=50,left=100, center=yes,resizable=yes");
	definedWin.openListingUrl(txtName,winUrl,null,true,okTxt,cancelTxt,curWidth,curHeight);
//2008-02-21 change by wangshuo end
}
definedWin.closeWindow = function(wid){
	Windows.close(wid);
	definedWin.isOpen = false;
}
definedWin.closeAllWindow = function(){
    var x = 0;
    var lag=250;
	for(var i=(definedWin.cWins.length-1);  i>=0;  i--) {
	  setTimeout('definedWin.cWins['+i+'].hide()',(x*lag));
	  x++;
	}
	definedWin.isOpen = false;
	setTimeout('definedWin.cWins.length=0; definedWin.idx=0; definedWin.cascade=0;',x*lag);
}

definedWin.modalWin = null;
definedWin.openModalWindow = function(txtName,url,curWidth,curHeight){
	definedWin.txtName = txtName;
	definedWin.setSelects("hidden");
	//modify by tongjq 2008/03/13 for set size
	definedWin.curWidth  = curWidth;
	definedWin.curHeight = curHeight;
	//modify by tongjq 2008/02/26 for set size
	if(top != self){
		top.definedWin.openModalWindow(txtName,url,curWidth,curHeight);
		top.definedWin.listObject = definedWin.listObject;	
		top.definedWin.hidden = definedWin.hidden;
		return ;
	}
	var objContent= document.getElementById(definedWin.content);
	var hei = (definedWin.curHeight ? definedWin.curHeight : definedWin.height) -2;
	var str = "";
	str += "<iframe style='width:100%;height:"+hei+"px' name='definedWin_openIframe_"+txtName+"' scrolling=yes frameborder=0 src='"+url+"' onload='definedWin.iframeOnload(this)'>";
	str += "</iframe>";
	definedWin.show();
	objContent.innerHTML = str;
	return true;
	
}
definedWin.closeModalWindow = function(){
	//top.definedWin.modalWin.hide();
	top.definedWin.beforeClose();
	definedWin.hidden();
	//modify by tongjq 2008/02/26 for set button text
	definedWin.ok = definedWin.oldOk;
	definedWin.cancel = definedWin.oldCancel;
	//modify by tongjq 2008/02/26 for set button text
	top.definedWin.selectListing = top.definedWin.oldSelectListing;
	top.definedWin.hidden = top.definedWin.oldHidden;
	top.definedWin.returnArray[0] = top.definedWin.oldReturnArray_0;
}

definedWin.openUrl = function(txtName,url,indexNum){
	//definedWin.txtName=txtName;
	definedWin.setSelects("hidden");
	if(top != self){
		var indexNum = Global.isDefinedWindow() ? 9010 : 100;
		top.definedWin.openUrl(txtName,url,indexNum);		
		return ;
	}
	definedWin.openWindow(txtName,url,indexNum);
}

definedWin.openListingUrl = function(txtName,url,no,noButton,okTxt,cancelTxt,curWidth,curHeight){
	//modify by wangshuo 2008/01/30 for multi window
	//no = no ? no : 0 ;
	if (!no) {
		no = 0;
		var num = definedWin.getModalNum(no);
		var objLightbox = document.getElementById(definedWin.lightbox+num);
		while (objLightbox && (objLightbox.style.display == 'block')) {
			no++;
			num = definedWin.getModalNum(no);
			objLightbox = document.getElementById(definedWin.lightbox+num);
		}
	}

	//modify by wangshuo 2008/01/30 for multi window
	//modify by tongjq 2008/02/26 for set button text
	definedWin.oldOk = definedWin.ok;
	if (okTxt) {
		definedWin.ok = okTxt;
	}
	definedWin.oldCancel = definedWin.cancel;
	if (cancelTxt) {
		definedWin.cancel = cancelTxt;
	}
	//modify by tongjq 2008/03/13 for set button text
	//modify by tongjq 2008/03/13 for set size
	definedWin.curWidth  = curWidth;
	definedWin.curHeight = curHeight;
	//modify by tongjq 2008/02/26 for set size
	
	top.definedWin.oldSelectListing = top.definedWin.selectListing;
	top.definedWin.oldHidden = top.definedWin.hidden;
	top.definedWin.oldReturnArray_0 = top.definedWin.returnArray[0];
	definedWin.txtName = txtName;
	definedWin.setSelects("hidden");
	if(top != self){
		top.definedWin.openListingUrl(txtName,url,no,noButton,okTxt,cancelTxt,curWidth,curHeight);
		top.definedWin.returnArray[0] = function(arr){
			definedWin.listObject(arr);
		}
		//top.definedWin.listObject = definedWin.listObject;	
		top.definedWin.hidden = definedWin.hidden;
		return ;
	}
	definedWin.show(no);
	var num = definedWin.getModalNum(definedWin.modalNum);
	if (noButton) {
		document.getElementById(definedWin.operaButton+num).style.display = "none";
	} else {
		document.getElementById(definedWin.operaButton+num).style.display = "block";
	}
	var frameName = "definedWin_openIframe_"+num;
	var oldUrl = frames[frameName] ? frames[frameName].location.href : "";
/*	if((oldUrl +"|").indexOf(url+"|")>0){
		return true;
	}
*/	
	var objContent= document.getElementById(definedWin.content+num);
	//if(objContent.innerHTML == "" || txtName != objContent.txtName){
		var hei = definedWin.curHeight ? definedWin.curHeight : definedWin.height;
		var str = "";
		str += "<iframe style='width:100%;height:"+hei+"px' name='"+frameName+"' scrolling=yes frameborder=0 src='"+url+"' onload='definedWin.iframeOnload(this)'>";
		str += "</iframe>";
		objContent.innerHTML = str;
		objContent.txtName = txtName;
	//}
	return true;
}

definedWin.setWindowTitle =function(str){
	var objTitle  = document.getElementById(definedWin.titleName);
	objTitle.innerText = str;
}

definedWin.iframeOnload = function(obj){
	if(frames[obj.name] && frames[obj.name].CurrentPage && frames[obj.name].CurrentPage.onLoadSelect){
		frames[obj.name].CurrentPage.onLoadSelect();
	}
}

definedWin.selectListingExtend = new Object();

definedWin.selectListingExtend[0] = function(){}

definedWin.selectListingButton = function() {
	try{
		definedWin.selectListing();
	}catch(e){
		definedWin.selectListingExtend[0]();
	}
}
definedWin.oldSelectListing= definedWin.selectListing;
definedWin.oldHidden = definedWin.hidden;
definedWin.selectListing = function(){
	definedWin.selectListingExtend[0]();
}
definedWin.closeListing  = function(){
	definedWin.closeModalWindow();
}

definedWin.openText = function(txt){}

definedWin.openLongTextWin = function(txtObj,hidObj,readonly,maxlength,no){
	//modify by wangshuo 2008/01/30 for multi window
	//no = no ? no : 0 ;
	top.definedWin.oldSelectListing = top.definedWin.selectListing;
	top.definedWin.oldHidden = top.definedWin.hidden;
	top.definedWin.oldReturnArray_0 = top.definedWin.returnArray[0];
	if (!no) {
		no = 0;
		var num = definedWin.getModalNum(no);
		var objLightbox = document.getElementById(definedWin.lightbox+num);
		while (objLightbox && (objLightbox.style.display == 'block')) {
			no++;
			num = definedWin.getModalNum(no);
			objLightbox = document.getElementById(definedWin.lightbox+num);
		}
	}
	//modify by wangshuo 2008/01/30 for multi window
	definedWin.setSelects("hidden");
	definedWin.txtName = txtObj.name;
	//modify by tongjq 2008/03/13 for set size
	definedWin.curWidth  = null;
	definedWin.curHeight = null;
	//modify by tongjq 2008/02/26 for set size
	if(top != self){
		top.definedWin.getMaxLength = definedWin.getMaxLength;
		top.definedWin.openLongTextWin(txtObj,hidObj,readonly,maxlength);
		top.definedWin.save = definedWin.save;
		top.definedWin.hidden = definedWin.hidden;		
		return ;
	}
	definedWin.show(no);
	var num = definedWin.getModalNum(definedWin.modalNum);
	document.getElementById(definedWin.operaButton+num).style.display = "none";	
	var objContent= document.getElementById(definedWin.content+num);
	var objTitle  = document.getElementById(definedWin.titleName+num);
	var hei = (definedWin.curHeight ? definedWin.curHeight : definedWin.height) - definedWin.buttonHeight;
	var reObj = hidObj ? hidObj : txtObj;
	var readStr = "";
	if(readonly){
		readonly = true;
		readStr = "readonly";
		objTitle.innerText = " 这里是全部的信息：";
	}else{
		readonly = false;
		objTitle.innerText = " 请在下面输入您需要的内容 ：";
	}
	var str = "<textarea style='width:100%;height:"+hei+"px'"+definedWin.getMaxLength(maxlength)+" name='definedWin.area' "+readStr+">"+txtObj.value+"</textarea>";
	str += " <input type='button' class='opera_confirm' name='' onclick='definedWin.save(\""+reObj.name+"\",document.getElementsByName(\"definedWin.area\")[0].value,"+readonly+")'/>";
	str += " <input type='button' class='opera_cancel'  name='' onclick='definedWin.closeModalWindow()'/>";
	objContent.innerHTML = str;
	objContent.txtName = txtObj.name;
	return true;
}

definedWin.getMaxLength = function(maxlength){
	if(maxlength != null){
		return " onkeydown='this.value = this.value.substring(0,"+ maxlength +");'";
	}else{return "";}
}

definedWin.openLoading = function(sec){

	var objOverlay  = document.getElementById(definedWin.overlay);
	var objLoading  = document.getElementById(definedWin.loading);
	if(!objOverlay)
		return;
	var arrayPageSize = definedWin.getPageSize();
	var arrayPageScroll = definedWin.getPageScroll();

	if (navigator.appVersion.indexOf("MSIE")!=-1){
		definedWin.pause(250);
	} 

	definedWin.isLoading = true;
	if(!definedWin.isModal){
		objOverlay.style.height = (arrayPageSize[1] + 'px');
		objOverlay.style.display = 'block';
	}

	if (navigator.appVersion.indexOf("MSIE")!=-1){
		definedWin.pause(250);
	}
	objLoading.style.display = 'block';	

	var lightboxTop = arrayPageScroll[1] + ((arrayPageSize[3] - 35 - 300) / 2);
	var lightboxLeft = ((arrayPageSize[0] - 20 - 400) / 2);
	objLoading.style.top  = (lightboxTop < 0) ? "0px" : lightboxTop + "px";
	objLoading.style.left = (lightboxLeft < 0) ? "0px" : lightboxLeft + "px";	
	try{
		frames["definedWin_frm_loading"].setBeginLoading(150,sec);
	}catch(e){}
	return true;
}

definedWin.closeLoading = function (){
	var objOverlay  = top.document.getElementById(definedWin.overlay);
	var objLoading  = top.document.getElementById(definedWin.loading);
	objLoading.style.display = 'none';
	//TODO
	if(!definedWin.isModal){
		objOverlay.style.display = 'none';
	}
	top.definedWin.isLoading = false;
	try{
		frames["definedWin_frm_loading"].clearTime();
	}catch(e){}
	return true;
}


definedWin.openImage = function(imgUrl){
	//TODO
}

definedWin.save = function(rname,rvalue,readonly){
	if(readonly != true){
		rname = document.getElementsByName(rname)[0];	
		rname.value = rvalue;
	}
	definedWin.closeModalWindow();
}


definedWin.listObject = function(arr){
	if(definedWin.returnArray[0]){
		return definedWin.returnArray[0](arr);
	}
	definedWin.beginListObject(arr);
	definedWin.returnListObject(arr);
	definedWin.beforeListObject(arr);
	definedWin.closeModalWindow();
}

definedWin.returnListObject = function(arr){
	var txtName = definedWin.txtName;
	var temp ;
	var tt;
	for(var i=0; i<arr.length; i++){
		if(definedWin.setListObjectInFor(arr[i])){
			for(var t in arr[i]) {			
				tt = t.replace(/_/gi, "."); //在列表定义field时候使用"_"代替"."
				temp = document.getElementsByName(txtName + "." + tt)[i];
				if (temp) {
					if(temp.tagName == "INPUT") {
						temp.value = arr[i][t];
						continue;
					} else {
						temp.innerText = arr[i][t];
						continue;
					}					
				}else{
					//如果声明field值，这里则可以填充多个
					var objs = document.getElementsByTagName("INPUT");
					for( n=0;n<objs.length;n++) {
						if(objs[n].field == (txtName + "." + tt)){
							objs[n].value = arr[i][t];
						}
					}
				}
			}
		}
	}
}
/**
**作者：谢伟，赵一非
**赵一非于2007-12-19修改于此
**/
definedWin.setListObjectInFor = function(arr,attribute,arrWithAtt){
	var txtName, i1, i2, index, i;
			if(typeof this.editPageIns!='object')
			{
			    return true;
			}
			txtName = definedWin.txtName;
			
			i1 = txtName.indexOf("["); 
			i2 = txtName.indexOf("]");
			index = txtName.substring(i1+1, i2);
			i = parseInt(index);
			txtNamebef = txtName.substring(0, i1);
			txtNameafter=txtName.substring(i2+1,txtName.length);
			if(i==-1)
			{
			i=this.editPageIns.rowNumber;
			txtName = txtNamebef + "[" + (i)+ "]"+txtNameafter;
			}
			var td = document.getElementById(txtName + "." + "id");
			if (td == 'unfined' || td == null) this.editPageIns.addRowIns();

			for(var t in arr) {			
				tt = t.replace(/_/gi, "."); //在列表定义field时候使用"_"代替"."
				temp = document.getElementById(txtName + "." + tt);
				if (temp) {
					if(temp.tagName == "INPUT") {
						temp.value = arr[t];
						continue;
					} else {
						temp.innerText = arr[t];
						continue;
					}					
				}else{
					//如果声明field值，这里则可以填充多个
					var objs = document.getElementsByTagName("INPUT");
					for( n=0;n<objs.length;n++) {
						if(objs[n].field == (txtName + "." + tt)){
							objs[n].value = arr[t];
						}
					}
				}
			}
			
			// 设置下一个控件名
			definedWin.txtName = txtNamebef + "[" + (i+1)+ "]"+txtNameafter;
}
/********************************************/
definedWin.beginListObject = function(arr){}
definedWin.beforeListObject = function(arr){}
definedWin.beforeClose = function(){}

definedWin.getModalNum = function(num){
	return "["+num+"]";
}


definedWin.show = function(no){
	no = no ? no : 0 ;
	var arrayPageSize = definedWin.getPageSize();
	var arrayPageScroll = definedWin.getPageScroll();
	definedWin.modalNum = no
	definedWin.isModal = true;
	var objOverlay  = document.getElementById(definedWin.overlay);
	objOverlay.style.height = (arrayPageSize[1] + 'px');
	objOverlay.style.display = 'block';    
	objOverlay.style.height = (arrayPageSize[1] + 'px');	

	var num = definedWin.getModalNum(no);
	var objLightbox = document.getElementById(definedWin.lightbox+num);
	if(!objLightbox){
		var tempObj = document.getElementById(definedWin.lightbox+definedWin.getModalNum(0));
		var str = tempObj.innerHTML.replace(/\[0\]/gi,num);
		str = str.replace(/<iframe.*>.*<\/iframe>/gi,"");
		var newObj = document.createElement("div");
		newObj.innerHTML = str;
		newObj.id = definedWin.lightbox+num;
		newObj.className = definedWin.lightbox;
		newObj.zIndex = parseFloat(newObj.zIndex,10)+5;
		newObj.style.zIndex = newObj.zIndex;
		tempObj.parentNode.appendChild(newObj);
	}else{
		//if(objLightbox.txtName == definedWin.txtName){
		//	objLightbox.style.display = 'block';
		//	return;
		//}
	}
	objLightbox = document.getElementById(definedWin.lightbox+num);
	//modify by tongjq 2008/02/26 for set button text
	var objButtons = document.getElementById(definedWin.operaButton+num).getElementsByTagName("INPUT");
	for (i = 0; i < objButtons.length; i++) {
		if (objButtons[i].name == "opera_ok") {
			objButtons[i].value = definedWin.ok;
		} else if (objButtons[i].name == "opera_cancel") {
			objButtons[i].value = definedWin.cancel;
		}
	}
	//modify by tongjq 2008/02/26 for set button text
	var objContent  = document.getElementById(definedWin.content+num);
	if (navigator.appVersion.indexOf("MSIE")!=-1){
		definedWin.pause(250);
	} 
	definedWin.setSelects("hidden");
	if (navigator.appVersion.indexOf("MSIE")!=-1){
		definedWin.pause(250);
	}
	objContent.style.height = (definedWin.curHeight ? definedWin.curHeight : definedWin.height) + 'px';
	objContent.style.width  = (definedWin.curWidth ? definedWin.curWidth : definedWin.width)  + 'px';
	objLightbox.childNodes[0].childNodes[0].width = (definedWin.curWidth ? definedWin.curWidth : definedWin.width) - 4;
	objLightbox.style.display = 'block';	
	var ChangeSpace =  20;
	var lightboxTop = arrayPageScroll[1] + ((arrayPageSize[3] - 35 - objContent.clientHeight) / 2);
	var lightboxLeft = ((arrayPageSize[0] - 20 - objContent.clientWidth) / 2);
	lightboxTop += ChangeSpace;
	lightboxLeft+= ChangeSpace;
	objLightbox.style.top  = (lightboxTop < 0) ? "0px" : lightboxTop + "px";
	objLightbox.style.left = (lightboxLeft < 0) ? "0px" : lightboxLeft + "px";
	//arrayPageSize = definedWin.getPageSize();
	return false;	
}

definedWin.hidden = function (){
	if(top.definedWin.modalNum == 0){
		objOverlay = top.document.getElementById(definedWin.overlay);
		objOverlay.style.display = 'none';
		top.definedWin.isModal = false;
	}
	var num = definedWin.getModalNum(top.definedWin.modalNum);
	// get objects	
	objLightbox = top.document.getElementById(definedWin.lightbox+num);
	if(objLightbox){
	
		objLightbox.style.display = 'none';
		top.definedWin.modalNum --;
		definedWin.setSelects("visible");
	}
}


definedWin.setSelects = function(flag, doc){
//2008-02-28 changed by wangshuo start 遍历各层select
	if (top.definedWin.modalNum > -1) {
		return;	
	}
	var docObj;
	if (doc) {
		docObj = doc;
	} else {
		docObj = top.document;
	}
	
	var selects = docObj.getElementsByTagName("select");
    for (var i = 0; i < selects.length; i++) {
		selects[i].style.visibility = flag;
	}
	
	var frames = docObj.frames;
	for (var i = 0; i < frames.length; i++) {
		definedWin.setSelects(flag, frames(i).document);
	}
/*	
	var selects = document.getElementsByTagName("select");
    for (i = 0; i != selects.length; i++) {
		selects[i].style.visibility = flag;
	}*/
//2008-02-28 changed by wangshuo end 遍历各层select
}


definedWin.getPageScroll = function (){
	var yScroll;
	if (self.pageYOffset) {
		yScroll = self.pageYOffset;
	} else if (document.documentElement && document.documentElement.scrollTop){	 // Explorer 6 Strict
		yScroll = document.documentElement.scrollTop;
	} else if (document.body) {// all other Explorers
		yScroll = document.body.scrollTop;
	}

	arrayPageScroll = new Array('',yScroll) 
	return arrayPageScroll;
}

definedWin.getPageSize = function(){
	
	var xScroll, yScroll;
	
	if (window.innerHeight && window.scrollMaxY) {	
		xScroll = document.body.scrollWidth;
		yScroll = window.innerHeight + window.scrollMaxY;
	} else if (document.body.scrollHeight > document.body.offsetHeight){ // all but Explorer Mac
		xScroll = document.body.scrollWidth;
		yScroll = document.body.scrollHeight;
	} else { // Explorer Mac...would also work in Explorer 6 Strict, Mozilla and Safari
		xScroll = document.body.offsetWidth;
		yScroll = document.body.offsetHeight;
	}
	
	var windowWidth, windowHeight;
	if (self.innerHeight) {	// all except Explorer
		windowWidth = self.innerWidth;
		windowHeight = self.innerHeight;
	} else if (document.documentElement && document.documentElement.clientHeight) { // Explorer 6 Strict Mode
		windowWidth = document.documentElement.clientWidth;
		windowHeight = document.documentElement.clientHeight;
	} else if (document.body) { // other Explorers
		windowWidth = document.body.clientWidth;
		windowHeight = document.body.clientHeight;
	}	
	
	// for small pages with total height less then height of the viewport
	if(yScroll < windowHeight){
		pageHeight = windowHeight;
	} else { 
		pageHeight = yScroll;
	}

	// for small pages with total width less then width of the viewport
	if(xScroll < windowWidth){	
		pageWidth = windowWidth;
	} else {
		pageWidth = xScroll;
	}


	arrayPageSize = new Array(pageWidth,pageHeight,windowWidth,windowHeight) 
	return arrayPageSize;
}


//
// pause(numberMillis)
// Pauses code execution for specified time. Uses busy code, not good.
// Code from http://www.faqts.com/knowledge_base/view.phtml/aid/1602
//
definedWin.pause = function (numberMillis) {

	var now = new Date();
	var exitTime = now.getTime() + numberMillis;
	while (true) {
		now = new Date();
		if (now.getTime() > exitTime)
			return;
	}
}



//
// addLoadEvent()
// Adds event to window.onload without overwriting currently assigned onload functions.
// Function found at Simon Willison's weblog - http://simon.incutio.com/
//
function addLoadEvent(func)
{	
	var oldonload = window.onload;
	if (typeof window.onload != 'function'){
    	window.onload = func;
	} else {
		window.onload = function(){
		oldonload();
		func();
		}
	}

}

//addLoadEvent(initLightbox);	// run initLightbox onLoad

var DivUtil = {};
DivUtil.left ;
DivUtil.top  ;

DivUtil.dragMouseMove = function(obj,event){
	event = event ? event : (window.event ? window.event : null);
	var mouseX = event.x ? event.x : event.pageX;
	var mouseY = event.y ? event.y : event.pageY;
	if(event.button==1){
		obj= obj.parentNode;
		var caoX=obj.clientLeft;
		var caoY=obj.clientTop;
		var left = caoX  + (mouseX - DivUtil.left);
		var top = caoY  + (mouseY - DivUtil.top);
		
		left = left < 0 ? 0 : left
		var documentWidth = document.body.offsetWidth - obj.offsetWidth;
		left = left > documentWidth ? documentWidth : left;
		
		top = top < document.body.clientTop ? 0 : top
		var documentHeight = document.body.offsetHeight - obj.offsetHeight;
		top = top > documentHeight ? documentHeight : top;
		
		obj.style.pixelLeft = left;
		obj.style.pixelTop = top;
	}
}
DivUtil.dragMouseDown = function(obj,event){
	event = event ? event : (window.event ? window.event : null);
	var mouseX = event.x ? event.x : event.pageX;
	var mouseY = event.y ? event.y : event.pageY;
	DivUtil.left= mouseX - obj.parentNode.style.pixelLeft;
	DivUtil.top = mouseY - obj.parentNode.style.pixelTop;
}
