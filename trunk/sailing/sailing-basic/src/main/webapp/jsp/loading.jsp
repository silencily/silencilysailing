
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN">
<head>
<title> - ϵͳ������ -   - </title>
<meta http-equiv="Content-Type" content="text/html; charset=GBK"/>
<link rel="stylesheet" type="text/css" media="all" href="../css/style.css" />
<link rel="stylesheet" type="text/css" media="all" href="../css/style_page.css" />
</head>
<body>

<table width="400" height="150" border="0" cellpadding="0" cellspacing="0" style="cursor:wait" background="../image/main/loading_logo.gif">
	<tr>
		<td height="70" valign="top" align="right" style="padding:5px">
			<img src="../image/main/loading_stop.gif" onclick="setClose()" alt="�رս�����" style="cursor:pointer">&nbsp;</td>
	</tr>
	<tr >
		<td style="padding-right:30px;padding-top:2px" align=right>
			<div ><img src="../image/main/loading_blank.gif" height="20" width="350" name="imgId_Loading" alt="���ڼ�������......"></div>
		</td>
	</tr><tr>
		<td style="padding-left:24px;height:26px">
			<div id="txtId_Loading">���Ժ����ڼ��� ......</div>
		</td>		
	</tr>
	<tr>
		<td height="*">&nbsp;</td>
	</tr>
</table>

<script type="text/javascript">
var temp_clock;
var initSize = 350;//��ʼ����
var temp_imgLen = 150;
function setBeginLoading(loadSize,sec){
	//self.focus();
	var sec = sec ? sec : 0.020;
	var txt = document.getElementById("txtId_Loading");
	var img = document.getElementById("imgId_Loading");
	if(loadSize)
		temp_imgLen = loadSize;
	temp_imgLen -= (initSize - temp_imgLen)*sec;
	img.width = temp_imgLen;
	txt.innerHTML = "���Ժ����ڼ��� ......";
	txt.className = "";
	if(temp_imgLen <= 2){		
		setClose();
		return;
	}else if(temp_imgLen <= 1){
		txt.innerHTML = "���س�ʱ�������ԣ�";
		txt.className = "font_error";
	}
	temp_clock = setTimeout("setBeginLoading('',"+sec+")",200);//10'
}
function setClose(){	
	parent.definedWin.closeLoading();
}

//�ṩ����ҳ����ȡ����ʱ
function clearTime(){
	var img  = document.getElementById("imgId_Loading");
	var txt = document.getElementById("txtId_Loading");
	img.width = 350;
	txt.innerHTML = "���Ժ����ڼ��� ......";
	txt.className = "";
	temp_imgLen = 150;
	clearTimeout(temp_clock);
}
</script>

</body>
</html>