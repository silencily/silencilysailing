
	/********************************************��������ѡ����********************************************/
	//  ����presetid˵��������ҳ���ϵĲ��ű�ſؼ���id����ֵ
	//  ����setid˵����Ҫ���õĲ��ű�ŵĿؼ���name����ֵ
	//  ����setname˵����Ҫ���õĲ������ƵĿؼ���name����ֵ	
	//  ����title˵��������ı������ƣ�����Ҫ����ʱ����������Ϊ''����
	//  ����iCheckMode˵����"1"����ѡ��"0"����ѡ��
	//  �����ο���	showCgmlTreeWindow(deptId, 'bean.deptId', 'bean.deptName', '', '1')
	/*****************************************************************************************************/
	function showCgmlTreeWindow(presetid, setid, setname, title, iCheckMode)
	{
		document.getElementById(setid).readOnly = false;
		document.getElementById(setname).readOnly = false;
		var USER_WIN_WIDTH = 500;
		var USER_WIN_HEIGHT = 430;
		var sFeatures = "dialogHeight:" + USER_WIN_HEIGHT + "px; dialogWidth:" + USER_WIN_WIDTH + "px;edge:Raised; center:Yes; help:No; resizable:no; status:No;scroll:no";
		//����showDeptTreeҳ��
		var url = ContextInfo.contextPath + "/sm/TblSmDeptAction.do?step=showDeptTree&treeType=" + iCheckMode;
		//���ô��������
		var deptInfo = window.showModalDialog(url, title, sFeatures);
		if (deptInfo != null)
		{
			//�õ�����id������name
			document.getElementById(setid).value = deptInfo.deptId;
		    document.getElementById(setname).value = deptInfo.deptName;
		}
		document.getElementById(setid).readOnly = true;
		document.getElementById(setname).readOnly = true;
		return;
	}
		
	/********************************************������Աѡ����********************************************/
	//  ����empInfoPre˵����Ҫ���õ�Ա���Ż�Ա������ID����ֵ��λ��"."��ǰ�벿�ֵ����ƣ�
	//                 ���磬��empinfo.empCd��empinfo.empName��ȡ"empinfo"����
	//  ����definedWin˵�����Զ��崰�ڶ���
	//  ����iCheckMode˵����"radio"����ѡ��"checkbox"����ѡ�����Ϊ"checkbox"ʱ������Ҫ�ڵ��øú�����
	//                  ҳ����ʵ�ֻص�����definedWin.setListObjectInFor = function(arr,attribute,arrWithAtt)
	//                  ��ѡʱ�������ο���qware�����е�web\jsp\hr\edu\trainTeamInfoInfo.jspҳ��
	//  �����ο���selectEmp('empinfo', definedWin, 'radio')
	/*****************************************************************************************************/
	selectEmp = function(empInfoPre, definedWin, iCheckMode){
		var url = ContextInfo.contextPath + "/sm/SelectInfoAction.do?step=selectEntry&checkType=" + iCheckMode;
		definedWin.openListingUrl(empInfoPre, url);
	}
	
		/********************************************�½�����ѡ����********************************************/
	//  ����empInfoPre˵����Ҫ���õ�Ա���Ż�Ա������ID����ֵ��λ��"."��ǰ�벿�ֵ����ƣ�
	//                 ���磬��empinfo.empCd��empinfo.empName��ȡ"empinfo"����
	//  ����definedWin˵�����Զ��崰�ڶ���
	//  ����iCheckMode˵����"radio"����ѡ��"checkbox"����ѡ�����Ϊ"checkbox"ʱ������Ҫ�ڵ��øú�����
	//                  ҳ����ʵ�ֻص�����definedWin.setListObjectInFor = function(arr,attribute,arrWithAtt)
	//                  ��ѡʱ�������ο���qware�����е�web\jsp\hr\edu\trainTeamInfoInfo.jspҳ��
	//  �����ο���selectEmp('empinfo', definedWin, 'radio')
	/*****************************************************************************************************/
		selectTcr = function(empInfoPre, definedWin, iCheckMode){
		var url = ContextInfo.contextPath + "/tr/web/TrSearchAction.do?step=selectEntry&checkType=" + iCheckMode;
		definedWin.openListingUrl(empInfoPre, url);
	}
	
	/********************************************������ɫѡ����********************************************/
	//  ����roleInfoPre˵����Ҫ���õĽ�ɫID���ɫ��������ֵ��λ��"."��ǰ�벿�ֵ����ƣ�
	//                 ���磬��roleinfo.id��roleinfo.name��ȡ"roleinfo"����
	//  ����definedWin˵�����Զ��崰�ڶ���
	//  �����ο���selectRole('roleinfo', definedWin)
	/*****************************************************************************************************/
	selectRole = function(roleInfoPre, definedWin){
		var url = ContextInfo.contextPath + "/sm/RoleAction.do?step=selectRoleEntry";
		definedWin.openListingUrl(roleInfoPre, url);
	}
		/********************************************���ݽ�ɫ�����û�ѡ����********************************************/
	//  ����role˵����	Ҫ����Ĳ�ѯ������Array���ͣ��ڴ��ɫID���û�ѡ�����н�ֻ��ʾ��role�н�ɫ��ص��û���
	//  ����ֵ˵����       Array���ͣ��ڴ��û�ID
	//  �����ο���		userIds = selectUser(roleIds)
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
	
			/********************************************���ݽ�ɫ�����ŵ����û�ѡ����********************************************/
	//  ����˵����	Ҫ����Ĳ�ѯ������roleΪArray���ͣ��ڴ��ɫID��deptΪ����ID,�û�ѡ�����н�ֻ��ʾ��role�н�ɫ��ؼ�����IDΪdept���û���
	//  ����ֵ˵����       Array���ͣ��ڴ��û�ID
	//  �����ο���		userIds = selectUser(roleIds,dept);
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
	
	/********************************************��������ѡ����********************************************/
	//  ����rateInfoPre˵����Ҫ���õĸ�λ�ڼ���ID����ֵ��λ��"."��ǰ�벿�ֵ����ƣ�
	//                 ���磬��rateInfo.station��rateInfo.stationGrade��ȡ"rateInfo"����
	//  ����definedWin˵�����Զ��崰�ڶ���
	//  ����iCheckMode˵����"radio"����ѡ��"checkbox"����ѡ��
	//  �����ο���selectRate('empinfo', definedWin, 'radio')
	/*****************************************************************************************************/
	selectRate = function(rateInfoPre, definedWin, iCheckMode){
		
		var url = ContextInfo.contextPath + "/hr/ratemanage.do?step=selectEntry&checkType=" + iCheckMode;
		
		definedWin.openListingUrl(rateInfoPre, url);
	}
	
	/********************************************�����������ѡ����********************************************/
	//����"iFlag"�������ƴӲ�ͬ��ԭҳ�浯��ʱ����Щradio���ã���Щradio�����á�
	//"λ�ù���-λ��"����ϸҳ�桢"�ʲ�����-�ʲ�"����ϸҳ�桢"״̬���-���"����ϸҳ��iFlag=0��"������Ŀ����-������Ŀ"����ϸҳ��iFlag=1��
	//����"arr"���������"�������ѡ��"����ҳ����ѡ���ĳ������URL
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
	/********************************************���������û��������********************************************/
	//  base��			���������û����Ϳ����ID����ֵ��λ��"."��ǰ�벿�ֵ����ƣ�
	//  �����ο���		
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
	/********************************************������ʾǩ�����б���********************************************/
	//  className��			����ʵ��������
	//  �����ο���		
	/*****************************************************************************************************/
	showSigners = function(className)
	{
		var iHeight="600";
		var iWidth="850";
		var sFeatures="dialogHeight: " + iHeight + "px;" + "dialogWidth: " + iWidth + "px;" + "help:no;" + "scroll:0;";
		var oid = document.getElementsByName("oid")[0].value;
		return window.showModalDialog(ContextInfo.contextPath + "/wo/ShowSignerAction.do?step=entry&oid="+oid+"&className="+className,"",sFeatures);
		
	}
	
	
