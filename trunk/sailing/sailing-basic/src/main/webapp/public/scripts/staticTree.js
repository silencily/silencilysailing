/**
 * ��ʽ���ز˵���
 * 
 * @author liuz
 * @version $Id: staticTree.js,v 1.1 2010/12/10 10:56:35 silencily Exp $

 * ʹ�ýڵ����:��û��ʹ�õĲ�����","Ԥ����ֵ������ "ϵͳ����,/zsu.htm,,,,,����������ϵͳ����"��
 * nodeLevel,nodeName,nodeUrl,target,nodeId,nodeType,noSelect,promptInfo,helpUrl,isLast,fathId,children
 * ��� / ���� / URL / Target / ID / ���� / �Ƿ��ѡ / ....	
 * 
 * (1) ���Ե�ѡ/��ѡ/��ѡ ѡ�񷵻�����/id 	
 * (2) Ӧ���� ϵͳ�˵� / ��֯�ṹ�� �� 
 * (3) ������չ�ڵ�ͼ��			
 * (4) ��ʼ��ʱ���Խ�ѡ�нڵ��չ��	 
 * (5) �ڵ�ȫ��չ������ȫ������	  
 * (6) ** �˵��������Զ����������ʽ
 * (7) ** 
 * 
 * 2005-01-12 update ����Ѿ����һ�����ӣ����μ��ر������ getClickNode ��Ҫͬ������ page_left.jsp
 * 2004-11-26 update ����selectCurrent���ԣ����������Ϊtrue������ֻѡ��ǰ�ڵ㣬��selectDeptBox�н������޸ģ����
 * 2004-11-23 update �Ż�ȫѡ�ڵ㣬�ͷ��ؽڵ��ٶ�
 * 2004-11-12 update switchDepartAll() ���� �Ը��ڵ���չ�任 
 */

if (StaticTree == null) 
{
	var StaticTree = {};
}

StaticTree.DepartTree = function(treename,imagePath){
	//Data member 
	this.TreeName  = treename
	this.Address   = 'span_menu';//��ʾ�˵�λ�õĶ�����
	this.DeptList  = new Array();//�ö������������ṹ
	this.LinkList  = new Array();//��Ź����ڵ��� [���][�ڵ�����]
	this.TreeType  = 0           //������ Ĭ��0 / ��ѡ1 /��ѡ2
	this.InitID    = 0           //��ʼ�����ڵ�
	this.InitString= '';         //��ʼֵ
	this.IsExtend  = false;      //�Ƿ���չ 
	this.IsScroll  = false;      //�Ƿ���ֹ�����
	this.IsHelp    = false;      //�Ƿ���ð�������
	this.IsDeploy  = false;      //�Ƿ������ʱ��չ���¼��ڵ�
	this.SelectId  = -1;		 //�ϴ�ѡ�нڵ�
	this.PageHeight= 0;
	this.OpenFlag  = 'display';
	this.returnList= new Array();//��ŷ��ض������飬�Ż�ѡ��
	this.SelectSon = false;      //ֻѡ���ӽڵ㣨���ڵ���࣬ѡ���ӽڵ���ӽڵ㽫Ӱ���ٶȣ�
    this.selectCurrent = false;  //ֻѡ��ǰ�ڵ�
	this.NotSelect = '';		 //��Ҫ��ʼ������ѡ��Ľڵ㣬��Ϊ�����н��ýڵ�������ӽڵ��£�������ѭ��
	this.noSelectLevel = 1000;   //private
	//this.DefinedID = 0         //׼������Զ��忪ʼ�ڵ�
	this.menuHeight= document.body.offsetHeight-120; //��ʾ�˵��߶�
	this.menuWidth = document.body.offsetWidth;  //��ʾ�˵����
	this.menuTop   = 58;
	this.menuLeft  = 8;
	this.menuRight = 15;
	//image 
	this.imagePath = imagePath ? imagePath : ContextInfo.contextPath+"/image/tree/column/";
	this.treeMid      = new Image();
	this.treeMid.src  = this.imagePath + "tree_mid.gif";
	this.treeEnd      = new Image();
	this.treeEnd.src  = this.imagePath + "tree_end.gif";
	this.treeOpenM    = new Image();
	this.treeOpenM.src= this.imagePath + "tree_t_mid.gif";
	this.treeOpenE    = new Image();
	this.treeOpenE.src= this.imagePath + "tree_t_end.gif";
	this.treeClseM    = new Image();
	this.treeClseM.src= this.imagePath + "tree_x_mid.gif";
	this.treeClseE    = new Image();
	this.treeClseE.src= this.imagePath + "tree_x_end.gif";
	this.treeBgImg    = new Image();
	this.treeBgImg.src= this.imagePath + "tree_bg.gif";
	this.treeTransImg    = new Image();
	this.treeTransImg.src= this.imagePath + "tree_trans.gif";
	this.picArray       = new Array();
	this.picArray[0]    = new Image();
	this.picArray[0].src= this.imagePath + "pic_root.gif";//���ڵ�ͼ�� 
	this.picArray[1]    = new Image();
	this.picArray[1].src= this.imagePath + "pic_dept.gif";//
	this.picArray[2]    = new Image();
	this.picArray[2].src= this.imagePath + "pic_role.gif";// 
	this.picArray[3]    = new Image();
	this.picArray[3].src= this.imagePath + "pic_user.gif";// 
	this.scrolldown = "arrowdown.gif";
	this.scrollup   = "arrowup.gif"
	//css
	this.focusCss   = "font_master";//���������Ч��;
	this.blurCss    = "";

	//currently Node
	this.currentId     = 0;
    this.currentValue  = '';
	this.currentName   = '';

	//method
	this.add        = addDepartChild;
	this.addArray   = addDepartArray;
	this.filter     = filterDepartChild;
	this.display    = displayDepartChild;
	this.showImage  = showNodeImage;
	this.showFlag   = showNodeFlagImage;
	this.clickNode  = clickDepartChild;
	this.switchTab  = switchDepartTab
	this.switchAll  = switchDepartAll;
	this.extendTab  = extendDepartTree;
	this.node       = departNode;
	this.linkNode   = linkNode;
	this.getTreeArr = getNumTreeChild
	this.setSelect  = setDepartSelect;
	this.selectBox  = selDepartBox;
	this.setClass   = setTableClass
	this.getObject  = getDepartObject

	this.showButton = showMenuButton
	this.dispScroll = DisplayMenuScroll;
	this.getHeight  = getInitMenuHeight;
	this.getScroll  = getMenuScrolling;
	this.getClip    = getClipMenu;
	this.getClipValue= getClipValue ;
	this.selScroll  = ArrowSelected
	this.clickScroll= ArrowClicked;
	//
	this.getReturn  = getDepartReturnValue //���� ��ѡ/��ѡ�е�ֵ
	this.getSelect  = getClickNode         //����ڵ���¼������Զ���ͬ����������
	this.setExtend  = setExtendNode
}

//����ĳ���ڵ�չ�����ڵ�
function setExtendNode(inum){
	while(inum != -1){
		if(this.getObject('tab',inum+1) && this.getObject('tab',inum+1).style.display == 'none')
			this.switchTab(inum)
		inum = parseInt(this.DeptList[inum].fathId)
	}
}

//�õ�ĳ���ڵ�
function getClickNode(inum){
	return true;
	/*alert('node Name : '+ this.DeptList[inum].nodeName +'\n' +
		  'node Id   : '+ this.DeptList[inum].nodeId   +'\n' +
		  'node Level: '+ this.DeptList[inum].nodeLevel+'\n' +
		  'node Type : '+ this.DeptList[inum].nodeType +'\n' )
	*/
}

//�õ����������б�
function getDepartReturnValue()
{		
	/*var returnArray= new Array();
	var inum;
	var j = 0; 
	for(var i=0;i<this.getObject('checkbox').length;i++)
	{
		if(this.getObject('checkbox')[i].checked)
		{
			inum = this.getObject('checkbox')[i].value;
			//if(!this.DeptList[inum].noSelect)
				returnArray[j++] = this.DeptList[inum];
		}
	}*/
	return this.returnList ;
}

//��ʼ�ڵ�
function departNode(arr)
{
	var  i =0;
	this.nodeLevel= parseInt(arr[i++],10); //�ڵ���
	this.nodeName = arr[i++]; //�ڵ�����
	this.nodeUrl  = arr[i++]; //�ڵ��Ӧurl
	this.target   = arr[i++]; //�ڵ�urlָ���Ŀ�괰����
	this.nodeId   = arr[i++]; //�ڵ�id
	this.nodeType = arr[i++]; //�ڵ�����
	this.noSelect = arr[i++]; //�Ƿ����ѡ��
	this.promptInfo= arr[i++];//���ı���ǰ�ӿ�
	this.helpUrl  = arr[i++]; //��Ӧhelp url
	this.fontCss  = arr[i++]; //������չĳ��ڵ��ò�ͬ��ɫ��ʶ����
	this.isLast   = arr[i++]; //�Ƿ�β�ڵ�
	this.fathId   = arr[i++]; //��Ӧ���ڵ�����DeptList�����
	this.children = arr[i++]; //�����ӽڵ�����string,����á�,���ָ�
	this.selfNum  = arr[i++];
}

//���ӽڵ�
function linkNode(arr)
{
	var  i =0;
	this.fathId  = arguments[i++]; 
	this.level   = arguments[i++];
	this.brother = arguments[i++];
}

//�������ڵ�����
function addDepartArray(arr)
{
	if(arr.length>2000 && !confirm('���ݽڵ�̫�࣬������ɻ������л�����ȷʵҪ������'))
		return
	if(arr.length>500 && this.TreeType == 2)
		this.SelectSon = true;
	for(var i=0;i<arr.length;i++)
	{
		var temp_arr     = arr[i].split(',');
		if(temp_arr[0]){
			var deptobj     = this.filter(temp_arr); //new this.node(temp_arr);
			this.DeptList[i]= deptobj;
		}else{
			alert(arr[i]+'�ڵ����ݲ���ȷ��');
			break;
		}
		//alert("i = "+i+" | fathid: "+this.DeptList[i].nodeLevel);
	}
}

//���������ӽڵ�
function addDepartChild(str)
{
	if(str == '')
		return false;
	var arr_str = str.split(',');
	var infoobj ;	
	if(arr_str.length > 0 && arr_str[0] != ""){
		infoobj = this.filter(arr_str);
	}else{
		return false;
	}	
	this.DeptList[this.DeptList.length] = infoobj;  
}
//���˻��ߴ���ڵ�
//�Բ�����У�������������
function filterDepartChild(arr_str){
	var infoobj;
	if(this.DeptList.length>0){
		var fatLevel = this.DeptList[this.DeptList.length-1].nodeLevel+1
		if((fatLevel)<arr_str[0]){
			arr_str[0] = fatLevel+1;
		}
	}
	//�ýڵ��ӽڵ㶼����ѡ��֮�ⶼ����
	if(arr_str[0]>this.noSelectLevel){		
		infoobj = new this.node(arr_str);
		infoobj.noSelect = true;
	}else{
		this.noSelectLevel = 1000;
		if(this.NotSelect != '' && this.NotSelect == arr_str[4]){
			infoobj = new this.node(arr_str);
			infoobj.noSelect = true;
			this.noSelectLevel = infoobj.nodeLevel;
		}else{
			infoobj = new this.node(arr_str);
		}	
	}
	return infoobj;
}

//---ͼ��任
function extendDepartTree(sid,imgid,flag)
{	
	//try{
		var subment     = this.getObject('tab',sid);
		if(!subment)
			return;
		var local_image = this.treeOpenM.src		
		var imgbtn      = this.getObject('img',imgid);
		var imgbtn2      = this.getObject('imgtab',imgid);
		var iflag       = 0;
		if(flag==''){
			if(subment.style.display=="none")
				iflag=1;
			else
				iflag=0;
		}else if(flag=='none')
			iflag=0;
		else if(flag=='display')	
			iflag=1;
			
		if(iflag!=0){
			subment.style.display='';
			if(this.DeptList[sid-1].isLast==-1)
					local_image = this.treeOpenE.src;
			if(imgbtn)
				imgbtn.src= local_image;
			if(imgbtn2)
				imgbtn2.src= this.picArray[2].src;
		}else{
			subment.style.display="none";
			if(this.DeptList[sid-1].isLast==-1)
				local_image = this.treeClseE.src;
			else 
				local_image = this.treeClseM.src;
			if(imgbtn)
				imgbtn.src=local_image;
			if(imgbtn2)
				imgbtn2.src= this.picArray[0].src;
		}
	//}catch(e){alert(e)}
}

//�������νڵ㼯�ϵ�����
function getNumTreeChild()
{
	var ilen     = 0;
	var prelevel = -1;
	var temp_self= 0;
	var temp_str = '';
	var fath_id  = 0; 

	for(var i=0;i<this.DeptList.length;i++)
	{
		this.DeptList[i].selfNum = i;
		var level = parseInt(this.DeptList[i].nodeLevel);
		this.DeptList[i].isLast=-1;
		//
		if(level > prelevel)//��ʼ���½ڵ�
		{	
			if(i==0)//////
				 fath_id = -1;
			else
				 fath_id = i-1;	
			this.DeptList[i].fathId  = fath_id	
			if(fath_id == -1)
				this.DeptList[0].children = i+"";
			else
				this.DeptList[fath_id].children = i+""; 
			this.LinkList[ilen] = new this.linkNode(fath_id,level,i);
			temp_self = ilen ;
			prelevel  = level 
			ilen ++ ;
		}
		else if( level == prelevel)//��ǰ��û���ӽڵ�ĸ���
		{			
			this.LinkList[temp_self].level = level;
			temp_str = this.LinkList[temp_self].brother + '';			
			if(temp_str.lastIndexOf(',')>=0) 
				temp_str=temp_str.substring(0,temp_str.lastIndexOf(','))+','+i;
			else 
				temp_str= i;
			this.LinkList[temp_self].brother = temp_str;
			this.DeptList[i-1].isLast = 0;			 
			this.DeptList[i].fathId   = this.DeptList[i-1].fathId
			var ffid = this.DeptList[i-1].fathId;
			if(ffid >=0)
				this.DeptList[ffid].children += ','+i 			
		}else 
		{
			temp_str = this.LinkList[temp_self].brother + '';
			if(temp_str.lastIndexOf(',')>=0)
				temp_str=temp_str.substring(0,temp_str.lastIndexOf(','));
			else 
				temp_str='';
			this.LinkList[temp_self].brother = temp_str;			
			for(var j= this.LinkList.length-1;j>=0;j--)
			{
				if(this.LinkList[j].level == level)
				{
					//alert("brother : "+ i+ "|"+ this.LinkList[j].brother)					
					this.LinkList[j].brother = this.LinkList[j].brother + ","
					//����β�ڵ���Ϣ
					var temp1=this.LinkList[j].brother.split(',');
					var templen=temp1[temp1.length-2];
					this.DeptList[templen].isLast =0;
					this.DeptList[i].fathId = this.DeptList[templen].fathId; 
					var ffid =  parseInt(this.DeptList[templen].fathId) ;
					if(ffid>=0)
						this.DeptList[ffid].children += ','+i;
					//
					this.LinkList[j].brother += i;
					temp_self   = j;
					prelevel = level;
					break;
				}
			}			
		}
	}
}

//--ȫ��չ����������
function switchDepartAll(temp)
{
	var temp_lists='';
	var temp_child=new Array();
	var temp_ch;
	/*--�Խڵ���չ�任--*/
	if(typeof temp == 'undefined' ){ 
		temp          = this.OpenFlag;
		if(this.OpenFlag == 'none')
			this.OpenFlag = 'display';
		else
			this.OpenFlag = 'none';
	}
	for(var i1=0;i1<this.LinkList.length;i1++)
	{		
		temp_lists=this.LinkList[i1].brother+",*";
		temp_child=temp_lists.split(',');
		//alert(temp_child);
		for(var i2=0;i2<temp_child.length;i2++)
		{			
			if(temp_child[i2]!='*'&&temp_child[i2]!='')
			{	
				temp_ch = parseInt(temp_child[i2])
				if(temp_ch > this.DeptList.length-2) 
					break;
				//if(this.LinkList[i1].level != 0)//��һ��β�����
					this.extendTab(temp_ch+1,temp_ch,temp);	
			}
		}		
	}
	this.getHeight()/////////
}

//--�������ͽṹ��
function switchDepartTab(inum){
	//��������������������Χ��
	var temp_child=new Array();
	var inums=","+inum+",";
	var temp_lists='';
	for(var i1=0;i1<this.LinkList.length;i1++)
	{			
		if(!this.LinkList[i1].brother && this.LinkList[i1].brother != 0)
			continue;
		temp_lists="*,"+this.LinkList[i1].brother+",*";
		if(temp_lists.indexOf(inums)!=-1)
		{
			temp_child=temp_lists.split(',');
			for(var i2=0;i2<temp_child.length;i2++)
			{
				if(temp_child[i2]==inum){
					this.extendTab((inum+1),inum,'');

				}else if(temp_child[i2]!='*'&&this.IsExtend)
				{	
					if(temp_child[i2]<this.DeptList.length-1)
						this.extendTab((parseInt(temp_child[i2])+1),temp_child[i2],'none');					
				}
			}
			break;
		}
	}
	this.getHeight()/////////
}

//����¼�
function clickDepartChild(inum)
{
	if(this.DeptList[inum].noSelect)
		return ;
	if(!this.getSelect(inum))
		return;
	/****************** add by huxf for solving panel's initialization **********************/
	 // nothing happends if the root node be clicked
	 /*
	 if (inum >= 0 && typeof(document.getElementById("panel.tabTrId0")) != undefined) {
	     // call the first tab's click method
	     document.getElementById("panel.tabTrId0").click();
	    }
	    */
	if (inum >= 0 && panel) {
	     // call the first tab's click method
		panel.click(0);
	}
	 /****************** add by huxf for solving panel's initialization **********************/
	if(this.TreeType == 2)
	{
		if(this.getObject('box',inum).checked )
		   this.getObject('box',inum).checked = false;			
		else
		   this.getObject('box',inum).checked = true;
	}
	/**-- Ϊ�˶�̬��ʾ��ǰλ�����ӵĴ���ű� Start --*/
/*	var userAgent = navigator.userAgent.toLowerCase();
	var is_webtv = userAgent.indexOf('webtv') != -1;
	var is_kon = userAgent.indexOf('konqueror') != -1;
	var is_mac = userAgent.indexOf('mac') != -1;
	var is_saf = userAgent.indexOf('applewebkit') != -1 || navigator.vendor == 'Apple Computer, Inc.';
	var is_opera = userAgent.indexOf('opera') != -1 && opera.version();
	var is_moz = (navigator.product == 'Gecko' && !is_saf) && userAgent.substr(userAgent.indexOf('firefox') + 8, 3);
	var is_ns = userAgent.indexOf('compatible') == -1 && userAgent.indexOf('mozilla') != -1 && !is_opera && !is_webtv && !is_saf;
	var is_ie = (userAgent.indexOf('msie') != -1 && !is_opera && !is_saf && !is_webtv) && userAgent.substr(userAgent.indexOf('msie') + 5, 3);
	var iselect = inum;
	var lvlName = "";
	for (;;){
		var node = this.DeptList[iselect];
		if (typeof node != 'object'){
			break;
		}
		if (lvlName == ""){
			lvlName = node.nodeName;
		}else{
			lvlName = node.nodeName + "\\" + lvlName;
		}
		iselect = node.fathId;
	}
//	window.parent.topFrame.document.getElementById("location").innerHTML = lvlName;	
top.document.getElementById("location").innerHTML = lvlName;*/
	/**-- Ϊ�˶�̬��ʾ��ǰλ�����ӵĴ���ű� End --*/
	this.selectBox(inum);
}

//�õ��ڵ����
function getDepartObject(txt,inum){
	try{
		if(inum || inum == 0){
			return document.getElementById(this.TreeName + '_' + txt + '_' + inum) ;
		}else{
			return document.getElementById(this.TreeName + '_' + txt) ;
		}
	}catch(e){}
}

//ѡ����������
function selDepartBox(temp)
{
	if(this.TreeType == 1)//
	{
		if(this.DeptList[temp].noSelect)
			return
		if(this.SelectId != -1 && this.getObject('box',this.SelectId).checked)
		{
			this.setClass(this.SelectId,false);
			this.returnList = null;
		}
		if(this.SelectId != temp )
		{
			this.SelectId = temp ;
			this.setClass(temp,true);
			this.returnList = new Array(this.DeptList[temp]);//
		}else
		{
			this.SelectId = -1 ;
			this.setClass(temp,false);
			this.returnList = null;
		}
	}else if(this.TreeType == 2)//
	{
		if(this.DeptList[temp].noSelect)
			return ;
		if( this.getObject('box',temp).checked)
		{
			//
			if(this.DeptList[temp].children && this.selectCurrent == false){
				var child = this.DeptList[temp].children.split(',');			
				for(var i=0;i<child.length;i++)
				{		
					if(child[i]!= 0){
						if(this.DeptList[parseInt(child[i])].noSelect)
							break ;					
						this.setClass(child[i],true);
						//if(!this.SelectSon)
						this.selectBox(child[i]);
					}
				}
			}
			this.setClass(temp,true);
			//--
			var flag = true;
			for(var i=0;i<this.returnList.length;i++){
				if(this.returnList[i] && this.returnList[i].nodeId == this.DeptList[temp].nodeId){
					flag =false;
					break;
				}
			}
			if(flag)
				this.returnList[this.returnList.length] = this.DeptList[temp];
		}
		else{
			/* //�������ڵ��Ƿ�ѡ��
			var fathId = this.DeptList[temp].fathId
			while(this.DeptList[fathId] && !this.DeptList[fathId].noSelect){
				this.setClass(fathId,false);
				fathId = this.DeptList[fathId].fathId;
				if(fathId == 0 )
					break;
			}
			*/
			if(this.DeptList[temp].children && this.selectCurrent == false ){
				var child = this.DeptList[temp].children.split(',');			
				for(var i=0;i<child.length;i++)
				{	
					if(child[i]!= 0){
						if(this.DeptList[parseInt(child[i])].noSelect)
							break ;
						this.setClass(child[i],false);
						//if(!this.SelectSon)
							this.selectBox(child[i]);
					}
				}
			}
			this.setClass(temp,false);
			//--
			for(var i=0;i<this.returnList.length;i++){
				if(this.returnList[i] && this.returnList[i].nodeId == this.DeptList[temp].nodeId){
					//this.returnList[i] = null ;
					this.returnList.splice(i,1);
				}
			}
		}
	}else 
	{
		if(this.SelectId != -1 && this.SelectId != temp )
			this.setClass(this.SelectId,false,true) ;
		if(temp != this.SelectId)
		{			
			this.setClass(temp,true,true);
		}	
		this.SelectId = temp;
	}
}
//���ñ��
function setTableClass(inum,temp,flag)
{
	try{
		if(temp)
		{	
			if(!flag)
				this.getObject('box',inum).checked = true;
			this.getObject('a',inum).className  = this.focusCss ;
		}
		else{
			if(!flag)
				this.getObject('box',inum).checked = false;
			if(this.DeptList[inum].fontCss)
				this.getObject('a',inum).className  = this.DeptList[inum].fontCss ;
			else
				this.getObject('a',inum).className  = this.blurCss ;
		}
	}catch(e){}
}

//�ڵ�ѡ��
function setDepartSelect(inum)
{
	/*for(var i=0;i<arr.length;i++)
	{
		var inum = parseInt(arr[i])

		this.setSelect(
	}*/
	inum = this.DeptList[inum].fathId;
	if(inum != 0 && inum != -1)
	{
		this.extendTab(inum+1,inum,'display');
		this.setSelect(inum);
	}
}

//--��ʾ�˵���
function displayDepartChild()
{
	this.getTreeArr();//����β�ڵ���Ϣ
	var init_num = 0;
	var init_txt = '<table width="100%"  border="0" cellpadding="0"  cellspacing="0" class="css_menu_depart">';	
	var tlen     = 0;
	for(var i=0;i<this.DeptList.length;i++)
	{		
		var name_str  = '';
		var img_str   = '';
		var click_str = '';	
		var tdClass   = '';
		var checked     = '';
		var returnFlag = "true";
		if(this.DeptList[i].fontCss)
			tdClass = this.DeptList[i].fontCss;
		var bgimage   = 'background ="'+ this.treeBgImg.src +'"';
		var bgimage1  = 'background ="'+ this.treeBgImg.src +'"';
		var transimage  = '<img src="'+ this.treeTransImg.src +'" width="15">';
		var promptInfo = this.DeptList[i].nodeName;
		if(this.DeptList[i].promptInfo)
			promptInfo = this.DeptList[i].promptInfo;

		var ifather=parseInt(this.DeptList[i].fathId);      //���ڵ��Ƿ�β�ڵ�	
		if(ifather != -1)
		{
			if(this.DeptList[ifather].isLast == -1)
				bgimage = '';
			var gfather=parseInt(this.DeptList[ifather].fathId);//�游
			if( gfather ==-1 || this.DeptList[gfather].isLast == -1)
				bgimage1 = '';
		}else
		{	bgimage  = '';
		    bgimage1 = '';
		}
		//
		if(this.InitString.indexOf(','+this.DeptList[i].nodeId+',')!=-1 ){
			if(this.SelectId == -1 || this.TreeType == 2){
				checked          = 'checked';
				tdClass          = this.focusCss ;
				this.SelectId    = i;
				this.returnList[this.returnList.length] = this.DeptList[i];
			}
		}
		/*----*/
		switch(this.TreeType){
			case 0:
				var url = this.DeptList[i].nodeUrl;
				if(this.IsHelp){
					url = this.DeptList[i].helpUrl;
					this.DeptList[i].nodeType = (parseInt(this.DeptList[i].nodeLevel)+1).toString()
				}
				if(url){
					var targetName = this.DeptList[i].target ? this.DeptList[i].target : 'mainFrame';	
					name_str = '<a onFocus="if(this.blur)this.blur()" href="'+url+'" onclick="return '+this.TreeName+'.clickNode('+i+','+((url == '#')?'false':'true')+')'
					if(this.IsDeploy)
						name_str += ';'+this.TreeName+'.switchTab('+(i)+')'
					if(url == '#')
						name_str += ';return false ';
					//�ͻ�Ҫ��ȡ��tips����ʾ��2008/1/8�����¸��޸ģ����ǰ
					//name_str += '" id="'+this.TreeName+'_a_'+i+'" name="'+this.TreeName+i+'"  title="'+promptInfo+'" class="'+tdClass+'"';
					//�����
					name_str += '" id="'+this.TreeName+'_a_'+i+'" name="'+this.TreeName+i+'"  class="'+tdClass+'"';
					//////////////////////////////////////////////////////////////////////////////////////////
					if(this.DeptList[i].nodeUrl != '#')
						name_str += ' target="'+targetName+'" '
					name_str += ' >'+this.DeptList[i].nodeName+'</a>'
				}else
					name_str = '<a name="'+this.TreeName+i+'"></a>'+this.DeptList[i].nodeName; 
				break;
			default : //type 1 | type 2 
			   var temp_nodeName = '';
				temp_nodeName = ' <a onFocus="if(this.blur)this.blur()" href="#" class="'+tdClass+'" onclick="'+this.TreeName+'.clickNode('+i+');return false" ';
				//�ͻ�Ҫ��ȡ��tips����ʾ��2008/1/8�����¸��޸ģ����ǰ
				//temp_nodeName +=' id="'+this.TreeName+'_a_'+i+'" name="'+this.TreeName+i+'" title="'+promptInfo+'">'+ this.DeptList[i].nodeName + '</a>';
				//�����
				temp_nodeName +=' id="'+this.TreeName+'_a_'+i+'" name="'+this.TreeName+i +'">'+ this.DeptList[i].nodeName + '</a>';
				//////////////////////////////////////////////////////////////////////////////////////////
				if(this.DeptList[i].noSelect ) // && this.TreeType == 1
					name_str = temp_nodeName
				else 	
				{
					name_str='<input type="checkbox" value="'+i+'" name="'+this.TreeName+'_checkbox"  id="'+this.TreeName+'_box_'+i+'" onclick="'+this.TreeName+'.selectBox('+i+')" ';
					name_str += checked+'>'+ temp_nodeName  ;
				}
				break;
		}

		img_str  = this.showImage(i);
		img_str += this.showFlag(i);
		img_str  = img_str+name_str;
		if( init_num == parseInt(this.DeptList[i].nodeLevel) )
		{
			if(this.DeptList[i].nodeLevel==0)//������ʱȥ��ǰ�������
				init_txt+='<tr  id="'+this.TreeName+'_tab_'+(i)+'"><td nowrap id="'+this.TreeName+'_tdId_'+i+'">'+img_str+' \n </td></tr>';
			else
				init_txt+='<tr  id="'+this.TreeName+'_tab_'+(i)+'"><td width="15" '+bgimage+' height="22">'+transimage+'</td><td nowrap id="'+this.TreeName+'_tdId_'+i+'">'+img_str+' \n </td></tr>';
		}
		else if( parseInt(this.DeptList[i].nodeLevel) > init_num)
		{	
			if(init_num==0)// 
				init_txt+='<tr id="'+this.TreeName+'_tab_'+(i)+'"><td>';
			else
				init_txt+='<tr id="'+this.TreeName+'_tab_'+(i)+'" style="display:none"><td '+bgimage1+' width="15">'+transimage+'</td><td>';
			init_txt+='<table width="100%"  border="0" cellpadding="0"  cellspacing="0"><tr><td width="15" height="22" '+bgimage+'>'+transimage+'</td><td nowrap id="'+this.TreeName+'_tdId_'+i+'">'+img_str+' \n </td></tr>';
		}
		else if(parseInt(this.DeptList[i].nodeLevel) < init_num)
		{
			for(var j=0;j<init_num-parseInt(this.DeptList[i].nodeLevel);j++)
				init_txt+='</table></td></tr>';

			if(parseInt(this.DeptList[i].nodeLevel) == 0)//
				init_txt+='<tr id="'+this.TreeName+'_tab_'+(i)+'" ><td nowrap id="'+this.TreeName+'_tdId_'+i+'" >'+img_str+' \n </td></tr>';
			else				
				init_txt+='<tr id="'+this.TreeName+'_tab_'+(i)+'" ><td width="15" height="22" '+bgimage+' >'+transimage+'</td><td nowrap id="'+this.TreeName+'_tdId_'+i+'" >'+img_str+' \n </td></tr>';
		}
		init_num= parseInt(this.DeptList[i].nodeLevel);
	}
	for(var j=0;j<init_num;j++)
		init_txt+='</table></td></tr></table>';
	//alert(init_txt)
	document.getElementById(this.Address).innerHTML = init_txt ; 
	//
	//if(this.SelectId != -1)
	//	this.setSelect(this.SelectId);
	for(var i=0;i<this.returnList.length;i++){
		this.setSelect(this.returnList[i].selfNum);
	}
	if(this.IsScroll)
	{
		this.showButton();
		this.getHeight();
	}
}

//��ʾ�ڵ������ͼƬ
function showNodeImage(i){
	var img_str;
	if(this.DeptList.length>(i+1))
	{	
		if(parseInt(this.DeptList[i+1].nodeLevel) > parseInt(this.DeptList[i].nodeLevel))
		{	
			click_str='onclick="'+this.TreeName+'.switchTab('+(i)+')"';
			if( this.DeptList[i].fathId == -1 && this.DeptList[i].isLast == -1) 
				img_str = '<img src = "'+ this.treeOpenE.src +'" '
			else if( this.DeptList[i].fathId == -1 && this.DeptList[i].isLast != -1)
				img_str = '<img src = "'+ this.treeOpenM.src +'" '
			else if( this.DeptList[i].isLast == -1)
				img_str = '<img src = "'+ this.treeClseE.src +'" ';			
			else
				img_str = '<img src = "'+ this.treeClseM.src +'" ';
			img_str += ' " align="absmiddle"  id="'+this.TreeName+'_img_'+i+'" '+click_str+' style="cursor:pointer" title="�����˵�">'; 
		}
		else  if(parseInt(this.DeptList[i+1].nodeLevel) < parseInt(this.DeptList[i].nodeLevel))
			img_str='<img src="'+ this.treeEnd.src +'"  align="absmiddle" >';
		else  
			img_str='<img src="'+ this.treeMid.src + '" align="absmiddle" >';
	}else   
		img_str='<img src="'+ this.treeEnd.src +'" align="absmiddle" >';
	return img_str;
}
//��ʾ�ڵ��ͼƬ�������ṩ����չʹ��
function showNodeFlagImage(i){
		var img_str;
	if(this.DeptList.length>(i+1))
	{	
		if(parseInt(this.DeptList[i+1].nodeLevel) > parseInt(this.DeptList[i].nodeLevel))
		{	
			click_str='onclick="'+this.TreeName+'.switchTab('+(i)+')"';
			if( this.DeptList[i].fathId == -1 && this.DeptList[i].isLast == -1) 
				img_str = '<img src = "'+ this.picArray[2].src +'" '
			else if( this.DeptList[i].fathId == -1 && this.DeptList[i].isLast != -1)
				img_str = '<img src = "'+ this.picArray[2].src +'" '
			else if( this.DeptList[i].isLast == -1)
				img_str = '<img src = "'+ this.picArray[0].src +'" ';			
			else
				img_str = '<img src = "'+ this.picArray[0].src +'" ';
			img_str += ' " align="absmiddle"  id="'+this.TreeName+'_imgtab_'+i+'" '+click_str+'>'; 
		}
		else  
			img_str='<img src="'+ this.picArray[3].src + '" align="absmiddle" >';
	}else   
		img_str='<img src="'+ this.picArray[3].src +'" align="absmiddle" >';
	return img_str;
	
	/*var img_str = "";
	var ii = this.DeptList[i].nodeType != "" ? parseInt(this.DeptList[i].nodeType) : 99;
	if(this.picArray[ii])
		img_str = '<img src=" '+ this.picArray[ii].src +'"  align="absmiddle" >';
	return img_str;*/
}

//===================ģ�����������==============

//--��ʾ������ť
var global_scroll_flag = '';
function showMenuButton()
{	
	var str="<div id='divId_imgbtn' style='position:absolute;top:"+this.menuTop+";left:"+this.menuLeft+";width:94%;z-index:110;visibility:hidden;clip:rect(0 auto auto 0)'>"+ //"+this.TreeName+".clickScroll(this) onMouseDown='"+this.TreeName+".clickScroll(this)' onMouseOver='OverArrow(this)' onMouseOut='OutArrow(this)'
	"<img alt='���¹���' onmousedown='global_scroll_flag = setInterval(\""+this.TreeName+".selScroll(	1)\",1)' onMouseup='clearInterval(global_scroll_flag)'  id='imgbtnId_down' " +
	"src='"+this.imagePath + this.scrolldown + "' style='position:absolute;top:"+this.menuHeight+";right:"+this.menuRight+";cursor:pointer;visibility:hidden;z-index:500'>"+
	"<img alt='���Ϲ���' onmousedown='global_scroll_flag = setInterval(\""+this.TreeName+".selScroll(2)\",-1)' onMouseup='clearInterval(global_scroll_flag)'   id='imgbtnId_up' " +
	"src='"+this.imagePath + this.scrollup + "' style='position:absolute;top:0;right:"+this.menuRight+";cursor:pointer;visibility:hidden;z-index:500'>"+
	"</div>";
	document.write(str);
	//��ť��λ
	//document.all["imgbtnId_down"].style.pixelTop=this.menuHeight;//this.menuTop+ 108 document.all["divId_imgbtn"].style.pixelTop
}

//�Զ����ڲ˵��߶�
function getInitMenuHeight()
{	
	if(this.IsScroll)
	{
		if(this.PageHeight!=this.menuHeight)
		{
			var clip = this.getClipValue(this.Address);
			var mwidth=this.menuWidth-20;
			if(mwidth <200)//--�����ַ��ض�
				mwidth = 200;
			var mheight=this.menuHeight;//105
			document.getElementById(this.Address).style.clip=clip='rect('+clip[1]+' '+mwidth+' '+(parseInt(clip[1])+mheight)+' '+clip[4]+')';
			this.PageHeight = this.menuHeight;
		}
		this.dispScroll()
	}
}
//�ж��Ƿ���ְ�ť
function DisplayMenuScroll()
{
	var pageHeight=this.menuHeight;//105
	var selfHeight=document.getElementById(this.Address).scrollHeight
	var folder= document.getElementById(this.Address).style;
	var startTop=this.menuTop;
	//alert(startTop+"| folder.pixelTop = "+folder.pixelTop +"| selfHeight = "+ selfHeight +"| pageHeight = "+ pageHeight)
	if(startTop-folder.pixelTop < selfHeight- pageHeight)
	{
		document.getElementById("imgbtnId_down").style.pixelTop=this.menuHeight;//this.menuTop+ 108document.all["divId_imgbtn"].style.pixelTop
		document.getElementById("imgbtnId_down").style.visibility="visible";
	}
	else{		
		document.getElementById("imgbtnId_down").style.visibility="hidden";
		if(global_scroll_flag)
			clearInterval(global_scroll_flag)
	}
	if(folder.pixelTop<startTop){
		document.getElementById("imgbtnId_up").style.visibility="visible";
	}else{		
		document.getElementById("imgbtnId_up").style.visibility="hidden";
		if(global_scroll_flag)
			clearInterval(global_scroll_flag)
	}
}

//ʵ�ֹ���
function getMenuScrolling(value)
{
	try{
		document.getElementById(this.Address).style.pixelTop+=value;
		var clip= this.getClipValue(this.Address)
		this.getClip((parseInt(clip[1])-value),parseInt(clip[2]),(parseInt(clip[3])-value),parseInt(clip[4]));
	}catch(e)
	{}
}
//�õ����˵�
function getClipMenu(top,right,bottom,left)
{
	document.getElementById(this.Address).style.clip=clip='rect('+top+' '+right+' '+bottom+' '+left+')';
}

//�õ����ͽڵ�ֵ
function getClipValue(temp)
{
	var filter = /rect\((\-{0,1}\d*)px (\d*)px (\d*)px (\d*)px\)/;
	var clipString=document.all[temp].style.clip;
	var clip=clipString.match(filter);
	if(clip == '' || clip ==null)
	{
		clip= new Array(0,0,0,0,0)
	}
	return clip
}

//--��ť�¼�--

function ArrowClicked(arrow)
{
	//arrow.style.border="1 inset #ffffff";
}

function ArrowSelected(arrow)
{
	//arrow.style.border="0 none black";
	if(arrow == '2')
		this.getScroll(20)
	else 
		this.getScroll(-20)
	this.dispScroll()
}

function OverArrow(arrow)
{
	//arrow.style.border="1 outset #ffffff";
}

function OutArrow(arrow)
{
	//arrow.style.border="0 none black";
}
//=====================================
