
/**
 * ʵʱ�������β˵� 
 * @author liuz
 * @version $Id: codeTree.js,v 1.1 2010/12/10 10:56:35 silencily Exp $
 *
 * ��̨ȡ�����ݸ�ʽ��
 *      �������ݣ�currentString ::: nodestr1 ||| nodestr2 ||| nodestr3 
 *               ��ǰ�ڵ����ݣ���Ҫ�ɲ�Ҫ��:::�ӽڵ�1����|||�ӽڵ�2����|||�ӽڵ�3����
 *      �ڵ����ݣ� this.selfId ### this.nodeName ### this.nodeType     ### this.nodeKind ### this.imgType ### this.noSelect
                  ���ڵ�ID    ### ���ڵ���        ### ���ڵ�����(���޶���)### �ڵ�����(����)  ### ʹ��ͼƬ����   ### �Ƿ��ѡ  
 * ʹ���ⲿ�ӿڲο�:
 *		var myTree = new CodeTree('myTree');
 *		myTree.Load(0)
 *		myTree.Load(0,false,'20000')
 *		myTree.Load(cid,true)
 *		myTree.imagePath = '/image/tree/'
 * ��������ط�����
 *		CodeTree.getPageUrl(cid)         //�õ���ǰ�ڵ���Ϣ/���ز���ҳ��
 *      CodeTree.getXmlHttpString(obj)   //�Ӻ��ȡ����
 *      CodeTree.initTreeList0()         //��ʼ�����ڵ�
 *
 *                 
 */

if (CodeTree == null) 
{
	var CodeTree = {};
}

CodeTree.CodeTree = function(treename,imagePath){

	//Data member 
	this.TreeName  = treename
	this.treeTitle = '';
	this.SelectId  = -1;		 //�ϴ�ѡ�нڵ�
	this.TreeList  = new Array();//�ö������������ṹ
	this.hostUrl   = '';         //��������ȡ�ַ�·��������������������
	this.dispPlace = 'span_menu';//��ʾ��λ��
	this.fatherFlag   = 0        //���ӽڵ�����Ϊ 0
	this.childFlag    = 1        //���ӽڵ�����Ϊ 1
	//this.InitID       = '0'    //��ʼ�����ڵ�
	this.getDataID    = this.InitID = '0' //׼������Զ��忪ʼ�ڵ�
	this.TreeType     = 0           //ʹ�������ͣ�1Ϊ��ѡ 2Ϊ��ѡ 0Ϊ��
	this.InitString   = '';
	this.returnList   = new Array();//��ŷ��ض������飬�Ż�ѡ��
	this.returnSelect = new Array();//��ų�ʼ��ʱѡ�к󱻹�����ѡ��
	this.compartChild = '|||'    //�Զ���ڵ�ָ���:�ڵ���ڵ�֮��
	this.compartNode  = '###'    //�ڵ������֮��
	this.compartCurr  = ':::'    //���ڵ����ӽڵ�֮��
	//image 
	this.imagePath      = imagePath ? imagePath : ContextInfo.contextPath+"/image/tree/";
	this.nochildImg     = new Image();
	this.nochildImg.src = this.imagePath + "tree_mid.gif";
	this.nochildImg1    = new Image();
	this.nochildImg1.src= this.imagePath + "tree_end.gif";
	this.openImg        = new Image();
	this.openImg.src    = this.imagePath + "tree_t_mid.gif";
	this.openImg1       = new Image();
	this.openImg1.src   = this.imagePath + "tree_t_end.gif";
	this.closeImg       = new Image();
	this.closeImg.src   = this.imagePath + "tree_x_mid.gif";
	this.closeImg1      = new Image();
	this.closeImg1.src  = this.imagePath + "tree_x_end.gif";
	this.bgImg          = new Image();
	this.bgImg.src      = this.imagePath + "tree_bg.gif";
	this.transImg       = new Image();
	this.transImg.src   = this.imagePath + "tree_trans.gif";
	this.picArray       = new Array();
	this.picArray[0]    = new Image();
	this.picArray[0].src= this.imagePath + "pic_root.gif";//���ڵ�ͼ��
	this.picArray[1]    = new Image();
	this.picArray[1].src= this.imagePath + "pic_dept.gif";// 
	this.picArray[2]    = new Image();
	this.picArray[2].src= this.imagePath + "pic_role.gif";// 
	this.picArray[3]    = new Image();
	this.picArray[3].src= this.imagePath + "pic_user.gif";// 
	//css
	this.focusCss   = "font_master";
	this.blurCss    = "";
	this.noSelectCss= "font_ignore";

	//currently Node
	this.currentArrID  = 0
	this.currentfaID   = 0;
    this.currentID     = 0;
	this.currentType   = 0;
	this.currentName   = '';
	this.currentChild  = '';	

	//method
	this.getData    = CodeTree.getXmlHttpString;
	this.getObj     = getTreeObject;
	this.initDisp   = initFirstTree;
	this.Init       = CodeTree.initTree;
	this.TreeList0  = CodeTree.initTreeList0;
	this.Add        = AddTreeChild;
	this.Update     = UpdateTreeChild;
	this.Remove     = RemoveTreeChild;
	this.Query		= QueryTreeChild
	this.Display    = DisplayTreeChild;
	this.NodeClick  = getClickTreeChild;
	this.BoxClick   = getClickCheckBox;
	this.SetCss     = setTreeChildCss;
	this.Load       = getLoadXmlTree;
	this.ShowTable  = MenuSwitchTable;
	this.Node       = getNode;
	this.getUrl     = CodeTree.getPageUrl;
	this.getSelect  = getTreeSelect;
	this.getNoSelect= getTreeNoSelect;
	this.DisFather  = DisplayAllFather;
	this.selectImage= selectImage;
	//this.selectAll  = getAllSelect;
	this.disSelectAll = disSelectAll;
	this.setFather    = setFatherNodeNocheck //���ѡ�еĸ��ڵ�
	this.getNew       = getNewTreeNode //���¼���ڵ�������
	this.setNoChild   = setTreeNoChild;
	this.viewTree     = viewhiddenTreeListDiv;
}

//--��ʼ��[0]�ڵ�
CodeTree.initTreeList0 = function(arr) {
	var obj = new this.Node(arr[0],arr[1],arr[2],arr[3],arr[4],arr[5],arr[6],arr[7],arr[8],arr[9]);
	this.currentArrID  = 0;//��ʼ��ʱָ��ǰ�ڵ�
	this.currentfaID   = arr[0];
    this.currentID     = arr[1];
	this.currentType   = arr[3];
	this.currentName   = arr[2];
	this.currentChild  = '';	
	return obj ;
}

//--��ʼ����ز���
CodeTree.initTree = function() {
	var i = 0;
	this.TreeList      = null;
	this.TreeList      = new Array();
	this.TreeList[0]   = this.TreeList0(arguments);
}

//չ���������ڵ�
function viewhiddenTreeListDiv() {
	var objDiv = document.getElementById("codeTreeListDiv");
	var objImg = document.getElementById("imgId_0");
	var objpicImg = document.getElementById("pic_imgId_0");
	//alert(objImg.src+"==="+this.closeImg1.src);
	if(objDiv.style.display=="block"){
		objImg.src = this.closeImg1.src;
		objDiv.style.display="none";
		objpicImg.src = this.picArray[0].src;
	}else{
		objImg.src = this.openImg1.src;
		objDiv.style.display="block";
		objpicImg.src = this.picArray[2].src;
	}
	//alert("test");
}

//--��ʼ��ʾ�ڵ�
function initFirstTree(obj){
	var str = '';
	var img = this.selectImage((obj.imgType)?obj.imgType:"2")	
	//alert((obj.imgType)?obj.imgType:"0"+"/"+obj.imgType);
	str +='	<table  width="100%"  border="0" cellpadding="0" cellspacing="0">';
	if(obj.nodeName!=''){
		str +=		'<tr><td border="100" height="25" title="������ﷵ�ظ��ڵ�">';//
		//if(obj.selfId != '0')
			str +=	'&nbsp; <img src="'+this.openImg1.src+'" id="imgId_0" onclick="'+this.TreeName+'.viewTree();" align="absMiddle">'+ img +'&nbsp; <a href="#" onclick="'+this.TreeName+'.NodeClick(\'0\');'+this.TreeName+'.getUrl(\'0\');return false" name="'+this.TreeName+'_mao_0">'
			str +=  '<span id="'+this.TreeName+'_spanId_'+obj.selfId+'">'+obj.nodeName+'</span></a>';
		//else
		//	str +=  '&nbsp; '+ img +'<a href="#" onclick="'+this.TreeName+'.Load(0);return false;">'+obj.nodeName+'</a>';
		str +=		'</td></tr>';
	}
	str +=		'<tr  id="trId_'+obj.selfId+'">';//
	str +=			'<td  valign="top" id="tdId_'+obj.selfId+'">';//'+obj.selfId+'
	str +=		'</td></tr>';
	str +='	</table>';
	if(this.dispPlace)
		document.getElementById(this.dispPlace).innerHTML = str;
	//Ĭ��ѡ�е�һ���ڵ�
	if(!obj.noSelect){
		this.NodeClick(0);
	}
}
//���ڵ����
function getNode(faid,sid,sname,stype,isLast,child,skind,imgtype,arrayId,noselect)
{
	this.arrayId  = arrayId //��������TreeList�����	
	this.fatherId = faid;   //���ڵ�����TreeList�����
	this.selfId   = sid;    //�������ݵ�id
	this.nodeName = filterSpecialChar(sname);  //�������ݵ�name
	this.nodeType = stype   //��Ÿýڵ����� ��0/��1 ���ӽڵ�
	this.nodeKind = skind   //��Ÿýڵ����� 0  ������
	this.imgType  = imgtype //��ʾͼƬ������ 0  0/1/2/3
	this.noSelect = 0       //�Ƿ��ѡ
	this.islast   = isLast; //�Ƿ���β�ڵ�
	this.children = child;  //���к��ӽڵ��string
	if(noselect && noselect != "0"){
		this.noSelect = noselect;
	} 
}
//���ض���
function getTreeObject(txt,inum){
	if(document.getElementById(this.TreeName + '_' + txt + '_' + inum))
		return document.getElementById(this.TreeName + '_' + txt + '_' + inum);
	else 
		return '';
}

//-- �����ӽڵ� ˢ�µ�ǰ�ڵ� 
function AddTreeChild(cid){
	cid = cid ? cid : this.SelectId;
	var sid = this.TreeList[cid].selfId;
	if(document.getElementById("imgId_"+sid)){
		if(this.TreeList[cid].islast)
			document.getElementById("imgId_"+sid).src= this.openImg1.src;
		else
			document.getElementById("imgId_"+sid).src= this.openImg.src;
	}
	this.Load(cid,true);
}
//-- �޸Ľڵ�ˢ�¸��ڵ�
function UpdateTreeChild(cid,temp){
	cid = cid ? cid : this.SelectId;
	var facid = this.TreeList[cid].fatherId;
	var sid   = this.TreeList[cid].selfId;
	if(facid)
		this.Load(facid,true);
	else
		this.Load(0);
	this.SetCss(sid,true,true);
	this.SelectId = cid;
	this.currentID = sid;
	this.currentArrID= cid
}
//-- ɾ���ڵ�ʱ�� ˢ�¸��ڵ�
function RemoveTreeChild(cid){	
	cid = cid ? cid : this.SelectId;
	var facid = this.TreeList[cid].fatherId;
	var sid   = this.TreeList[facid].selfId;
	document.getElementById("tdId_"+sid).innerHTML = ""
	this.Load(facid,true);
	this.SetCss(sid,true,true);
	//alert(facid);
	this.SelectId = facid;
	this.currentID = sid;
	this.currentArrID= facid;
}
//-- ��ѯ�ڵ�
function QueryTreeChild(query_str,begin_inum)
{
	if(typeof begin_inum == 'undefined')
		begin_inum = 0;
	var flag = false;
	var reg = /^([0-9]){1,}$/gi;
	var facid = '';
	var chcid = '';
	this.treeTitle = '���ظ��ڵ�';
	if(reg.test(query_str))
	{		
		for(var i=0;i<this.TreeList.length;i++)
		{
			if(query_str == this.TreeList[i].selfId)
			{
				//facid = this.TreeList[i].fatherId;
				//chcid = i;
				//this.treeTitle = this.TreeList[i].nodeName
				chcid = query_str
				flag  = true;
				break;
			}
		}
		//this.treeTitle = '';
		chcid = query_str;
	}else
	{
		for(var i=0;i<this.TreeList.length;i++)
		{
			if( this.TreeList[i].nodeName.indexOf(query_str) >=0 )
			{
				//facid = this.TreeList[i].fatherId;
				//chcid = i;
				//this.treeTitle = this.TreeList[i].nodeName
				chcid = this.TreeList[i].selfId;
				flag  = true;
				break;
			}
		}		
	}
	if(chcid && !flag)
	    this.Load(0,true,chcid);
	else if(chcid !='' && flag )
	{
		this.Load(0,true,chcid);
		//this.Load(chcid);
		//this.NodeClick(chcid);
	}else{
		alert('����ʧ�ܣ�û���ҵ��ýڵ�!');
		this.Load(0);
	}
	return chcid;
}

//--�ù����ڶ�ѡ��û�д������� ��ʱ����
function setFatherNodeNocheck(inum){
	return ;//***

	if(this.TreeType != 2)
		return;
	var fid = this.TreeList[inum].fatherId
	var sid = this.TreeList[fid].selfId
	var cid = this.TreeList[inum].selfId
	if(!this.getObj('box',cid).checked){
		if(sid != this.getDataID && fid != this.InitID && !this.TreeList[fid].noSelect  && this.getObj('box',sid).checked ){		
			this.SetCss(sid,false);
			//�ݹ���� �Ը���checkbox���
			this.setFather(fid)
			//
			for(var i=0;i<this.returnList.length;i++){
				if(this.returnList[i] && this.returnList[i].selfId == sid){
					this.returnList.splice(i,1);
				}
			}
		}
	}else{
		//this.ShowTable(cid,cid,'display');
		if(this.TreeList[inum].nodeType == this.fatherFlag)
			this.Load(inum);
		/*var chd =  this.TreeList[inum].children.concat();
		if(chd){
			//�Զ�չ���Ӽ�
			//alert("init"+chd.length)
			this.ShowTable(cid,cid,'display');
			for(var i=0;i<chd.length;i++){
				this.Load(chd[i].arrayId);
				if(chd[i].nodeType == this.fatherFlag)
					this.setFather(chd[i].arrayId);
			}
		}*/
	}
}

//--����ڵ��¼�
function getClickTreeChild(inum){
	if(this.TreeList[inum].noSelect)
		return
	var uid = this.TreeList[inum].selfId;
	if(this.TreeType == 2){
		if(this.getObj('box',uid).checked ){
		   this.getObj('box',uid).checked = false;	
		   //var faid = this.TreeList[inum].fatherId;
		   //this.setFather(inum);
		}else
		   this.getObj('box',uid).checked = true;
	}
	this.BoxClick(inum);
	//this.SetCss(inum);
}

//--����ڵ�Ч��
function setTreeChildCss(uid,temp,flag){
	//if(uid == this.getDataID || uid == this.InitID)
	//	return
	if(temp){	
		if(!flag)
			this.getObj('box',uid).checked = true;
		this.getObj('spanId',uid).className  = this.focusCss ;
	}
	else{
		if(!flag)
			this.getObj('box',uid).checked = false;
		this.getObj('spanId',uid).className  = this.blurCss ;
	}	
}
//--ѡ����������
function getClickCheckBox(temp){
	var uid = this.TreeList[temp].selfId;
	var lid = "";
	if(this.SelectId>=0){
		lid = this.TreeList[this.SelectId].selfId;
	}
	if(this.TreeType == 1)/*-----*/
	{
		if(this.TreeList[temp].noSelect)
			return
		if(this.SelectId != -1 && this.getObj('box',lid).checked)
		{
			this.SetCss(lid,false);
			this.returnList = null;//
		}
		if(this.SelectId != temp )
		{
			this.SelectId = temp ;
			this.SetCss(uid,true);
			this.returnList = new Array(this.TreeList[temp]);//
		}else
		{
			this.SelectId = -1 ;
			this.SetCss(uid,false);
			this.returnList = null;//
		}
	}else if(this.TreeType == 2)/*-----*/
	{
		var arr  = this.TreeList[temp].children;
		if( this.getObj('box',uid).checked)
		{
			for(var i=0;i<arr.length;i++)
			{
				this.SetCss(arr[i].selfId,true);
				this.BoxClick(arr[i].arrayId);
			}
			this.SetCss(uid,true);
			//
			var flag = true;
			for(var i=0;i<this.returnList.length;i++){
				if(this.returnList[i] && this.returnList[i].selfId == uid){
					flag =false;
					break;
				}
			}
			if(flag)
				this.returnList[this.returnList.length] = this.TreeList[temp];
		}
		else{
			for(var i=0;i<arr.length;i++)
			{
				this.SetCss(arr[i].selfId,false);
				this.BoxClick(arr[i].arrayId);
			}
			this.SetCss(uid,false);
			//
			for(var i=0;i<this.returnList.length;i++){
				if(this.returnList[i] && this.returnList[i].selfId == uid){
					//this.returnList[i] = null ;
					this.returnList.splice(i,1);
				}
			}
		}
	}else {
	   //if(temp != 0){}
		if(temp!=this.SelectId){//lib != this.getDataID  lid != 0 &&
			if(this.SelectId != -1)
				this.getObj('spanId',lid).className = this.blurCss ;
			this.getObj('spanId',uid).className = this.focusCss ;
			this.SelectId = temp ;
		}		
	}
}

//-- չ��ָ���Ľڵ�
//-- @param str = ' һ���ڵ�,�����ڵ�,����... '
function DisplayAllFather(str){ 
	if(!str)
		return false;
	var temp = str.split(this.compartChild);
	if(temp.length > 10) //����5�� ����
		return false;
	var prelen = 0;
	var curlen = 0;
	var lastId = 0;
	for(var i=0;i<temp.length;i++){
		curlen = this.TreeList.length;
		for(var j=prelen;j<curlen;j++){
			//alert(temp[i] +"|"+ this.TreeList[j].selfId)
			if(temp[i] == this.TreeList[j].selfId){
				this.Load(j);
				lastId = j;
			}
		}
		prelen = curlen;
	}
	if(temp[i-1]){ //չ����ָ���ڵ�
		location = '#'+this.TreeName+'_mao_'+temp[i-1];
	}
}

//--ʵʱ�������ӽڵ�
//--Display tree children
function DisplayTreeChild(cid,ccid)
{	
	var sid = this.TreeList[cid].selfId+'';
	var menu_array = this.TreeList[cid].children;
	//alert("cid : "+cid+" sid : "+sid+" ccid : "+ccid)
	var init_num=0;
	var init_txt=""+
	"<div id='codeTreeListDiv' style='display:block'><table width='100%' border='0' cellpadding='0'  cellspacing='0' id='tabId_"+this.name+"_"+sid+"' class='css_menu_depart'>";
	var temp_array=new Array();
	var img_str='';
	var img_str1=this.openImg.src;
	var menu_bgimage='';
	var transimage  = '<img src="'+ this.transImg.src +'" width="15">';
	for(var i=0;i<menu_array.length;i++)
	{		
		var t        = ccid+i
		var click_css='';
		var click_str='';
		var child_str=''

		if(sid!=0 && sid != this.getDataID)
			menu_bgimage=' background="'+ this.bgImg.src+'"';
		if(ccid==0 || this.TreeList[this.currentArrID].islast)
		{
			menu_bgimage='';
			img_str1=this.openImg1.src;
		}
		var temp = new Array();
		temp[0]  = menu_array[i].selfId;
		temp[1]  = menu_array[i].nodeName;
		temp[5]  = menu_array[i].imgType;
		
		switch(this.TreeType){
			case 0:
				child_str += '<a name="'+this.TreeName+'_mao_'+t+'" href="javascript:'+this.TreeName+'.NodeClick(\''+t+'\');'+this.TreeName+'.getUrl(\''+t+'\');" ><span id="'+this.TreeName+'_spanId_'+temp[0]+'"';
				if(menu_array[i].noSelect)
					child_str += ' class="'+this.noSelectCss+'" ';
				child_str += '>';
				break;
			default:
				if(!menu_array[i].noSelect && typeof menu_array[i].noSelect != 'undefined'){
					child_str += '<input type="checkbox" value="'+t+'" id="'+this.TreeName+'_box_'+temp[0]+'" name="'+this.TreeName+'_checkbox"';
					child_str += ' onclick="'+this.TreeName+'.BoxClick('+t+');'+this.TreeName+'.setFather('+t+');"';
					//if(this.TreeType == 1)						
					//else
						//child_str += ' onclick="'+this.TreeName+'.selectAll('+t+')"';
					if(this.InitString!='' && this.InitString.indexOf(','+temp[0]+',')!=-1 )//|| (this.TreeType ==2 && this.getObj('box',sid)&&this.getObj('box',sid).checked)				
					{				
						child_str      += ' checked >';
						click_css       = this.focusCss ;
						this.SelectId   = t;
						this.returnList[this.returnList.length] = this.TreeList[t];
						this.returnSelect.push(this.TreeList[t]);
					}else
						child_str += '>';
				}
				child_str     += '<a name="'+this.TreeName+'_mao_'+t+'" href="#" onclick="'+this.TreeName+'.NodeClick(\''+t+'\');return false" ><span id="'+this.TreeName+'_spanId_'+temp[0]+'" class="'+click_css+'">';
				break;
			//case 2:
				//child_str += '<input type="checkbox" value="'+i+'" id="'+this.TreeName+'_box_'+i+'">'
				//break;
			//default:
				//child_str +
				//break;
		}
		child_str += temp[1]+'</span></a>';

		if( i+1 == menu_array.length)
			img_str = this.closeImg1.src;
		else
			img_str = this.closeImg.src;

		if(this.TreeList[ccid+i].nodeType == this.fatherFlag )////\''+sid+'\',\''+temp[0]+'\',   '+this.TreeName+'.BoxClick(\''+t+'\');
			click_str = ' onclick="switchImg(document.getElementById(\'pic_imgId_'+temp[0]+'\'),\''+this.picArray[1].src+'\',\''+this.picArray[2].src+'\');'+this.TreeName+'.Load(\''+t+'\');" style="cursor:hand" title="�����ڵ�" ' 
		else
		{
			click_str = '';
			if(i+1 == menu_array.length)
				img_str = this.nochildImg1.src;				
			else
				img_str = this.nochildImg.src;
		}
		img_str = '<img src="'+img_str;
		img_str += ' "  align="absmiddle"  id="imgId_'+temp[0]+'" '+click_str+' >'; 
		/*----*/
		//img_str += this.selectImage(temp[5])
		/*----*/	
		//alert(cid+"/"+this.TreeList[cid].fatherId+"/"+TreeList[cid].selfId+"/"+this.TreeList[cid].nodeType+"/"+this.TreeList[cid].nodeName+"/"+this.TreeList[cid].children);
		//�Ƿ�ΪҶ�ӽڵ�
		if(this.TreeList[ccid+i].nodeType == this.fatherFlag )////\''+sid+'\',\''+temp[0]+'\',   '+this.TreeName+'.BoxClick(\''+t+'\');
			img_str += '<img src=" '+this.picArray[1].src+'" id="pic_imgId_'+temp[0]+'" align="absmiddle" >';
		else
		{
			img_str += '<img src=" '+this.picArray[3].src+'" id="pic_imgId_0" align="absmiddle" >';
		}
			
		init_txt+='<tr id="trId_child_'+temp[0]+'"><td '+menu_bgimage+' width="15">'+transimage+'</td>\n<td nowrap id="tdId_child_'+temp[0]+'" >\n'+img_str+'\n'+child_str+'\n</td></tr>';
		init_txt+='<tr id="trId_'+temp[0]+'" style="display:none"><td '+menu_bgimage+' width="15">'+transimage+'</td><td nowrap id="tdId_'+temp[0]+'"></td></tr>';
			
	}
	init_txt+="</talbe></div>"
	if(sid != 'undefined'){
		if( sid != this.InitID && sid != this.getDataID){		
			document.getElementById("imgId_"+sid).src= img_str1;
		}
		document.getElementById("trId_"+sid).style.display = "";
		document.getElementById("tdId_"+sid).innerHTML     = init_txt;
	}
}

function switchImg(imgId,img1,img2){
	if(imgId.src == img2){
		imgId.src = img1;
	}
	else {
		imgId.src = img2;
	}
}

//--
function selectImage(temp){
	var img_str = '';
	var ii = temp != "" || temp == "0" ? parseInt(temp) : 99;
	if(this.picArray[ii])
		img_str = '<img src=" '+this.picArray[ii].src+'" id="pic_imgId_0" align="absmiddle" >';
	return img_str ;
}

/**
 * get Load XML Tree
 * @params cid   ���������
 * @params temp  �Ƿ����ˢ�½ڵ�
 * @params txtid �Զ���ڵ���
 * 
 */
function getLoadXmlTree(cid,temp,txtid){	
	temp  = temp +'' == 'undefined'?false:temp;
    txtid = txtid +'' == 'undefined'?0:txtid;
	if(cid == this.InitID)
		this.Init(0,txtid,this.treeTitle,0,true,'',0,0);
	if(!this.TreeList[cid])
		return;
	if(txtid!=0){
		//txtid =  this.getDataID
		this.getDataID = txtid;
		cid ++;
		this.TreeList[cid] = new this.Node(0,txtid);
	}
	
	var faid = this.TreeList[cid].fatherId;
	var sid  = this.TreeList[cid].selfId;

	if(this.TreeList[cid].children && this.TreeList[cid].children !=''&&!temp ){
			this.ShowTable(sid,sid,'');
	}else{ //-------------get data from xmlhttp	
		//if(txtid !='' && txtid!='0')
		//{
			//var temp_string = this.getData(this.TreeList[cid]);//�Զ��忪ʼ�ڵ�
		//}else
		var temp_string = this.getData(this.TreeList[cid]);//Ĭ��Ϊ0�ڵ�
		//--display error info--
		if(temp_string.indexOf('\n') >= 0 || (!temp &&( !temp_string || temp_string.length == 0))){
			if(cid == 0){
				this.initDisp(this.TreeList[0]);
			}else if(temp_string == "" ){
				this.setNoChild(cid,sid);
				return;
		    }else{
				this.initDisp(new this.Node(0,0,'û���ҵ�����Ҫ������!',0,true,'',0,0));
				return;
			}
		}
		var data_string = temp_string;
		//��string:string11;string12�з�������ڵ�string
		if(temp_string.indexOf(this.compartCurr)>=0){
			var node_array = temp_string.split(this.compartCurr);					
			if(txtid){
				this.TreeList[0].selfId = 0;
				if(!document.getElementById("tdId_"+this.TreeList[0].selfId))
					this.initDisp(this.TreeList[0]);//��ʼ��
				var curArr = node_array[0].split(this.compartNode);
				this.TreeList[cid] = new this.Node(0,curArr[0],curArr[1],curArr[2],curArr[3],'',curArr[4],curArr[5]);
				this.TreeList[0].children = new Array(this.TreeList[cid]);
				this.Display(0,1);
			}
			data_string = node_array[1];
		}
		temp = data_string.split(",");
		if(temp.length>0){
			if(temp[0]=="null"){
				this.initDisp(new this.Node(0,0,'���ڵ�',0,true,'',0,0));
				alert('û���ҵ��ýڵ㣡');
				return ;
			}
			if(cid !=0 && temp[0] == this.TreeList[cid].selfId){
				//this.TreeList[cid].islast = true;
				data_string = '';
			}
		}
		if(data_string == ''){
			this.setNoChild(cid,sid);
		}else{
			if(!document.getElementById("tdId_"+this.TreeList[0].selfId))
				this.initDisp(this.TreeList[0]);
			this.getNew(data_string,cid);
		}
	}
}

//û���ӽڵ㣬�任ͼƬ
function setTreeNoChild(cid,sid){
	if(cid != this.InitID && document.getElementById("imgId_"+sid)){	
			if(this.TreeList[cid].islast)
				document.getElementById("imgId_"+sid).src=this.nochildImg1.src;
			else
				document.getElementById("imgId_"+sid).src=this.nochildImg.src;
	}
}


//--�����ݼ��� �¼���ڵ�
function getNewTreeNode(data_string,cid){
	var j=this.TreeList.length;
	var temp = false; 
	var temp_array = data_string.split(this.compartChild);
	var temp_data  = new Array();
	for(var i=0;i<temp_array.length;i++)
	{
		var temp_child = temp_array[i].split(this.compartNode);			
		if(i==temp_array.length-1)
			temp = true; 
		temp_child = 
		this.TreeList[j+i]=new this.Node(cid,temp_child[0],temp_child[1],temp_child[2],temp,'',temp_child[3],temp_child[4],(j+i),temp_child[5]);
		temp_data[i] = this.TreeList[j+i];
	}
	this.TreeList[cid].children = temp_data ;//child_string;
	this.currentArrID = cid;
	this.Display(cid,j);
}

//---ͼ��任
function MenuSwitchTable(sid,imgid,flag){	
	var local_image=this.openImg.src;
	var subment=document.getElementById("trId_"+sid);
	var imgbtn =document.getElementById("imgId_"+imgid);
	var iflag=0;
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
		if(imgbtn.src.indexOf(this.closeImg1.src)>=0)
				local_image=this.openImg1.src;
		imgbtn.src=local_image;
	}else{
		subment.style.display="none";
		if(imgbtn.src.indexOf(this.openImg1.src)>=0)
			local_image=this.closeImg1.src;
		else 
			local_image=this.closeImg.src;
		imgbtn.src=local_image;
	}
}

//--
function httpGet(url)
{
   try{
		 var xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
		 var content = '' 
		 if(xmlhttp != null || xmlhttp!=null)
		 {
			url = url+"&dateTimeTime="+DateUtils.GetDBDate();
			xmlhttp.open("GET", url, false);
			//xmlhttp.setRequestHeader( null , cookie );
			xmlhttp.send();
			content = xmlhttp.responseText;
			if("ERRORS_HAPPEND!!!".toString() == content.toString()){
				content = "";
			}
		}
   }catch(e){
		alert("�ӷ������˻�����ݵĹ����г��ִ���" + e);
		content = '';
   }  
   return content;
}

/**
 *  ���ݽڵ���sid,��ȡxmlhttp �����ַ���
 */
CodeTree.getXmlHttpString = function(obj)
{
	var sid = obj.selfId;
	//var str = httpGet(this.hostUrl+sid);
	var str = XMLHttpEngine.getResponseText(this.hostUrl+sid, false);
	if(typeof str == 'undefined')
		str = '';
	return str;
}

/**
 *  ��������ҳ�� 
 *  cid Ϊ��������ı�� ��ǰ�ڵ���Ϣ 
 */
CodeTree.getPageUrl = function(cid)
{
	this.currentArrID  = cid
	this.currentfaID   = this.TreeList[cid].fatherId;
    this.currentID     = this.TreeList[cid].selfId;
	this.currentType   = this.TreeList[cid].nodeType;
	this.currentName   = this.TreeList[cid].nodeName;
	this.currentChild  = this.TreeList[cid].children;
	//ifrm_listing.location = '/codemgr.do?method=get&dmid='+this.currentID;		
}

function getTreeSelect(){
	return this.returnList;	
	/*var returnArray= new Array();
	var inum;
	var j = 0;	
	var box = eval('document.all("'+this.TreeName+'_checkbox")');
	if(box){
		for(var i=0;i<box.length;i++)
		{
			if(box[i].checked)
			{
				inum = parseInt(box[i].value);
				returnArray[j++] = this.TreeList[inum];
				if(this.TreeType == 1)
					return returnArray;
			}
		}
		return returnArray;
	}else
		return '';
	*/
}

function getTreeNoSelect(){
	for(var i=this.returnSelect.length-1;i>=0;i--){
		for(var j=0;j<this.returnList.length;j++){
			if(this.returnSelect[i] == this.returnList[j])
				this.returnSelect.splice(i,1);
	   }
	}
	return this.returnSelect;
}

function disSelectAll(){
	var arr = this.returnList.concat();//ע��this.NodeClic �ı� this.returnList ֵ
	for(var i=0;i<arr.length;i++){
		var id = arr[i].arrayId;
		this.NodeClick(id);
	}
   /*for(var i = 0;i<this.TreeList.length;i++){
	    var uid = this.TreeList[i].selfId; 
		 if(this.getObj('box',uid) && this.getObj('box',uid).checked )
		    this.getObj('box',uid).checked = false;		
   }*/   
} 

//2008-02-20 add by wangshuo start
function filterSpecialChar(str) {
	if (str) {
		str = str.replace(/&/g, "&amp");
		str = str.replace(/</g, "&lt");
		str = str.replace(/>/g, "&gt");
		return str;
	} else {
		return "";
	}
}
//2008-02-20 add by wangshuo end