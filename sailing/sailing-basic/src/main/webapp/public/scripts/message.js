

/* 
 * 消息提醒的JS对象以及提供接口、方法
 * liuz 
 * 2006-10-20
 */

var Message = {};

//建立一个消息窗口对象
Message.msgObject = function(name,reflashObj,contextPath,imgObj){
	this.name = name;
	this.path = contextPath ? contextPath : "..";
	this.reflashTime = 60000; //刷新时间 1'00"
	this.width  = 200;
	this.height = 170;
	this.msgArray    = new Array();
	this.reflashUrl  = "";
	this.reflashTimeObj = null;//从后台取数据刷新计时器
	this.switchAddress  = null;//切换显示的位置
	this.reflashObj = reflashObj;
	this.winObject  = window.createPopup();//弹出窗口对象
	this.msgImageObj= imgObj;
	if(imgObj){
		this.msgImageUrl    = this.msgImageObj.src;    //默认时候图片路径
		this.msgImageNewUrl = this.msgImageObj.newSrc; //指定有消息的时候的图片路径
	}

	//刷新取数据
	this.reflash = function(){
		//在这里取数据的时候激活回调
		try{
			this.reflashObj.src = this.reflashUrl;
			//this.reflashTimeObj = setTimeout(this.name+'.reflash()',this.reflashTime);
		}catch(e){
			this.stopReflash();
		}
	}
	//停止获取
	this.stopReflash = function(){
		clearTimeout(this.reflashTimeObj);
	}

	//消息内容对象
	this.message = function(){
		this.id    = "";
		this.title = "";
		this.sender= "";
		this.link  = "";
		this.time  = "";
		this.more  = "";
	}

	this.getMessage = function(){
		//在取数据的JS中实现，返回一个消息数组
	}

	//组装消息
	this.getMsgStr = function(){
		var arr = this.getMessage();
		if(arr.length<1)
			return "";
		var str = '';
		str += '<table border="0" width="100%" cellpadding="0" cellspacing="0" class="message_content">';
		for(var i=0;i<arr.length;i++){
			str += '<tr>';			
			if(i>2){
				str += '<td id="message_more">';
				str += '<a href=#  onclick="parent.'+this.name+'.getAction(\''+arr[i].more+'\')">更多>></a>';
				str += '</td></tr>';
				break;
			}
			var title = arr[i].title.length > 20 ? arr[i].title.substring(0,20)+"..." : arr[i].title;
			str += '<td id="message_list">';
			str += ' <a href="#" onClick="parent.'+this.name+'.getAction(\''+arr[i].link+'\')" title="发送时间：'+arr[i].time+'">';
			str += ' <span class="message_sender">'+arr[i].sender+'</span>：'+title+'</a>'; 	
			str += '</td></tr>';			
		}
		str += '</table>'
		var winStr = '\
			<html><head><link rel="stylesheet" type="text/css" media="all" href="'+this.path+'/css/style.css" />\
			<link rel="stylesheet" type="text/css" media="all" href="'+this.path+'/css/style_interface.css" /></head> \
			<body style="overflow:hidden;margin:0px"> \
			<table width="'+this.width+'" height="'+this.height+'" border="0" cellpadding="0" cellspacing="0" class="message_box"> \
				<tr><td align="right"  onClick="parent.'+this.name+'.windowClose();" \
				class="message_close" title="关闭窗口" >\
					&nbsp;×&nbsp; </td></tr> \
				<tr><td>'+str+'</td></tr>\
			</table></body></html> ';
		return winStr;
	}
	
	//方式一：弹出窗口显示
	this.winTimeObject = null;
	this.winNumber = 0;
	this.yCoordinate = top.document.body.clientHeight+ top.screenTop - 7;
	this.xCoordinate = top.document.body.clientWidth + top.screenLeft - this.width - 15;
	this.windowOpen = function(){
		var content = this.getMsgStr();
		if(content == ""){	
			this.setMsgImage(false);
			this.windowClose();			
			return;
		}
		this.setMsgImage(true);
		this.winObject.document.write(content);
		this.winObject.document.close();
		//this.winObject.innerHTML = this.getMsgStr();
		//this.winObject.style.display = "";
		this.winNumber = 0;	
		this.windowDisplay();
	}
	this.windowDisplay = function(){
		if(this.winNumber <= this.height){
			this.winObject.show(this.xCoordinate,this.yCoordinate-this.winNumber,this.width,this.winNumber);
			this.winNumber += 10;
			this.winTimeObject=setTimeout(this.name+".windowDisplay();",80);
		}else{
			clearTimeout(this.winTimeObject);
		}
	}

	//关闭弹出窗口
	this.windowClose = function(){		
		if(this.winNumber > 0){
			var maxTime = 1000;
			this.winObject.show(this.xCoordinate,this.yCoordinate+maxTime-this.winNumber,this.width,this.winNumber);
			this.winNumber = this.winNumber - 10;
			this.winTimeObject=setTimeout(this.name+".windowClose();",80);
		}else if(this.winObject.isOpen){
			clearTimeout(this.winTimeObject);
			this.winNumber = 0;
			this.winObject.hide();
		}
	}
	//对消息图片的设置
	this.setMsgImage = function(flag){
		if(this.msgImageObj){
			if(flag){
				this.msgImageObj.src  = this.msgImageNewUrl;
			}else{
				this.msgImageObj.src  = this.msgImageUrl;
			}
		}
	}
	//如果是层移动
	this.show = function(tleft,ttop,twidth,theight){
		this.winObject.style.left = tleft + "px";
		this.winObject.style.top = ttop + "px";
		this.winObject.style.width = twidth + "px";
		this.winObject.style.height = theight + "px";
	}
	this.hide = function(){
		this.winObject.style.display = "none";
	}


	//方式二：以切换形式显示消息
	//切换计数变量
	this.switchNum  = -1;
	this.switchTime = 10005; //切换显示的时间
	this.switchDisplay = function(){
		try{
			var temp;
			var arr = this.getMessage();
			var obj = this.switchAddress;
			clearTimeout(temp);
			obj.cloneNode();
			obj.innerHTML = '';		
			if(this.switchNum < arr.length-1)
				this.switchNum++;
			else
				this.switchNum = 0;
			obj.filters.revealTrans.Transition=23;
			obj.filters.revealTrans.apply();
			var title = arr[this.switchNum].title;
			title = title.length > 13 ? title.substring(0,10)+"..." : title;
			obj.innerHTML = '<a href="javascript:'+this.name+'.getAction(\''+arr[this.switchNum].link+'\');" class="">'+
				arr[this.switchNum].sender +'：'+ title +' </a>';
			obj.filters.revealTrans.play()
			temp = setTimeout(this.name+'.switchDisplay()',this.switchTime);
		}catch(e){}
	}
	
	this.getAction = function(url){
		frames["mainFrame"].focus();
		frames["mainFrame"].location = url;
	}
}



