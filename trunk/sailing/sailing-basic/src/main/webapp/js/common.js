
	/********************************************弹出部门选择窗体********************************************/
	//  参数presetid说明：调用页面上的部门编号控件的id属性值
	//  参数setid说明：要设置的部门编号的控件的name属性值
	//  参数setname说明：要设置的部门名称的控件的name属性值	
	//  参数title说明：窗体的标题名称，不需要设置时，将其设置为''即可
	//  参数iCheckMode说明："1"代表单选框，"0"代表复选框
	//  样例参考：	showCgmlTreeWindow(deptId, 'bean.deptId', 'bean.deptName', '', '1')
	/*****************************************************************************************************/
	function showCgmlTreeWindow(presetid, setid, setname, title, iCheckMode)
	{
		document.getElementById(setid).readOnly = false;
		document.getElementById(setname).readOnly = false;
		var USER_WIN_WIDTH = 500;
		var USER_WIN_HEIGHT = 430;
		var sFeatures = "dialogHeight:" + USER_WIN_HEIGHT + "px; dialogWidth:" + USER_WIN_WIDTH + "px;edge:Raised; center:Yes; help:No; resizable:no; status:No;scroll:no";
		//跳到showDeptTree页面
		var url = ContextInfo.contextPath + "/sm/TblSmDeptAction.do?step=showDeptTree&treeType=" + iCheckMode;
		//设置窗体的属性
		var deptInfo = window.showModalDialog(url, title, sFeatures);
		if (deptInfo != null)
		{
			//得到部门id，部门name
			document.getElementById(setid).value = deptInfo.deptId;
		    document.getElementById(setname).value = deptInfo.deptName;
		}
		document.getElementById(setid).readOnly = true;
		document.getElementById(setname).readOnly = true;
		return;
	}
		
	/********************************************弹出人员选择窗体********************************************/
	//  参数empInfoPre说明：要设置的员工号或员工名的ID属性值中位于"."的前半部分的名称，
	//                 例如，在empinfo.empCd或empinfo.empName的取"empinfo"部分
	//  参数definedWin说明：自定义窗口对象
	//  参数iCheckMode说明："radio"代表单选框，"checkbox"代表复选框。如果为"checkbox"时，还需要在调用该函数的
	//                  页面上实现回调函数definedWin.setListObjectInFor = function(arr,attribute,arrWithAtt)
	//                  多选时的样例参考：qware工程中的web\jsp\hr\edu\trainTeamInfoInfo.jsp页面
	//  样例参考：selectEmp('empinfo', definedWin, 'radio')
	/*****************************************************************************************************/
	selectEmp = function(empInfoPre, definedWin, iCheckMode){
		var url = ContextInfo.contextPath + "/sm/SelectInfoAction.do?step=selectEntry&checkType=" + iCheckMode;
		definedWin.openListingUrl(empInfoPre, url);
	}
	
		/********************************************新建弹出选择窗体********************************************/
	//  参数empInfoPre说明：要设置的员工号或员工名的ID属性值中位于"."的前半部分的名称，
	//                 例如，在empinfo.empCd或empinfo.empName的取"empinfo"部分
	//  参数definedWin说明：自定义窗口对象
	//  参数iCheckMode说明："radio"代表单选框，"checkbox"代表复选框。如果为"checkbox"时，还需要在调用该函数的
	//                  页面上实现回调函数definedWin.setListObjectInFor = function(arr,attribute,arrWithAtt)
	//                  多选时的样例参考：qware工程中的web\jsp\hr\edu\trainTeamInfoInfo.jsp页面
	//  样例参考：selectEmp('empinfo', definedWin, 'radio')
	/*****************************************************************************************************/
		selectTcr = function(empInfoPre, definedWin, iCheckMode){
		var url = ContextInfo.contextPath + "/tr/web/TrSearchAction.do?step=selectEntry&checkType=" + iCheckMode;
		definedWin.openListingUrl(empInfoPre, url);
	}
	
	/********************************************弹出角色选择窗体********************************************/
	//  参数roleInfoPre说明：要设置的角色ID或角色名的属性值中位于"."的前半部分的名称，
	//                 例如，在roleinfo.id或roleinfo.name的取"roleinfo"部分
	//  参数definedWin说明：自定义窗口对象
	//  样例参考：selectRole('roleinfo', definedWin)
	/*****************************************************************************************************/
	selectRole = function(roleInfoPre, definedWin){
		var url = ContextInfo.contextPath + "/sm/RoleAction.do?step=selectRoleEntry";
		definedWin.openListingUrl(roleInfoPre, url);
	}
		/********************************************根据角色弹出用户选择窗体********************************************/
	//  参数role说明：	要输入的查询条件，Array类型，内存角色ID，用户选择窗体中将只显示与role中角色相关的用户。
	//  返回值说明：       Array类型，内存用户ID
	//  样例参考：		userIds = selectUser(roleIds)
	/*****************************************************************************************************/
	selectUser = function(role){
		var strs ="";
		var len=role.length;
		for(var i=0;i<len;i++)
		{
			strs+=role[i] + "$";
		}
		var iHeight="600";
		var iWidth="850";
		var sFeatures="dialogHeight: " + iHeight + "px;" + "dialogWidth: " + iWidth + "px;" + "help:no;" + "scroll:0;";
		return window.showModalDialog(ContextInfo.contextPath + "/jsp/sm/user/selectUserEntry.jsp?roleIds="+strs,"",sFeatures);
	}
	
			/********************************************根据角色及部门弹出用户选择窗体********************************************/
	//  参数说明：	要输入的查询条件，role为Array类型，内存角色ID，dept为部门ID,用户选择窗体中将只显示与role中角色相关及部门ID为dept的用户。
	//  返回值说明：       Array类型，内存用户ID
	//  样例参考：		userIds = selectUser(roleIds,dept);
	/*****************************************************************************************************/
	selectUser = function(role,deptId){
		var strs ="";
		var len=role.length;
		for(var i=0;i<len;i++)
		{
			strs+=role[i] + "$";
		}
		var iHeight="600";
		var iWidth="850";
		var sFeatures="dialogHeight: " + iHeight + "px;" + "dialogWidth: " + iWidth + "px;" + "help:no;" + "scroll:0;";
		return window.showModalDialog(ContextInfo.contextPath + "/jsp/sm/user/selectUserIdsEntry.jsp?roleIds="+strs+"&deptId="+deptId,"",sFeatures);
	}
	
	/********************************************弹出费率选择窗体********************************************/
	//  参数rateInfoPre说明：要设置的岗位岗级的ID属性值中位于"."的前半部分的名称，
	//                 例如，在rateInfo.station或rateInfo.stationGrade的取"rateInfo"部分
	//  参数definedWin说明：自定义窗口对象
	//  参数iCheckMode说明："radio"代表单选框，"checkbox"代表复选框。
	//  样例参考：selectRate('empinfo', definedWin, 'radio')
	/*****************************************************************************************************/
	selectRate = function(rateInfoPre, definedWin, iCheckMode){
		
		var url = ContextInfo.contextPath + "/hr/ratemanage.do?step=selectEntry&checkType=" + iCheckMode;
		
		definedWin.openListingUrl(rateInfoPre, url);
	}
	
	/********************************************弹出工单类别选择窗体********************************************/
	//参数"iFlag"用来控制从不同的原页面弹出时，哪些radio可用，哪些radio不可用。
	//"位置管理-位置"的详细页面、"资产管理-资产"的详细页面、"状态监测-测点"的详细页面iFlag=0；"检修项目管理-检修项目"的详细页面iFlag=1；
	//参数"arr"用来保存从"工单类别选择"弹出页面中选择的某工单的URL
	selectWoType = function(iFlag){
		var url = ContextInfo.contextPath + "/am/WoTypeSelAction.do?step=info&iFlag=" + iFlag;
		var arr = showModalDialog(url,"","font-family:Verdana; font-size:10; dialogWidth:800px; dialogHeight:500px;");
		return arr;
	}
	
	settable =function(pageid,asc)
	{
		var url = ContextInfo.contextPath +'/curd/curdAction.do?step=setTable&pageId=';
		url+=pageid;
		if(asc!=null)
			url+='&asc=asc';
		definedWin.openListingUrl("setTable",url);
	}
	
	popSelect = function(rateInfoPre,url, selectType, editPage){
		if(selectType==null)
		selectType='only';
		var url = ContextInfo.contextPath + url+'&popSelectType='+selectType+"&paginater.page=0";
		
		definedWin.editPageIns=editPage;
		definedWin.openListingUrl(rateInfoPre, url);
	}
	/********************************************弹出输入用户名口令窗体********************************************/
	//  base：			用来接受用户名和口令的ID属性值中位于"."的前半部分的名称，
	//  样例参考：		
	/*****************************************************************************************************/
	inputUserPwd = function(base){
		var iHeight="600";
		var iWidth="850";
		var sFeatures="dialogHeight: " + iHeight + "px;" + "dialogWidth: " + iWidth + "px;" + "help:no;" + "scroll:0;";
		if(base)
			return window.showModalDialog(ContextInfo.contextPath + "/jsp/sm/user/inputUserPwd.jsp?elementBaseName="+base,"",sFeatures);
		else
			return window.showModalDialog(ContextInfo.contextPath + "/jsp/sm/user/inputUserPwd.jsp","",sFeatures);	
	}
	/********************************************弹出显示签字人列表窗体********************************************/
	//  className：			工单实体类名称
	//  样例参考：		
	/*****************************************************************************************************/
	showSigners = function(className)
	{
		var iHeight="600";
		var iWidth="850";
		var sFeatures="dialogHeight: " + iHeight + "px;" + "dialogWidth: " + iWidth + "px;" + "help:no;" + "scroll:0;";
		var oid = document.getElementsByName("oid")[0].value;
		return window.showModalDialog(ContextInfo.contextPath + "/wo/ShowSignerAction.do?step=entry&oid="+oid+"&className="+className,"",sFeatures);
		
	}
	
	
