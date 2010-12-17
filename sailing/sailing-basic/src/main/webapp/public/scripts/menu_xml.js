//解析XML文件构造菜单的javascrip对象
//使用文件名称初始化对象
//需要修改程序中的部分代码来适应需要
//主要是修改this.BuildMenu=function(_Menus)函数中的内容
//分别改写构造三种菜党的具体代码
//当然也可以使用“树”
//兼容IE和FireFox

function TMenuXML(_filename, _m){
	this.isShowTree = false;
	//需要装载的XML文件名称
	var XMLFileName=_filename;
	//javascrip XML 对象
	//var XMLDoc = new ActiveXObject("MSXML2.DOMDocument.3.0");
	//到文件左边的空格数量
	var LeftMargin=0;

	//根节点数据，用于判断
	this.rootDate = new Array();

	//#link xmlhttp.js
	var XMLDoc = XMLHttpEngine.getXMLDoc();

	//对象初始化函数
	this.Create=function(){
		if(XMLDoc.load(XMLFileName)){
			//var Menus = XMLDoc.selectSingleNode("/Data/Menu");
			var Menus = XMLDoc.getElementsByTagName('Menu');			

			//按照情况构造Menu菜单	displayType 在 memu.js 中定义		
			if(_m.displayType){
				this.BuildMenuList(Menus[0]);
			}else{
				this.BuildMenu(Menus[0]);				
			}
		}
	}

	//取得节点的数据、文本
	//也可以取得节点属性的值
	this.getNode=function(doc, xpath) {
		var retval = "";
		//var value = doc.selectSingleNode(xpath);
		//if (value) retval = value.text;
		xpath = xpath.replace("@","");
		var retval = doc.getAttribute(xpath);
		return retval;
	}

	//递归读取下一个需要循环的菜单
	this.getNextNode=function(_Node){
		if(_Node == null) return null;
		//这里判断是否是根节点，是则退出
		if(_m.displayType){
			for(var i=0;i<this.rootDate.length;i++){
				if(_Node == this.rootDate[i])
					return null;
			}
		}
		if(_Node.nextSibling != null){
			return _Node.nextSibling;
		}else{
			LeftMargin -= 1;
			_m.CurrentMenuBoard = _m.getParentBoard(_m.CurrentMenuBoard);
			return this.getNextNode(_Node.parentNode);
		}
	}

	//构造根节点菜单,
	this.BuildMenuList=function(_Menus){
		var _temp;
		for(var i=0;i<_Menus.childNodes.length;i++){
			_temp = _Menus.childNodes[i];
			if(_temp.nodeName == "SubMenu"){
				this.rootDate[this.rootDate.length] = _temp;				
			}
		}
		for(var i=0;i<this.rootDate.length;i++){
			this.addRootMenu(this.rootDate[i]);
			LeftMargin += 1;
			this.BuildItemMenu(this.rootDate[i]);
		}

	}
	//生成根节点数据
	this.addRootMenu = function(_temp){	
		if(this.isShowTree){
			document.write(this.getNode(_temp, "@text") +"<br>");
		}
		var imagStr = this.getNode(_temp, "@icon");
		var nameStr = this.getNode(_temp, "@text");
		_m.addMenu(imagStr,nameStr);
		_m.addMenuBoard(_m.CurrentMenu, true);
	}
	//生成子节点数据
	this.addChildMenu = function(_temp){
		if(this.isShowTree){
			document.write(this.getLeftMarginStr() + this.getNode(_temp, "@text") +" &gt;<br>");
		}
		_m.addMenuItem(_m.CurrentMenuBoard, this.getNode(_temp, "@text"), this.getNode(_temp, "@icon"), 
				this.getNode(_temp, "@href"), this.getNode(_temp, "@targ"), true);
		_m.addMenuBoard(_m.CurrentMenuItem.childNodes[2], false);
	}

	//构造菜单
	this.BuildMenu=function(_Menus,flag){
		if (_Menus!=null) {
			LeftMargin += 1;

			if(_Menus.nodeName=="Menu"){
				this.addRootMenu(_Menus);
			}
			//按照情况构造SubMenu菜单
			if(!flag && _Menus.nodeName=="SubMenu"){
				this.addChildMenu(_Menus);
			}
			this.BuildItemMenu(_Menus);
		}
	}
	this.BuildItemMenu = function(_Menus,_last){
		//按照情况构造MenuItem菜单
		if(_Menus.hasChildNodes()){
			_MenuNode = _Menus.firstChild;
			while(_MenuNode != null){	
				if(_MenuNode.nodeName=="MenuItem"){
					if(this.isShowTree){
						document.write(this.getLeftMarginStr() + "·" + this.getNode(_MenuNode, "@text") +"<br>");
					}
					_m.addMenuItem(_m.CurrentMenuBoard, this.getNode(_MenuNode, "@text"), this.getNode(_MenuNode, "@icon"), this.getNode(_MenuNode, "@href"), this.getNode(_MenuNode, "@targ"), false);
				}
				
				if(_MenuNode.hasChildNodes()){
					this.BuildMenu(_MenuNode);
				}
				
				_MenuNode = this.getNextNode(_MenuNode);
			}
		}
	}

	this.getLeftMarginStr=function(){
		lmStr="";
		for(i=0; i<LeftMargin; i++){
			lmStr += "　";
		}
		return lmStr;
	}
}
