/**
 * 定义常用的 form 操作函数
 * @author 王政
 * @version $Id: formUtils.js,v 1.1 2010/12/10 10:56:35 silencily Exp $
 */

if (FormUtils == null) {
	var FormUtils = {};
}

/**
 * 复选框函数， 点击后选择（或取消选择）所有 checkbox
 * @param checkboxAll 总控制复选框
 * @param checkbox 其他被控制复选框
 */
FormUtils.checkAll = function(checkboxAll, checkbox) {
	FormUtils.beforeCheckAll(checkboxAll, checkbox);
	
	if(!checkboxAll || !checkbox) {
		return;
	}

	FormUtils.setCheckBoxStyle(checkboxAll);
		
	if('checkbox' == checkbox.type && checkbox.disabled == false) {
		checkbox.checked = checkboxAll.checked;
		FormUtils.setCheckBoxStyle(checkbox);	
	} else {	
		for(var i = 0; i < checkbox.length; i++) {
			if(checkbox[i].disabled == false){
				checkbox[i].checked = checkboxAll.checked;
				FormUtils.setCheckBoxStyle(checkbox[i]);
			}
		}
	}
	
	FormUtils.afterCheckAll(checkboxAll, checkbox);
}

/**
 * FormUtils.checkAll 的 callback 函数, 发生在 checkAll 调用之前, 调用者可覆盖此方法实现业务功能
 * @param checkboxAll 总控制复选框
 * @param checkbox 其他被控制复选框 
 */
FormUtils.beforeCheckAll = function(checkboxAll, checkbox) {
}

/**
 * FormUtils.checkAll 的 callback 函数, 发生在 checkAll 调用之后, 调用者可覆盖此方法实现业务功能
 * @param checkboxAll 总控制复选框
 * @param checkbox 其他被控制复选框 
 */
FormUtils.afterCheckAll = function(checkboxAll, checkbox) {
}

FormUtils.setCheckBoxStyle = function(checkbox) {
	if(checkbox.checked) {
		checkbox.className = "focus";
	} else {
		checkbox.className = "";
	}	
}

/**
 * 复选框函数， 点击后选择（或取消选择）某一个复选框, 同时根据情况决定是否选中总控制复选框
 * @param checkboxAll 总控制复选框
 * @param checkbox 其他被控制复选框
 */
FormUtils.check = function(checkboxAll,checkbox) {
	FormUtils.beforeCheck(checkboxAll, checkbox);
	
	if(!checkboxAll || !checkbox) {
		return;
	}			
	var checked = true;
	if('checkbox' == checkbox.type) {
		checked = checkbox.checked;
		FormUtils.setCheckBoxStyle(checkbox);
	} else {
		for(var i = 0; i < checkbox.length; i++) {
			if(false == checkbox[i].checked) {
				checked = false;
			}
			FormUtils.setCheckBoxStyle(checkbox[i]);
		}
	}
	
	checkboxAll.checked = checked;
	FormUtils.setCheckBoxStyle(checkboxAll);
	
	FormUtils.afterCheck(checkboxAll, checkbox);
}

/**
 * FormUtils.check 的 callback 函数, 发生在 FormUtils.check 调用之前, 调用者可覆盖此方法实现业务功能
 * @param checkboxAll 总控制复选框
 * @param checkbox 其他被控制复选框 
 */
FormUtils.beforeCheck = function(checkboxAll, checkbox) {
}

/**
 * FormUtils.check 的 callback 函数, 发生在 FormUtils.check 调用之后, 调用者可覆盖此方法实现业务功能
 * @param checkboxAll 总控制复选框
 * @param checkbox 其他被控制复选框 
 */
FormUtils.afterCheck = function(checkboxAll, checkbox) {	
}

/**
 * 功能： 复选框函数， 得到复选框选中的个数
 * 输入： checkbox 复选框
 * 输出： 选中个数
 */
FormUtils.getCheckedNumber = function(checkbox) {
	if(!checkbox) {
		return 0;
	}	
	var count = 0;
	
	if('checkbox' == checkbox.type) {
		if(checkbox.checked) {
			count++;
		}
	} else {
		for(var i = 0; i < checkbox.length; i++) {
			if(checkbox[i].checked) {
				count++;
			}		
		}
	}
	return count;
}

/**
 * 功能： 单选框函数， 得到单选框选中的索引
 * 输入： radiobutton 单选框
 * 输出： 选中的索引
 */
FormUtils.getCheckedIndex = function(radiobutton) {
	if (! radiobutton) {
		return -1;
	}
	
	var checkedIndex = -1;
	if (radiobutton.length) {
		for(var i = 0; i < radiobutton.length; i++) {
			if(radiobutton[i].checked) {
				checkedIndex = i;	
				break;
			}
		}
	} else {
		if (radiobutton.checked) {
			checkedIndex = 0;
		}
	}
	
	return checkedIndex;
}


/**
 * 单选框函数， 得到单选框选中的值
 * @param radiobutton 单选框
 * @return 选中的值, 没有选中返回 null
 */
FormUtils.getCheckedValue = function(radiobutton) {
	var checkedIndex = FormUtils.getCheckedIndex(radiobutton);
	if (checkedIndex == -1) {
		return null;
	}
	if (radiobutton.length) {
		return radiobutton[checkedIndex].value;
	}
	return radiobutton.value;
}


/**
 * disabled 掉整个页面
 */
FormUtils.disableBody = function() {
	document.body.disabled = true;
	
	
	// 禁止文本域
	var textareas = document.getElementsByTagName("textarea");
	for (var i = 0; i < textareas.length; i++) {
		textareas[i].disabled = true;
	}
	
	// 禁止下拉框
	var combos = document.getElementsByTagName("select");
	for (var i = 0; i < combos.length; i++) {
		combos[i].disabled = true;
	}
	
	// 禁止输入框
	var inputs = document.getElementsByTagName("input");
	for (var i = 0; i < inputs.length; i++) {
		inputs[i].disabled = true;
		inputs[i].onclick = null;
	}
	
	// 禁止图片
	var images = document.getElementsByTagName("img");
	for (var i = 0; i < images.length; i++) {
		images[i].disabled = true;
		images[i].onclick = null;
	}
	
	// 禁止链接
	var hrefs = document.getElementsByTagName("a");
	for (var i = 0; i < hrefs.length; i++) {
		hrefs[i].disabled = true;
		hrefs[i].onclick = null;
	}
}

/**
 * 提交页面请求, 同时加入一些统一行为如显示进度条等
 * @param theForm theForm
 * @param url 请求 url
 * @param dontRequiredisableBody 是否禁止整个页面
 * @param target 请求的 target
 */
FormUtils.post = function(theForm, url, dontRequiredisableBody, target) {
	if (!target) {
		target = '_self';
	}
	theForm.target = target;
	theForm.method = 'POST';
	theForm.action = url;
	theForm.submit();
	
	if (dontRequiredisableBody == true) {
		return;	
	}
	
	//FormUtils.disableBody();
	
	// 展示进度条
	if(top.definedWin){
		top.definedWin.openLoading(0.7);
	}
}

FormUtils.post_up = function(theForm,u) {
	return this.post(theForm,u,false,'mainFrame');
}

/**
 * 重定向页面 url, 同时加入一些统一行为如显示进度条等
 * @param url 请求 url
 */
FormUtils.redirect = function(url) {
	location.href = url;
	
	// 展示进度条
	if(top.definedWin){
		top.definedWin.openLoading(0.7);
	}
}


/**
 * 禁止某 div 中的排除某些制定元素所有元素 
 * @div div 对象本身
 * @especialElementsIdArray  要排除的元素名称数组
 */
FormUtils.disableElementsInDiv = function(div, especialElementsIdArray) {
	var inputs = document.getElementsByTagName('input');
	var textareas = document.getElementsByTagName('textarea');
	var combos = document.getElementsByTagName('select');
	var hrefs = document.getElementsByTagName('a');
	var images = document.getElementsByTagName('img');
		
		
	for (var i = 0; i < inputs.length; i++) {
		FormUtils.disableElements(div, inputs[i], especialElementsIdArray);
	}
	for (var i = 0; i < textareas.length; i++) {
		FormUtils.disableElements(div, textareas[i], especialElementsIdArray);
	}
	for (var i = 0; i < combos.length; i++) {
		FormUtils.disableElements(div, combos[i], especialElementsIdArray);
	}
	for (var i = 0; i < hrefs.length; i++) {
		FormUtils.disableElements(div, hrefs[i], especialElementsIdArray);
	}
	for (var i = 0; i < images.length; i++) {
		FormUtils.disableElements(div, images[i], especialElementsIdArray);
	}
}

FormUtils.disableElements = function(div, element, especialElementsIdArray) {	
	if ((element.getAttribute('id') != null && element.getAttribute('id') != '' && especialElementsIdArray.indexOf(element.getAttribute('id')) != -1)
		|| (element.getAttribute('name') != null && element.getAttribute('name') != '' && especialElementsIdArray.indexOf(element.getAttribute('name')) != -1)) {
		return;
	}
	if (FormUtils.isInDiv(div, element)) {
		var type = element.getAttribute('type');			
		if (type == 'text' || type == 'textarea') {
			element.readOnly = true;
			// 取消 ec 下拉框的选择按钮
			if (element.id == "input_text") {
				element.id = "";
			}
		} else if (type == 'button' || type == 'image') {
			element.style.display = 'none';
		} else {
			element.disabled = true;
		}
		if (type == 'text' || type == 'select-one' || type == 'select-multiple') {
			if (element.className.indexOf('readonly') < 0) {
				element.className = 'readonly ' + element.className ;
			}		
		}
		
		if (element.onclick) {
			element.onclick = null;
		}
	}
}


FormUtils.isInDiv = function(div, object) {
	var parentNode = null;
	try {
		parentNode = object.parentNode;
	} catch (e) {
	}
	
	if (parentNode == null) {
		return false;
	}
	
	if (parentNode == div) {
		return true;
	}
	if (parentNode == document.body) {
		return false;
	}
	return FormUtils.isInDiv(div, parentNode);
}

/**
 * 某表单元素是否可写
 * @param inputElement 表单元素对象
 * @return 是否可写
 */
FormUtils.isWriteable = function (inputElement) {
	if (!inputElement) {
		return false;
	}
	if (inputElement.disabled) {
		return false;
	}
	if (inputElement.readOnly) {
		return false;
	}
	
	var writeableTypes = new Array();
	writeableTypes.push('text');
	writeableTypes.push('hidden');
	writeableTypes.push('textarea');
	writeableTypes.push('select-one');
	writeableTypes.push('select-multiple');
	
	//FIXME 需要验证 type 是否是这几个
	writeableTypes.push('radio');
	writeableTypes.push('checkbox');
	
	for (var i = 0; i < writeableTypes.length; i++) {
		try {
			var typeName = inputElement.getAttribute('type');
			if (typeName == writeableTypes[i]) {
				return true;
			}
		} catch (ignore) {
		}
	}
	
	return false;
}

/**
 * 删除某个表单元素对象及所有子对象
 * @param object the object
 */
FormUtils.removeAllChildren = function(object) {
	while (object.hasChildNodes()) {
		object.removeChild(object.lastChild);
	}
	Global.setHeight();
}

/**
 * 清空参数中的每一个 input 的值或 span 和 div 的 innerText
 */
FormUtils.cleanValues = function() {
	for (var i = 0; i < arguments.length; i++) {
		var typeName = arguments[i].tagName;
		if (typeName == 'INPUT' || typeName == 'TEXTAREA') {
			arguments[i].value = '';
		} else if (typeName == 'SPAN' || typeName == 'DIV') {
			arguments[i].innerText = '';
		}		
	}
}

FormUtils.doAction = function(url,tar){
	document.forms[0].method = "post";
	document.forms[0].target = tar ? tar : "_self";
	document.forms[0].action = url;
	document.forms[0].submit();
}

