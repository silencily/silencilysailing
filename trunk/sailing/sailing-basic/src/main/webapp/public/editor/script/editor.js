

/**
 * 简洁的多媒体编辑器对象
 * 2006-10-25
 * liuz
 */

var Editor = {};
Editor.iframeEditObj = null;
Editor.docEditObj    = null;

var gSetColorType = ""; 
var gIsIE = document.all; 
var ev = null;
function fGetEv(e){
	ev = e;
}

/* 初始化编辑内容
 * 这里约定 父页面是名为 content 来存放内容
 * TODO 加入无边框的css，在发布时候过滤
 */
Editor.setContent = function(thtml){
	if(!thtml){
		var initStr = "";
		initStr += '\
			<html> \
				<head> \
				<title></title>\
				<meta content="text/html; charset=GBK" http-equiv="content-type">\
				<link rel="stylesheet" href="css/editor.css" type="text/css" media="all">\
				</head>\
				<body>';				
		if(parent.document.getElementById("content"))
			initStr += parent.document.getElementById("content").value;
		initStr += '</body></html>';
		thtml = initStr;
	}	
	Editor.iframeEditObj.document.open();
	Editor.iframeEditObj.document.write(thtml);
	Editor.iframeEditObj.document.close();
	//Editor.iframeEditObj.document.body.innerHTML = thtml;
}

/* 获取到内容，html格式代码
 * 这里已经过滤掉上传的文件路径：
 */
Editor.getContent = function(){
	var thtml = Editor.iframeEditObj.document.body.innerHTML;
	return thtml;
}

/* 在divObj位置生成上传文件的file域
 * 同名的已经过滤
 */
Editor.getUploadFile = function(divObj){
	
}

//得到IE版本
Editor.getIEVer = function(){
	var iVerNo = 0;
	var sVer = navigator.userAgent;
	if(sVer.indexOf("MSIE")>-1){
		var sVerNo = sVer.split(";")[1];
		sVerNo = sVerNo.replace("MSIE","");
		iVerNo = parseFloat(sVerNo);
	}
	return iVerNo;
}

Editor.setEditAble = function(){
	var f = Editor.iframeEditObj;
	f.document.designMode="on";
	if(!gIsIE)
		f.document.execCommand("useCSS",false, true);
}
Editor.onSetFrameClick = function(){
	var f = Editor.iframeEditObj;
	f.document.onmousemove = function(){
		window.onblur();
	}
	f.document.onclick = function(){
		fHideMenu();
	}
	f.document.onkeydown = function(){
		//top.frames["jsFrame"].gIsEdited = true;
	}	
	f.document.ondblclick = function(){
		if(f.document.selection.createRange()){			
			if(f.document.selection.createRange().item(0).tagName == "IMG"){
				createImg();
			}else if(f.document.selection.createRange().item(0).tagName == "TABLE"){
				createTable();
			}
		}
	}
}

function fSetColor(){
	var dvForeColor =$("dvForeColor");
	if(dvForeColor.getElementsByTagName("TABLE").length == 1){
		dvForeColor.innerHTML = drawCube() + dvForeColor.innerHTML;
	}
}
window.onblur =function(){
	return;
	var dvForeColor =$("dvForeColor");
	var dvPortrait =$("dvPortrait");
	dvForeColor.style.display = "none";
	dvPortrait.style.display = "none";
	if(!gIsIE || 1==1){
		fHideMenu();
	}
}
document.onmousemove = function(e){
	if(gIsIE) var el = event.srcElement;
	else var el = e.target;
	var tdView = $("tdView");
	var tdColorCode = $("tdColorCode");
	var dvForeColor =$("dvForeColor");
	var dvPortrait =$("dvPortrait");
	var fontsize =$("fontsize");
	var fontface =$("fontface");
//	if(el.tagName == "IMG"){
//		el.style.borderRight="1px #cccccc solid";
//		el.style.borderBottom="1px #cccccc solid";
//	}else{
//		fSetImgBorder();
//	}
	if(el.tagName == "IMG"){
		try{
			if(fCheckIfColorBoard(el)){
				tdView.bgColor = el.parentNode.bgColor;
				tdColorCode.innerHTML = el.parentNode.bgColor
			}
		}catch(e){}
	}else{
		return;
		dvForeColor.style.display = "none";
		if(!fCheckIfPortraitBoard(el)) dvPortrait.style.display = "none";
		if(!fCheckIfFontFace(el)) fontface.style.display = "none";
		if(!fCheckIfFontSize(el)) fontsize.style.display = "none";
	}
}
document.onclick = function(e){
	if(gIsIE) var el = event.srcElement;
	else var el = e.target;
	var dvForeColor =$("dvForeColor");
	var dvPortrait =$("dvPortrait");

	if(el.tagName == "IMG"){
		try{
			if(fCheckIfColorBoard(el)){
				format(gSetColorType, el.parentNode.bgColor);
				dvForeColor.style.display = "none";
				return;
			}
		}catch(e){}
		try{
			if(fCheckIfPortraitBoard(el)){
				format("InsertImage", el.src);
				dvPortrait.style.display = "none";
				return;
			}
		}catch(e){}
	}
	fHideMenu();
	switch(el.id){
		case "imgFontface":
			var fontface = $("fontface");
			if(fontface) fontface.style.display = "";
			break;
		case "imgFontsize":
			var fontsize = $("fontsize");
			if(fontsize) fontsize.style.display = "";
			break;
		case "imgFontColor":
			var dvForeColor = $("dvForeColor");
			if(dvForeColor) dvForeColor.style.display = "";
			break;
		case "imgBackColor":
			var dvForeColor = $("dvForeColor");
			if(dvForeColor) dvForeColor.style.display = "";
			break;
		case "imgFace":
			var dvPortrait = $("dvPortrait");
			if(dvPortrait) dvPortrait.style.display = "";
			break;
		case "imgAlign":
			var divAlign = $("divAlign");
			if(divAlign) divAlign.style.display = "";
			break;
		case "imgList":
			var divList = $("divList");
			if(divList) divList.style.display = "";
			break;
	}
}
function format(type, para){
	var f = Editor.iframeEditObj;
	var sAlert = "";
	if(!gIsIE){
		switch(type){
			case "Cut":
				sAlert = "你的浏览器安全设置不允许编辑器自动执行剪切操作,请使用键盘快捷键(Ctrl+X)来完成";
				break;
			case "Copy":
				sAlert = "你的浏览器安全设置不允许编辑器自动执行拷贝操作,请使用键盘快捷键(Ctrl+C)来完成";
				break;
			case "Paste":
				sAlert = "你的浏览器安全设置不允许编辑器自动执行粘贴操作,请使用键盘快捷键(Ctrl+V)来完成";
				break;
		}
	}
	if(sAlert != ""){
		alert(sAlert);
		return;
	}
	f.focus();
	if(!para)
		if(gIsIE)
			f.document.execCommand(type)
		else
			f.document.execCommand(type,false,false)
	else
		f.document.execCommand(type,false,para)
	f.focus();
}
//设置模式
function setMode(bStatus){
	var sourceEditor = $("sourceEditor");
	var HtmlEditor = $("HtmlEditor");
	var divEditor = $("divEditor");
	var f = Editor.iframeEditObj;
	var body = f.document.getElementsByTagName("BODY")[0];
	if(bStatus){
		sourceEditor.style.display = "";
		HtmlEditor.style.height = "0px";
		divEditor.style.height = "0px";
		sourceEditor.value = body.innerHTML;
	}else{
		sourceEditor.style.display = "none";
		if(gIsIE){
			//HtmlEditor.style.height = "286px";
			divEditor.style.height = "286px";
		}else{
			//HtmlEditor.style.height = "283px";
			divEditor.style.height = "283px";
		}
		body.innerHTML = sourceEditor.value;
		//Editor.setEditAble();
	}
}
function foreColor(e) {
	fDisplayColorBoard(e);
	gSetColorType = "foreColor";
	/*var sColor = fDisplayColorBoard(e);
	gSetColorType = "foreColor";
	if(gIsIE) format(gSetColorType, sColor);*/
}
function backColor(e){
	var sColor = fDisplayColorBoard(e);
	if(gIsIE)
		gSetColorType = "backcolor";
	else
		gSetColorType = "backcolor";
	// if(gIsIE) format(gSetColorType, sColor);
}
function fDisplayColorBoard(e){
	if(gIsIE){
		var e = window.event;
	}
	if(Editor.getIEVer()<=5.01 && gIsIE){
		//var arr = showModalDialog("ColorSelect.htm", "", "font-family:Verdana; font-size:12; status:no; dialogWidth:21em; dialogHeight:21em");
		if (arr != null) return arr;
		return;
	}
	var dvForeColor =$("dvForeColor");
	fSetColor();
	var iX = e.clientX;
	var iY = e.clientY;
	dvForeColor.style.display = "";
	dvForeColor.style.left = (iX-60) + "px";
	dvForeColor.style.top = 33 + "px";
	return true;
}
function createLink() {
	var sURL=window.prompt("请输入连接的网址（例如. http://www.yndde.com/）：", "http://");
	if ((sURL!=null) && (sURL!="http://")){
		format("CreateLink", sURL);
	}
}
//插入图片
function createImg(){
	var url = showModalDialog("script/insert_img.htm",window, 
		"font-family:Verdana; font-size:12; status:no; unadorned:yes; scroll:no; resizable:false;dialogWidth:600px; dialogHeight:430px");
	if(url){
		format("InsertImage", url);
	}
	/* 使用definedWin
	var winUrl = ContextInfo.contextPath + '/public/editor/script/insert_img.htm';
	var txtName = Editor.iframeEditObj.document.selection;
	definedWin.openModalWindow(txtName,winUrl);
	top.definedWin.returnOpenTreeValue = function(url){
		format("InsertImage", url);
	}
	*/
}
//上传图片时候预览图片
Editor.previewImg = function(obj){
	var imgArr = ["gif","bmp","jpg","jpeg","png"]
	var imgUrl = obj.value.toLowerCase();
	if(obj.fileName){
		//将原先的fileName删除
		//frames["frame_editor"].setPreviewImageBlank(obj.fileName);
	}
	for(var i=0;i<imgArr.length;i++){
		if(imgUrl.lastIndexOf("."+imgArr[i]) > 0 ){
			obj.fileName = imgUrl.substring(imgUrl.lastIndexOf("/")+1,imgUrl.length);
			format("InsertImage", obj.value);
			break;
		}
	}
}
//因为删除了本地图片，将编辑器中对应预览图片置空
function setPreviewImageBlank(fileName){
	if(fileName.indexOf("\\")>0)
		fileName = fileName.substring(fileName.lastIndexOf("\\")+1,fileName.length);
	var content = Editor.getContent();
	content = content.replace(fileName,"@delete@");
	var regex1 = /<img[^>]*src=\"[A-Z]{1}:.*\\@delete@[^>]*>/gi;
	content = content.replace(regex1,"");
	var regex2 = /<img[^>]*src=\"[^\"]*@delete@[^>]*>/gi;
	content = content.replace(regex2,"");	
	Editor.setContent(content);
}
//替换成新的图片路径：用于上传图片时候将路径转化为服务器路径
//@param newImgUrl 替换后的图片路径
Editor.getContentAfterFilterImageUrl = function (newImgUrl){
	content = Editor.getContent();
	var regex0 = /:\\[^\"\>]*\\/gi;
	content = content.replace(regex0,":\\");
	var regex2 = /\"[A-Z]{1}:\\/gi;
	content = content.replace(regex2,newImgUrl);
	var regex3 = /\"file:\/\/\/[A-Z]{1}:[^\"]*\//gi;
	content = content.replace(regex3,newImgUrl);
	return content;
}

//插入表格
function createTable(){
	var tabStr = showModalDialog("script/insert_table.htm",window, 
		"font-family:Verdana; font-size:12; status:no; unadorned:yes; scroll:no; resizable:false;dialogWidth:400px; dialogHeight:250px");
	if(tabStr){
		Editor.insertHTML(tabStr);
	}
}
//插入代码（表格等）
Editor.insertHTML = function(str){
	Editor.iframeEditObj.focus();
	with(eval("Editor.iframeEditObj.document.selection")){
		clear()
		createRange().pasteHTML(str);
	}
	Editor.iframeEditObj.focus();
}

//没有使用
function addPortrait(e){
	if(Editor.getIEVer()<=5.01 && gIsIE || 1==1){
		// var imgurl = showModalDialog("portraitSelect.htm","", "font-family:Verdana; font-size:12; status:no; unadorned:yes; scroll:no; resizable:yes;dialogWidth:358px; dialogHeight:232px");
		// if (imgurl != null)	format("InsertImage", imgurl);
		var dvPortrait =$("dvPortrait");
		if(dvPortrait){
			dvPortrait.parentNode.removeChild(dvPortrait);
		}
		var div = document.createElement("DIV");
		div.style.position = "absolute";
		div.style.zIndex = "9";
		div.id = "dvPortrait";
		var iX = e.clientX;
		div.style.top = 33 + "px";
		div.style.left = (iX-380) + "px";
		div.innerHTML = '<iframe border=0 marginWidth=0 marginHeight=0 frameBorder=no style="width:358px;height:232px" src="portraitSelect.htm">';
		document.body.appendChild(div);
		var dvPortrait = $("dvPortrait");
		dvPortrait.style.display = "";
		return;
	}
	var dvPortrait =$("dvPortrait");
	var tbPortrait = $("tbPortrait");
	var iX = e.clientX;
	var iY = e.clientY;
	dvPortrait.style.display = "";
	if(window.screen.width == 1024){
		dvPortrait.style.left = (iX-380) + "px";
	}else{
		if(gIsIE)
			dvPortrait.style.left = (iX-380) + "px";
		else
			dvPortrait.style.left = (iX-380) + "px";
	}
	dvPortrait.style.top = 33 + "px";
	dvPortrait.innerHTML = '<table width="100%" border="0" cellpadding="5" cellspacing="1" style="cursor:hand" bgcolor="black" ID="tbPortrait"><tr align="left" bgcolor="#f8f8f8" class="unnamed1" align="center" ID="trContent">'+ drawPortrats() +'</tr>	</table>';
}
function fCheckIfColorBoard(obj){
	if(obj.parentNode){
		if(obj.parentNode.id == "dvForeColor") return true;
		else return fCheckIfColorBoard(obj.parentNode);
	}else{
		return false;
	}
}
function fCheckIfPortraitBoard(obj){
	if(obj.parentNode){
		if(obj.parentNode.id == "dvPortrait") return true;
		else return fCheckIfPortraitBoard(obj.parentNode);
	}else{
		return false;
	}
}
function fCheckIfFontFace(obj){
	if(obj.parentNode){
		if(obj.parentNode.id == "fontface") return true;
		else return fCheckIfFontFace(obj.parentNode);
	}else{
		return false;
	}
}
function fCheckIfFontSize(obj){
	if(obj.parentNode){
		if(obj.parentNode.id == "fontsize") return true;
		else return fCheckIfFontSize(obj.parentNode);
	}else{
		return false;
	}
}
function fImgOver(el){
	if(el.tagName == "IMG"){
		el.style.borderRight="1px #cccccc solid";
		el.style.borderBottom="1px #cccccc solid";
	}
}
function fImgMoveOut(el){
	if(el.tagName == "IMG"){
		el.style.borderRight="1px #F3F8FC solid";
		el.style.borderBottom="1px #F3F8FC solid";
	}
}
String.prototype.trim = function(){
	return this.replace(/(^\s*)|(\s*$)/g, "");
}
function fSetBorderMouseOver(obj) {
	obj.style.borderRight="1px solid #aaa";
	obj.style.borderBottom="1px solid #aaa";
	obj.style.borderTop="1px solid #fff";
	obj.style.borderLeft="1px solid #fff";
	/*var sd = document.getElementsByTagName("div");
	for(i=0;i<sd.length;i++) {
		sd[i].style.display = "none";
	}*/
} 

function fSetBorderMouseOut(obj) {
	obj.style.border="none";
}

function fSetBorderMouseDown(obj) {
	obj.style.borderRight="1px #F3F8FC solid";
	obj.style.borderBottom="1px #F3F8FC solid";
	obj.style.borderTop="1px #cccccc solid";
	obj.style.borderLeft="1px #cccccc solid";
}

function fDisplayElement(element,displayValue) {
	if(Editor.getIEVer()<=5.01 && gIsIE){
		//不支持IE5
		return;
	}
	fHideMenu();
	if ( typeof element == "string" )
		element = $(element);
	if (element == null) return;
	element.style.display = displayValue;
	if(gIsIE){
		var e = event;
	}else{
		var e = ev;
	}
	var iX = e.clientX;
	var iY = e.clientY;
	element.style.display = "";
	element.style.left = (iX-30) + "px";
	element.style.top = 33 + "px";
	return true;
}
function fSetModeTip(obj){
	var x = f_GetX(obj);
	var y = f_GetY(obj);
	var dvModeTip = $("dvModeTip");
	if(!dvModeTip){
		var dv = document.createElement("DIV");
		dv.style.position = "absolute";
		dv.style.top = 33 + "px";
		dv.style.left = (x-40) + "px";
		dv.style.zIndex = "999";
		dv.style.fontSize = "12px";
		dv.id = "dvModeTip";
		dv.style.padding = "2 2 0 2px";
		dv.style.border = "1px #000000 solid";
		dv.style.backgroundColor = "#FFFFCC";
		dv.style.height = "12px";
		dv.innerHTML = "编辑源码";
		document.body.appendChild(dv);
	}else{
		dvModeTip.style.display = "";
	}
}
function fHideTip(){
	$("dvModeTip").style.display = "none";
}
function f_GetX(e)
{
	var l=e.offsetLeft;
	while(e=e.offsetParent){				
		l+=e.offsetLeft;
	}
	return l;
}
function f_GetY(e)
{
	var t=e.offsetTop;
	while(e=e.offsetParent){
		t+=e.offsetTop;
	}
	return t;
}
function fHideMenu(){
	var fontface    = $("fontface");
	var fontsize    = $("fontsize");
	var dvForeColor = $("dvForeColor");
	var dvPortrait  = $("dvPortrait");
	var divAlign    = $("divAlign");
	var divList     = $("divList");
	if(dvForeColor) dvForeColor.style.display = "none";
	if(dvPortrait) dvPortrait.style.display   = "none";
	if(fontface) fontface.style.display = "none";
	if(fontsize) fontsize.style.display = "none";
	if(divAlign) divAlign.style.display = "none";
	if(divList) divList.style.display   = "none";
}
function $(id){
	return document.getElementById(id);
}
window.onerror = function(){
	return true;
}
function fGetList(){

}
function fGetAlign(){
	
}
function fHide(obj){
	obj.style.display="none";
}


//页面初始化
window.onload = function(){
	//try{
		if(parent.document.getElementsByName("frame_editor")[0])
			$('tdId_htmlEditor').height = parent.document.getElementsByName("frame_editor")[0].height - 40;
		Editor.iframeEditObj = window.frames["HtmlEditor"];
		//Editor.docEditObj    = document.getElementsByName("HtmlEditor")[0];
		Editor.iframeEditObj.focus();
		Editor.setEditAble();
		Editor.setContent();
		Editor.onSetFrameClick();				
	//}catch(e){}
}
function confirmSave(){
	if (confirm("是否放弃编辑操作?")) 
		return(true);
	else{ 
		return(false);
	}
}
//window.onunload = confirmSave;

//调节表格高度
Editor.dragHeight = function(tabid,num,evt){
	num = num ? num : 0;
	tabid = tabid.previousSibling ? tabid.previousSibling : tabid;
	evt = evt ? evt : (window.event ? window.event : null); 
	//将宽度调节为0时候有错误
	//try{
		var mX = evt.x ? evt.x : evt.pageX;
		if(evt.button ==1){
			var magin = (num + '' == 'undefined')?0:num;
			var xx = (mX - magin < 0)?0: mX - magin;
			tabid.style.height = xx;
			parent.Global.setHeight();
		}
	//}catch(e){}
}