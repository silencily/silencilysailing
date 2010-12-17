<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN">
<head>
<title>日历</title>
<META content="MSHTML 6.00.2800.1106" name=GENERATOR>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="../../css/style.css" rel="stylesheet" type="text/css">
<link href="../../css/style_page.css" rel="stylesheet" type="text/css">
<link href="../../css/style_buttom.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src = "../../public/scripts/contextInfo.js"></script>
<script type="text/javascript">
ContextInfo.contextPath = '<c:out value="${initParam['publicResourceServer']}"/>'
</script>
<script type="text/javascript" src = "../../public/scripts/styleControl.js"></script>
<script type="text/javascript" src = "../../public/scripts/global.js"></script>
<script type="text/javascript" src = "../../public/scripts/alt.js"></script>
<script type="text/javascript" src = "../../public/scripts/extendCombo.js"></script>
<script type="text/javascript" src = "../../public/scripts/dateUtils.js"></script>
<script type="text/javascript" src="../../public/scripts/definedWin.js"></script>

<style >
/*覆盖style_page.css*/
table.Listing td{
	height:70px;
}

.cal_today {
	background-color: #eee;
}
.cal_day{
	color:#999999;
	font-size:13px;
	/*font-weight:bold;*/
    font-family: Arial Black;
	text-align:right;
	cursor:pointer;
}
.cal_work{
	height:32px;
	text-align:center;
}
.cal_work a,.cal_work a:visited,.cal_work a:active{
	color:#bbbbbb;
}
.cal_oldDay{
	color:#333;
	font-size:10px;
}
.cal_oldDate{
	color:#bbbbbb;
	font-size:11px;	
}
.cal_fest_bg{
	background-color:#eeeeee;
	padding:2px 0px 0px 4px;
	height:20px;
	text-align:left;
}
.cal_fest{
	color:#6699FF;
}
.cal_oldFest{
	color:#CC0000;
}
.cal_24Fest{
	color:#00CC00;
}
.cal_head td{
	width:100px;
	text-align:center;
}
.cal_alt{
	width:140px;
	color:#666666;
	text-align:right;
	font-family: Verdana, Arial, Helvetica, sans-serif;
}
</style>

<SCRIPT language=JavaScript>
function click() { }
</SCRIPT>


<SCRIPT language=JavaScript>
<!--
/*****************************************************************************
                                   日期资料
*****************************************************************************/

var Calendar = {};
Calendar.clickTdObj = null;//今天的td对象
Calendar.clickDate  = "";  //今天日期
//Calendar.click

//这里传入当前日期
Calendar.Today = new Date();
Calendar.currentYear  = Calendar.Today.getFullYear();
Calendar.currentMonth = Calendar.Today.getMonth();
//这是前端对年月解析
var dateArr = Global.URLParams["date"];
if(dateArr){
	dateArr = dateArr.split("-");
	Calendar.currentYear = parseInt(dateArr[0],10);
	Calendar.currentMonth = parseInt(dateArr[1],10);
}
var tD = Calendar.Today.getDate();

/*初始化工作内容，这些内容是从后台传到页面上*/
Calendar.work = [];
//"0702 待办(1)",
//"0702 备忘(2)",
//"0718 待办(1)",
//"0725 备忘(2)"

Calendar.doAction = function(sYear,sMonth){
	location.href = "calendar.jsp?date="+sYear+"-"+sMonth;
}

var lunarInfo=new Array(
0x04bd8,0x04ae0,0x0a570,0x054d5,0x0d260,0x0d950,0x16554,0x056a0,0x09ad0,0x055d2,
0x04ae0,0x0a5b6,0x0a4d0,0x0d250,0x1d255,0x0b540,0x0d6a0,0x0ada2,0x095b0,0x14977,
0x04970,0x0a4b0,0x0b4b5,0x06a50,0x06d40,0x1ab54,0x02b60,0x09570,0x052f2,0x04970,
0x06566,0x0d4a0,0x0ea50,0x06e95,0x05ad0,0x02b60,0x186e3,0x092e0,0x1c8d7,0x0c950,
0x0d4a0,0x1d8a6,0x0b550,0x056a0,0x1a5b4,0x025d0,0x092d0,0x0d2b2,0x0a950,0x0b557,
0x06ca0,0x0b550,0x15355,0x04da0,0x0a5d0,0x14573,0x052d0,0x0a9a8,0x0e950,0x06aa0,
0x0aea6,0x0ab50,0x04b60,0x0aae4,0x0a570,0x05260,0x0f263,0x0d950,0x05b57,0x056a0,
0x096d0,0x04dd5,0x04ad0,0x0a4d0,0x0d4d4,0x0d250,0x0d558,0x0b540,0x0b5a0,0x195a6,
0x095b0,0x049b0,0x0a974,0x0a4b0,0x0b27a,0x06a50,0x06d40,0x0af46,0x0ab60,0x09570,
0x04af5,0x04970,0x064b0,0x074a3,0x0ea50,0x06b58,0x055c0,0x0ab60,0x096d5,0x092e0,
0x0c960,0x0d954,0x0d4a0,0x0da50,0x07552,0x056a0,0x0abb7,0x025d0,0x092d0,0x0cab5,
0x0a950,0x0b4a0,0x0baa4,0x0ad50,0x055d9,0x04ba0,0x0a5b0,0x15176,0x052b0,0x0a930,
0x07954,0x06aa0,0x0ad50,0x05b52,0x04b60,0x0a6e6,0x0a4e0,0x0d260,0x0ea65,0x0d530,
0x05aa0,0x076a3,0x096d0,0x04bd7,0x04ad0,0x0a4d0,0x1d0b6,0x0d250,0x0d520,0x0dd45,
0x0b5a0,0x056d0,0x055b2,0x049b0,0x0a577,0x0a4b0,0x0aa50,0x1b255,0x06d20,0x0ada0)

var solarMonth=new Array(31,28,31,30,31,30,31,31,30,31,30,31);
var Gan=new Array("甲","乙","丙","丁","戊","己","庚","辛","壬","癸");
var Zhi=new Array("子","丑","寅","卯","辰","巳","午","未","申","酉","戌","亥");
var Animals=new Array("鼠","牛","虎","兔","龙","蛇","马","羊","猴","鸡","狗","猪");
var solarTerm = new Array("小寒","大寒","立春","雨水","惊蛰","春分","清明","谷雨","立夏","小满","芒种","夏至","小暑","大暑","立秋","处暑","白露","秋分","寒露","霜降","立冬","小雪","大雪","冬至")
var sTermInfo = new Array(0,21208,42467,63836,85337,107014,128867,150921,173149,195551,218072,240693,263343,285989,308563,331033,353350,375494,397447,419210,440795,462224,483532,504758)
var nStr1 = new Array('日','一','二','三','四','五','六','七','八','九','十')
var nStr2 = new Array('初','十','廿','卅','　')
var monthName = new Array("JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC");

//国历节日 *表示放假日
var sFtv = new Array(
"0101*元旦",
"0214 情人节",
"0308 妇女节",
"0312 植树节",
"0315 消费者权益日",
//"0317 St. Patrick's",
"0401 愚人节",
"0501 劳动节",
"0504 青年节",
"0512 护士节",
"0601 儿童节",
//"0613 坤生日",
//"0614 Flag Day",
"0701 建党节 香港回归纪念",
"0801 建军节",
"0808 父亲节",
//"0908 茂生日",
"0909 毛泽东逝世纪念",
"0910 教师节",
"0928 孔子诞辰",
"1001*国庆节",
"1006 老人节",
"1024 联合国日",
//"1111 Veteran's / Remembrance Day",
"1112 孙中山诞辰纪念",
"1220 澳门回归纪念",
"1225 Christmas Day",
"1226 毛泽东诞辰纪念")
//"1011 少珊生日","0520 文珊生日",

//农历节日 *表示放假日
var lFtv = new Array(
"0101*春节",
"0115 元宵节",
"0505 端午节",
"0707 七夕情人节",
"0715 中元节",
"0815 中秋节",
"0909 重阳节",
"1208 腊八节",
"1224 小年",
"0100*除夕")

//某月的第几个星期几
var wFtv = new Array(
//"0131 Martin Luther King Day",
//"0231 President's Day",
"0520 母亲节",
//"0530 Armed Forces Day",
//"0531 Victoria Day",
"0716 合作节")
//"0811 Civic Holiday",
//"0911 Labor Holiday",
//"1021 Columbus Day",
//"1144 Thanksgiving")
//"0730 被奴役国家周",

/*****************************************************************************
                                      日期计算
*****************************************************************************/

//====================================== 传回农历 y年的总天数
function lYearDays(y) {
   var i, sum = 348
   for(i=0x8000; i>0x8; i>>=1) sum += (lunarInfo[y-1900] & i)? 1: 0
   return(sum+leapDays(y))
}

//====================================== 传回农历 y年闰月的天数
function leapDays(y) {
   if(leapMonth(y))  return((lunarInfo[y-1900] & 0x10000)? 30: 29)
   else return(0)
}

//====================================== 传回农历 y年闰哪个月 1-12 , 没闰传回 0
function leapMonth(y) {
   return(lunarInfo[y-1900] & 0xf)
}

//====================================== 传回农历 y年m月的总天数
function monthDays(y,m) {
   return( (lunarInfo[y-1900] & (0x10000>>m))? 30: 29 )
}

//====================================== 算出农历, 传入日期物件, 传回农历日期物件
//                                       该物件属性有 .year .month .day .isLeap .yearCyl .dayCyl .monCyl
function Lunar(objDate) {

   var i, leap=0, temp=0
   var baseDate = new Date(1900,0,31)
   var offset   = (objDate - baseDate)/86400000

   this.dayCyl = offset + 40
   this.monCyl = 14

   for(i=1900; i<2050 && offset>0; i++) {
      temp = lYearDays(i)
      offset -= temp
      this.monCyl += 12
   }

   if(offset<0) {
      offset += temp;
      i--;
      this.monCyl -= 12
   }

   this.year = i
   this.yearCyl = i-1864

   leap = leapMonth(i) //闰哪个月
   this.isLeap = false

   for(i=1; i<13 && offset>0; i++) {
      //闰月
      if(leap>0 && i==(leap+1) && this.isLeap==false)
         { --i; this.isLeap = true; temp = leapDays(this.year); }
      else
         { temp = monthDays(this.year, i); }

      //解除闰月
      if(this.isLeap==true && i==(leap+1)) this.isLeap = false

      offset -= temp
      if(this.isLeap == false) this.monCyl ++
   }

   if(offset==0 && leap>0 && i==leap+1)
      if(this.isLeap)
         { this.isLeap = false; }
      else
         { this.isLeap = true; --i; --this.monCyl;}

   if(offset<0){ offset += temp; --i; --this.monCyl; }

   this.month = i
   this.day = offset + 1
}

//==============================传回国历 y年某m+1月的天数
function solarDays(y,m) {
   if(m==1)
      return(((y%4 == 0) && (y%100 != 0) || (y%400 == 0))? 29: 28)
   else
      return(solarMonth[m])
}
//============================== 传入 offset 传回干支, 0=甲子
function cyclical(num) {
   return(Gan[num%10]+Zhi[num%12])
}

//============================== 月历属性
function calElement(sYear,sMonth,sDay,week,lYear,lMonth,lDay,isLeap,cYear,cMonth,cDay) {

      this.isToday    = false;
      //国历
      this.sYear      = sYear;
      this.sMonth     = sMonth;
      this.sDay       = sDay;
      this.week       = week;
      //农历
      this.lYear      = lYear;
      this.lMonth     = lMonth;
      this.lDay       = lDay;
      this.isLeap     = isLeap;
      //干支
      this.cYear      = cYear;
      this.cMonth     = cMonth;
      this.cDay       = cDay;

      this.color      = '';

      this.lunarFestival = ''; //农历节日
      this.solarFestival = ''; //国历节日
      this.solarTerms    = ''; //节气
	  this.work          = ''; //工作内容
}

//===== 某年的第n个节气为几日(从0小寒起算)
function sTerm(y,n) {
   var offDate = new Date( ( 31556925974.7*(y-1900) + sTermInfo[n]*60000  ) + Date.UTC(1900,0,6,2,5) )
   return(offDate.getUTCDate())
}

//============================== 传回月历物件 (y年,m+1月)
function calendar(y,m) {

   var sDObj, lDObj, lY, lM, lD=1, lL, lX=0, tmp1, tmp2
   var lDPOS = new Array(3)
   var n = 0
   var firstLM = 0

   sDObj = new Date(y,m,1)            //当月一日日期

   this.length    = solarDays(y,m)    //国历当月天数
   this.firstWeek = sDObj.getDay()    //国历当月1日星期几
   this.fontRed   = "red";
   this.fontGray  = "#ccc";
   this.fontWork  = "";

   for(var i=0;i<this.length;i++) {

      if(lD>lX) {
         sDObj = new Date(y,m,i+1)    //当月一日日期
         lDObj = new Lunar(sDObj)     //农历
         lY    = lDObj.year           //农历年
         lM    = lDObj.month          //农历月
         lD    = lDObj.day            //农历日
         lL    = lDObj.isLeap         //农历是否闰月
         lX    = lL? leapDays(lY): monthDays(lY,lM) //农历当月最後一天

         if(n==0) firstLM = lM
         lDPOS[n++] = i-lD+1
      }

      //sYear,sMonth,sDay,week,
      //lYear,lMonth,lDay,isLeap,
      //cYear,cMonth,cDay
      this[i] = new calElement(y, m+1, i+1, nStr1[(i+this.firstWeek)%7],
                               lY, lM, lD++, lL,
                               cyclical(lDObj.yearCyl) ,cyclical(lDObj.monCyl), cyclical(lDObj.dayCyl++) )

      if((i+this.firstWeek+1)%7==0) this[i].color = this.fontGray;  //周休二日颜色
      if((i+this.firstWeek)%7==0)   this[i].color = this.fontGray;  //周日颜色
      //if((i+this.firstWeek)%14==13) this[i].color = this.fontGray;  
   }

   //节气
   tmp1=sTerm(y,m*2  )-1
   tmp2=sTerm(y,m*2+1)-1
   this[tmp1].solarTerms = solarTerm[m*2]
   this[tmp2].solarTerms = solarTerm[m*2+1]
   if(m==3) this[tmp1].color = this.fontRed; //清明颜色

   //国历节日
   for(i in sFtv)
      if(sFtv[i].match(/^(\d{2})(\d{2})([\s\*])(.+)$/))
         if(Number(RegExp.$1)==(m+1)) {
            this[Number(RegExp.$2)-1].solarFestival += RegExp.$4 + ' '
            if(RegExp.$3=='*') this[Number(RegExp.$2)-1].color = this.fontRed;
         }

   //月周节日
   for(i in wFtv)
      if(wFtv[i].match(/^(\d{2})(\d)(\d)([\s\*])(.+)$/))
         if(Number(RegExp.$1)==(m+1)) {
            tmp1=Number(RegExp.$2)
            tmp2=Number(RegExp.$3)
            this[((this.firstWeek>tmp2)?7:0) + 7*(tmp1-1) + tmp2 - this.firstWeek].solarFestival += RegExp.$5 + ' '
         }

   //农历节日
   for(i in lFtv)
      if(lFtv[i].match(/^(\d{2})(.{2})([\s\*])(.+)$/)) {
         tmp1=Number(RegExp.$1)-firstLM
         if(tmp1==-11) tmp1=1
         if(tmp1 >=0 && tmp1<n) {
            tmp2 = lDPOS[tmp1] + Number(RegExp.$2) -1
            if( tmp2 >= 0 && tmp2<this.length) {
               this[tmp2].lunarFestival += RegExp.$4 + ' '
               if(RegExp.$3=='*') this[tmp2].color = this.fontRed;
            }
         }
      }
	
   //填入工作内容
   for(i in Calendar.work){
   		if(Calendar.work[i].match(/^(\d{2})(\d{2})([\s\*])(.+)$/)){
			 if(Number(RegExp.$1)==(m+1)) {
				this[Number(RegExp.$2)-1].work += '<a href="#" onclick="getWork(this)">'+RegExp.$4 + '</a><br/> '
				if(RegExp.$3=='*') 
					this[Number(RegExp.$2)-1].color = this.fontWork;
			 }
		}
   }
   //黑色星期五
   //if((this.firstWeek+12)%7==5)
   //   this[12].solarFestival += '黑色星期五 '

   //今日
   if(y==Calendar.currentYear && m==Calendar.currentMonth) this[tD-1].isToday = true;

}

//====================== 中文日期
function cDay(d){
   var s;

   switch (d) {
      case 10:
         s = '初十'; break;
      case 20:
         s = '二十'; break;
         break;
      case 30:
         s = '三十'; break;
         break;
      default :
         s = nStr2[Math.floor(d/10)];
         s += nStr1[d%10];
   }
   return(s);
}

///////////////////////////////////////////////////////////////////////////////

var cld;

function drawCld(SY,SM) {
	var i,sD,s,size;
	cld = new calendar(SY,SM);
	
	if(SY>1874 && SY<1909) yDisplay = '光绪' + (((SY-1874)==1)?'元':SY-1874)
	if(SY>1908 && SY<1912) yDisplay = '宣统' + (((SY-1908)==1)?'元':SY-1908)
	if(SY>1911 && SY<1950) yDisplay = '民国' + (((SY-1911)==1)?'元':SY-1911)
	if(SY>1949) yDisplay = '共和国' + (((SY-1949)==1)?'元':SY-1949)
	
	document.getElementById("GZ").innerHTML = yDisplay +'年 农历' + cyclical(SY-1900+36) + '年 &nbsp;&nbsp;【'+Animals[(SY-4)%12]+'】';
	
	//YMBG.innerHTML = "&nbsp;" + SY + "<BR>&nbsp;" + monthName[SM];
	
	var dateStr = "";
	for(i=0;i<42;i++) {
		
		wObj=document.getElementById('work_'+i);
		sObj=document.getElementById('SD'+ i);
		lObj=document.getElementById('LD'+ i);
		lObj.className = 'cal_oldDay';
		sObj.parentNode.className = '';		
		sD = i - cld.firstWeek;
		
		if(sD>-1 && sD<cld.length) { //日期内
			sObj.innerHTML = sD+1;		 
			dateStr = cld[sD].sYear +"-"+cld[sD].sMonth +"-"+cld[sD].sDay;	
			sObj.parentNode.date=dateStr;
				
			if(cld[sD].isToday) {
				Calendar.clickTdObj = sObj.parentNode;
				Calendar.clickTdObj.className = 'cal_today'; //今日颜色
				document.getElementById("spanId_date").innerHTML = dateStr;
				Calendar.clickDate  = dateStr;
			}
			sObj.style.color = cld[sD].color; //国定假日颜色
			
			if(cld[sD].lDay==1) //显示农历月
				lObj.innerHTML = '<b>'+(cld[sD].isLeap?'闰':'') + cld[sD].lMonth + '月' + (monthDays(cld[sD].lYear,cld[sD].lMonth)==29?'小':'大')+'</b>';
				else //显示农历日
				lObj.innerHTML = cDay(cld[sD].lDay);
				
				s=cld[sD].lunarFestival;
				if(s.length>0) { //农历节日
				if(s.length>6) s = s.substr(0, 4)+'…';
				//s = s.fontcolor(this.fontRed);
				lObj.className = "cal_oldFest";
			}else { //国历节日
				s=cld[sD].solarFestival;
				if(s.length>0) {
				   size = (s.charCodeAt(0)>0 && s.charCodeAt(0)<128)?8:4;
				   if(s.length>size+2) s = s.substr(0, size)+'…';
				   //s = s.fontcolor(this.fontFest);
				   lObj.className = "cal_fest";
				}else { //廿四节气
				   s = cld[sD].solarTerms;
				   //if(s.length>0) s = s.fontcolor(this.font24);
				   if(s.length>0) 
				   		lObj.className = "cal_24Fest";				
				}
			}
			if(s.length>0) 
				lObj.innerHTML = s;
			var w = cld[sD].work;
			if(w)
				wObj.innerHTML = w;	
		
		}else { //非日期
			 sObj.innerHTML = '';
			 lObj.innerHTML = '';
		}
	}
}


function changeCld() {
   var y,m;
   y=document.cal_form.SY.value;
   m=document.cal_form.SM.value;
   //drawCld(y,m);
   m = parseInt(m,10);
   Calendar.doAction(y,m);
}

//跳转日历
function pushBtm(K) {
	var selectY = parseInt(document.cal_form.SY.value,10);
	var selectM = parseInt(document.cal_form.SM.value,10);
	switch (K){
	  case 'YU' :
		 if(selectY>0) 
			selectY--;
		 break;
	  case 'YD' :
		 if(selectY<2050) 
			selectY++;
		 break;
	  case 'MU' :
		 if(selectM>0) {
			selectM--;
		 }
		 else {
			selectM=11;
			if(selectY>1900) 
				selectY--;
		 }
		 break;
	  case 'MD' :
		 if(selectM<11) {
			selectM++;
		 }
		 else {
			selectM=0;
			if(selectY<2050) 
				selectY++;
		 }
		 break;
	  default :
		 selectY = Calendar.currentYear;
		 selectM = Calendar.currentMonth;
	}
	document.cal_form.SY.value = selectY;
	document.cal_form.SM.value = selectM;
	changeCld();
}


//////////////////////////////////////////////////////////////////////////////

var width = "130";
var offsetx = 2;
var offsety = 16;

var temp_x = 0;
var temp_y = 0;
var snow = 0;
var sw = 0;
var cnt = 0;

var divId_detail;
document.onmousemove = function(event){ getEventPosition(event) };

//显示详细日期资料
function mOvr(v,divObj) {
   var s,festival;
   var sObj=eval('SD'+ v);
   var d=sObj.innerHTML-1;

      //sYear,sMonth,sDay,week,
      //lYear,lMonth,lDay,isLeap,
      //cYear,cMonth,cDay

   if(sObj.innerHTML!='') {
      //sObj.style.cursor = '';
      if(cld[d].solarTerms == '' && cld[d].solarFestival == '' && cld[d].lunarFestival == '')
         festival = '';
      else
         festival = '<div class="cal_fest_bg cal_fest">'+cld[d].solarTerms + ' ' + cld[d].solarFestival + ' ' + cld[d].lunarFestival+'</div>';

      s= '<div class="cal_alt">'+cld[d].sYear+' 年 '+cld[d].sMonth+' 月 '+cld[d].sDay+' 日<br>星期'+cld[d].week+''+
         '<div class="cal_oldDay">农历'+(cld[d].isLeap?'闰 ':' ')+cld[d].lMonth+' 月 '+cld[d].lDay+' 日</div>'+
         '<div class="cal_oldDate">'+cld[d].cYear+'年 '+cld[d].cMonth+'月 '+cld[d].cDay + '日</div>'+
         festival +'</div>';
		divObj.title = s;
		/*
        document.all["detail"].innerHTML = s;

		if (snow == 0) {
			 divId_detail.left = temp_x+offsetx-(width/2);
			 divId_detail.top = temp_y+offsety;
			divId_detail.visibility = "visible";
			snow = 1;
		}*/
	}
}

//清除详细日期资料
function mOut() {
	if ( cnt >= 1 ) { sw = 0 }
	if ( sw == 0 ) { snow = 0;	divId_detail.visibility = "hidden";}
	else cnt++;
}

//取得位置
function getEventPosition(event) {
	event = event ? event : (window.event ? window.event : null);
	var MouseX = event.x ? event.x : event.pageX;
	var MouseY = event.y ? event.y : event.pageY;
    temp_x=MouseX;
    temp_y=MouseY;
	if (document.body.scrollLeft){
		temp_x =MouseX+document.body.scrollLeft; 
		temp_y=MouseY+document.body.scrollTop;
	}
	if (snow){
		divId_detail.left = temp_x + offsetx - (width/2)
		divId_detail.top  = temp_y + offsety
	}
}

//选中日期
function mDown(obj){
	if(obj == Calendar.clickTdObj)
		return;
	var txt = obj.date;
	if(!txt)
		return;
	document.getElementById("spanId_date").innerHTML = txt;
	obj.className = "cal_today";
	Calendar.clickTdObj.className = "";
	Calendar.clickTdObj = obj;
	Calendar.clickDate  = txt;
}
//选中工作转到详细页面
function getWork(obj){
	var txt = obj.innerText;
	if(txt.indexOf("待办")>=0){
		parent.panel.click(2);
	}else{
		parent.panel.click(4);
	}
}
///////////////////////////////////////////////////////////////////////////

function changeTZ() {
   CITY.innerHTML = document.cal_form.TZ.value.substr(6)
   setCookie("TZ",document.cal_form.TZ.selectedIndex)
}


function tick() {
   var today
   today = new Date()
   Clock.innerHTML = today.toLocaleString().replace(/(年|月)/g, "/").replace(/日/, "");
   Clock2.innerHTML = TimeAdd(today.toGMTString(), document.cal_form.TZ.value)
   window.setTimeout("tick()", 1000);
}

function setCookie(name, value) {
	var today = new Date()
	var expires = new Date()
	expires.setTime(today.getTime() + 1000*60*60*24*365)
	document.cookie = name + "=" + escape(value)	+ "; expires=" + expires.toGMTString()
}

function getCookie(Name) {
   var search = Name + "="
   if(document.cookie.length > 0) {
      offset = document.cookie.indexOf(search)
      if(offset != -1) {
         offset += search.length
         end = document.cookie.indexOf(";", offset)
         if(end == -1) end = document.cookie.length
         return unescape(document.cookie.substring(offset, end))
      }
      else return ""
   }
}

/////////////////////////////////////////////////////////

function initial() {
   divId_detail = document.getElementById("detail").style;
   //document.cal_form.SY.selectedIndex=Calendar.currentYear-1900;
   //document.cal_form.SM.selectedIndex=Calendar.currentMonth;
   document.cal_form.SY.value = Calendar.currentYear;
   document.cal_form.SM.value = Calendar.currentMonth ;
   drawCld(Calendar.currentYear,Calendar.currentMonth);

   //document.cal_form.TZ.selectedIndex=getCookie("TZ");
   //changeTZ();
   //tick();
}

//将当前日历保存与其他对照
function viewBtn(){
	var str = document.getElementById("tdId_current").outerHTML;
	str = str.replace(/name=/gi,"name=name_");
	str = str.replace(/id=/gi,"id=id_")
	document.getElementById("tdId_view").innerHTML = str; 
}
//-->
</SCRIPT>

</HEAD>
<BODY bgProperties=fixed>

<FORM name="cal_form">

<!--div class="list_group">
	<div class="list_title"><img src="../../image/common/pic_calendar.gif"  align="absmiddle"> 我的日历</div>
</div-->

<DIV id=detail style="POSITION: absolute"><!-- 详细信息--></DIV>

<table width="100%"  border="0" cellpadding="0" cellspacing="0">
	<tr>
	<td >
	  
	  <TABLE width=100%>
		<TBODY>
		<TR>
		  <TD width="80%" valign="top">
			
			<div class="update_subhead">
				<div class="opera_left">
				&nbsp; 西历

				<input type="hidden" name="text_yearMonth"/>
				<input type="hidden" name="SY"/>
				<input type="hidden" name="SM"/>
				<script type="text/javascript">	
					var disM = parseInt(Calendar.currentMonth,10)+1;
					var date = Calendar.currentYear+"-"+ ( disM>=10 ? disM : "0"+disM );
					document.forms[0].text_yearMonth.value = date;
					var str  = DateUtils.dateObj.displayYearAndMonth('text_yearMonth',date,1900,2050)
					document.write(str);
					document.close();

					function DateUtils.dateObj.afterSelectOption(obj,hidd){
						date = hidd.value.split("-");
						document.forms[0].SY.value = date[0];
						document.forms[0].SM.value = parseInt(date[1],10)-1;
						changeCld();
					}
				</script>	
				
				<%--
				<SELECT  onchange=changeCld() name=SY> 
					<SCRIPT language=JavaScript>
					<!--
					for(i=1900;i<2050;i++) {
						document.write('<option>'+i);
					}
					document.close();
					//-->
					</SCRIPT>
				 </SELECT>年
				 <SELECT  onchange=changeCld() name=SM> 
					<SCRIPT language=JavaScript>
					<!--
						for(i=1;i<13;i++) {
							document.write('<option>'+i);
						}
						document.close();
					//-->
					</SCRIPT>
				  </SELECT>月
				  --%>

				  <span id="GZ"></span>
				  </div>
				  
				  <div class="opera_right">
					<a href="#" onClick="pushBtm('YU');return false"><img src="../../image/main/scroll_left.gif"   title="上一年"></a>
					<a href="#" onClick="pushBtm('MU');return false"><img src="../../image/main/scroll_left1.gif"  title="上一月"></a>
					<a href="#" onClick="pushBtm('');return false">今天</a>
					<a href="#" onClick="pushBtm('MD');return false"><img src="../../image/main/scroll_right1.gif" title="下一月"></a>
					<a href="#" onClick="pushBtm('YD');return false"><img src="../../image/main/scroll_right.gif"  title="下一年"></a>
					 &nbsp; 
				</div>
			</div>
			<!--DIV style="Z-INDEX: -1; POSITION: absolute; TOP: 60px;border=1">
				<FONT id=YMBG style="FONT-SIZE: 90pt; COLOR: #f0f0f0; FONT-FAMILY: 'Arial Black'">
					&nbsp;0000<BR>&nbsp;JUN
				</FONT> 
			</DIV-->
			<TABLE border=0 id="tdId_current" class="Listing"%>			  
			 <thead>
			 <tr class="cal_head">
				<td >日</td>
				<td >一</td>
				<td >二</td>
				<td >三</td>
				<td >四</td>
				<td >五</td>
				<td >六</td>
			</tr>
			</thead>
			<tbody>
			<script language=javascript>
			<!--
				var gNum
				var str = "";
				for(i=0;i<6;i++) {
				   str += '<tr>';
				   for(j=0;j<7;j++) {
					  gNum = i*7+j
					  str += '<td  id="GD' + gNum +'" onclick="mDown(this)">';
					  str += '<div id="SD' + gNum +'" class="cal_day" onMouseOver="mOvr(' + gNum +',this)"  ';
					  //if(j == 0) document.write(' color="#888888"')onMouseOut="mOut()"
					  /*if(j == 6)
						  if(i%2==1) 
							str += ' color="#888888"';
						  else 
							str += ' color=green';
					  */
					  str += ' title=""> </div>';
					  str += '<div id="work_'+gNum+'"  class="cal_work"></div>';
					  str += '<div id="LD' + gNum + '" class="cal_oldDay"></div>';
					  str += '<div id="fest_'+gNum+'"  ></div>';//class="cal_fest"
					  str += '</td>';
				   }
				   str += '</tr>';
				}
				document.write(str);
				document.close();
			//-->
			</script>
			</tbody>
			</table>
	 
	 </TD>
	<TD vAlign=top>
    <table width="100%"  border="0"  cellpadding="2" cellspacing="0">
      <tr>
        <td align="right">查看：<span id="spanId_date"></span> &nbsp;</td>
      </tr>
      <!--tr>
        <td>
		<div class="update_subhead ">
			<div class="opera_left"><span class="switch_open" onClick="StyleControl.switchDiv(this,gonggao)">当天公告</span></div>
			<div class="opera_right"><input type="button" class="more_out" title="查看更多公告" onClick="parent.panel.click(1)"/></div>
		</div>
		<div id="gonggao">
			<ul>
				<li>系统更新</li>
				<li>系统更新</li>
			</ul>	
		</div>	
		</td>
      </tr>
      <tr>
        <td>
		<div class="update_subhead">
			<div class="opera_left"><span class="switch_open" onClick="StyleControl.switchDiv(this,renwu)">当天任务</span></div>
			<div class="opera_right"><input type="button" class="more_out" title="查看更多公告" onClick="parent.panel.click(2)"/></div>
		</div>
		<div id="renwu">
			<ul>
				<li>任务1</li>
				<li>任务2</li>
			</ul>
		</div>
		</td>
      </tr>
      <tr>
        <td>
		<div class="update_subhead">
			<div class="opera_left"><span class="switch_open" onClick="StyleControl.switchDiv(this,xiaoxi)">当天消息</span></div>
			<div class="opera_right"><input type="button" class="more_out" title="查看更多公告" onClick="parent.panel.click(3)"/></div>
		</div>
		<div id="xiaoxi">
			<ul>
				<li>消息1</li>
				<li>消息2</li>
			</ul>
		</div>
		</td>
      </tr>
      <tr>
        <td>
		<div class="update_subhead">
			<div class="opera_left"><span class="switch_open" onClick="StyleControl.switchDiv(this,beiwang)">当天备忘</span></div>
			<div class="opera_right"><input type="button" class="more_out" title="查看更多公告" onClick="parent.panel.click(4)"/></div>
		</div>
		<div id="beiwang">
			<ul>
				<li>工作1</li>
				<li>工作2</li>
			</ul>
			<div class="list_button"><a href="#" onclick="definedWin.openLongTextWin(aaa);return false">新增备忘</a>
			<input type="hidden" name="aaa" value="这里你看到的默认值"/>
			</div>
		</div>
		</td>
      </tr-->
      <tr>
        <td>

		<div class="update_subhead">
			<div class="opera_left"><span class="switch_open" onClick="StyleControl.switchDiv(this,zhinan)">系统操作指南</span></div>
		</div>
		<div id="zhinan">
			<ul>
				<li><br/>1.桌面默认显示“日历”，点击查看每天具体信息</li>
				<li><br/>2.点击页面顶部“功能”，将弹出系统的功能菜单</li>
				<li><br/>3.“新闻中心”是通过信息发布的外部和内部的新闻汇总</li>
				<li><br/>4.“帮助文档”是各个子系统的使用帮助文档下载</li>
			</ul>
		</div>
		
		
		</td>
      </tr>
      <tr>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td>&nbsp;</td>
      </tr>
      <!--tr>
        <td>&nbsp;</td>
        <td><input type="button" onClick="viewBtn('')"   class="opera_normal"  value="对 照"></td>
      </tr-->
    </table>
	</TD>
		</TR>
	</TBODY>
	</TABLE>
		  
	  </td>
	  <td valign="top" id="tdId_view">
	  &nbsp;
	  <div class=""></div>
	  </td>
	</tr>
  </table>
	  
<div class="list_bottom"></div>
	  
</FORM>
			

<SCRIPT language=VBScript>
<!--
'===== 算世界时间
Function TimeAdd(UTC,T)
   Dim PlusMinus, DST, y
   If Left(T,1)="-" Then PlusMinus = -1 Else PlusMinus = 1
   UTC=Right(UTC,Len(UTC)-5)
   UTC=Left(UTC,Len(UTC)-4)
   y = Year(UTC)
   TimeAdd=DateAdd("n", (Cint(Mid(T,2,2))*60 + Cint(Mid(T,4,2))) * PlusMinus, UTC)
   '美国日光节约期间: 4月第一个星日00:00 至 10月最後一个星期日00:00
   If Mid(T,6,1)="*" And DateSerial(y,4,(9 - Weekday(DateSerial(y,4,1)) mod 7) ) <= TimeAdd And DateSerial(y,10,31 - Weekday(DateSerial(y,10,31))) >= TimeAdd Then
      TimeAdd=CStr(DateAdd("h", 1, TimeAdd))
      tSave.innerHTML = "R"
   Else
      tSave.innerHTML = ""
   End If
   TimeAdd = CStr(TimeAdd)
End Function
'-->
</SCRIPT>

<script type="text/javascript">
initial();
//var str = document.getElementById("tdId_current").innerHTML;
//var w = window.open();
//w.document.write(str);
//w.document.close();
</script>			
</body>
</html>