
/**
 * 可输入下拉选择框控件, 后台实现参见 {@link com.coheg.web.view.taglibs.ExtendComboTextTag} 和 {@link com.coheg.web.view.taglibs.ExtendComboCompositeTag}
 * @author pillarliu
 * @version $Id: extendCombo.js,v 1.1 2010/12/10 10:56:35 silencily Exp $
 *
 * 注意这里依赖 styleControl.js 的实现
 * 精简后不再支持 多选
 */

if (ExtendCombo == null) {
	var ExtendCombo = {};
}

ExtendCombo.txtObj;				// 输入框对象
ExtendCombo.hidObj;				// 隐藏对象
ExtendCombo.btnObj;				// 按钮对象
ExtendCombo.tempList    = '';	// 中转变量
ExtendCombo.tempArray   = '';	//保存多选的值
ExtendCombo.initArray = new Object()// 数组缓存--第一次加载后重复利用
ExtendCombo.inputContent= '';
ExtendCombo.defFunction  = null;//提供自定义的函数
ExtendCombo.hostUrl    = "";
ExtendCombo.splitNode  = "###";//节点与节点间分隔
ExtendCombo.splitField = "|||";//属性间分隔

ExtendCombo.selectWin   = null; 
ExtendCombo.isIE6       = false;
if(typeof ContextInfo.checkExplorer != 'undefined' && ContextInfo.checkExplorer(5.5))
{
	ExtendCombo.isIE6   = true;
	ExtendCombo.selectWin = window.createPopup(); //
}

if(typeof displaySelectLayer != 'object'){
	document.writeln('<iframe id=displaySelectLayer frameborder=0 style="border:0px;position: absolute; width: 160px; height: 0px; z-index: 9998; display: none;" scrolling=yes></iframe>');
	document.close();	
}


/**
 * 将取到的数组弹出窗口显示出来
 * 默认组装的数据格式上是   id1|||name1|||other1,,,id2|||name2|||other2
 * @param temp_obj  	欲填充的文本框对象名称, 保存Name		
 * @param temp_btn  	文本框或按钮对象引用		
 * @param optionInfos	从服务器取得或者前端构造的数组
 * @param nextPage      分页开始。默认为0	
 后来扩展：
 * @param arguments[4]  存放Id的hidden对象名
 * @param arguments[5]  显示数据数目，默认为10出现分页，其他数字为出现滚动条
 * @param arguments[6]  用于多选中，保存已经选中的字符串（以‘ ; ’隔开）。默认为输入框this.value
 * @param arguments[7]  是否屏蔽“置空” 默认为false /  true 则不出现“置空”
 */
ExtendCombo.setMenu = function (temp_obj,optionInfos,nextPage, temp_btn,temp_hidd,temp_count,multStr,isClear,fun){
	//自定义函数
	ExtendCombo.defFunction = fun ? fun : null;
	if(isNaN(nextPage))
		return false;
	//if('button3' == temp_obj.className) 
	//	return false;
	if(!optionInfos)
		return false;
	ExtendCombo.tempList = optionInfos;	
	var th = temp_btn;	
	ExtendCombo.txtObj = (arguments.length == 1) ? th : temp_obj
	ExtendCombo.btnObj = (arguments.length == 1) ? null : th;	//设定外部点击的按钮
	ExtendCombo.hidObj = (arguments.length >= 5 && typeof arguments[4] == 'object') ? arguments[4] : null;
		
	var inteval     = 10;   //每页数
	var isPageCount = true; //是否分页
	var resertBtn   = arguments[7] ? arguments[7]:false;
	//-- 增加参数6,arg5 !=10 则显示所有信息,出现滚动条,自动调节宽度 --参数5预留给system 2004-08-20 liuz
	var arg5        = (arguments.length >= 6)?parseInt(arguments[5]):10
	if(arg5 != 10)
		isPageCount = false;
	var selBox      = false; //是否多选
	//-- 参数7 多选,存放已选项string  2004-09-30 liuz
	var arg6        = (arguments.length == 7)?arguments[6]:'null';
	if(arg6 != 'null' ){
		selBox      = true;
		if(nextPage == 0){
			ExtendCombo.tempArray = arg6;
			if(ExtendCombo.hidObj){
				ExtendCombo.tempArray = '';
				var hid_temp = ExtendCombo.hidObj.value.split(';');
				var txt_temp = ExtendCombo.txtObj.value.split(';');
					for(var i=0;i<hid_temp.length;i++)
						if(hid_temp[i]!='')
							ExtendCombo.tempArray += '**'+hid_temp[i]+ExtendCombo.splitField+txt_temp[i];
			}else
				ExtendCombo.tempArray = ExtendCombo.tempArray.replace(/\;/gi,'**');
		}
	}			
	var local_page_width  = (temp_obj == th )? temp_obj.clientWidth+20 : temp_obj.clientWidth + th.clientWidth+5;
	var slen			  =  5  //页面宽度
	var shei			  =  25 //页面高度
	var local_page_scroll = 'hidden';
	var local_page_height =  210;
	var table_width       = '100%';   
	if(!isPageCount){		
		local_page_scroll = 'auto'
		table_width       = '100%'
	}
	
	var showNum   = 4;     //分页
	var allCount  = optionInfos.length;
	var reg =/[\u4E00-\u9FA5]|[\uFE30-\uFFA0]/gi
	if(!isPageCount)
		inteval   = allCount 
	var pageCount = parseInt(allCount / inteval, 10) + 1;
	if(allCount % inteval == 0) 
		pageCount = pageCount -1 ;
	if(nextPage < 0)
		nextPage = 0;
	if(nextPage >= pageCount)
		nextPage = pageCount - 1;		
	var begin_list = nextPage * inteval;
	var end_list = begin_list + inteval;
	if(end_list > allCount)
		end_list = allCount;
	var tabTableString  = '';
	var tabSelectString = '';
	//针对原型的工作 location.href.indexOf("///")>0 ? ContextInfo.getDemoContext() : 
	var contextPath = ContextInfo.contextPath ;
	var tabHtmlString   = '<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN"><head><link href="'+contextPath+'/css/style.css" rel="stylesheet" type="text/css">';
	tabHtmlString += ' <meta http-equiv="Content-Type" content="text/html; charset=GBK"/>';
	//tabHtmlString += '<style>body{margin-left:1px;margin-top:1px;margin-right:1px;margin-bottom:0px}</style>';
	tabHtmlString += '</head><body bgColor="pageLineColor" style="overflow:hidden">';//style="overflow-x:hidden;overflow-y:"+local_page_scroll+"'
	
	tabTableString += '<table cellpadding="1" cellspacing="1" width="'+table_width+'" bgcolor="pageLineColor" id="tableId_selectList"> \n'; // 100% -> 160		
	if(0 != allCount){				
		for(var i = begin_list; i < end_list; i++){
			var filter_str = (optionInfos[i]+"").replace(/\<[^>]*>/gi,"");
			filter_str = escape(filter_str);
			var  dmName = ExtendCombo.getArray(optionInfos[i])
			var  dmName1 = dmName.replace(/\<[^>]*>/gi,"");
			if(selBox){	
				if(!reg.test(dmName1))
					slen = (dmName1.length/2 +1 <= slen)?slen:(dmName1.length/2.5 +1)
				else
					slen = ((dmName1.length+2)<= slen)?slen:(dmName1.length+2)
				var txtvalue = ';'+temp_obj.value+';'; //回选
				if(txtvalue.indexOf(';'+dmName+';')>=0){
					tabTableString += '<tr id='+i+' bgcolor="pageMainColor"><td  style="background-color:pageClickColor" id="td_class_'+i+'" onclick="parent.fc_setBoxCheck(getElementById(\'box_'+i+'\'),this)"  style="cursor:hand" height="22" >';
					tabTableString += '<input type="checkbox" name="box_item" value="'+optionInfos[i]+'" checked onclick="parent.fc_setBoxCheck(this,td_class_'+i+')" id="box_'+i+'">';
				}else{
					tabTableString += '<tr  bgcolor="pageMainColor"><td  id="td_class_'+i+'" onclick="parent.fc_setBoxCheck(getElementById(\'box_'+i+'\'),this)" style="cursor:hand" height="22">';//title="'+optionInfos[i]+'"
					tabTableString += '<input type="checkbox" name="box_item" value="'+optionInfos[i]+'"  onclick="parent.fc_setBoxCheck(this,td_class_'+i+')" id="box_'+i+'">';
				}
				tabTableString +=  dmName;
				tabTableString += '</td></tr>';
			}else{						
				if(!reg.test(dmName1))
					slen = (dmName1.length/2 <= slen)?slen:(dmName1.length/2.5)
				else						
					slen = (dmName1.length <= slen)?slen:(dmName1.length)
				if(temp_obj.value == dmName)
					tabTableString += "<tr id="+i+" height=\"22\" bgcolor='pageClickColor' ";
				else
					tabTableString += "<tr id="+i+" height=\"22\" bgcolor= 'pageMainColor' onMouseOver=\"this.bgColor=\'pageClickColor\'\" onMouseOut=\"this.bgColor='pageMainColor'\"";
				tabTableString += " style=\"cursor:hand;\">";
				tabTableString += "<td onclick=\"parent.ExtendCombo.getValue(\'"+filter_str+"\',document.getElementById('spanId_"+i+"').innerText);\">&nbsp;<span id='spanId_"+i+"'>"+dmName +"</span><\/td>";
				tabTableString+="</tr> \n ";
			}
		}
		shei = (end_list - begin_list)*23 + 28
	}		
	tabSelectString += "<tr id="+i+" height=\"22\" bgcolor='pageClickColor' align='right'>";	
	tabSelectString += "	<td style=\"cursor:hand;\" title=\"将输入框置空\" class='article_copyright'>";//<img src='"+ContextInfo.contextPath+"image/shenhe_wei1.gif' align='absmiddle' border=0> <img src='"+ContextInfo.contextPath+"image/shenhe_no.gif' align='absmiddle' border=0>
	if(selBox)
		tabSelectString +=   "<a href='#' onclick='parent.ExtendCombo.getValue(parent.ExtendCombo.tempArray)'>选取</a> ";
	/**if(!resertBtn)*/
	if(!isClear)
		tabSelectString +=   "<a href='#' onclick='parent.ExtendCombo.getValue(\"\")'>置空</a> ";
	if(local_page_width > 100)
		tabSelectString +=   "<a href='#' onclick='parent.closeSelectMenu()'>关闭</a>&nbsp;";
	tabSelectString +="<\/td></tr>";	
	if(isPageCount)
		var tabString = tabHtmlString + tabTableString + tabSelectString;
	else{
		var tabString = '';
		if(shei > local_page_height)
			tabString = tabHtmlString + '<div style="overflow-y:auto;height:'+(local_page_height-26)+'px">'+tabTableString+
					  '</table></div><table cellpadding=\"1\" cellspacing=\"1\" width=\"'+table_width+'\" bgcolor="pageLineColor">'+tabSelectString;
		else
			tabString = tabHtmlString + tabTableString + tabSelectString;
	}
	if(pageCount > 1){
		tabString+="<tr  bgcolor='pageClickColor'><td align='right' height=\"22\">";
		tabString+="<img src='"+contextPath+"/image/main/scroll_left.gif' onclick='parent.ExtendCombo.setMenu(parent.ExtendCombo.txtObj,parent.ExtendCombo.tempList," + (nextPage - 2*showNum ) + ", parent.ExtendCombo.btnObj,parent.ExtendCombo.hidObj,"+arg5+",\""+arg6+"\");' style=\"cursor:hand;\" align=\"absmiddle\">";
		var beginPage=nextPage - showNum;
		var maxPage = nextPage + showNum
		if(beginPage <= 0){					
			maxPage -= beginPage;
			beginPage = 0;
		}
		if(maxPage > pageCount){					  
			beginPage-=maxPage-pageCount						
			maxPage = pageCount;
		}
		if(beginPage < 0)
			beginPage=0		
		for(var i = beginPage; i < maxPage; i++ ){
			if(i == nextPage)
				tabString+="&nbsp;<a href='#' onclick='parent.ExtendCombo.setMenu(parent.ExtendCombo.txtObj,parent.ExtendCombo.tempList," + i + ", parent.ExtendCombo.btnObj,parent.ExtendCombo.hidObj,"+arg5+",\""+arg6+"\",null,parent.ExtendCombo.defFunction)'><font color=red>" + (i + 1) + "</font></a>";
			else
				tabString+="&nbsp;<a href='#' onclick='parent.ExtendCombo.setMenu(parent.ExtendCombo.txtObj,parent.ExtendCombo.tempList," + i + ", parent.ExtendCombo.btnObj,parent.ExtendCombo.hidObj,"+arg5+",\""+arg6+"\",null,parent.ExtendCombo.defFunction)'>" + (i + 1) + "</a>";
		}				
		var temp_nextPage=nextPage+showNum*2 //(parseInt(nextPage))<!---->
		if(temp_nextPage>pageCount) temp_nextPage=pageCount;
		if(temp_nextPage<0)         temp_nextPage=0;
		tabString+="&nbsp;<input type='hidden' value='"+temp_nextPage+"' name='txt_page_num' style='width:20px'><img src='"+contextPath+"/image/main/scroll_right.gif' onclick='parent.ExtendCombo.setMenu(parent.ExtendCombo.txtObj,parent.ExtendCombo.tempList,txt_page_num.value-1, parent.ExtendCombo.btnObj,parent.ExtendCombo.hidObj,"+arg5+",\""+arg6+"\")' style=\"cursor:hand;\" align=\"absmiddle\">";
		tabString+="</td>";	
		tabString+="</tr>";
		shei += 22
	}	
	tabString+="</table></body></html>"
	if(!isPageCount)
		local_page_height = (shei < local_page_height)?shei:local_page_height;
	else
		local_page_height = shei;
	local_page_width  = (local_page_width >(slen * 12 + 20))?local_page_width : (slen * 12 + 20);
	if(local_page_width>400){
		local_page_width=400;
	}
	StyleControl.displaySelectMenu(tabString,temp_obj,temp_btn,local_page_width,local_page_height,isPageCount)
}

/**返回选取的值
 * @param content 传入的内容
 */
ExtendCombo.getValue = function (content,dmstring){
	content = unescape(content);
	if(content != ""){	
		if(-1 == content.indexOf(ExtendCombo.splitField)){
			ExtendCombo.txtObj.value = content;
		}else{
			var arrayTemp = content.split(ExtendCombo.splitField);
			var dmId   = '';
			var dmName = '';			
			dmId   = arrayTemp[0];
			dmName = dmstring;//arrayTemp[1]
			ExtendCombo.inputContent = dmName;
			if(ExtendCombo.hidObj){//如果没有改变值将不再继续				
				ExtendCombo.hidObj.value = dmId;
			}
			ExtendCombo.txtObj.value = dmName;	
			/*
			if(!ExtendCombo.inputContent){
				ExtendCombo.hidObj.value = '';
				ExtendCombo.txtObj.value = '';	
			}*/
		}
		ExtendCombo.txtObj.select();		
	}else{
		ExtendCombo.txtObj.value = '' ;
		if(ExtendCombo.hidObj)
			ExtendCombo.hidObj.value = '';		
	}
	if(ExtendCombo.defFunction){
		eval(ExtendCombo.defFunction(content));
	}
	ExtendCombo.onSelect(ExtendCombo.txtObj,content);
	closeSelectMenu();
}

/*-- 返回选中的值 
 * 重构该方法,具体使用如 级连中构造关连性
 * obj 选择输入框对象 
 * str 选中返回值（包括多选）
 */
ExtendCombo.onSelect = function (obj,str){}


/**
 * 从树上选择代码, 将选中值的自动填充到输入框中, field 包括 code, name, description
 * @param systemModuleName 子系统模块名
 * @param codeWrapperObjectPrefix 页面上 codeWrapper 类型对象的前缀
 * @param type 所属类型代码, 即上级代码
 * @param fieldName todo
 * @param fieldCode todo
 */
ExtendCombo.selectCodeFromTree = function(systemModuleName, codeWrapperObjectPrefix, type, fieldName, fieldCode) {
	var url = ContextInfo.contextPath + '/common/systemcode.do?step=frame&systemModuleName=' + systemModuleName + '&entryStep=listForSelect&oid=' + type;
	if (fieldName && fieldCode) {
		url += '&fieldName=' + fieldName + '&fieldCode=' + fieldCode;
	}	
	definedWin.openListingUrl(codeWrapperObjectPrefix, url);
}

/**
 * 从下拉框中选择代码
 * @param systemModuleName 子系统模块名
 * @param hiddenObject 隐藏框对象
 * @param textObject 文本框对象
 * @param buttonObject 按钮对象
 * @param type 所属类型代码, 即上级代码  
 */
ExtendCombo.selectOptions = function(systemModuleName, hiddenObject, textObject, buttonObject, type) {
	ExtendCombo.hostUrl = ContextInfo.contextPath + '/common/systemcode.do?step=comboSupportList&systemModuleName=' + systemModuleName + '&timpstamp=' + DateUtils.GetDBDate().getTime() + '&parentCode=';
	ExtendCombo.getOptions(textObject, buttonObject, hiddenObject, type);
}


//下拉菜单
/*
HrUtils.selectOptions = function(textObj,btnObj,itemName){
	ExtendCombo.hostUrl = "<c:url value="/hr/CodeTableAction.do?step=getTreeNode&pageUrl=/jsp/common/codetable/options.jsp&id="/>";
	ExtendCombo.getOptions(textObj,btnObj,'',itemName);
}
*/


//--这个层的关闭
function closeSelectMenu(){	
	ExtendCombo.tempArray = '' ;//清空多选值
	if(ExtendCombo.isIE6)
		ExtendCombo.selectWin.hide();
	else
		document.getElementById("displaySelectLayer").style.display="none";
}


//--构造xmlhttp取数据的代码 与 ID 对应
function getArrayMap(){
	var arrayMapping = new Array();	
	arrayMapping.add(new codeMapping('xb',   20001));//性别
	return arrayMapping ;
}
//重新给映射赋值
function codeMapping(itemName, fatherId){
	this.itemName = itemName;
	this.fatherId = fatherId;
}

//--从后端获取数据
ExtendCombo.getXmlhttpString = function(itemName,level,flag){
	var kcmlData = '';	
	if(ExtendCombo.initArray && ExtendCombo.initArray[itemName])
		kcmlData = ExtendCombo.initArray[itemName];
	else{
		var url = ExtendCombo.hostUrl + itemName ; 
		var allContent = XMLHttpEngine.getResponseText(url, false);
		kcmlData = ExtendCombo.getArrayOptions(allContent);			
		ExtendCombo.initArray[itemName]=kcmlData;
	}
	return kcmlData;
}

/** 返回拆分后纯字符串排列
 * @param allContent 原始字符串内容
 * return retArray
 */
ExtendCombo.getArrayOptions = function(allContent){
	var retArray = new Array()
	var SEPERATOR = ",";
	try{
		//特殊字符过滤
		allContent = (allContent+"").replace(/&amp;40;/gi,"(");
		allContent = allContent.replace(/&amp;41;/gi,")");
		allContent = allContent.replace(/&amp;/gi,"&");   		
		if (allContent.indexOf(ExtendCombo.splitNode)>=0){
			SEPERATOR = ExtendCombo.splitNode;
			retArray = allContent.split(SEPERATOR);
		}else{
			retArray[0] = allContent  ;
		}  
	}catch(e){
		alert("拆分字符串错误" + e);
	}
	return retArray;	
}

/** 分离字段
* @param src 字符串
* @param num 顺序号
*/
ExtendCombo.getArray = function (src,num){
	var ret = '';	
	src = src + "";
	if(-1 == src.indexOf(ExtendCombo.splitField))
		ret = src.trim();
	else{
		var i   = (typeof num != 'undefined')?num:1;
		var temp = src.split(ExtendCombo.splitField);
		ret = temp[i];
	}
	return ret;
}


/** 在排列中索引关键字
 * @param keyWord 传入的关键字
 * return pos
 
function Array.prototype.indexOf(keyWord){
   var pos = - 1;
   for(var i = 0; i < this.length; i++){
      if(keyWord == this[i]){
         pos = i;
         break;
      }
   }
   return pos;
}*/

/** 在排列中增加内容
 * @param content 内容
 
function Array.prototype.add(content){
	this[this.length] = content;
}*/

/** 字符串排序
 */
String.prototype.trim = function(){
   return this.replace(/(^(\s|\u3000)*)|((\s|\u3000)*$)/g, '');
}


/** 控件的查询功能，将匹配的用红色标志*/
ExtendCombo.query = function (kcmlData,txtObj,btnObj){
	if(btnObj == txtObj  && txtObj.value !='' ) {
		var j = 0;
		var findData = new Array();
		for(var i = 0; i < kcmlData.length; i++) {
			var temp =(kcmlData[i]+'').split(ExtendCombo.splitField);
			var str = (temp.length >1)?temp[1].toString():temp[0].toString(); 
			str = str.replace(/\<[^>]*>/gi,""); //将所有<>过滤掉
			str = str.replace(/&amp;40;/gi,"(");
			str = str.replace(/&amp;41;/gi,")");
			//str = str.replace(/&amp;/gi,"&");
			str = str.replace(/&{1}[a-z]{1,};{1}/gi,"");//过滤html特殊字符
			if(str.indexOf(txtObj.value) >= 0) {
				var showData = str;
				showData = showData.replace(txtObj.value, '<font color = red>' + txtObj.value + '</font>');	
				showData = kcmlData[i].replace(str,showData)
				findData[j] = showData;
				j++;
			}
		}
		kcmlData = findData;
	}
	return kcmlData;
}

/*  显示控件多选功能
 *  2004-11-10 update Liuz  
 *  2004-09-30 Add    Liuz
 */
//--选中是变换 和 改变值
function fc_setBoxCheck(ftm,tdobj){
	if(ftm.checked){
		tdobj.style.backgroundColor = '';
		ftm.checked     = false;
		fc_delSelectItems(ftm.value);
	}else{
		tdobj.style.backgroundColor = '#ffffff';
		ftm.checked     = true;		
		fc_addSelectItems(ftm.value);
	}
}
//--增加新值
function fc_addSelectItems(str){
	var flag  = true;
	var temp = ExtendCombo.tempArray.split('**');
	for(var i=0;i<temp.length;i++){
		if(temp[i]&&str.indexOf(temp[i])>=0){
			flag =false;break;
		}
	}
	if(flag)
		ExtendCombo.tempArray  += '**'+str;
}
//--减少值
function fc_delSelectItems(str){
	var temp = ExtendCombo.tempArray.split('**');
	for(var i=0;i<temp.length;i++){
		if(temp[i]&&str.indexOf(temp[i])>=0){
			ExtendCombo.tempArray = ExtendCombo.tempArray.replace(temp[i],"");
			break;
		}
	}
}


/*---提供调用接口---*/

/**
 * 取得公用代码表的内容, 包括Id和Name
 * @param txtObj  		欲填充name的文本框对象 
 * @param btnObj  		按钮或文本框对象
 * @param hiddObj		欲填充id  的hidden域对象
 * @param itemName		欲取得的代码类型, 可直接传递father_id
 * @param arg[4]        =this.value,用于多选的回选内容
 * @param arg[5]        是否屏蔽“置空”按钮 默认false(为空) / true 则屏蔽 
 * @return				set页面中的文本域和hidden域
 */
ExtendCombo.getOptions = function(txtObj, btnObj, hiddObj, itemName,arg4,arg5) {
	var optionsData;  //中转数组					
	var kcmlData = ExtendCombo.getXmlhttpString(itemName);
	optionsData = ExtendCombo.query(kcmlData,txtObj,btnObj)
	ExtendCombo.setMenu(txtObj,optionsData,0, btnObj,hiddObj,10,arg4,arg5)
}

/* 在前端构造控件,支持前端搜索 
 * 可以使用ExtendCombo.initArray['itemName']
 */
ExtendCombo.getOptionStatic = function (txtObj,btnObj,hiddObj,arr,fun,isCountPage,isClear){
	//alert(txtObj+"<==>"+btnObj+"<==>"+hiddObj+"<==>"+arr+"<==>"+fun+"<==>"+isCountPage+"<==>"+isClear);
	if(arr == '')
		return;
	arr = ExtendCombo.query(arr,txtObj,btnObj);
	//是否使用分页，10为分页，11为不分页
	isCountPage = arr.length > 30 ?  10 : 11 ;
	ExtendCombo.setMenu(txtObj, arr,0,btnObj,hiddObj,isCountPage,'',isClear,fun);
}






/*-- 检查数据合法 --*/
/* @params txtname  输入框的名称 
 * @params codename 对应代码的名称 'jlxy','jg'...
 */
function checkGetOptionValue(txtname,codename){
	return checkTextSelected(codename,txtname);
}

/* 文本选择
 * @params item 对应代码的名称
 * @params txt     输入框的值
 */
function checkTextSelected(item,txt){
	if(checkOptionsWithIdValue(document.all(txt),'',item))
		return true;
	else{
		alert('输入的 " '+txt+' " 数据在数据库中不存在！\n 请选择适合的数据。');
		getOptionsDefined(obj,obj,'',item)
		return false;
	}
}

/** 检查代码合法性 2005-04-14  改进以上方法直接使用扩展本方法
 *  obj   输入框对象
 *  hid   隐藏域对象
 *  item  代码名字:jg,xb 或者代码
 */
function checkOptionsWithIdValue(obj,hid,item){
	if(obj.value != ''){
		if(! ExtendCombo.initArray || ! ExtendCombo.initArray[item])
			//getOptions(obj,obj,txtname);
			getOptionsDefined(obj,obj,hid,item)
		for(var i = 0; i < ExtendCombo.initArray[item].length; i++) {
			var temp = ExtendCombo.initArray[item][i].split(ExtendCombo.splitField);
			if(hid && temp.length>1 && temp[1]==obj.value && temp[0]==hid.value)
				return true;
			if(!hid && temp.length>1 && temp[1] == obj.value)
				return true;
			else if(temp.length == 1 && temp[0]==obj.value)
				return true;
		}
	}
	return false;
}

/** 10-14 补充固定的选项 */
function fc_selectOptions(txt,btn,itype,ibegin,iend)
{
	var year_arr = new Array();
	var j = 0; 	
	iibegin = (ibegin +'' != 'undefined')? ibegin : 1980 ;
	iiend   = (iend   +'' != 'undefined')? iend   : 2010 ;
	if(txt.value !=''){
		if(!ibegin&&ibegin != 0)
			iibegin = parseInt(txt.value) - 30;	
		if(!iend)
			iiend   = parseInt(txt.value) + 30
	}
	for(var i=iiend ; i>=iibegin; i--){
		year_arr[j++] = i+'';
	}
	ExtendCombo.setMenu(txt,year_arr,0,btn,'',j);
}

//--检查输入合法数据
function fc_checkInput(txt,btn,itype,ibegin,iend){
	if(txt.value!=''){
		if(isNaN(txt.value)){
			alert('格式有错误！');txt.select();
			return false;
		}
		switch(itype){
			case 'year':
				if(ibegin && parseInt(txt.value)<ibegin)
					return error_msg(txt,'数据不在范围之内');
				if(iend && parseInt(txt.value)>iend)
					return error_msg(txt,'数据超过范围');
				break;
			case 'number':
				if( parseInt(txt.value)>iend || parseInt(txt.value)<ibegin )
				{	
					alert('请输入限定范围之内的数值');txt.select();
					return false;
				}				
				break;
			case '':
				break;
		}
	}
	return true;
}

function getTextareaValue(str,flag,txt,hidd){
	document.all(txt).value = str;
	if(flag)
		document.all(hidd).value = str;	
}

ExtendCombo.basicChangeByOther = function (typeCode,subId,name,parentName){
	//0610修改需求，无初始值
	//增加置空的关联操作,置空以后，目标下拉不可动

	var url = ContextInfo.contextPath + "/common/basiccodemanager.do?step=getBasicCodeBySubIdAndCode";
	var params = "&typeCode=" + typeCode;
	params += "&subId=";
	params += subId;
	var temp = XMLHttpEngine.getResponseText(url, false, 'POST', params);	
	
	//修改2个HIDDEN
	document.getElementsByName("hiddenValueDefault" + name)[0].value = '';
	document.getElementsByName(name)[0].value = '';
	
	//TEXT框的VALUE
	var tempTextValue = "";
	var beginIndex = temp.indexOf("|||");
    var endIndex = temp.indexOf('"',beginIndex);
	tempTextValue = temp.substr(beginIndex+3,endIndex-beginIndex-3);
	document.getElementsByName("temp." + name)[0].value = '';
	
	//TEXT框的VALUE的JAVASCRIPT
	var tdObject = document.getElementsByName(name)[0].parentElement;
	var tdHtml = tdObject.innerHTML;
	var regtxtStr = " = \\[.*\\]";
	var chageValue = " = [" + temp + "]";
	var regtxt = new RegExp(regtxtStr,"gi");
	tdHtml = tdHtml.replace(regtxt,chageValue);	
	document.getElementsByName(name)[0].parentElement.innerHTML = tdHtml;
	var codeString = "";
	var beginIndex1 = tdHtml.lastIndexOf("ExtendComboText.source");
    var endIndex1 = tdHtml.indexOf("<\/SCRIPT>");
    codeString = tdHtml.substr(beginIndex1,endIndex1-beginIndex1);
	eval(codeString);
	try{
		//如果主下拉被清空，从被禁用
	    var parentValue = document.getElementsByName(parentName)[0].value;
		if(''==  parentValue){
				//禁用从下拉框
	            document.getElementsByName("temp." + name)[0].readOnly=true;
	            document.getElementsByName("temp." + name)[0].disabled=true;
				var parObj =  document.getElementsByName("temp." + name)[0].parentElement;
				var objAll = parObj.getElementsByTagName("*");
				for( i=0;i<objAll.length;i++) {
					if((objAll[i].tagName == "INPUT")){
						objAll[i].disabled=true;
					}
				}
		}else{
				//启用从下拉框
	            document.getElementsByName("temp." + name)[0].readOnly=false;
	            document.getElementsByName("temp." + name)[0].disabled=false;
				var parObj =  document.getElementsByName("temp." + name)[0].parentElement;
				var objAll = parObj.getElementsByTagName("*");
				for( i=0;i<objAll.length;i++) {
					if((objAll[i].tagName == "INPUT")){
						objAll[i].disabled=false;
					}
				}	
		}	
	}catch(e){}
	return;
}



