/**
 * 显式加载菜单树
 * 
 * Date:  2004/10/10 
 * Author:liuz
 * 使用节点参数:（没有使用的参数用","预留空值，例如 "系统管理,/zsu.htm,,,,,点击这里进入系统管理"）
 * nodeLevel,nodeName,nodeUrl,target,nodeId,nodeType,noSelect,promptInfo,helpUrl,isLast,fathId,children
 * 层次 / 名称 / URL / Target / ID / 类型 / 是否可选 / ....	
 * 
 * (1) 可以单选/多选/不选 选择返回名称/id 	
 * (2) 应用在 系统菜单 / 组织结构树 等 
 * (3) 可以扩展节点图标			
 * (4) 初始化时可以将选中节点的展开	 
 * (5) 节点全部展开或者全部收缩	  
 * (6) ** 菜单树可以自定义滚动条形式
 * (7) ** 
 * 
 * 2005-01-12 update 如果已经点击一个连接，屏蔽加载别的连接 getClickNode 需要同步更新 page_left.jsp
 * 2004-11-26 update 加入selectCurrent属性，如果此属性为true，将会只选择当前节点，在selectDeptBox中进行了修改，顾杨。
 * 2004-11-23 update 优化全选节点，和返回节点速度
 * 2004-11-12 update switchDepartAll() 加入 对根节点收展变换 
 */
 
 /**树形结构
 * @param treename 树型名称
 */
function DepartTree(treename)
{
	//Data member 
	this.TreeName  = treename
	this.Address   = 'span_menu';//显示菜单位置的对象名
	this.DeptList  = new Array();//用对象数组存放树结构
	this.LinkList  = new Array();//存放关联节点组 [层次][节点编号组]
	this.TreeType  = 0           //树类型 默认0 / 单选1 /多选2
	this.InitID    = 0           //初始化父节点
	this.InitString= '';         //初始值
	this.IsExtend  = false;      //是否伸展 
	this.IsScroll  = false;      //是否出现滚动条
	this.IsHelp    = false;      //是否采用帮助连接
	this.IsDeploy  = false;      //是否点连接时候展开下级节点
	this.SelectId  = -1;		 //上次选中节点
	this.PageHeight= 0;
	this.OpenFlag  = 'display';
	this.returnList= new Array();//存放返回对象数组，优化选择
	this.SelectSon = false;      //只选择子节点（当节点过多，选择子节点的子节点将影响速度）
    this.selectCurrent = false;  //只选择当前节点
	//this.DefinedID = 0         //准备存放自定义开始节点
	this.menuHeight= document.body.offsetHeight-120; //显示菜单高度
	this.menuWidth = document.body.offsetWidth;  //显示菜单宽度
	this.menuTop   = 58;
	this.menuLeft  = 8;
	this.menuRight = 15;
	//image 
	this.imagePath  = "../../../image/tree/"; //图片存放路径 
	this.treeMid    = "tree_mid.gif";
	this.treeEnd    = "tree_end.gif";
	this.treeOpenM  = "tree_t_mid.gif";
	this.treeOpenE  = "tree_t_end.gif";
	this.treeClseM  = "tree_x_mid.gif";
	this.treeClseE  = "tree_x_end.gif";
	this.treeBgImg  = "tree_bg.gif";
	this.picArray   = new Array();
	this.picArray[0]= "pic_root.gif";//根节点图标 
	this.picArray[1]= "pic_dept.gif";// 
	this.picArray[2]= "pic_role.gif";// 
	this.picArray[3]= "pic_user.gif";// 
	this.scrolldown = "arrowdown.gif";
	this.scrollup   = "arrowup.gif"
	//css
	this.focusCss   = "font_master";//点击的文字效果;
	this.blurCss    = "";

	//currently Node
	this.currentId     = 0;
    this.currentValue  = '';
	this.currentName   = '';

	//method
	this.add        = addDepartChild;
	this.addArray   = addDepartArray;
	this.display    = displayDepartChild;
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
	this.getReturn  = getDepartReturnValue //返回 单选/多选中的值
	this.getSelect  = getClickNode         //点击节点后事件，可以定义同名函数覆盖
	this.setExtend  = setExtendNode
}

//根据某个节点展开父节点
function setExtendNode(inum){
	while(inum != -1){
		if(this.getObject('tab',inum+1) && this.getObject('tab',inum+1).style.display == 'none')
			this.switchTab(inum)
		inum = parseInt(this.DeptList[inum].fathId)
	}
}

//得到某个节点
function getClickNode(inum)
{
	return true;
	/*alert('node Name : '+ this.DeptList[inum].nodeName +'\n' +
		  'node Id   : '+ this.DeptList[inum].nodeId   +'\n' +
		  'node Level: '+ this.DeptList[inum].nodeLevel+'\n' +
		  'node Type : '+ this.DeptList[inum].nodeType +'\n' )
	*/
}

//得到返回树型列表
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

//开始节点
function departNode(arr)
{
	var  i =0;
	this.nodeLevel= arr[i++]; //节点层次
	this.nodeName = arr[i++]; //节点名称
	this.nodeUrl  = arr[i++]; //节点对应url
	this.target   = arr[i++]; //节点url指向的目标窗口名
	this.nodeId   = arr[i++]; //节点id
	this.nodeType = arr[i++]; //节点类型
	this.noSelect = arr[i++]; //是否可以选择
	this.promptInfo= arr[i++];//不改变以前接口
	this.helpUrl  = arr[i++]; //对应help url
	this.isLast   = arr[i++]; //是否尾节点
	this.fathId   = arr[i++]; //对应父节点所在DeptList中序号
	this.children = arr[i++]; //所有子节点数据string
	this.selfNum  = arr[i++];       //本身的序号
}

//连接节点
function linkNode(arr)
{
	var  i =0;
	this.fathId  = arguments[i++]; 
	this.level   = arguments[i++];
	this.brother = arguments[i++];
}

//增加树节点数组
function addDepartArray(arr)
{
	if(arr.length>1000 && !confirm('数据节点太多，可能造成机器运行缓慢，确实要进行吗？'))
		return
	if(arr.length>500 && this.TreeType == 2)
		this.SelectSon = true;
	for(var i=0;i<arr.length;i++)
	{
		var temp_arr     = arr[i].split(',');
		if(temp_arr[0]){
			var deptobj      = new this.node(temp_arr);
			this.DeptList[i]= deptobj;
		}else{
			alert(arr[i]+'节点数据不正确！');
			break;
		}
		//alert("i = "+i+" | fathid: "+this.DeptList[i].nodeLevel);
	}
}

//增加树型子节点
function addDepartChild(str)
{
	if(str == '')
		return false;
	var arr_str = str.split(',');
	var infoobj ;
	if(arr_str.length > 0 && arr_str[0] != "")
		infoobj = new this.node(arr_str);
	else{
		return false;
	}
	this.DeptList[this.DeptList.length] = infoobj;  
}

//---图标变换
function extendDepartTree(sid,imgid,flag)
{	
	//try{
		var subment     = this.getObject('tab',sid);
		if(!subment)
			return;
		var local_image = this.treeOpenM		
		var imgbtn      = this.getObject('img',imgid);
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
					local_image = this.treeOpenE;
			if(imgbtn)
				imgbtn.src=this.imagePath+local_image;
		}else{
			subment.style.display="none";
			if(this.DeptList[sid-1].isLast==-1)
				local_image = this.treeClseE;
			else 
				local_image = this.treeClseM;
			if(imgbtn)
				imgbtn.src=this.imagePath+local_image;
		}
	//}catch(e){alert(e)}
}

//构造树形节点集合的数组
function getNumTreeChild()
{
	var ilen     = 0;
	var prelevel = -1;
	var temp_self= 0;
	var temp_str = '';
	var fath_id  = 0; 

	for(var i=0;i<this.DeptList.length;i++)
	{
		this.DeptList[i].selfNum =  i;
		var level = parseInt(this.DeptList[i].nodeLevel);
		this.DeptList[i].isLast=-1;
		//
		if(level > prelevel)//初始化新节点
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
		else if( level == prelevel)//对前面没有子节点的覆盖
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
					//加上尾节点信息
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

//--全部展开或者收缩
function switchDepartAll(temp)
{
	var temp_lists='';
	var temp_child=new Array();
	var temp_ch;
	/*--对节点收展变换--*/
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
				//if(this.LinkList[i1].level != 0)//第一层次不收缩
					this.extendTab(temp_ch+1,temp_ch,temp);	
			}
		}		
	}
	this.getHeight()/////////
}

//--伸缩树型结构。
function switchDepartTab(inum){
	//建立关联，定义伸缩范围。
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

//点击事件
function clickDepartChild(inum)
{
	if(this.DeptList[inum].noSelect)
		return ;
	if(!this.getSelect(inum))
		return;
	if(this.TreeType == 2)
	{
		if(this.getObject('box',inum).checked )
		   this.getObject('box',inum).checked = false;			
		else
		   this.getObject('box',inum).checked = true;
	}
	this.selectBox(inum);
}

//得到节点对象
function getDepartObject(txt,inum)
{
	if(inum || inum == 0)
	{
		return document.getElementById(this.TreeName + '_' + txt + '_' + inum) ;
	}
	else
	{
		return document.getElementById(this.TreeName + '_' + txt) ;
	}
}

//选择下属部门
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
			if(this.DeptList[temp].children && this.selectCurrent == false){
				var child = this.DeptList[temp].children.split(',');			
				for(var i=0;i<child.length;i++)
				{				
					if(this.DeptList[parseInt(child[i])].noSelect)
						break ;
					this.setClass(child[i],true);
					//if(!this.SelectSon)
						this.selectBox(child[i]);
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
			if(this.DeptList[temp].children && this.selectCurrent == false ){
				var child = this.DeptList[temp].children.split(',');			
				for(var i=0;i<child.length;i++)
				{				
					if(this.DeptList[parseInt(child[i])].noSelect)
						break ;
					this.setClass(child[i],false);
					//if(!this.SelectSon)
						this.selectBox(child[i]);
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
//设置表格
function setTableClass(inum,temp,flag)
{
	if(temp)
	{	
		if(!flag)
			this.getObject('box',inum).checked = true;
		this.getObject('tdId',inum).className  = this.focusCss ;
	}
	else{
		if(!flag)
			this.getObject('box',inum).checked = false;
		this.getObject('tdId',inum).className  = this.blurCss ;
	}
}

//节点选择
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

//--显示菜单树
function displayDepartChild()
{
	this.getTreeArr();//增加尾节点信息
	var init_num = 0;
	var init_txt = '<table width="100%"   border="0" cellpadding="0"  cellspacing="0" class="css_menu_depart">';	
	var tlen     = 0;
	var checked  = ''
    var name_str = '';
    var img_str  = '';
    var click_str = '';
	var tdClass   = '';
    var bgimage   = '';
	var bgimage1  = '';
	var promptInfo= '';
	for(var i=0;i<this.DeptList.length;i++){		
		name_str  = '';
		img_str   = '';
		click_str = '';	
		tdClass   = '';	
		checked   = '';
		bgimage   = 'background ="'+this.imagePath + this.treeBgImg +'"';
		bgimage1  = 'background ="'+this.imagePath + this.treeBgImg +'"';
		promptInfo= this.DeptList[i].nodeName;
		if(this.DeptList[i].promptInfo)
			promptInfo = this.DeptList[i].promptInfo;

		var ifather=parseInt(this.DeptList[i].fathId);      //父节点是否尾节点	
		if(ifather != -1){
			if(this.DeptList[ifather].isLast == -1)
				bgimage = '';
			var gfather=parseInt(this.DeptList[ifather].fathId);//祖父
			if( gfather ==-1 || this.DeptList[gfather].isLast == -1)
				bgimage1 = '';
		}else{	bgimage  = '';
		    bgimage1 = '';
		}
		//默认选中
		if(this.InitString.indexOf(','+this.DeptList[i].nodeId+',')!=-1 ){
			if(this.SelectId == -1 || this.TreeType == 2){
				checked          = 'checked';
				tdClass          = this.focusCss ;
				this.SelectId    = i;
				this.returnList[this.returnList.length] = this.DeptList[i];
			}
		}
		/*--树类型--*/
		switch(this.TreeType){
			case 0:
				var url = this.DeptList[i].nodeUrl;
				if(this.IsHelp){
					url = this.DeptList[i].helpUrl;
					this.DeptList[i].nodeType = (parseInt(this.DeptList[i].nodeLevel)+1).toString()
				}
				if(url){
					var targetName= '';//--add for xyh ||this.DeptList[i].target == '\'\'' || typeof this.DeptList[i].target == 'undefined'
					if(!this.DeptList[i].target) 
						targetName = 'mainFrame';
					else
						targetName = this.DeptList[i].target;	
					if(this.IsDeploy)
						name_str = '<a href="'+url+'" onclick="'+this.TreeName+'.clickNode('+i+');'+this.TreeName+'.switchTab('+(i)+')" '
					else
						name_str = '<a href="'+url+'" onclick="'+this.TreeName+'.clickNode('+i+')" '
					name_str += ' name="'+this.TreeName+i+'"  title="'+promptInfo+'"';
					if(this.DeptList[i].nodeUrl == '#')
						name_str += ' >'+this.DeptList[i].nodeName+'</a>'					
					else	
						name_str += ' target="'+targetName+'" >'+this.DeptList[i].nodeName+'</a>'
				}else
					name_str = '<a name="'+this.TreeName+i+'"></a>'+this.DeptList[i].nodeName 
				break;
			default : //type 1 | type 2 
			   var temp_nodeName = '';
				temp_nodeName = ' <a href="#" onclick="'+this.TreeName+'.clickNode('+i+');return false" name="'+this.TreeName+i+'" title="'+promptInfo+'">'+ this.DeptList[i].nodeName + '</a>';
				if(this.DeptList[i].noSelect ) //不可选 && this.TreeType == 1
					name_str = temp_nodeName
				else 	
				{
					name_str='<input type="checkbox" value="'+i+'" name="'+this.TreeName+'_checkbox"  id="'+this.TreeName+'_box_'+i+'" onclick="'+this.TreeName+'.selectBox('+i+')" ';
					name_str += checked+'>'+ temp_nodeName  ;
				}
				break;
		}

		/*--展开或者收缩节点图标--*/
		if(this.DeptList.length>(i+1))
		{	
			if(parseInt(this.DeptList[i+1].nodeLevel) > parseInt(this.DeptList[i].nodeLevel))
			{	
				click_str='onclick="'+this.TreeName+'.switchTab('+(i)+')"';
				if( this.DeptList[i].fathId == -1 && this.DeptList[i].isLast == -1) 
					img_str = '<img src = "'+this.imagePath + this.treeOpenE +'" '
				else if( this.DeptList[i].fathId == -1 && this.DeptList[i].isLast != -1)
					img_str = '<img src = "'+this.imagePath + this.treeOpenM +'" '
				else if( this.DeptList[i].isLast == -1)
					img_str = '<img src = "'+this.imagePath + this.treeClseE +'" ';			
				else
					img_str = '<img src = "'+this.imagePath + this.treeClseM +'" ';
				img_str += ' " align="absmiddle"  id="'+this.TreeName+'_img_'+i+'" '+click_str+' style="cursor:hand" title="伸缩菜单">'; 
			}
			else  if(parseInt(this.DeptList[i+1].nodeLevel) < parseInt(this.DeptList[i].nodeLevel))
				img_str='<img src="'+this.imagePath + this.treeEnd +'"  align="absmiddle" >';
			else  
				img_str='<img src="'+this.imagePath + this.treeMid + '" align="absmiddle" >';
		}else   
			img_str='<img src="'+this.imagePath + this.treeEnd +'" align="absmiddle" >';

		/*----*/
		var ii = this.DeptList[i].nodeType != "" || this.DeptList[i].nodeType == "0" ? parseInt(this.DeptList[i].nodeType) : 99;
		if(this.picArray[ii])
			img_str += '<img src=" '+this.imagePath + this.picArray[ii]+'"  align="absmiddle" >';
		/*----*/

		if( init_num == parseInt(this.DeptList[i].nodeLevel) )
		{
			if(this.DeptList[i].nodeLevel==0)//当顶层时去掉前面的树形
				init_txt+='<tr  id="'+this.TreeName+'_tab_'+(i)+'"><td id="'+this.TreeName+'_tdId_'+i+'" class="'+tdClass+'">'+img_str+name_str+' \n </td></tr>';
			else
				init_txt+='<tr  id="'+this.TreeName+'_tab_'+(i)+'"><td width="15" '+bgimage+' height="22"></td><td id="'+this.TreeName+'_tdId_'+i+'" class="'+tdClass+'">'+img_str + name_str+' \n </td></tr>';
		}
		else if( parseInt(this.DeptList[i].nodeLevel) > init_num)
		{	
			if(init_num==0)// 
				init_txt+='<tr id="'+this.TreeName+'_tab_'+(i)+'"><td>';
			else
				init_txt+='<tr id="'+this.TreeName+'_tab_'+(i)+'" style="display:none"><td '+bgimage1+' width="15"></td><td>';
			init_txt+='    <table width="100%"  border="0" cellpadding="0"  cellspacing="0"><tr><td width="15" height="22" '+bgimage+'></td><td id="'+this.TreeName+'_tdId_'+i+'" class="'+tdClass+'">'+img_str+name_str+' \n </td></tr>';
		}
		else if(parseInt(this.DeptList[i].nodeLevel) < init_num)
		{
			for(var j=0;j<init_num-parseInt(this.DeptList[i].nodeLevel);j++)
				init_txt+='</table></td></tr>';

			if(parseInt(this.DeptList[i].nodeLevel) == 0)//
				init_txt+='<tr id="'+this.TreeName+'_tab_'+(i)+'" ><td id="'+this.TreeName+'_tdId_'+i+'" class="'+tdClass+'">'+img_str+name_str+' \n </td></tr>';
			else				
				init_txt+='<tr id="'+this.TreeName+'_tab_'+(i)+'" ><td width="15" height="22" '+bgimage+' ></td><td id="'+this.TreeName+'_tdId_'+i+'" class="'+tdClass+'">'+img_str+name_str+' \n </td></tr>';
		}
		init_num= parseInt(this.DeptList[i].nodeLevel);
	}
	for(var j=0;j<init_num;j++)
		init_txt+='</table></td></tr></table>';
	
	document.getElementById(this.Address).innerHTML = init_txt ; 
	//
	//if(this.SelectId != -1)
	//	this.setSelect(this.SelectId);
	for(var i=0;i<this.returnList.length;i++)
		this.setSelect(this.returnList[i].selfNum);

	if(this.IsScroll){
		this.showButton();
		this.getHeight();
	}
}

//===================模拟滚动条方法==============

//--显示滚动按钮
var global_scroll_flag = '';
function showMenuButton()
{	
	var str="<div id='divId_imgbtn' style='position:absolute;top:"+this.menuTop+";left:"+this.menuLeft+";width:94%;z-index:110;visibility:hidden;clip:rect(0 auto auto 0)'>"+ //"+this.TreeName+".clickScroll(this) onMouseDown='"+this.TreeName+".clickScroll(this)' onMouseOver='OverArrow(this)' onMouseOut='OutArrow(this)'
	"<img alt='向下滚动' onmousedown='global_scroll_flag = setInterval(\""+this.TreeName+".selScroll(	1)\",1)' onMouseup='clearInterval(global_scroll_flag)'  id='imgbtnId_down' " +
	"src='"+this.imagePath + this.scrolldown + "' style='position:absolute;top:"+this.menuHeight+";right:"+this.menuRight+";cursor:hand;visibility:hidden;z-index:500'>"+
	"<img alt='向上滚动' onmousedown='global_scroll_flag = setInterval(\""+this.TreeName+".selScroll(2)\",-1)' onMouseup='clearInterval(global_scroll_flag)'   id='imgbtnId_up' " +
	"src='"+this.imagePath + this.scrollup + "' style='position:absolute;top:0;right:"+this.menuRight+";cursor:hand;visibility:hidden;z-index:500'>"+
	"</div>";
	document.write(str);
	//按钮定位
	//document.all["imgbtnId_down"].style.pixelTop=this.menuHeight;//this.menuTop+ 108 document.all["divId_imgbtn"].style.pixelTop
}

//自动调节菜单高度
function getInitMenuHeight()
{	
	if(this.IsScroll)
	{
		if(this.PageHeight!=this.menuHeight)
		{
			var clip = this.getClipValue(this.Address);
			var mwidth=this.menuWidth-20;
			if(mwidth <200)//--避免字符截断
				mwidth = 200;
			var mheight=this.menuHeight;//105
			document.getElementById(this.Address).style.clip=clip='rect('+clip[1]+' '+mwidth+' '+(parseInt(clip[1])+mheight)+' '+clip[4]+')';
			this.PageHeight = this.menuHeight;
		}
		this.dispScroll()
	}
}
//判断是否出现按钮
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

//实现滚动
function getMenuScrolling(value)
{
	try{
		document.getElementById(this.Address).style.pixelTop+=value;
		var clip= this.getClipValue(this.Address)
		this.getClip((parseInt(clip[1])-value),parseInt(clip[2]),(parseInt(clip[3])-value),parseInt(clip[4]));
	}catch(e)
	{}
}
//得到树菜单
function getClipMenu(top,right,bottom,left)
{
	document.getElementById(this.Address).style.clip=clip='rect('+top+' '+right+' '+bottom+' '+left+')';
}

//得到树型节点值
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

//--按钮事件--

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
