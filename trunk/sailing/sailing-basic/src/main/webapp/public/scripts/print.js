
if(Print == null){
	var Print = {};
}

/* 
 * 将选中内容 导出 到 Excel
 * 缺省选中 整个页面
 * obj 只能是table 的 ID 
 */
 Print.exportExcel = function(obj, method){
	//try{
		//var obj = document.excelObj;
		if(obj){ 
			//var controlRange = document.body.createControlRange();	
			//controlRange.add(obj);	
			//controlRange.execCommand( "Copy" , 0 , "" );
			var copyHTML = obj.outerHTML;
			for (var i = 0; i < obj.all.length; i++) {
				var o = obj.all.item(i);
				if (o.tagName == "INPUT") {
					//if (o.type && o.type.toUpperCase() == "HIDDEN") {
						copyHTML = copyHTML.replace(o.outerHTML, "");
					//}
				}
			}
			window.clipboardData.setData("Text", copyHTML);
		}else{
			document.execCommand( "SelectAll" , 0 , "" );
			document.execCommand( "Copy" , 0 , "" );
			//clipboardData.setData('Text','');
		}
		
		var excel_app = null;
		try {
			excel_app = new ActiveXObject( "Excel.Application" );
		} catch (e) {
			alert(' 请将服务器 [ ' + ContextInfo.serverAddr + ' ] 设置为受信任站点并安装 Excel 以便正常使用导出功能 ');	
			return;
		}
		
		var excel_book = excel_app.Workbooks.Add(); 
		excel_app.Visible = true;
		excel_book.Activate();

		var excel_sheet = excel_book.Sheets(1)
		excel_sheet.Activate();
		
		//兼容excel 2000机器
        excel_sheet.Paste();
        //excel_sheet.Cells.NumberFormat = "@";
       //excel_sheet.PasteSpecial( "HTML" , false , false , "" , 0 , "" , true );
        //适应表格尺寸
		var oRng = excel_sheet.Range("A1");
		oRng.EntireColumn.AutoFit();
		excel_app.Visible = true;
		excel_app.UserControl = true;
		
		if (method) {
			try {
				if ("printpreview" == method.toLowerCase()) {
					excel_sheet.PrintPreview();
				} else if ("print" == method.toLowerCase()) {
					excel_sheet.PrintOut();
				}
			} catch (e) {
				//打印取消时会有异常
			}
		}
	/*}catch( e ){
		alert("请刷新页面后重试");
		if(excel_book)
			excel_book.Close();
		if(excel_app)
			excel_app.Quit();
	}*/
}

/* 
 * 将选中内容 导出 到 Excel
 * 缺省选中 整个页面
 * obj 只能是table 的 ID 
 */
 Print.exportExcelByHTML = function(outerHTML, method){
	//try{
		window.clipboardData.setData("Text", outerHTML);
		
		var excel_app = null;
		try {
			excel_app = new ActiveXObject( "Excel.Application" );
		} catch (e) {
			alert(' 请将服务器 [ ' + ContextInfo.serverAddr + ' ] 设置为受信任站点并安装 Excel 以便正常使用导出功能 ');	
			return;
		}
		
		var excel_book = excel_app.Workbooks.Add(); 
		excel_app.Visible = true;
		excel_book.Activate();

		var excel_sheet = excel_book.Sheets(1)
		excel_sheet.Activate();
		
		//兼容excel 2000机器
        excel_sheet.Paste();
        //excel_sheet.Cells.NumberFormat = "@";
       //excel_sheet.PasteSpecial( "HTML" , false , false , "" , 0 , "" , true );
        //适应表格尺寸
		var oRng = excel_sheet.Range("A1");
		oRng.EntireColumn.AutoFit();
		excel_app.Visible = true;
		excel_app.UserControl = true;
		
		if (method) {
			try {
				if ("printpreview" == method.toLowerCase()) {
					excel_sheet.PrintPreview();
				} else if ("print" == method.toLowerCase()) {
					excel_sheet.PrintOut();
				}
			} catch (e) {
				//打印取消时会有异常
			}
		}
	/*}catch( e ){
		alert("请刷新页面后重试");
		if(excel_book)
			excel_book.Close();
		if(excel_app)
			excel_app.Quit();
	}*/
}

Print.getOuterHTML = function(obj,doNotDisplayHiddenObject){
	if(obj){ 
		//var controlRange = document.body.createControlRange();	
		//controlRange.add(obj);	
		//controlRange.execCommand( "Copy" , 0 , "" );
		var copyHTML = obj.outerHTML;
		for (var i = 0; i < obj.all.length; i++) {
			var o = obj.all.item(i);
			if (o.tagName == "INPUT") {
				if (o.type && o.type.toUpperCase() == "TEXT") {
					copyHTML = copyHTML.replace(o.outerHTML, o.value);
				} else {
					copyHTML = copyHTML.replace(o.outerHTML, "");
				}
			} else {
				if (doNotDisplayHiddenObject && o.style.display == "none") {
					copyHTML = copyHTML.replace(o.outerHTML, "");
				}
			}
		}
		return copyHTML;
	}else{
		return "";
	}
}
/* 
 * 将选中内容 导出 到word
 * 缺省选中 整个页面
 * obj 只能是table 的 ID 
 */
 Print.exportWord = function(obj){
	try{
		//var obj  = document.wordObj;
		if(obj){ 
			var controlRange = document.body.createControlRange();	
			controlRange.add(obj);	
			controlRange.execCommand( "Copy" , 0 , "" );
		}else{
			document.execCommand( "SelectAll" , 0 , "" );
			document.execCommand( "Copy" , 0 , "" );
		}
		var word_app = new ActiveXObject( "Word.Application" );
		var word_doc = word_app.Documents.Add();
		
		word_doc.Content.Paste();
		word_app.Visible = true;
	}catch( e ){
		alert("请刷新页面后重试");
		word_doc.Close();
		word_app.Quit();
	}
}


/* 将指定的数据打印到word上
 * 2006-09-17
 */

Print.replace= function ( word_doc, variables){
    //try{
        if( variables != null && variables.length != null ){
            for( var i = 0 ; i < variables.length ; i++ ){
                var tag = variables[i];
				if(tag.length <2)
					return;
                innerReplace( word_doc , tag[0] , tag[1] );
            }
        }
    //}catch( e ){}
}

/**
 * 替换word文档中的tag.这个方法一般被replace使用.
 */
Print.innerReplace = function( word_doc , tag , value ){
	if( value == '' || value == null ){
		value = " ";
	}
    if( tag != null && tag != "" && value != null && value != "" ){
        var range = word_doc.Content;
        range.Find.MatchWholeWord = true;
        range.Find.Forward = true;
		var result = range.Find.Execute( "[" + tag + "]" );
		if( result == true ){
            range.Select();
		    word_doc.Application.ActiveWindow.Selection.Text = value;
        }
    }
}

// 替换 div中内容，真正实现套打
// 
Print.textboxReplace = function( word_doc , tag , value ){
    try{
		var label = "[" + tag + "]";
		if( word_doc.Shapes != null ){
			for( var i = 0 ; i < word_doc.Shapes.Count ; i++ ){
				var shape = word_doc.Shapes.Item( i + 1 );
				if( shape.Type == 17 ){
					var text = shape.TextFrame.TextRange.Text;
					if( text == null )
						continue;
					if( text.indexOf(label)>=0 && value != null && value != "" ){
						shape.TextFrame.TextRange.Text = value;
						break;
					}
				}
			}
		}
	}catch(e){
		alert(tag + " 替换为 "+value+" 出错："+e);
	}
}

//--打开打印模板
// @param path         打印模板路径
// @param replaceArray 需要替换字符数组 ["替换前的字符","替换后字符"]
Print.execute = function(path,replaceArray){
    try{
        //var path = "http://faWenAction.do?step=toRead&readFile.id";
        var word_app = new ActiveXObject( "Word.Application" );
        var word_doc = word_app.Documents.Open( path , false , true );
        word_app.Visible = true;
        Print.replace(word_doc,replaceArray );
	}
    catch( e ){
        if( word_doc != null ){
            word_doc.Close();
        }
        if( word_app != null ){
            word_app.Quit();
        } 
        alert( "请修改浏览器安全设置，对没有标记为安全的ActiveX控件进行初始化和脚本运行时候进行提示" );
        return;
    }
}

/* 
 * 批量打印
 * @param path  打印模板路径
 * @param arr   需要替换字符数组 ["替换前的字符","替换后字符"]
 * @param flag  是否分页
 */
Print.batchExecuteWord = function(path,arr,flag){
    try{
        var word_app = new ActiveXObject( "Word.Application" );
        var word_doc = word_app.Documents.Open( path , false , true );

        word_doc.Content.Copy();        
		var inum = arr.length;
        for( var i = 0 ; i < inum-1 ; i++ ){			
			var r = word_doc.Goto( 3 , -1 );
			if(flag)
				r.InsertBreak( 7 );
            r.Paste();
        }        
        for( var i = 0 ; i < inum ; i++ ){
			var temp = arr[i];
			for(var j=0;j<temp.length;j++){
				if(temp[j].length <3)
					 Print.innerReplace( word_doc, temp[j][0] , temp[j][1]);//+"-"+i+"-"+j
				else
					 Print.textboxReplace( word_doc, temp[j][0] , temp[j][1]);
			}
        }        
		word_app.Visible = true;

	}catch( e ){
        if( word_doc != null ){
            word_doc.Close();
        }
        if( word_app != null ){
            word_app.Quit();
        } 
        alert( "请修改浏览器安全设置，将系统网址设置为信任站点！" );
        return;
    }
}