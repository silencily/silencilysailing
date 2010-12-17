

/*
 * ����һ���༭ҳ������������ֶ���ӱ����
 *
 */
EditPage = function(name){
	this.name = name;
	//��ǰ������Ĭ��Ϊ1������޸�ʱ������Ѿ���ʼ������������ֻ���ۼӣ���ΪҪΨһ��ʶ����
	this.rowNumber = 1;
	//ΨһID�Ķ��������ں�̨�ж���������Ϊ�գ������޸ģ���Ϊ�գ�
	this.uuidObj   = "";
	//�Ƿ������ N �У��ڵ����� N ��ǰ��λ�ò�������(N = holdRow)
	this.holdRow   = 0;

	this.regtxt    = /\[0\]/gi;
	this.regtxt1   = /row=\"0\"/gi;
	this.regtxt2  = /\(template\)/gi;

	//this.delRow ;
	//this.renRow ;
	//this.addRow ;
	//this.clearAll  ;
	
	//�������������滻������ַ��Ƚ���ת��,ע�� ||| �Ǳ����ַ�
	this.beginReplaceString = function(str){
		var reg = /\)\[0\]/gi;
		var str1 = str.replace(reg,"|||");
		return str1;
	}
	//���滻��Ĳ����Ϲ����ַ��滻����
	this.afterReplaceString = function(str){
		var reg = /\|\|\|/gi;
		var str1 = str.replace(reg,")[0]");
		return str1;		
	}

	

	//������
	this.addRow = function(table){
		var len = table.rows.length;
		len = this.holdRow ? len - this.holdRow : len;
		var row = table.insertRow(len);
		var tabStr = document.getElementById(this.name+"_trId_[0]").childNodes[0].innerHTML;
		tabStr = this.beginReplaceString(tabStr)
		tabStr = tabStr.replace(this.regtxt,"["+this.rowNumber+"]");
		tabStr = tabStr.replace(this.regtxt1,"row='"+this.rowNumber+"'");
		tabStr = tabStr.replace(this.regtxt2,"("+this.rowNumber+")");
		tabStr = this.afterReplaceString(tabStr)
		var td = document.createElement("TD");
		row.appendChild(td);
		row.row = this.rowNumber;
		row.id  = this.name+"_trId_["+this.rowNumber+"]";
		td.innerHTML = tabStr;
		this.switchButton(this.rowNumber,"","","none","");
		this.clearValue(row);
		this.setLock(false,row);
		this.rowNumber++;
		this.afterAddRow(table)
		Global.hightLightForm();
		Global.setHeight();
	}

	//�б��������
	this.addListingRow = function(table){
		var len = table.rows.length;
		len = this.holdRow ? len - this.holdRow : len;
		var row = table.insertRow(len);
		row.row = this.rowNumber;
		row.id  = this.name+"_trId_["+this.rowNumber+"]";
		//row.style.cssText=document.getElementById(this.name+"_trId_[0]").style.cssText;
		row.uuid="";
		var tdStr = "";
		var td;
		//tongjq
		if (document.getElementById(this.name+"_trId_[template]")) {
			row.style.display = "";
			var tds = document.getElementById(this.name+"_trId_[template]").childNodes;
			tempRegtxt  = /\[template\]/gi;
			tempRegtxt1 = /row=\"template\"/gi;
			tempRegtxt2  = /\(template\)/gi;
			for(var i=0;i<tds.length;i++){
				tdStr = tds[i].innerHTML;
				tdStr = tdStr.replace(tempRegtxt,"["+this.rowNumber+"]");
				tdStr = tdStr.replace(tempRegtxt1,"row='"+this.rowNumber+"'");
				tdStr = tdStr.replace(tempRegtxt2,"("+this.rowNumber+")");
				td = document.createElement("TD");
				td.innerHTML = tdStr;
				td.className = tds[i].className;
				row.appendChild(td);
			}
			this.switchButton(this.rowNumber,"","","none","");
			this.clearValue(row);
			
			this.rowNumber++;
			this.afterAddRow(table)
			Global.hightLightForm();
			Global.setHeight();
			return;
		}
		//tongjq
		var tds = document.getElementById(this.name+"_trId_[0]").childNodes;
		//var tds = table.rows[1].cells;
		for(var i=0;i<tds.length;i++){
			tdStr = tds[i].innerHTML;
			tdStr = this.beginReplaceString(tdStr)
			tdStr = tdStr.replace(this.regtxt,"["+this.rowNumber+"]");
			tdStr = tdStr.replace(this.regtxt1,"row='"+this.rowNumber+"'");
			tdStr = this.afterReplaceString(tdStr)
			td = document.createElement("TD");
			td.innerHTML = tdStr;
			td.className = tds[i].className;
			row.appendChild(td);
		}
		this.switchButton(this.rowNumber,"","","none","");
		this.clearValue(row);
		
		this.rowNumber++;
		this.afterAddRow(table)
		Global.hightLightForm();
		Global.setHeight();
		//��һ��Ϊɾ��״̬��ʱ�������´���
		if (document.getElementById(this.name+"_[0].delFlg") && document.getElementById(this.name+"_[0].delFlg").value == "1") {
			//�ָ�������״̬
			this.renRowCur(table,row);
			var obj = document.getElementById(this.name+"_trId_[0]").all;
			for (var i=0;i<obj.length;i++) {
				//�����ûָ���Ŀ��״̬�����Ĭ��ֵ
				if (obj.isUnchange == true && !obj.holdObj) {
					var id = obj.id;
					id.replace(this.regtxt,"["+(this.rowNumber - 1)+"]");
					newobj = document.getElementById(id);
					if(obj.type == "button") {
						newobj.disabled = obj.disabled;
						newobj.className = obj.oldClass;
					} else {
						newobj.readOnly = obj.readOnly;
						newobj.className = obj.oldClass;
					}
				}
			}
		}
	}

	//���ò�����ť
	this.switchButton = function(inum,arr1,arr2,arr3,arr4){
		if(document.getElementById(this.name+"_["+inum+"].add"))
			document.getElementById(this.name+"_["+inum+"].add").style.display = arr1;
		if(document.getElementById(this.name+"_["+inum+"].del"))
			document.getElementById(this.name+"_["+inum+"].del").style.display = arr2;
		if(document.getElementById(this.name+"_["+inum+"].old"))
			document.getElementById(this.name+"_["+inum+"].old").style.display = arr3;
		if(document.getElementById(this.name+"_["+inum+"].copy"))
			document.getElementById(this.name+"_["+inum+"].copy").style.display = arr4;
	}

	//������obj�ڵ�δ����holdValue��Ķ���ֵ��ջ���ָ��Ϊ�Զ����defValue
	this.clearValue = function(obj){
		var defValue;
		var objAll = obj.getElementsByTagName("*");
		for( i=0;i<objAll.length;i++) {
			//��ճ��ı���ʾ
			if((objAll[i].tagName == "INPUT" || objAll[i].tagName == "TEXTAREA") && !objAll[i].holdValue){
				defValue = objAll[i].defValue ? objAll[i].defValue : "";
				objAll[i].value = defValue;
				objAll[i].checked = false;
			}else if(objAll[i].fillable && !objAll[i].holdValue){
				objAll[i].innerText = "";
			}
			if(objAll[i].id == "edit_longText" && objAll[i].tagName == "INPUT" 
					&& objAll[i].type == "button"){
				//alert(objAll[i].tagName+"=="+objAll[i].type+"=="+objAll[i].id+"=="+objAll[i].title);
				objAll[i].title = "";
			}
		}	
	}

	//������obj��δ����holdObj�Ķ�������Ϊdisabeld;
	this.disableButton = function(obj){
		obj.disabled = true;
		var temp = obj.getElementsByTagName("*");
		for( i=0;i<temp.length;i++) {
			if(temp[i].tagName == "INPUT"){
				if(!temp[i].holdObj){
					temp[i].disabled = true;
				}
			}
		}
		obj.oldClass = obj.className;
		obj.className = "disabled";
	}
	this.disableAll = function(obj){
		var temp = obj.getElementsByTagName("*");
		for( i=0;i<temp.length;i++) {
			if(temp[i].tagName == "INPUT"){
				temp[i].disabled = true;
			}
		}
	}
	//�����ж�������Ϊable;
	this.ableButton = function(obj){
		obj.disabled = false;
		var temp = obj.getElementsByTagName("*");
		for( i=0;i<temp.length;i++) {
			temp[i].disabled = false;
		}
		obj.className = obj.oldClass;
	}

	//��ҳ���������Ҫɾ����Ԫ��
	this.clearAll = function (table){
		var old = document.getElementsByName(this.name+"_oldBtn");
		var obj;
		for(var i=0;i<old.length;i++){
			if(old[i].style.display ==""){
				obj = document.getElementById(this.name+"_trId_["+old[i].row+"]");
				obj.childNodes[0].innerHTML = "";//���
				obj.style.display = "none";
			}
		}
		Global.setHeight();
	}

	this.mouseOver = function(tdObj){
		tdObj.fvalue = tdObj.innerText;
		var str = "";
		if(!tdObj.fname)
			return;
		str += "<input type='text' value='"+tdObj.fvalue+"' name='"+tdObj.fname+"'/>";
		var inum = tdObj.parentNode.row;
		var uuid = tdObj.parentNode.uuid;
		var objStr = this.uuidObj.replace(this.regtxt,"["+inum+"]");
		if(document.getElementsByName(objStr)[0].disabled)
			document.getElementsByName(objStr)[0].disabled = false;
		tdObj.innerHTML = str;
		//document.getElementsByName(tdObj.fname)[0].onmouseOut = this.mouseOut;
	}
	this.mouseOut = function(tdObj){
		tdObj = this;
		if(!tdObj.fname)
			return;
		var nvalue = document.getElementsByName(tdObj.fname)[0].value;
		tdObj.fvalue = nvalue;
		var str = "<input type='hidden' value='"+tdObj.fvalue+"' name='"+tdObj.fname+"'/>";
		tdObj.innerHTML = tdObj.fvalue + str;
	}
	/*
	this.overTd = function(e) {
		var src = e.srcElement;
		while(src!=null){
			  if(src.nodeName!="INPUT"){   
				  this.mouseOver(src);
				  return;
			  }
			  src=src.parentNode;
	   }
	}
	this.outTd = function(e){
		var src = e.srcElement;
		while(src!=null){
			  if(src.nodeName=="TD"){   
				  this.mouseOut(src);
				  return;
			  }
			  src=src.parentNode;
	   }
	}*/

	/*��ų�ʼ��ֵ
	 * @param item ��Ӧ���ж�����
	 * @param arr  ��Ӧ��������ֵ�������inputһһ��Ӧ��ע���Ⱥ�˳��
	 * ��չԪ��һ������ fillable�������Ԫ�أ�span��div����������ԣ���������;
	   �����������û����;����Ϊ��ֵ��Ӧ���
	 */
	this.initEditDate = function(itemObj,arrValue){
		var j = 0;
		var arr = itemObj.getElementsByTagName("*");
		for(var i=0;i<arr.length;i++){
			if(arr[i].tagName == 'INPUT'){
				if(arr[i].type == 'text' || arr[i].type == 'hidden' || arr[i].type == 'textarea' || arr[i].type == 'radio'){
					arr[i].value = arrValue[j];
					arr[i].oldValue=arrValue[j];
					j++;
				}
				if(arr[i].type == 'checkbox'){
					//checkbox Ҫ�� holdValue = true 
					if(arrValue[j] == 'true'){
						arr[i].checked = true;						
					}else
						arr[i].checked = false;
					j++;
				}
			}else if(arr[i].fillable){
				arr[i].innerText = arrValue[j];
				j++;
			}
		}
	}

	//�������к��ṩ���صķ���
	this.afterAddRow = function(table){
		//TODO
	}
	//��ɾ���к��ṩ���صķ���,������ο� this.delRow
	this.afterDeleteRow = function(table,sel){
		//TODO
	}
	//
	//
	//Ϊ���߼�ɾ�����ӵķ���
	//
	//
	this.delRowaa = function (table,sel,replaceIdx){
			var inum = sel.row;
			var del = document.getElementById(this.name+"_["+inum+"].delFlg").value;
			var str = document.getElementById(this.name+"_["+inum+"].id").value;
			var obj = document.getElementById(this.name+"_trId_["+inum+"]");
			this.afterDeleteRow(table,sel)
			if(str ==""){
				if(inum == 0 && !document.getElementById(this.name+"_trId_[template]")){//�̨���?DD��??����?3y
					this.lockTr(obj);
					obj.className = "disabled";
					document.getElementById(this.name+"_[0].delFlg").value="1";
					this.switchButton(inum,"none","none","","none")
				}else{
					//?��??��?3y	
					table.deleteRow(obj.rowIndex);
					if (replaceIdx) {
						this.replaceObjIdx(table,inum);
					}
					Global.setHeight();
				}
			}else{
			
				this.lockTr(obj);
				obj.className = "disabled";
				document.getElementById(this.name+"_["+inum+"].delFlg").value="1";
				this.switchButton(inum,"none","none","","none")
			}
		}
		
	this.renRowCur = function (table,sel){
		var inum = sel.row;
		document.getElementById(this.name+"_["+inum+"].delFlg").value="0";
		this.renRow(table,sel);
	}
	//ɾ����
	this.delRow = function (table,sel,replaceIdx){
	
		var inum = sel.row;
		var objStr = this.uuidObj.replace(this.regtxt,"["+inum+"]");
		var str = document.getElementsByName(objStr)[0].value;
		var obj = document.getElementById(this.name+"_trId_["+inum+"]");
		this.afterDeleteRow(table,sel)
		if(str ==""){
			if(inum == 0){//��һ���Ǽ�ɾ��
				this.disableButton(obj);
				this.switchButton(inum,"none","none","","none")
			}else{
				//ǰ��ɾ��		
				table.deleteRow(obj.rowIndex);
				if (replaceIdx) {
					this.replaceObjIdx(table,inum);
				}
				Global.setHeight();
			}
		}else{
			this.disableButton(obj);
			this.switchButton(inum,"none","none","","none")
		}
	}
	//�ָ��У�����ɾ��
	this.renRow = function(table,sel){
		var inum = sel.row;
		var obj = document.getElementById(this.name+"_trId_["+inum+"]");
		this.unlockTr(obj);
		//if(inum == 0)
			this.switchButton(inum,"","","none","")
		//else
		//	this.switchButton(inum,"none","","none")
	}
	//�����ж����ReadOnly������Ϊfalse;
	this.unlockTr = function(obj){
		//obj.disabled = false;
		this.setLock(false,obj)
	}
	//������obj��δ����holdObj�Ķ����ReadOnly������Ϊtrue;
	this.lockTr = function(obj){
		//obj.disabled = true;
		this.setLock(true,obj)
	}
	
	this.setLock = function (isLock,obj)
	{
		isLock = isLock ? true : false;
		var temp = obj.getElementsByTagName("*");
		for(var i=0;i<temp.length;i++) {
			if(!temp[i].holdObj){
				var tag = temp[i];
					//if(tag.type == "button" && tag.id == "input_date")alert(tag.id);
				if(tag.type == "button")
				{
					if(tag.disabled == isLock)
						tag.isUnchange = true;
					if(!tag.isUnchange)
						tag.disabled = isLock;
				}else
				{
					if(tag.readOnly == isLock)
						tag.isUnchange = true;
					if(!tag.isUnchange)
						tag.readOnly = isLock;
				}
			}
		}
		if(isLock)
		{
			obj.oldClass = obj.className;
			obj.className = "disabled";
		}
		else
		{
			obj.className = obj.oldClass;
		}
	}
	
	//�õ������±����ڵ��е�Index
	this.getRowNumBySubscript = function(table,snum)
	{
		//alert("11\\"+snum);
		var subscript = this.name+"_trId_[" + snum + "]";
		for(var i =0 ; i < table.rows.length ; i++ )
		{
			var row = table.rows(i);
			//alert("22\\"+row.id+"||"+subscript);
			if(row.id == subscript)
			{
				return i;
			}
		}
		return -1;
	}
	//�õ���ǰTable�����е�����±�
	this.getMaxSubscript = function(table)
	{
		var len = table.rows.length;
		len = this.holdRow ? len - this.holdRow : len;
		len = len -1;
		var tr = table.rows[len];
		var snum = tr.id.replace(this.name+"_trId_[","");
		snum = snum.replace("]","");
		return snum;
	}

	//���idx����Ӧ�ѱ�����ɾ�����򽫴�idx֮��������input�����index-1
	this.replaceObjIdx = function(table,idx)
	{
		if (idx == this.rowNumber - 1) {
			this.rowNumber--;
			return;
		}
		var index = parseInt(idx);
		for(var index=parseInt(idx)+1;index< this.rowNumber;index++ ) {
//			alert("index" + index);
			var tds = document.getElementById(this.name+"_trId_[" + index + "]").childNodes;
			//var tds = table.rows[1].cells;
//			var holdString = ")[" + index + "]"
//			var srcString = "[" + index + "]";
//			var srcStringR = "row='" + index + "'";
			var srcString  = /\[\d*\]/gi;
			var srcStringR = /row=\"\d*\"/gi;

			for(var i=0;i<tds.length;i++){
//			alert(tds[i].innerHTML);
				tdStr = tds[i].innerHTML;
				tdStr = this.beginReplaceString(tdStr)
	//			tdStr = tdStr.replace(holdString,"||||||");
				tdStr = tdStr.replace(srcString,"["+(index - 1)+"]");
				tdStr = tdStr.replace(srcStringR,"row='"+(index - 1)+"'");
				tdStr = this.afterReplaceString(tdStr)
	//			tdStr = tdStr.replace("||||||",holdString);
				tds[i].innerHTML = tdStr;
			}
			var tr = document.getElementById(this.name+"_trId_[" + index + "]");
			tr.id = this.name+"_trId_[" + (index - 1) + "]";
		}
		this.rowNumber--;
	}
	
	//�б��������
	this.addListingRowWithValue = function(table,temp){
		var len = table.rows.length;
		len = this.holdRow ? len - this.holdRow : len;
		var row = table.insertRow(len);
		row.row = this.rowNumber;
		row.id  = this.name+"_trId_["+this.rowNumber+"]";
		//�����к�
		
		//�õ���ǰ�����е��к�
		var currentRowNum;
		var eventSrcElemntId = temp.id;
		currentRowNum= eventSrcElemntId.substring(eventSrcElemntId.indexOf('[')+1,eventSrcElemntId.indexOf(']'));
        //�õ���ǰ�����е��к�
        
		row.uuid="";
		var tdStr = "";
		var td;
		var tds = document.getElementById(this.name+"_trId_["+ currentRowNum +"]").childNodes;
		for(var i=0;i<tds.length;i++){
			tdStr = tds[i].innerHTML;
			tdStr = this.beginReplaceString(tdStr)     
			var regtxtWithValueTempStr = "\\["+currentRowNum+"\\]";
		    var regtxtWithValue = new RegExp(regtxtWithValueTempStr,"gi");
		    	    
		    var regtxtWithValueTempStr2 = "\\("+currentRowNum+"\\)";
		    var regtxtWithValue2 = new RegExp(regtxtWithValueTempStr2,"gi");    
		    
	        var regtxt1WithValueTempStr   = "row=\""+currentRowNum+"\"";
	        var regtxt1WithValue   = new RegExp(regtxt1WithValueTempStr,"gi");
	        

			tdStr = tdStr.replace(regtxtWithValue,"["+this.rowNumber+"]");	
			tdStr = tdStr.replace(regtxtWithValue2,"["+this.rowNumber+"]");		
			tdStr = tdStr.replace(regtxt1WithValue,"row='"+this.rowNumber+"'");
			
			tdStr = this.afterReplaceString(tdStr)
			td = document.createElement("TD");
			td.innerHTML = tdStr;
			td.className = tds[i].className;
			row.appendChild(td);
		}
		this.switchButton(this.rowNumber,"","","none","");
		this.clearIdVersionDelflg(row);
		this.rowNumber++;
		this.afterAddRow(table)
		Global.hightLightForm();
		Global.setHeight();
	}
	//������obj�ڵ�δ����holdValue��Ķ���ֵ��ջ���ָ��Ϊ�Զ����defValue
	this.clearIdVersionDelflg = function(obj){
		var objAll = obj.getElementsByTagName("*");
		for( i=0;i<objAll.length;i++) {
			//���id,version,delFlg
			if((objAll[i].tagName == "INPUT" && objAll[i].type == "hidden")){
				var nameTemp =  objAll[i].name;
				if(nameTemp.indexOf('].version')!=-1){
					objAll[i].value = '';
				}
				if(nameTemp.indexOf('].delFlg')!=-1){
					objAll[i].value = 0;
				}
				if(nameTemp.indexOf('].id')!=-1){
					objAll[i].value = '';
				}
			}
		}
	}
	
	
}
	