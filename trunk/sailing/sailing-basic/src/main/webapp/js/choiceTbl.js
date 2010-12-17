
/* 批量添加公用
 * 2008-01-15  gengxn
 * @link www.vfp.cn
 */
 

/* 批量添加选择的项
 * 2008-01-15  gengxn
 */
 var itemName1 ;
 AddChecked = function(item,choice,strId,itemName){
        itemName1 = itemName; 
        var tblCheck = document.frames('list').document.getElementById(item);
        var tblSelect = document.getElementById(choice);
        var index = tblSelect.getElementsByTagName("tr").length-1;
        var number =0;
        for (var i=1 ;i<tblCheck.getElementsByTagName("tr").length;i++){ 
            if (document.frames('list').document.getElementsByName("oldOid")[i - 1].checked ){
            	var oid = document.frames('list').document.getElementsByName("oldOid")[i - 1].value;
            	if(!selectAlready(oid,tblSelect,i-1)){
            	number++;
            	//没有重复的；
            	var insTr = tblSelect.insertRow();
                var cheTr = tblCheck.rows(i); 
                   for (var j = 0;j < cheTr.getElementsByTagName("td").length;j ++){ 
                	
                	var insTd = insTr.insertCell();
                	insTd.setAttribute("name","")
                	if (j == 0){ 
                		 var id = document.frames('list').document.getElementById(strId+(i-1)).value;
                		  var rowValue = document.frames('list').document.getElementById("ojbName"+(i-1)).value; 
                	     var idhtml = ' <input type="checkbox" style=display:none checked="true" name="oid" value="'+id+'"/> <input type="hidden" id="detailId_'+(i-1)+'"  name="detailId_'+(i-1)+'" value="'+id+'"/>';
                	    var delhtml = '<input type="button" row="'+rowValue+'" class="list_delete" title="删除"     onclick="delSelectRow(this,'+choice+');" />';
                	    insTd.setAttribute("align", "center");
                	    insTd.innerHTML = idhtml+delhtml;
                	}else{
                	   if(cheTr.cells(j).getAttribute("align")){ 
                	   insTd.setAttribute("align", "right");
                	   } 
                	   if(cheTr.cells(j).style.display=='none'){ 
                	   insTd.style.display='none'
                	   } 
                	    insTd.innerHTML = cheTr.cells(j).innerHTML;
                	}
                  }
            	}      
            }
        } 
         var msgInfo_ = new msgInfo();
         Validator.clearValidateInfo();
         var msg1 = msgInfo_.getMsgById('AM_I159_R_0',[itemName]);
         var msg2 = msgInfo_.getMsgById('AM_I157_R_0',[number,itemName]);
         var message = number == 0 ? msg1 : msg2;
         Validator.successMessage(message);
      
        
    }
    
    /* 判断有无重复添加项
 * 2008-01-15  gengxn
 */
      function selectAlready(oid,tblSelect,i){
    if(document.getElementById("detailId_"+i)){
      if(oid=document.getElementById("detailId_"+i).value)
      return true;
    }
     return false;
    }
    /* 删除选择的项
 * 2008-01-15  gengxn
 */
 delSelectRow = function(item,choice){ 
        var curRow = item.parentNode.parentNode;
         var ojbName = item.getAttribute("row"); 
        choice.deleteRow(curRow.rowIndex);
        var msgInfo_ = new msgInfo();
        Validator.clearValidateInfo();
        var message = msgInfo_.getMsgById('AM_I158_R_0',[itemName1,ojbName]); 
         Validator.successMessage(message);
    }
 listNum=function(item){
      var num =0;
        for (var i=1 ;i<document.frames('list').document.getElementById(item).getElementsByTagName("tr").length;i++){ 
          if (document.frames('list').document.getElementsByName("oldOid")[i - 1].checked ){
             num=num+1;
          }
        }
        return num;
    }
	
	/* 批量添加选择带回值
 * 2008-01-15  gengxn
 */
	returnSelect = function(obj,pre,dir,rows){
	//obj:自定义表对象
	//pre:EditPage对象
	//  参数dir说明：要设置的工器具的ID属性值中位于"."的前半部分的名称，
	//                 例如，在tasktools.tblAmJobTool.toolName的取"tasktools.tblAmJobTool"部分
	//rows:表示弹出画面带回的集合对象
	if(rows.length > 1)
	{
		for(var i=1;i<rows.length;i++){
             pre.addListingRow(obj);
             var index = pre.rowNumber -1;
             var dir1= dir.replace("-1", index);
            for(var t=0;t<rows[0].length;t++) {			
			   
			var	temp = document.getElementById(dir1 + "." + rows[0][t]);

				if (temp) {
					if(temp.tagName == "INPUT") {
						temp.value = rows[i][t];
						 
					} else {
						temp.innerText = rows[i][t];
						 
					}					
				}
			}
		}
       }
	}
	
	returnSelectResult =function(choice){		
	window.returnValue=selectField(choice);	
	window.close();	
	}
	function selectField(tableName){			
     
		var rows=new Array();
		var tblSelect = document.getElementById(tableName); 
        for (var i=0 ;i<tblSelect.getElementsByTagName("tr").length;i++){
           var fields = new Array();
           var cheTr = tblSelect.rows(i);
           var tds = cheTr.getElementsByTagName("td");
			if(i==0){
			   for( var z=0;z<tds.length;z++){
			    fields[z] = tds[z].getAttribute("field");
			   }
			}else{
           fields[0] = tds[0].childNodes[0].value;
             for(var z=1;z<tds.length;z++){
             fields[z] = tds[z].innerText;
             } 
          }
           rows[i] = fields; 
        }
		return rows;
	}
/**	by baihe
	 SelectShowModalDialog = function(obj,url,rateInfoPre,dir){
	var strPal = "";
	var iHeight="600";
	var iWidth="850";
	var sFeatures="dialogHeight: " + iHeight + "px;" + "dialogWidth: " + iWidth + "px;" + "help:no;" + "scroll:0;";

	rows=window.showModalDialog("../../qware/jsp/windowFrame/windowFrame.jsp",url,sFeatures);	
	returnSelect(obj,rateInfoPre,dir,rows);
	}
	*/