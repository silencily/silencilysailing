
/* 系统说明注释效果
 * 2006-09-21  liuz
 * @link www.vfp.cn
 */

//**默认设置定义.
var Alt = {};
Alt.tPopWait    = 30;  //停留tWait豪秒后显示提示。
Alt.tPopShow    = 10000;//显示tShow豪秒后关闭提示
Alt.showPopStep = 10;
Alt.popOpacity  = 100;
Alt.pltsoffsetX = 10; //显示内容与鼠标的相对位置
Alt.pltsoffsetY = 15;

//**内部变量定义
Alt.sPop        = null;
Alt.curShow     = null;
Alt.tFadeOut    = null;
Alt.tFadeIn     = null;
Alt.tFadeWaiting= null;
Alt.mouseX;
Alt.mouseY;
Alt.dypopLayer = null;

document.write("<div id='alt_dypopLayer' style='position:absolute;z-index:2000;display:none' class='cPopText'></div>");
document.close();


/** 显示提示文字
 */
Alt.showPopupText = function (event){	
	event = event ? event : (window.event ? window.event : null);
	Alt.mouseX = event.x ? event.x : event.pageX;
	Alt.mouseY = event.y ? event.y : event.pageY;
	var o;
	if(event.x) {
		o = event.srcElement;
	} else {
		o = event.target;
	}
	if(o==null)
		return;
	if(o.alt!=null && o.alt!=""){
		o.dypop=o.alt;
		o.alt=""
	};
	try{
		if(o.title!=null && o.title!=""){
			 o.dypop=o.title;
			 o.title="";
		}
	}catch(e){return;}
	if(o.dypop!=Alt.sPop) {
			Alt.sPop = o.dypop
			clearTimeout(Alt.curShow);
			clearTimeout(Alt.tFadeOut);
			clearTimeout(Alt.tFadeIn);
			clearTimeout(Alt.tFadeWaiting);	
			if(Alt.sPop==null || Alt.sPop=="") {
				Alt.dypopLayer.innerHTML="";
				Alt.dypopLayer.style.filter="Alpha()";
				if(Alt.dypopLayer.filters)
					Alt.dypopLayer.filters.Alpha.opacity=0;	
				Alt.dypopLayer.style.display = "none";
			}else {
				if(o.dyclass!=null) 
					popStyle=o.dyclass 
				else 
					popStyle="cPopText";
				Alt.curShow=setTimeout("Alt.showIt()",Alt.tPopWait);
			}
			
	}
}

/** 显示提示窗口 
 */
Alt.showIt = function(){
	Alt.dypopLayer.style.display = "";
	Alt.dypopLayer.className=popStyle;
	var Msg = Alt.sPop;
	var re;
	re=/\r\n/g; 
	var temp = Msg.replace(re,'<br>');
	var pltsTitle = "";//⊿ 系统注释 style="FILTER:alpha(opacity=70) shadow(color=#bbbbbb,direction=110);"
	var content =
	'<table  id="AltId_toolTipTalbe" border=0><tr><td width="100%"><table  cellspacing="0" cellpadding="0" style="width:100%">'+
	'<tr id="AltId_pltsPoptop"><th height=12 valign=bottom><p id="AltId_topleft" align="left" class="cPopText_top">↖'+pltsTitle+'</p><p id="AltId_topright" class="cPopText_top" align=right style="display:none">'+pltsTitle+'↗</font></th></tr>'+
	'<tr><td style="line-height:135%;word-break : break-all;line-break : break-all ;word-wrap : break-all;">'+temp+'</td></tr>'+
	'<tr id="AltId_pltsPopbot" style="display:none"><th height=12 valign=bottom><p id="AltId_botleft" align="left" class="cPopText_top">↙'+pltsTitle+'</p><p id=AltId_botright class="cPopText_top" align=right style="display:none">'+pltsTitle+'↘</font></th></tr>'+
	'</table></td></tr></table>';
	Alt.dypopLayer.innerHTML = content;
	document.getElementById("AltId_toolTipTalbe").style.width=Math.min(Alt.dypopLayer.clientWidth,document.body.clientWidth/2.2);	
	Alt.setShow();
}

Alt.setShow = function(){
	if(Alt.dypopLayer.innerHTML=='')
		return true;
	var popHeight=Alt.dypopLayer.clientHeight;
	var popWidth=Alt.dypopLayer.clientWidth;
	var popTopAdjust;
	var scrollLeftTemp;
	var scrollTopTemp;
	
	if(document.documentElement&&document.documentElement.scrollTop){
		scrollTopTemp = document.documentElement.scrollTop;
	}else{
		scrollTopTemp = document.body.scrollTop;
	}
	
	if(document.documentElement&&document.documentElement.scrollLeft){
		scrollLeftTemp = document.documentElement.scrollLeft;
	}else{
		scrollLeftTemp = document.body.scrollLeft;
	}
	
	if(Alt.mouseY+Alt.pltsoffsetY+popHeight>document.body.clientHeight)
	{
	  	popTopAdjust=-popHeight-Alt.pltsoffsetY*1.5;
	  	document.getElementById("AltId_pltsPoptop").style.display="none";
	  	document.getElementById("AltId_pltsPopbot").style.display="";
	}
	 else
	{
	   	popTopAdjust=0;
	  	document.getElementById("AltId_pltsPoptop").style.display="";
	  	document.getElementById("AltId_pltsPopbot").style.display="none";
	}
	if(Alt.mouseX+Alt.pltsoffsetX+popWidth>document.body.clientWidth)
	{
		popLeftAdjust=-popWidth-Alt.pltsoffsetX*2;
		document.getElementById("AltId_topleft").style.display="none";
		document.getElementById("AltId_botleft").style.display="none";
		document.getElementById("AltId_topright").style.display="";
		document.getElementById("AltId_botright").style.display="";
	}
	else
	{
		popLeftAdjust=0;
		document.getElementById("AltId_topleft").style.display="";
		document.getElementById("AltId_botleft").style.display="";
		document.getElementById("AltId_topright").style.display="none";
		document.getElementById("AltId_botright").style.display="none";
	}
	try{
		Alt.dypopLayer.style.left=Alt.mouseX+Alt.pltsoffsetX+scrollLeftTemp+popLeftAdjust;
		Alt.dypopLayer.style.top=Alt.mouseY+Alt.pltsoffsetY+scrollTopTemp+popTopAdjust;
		Alt.dypopLayer.style.filter="Alpha(Opacity=0)";	
		Alt.fadeOut();
	}catch(e){};
  	return true;
}

/** 窗口淡出 
 */
Alt.fadeOut = function(){
	if(Alt.dypopLayer && Alt.dypopLayer.filters){
		if(Alt.dypopLayer.filters.Alpha.opacity<Alt.popOpacity) {
			Alt.dypopLayer.filters.Alpha.opacity+=Alt.showPopStep;
			Alt.tFadeOut=setTimeout("Alt.fadeOut()",1);
		}else {
			Alt.dypopLayer.filters.Alpha.opacity=Alt.popOpacity;
			Alt.tFadeWaiting=setTimeout("Alt.fadeIn()",Alt.tPopShow);
		}
	}
}

/** 窗口淡入 
 */
Alt.fadeIn = function(){
	if(Alt.dypopLayer && Alt.dypopLayer.filters.Alpha.opacity>0) {
		Alt.dypopLayer.filters.Alpha.opacity-=1;
		Alt.tFadeIn=setTimeout("Alt.fadeIn()",1);
	}
}

document.onmouseover = function(event){
	Alt.dypopLayer = document.getElementById("alt_dypopLayer");
	Alt.showPopupText(event);
}

