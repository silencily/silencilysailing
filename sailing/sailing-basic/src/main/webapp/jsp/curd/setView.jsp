<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>
<html>
    <head>
        <script language="JavaScript">
        function setButtonStatu() {
            document.getElementById("btnAdd").disabled = true;
            document.getElementById("btnDel").disabled = true;
            document.getElementById("btnAddAll").disabled = false;
            document.getElementById("btnDelAll").disabled = false;
            document.getElementById("up_").disabled = true;
            document.getElementById("down_").disabled = true;
        }
        </script>
    </head>
<body class="list_body">
	<script language="javascript">
	
		var CurrentPage = {};
		if (CurrentPage == null) {
			var CurrentPage = {};
		}
		
		function AddItem(leftControl, rightControl) {
            Control1 = null;
            Control2 = null;
            Control1=leftControl;
            Control2=rightControl;
            var opt;

            var i=Control1.length;
           if(i==0) return;
            for(j=0; j<i; j++) {
                if(Control1.options[j].selected==true) {
                    opt = new Option();
                    opt.text =Control1.options[j].text;
                    opt.value=Control1.options[j].value;
                    Control2.options.add(opt);
                    Control1.options[j] = null;
                    i=i-1;
                    j=j-1;
                }
            }
        }

        function AddItemAll(leftControl, rightControl) {
            Control1 = null;
            Control2 = null;

            Control1=leftControl;
            Control2=rightControl;
            var opt;

            var i=Control1.length;
            if(i==0) return;
            for(j=0;j<i;j++) {
                if(Control1.options[j].selected=true) {
                    opt = new Option();
                    opt.text =Control1.options[j].text;
                    opt.value=Control1.options[j].value;
                    Control2.options.add(opt);
                    Control1.options[j] = null;
                    i=i-1;
                    j=j-1;
                }
            }
        }
        
        function DelItem(leftControl, rightControl) {
            Control1 = null;
            Control2 = null;
            
            Control1=leftControl;
            Control2=rightControl;
            
            var opt;
            var i=Control2.length;
            if(i==0) return;
            for(j=0;j<i;j++) {
                if(Control2.options[j].selected==true) {
                    opt = new Option();
                    opt.text = Control2.options[j].text;
                    opt.value= Control2.options[j].value;
                    changeAsc(opt);
                    Control1.options.add(opt);
                    Control2.options[j]=null;
                    i=i-1;
                    j=j-1;
                }
            }
        }
        
        function DelItemAll(leftControl, rightControl) {
            Control1 = null;
            Control2 = null;
            
            Control1=leftControl;
            Control2=rightControl;
            
            var opt;
            var i=Control2.length;
            if(i==0) return;
            for(j=0;j<i;j++) {
                if(Control2.options[j].selected=true) {
                    opt = new Option();
                    opt.text = Control2.options[j].text;
                    opt.value= Control2.options[j].value;
                    changeAsc(opt);
                    Control1.options.add(opt);
                    Control2.options[j]=null;
                    i=i-1;
                    j=j-1;
                }
            }
        }
        
        function addwritetext(obj) {
            var s="";
            for( i=0;i<obj.length;i++)
                s=s+obj.options[i].text + ",";
            s=s.substring(0,s.length-1);
            return(s);
        }

        function addwritevalue(obj) {
            var s="";
            for(i=0;i<obj.length;i++)
                s=s+obj.options[i].value + ",";
            s=s.substring(0,s.length-1);
            return(s);
        }
  
		function moveItemForSelectTag(objSelect, flgUpDown) {
		 	if (objSelect == null || flgUpDown == null) return;
			if (flgUpDown != "U" && flgUpDown != "D") return;
			if (objSelect.tagName != "SELECT") return;
		
			if (objSelect.options == null) return;
			if (objSelect.options.length < 2) return;
			if (objSelect.selectedIndex < 0) return;
			if (objSelect.selectedIndex == 0 && flgUpDown == "U") return;
			if (objSelect.selectedIndex == objSelect.options.length-1 && flgUpDown == "D") return;
		
			var curIndex = objSelect.selectedIndex;
			var curValue = objSelect.options[curIndex].value;
			var curText = objSelect.options[curIndex].text;
		
			if (flgUpDown == "U") {
				objSelect.options[curIndex].value = objSelect.options[curIndex-1].value;
				objSelect.options[curIndex].text = objSelect.options[curIndex-1].text;
				objSelect.options[curIndex-1].value = curValue;
				objSelect.options[curIndex-1].text = curText;
				objSelect.selectedIndex = curIndex-1;
			} else {
				objSelect.options[curIndex].value = objSelect.options[curIndex+1].value;
				objSelect.options[curIndex].text = objSelect.options[curIndex+1].text;
				objSelect.options[curIndex+1].value = curValue;
				objSelect.options[curIndex+1].text = curText;
				objSelect.selectedIndex = curIndex+1;
			}
		}
		function asca(objSelect, flgUpDown) {
		 	if (objSelect == null || flgUpDown == null) return;
			if (flgUpDown != "A" && flgUpDown != "D") return;
			if (objSelect.tagName != "SELECT") return;
		
			if (objSelect.options == null) return;
			var curIndex = objSelect.selectedIndex;
			var curValue = objSelect.options[curIndex].value;
			var curText = objSelect.options[curIndex].text;
			var vindex=curValue.lastIndexOf('-')==-1?curValue.length:curValue.lastIndexOf('-');
			var tindex=curText.lastIndexOf('-')==-1?curText.length:curText.lastIndexOf('-');
			if (flgUpDown == "A") {
				objSelect.options[curIndex].value = curValue.substring(0,vindex)+'-asc';
				objSelect.options[curIndex].text = curText.substring(0,tindex)+'-升序';
			} else {
				objSelect.options[curIndex].value = curValue.substring(0,vindex)+'-desc';
				objSelect.options[curIndex].text = curText.substring(0,tindex)+'-降序';
			}
		}
		function changeAsc(obj) {
			var curValue = obj.value;
			var curText = obj.text;
			var vindex=curValue.lastIndexOf('-')==-1?curValue.length:curValue.lastIndexOf('-');
			var tindex=curText.lastIndexOf('-')==-1?curText.length:curText.lastIndexOf('-');
			obj.value = curValue.substring(0,vindex);
			obj.text = curText.substring(0,tindex);
		}
		/*
		 * added bu XuefangHu to control button's status
		 */
		function clickRight() {
		    document.getElementById("btnAdd").disabled = true;
		    document.getElementById("btnDel").disabled = false;
		    document.getElementById("down_").disabled = false;
		    document.getElementById("up_").disabled = false;
		}
		function clickLeft() {
		    document.getElementById("btnAdd").disabled = false;
		    document.getElementById("btnDel").disabled = true;
		    document.getElementById("down_").disabled = true;
		    document.getElementById("up_").disabled = true;
		}
		function changeSelect() {
			left = document.getElementById("selLeft");
			right = document.getElementById("selRight");
			if (left.selectedIndex > -1) {
		    	document.getElementById("btnAdd").disabled = false;
		    } else {
		    	document.getElementById("btnAdd").disabled = true;
		    }
			if (right.selectedIndex > -1) {
			    document.getElementById("btnDel").disabled = false;
			    document.getElementById("down_").disabled = false;
			    document.getElementById("up_").disabled = false;
		    } else {
			    document.getElementById("btnDel").disabled = true;
			    document.getElementById("down_").disabled = true;
			    document.getElementById("up_").disabled = true;
		    }
		}
	</script>

	<form id="f" method="post">
	<input type="hidden" name="pageId" value='<c:out value="${theForm.pageId}"/>'/>
		<div class="update_subhead">
 			<span class="switch_open" onclick="StyleControl.switchDiv(this,$('submenu1'))" title="">定制显示</span>
		</div>
    	<table class="EasyTable" cellpadding="0" width="100%" border='0'id="submenu1">
        	<tr align="center">
            	<td width="18%"></td>
                <td>未设置页面项<br>
                   	<select onchange="changeSelect()" id="selLeft" ondblclick="AddItem(selLeft, selRight);changeSelect();" style="width: 150px" 
                    		multiple="multiple" size="15" name="selLeft" runat="server" type="select-multiple" >
               			<c:if test="${not empty theForm.select}">
							<table:settable all="${theForm.all}" select="${theForm.select}" isSel="all"/>
						</c:if>
               		</select>
                </td>
                <td width="100">
                    <div style="Z-index:1">
               		<table id="Table3" align="center" style="width: 200px">
                       	<tr>
                           	<td>
                           		<input id="btnAdd" title="将左边列表选中内容添加到右边列表" onclick="AddItem(selLeft, selRight);changeSelect();" 
                           			type="button" value=">>" name="btnAdd" class="opera_display"
                           			/>
                           	</td>
                        </tr>
                        <tr>
                           	<td>
                           		<input id="btnAddAll" title="将左边列表选中内容全部添加到右边列表" onclick="AddItemAll(selLeft, selRight);changeSelect();" 
                           			type="button" value="全部>>" name="btnAddAll" class="opera_display"
                           			/>
                           	</td>
                        </tr>
                        <tr>
                           	<td>
                           		<input id="btnDelAll" title="将右边列表选中内容全部删除" onclick="DelItemAll(selLeft, selRight);changeSelect();" 
                           			type="button" value="<<全部" name="btnDelAll" class="opera_display"
                           			/>
                           	</td>
                        </tr>
                        <tr>
                        	<td>
                        		<input id="btnDel" title="将右边列表选中内容删除" onclick="DelItem(selLeft, selRight);changeSelect();" 
                        		   type="button"  value="<<" name="btnDel" class="opera_display"
                        		   />
                            </td>
                        </tr>
                   	</table> 
                </div>
                </td>
                <td>已设置页面项<br>
              		<select id="selRight" onchange="changeSelect()" ondblclick="DelItem(selLeft, selRight);changeSelect();" style="width: 150px;Z-index:0" 
               			multiple="multiple" size="15" name="saveList" type="select-multiple">
               			 <c:choose>
               			 <c:when test="${empty theForm.select}">
	               			 <table:settable all="${theForm.all}" select="${theForm.select}" isSel="all"/>
               			 </c:when>
               			 <c:otherwise>
	               			 <table:settable all="${theForm.all}" select="${theForm.select}" isSel="select"/>
               			 </c:otherwise>
               			 </c:choose>
                    </select>
                </td>
                <td>
                <table>
                <tr><input type="button" name="up_" id="up_" value="上移" class="opera_display" onClick="moveItemForSelectTag(selRight,'U');"/></tr>
                <tr><input type="button" name="down_" id="down_"  value="下移" class="opera_display" onClick="moveItemForSelectTag(selRight,'D');"/></tr>
                <c:if test="${theForm.asc}">
                 <tr><input type="button" name="" id="" value="asc"  onClick="asca(selRight,'A');"/></tr>
                 <tr><input type="button" name="" id=""  value="desc" onClick="asca(selRight,'D');"/></tr>
                </c:if>
                </table>
                </td>
                <td width="18%"></td>
            </tr>
            <tr>
            	<td colspan="5" height="2"><input id="hdnValue" type="hidden" name="hdnValue" runat="server">
            	</td>
            </tr>
         
        </table>
        <input type='hidden' name="asc" value="<c:out value='${theForm.asc}'/>"/>
    </form>
</body>
<script type="text/javascript">

	if (CurrentPage == null) {
	    var CurrentPage = {};
	}  
	CurrentPage.save = function() {
		var sel=$('selRight');
		var size=sel.options.length;
		if (size < 1) {
			alert("已设置页面项中至少应该有一项");
			return false;
		}
		for(i=0;i<size;i++) {
			sel.options[i].selected=true
		}
		
		FormUtils.post(document.forms[0], '<c:url value="/curd/curdAction.do?step=saveTable"/>');
		try {
			//取得Under框架的document对象
			var temp = window.parent.document.getElementById("under").document;
			//从under框架中获得frame_panel_0框架的对象
			temp = temp.getElementById("under").document.frames[2].document.frames[2];//.getElementById("mainFrame");//.getElementById("frame_panel_0");
			//alert(temp.location);
			//框架重新读取
			temp.location.reload();
		}catch(e){}
		//top.definedWin.closeListing();
		definedWin.closeModalWindow();
	//	alert(window.parent.document.frames('mainFrame').document.frames[0]);
	//	opener.document.getElementById('opera_query').onclick();
		}
	
	CurrentPage.onLoadSelect = function(){		
			top.definedWin.selectListing = function(inum) {
				CurrentPage.save();			
			}
	}
	CurrentPage.onLoadSelect();
		    setButtonStatu();

</script>
</html>