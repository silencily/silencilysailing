/**
 * 定义常用的选择树操作
 * @since 2006-06-22
 * @author 王政
 * @version $Id: treeUtils.js,v 1.1 2010/12/10 10:56:35 silencily Exp $
 */

if (TreeUtils == null) {
	var TreeUtils = {};
}

//~ Instance fields ========================================================

// 不选
TreeUtils.treeTypeNone = 0; 
// 单选
TreeUtils.treeTypeSingle = 1;
// 多选
TreeUtils.treeTypeMulti = 2;
// 多选值之间的间隔
TreeUtils.separator = ',';


//~ Methods ================================================================

/**
 * 选择用户并设置到输入域中
 * @param objId 需要填充 用户名 的输入域
 * @param objDepartmentId 需要填充 用户组织机构Id 的输入域
 * @param objName 需要填充 用户中文名 的输入域
 * @param treeType 树类型, 可分为 TreeUtils.treeTypeNone, TreeUtils.treeTypeSingle, TreeUtils.treeTypeMulti
 * @departmentId 查询的组织机构号, 将列出此组织机构下的用户
 * @roleCodes  根据 role Code 来过滤用户，这里可以是多个，用","分隔
 * add：将原来输入框值保留后添加，默认为 treeType == TreeUtils.treeTypeMulti 有效
 * fix: 重复数据过滤掉
 */
TreeUtils.selectAndSetUser = function(objId, objDepartmentId, objName, treeType, departmentId,roleCodes) {
	TreeUtils.selectUser(objId.value, treeType, departmentId,roleCodes);
	
	top.definedWin.returnOpenTreeValue = function(users){
		users = TreeUtils.beforeReturnValue(users)
		var idValue = '';
		var deptIdValue = '';
		var deptNaValue = '';
		var nameValue = '';	
		var oldValue = TreeUtils.separator+objId.value+TreeUtils.separator;
		for (var i = 0; i < users.length; i++) {
			if(oldValue.indexOf(TreeUtils.separator+users[i].code+TreeUtils.separator)>=0)
				continue;
			idValue += users[i].code;
			deptIdValue += users[i].departId;
			deptNaValue += users[i].departName;
			nameValue += users[i].name;
			if (i < users.length - 1) {
				idValue += TreeUtils.separator;
				deptIdValue += TreeUtils.separator;
				deptNaValue += TreeUtils.separator;
				nameValue += TreeUtils.separator;
			}
		}
		if(treeType == TreeUtils.treeTypeMulti){
			if(objId){
				if(objId.value != "")
					objId.value += TreeUtils.separator; 
				objId.value += idValue;	
			}
			if(objName){
				if(objName.value != "")
					objName.value += TreeUtils.separator;
				objName.value += nameValue;
			}
		}else{
			if(objId){
				objId.value = idValue;	
			}
			if(objName){
				objName.value = nameValue;
			}
			if(objDepartmentId){
				objDepartmentId.value = deptIdValue;
			}
		}
		TreeUtils.afterReturnValue(idValue,nameValue,deptIdValue);
		top.definedWin.closeModalWindow();
	}
}
//TODO 回调
TreeUtils.beforeReturnValue = function(users){return users;}
TreeUtils.afterReturnValue = function(ids,names,depts){}

/**
 * 选择组织机构并设置到输入域中
 * @param objId 需要填充 组织机构Id 的输入域
 * @param objName 需要填充 组织机构名称 的输入域
 * @param treeType 树类型, 可分为 TreeUtils.treeTypeNone, TreeUtils.treeTypeSingle, TreeUtils.treeTypeMulti
 * @departmentId 过滤组织机构的父组织机构ID号, 设定后将列出此组织机构下的组织
 */
TreeUtils.selectAndSetDepartment = function(objId, objName, treeType, departmentId) {
	TreeUtils.selectDepartment(objId.value, treeType, departmentId);
	
	top.definedWin.returnOpenTreeValue = function(departments){	
		var idValue = '';
		var nameValue = '';		
		for (var i = 0; i < departments.length; i++) {
			idValue += departments[i].nodeId;
			nameValue += departments[i].nodeName;
			if (i < departments.length - 1) {
				idValue += TreeUtils.separator;
				nameValue += TreeUtils.separator;
			}
		}		
		objId.value = idValue;
		objName.value = nameValue;
		top.definedWin.closeModalWindow();
	}
}

/**
 * 选择代码表数据并设置到输入域中
 * @param objId 需要 代码 code 的输入域
 * @param objName 需要填充 代码名称 的输入域
 * @param treeType 树类型, 可分为 TreeUtils.treeTypeNone, TreeUtils.treeTypeSingle, TreeUtils.treeTypeMulti
 * @param codeCategory 代码表 code, 将列出此 code 的所有子 code
 */
TreeUtils.selectAndSetCode = function(objId, objName, treeType, codeCategory) {			
	TreeUtils.selectCode(objId.value, treeType, codeCategory);
	
	top.definedWin.returnOpenTreeValue = function(codes){
		var idValue = '';
		var nameValue = '';
		
		for (var i = 0; i < codes.length; i++) {
			  idValue += codes[i].selfId;
			nameValue += codes[i].nodeName;
			if (i < codes.length - 1) {
				idValue += TreeUtils.separator;
				nameValue += TreeUtils.separator;
			}
		}	
		objId.value = idValue;
		objName.value = nameValue;
		top.definedWin.closeModalWindow();
	}
}

/**
 * 选择用户方法
 * @param objectValue 已选中的 username, 一般从页面上获取
 * @param treeType 树类型, 可分为 TreeUtils.treeTypeNone, TreeUtils.treeTypeSingle, TreeUtils.treeTypeMulti
 * @param id 组织机构号, 将列出此组织机构下的用户
 * @return 数组, 里面的对象为 user, 属性有 username/chineseName/departmentId, 如果用户没有选择, 返回长度为零的数组
 */
TreeUtils.selectUser = function(objectValue, treeType, departmentId,roleCodes) {
	treeType = treeType ? treeType : TreeUtils.treeTypeSingle;
	departmentId = departmentId ? departmentId : "";
	roleCodes = roleCodes ? roleCodes : "";
	//var url = ContextInfo.contextPath + '/common/ui/selector.do?step=selectUser&id=' + departmentId; 
	var url =  ContextInfo.contextPath + '/common/ui/organization.do?step=frame&oid='+departmentId
		url += '&roleCodes='+roleCodes;
	var retrivedValue = TreeUtils.openTree(objectValue, treeType, url,'selectUser'+treeType+departmentId+roleCodes);
	//userName chinese orgId
}

/**
 * 选择组织机构方法
 * @param objectValue 已选中的 departmentId, 一般从页面上获取
 * @param treeType 树类型, 可分为 TreeUtils.treeTypeNone, TreeUtils.treeTypeSingle, TreeUtils.treeTypeMulti
 * @param id 组织机构号, 将列出此组织机构下的组织机构
 * @return 数组, 里面的对象为 department, 属性有 departmentId 和 depattmentName, 如果用户没有选择, 返回长度为零的数组
 */
TreeUtils.selectDepartment = function(objectValue, treeType, departmentId) {
	if (typeof treeType == 'undefined') {
		treeType = TreeUtils.treeTypeSingle;
	}
	if (typeof departmentId == 'undefined') {
		departmentId = '';
	}
	
	var url = ContextInfo.contextPath + '/common/ui/selector.do?step=selectDept&id=' + departmentId; 
	TreeUtils.openTree(objectValue, treeType, url,'selectDept'+treeType+departmentId);
}


/**
 * 选择代码表数据
 * @param objectValue 已选中的 code, 一般从页面上获取
 * @param treeType 树类型, 可分为 TreeUtils.treeTypeNone, TreeUtils.treeTypeSingle, TreeUtils.treeTypeMulti
 * @param id 代码表 code, 将列出此 code 的所有子 code
 * @return 数组, 里面的对象为 code, 属性有 code 和 name, 如果用户没有选择, 返回长度为零的数组
 */
TreeUtils.selectCode = function(objectValue, treeType, codeType) {
	if (typeof treeType == 'undefined') {
		treeType = TreeUtils.treeTypeSingle;
	}
	
	var url = ContextInfo.contextPath + '/jsp/selector/selectCode1.jsp?id=' + codeType;
	TreeUtils.openTree(objectValue, treeType, url,'selectCode'+treeType+codeType);
}

TreeUtils.openTree = function(objectValue, treeType, url,txtName) {
	if (url.indexOf('?') < 0) {
		url += '?';
	} else {
		url += '&';
	}	
	url += 'treeType=' + treeType + '&SelectNodeId=' + objectValue;
	//return StyleControl.openDialogInFrame(url, 600, 500);
	definedWin.openListingUrl(txtName,url);
}

/* 选择动态树，将返回的selfId和nodeName分别装入tid & tname
 * tid    存放ID,可以是对象，或者是对象名
 * tname  存放Name, 同上
 */
TreeUtils.selectDynamicTree = function (tid,tname,url,txtName){
	var iobj = typeof tid == 'object' ? tid : document.getElementsByName(tid)[0];
	var nobj = typeof tname == 'object' ? tname : document.getElementsByName(tname)[0];
	definedWin.openListingUrl(txtName,url);
	top.definedWin.returnOpenTreeValue = function(rev){
		if(rev){
			iobj.value = rev[0].selfId;
			nobj.value = rev[0].nodeName;
		}
		top.definedWin.closeModalWindow();
	}
}

//选择静态树，将返回的nodeId和nodeName分别装入tid & tname
TreeUtils.selectTree = function (tid,tname,url,txtName){
	var iobj = document.getElementsByName(tid)[0];
	var nobj = document.getElementsByName(tname)[0];
	//var rev  = StyleControl.openDialogInFrame(url,650,550);
	definedWin.openListingUrl(txtName,url);
	top.definedWin.returnOpenTreeValue = function(rev){
		if(rev){
			iobj.value = rev[0].nodeId;
			nobj.value = rev[0].nodeName;
		}
		top.definedWin.closeModalWindow();
	}
}

TreeUtils.selectListing = function (txtName,url){
	definedWin.openUrl(txtName,url);
}

