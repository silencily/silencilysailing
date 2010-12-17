
/**
 * ����������ѡ���ؼ�, ��̨ʵ�ֲμ� {@link com.coheg.web.view.taglibs.ExtendComboTextTag} �� {@link com.coheg.web.view.taglibs.ExtendComboCompositeTag}
 * @author pillarliu
 * @version $Id: extendCombo.js,v 1.1 2010/12/10 10:56:35 silencily Exp $
 *
 * ע���������� styleControl.js ��ʵ��
 * �������֧�� ��ѡ
 */

if (ExtendCombo == null) {
	var ExtendCombo = {};
}

ExtendCombo.txtObj;				// ��������
ExtendCombo.hidObj;				// ���ض���
ExtendCombo.btnObj;				// ��ť����
ExtendCombo.tempList    = '';	// ��ת����
ExtendCombo.tempArray   = '';	//�����ѡ��ֵ
ExtendCombo.initArray = new Object()// ���黺��--��һ�μ��غ��ظ�����
ExtendCombo.inputContent= '';
ExtendCombo.defFunction  = null;//�ṩ�Զ���ĺ���
ExtendCombo.hostUrl    = "";
ExtendCombo.splitNode  = "###";//�ڵ���ڵ��ָ�
ExtendCombo.splitField = "|||";//���Լ�ָ�

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
 * ��ȡ�������鵯��������ʾ����
 * Ĭ����װ�����ݸ�ʽ����   id1|||name1|||other1,,,id2|||name2|||other2
 * @param temp_obj  	�������ı����������, ����Name		
 * @param temp_btn  	�ı����ť��������		
 * @param optionInfos	�ӷ�����ȡ�û���ǰ�˹��������
 * @param nextPage      ��ҳ��ʼ��Ĭ��Ϊ0	
 ������չ��
 * @param arguments[4]  ���Id��hidden������
 * @param arguments[5]  ��ʾ������Ŀ��Ĭ��Ϊ10���ַ�ҳ����������Ϊ���ֹ�����
 * @param arguments[6]  ���ڶ�ѡ�У������Ѿ�ѡ�е��ַ������ԡ� ; ����������Ĭ��Ϊ�����this.value
 * @param arguments[7]  �Ƿ����Ρ��ÿա� Ĭ��Ϊfalse /  true �򲻳��֡��ÿա�
 */
ExtendCombo.setMenu = function (temp_obj,optionInfos,nextPage, temp_btn,temp_hidd,temp_count,multStr,isClear,fun){
	//�Զ��庯��
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
	ExtendCombo.btnObj = (arguments.length == 1) ? null : th;	//�趨�ⲿ����İ�ť
	ExtendCombo.hidObj = (arguments.length >= 5 && typeof arguments[4] == 'object') ? arguments[4] : null;
		
	var inteval     = 10;   //ÿҳ��
	var isPageCount = true; //�Ƿ��ҳ
	var resertBtn   = arguments[7] ? arguments[7]:false;
	//-- ���Ӳ���6,arg5 !=10 ����ʾ������Ϣ,���ֹ�����,�Զ����ڿ�� --����5Ԥ����system 2004-08-20 liuz
	var arg5        = (arguments.length >= 6)?parseInt(arguments[5]):10
	if(arg5 != 10)
		isPageCount = false;
	var selBox      = false; //�Ƿ��ѡ
	//-- ����7 ��ѡ,�����ѡ��string  2004-09-30 liuz
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
	var slen			  =  5  //ҳ����
	var shei			  =  25 //ҳ��߶�
	var local_page_scroll = 'hidden';
	var local_page_height =  210;
	var table_width       = '100%';   
	if(!isPageCount){		
		local_page_scroll = 'auto'
		table_width       = '100%'
	}
	
	var showNum   = 4;     //��ҳ
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
	//���ԭ�͵Ĺ��� location.href.indexOf("///")>0 ? ContextInfo.getDemoContext() : 
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
				var txtvalue = ';'+temp_obj.value+';'; //��ѡ
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
	tabSelectString += "	<td style=\"cursor:hand;\" title=\"��������ÿ�\" class='article_copyright'>";//<img src='"+ContextInfo.contextPath+"image/shenhe_wei1.gif' align='absmiddle' border=0> <img src='"+ContextInfo.contextPath+"image/shenhe_no.gif' align='absmiddle' border=0>
	if(selBox)
		tabSelectString +=   "<a href='#' onclick='parent.ExtendCombo.getValue(parent.ExtendCombo.tempArray)'>ѡȡ</a> ";
	/**if(!resertBtn)*/
	if(!isClear)
		tabSelectString +=   "<a href='#' onclick='parent.ExtendCombo.getValue(\"\")'>�ÿ�</a> ";
	if(local_page_width > 100)
		tabSelectString +=   "<a href='#' onclick='parent.closeSelectMenu()'>�ر�</a>&nbsp;";
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

/**����ѡȡ��ֵ
 * @param content ���������
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
			if(ExtendCombo.hidObj){//���û�иı�ֵ�����ټ���				
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

/*-- ����ѡ�е�ֵ 
 * �ع��÷���,����ʹ���� �����й��������
 * obj ѡ���������� 
 * str ѡ�з���ֵ��������ѡ��
 */
ExtendCombo.onSelect = function (obj,str){}


/**
 * ������ѡ�����, ��ѡ��ֵ���Զ���䵽�������, field ���� code, name, description
 * @param systemModuleName ��ϵͳģ����
 * @param codeWrapperObjectPrefix ҳ���� codeWrapper ���Ͷ����ǰ׺
 * @param type �������ʹ���, ���ϼ�����
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
 * ����������ѡ�����
 * @param systemModuleName ��ϵͳģ����
 * @param hiddenObject ���ؿ����
 * @param textObject �ı������
 * @param buttonObject ��ť����
 * @param type �������ʹ���, ���ϼ�����  
 */
ExtendCombo.selectOptions = function(systemModuleName, hiddenObject, textObject, buttonObject, type) {
	ExtendCombo.hostUrl = ContextInfo.contextPath + '/common/systemcode.do?step=comboSupportList&systemModuleName=' + systemModuleName + '&timpstamp=' + DateUtils.GetDBDate().getTime() + '&parentCode=';
	ExtendCombo.getOptions(textObject, buttonObject, hiddenObject, type);
}


//�����˵�
/*
HrUtils.selectOptions = function(textObj,btnObj,itemName){
	ExtendCombo.hostUrl = "<c:url value="/hr/CodeTableAction.do?step=getTreeNode&pageUrl=/jsp/common/codetable/options.jsp&id="/>";
	ExtendCombo.getOptions(textObj,btnObj,'',itemName);
}
*/


//--�����Ĺر�
function closeSelectMenu(){	
	ExtendCombo.tempArray = '' ;//��ն�ѡֵ
	if(ExtendCombo.isIE6)
		ExtendCombo.selectWin.hide();
	else
		document.getElementById("displaySelectLayer").style.display="none";
}


//--����xmlhttpȡ���ݵĴ��� �� ID ��Ӧ
function getArrayMap(){
	var arrayMapping = new Array();	
	arrayMapping.add(new codeMapping('xb',   20001));//�Ա�
	return arrayMapping ;
}
//���¸�ӳ�丳ֵ
function codeMapping(itemName, fatherId){
	this.itemName = itemName;
	this.fatherId = fatherId;
}

//--�Ӻ�˻�ȡ����
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

/** ���ز�ֺ��ַ�������
 * @param allContent ԭʼ�ַ�������
 * return retArray
 */
ExtendCombo.getArrayOptions = function(allContent){
	var retArray = new Array()
	var SEPERATOR = ",";
	try{
		//�����ַ�����
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
		alert("����ַ�������" + e);
	}
	return retArray;	
}

/** �����ֶ�
* @param src �ַ���
* @param num ˳���
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


/** �������������ؼ���
 * @param keyWord ����Ĺؼ���
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

/** ����������������
 * @param content ����
 
function Array.prototype.add(content){
	this[this.length] = content;
}*/

/** �ַ�������
 */
String.prototype.trim = function(){
   return this.replace(/(^(\s|\u3000)*)|((\s|\u3000)*$)/g, '');
}


/** �ؼ��Ĳ�ѯ���ܣ���ƥ����ú�ɫ��־*/
ExtendCombo.query = function (kcmlData,txtObj,btnObj){
	if(btnObj == txtObj  && txtObj.value !='' ) {
		var j = 0;
		var findData = new Array();
		for(var i = 0; i < kcmlData.length; i++) {
			var temp =(kcmlData[i]+'').split(ExtendCombo.splitField);
			var str = (temp.length >1)?temp[1].toString():temp[0].toString(); 
			str = str.replace(/\<[^>]*>/gi,""); //������<>���˵�
			str = str.replace(/&amp;40;/gi,"(");
			str = str.replace(/&amp;41;/gi,")");
			//str = str.replace(/&amp;/gi,"&");
			str = str.replace(/&{1}[a-z]{1,};{1}/gi,"");//����html�����ַ�
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

/*  ��ʾ�ؼ���ѡ����
 *  2004-11-10 update Liuz  
 *  2004-09-30 Add    Liuz
 */
//--ѡ���Ǳ任 �� �ı�ֵ
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
//--������ֵ
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
//--����ֵ
function fc_delSelectItems(str){
	var temp = ExtendCombo.tempArray.split('**');
	for(var i=0;i<temp.length;i++){
		if(temp[i]&&str.indexOf(temp[i])>=0){
			ExtendCombo.tempArray = ExtendCombo.tempArray.replace(temp[i],"");
			break;
		}
	}
}


/*---�ṩ���ýӿ�---*/

/**
 * ȡ�ù��ô���������, ����Id��Name
 * @param txtObj  		�����name���ı������ 
 * @param btnObj  		��ť���ı������
 * @param hiddObj		�����id  ��hidden�����
 * @param itemName		��ȡ�õĴ�������, ��ֱ�Ӵ���father_id
 * @param arg[4]        =this.value,���ڶ�ѡ�Ļ�ѡ����
 * @param arg[5]        �Ƿ����Ρ��ÿա���ť Ĭ��false(Ϊ��) / true ������ 
 * @return				setҳ���е��ı����hidden��
 */
ExtendCombo.getOptions = function(txtObj, btnObj, hiddObj, itemName,arg4,arg5) {
	var optionsData;  //��ת����					
	var kcmlData = ExtendCombo.getXmlhttpString(itemName);
	optionsData = ExtendCombo.query(kcmlData,txtObj,btnObj)
	ExtendCombo.setMenu(txtObj,optionsData,0, btnObj,hiddObj,10,arg4,arg5)
}

/* ��ǰ�˹���ؼ�,֧��ǰ������ 
 * ����ʹ��ExtendCombo.initArray['itemName']
 */
ExtendCombo.getOptionStatic = function (txtObj,btnObj,hiddObj,arr,fun,isCountPage,isClear){
	//alert(txtObj+"<==>"+btnObj+"<==>"+hiddObj+"<==>"+arr+"<==>"+fun+"<==>"+isCountPage+"<==>"+isClear);
	if(arr == '')
		return;
	arr = ExtendCombo.query(arr,txtObj,btnObj);
	//�Ƿ�ʹ�÷�ҳ��10Ϊ��ҳ��11Ϊ����ҳ
	isCountPage = arr.length > 30 ?  10 : 11 ;
	ExtendCombo.setMenu(txtObj, arr,0,btnObj,hiddObj,isCountPage,'',isClear,fun);
}






/*-- ������ݺϷ� --*/
/* @params txtname  ���������� 
 * @params codename ��Ӧ��������� 'jlxy','jg'...
 */
function checkGetOptionValue(txtname,codename){
	return checkTextSelected(codename,txtname);
}

/* �ı�ѡ��
 * @params item ��Ӧ���������
 * @params txt     ������ֵ
 */
function checkTextSelected(item,txt){
	if(checkOptionsWithIdValue(document.all(txt),'',item))
		return true;
	else{
		alert('����� " '+txt+' " ���������ݿ��в����ڣ�\n ��ѡ���ʺϵ����ݡ�');
		getOptionsDefined(obj,obj,'',item)
		return false;
	}
}

/** ������Ϸ��� 2005-04-14  �Ľ����Ϸ���ֱ��ʹ����չ������
 *  obj   ��������
 *  hid   ���������
 *  item  ��������:jg,xb ���ߴ���
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

/** 10-14 ����̶���ѡ�� */
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

//--�������Ϸ�����
function fc_checkInput(txt,btn,itype,ibegin,iend){
	if(txt.value!=''){
		if(isNaN(txt.value)){
			alert('��ʽ�д���');txt.select();
			return false;
		}
		switch(itype){
			case 'year':
				if(ibegin && parseInt(txt.value)<ibegin)
					return error_msg(txt,'���ݲ��ڷ�Χ֮��');
				if(iend && parseInt(txt.value)>iend)
					return error_msg(txt,'���ݳ�����Χ');
				break;
			case 'number':
				if( parseInt(txt.value)>iend || parseInt(txt.value)<ibegin )
				{	
					alert('�������޶���Χ֮�ڵ���ֵ');txt.select();
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
	//0610�޸������޳�ʼֵ
	//�����ÿյĹ�������,�ÿ��Ժ�Ŀ���������ɶ�

	var url = ContextInfo.contextPath + "/common/basiccodemanager.do?step=getBasicCodeBySubIdAndCode";
	var params = "&typeCode=" + typeCode;
	params += "&subId=";
	params += subId;
	var temp = XMLHttpEngine.getResponseText(url, false, 'POST', params);	
	
	//�޸�2��HIDDEN
	document.getElementsByName("hiddenValueDefault" + name)[0].value = '';
	document.getElementsByName(name)[0].value = '';
	
	//TEXT���VALUE
	var tempTextValue = "";
	var beginIndex = temp.indexOf("|||");
    var endIndex = temp.indexOf('"',beginIndex);
	tempTextValue = temp.substr(beginIndex+3,endIndex-beginIndex-3);
	document.getElementsByName("temp." + name)[0].value = '';
	
	//TEXT���VALUE��JAVASCRIPT
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
		//�������������գ��ӱ�����
	    var parentValue = document.getElementsByName(parentName)[0].value;
		if(''==  parentValue){
				//���ô�������
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
				//���ô�������
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



