var SearchPage ={};

SearchPage.COLUMN_NUM = 2;
SearchPage.NEW_COLUMN_ARRAY_CNT = SearchPage.COLUMN_NUM * 2 + 1;
SearchPage.NOT_DATE_COLUMN_FLG = 0;
SearchPage.DATE_COLUMN_FLG = 1;
SearchPage.NO_LAYOUT_FLG = -1;
SearchPage.FIRST_TD_FLAG = "_Search";
SearchPage.MOVE_TD_NUM = 2;
SearchPage.condColNames = new Array("isGroup_Search", "idCard_Search", "currEduStyle_Search", "startWorkTime_Search", "manageSort_Search");
SearchPage.switchDiv = function (obj) {
	var divObj = document.getElementById(obj);
	if (divObj.style.display == "none") {
		divObj.style.display = "block";
	} else {
		divObj.style.display = "none";
	}
};
SearchPage.cellMapping = function (tblObj) {
	var srcTblRows = tblObj.rows.length;
	var newRowsPos = new Array(srcTblRows);
	for (i = 0; i < srcTblRows; i++) {
		newRowsPos[i] = new Array(this.NEW_COLUMN_ARRAY_CNT);
		for (j = 0; j < this.NEW_COLUMN_ARRAY_CNT; j++) {
			newRowsPos[i][j] = this.NO_LAYOUT_FLG;
		}
	}
  
  //condColName
	var condColCnt = this.condColNames.length;
	var condColPos = new Array(condColCnt);
  //search condColName
	var colTmp = "\n";
	for (i = 0; i < condColCnt; i++) {
		var colChild = document.getElementById(this.condColNames[i]);
		condColPos[i] = new Array(3);
		condColPos[i][0] = colChild.parentNode.rowIndex;
		condColPos[i][1] = colChild.cellIndex;
		condColPos[i][2] = tblObj.rows(condColPos[i][0]).cells.length;
    //alert("condColNames["+i+"]="+condColNames[i]+" row="+condColPos[i][0]+" cell="+condColPos[i][1]+" td count="+condColPos[i][2]);
		colTmp = colTmp + "condColNames[" + i + "]=" + this.condColNames[i] + " row=" + condColPos[i][0] + " cell=" + condColPos[i][1] + " td count=" + condColPos[i][2] + "\n";
	}
  
//  alert(colTmp);
  
  //SRC Cell Target Cell Mapping
	var newRowCnt = 0;
	var isFirst = true;
	var isLast = false;
	for (i = 0; i < condColCnt; i++) {
		if (i > 0) {
			isFirst = false;
		}
		if (i == (condColCnt - 1)) {
			isLast = true;
		}
  	
  	//DATE Column
		if (condColPos[i][2] == 2) {
			if (newRowsPos[newRowCnt][0] != this.NO_LAYOUT_FLG) {
				if (!isFirst) {
					if (newRowsPos[newRowCnt][4] == this.NO_LAYOUT_FLG) {
						newRowsPos[newRowCnt][4] = this.NOT_DATE_COLUMN_FLG;
					}
				}
				newRowCnt += 1;
			}
			newRowsPos[newRowCnt][0] = condColPos[i][0];
			newRowsPos[newRowCnt][1] = condColPos[i][1];
			newRowsPos[newRowCnt][4] = this.DATE_COLUMN_FLG;
			newRowCnt += 1;
		} else {
		    /*
		     * add a check condition for bug "QI-HR-040"
		     * not konw the logic detail. maybe should be confirmed!!!
		     */
		    //if (condColPos[i][2] == 4) {
			if (newRowsPos[newRowCnt] != undefined && condColPos[i][2] == 4) {
  		//Not DATE Column
				if (newRowsPos[newRowCnt][0] == this.NO_LAYOUT_FLG) {
					newRowsPos[newRowCnt][0] = condColPos[i][0];
					newRowsPos[newRowCnt][1] = condColPos[i][1];
					if (isLast) {
						newRowsPos[newRowCnt][4] = this.NOT_DATE_COLUMN_FLG;
						newRowCnt += 1;
					}
				} else {
					if (newRowsPos[newRowCnt][2] == this.NO_LAYOUT_FLG) {
						newRowsPos[newRowCnt][2] = condColPos[i][0];
						newRowsPos[newRowCnt][3] = condColPos[i][1];
						newRowsPos[newRowCnt][4] = this.NOT_DATE_COLUMN_FLG;
						newRowCnt += 1;
					} else {
					}
				}
			} else {
			    // deleting following sentence
				//alert("Layout Error");
			}
		}
  	
  	//alert("i="+i+" newRowCnt="+newRowCnt);
	}
  
  //alert("newRowCnt="+newRowCnt);
  
  //check new Row position data
	var msgTmp = "\n";
	for (i = 0; i < newRowCnt; i++) {
		for (j = 0; j < this.NEW_COLUMN_ARRAY_CNT; j++) {
			msgTmp = msgTmp + newRowsPos[i][j] + ",";
		}
		msgTmp = msgTmp + "\n";
	}
  //alert(colTmp+"nnewRowsPos[]="+msgTmp);
	var rtnMapping = new Array(newRowCnt);
	for (i = 0; i < newRowCnt; i++) {
		rtnMapping[i] = new Array(this.NEW_COLUMN_ARRAY_CNT);
		for (j = 0; j < this.NEW_COLUMN_ARRAY_CNT; j++) {
			rtnMapping[i][j] = newRowsPos[i][j];
		}
	}
	return rtnMapping;
};
SearchPage.moveCells = function (srcTbl, srcX, srcY, tgtTbl, tgtX, tgtY, moveCellNum) {
    //alert("srcX="+srcX+" srcY="+srcY+" tgtX="+tgtX+" tgtY="+tgtY);
	var srcRow = srcTbl.rows(srcX);
	var tgtRow = tgtTbl.rows(tgtX);
	if (tgtRow == null) {
		tgtRow = tgtTbl.insertRow(tgtX);
	}
	var srcAllCellsCnt = srcRow.cells.length;
	var tgtCnt = 0;
	for (k = srcY; k < (srcY + moveCellNum); k++) {
		var tgtCell = tgtRow.insertCell(tgtY + tgtCnt);
	  //old
		var srcCell = srcRow.cells(k);
		var srcCellElmsCnt = srcCell.children.length;
	  //alert("srcCell.children.length="+srcCellElmsCnt);
	  //setAttribute class and colSpan
		var srcCellClass = srcCell.getAttributeNode("class").nodeValue;
		if (srcCellClass.length > 0) {
			tgtCell.setAttribute("className", srcCellClass);
		}
	  //alert("Old Cell class='"+srcCell.getAttributeNode("class").nodeValue+"'"+ "New Cell class='"+tgtCell.getAttributeNode("class").nodeValue+"'");
		var srcColSpan = srcCell.colSpan;
		tgtCell.colSpan = srcColSpan;
	  //alert("Old Cell colSpan='"+srcCell.colSpan+"'"+ "New Cell colSpan='"+tgtCell.colSpan);
		if (srcCellElmsCnt == 0 && srcCell.firstChild != null) {
	    //label
			tgtCell.appendChild(srcCell.firstChild);
		} else {
			for (l = 0; l < srcCellElmsCnt; l++) {
				tgtCell.appendChild(srcCell.children[0]);
			}
		}
		tgtCnt += 1;
	}
};
SearchPage.creatEmptyTD = function (tblObj, curRowNum, moveCellNum) {
	var tgtRow = tblObj.rows(curRowNum);
	//Insert two empty td
	
	//get class & colspan of First TD
	var srcCellClass = tgtRow.cells(0).getAttributeNode("class").nodeValue;
	var srcColSpan = tgtRow.cells(0).colSpan;
	for (i = 0; i < this.COLUMN_NUM; i++) {
		var tgtCell = tgtRow.insertCell(this.COLUMN_NUM + i);
		tgtCell.appendChild(document.createTextNode(""));
		if (srcCellClass.length > 0) {
			tgtCell.setAttribute("className", srcCellClass);
		}
		tgtCell.colSpan = srcColSpan;
	}
};
SearchPage.deleteTableRows = function (tblObj, start, end) {
	var headerCount = 0;
	var footerCount = 0;
//	alert("tblObj.tHead='"+tblObj.tHead+"'");
	if (tblObj.tHead) {
		headerCount = tblObj.tHead.rows.length;
	} else {
	}
//	alert("tblObj.tFoot='"+tblObj.tFoot+"'");
	if (tblObj.tFoot) {
		footerCount = tblObj.tFoot.rows.length;
	}
	var bodyLast = end - footerCount;
//	alert("headerCount="+headerCount+" bodyLast"+bodyLast);
	for (var i = headerCount; i < bodyLast; i++) {
		tblObj.deleteRow(headerCount);
	}
};
SearchPage.checkTableContence = function (tblObj) {
	var newTblLen = tblObj.rows.length;
  //alert("New Table lines is "+newTblLen);
	for (i = 0; i < newTblLen; i++) {
		var rowi = tblObj.rows(i);
		var cellsCnt = tblObj.rows(i).cells.length;
		alert("New Tbl i=" + i + " cellsCnt=" + cellsCnt);
		for (j = 0; j < cellsCnt; j++) {
			var cellsk = tblObj.rows(i).cells(j);
			var oldCellElmsLength = cellsk.children.length;
	  //alert("cellsk.children.length="+oldCellElmsLength);
	  //setAttribute class and colSpan
			var srcCellClass = cellsk.getAttributeNode("class").nodeValue;
	  //alert("New Cell colSpan='"+cellsk.colSpan+"'"+ " class="+srcCellClass);
			if (oldCellElmsLength == 0) {
	    //label
	    //newCell.appendChild(document.createElement(cellsk.firstChild));
				alert(cellsk.firstChild.nodeValue);
			} else {
				for (l = 0; l < oldCellElmsLength; l++) {
		    //alert("row="+i+" column="+j+" l="+l+ " "+cellsk.children[l].tagName);
				}
			}
		}
	}
};
SearchPage.moveElement = function (tblid) {
if(this.condColNames.length==0)
return;
	var tblObj = document.getElementById(tblid);
	var oldTblRowCnt = tblObj.rows.length;
  //alert("表长；"+oldTblRowCnt+" Table border Attribute:"+tblObj.getAttributeNode("class").nodeValue);
	var newTblLastRowNum = oldTblRowCnt;
  
  //get cells mapping info
	var cellsMap = this.cellMapping(tblObj);

  //add blank line 
	tblObj.insertRow(newTblLastRowNum);
	newTblLastRowNum += 1;
  
  //check context
	var msgTmp = "\n";
	for (var i = 0; i < cellsMap.length; i++) {
    //DATE Column
    //alert("i="+i+" cellsMap[i][4]="+cellsMap[i][4]);
		if (cellsMap[i][4] == this.DATE_COLUMN_FLG) {
			this.moveCells(tblObj, cellsMap[i][0], cellsMap[i][1], tblObj, newTblLastRowNum, 0, this.MOVE_TD_NUM);
		} else {
			if (cellsMap[i][4] == this.NOT_DATE_COLUMN_FLG) {
        //Move first Block
				this.moveCells(tblObj, cellsMap[i][0], cellsMap[i][1], tblObj, newTblLastRowNum, 0, this.MOVE_TD_NUM);
    	//Second TD Block
				if (cellsMap[i][2] != this.NO_LAYOUT_FLG) {
					this.moveCells(tblObj, cellsMap[i][2], cellsMap[i][3], tblObj, newTblLastRowNum, 2, this.MOVE_TD_NUM);
				} else {
    		//Create two empty td with class
					this.creatEmptyTD(tblObj, newTblLastRowNum, this.MOVE_TD_NUM);
				}
			} else {
				alert("Data Error");
			}
		}
		newTblLastRowNum += 1;
		for (j = 0; j < this.NEW_COLUMN_ARRAY_CNT; j++) {
			msgTmp = msgTmp + cellsMap[i][j] + ",";
		}
		msgTmp = msgTmp + "\n";
	}
  //alert(msgTmp);
  //alert("表长1；"+tblObj.rows.length);
 
  // alert("Delete Table Start=0,"+"End="+(oldTblRowCnt));
  //delete Old table Element
	this.deleteTableRows(tblObj, 0, oldTblRowCnt);
  
  //new Table content
  //checkTableContence(tblObj);
};