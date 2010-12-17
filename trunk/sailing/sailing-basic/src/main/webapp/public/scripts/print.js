
if(Print == null){
	var Print = {};
}

/* 
 * ��ѡ������ ���� �� Excel
 * ȱʡѡ�� ����ҳ��
 * obj ֻ����table �� ID 
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
			alert(' �뽫������ [ ' + ContextInfo.serverAddr + ' ] ����Ϊ������վ�㲢��װ Excel �Ա�����ʹ�õ������� ');	
			return;
		}
		
		var excel_book = excel_app.Workbooks.Add(); 
		excel_app.Visible = true;
		excel_book.Activate();

		var excel_sheet = excel_book.Sheets(1)
		excel_sheet.Activate();
		
		//����excel 2000����
        excel_sheet.Paste();
        //excel_sheet.Cells.NumberFormat = "@";
       //excel_sheet.PasteSpecial( "HTML" , false , false , "" , 0 , "" , true );
        //��Ӧ���ߴ�
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
				//��ӡȡ��ʱ�����쳣
			}
		}
	/*}catch( e ){
		alert("��ˢ��ҳ�������");
		if(excel_book)
			excel_book.Close();
		if(excel_app)
			excel_app.Quit();
	}*/
}

/* 
 * ��ѡ������ ���� �� Excel
 * ȱʡѡ�� ����ҳ��
 * obj ֻ����table �� ID 
 */
 Print.exportExcelByHTML = function(outerHTML, method){
	//try{
		window.clipboardData.setData("Text", outerHTML);
		
		var excel_app = null;
		try {
			excel_app = new ActiveXObject( "Excel.Application" );
		} catch (e) {
			alert(' �뽫������ [ ' + ContextInfo.serverAddr + ' ] ����Ϊ������վ�㲢��װ Excel �Ա�����ʹ�õ������� ');	
			return;
		}
		
		var excel_book = excel_app.Workbooks.Add(); 
		excel_app.Visible = true;
		excel_book.Activate();

		var excel_sheet = excel_book.Sheets(1)
		excel_sheet.Activate();
		
		//����excel 2000����
        excel_sheet.Paste();
        //excel_sheet.Cells.NumberFormat = "@";
       //excel_sheet.PasteSpecial( "HTML" , false , false , "" , 0 , "" , true );
        //��Ӧ���ߴ�
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
				//��ӡȡ��ʱ�����쳣
			}
		}
	/*}catch( e ){
		alert("��ˢ��ҳ�������");
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
 * ��ѡ������ ���� ��word
 * ȱʡѡ�� ����ҳ��
 * obj ֻ����table �� ID 
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
		alert("��ˢ��ҳ�������");
		word_doc.Close();
		word_app.Quit();
	}
}


/* ��ָ�������ݴ�ӡ��word��
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
 * �滻word�ĵ��е�tag.�������һ�㱻replaceʹ��.
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

// �滻 div�����ݣ�����ʵ���״�
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
		alert(tag + " �滻Ϊ "+value+" ����"+e);
	}
}

//--�򿪴�ӡģ��
// @param path         ��ӡģ��·��
// @param replaceArray ��Ҫ�滻�ַ����� ["�滻ǰ���ַ�","�滻���ַ�"]
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
        alert( "���޸��������ȫ���ã���û�б��Ϊ��ȫ��ActiveX�ؼ����г�ʼ���ͽű�����ʱ�������ʾ" );
        return;
    }
}

/* 
 * ������ӡ
 * @param path  ��ӡģ��·��
 * @param arr   ��Ҫ�滻�ַ����� ["�滻ǰ���ַ�","�滻���ַ�"]
 * @param flag  �Ƿ��ҳ
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
        alert( "���޸��������ȫ���ã���ϵͳ��ַ����Ϊ����վ�㣡" );
        return;
    }
}