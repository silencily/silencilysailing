/**
 * 样式控制 javascript 引擎
 * @author java2enterprise
 * @author pillarliu
 * @version $Id: styleControl.js,v 1.1 2010/12/10 10:56:35 silencily Exp $
 */

if (StyleControl == null) {
	var StyleControl = {};
}

/**
 * 设置 IFrame 高度
 * @param parentIFrameId 父窗口 IFrame Id
 */
StyleControl.setIFrameHeight =  function(parentIFrameId) {
	if (parent.document.getElementById(parentIFrameId)) {
		parent.document.getElementById(parentIFrameId).style.height = document.body.scrollHeight + "px";
	}
}

/*	
 * 操作表格： 显示&隐藏表格id=sid效果，同时也切换图片id=imgbtn：
 * @param sid  操作对象ID
 * @param img  操作图片ID
 * @param flag 窗口大小，默认60%，如果为0则隐藏下框架，如果为100%则隐藏上框架
 */
StyleControl.switchTable = function(sid, imgbtn, flag) {
	if(imgbtn != '') {
		var urlpath = ContextInfo.contextPath + "/";
	}
		
	var subment = document.getElementById(sid);
	var iflag = 0;
 	
 	if (flag == '') {
  		if(subment.style.display == "none") {
  			iflag=1;
  		}
  		else
  		{
  			iflag=0;	
  		}		
	}
	else if (flag == 'none')
	{
		iflag=0;
	}
	else if (flag == 'display')	
	{
		iflag=1;
	}
		
	if (iflag != 0 )
	{
		document.getElementById(sid).style.display = "";
     	if (imgbtn && document.getElementById(imgbtn))
     	{
     		document.getElementById(imgbtn).src = urlpath + 'image/tree/menu/pic_open.gif';
     	} 		
	} 
	else 
	{
		document.getElementById(sid).style.display = "none";
     	if (imgbtn && document.getElementById(imgbtn)) 
     	{
     		document.getElementById(imgbtn).src = urlpath + 'image/tree/menu/pic_close.gif';
     	}
	}
	Global.setHeight();
}

StyleControl.switchDiv = function(selObj,divObj){
	var css = selObj.className;
	if( css.indexOf("switch_open") >= 0){
		css = css.replace("switch_open","switch_close");
		divObj.style.display = "none";
	}else{
		css = css.replace("switch_close","switch_open");
		divObj.style.display = "";
	}
	selObj.className = css;
	Global.setHeight();
}

StyleControl.switchDivListing = function(selObj,divObj){
	var css = selObj.className;
	if( css.indexOf("switch_open1") >= 0){
		css = css.replace("switch_open1","switch_close1");
		divObj.style.display = "none";
	}else{
		css = css.replace("switch_close1","switch_open1");
		divObj.style.display = "";
	}
	selObj.className = css;
	Global.setHeight();
}


/**
 * 操作窗口：：打开一个新Frame窗口, Frame中有关闭按钮
 * @param url  		要打开的地址
 * @param winName   窗口名
 * @param wwidth	宽 默认为屏幕宽1/2
 * @param wheight 	高 默认为屏幕高1/2
 */
StyleControl.openWindowInFrame = function(url,winName,wwidth, wheight)
{
	winName = winName ? winName : "_blank";
	wwidth = wwidth ? wwidth : window.screen.width /2;
	wheight= wheight ? wheight : window.screen.height/2;	
	//var frameUrl = "/" + ContextInfo.contextPath + '/application/pages/select_frame.jsp?url=' + escape(url);	
	window.open(url,winName, 'height='+ wheight +', width='+ wwidth +', menubar=no, status=no, toolbar=no,scrollbars=auto, location=no,center=1, resizable=1');	
	return winName;
}

/**
 * 操作窗口：：在一个模态窗口中打开公用Frame页面
 * @param url  		要打开的地址
 * @param wwidth		宽度, 如果不填, 默认为700
 * @param wheight 	高度, 如果不填, 默认为460
 * @return 			对话框返回值 
 */
StyleControl.openDialogInFrame = function(url, wwidth, wheight)
{
	if(!url) {
		return;
	}
	wwidth = wwidth ? wwidth : window.screen.width /2;
	wheight= wheight ? wheight : window.screen.height/2;	
	//var frameUrl = "/" + ContextInfo.contextPath + '/application/pages/select_frame.jsp';
	var str = showModalDialog(url, window, 'dialogWidth:' + wwidth +'px;dialogHeight:' + wheight + 'px;status:no;scroll:auto;help:no;')
	return str;
}


/** 弹出显示 textarea 编辑框   
 *  txt  输入框:obj
 *  btn  按钮：obj *可不用
 *  col  宽度：int
 *  row  高度：int
 *  hidd 隐藏域:obj
 */
 /*
StyleControl.setTextareaContext = function(txt, btn, hidd, col, row)
{	
	var str = '';
	var dis =  txt.value;
	var flag = false;
	col = (col)?col:200;
	row = (row)?row:200;
	var url = ContextInfo.contextPath + "/";	 
	var txtname = (txt.id)?txt.id:txt.name;
	ExtendCombo.txtObj = txt;
	ExtendCombo.btnObj = btn;
	var hidname = '';
	if(hidd){
		hidname = (hidd.id)?hidd.id:hidd.name;
		flag = true;
		if(dis)
			dis = hidd.value;
	}
	str += '<html><head><title>Textarea</title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=GBK\"><link href=\"'+url+'css/style.css\" rel=\"stylesheet\" type=\"text/css\">';
	str += '<style>body{margin-left:1px;margin-top:0px;margin-right:1px;margin-bottom:0px}</style>';
		str += '<scr'+'ipt>';
	str += 'function getElementRight(e){ ';  
	str += '	var r =e.createTextRange();  ';
	str += '	r.moveStart(\'character\',e.value.length);  ';
	str += '	r.collapse(true);  ';
	str += '	r.select();  ';
	str += '}';
	str += '</scr'+'ipt>';
	str += '<body style="overflow:hidden" oncontextmenu="self.event.returnValue=false" onload="if(parent.document.getElementById(\'displaySelectLayer\').style.display==\'\')getElementRight(txt_context)">';
	str += '<table width="100%" title="请在这里输入文本内容" border=0 bgcolor="pageLineColor" cellSpacing=1 cellPadding=0 id="tableId_selectList"><tr><td bgcolor="pageClickColor">';
	str += '<textarea name="txt_context" style="width:'+parseInt(col-3)+'px;height:'+row+'px;border:0px;overflow:hidden;overflow-y:auto" wrap="VIRTUAL" ';
	str += ' onkeyup="text_len.innerText=this.value.length;parent.getTextareaValue(txt_context.value,'+flag+',\''+txtname+'\',\''+hidname+'\')">'+dis+'</textarea></td></tr>';
	str += '<tr><td height="22" bgcolor="pageMainColor">';
	str += '<table width="100%"><tr><td width="50%">字数:<span id="text_len">'+dis.length+'</span></td><td align="right">';
	str += '<a href="#" onclick="parent.getTextareaValue(txt_context.value,'+flag+',\''+txtname+'\',\''+hidname+'\');parent.document.getElementById(\'displaySelectLayer\').style.display=\'none\'">确定</a> ';
	str += '<a href="#" onclick="parent.getTextareaValue(\'\','+flag+',\''+txtname+'\',\''+hidname+'\');txt_context.value=\'\';return false">重置</a> &nbsp; </td></tr><table>';
	str += '</td></tr></table></body></html>';
	StyleControl.displaySelectMenu(str,txt,btn,col,row+27,'',true)
}
*/

/**
 * 替换实现为弹出大输入框
 */
StyleControl.setTextareaContext = function(textObject) {
	definedWin.openLongTextWin(textObject);
}

/** 公用显示下拉菜单方法
 *  tabString			显示的内容(string)
 *  temp_obj			输入控件对象，用于定位 
 *  temp_btn			按钮对象
 *  local_page_width    显示宽度
 *  local_page_height   显示高度
 *  isPageCount			根据是否有滚动条而设置高度、宽度	
 *  flag 是否不采用creatpop
 */
StyleControl.displaySelectMenu = function (tabString,temp_obj,temp_btn,local_page_width,local_page_height,isPageCount,flag){
	var ttop  = temp_obj.offsetTop;     //TT控件的定位点高
	var thei  = temp_obj.clientHeight+3;//TT控件本身的高
	var tleft = temp_obj.offsetLeft;    //TT控件的定位点宽
	var ttyp  = temp_btn.type;          //TT控件的类型
	while (temp_btn = temp_btn.offsetParent){ttop+=temp_btn.offsetTop; tleft+=temp_btn.offsetLeft;}
	
	var pageMainColor  = "#F1F0FF";
	var pageLineColor  = "#9AAFE9";
	var pageClickColor = "#ffffff";
	/*if(typeof document.styleSheets != 'undefined' && document.styleSheets[0].rules.length>40){		
		pageLineColor=document.styleSheets[0].rules[26].style.backgroundColor;//
		pageMainColor=document.styleSheets[0].rules[29].style.backgroundColor;//	
	}*/
	tabString = tabString.replace(/pageMainColor/gi,pageMainColor);
	tabString = tabString.replace(/pageLineColor/gi,pageLineColor);
	tabString = tabString.replace(/pageClickColor/gi,pageClickColor);
	if(!ExtendCombo.isIE6 || flag){
		var win   = frames["displaySelectLayer"];
		var divs  = document.getElementById("displaySelectLayer").style;
		divs.top  = (ttyp=="image")? ttop+thei : ttop+thei+4;
		divs.left = tleft;
		divs.display = '';
		win.document.close()
		win.document.writeln(tabString)
		win.document.close();	
		divs.width = "160";
		if(isPageCount){
			var twidth  = win.document.getElementById("tableId_selectList").clientWidth+2
			local_page_width  = (twidth > local_page_width)?twidth : local_page_width ;			
		}
		divs.width = local_page_width;
		if(isPageCount){
			var tempHeight = win.document.getElementById("tableId_selectList").offsetHeight+2;
			local_page_height = local_page_height < tempHeight? tempHeight : local_page_height;	
		}	
		divs.height= local_page_height;
		Global.setHeight(local_page_height);
	}else{		
		ExtendCombo.selectWin.document.body.style.border   = '0';
		ExtendCombo.selectWin.document.writeln(tabString);
		ExtendCombo.selectWin.document.close();
		ExtendCombo.selectWin.show(0,thei,local_page_width, local_page_height,temp_obj);
		var tabContext = ExtendCombo.selectWin.document.getElementById("tableId_selectList");
		var twidth  = tabContext.clientWidth+0
		var theight = tabContext.clientHeight+0
		local_page_width  = (twidth > local_page_width)?twidth : local_page_width ;			
		if(isPageCount){	
			ExtendCombo.selectWin.show(0,thei, local_page_width, theight,temp_obj);
		}
	}
}

/* 调节表格的宽度，目前出现在树操作中
 * tabid 前一个td的对象，这里主要调节的就是它的宽度
 * num   要减除的宽度，一般是页面 document.body.style.marginLeft + document.body.style.margetRight;
 */
StyleControl.dragWidth = function(tabid,num,evt){
	num = num ? num : 0;
	tabid = tabid.previousSibling ? tabid.previousSibling : tabid;
	evt = evt ? evt : (window.event ? window.event : null); 
	//将宽度调节为0时候有错误
	try{
		var mX = evt.x ? evt.x : evt.pageX;
		if(evt.button ==1){
			var magin = (num + '' == 'undefined')?0:num;
			var xx = (mX - magin < 0)?0: mX - magin;
			
			//修改td中树型div元素的宽度
			if (span_menu) {
				span_menu.style.width = xx;
			}
			
			tabid.style.width = xx;
		}
	}catch(e){}
}

//TODO 兼容firefox 移动层做的工作
function setCapture(){
	
}
//TODO 兼容firefox 移动层做的工作
function releaseCapture(){

}
