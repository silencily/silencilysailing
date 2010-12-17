/* 建立日历对象
 * 2005-03 liuz 
 * 由date.js 改进而来
 */

var Calendar = {};

Calendar.DisplayDate = function(temp_name,table_name,currentDate){

     this.Name    = temp_name;
     _this = this;
     this.tabName = table_name;
     var temp = currentDate ? currentDate.split("-") : new Array();
     this.currentDate  = currentDate ? new Date(temp[0],temp[1],temp[2]) : new Date();
     this.currentDateString = currentDate; //TODO
     this.meizzTheYear = this.currentDate.getFullYear();    //定义年的变量的初始值
     this.meizzTheMonth= this.currentDate.getMonth()+1;     //定义月的变量的初始值  
     this.MonHead      = new Array(31,28,31,30,31,30,31,31,30,31,30,31)    
     this.meizzWDay    = new Array(39);               //定义写日期的数组
     this.selectDate   = ''                             //存放当前选中日期yyyy-MM-dd
     this.doc      = document.all;                 //存放日历对象
     this.lastItem    = '';
     this.work= new Array(39);   //存放工作内容
     this.workContents='';
     //
     this.noColor     = '#ffffff'
     this.lightColor  = ''
     this.weightColor = ''
     this.lineColor   = '#ffffff'                    
     //menthod
     this.InitDate = Calendar.getInitDate    //定义初始化日期格式：yyyy-mm-dd
     this.Click    = Calendar.meizzDayClick; //单击
     this.DoubleClick=Calendar.meizzDayDoubleClick;   //双击
     this.Display  = Calendar.meizzSetDay;   //显示日历
     this.PreMonth = Calendar.meizzPrevM     //
     this.NextMonth= Calendar.meizzNextM
     this.PreYear  = Calendar.meizzPrevY
     this.NextYear = Calendar.meizzNextY
     this.ClickToday = Calendar.meizzToday
     this.getHead  = Calendar.meizzWriteHead
     this.PinYear  = Calendar.IsPinYear
     this.PinMon   = Calendar.GetPinMonth
     this.PinWeek  = Calendar.GetDOW 
     this.getUrl   = Calendar.GetActionUrl    //点击日历后跳转事件
     this.setSelctDate    = Calendar.setSelctDate
     this.setSelectClass = Calendar.setSelectClass;
     //
     this.disableDate = new Array() //不可用的日期数组([1,2,3])，用于点击时候屏蔽掉
     this.isCurrentAble = false;    //是否只允许当前有效，上月和下月的都屏蔽
     this.tempDate = '';
     this.tempYear = '';
     this.tempMonth = '';
     this.tempDay  = '';
}

Calendar.getInitDate = function(temp_date,workContents){
     //初始日期与当前日期可能不同
     this.tempDate  = temp_date ? temp_date : this.currentDateString;
     this.tempYear  = parseInt(this.tempDate.split("-")[0],10);
     this.tempMonth = parseInt(this.tempDate.split("-")[1],10);
     this.tempDay   = parseInt(this.tempDate.split("-")[2],10);
     //工作内容
     this.workContents=workContents;
     //============= the CSS of Date Page
     this.weightColor="#b9cbf7";
     this.lightColor ="#eaeffd";

     var strFrame='';//存放日历层的HTML代码

     strFrame+='<table border=0 cellspacing=1 cellpadding=0 class="calendar_table" >';//#ff9900
     strFrame+='  <tr><td><table border=0 cellspacing=0 cellpadding=0 class="calendar_opera_table"><tr align=center >';
     strFrame+='      <td class="calendar_opera" onclick="'+this.Name+'.PreYear()" title="向前翻1 年"><b>&lt;</b></td>';
     strFrame+='      <td class="calendar_opera" onclick="'+this.Name+'.NextYear()" title="向后翻1 年"><b>&gt;</b></td>';
     strFrame+='        <td ><span id=meizzYearHead></span></td>';
     strFrame+='           <td ><span id=meizzMonthHead></span></td>';
     strFrame+='      <td class="calendar_opera" onclick="'+this.Name+'.PreMonth()" title="向前翻1 月"><b>&lt;</b></td>';
     strFrame+='      <td class="calendar_opera" onclick="'+this.Name+'.NextMonth()" title="向后翻1 月"><b>&gt;</b></td>';
     strFrame+='</tr></table></td></tr>';
     strFrame+='<tr><td>';
     strFrame+='        <table cellspacing=0 cellpadding=0 class="calendar_week" >';
     strFrame+='        <tr align=center>';
     strFrame+='        <td >日</td>';
     strFrame+='        <td >一</td><td >二</td>';
     strFrame+='        <td >三</td><td >四</td>';
     strFrame+='        <td >五</td><td >六</td>';
     strFrame+='        </tr></table></td></tr>';
     strFrame+='<tr><td>';
     strFrame+='    <table border=0 cellspacing=0 cellpadding=0  class="calendar_content">';
                       var n=0; for (j=0;j<5;j++){ strFrame+= ' <tr align=center >'; for (i=0;i<7;i++){
     strFrame+='            <td width=100 height=100 id=meizzDay'+n+' valign=top></td>';n++;}
     strFrame+='        </tr>';}
     strFrame+='        <tr align=center >';
                       for (i=35;i<39;i++)strFrame+='<td width=100 height=100 id=meizzDay'+i+' valign=top></td>'
     strFrame+='        <td colspan=3 onclick="'+this.Name+'.ClickToday()"  style="cursor:pointer"> 今天';
     strFrame+='   </td></tr></table></td></tr></table>';
     eval('document.all.'+this.tabName).innerHTML = strFrame
     this.Display(this.tempYear,this.tempMonth);    
     this.selectDate = this.tempDate ;
}

 

Calendar.meizzWriteHead = function(yy,mm)  //往head 中写入当前的年与月
{
     this.doc.meizzYearHead.innerText  = yy + " 年";
     this.doc.meizzMonthHead.innerText = mm + " 月";
}

 

Calendar.IsPinYear = function(year)            //判断是否闰平年
{
    if (0==year%4&&((year%100!=0)||(year%400==0))) return true;else return false;
}

Calendar.GetPinMonth = function(year,month)  //闰年二月为天
{
    var c=this.MonHead[month-1];
     if((month==2)&&this.PinYear(year)) 
         c++;
     return c;
}

Calendar.GetDOW  = function(day,month,year)     //求某天的星期几
{
    var dt=new Date(year,month-1,day).getDay()/7; 
     return dt;
}

 

Calendar.meizzPrevY  = function()  //往前翻Year
{
    if(this.meizzTheYear > 999 && this.meizzTheYear <10000){this.meizzTheYear--;}
    else{alert("年份超出范围（-9999）！");}
	this.meizzTheMonth= this.meizzTheMonth>10 ? this.meizzTheMonth : "0"+ this.meizzTheMonth;
	var today=this.meizzTheYear + '-' + this.meizzTheMonth + '-01';
	Calendar.ForwardActionUrl(today);
}

Calendar.meizzNextY = function()  //往后翻Year
{
    if(this.meizzTheYear > 999 && this.meizzTheYear <10000){this.meizzTheYear++;}
    else{alert("年份超出范围（-9999）！");}
	this.meizzTheMonth= this.meizzTheMonth>10 ? this.meizzTheMonth : "0"+ this.meizzTheMonth;
	var today=this.meizzTheYear + '-' + this.meizzTheMonth + '-01';
	Calendar.ForwardActionUrl(today);
}

//点击今天

Calendar.meizzToday = function(){
    this.meizzTheYear  = this.currentDate.getFullYear();
    this.meizzTheMonth = this.currentDate.getMonth()+1;
	this.meizzTheMonth= this.meizzTheMonth>10 ? this.meizzTheMonth : "0"+ this.meizzTheMonth; 
    var today=this.currentDate.getDate();
	if(today < 10)
	{
		today = "0" + today;
	}
    today= this.meizzTheYear +"-"+this.meizzTheMonth+"-"+today;
	if(this.selectDate.substring(0,7) ==today.substring(0,7))
	{
		this.InitDate(today,this.workContents);
	}else
	{
		Calendar.ForwardActionUrl(today);
	}
}

Calendar.meizzPrevM = function()  //往前翻月份
{
    if(this.meizzTheMonth>1){this.meizzTheMonth--}else{this.meizzTheYear--;this.meizzTheMonth=12;}
	this.meizzTheMonth= this.meizzTheMonth>10 ? this.meizzTheMonth : "0"+ this.meizzTheMonth;
	var today=this.meizzTheYear + '-' + this.meizzTheMonth + '-01';
	Calendar.ForwardActionUrl(today);
}

Calendar.meizzNextM = function()  //往后翻月份
{
    if(this.meizzTheMonth==12){this.meizzTheYear++;this.meizzTheMonth=1}else{this.meizzTheMonth++}
	this.meizzTheMonth= this.meizzTheMonth>10 ? this.meizzTheMonth : "0"+ this.meizzTheMonth;
	var today=this.meizzTheYear + '-' + this.meizzTheMonth + '-01';
	Calendar.ForwardActionUrl(today);
}

Calendar.meizzSetDay = function(yy,mm){   //主要的写程序=this.Display() **********

     this.lastItem = "";
     this.meizzTheYear=yy;
     this.meizzTheMonth=mm;
     this.getHead(yy,mm);
     //设置当前年月的公共变量为传入值
     for (var i = 0; i < 39; i++){this.meizzWDay[i]="";  this.work[i]='<br>';};  //将显示框的内容全部清空
     var day1 = 1,day2=1,firstday = new Date(yy,mm-1,1).getDay();  //某月第一天的星期几
     for (i=0;i<firstday;i++)
     { 
       this.meizzWDay[i]=this.PinMon(mm==1?yy-1:yy,mm==1?12:mm-1)-firstday+i+1  //上个月的最后几天
     }
	var temp = 0;
     for (i = firstday; day1 < this.PinMon(yy,mm)+1; i++)    //当前月
       {
           this.meizzWDay[i]=day1;
          //填入工作内容
          for(j = temp; j< this.workContents.length; j++)
          {
			  this.workContents[j]=this.workContents[j].Trim();
              if(this.workContents[j].match(/^(\d{6})(\d{2})([\s\*])(.+)$/))  //符合"年月工作内容.." 的格式
              {  
                  var m0 = mm>10 ? mm : "0"+mm;
                  if(  (Number(RegExp.$1)==yy+''+m0) && (Number(RegExp.$2))== this.meizzWDay[i] )    //与年月日匹配
                  {
                       this.work[i] +='<a >'+RegExp.$4 + '</a><br/> '    //这一天的工作内容是表达式的内容
						temp++;
                  }
             }
          }
          day1++;
       }

     for (i=firstday+this.PinMon(yy,mm);i<39;i++)          //下个月的前几天
       {this.meizzWDay[i]=day2;day2++}

     //为日期添加属性：样式与事件
     var tdClick = null;
     var tdDoubleClick=null;
     var tdHtml  = "";
     var tdTitle = "";
     var tdClass = "";
     var disableFlag = false;

     for (i = 0; i < 39; i++)
     { 
         var da = eval("document.all.meizzDay"+i)     //书写新的一个月的日期星期排列
         da.onclick = "";
         da.className = "";
         tdTitle   = "";
         tdHtml    = "";
         tdClass   = "calendar_text_able";
         if (this.meizzWDay[i]!="")
         {
             var nn1 = parseInt(this.meizzWDay[i],10);
                  nn1 = nn1 >= 10 ? nn1 : "0"+nn1;         //小于前面补0
             var yy1 = yy;
             var mm1 = mm;
             var mms = mm1>=10 ? mm1 : "0"+mm1;

             disableFlag   = false;
             //上个月的部分
             if(i<firstday)
             {         
                  mm1 = (mm==1?12:parseInt(mm,10)-1);                 
                  if(this.isCurrentAble)
                  {
                       disableFlag = true;
                  }
                  tdClick  = Function(""+this.Name+".Click(this.innerText,-1,this)");    
                   //tdDoubleClick=Function ( ""+ this.Name+".DoubleClick(this.innerText,-1,this)");   
                  tdClass  = "calendar_noCurrent";
             }
             //下个月的部分
             else if (i>=firstday+this.PinMon(yy,mm)) 
             {
                  mm1 = (mm==12?1:parseInt(mm,10)+1);
                  if(this.isCurrentAble)
                  {
                       disableFlag = true;
                  }
                  tdClick=Function(""+this.Name+".Click(this.innerText,1,this)");
                  //tdDoubleClick=Function ( ""+ this.Name+".DoubleClick(this.innerText,1,this)");     
                  tdClass = "calendar_noCurrent";       
              }
              //本月的部分
              else
              {        
                   if((yy +"-"+ mms + "-"+ nn1) > this.currentDateString)
                   {
                       tdClass = "calendar_text_disable";
                   }
                   else
                   {
                        tdClick  = Function(""+this.Name+".Click(this.innerText,0,this)");   
                        tdDoubleClick=Function ( ""+ this.Name+".DoubleClick(this.innerText,0,this)");     
                           if((yy +"-"+ mms + "-"+ nn1) == this.tempDate)
                           {
                                //初始化选中的激活 
                                tdClass = "calendar_selectDate";
                                this.setSelectClass(da);                            
                           }
                           else
                           {
                                for(var t=0;t<this.disableDate.length;t++){
                                     if(this.disableDate[t] == yy +"-"+ mm1 + "-"+ nn1){
                                         disableFlag = true;break;
                                     }
                           }
                       }
                       if(disableFlag)
                       {
                            tdClass = "calendar_text_disable";
                       }
                       else
                       {
                            //给td赋予onclick事件的处理
                            da.onclick=tdClick;         
                            da.ondblclick=tdDoubleClick;     
                            tdHtml = "<a>" + tdHtml + "</a>"
                       }
                   }
              }
             tdHtml  =  this.meizzWDay[i]+ this.work[i] ;
              tdTitle = mm1 +"月" + this.meizzWDay[i] + "日";
         }    
         da.className       = tdClass;
         da.innerHTML       = tdHtml;
         da.title           = tdTitle;
     }
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
}

Calendar.setSelectClass = function(obj){
     obj.className = "calendar_selectDate";
     if(this.lastItem != obj && this.lastItem !=""){
         this.lastItem.className = "calendar_text_able";
     }
     this.lastItem = obj;
}

Calendar.setSelctDate = function(yy,mm,dd){
     var mms = mm;
     if (mms < 10)
         mms = "0" + mms;
     var nns = dd;
     if ( nns < 10)
         nns = "0" + nns;                 
     this.selectDate = yy + "-" + mms + "-" + nns ;
     //this.getUrl(yy,mm,dd);
}

String.prototype.Trim = function() 
{
	return this.replace(/(^\s*)|(\s*$)/g, ""); 
} 

//向外接口函数，可以自定义内部方法 
Calendar.GetActionUrl = function(year,month,day){

}

 

//双击跳转到第二个页面.  //n 代表点击的点击的天，ex -1 代表上个月，0代表本月 1 代表下个月，obj指dateobj
Calendar.meizzDayDoubleClick=function (n,ex, obj)
{

}

Calendar.ForwardActionUrl = function(today)
{

}
