<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK"/>
<title>桌面</title>
<link rel="stylesheet" type="text/css" media="all"   href="../../css/style.css"/>
<link rel="stylesheet" type="text/css" media="all"   href="../../css/style_page.css" />
<link rel="stylesheet" type="text/css" media="all"   href="../../css/style_buttom.css" />
<link rel="stylesheet" type="text/css" media="print" href="../../css/print.css"/>

<script type="text/javascript" src = "../../public/scripts/contextInfo.js"></script>
<script type="text/javascript">
ContextInfo.contextPath = '<c:out value="${initParam['publicResourceServer']}"/>'
</script>
<script type="text/javascript" src = "../../public/scripts/global.js"></script>
<script type="text/javascript" src = "../../public/scripts/formUtils.js"></script>
<script type="text/javascript" src = "../../public/scripts/panel.js"></script>
</head>

<body>

<div class="list_group">	
	<div class="list_title">					
			帮助文档和相关软件下载		
		<span class="list_notes">点击下载连接将文档保存到您的本地机器，请您在本地打开查看</span>
	</div>
</div>
<div class="update_subhead" >
	 <span class="switch_open" onClick="StyleControl.switchDiv(this,submenu0)" title="点击收缩表格">系统帮助文档下载</span>
</div>
<table border="0" cellspacing="0" cellpadding="0" class="Update" id="submenu0">
     <tr>
        <td class="text" style="width:20%">注意</td>
        <td colspan="3"> 
			<ul>
				<li>正常访问MIS系统要求的软件环境：Microsoft Internet Explorer 6.0浏览器</li>
				<li>
				下载的文件后缀名是doc的是Word文档，您本地机器上装有软件 <a href="http://10.140.229.158/files/ruanjian/Office2003SP2.rar">MS Office</a> 或者 WPS 即可正常查看
				</li>
				<li>下载的文件后缀名是rar的是压缩文件，
				请使用软件 <a href="http://10.140.229.158/files/ruanjian/wrar351scfinal.exe">WinRAR</a> 解压缩得到文档。
				</li>				
			</ul>
		</td>
      </tr>
     <tr>
        <td class="text">物资系统 文档</td> 
        <td colspan="3"> 
			 <a href="/info/site/infoInformationEditableAction.do?step=download&amp;infoInformation.id=ff8080810d8cbb98010d8cbf64760005&amp;resourceName=material_userGuide.doc">material_userGuide.doc 下载</a>			 
			<!--| <a href="/docs/userGuide/material_userGuide.rar">RAR下载</a>-->
		</td>
      </tr>
	  <tr>
        <td class="text">缺陷系统 文档</td>
        <td colspan="3">
			<a href="#" onclick="javascript:this.href='/info/site/infoInformationEditableAction.do?step=download&amp;infoInformation.id=ff8080810d8cbb98010d8cc167970006&amp;resourceName=flaw_userGuide.rar'" shape="rect">flaw_userGuide.rar 下载</a> 
			<!--| <a href="/docs/userGuide/flaw_userGuide.rar">RAR下载</a> -->
		</td>
      </tr>
	  <tr>
        <td class="text">企业网站 文档</td>
        <td colspan="3">
			<a href="#" onclick="javascript:this.href='/info/site/infoInformationEditableAction.do?step=download&amp;infoInformation.id=ff8080810d8cbb98010d8cbd60030004&amp;resourceName=site_userGuide.doc'" shape="rect">site_userGuide.doc 下载</a> 
			<!--| <a href="javascript:location = '/docs/userGuide/site_userGuide.rar'">RAR下载</a>-->
		</td>
      </tr>
	  <tr>
        <td class="text">人事系统 文档</td>
        <td colspan="3">
			<a href="#" onclick="javascript:this.href='/info/site/infoInformationEditableAction.do?step=download&amp;infoInformation.id=ff8080810d8cbb98010d8ccb26f40007&amp;resourceName=hr_userGuide.doc'" shape="rect">hr_userGuide.doc 下载</a> 
			<!--| <a href="/docs/userGuide/hr_userGuide.rar">RAR下载</a>-->
		</td>
      </tr>
	  <tr>
        <td class="text">&nbsp;</td>
        <td colspan="3">
			<a href="/info/commons/site/desktop_news.jsp?colsId=ff8080810d8cbb98010d8cbb987e0000" target="_blank">查看更多文档</a>
		</td>
      </tr>
</table>

<div class="update_subhead" >
	 <span class="switch_open" onClick="StyleControl.switchDiv(this,submenu1)" title="点击收缩表格">相关软件下载</span>
</div>
<table border="0" cellspacing="0" cellpadding="0" class="Update" id="submenu1">
 <tr>
	<td class="text" style="width:20%">解压缩软件 WinRAR</td>
	<td colspan="3" > 
		<a href="http://10.140.229.158/files/ruanjian/wrar351scfinal.exe">wrar351scfinal.exe 下载</a>
	</td>
  </tr>
  <tr>
	<td class="text">MS Office 软件</td>
	<td colspan="3">
		<a href="http://10.140.229.158/files/ruanjian/Office2003SP2.rar">Office2003SP2.rar 下载</a>
	</td>
  </tr>
  <tr>
	<td class="text">其他软件</td>
	<td colspan="3">
		<a href="http://10.140.229.158" target="_blank">访问信息中心网站</a>
	</td>
  </tr>
</table>
</body>
</html>
