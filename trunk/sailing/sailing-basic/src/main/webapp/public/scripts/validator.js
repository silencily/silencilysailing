/* ���õ�ǰ����֤����
 * @since 2006-03-15
 * @version $Id: validator.js,v 1.1 2010/12/10 10:56:35 silencily Exp $
 
 * ��һ�����������֤������dataType����"|"�ָ��������ֵ��msg��Ҳ�Ƕ�Ӧ���� (2006-07-16 liuz)
 * �޸���ʷ��
 * �Գɹ���Ϣ�ص�����������Ϣ���� yushn 2007-12-20
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
	ErrorMessage : ["����ԭ�����ύʧ�ܣ�\t\t\t\t"],
	Validate : function(theForm, mode){
		//�������ǰ��֤��Ϣ
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
			mode = mode || 4;//Ĭ���ǵ�4��ģʽ
			var errCount = this.ErrorItem.length;
			switch(mode){
				case 4 ://��չ
					try {
						this.ErrorItem[1].focus();
					} catch (e) {
					}
					for(var i=1;i<errCount;i++) {
						Validator.warnMessage(this.ErrorMessage[i].replace(/\d+:/,""));		
					}			
					break;
				case 5 ://������ÿ��ֻ��ʾһ��������ʾ
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
	 * �ֶ�����һ��������Ϣ�ķ���
	 * @param index ������, �� 0 ��ʼ
	 * @param str ������Ϣ
	 * @obj ��֤����, һ���� form ����
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
		var area = ['','','','','','','','','','','','����','���','�ӱ�','ɽ��','���ɹ�','','','','','','����','����','������','','','','','','','','�Ϻ�','����','�㽭','��΢','����','����','ɽ��','','','','����','����','����','�㶫','����','����','','','','����','�Ĵ�','����','����','����','','','','','','','����','����','�ຣ','����','�½�','','','','','','̨��','','','','','','','','','','���','����','','','','','','','','','����'];
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
 * �Ƿ��������Ϣ
 */
Validator.isClearMessage = false;

// Ĭ��������ʾ��Ϣ�� div ����, ������ҳ���ϸ��Ǵ�����
Validator.messageDivName = 'validateInfo';

/**
 * ��֤ǰ�Ļص�����, ҳ����Ը��Ǵ˷���ʵ��һЩҵ��
 */
Validator.beforeValidate = function() {
}

/**
 * ��֤��Ļص�����, ҳ����Ը��Ǵ˷���ʵ��һЩҵ��
 */
Validator.afterValidate = function() {
}

/**
 * ��ʾ��ȷ��Ϣǰ�Ļص�����, ҳ����Ը��Ǵ˷���ʵ��һЩҵ��
 */
Validator.beforeSuccessMessage = function() {
}

/**
 * ��ʾ��ȷ��Ϣ��Ļص�����, ҳ����Ը��Ǵ˷���ʵ��һЩҵ��
 * ������Ϣ���� yushn 2007-12-20
 */
Validator.afterSuccessMessage = function(info) {
}

/** �� alert ��Ϣת��Ϊ ҳ�涥��������ʾ ��չ
 *  info ��ʾ��Ϣ
 *  TODO �ĳɻ���ɫ
 */
Validator.warnMessage = function (info) {
	Validator.beforeValidate();
	
	var obj = new Object();
	obj.infoCss = "update_warn";
	obj.msgCss  = "update_errorInfo";
	obj.title   = "���Ĳ������ִ��󣬴���ԭ���ǣ�";
	obj.msg     = info;
	Validator.setMessage(obj);
	
	Validator.afterValidate();		
}

/**
 * ����һ��������Ϣ
 */
Validator.errorMessage = function (info) {
	var obj = new Object();
	obj.infoCss = "update_warn";
	obj.msgCss  = "update_errorInfo";
	obj.title   = "���Ĳ������ִ��󣬴���ԭ���ǣ�";
	obj.msg     = info;
	Validator.setMessage(obj);
}


//�ɹ���ʾ
Validator.successMessage = function (info) {
	Validator.beforeSuccessMessage();
	
	var obj = new Object();
	obj.infoCss = "update_right";
	obj.msgCss  = "update_rightInfo";
	obj.title   = "���Ĳ�������ǣ�";
	obj.msg     = info;
	Validator.setMessage(obj);
	
	Validator.afterSuccessMessage(info);//������Ϣ���� yushn 2007-12-20
}

//ͳһ����Ϣ��ʾ��ʽ
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
		
	//����һ����Ϊ����ҳ��Ӷ�����ʼ��ʾ
	//location = '#';
}

/**
 * ���ҳ����û��������ʾ��Ϣ�� div, �Զ���ҳ�涥������һ��
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
 * ����
 * 	 ��֤�б��е������Ƿ��ظ�
 * ����			
 * 	 ??arr��Ҫ��֤���б� (Array)
 * ����ֵ
 * 	 ��һ���ظ������� �� null�����ظ���
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
 * ����
 * 	 ��֤�б��е������Ƿ��ظ�
 * ����			
 * 	 ??arr��Ҫ��֤���б� (Array)
 * 	 ??delFlagName��ɾ����־�ؼ�����
 * ����ֵ
 * 	 ��һ���ظ������� �� null�����ظ���
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
 * ����
 * 	 ��֤����
 * ����			
 * 	 ??input��Ҫ��֤���ַ���
 * ����ֵ
 * 	 true  ͨ����֤
 * 	 false  δͨ����֤
 **/
Validator.isNumber=function (input) {
	
	if (input == null) return false;
	
	input = Validator.trim(input);

	var regexp = /^\d*$/;
	return regexp.test(input);
}

/**
 * checkNumberWithCamma(input)		
 * ����
 * 	 ��֤���ֺͰ�Ƕ��Ź��ɵ��ַ���
 * 	 (����"2,56,2"�ܹ�ͨ����֤, ",2,56,,2,"����ͨ��)
 * ����			
 * 	 ??input��Ҫ��֤���ַ���
 * ����ֵ
 * 	 true  ͨ����֤
 * 	 false  δͨ����֤
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
 * ����
 * 	 ȥ���ַ�����ǰ�ÿո�ͺ��ÿո�
 * ����			
 * 	 ??input��Ҫ������ַ���
 * ����ֵ
 * 	 �������ַ���
 **/
Validator.trim=function (input) {
	if (input == null) return null;
	return input.replace(/^\s+|\s+$/g, "");
}
}

        /**
         * �ж�һ���ַ����Ƿ�������(ʮ����)��<BR>
         * �жϹ���^((\+|-)?)((0)|([1-9][0-9]*))$��<BR>
         *         ��������(+)�򸺺�(-)��ͷ��Ҳ����û�������ţ�<BR>
         *         �����������⣬ֻ�ܰ�����������ַ���<BR>
         *         ������0����0֮���ܸ�����������(Ҳ����0)��<BR>
         * @param strInteger String���� ���ж��ַ���
         * @return   boolean���ͣ�<BR>
         *           true:   �����жϹ���ʱ<BR>
         *           false:  �������жϹ���ʱ<BR>
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
         * �ж�һ���ַ����Ƿ�������(ʮ����)��<BR>
         * �жϹ���^((\+|-)?)((0)|([1-9][0-9]*))((\.[0-9]+)?)$��<BR>
         *         ��������(+)�򸺺�(-)��ͷ��Ҳ����û�������ţ�<BR>
         *         �����������⣬ֻ�ܰ�����������ַ������һ��С����(���)��<BR>
         *         �������֣�<BR>
         *            ������0����0֮���ܸ�����������(Ҳ����0)��<BR>
         *         С�����֣�<BR>
         *            ����û��С�����֣�<BR>
         *            С�������ֻ����1λ�����ұ���λ�����������ַ�֮�䣻<BR>
         * @param strNumber String���� ���ж��ַ���
         * @return   boolean���ͣ�<BR>
         *           true:   �����жϹ���ʱ<BR>
         *           false:  �������жϹ���ʱ<BR>        
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
         * �ж�һ���ַ����Ƿ�������(ʮ����)������������С�����ֵ�λ���Ƿ���ָ����Χ֮�ڡ�<BR>
         * �жϹ���^((\+|-)?)((0)|([1-9][0-9]*))((\.[0-9]+)?)$��<BR>
         *         ��������(+)�򸺺�(-)��ͷ��Ҳ����û�������ţ�<BR>
         *         �����������⣬ֻ�ܰ�����������ַ������һ��С����(���)��<BR>
         *         �����Ų���Ϊλ���жϵ����ݣ�<BR>
         *         �������֣�<BR>
         *            ������0����0֮���ܸ�����������(Ҳ����0)��<BR>
         *         С�����֣�<BR>
         *            ����û��С�����֣�<BR>
         *            С�������ֻ����1λ�����ұ���λ�����������ַ�֮�䣻<BR>
         * @param strNumber            String���� ���ж��ַ���
         * @param maxIntDigits         int����    �������ֵ����λ�����ƣ�������
         * @param maxFractionDigits    int����    С�����ֵ����λ�����ƣ���������0��
         * @return   boolean���ͣ�<BR>
         *           true:   �����жϹ���ʱ<BR>
         *           false:  �������жϹ���ʱ<BR>        
         */
        Validator.validateNumberAndDigits = function (strNumber, maxIntDigits, maxFractionDigits){
                
                //����������֣��򷵻�false
                if(! Validator.validateNumber(strNumber)){
                        return false;
                }
                
                var indexOfPoint = 0;
                var lenOfIntPart = 0;
                var lenOfFractionPart = 0;
                var tmpStrNumber = "";
                
                // ����������ţ���ȥ��
                if (( strNumber.indexOf("+") == 0 ) || ( strNumber.indexOf("-") == 0 )) {
                        tmpStrNumber = strNumber.substring(1);
                } else {
                        tmpStrNumber = strNumber;
                }
                
                // �����������ֺ�С�����ֵ�λ��
                indexOfPoint = tmpStrNumber.indexOf(".");
                if ( indexOfPoint < 0 ) {
                        //������С����ʱ��
                        lenOfIntPart = tmpStrNumber.length;
                        lenOfFractionPart =0;
                } else {
                        //����С����ʱ��
                        lenOfIntPart = indexOfPoint ;
                        lenOfFractionPart =tmpStrNumber.length - indexOfPoint - 1;
                }
                
                //�ж�
                if ( (maxIntDigits >=  lenOfIntPart) && (maxFractionDigits >= lenOfFractionPart)) {
                        return true;
                } else {
                        return false;
                }
        }

        /**
         * �ж�һ���ַ����Ƿ��ǷǸ�����(ʮ����)������������С�����ֵ�λ���Ƿ���ָ����Χ֮�ڡ�<BR>
         * �жϹ���^((+|-)?)((0)|([1-9][0-9]*))((\\.[0-9]+)?)$��<BR>
         *         ��������(+)��ͷ��Ҳ����û�����ţ�<BR>
         *         ���������⣬ֻ�ܰ�����������ַ������һ��С����(���)��<BR>
         *         ���Ų���Ϊλ���жϵ����ݣ�<BR>
         *         �������֣�<BR>
         *            ������0����0֮���ܸ�����������(Ҳ����0)��<BR>
         *         С�����֣�<BR>
         *            ����û��С�����֣�<BR>
         *            С�������ֻ����1λ�����ұ���λ�����������ַ�֮�䣻<BR>
         * @param strNumber            String���� ���ж��ַ���
         * @param maxIntDigits         int����    �������ֵ����λ�����ƣ�������
         * @param maxFractionDigits    int����    С�����ֵ����λ�����ƣ���������0��
         * @return   boolean���ͣ�<BR>
         *           true:   �����жϹ���ʱ<BR>
         *           false:  �������жϹ���ʱ<BR>        
         */
        Validator.validateNonNegativeNumberAndDigits = function (strNumber, maxIntDigits, maxFractionDigits){
                
                //����������֣��򷵻�false
                if(! Validator.validateNumber(strNumber)){
                        return false;
                }
                
                // ����и��ţ��򷵻�false
                if ( strNumber.indexOf("-") == 0 ) {
                        return false;
                }

                var indexOfPoint = 0;
                var lenOfIntPart = 0;
                var lenOfFractionPart = 0;
                var tmpStrNumber = "";
                

                // ��������ţ���ȥ��
                if ( strNumber.indexOf("+") == 0 ) {
                        tmpStrNumber = strNumber.substring(1);
                } else {
                        tmpStrNumber = strNumber;
                }
                
                // �����������ֺ�С�����ֵ�λ��
                indexOfPoint = tmpStrNumber.indexOf(".");
                if ( indexOfPoint < 0 ) {
                        //������С����ʱ��
                        lenOfIntPart = tmpStrNumber.length;
                        lenOfFractionPart =0;
                } else {
                        //����С����ʱ��
                        lenOfIntPart = indexOfPoint ;
                        lenOfFractionPart =tmpStrNumber.length - indexOfPoint - 1;
                }
                
                //�ж�
                if ( (maxIntDigits >=  lenOfIntPart) && (maxFractionDigits >= lenOfFractionPart)) {
                        return true;
                } else {
                        return false;
                }
        }
