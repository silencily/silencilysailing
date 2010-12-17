<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%--
    @version:$Id: scheduleCalendar.jsp,v 1.1 2010/12/10 10:56:18 silencily Exp $
    @since $Date: 2010/12/10 10:56:18 $
--%>

<%@ include file = "/decorators/tablibs.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK"/>
</head>
<link rel="stylesheet" href="<c:out value = "${initParam['publicResourceServer']}/css/style.css" />" type="text/css" media="all" />
<link rel="stylesheet" href="<c:out value = "${initParam['publicResourceServer']}/css/style_interface.css" />" type="text/css" media="all" />
<link rel="stylesheet" href="<c:out value = "${initParam['publicResourceServer']}/css/style_page.css" />" type="text/css" media="all" />
<link rel="stylesheet" href="<c:out value = "${initParam['publicResourceServer']}/css/style_buttom.css" />" type="text/css" media="all" />
<link rel="stylesheet" href="<c:out value = "${initParam['publicResourceServer']}/css/print.css" />" type="text/css" media="all" />
<link rel="stylesheet" href="<c:out value = "${initParam['publicResourceServer']}/css/style_buttom_opera.css" />" type="text/css" media="all" />
<link rel="stylesheet" href="<c:out value = "${initParam['publicResourceServer']}/css/style_module.css" />" type="text/css" media="all" />

<script type="text/javascript" src = "<c:out value = "${initParam['publicResourceServer']}/public/scripts/contextInfo.js" />"></script>
<script type="text/javascript">
ContextInfo.contextPath = '<c:out value="${initParam['publicResourceServer']}"/>'
</script>
<script type="text/javascript" src = "<c:out value = "${initParam['publicResourceServer']}/public/scripts/styleControl.js" />"></script>
<script type="text/javascript" src = "<c:out value = "${initParam['publicResourceServer']}/public/scripts/xmlhttp.js" />"></script>
<script type="text/javascript" src = "<c:out value = "${initParam['publicResourceServer']}/public/scripts/dateUtils.js" />"></script>
<script type="text/javascript" src = "<c:out value = "${initParam['publicResourceServer']}/public/scripts/staticTree.js" />"></script>
<script type="text/javascript" src = "<c:out value = "${initParam['publicResourceServer']}/public/scripts/global.js" />"></script>
<script type="text/javascript" src = "<c:out value = "${initParam['publicResourceServer']}/public/scripts/treeUtils.js" />"></script>
<script type="text/javascript" src = "<c:out value = "${initParam['publicResourceServer']}/public/scripts/panel.js" />"></script>
<script type="text/javascript" src = "<c:out value = "${initParam['publicResourceServer']}/public/scripts/calendar.js" />"></script>
<script type="text/javascript" src = "<c:out value = "${initParam['publicResourceServer']}/public/scripts/formUtils.js" />"></script>
<script type="text/javascript">

var dateObj = {};

var workString= "<c:out value='${theForm.memo}'/>" ;
var work=workString.substring(1, workString.length-1);
var items =work.split(',');

function initiate(){
	if(top.definedWin && top.definedWin.isLoading){
		top.definedWin.closeLoading();
	}
	Global.topOpera.setUnVisibleBtn(0);
   	Global.topOpera.setUnVisibleBtn(1);
   	Global.topOpera.setUnVisibleBtn(2);
	var curDay = document.getElementById("currentDay").value;
	dateObj = new Calendar.DisplayDate("dateObj","divId_calendar");
	dateObj.InitDate(curDay,items);
}

Calendar.meizzDayDoubleClick=function (n,ex, obj)
{
	this.Click (n,ex,obj) ;
    document.getElementById("currentDay").value=this.selectDate;
    if (parent.panel )
    {
		parent.panel.setNewUrl(document.getElementById("currentDay").value);
		parent.panel.click(1);
    }
}

Calendar.ForwardActionUrl = function(today)
{
	document.getElementById("currentDay").value=today;
	FormUtils.post(document.forms[0], 'ScheduleAction.do?step=calendar');
}

Calendar.meizzDayClick = function(n,ex,obj)  //点击显示框选取日期，主输入函数************* 
{
     this.setSelectClass(obj);
     var yy=this.meizzTheYear;
     var mm = parseInt(this.meizzTheMonth,10)+ex;   //ex表示偏移量，用于选择上个月份和下个月份的日期
     //判断月份，并进行对应的处理
     if(mm<1){
         yy--;
         mm=12+mm;
     }
     else if(mm>12){
         yy++;
         mm=mm-12;
     }
     n=n.substring(0,2);  //日期
     this.setSelctDate(yy,mm,n); 
     document.getElementById("currentDay").value=this.selectDate;
	 parent.panel.setNewUrl(document.getElementById("currentDay").value);
}

</script>

<body class="list_body" onload="javascript:initiate();Global.setHeight();">
	<form name="instrument_entry" method="post" action="">
	<input type="hidden" name="currentDay" value="<c:out value='${theForm.currentDay}'/>"/>
		<div id="divId_calendar"  ></div>	
	
	</form>
</body>
</html>