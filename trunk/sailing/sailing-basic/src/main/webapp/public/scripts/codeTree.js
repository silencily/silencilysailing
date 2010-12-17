
/**
 * 实时构造树形菜单 
 * @author liuz
 * @version $Id: codeTree.js,v 1.1 2010/12/10 10:56:35 silencily Exp $
 *
 * 后台取得数据格式：
 *      整个数据：currentString ::: nodestr1 ||| nodestr2 ||| nodestr3 
 *               当前节点数据（可要可不要）:::子节点1数据|||子节点2数据|||子节点3数据
 *      节点数据： this.selfId ### this.nodeName ### this.nodeType     ### this.nodeKind ### this.imgType ### this.noSelect
                  本节点ID    ### 本节点名        ### 本节点种类(有无儿子)### 节点类型(无用)  ### 使用图片类型   ### 是否可选  
 * 使用外部接口参考:
 *		var myTree = new CodeTree('myTree');
 *		myTree.Load(0)
 *		myTree.Load(0,false,'20000')
 *		myTree.Load(cid,true)
 *		myTree.imagePath = '/image/tree/'
 * 在外可重载方法：
 *		CodeTree.getPageUrl(cid)         //得到当前节点信息/加载操作页面
 *      CodeTree.getXmlHttpString(obj)   //从后端取数据
 *      CodeTree.initTreeList0()         //初始化跟节点
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
	this.SelectId  = -1;		 //上次选中节点
	this.TreeList  = new Array();//用对象数组存放树结构
	this.hostUrl   = '';         //服务器获取字符路径，后面参数这里给定。
	this.dispPlace = 'span_menu';//显示的位置
	this.fatherFlag   = 0        //有子节点类型为 0
	this.childFlag    = 1        //无子节点类型为 1
	//this.InitID       = '0'    //初始化父节点
	this.getDataID    = this.InitID = '0' //准备存放自定义开始节点
	this.TreeType     = 0           //使用树类型：1为单选 2为多选 0为无
	this.InitString   = '';
	this.returnList   = new Array();//存放返回对象数组，优化选择
	this.returnSelect = new Array();//存放初始化时选中后被勾掉的选项
	this.compartChild = '|||'    //自定义节点分隔符:节点与节点之间
	this.compartNode  = '###'    //节点的属性之间
	this.compartCurr  = ':::'    //父节点与子节点之间
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
	this.picArray[0].src= this.imagePath + "pic_root.gif";//根节点图标
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
	this.setFather    = setFatherNodeNocheck //清除选中的父节点
	this.getNew       = getNewTreeNode //将新加入节点分离出来
	this.setNoChild   = setTreeNoChild;
	this.viewTree     = viewhiddenTreeListDiv;
}

//--初始化[0]节点
CodeTree.initTreeList0 = function(arr) {
	var obj = new this.Node(arr[0],arr[1],arr[2],arr[3],arr[4],arr[5],arr[6],arr[7],arr[8],arr[9]);
	this.currentArrID  = 0;//初始化时指向当前节点
	this.currentfaID   = arr[0];
    this.currentID     = arr[1];
	this.currentType   = arr[3];
	this.currentName   = arr[2];
	this.currentChild  = '';	
	return obj ;
}

//--初始化相关参数
CodeTree.initTree = function() {
	var i = 0;
	this.TreeList      = null;
	this.TreeList      = new Array();
	this.TreeList[0]   = this.TreeList0(arguments);
}

//展开收缩根节点
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

//--初始显示节点
function initFirstTree(obj){
	var str = '';
	var img = this.selectImage((obj.imgType)?obj.imgType:"2")	
	//alert((obj.imgType)?obj.imgType:"0"+"/"+obj.imgType);
	str +='	<table  width="100%"  border="0" cellpadding="0" cellspacing="0">';
	if(obj.nodeName!=''){
		str +=		'<tr><td border="100" height="25" title="点击这里返回根节点">';//
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
	//默认选中第一个节点
	if(!obj.noSelect){
		this.NodeClick(0);
	}
}
//将节点对象化
function getNode(faid,sid,sname,stype,isLast,child,skind,imgtype,arrayId,noselect)
{
	this.arrayId  = arrayId //本身所在TreeList中序号	
	this.fatherId = faid;   //父节点所在TreeList中序号
	this.selfId   = sid;    //本身数据的id
	this.nodeName = filterSpecialChar(sname);  //本身数据的name
	this.nodeType = stype   //存放该节点种类 有0/无1 儿子节点
	this.nodeKind = skind   //存放该节点类型 0  暂无用
	this.imgType  = imgtype //显示图片的类型 0  0/1/2/3
	this.noSelect = 0       //是否可选
	this.islast   = isLast; //是否是尾节点
	this.children = child;  //所有孩子节点的string
	if(noselect && noselect != "0"){
		this.noSelect = noselect;
	} 
}
//返回对象
function getTreeObject(txt,inum){
	if(document.getElementById(this.TreeName + '_' + txt + '_' + inum))
		return document.getElementById(this.TreeName + '_' + txt + '_' + inum);
	else 
		return '';
}

//-- 新增子节点 刷新当前节点 
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
//-- 修改节点刷新父节点
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
//-- 删除节点时候 刷新父节点
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
//-- 查询节点
function QueryTreeChild(query_str,begin_inum)
{
	if(typeof begin_inum == 'undefined')
		begin_inum = 0;
	var flag = false;
	var reg = /^([0-9]){1,}$/gi;
	var facid = '';
	var chcid = '';
	this.treeTitle = '返回根节点';
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
		alert('操作失败：没有找到该节点!');
		this.Load(0);
	}
	return chcid;
}

//--该功能在多选中没有存在意义 暂时屏蔽
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
			//递归调用 对父级checkbox清除
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
			//自动展开子级
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

//--点击节点事件
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

//--点击节点效果
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
//--选择下属部门
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

//-- 展开指定的节点
//-- @param str = ' 一级节点,二级节点,三级... '
function DisplayAllFather(str){ 
	if(!str)
		return false;
	var temp = str.split(this.compartChild);
	if(temp.length > 10) //大于5级 返回
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
	if(temp[i-1]){ //展开到指定节点
		location = '#'+this.TreeName+'_mao_'+temp[i-1];
	}
}

//--实时加载树子节点
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
			click_str = ' onclick="switchImg(document.getElementById(\'pic_imgId_'+temp[0]+'\'),\''+this.picArray[1].src+'\',\''+this.picArray[2].src+'\');'+this.TreeName+'.Load(\''+t+'\');" style="cursor:hand" title="伸缩节点" ' 
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
		//是否为叶子节点
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
 * @params cid   存放数组编号
 * @params temp  是否必须刷新节点
 * @params txtid 自定义节点编号
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
			//var temp_string = this.getData(this.TreeList[cid]);//自定义开始节点
		//}else
		var temp_string = this.getData(this.TreeList[cid]);//默认为0节点
		//--display error info--
		if(temp_string.indexOf('\n') >= 0 || (!temp &&( !temp_string || temp_string.length == 0))){
			if(cid == 0){
				this.initDisp(this.TreeList[0]);
			}else if(temp_string == "" ){
				this.setNoChild(cid,sid);
				return;
		    }else{
				this.initDisp(new this.Node(0,0,'没有找到您需要的数据!',0,true,'',0,0));
				return;
			}
		}
		var data_string = temp_string;
		//在string:string11;string12中分离出父节点string
		if(temp_string.indexOf(this.compartCurr)>=0){
			var node_array = temp_string.split(this.compartCurr);					
			if(txtid){
				this.TreeList[0].selfId = 0;
				if(!document.getElementById("tdId_"+this.TreeList[0].selfId))
					this.initDisp(this.TreeList[0]);//初始化
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
				this.initDisp(new this.Node(0,0,'根节点',0,true,'',0,0));
				alert('没有找到该节点！');
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

//没有子节点，变换图片
function setTreeNoChild(cid,sid){
	if(cid != this.InitID && document.getElementById("imgId_"+sid)){	
			if(this.TreeList[cid].islast)
				document.getElementById("imgId_"+sid).src=this.nochildImg1.src;
			else
				document.getElementById("imgId_"+sid).src=this.nochildImg.src;
	}
}


//--向数据集中 新加入节点
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

//---图标变换
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
		alert("从服务器端获得数据的过程中出现错误" + e);
		content = '';
   }  
   return content;
}

/**
 *  根据节点编号sid,获取xmlhttp 返回字符串
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
 *  点击后加载页面 
 *  cid 为对象数组的编号 当前节点信息 
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
	var arr = this.returnList.concat();//注意this.NodeClic 改变 this.returnList 值
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