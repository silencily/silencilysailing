/**
 * ��ʽ���� javascript ����
 * @author java2enterprise
 * @author pillarliu
 * @version $Id: styleControl.js,v 1.1 2010/12/10 10:56:35 silencily Exp $
 */

if (StyleControl == null) {
	var StyleControl = {};
}

/**
 * ���� IFrame �߶�
 * @param parentIFrameId ������ IFrame Id
 */
StyleControl.setIFrameHeight =  function(parentIFrameId) {
	if (parent.document.getElementById(parentIFrameId)) {
		parent.document.getElementById(parentIFrameId).style.height = document.body.scrollHeight + "px";
	}
}

/*	
 * ������� ��ʾ&���ر��id=sidЧ����ͬʱҲ�л�ͼƬid=imgbtn��
 * @param sid  ��������ID
 * @param img  ����ͼƬID
 * @param flag ���ڴ�С��Ĭ��60%�����Ϊ0�������¿�ܣ����Ϊ100%�������Ͽ��
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
 * �������ڣ�����һ����Frame����, Frame���йرհ�ť
 * @param url  		Ҫ�򿪵ĵ�ַ
 * @param winName   ������
 * @param wwidth	�� Ĭ��Ϊ��Ļ��1/2
 * @param wheight 	�� Ĭ��Ϊ��Ļ��1/2
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
 * �������ڣ�����һ��ģ̬�����д򿪹���Frameҳ��
 * @param url  		Ҫ�򿪵ĵ�ַ
 * @param wwidth		���, �������, Ĭ��Ϊ700
 * @param wheight 	�߶�, �������, Ĭ��Ϊ460
 * @return 			�Ի��򷵻�ֵ 
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


/** ������ʾ textarea �༭��   
 *  txt  �����:obj
 *  btn  ��ť��obj *�ɲ���
 *  col  ��ȣ�int
 *  row  �߶ȣ�int
 *  hidd ������:obj
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
	str += '<table width="100%" title="�������������ı�����" border=0 bgcolor="pageLineColor" cellSpacing=1 cellPadding=0 id="tableId_selectList"><tr><td bgcolor="pageClickColor">';
	str += '<textarea name="txt_context" style="width:'+parseInt(col-3)+'px;height:'+row+'px;border:0px;overflow:hidden;overflow-y:auto" wrap="VIRTUAL" ';
	str += ' onkeyup="text_len.innerText=this.value.length;parent.getTextareaValue(txt_context.value,'+flag+',\''+txtname+'\',\''+hidname+'\')">'+dis+'</textarea></td></tr>';
	str += '<tr><td height="22" bgcolor="pageMainColor">';
	str += '<table width="100%"><tr><td width="50%">����:<span id="text_len">'+dis.length+'</span></td><td align="right">';
	str += '<a href="#" onclick="parent.getTextareaValue(txt_context.value,'+flag+',\''+txtname+'\',\''+hidname+'\');parent.document.getElementById(\'displaySelectLayer\').style.display=\'none\'">ȷ��</a> ';
	str += '<a href="#" onclick="parent.getTextareaValue(\'\','+flag+',\''+txtname+'\',\''+hidname+'\');txt_context.value=\'\';return false">����</a> &nbsp; </td></tr><table>';
	str += '</td></tr></table></body></html>';
	StyleControl.displaySelectMenu(str,txt,btn,col,row+27,'',true)
}
*/

/**
 * �滻ʵ��Ϊ�����������
 */
StyleControl.setTextareaContext = function(textObject) {
	definedWin.openLongTextWin(textObject);
}

/** ������ʾ�����˵�����
 *  tabString			��ʾ������(string)
 *  temp_obj			����ؼ��������ڶ�λ 
 *  temp_btn			��ť����
 *  local_page_width    ��ʾ���
 *  local_page_height   ��ʾ�߶�
 *  isPageCount			�����Ƿ��й����������ø߶ȡ����	
 *  flag �Ƿ񲻲���creatpop
 */
StyleControl.displaySelectMenu = function (tabString,temp_obj,temp_btn,local_page_width,local_page_height,isPageCount,flag){
	var ttop  = temp_obj.offsetTop;     //TT�ؼ��Ķ�λ���
	var thei  = temp_obj.clientHeight+3;//TT�ؼ�����ĸ�
	var tleft = temp_obj.offsetLeft;    //TT�ؼ��Ķ�λ���
	var ttyp  = temp_btn.type;          //TT�ؼ�������
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

/* ���ڱ��Ŀ�ȣ�Ŀǰ��������������
 * tabid ǰһ��td�Ķ���������Ҫ���ڵľ������Ŀ��
 * num   Ҫ�����Ŀ�ȣ�һ����ҳ�� document.body.style.marginLeft + document.body.style.margetRight;
 */
StyleControl.dragWidth = function(tabid,num,evt){
	num = num ? num : 0;
	tabid = tabid.previousSibling ? tabid.previousSibling : tabid;
	evt = evt ? evt : (window.event ? window.event : null); 
	//����ȵ���Ϊ0ʱ���д���
	try{
		var mX = evt.x ? evt.x : evt.pageX;
		if(evt.button ==1){
			var magin = (num + '' == 'undefined')?0:num;
			var xx = (mX - magin < 0)?0: mX - magin;
			
			//�޸�td������divԪ�صĿ��
			if (span_menu) {
				span_menu.style.width = xx;
			}
			
			tabid.style.width = xx;
		}
	}catch(e){}
}

//TODO ����firefox �ƶ������Ĺ���
function setCapture(){
	
}
//TODO ����firefox �ƶ������Ĺ���
function releaseCapture(){

}
