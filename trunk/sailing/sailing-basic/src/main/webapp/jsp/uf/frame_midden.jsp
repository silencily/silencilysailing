<jsp:directive.include file="/decorators/default.jspf"/>
<jsp:directive.page contentType="text/html; charset=GBK"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK"/>
<title></title>
<link href="<%= request.getContextPath() %>/css/style_interface.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript">
var x = '';
var y = '';
var menuWidth = 180;
function getFrameWidth()
{
	if(event.button ==1)
	{	var iWidth = event.x-x;
		menuWidth = menuWidth + iWidth;
		window.parent.buttonFrame.cols = menuWidth + ',8,*'
	}
}
/*
 * Hide or show menu function
 * @param image object which been clicked
 */
function Win_left(obj){
	// menu is shown
	if(window.parent.buttonFrame.cols == '0,8,*'){
  		// set width fo menu's frame as 180(show)
		window.parent.buttonFrame.cols = '180,8,*';
		obj.src = "<%= request.getContextPath() %>/img/controlNavTip_close.gif";
		obj.alt = "Show";
  	}else{
		// set width of menu's frame as 0(hide)
		window.parent.buttonFrame.cols = '0,8,*';
		obj.src ="<%= request.getContextPath() %>/img/controlNavTip_open.gif";
		obj.alt = "Hide"
  	}
}

/*
 * change picture when mouse moing in or out
 */
function onOver(obj){
	if(window.parent.buttonFrame.cols == '0,8,*')
		obj.src = "<%= request.getContextPath() %>/img/controlNavTip_open.gif";
	else
		obj.src = "<%= request.getContextPath() %>/img/controlNavTip_close.gif";		
}
function onOut(obj){
	if(window.parent.buttonFrame.cols == '0,8,*')
		obj.src = "<%= request.getContextPath() %>/img/controlNavTip_open.gif";
	else
		obj.src = "<%= request.getContextPath() %>/img/controlNavTip_close.gif";
}

</script>

</head>
<body>
 <div class="frame_midden" style="background-image: url(<c:out value="${initParam['publicResourceServer']}"/>/img/controlNavTip_bg.gif); "
	onmousedown='x=event.x;y=event.y;setCapture()' 
   	onmouseup='releaseCapture();' onmousemove='getFrameWidth()'>
 </div>
 <div style="position: absolute;top: 50%;">
	<img src="<%= request.getContextPath() %>/img/controlNavTip_close.gif" onclick="Win_left(this)" alt="点击这里关闭左边菜单" 
	onmouseover="onOver(this)" onmouseout="onOut(this)" style="cursor:hand" >
 </div>
</body>
</html>