/******************************************************/
/* �ļ�����bisValidate.js                               */
/******************************************************/
/* ���ָ���ı��������Ƿ�Ϸ� */
function validateInput(input)
{
	/* �ǿ�У�� */
	var bisRequired = input.bisRequired==undefined?false:true;
	if (bisRequired && input.bisRequired=="true" && input.value==''){
		Validator.warnMessage('���ݲ�����:'+input.bisname+'����Ϊ��');
		return false;
	}
	return true;
}
/* ���ָ��FORM�����м���bisRequired���Ե�tagԪ��
����Щ�����Զ������Ե�Ԫ�أ��Ƿ�Ϸ����˺������ڱ���onsubmit�¼� */
function validateform(myform)
{
	var i;
	for (i=0;i<myform.elements.length;i++)
	{
		 /* ���Զ������Ե�Ԫ�ز������ */
		if (myform.elements[i].bisname+""=="undefined") continue;
		/* У�鵱ǰԪ�� */
		if (validateInput(myform.elements[i])==false)
		{
			myform.elements[i].focus();
			return false;
		}
	}
	return true;
}
/* �������FORM������Ӧ������tagԪ�� */
function validateAllform()
{
	var j;
	Validator.clearValidateInfo();
	for (j=0;j<document.forms.length;j++)
	{
		/* У�鵱ǰformԪ�� */
		if (validateform(document.forms[j])==false)
		{
			return false;
		}
	}
	return true;
}