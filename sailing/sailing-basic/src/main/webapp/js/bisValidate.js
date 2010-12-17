/******************************************************/
/* 文件名：bisValidate.js                               */
/******************************************************/
/* 检测指定文本框输入是否合法 */
function validateInput(input)
{
	/* 非空校验 */
	var bisRequired = input.bisRequired==undefined?false:true;
	if (bisRequired && input.bisRequired=="true" && input.value==''){
		Validator.warnMessage('数据不完整:'+input.bisname+'不能为空');
		return false;
	}
	return true;
}
/* 检测指定FORM表单所有加入bisRequired属性的tag元素
（那些具有自定义属性的元素）是否合法，此函数用于表单的onsubmit事件 */
function validateform(myform)
{
	var i;
	for (i=0;i<myform.elements.length;i++)
	{
		 /* 非自定义属性的元素不予理睬 */
		if (myform.elements[i].bisname+""=="undefined") continue;
		/* 校验当前元素 */
		if (validateInput(myform.elements[i])==false)
		{
			myform.elements[i].focus();
			return false;
		}
	}
	return true;
}
/* 检测所有FORM表单所有应被检测的tag元素 */
function validateAllform()
{
	var j;
	Validator.clearValidateInfo();
	for (j=0;j<document.forms.length;j++)
	{
		/* 校验当前form元素 */
		if (validateform(document.forms[j])==false)
		{
			return false;
		}
	}
	return true;
}