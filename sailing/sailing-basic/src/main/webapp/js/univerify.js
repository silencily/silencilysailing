/******************************************************/
/* 文件名：univerify.js                               */
/* 功  能：基于自定义属性的统一检测用javascript函数库 */
/* 作  者：纵横软件制作中心雨亦奇(zhsoft88@sohu.com)  */
/******************************************************/
/* 取得字符串的字节长度 */
function strbytelen(str)
{
	var i;
	var len;
	len = 0;
	for (i=0;i<str.length;i++)
	{
		if (str.charCodeAt(i)>255) len+=2; else len++;
	}
	return len;
}
/* 检测字符串是否为空 */
function isnull(str)
{
	var i;
	 for (i=0;i < str.length;i++)
	{
		if (str.charAt(i)!=' ') return false;
	}
	return true;
}
/* 检测字符串是否全为数字 */
function isnumber(str)
{
	var number_chars = "1234567890";
	var i;
	for (i=0;i<str.length;i++)
	{
		if (number_chars.indexOf(str.charAt(i))==-1) return false;
	}
	return true;
}
/* 检测指定文本框输入是否合法 */
function verifyInput(input)
{
	//alert("in vf "+ input.bisname);
	//var image;
	//var i;
	//var error = false;
	/* 长度校验 */
	var bismaxlength = input.maxLength==undefined?input.maxlength:input.maxLength;
	var bisnotnull = input.notnull==undefined?false:true;
	//alert("bismaxlength:"+bismaxlength);
	//alert(input.maxlength+"/"+input.maxLength);
	//流内才进行必须入力验证
	/* 工作流部分可编辑必须入力验证 */
	var biswfRequried = input.wfRequried==undefined?false:true;
	try{
		var formInTheWorkFlow = document.getElementById("formInTheWorkFlow");
		if(formInTheWorkFlow && formInTheWorkFlow.value == "true"){
			/* 工作流部分可编辑必须入力验证 */
			if (biswfRequried && input.wfRequried=="true"){
				if (trim(input.value)=='')//input.nullable=="no"&&
				{
					//alert("HelloWorld!INworkFlow!Check!");
					//alert(input.bisname+"不能为空");
					Validator.warnMessage("数据不完整:"+input.bisname+"不能为空");
					return false;
					//error = true;
				}
			}
		}
	}catch(e){}

	if (bismaxlength != undefined){
		if (strbytelen(input.value)>parseInt(bismaxlength))
		{
			//alert(input.bisname+"超出最大长度"+bismaxlength);
			Validator.warnMessage('数据越界:'+input.bisname+'不能超出最大长度'+bismaxlength);
			return false;
			//error = true;
		}
	}
	/* 非空校验 */
	if (bisnotnull && input.notnull=="true"){
		if (trim(input.value)=='')//input.nullable=="no"&&
		{
			//alert(input.bisname+"不能为空");
			Validator.warnMessage('数据不完整:'+input.bisname+'不能为空');
			return false;
			//error = true;
		}
	}
	return true;
	/*else
	{*/
		/* 数据类型校验 */
		/*switch(input.datatype)
		{
			case "number": if (isnumber(input.value)==false)
		{
			alert(input.bisname+"值应该全为数字");
			error = true;
		}
		break;*/
		/* 在这里可以添加多个自定义数据类型的校验判断 */
		/*  case datatype1: ... ; break;        */
		/*  case datatype2: ... ; break;        */
		/*  ....................................*/
		/*default  : break;
		}
	} */
	/* 根据有无错误设置或取消警示标志 
	if (error)
	{
		image = document.getElementById("img_"+input.name);
		image.src="img/warning.gif";
		return false;
	}
	else
	{
		image = document.getElementById("img_"+input.name);
		image.src="img/space.gif";
		return true;
	}*/
}
/* 检测指定FORM表单所有应被检测的tag元素
（那些具有自定义属性的元素）是否合法，此函数用于表单的onsubmit事件 */
function verifyform(myform)
{
	var i;
	//alert("len:"+myform.elements.length);
	for (i=0;i<myform.elements.length;i++)
	{
		 /* 非自定义属性的元素不予理睬 */
		if (myform.elements[i].bisname+""=="undefined") continue;
		/* 对可编辑列表置删除位元素跳过 */
		if(isDelFlg(myform.elements[i])) {continue;}
		
		/* 校验当前元素 */
		if (verifyInput(myform.elements[i])==false)
		{
			myform.elements[i].focus();
			return false;
		}
	}
	return true;
}
/* 检测所有FORM表单所有应被检测的tag元素 */
function verifyAllform()
{
	//alert("in verifyAllform");
	var j;
	Validator.clearValidateInfo();
	for (j=0;j<document.forms.length;j++)
	{
		/* 校验当前form元素 */
		if (verifyform(document.forms[j])==false)
		{
			return false;
		}
	}
	return true;
}
function trim(input) {
	if (input == null) return null;
	return input.replace(/^\s+|\s+$/g, "");
}

/* 通过当前tagNode判断是否在可编辑列表行
 * 返回可编辑行是否为删除状态
 */
function isDelFlg(tagNode){
	var trtag = tagNode.parentNode.parentNode;
	var notcheck=false;
	if(trtag.tagName == "TR"){
		var trtagElements = trtag.getElementsByTagName("*");
		for(k=0;k<trtagElements.length;k++){
			if(trtagElements[k].tagName == "INPUT" && trtagElements[k].type == "hidden" 
				&& trtagElements[k].id.search(".delFlg") != -1 && trtagElements[k].value == "1"){
				notcheck=true;
				break;
			}
		}
	}
	return notcheck;
}
