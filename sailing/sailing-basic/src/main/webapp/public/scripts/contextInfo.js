/**
 * @author java2enterprise
 * @version $Id: contextInfo.js,v 1.1 2010/12/10 10:56:35 silencily Exp $
 */
if (ContextInfo == null) {
	var ContextInfo = {};
}

ContextInfo.fetchServerAddr = function () {
	var url = location.href;
	var useHttps = url.indexOf('https://') > -1;
	var schmePart = useHttps ? 'https://' : 'http://';
	var remainUrl = url.substring(schmePart.length);
	var serverPart = remainUrl.substring(0, remainUrl.indexOf('/'));
	return schmePart + serverPart;
}

ContextInfo.serverAddr = ContextInfo.fetchServerAddr();
 

/* 浏览器版本 检测 temp为你需要判断的版本.
 * firefox 待补充
 */
ContextInfo.checkExplorer = function(temp){
	try{
		var BrowserInfo = new Object() ;
		BrowserInfo.MajorVer = navigator.appVersion.match(/MSIE (.)/)[1] ;
		BrowserInfo.MinorVer = navigator.appVersion.match(/MSIE .\.(.)/)[1] ;
		BrowserInfo.IsIEgoon = false;
		switch (temp)
		{
			case "6":
				BrowserInfo.IsIEgoon = BrowserInfo.MajorVer >= 6;break;
			case "5.5":
				BrowserInfo.IsIEgoon = BrowserInfo.MajorVer >= 6 || ( BrowserInfo.MajorVer >= 5 && BrowserInfo.MinorVer >= 5 ) ;break;
			case "4":
				BrowserInfo.IsIEgoon = BrowserInfo.MajorVer >= 4;break;
			default:
				BrowserInfo.IsIEgoon = BrowserInfo.MajorVer >= temp;break;
		}
		if (BrowserInfo.IsIEgoon)
			return true;
		else
			return false;
	}catch(e){return false;}
}

/* 
 * 用于原型中获取文件所在的目录级别，主要用于页面的相对路径，这里是以"publicresource"取值 
 * 类同JSP中 request.getContextPath() 方法
 * 如果不用 publicresource 为目录名，则需要修改这里（注意路径中不要重复出现"publicresource"）
 */
ContextInfo.getDemoContext = function(){
	if(location.href.indexOf("///")==-1)
		return '/qware';
	var folderName = "web";
	var file_href=location.href;
	var href_end=file_href.lastIndexOf("/");
	var myUrl="";
	if(file_href.indexOf(folderName)>0){
		myUrl = file_href.substring(file_href.indexOf(folderName) + 3);
		if(myUrl.indexOf('?')>0)
			myUrl = myUrl.substring(0,myUrl.indexOf('?'));
	}
	var url_count = 0;
	for(var url_i = 0; url_i < myUrl.length; url_i++){
		var char_flag = myUrl.charAt(url_i);
		if('/' == char_flag)
			url_count++;		
	}
	myUrl=folderName+"/";
	for(url_i=0;url_i< url_count;url_i++)
	{
		myUrl=myUrl+"../";
	}
	myUrl = myUrl.substring(0,myUrl.length-1);
	return 	myUrl;
}

ContextInfo.contextPath = ContextInfo.getDemoContext();
