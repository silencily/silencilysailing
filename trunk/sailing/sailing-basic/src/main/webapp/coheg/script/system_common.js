
//ȫ�ֲ���
//�������loading������
var gobal_loading_num = 0;

/* ��ȡ�ļ����ڵ�Ŀ¼������Ҫ����ҳ������·������������"prototype"ȡֵ 
 * �������prototypeΪĿ¼��������Ҫ�޸����ע��·���в�Ҫ�ظ�����"prototype"��
 * �����ʽ����ϵͳ����ʹ�� getContextPath() ������
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
//��jsp�� request.getContextPath()������ͬ�����ھ�̬ҳ��
function getContextPath(){
	var str = get_filepath();
	str = str.substring(0,str.length-1);
	return str
}
//��ԭ�ͽ׶�ʹ������ķ���������ʽ����ϵͳ(jsp������)��ʹ�����·���
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

//// ������汾 ��� tempΪ����Ҫ�жϵİ汾.ĿǰֻΪIE,�Ժ󲹳� Netscape
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
 * ���� ����ʹ��iframe ���� createpopup
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

//-- ���ع���ҳ��
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
 * ������¼� ���ֽ�����
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
						'<td height="92" valign="top" align="right"><img src="'+url+'/image/common/loading_stop.gif" onclick="top.closeLoading()" alt="ȡ�����ض���" style="cursor:hand">&nbsp;</td>'+
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
							'<td><img src="'+url+'/image/common/loading.gif" height="18" name="imgId_Loading" alt="���ڼ�������......" style="background-color:#477DF5"></td>'+
						  '</tr>'+
						  '<tr>'+
							'<td height="24"><div id="txtId_Loading" style="font-size:12px"> ���Ժ����ڼ��� '+str+' ......</div></td>'+
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
					'		document.getElementById("txtId_Loading").innerHTML = "<font color=red>���� '+str+' ʧ�ܣ�  ������......</font>";}'+
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

//-- ���� �ж��Ƿ����loading��������ھ� ���ٽ�����ȥ��������ֽ����� 
//@param str  ����������ʾ���ص���Ϣ�������ڼ��� XXXXX
//@param inum ���ý����������ٶȣ�Ĭ��0.025,����Խ���ٶ�Խ�죬��֮Խ����
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

//-- ���� �ж��Ƿ����loading�����û����ɾ� ���ٽ�����һ��.. 
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

//-- ����ǿ�ƹرս�����.. 
function closeLoading(){
	hiddenLoading();
	if(gobal_loading_num > 1)
		parent.history.back();
}

//--ȡ������¼�
function ClickFalse(){
	self.event.returnValue=false
}


// ȡͨ��URL�������Ĳ��� (��ʽ�� ?Param1=Value1&Param2=Value2)
var URLParams = new Object() ;
var uParams = document.location.href.split('?');
var aParams = document.location.search.substr(1).split('&') ;
for (i=0 ; i < aParams.length ; i++) {
	var aParam = aParams[i].split('=') ;
	URLParams[aParam[0]] = aParam[1] ;
}