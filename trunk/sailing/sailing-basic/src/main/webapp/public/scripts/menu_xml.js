//����XML�ļ�����˵���javascrip����
//ʹ���ļ����Ƴ�ʼ������
//��Ҫ�޸ĳ����еĲ��ִ�������Ӧ��Ҫ
//��Ҫ���޸�this.BuildMenu=function(_Menus)�����е�����
//�ֱ��д�������ֲ˵��ľ������
//��ȻҲ����ʹ�á�����
//����IE��FireFox

function TMenuXML(_filename, _m){
	this.isShowTree = false;
	//��Ҫװ�ص�XML�ļ�����
	var XMLFileName=_filename;
	//javascrip XML ����
	//var XMLDoc = new ActiveXObject("MSXML2.DOMDocument.3.0");
	//���ļ���ߵĿո�����
	var LeftMargin=0;

	//���ڵ����ݣ������ж�
	this.rootDate = new Array();

	//#link xmlhttp.js
	var XMLDoc = XMLHttpEngine.getXMLDoc();

	//�����ʼ������
	this.Create=function(){
		if(XMLDoc.load(XMLFileName)){
			//var Menus = XMLDoc.selectSingleNode("/Data/Menu");
			var Menus = XMLDoc.getElementsByTagName('Menu');			

			//�����������Menu�˵�	displayType �� memu.js �ж���		
			if(_m.displayType){
				this.BuildMenuList(Menus[0]);
			}else{
				this.BuildMenu(Menus[0]);				
			}
		}
	}

	//ȡ�ýڵ�����ݡ��ı�
	//Ҳ����ȡ�ýڵ����Ե�ֵ
	this.getNode=function(doc, xpath) {
		var retval = "";
		//var value = doc.selectSingleNode(xpath);
		//if (value) retval = value.text;
		xpath = xpath.replace("@","");
		var retval = doc.getAttribute(xpath);
		return retval;
	}

	//�ݹ��ȡ��һ����Ҫѭ���Ĳ˵�
	this.getNextNode=function(_Node){
		if(_Node == null) return null;
		//�����ж��Ƿ��Ǹ��ڵ㣬�����˳�
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

	//������ڵ�˵�,
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
	//���ɸ��ڵ�����
	this.addRootMenu = function(_temp){	
		if(this.isShowTree){
			document.write(this.getNode(_temp, "@text") +"<br>");
		}
		var imagStr = this.getNode(_temp, "@icon");
		var nameStr = this.getNode(_temp, "@text");
		_m.addMenu(imagStr,nameStr);
		_m.addMenuBoard(_m.CurrentMenu, true);
	}
	//�����ӽڵ�����
	this.addChildMenu = function(_temp){
		if(this.isShowTree){
			document.write(this.getLeftMarginStr() + this.getNode(_temp, "@text") +" &gt;<br>");
		}
		_m.addMenuItem(_m.CurrentMenuBoard, this.getNode(_temp, "@text"), this.getNode(_temp, "@icon"), 
				this.getNode(_temp, "@href"), this.getNode(_temp, "@targ"), true);
		_m.addMenuBoard(_m.CurrentMenuItem.childNodes[2], false);
	}

	//����˵�
	this.BuildMenu=function(_Menus,flag){
		if (_Menus!=null) {
			LeftMargin += 1;

			if(_Menus.nodeName=="Menu"){
				this.addRootMenu(_Menus);
			}
			//�����������SubMenu�˵�
			if(!flag && _Menus.nodeName=="SubMenu"){
				this.addChildMenu(_Menus);
			}
			this.BuildItemMenu(_Menus);
		}
	}
	this.BuildItemMenu = function(_Menus,_last){
		//�����������MenuItem�˵�
		if(_Menus.hasChildNodes()){
			_MenuNode = _Menus.firstChild;
			while(_MenuNode != null){	
				if(_MenuNode.nodeName=="MenuItem"){
					if(this.isShowTree){
						document.write(this.getLeftMarginStr() + "��" + this.getNode(_MenuNode, "@text") +"<br>");
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
			lmStr += "��";
		}
		return lmStr;
	}
}
