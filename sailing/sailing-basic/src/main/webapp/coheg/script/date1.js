
function  DisplayDate(temp_name,table_name)
{
	this.Name    = temp_name;
	this.tabName = table_name;
	this.MonHead      = new Array(31,28,31,30,31,30,31,31,30,31,30,31)
	this.meizzTheYear = new Date().getFullYear();    //定义年的变量的初始值
	this.meizzTheMonth= new Date().getMonth()+1;     //定义月的变量的初始值
	this.meizzWDay    = new Array(39);               //定义写日期的数组
	this.outDate     = ''							//存放对象的日期
	this.doc		 = document.all;				//存放日历对象 odatelayer
	this.lastItem    = '';
	//
	this.noColor     = '#ffffff'
	this.lightColor  = ''
	this.weightColor = ''
	this.lineColor   = '#ffffff'                    
	
	//menthod
	this.InitDate = getInitDate
	this.Click    = meizzDayClick;
	this.Display  = meizzSetDay;
	this.PreMonth = meizzPrevM
	this.NextMonth= meizzNextM
	this.PreYear  = meizzPrevY
	this.NextYear = meizzNextY
	this.ClickToday = meizzToday
	this.getHead  = meizzWriteHead
	this.PinYear  = IsPinYear
	this.PinMon   = GetPinMonth
	this.PinWeek  = GetDOW 
	this.getUrl   = GetActionUrl
	//
}

function getInitDate(temp_date)
{
	//============= the CSS of Date Page
	try{  
		this.weightColor=document.styleSheets[0].rules[26].style.backgroundColor;//
		this.lightColor =document.styleSheets[0].rules[28].style.backgroundColor;//	
	}catch(e)
	{
		this.weightColor="#b9cbf7";
		this.lightColor ="#eaeffd";
	}
	var strFrame='';//存放日历层的HTML代码
	strFrame+='<table border=1 cellspacing=0 cellpadding=0 width=180 bordercolor='+this.weightColor+' bgcolor='+this.weightColor+' >';//#ff9900
	strFrame+='  <tr><td width=100%  bgcolor='+this.noColor+'><table border=0 cellspacing=1 cellpadding=0 width=100%  height=25><tr align=center >';
	strFrame+='      <td width=18 align=center bgcolor="'+this.weightColor+'"   style="font-size:13px;cursor: hand;color: '+this.noColor+'" onclick="'+this.Name+'.PreYear()" title="向前翻 1 年"><b>&lt;</b></td>';
	strFrame+='      <td width=18 align=center bgcolor="'+this.weightColor+'"   style="font-size:13px;cursor: hand;color: '+this.noColor+'" onclick="'+this.Name+'.NextYear()" title="向后翻 1 年"><b>&gt;</b></td>';
	strFrame+='        <td width=80 align=center bgcolor="'+this.lightColor+'"  style="font-size:12px;cursor:default" ><span id=meizzYearHead></span></td>';
	strFrame+='		   <td width=70 align=center bgcolor="'+this.lightColor+'"  style="font-size:12px;cursor:default" ><span id=meizzMonthHead></span></td>';
	strFrame+='      <td width=18 align=center bgcolor="'+this.weightColor+'"   style="font-size:13px;cursor: hand;color: '+this.noColor+'" onclick="'+this.Name+'.PreMonth()" title="向前翻 1 月"><b>&lt;</b></td>';
	strFrame+='      <td width=18 align=center bgcolor="'+this.weightColor+'"   style="font-size:13px;cursor: hand;color: '+this.noColor+'" onclick="'+this.Name+'.NextMonth()" title="向后翻 1 月"><b>&gt;</b></td>';
	strFrame+='</tr></table></td></tr>';
	strFrame+='<tr><td width=100% >';
	strFrame+='		<table border=1 cellspacing=0 cellpadding=0 bgcolor='+this.weightColor+' BORDERCOLORLIGHT='+this.weightColor+' BORDERCOLORDARK='+this.noColor+' width="100%" height=30  style="cursor:default">';
	strFrame+='		<tr align=center>';
	strFrame+='		<td >日</td>';
	strFrame+='		<td >一</td><td >二</td>';
	strFrame+='		<td >三</td><td >四</td>';
	strFrame+='		<td >五</td><td >六</td>';
	strFrame+='		</tr></table></td></tr>';
	strFrame+='<tr><td width="100%" >';
	strFrame+='    <table border=1 cellspacing=0 cellpadding=0 BORDERCOLORLIGHT='+this.weightColor+' BORDERCOLORDARK='+this.lineColor+' bgcolor='+this.lightColor+' width=100% >';
					var n=0; for (j=0;j<5;j++){ strFrame+= ' <tr align=center >'; for (i=0;i<7;i++){
	strFrame+='			<td width=35 height=25 id=meizzDay'+n+' style="font-size:12px" onclick="'+this.Name+'.Click(this.innerText,0,this)"></td>';n++;}
	strFrame+='		</tr>';}
	strFrame+='		<tr align=center >';
					for (i=35;i<39;i++)strFrame+='<td width=35 height=25 id=meizzDay'+i+' style="font-size:12px"  onclick="'+this.Name+'.Click(this.innerText,0,this)"></td>';
	strFrame+='        <td colspan=3 onclick="'+this.Name+'.ClickToday()" style="font-size:12px;cursor: hand;"> 今天';
	strFrame+='	</td></tr></table></td></tr></table>';
	eval('document.all.'+this.tabName).innerHTML = strFrame

	if(!temp_date || temp_date == '')
		this.Display(new Date().getFullYear(), new Date().getMonth() + 1)
	else 
	{
		var temp_array = temp_date.split('-');
		if(temp_array.length == 2)
		{
			this.outDate = '' ;
			this.Display(temp_array[0],temp_array[1]);
		}
		else if(temp_array.length == 3)
		{
			this.outDate = temp_date ;//new Date(temp_date.getFullYear(),temp_date.getMonth(),temp_date.getDate());
			this.Display(temp_array[0],temp_array[1]);
		}else
			alert('日期格式有错误！yyyy-mm | yyyy-mm-dd : '+temp_date);
	}
}

function meizzWriteHead(yy,mm)  //往 head 中写入当前的年与月
{
	this.doc.meizzYearHead.innerText  = yy + " 年";
	this.doc.meizzMonthHead.innerText = mm + " 月";
}

function IsPinYear(year)            //判断是否闰平年
{
    if (0==year%4&&((year%100!=0)||(year%400==0))) return true;else return false;
}
function GetPinMonth(year,month)  //闰年二月为29天
{
    var c=this.MonHead[month-1];
	if((month==2)&&this.PinYear(year)) 
		c++;
	return c;
}
function GetDOW(day,month,year)     //求某天的星期几
{
    var dt=new Date(year,month-1,day).getDay()/7; 
	return dt;
}

function meizzPrevY()  //往前翻 Year
{
    if(this.meizzTheYear > 999 && this.meizzTheYear <10000){this.meizzTheYear--;}
    else{alert("年份超出范围（1000-9999）！");}
    this.Display(this.meizzTheYear,this.meizzTheMonth);
}
function meizzNextY()  //往后翻 Year
{
    if(this.meizzTheYear > 999 && this.meizzTheYear <10000){this.meizzTheYear++;}
    else{alert("年份超出范围（1000-9999）！");}
    this.Display(this.meizzTheYear,this.meizzTheMonth);
}
function meizzToday()  //Today Button
{
	var today;
    this.meizzTheYear = new Date().getFullYear();
    this.meizzTheMonth = new Date().getMonth()+1;
    today=new Date().getDate();
	this.getUrl(this.meizzTheYear ,  this.meizzTheMonth ,today);
    //this.Display(this.meizzTheYear,this.meizzTheMonth);
    //if(global_date_input){
		//global_date_input.value=this.meizzTheYear + "-" + (this.meizzTheMonth > 9 ? this.meizzTheMonth + "" : "0" + this.meizzTheMonth) + "-" + (today > 9 ? today + "" : "0" + today);
    //}
    //closeLayer();
}
function meizzPrevM()  //往前翻月份
{
    if(this.meizzTheMonth>1){this.meizzTheMonth--}else{this.meizzTheYear--;this.meizzTheMonth=12;}
    this.Display(this.meizzTheYear,this.meizzTheMonth);
}
function meizzNextM()  //往后翻月份
{
    if(this.meizzTheMonth==12){this.meizzTheYear++;this.meizzTheMonth=1}else{this.meizzTheMonth++}
    this.Display(this.meizzTheYear,this.meizzTheMonth);
}

function meizzSetDay(yy,mm)   //主要的写程序**********
{
  this.meizzTheYear=yy;
  this.meizzTheMonth=mm;
  this.getHead(yy,mm);
  //设置当前年月的公共变量为传入值

  for (var i = 0; i < 39; i++){this.meizzWDay[i]=""};  //将显示框的内容全部清空

  var day1 = 1,day2=1,firstday = new Date(yy,mm-1,1).getDay();  //某月第一天的星期几

  for (i=0;i<firstday;i++)
	  this.meizzWDay[i]=this.PinMon(mm==1?yy-1:yy,mm==1?12:mm-1)-firstday+i+1	//上个月的最后几天
  for (i = firstday; day1 < this.PinMon(yy,mm)+1; i++)
	  {this.meizzWDay[i]=day1;day1++;}
  for (i=firstday+this.PinMon(yy,mm);i<39;i++)
	  {this.meizzWDay[i]=day2;day2++}

  for (i = 0; i < 39; i++)
  { 
	var da = eval("document.all.meizzDay"+i)     //书写新的一个月的日期星期排列
    if (this.meizzWDay[i]!="")
     {
		var nn1=this.meizzWDay[i]
		var yy1=yy;
		var mm1=mm;
		da.borderColorLight=this.weightColor;
		da.borderColorDark=this.noColor;
		if(i<firstday)		//上个月的部分
		{
			da.innerHTML="<font color=gray>" + this.meizzWDay[i] + "</font>";
			da.title=(mm==1?12:parseInt(mm)-1) +"月" + this.meizzWDay[i] + "日";
			da.onclick=Function(""+this.Name+".Click(this.innerText,-1,this)");
			if(!this.outDate)
				da.style.backgroundColor = ((mm==1?yy-1:yy) == new Date().getFullYear() &&
					(mm==1?12:mm-1) == new Date().getMonth()+1 && this.meizzWDay[i] == new Date().getDate()) ?
					 this.weightColor:this.lightColor;
			else
			{
				if(mm<2)
				{
					mm1=12;yy1=parseInt(yy-1)
				}else
					mm1=mm-1;
				if(this.outDate !='' && this.outDate == yy1+"-"+mm1+"-"+nn1)
				{
					da.borderColorLight= this.noColor;
					da.borderColorDark = this.weightColor;
				}				
				/*da.style.backgroundColor =((mm==1?yy-1:yy)==this.outDate.getFullYear() && (mm==1?12:mm-1)== this.outDate.getMonth() + 1 &&
				this.meizzWDay[i]==this.outDate.getDate())? this.lineColor :
				(((mm==1?yy-1:yy) == new Date().getFullYear() && (mm==1?12:mm-1) == new Date().getMonth()+1 &&
				this.meizzWDay[i] == new Date().getDate()) ? this.weightColor:this.lightColor);
				//将选中的日期显示为凹下去

				if((mm==1?yy-1:yy)==this.outDate.getFullYear() && (mm==1?12:mm-1)== this.outDate.getMonth() + 1 &&
				this.meizzWDay[i]==this.outDate.getDate())
				{
					da.borderColorLight=this.noColor;
					da.borderColorDark=this.weightColor;
				}*/
			}
		}
		else if (i>=firstday+this.PinMon(yy,mm))		//下个月的部分
		{
			da.innerHTML="<font color=gray>" + this.meizzWDay[i] + "</font>";
			da.title=(mm==12?1:parseInt(mm)+1) +"月" + this.meizzWDay[i] + "日";
			da.onclick=Function(""+this.Name+".Click(this.innerText,1,this)");
			if(!this.outDate)
				da.style.backgroundColor = ((mm==12?yy+1:yy) == new Date().getFullYear() &&
					(mm==12?1:mm+1) == new Date().getMonth()+1 && this.meizzWDay[i] == new Date().getDate()) ?
					 this.weightColor:this.lightColor;
			else
			{
				if(mm>11)
				{
					mm1=1;yy1=parseInt(yy+1)
				}else
					mm1=mm+1;
				if(this.outDate !='' && this.outDate == yy1+"-"+mm1+"-"+nn1)
				{
					da.borderColorLight= this.noColor;
					da.borderColorDark = this.weightColor;
				}
				/*da.style.backgroundColor =((mm==12?yy+1:yy)==this.outDate.getFullYear() && (mm==12?1:mm+1)== this.outDate.getMonth() + 1 &&
				this.meizzWDay[i]==this.outDate.getDate())? this.lineColor :
				(((mm==12?yy+1:yy) == new Date().getFullYear() && (mm==12?1:mm+1) == new Date().getMonth()+1 &&
				this.meizzWDay[i] == new Date().getDate()) ? this.weightColor:this.lightColor);
				//将选中的日期显示为凹下去

				if((mm==12?yy+1:yy)==this.outDate.getFullYear() && (mm==12?1:mm+1)== this.outDate.getMonth() + 1 &&
				this.meizzWDay[i]==this.outDate.getDate())
				{
					da.borderColorLight=this.noColor;
					da.borderColorDark=this.weightColor;
				}*/
			}
		}
		else		//本月的部分
		{
			da.innerHTML="" + this.meizzWDay[i] + "";
			da.title=mm +"月" + this.meizzWDay[i] + "日";
			da.onclick=Function(""+this.Name+".Click(this.innerText,0,this)");		//给td赋予onclick事件的处理

			//如果是当前选择的日期，则显示亮蓝色的背景；如果是当前日期，则显示暗黄色背景
			if(!this.outDate)
				da.style.backgroundColor = (yy == new Date().getFullYear() && mm == new Date().getMonth()+1 && this.meizzWDay[i] == new Date().getDate())?
					this.weightColor:this.lightColor;
			else
			{
				if(this.outDate !='' && this.outDate == (yy1+"-"+mm1+"-"+nn1))
				{
					da.borderColorLight= this.noColor;
					da.borderColorDark = this.weightColor;
					da.style.backgroundColor = this.lineColor;
				}
				/*da.style.backgroundColor =(yy==this.outDate.getFullYear() && mm== this.outDate.getMonth() + 1 && this.meizzWDay[i]==this.outDate.getDate())?
					this.lineColor:((yy == new Date().getFullYear() && mm == new Date().getMonth()+1 && this.meizzWDay[i] == new Date().getDate())?
					this.weightColor:this.lightColor);
				//将选中的日期显示为凹下去

				if(yy==this.outDate.getFullYear() && mm== this.outDate.getMonth() + 1 && this.meizzWDay[i]==this.outDate.getDate())
				{
					da.borderColorLight=this.noColor;
					da.borderColorDark=this.weightColor;
				}*/
			}
		}
        da.style.cursor="hand"
      }
    else{da.innerHTML="";da.style.backgroundColor="";da.style.cursor="default"}
  }
}

function meizzDayClick(n,ex,obj)  //点击显示框选取日期，主输入函数*************
{
	obj.borderColorLight=this.noColor;
	obj.borderColorDark=this.weightColor;
	obj.style.backgroundColor = '#ffffff';
	if(this.lastItem !='')
	{
	  this.lastItem.borderColorLight=this.weightColor;
	  this.lastItem.borderColorDark =this.noColor;
	  this.lastItem.style.backgroundColor = '';
	}
	this.lastItem = obj;
	var yy=this.meizzTheYear;
	var mm = parseInt(this.meizzTheMonth)+ex;	//ex表示偏移量，用于选择上个月份和下个月份的日期
	//判断月份，并进行对应的处理
	if(mm<1){
		yy--;
		mm=12+mm;
	}
	else if(mm>12){
		yy++;
		mm=mm-12;
	}
	/*if (mm < 10)
		mm = "0" + mm;
	if (!n) 
		return;
	if ( n < 10)
		n = "0" + n;*/  				
	//this.outDate= yy + "-" + mm + "-" + n ; //注：在这里你可以输出改成你想要的格式
	this.getUrl(yy,mm,n);
}
//向外接口函数，可以自定义内部方法
function GetActionUrl(year,month,day)
{
	//alert(year);
}