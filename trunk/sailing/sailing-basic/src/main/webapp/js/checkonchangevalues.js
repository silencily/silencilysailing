
if(CheckValues == null){
	var CheckValues = {};
}
CheckValues.loadValues = function(){
	try{
	//全局form值改变
		var beforefrmNa = "frame_"+parent.panel.name+"_"+parent.panel.beforeindex;
		var beforefrm  = parent.document.getElementById(beforefrmNa);
		var dfs = parent.frames[beforefrmNa].document.forms;
		var allformvaluelist = new Array();
		for(i=0;i<dfs.length;i++){
			var df = dfs[i].getElementsByTagName("*");
			//alert(df.length);
			var formvaluelist = new Array();
			//alert(df.length+"::::"+dfs[i].length);
			for(j=0;j<df.length;j++){
				if(df[j].tagName == "IMG"){
					formvaluelist[j] = df[j].src;
				}
				else if(df[j].tagName == "OPTION"){
					//alert("OPTION");
					formvaluelist[j] = df[j].value;
				}
				else if(df[j].tagName == "INPUT" && df[j].type == "checkbox"){
					formvaluelist[j] = df[j].checked;
				}
				else {
					//alert("df[j].value:"+df[j].value);
					formvaluelist[j] = df[j].value;
				}
			}
			allformvaluelist[i] = formvaluelist;
		}
		parent.panel.beforeFrameValues = allformvaluelist;
		parent.panel.checkallvalue = true;
	}
	catch(e){}
}

CheckValues.loadValues();

CheckValues.ValuesObject = function (name){
	this.name = name;
	this.thisname = CheckValues.thisname;
	//alert(this.name);
}

CheckValues.thisname = function (name){
	
	//alert(name);
}

//检测form值是否改变
function checkOnchangeValueOfAllForm(){
	if(allformvaluelist==null){
		return false;
	}
	for(i=0;i<document.forms.length;i++){
		for(j=0;j<document.forms[i].length;j++){
			if(!(document.forms[i][j].type == "hidden")){
				if(document.forms[i][j].value != allformvaluelist[i][j]){
					//alert(document.forms[i][j].value+''+allformvaluelist[i][j]);
					//if(confirm("页面值已修改，是否保存？")){
						return true;
					//}
				}
			}
		}
	}
	return false;
}

//document.body.onclick = function(){
//	if(event.srcElement.tagName == "A"){
//		return catchpagetransfer();
//	}
//}

//document.body.onunload = function(){
//	alert("aaa");
//	if(!checkOnchangeValueOfAllForm()){
//		alert("bbb");
//		return true;
//	}else {
//		alert("ccc");
//		return false;
//	}
//}

//document.body.onbeforeunload = function(){
//	if(checkOnchangeValueOfAllForm()){
//		window.event.returnValue='本画面页面已修改,是否继续迁移?';
//	}
//}

//document.body.aLink = function(){
//	alert("asdfasdf");
//	return catchpagetransfer();
//}

function checktest(){
	//alert("checktest");
}

function pagebodyonbeforeunload(){
	return catchpagetransfer();
}

function catchpagetransfer(){
	//alert('catching!!');
	//alert(event.srcElement.href);
	if(!checkOnchangeValueOfAllForm()){
		return false;
	}
	return true;
}

//clue on message
function clewmessage(){confirm('页面值已修改，是否继续迁移？ 按"确定"直接迁移，选择"否"回到当前页面');}
