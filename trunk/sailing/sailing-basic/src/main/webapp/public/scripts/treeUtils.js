/**
 * ���峣�õ�ѡ��������
 * @since 2006-06-22
 * @author ����
 * @version $Id: treeUtils.js,v 1.1 2010/12/10 10:56:35 silencily Exp $
 */

if (TreeUtils == null) {
	var TreeUtils = {};
}

//~ Instance fields ========================================================

// ��ѡ
TreeUtils.treeTypeNone = 0; 
// ��ѡ
TreeUtils.treeTypeSingle = 1;
// ��ѡ
TreeUtils.treeTypeMulti = 2;
// ��ѡֵ֮��ļ��
TreeUtils.separator = ',';


//~ Methods ================================================================

/**
 * ѡ���û������õ���������
 * @param objId ��Ҫ��� �û��� ��������
 * @param objDepartmentId ��Ҫ��� �û���֯����Id ��������
 * @param objName ��Ҫ��� �û������� ��������
 * @param treeType ������, �ɷ�Ϊ TreeUtils.treeTypeNone, TreeUtils.treeTypeSingle, TreeUtils.treeTypeMulti
 * @departmentId ��ѯ����֯������, ���г�����֯�����µ��û�
 * @roleCodes  ���� role Code �������û�����������Ƕ������","�ָ�
 * add����ԭ�������ֵ��������ӣ�Ĭ��Ϊ treeType == TreeUtils.treeTypeMulti ��Ч
 * fix: �ظ����ݹ��˵�
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
//TODO �ص�
TreeUtils.beforeReturnValue = function(users){return users;}
TreeUtils.afterReturnValue = function(ids,names,depts){}

/**
 * ѡ����֯���������õ���������
 * @param objId ��Ҫ��� ��֯����Id ��������
 * @param objName ��Ҫ��� ��֯�������� ��������
 * @param treeType ������, �ɷ�Ϊ TreeUtils.treeTypeNone, TreeUtils.treeTypeSingle, TreeUtils.treeTypeMulti
 * @departmentId ������֯�����ĸ���֯����ID��, �趨���г�����֯�����µ���֯
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
 * ѡ���������ݲ����õ���������
 * @param objId ��Ҫ ���� code ��������
 * @param objName ��Ҫ��� �������� ��������
 * @param treeType ������, �ɷ�Ϊ TreeUtils.treeTypeNone, TreeUtils.treeTypeSingle, TreeUtils.treeTypeMulti
 * @param codeCategory ����� code, ���г��� code �������� code
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
 * ѡ���û�����
 * @param objectValue ��ѡ�е� username, һ���ҳ���ϻ�ȡ
 * @param treeType ������, �ɷ�Ϊ TreeUtils.treeTypeNone, TreeUtils.treeTypeSingle, TreeUtils.treeTypeMulti
 * @param id ��֯������, ���г�����֯�����µ��û�
 * @return ����, ����Ķ���Ϊ user, ������ username/chineseName/departmentId, ����û�û��ѡ��, ���س���Ϊ�������
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
 * ѡ����֯��������
 * @param objectValue ��ѡ�е� departmentId, һ���ҳ���ϻ�ȡ
 * @param treeType ������, �ɷ�Ϊ TreeUtils.treeTypeNone, TreeUtils.treeTypeSingle, TreeUtils.treeTypeMulti
 * @param id ��֯������, ���г�����֯�����µ���֯����
 * @return ����, ����Ķ���Ϊ department, ������ departmentId �� depattmentName, ����û�û��ѡ��, ���س���Ϊ�������
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
 * ѡ����������
 * @param objectValue ��ѡ�е� code, һ���ҳ���ϻ�ȡ
 * @param treeType ������, �ɷ�Ϊ TreeUtils.treeTypeNone, TreeUtils.treeTypeSingle, TreeUtils.treeTypeMulti
 * @param id ����� code, ���г��� code �������� code
 * @return ����, ����Ķ���Ϊ code, ������ code �� name, ����û�û��ѡ��, ���س���Ϊ�������
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

/* ѡ��̬���������ص�selfId��nodeName�ֱ�װ��tid & tname
 * tid    ���ID,�����Ƕ��󣬻����Ƕ�����
 * tname  ���Name, ͬ��
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

//ѡ��̬���������ص�nodeId��nodeName�ֱ�װ��tid & tname
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

