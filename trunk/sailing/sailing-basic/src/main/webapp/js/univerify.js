/******************************************************/
/* �ļ�����univerify.js                               */
/* ��  �ܣ������Զ������Ե�ͳһ�����javascript������ */
/* ��  �ߣ��ݺ������������������(zhsoft88@sohu.com)  */
/******************************************************/
/* ȡ���ַ������ֽڳ��� */
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
/* ����ַ����Ƿ�Ϊ�� */
function isnull(str)
{
	var i;
	 for (i=0;i < str.length;i++)
	{
		if (str.charAt(i)!=' ') return false;
	}
	return true;
}
/* ����ַ����Ƿ�ȫΪ���� */
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
/* ���ָ���ı��������Ƿ�Ϸ� */
function verifyInput(input)
{
	//alert("in vf "+ input.bisname);
	//var image;
	//var i;
	//var error = false;
	/* ����У�� */
	var bismaxlength = input.maxLength==undefined?input.maxlength:input.maxLength;
	var bisnotnull = input.notnull==undefined?false:true;
	//alert("bismaxlength:"+bismaxlength);
	//alert(input.maxlength+"/"+input.maxLength);
	//���ڲŽ��б���������֤
	/* ���������ֿɱ༭����������֤ */
	var biswfRequried = input.wfRequried==undefined?false:true;
	try{
		var formInTheWorkFlow = document.getElementById("formInTheWorkFlow");
		if(formInTheWorkFlow && formInTheWorkFlow.value == "true"){
			/* ���������ֿɱ༭����������֤ */
			if (biswfRequried && input.wfRequried=="true"){
				if (trim(input.value)=='')//input.nullable=="no"&&
				{
					//alert("HelloWorld!INworkFlow!Check!");
					//alert(input.bisname+"����Ϊ��");
					Validator.warnMessage("���ݲ�����:"+input.bisname+"����Ϊ��");
					return false;
					//error = true;
				}
			}
		}
	}catch(e){}

	if (bismaxlength != undefined){
		if (strbytelen(input.value)>parseInt(bismaxlength))
		{
			//alert(input.bisname+"������󳤶�"+bismaxlength);
			Validator.warnMessage('����Խ��:'+input.bisname+'���ܳ�����󳤶�'+bismaxlength);
			return false;
			//error = true;
		}
	}
	/* �ǿ�У�� */
	if (bisnotnull && input.notnull=="true"){
		if (trim(input.value)=='')//input.nullable=="no"&&
		{
			//alert(input.bisname+"����Ϊ��");
			Validator.warnMessage('���ݲ�����:'+input.bisname+'����Ϊ��');
			return false;
			//error = true;
		}
	}
	return true;
	/*else
	{*/
		/* ��������У�� */
		/*switch(input.datatype)
		{
			case "number": if (isnumber(input.value)==false)
		{
			alert(input.bisname+"ֵӦ��ȫΪ����");
			error = true;
		}
		break;*/
		/* �����������Ӷ���Զ����������͵�У���ж� */
		/*  case datatype1: ... ; break;        */
		/*  case datatype2: ... ; break;        */
		/*  ....................................*/
		/*default  : break;
		}
	} */
	/* �������޴������û�ȡ����ʾ��־ 
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
/* ���ָ��FORM������Ӧ������tagԪ��
����Щ�����Զ������Ե�Ԫ�أ��Ƿ�Ϸ����˺������ڱ���onsubmit�¼� */
function verifyform(myform)
{
	var i;
	//alert("len:"+myform.elements.length);
	for (i=0;i<myform.elements.length;i++)
	{
		 /* ���Զ������Ե�Ԫ�ز������ */
		if (myform.elements[i].bisname+""=="undefined") continue;
		/* �Կɱ༭�б���ɾ��λԪ������ */
		if(isDelFlg(myform.elements[i])) {continue;}
		
		/* У�鵱ǰԪ�� */
		if (verifyInput(myform.elements[i])==false)
		{
			myform.elements[i].focus();
			return false;
		}
	}
	return true;
}
/* �������FORM������Ӧ������tagԪ�� */
function verifyAllform()
{
	//alert("in verifyAllform");
	var j;
	Validator.clearValidateInfo();
	for (j=0;j<document.forms.length;j++)
	{
		/* У�鵱ǰformԪ�� */
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

/* ͨ����ǰtagNode�ж��Ƿ��ڿɱ༭�б���
 * ���ؿɱ༭���Ƿ�Ϊɾ��״̬
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
