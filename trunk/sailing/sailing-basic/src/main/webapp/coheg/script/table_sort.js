
/*-- 2004-11-06 
 *-- 前端Table排序
 */
var global_isByTagName = (document.getElementsByTagName) ? true : false;
var global_isMSIE5 = (document.getElementsByTagName && document.all) ? true : false;
var arrowUp, arrowDown;

if (global_isMSIE5 || global_isByTagName)
	initSortTable();

function initSortTable() {
	 arrowUp = document.createElement("span");
	 var tn = document.createTextNode("↓");
	 arrowUp.appendChild(tn);
	 //arrowUp.className = "arrow_up";
	 arrowDown = document.createElement("span");
	 var tn = document.createTextNode("↑");
	 arrowDown.appendChild(tn);
	 //arrowDown.className = "arrow_down";
}

function sortTable(tableNode, nCol, bDesc, sType) {
	 var tBody = tableNode.tBodies[0];
	 var trs = tBody.rows;
	 var a = new Array();
	 
	 for (var i=0; i<trs.length; i++) {
		a[i] = trs[i];
	 }
 
	a.sort(compareByColumn(nCol,bDesc,sType));
 
	for (var i=0; i<a.length; i++) {
		tBody.appendChild(a[i]);
	 }
}
function CaseInsensitiveString(s) {
	return String(s).toUpperCase();
}
function parseDate(s) {
	return Date.parse(s.replace(/\-/g, '/'));
}
function toNumber(s) {
    return Number(s.replace(/[^0-9\.]/g, ""));
}
function compareByColumn(nCol, bDescending, sType) {
	 var c = nCol;
	 var d = bDescending;
	 
	 var fTypeCast = String;

	 if (sType == "Number")
		fTypeCast = Number;
	 else if (sType == "Date")
		fTypeCast = parseDate;
	 else if (sType == "CaseInsensitiveString")
		fTypeCast = CaseInsensitiveString;
	 return function (n1, n2) {
		  if (fTypeCast(getInnerText(n1.cells[c])) < fTypeCast(getInnerText(n2.cells[c])))
				return d ? -1 : +1;
		  if (fTypeCast(getInnerText(n1.cells[c])) > fTypeCast(getInnerText(n2.cells[c])))
				return d ? +1 : -1;
		  return 0;
	 };
}

function sortColumn(e) {
	 var tmp, el, tHeadParent;
	 if (global_isMSIE5)
		tmp = e.srcElement;
	 else if (global_isByTagName)
		tmp = e.target;
	 tHeadParent = getParentTagName(tmp, "THEAD");
	 el = getParentTagName(tmp, "TD");
	 if (tHeadParent == null)
		return;

	 if(el.innerHTML.indexOf('type=checkbox')>=0)
		 return
	 if (el != null) {
		var p = el.parentNode;
		var i;
		if (el._descending) // catch the null
			el._descending = false;
		else
			el._descending = true;
	  
		  if (tHeadParent.arrow != null) {
			   if (tHeadParent.arrow.parentNode != el) {
					tHeadParent.arrow.parentNode._descending = null;   
			   }
				tHeadParent.arrow.parentNode.removeChild(tHeadParent.arrow);
		  }
		  if (el._descending)
				tHeadParent.arrow = arrowDown.cloneNode(true);
		  else
				tHeadParent.arrow = arrowUp.cloneNode(true);
		  el.appendChild(tHeadParent.arrow);
		   
		  // get the index of the td
		  for (i=0; i<p.cells.length; i++) {
				if (p.cells[i] == el) break;
		  }
		  var table = getParentTagName(el, "TABLE");
		  // can't fail	  
		  sortTable(table,i,el._descending, el.getAttribute("type"));
	}
}

function getInnerText(el) {
	if (global_isMSIE5) return el.innerText; //Not needed but it is faster 
	var str = "";
 
	for (var i=0; i<el.childNodes.length; i++) {
		  switch (el.childNodes.item(i).nodeType) {
		   case 1: //ELEMENT_NODE
				str += getInnerText(el.childNodes.item(i));
				break;
		   case 3: //TEXT_NODE
				str += el.childNodes.item(i).nodeValue;
				break;
		  }  
	} 
	return str;
}
function getParentTagName(el, pTagName) {
	 if (el == null) 
		 return null;
	 else if (el.nodeType == 1 && el.tagName.toLowerCase() == pTagName.toLowerCase())
		return el;
	 else
		return getParentTagName(el.parentNode, pTagName);
}

//点击列表事件
var temp_last_click = null;
function clickListing(el){
	var tmp;
	if (global_isMSIE5)
		tmp = el.srcElement;
	 else if (global_isByTagName)
		tmp = el.target;
	 trObj = getParentTagName(tmp, "TR");
	 if(trObj == temp_last_click)
		 return;
	 trObj.className = "Alt";
	 if(temp_last_click)
	 	temp_last_click.className = "";
	 temp_last_click = trObj
}