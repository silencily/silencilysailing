/**
 * ���峣�õ� form ��������
 * @author ����
 * @version $Id: formUtils.js,v 1.1 2010/12/10 10:56:35 silencily Exp $
 */

if (FormUtils == null) {
	var FormUtils = {};
}

/**
 * ��ѡ������ �����ѡ�񣨻�ȡ��ѡ������ checkbox
 * @param checkboxAll �ܿ��Ƹ�ѡ��
 * @param checkbox ���������Ƹ�ѡ��
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
 * FormUtils.checkAll �� callback ����, ������ checkAll ����֮ǰ, �����߿ɸ��Ǵ˷���ʵ��ҵ����
 * @param checkboxAll �ܿ��Ƹ�ѡ��
 * @param checkbox ���������Ƹ�ѡ�� 
 */
FormUtils.beforeCheckAll = function(checkboxAll, checkbox) {
}

/**
 * FormUtils.checkAll �� callback ����, ������ checkAll ����֮��, �����߿ɸ��Ǵ˷���ʵ��ҵ����
 * @param checkboxAll �ܿ��Ƹ�ѡ��
 * @param checkbox ���������Ƹ�ѡ�� 
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
 * ��ѡ������ �����ѡ�񣨻�ȡ��ѡ��ĳһ����ѡ��, ͬʱ������������Ƿ�ѡ���ܿ��Ƹ�ѡ��
 * @param checkboxAll �ܿ��Ƹ�ѡ��
 * @param checkbox ���������Ƹ�ѡ��
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
 * FormUtils.check �� callback ����, ������ FormUtils.check ����֮ǰ, �����߿ɸ��Ǵ˷���ʵ��ҵ����
 * @param checkboxAll �ܿ��Ƹ�ѡ��
 * @param checkbox ���������Ƹ�ѡ�� 
 */
FormUtils.beforeCheck = function(checkboxAll, checkbox) {
}

/**
 * FormUtils.check �� callback ����, ������ FormUtils.check ����֮��, �����߿ɸ��Ǵ˷���ʵ��ҵ����
 * @param checkboxAll �ܿ��Ƹ�ѡ��
 * @param checkbox ���������Ƹ�ѡ�� 
 */
FormUtils.afterCheck = function(checkboxAll, checkbox) {	
}

/**
 * ���ܣ� ��ѡ������ �õ���ѡ��ѡ�еĸ���
 * ���룺 checkbox ��ѡ��
 * ����� ѡ�и���
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
 * ���ܣ� ��ѡ������ �õ���ѡ��ѡ�е�����
 * ���룺 radiobutton ��ѡ��
 * ����� ѡ�е�����
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
 * ��ѡ������ �õ���ѡ��ѡ�е�ֵ
 * @param radiobutton ��ѡ��
 * @return ѡ�е�ֵ, û��ѡ�з��� null
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
 * disabled ������ҳ��
 */
FormUtils.disableBody = function() {
	document.body.disabled = true;
	
	
	// ��ֹ�ı���
	var textareas = document.getElementsByTagName("textarea");
	for (var i = 0; i < textareas.length; i++) {
		textareas[i].disabled = true;
	}
	
	// ��ֹ������
	var combos = document.getElementsByTagName("select");
	for (var i = 0; i < combos.length; i++) {
		combos[i].disabled = true;
	}
	
	// ��ֹ�����
	var inputs = document.getElementsByTagName("input");
	for (var i = 0; i < inputs.length; i++) {
		inputs[i].disabled = true;
		inputs[i].onclick = null;
	}
	
	// ��ֹͼƬ
	var images = document.getElementsByTagName("img");
	for (var i = 0; i < images.length; i++) {
		images[i].disabled = true;
		images[i].onclick = null;
	}
	
	// ��ֹ����
	var hrefs = document.getElementsByTagName("a");
	for (var i = 0; i < hrefs.length; i++) {
		hrefs[i].disabled = true;
		hrefs[i].onclick = null;
	}
}

/**
 * �ύҳ������, ͬʱ����һЩͳһ��Ϊ����ʾ��������
 * @param theForm theForm
 * @param url ���� url
 * @param dontRequiredisableBody �Ƿ��ֹ����ҳ��
 * @param target ����� target
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
	
	// չʾ������
	if(top.definedWin){
		top.definedWin.openLoading(0.7);
	}
}

FormUtils.post_up = function(theForm,u) {
	return this.post(theForm,u,false,'mainFrame');
}

/**
 * �ض���ҳ�� url, ͬʱ����һЩͳһ��Ϊ����ʾ��������
 * @param url ���� url
 */
FormUtils.redirect = function(url) {
	location.href = url;
	
	// չʾ������
	if(top.definedWin){
		top.definedWin.openLoading(0.7);
	}
}


/**
 * ��ֹĳ div �е��ų�ĳЩ�ƶ�Ԫ������Ԫ�� 
 * @div div ������
 * @especialElementsIdArray  Ҫ�ų���Ԫ����������
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
			// ȡ�� ec �������ѡ��ť
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
 * ĳ��Ԫ���Ƿ��д
 * @param inputElement ��Ԫ�ض���
 * @return �Ƿ��д
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
	
	//FIXME ��Ҫ��֤ type �Ƿ����⼸��
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
 * ɾ��ĳ����Ԫ�ض��������Ӷ���
 * @param object the object
 */
FormUtils.removeAllChildren = function(object) {
	while (object.hasChildNodes()) {
		object.removeChild(object.lastChild);
	}
	Global.setHeight();
}

/**
 * ��ղ����е�ÿһ�� input ��ֵ�� span �� div �� innerText
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

