/* 公用的前端验证方法
 * @since 2006-03-15
 * @version $Id: validator.js,v 1.1 2010/12/10 10:56:35 silencily Exp $
 
 * 对一个域加入多个验证条件，dataType中用"|"分隔多个属性值，msg中也是对应出现 (2006-07-16 liuz)
 * 修改历史：
 * 对成功消息回调函数增加消息参数 yushn 2007-12-20
 */

Validator = {
	Require : /\S+/,
	Email : /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/,
	Phone : /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/,
	Mobile : /^((\(\d{2,3}\))|(\d{3}\-))?13\d{9}$/,
	Url : /^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/,
	IdCard : "this.IsIdCard(value)",
	Currency : /^\d*(\.\d*)?$/,
	Number : /^\d*$/,
	Zip : /^[1-9]\d{5}$/,
	QQ : /^[1-9]\d{4,8}$/,
	Integer : /^[-\+]?\d*$/,
	Double : /^[-\+]?\d*(\.\d*)?$/,
	PositiveInteger : /^\d*$/,
	PositiveDouble : /^\d*(\.\d*)?$/,
	English : /^[A-Za-z]+$/,
	EnglishNumber:/^[A-Za-z0-9]*$/,
	Chinese :  /^[\u0391-\uFFE5]+$/,
	Username : /^[a-z]\w{3,}$/i,
	UnSafe : /^(([A-Z]*|[a-z]*|\d*|[-_\~!@#\$%\^&\*\.\(\)\[\]\{\}<>\?\\\/\'\"]*)|.{0,5})$|\s/,
	IsSafe : function(str){return !this.UnSafe.test(str);},
	SafeString : "this.IsSafe(value)",
	Filter : "this.DoFilter(value, getAttribute('accept'))",
	Limit : "this.limit(value.length,getAttribute('min'),  getAttribute('max'))",
	LimitB : "this.limit(this.LenB(value), getAttribute('min'), getAttribute('max'))",
	Date : "this.IsDate(value, getAttribute('min'), getAttribute('format'))",
	Repeat : "value == document.getElementsByName(getAttribute('to'))[0].value",
	Range : "getAttribute('min') < (value|0) && (value|0) < getAttribute('max')",
	Compare : "this.compare(value,getAttribute('operator'),getAttribute('to'))",
	Custom : "this.Exec(value, getAttribute('regexp'))",
	Group : "this.MustChecked(getAttribute('name'), getAttribute('min'), getAttribute('max'))",
	ErrorItem : [document.forms[0]],
	ErrorMessage : ["以下原因导致提交失败：\t\t\t\t"],
	Validate : function(theForm, mode){
		//先清空以前验证信息
		Validator.clearValidateInfo();
		var obj = theForm || event.srcElement;
		var count = obj.elements.length;
		this.initError(obj);
		this.beforeAddError();
		for(var i=0;i<count;i++){
			with(obj.elements[i]){
				if(disabled == true || style.display == "none" || null == getAttribute("dataType"))
					continue;
				var _dataType = getAttribute("dataType").split("|");
				var msgArr    = getAttribute("msg").split("|");
				for(var j=0;j<_dataType.length;j++){
					if(typeof(_dataType[j]) == "object" || typeof(this[_dataType[j]]) == "undefined")  continue;
					this.ClearState(obj.elements[i]);
					if(getAttribute("Require") == "false" && value == "") continue;
					switch(_dataType[j]){
						case "IdCard" :
						case "Date" :
						case "Repeat" :
						case "Range" :
						case "Compare" :
						case "Custom" :
						case "Group" : 
						case "Limit" :
						case "LimitB" :
						case "SafeString" :
						case "Filter" :
							if(!eval(this[_dataType[j]]))	{
								this.AddError(i, msgArr[j], obj);
							}
							break;
						default :
							if(!this[_dataType[j]].test(value)){
								this.AddError(i, msgArr[j], obj);
							}
							break;
					}
				}//end for
			}
		}//edn for
		if(this.ErrorMessage.length > 1){
			mode = mode || 4;//默认是第4种模式
			var errCount = this.ErrorItem.length;
			switch(mode){
				case 4 ://扩展
					try {
						this.ErrorItem[1].focus();
					} catch (e) {
					}
					for(var i=1;i<errCount;i++) {
						Validator.warnMessage(this.ErrorMessage[i].replace(/\d+:/,""));		
					}			
					break;
				case 5 ://这里是每次只显示一条错误提示
					try {
						this.ErrorItem[1].focus();
					} catch (e) {
					}					
					Validator.warnMessage(this.ErrorMessage[1].replace(/\d+:/,""));
					break;
				case 2 :
					for(var i=1;i<errCount;i++) {
						this.ErrorItem[i].style.color = "red";
					}						
				case 1 :
					alert(this.ErrorMessage.join("\n"));
					try {
						this.ErrorItem[1].focus();
					} catch (e) {
					}
					
					break;
				case 3 :
					for(var i=1;i<errCount;i++){
					try{
						var span = document.createElement("SPAN");
						span.id = "__ErrorMessagePanel";
						span.style.color = "red";
						this.ErrorItem[i].parentNode.appendChild(span);
						span.innerHTML = this.ErrorMessage[i].replace(/\d+:/,"*");
						}
						catch(e){alert(e.description);}
					}
					try {
						this.ErrorItem[1].focus();
					} catch (e) {
					}					
					break;
				default :
					alert(this.ErrorMessage.join("\n"));
					break;
			}
			return false;
		}
		return true;
	},
	initError : function(obj){
		this.ErrorMessage.length= 1;
		this.ErrorItem.length   = 1;
		this.ErrorItem[0] = obj;
	},
	limit : function(len,min, max){
		min = min || 0;
		max = max || Number.MAX_VALUE;
		return min <= len && len <= max;
	},
	LenB : function(str){
		return str.replace(/[^\x00-\xff]/g,"**").length;
	},
	ClearState : function(elem){
		with(elem){
			if(style.color == "red")
				style.color = "";
			var lastNode = parentNode.childNodes[parentNode.childNodes.length-1];
			if(lastNode.id == "__ErrorMessagePanel")
				parentNode.removeChild(lastNode);
		}
	},
	
	beforeAddError:function(){},
	/**
	 * 手动增加一条错误信息的方法
	 * @param index 索引号, 从 0 开始
	 * @param str 错误信息
	 * @obj 验证对象, 一般是 form 对象
	 */
	AddError : function(index, str, obj){
		this.ErrorItem[this.ErrorItem.length] = this.ErrorItem[0].elements[index];
		this.ErrorMessage[this.ErrorMessage.length] = this.ErrorMessage.length + ":" + str;
	},
	
	
	Exec : function(op, reg){
		return new RegExp(reg,"g").test(op);
	},
	compare : function(op1,operator,op2){
		switch (operator) {
			case "NotEqual":
				return (op1 != op2);
			case "GreaterThan":
				return (op1 > op2);
			case "GreaterThanEqual":
				return (op1 >= op2);
			case "LessThan":
				return (op1 < op2);
			case "LessThanEqual":
				return (op1 <= op2);
			default:
				return (op1 == op2);            
		}
	},
	MustChecked : function(name, min, max){
		var groups = document.getElementsByName(name);
		var hasChecked = 0;
		min = min || 1;
		max = max || groups.length;
		for(var i=groups.length-1;i>=0;i--)
			if(groups[i].checked) hasChecked++;
		return min <= hasChecked && hasChecked <= max;
	},
	DoFilter : function(input, filter){
return new RegExp("^.+\.(?=EXT)(EXT)$".replace(/EXT/g, filter.split(/\s*,\s*/).join("|")), "gi").test(input);
	},
	IsIdCard : function(number){
		var date, Ai;
		var verify = "10x98765432";
		var Wi = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
		var area = ['','','','','','','','','','','','北京','天津','河北','山西','内蒙古','','','','','','辽宁','吉林','黑龙江','','','','','','','','上海','江苏','浙江','安微','福建','江西','山东','','','','河南','湖北','湖南','广东','广西','海南','','','','重庆','四川','贵州','云南','西藏','','','','','','','陕西','甘肃','青海','宁夏','新疆','','','','','','台湾','','','','','','','','','','香港','澳门','','','','','','','','','国外'];
		var re = number.match(/^(\d{2})\d{4}(((\d{2})(\d{2})(\d{2})(\d{3}))|((\d{4})(\d{2})(\d{2})(\d{3}[x\d])))$/i);
		if(re == null) return false;
		if(re[1] >= area.length || area[re[1]] == "") return false;
		if(re[2].length == 12){
			Ai = number.substr(0, 17);
			date = [re[9], re[10], re[11]].join("-");
		}
		else{
			Ai = number.substr(0, 6) + "19" + number.substr(6);
			date = ["19" + re[4], re[5], re[6]].join("-");
		}
		if(!this.IsDate(date, "ymd")) return false;
		var sum = 0;
		for(var i = 0;i<=16;i++){
			sum += Ai.charAt(i) * Wi[i];
		}
		Ai +=  verify.charAt(sum%11);
		return (number.length ==15 || number.length == 18 && number == Ai);
	},
	IsDate : function(op, formatString){
		formatString = formatString || "ymd";
		var m, year, month, day;
		switch(formatString){
			case "ymd" :
				m = op.match(new RegExp("^((\\d{4})|(\\d{2}))([-./])(\\d{1,2})\\4(\\d{1,2})$"));
				if(m == null ) return false;
				day = m[6];
				month = m[5]*1;
				year =  (m[2].length == 4) ? m[2] : GetFullYear(parseInt(m[3], 10));
				break;
			case "dmy" :
				m = op.match(new RegExp("^(\\d{1,2})([-./])(\\d{1,2})\\2((\\d{4})|(\\d{2}))$"));
				if(m == null ) return false;
				day = m[1];
				month = m[3]*1;
				year = (m[5].length == 4) ? m[5] : GetFullYear(parseInt(m[6], 10));
				break;
			//add by lzx start 08-6-17
			case "HH:mm" :
				var rr = /^([0-1][0-9]|2[0-3]):[0-5]\d$/;
				if(!rr.test(op)) return false;
				return true;
			//add by lzx end 08-6-17			
			//add by tongjq start
			case "yyyy-MM-dd" :
				m = op.match(new RegExp("^((\\d{4}))([-])(\\d{2})\\3(\\d{2})$"));
				if(m == null ) return false;
				day = m[5];
				month = m[4]*1;
				year =  m[1];
				break;
			case "yyyy-MM" :
				var rr = new RegExp("^(\\d{4})([-])([0][1-9]|[1][0-2])$");
				if(!rr.test(op)) return false;
				return true;
			//add by tongjq end			
			default :
				break;
		}
		if(!parseInt(month)) return false;
		month = month==0 ?12:month;
		var date = new Date(year, month-1, day);
        return (typeof(date) == "object" && year == date.getFullYear() && month == (date.getMonth()+1) && day == date.getDate());
		function GetFullYear(y){return ((y<30 ? "20" : "19") + y)|0;}
	},
	trim : function (input) {
		if (input == null) return null;
		return input.replace(/^\s+|\s+$/g, "");
	},
	isNumber : function(input){
		if (input == null) return false;
		
		input = Validator.trim(input);
	
		var regexp = /^\d*$/;
		return regexp.test(input);
	}
 }


/**
 * 是否已清除信息
 */
Validator.isClearMessage = false;

// 默认用于显示信息的 div 名称, 可以在页面上覆盖此属性
Validator.messageDivName = 'validateInfo';

/**
 * 验证前的回调方法, 页面可以覆盖此方法实现一些业务
 */
Validator.beforeValidate = function() {
}

/**
 * 验证后的回调方法, 页面可以覆盖此方法实现一些业务
 */
Validator.afterValidate = function() {
}

/**
 * 提示正确信息前的回调方法, 页面可以覆盖此方法实现一些业务
 */
Validator.beforeSuccessMessage = function() {
}

/**
 * 提示正确信息后的回调方法, 页面可以覆盖此方法实现一些业务
 * 增加消息参数 yushn 2007-12-20
 */
Validator.afterSuccessMessage = function(info) {
}

/** 将 alert 信息转化为 页面顶部报错提示 扩展
 *  info 显示信息
 *  TODO 改成黄颜色
 */
Validator.warnMessage = function (info) {
	Validator.beforeValidate();
	
	var obj = new Object();
	obj.infoCss = "update_warn";
	obj.msgCss  = "update_errorInfo";
	obj.title   = "您的操作出现错误，错误原因是：";
	obj.msg     = info;
	Validator.setMessage(obj);
	
	Validator.afterValidate();		
}

/**
 * 增加一条错误信息
 */
Validator.errorMessage = function (info) {
	var obj = new Object();
	obj.infoCss = "update_warn";
	obj.msgCss  = "update_errorInfo";
	obj.title   = "您的操作出现错误，错误原因是：";
	obj.msg     = info;
	Validator.setMessage(obj);
}


//成功提示
Validator.successMessage = function (info) {
	Validator.beforeSuccessMessage();
	
	var obj = new Object();
	obj.infoCss = "update_right";
	obj.msgCss  = "update_rightInfo";
	obj.title   = "您的操作结果是：";
	obj.msg     = info;
	Validator.setMessage(obj);
	
	Validator.afterSuccessMessage(info);//增加消息参数 yushn 2007-12-20
}

//统一的消息提示格式
Validator.setMessage = function (infoObj) {
	Validator.addMessageDivIfNecessary();
	
	infoObj.infoId = "divId_displayError";
	
	var info1  = "";
	info1 += '<div class="div_message"><div class="'+infoObj.infoCss+'">'+infoObj.title+'</div>';
    info1 += '<div class="'+infoObj.msgCss+'" id="'+infoObj.infoId+'"> &#8226;&nbsp;' + infoObj.msg + '</div></div>';
	if(document.getElementById(infoObj.infoId)) { 
		var parent = document.getElementById(infoObj.infoId).parentNode;		
		var obj = document.createElement("div");
		obj.className = infoObj.msgCss
		obj.innerHTML = "&#8226;&nbsp;" + infoObj.msg;
		parent.appendChild(obj);
	}else if(document.getElementById(Validator.messageDivName))   { 
		document.getElementById(Validator.messageDivName).innerHTML = info1;
		document.getElementById(Validator.messageDivName).style.display = '';
	}
	Global.setHeight();
		
	//以下一句是为了让页面从顶部开始显示
	//location = '#';
}

/**
 * 如果页面上没有用于显示信息的 div, 自动在页面顶部创建一个
 */
Validator.addMessageDivIfNecessary = function() {
	if (!document.getElementById(Validator.messageDivName)) {
		var messageDiv = document.createElement("<div id = \"" + Validator.messageDivName + "\"></div>");
		document.body.insertBefore(messageDiv, document.body.firstChild);
	}
}

Validator.clearValidateInfo = function () {
	//alert(document.getElementById(Validator.messageDivName));
	if(document.getElementById(Validator.messageDivName)) {
		document.getElementById(Validator.messageDivName).innerHTML = "";
	}
/**
 * checkRepeat(arr)		
 * 功能
 * 	 验证列表中的数据是否重复
 * 参数			
 * 	 ??arr：要验证的列表 (Array)
 * 返回值
 * 	 第一个重复的数据 或 null（不重复）
 **/
Validator.checkRepeat=function (title,tail,tableRowNum) {
	if (tableRowNum < 2) return null;
	var arr = new Array();
	for(var i=0;i<tableRowNum;i++){
		if (!document.getElementById(title + "[" + i + "].delFlg")) {
			continue;
		}
		var flag = document.getElementById(title + "[" + i + "].delFlg").value;
		if(flag != "1"){
			var temp = document.getElementById(title + "[" + i + "]." + tail).value;
			arr[arr.length]=temp;
		} else {
			arr[arr.length]=null;
		}
	}
	for (var i=0; i<arr.length-1; i++) {
		if (arr[i] == null) {
			continue;
		}
		for (var j=i+1; j<arr.length; j++) {
			if (arr[j] == null) {
				continue;
			}
			if (Validator.trim(arr[i]) == Validator.trim(arr[j])){
				return (i+1) + "," + (j+1);
			}
		}
	}
	return null;
}

/**
 * personCheck(arr)		
 * 功能
 * 	 验证列表中的数据是否重复
 * 参数			
 * 	 ??arr：要验证的列表 (Array)
 * 	 ??delFlagName：删除标志控件名称
 * 返回值
 * 	 第一个重复的数据 或 null（不重复）
 **/
Validator.personCheck=function (title,tail,tableRowNum,delFlagName) {
	if (tableRowNum < 2) return null;
	var arr = new Array();
	for(var i=0;i<tableRowNum;i++){
		if (!document.getElementById(delFlagName + "[" + i + "].delFlg")) {
			continue;
		} else {
			var flag = document.getElementById(delFlagName + "[" + i + "].delFlg").value;
			if(flag != "1"){
				var temp = document.getElementById(title + "[" + i + "]." + tail).value;
				arr[arr.length]=temp;
			} else {
				arr[arr.length]=null;
			}
		}
	}
	for (var i=0; i<arr.length-1; i++) {
		if (arr[i] == null) {
			continue;
		}
		for (var j=i+1; j<arr.length; j++) {
			if (arr[j] == null) {
				continue;
			}
			if (Validator.trim(arr[i]) == Validator.trim(arr[j])){
				return (i+1) + "," + (j+1);
			}
		}
	}
	return null;
}

/**
 * isNumber(input)		
 * 功能
 * 	 验证数字
 * 参数			
 * 	 ??input：要验证的字符串
 * 返回值
 * 	 true  通过验证
 * 	 false  未通过验证
 **/
Validator.isNumber=function (input) {
	
	if (input == null) return false;
	
	input = Validator.trim(input);

	var regexp = /^\d*$/;
	return regexp.test(input);
}

/**
 * checkNumberWithCamma(input)		
 * 功能
 * 	 验证数字和半角逗号构成的字符串
 * 	 (例如"2,56,2"能够通过验证, ",2,56,,2,"不能通过)
 * 参数			
 * 	 ??input：要验证的字符串
 * 返回值
 * 	 true  通过验证
 * 	 false  未通过验证
 **/
Validator.checkNumberWithCamma=function (input) {
	if (input == null) return false;
	input = Validator.trim(input);

	var regexp = /^\d*$|^[0-9][0-9\,]*[0-9]$/;
	if (!regexp.test(input)) return false;

	if (input.match(/\,\,+/))
		return false;
	else
		return true;
}

/**
 * trim(input)		
 * 功能
 * 	 去掉字符串的前置空格和后置空格
 * 参数			
 * 	 ??input：要处理的字符串
 * 返回值
 * 	 处理后的字符串
 **/
Validator.trim=function (input) {
	if (input == null) return null;
	return input.replace(/^\s+|\s+$/g, "");
}
}

        /**
         * 判断一个字符串是否是整数(十进制)。<BR>
         * 判断规则：^((\+|-)?)((0)|([1-9][0-9]*))$：<BR>
         *         可以正号(+)或负号(-)开头，也可以没有正负号；<BR>
         *         除正负号以外，只能包含半角数字字符；<BR>
         *         可以是0，但0之后不能跟随其他数字(也包括0)；<BR>
         * @param strInteger String类型 被判断字符串
         * @return   boolean类型：<BR>
         *           true:   满足判断规则时<BR>
         *           false:  不满足判断规则时<BR>
         */
        Validator.validateInteger = function (strInteger){
                if ( strInteger == null ) {
                        return false;
                }
                if ( strInteger == "" ) {
                        return false;
                }

                var pattern = /^((\+|-)?)((0)|([1-9][0-9]*))$/; 
                return pattern.test(strInteger);
        }

        /**
         * 判断一个字符串是否是数字(十进制)。<BR>
         * 判断规则：^((\+|-)?)((0)|([1-9][0-9]*))((\.[0-9]+)?)$：<BR>
         *         可以正号(+)或负号(-)开头，也可以没有正负号；<BR>
         *         除正负号以外，只能包含半角数字字符和最多一个小数点(半角)；<BR>
         *         整数部分：<BR>
         *            可以是0，但0之后不能跟随其他数字(也包括0)；<BR>
         *         小数部分：<BR>
         *            可以没有小数部分；<BR>
         *            小数点最多只能有1位，并且必须位于其他数字字符之间；<BR>
         * @param strNumber String类型 被判断字符串
         * @return   boolean类型：<BR>
         *           true:   满足判断规则时<BR>
         *           false:  不满足判断规则时<BR>        
         */
        Validator.validateNumber = function (strNumber){
               
                if ( strNumber == null ) {
                        return false;
                }
                if ( strNumber == "" ) {
                        return false;
                }
                var strNumberPattern = /^((\+|-)?)((0)|([1-9][0-9]*))((\.[0-9]+)?)$/; 
                
                return strNumberPattern .test(strNumber);
        }

        /**
         * 判断一个字符串是否是数字(十进制)、整数部分与小数部分的位数是否在指定范围之内。<BR>
         * 判断规则：^((\+|-)?)((0)|([1-9][0-9]*))((\.[0-9]+)?)$：<BR>
         *         可以正号(+)或负号(-)开头，也可以没有正负号；<BR>
         *         除正负号以外，只能包含半角数字字符和最多一个小数点(半角)；<BR>
         *         正负号不作为位数判断的内容；<BR>
         *         整数部分：<BR>
         *            可以是0，但0之后不能跟随其他数字(也包括0)；<BR>
         *         小数部分：<BR>
         *            可以没有小数部分；<BR>
         *            小数点最多只能有1位，并且必须位于其他数字字符之间；<BR>
         * @param strNumber            String类型 被判断字符串
         * @param maxIntDigits         int类型    整数部分的最大位数限制，正整数
         * @param maxFractionDigits    int类型    小数部分的最大位数限制，正整数或0；
         * @return   boolean类型：<BR>
         *           true:   满足判断规则时<BR>
         *           false:  不满足判断规则时<BR>        
         */
        Validator.validateNumberAndDigits = function (strNumber, maxIntDigits, maxFractionDigits){
                
                //如果不是数字，则返回false
                if(! Validator.validateNumber(strNumber)){
                        return false;
                }
                
                var indexOfPoint = 0;
                var lenOfIntPart = 0;
                var lenOfFractionPart = 0;
                var tmpStrNumber = "";
                
                // 如果有正负号，则去掉
                if (( strNumber.indexOf("+") == 0 ) || ( strNumber.indexOf("-") == 0 )) {
                        tmpStrNumber = strNumber.substring(1);
                } else {
                        tmpStrNumber = strNumber;
                }
                
                // 计算整数部分和小数部分的位数
                indexOfPoint = tmpStrNumber.indexOf(".");
                if ( indexOfPoint < 0 ) {
                        //不含有小数的时候
                        lenOfIntPart = tmpStrNumber.length;
                        lenOfFractionPart =0;
                } else {
                        //含有小数的时候
                        lenOfIntPart = indexOfPoint ;
                        lenOfFractionPart =tmpStrNumber.length - indexOfPoint - 1;
                }
                
                //判断
                if ( (maxIntDigits >=  lenOfIntPart) && (maxFractionDigits >= lenOfFractionPart)) {
                        return true;
                } else {
                        return false;
                }
        }

        /**
         * 判断一个字符串是否是非负数字(十进制)、整数部分与小数部分的位数是否在指定范围之内。<BR>
         * 判断规则：^((+|-)?)((0)|([1-9][0-9]*))((\\.[0-9]+)?)$：<BR>
         *         可以正号(+)开头，也可以没有正号；<BR>
         *         除正号以外，只能包含半角数字字符和最多一个小数点(半角)；<BR>
         *         正号不作为位数判断的内容；<BR>
         *         整数部分：<BR>
         *            可以是0，但0之后不能跟随其他数字(也包括0)；<BR>
         *         小数部分：<BR>
         *            可以没有小数部分；<BR>
         *            小数点最多只能有1位，并且必须位于其他数字字符之间；<BR>
         * @param strNumber            String类型 被判断字符串
         * @param maxIntDigits         int类型    整数部分的最大位数限制，正整数
         * @param maxFractionDigits    int类型    小数部分的最大位数限制，正整数或0；
         * @return   boolean类型：<BR>
         *           true:   满足判断规则时<BR>
         *           false:  不满足判断规则时<BR>        
         */
        Validator.validateNonNegativeNumberAndDigits = function (strNumber, maxIntDigits, maxFractionDigits){
                
                //如果不是数字，则返回false
                if(! Validator.validateNumber(strNumber)){
                        return false;
                }
                
                // 如果有负号，则返回false
                if ( strNumber.indexOf("-") == 0 ) {
                        return false;
                }

                var indexOfPoint = 0;
                var lenOfIntPart = 0;
                var lenOfFractionPart = 0;
                var tmpStrNumber = "";
                

                // 如果有正号，则去掉
                if ( strNumber.indexOf("+") == 0 ) {
                        tmpStrNumber = strNumber.substring(1);
                } else {
                        tmpStrNumber = strNumber;
                }
                
                // 计算整数部分和小数部分的位数
                indexOfPoint = tmpStrNumber.indexOf(".");
                if ( indexOfPoint < 0 ) {
                        //不含有小数的时候
                        lenOfIntPart = tmpStrNumber.length;
                        lenOfFractionPart =0;
                } else {
                        //含有小数的时候
                        lenOfIntPart = indexOfPoint ;
                        lenOfFractionPart =tmpStrNumber.length - indexOfPoint - 1;
                }
                
                //判断
                if ( (maxIntDigits >=  lenOfIntPart) && (maxFractionDigits >= lenOfFractionPart)) {
                        return true;
                } else {
                        return false;
                }
        }
