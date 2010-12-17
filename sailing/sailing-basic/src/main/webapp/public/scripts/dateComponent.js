/*
 * 日期控件
 * 调用方式： <input type="text" name="gradute" onblur="DateComponent.checkDay(this)">
   <input name="button" type="button" class="button" onclick="DateComponent.setDay(this,gradute)" value="..."/>
 *
 */

if (DateComponent == null) {
	var DateComponent  = {};
}


//src="'+ ContextInfo.contextPath + '/public/scripts/empty.htm"
document.writeln('<iframe id=ifrmId_DateLayer name="ifrmId_DateLayer" src="about:blank" Author=wayx frameborder=0 '+
			 'style="position: absolute; width: 100%; height: 100%; z-index: 5; display: none;" '+			 
			 'scrolling=yes></iframe>');
document.close();


/* 日期控件 对象
 * 2006-04-15
 * 通过建立日期控件对象，达到参数的局部化和可重用性，进行代码优化
 * 每个页面建立一个日期对象即可
 */

function dateObject(name){
	this.name   = name;
	this.popWin = '';
	this.init   = new Date();   //初始化日期，指定一个日期传进来
	this.lineColor = "#bbbbbb"; //this.lineColor
	this.bgColor   = "#eeeeee"; //this.bgColor
	this.defColor  = "#ffffff"; //this.defColor
	this.restColor = "#dddddd";
	this.textObj  = null;       //输入域对象
	this.btnObj   = null;      //点击按钮对象

	this.timeStr   = "";        //临时时间参数
	this.outDate   = '';        //中转变量，用于存放对象的日期 outDate
	this.getTime   = '' ;       //存放时间obj
	this.dateLayer = null;	    //存放日历内容对象
	this.lastValue = '';
	this.isIE6     = false;
	this.viewSec   = true;      //是否显示秒数，默认显示
	
	/*存储当前返回数据类型和时间控件显示方式 5： 月 6： 季度 7： 年 wenjb@bis.com.cn 2008-4-30 Start*/
	this.specialDayType = ''; 
	/*存储当前返回数据类型和时间控件显示方式 5： 月 6： 季度 7： 年 wenjb@bis.com.cn 2008-4-30 End*/
	
	//this.build   = buildDate;   //生成日期控件
	this.display = displayDate; //显示日期控件
	//this.click   = clickDate;   //选取日期
	this.close   = closeLayer;  //关闭控件
	this.currTime= getCurrentTime;

	this.GetFormatYear = GetFormatYear;
	this.GetMonthDays  = GetMonthDays;
	this.SetDateFormat = SetDateFormat;
	this.GetTextDate   = GetTextDate;
	this.getSelectTimeStr = getSelectTimeStr;
	this.IsPinYear    = IsPinYear;
	this.GetMonthCount= GetMonthCount;
	this.GetDOW       = GetDOW;
	this.meizzWriteHead = meizzWriteHead;
	this.meizzNextNum = meizzNextNum;
	this.meizzPrevY   = meizzPrevY;
	this.meizzNextY   = meizzNextY;
	this.meizzScrollNextY   = meizzScrollNextY;
	this.meizzScrollPrevY   = meizzScrollPrevY;
	this.stopScrollY   = stopScrollY;
	this.changeScrollY  = changeScrollY;
	this.meizzToday   = meizzToday;
	this.meizzPrevM   = meizzPrevM;
	this.meizzNextM   = meizzNextM;
	
	this.meizzPrevS   = meizzPrevS;
	this.meizzNextS   = meizzNextS;
	
	this.isCurrentDate= isCurrentDate;
	this.meizzSetDay  = meizzSetDay;
	this.selectClickDay = selectClickDay;
	this.clearDay = clearDay;
	this.meizzDayClick  = meizzDayClick;
	this.setFormatTimeStr = setFormatTimeStr;
	this.selectTimeNum  = selectTimeNum;
	this.setDateInputValue= setDateInputValue;
	this.afterSelectDate = DateComponent.afterSelectDate; //点击事件
	this.updateTime = updateTime;	// heyh add
	
	this.DateFormat = "<yyyy>-<mm>-<dd>";
	this.MonthName = new Array();
		this.MonthName[0] = "一";this.MonthName[1] = "二";this.MonthName[2] = "三";this.MonthName[3] = "四";
		this.MonthName[4] = "五";this.MonthName[5] = "六";this.MonthName[6] = "七";this.MonthName[7] = "八";
		this.MonthName[8] = "九";this.MonthName[9] = "十";this.MonthName[10] = "十一";this.MonthName[11] = "十二";
	
	this.SeasonName = new Array();
		this.SeasonName[0] = "一";this.SeasonName[1] = "二";this.SeasonName[2] = "三";this.SeasonName[3] = "四";
	
	this.MonHead  = new Array(12); //定义阳历中每个月的最大天数
		this.MonHead[0] = 31; this.MonHead[1] = 28; this.MonHead[2] = 31; 
		this.MonHead[3] = 30; this.MonHead[4] = 31; this.MonHead[5] = 30;
		this.MonHead[6] = 31; this.MonHead[7] = 31; this.MonHead[8] = 30; 
		this.MonHead[9] = 31; this.MonHead[10]= 30; this.MonHead[11]= 31;
	this.meizzTheYear  = this.init.getFullYear();    //定义年的变量的初始值
	this.meizzTheMonth = this.init.getMonth()+1;     //定义月的变量的初始值
	this.meizzTheSeason =  Math.ceil(this.meizzTheMonth/3);     //定义月的变量的初始值
	this.meizzWDay     = new Array(42);               //定义写日期的数组

	//init

	if(typeof ContextInfo.checkExplorer != 'undefined' && ContextInfo.checkExplorer(5.5)){
		this.isIE6    = true;
		this.popWin   = window.createPopup(); //
		this.dateLayer= this.popWin.document;	
	}else{
		this.dateLayer = frames["ifrmId_DateLayer"];
	}
	
	//初始选中了小时
	this.tempClick = null

}







//== WEB 页面显示部分
function displayDate(viewSec){
	if(viewSec == false){this.viewSec=viewSec;}else{this.viewSec=true;}//是否显示时间设置
	
	var url = ContextInfo.contextPath;
	//var url = ".";

	var strFrame='';		//存放日历层的HTML代码
	strFrame='<html><head><style>';
	strFrame+='td {font-size: 9pt;font-family:宋体;}a{font-size: 10pt;color:#000000;text-decoration:none;}';
	strFrame+='.date_switch{cursor:pointer;height:10px}';
	strFrame+='.date_body  {width:200px;border-left:1px solid #aaa;border-top:1px solid #aaa;background-color:'+this.bgColor+';}';
	strFrame+='.date_body td{border-bottom:1px solid #aaa;border-right:1px solid #aaa;height:22px;cursor:pointer}';
	strFrame+='.date_td0  {background-color:'+this.defColor+'}';
	strFrame+='.date_td1  {background-color:#888;color:#fff}';
	strFrame+='.date_head td{font-size:11px;color:#fff;height:22px;background-color:'+this.lineColor+'}';
	strFrame+='.date_head1 {color:#000;background-color:'+this.restColor+'}';	
	strFrame+='.date_bottom{padding-right:5px;margin-top:5px;height:25px;background-color:'+this.bgColor+'}';
	strFrame+='.date_tab {margin-top:8px;margin-bottom:5px}';
	strFrame+='.td_cell  {border:1px solid #aaa;width:60px;height:22px;text-align:center}';
	strFrame+='.td_time  {font-size:12px;height:20px;width:20px;text-align:center}';
	strFrame+='.td_time1 {font-size:12px;background-color:#ddd;width:20px;text-align:center;cursor:pointer}';
	strFrame+='.td_time2 {font-size:13px;}';
	strFrame+='.div_btn  {margin:2px;padding:2px 3px 2px 3px;float:right;border:1px solid #aaa}';
	strFrame+='.div_time1{margin-top:1px;margin-right:5px;float:right}';
	strFrame+='.div_time2{float:right;border:1px solid #aaa}';
	strFrame+='.div_body {margin:0px;overflow:hidden;border:1px solid '+this.lineColor+'}';
    strFrame+='body {overflow:hidden;border:0px;margin:0px 3px 3px 0px;padding:0px;}';
	strFrame+='</style>';
	strFrame+='</head><body><div class="div_body" onselectstart="return false" id="timectrl">';
	strFrame+='<table border=0 cellspacing=0 cellpadding=0 width=100% height=100% >';
// heyh --- start
//	strFrame+='  <tr><td align="center">';

	if(dateObj.specialDayType=='12'||dateObj.specialDayType=='13'||dateObj.specialDayType=='14'){
    	strFrame+='    <tr id="tblYM"><td height="30" align="center">';
    }else{
    	strFrame+='  <tr id="tblYM"><td align="center">';
    }
// heyh --- end
	strFrame+='		<table border=0 cellspacing=2 cellpadding=0 class="date_tab">';
	strFrame+='       <tr>';
	
	/*月 季度 年 处理 wenjb@bis.com.cn 2008-4-30 Start*/
	if(dateObj.specialDayType=='13'||dateObj.specialDayType=='14'){
		if(dateObj.specialDayType=='13'){
			//alert('dateObj.specialDayType==13');
			strFrame+='         <td class="td_cell" id="tabId_innerseason">';
			strFrame+='				<span id=meizzSeasonHead></span>';
			strFrame+='			</td><td>';
			strFrame+='				<img src="'+url+'/image/common/date_up.gif"   onclick="parent.dateObj.meizzNextS()" title="向后翻 1 季" class="date_switch"/><br/>';
			strFrame+='				<img src="'+url+'/image/common/date_down.gif" onclick="parent.dateObj.meizzPrevS()" title="向前翻 1 季" class="date_switch"/>';
			strFrame+='         </td>';
		}
		
	}else{
		strFrame+='         <td class="td_cell" id="tabId_inneryear">';
		strFrame+='				<span id=meizzMonthHead></span>';
		strFrame+='			</td><td>';
		strFrame+='				<img src="'+url+'/image/common/date_up.gif"   onclick="parent.dateObj.meizzNextM()" title="向后翻 1 月" class="date_switch"/><br/>';
		strFrame+='				<img src="'+url+'/image/common/date_down.gif" onclick="parent.dateObj.meizzPrevM()" title="向前翻 1 月" class="date_switch"/>';
		strFrame+='         </td>';
	}
	
	strFrame+='			<td class="td_cell" >';
	strFrame+='				<span id=meizzYearHead></span>';
	strFrame+='			</td><td> ';
	strFrame+='				<img src="'+url+'/image/common/date_up.gif" onmousedown="parent.dateObj.meizzScrollNextY();" onmouseup="parent.dateObj.stopScrollY();" onmouseout="parent.dateObj.stopScrollY();" onclick="parent.dateObj.meizzNextY()" title=" 1 " class="date_switch"/><br/>';
	strFrame+='				<img src="'+url+'/image/common/date_down.gif" onmousedown="parent.dateObj.meizzScrollPrevY();" onmouseup="parent.dateObj.stopScrollY();" onmouseout="parent.dateObj.stopScrollY();" onclick="parent.dateObj.meizzPrevY()" title="前 1 " class="date_switch"/>';
	 
		/***************年份显示***********************
	strFrame+='			<td class="td_cell"><span id="meizzYearHead" style="display:none;" ></span>';
    strFrame+='         <select  name=tmpSelectYear style="font-size:12px;" title="点击这里选择年份"'
    strFrame+='          onchange="parent.dateObj.meizzTheYear=this.value; parent.dateObj.meizzSetDay(this.value, parent.dateObj.meizzTheMonth);" >';  
    var selectInnerHTML="";
    //设置显示的年份
    var m= (this.textObj.value)? this.textObj.value.substring(0,4):new Date().getFullYear();
    for (var i = 1900; i < 2100; i++)
    {
          if (i==m)
            selectInnerHTML += "<option value='" + i + "' selected>" + i + "年" + "</option>\r\n";
          else   
          selectInnerHTML += "<option value='" + i + "'>" + i + "年" + "</option>\r\n";
    }
    strFrame+=selectInnerHTML+ '</select> ' ;	
   //*************************************************/
 	strFrame+='        </td></tr>';
	strFrame+='     </table>';
	strFrame+=' </td></tr>';

    
	if(dateObj.specialDayType=='12'||dateObj.specialDayType=='13'||dateObj.specialDayType=='14'){
		//alert('dateObj.specialDayType==12,13,14');
	}else{
		strFrame+='  <tr id="tblD"><td align="center">';
		strFrame+='  <table border=0 cellspacing=0 cellpadding=0 class="date_body">';
		strFrame+='		<tr  align=center valign=bottom class="date_head">';
		strFrame+='			<td class="date_head1">日</td><td >一</td> <td >二</td>';
		strFrame+='			<td >三</td> <td >四</td><td >五</td><td class="date_head1">六</td></tr>';
		var n=0; 
		for (j=0;j<5;j++){ 
			strFrame+= ' <tr align=center >'; 
			for (i=0;i<7;i++){
				strFrame+='<td id=meizzDay'+n+' )>'+n+'</td>';
				n++;
			}
			strFrame+='</tr>';
		}
		strFrame+='      <tr align=center >';
		for (i=35;i<42;i++)
			strFrame+='<td id=meizzDay'+i+'></td>';
		//strFrame+='        <td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>';
		strFrame+='        </tr></table></td></tr>';

	}

	//拼装时间

	strFrame+='        <tr><td height="30" align="right">';

	if(this.getTime){
		var temp = this.currTime(this.timeStr);
// heyh -- start
		strFrame+='		<div class="div_time1"><img src="'+url+'/image/common/date_up.gif"   onclick="parent.dateObj.meizzNextNum(1)" title="增加选中数值" class="date_switch"/><br/>';
		strFrame+='			 <img src="'+url+'/image/common/date_down.gif" onclick="parent.dateObj.meizzNextNum(-1)" title="减少选中数值" class="date_switch"/></div>';
//		strFrame+='		<div class="div_time1">';
//		strFrame+='		<OBJECT id="MyScroll" classid="CLSID:DFD181E0-5E2F-11CE-A449-00AA004A803D" height="22" width="15">';
//		strFrame+='		<param name="Orientation" value="0">';
//		strFrame+='		</OBJECT>';
//		strFrame+='		</div>';
// heyh -- end
		strFrame+='		 <div class="div_time2"><table border=0 cellspacing=0 cellpadding=1>';
		strFrame+='      <tr>';
// heyh -- start
//		strFrame+='         <td class="td_time1" id=spanId_dateHour  onclick="parent.dateObj.selectTimeNum(this)" title="选中这里再点击后面的按钮调节小时的数值">';
		strFrame+='         <td class="td_time1" id=spanId_dateHour  onclick="parent.dateObj.selectTimeNum(this)" title="选中这里再点击后面的按钮调节小时的数值" min="-1" max="24">';
// heyh -- end
		strFrame+='				'+temp[0]+'';
		strFrame+='			</td><td class="td_time2"> ';
		strFrame+='			 :';
// heyh -- start
//		strFrame+='			</td><td class="td_time" id=spanId_dateMinu onclick="parent.dateObj.selectTimeNum(this)" title="选中这里再点击后面的按钮调节分钟的数值">';
		strFrame+='			</td><td class="td_time" id=spanId_dateMinu onclick="parent.dateObj.selectTimeNum(this)" title="选中这里再点击后面的按钮调节分钟的数值" min="-1" max="60">';
// heyh -- end
		strFrame+='				'+temp[1]+'';
		strFrame+='			</td><td class="td_time2"'+(this.viewSec?'':' style="display:none"')+'> ';
		strFrame+='			 :';
		strFrame+='			</td><td class="td_time"'+(this.viewSec?'':' style="display:none"')+' id=spanId_dateSeco>';
		strFrame+='				'+temp[2]+'';
		strFrame+='        </td></tr>';
		strFrame+='      </table></div>';
	}

	strFrame+='        </td></tr>';
	strFrame+='        <tr><td class="date_bottom">';
	strFrame+='				<div class="div_btn"><a href="#" onclick="parent.dateObj.close()" ';
	strFrame+='					title="点击这里关闭控件">取 消</a></div>';	
// heyh --- start
//	strFrame+='				<div class="div_btn"><a href="#" onclick="parent.dateObj.meizzToday()" title="当前日期">今 天</a></div> ';
	strFrame+='				<div class="div_btn" id="btnToday"><a href="#" onclick="parent.dateObj.meizzToday()" title="当前日期">今 天</a></div> ';
// heyh --- end
	strFrame+='				<div class="div_btn"><a href="#" onclick="parent.dateObj.selectClickDay()">选 取</a></div> ';
	strFrame+='				<div class="div_btn"><a href="#" onclick="parent.dateObj.clearDay()">清 空</a></div> ';
// heyh --- start
	strFrame+='			</td></tr></table></div></body></html>';
/*	strFrame+='			</td></tr></table></div></body>\n';
	strFrame+='<SCR' + 'IPT LANGUAGE="VBScript">\n';
	strFrame+='Sub MyScroll_Change()\n';
	strFrame+=' Dim scrollValue\n';
	strFrame+=' scrollValue = document.all("MyScroll").Value\n';
	strFrame+='	If document.all("MyScroll").Value = parent.dateObj.tempClick.innerText - 1 Then\n';
	strFrame+='		If document.all("MyScroll").Value = document.all("MyScroll").max - 2 Then\n';
	strFrame+='			scrollValue = parent.dateObj.tempClick.innerText\n';
	strFrame+='		Else\n';
	strFrame+='			scrollValue = CInt(document.all("MyScroll").Value) + 2\n';
	strFrame+='		End if\n';
	strFrame+='	Else\n';
	strFrame+='		If document.all("MyScroll").Value = parent.dateObj.tempClick.innerText + 1 Then\n';
	strFrame+='			If document.all("MyScroll").Value = document.all("MyScroll").min + 2 Then\n';
	strFrame+='				scrollValue = parent.dateObj.tempClick.innerText\n';
	strFrame+='			Else\n';
	strFrame+='				scrollValue = CInt(document.all("MyScroll").Value) - 2\n';
	strFrame+='			End if\n';
	strFrame+='		End if\n';
	strFrame+='	End if\n';
	strFrame+='	If scrollValue < 10 Then\n';
	strFrame+='		parent.dateObj.tempClick.innerText = "0" & scrollValue\n';
	strFrame+='	Else\n';
	strFrame+='		parent.dateObj.tempClick.innerText = scrollValue\n';
	strFrame+='	End if\n';
	strFrame+=' document.all("MyScroll").Value = scrollValue\n';
	strFrame+='	call parent.dateObj.updateTime\n';
	strFrame+='End Sub\n';
	strFrame+='</SCR' + 'IPT>\n';
	strFrame+='			</html>';
*/

// heyh --- end
	//<a href="#" onclick="parent.getWinHelp();return false" title=\'点击这里查看时间控件帮助\'>帮助</a>;
	if(this.isIE6){		
		this.popWin.document.open();
		this.popWin.document.writeln(strFrame); 
		this.popWin.document.close();
		this.popWin.document.body.style.overflow = 'hidden'
		this.popWin.document.body.style.border   = '0';
	}else{
		var obj = window.frames.ifrmId_DateLayer;
		obj.document.open();	
		obj.document.writeln(strFrame);
		obj.document.close();//解决ie进度条不结束的问题	
	}
	this.tempClick = this.dateLayer.getElementById("spanId_dateHour");
// heyh --- start
//	selectTimeNum(this.dateLayer.getElementById("spanId_dateHour"));
/*	if (this.dateLayer.all["MyScroll"]) {
		this.dateLayer.all["MyScroll"].min = this.tempClick.min;
		this.dateLayer.all["MyScroll"].max = this.tempClick.max;
		this.dateLayer.all["MyScroll"].value = this.tempClick.innerText;
	}
*/


	if (typeof this.getTime == 'object') {
		this.dateLayer.all["tblYM"].style.display = "none";
		this.dateLayer.all["tblD"].style.display = "none";
		this.dateLayer.all["btnToday"].style.display = "none";
	}

// heyh --- end

	/*月 季度 年 处理 wenjb@bis.com.cn 2008-4-30 Start*/
	if (dateObj.specialDayType=='12'||dateObj.specialDayType=='13'||dateObj.specialDayType=='14') {
		    this.dateLayer.all["btnToday"].style.display = "none";
	}
	/*月 季度 年 处理 wenjb@bis.com.cn 2008-4-30 End*/
	//alert('displayOver');
}

function getCurrentTime(str){
	var temp = new Array();
	if(str != '')
		temp = this.setFormatTimeStr(str).split(":");
	else{
		temp[0]= this.init.getHours()<10?'0'+this.init.getHours():this.init.getHours();
		temp[1]= this.init.getMinutes()<10?'0'+this.init.getMinutes():this.init.getMinutes();
		//temp[2]= new Date().getSeconds()<10?'0'+new Date().getSeconds():new Date().getSeconds();
		temp[2]="00";
		this.timeStr = temp[0]+":"+temp[1]+":"+temp[2];
	}
	return temp
}

//format theYear to 4 digit
function GetFormatYear(theYear){   
	var tmpYear = theYear;
	if (tmpYear < 100){
		tmpYear += 1900;
		if (tmpYear < 1970){
			tmpYear += 100;
		}
	}
	if (tmpYear < this.MinYear){
		tmpYear = this.MinYear;
	}
	if (tmpYear > this.MaxYear){
		tmpYear = this.MaxYear;
	}
	return(tmpYear);
}
function GetMonthDays(theYear, theMonth){ //get theYear and theMonth days number
	var theDays = new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
	var theMonthDay = 0, tmpYear = this.GetFormatYear(theYear);
	theMonthDay = theDays[theMonth];
	if (theMonth == 1){ //theMonth is February
		if(((tmpYear % 4 == 0) && (tmpYear % 100 != 0)) || (tmpYear % 400 == 0)){
			theMonthDay++;
		}
	}
	return(theMonthDay);
}

//format a date to this.DateFormat 
function SetDateFormat(theYear, theMonth, theDay){
	var theDate  = this.DateFormat;
	 var tmpYear = this.GetFormatYear(theYear);
	 var tmpMonth = theMonth;
	 if (tmpMonth < 0){
	   tmpMonth = 0;
	 }
	 if (tmpMonth > 11){
	   tmpMonth = 11;
	 }
	 var tmpDay = theDay;
	 if (tmpDay < 1){
	   tmpDay = 1;
	 }else{
		  tmpDay = this.GetMonthDays(tmpYear, tmpMonth);
		  if (theDay < tmpDay){
		  tmpDay = theDay;
	   }
	 }

    theDate = theDate.replace(/<yyyy>/g, tmpYear.toString());
    theDate = theDate.replace(/<yy>/g, tmpYear.toString().substr(2,2));
    //theDate = theDate.replace(/<MMMMMM>/g, this.MonthName[tmpMonth]);
    //theDate = theDate.replace(/<MMM>/g, this.MonthName[tmpMonth].substr(0,3));
    if (theMonth < 9){
		theDate = theDate.replace(/<mm>/g, "0" + (tmpMonth + 1).toString());
    }else{
		theDate = theDate.replace(/<mm>/g, (tmpMonth + 1).toString());
    }
	theDate = theDate.replace(/<m>/g, (tmpMonth + 1).toString());
    if (theDay < 10){
		theDate = theDate.replace(/<dd>/g, "0" + tmpDay.toString());
    }else{
		theDate = theDate.replace(/<dd>/g, tmpDay.toString());
    }
    theDate = theDate.replace(/<d>/g, tmpDay.toString());
    return(theDate);
}


//convert a string to a date, 
//if the string is not a date, return a empty string
function GetTextDate(theString){ 
	var da = new Date(tmpYear, tmpMonth, tmpDay);
	if(da)
		return da;
	else
		return '';
}

//点击选中要增减的项
function selectTimeNum(obj){
	this.tempClick.className = 'td_time';
	this.tempClick = obj;
	this.tempClick.className = 'td_time1';

// heyh --- start
//	this.dateLayer.all["MyScroll"].min = this.tempClick.min;
//	this.dateLayer.all["MyScroll"].max = this.tempClick.max;
//	this.dateLayer.all["MyScroll"].value = this.tempClick.innerText;
// heyh --- end
}

//==对选定的数字增加或者减少
function meizzNextNum(inum){
	//if(!this.tempClick);
	//	this.tempClick = this.dateLayer.getElementById("spanId_dateHour");
	var i = this.tempClick.innerText;
	i = parseInt(i,10)+inum;
	switch(this.tempClick.id){
		case "spanId_dateHour":
			i = i > 23 ? 0 : i;
			i = i < 0  ? 23: i;
			break;
		default://TODO:分钟改变可能改变小时数
			i = i > 59 ? 0 : i;
			i = i < 0  ? 59: i;
			break;
	}
	i = i < 10 ? "0"+i : i;
	this.tempClick.innerText = i;
	var str1 = this.dateLayer.getElementById("spanId_dateHour").innerText;
	var str2 = this.dateLayer.getElementById("spanId_dateMinu").innerText;
	var str3 = this.dateLayer.getElementById("spanId_dateSeco").innerText;
	this.timeStr = setFormatTimeStr(str1)+":"+setFormatTimeStr(str2)+":"+setFormatTimeStr(str3);
}

// heyh --- start
function updateTime(){
	var str1 = this.dateLayer.getElementById("spanId_dateHour").innerText;
	var str2 = this.dateLayer.getElementById("spanId_dateMinu").innerText;
	var str3 = this.dateLayer.getElementById("spanId_dateSeco").innerText;
	this.timeStr = setFormatTimeStr(str1)+":"+setFormatTimeStr(str2)+":"+setFormatTimeStr(str3);
}
// heyh --- end

//== 往 head 中写入当前的年与月或者季度
function meizzWriteHead(yy,mm){
	this.dateLayer.getElementById("meizzYearHead").innerText  = yy + " 年";
	if(dateObj.specialDayType=='13'||dateObj.specialDayType=='14'){
		if(dateObj.specialDayType=='13'){
			this.dateLayer.getElementById("meizzSeasonHead").innerText = this.SeasonName[mm-1] + " 季";
		}
	}else{
		this.dateLayer.getElementById("meizzMonthHead").innerText = this.MonthName[mm-1] + " 月";
	}
	
}

//== 判断是否闰平年
function IsPinYear(year){
    if (0==year%4&&((year%100!=0)||(year%400==0))) 
		return true;
	else 
		return false;
}

//== 闰年二月为29天
function GetMonthCount(year,month)  
{
    var c=this.MonHead[month-1];if((month==2)&&this.IsPinYear(year)) c++;return c;
}
//== 求某天的星期几
function GetDOW(day,month,year)     
{
    var dt=new Date(year,month-1,day).getDay()/7; return dt;
}

//== 往前翻 Year
function meizzPrevY(){
	this.meizzTheYear--;
	if(this.specialDayType=='13'){
		this.meizzSetDay(this.meizzTheYear,this.meizzTheSeason);
	}else{
		this.meizzSetDay(this.meizzTheYear,this.meizzTheMonth);
	}
}
//== 往后翻 Year
function meizzNextY(){
    this.meizzTheYear++;
	if(this.specialDayType=='13'){
		this.meizzSetDay(this.meizzTheYear,this.meizzTheSeason);
	}else{
		this.meizzSetDay(this.meizzTheYear,this.meizzTheMonth);
	}
}
var timeout;
var interval;
function meizzScrollPrevY(){
	//alert(dateObj.dateLayer);
	timeout = setTimeout("dateObj.changeScrollY('dateObj.meizzPrevY')",300);
}
function meizzScrollNextY(){
	timeout = setTimeout("dateObj.changeScrollY('dateObj.meizzNextY')",300);
}
function changeScrollY(method){
	interval = setInterval(method+"(dateObj)",100);
}
function stopScrollY(){
	if(interval != null)
		clearInterval(interval);
	if(timeout != null)
		clearTimeout(timeout);
}
//== Today Button
function meizzToday(){
	var d;
    this.meizzTheYear  = this.init.getFullYear();
    this.meizzTheMonth = this.init.getMonth()+1;
    d=this.init.getDate();
    //meizzSetDay(this.meizzTheYear,meizzTheMonth);
    if(this.textObj){
		this.textObj.value=this.meizzTheYear + "-" + (this.meizzTheMonth > 9 ? this.meizzTheMonth: "0" + this.meizzTheMonth) + "-" + (d > 9 ? d: "0" + d);
    }
    if(this.getTime){
    	var timeStrTemp = this.viewSec?this.setFormatTimeStr(this.timeStr):this.setFormatTimeStr(this.timeStr).substring(0,5)
		this.textObj.value = this.textObj.value.substring(0,10) + " "+ timeStrTemp
	}
    this.close();
}
//== 往前翻月份
function meizzPrevM()  
{
    if(this.meizzTheMonth>1){this.meizzTheMonth--}else{this.meizzTheYear--;this.meizzTheMonth=12;}
    this.meizzSetDay(this.meizzTheYear,this.meizzTheMonth);
}
//== 往后翻月份
function meizzNextM()  
{
    if(this.meizzTheMonth==12){this.meizzTheYear++;this.meizzTheMonth=1}else{this.meizzTheMonth++}
    this.meizzSetDay(this.meizzTheYear,this.meizzTheMonth);
}

//== 往前翻季度
function meizzPrevS()  
{
    if(this.meizzTheSeason>1){this.meizzTheSeason--}else{this.meizzTheYear--;this.meizzTheSeason=4;}
    this.meizzSetDay(this.meizzTheYear,this.meizzTheSeason);
}
//== 往后翻季度
function meizzNextS()  
{
    if(this.meizzTheSeason==4){this.meizzTheYear++;this.meizzTheSeason=1}else{this.meizzTheSeason++}
    this.meizzSetDay(this.meizzTheYear,this.meizzTheSeason);
}

//是否是当前日期
function isCurrentDate(yy,mm,dd){
	var flag = yy == this.init.getFullYear() && mm == this.init.getMonth() && dd == this.init.getDate();
	return flag;
}

//== 主要的写程序**********
function meizzSetDay(yy,mm){
	this.meizzWriteHead(yy,mm);
	//设置当前年月的公共变量为传入值
	this.meizzTheYear=yy;
	if(dateObj.specialDayType=='13'){
		this.meizzTheSeason=mm;
	}else{
		this.meizzTheMonth=mm;
	}
	//将显示框的内容全部清空
	if(dateObj.specialDayType=='12'){
		//修改返回值
		this.lastValue = yy + "-" + mm;
	}else{
		if(dateObj.specialDayType=='13'){
			//alert('主要的写程序meizzSetDaySpecialDayType6');
			this.lastValue = yy + "-" + mm + "季";
			//alert(this.lastValue);
		}else{
			if(dateObj.specialDayType=='14'){
				this.lastValue = yy;
			}else{
				for (var i = 0; i < 42; i++){
				  this.meizzWDay[i]="";
				} 
				//某月第一天的星期几,这里从周一计算起
				var day1 = 1,day2=1,firstday = new Date(yy,mm-1,1).getDay();
				//上个月的最后几天
				for (i=0;i<firstday;i++)
					this.meizzWDay[i]=this.GetMonthCount(mm==1?yy-1:yy,mm==1?12:mm-1)-firstday+i+1;
				for (i = firstday; day1 < this.GetMonthCount(yy,mm)+1; i++){
					this.meizzWDay[i]=day1;day1++;
				}
				for (i=firstday+this.GetMonthCount(yy,mm);i<42;i++){
					this.meizzWDay[i]=day2;day2++
				}
				var da,temp,tempClass,tempdis,tempy,tempm,tempnum;
				for (i = 0; i < 42; i++){
					//书写新的一个月的日期星期排列
					da = this.dateLayer.getElementById("meizzDay"+i)
					tempClass = '';
					tempy = yy;
					tempm = mm;
					tempnum = 0;
					tempdis = false;
					//上个月的部分
					if(i<firstday){	
						tempy = mm ==1 ? yy-1: yy;
						tempm = mm ==1 ? 12  : mm-1;
						tempClass = 'date_td0';	
						tempdis = true;
						tempnum = -1;
					}else if(i>=(firstday+this.GetMonthCount(yy,mm))){		
						//下个月的部分
						tempy = mm==12?yy+1:yy;
						tempm = mm==12?1:mm+1
						tempClass = 'date_td0';	
						tempdis = true;
						tempnum = 1;
					}
					//默认本月部分
					da.title    = tempm +"月" + this.meizzWDay[i] + "日";	
					//da.disabled = tempdis;
			        da.innerHTML= this.meizzWDay[i] ;
					da.onclick  = Function("dateObj.meizzDayClick(this.innerText,"+tempnum+")");
			
					if(!this.outDate){
						tempClass = this.isCurrentDate(tempy,tempm,this.meizzWDay[i])? "date_td1":"date_td0";
						this.outDate = this.init;
					}else{			
						//if(this.isCurrentDate(tempy,tempm,this.meizzWDay[i]))
						//	tempClass = "date_td2";	
						if(this.outDate.getFullYear() == tempy && (this.outDate.getMonth()+1) == tempm && 
							this.outDate.getDate() == this.meizzWDay[i]){
							tempClass = "date_td1";
						}
					}
					da.className= tempClass;
					//if((i+2)%7 == 0 || (i+1) %7 == 0 )
					//	da.style.backgroundColor = this.restColor;
				}
				//alert(this.dateLayer.body.innerHTML);
			}
		}
	}
}

//== 点击显示框选取日期，主输入函数*************
function selectClickDay(){
	if(this.textObj && this.lastValue){	
		this.setDateInputValue();			
	}
	this.close();
}

//== 清空日期
function clearDay(){
	if(this.textObj && this.lastValue){
		if(typeof this.getTime == 'object')
		{
		FormUtils.cleanValues(this.getTime);
	}
		else 
			{
		FormUtils.cleanValues(this.textObj);
	}
	}
	this.close();
}

function meizzDayClick(n,ex){
	var yy=this.meizzTheYear;
	var mm = parseInt(this.meizzTheMonth,10)+ex;//ex表示偏移量，用于选择上个月份和下个月份的日期
	//判断月份，并进行对应的处理
	if(mm<1){
		yy--;
		mm=12+mm;
	}
	else if(mm>12){
		yy++;
		mm=mm-12;
	}
	this.outDate = new Date(yy,mm-1,n);
	if (mm < 10)
		mm = "0" + mm;
	if ( n < 10)
		n = "0" + n;
	this.lastValue = yy + "-" + mm + "-" + n;
	this.meizzSetDay(this.meizzTheYear,this.meizzTheMonth);
	//选中后关闭
	this.selectClickDay();
}

function setDateFormart(){

}

//组装指定的时间格式
function getSelectTimeStr(str,flag){
	if(this.timeStr == "")
		return;
	var temp = this.setFormatTimeStr(this.timeStr).split(":");
	temp[flag] = str;
	this.timeStr = temp[0]+":"+temp[1]+":"+temp[2];
}
function setFormatTimeStr(str){
	if(this.viewSec==false && str != "" && str.split(":").length == 2){str += ":00";}//是否显示秒数补位
	var ex = //gi;
	str = str.replace(ex,":");
	ex = /(^(\s|\u3000)*)|((\s|\u3000)*$)/gi;
	str = str.replace(ex, '');
	return str 
}
//向输入框赋值
function setDateInputValue(str){
	if(this.specialDayType == '12'){
		//alert(this.lastValue);
		var textObjTempYear = this.lastValue.substring(0,4);
		var textObjTempMonth = this.lastValue.substring(5,7);
		if(textObjTempMonth.length<2){
		  	this.textObj.value = textObjTempYear + "-" +"0" + textObjTempMonth;
		}else{
			this.textObj.value = textObjTempYear + "-" + textObjTempMonth;
		}
		//alert(this.textObj.value);
		this.afterSelectDate(str);
	}else{
		if(this.specialDayType == '13'){
				//alert('setDateInputValue + set6');
				//alert(this.lastValue);
				this.textObj.value = this.lastValue;
				//alert(this.textObj.value);
				this.afterSelectDate(str);			
		}else{
			if(this.specialDayType == '14'){
				//alert('set7');
				//alert(this.lastValue);
				//alert(this.lastValue.length);
				if (undefined != this.lastValue.length){
					if(this.lastValue.length>4){
						this.textObj.value = this.lastValue.substring(0,4);
					}
				}else{
					this.textObj.value = this.lastValue;
				}
				//alert(this.textObj.value);
				this.afterSelectDate(str);
			}else{
				//alert('setother');
				this.textObj.value = this.lastValue;
				var timeStrTemp = this.viewSec?this.setFormatTimeStr(this.timeStr):this.setFormatTimeStr(this.timeStr).substring(0,5);
				if(typeof this.getTime == 'object')
					this.getTime.value = timeStrTemp;
				else if(this.getTime)
					this.textObj.value = this.textObj.value.substring(0,10) + " "+ timeStrTemp;
				//alert(this.textObj.value);
				this.afterSelectDate(str);
			}
		}
	}
	//this.textObj.focus();	
}
//== 按Esc键关闭，切换焦点关闭
function closeLayer(){
    if(this.isIE6)
		this.popWin.hide();
	else
		this.dateLayer.style.display="none";	
}

//选中时候后返回事件
DateComponent.afterSelectDate = function(str){
	//TODO refact for current page
}


/* 
 * 以下是声明、调用方法
 */
var dateObj = new dateObject('dateObj');

/* 触发事件
 * tt      点击按钮
 * obj     保存日期obj
 * timeobj 保存时间obj 如果不是对象则时间和日期在一块 
 */
DateComponent.setDay = function(tt,obj,timeobj,viewSec){
	if (arguments.length > 2)
		dateObj.getTime = (typeof timeobj == 'object')?timeobj:1;
	else
		dateObj.getTime = '';
	if(typeof obj != 'object')
	{
	obj=timeobj;
	}
	var th = tt;
	var ttop  = obj.offsetTop;     //TT控件的定位点高
	var thei  = obj.clientHeight;  //TT控件本身的高
	var tleft = obj.offsetLeft;    //TT控件的定位点宽
	var ttyp  = tt.type;           //TT控件的类型
	while (tt = tt.offsetParent){ttop+=tt.offsetTop; tleft+=tt.offsetLeft;}
	if(!dateObj.isIE6){
		var dads  = dateObj.dateDivId.style;
		dads.top  = (ttyp=="image")? ttop+thei : ttop+thei+6;
		dads.left = tleft;
		dads.display = '';
	}
	dateObj.textObj  = (arguments.length == 1) ? th : obj;
	//alert('textObj:'+dateObj.textObj);
	dateObj.lastValue= dateObj.textObj.value;
	//alert('2lastValue&textObje.value:'+dateObj.lastValue);
	dateObj.btnObj   = (arguments.length == 1) ? null : th;
	//alert('3btnObj:'+ dateObj.btnObj);
	var tmpYear,tmpMonth,tmpDay;
	var rrr_tmp = "";
	var tempDate = dateObj.textObj.value.substring(0,10);
	//alert('4tempDate' + tempDate);
		dateObj.timeStr = "";

	if(typeof dateObj.getTime == 'object') {
		dateObj.timeStr = dateObj.getTime.value;
		//alert('5dateObj.getTime.value&dateObj.timeStr' + dateObj.timeStr);
	} else if(dateObj.getTime!='')	{
		dateObj.timeStr = dateObj.textObj.value.substring(10);
		//alert('6dateObj.textObj.value.substring(10)&dateObj.timeStr' + dateObj.timeStr);
	}
	dateObj.timeStr = (dateObj.timeStr!="")?checkFormatTime2(dateObj.timeStr):dateObj.timeStr;
	dateObj.display(viewSec);

	var temp = tempDate.split("-");
	//alert('temp.length'+temp.length);
	var rrr = '',flag = false;
	if(temp.length==3){
		rrr = new Date(temp[0],parseInt(temp[1],10)-1,parseInt(temp[2],10)); 
		if(rrr){
			rrr_tmp=tempDate;
			flag = true;
			tmpMonth=parseInt(temp[1],10);
		}
	}
	if(!flag){
		rrr = dateObj.init;
		rrr_tmp=dateObj.SetDateFormat(rrr.getFullYear(),(rrr.getMonth()),rrr.getDate());
		tmpMonth=rrr.getMonth()+1;
	} 
	tmpYear=rrr.getFullYear();	
	tmpDay=rrr.getDate();
	dateObj.outDate= rrr;
	//alert('tmpYear' +tmpYear);
	//alert('tmpDay'+tmpDay);
	//初始化季度处理
	if(dateObj.specialDayType=='13'){
		var seasonTemp;
		//alert('setDay季度处理');
		if(dateObj.textObj.value.length!=0){
			tmpYear = dateObj.textObj.value.substring(0,4);
			seasonTemp = dateObj.textObj.value.substring(5,6);
			//alert(tmpYear);
			//alert(seasonTemp);
		}else{
			seasonTemp =  Math.ceil(tmpMonth/3);
		}
		//alert('seasonTemp'+seasonTemp);
		dateObj.meizzSetDay(tmpYear,seasonTemp);
		dateObj.lastValue = tmpYear + "-" + seasonTemp + "季" ;
		//alert('lastValue' + dateObj.lastValue);
	}else{
		if(dateObj.specialDayType=='12'&& dateObj.textObj.value.length!=0){
			//初始化月份处理
			tmpYear = dateObj.textObj.value.substring(0,4);
			tmpMonth = dateObj.textObj.value.substring(5,7);
			dateObj.meizzSetDay(tmpYear,tmpMonth);
			dateObj.lastValue = dateObj.textObj.value;		
		}else{
			if(dateObj.specialDayType=='14'&& dateObj.textObj.value.length!=0){
				tmpYear = dateObj.textObj.value.substring(0,4);
				dateObj.meizzSetDay(tmpYear,tmpMonth);
				dateObj.lastValue = dateObj.textObj.value;
			}else{
				dateObj.meizzSetDay(tmpYear,tmpMonth);
				dateObj.lastValue = rrr_tmp;			
			}
			//alert('lastValue'+ dateObj.lastValue);
		}
	}
	if(dateObj.textObj.value == ""){
		//alert("setDay:dateObj.textObj.value");
		dateObj.setDateInputValue();
	}
// heyh --- start
//	if(dateObj.isIE6) 
//		dateObj.popWin.show(0,(obj.clientHeight+4), 230, 260,obj);
	//alert('showOk');
	if(dateObj.isIE6) {
		if(typeof dateObj.getTime == 'object') {
			dateObj.popWin.show(0,(obj.clientHeight+4), 155, 60,obj);
		} else {
			dateObj.popWin.show(0,(obj.clientHeight+4), 230, 260,obj);
		}
	}
// heyh --- end
	event.returnValue=false;
}


/*用来处理诸如2008-1季，2008，2008-04的情况 wenjb@bis.com.cn 2008-4-30 Start*/
/* 触发事件
 * 功能介绍：用来处理诸如2008-1季，2008，2008-04的情况
 * tt      点击按钮
 * obj     保存日期obj
 * timeobj 保存时间obj 如果不是对象则时间和日期在一块 
 * viewSec 是否显示秒数，默认显示
 */
DateComponent.setSpecialDay = function(specialDay,tt,obj,timeobj,viewSec){
	dateObj.specialDayType = specialDay;
	//判断调用情况
	if (arguments.length < 4){
		DateComponent.setDay(tt,obj);
	}else if(arguments.length == 4){
		DateComponent.setDay(tt,obj,timeobj);
	}else{
		DateComponent.setDay(tt,obj,timeobj,viewSec);
	}
}
/*用来处理诸如2008-1季，2008，2008-04的情况 wenjb@bis.com.cn 2008-4-30 End*/




//检查格式是否正确
DateComponent.checkDay = function(obj,timeobj) {
	var rrr,rrr_tmp;
	dateObj.textObj = obj;
	if(typeof timeobj == 'object'){
		dateObj.getTime = timeobj;
		dateObj.timeStr = timeobj.value = checkFormatTime2(timeobj.value)
	}else if (arguments.length > 1){
		dateObj.getTime = timeobj
		dateObj.timeStr = checkFormatTime2(dateObj.textObj.value.substring(11))
	}else{
		dateObj.getTime = '';
		dateObj.timeStr = checkFormatTime2(dateObj.textObj.value.substring(11))
	}
	var str = dateObj.textObj.value.substring(0,10)
	if(str!='' && str != ' '){ 
		var revalue=dateObj.GetTextDate(str)
		var rrr    =new Date(revalue);
		if ((revalue ==''&&str!='')|| isNaN(rrr)){			
			dateObj.textObj.select();
			return false;
		}else{			 
			rrr_tmp=dateObj.SetDateFormat(rrr.getFullYear(),rrr.getMonth(),rrr.getDate());
			dateObj.lastValue = rrr_tmp;
			dateObj.setDateInputValue()
			return true;
		}
	}
	return false;
}





//--根据 class="inputMonth" onkeyup 来验证 时间格式 hh:mm:ss
// obj 为输入框对象
function setHourFormatDate(obj){
	var str = obj.value
	var inum= str.length
	var arr = checkFormatTime1(str)
	str = arr[0];
	inum= arr[1];
	if(str.length > 7){
		obj.className = 'input_textNormal1';
		str = str.substr(0,8)
	}else 
		obj.className = 'input_textHour';
	obj.value = str
	getElementRight(inum);
}
//验证格式
//return array [str 数据,inum定位]
function checkFormatTime1(str){
	var reg4= '0123456789';
	var inum = str.length;
	for (i = 0; i < str.length; i++){ 
		var c = str.charAt(i);
		var p = '';
		if(i>0) p = str.charAt(i-1);
		if(i==2){
			if( c != ':'){
				str = str.substr(0,2)+':'+str.substr(2);
				inum = i+2;
			}
		}else if(i==5){
			if( c != ':'){
				str = str.substr(0,5)+':'+str.substr(5);
				inum = i+2;
			}
		}else if( reg4.indexOf(c) == -1) {
				str = str.substr(0,i-1)+""+str.substr(i+1);
				inum = i;
				break;				
		}else if(i==0 && parseInt(c)>2){//<24
			str = str.substr(0,0)+""+str.substr(1);
			inum = i+1;
		}else if(i==1 && parseInt(p)==2&&parseInt(c)>3){
			str = str.substr(0,1)+""+str.substr(2);
			inum = i+1;			
		}else if(i==3 && parseInt(c)>5){//<60
			str = str.substr(0,3)+""+str.substr(4);
			inum = i+1;
		}else if(i==6 && parseInt(c)>5){//<60
			str = str.substr(0,6)+""+str.substr(7);
			inum = i+1;
		}
	}
	return [str,inum]
}
//验证格式
function checkFormatTime2(str){
	if(str != "" && str.split(":").length == 2){str += ":00";}//是否显示秒数补位
	var reg = /^([0-9]){2}\:([0-9]){2}\:([0-9]){2}$/gi;
	var mid_num = str.split(":");
	if(!reg.test(str)&&mid_num.length<3)
		return "00:00:00"
	else{
		for(i=0;i<mid_num.length;i++)
			if(isNaN(mid_num[i])){
				mid_num[i] = "00";
			}
		/*
		var reg1 = /^([0]{1}[0-9]){1}$/gi;
		if(reg1.test(mid_num[0]))
			mid_num[0] = parseInt(mid_num[0].replace(/0/,''));
		if(reg1.test(mid_num[1]))
			mid_num[1] = parseInt(mid_num[1].replace(/0/,''));
		if(reg1.test(mid_num[2]))
			mid_num[2] = parseInt(mid_num[2].replace(/0/,''));
		*/
		if(parseInt(mid_num[0],10)>23)
			mid_num[0] = "23";
		if(parseInt(mid_num[1],10)>59)
			mid_num[1] = "00";
		if(parseInt(mid_num[2],10)>59)
			mid_num[2] = "00";
		return  mid_num[0]+":"+mid_num[1]+":"+mid_num[2]
	}
}

//--根据 class="inputMonth" onkeyup 来验证 年月格式 yyyy-mm
// obj 为输入框对象
function setShortFormatDate(obj){
	var reg4= '0123456789';
	var str = obj.value
	var inum= str.length
	for (i = 0; i < str.length; i++){ 
		var p = '';
		if(i>0) p = str.charAt(i-1);
		var c = str.charAt(i);
		if(i==4){
			if( c != '-'){
				str = str.substr(0,4)+'-'+str.substr(4);
				inum = i+2;
			}
		}else if( reg4.indexOf(c) == -1) {
				str = str.substr(0,i-1)+""+str.substr(i+1);
				inum = i;
				break;				
		}else if(i==5 && parseInt(c)>1){
			str = str.substr(0,5)+""+str.substr(6);
			inum = i+1;
		}else if(i==6 && parseInt(p)==1 && parseInt(c)>2){//<12
			str = str.substr(0,6)+""+str.substr(7);
			inum = i+1;
		}
	}
	if(str.length > 6){
		obj.className = 'input_textNormal1';
		str = str.substr(0,7)
	}else 
		obj.className = 'input_textMonth';
	obj.value = str
	getElementRight(inum);
}

//--根据 class="inputDate" onkeyup 来验证 年月日格式 yyyy-mm-nn
// obj 为输入框对象
function setLongFormatDate(obj){
	var reg4= '0123456789';
	var str = obj.value
	var inum= str.length
	for (i = 0; i < str.length; i++){ 
		var p = '';
		if(i>0) p = str.charAt(i-1);
		var c = str.charAt(i);
		if(i==4){
			if( c != '-'){
				str = str.substr(0,4)+'-'+str.substr(4);
				inum = i+2;
			}
		}else if(i==7){
			if( c != '-'){
				str = str.substr(0,7)+'-'+str.substr(7);
				inum = i+2;
			}
		}else if( reg4.indexOf(c) == -1) {
				str = str.substr(0,i-1)+""+str.substr(i+1);
				inum = i;
				break;				
		}else if(i==5 && parseInt(c)>1){
			str = str.substr(0,5)+""+str.substr(6);
			inum = i+1;
		}else if(i==6 && parseInt(p)==1 && parseInt(c)>2){//<12
			str = str.substr(0,6)+""+str.substr(7);
			inum = i+1;
		}else if(i==8 && parseInt(c)>3 ){
			str = str.substr(0,8)+str.substr(9);
			inum = i+1;
		}else if(i==9 && parseInt(p)==3 && parseInt(c)>1)//<31
			str = str.substr(0,9);
		;
	}
	if(str.length > 9){
		obj.className = 'input_textNormal1';
		str = str.substr(0,10)
	}else 
		obj.className = 'input_textDate'
	obj.value = str
	getElementRight(inum);
}

