/**
 * 定义常用的 date 操作函数
 * @author 王政
 * @version $Id: dateUtils.js,v 1.1 2010/12/10 10:56:36 silencily Exp $
 */
 
if (DateUtils == null) {
	var DateUtils = {};
} 
 
DateUtils.getTimeZoneDifference = function(timeZone) {
	var returnObject = new Object();
	
	var hours = parseInt(timeZone / ( 60 * 60 * 1000 ), 10);
	timeZone -= hours * 60 * 60 * 1000 ;
	var minutes = parseInt(timeZone / (60 * 1000), 10);
	timeZone -= minutes * 60 * 1000 ;
	var seconds = parseInt(timeZone / 1000, 10);

	returnObject['difference'] = timeZone;
	returnObject['hours'] = Math.abs(hours);
	returnObject['minutes'] = Math.abs(minutes);
	returnObject['seconds'] = Math.abs(seconds);

	return returnObject;
}

/** 
 * 功能：　比较日期函数
 * 输入：　strDate1 日期1, strDate2 日起2
 * 输出：　得到两个日期相差的天数,正数表示strDate2 > strDate1
 */
DateUtils.CompareDate = function(strDate1, strDate2){

	var yearOfDate1 = strDate1.substring(0, 4);
	var monthOfDate1 = strDate1.substring(5, 7);
	var dayOfDate1 = strDate1.substring(8);
	
	var yearOfDate2 = strDate2.substring(0, 4);
	var monthOfDate2 = strDate2.substring(5, 7);
	var dayOfDate2 = strDate2.substring(8);
	
	date1 =new Date(new Number(yearOfDate1),new Number(monthOfDate1)-1,new Number(dayOfDate1));
	date2 =new Date(new Number(yearOfDate2),new Number(monthOfDate2)-1,new Number(dayOfDate2));
	
	var ret = date2.getTime()-date1.getTime();
	ret = ret /(1000 * 3600 * 24);
	ret = parseInt(ret,10);
	return ret;
}

/* 
 * 显示指定格式选择框
 * 实现依赖 ExtendCombo.setMenu
 *
 */

DateUtils.selectDateObject = function(name){
	this.name = name;
	_this = this;
	name = name.replace(/\./gi,"_");
	this.txtYearObj  = "ec_"+name+"_year";
	this.txtMonthObj = "ec_"+name+"_month";	
	this.txtDayObj   = "ec_"+name+"_day";
	this.txtHourObj  = "ec_"+name+"_hour";
	this.txtMinuteObj= "ec_"+name+"_minute";
	
	//构造 HH:mm 格式
	this.displayHourAndMinute = function(name,defValue,readonly){
		var str = "";
		var arr;
		defValue = defValue ? defValue : document.getElementById(name).value;
		if(defValue.indexOf(":")>=0)
			arr = defValue.split(":");
		else
			arr = new Array(defValue,"");
		this.txtHourObj  = "ec_"+name+"_hour";
		this.txtMinuteObj= "ec_"+name+"_minute";
		var flag = name.replace(/\./gi,"_");
		flag = flag.replace(/\(/gi,"_");
		flag = flag.replace(/\)/gi,"_");
		var monthName = this.name+"."+flag+"HourArray";
		var dayName   = this.name+"."+flag+"MinuteArray";
		eval( monthName + "= "+this.name+".showOptions(0,23)");
		eval( dayName + "  = "+this.name+".showOptions(0,59)");
		readonly = readonly ? " readonly " : ""
		str += "<input type='text' style='width:12px' "+readonly+" setName = '"+name+"' setType='hour_minute' setNum=0 name='"+this.txtHourObj+"' value='"+arr[0]+"' id='input_text'/>"+
			   "<input type='button' id='input_select' onclick='ExtendCombo.setMenu(this.previousSibling,"+monthName+",0,this,\"\",12,\"\",\"\","+this.name+".onSelect)'/>时";
		str += "<input type='text' style='width:12px' "+readonly+" setName = '"+name+"' setType='hour_minute' setNum=1 name='"+this.txtMinuteObj+"' value='"+arr[1]+"' id='input_text'/>"+
			   "<input type='button' id='input_select' onclick='ExtendCombo.setMenu(this.previousSibling,"+dayName+",0,this,\"\",12,\"\",\"\","+this.name+".onSelect)'/>分"; 
		return str;
	}

	//构造 YYYY 选择框
	//TODO 年份可以自动扩展：自动向前（后）推进30年
	this.displayYear = function(name,defValue,beginYear,endYear,readonly,isNum){
		this.displayNumber(name,defValue,beginYear,endYear,readonly,isNum,'年');
	}
	this.displayNumber = function(name,defValue,beginYear,endYear,readonly,isNum,labal){
		var str = "";
		defValue = defValue.substring(0,4);
		var flag = name.replace(/\./gi,"_");
		flag = flag.replace(/\(/gi,"_");
		flag = flag.replace(/\)/gi,"_");
		this.txtYearObj  = "ec_"+name+"_number";
		var yearName  = this.name+"."+flag+"YearArray";
		beginYear = typeof beginYear != 'undefined' ? beginYear : 2000;
		endYear   = typeof endYear != 'undefined'  ? endYear   : 2030;
		isNum = typeof isNum != 'undefined' ?  isNum : false;
		var len = (endYear+"").length * 8;
		len = len < 16 ? 16 : len;
		eval( yearName +" = " + this.name+".showOptions("+beginYear+","+endYear+","+isNum+")");
		readonly = readonly ? " readonly " : ""
		labal = labal ? labal : "";
		str += "<input type='text' id='input_text' "+readonly+" style='width:"+len+"px' setType='year' setName = '"+name+"' setNum=0 name='"+this.txtYearObj+"' value='"+defValue+"'/>"+
			   "<input type='button' id='input_select' onclick='ExtendCombo.setMenu(this.previousSibling,"+yearName+",0,this,\"\",12,\"\",\"\","+this.name+".onSelect)'/>"+labal;
		return str;
	}

	//name 输入框名，defValue 默认值 MM-DD
	this.displayMonthAndDay = function(name,defValue,readonly){
		var str = "";
		var arr;
		if(defValue.indexOf("-")>=0)
			arr = defValue.split("-");
		else
			arr = new Array(defValue,"");
		var flag = name.replace(/\./gi,"_");
		flag = flag.replace(/\(/gi,"_");
		flag = flag.replace(/\)/gi,"_");
		var monthName = this.name+"."+flag+"MonthArray";
		var dayName   = this.name+"."+flag+"DayArray";
		eval( monthName + "= "+this.name+".showOptions(1,12)");
		eval( dayName + "  = "+this.name+".showOptions(1,31)");
		readonly = readonly ? " readonly " : ""
		str += "<input type='text' style='width:12px' "+readonly+" setName = '"+name+"' setType='month_day' setNum=0 name='"+this.txtMonthObj+"' value='"+arr[0]+"' id='input_text'/>"+
			   "<input type='button' id='input_select' onclick='ExtendCombo.setMenu(this.previousSibling,"+monthName+",0,this,\"\",12)'/>月";
		str += "<input type='text' style='width:12px' "+readonly+" setName = '"+name+"' setType='month_day' setNum=1 name='"+this.txtDayObj+"' value='"+arr[1]+"' id='input_text'/>"+
			   "<input type='button' id='input_select' onclick='ExtendCombo.setMenu(this.previousSibling,"+dayName+",0,this,\"\",12)'/>日"; 
		return str;
	}

	//构造 YYYY-MM 格式
	//readonly 是否将填项设置为readonly
	this.displayYearAndMonth = function(name,defValue,beginYear,endYear,readonly){
		var str = "";
		var arr;
		if(defValue.indexOf("-")>=0)
			arr = defValue.split("-");
		else
			arr = new Array(defValue,"");
		var flag = name.replace(/\./gi,"_");
		flag = flag.replace(/\(/gi,"_");
		flag = flag.replace(/\)/gi,"_");
		var monthName = this.name+"."+flag+"MonthArray";
		var yearName  = this.name+"."+flag+"YearArray";
		beginYear = beginYear ? beginYear : 2000;
		endYear   = endYear   ? endYear   : 2030
		eval( yearName +" = " + this.name+".showOptions("+beginYear+","+endYear+")");
		eval( monthName+" =  "+ this.name+".showOptions(1,12)");
		readonly = readonly ? " readonly " : ""
		str += "<input type='text' id='input_text' "+readonly+" style='width:30px' setType='year_month' setName = '"+name+"' setNum=0 name='"+this.txtYearObj+"' value='"+arr[0]+"'/>"+
			   "<input type='button' id='input_select' onclick='ExtendCombo.setMenu(this.previousSibling,"+yearName+",0,this,\"\",12,\"\",\"\","+this.name+".onSelect)'/>年";
		str += "<input type='text' id='input_text' "+readonly+" style='width:15px' setType='year_month' setName = '"+name+"' setNum=1 name='"+this.txtMonthObj+"' value='"+arr[1]+"'/>"+
			   "<input type='button' id='input_select' onclick='ExtendCombo.setMenu(this.previousSibling,"+monthName+",0,this,\"\",12,\"\",\"\","+this.name+".onSelect)'/>月"; 
		return str;
	}
	//这个方法是在点击选项时候激活的
	this.onSelect = function(str){
		var obj = ExtendCombo.txtObj;
		var setName = obj.setName;
		var setNum  = obj.setNum;
		obj.value = str;
		_this.selectOption(obj,setName,setNum);
	}

	this.showOptions = function(begin,end,isNum){
		var arr = new Array();
		var inum = "00";
		for(var i=begin;i<=end;i++){
			if(i<10 && !isNum)
				inum = "0"+i;
			else
				inum = i;
			arr[arr.length] = inum;
		}
		return arr;
	}
	this.selectOption = function(obj,name,flag){
		var spaceStr = "-";
		var hidd  = document.getElementsByName(name)[0];
		var str = "";
		var value = hidd.value;
		if(obj.setType == "hour_minute")
			spaceStr = ":";
		//默认从1900年开始
		if(value.indexOf(spaceStr)== -1){
			switch(obj.setType){
				case "month_day":
					value = document.getElementsByName(this.txtMonthObj)[0].value+"-"+document.getElementsByName(this.txtDayObj)[0].value;
					break;
				case "year_month":
					value = document.getElementsByName(this.txtYearObj)[0].value+"-"+document.getElementsByName(this.txtMonthObj)[0].value;
					break;
				case "year":
					str = obj.value;
					break;
				case "hour_minute":
					value = document.getElementsByName(this.txtHourObj)[0].value+"-"+document.getElementsByName(this.txtMinuteObj)[0].value;
					spaceStr = ":";
					break;
			}				
		}
		if(value.indexOf(spaceStr) > 0){
			var arr = value.split(spaceStr);
			arr[flag] = obj.value;
			str = arr[0]+spaceStr+arr[1];
		}
		hidd.value = str;
		this.afterSelectOption(obj,hidd);
	}

	this.afterSelectOption = function(obj,hidd){}

	//得到指定日期的前一天 
	//str -> YYYY-MM-DD
	this.getPreDay = function(str){
		var arr = str.split("-");
		var theDate = new Date(arr[0],parseInt(arr[1],10)-1,arr[2]);
		var temp = theDate.getTime();
		temp = temp - 24*3600*1000;
		var newDate = DateUtils.GetDBDate();
		newDate.setTime(temp);
		temp = this.SetDateFormat(newDate,'<yyyy>-<mm>-<dd>');
		return temp;
	}

	//得到指定日期的后一天
	this.getNextDay = function(str){
		var arr = str.split("-");
		var theDate = new Date(arr[0],parseInt(arr[1],10)-1,arr[2]);
		var temp = theDate.getTime();
		temp = temp + 24*3600*1000;
		var newDate = DateUtils.GetDBDate();
		newDate.setTime(temp);
		temp = this.SetDateFormat(newDate,'<yyyy>-<mm>-<dd>');
		return temp;
	}

	/** 将 Date 日期转为指定格式 <yyyy>-<mm>-<dd> */
	this.SetDateFormat = function(oldDate,formatStr){
		var theYear  = oldDate.getFullYear();
		var theMonth = oldDate.getMonth();
		var theDay   = oldDate.getDate();
		var theDate  = formatStr;
		var tmpYear = theYear;
		var tmpMonth = theMonth;
		if (tmpMonth < 0){
		   tmpMonth = 0;
		}
		if (tmpMonth > 11){
		   tmpMonth = 11;
		}
		var tmpDay = theDay;

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
}

DateUtils.dateObj = new DateUtils.selectDateObject("DateUtils.dateObj");

/** 
 * 功能：　通过同步的JAVASCRIPT获取DB的时间
 * 输出：　得到一个DATE类型的时间
 */
DateUtils.GetDBDate = function(){
	var url = ContextInfo.contextPath + "/GetDBTimeServlet";
	var allContent = XMLHttpEngine.getResponseText(url, false);
	//取不到时间才从客户端取时间
	if(typeof allContent == 'undefined'){
		return new Date();
	}
	var ss;
    // 在每个|字符处进行分解。
    ss = allContent.split("|");
    //注意MONTH在取值上和JAVA有所区别，应该减一
	var newDateObj = new Date(ss[0], ss[1]-1, ss[2], ss[3], ss[4], ss[5]);
	return newDateObj;
}










