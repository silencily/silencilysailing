/*
 * ѡ��ؼ�����
 * 2006-04-06
 * 
 * @name     �ؼ����֣�����Ӧ�ó��ֶ���ѡ������
 * @arr      ����ѡ������ݶ�ά���� [��ʾ����,����¼�]��
             ���磺arr[[name1,function1,url1],[name2,function2,url2],[...]];
 * @selectId Ĭ��ѡ�е������ţ�int������0��ʼ��
 */

if(Panel == null){
	var Panel = {};
}


Panel.panelObject = function (name,arr,selectId,imagePath,oneTimeUrl){
	this.checktaglib = false;
	this.checkallvalue = false;
	this.beforeindex = selectId;
	this.beforeFrameValues = new Array();
	this.beforeSearchTermList = new Array();
	this.afterSearchTermList = new Array();
	this.ispanelclick = false;
	this.name = name;	
	this.imagePath = imagePath ? imagePath : ContextInfo.contextPath+"/image/tree/";
	this.bgCss  = "panel_bg";		//����css
	this.tabCss = "panel_tab";		//ѡ�񿨱���css
	//this.focusCssv=  "";			//�۽�css
	//this.blurCss  =  "";			//��ͨcss
	this.dataArr   = arr;
	this.selectId  = selectId ? selectId : 0;//��ǰѡ�е�ѡ����
	this.newPanel  = new Array();   //����һ����ѡ�����ǰ������޸Ķ�Ӧ��ŵĿ�����
	this.disablePanel = new Array();//ָ��TabΪ���ã�����new Array(1,2)
	//this.address   = address;//��ʾλ��
	this.lastUrl   = "";					 //��һ��ѡ��url
	this.lastDivId = "divId_"+this.name+"_0";//��һ��ѡ��divId
	this.focusFont = "";
	this.blurFont  = "font_blur";
	this.uuid      = "oid";					 //������б��Ψһ���������ڴ��ݲ���
	this.listFrame = "frame_"+this.name+"_0";//�����б�frame��
	this.isPass    = true;			//�Ƿ������л����������ҳ��ָ��false,������message��ʾ
	this.message   = "Ǩ��ҳ����ܻᶪʧ����δ��������ݣ�ȷʵҪǨ����";//��ǰ����ʾ��Ϣ��"�л�Tab���ܻᶪʧ����δ��������ݣ�ȷʵҪ�л���";
	this.holdPage  = null;          //���Ҫ����ԭҳ�棨��ˢ�£��������︳��tab�����
	this.reloadPage= null;          //���ֻ��ҳ�����reload,��������url,�����︳��tab�����
	this.isUseOid  = true;          //�Ƿ�ʹ��oid���������ݣ�Ĭ���ǽ��е�
	this.oneTimeUrl = oneTimeUrl ? oneTimeUrl : "";   //�������ȴ���������ʾentry�µ�ĳһ����ҳ���URL

	/** Ϊ��ֹ����ͼƬʱ�����쳣�����뻺��*/
    this.oneImage1 = new Image();
	this.oneImage1.src = this.imagePath+"panel_one_1.gif";
	this.oneImage2 = new Image();
	this.oneImage2.src = this.imagePath+"panel_one_2.gif";
	this.oneImage3 = new Image();
	this.oneImage3.src = this.imagePath+"panel_one_3.gif";
	this.twoImage1 = new Image();
	this.twoImage1.src = this.imagePath+'panel_two_1.gif';
	this.twoImage2 = new Image();
	this.twoImage2.src = this.imagePath+'panel_two_2.gif';
	this.twoImage3 = new Image();
	this.twoImage3.src = this.imagePath+'panel_two_3.gif';
	this.twoImage31 = new Image();
	this.twoImage31.src = this.imagePath+'panel_two_3_1.gif';

	//public
	this.display       = Panel.display;		 //��ʾѡ�
	this.click         = Panel.click;		 //���ѡ�
	this.displayFrame  = Panel.displayFrame; //����iframe��·��
	this.setUrl        = Panel.setUrl;		 //�ṩ����url·��
	this.setContent    = Panel.setContent ;  //������л���ʾtab����������Ƕ�����ʾ������
	this.beforeClick   = Panel.beforeClick;  //���л�tab֮ǰ���Զ������
	this.afterClick    = Panel.afterClick;   //���л�tab֮����Զ������
	this.selfClick     = Panel.selfClick;    //ҵ��ҳ���Զ������
	//private
	this.setTab     = Panel.setTab;		 //��ʾĳ����	
	this.switchTab  = Panel.switchTab;
	this.displayDiv = Panel.displayDiv;
	this.setDisable = Panel.setPanelDisable;
	this.setAble    = Panel.setPanelAble;
	this.setProperies = Panel.setPanelProperies;
	
	this.checkValues = Panel.checkValues;
	this.checkTaglibValues = Panel.checkTaglibValues;
	this.confirmMessage = Panel.confirmMessage;
	this.movePage = Panel.movePage;
	this.saveSearchTerm = Panel.saveSearchTerm;
	this.setSearchTerm = Panel.setSearchTerm;
}

Panel.display = function (){
	var arr = this.dataArr;
	var selid = this.selectId;
	var str = "<table border=0 cellspacing='0' cellpadding='0' width='100%'><tr><td nowrap ><div class='"+this.bgCss+"'>";
	var frm = "";
	var i = 0;
	if (this.oneTimeUrl != "") {
		while(i<arr.length){
			if (this.oneTimeUrl.indexOf(arr[i][2]) != -1) {
				this.selectId = i;
				selid = i;
				break;
			}
			i++;
		}
		if (i >= arr.length) {
		//panel��û����oneTimeUrl���Ƶ�url
			this.oneTimeUrl = "";
		}
		i = 0;
	}
	try{
	while(i<arr.length){
		this.lastUrl   = "about:blank";
		str += "<div onclick='"+arr[i][1]+"' class='"+this.tabCss+"'>";
		str += this.setTab(i);
		str += "</div>";
		frm += ' <tr';
		if(i != selid )
			frm += ' style="display:none" ';
		else{
			this.lastDivId = 'divId_'+this.name+'_'+i;
			if (this.oneTimeUrl != ""){
				this.lastUrl   =  this.oneTimeUrl;
				this.oneTimeUrl = "";
			} else if(arr[i][2]){
				this.lastUrl   =  arr[i][2];
			}
		}
		frm += ' id="divId_'+this.name+'_'+i+'"><td nowrap  ';//divId_panel_0
		frm += ' class="panel_content">';
		if(arr[i][2]){//frame_panel_0
			frm +='	<iframe frameborder="0" scrolling="no" src="'+this.lastUrl+'" height="100%" width="100%" '
		       +'    name="frame_'+this.name+'_'+i+'" id="frame_'+this.name+'_'+i+'"></iframe> ';
			 arr[i][3] = 'frame_'+this.name+'_'+i;
		}
		frm += '</td></tr>';
		i++;
	}
	}catch(e){}
	frm += '</table>';
	this.dataArr = arr;
	//str += "<div background='"+golobal_image_path+"/panel_end.jpg' class='panel_end' id='tdId_panel_end'></div>";
	//str += "<div class='panel_line'>&nbsp;</div>";	
	str += "</div></td></tr>";	
	str += frm;
	this.beforeindex = this.selectId; //yushn 2008-03-04
	return str;
}

Panel.setTab = function(index) {
	var tab = "";
	inum = this.dataArr.length
	var str = this.dataArr[index][0]; 
	if(index == this.selectId) {
		tab += "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" >";
		tab += "<tr id=\""+this.name+".tabTrId"+index+"\" style=\"cursor:pointer\" onclick=\""+this.name+".click("+index+",true)\">";
		tab += "<td nowrap background = '"+this.oneImage1.src+"' height=\"23\" width=\"5\">&nbsp;</td>";//class=\"tab_selected_panel1\"
		tab += "<td nowrap background = '"+this.oneImage2.src+"' class='"+this.focusFont+"' id='"+this.name+"_tdId_panel_"+index+"'> &nbsp; "+str+" &nbsp; </td>";
		tab += "<td nowrap background = '"+this.oneImage3.src+"' width=\"5\">&nbsp;</td>";
		tab += "</tr>";
		tab += "</table>";
	}
	else {
		tab += "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" >";
		tab += "<tr id=\""+this.name+".tabTrId"+index+"\" style=\"cursor:pointer\" onclick=\""+this.name+".click("+index+",true)\">";
		if((index-1) == this.selectId)
			tab += "<td nowrap background=\""+this.twoImage2.src+"\" height=\"23\" width=\"5\">&nbsp;</td>";
		else
			tab += "<td nowrap background=\""+this.twoImage1.src+"\" height=\"23\" width=\"5\">&nbsp;</td>";
		tab += "<td nowrap background=\""+this.twoImage2.src+"\" class='"+this.blurFont+"' id='"+this.name+"_tdId_panel_"+index+"'> &nbsp; "+str+" &nbsp; </td>";
		if(index == (inum-1))
			tab += "<td nowrap background=\""+this.twoImage31.src+"\" width=\"5\"></td>";
		else
			tab += "<td nowrap background=\""+this.twoImage3.src+"\" width=\"5\"></td>";
		tab += "</tr>";
		tab += "</table>";
	}
	return tab;
	//document.write(tab);
	//document.close();
}

//�л�tab
Panel.switchTab = function(index){
	var tdList;
	var inum = this.dataArr.length;
	for(var i = 0; i < inum; i++) {
		if(i==index || document.getElementById(this.name+".tabTrId"+i)==null) 
			continue;
		tdList = document.getElementById(this.name+".tabTrId"+i).childNodes;
		if(i == (index+1))
			tdList[0].style.backgroundImage = "url("+this.twoImage2.src+")";
		else
			tdList[0].style.backgroundImage = "url("+this.twoImage1.src+")";
		tdList[1].className  = this.blurFont;
		//tdList[1].disabled   = true;
		tdList[1].style.backgroundImage = "url("+this.twoImage2.src+")";
		if(i == inum-1)
			tdList[2].style.backgroundImage = "url("+this.twoImage31.src+")";				
		else
			tdList[2].style.backgroundImage = "url("+this.twoImage3.src+")";
	}
	tdList = document.getElementById(this.name+".tabTrId"+index).childNodes;
	tdList[0].style.backgroundImage = "url("+this.oneImage1.src+")";
	tdList[1].style.backgroundImage = "url("+this.oneImage2.src+")";	
	tdList[2].style.backgroundImage = "url("+this.oneImage3.src+")";
	tdList[1].className  = this.focusFont;

	var frmNa = "frame_"+this.name+"_"+index;
	var divId = "divId_"+this.name+"_"+index;
	var frm  = document.getElementById(frmNa);
	if(frm)
		frm.parentNode.parentNode.style.display = "";
	if(this.lastDivId && divId != this.lastDivId){
		document.getElementById(this.lastDivId).style.display = "none";
	}
	this.lastDivId = divId;
	this.selectId = index;	
}
//�������iframe,ֱ���л�div
Panel.displayDiv = function(index){
	var divStr  = "divId_"+this.name+"_"+index;
	var divObj  = document.getElementById(divStr);
	divObj.style.display = "";		
	this.setContent(divObj,index)
	//this.lastDivId = divStr;
}

/* divObj tr�Ķ���divObj.childNodes[0]���������������
 * index  ��ǰѡ������
 */
Panel.setContent = function (divObj,index){
	divObj.childNodes[0].innerHTML = "��ǰѡ����Ϊ��";
}

/*
 * ����¼��������Լ�������������
 *
 * commitFlg�Ƿ��ύ���
 */
Panel.click = function(index,commitFlg) {
	//��֪��������ô�д��� �� �з������������ʵ�� ?
	try{
		if(!this.beforeClick(index))
			return;
		if(!this.selfClick(index))
			return;
		if(!this.isPass){
			if(!confirm(this.message))
				return;
		}
	}catch(e){}
	/*
	//�޸�����������ж�����
	try{
		//�����TABҳ���л�����ô��������Ȩ�޾Ͳ������
		top.document.frames["under"].document.frames["mainFrame"].document.getElementById("mustClearFormPermission").value="false";
	}catch(e){}
	*/
	this.switchTab(index);
	var url =  this.dataArr[index][2];
	if(url){
		this.displayFrame(index,url,commitFlg);			
	}else{
		this.displayDiv(index);
	}
	this.afterClick(index);
}

Panel.displayFrame = function (index,url,commitFlg){
	var frmNa = "frame_"+this.name+"_"+index;
	var divId = "divId_"+this.name+"_"+index;
	var frm  = document.getElementById(frmNa);	
	var formTemp = frames[frmNa].document.forms[0];
	if(this.reloadPage == index && formTemp.step && formTemp.step.value.indexOf('remove') == -1){
		//document.frames[frmNa].location.reload(true);
		//TODO �����ҳ���ύ���п��ܴ�������		
		frames[frmNa].FormUtils.post(formTemp, frames[frmNa].location.href);
	}else if(this.holdPage != index){
		frm.src = "about:blank";
		//open loading
		if(top.definedWin){
			top.definedWin.openLoading();
		}
		var inum = 0,newName = this.dataArr[index][0];newUrl = url;
		if(this.newPanel.length > 0 && this.newPanel[0] == index){
			inum    = this.newPanel[0];
			newName = this.newPanel[1] ? this.newPanel[1] : newName;
			newUrl  = this.newPanel[2] ? this.newPanel[2] : newUrl;
		}		
		if(document.frames(frmNa).CurrentPage && document.frames(frmNa).CurrentPage.query && commitFlg==true){
			//���Ϊ��ѯҳ��,���²�ѯ
			try{
				var searchDiv = frames[frmNa].document.getElementById("supplierQuery");
				if(searchDiv){
					this.afterSearchTermList[this.selectId] = new Array();
					this.saveSearchTerm(this.afterSearchTermList[this.selectId],searchDiv);
					this.setSearchTerm(this.beforeSearchTermList[this.selectId],searchDiv);
					this.ispanelclick = true;
				}
			}catch(e){}
			document.frames(frmNa).CurrentPage.query();
		}else{
			switch(index){
				case inum:
						frm.src = newUrl;
					break;
				default:
					frm.src = this.setUrl(index,newUrl);
					break;
			}
		}
	}
		
	if(this.disablePanel.length > 0){
		this.setDisable();
	}else
		this.setAble();
	//�������ø߶�
	Global.setHeight();
}

//����Tab�����֡�·����
Panel.setPanelProperies = function(arr){
	for(var i=0;i<arr.length;i++){
		if(arr[i]){
			this.dataArr[i][0] = arr[i][0];
			this.dataArr[i][2] = arr[i][2];
			document.getElementById(this.name+"_tdId_panel_"+i).innerHTML = " &nbsp; "+arr[i][0]+" &nbsp; ";
		}
	}
}

/* ��̬��չ���panel��ʱ�򣬼����Լ�url
 * ��������ڸ����Լ�ʹ�����ع�,
 * index �ǵ�ǰѡ���ţ���0��ʼ��
 * ������һ��Ĭ��ʵ�֣�Ҫ���б���ڵ�һ��tab,�б���Ψһ����Ϊ this.uuid
 *
 * ����Ϊ�գ���ʹ��ԭ����url
 * ����һ��url,��ͼ������url
 */
Panel.setUrl = function(index,url){
	var frmNa = this.listFrame;
	var beforefrmNa = "frame_"+this.name+"_"+this.beforeindex;
	try{
		//�п������frame����ȡ����������???����˵ʲô�����!!!
		var oid= frames[beforefrmNa].document.getElementsByName(this.uuid);
		//��ǰҳΪ����oidΪ�գ���ȡ�б�ҳoid
		if(oid.length==1 && (oid[0].value == null || oid[0].value == "" || oid[0].value == "undefined")){
			oid= frames[frmNa].document.getElementsByName(this.uuid);
		}
		if(oid.length && this.isUseOid){
			if(url.indexOf("?")>0)
				url += "&";
			else
				url += "?";
			
			//���û��ѡ�У���Ĭ��ȡ��һ�е�oidֵ
			//Ĭ��ȡֵ������,ע��
			var temp = null;
			for(var i=0;i<oid.length;i++){
				if(oid[i].selectedNo){
					temp = oid[i].selectedNo;
					break;
				}
			}
			//var temp = frames[frmNa].document.getElementById("detailIdsForPrintAll");
			
			//if(temp != null && temp.value != ""){
			if(temp == null || temp == "" || temp == "undefined"){
				temp = oid[0].value;
			}
			
			if(url.indexOf(this.uuid+"=")>0){
				if(Global.URLParams[this.uuid])
					return url;
				else{
					url = url.replace(this.uuid+"=",this.uuid+"="+temp);
				}
			}else{
				url += this.uuid+"="+temp;
			}
		}
	}catch(e){}
	return url;
}

//��ָ����Tab����Ϊ����
Panel.setPanelDisable = function(arr){
	arr = arr ? arr : this.disablePanel;
	var tab;
	for(var j=0;j<arr.length;j++){
		tab  = document.getElementById(this.name+".tabTrId"+arr[j]);
		tab.disabled = true;
	}
}
//������Tab����Ϊ ʹ��
Panel.setPanelAble = function(){
	var tab;
	for(var i=0;i<this.dataArr.length;i++){
		tab  = document.getElementById(this.name+".tabTrId"+i);
		tab.disabled = false;
	}
}

//���л�tab֮ǰ���Զ������
Panel.beforeClick = function(index){
	//alert(this.checktaglib);
	//alert(this.checkallvalue);
	if(this.checktaglib){
		return this.checkTaglibValues();
	}
	else if(this.checkallvalue){
		return this.checkValues();
	}else{return true;}
}
//���л�tab֮����Զ������
Panel.afterClick = function(index){
	this.beforeindex = index;
}

Panel.selfClick = function(index){
	return true;
}

Panel.movePage = function () {
	try{
		if(this.checktaglib){
			return this.checkTaglibValues();
		}
		else if(this.checkallvalue){
			return this.checkValues();
		}else{return true;}
	}catch(e){
		return true;
	}
}

//���formֵ�Ƿ�ı�
Panel.checkValues = function (temp){
	//var checkValues = new frames[beforefrmNa].document.CheckValues.ValuesObject("panelaaaaa");
	//alert(checkBalues.name);
	//checkValues.thisname();
	//alert(this.beforeFrameValues.length);
	if(this.beforeFrameValues.length==0){
		return true;
	}
	var beforefrmNa = "frame_"+this.name+"_"+this.beforeindex;
	var dfs = frames[beforefrmNa].document.forms;
	//��������form
	for(i=0;i<dfs.length;i++){
		var df = dfs[i].getElementsByTagName("*");
		//��������form�����ж���
		for(j=0;j<df.length;j++){
			/* �Կɱ༭�б���ɾ��λԪ������ */
			if(isDelFlg(df[j])){
				return this.confirmMessage(temp);
			}
			//���˵�hidden��,hidden�����ȶ�
			if(!(df[j].type == "hidden")){
				//tagNameΪ"IMG"����
				if(df[j].tagName == "IMG"){
					//�ȶ�src����
					if(df[j].src != this.beforeFrameValues[i][j]){
						return this.confirmMessage(temp);
					}
				}
				//tagNameΪ"INPUT"����
				else if(df[j].tagName == "INPUT" && df[j].type == "checkbox"){
					//�ȶ�checked����
					if(df[j].checked != this.beforeFrameValues[i][j]){
						//alert(df[j].checked+"///"+this.beforeFrameValues[i][j]);
						return this.confirmMessage(temp);
					}
				}
				//tagNameΪ"OPTION"����
				else if(df[j].tagName == "OPTION"){
					//�ȶ�value����
					if(df[j].value != this.beforeFrameValues[i][j]){
						//alert(df[j].value+"///"+this.beforeFrameValues[i][j]);
						return this.confirmMessage(temp);
					}
					/*ȥ��selectѡ����
					var selectTag = df[j];
					if(selectTag.getElementsByTagName("*").length != this.beforeFrameValues[i][j].length){
						return this.confirmMessage(temp);
					}
					for(var k = 0;0 < selectTag[k].length;k++){
						if(df[j].src != this.beforeFrameValues[i][j]){
							if(!confirm(this.message)){
								return false;
							}else{
								this.checkallvalue = false;
								return true;
							}
						}
					}*/
				}
				//������ֱ�ӱȶ�value����
				else if(df[j].name != undefined){
					if(df[j].value != this.beforeFrameValues[i][j]){
						//alert(df[j].name);
						//alert("beforeValue:"+df[j].value+"======"+this.beforeFrameValues[i][j]);
						return this.confirmMessage(temp);
					}
				}
			}
		}
	}
	this.checkallvalue = false;
	return true;
}

Panel.confirmMessage = function(temp){
if(temp){
	return false;
}else{
	if(!confirm(this.message)){
		return false;
	}else{
		this.checkallvalue = false;
		return true;
	}
}
}

//���formֵ�Ƿ�ı�,�˷����ѱ��ϳ�
Panel.checkTaglibValues = function (){
	//alert("begin");//�ȶԿ�ʼ
	var beforefrmNa = "frame_"+this.name+"_"+this.beforeindex;
	//��������form
	for(i=0;i<frames[beforefrmNa].document.forms.length;i++){
		//��������form������tag����
		for(j=0;j<frames[beforefrmNa].document.forms[i].length;j++){
			//�õ���hiddenValueDefaultΪ��׺�ı�ǩ�Զ����ʼhiddenֵ
			hiddenID = frames[beforefrmNa].document.forms[i].elements[j].name;
			if(!(hiddenID == null) && !(hiddenID.search("hiddenValueDefault") == -1)){
				name = hiddenID.substr(18,hiddenID.length);
				//�Ƚ�ҳ��tag��ʼֵ�뵱ǰֵ�Ƿ����
				if(frames[beforefrmNa].document.forms[i].elements[j].value != frames[beforefrmNa].document.getElementsByName(name)[0].value){
					if(!confirm(this.message)){
						return false;
					}else{this.checktaglib = false;return true;}
				}
			}
		}
	}
	this.checktaglib = false;
	//alert("end");//�ȶԽ���
	return true;
}

Panel.saveValues = function (index){
	var noncefrmNa = "frame_"+this.name+"_"+this.index;
	var noncefrm  = document.getElementById(noncefrmNa);
	var allformvaluelist = new Array();
	for(i=0;i<dfs.length;i++){
		var df = dfs[i];
		var formvaluelist = new Array();
		for(j=0;j<df.length;j++){
			formvaluelist[j] = dfs[i].elements[j].value;
		}
		allformvaluelist[i] = formvaluelist;
	}
}

Panel.getLineClassName = function (obj){
	if (obj.parentNode.tagName == "TR") {
		return obj.parentNode.className;
	} else if (obj.parentNode.tagName != "HTML") {
		return Panel.getLineClassName(obj.parentNode);
	} else {
		return "";
	}
}

Panel.saveSearchTerm = function(searchTermList,searchDiv){
	var searchTag = searchDiv.getElementsByTagName("*");
	for(i=0;i<searchTag.length;i++){
		if(searchTag[i].name && (searchTag[i].name).indexOf("conditions(") != -1){
			searchTermList[i] = searchTag[i].value;
		}
	}
}

Panel.setSearchTerm = function(searchTermList,searchDiv){
	var searchTag = searchDiv.getElementsByTagName("*");
	for(j=0;j<searchTag.length;j++){
		if(searchTag[j].name && (searchTag[j].name).indexOf("conditions(") != -1){
			searchTag[j].value = searchTermList[j];
		}
	}
}
