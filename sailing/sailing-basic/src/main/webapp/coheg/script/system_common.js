
//全局参数
//计算出现loading条次数
var gobal_loading_num = 0;

/* 获取文件所在的目录级别，主要用于页面的相对路径，这里是以"prototype"取值 
 * 如果不用prototype为目录名，则需要修改这里（注意路径中不要重复出现"prototype"）
 * 如果正式运行系统，则使用 getContextPath() 方法。
 */
function get_filepath(){
	var folderName = "prototype";
	var file_href=location.href;
	var href_end=file_href.lastIndexOf("/");
	var myUrl="";
	if(file_href.indexOf(folderName)>0){
		myUrl = file_href.substring(file_href.indexOf(folderName) + 9);
		if(myUrl.indexOf('?')>0)
			myUrl = myUrl.substring(0,myUrl.indexOf('?'));
	}
	var url_count = 0;
	for(var url_i = 0; url_i < myUrl.length; url_i++){
		var char_flag = myUrl.charAt(url_i);
		if('/' == char_flag)
			url_count++;		
	}
	myUrl=folderName+"/";
	for(url_i=0;url_i< url_count;url_i++)
	{
		myUrl=myUrl+"../";
	}
	return 	myUrl;
}
//与jsp中 request.getContextPath()作用类同，用于静态页面
function getContextPath(){
	var str = get_filepath();
	str = str.substring(0,str.length-1);
	return str
}
//在原型阶段使用上面的方法，在正式运行系统(jsp环境下)，使用以下方法
/*
function getContextPath(){
	var url = location.href;
   url = url.substring(url.indexOf('://') + 3);
   if(url.indexOf('/') < 0)
   		return "./";
   url = url.substring(url.indexOf('/') + 1);
   var pos;
   if(url.indexOf('/') >= 0) 
		pos = url.indexOf('/');
   else pos = url.length;
   		url = url.substring(0, pos);
   return "/"+url;
}
*/

//// 浏览器版本 检测 temp为你需要判断的版本.目前只为IE,以后补充 Netscape
function checkExplorer(temp){
	try{
		var BrowserInfo = new Object() ;
		BrowserInfo.MajorVer = navigator.appVersion.match(/MSIE (.)/)[1] ;
		BrowserInfo.MinorVer = navigator.appVersion.match(/MSIE .\.(.)/)[1] ;
		BrowserInfo.IsIEgoon = false;
		switch (temp)
		{
			case "6":
				BrowserInfo.IsIEgoon = BrowserInfo.MajorVer >= 6;break;
			case "5.5":
				BrowserInfo.IsIEgoon = BrowserInfo.MajorVer >= 6 || ( BrowserInfo.MajorVer >= 5 && BrowserInfo.MinorVer >= 5 ) ;break;
			case "4":
				BrowserInfo.IsIEgoon = BrowserInfo.MajorVer >= 4;break;
			default:
				BrowserInfo.IsIEgoon = BrowserInfo.MajorVer >= temp;break;
		}
		if (BrowserInfo.IsIEgoon)
			return true;
		else
			return false;
	}catch(e){return false;}
}

function hiddenLoading(){
	if(top.document.getElementById("ifrmId_systemLoading")){
		top.document.getElementById("ifrmId_systemLoading").style.display = 'none'
	}else{
		document.getElementById("ifrmId_systemLoading").style.display = 'none'
	}
	return true;
}


/*----------------------------------------------------
 * 生成 公用使用iframe 或者 createpopup
 */
var SelectInputWin        = ''; 
var global_explorer_ie6   = false;
if(checkExplorer(5.5)){
	global_explorer_ie6   = true;
	SelectInputWin = window.createPopup(); //
}
if(typeof ifrmId_systemLoading != 'object'){
	var str  = '<iframe id=ifrmId_systemLoading Author=heerit frameborder=0 ';
		str += 'style="position: absolute; width: 160px; height: 0px; z-index: 9998; display: none;" '
		str += 'scrolling=yes src="about:blank"></iframe>';
	document.writeln(str);
	window.frames.ifrmId_systemLoading.document.close();	
	//
	hiddenLoading();
}

//-- 加载公用页面
function loadCommonPage(str,pleft,ptop,pwidth,pheight,obj,flag){
	var url = getContextPath();	
	var tabString = '';
	//-- the CSS of  Page 
	var pageMainColor  = "#dddddd";
	var pageLineColor  = "#666666";
	var pageClickColor = "#ffffff";
	try{  
		if(typeof document.styleSheets != 'undefined'){			
			//pageLineColor=document.styleSheets[0].rules[26].style.backgroundColor;//
			pageLineColor=document.styleSheets[0].rules[26].style.backgroundColor;//
			pageMainColor=document.styleSheets[0].rules[29].style.backgroundColor;//	
		}
	}catch(e){}

	tabString += '<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN"><head><title>loading</title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=GBK\"><link href=\""+url+"/css/style.css\" rel=\"stylesheet\" type=\"text/css\">';
	tabString += '<style>body{margin-left:1px;margin-top:1px;margin-right:1px;margin-bottom:0px}</style>';
	tabString += '<body style="overflow:hidden" oncontextmenu="self.event.returnValue=false">';
	tabString += str;
	tabString += '</body></html>';
	
	tabString = tabString.replace(/pageMainColor/gi,pageMainColor);
	tabString = tabString.replace(/pageLineColor/gi,pageLineColor);
	tabString = tabString.replace(/pageClickColor/gi,pageClickColor);
	if(global_explorer_ie6 && flag){
			SelectInputWin.document.body.style.border   = '0';
			SelectInputWin.document.writeln(tabString);
			SelectInputWin.document.close();
			SelectInputWin.show(pleft,ptop, pwidth, pheight);
	}else{
		var pobj = document.getElementById("ifrmId_systemLoading").style
		pobj.display = '';
		window.frames.ifrmId_systemLoading.document.writeln(tabString)
		window.frames.ifrmId_systemLoading.document.close();
		pobj.top    = ptop;
		pobj.left   = pleft;
		pobj.width  = pwidth;
		pobj.height = pheight;
	}
}

function getMouseClick(){
	if(event.button == 1){
		var src = event.srcElement;	
		//alert(src.target +"|"+ src.onclick + "|"+src.parentElement.onclick)
		//if(src.target == '' || (src.onclick == null && src.parentElement.onclick == null )){
			//alert(src.onclick)
			//top.ClickLoading();
			//getClickLoading()
		//}
	}else if(event.button == 2 || event.button == 3)
		disMouseRightHand();
}
//document.body.onmousedown= getMouseClick;

/* -------------------------------------
 * 鼠标点击事件 出现进度条
 */
 function ClickLoading(str,inum,path){
	 	var url = (path + '' == 'undefined')?getContextPath():path;	
		str = (str + '' == 'undefined')?'':str;
		inum = ( inum + '' != 'undefined' )?inum:0.010;
		var pleft,ptop, pwidth, pheight,load_flag;
		var tabString = '';			
		if(document.getElementById("ifrmId_systemLoading").style.display == '')
			return;
		tabString += '<table width="398"  border="0" cellpadding="0" cellspacing="1" bordercolorlight="#c0c0c0"  bgcolor="#000000"><tr><td valign="top" bgcolor="#ffffff">'+
					 '<table width="100%" height="146" border="0" cellpadding="0" cellspacing="0" background="'+url+'/image/common/loading_name.gif">'+
					 '<tr>'+
						'<td height="92" valign="top" align="right"><img src="'+url+'/image/common/loading_stop.gif" onclick="top.closeLoading()" alt="取消加载动作" style="cursor:hand">&nbsp;</td>'+
					 '</tr>'+
					 '<tr>'+
						'<td height="42"><table width="88%"  border="0" align="center" cellpadding="0" cellspacing="0">'+
						  '<tr>'+
							/*'<td>'+
			'<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0" width="350" height="18">'+
            '<param name="movie" value="'+url+'image/logo/system/loading.swf">'+
            '<param name="quality" value="high">'+
            '<embed src="'+url+'image/logo/system/loading.swf" quality="high" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" ></embed>'+
			'</object>'+
							'</td>'+*/
							'<td><img src="'+url+'/image/common/loading.gif" height="18" name="imgId_Loading" alt="正在加载数据......" style="background-color:#477DF5"></td>'+
						  '</tr>'+
						  '<tr>'+
							'<td height="24"><div id="txtId_Loading" style="font-size:12px"> 请稍候，正在加载 '+str+' ......</div></td>'+
						  '</tr>'+
						'</table></td>'+
					'</tr>'+
					'<tr>'+
						'<td></td>'+
					'</tr>'+
					'</table></td></tr></table>';
		tabString +='<scr'+'ipt type="text/javascript">';
		tabString +=' function DisplayLoading(bwidth){'+
					'	var swidth = 358;'+
					'	if(bwidth<swidth *0.98)'+
					'		bwidth += (swidth - bwidth)*'+inum+';'+
					'	document.imgId_Loading.width = bwidth;'+
					'	if(bwidth > swidth - 15){'+
					'		document.getElementById("txtId_Loading").innerHTML = "<font color=red>加载 '+str+' 失败！  请重试......</font>";}'+
					'	if(bwidth > swidth - 10){'+
					'		parent.document.getElementById("ifrmId_systemLoading").style.display="none";return true;'+
					'	}'+
					'	setTimeout("DisplayLoading("+bwidth+")",150);'+
					'	return false;'+
					' }'+
					' DisplayLoading(100)';
		tabString +='</scr'+'ipt>';
		pleft   = top.document.body.offsetWidth/2 - 200;
		ptop    = top.document.body.offsetHeight/2 - 200;
		pwidth  = 400;
		pheight = 150;	
		loadCommonPage(tabString,pleft,ptop,pwidth,pheight);
		gobal_loading_num ++;
		//
		if(document.getElementById("iframeMain")){
			//window.frames.iframeMain.topFrame.disabled = true;
		}
 }

//-- 用于 判断是否存在loading，如果存在就 不再进行下去，否则出现进度条 
//@param str  进度条上显示加载的信息，如正在加载 XXXXX
//@param inum 设置进度条加载速度，默认0.025,数字越大速度越快，反之越慢。
function getClickLoading(str,inum,path){
	if(typeof top == 'object' && typeof top.ClickLoading == 'function'){
		if(top.document.getElementById("ifrmId_systemLoading").style.display == ''){				
				self.event.returnValue=false;	
				return false;
		}		
		top.ClickLoading(str,inum,path)
	}else{
		if(document.getElementById("ifrmId_systemLoading").style.display == ''){
				self.event.returnValue=false;
				return false;
		}		
		ClickLoading(str,inum,path)
	}
	return true;
}

//-- 用于 判断是否完成loading，如果没有完成就 不再进行下一步.. 
function isFinisthLoading(){
	if(typeof top == 'object' && typeof top.ClickLoading == 'function'){
		if(top.document.getElementById("ifrmId_systemLoading").style.display == ''){
				self.event.returnValue=false;	
				return false;
		}		
	}else{
		if(document.getElementById("ifrmId_systemLoading").style.display == ''){
				self.event.returnValue=false;
				return false;
		}		
	}
	return true;
}

//-- 用于强制关闭进度条.. 
function closeLoading(){
	hiddenLoading();
	if(gobal_loading_num > 1)
		parent.history.back();
}

//--取消点击事件
function ClickFalse(){
	self.event.returnValue=false
}


// 取通过URL传过来的参数 (格式如 ?Param1=Value1&Param2=Value2)
var URLParams = new Object() ;
var uParams = document.location.href.split('?');
var aParams = document.location.search.substr(1).split('&') ;
for (i=0 ; i < aParams.length ; i++) {
	var aParam = aParams[i].split('=') ;
	URLParams[aParam[0]] = aParam[1] ;
}