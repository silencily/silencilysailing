<!--

-->

<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<title> - ������ҵ��Դ�����׼�������� - ������ҳ -- </title>
<meta http-equiv="Content-Type" content="text/html; charset=GBK"/>
<meta name="author" content="coheg (coheg@coheg.com) �㽭��ѧ�����Ƽ��������޹�˾ "/>

<link rel="stylesheet" href="<c:out value = "${initParam['publicResourceServer']}/css/style.css" />" type="text/css" media="all" />
<link rel="stylesheet" href="<c:out value = "${initParam['publicResourceServer']}/css/style_interface.css" />" type="text/css" media="all" />
<link rel="stylesheet" href="<c:out value = "${initParam['publicResourceServer']}/css/style_buttom.css" />" type="text/css" media="all" />
<link rel="stylesheet" href="<c:out value = "${initParam['publicResourceServer']}/css/style_page.css" />" type="text/css" media="all" />
<link rel="stylesheet" href="<c:out value = "${initParam['publicResourceServer']}/css/style_buttom_opera.css" />" type="text/css" media="all" />

<link rel="shortcut icon" href="<ww:url value="'/coheg/image/favicon.ico'"/>"/>
<link rel="icon" type="image/png" href="<ww:url value="'/coheg/image/favicon.png'"/>"/>

<script type="text/javascript" src = "<c:out value = "${initParam['publicResourceServer']}/public/scripts/contextInfo.js" />"></script>
<script type="text/javascript">
ContextInfo.contextPath = '<c:out value="${initParam['publicResourceServer']}"/>'
</script>
<script type="text/javascript" src = "<c:out value = "${initParam['publicResourceServer']}/public/scripts/global.js" />"></script>
<script type="text/javascript" src="<c:out value = "${initParam['publicResourceServer']}"/>/public/scripts/xmlhttp.js"></script>
<script type="text/javascript" src="<c:out value = "${initParam['publicResourceServer']}"/>/public/scripts/menu.js"></script>
<script type="text/javascript" src="<c:out value = "${initParam['publicResourceServer']}"/>/public/scripts/menu_xml.js"></script>

<link rel="stylesheet" href="<c:out value = "${initParam['publicResourceServer']}"/>/css/definedWin.css" type="text/css" media="screen" />
<script type="text/javascript" src="<c:out value = "${initParam['publicResourceServer']}"/>/public/scripts/definedWin.js"></script>
<script type="text/javascript" src="<c:out value = "${initParam['publicResourceServer']}"/>/public/scripts/styleControl.js"></script>

<script type="text/javascript" src="<c:out value = "${initParam['publicResourceServer']}"/>/public/scripts/prototype.js"> </script> 


<script type="text/javascript" src="<c:out value = "${initParam['publicResourceServer']}"/>/public/scripts/dragdiv.js"></script>

<!--for message -->
<script type="text/javascript" src="" id="jsId_message"></script>

<style type="text/css">
<!--
a{
	text-decoration: none ;
}
.link,.link:visited,.link:active,.link:hover{
    color:#ffffff;
	text-decoration: none ;
}
-->
</style>

<title></title>
<style type="text/css">
<!--
.STYLE1 {
	font-size: 45px;
	font-weight: bold;
	color: #6699FF;
}
.STYLE2 {font-size: xx-large}
.STYLE3 {font-size: 12px}
a:link {
	text-decoration: none;
}
-->
</style>

<style type="text/css">
<!--
#btnnew1,.btnnew1 {
	background-image: url(<c:out value="${initParam['publicResourceServer']}"/>/img/new.gif);
	margin: 0px;
	padding: 0px;
	height: 22px;
	width: 60px;
	border-top-style: none;
	border-right-style: none;
	border-bottom-style: none;
	border-left-style: none;
  background-repeat:no-repeat; 
  background-color:transparent;
  cursor:pointer;
}
.btnnew1_disabled{
	background-image: url(<c:out value="${initParam['publicResourceServer']}"/>/img/newnull.gif);
	margin: 0px;
	padding: 0px;
	height: 22px;
	width: 60px;
	border-top-style: none;
	border-right-style: none;
	border-bottom-style: none;
	border-left-style: none;
  background-repeat:no-repeat; 
  background-color:transparent;
}
#btnsave1,.btnsave1 {
	background-image: url(<c:out value="${initParam['publicResourceServer']}"/>/img/save.gif);
	margin: 0px;
	padding: 0px;
	height: 22px;
	width: 60px;
	border-top-style: none;
	border-right-style: none;
	border-bottom-style: none;
	border-left-style: none;
  background-repeat:no-repeat; 
  background-color:transparent;
  cursor:pointer;
}
.btnsave1_disabled{
	background-image: url(<c:out value="${initParam['publicResourceServer']}"/>/img/savenull.gif);
	margin: 0px;
	padding: 0px;
	height: 22px;
	width: 60px;
	border-top-style: none;
	border-right-style: none;
	border-bottom-style: none;
	border-left-style: none;
  background-repeat:no-repeat; 
  background-color:transparent;
}
#btnsbt1,.btnsbt1 {
	background-image: url(<c:out value="${initParam['publicResourceServer']}"/>/img/submit.gif);
	margin: 0px;
	padding: 0px;
	height: 22px;
	width: 60px;
	border-top-style: none;
	border-right-style: none;
	border-bottom-style: none;
	border-left-style: none;
  background-repeat:no-repeat; 
  background-color:transparent;
  cursor:pointer;
}
.btnsbt1_disabled{
	background-image: url(<c:out value="${initParam['publicResourceServer']}"/>/img/submitnull.gif);
	margin: 0px;
	padding: 0px;
	height: 22px;
	width: 60px;
	border-top-style: none;
	border-right-style: none;
	border-bottom-style: none;
	border-left-style: none;
  background-repeat:no-repeat; 
  background-color:transparent;
}
--> 
</style>
<script type=text/javascript>
function getCurDate(){
    var pdate = new Date();
    var curdate = pdate.getYear();
    curdate +="��";
    if(pdate.getMonth()+1<10){
        curdate +="0";
        curdate +=pdate.getMonth()+1;
	 }
	else
		curdate += pdate.getMonth()+1;		
	curdate +="��";  
    if(pdate.getDate()<10)
        curdate +="0";
    curdate += pdate.getDate();
	curdate +="�� ";
	myArray=new Array(6);                 
	myArray[0]="������"                 
	myArray[1]="����һ"                 
	myArray[2]="���ڶ�"                 
	myArray[3]="������"                 
	myArray[4]="������"                 
	myArray[5]="������"                 
	myArray[6]="������"                 
	weekday=pdate.getDay();                                              
	curdate=curdate+myArray[weekday];   
    return curdate;
}
</script>

<script language="javascript">
<!--
	var IsClosed = false;
	var IsChanged = false;
	var IsFirst = true;
	var Operation = "";
//-->
</script>

<meta http-equiv="Content-Type" content="text/html; charset=GBK"/>

</head>

<body scroll="no">
<!-- ���ش���-->
<div id="definedWin_overlay"></div>
<div class="definedWin_lightbox" id="definedWin_lightbox[0]" zIndex="9005" txtName="">
	<div class="definedWin_title" id="definedWin_title[0]" onmousedown='DivUtil.dragMouseDown(this,event);setCapture();' onmouseup='releaseCapture();' onmousemove='DivUtil.dragMouseMove(this,event)' > 
		<table border=0  cellpadding="0" cellspacing="0"><tr><td class="definedWin_titleName" id="definedWin_titleName[0]">&nbsp; ϵͳ���� </td>
		<td width="5%" align=right id="definedWin_close" valign="top">
		<img src="<c:out value = "${initParam['publicResourceServer']}"/>/image/skin/dialog/close.gif" alt="�������رմ���" onclick="definedWin.closeModalWindow();" style="cursor:pointer"/></td></tr></table>
	</div>	
	<div class="definedWin_box">					
		<div id="definedWin_button[0]" class="definedWin_button">
			<input type="button" class="opera_display" name="opera_ok" value="ȷ��" title="ִ�в���" onclick="definedWin.selectListingButton()"/>
			<input type="button" class="opera_display" name="opera_cancel" value="ȡ��" title="ȡ��ѡ��" onclick="definedWin.closeModalWindow()"/>
		</div>
		<div id="definedWin_content[0]" class="definedWin_content"></div>
	</div>
</div>


<div id="definedWin_loading">
	<iframe name="definedWin_frm_loading" id="definedWin_frm_loading" width="400" height="150" frameborder="0" src="<c:out value = "${initParam['publicResourceServer']}"/>/jsp/loading.jsp"></iframe>
</div>


<div id="header">
  <div>
    <div id="logo"><a href="#"><img src="<c:url value="/img/logo.jpg" />" /></a></div>
    
	<div id="headerright"><img src="<c:url value="/img/headerrightbar.jpg" />" width="210" height="26" border="0" usemap="#Map" />
	<map name="Map" id="Map">
	  <area shape="rect" coords="21,3,73,22" href="#" onclick="gotoDesktop();"  title="��ҳ" />
	  <area shape="rect" coords="87,3,141,23" href="#" onclick="MM_logout();" title="ע��ϵͳ" />
	  <area shape="rect" coords="151,4,202,23" href="#" onclick="MM_exit();" title="�˳�ϵͳ" />
	</map>
	</div>
    
    <div id="nav">
      <div id="navleft" nowrap>�����ߣ�<span class="who"><c:out value="${theForm.loginUserName}"/></span>&nbsp;&nbsp;&nbsp;&nbsp; ͣ��ʱ�䣺<span id="stayTime"></span></div>
      <div id="navmid">&nbsp;&nbsp;&nbsp;&nbsp;��ǰλ�ã�<span id="location">&nbsp;</span></div>
	      <div id="navright">
      <div id="divId_operaButton">
	      	<input type="button" class="btnnew1" onclick="TopOpera.createChangeValue()" value=" " />
	      	<input type="button" class="btnsave1" onclick="TopOpera.saveChangeValue()" value=" " />
	      	<input type="button" class="btnsbt1" onclick="TopOpera.submit()" value=" " />
	        <input type="button" class="top_opera_prePage"  style="display:none"  />
			<input type="button" class="top_opera_nextPage" style="display:none" />
		    <input type="button" style="display:none"/>
		    <input type="button" style="display:none"/>
	      </div>
      </div>
    </div>
  </div>
</div>

<iframe  src="<c:out value = "${initParam['publicResourceServer']}/uf/login/MainPageAction.do?step=getMain&mainUrl=${theForm.mainUrl}" />" id="under" name="under" height="83%" width="100%" frameborder="0" scrolling="no" onkeydown="KeyDown()" oncontextmenu="event.returnValue=false">
</iframe>

</body>

<script language=javascript type=text/javascript>

var beginDate = new Date();
function getLoginTime(){
	var currentDate = new Date();
	var stayTime = currentDate.getTime() - beginDate.getTime();
	
	var tempDate = new Date();
	tempDate.setTime(stayTime);

	var hour = tempDate.getHours() - 8;
	var minute = tempDate.getMinutes();
	var second = tempDate.getSeconds();
	
	if(hour < 10) hour = '0' + hour;
	if(minute < 10) minute = '0' + minute;
	if(second < 10) second = '0' + second;
	
	var temp = hour + ":" + minute + ":" + second;

    window.status = "��ӭʹ�÷�����ҵ��Դ�����׼����������";
    window.title = window.status;
    if (typeof document.getElementById("stayTime") == 'object')
        document.getElementById("stayTime").innerHTML =  temp;  
    setTimeout("getLoginTime()", 1000);  
}
getLoginTime();
/*-----------------------------------------------------------------------------------
|������  ��Global.afterOnload()
|�������ܣ�ҳ�������ɺ���еĴ���(�����ʾ˳��ֵΪ�գ�������Ϊֻ��)��
|----------------------------------------------------------------------------------*/
Global.afterOnload = function(){
//    getLoginTime();
}
/**
 * ʹ�� AJAX ѭ�� Logoff ҵ��ϵͳ 
 */
function MM_logout(){
	if (! confirm('��ȷ��Ҫע��ϵͳ��?')) {
		return;
	}	
	top.location.href = '<c:url value = '/logoff' />';
}

function MM_exit(){
    window.close(true);
}
gotoDesktop = function(){
    document.getElementById("location").innerHTML="";
    var ifm=window.frames["under"].window.frames["mainFrame"];
    ifm.window.location.reload("<c:out value="${initParam['publicResourceServer']}"/>/uf/desk/TblColumnDisplayAction.do?step=listColumn");
}
/**
 * to show stay time of user
 */ 
var beginDate = new Date();
function resetTime(){
	var currentDate = new Date();
	var stayTime = currentDate.getTime() - beginDate.getTime();
	
	var tempDate = new Date();
	tempDate.setTime(stayTime);

	var hour = tempDate.getHours() - 8;
	var minute = tempDate.getMinutes();
	var second = tempDate.getSeconds();
	
	if(hour < 10) hour = '0' + hour;
	if(minute < 10) minute = '0' + minute;
	if(second < 10) second = '0' + second;
	
	var temp = hour + ":" + minute + ":" + second;
	if(typeof document.getElementById("stayTime") == 'object')
		document.getElementById("stayTime").innerText = temp;	
	setTimeout("resetTime()", 1000);
}
//resetTime();


var TopOpera = {};
//����ǰ����ҳ��ֵ�Ƿ��޸�
TopOpera.createChangeValue = function(){
	try{
		if(!top.document.frames["under"].document.frames["mainFrame"].panel.movePage()){
			return;
		}
	}
	catch(e){}
	TopOpera.create();
}

//�����ʱ�����ò�����յı�־λ
//080222�޸������ں�������ж����� wenjb
TopOpera.saveChangeValue = function(){
<!--	try{-->
<!--		//����Ҫ��յı�־λ����Ϊfalse;-->
<!--		top.document.frames["under"].document.frames["mainFrame"].document.getElementById("mustClearFormPermission").value = "false";-->
<!--		//alert("topMustClear:"+top.document.frames["under"].document.frames["mainFrame"].document.getElementById("mustClearFormPermission").value);-->
<!--	}-->
<!--	catch(e){}-->
	TopOpera.save();
}

TopOpera.create = function(){}
TopOpera.save   = function(){}
TopOpera.submit = function(){}
//TopOpera.reset  = function(){}
TopOpera.prePage = function(){}
TopOpera.nextPage= function(){}
TopOpera.preLine = function(){}
TopOpera.nextLine= function(){}

TopOpera.operaButton = document.getElementById("divId_operaButton");
var arr = TopOpera.operaButton.getElementsByTagName("*");
TopOpera.operaButtonChildren = new Array();
for(var i=0;i<arr.length;i++){
	if(arr[i].type == 'button')
		TopOpera.operaButtonChildren[TopOpera.operaButtonChildren.length] = arr[i]; 
}


//������button��Ϊdisabled
TopOpera.disabledBtn = function(){
	var divObj = TopOpera.operaButton
	var divChild = TopOpera.operaButtonChildren;
	for( i=0;i<divChild.length;i++) {

		if(!divChild[i].disabled){
			divChild[i].disabled = true;
			divChild[i].className += "_disabled";
		}
	}
}

//ָ����ť�����벻����
TopOpera.abledBtn = function(arr){
	var divObj = TopOpera.operaButton
	var divChild = TopOpera.operaButtonChildren;
	for( i=0;i<arr.length;i++) {
//		if(!divChild[i].disabled){///////////////////
				divChild[i].disabled = arr[i];
				if(!arr[i]){
					divChild[i].className = divChild[i].className.replace("_disabled","");
				}
//		}//////////////////////
	}
}
//ָ����ť������
TopOpera.setUnabledBtn = function(inum){
	var divObj = TopOpera.operaButton
	var divChild = TopOpera.operaButtonChildren;
	if(!divChild[inum].disabled){
		divChild[inum].disabled = true;
		divChild[inum].className += "_disabled";
	}
}
//ָ����ť����
TopOpera.setAbledBtn = function(inum){
	var divObj = TopOpera.operaButton;
	var divChild = TopOpera.operaButtonChildren;
	if(divChild[inum].disabled){
		divChild[inum].disabled = false;
		divChild[inum].className = divChild[inum].className.replace("_disabled","");
	}
}
//ָ����ť���ɼ�
TopOpera.setUnVisibleBtn = function(inum){
	var divObj = TopOpera.operaButton;
	var divChild = TopOpera.operaButtonChildren;
	divChild[inum].style.display="none";
}//ָ����ť����
TopOpera.setVisibleBtn = function(inum){
	var divObj = TopOpera.operaButton;
	var divChild = TopOpera.operaButtonChildren;
	divChild[inum].style.display="";
}//-->

//Ϊ�˶�̬��ʾ��ǰλ�����ӵĴ���ű�,�ڹ�����ҳ����˶���ֵ
var TopTree=null;
</script>
</html>