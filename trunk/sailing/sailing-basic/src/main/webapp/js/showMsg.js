/*
 * Object for showing message
 * @Auther huxf@bis.com.cn
 */
 
 /*
  * Constructor
  */
function msgInfo() {
	// input parameter(Array)
	this.inputParam;
	// message ID
	this.msgId;
	// location where msg will be shown
	this.locationAdd;
	// message content
	this.message;
	// CSS class
	this.className;
}

msgInfo.prototype.showServerMsg = function(msgServer_) {
	msgServer_ = msgServer_ || "NO SERVER MSG";
	document.getElementById("ServerMsg").innerHTML = msgServer_;
}

/*
 * @param string  message ID which will be show
 * @param array   content which will be repleaced from parameter
 * @param string  where msg will be shown
 */
msgInfo.prototype.showMsg = function(msgId_, inputParam_, location_) {
	this.inputParam = inputParam_ || new Array();
	this.msgId = msgId_ || "default";
	this.locationAdd = location_ || "msgArea";
	
	// get message
	this.message = this.repleaceInputParam();
	// show message
	document.getElementById(this.locationAdd).innerHTML = this.message;
}

/*
 * @param string  message ID which will be show
 * @param array   content which will be repleaced from parameter
 * @param string  popup message window's type (something like how many buttons)
 */
msgInfo.prototype.popMsg = function(msgId_, inputParam_, msgType) {
	this.inputParam = inputParam_ || new Array();
	this.msgId = msgId_ || "default";
	
	// pop up message window
	var returnVar = window.showModalDialog("popMsg.html", this.msgId, "dialogHeight:600px;dialogWidth:700px;status:no");	

	// if pop msg window is closed by click "X", return cancel
	returnVar = typeof(returnVar) == "undefined" ? "cancel" : returnVar;
	
	return returnVar;
}

/*
 * get message content and replace the input parameter
 * if message ID has not been defined, show a default
 *
 * @return string   final message
 */
msgInfo.prototype.repleaceInputParam = function() {
	// get the amounts of the parameters
	var inputLength = this.inputParam.length;
	// get message from property file
	var msgContent = this.getMsgById_();
	// when input msgid doesn't exsit, show default value
	msgContent = typeof(msgContent) == "undefined" ? "Empty msg" : msgContent;
	
	// replace parameters
	for (var a = 0; a < inputLength; a++) {
		// define the source {a}
		var tmp = "{" + a.toString() + "}";
		// replace source index from target msg
		msgContent = msgContent.replace(tmp, this.inputParam[a]);
	}
	
	return msgContent;
}

/*
 * get message content by message ID
 *
 * @return string   message content
 */
msgInfo.prototype.getMsgById_ = function() {
	return msgHash[this.msgId];
}
/*
 * get message content by message ID
 *
 * @return string   message content
 */
msgInfo.prototype.getMsgById = function(msgId_) {
	return msgHash[msgId_];
}
/*
 * get message content by message ID
 *
 * @return string   message content
 */
msgInfo.prototype.getMsgById = function(msgId_,inputParam_) {
this.inputParam = inputParam_ || new Array();
	this.msgId = msgId_ || "default";
	return this.repleaceInputParam();
}
