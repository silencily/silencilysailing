

/* 
 * ��Ϣ���ѵ�JS�����Լ��ṩ�ӿڡ�����
 * liuz 
 * 2006-10-20
 */

var Message = {};

//����һ����Ϣ���ڶ���
Message.msgObject = function(name,reflashObj,contextPath,imgObj){
	this.name = name;
	this.path = contextPath ? contextPath : "..";
	this.reflashTime = 60000; //ˢ��ʱ�� 1'00"
	this.width  = 200;
	this.height = 170;
	this.msgArray    = new Array();
	this.reflashUrl  = "";
	this.reflashTimeObj = null;//�Ӻ�̨ȡ����ˢ�¼�ʱ��
	this.switchAddress  = null;//�л���ʾ��λ��
	this.reflashObj = reflashObj;
	this.winObject  = window.createPopup();//�������ڶ���
	this.msgImageObj= imgObj;
	if(imgObj){
		this.msgImageUrl    = this.msgImageObj.src;    //Ĭ��ʱ��ͼƬ·��
		this.msgImageNewUrl = this.msgImageObj.newSrc; //ָ������Ϣ��ʱ���ͼƬ·��
	}

	//ˢ��ȡ����
	this.reflash = function(){
		//������ȡ���ݵ�ʱ�򼤻�ص�
		try{
			this.reflashObj.src = this.reflashUrl;
			//this.reflashTimeObj = setTimeout(this.name+'.reflash()',this.reflashTime);
		}catch(e){
			this.stopReflash();
		}
	}
	//ֹͣ��ȡ
	this.stopReflash = function(){
		clearTimeout(this.reflashTimeObj);
	}

	//��Ϣ���ݶ���
	this.message = function(){
		this.id    = "";
		this.title = "";
		this.sender= "";
		this.link  = "";
		this.time  = "";
		this.more  = "";
	}

	this.getMessage = function(){
		//��ȡ���ݵ�JS��ʵ�֣�����һ����Ϣ����
	}

	//��װ��Ϣ
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
				str += '<a href=#  onclick="parent.'+this.name+'.getAction(\''+arr[i].more+'\')">����>></a>';
				str += '</td></tr>';
				break;
			}
			var title = arr[i].title.length > 20 ? arr[i].title.substring(0,20)+"..." : arr[i].title;
			str += '<td id="message_list">';
			str += ' <a href="#" onClick="parent.'+this.name+'.getAction(\''+arr[i].link+'\')" title="����ʱ�䣺'+arr[i].time+'">';
			str += ' <span class="message_sender">'+arr[i].sender+'</span>��'+title+'</a>'; 	
			str += '</td></tr>';			
		}
		str += '</table>'
		var winStr = '\
			<html><head><link rel="stylesheet" type="text/css" media="all" href="'+this.path+'/css/style.css" />\
			<link rel="stylesheet" type="text/css" media="all" href="'+this.path+'/css/style_interface.css" /></head> \
			<body style="overflow:hidden;margin:0px"> \
			<table width="'+this.width+'" height="'+this.height+'" border="0" cellpadding="0" cellspacing="0" class="message_box"> \
				<tr><td align="right"  onClick="parent.'+this.name+'.windowClose();" \
				class="message_close" title="�رմ���" >\
					&nbsp;��&nbsp; </td></tr> \
				<tr><td>'+str+'</td></tr>\
			</table></body></html> ';
		return winStr;
	}
	
	//��ʽһ������������ʾ
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

	//�رյ�������
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
	//����ϢͼƬ������
	this.setMsgImage = function(flag){
		if(this.msgImageObj){
			if(flag){
				this.msgImageObj.src  = this.msgImageNewUrl;
			}else{
				this.msgImageObj.src  = this.msgImageUrl;
			}
		}
	}
	//����ǲ��ƶ�
	this.show = function(tleft,ttop,twidth,theight){
		this.winObject.style.left = tleft + "px";
		this.winObject.style.top = ttop + "px";
		this.winObject.style.width = twidth + "px";
		this.winObject.style.height = theight + "px";
	}
	this.hide = function(){
		this.winObject.style.display = "none";
	}


	//��ʽ�������л���ʽ��ʾ��Ϣ
	//�л���������
	this.switchNum  = -1;
	this.switchTime = 10005; //�л���ʾ��ʱ��
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
				arr[this.switchNum].sender +'��'+ title +' </a>';
			obj.filters.revealTrans.play()
			temp = setTimeout(this.name+'.switchDisplay()',this.switchTime);
		}catch(e){}
	}
	
	this.getAction = function(url){
		frames["mainFrame"].focus();
		frames["mainFrame"].location = url;
	}
}



