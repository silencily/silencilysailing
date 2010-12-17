<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file="/common/taglibs.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">

<script>
function MM_openDialog(url, width, height)
{
	if(! width)
	{	
		width = 800;
	}	
	if(! height)
	{	
		height = 600;
	}	
	
	if (url.indexOf('?') > 0) 
	{
		url += '&dontUserMeDate=' + escape(new Date().toString());
	}
	else 
	{
		url += '?dontUserMeDate=' + escape(new Date().toString());
	}

	return showModalDialog('<c:url value = '/jsp/dialogContainer.jsp' />', url, 'dialogWidth:' + width + 'px;dialogHeight:' + height + 'px;status:no;help:no;resizable:yes');
}


function MM_confirm(message) 
{
	if (! confirm('您确定要' + message + '吗 ?'))
	{
		return false;
	}
	return true;
}

function MM_reset() 
{
	document.forms[0].reset();
}

function readRSSText(feedURL)
{
	var request;
	/* Create XMLHttpRequest Object */
	try 
	{
		request = new XMLHttpRequest();	
	} 
	catch (e) 
	{  
		request = new ActiveXObject("Msxml2.XMLHTTP"); 
	}
	
	try 
	{
		// Needed for Mozilla if local file tries to access an http URL
		netscape.security.PrivilegeManager.enablePrivilege("UniversalBrowserRead");
	} 
	catch (e) {  /* ignore */ }
	
	var doc = request.open("POST", feedURL, false);
	request.send(doc);
	var feed = request.responseText;
	return feed;
}
</script>