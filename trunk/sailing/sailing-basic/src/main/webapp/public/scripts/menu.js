
/*
 * ģ��VS.net�˵���Javascript�˵�
 * һ���򵥵Ĳ˵���
 * 2006-07-30
 * ����IE��FireFox
 * �Զ������˵���λ
 */

function Menu(name){
	//�����������ֲ˵����������
	this.name = name;
	_this = this;

	//����˵�������ʽ: ��ֱ����ʾ���ڵ�(=1)������maximo�˵�(=0)
	this.displayType = 0;

	//�˵���<TR>
	this.MenuBar;
	//������壬���еĲ˵����ݶ����������<div>
	this.ContentPane;
	//��ǰ�Ĳ˵����ڲ˵�������ʾ�Ĳ˵���Ŀ<TD>
	this.CurrentMenu;
	//��ǰ�˵���壬��ʾ�˵���Ŀ�б������<TABLE>
	this.CurrentMenuBoard;
	//��ǰ�Ĳ˵���Ŀ<TR>
	this.CurrentMenuItem;
	this.cellIndex=0;

	//����˵�����ʾλ��
	this.displayAddress  = document.body;
	
	//����˵�����ͼƬ
	this.indexMenuImg   = "image/right.gif";
	//���css
	this.menuCss   = "top_menu_captionFocus";
	//�˵����Ⱥ͸߶�
	this.menuWidth = 160;
	this.menuHeight = 22;
	this.lastMenu;

	//���Ӳ˵���
	//ͬʱ����MenuBar��ContentPane
	this.addMenuBar=function(){
		this.ContentPane = document.createElement("div");
		this.displayAddress.appendChild(this.ContentPane);
		this.ContentPane.className = "top_menu_bar";
		
		var _menubar = document.createElement("table");
		_menubar.className = "top_menu_titleTable";
		_menubar.cellPadding = 1;
		_menubar.cellSpacing = 1;

		this.ContentPane.appendChild(_menubar);
		this.MenuBar = _menubar.insertRow(0);
	}
	
	//�ڲ˵���������һ���˵�
	//ͬʱ���õ�ǰ�˵�
	//�������˵����ı�����
	this.addMenu=function(_img,_mStr){
		_menu = document.createElement("td");
		this.MenuBar.appendChild(_menu);
		_menu.noWrap = true;
		_menu.className = "top_menu_titleBlur";
		var str =  "";
		if(_img){
			_img = ContextInfo.contextPath + "/"+ _img;
			str += "<img src='"+_img+"'/> ";
		}
		str += "<a href=# class='link'>"+_mStr+"</a>&nbsp";
		_menu.innerHTML = str;
		_menu.onmouseover = function(){
			_this.showMenuBoard(_menu.parentNode.childNodes[this.cellIndex],true);
			if(_this.lastMenu){
				_this.lastMenu.className  = "top_menu_titleBlur";
			}
			this.className = "top_menu_titleFocus";			
			_this.lastMenu = this;
		}
		_menu.onmouseout = function(){
			hiddenMenuBoard(_menu.parentNode.childNodes[this.cellIndex]);
		}
		this.CurrentMenu = _menu;
		this.cellIndex += 1;
	}

	//�趨�˵�λ��
	this.setMainMenuAddress = function(_panel,_owner){
		var temp = _owner;
		var tleft = temp.offsetLeft + 1;
		var ttop  = temp.offsetTop;
		while (temp = temp.offsetParent){
			ttop  += temp.offsetTop;
			tleft += temp.offsetLeft;
		}
		_panel.style.left = tleft+"px";
		_panel.style.top  = ttop + _owner.offsetHeight - 0;
	}

	this.setLeafMenuAddress = function(_panel,_owner){		
		var tleft = _owner.offsetLeft;
		var ttop  = (parseInt(_owner.height,10)+0) * _owner.parentNode.rowIndex;
		var temp_btn = _owner;
		while (temp_btn = temp_btn.offsetParent){
			tleft+= temp_btn.style.left;
		}
		tleft = tleft+"";
		var arrLeft = tleft.split("px");
		tleft = 0;
		for (var i=0;i<arrLeft.length ;i++ ){
			tleft += parseInt(arrLeft[i],10);
		}
		if(tleft + (this.menuWidth*2) > document.body.clientWidth)
			tleft = -this.menuWidth;
		else
			tleft = this.menuWidth+10;
		_panel.style.left = tleft;
		_panel.style.top  = ttop;
	}
	
	//���Ӳ˵���Ŀ�б�����
	//ͬʱ���õ�ǰ������
	//�����������������ߣ�����parentNode�����Ƿ�Ϊ��������
	this.addMenuBoard=function(_owner, _isTop){
		var _panel = document.createElement("div");//visibility:hidden;
		_panel.className ="top_menu_panel";
		
		/**/
		if(_isTop){
			this.setMainMenuAddress(_panel,_owner);
		}else{
			this.setLeafMenuAddress(_panel,_owner);	
		
			_owner.parentNode.childNodes[0].onmouseover = function(){
				setMenuItemBgColor(_owner.parentNode, true)
				_this.showMenuBoard(_owner,_panel);
			}
			_owner.parentNode.childNodes[0].onmouseout = function(){
				setMenuItemBgColor(_owner.parentNode, false)
				hiddenMenuBoard(_owner);
			}
			_owner.parentNode.childNodes[1].onmouseover = function(){
				setMenuItemBgColor(_owner.parentNode, true)
				_this.showMenuBoard(_owner,_panel);
			}
			_owner.parentNode.childNodes[1].onmouseout = function(){
				setMenuItemBgColor(_owner.parentNode, false)
				hiddenMenuBoard(_owner);
			}
			_owner.parentNode.childNodes[2].onmouseover = function(){
				setMenuItemBgColor(_owner.parentNode, true)
				_this.showMenuBoard(_owner,_panel);
			}
			_owner.parentNode.childNodes[2].onmouseout = function(){
				setMenuItemBgColor(_owner.parentNode, false)
				hiddenMenuBoard(_owner);
			}
		}

		this.CurrentMenuBoard = document.createElement("table");
		this.CurrentMenuBoard.className = "top_menu_board";
		this.CurrentMenuBoard.border = 0;
		this.CurrentMenuBoard.width = this.menuWidth;
		this.CurrentMenuBoard.cellPadding = 0;
		this.CurrentMenuBoard.cellSpacing = 0;
		_owner.appendChild(_panel);
		_panel.appendChild(this.CurrentMenuBoard);
	}
	
	//���Ӳ˵���
	//ͬʱ���õ�ǰ�˵���
	//�������˵���Ŀ�б��������˵����ı���ͼ��(16 x 16)������λ�ã�����Ŀ�꣬�Ƿ����¼��˵�
	this.addMenuItem=function(_board, _mStr, _mIcon, _mHref, _mTrg, _hasSubMenu){
		var _itemBar = _board.insertRow(_board.rows.length);
		//_itemBar.style.cssText = "background-color:#FFFFFF";
		
		var _iconCell = document.createElement("TD");
		_itemBar.appendChild(_iconCell);
		_iconCell.align = "center"; 
		_iconCell.width = 25;
		_iconCell.noWrap = true;
		_iconCell.className = "top_menu_icon";
		
		//var _strCell = _itemBar.insertCell();
		var _strCell = document.createElement("TD");
		_itemBar.appendChild(_strCell);
		_strCell.align = "left"; 
		_strCell.width = this.menuWidth - 35;
		_strCell.noWrap = true;
		_strCell.className = "top_menu_caption";
				
		if(_mStr != "-"){
			//var _subCell = _itemBar.insertCell();
			var _subCell = document.createElement("TD");
			_itemBar.appendChild(_subCell);
			_subCell.noWrap = true;
			_subCell.className = "top_menu_indexImg";
			
			_iconCell.onmouseover = function(){
				setMenuItemBgColor(_iconCell.parentNode, true);
			}
			_iconCell.onmouseout = function(){
				setMenuItemBgColor(_iconCell.parentNode, false);
			}
			_strCell.onmouseover = function(){
				setMenuItemBgColor(_strCell.parentNode, true);
			}
			_strCell.parentNode.onmouseover = function(){
				_strCell.className = "top_menu_caption " + _this.menuCss;
				this.style.cursor = "pointer";
			}
			_strCell.parentNode.onmouseout = function(){
				_strCell.className = "top_menu_caption";
			}
			_strCell.onmouseout = function(){
				setMenuItemBgColor(_strCell.parentNode, false);
			}
			_subCell.onmouseover = function(){
				setMenuItemBgColor(_strCell.parentNode, true);
			}
			_subCell.onmouseout = function(){
				setMenuItemBgColor(_strCell.parentNode, false);
			}
			_iconCell.height = this.menuHeight;
			_strCell.height  = this.menuHeight;
			_subCell.height  = this.menuHeight;
			if(_mIcon != ""){
				_mIcon = ContextInfo.contextPath + "/"+ _mIcon;
				_iconCell.innerHTML = "<img src="+_mIcon+" border=0>";
			}else{
				_iconCell.innerHTML = "&nbsp;";
			}
			_mTrg  = _mTrg  ? _mTrg  : "mainFrame";
			_mHref = _mHref ? _mHref : "#";
			//var hrf = "&nbsp;&nbsp;<a ";
			//if(_mHref != "#")
			//	hrf += " href='"+ _mHref +"' onclick='"+this.name+".click(this)'";
			//hrf += " target="+ _mTrg +" class="+this.menuCss+">";
			var hrf = _mStr;
			//hrf += "</a>"
			if(_mHref != "#"){
				_strCell.parentNode.onclick = function(){				
						frames[_mTrg].location = _mHref;
						_this.click(this);				
				}
			}
			_strCell.innerHTML = hrf;
			if(_hasSubMenu){
				_subCell.innerHTML = "<img src="+this.indexMenuImg+">&nbsp;";
			}else{
				_subCell.innerHTML = "&nbsp;";
			}
		}else{
			_iconCell.height = 2;
			_strCell.height = 2;
			_strCell.colSpan = 2;
			_strCell.style.padding = 0;
			_strCell.innerHTML = "<TABLE width=100% height=2><TR><TD bgcolor='buttonface'></TD></TR></TABLE>";
		}
		this.CurrentMenuItem = _itemBar;
	}
	
	//����˵���
	this.click = function(sel){
		//open loading
		top.definedWin.openLoading();
		while(sel.tagName != "DIV"){
			sel =  sel.offsetParent;
		}
		sel.style.display = "none";
	}

	
	//��ʾ_owner�Ĳ˵���Ŀ�б�
	this.showMenuBoard =function (_owner,_panel){
		var _obj = _owner.getElementsByTagName("DIV");
		if(true == _panel)
			this.setMainMenuAddress(_obj[0],_owner);
		else
			this.setLeafMenuAddress(_obj[0],_owner);
		_obj[0].style.display = "block";
		//var _obj = _owner.all.tags("DIV");		
		//_obj[0].style.visibility = "visible";
	}
	
	//����_owner�Ĳ˵���Ŀ�б�
	function hiddenMenuBoard(_owner){
		//var _obj = _owner.all.tags("DIV");
		var _obj = _owner.getElementsByTagName("DIV");
		//_obj[0].style.visibility = "hidden";
		_obj[0].style.display = "none";
	}
	
	//���ò˵���Ŀ�ı�����ɫ
	//�������˵���Ƿ�ѡ��
	function setMenuItemBgColor(_mItem, _isSelect){
		if(_isSelect){
			_mItem.childNodes[0].className = "top_menu_iconFocus";
			_mItem.childNodes[1].className = "top_menu_captionFocus";
			_mItem.childNodes[2].className = "top_menu_indexImgFocus";
		}else{
			_mItem.childNodes[0].className = "top_menu_icon";
			_mItem.childNodes[1].className = "top_menu_caption";
			_mItem.childNodes[2].className = "top_menu_indexImg";
		}
	}
	
	//�ݹ�_board�ĸ��˵����б�����
	this.getParentBoard=function(_board){
		if(!_board || "HTML" == _board.tagName)
			return;
		if (_board.parentNode.tagName=="TABLE"){
			return _board.parentNode;
		}else{
			return this.getParentBoard(_board.parentNode);
		}
		
	}
}