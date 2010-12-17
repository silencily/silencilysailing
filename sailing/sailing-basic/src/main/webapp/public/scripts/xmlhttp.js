/**
 * XMLHttp 引擎实现
 * @author 王政
 * @version $Id: xmlhttp.js,v 1.1 2010/12/10 10:56:35 silencily Exp $
 */

if (XMLHttpEngine == null)  {
	var XMLHttpEngine = {};
}

// 定义 XMLHttp 的属性
XMLHttpEngine._verb = "GET";
XMLHttpEngine._ordered = false;
XMLHttpEngine._async = true;
XMLHttpEngine._batch = null;
XMLHttpEngine._timeout = 0;
XMLHttpEngine._DOMDocument = ["Msxml2.DOMDocument.6.0", "Msxml2.DOMDocument.5.0", "Msxml2.DOMDocument.4.0", "Msxml2.DOMDocument.3.0", "MSXML2.DOMDocument", "MSXML.DOMDocument", "Microsoft.XMLDOM"];
//XMLHttpEngine._XMLHTTP = ["Msxml2.XMLHTTP.6.0", "Msxml2.XMLHTTP.5.0", "Msxml2.XMLHTTP.4.0", "MSXML2.XMLHTTP.3.0", "MSXML2.XMLHTTP", "Microsoft.XMLHTTP"];

XMLHttpEngine._XMLHTTP = ["Msxml2.XMLHTTP"];

XMLHttpEngine.READY_STATE_UNINITIALIZED = 0;
XMLHttpEngine.READY_STATE_LOADING = 1;
XMLHttpEngine.READY_STATE_LOADED = 2;
XMLHttpEngine.READY_STATE_INTERACTIVE = 3;
XMLHttpEngine.READY_STATE_COMPLETE = 4;


/**
 * 发送一个 xmlHttp 请求
 * @param url the request url
 * @param async 是否以异步方式请求
 * @HttpMethod 'POST' 或  'GET'
 * @param params 参数
 * @param timeout 超时时间 
 */
XMLHttpEngine.sendRequest = function(url, async, HttpMethod, params, timeout) {
	if (typeof async == 'undefined') {
		async = XMLHttpEngine._async;	
	}
	if (!HttpMethod) {
		HttpMethod = XMLHttpEngine._verb;
	}
	var request = XMLHttpEngine.getXMLHTTPRequest();

	if (request) {
		try {
			// request.onReadyStateChange = XMLHttpEngine.onReadyState(request);
			var operator = url.indexOf('?') < 0 ? '?' : '&';
			var enhancedUrl = url + operator + new Date().getTime();
			request.open(HttpMethod, enhancedUrl, async);
			request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			request.send(params);
			return request;
		} catch (e) {
			alert('XmlHttp 请求出错 : ' + e);
		}
	}
}

/**
 * 发送一个 xmlHttp 请求, 得到 responseText
 * @param url the request url
 * @param async 是否以异步方式请求
 * @HttpMethod 'POST' 或  'GET'
 * @param params 参数
 * @param timeout 超时时间 
 */
XMLHttpEngine.getResponseText = function(url, async, HttpMethod, params, timeout) {
	var request = XMLHttpEngine.sendRequest(url, async, HttpMethod, params, timeout);
	return request.responseText;
}

XMLHttpEngine.onReadyState = function(request) {
	var ready = request.readyState;
	if (ready == XMLHttpEngine.READY_STATE_COMPLETE) {
		XMLHttpEngine.processResponseText(request.responseText);
		XMLHttpEngine.processResponseXML(request.responseXML);
	}
}

XMLHttpEngine.getXMLHTTPRequest =  function(xmlEngine) {
	var xRequest  = null;
	xmlEngine = xmlEngine ? xmlEngine : XMLHttpEngine._XMLHTTP;
	if (window.XMLHttpRequest) {
		xRequest = new XMLHttpRequest();
	}  else if (window.ActiveXObject && !(navigator.userAgent.indexOf('Mac') >= 0 && navigator.userAgent.indexOf("MSIE") >= 0)) {
		xRequest = XMLHttpEngine.newActiveXObject(xmlEngine);
	} else {
		alert(' 无法获取 XmlHttp 对象 ! ');	
	}
	
	return xRequest;
}

XMLHttpEngine.newActiveXObject = function(axarray) {
	var returnValue;
	for (var i = 0; i < axarray.length; i++) {
		try {
			returnValue = new ActiveXObject(axarray[i]);
			break;
		} catch (ex) {
		}
	}
	if (returnValue == null) {
		alert(' 创建 ActiveObject 对象失败 ');
	}
	return returnValue;
};

/* 
 * 声明DOM对象,与newActiveXObject不同在于这里使用"Microsoft.XMLDOM"
 * 兼容IE和FireFox
 */
XMLHttpEngine.getXMLDoc = function(){
	var xmldom;
	if (window.ActiveXObject){
		xmldom = new ActiveXObject("Microsoft.XMLDOM");
	} else {
		if (document.implementation && document.implementation.createDocument) {
			xmldom = document.implementation.createDocument("","doc",null);
		}
	}
	xmldom.async = false;
	xmldom.resolveExternals = false;
	xmldom.validateOnParse = false;
	xmldom.preserveWhiteSpace = true;
	return xmldom;
}