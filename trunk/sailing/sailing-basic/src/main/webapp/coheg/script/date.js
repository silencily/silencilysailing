/*
 * ���ڿؼ�
 * ���÷�ʽ�� <input type="text" name="gradute" onblur="checkDay(this)">
   <input name="button" type="button" class="button" onclick="setday(this,gradute)" value="..."/>
 *
 */



document.writeln('<iframe id=ifrmId_DateLayer name="ifrmId_DateLayer" Author=wayx frameborder=0 '+
				 'style="position: absolute; width: 100%; height: 100%; z-index: 5; display: none;" '+
				 'scrolling=yes></iframe>');


/* ���ڿؼ� ����
 * 2006-04-15
 * ͨ���������ڿؼ����󣬴ﵽ�����ľֲ����Ϳ������ԣ����д����Ż�
 * ÿ��ҳ�潨��һ�����ڶ��󼴿�
 */

function dateObject(name){
	this.name   = name;
	this.popWin = '';
	this.init   = new Date();   //��ʼ�����ڣ�ָ��һ�����ڴ�����
	this.lineColor = "#bbbbbb"; //this.lineColor
	this.bgColor   = "#eeeeee"; //this.bgColor
	this.defColor   = "#ffffff"; //this.defColor
	this.restColor = "#dddddd";
	this.textObj  = null;       //���������
	this.btnObj   = null;      //�����ť����

	this.timeStr   = "";        //��ʱʱ�����
	this.outDate   = '';        //��ת���������ڴ�Ŷ�������� outDate
	this.getTime   = '' ;       //���ʱ��obj
	this.dateDivId = null;      //�����������
	this.dateLayer = null;	    //����������ݶ���
	this.lastValue = '';
	this.isIE6     = false;
	
	//this.build   = buildDate;   //�������ڿؼ�
	this.display = displayDate; //��ʾ���ڿؼ�
	//this.click   = clickDate;   //ѡȡ����
	this.close   = closeLayer;  //�رտؼ�
	this.currTime= getCurrentTime;
	this.GetFormatYear = GetFormatYear;
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
	this.meizzToday   = meizzToday;
	this.meizzPrevM   = meizzPrevM;
	this.meizzNextM   = meizzNextM;
	this.isCurrentDate= isCurrentDate;
	this.meizzSetDay  = meizzSetDay;
	this.selectClickDay = selectClickDay;
	this.meizzDayClick  = meizzDayClick;
	this.setFormatTimeStr = setFormatTimeStr;
	this.selectTimeNum  = selectTimeNum;
	this.setDateInputValue= setDateInputValue;
	
	this.DateFormat = "<yyyy>-<mm>-<dd>";
	this.MonthName = new Array();
	  this.MonthName[0] = "һ";this.MonthName[1] = "��";this.MonthName[2] = "��";this.MonthName[3] = "��";
	  this.MonthName[4] = "��";this.MonthName[5] = "��";this.MonthName[6] = "��";this.MonthName[7] = "��";
	  this.MonthName[8] = "��";this.MonthName[9] = "ʮ";this.MonthName[10] = "ʮһ";this.MonthName[11] = "ʮ��";
	this.MonHead  = new Array(12); //����������ÿ���µ��������
		this.MonHead[0] = 31; this.MonHead[1] = 28; this.MonHead[2] = 31; 
		this.MonHead[3] = 30; this.MonHead[4] = 31; this.MonHead[5] = 30;
		this.MonHead[6] = 31; this.MonHead[7] = 31; this.MonHead[8] = 30; 
		this.MonHead[9] = 31; this.MonHead[10]= 30; this.MonHead[11]= 31;
	this.meizzTheYear  = this.init.getFullYear();    //������ı����ĳ�ʼֵ
	this.meizzTheMonth = this.init.getMonth()+1;     //�����µı����ĳ�ʼֵ
	this.meizzWDay     = new Array(42);               //����д���ڵ�����

	//init
	if(typeof checkExplorer != 'undefined' && checkExplorer(5.5)){
		this.isIE6    = true;
		this.popWin   = window.createPopup(); //
		this.dateLayer= this.popWin.document;	
	}else{
		this.dateLayer=  window.frames.ifrmId_DateLayer.document;
		this.dateDivId = window.frames.ifrmId_DateLayer��
	}
	
	//��ʼѡ����Сʱ
	this.tempClick = null
}

//== WEB ҳ����ʾ����
function displayDate(){
	var url = getContextPath();
	var strFrame='';		//����������HTML����
	strFrame='<html><head><style>';
	strFrame+='td {font-size: 9pt;font-family:����;}a{font-size: 10pt;color:#000000;text-decoration:none;}';
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
	strFrame+='</head><body><div class="div_body" onselectstart="return false" >';
	strFrame+='<table border=0 cellspacing=0 cellpadding=0 width=100% height=100% >';
	strFrame+='  <tr ><td align="center">';
	strFrame+='		<table border=0 cellspacing=2 cellpadding=0 class="date_tab">';
	strFrame+='         <tr><td class="td_cell" id="tabId_inneryear">';
	strFrame+='				<span id=meizzMonthHead></span>';
	strFrame+='			</td><td>';
	strFrame+='				<img src="'+url+'/image/common/date_up.gif"   onclick="parent.dateObj.meizzNextM()" title="��� 1 ��" class="date_switch"/><br/>';
	strFrame+='				<img src="'+url+'/image/common/date_down.gif" onclick="parent.dateObj.meizzPrevM()" title="��ǰ�� 1 ��" class="date_switch"/>';
	strFrame+='			</td><td class="td_cell" >';
	strFrame+='				<span id=meizzYearHead></span>';
	strFrame+='			</td><td> ';
	strFrame+='				<img src="'+url+'/image/common/date_up.gif"   onclick="parent.dateObj.meizzNextY()" title="��� 1 ��" class="date_switch"/><br/>';
	strFrame+='				<img src="'+url+'/image/common/date_down.gif" onclick="parent.dateObj.meizzPrevY()" title="��ǰ�� 1 ��" class="date_switch"/>';
	strFrame+='        </td></tr>';
	strFrame+='     </table>';
	strFrame+=' </td></tr>';

	strFrame+='  <tr ><td align="center">';
	strFrame+='  <table border=0 cellspacing=0 cellpadding=0 class="date_body">';
	strFrame+='		<tr  align=center valign=bottom class="date_head">';
	strFrame+='			<td class="date_head1">��</td><td >һ</td> <td >��</td>';
	strFrame+='			<td >��</td> <td >��</td><td >��</td><td class="date_head1">��</td></tr>';
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
	strFrame+='        <tr><td height="30" align="right">';
	
	var temp = this.currTime(this.timeStr);
	strFrame+='		<div class="div_time1"><img src="'+url+'/image/common/date_up.gif"   onclick="parent.dateObj.meizzNextNum(1)" title="����ѡ����ֵ" class="date_switch"/><br/>';
	strFrame+='			 <img src="'+url+'/image/common/date_down.gif" onclick="parent.dateObj.meizzNextNum(-1)" title="����ѡ����ֵ" class="date_switch"/></div>';
    strFrame+='		 <div class="div_time2"><table border=0 cellspacing=0 cellpadding=1>';
	strFrame+='      <tr>';
	strFrame+='         <td class="td_time1" id=spanId_dateHour  onclick="parent.dateObj.selectTimeNum(this)" title="ѡ�������ٵ������İ�ť����Сʱ����ֵ">';
	strFrame+='				'+temp[0]+'';
	strFrame+='			</td><td class="td_time2"> ';
	strFrame+='			 :';
	strFrame+='			</td><td class="td_time" id=spanId_dateMinu onclick="parent.dateObj.selectTimeNum(this)" title="ѡ�������ٵ������İ�ť���ڷ��ӵ���ֵ">';
	strFrame+='				'+temp[1]+'';
	strFrame+='			</td><td class="td_time2"> ';
	strFrame+='			 :';
	strFrame+='			</td><td class="td_time" id=spanId_dateSeco>';
	strFrame+='				'+temp[2]+'';
	strFrame+='        </td></tr>';
	strFrame+='      </table></div>';
	
	strFrame+='        </td></tr>';
	strFrame+='        <tr><td class="date_bottom">';
	strFrame+='				<div class="div_btn"><a href="#" onclick="parent.dateObj.close()" ';
	strFrame+='					title="�������رտؼ�">ȡ ��</a></div>';	
	strFrame+='				<div class="div_btn"><a href="#" onclick="parent.dateObj.meizzToday()" title="��ǰ����">�� ��</a></div> ';
	strFrame+='				<div class="div_btn"><a href="#" onclick="parent.dateObj.selectClickDay()">ѡ ȡ</a></div> ';
	strFrame+='			</td></tr></table></div></body></html>';
	//<a href="#" onclick="parent.getWinHelp();return false" title=\'�������鿴ʱ��ؼ�����\'>����</a>;
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
		obj.document.close();	
	}
	this.tempClick = this.dateLayer.getElementById("spanId_dateHour");
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

//���ѡ��Ҫ��������
function selectTimeNum(obj){
	this.tempClick.className = 'td_time';
	this.tempClick = obj;
	this.tempClick.className = 'td_time1';
}
//==��ѡ�����������ӻ��߼���
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
		default://TODO:���Ӹı���ܸı�Сʱ��
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

//== �� head ��д�뵱ǰ��������
function meizzWriteHead(yy,mm){
	this.dateLayer.getElementById("meizzYearHead").innerText  = yy + " ��";
	this.dateLayer.getElementById("meizzMonthHead").innerText = this.MonthName[mm-1] + " ��";
}

//== �ж��Ƿ���ƽ��
function IsPinYear(year){
    if (0==year%4&&((year%100!=0)||(year%400==0))) 
		return true;
	else 
		return false;
}

//== �������Ϊ29��
function GetMonthCount(year,month)  
{
    var c=this.MonHead[month-1];if((month==2)&&this.IsPinYear(year)) c++;return c;
}
//== ��ĳ������ڼ�
function GetDOW(day,month,year)     
{
    var dt=new Date(year,month-1,day).getDay()/7; return dt;
}

//== ��ǰ�� Year
function meizzPrevY(){
	this.meizzTheYear--
    this.meizzSetDay(this.meizzTheYear,this.meizzTheMonth);
}
//== ���� Year
function meizzNextY(){
    this.meizzTheYear++;
    this.meizzSetDay(this.meizzTheYear,this.meizzTheMonth);
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
    this.close();
}
//== ��ǰ���·�
function meizzPrevM()  
{
    if(this.meizzTheMonth>1){this.meizzTheMonth--}else{this.meizzTheYear--;this.meizzTheMonth=12;}
    this.meizzSetDay(this.meizzTheYear,this.meizzTheMonth);
}
//== �����·�
function meizzNextM()  
{
    if(this.meizzTheMonth==12){this.meizzTheYear++;this.meizzTheMonth=1}else{this.meizzTheMonth++}
    this.meizzSetDay(this.meizzTheYear,this.meizzTheMonth);
}
//�Ƿ��ǵ�ǰ����
function isCurrentDate(yy,mm,dd){
	var flag = yy == this.init.getFullYear() && mm == this.init.getMonth() && dd == this.init.getDate();
	return flag;
}

//== ��Ҫ��д����**********
function meizzSetDay(yy,mm){
	this.meizzWriteHead(yy,mm);
	//���õ�ǰ���µĹ�������Ϊ����ֵ
	this.meizzTheYear=yy;
	this.meizzTheMonth=mm;	
	//����ʾ�������ȫ�����
	for (var i = 0; i < 42; i++){
	  this.meizzWDay[i]="";
	} 
	//ĳ�µ�һ������ڼ�,�������һ������
	var day1 = 1,day2=1,firstday = new Date(yy,mm-1,1).getDay();
	//�ϸ��µ������
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
		//��д�µ�һ���µ�������������
		da = this.dateLayer.getElementById("meizzDay"+i)
		tempClass = '';
		tempy = yy;
		tempm = mm;
		tempnum = 0;
		tempdis = false;
		//�ϸ��µĲ���
		if(i<firstday){	
			tempy = mm ==1 ? yy-1: yy;
			tempm = mm ==1 ? 12  : mm-1;
			tempClass = 'date_td0';	
			tempdis = true;
			tempnum = -1;
		}else if(i>=(firstday+this.GetMonthCount(yy,mm))){		
			//�¸��µĲ���
			tempy = mm==12?yy+1:yy;
			tempm = mm==12?1:mm+1
			tempClass = 'date_td0';	
			tempdis = true;
			tempnum = 1;
		}
		//Ĭ�ϱ��²���
		da.title    = tempm +"��" + this.meizzWDay[i] + "��";	
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

//== �����ʾ��ѡȡ���ڣ������뺯��*************
function selectClickDay(){
	if(this.textObj && this.lastValue){	
		this.setDateInputValue();			
	}
	this.close();
}


function meizzDayClick(n,ex){
	var yy=this.meizzTheYear;
	var mm = parseInt(this.meizzTheMonth,10)+ex;//ex��ʾƫ����������ѡ���ϸ��·ݺ��¸��·ݵ�����
	//�ж��·ݣ������ж�Ӧ�Ĵ���
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
}

//��װָ����ʱ���ʽ
function getSelectTimeStr(str,flag){
	if(this.timeStr == "")
		return;
	var temp = this.setFormatTimeStr(this.timeStr).split(":");
	temp[flag] = str;
	this.timeStr = temp[0]+":"+temp[1]+":"+temp[2];
}
function setFormatTimeStr(str){
	str = str.replace(/��/gi,":");
	str = str.replace(/(^(\s|\u3000)*)|((\s|\u3000)*$)/gi, '');
	return str 
}
//�������ֵ
function setDateInputValue(str){
	this.textObj.value = this.lastValue;
	if(typeof this.getTime == 'object')
		this.getTime.value = this.setFormatTimeStr(this.timeStr)
	else if(this.getTime)
		this.textObj.value = this.textObj.value.substring(0,10) + " "+ this.setFormatTimeStr(this.timeStr)
	//this.textObj.focus();	
}
//== ��Esc���رգ��л�����ر�
function closeLayer(){
    if(this.isIE6)
		this.popWin.hide();
	else
		this.dateDivId.style.display="none";	
}

/* 
 * ���������������÷���
 */
var dateObj = new dateObject('dateObj');

/* �����¼�
 * tt      �����ť
 * obj     ��������obj
 * timeobj ����ʱ��obj ������Ƕ�����ʱ���������һ�� 
 */
function setday(tt,obj,timeobj){
	if (arguments.length > 2)
		dateObj.getTime = (typeof timeobj == 'object')?timeobj:1;
	else
		dateObj.getTime = '';
	var th = tt;
	var ttop  = obj.offsetTop;     //TT�ؼ��Ķ�λ���
	var thei  = obj.clientHeight;  //TT�ؼ�����ĸ�
	var tleft = obj.offsetLeft;    //TT�ؼ��Ķ�λ���
	var ttyp  = tt.type;           //TT�ؼ�������
	while (tt = tt.offsetParent){ttop+=tt.offsetTop; tleft+=tt.offsetLeft;}
    
	if(!dateObj.isIE6){
		var dads  = dateObj.dateDivId.style;
		dads.top  = (ttyp=="image")? ttop+thei : ttop+thei+6;
		dads.left = tleft;
		dads.display = '';
	}
	dateObj.textObj  = (arguments.length == 1) ? th : obj;
	dateObj.lastValue= dateObj.textObj.value;
	dateObj.btnObj   = (arguments.length == 1) ? null : th;
	var tmpYear,tmpMonth,tmpDay;
	var rrr_tmp = "";
	var tempDate = dateObj.textObj.value.substring(0,10);
		dateObj.timeStr = "";

	if(typeof dateObj.getTime == 'object')
		dateObj.timeStr = dateObj.getTime.value
	else if(dateObj.getTime!='')	
		dateObj.timeStr = dateObj.textObj.value.substring(10)
	dateObj.timeStr = (dateObj.timeStr!="")?checkFormatTime2(dateObj.timeStr):dateObj.timeStr;

	dateObj.display();

	var temp = tempDate.split("-");
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
		rrr_tmp=rrr.getFullYear()+"-"+(rrr.getMonth()+1)+"-"+rrr.getDate();
		tmpMonth=rrr.getMonth()+1;
	} 
	tmpYear=rrr.getFullYear();		
	tmpDay=rrr.getDate();
	dateObj.outDate= rrr;
	dateObj.meizzSetDay(tmpYear,tmpMonth);
	dateObj.lastValue = rrr_tmp;
	dateObj.setDateInputValue();
	if(dateObj.isIE6)
		dateObj.popWin.show(0,(obj.clientHeight+1), 230, 260,obj);
	event.returnValue=false;
}
//����ʽ�Ƿ���ȷ
function checkDay(obj,timeobj) {
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

//--���� class="inputMonth" onkeyup ����֤ ʱ���ʽ hh:mm:ss
// obj Ϊ��������
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
//��֤��ʽ
//return array [str ����,inum��λ]
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
//��֤��ʽ
function checkFormatTime2(str){
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

//--���� class="inputMonth" onkeyup ����֤ ���¸�ʽ yyyy-mm
// obj Ϊ��������
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

//--���� class="inputDate" onkeyup ����֤ �����ո�ʽ yyyy-mm-nn
// obj Ϊ��������
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

//�����λ�ö�λ inum Ϊ�������λ��
//������ general.js
function getElementRight(inum){  
	  inum = (inum + "" == "undefined")?e.value.length:inum;
	  var e = event.srcElement;  
	  var r =e.createTextRange();  
	  r.moveStart('character',inum);  
	  r.collapse(true);  
	  r.select();  
}  


/* //TODO ���·���ʵ����ʾ�����˵���ѡ������
 * �����Ժ�汾ʵ��
 */

//== ��ݵ�������
function tmpSelectYearInnerHTML(strYear){
	  if (strYear.match(/\D/)!=null){alert("�����������������֣�");return;}
	  var m = (strYear) ? strYear : this.init.getFullYear();
	  if (m < 1000 || m > 9999) {alert("���ֵ���� 1000 �� 9999 ֮�䣡");return;}
	  var n = m - 30; //pre year
	  if (n < 1000) n = 1000;
	  if (n + 26 > 9999) n = 9974;
	  var s = "<select Author=meizz name=tmpSelectYear style='font-size: 12px' "
		 s += "onblur='document.all.tmpSelectYearLayer.style.display=\"none\"' "
		 s += "onchange='document.all.tmpSelectYearLayer.style.display=\"none\";"
		 s += "parent.meizzTheYear = this.value; parent.meizzSetDay(parent.meizzTheYear,parent.meizzTheMonth)'>\r\n";
	  var selectInnerHTML = s;
	  for (var i = n; i < n + 61; i++) //next year
	  {
		if (i == m)
		   {selectInnerHTML += "<option Author=wayx value='" + i + "' selected>" + i + "��" + "</option>\r\n";}
		else {selectInnerHTML += "<option Author=wayx value='" + i + "'>" + i + "��" + "</option>\r\n";}
	  }
	  selectInnerHTML += "</select>";
	  this.dateLayer.getElementById("tmpSelectYearLayer").style.display="";
	  this.dateLayer.getElementById("tmpSelectYearLayer").innerHTML = selectInnerHTML;
	  if(!this.isIE6)
			this.dateLayer.getElementById("tmpSelectYear").focus();
}

//== �·ݵ�������
function tmpSelectMonthInnerHTML(strMonth) 
{
	  if (strMonth.match(/\D/)!=null){alert("�·���������������֣�");return;}
	  var m = (strMonth) ? strMonth : this.init.getMonth() + 1;
	  var s = "<select Author=meizz name=tmpSelectMonth style='font-size: 12px' "
		 s += "onblur='document.all.tmpSelectMonthLayer.style.display=\"none\"' "
		 s += "onchange='document.all.tmpSelectMonthLayer.style.display=\"none\";"
		 s += "parent.meizzTheMonth = this.value; parent.meizzSetDay(parent.meizzTheYear,parent.meizzTheMonth)'>\r\n";
	  var selectInnerHTML = s;
	  for (var i = 1; i < 13; i++)
	  {
		if (i == m)
		   {selectInnerHTML += "<option Author=wayx value='"+i+"' selected>"+i+"��"+"</option>\r\n";}
		else {selectInnerHTML += "<option Author=wayx value='"+i+"'>"+i+"��"+"</option>\r\n";}
	  }
	  selectInnerHTML += "</select>";
	  this.dateLayer.getElementById("tmpSelectMonthLayer").style.display="";
	  this.dateLayer.getElementById("tmpSelectMonthLayer").innerHTML = selectInnerHTML;
	  if(!this.isIE6)
			this.dateLayer.getElementById("tmpSelectMonth").focus();
}

function tmpSelectHourHTML(str){
	var h = (str!=' ')?parseInt(str,10):this.init.getHour();
	var s = '<select name=selHour Author=meizz ';
		 s += 'onblur = "document.all.tmpSelectHourLayer.style.display=\'none\';" ';
		 s += 'onchange="document.all.tmpSelectHourLayer.style.display=\'none\';';
		 s += 'document.all.tdId_hour.innerText =this.value+\'��\';parent.getSelectTimeStr(this.value,0)">\r\n';
		for(j=0;j<24;j++){
		    var temp = j< 10 ? "0"+j:j;
			if(h == j)
				s+= '<option value="'+temp+'" selected>'+temp+'</option>\n';
			else
			    s+='<option value="'+temp+'">'+temp+'</option>\n';
		}
	   s+= '<select>';	
	  this.dateLayer.getElementById("tmpSelectHourLayer").style.display="";
	 this.dateLayer.getElementById("tmpSelectHourLayer").innerHTML = s;
	  if(!this.isIE6)
		this.dateLayer.getElementById("tmpSelectHour").focus();
}
function tmpSelectMiniHTML(str){
	var h = (str!=' ')?parseInt(str,10):this.init.getHour();
	var s = '<select name=selHour  Author=meizz ';
		 s += 'onblur = "document.all.tmpSelectMiniLayer.style.display=\'none\'" ';
		 s += 'onchange="document.all.tmpSelectMiniLayer.style.display=\'none\';';
		 s += 'document.all.tdId_mini.innerText =this.value+\'��\';parent.getSelectTimeStr(this.value,1)">\r\n';
		for(j=0;j<60;j++){
		    var temp = j< 10 ? "0"+j:j;
			if(h == j)
				s+= '<option value="'+temp+'" selected>'+temp+'</option>\n';
			else
			    s+='<option value="'+temp+'">'+temp+'</option>\n';
		}
	   s+= '<select>';	
	  this.dateLayer.getElementById("tmpSelectMiniLayer").style.display="";
	  this.dateLayer.getElementById("tmpSelectMiniLayer").innerHTML = s;
	  if(!this.isIE6)
		this.dateLayer.getElementById("tmpSelectMini").focus();
}
function tmpSelectSecoHTML(str){
	var h = (str!=' ')?parseInt(str,10):this.init.getHour();
	var s = '<select name=selHour Author=meizz ';
		 s += 'onblur = "document.all.tmpSelectSecoLayer.style.display=\'none\'" ';
		 s += 'onchange="document.all.tmpSelectSecoLayer.style.display=\'none\';';
		 s += 'document.all.tdId_seco.innerText =this.value;parent.getSelectTimeStr(this.value,2);">\r\n';
		for(j=0;j<60;j++){
		    var temp = j< 10 ? "0"+j:j;
			if(h == j)
				s+= '<option value="'+temp+'" selected>'+temp+'</option>\n';
			else
			    s+='<option value="'+temp+'">'+temp+'</option>\n';
		}
	   s+= '<select>';	
	  this.dateLayer.getElementById("tmpSelectSecoLayer").style.display="";
	  this.dateLayer.getElementById("tmpSelectSecoLayer").innerHTML = s;
	  if(!this.isIE6)
		this.dateLayer.getElementById("tmpSelectSeco").focus();
}

//ie6�����������������л����㴦�����
//== ������ʱ�رոÿؼ�	
/*function document.onclick() {
  with(window.event){  
	 //if (srcElement.getAttribute("Author")==null && srcElement != dateObj.textObj && srcElement != dateObj.btnObj)
	 //dateObj.closeLayer();
  	 if (typeof dateObj.textObj == 'object' && srcElement !=dateObj.textObj && srcElement !=dateObj.btnObj){
	 	 dateObj.closeLayer();
		//dateObj.dateLayer.body.style.display="none";
	 }
  }
}*/
//== ʹ�ÿ�ݼ� �л� 
function simulate(){ 
    if (window.event.keyCode==27){
		if(dateObj.textObj) dateObj.textObj.value=dateObj.lastValue;
		dateObj.textObj.blur();
		dateObj.closeLayer();
	}
	else if (13==event.keyCode||32==event.keyCode){  
		//enter�� �ո�� ת�ƽ��㵽��һ���ؼ�
		if(document.activeElement){
               event.keyCode = 9;
				dateObj.closeLayer();
		}
	}
}
//== ������������
function getWinHelp(){}