/* ������������
 * 2005-03 liuz 
 * ��date.js �Ľ�����
 */

var Calendar = {};

Calendar.DisplayDate = function(temp_name,table_name,currentDate){

     this.Name    = temp_name;
     _this = this;
     this.tabName = table_name;
     var temp = currentDate ? currentDate.split("-") : new Array();
     this.currentDate  = currentDate ? new Date(temp[0],temp[1],temp[2]) : new Date();
     this.currentDateString = currentDate; //TODO
     this.meizzTheYear = this.currentDate.getFullYear();    //������ı����ĳ�ʼֵ
     this.meizzTheMonth= this.currentDate.getMonth()+1;     //�����µı����ĳ�ʼֵ  
     this.MonHead      = new Array(31,28,31,30,31,30,31,31,30,31,30,31)    
     this.meizzWDay    = new Array(39);               //����д���ڵ�����
     this.selectDate   = ''                             //��ŵ�ǰѡ������yyyy-MM-dd
     this.doc      = document.all;                 //�����������
     this.lastItem    = '';
     this.work= new Array(39);   //��Ź�������
     this.workContents='';
     //
     this.noColor     = '#ffffff'
     this.lightColor  = ''
     this.weightColor = ''
     this.lineColor   = '#ffffff'                    
     //menthod
     this.InitDate = Calendar.getInitDate    //�����ʼ�����ڸ�ʽ��yyyy-mm-dd
     this.Click    = Calendar.meizzDayClick; //����
     this.DoubleClick=Calendar.meizzDayDoubleClick;   //˫��
     this.Display  = Calendar.meizzSetDay;   //��ʾ����
     this.PreMonth = Calendar.meizzPrevM     //
     this.NextMonth= Calendar.meizzNextM
     this.PreYear  = Calendar.meizzPrevY
     this.NextYear = Calendar.meizzNextY
     this.ClickToday = Calendar.meizzToday
     this.getHead  = Calendar.meizzWriteHead
     this.PinYear  = Calendar.IsPinYear
     this.PinMon   = Calendar.GetPinMonth
     this.PinWeek  = Calendar.GetDOW 
     this.getUrl   = Calendar.GetActionUrl    //�����������ת�¼�
     this.setSelctDate    = Calendar.setSelctDate
     this.setSelectClass = Calendar.setSelectClass;
     //
     this.disableDate = new Array() //�����õ���������([1,2,3])�����ڵ��ʱ�����ε�
     this.isCurrentAble = false;    //�Ƿ�ֻ����ǰ��Ч�����º����µĶ�����
     this.tempDate = '';
     this.tempYear = '';
     this.tempMonth = '';
     this.tempDay  = '';
}

Calendar.getInitDate = function(temp_date,workContents){
     //��ʼ�����뵱ǰ���ڿ��ܲ�ͬ
     this.tempDate  = temp_date ? temp_date : this.currentDateString;
     this.tempYear  = parseInt(this.tempDate.split("-")[0],10);
     this.tempMonth = parseInt(this.tempDate.split("-")[1],10);
     this.tempDay   = parseInt(this.tempDate.split("-")[2],10);
     //��������
     this.workContents=workContents;
     //============= the CSS of Date Page
     this.weightColor="#b9cbf7";
     this.lightColor ="#eaeffd";

     var strFrame='';//����������HTML����

     strFrame+='<table border=0 cellspacing=1 cellpadding=0 class="calendar_table" >';//#ff9900
     strFrame+='  <tr><td><table border=0 cellspacing=0 cellpadding=0 class="calendar_opera_table"><tr align=center >';
     strFrame+='      <td class="calendar_opera" onclick="'+this.Name+'.PreYear()" title="��ǰ��1 ��"><b>&lt;</b></td>';
     strFrame+='      <td class="calendar_opera" onclick="'+this.Name+'.NextYear()" title="���1 ��"><b>&gt;</b></td>';
     strFrame+='        <td ><span id=meizzYearHead></span></td>';
     strFrame+='           <td ><span id=meizzMonthHead></span></td>';
     strFrame+='      <td class="calendar_opera" onclick="'+this.Name+'.PreMonth()" title="��ǰ��1 ��"><b>&lt;</b></td>';
     strFrame+='      <td class="calendar_opera" onclick="'+this.Name+'.NextMonth()" title="���1 ��"><b>&gt;</b></td>';
     strFrame+='</tr></table></td></tr>';
     strFrame+='<tr><td>';
     strFrame+='        <table cellspacing=0 cellpadding=0 class="calendar_week" >';
     strFrame+='        <tr align=center>';
     strFrame+='        <td >��</td>';
     strFrame+='        <td >һ</td><td >��</td>';
     strFrame+='        <td >��</td><td >��</td>';
     strFrame+='        <td >��</td><td >��</td>';
     strFrame+='        </tr></table></td></tr>';
     strFrame+='<tr><td>';
     strFrame+='    <table border=0 cellspacing=0 cellpadding=0  class="calendar_content">';
                       var n=0; for (j=0;j<5;j++){ strFrame+= ' <tr align=center >'; for (i=0;i<7;i++){
     strFrame+='            <td width=100 height=100 id=meizzDay'+n+' valign=top></td>';n++;}
     strFrame+='        </tr>';}
     strFrame+='        <tr align=center >';
                       for (i=35;i<39;i++)strFrame+='<td width=100 height=100 id=meizzDay'+i+' valign=top></td>'
     strFrame+='        <td colspan=3 onclick="'+this.Name+'.ClickToday()"  style="cursor:pointer"> ����';
     strFrame+='   </td></tr></table></td></tr></table>';
     eval('document.all.'+this.tabName).innerHTML = strFrame
     this.Display(this.tempYear,this.tempMonth);    
     this.selectDate = this.tempDate ;
}

 

Calendar.meizzWriteHead = function(yy,mm)  //��head ��д�뵱ǰ��������
{
     this.doc.meizzYearHead.innerText  = yy + " ��";
     this.doc.meizzMonthHead.innerText = mm + " ��";
}

 

Calendar.IsPinYear = function(year)            //�ж��Ƿ���ƽ��
{
    if (0==year%4&&((year%100!=0)||(year%400==0))) return true;else return false;
}

Calendar.GetPinMonth = function(year,month)  //�������Ϊ��
{
    var c=this.MonHead[month-1];
     if((month==2)&&this.PinYear(year)) 
         c++;
     return c;
}

Calendar.GetDOW  = function(day,month,year)     //��ĳ������ڼ�
{
    var dt=new Date(year,month-1,day).getDay()/7; 
     return dt;
}

 

Calendar.meizzPrevY  = function()  //��ǰ��Year
{
    if(this.meizzTheYear > 999 && this.meizzTheYear <10000){this.meizzTheYear--;}
    else{alert("��ݳ�����Χ��-9999����");}
	this.meizzTheMonth= this.meizzTheMonth>10 ? this.meizzTheMonth : "0"+ this.meizzTheMonth;
	var today=this.meizzTheYear + '-' + this.meizzTheMonth + '-01';
	Calendar.ForwardActionUrl(today);
}

Calendar.meizzNextY = function()  //����Year
{
    if(this.meizzTheYear > 999 && this.meizzTheYear <10000){this.meizzTheYear++;}
    else{alert("��ݳ�����Χ��-9999����");}
	this.meizzTheMonth= this.meizzTheMonth>10 ? this.meizzTheMonth : "0"+ this.meizzTheMonth;
	var today=this.meizzTheYear + '-' + this.meizzTheMonth + '-01';
	Calendar.ForwardActionUrl(today);
}

//�������

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

Calendar.meizzPrevM = function()  //��ǰ���·�
{
    if(this.meizzTheMonth>1){this.meizzTheMonth--}else{this.meizzTheYear--;this.meizzTheMonth=12;}
	this.meizzTheMonth= this.meizzTheMonth>10 ? this.meizzTheMonth : "0"+ this.meizzTheMonth;
	var today=this.meizzTheYear + '-' + this.meizzTheMonth + '-01';
	Calendar.ForwardActionUrl(today);
}

Calendar.meizzNextM = function()  //�����·�
{
    if(this.meizzTheMonth==12){this.meizzTheYear++;this.meizzTheMonth=1}else{this.meizzTheMonth++}
	this.meizzTheMonth= this.meizzTheMonth>10 ? this.meizzTheMonth : "0"+ this.meizzTheMonth;
	var today=this.meizzTheYear + '-' + this.meizzTheMonth + '-01';
	Calendar.ForwardActionUrl(today);
}

Calendar.meizzSetDay = function(yy,mm){   //��Ҫ��д����=this.Display() **********

     this.lastItem = "";
     this.meizzTheYear=yy;
     this.meizzTheMonth=mm;
     this.getHead(yy,mm);
     //���õ�ǰ���µĹ�������Ϊ����ֵ
     for (var i = 0; i < 39; i++){this.meizzWDay[i]="";  this.work[i]='<br>';};  //����ʾ�������ȫ�����
     var day1 = 1,day2=1,firstday = new Date(yy,mm-1,1).getDay();  //ĳ�µ�һ������ڼ�
     for (i=0;i<firstday;i++)
     { 
       this.meizzWDay[i]=this.PinMon(mm==1?yy-1:yy,mm==1?12:mm-1)-firstday+i+1  //�ϸ��µ������
     }
	var temp = 0;
     for (i = firstday; day1 < this.PinMon(yy,mm)+1; i++)    //��ǰ��
       {
           this.meizzWDay[i]=day1;
          //���빤������
          for(j = temp; j< this.workContents.length; j++)
          {
			  this.workContents[j]=this.workContents[j].Trim();
              if(this.workContents[j].match(/^(\d{6})(\d{2})([\s\*])(.+)$/))  //����"���¹�������.." �ĸ�ʽ
              {  
                  var m0 = mm>10 ? mm : "0"+mm;
                  if(  (Number(RegExp.$1)==yy+''+m0) && (Number(RegExp.$2))== this.meizzWDay[i] )    //��������ƥ��
                  {
                       this.work[i] +='<a >'+RegExp.$4 + '</a><br/> '    //��һ��Ĺ��������Ǳ��ʽ������
						temp++;
                  }
             }
          }
          day1++;
       }

     for (i=firstday+this.PinMon(yy,mm);i<39;i++)          //�¸��µ�ǰ����
       {this.meizzWDay[i]=day2;day2++}

     //Ϊ����������ԣ���ʽ���¼�
     var tdClick = null;
     var tdDoubleClick=null;
     var tdHtml  = "";
     var tdTitle = "";
     var tdClass = "";
     var disableFlag = false;

     for (i = 0; i < 39; i++)
     { 
         var da = eval("document.all.meizzDay"+i)     //��д�µ�һ���µ�������������
         da.onclick = "";
         da.className = "";
         tdTitle   = "";
         tdHtml    = "";
         tdClass   = "calendar_text_able";
         if (this.meizzWDay[i]!="")
         {
             var nn1 = parseInt(this.meizzWDay[i],10);
                  nn1 = nn1 >= 10 ? nn1 : "0"+nn1;         //С��ǰ�油0
             var yy1 = yy;
             var mm1 = mm;
             var mms = mm1>=10 ? mm1 : "0"+mm1;

             disableFlag   = false;
             //�ϸ��µĲ���
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
             //�¸��µĲ���
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
              //���µĲ���
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
                                //��ʼ��ѡ�еļ��� 
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
                            //��td����onclick�¼��Ĵ���
                            da.onclick=tdClick;         
                            da.ondblclick=tdDoubleClick;     
                            tdHtml = "<a>" + tdHtml + "</a>"
                       }
                   }
              }
             tdHtml  =  this.meizzWDay[i]+ this.work[i] ;
              tdTitle = mm1 +"��" + this.meizzWDay[i] + "��";
         }    
         da.className       = tdClass;
         da.innerHTML       = tdHtml;
         da.title           = tdTitle;
     }
}



 

 

 

Calendar.meizzDayClick = function(n,ex,obj)  //�����ʾ��ѡȡ���ڣ������뺯��************* 
{
     this.setSelectClass(obj);
     var yy=this.meizzTheYear;
     var mm = parseInt(this.meizzTheMonth,10)+ex;   //ex��ʾƫ����������ѡ���ϸ��·ݺ��¸��·ݵ�����
     //�ж��·ݣ������ж�Ӧ�Ĵ���
     if(mm<1){
         yy--;
         mm=12+mm;
     }
     else if(mm>12){
         yy++;
         mm=mm-12;
     }
     n=n.substring(0,2);  //����
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

//����ӿں����������Զ����ڲ����� 
Calendar.GetActionUrl = function(year,month,day){

}

 

//˫����ת���ڶ���ҳ��.  //n �������ĵ�����죬ex -1 �����ϸ��£�0������ 1 �����¸��£�objָdateobj
Calendar.meizzDayDoubleClick=function (n,ex, obj)
{

}

Calendar.ForwardActionUrl = function(today)
{

}
