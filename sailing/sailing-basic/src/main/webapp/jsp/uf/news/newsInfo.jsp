<%--
    @version:$Id: newsInfo.jsp,v 1.1 2010/12/10 10:56:43 silencily Exp $
    @since $Date: 2010/12/10 10:56:43 $
    @组织的详细页面
--%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>
<html>
<head>
<LINK id=css href="../../jsp/uf/news/texteditor/style_13.css" type=text/css rel=stylesheet>

</head>
<body class="main_body" >
<form name="f" action="" method="post"  enctype="multipart/form-data">
<input type="hidden" name="oid" value="<c:out value='${theForm.bean.id}'/>"/>
<input type="hidden" name="step" value="save"/>
<!-- 信息发布用 Start -->
<SCRIPT src="../../jsp/uf/news/texteditor/post.js" type=text/javascript></SCRIPT>
<SCRIPT src="../../jsp/uf/news/texteditor/common.js" type=text/javascript></SCRIPT>
<SCRIPT src="../../jsp/uf/news/texteditor/ajax.js" type=text/javascript></SCRIPT>
<SCRIPT src="../../jsp/uf/news/texteditor/bbcode.js" type=text/javascript></SCRIPT>
<SCRIPT src="../../jsp/uf/news/texteditor/editor.js" type=text/javascript></SCRIPT>
<SCRIPT src="../../jsp/uf/news/texteditor/menu.js" type=text/javascript></SCRIPT>
<SCRIPT type=text/javascript>
	var postminchars = parseInt('1');
	var postmaxchars = parseInt('20000');
	var disablepostctrl = parseInt('0');
	var bbinsert = parseInt('1');
	var seccodecheck = parseInt('0');
	var secqaacheck = parseInt('');
	function checklength(theform) {
		var message = bbinsert && wysiwyg ? html2bbcode(getEditorContents()) : (!theform.parseurloff.checked ? parseurl(document.getElementsByName("bean.content")[0].value) : document.getElementsByName("bean.content")[0].value);
		var showmessage = postmaxchars != 0 ? '系统限制: ' + postminchars + ' 到 ' + postmaxchars + ' 字节' : '';
		alert('\n当前长度: ' + mb_strlen(message) + ' 字节\n\n' + showmessage);
	}
	function validate(theform, previewpost) {
		var message = bbinsert && wysiwyg ? html2bbcode(getEditorContents()) : (!theform.parseurloff.checked ? parseurl(document.getElementsByName("bean.content")[0].value) : document.getElementsByName("bean.content")[0].value);
		if(message == "" && document.getElementsByName("bean.title")[0].value == "") {
			alert("请完成标题或内容栏。");
			document.getElementsByName("bean.content")[0].focus();
			return false;
		} else if(mb_strlen(document.getElementsByName("bean.title")[0].value) > 80) {
			alert("您的标题超过 80 个字符的限制。");
			document.getElementsByName("bean.title")[0].focus();
			return false;
		}
		if(!disablepostctrl && ((postminchars != 0 && mb_strlen(message) < postminchars) || (postmaxchars != 0 && mb_strlen(message) > postmaxchars))) {
			alert("您的帖子长度不符合要求。\n\n当前长度: "+mb_strlen(message)+" 字节\n系统限制: "+postminchars+" 到 "+postmaxchars+" 字节");
			return false;
		}
		document.getElementsByName("bean.content")[0].value = message;
		seccheck(theform, seccodecheck, secqaacheck, previewpost);
		if(previewpost) return true;
	}
</SCRIPT>

<div class="update_subhead">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td>
			<span class="switch_open" onClick="StyleControl.switchDiv(this, divId_scrollLing)"  title="点击收缩表格">发布信息详细</span>
		</td>
		<td align="right">
	    </td>
	</tr>
</table>
</div>
<div id="divId_scrollLing" class="list_scroll">
<table width="100%" border="0" class="NewsDetail" id="listtable">
	<tr>
		<td colspan="4">
		<input name="" type="button" value="预 览" onclick='openPreview();'/>
		</td>
	</tr>
  <tr>
    <td class="attribute">标题</td>
    <td>
		<vision:text
			name="bean.title"
			value="${theForm.bean.title}"
			comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagDefautPolicy"
			maxlength="50"
			bisname="标题"
			id="bean.title"
			required='true'
		/>
		<input type="hidden" name="resourceUrl" value="<c:out value='${theForm.resourceUrl}'/>">
		<input type="hidden" name="preview.publisherId" value="<c:out value='${theForm.bean.publisherId}'/>">
		<input type="hidden" name="preview.publishTime" value="<fmt:formatDate value="${theForm.bean.publishTime}" pattern="yyyy-MM-dd HH:mm"/>">
    </td>
    <td class="attribute">作者</td>
    <td>
		<vision:text
			name="bean.author"
			value="${theForm.bean.author}"
			comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagDefautPolicy"
			maxlength="50"
			bisname="作者"
			id="bean.author"
		/>
      	<input type=hidden name="bean.publishTime" value="<c:out value="${theForm.bean.publishTime}"/>"/>
      	<input type=hidden name="bean.sendAllFlg" value="<c:out value="${theForm.bean.sendAllFlg}"/>"/>
      </td>
  </tr>
  <tr>
    <td class="attribute">所属栏目</td>
    <td>
		<vision:choose
			value="${theForm.bean.tblUfColumn.id}" 
			textName="temp.bean.tblUfColumn.id"
			valueName="tblUfColumnid"
			source='${theForm.columnList}'
			comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagDefautPolicy"
			onchange="CurrentPage.chgColumn();"
			required='true'
		/>
    </td>
    <td class="attribute">来源</td>
    <td>
		<vision:text
			name="bean.source"
			value="${theForm.bean.source}"
			comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagDefautPolicy"
			maxlength="100"
			bisname="来源"
			id="bean.source"
		/>
    </td>
  </tr>
  <tr>
    <td class="attribute">关键字</td>
    <td>
		<vision:text
			name="bean.keyword"
			value="${theForm.bean.keyword}"
			comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagDefautPolicy"
			maxlength="40"
			bisname="关键字"
			id="bean.keyword"
		/>
    </td>
    <td class="attribute">有效期</td>
    <td>
		<vision:text
			name="bean.invalidTime"
			value="${theForm.bean.invalidTime}"
			comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagDefautPolicy"
			dataType="2"
			id="bean.invalidTime"
			required='true'
		/>
      </td>
  </tr>
  <tr>
    <td class="attribute">推荐</td>
    <td>
		<input type="checkbox" name="chk.recommend" value="1" <c:if test="${theForm.bean.recommend=='1'}">checked</c:if>
		       onclick="doCheck(this,document.getElementById('bean.recommend.code'))">
		<input type="hidden" name="bean.recommend.code" id="bean.recommend.code" value="<c:out value='${theForm.bean.recommend}'/>" >
    </td>
    <td class="attribute">发布</td>
    <td>
		<input type="checkbox" name="chk.published" value="1" <c:if test="${theForm.bean.published=='1'}">checked</c:if>
		       onclick="doCheck(this,document.getElementById('bean.published.code'))">
		<input type="hidden" name="bean.published.code" id="bean.published.code" value="<c:out value='${theForm.bean.published}'/>" >
    </td>
  </tr>
  <tr>
    <td align="right" valign="top" class="attribute">内容</td>
    <td colspan="3">
      <DIV id=posteditor>
      <LINK href="../../jsp/uf/news/texteditor/style_13_editor.css" type=text/css rel=stylesheet>
      <SCRIPT type=text/javascript language=javascript>
		var editorid = 'posteditor';
		var wysiwyg = (is_ie || is_moz || (is_opera >= 9)) && parseInt('1') && bbinsert == 1 ? 1 : 0;
		var allowswitcheditor = parseInt('1');
		var allowhtml = parseInt('0');
		var forumallowhtml = parseInt('0');
		var allowsmilies = parseInt('1');
		var allowbbcode = parseInt('1');
		var allowimgcode = parseInt('1');
		var smilies = new Array();
		smilies[1]			= {'code' : ':)', 'url' : 'smile.gif'};
		smilies[2]			= {'code' : ':(', 'url' : 'sad.gif'};
		smilies[3]			= {'code' : ':D', 'url' : 'biggrin.gif'};
		smilies[4]			= {'code' : ':\')', 'url' : 'cry.gif'};
		smilies[5]			= {'code' : ':N', 'url' : 'huffy.gif'};
		smilies[6]			= {'code' : ':o', 'url' : 'shocked.gif'};
		smilies[7]			= {'code' : ':P', 'url' : 'tongue.gif'};
		smilies[8]			= {'code' : ':$', 'url' : 'shy.gif'};
		smilies[9]			= {'code' : ';P', 'url' : 'titter.gif'};
		smilies[10]			= {'code' : ':L', 'url' : 'sweat.gif'};
		smilies[11]			= {'code' : ':M', 'url' : 'mad.gif'};
		smilies[12]			= {'code' : ':lol', 'url' : 'lol.gif'};
		smilies[13]			= {'code' : ':hug:', 'url' : 'hug.gif'};
		smilies[14]			= {'code' : ':victory:', 'url' : 'victory.gif'};
		smilies[15]			= {'code' : ':time:', 'url' : 'time.gif'};
		smilies[16]			= {'code' : ':kiss:', 'url' : 'kiss.gif'};
		smilies[17]			= {'code' : ':handshake', 'url' : 'handshake.gif'};
		smilies[18]			= {'code' : ':call:', 'url' : 'call.gif'};
		smilies[28]			= {'code' : ':hao:', 'url' : 'hao.gif'};
		smilies[29]			= {'code' : ':kun:', 'url' : 'kun.gif'};
		smilies[30]			= {'code' : ':why:', 'url' : 'why.gif'};
		smilies[31]			= {'code' : ':hua:', 'url' : 'rose.gif'};
		smilies[45]			= {'code' : ':qy:', 'url' : 'qy.gif'};
		smilies[34]			= {'code' : ':ting:', 'url' : 'ting.gif'};
		smilies[35]			= {'code' : ':tea:', 'url' : 'tea.gif'};
		smilies[36]			= {'code' : ':yun:', 'url' : 'yun.gif'};
		smilies[37]			= {'code' : ':cb:', 'url' : 'cb.gif'};
		smilies[38]			= {'code' : ':mei:', 'url' : 'mei.gif'};
		smilies[40]			= {'code' : ':bao:', 'url' : 'bao.gif'};
		smilies[44]			= {'code' : ':sk:', 'url' : 'sk.gif'};
		smilies[47]			= {'code' : ':loveliness:', 'url' : 'loveliness.gif'};
		smilies[48]			= {'code' : ':funk:', 'url' : 'funk.gif'};
		smilies[49]			= {'code' : ':028:', 'url' : '028.gif'};
		smilies[50]			= {'code' : ':02:', 'url' : '02.gif'};
		smilies[51]			= {'code' : ':04:', 'url' : '04.gif'};
		smilies[52]			= {'code' : ':05:', 'url' : '05.gif'};
		smilies[53]			= {'code' : ':06:', 'url' : '06.gif'};
		smilies[54]			= {'code' : ':07:', 'url' : '07.gif'};
		smilies[55]			= {'code' : ':08:', 'url' : '08.gif'};
		smilies[56]			= {'code' : ':09:', 'url' : '09.gif'};
		smilies[57]			= {'code' : ':010:', 'url' : '010.gif'};
		smilies[58]			= {'code' : ':011:', 'url' : '011.gif'};
		smilies[59]			= {'code' : ':013:', 'url' : '013.gif'};
		smilies[60]			= {'code' : ':014:', 'url' : '014.gif'};
		smilies[61]			= {'code' : ':015:', 'url' : '015.gif'};
		smilies[62]			= {'code' : ':030:', 'url' : '030.gif'};
		smilies[63]			= {'code' : ':022:', 'url' : '022.gif'};
		smilies[64]			= {'code' : ':023:', 'url' : '023.gif'};
		smilies[65]			= {'code' : ':024:', 'url' : '024.gif'};
		smilies[66]			= {'code' : ':025:', 'url' : '025.gif'};
		smilies[67]			= {'code' : ':029:', 'url' : '029.gif'};
		smilies[68]			= {'code' : ':032:', 'url' : '032.gif'};
		smilies[69]			= {'code' : ':033:', 'url' : '033.gif'};
		smilies[70]			= {'code' : ':034:', 'url' : '034.gif'};
		smilies[71]			= {'code' : ':035:', 'url' : '035.gif'};
		smilies[72]			= {'code' : ':036:', 'url' : '036.gif'};
		smilies[73]			= {'code' : ':037:', 'url' : '037.gif'};
		smilies[74]			= {'code' : ':038:', 'url' : '038.gif'};
		smilies[75]			= {'code' : ':039:', 'url' : '039.gif'};
		smilies[76]			= {'code' : ':040:', 'url' : '040.gif'};
		smilies[77]			= {'code' : ':041:', 'url' : '041.gif'};
		smilies[78]			= {'code' : ':044:', 'url' : '044.gif'};
		smilies[79]			= {'code' : ':045:', 'url' : '045.gif'};
		smilies[80]			= {'code' : ':047:', 'url' : '047.gif'};
		smilies[81]			= {'code' : ':048:', 'url' : '048.gif'};
		smilies[82]			= {'code' : ':049:', 'url' : '049.gif'};
		smilies[83]			= {'code' : ':050:', 'url' : '050.gif'};
		smilies[84]			= {'code' : ':051:', 'url' : '051.gif'};
		smilies[85]			= {'code' : ':052:', 'url' : '052.gif'};
		var BORDERCOLOR = "#A4B6D7";
		var ALTBG2 = "#F9F9F9";
		var charset = 'gbk';
		
		function previewpost(){
			if(!validate($('f'), true)) {
				$('bean.title').focus();
				return;
			}
			$("previewmessage").innerHTML = '<span class="bold"><span class="smalltxt">' + $('bean.title').value + '</span></span><br><br><span style="font-size: 12px">' + bbcode2html($('bean.conten').value) + '</span>';
			$("previewtable").style.display = '';
			window.scroll(0, 0);
		}
		
		function clearcontent() {
			if(wysiwyg && bbinsert) {
				editdoc.body.innerHTML = is_moz ? '<br />' : '';
			} else {
				textobj.value = '';
			}
		}
		
		function resizeEditor(change) {
			var editorbox = bbinsert ? editbox : textobj;
			var newheight = parseInt(editorbox.style.height, 10) + change;
			if(newheight >= 100) {
				editorbox.style.height = newheight + 'px';
			}
		}
		var lang = new Array();
		if(is_ie >= 5 || is_moz >= 2) {
			window.onbeforeunload = function () {saveData(wysiwyg && bbinsert ? editdoc.body.innerHTML : textobj.value)};
			lang['post_autosave_none'] = "没有可以恢复的数据！";
			lang['post_autosave_confirm'] = "此操作将覆盖当前帖子内容，确定要恢复数据吗？";
		}
	  
      
        lang['enter_list_item']			= "输入一个列表项目.\r\n留空或者点击取消完成此列表.";
		lang['enter_link_url']			= "请输入链接的地址:";
		lang['enter_image_url']			= "请输入图片链接地址:";
		lang['enter_email_link']		= "请输入此链接的邮箱地址:";
		lang['fontname']			= "字体";
		lang['fontsize']			= "大小";
		var custombbcodes = new Array();
		custombbcodes["rm"] = {'example' : '[rm]rtsp://your.com/example.rm[/rm]', 'prompt' : '请输入 Real 音频或视频的 URL:'};
		custombbcodes["flash"] = {'example' : 'Flash Movie', 'prompt' : '请输入 Flash 动画的 URL:'};
		custombbcodes["qq"] = {'example' : '[qq]688888[/qq]', 'prompt' : '请输入显示在线状态 QQ 号码:'};
		var fontoptions = new Array("仿宋_GB2312", "黑体", "楷体_GB2312", "宋体", "新宋体", "Tahoma", "Arial", "Impact", "Verdana", "Times New Roman");
	  </SCRIPT>

      <TABLE class=editor cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
        <TR>
          <TD id=posteditor_controls colSpan=2>
            <TABLE cellSpacing=0 cellPadding=0 border=0>
              <TBODY>
              <TR>
                <TD>
                  <DIV class=editor_buttonnormal id=posteditor_cmd_removeformat 
                  onmouseover="buttonContext(this, 'mouseover')" 
                  onclick="discuzcode('removeformat')" 
                  onmouseout="buttonContext(this, 'mouseout')"><IMG title=清除文本格式 
                  height=20 alt=清除文本格式 
                  src="../../jsp/uf/news/texteditor/bb_removeformat.gif" 
                  width=21></DIV></TD>
                <TD><IMG height=20 alt="" 
                  src="../../jsp/uf/news/texteditor/bb_separator.gif" 
                  width=6></TD>
                <TD>
                  <DIV class=editor_buttonnormal id=posteditor_cmd_bold 
                  onmouseover="buttonContext(this, 'mouseover')" 
                  onclick="discuzcode('bold')" 
                  onmouseout="buttonContext(this, 'mouseout')"><IMG title=粗体 
                  height=20 alt=粗体 
                  src="../../jsp/uf/news/texteditor/bb_bold.gif" 
                  width=21></DIV></TD>
                <TD>
                  <DIV class=editor_buttonnormal id=posteditor_cmd_italic 
                  onmouseover="buttonContext(this, 'mouseover')" 
                  onclick="discuzcode('italic')" 
                  onmouseout="buttonContext(this, 'mouseout')"><IMG title=斜体 
                  height=20 alt=斜体 
                  src="../../jsp/uf/news/texteditor/bb_italic.gif" 
                  width=21></DIV></TD>
                <TD>
                  <DIV class=editor_buttonnormal id=posteditor_cmd_underline 
                  onmouseover="buttonContext(this, 'mouseover')" 
                  onclick="discuzcode('underline')" 
                  onmouseout="buttonContext(this, 'mouseout')"><IMG title=下划线 
                  height=20 alt=下划线 
                  src="../../jsp/uf/news/texteditor/bb_underline.gif" 
                  width=21></DIV></TD>
                <TD><IMG height=20 alt="" 
                  src="../../jsp/uf/news/texteditor/bb_separator.gif" 
                  width=6></TD>
                <TD id=posteditor_popup_fontname title=字体 
                onclick=editorMenu(this)>
                  <DIV class=editor_buttonnormal 
                  onmouseover="menuContext(this, 'mouseover')" 
                  onmouseout="menuContext(this, 'mouseout')">
                  <TABLE cellSpacing=0 cellPadding=0 border=0 
                    unselectable="on"><TBODY>
                    <TR>
                      <TD class=editor_menunormal id=posteditor_menu_fontname 
                      unselectable="on">
                        <DIV id=posteditor_font_out style="WIDTH: 91px" 
                        unselectable="on">字体</DIV></TD>
                      <TD unselectable="on"><IMG height=4 alt="" 
                        src="../../jsp/uf/news/texteditor/bb_menupop.gif" 
                        width=7></TD></TR></TBODY></TABLE></DIV></TD>
                <TD id=posteditor_popup_fontsize title=大小 
                onclick=editorMenu(this)>
                  <DIV class=editor_buttonnormal 
                  onmouseover="menuContext(this, 'mouseover')" 
                  onmouseout="menuContext(this, 'mouseout')">
                  <TABLE cellSpacing=0 cellPadding=0 border=0 
                    unselectable="on"><TBODY>
                    <TR>
                      <TD class=editor_menunormal id=posteditor_menu_fontsize 
                      unselectable="on">
                        <DIV id=posteditor_size_out style="WIDTH: 25px" 
                        unselectable="on">大小</DIV></TD>
                      <TD unselectable="on"><IMG height=4 alt="" 
                        src="../../jsp/uf/news/texteditor/bb_menupop.gif" 
                        width=7></TD></TR></TBODY></TABLE></DIV></TD>
                <TD id=posteditor_popup_forecolor title=颜色 
                onclick=editorMenu(this)>
                  <DIV class=editor_buttonnormal 
                  onmouseover="menuContext(this, 'mouseover')" 
                  onmouseout="menuContext(this, 'mouseout')">
                  <TABLE cellSpacing=0 cellPadding=0 border=0 
                    unselectable="on"><TBODY>
                    <TR>
                      <TD class=editor_colormenunormal id=posteditor_colormenu 
                      unselectable="on"><IMG height=16 alt="" 
                        src="../../jsp/uf/news/texteditor/bb_color.gif" 
                        width=21><BR><IMG id=posteditor_color_bar 
                        style="BACKGROUND-COLOR: black" height=4 alt="" 
                        src="../../jsp/uf/news/texteditor/bb_clear.gif" 
                        width=21></TD>
                      <TD unselectable="on"><IMG height=4 alt="" 
                        src="../../jsp/uf/news/texteditor/bb_menupop.gif" 
                        width=7></TD></TR></TBODY></TABLE></DIV></TD>
                <TD><IMG height=20 alt="" 
                  src="../../jsp/uf/news/texteditor/bb_separator.gif" 
                  width=6></TD>
                <TD>
                  <DIV class=editor_buttonnormal id=posteditor_cmd_justifyleft 
                  onmouseover="buttonContext(this, 'mouseover')" 
                  onclick="discuzcode('justifyleft')" 
                  onmouseout="buttonContext(this, 'mouseout')"><IMG title=居左 
                  height=20 alt=居左 
                  src="../../jsp/uf/news/texteditor/bb_left.gif" 
                  width=21></DIV></TD>
                <TD>
                  <DIV class=editor_buttonnormal id=posteditor_cmd_justifycenter 
                  onmouseover="buttonContext(this, 'mouseover')" 
                  onclick="discuzcode('justifycenter')" 
                  onmouseout="buttonContext(this, 'mouseout')"><IMG title=居中 
                  height=20 alt=居中 
                  src="../../jsp/uf/news/texteditor/bb_center.gif" 
                  width=21></DIV></TD>
                <TD>
                  <DIV class=editor_buttonnormal id=posteditor_cmd_justifyright 
                  onmouseover="buttonContext(this, 'mouseover')" 
                  onclick="discuzcode('justifyright')" 
                  onmouseout="buttonContext(this, 'mouseout')"><IMG title=居右 
                  height=20 alt=居右 
                  src="../../jsp/uf/news/texteditor/bb_right.gif" 
                  width=21></DIV></TD>
                <TD>
                  <DIV class=editor_buttonnormal id=posteditor_cmd_floatleft 
                  onmouseover="buttonContext(this, 'mouseover')" 
                  onclick="discuzcode('floatleft')" 
                  onmouseout="buttonContext(this, 'mouseout')"><IMG title=左浮动 
                  height=20 alt=左浮动 
                  src="../../jsp/uf/news/texteditor/bb_floatleft.gif" 
                  width=21></DIV></TD>
                <TD>
                  <DIV class=editor_buttonnormal id=posteditor_cmd_floatright 
                  onmouseover="buttonContext(this, 'mouseover')" 
                  onclick="discuzcode('floatright')" 
                  onmouseout="buttonContext(this, 'mouseout')"><IMG title=右浮动 
                  height=20 alt=右浮动 
                  src="../../jsp/uf/news/texteditor/bb_floatright.gif" 
                  width=21></DIV></TD>
                <TD><IMG height=20 alt="" 
                  src="../../jsp/uf/news/texteditor/bb_separator.gif" 
                  width=6></TD>
                <TD>
                  <DIV class=editor_buttonnormal 
                  id=posteditor_cmd_insertorderedlist 
                  onmouseover="buttonContext(this, 'mouseover')" 
                  onclick="discuzcode('insertorderedlist')" 
                  onmouseout="buttonContext(this, 'mouseout')"><IMG title=排序的列表 
                  height=20 alt=排序的列表 
                  src="../../jsp/uf/news/texteditor/bb_orderedlist.gif" 
                  width=21></DIV></TD>
                <TD>
                  <DIV class=editor_buttonnormal 
                  id=posteditor_cmd_insertunorderedlist 
                  onmouseover="buttonContext(this, 'mouseover')" 
                  onclick="discuzcode('insertunorderedlist')" 
                  onmouseout="buttonContext(this, 'mouseout')"><IMG title=未排序列表 
                  height=20 alt=未排序列表 
                  src="../../jsp/uf/news/texteditor/bb_unorderedlist.gif" 
                  width=21></DIV></TD>
                <TD>
                  <DIV class=editor_buttonnormal id=posteditor_cmd_outdent 
                  onmouseover="buttonContext(this, 'mouseover')" 
                  onclick="discuzcode('outdent')" 
                  onmouseout="buttonContext(this, 'mouseout')"><IMG title=减少缩进 
                  height=20 alt=减少缩进 
                  src="../../jsp/uf/news/texteditor/bb_outdent.gif" 
                  width=21></DIV></TD>
                <TD>
                  <DIV class=editor_buttonnormal id=posteditor_cmd_indent 
                  onmouseover="buttonContext(this, 'mouseover')" 
                  onclick="discuzcode('indent')" 
                  onmouseout="buttonContext(this, 'mouseout')"><IMG title=增加缩进 
                  height=20 alt=增加缩进 
                  src="../../jsp/uf/news/texteditor/bb_indent.gif" 
                  width=21></DIV></TD>
                <TD><IMG height=20 alt="" 
                  src="../../jsp/uf/news/texteditor/bb_separator.gif" 
                  width=6></TD>
                <TD>
                <TD>
                  <DIV class=editor_buttonnormal id=posteditor_cmd_createlink 
                  onmouseover="buttonContext(this, 'mouseover')" 
                  onclick="discuzcode('createlink')" 
                  onmouseout="buttonContext(this, 'mouseout')"><IMG title=插入链接 
                  height=20 alt=插入链接 
                  src="../../jsp/uf/news/texteditor/bb_url.gif" 
                  width=21></DIV></TD>
                <TD>
                  <DIV class=editor_buttonnormal id=posteditor_cmd_unlink 
                  onmouseover="buttonContext(this, 'mouseover')" 
                  onclick="discuzcode('unlink')" 
                  onmouseout="buttonContext(this, 'mouseout')"><IMG title=移除链接 
                  height=20 alt=移除链接 
                  src="../../jsp/uf/news/texteditor/bb_unlink.gif" 
                  width=21></DIV></TD>
                <TD>
                  <DIV class=editor_buttonnormal id=posteditor_cmd_email 
                  onmouseover="buttonContext(this, 'mouseover')" 
                  onclick="discuzcode('email')" 
                  onmouseout="buttonContext(this, 'mouseout')"><IMG title=插入邮箱链接 
                  height=20 alt=插入邮箱链接 
                  src="../../jsp/uf/news/texteditor/bb_email.gif" 
                  width=21></DIV></TD>
                <TD>
                  <DIV class=editor_buttonnormal id=posteditor_cmd_insertimage 
                  onmouseover="buttonContext(this, 'mouseover')" 
                  onclick="discuzcode('insertimage')" 
                  onmouseout="buttonContext(this, 'mouseout')"><IMG title=插入图片 
                  height=20 alt=插入图片 
                  src="../../jsp/uf/news/texteditor/bb_image.gif" 
                  width=21></DIV></TD>
                  <TD>
                  <DIV class=editor_buttonnormal id=posteditor_popup_table 
                  onmouseover="buttonContext(this, 'mouseover')" 
                  onclick=editorMenu(this) 
                  onmouseout="buttonContext(this, 'mouseout')"><IMG title=插入表格 
                  height=20 alt=插入表格 
                  src="../../jsp/uf/news/texteditor/bb_table.gif" 
                  width=21></DIV></TD>
                  </TR></TBODY></TABLE>
            <DIV id=posteditor_switcher>
            <INPUT class=editor_switcher_highlight id=bbcodemode onclick=switchEditor(0) type=button value="代码模式"> 
			<INPUT class=editor_switcher id=wysiwygmode onclick=switchEditor(1) type=button value=所见即所得模式> 
            </DIV>
            <DIV class=popupmenu_popup id=posteditor_popup_fontname_menu 
            style="DISPLAY: none">
            <TABLE cellSpacing=0 cellPadding=4 border=0 unselectable="on">
              <TBODY>
              <TR>
                <TD class=popupmenu_option 
                onclick="discuzcode('fontname', '仿宋_GB2312')" 
                  unselectable="on"><FONT face=仿宋_GB2312 
                  unselectable="on">仿宋_GB2312</FONT></TD></TR>
              <TR>
                <TD class=popupmenu_option 
                onclick="discuzcode('fontname', '黑体')" unselectable="on"><FONT 
                  face=黑体 unselectable="on">黑体</FONT></TD></TR>
              <TR>
                <TD class=popupmenu_option 
                onclick="discuzcode('fontname', '楷体_GB2312')" 
                  unselectable="on"><FONT face=楷体_GB2312 
                  unselectable="on">楷体_GB2312</FONT></TD></TR>
              <TR>
                <TD class=popupmenu_option 
                onclick="discuzcode('fontname', '宋体')" unselectable="on"><FONT 
                  face=宋体 unselectable="on">宋体</FONT></TD></TR>
              <TR>
                <TD class=popupmenu_option 
                onclick="discuzcode('fontname', '新宋体')" 
                  unselectable="on"><FONT face=新宋体 
                unselectable="on">新宋体</FONT></TD></TR>
              <TR>
                <TD class=popupmenu_option 
                onclick="discuzcode('fontname', 'Tahoma')" 
                  unselectable="on"><FONT face=Tahoma 
                  unselectable="on">Tahoma</FONT></TD></TR>
              <TR>
                <TD class=popupmenu_option 
                onclick="discuzcode('fontname', 'Arial')" 
                  unselectable="on"><FONT face=Arial 
                  unselectable="on">Arial</FONT></TD></TR>
              <TR>
                <TD class=popupmenu_option 
                onclick="discuzcode('fontname', 'Impact')" 
                  unselectable="on"><FONT face=Impact 
                  unselectable="on">Impact</FONT></TD></TR>
              <TR>
                <TD class=popupmenu_option 
                onclick="discuzcode('fontname', 'Verdana')" 
                  unselectable="on"><FONT face=Verdana 
                  unselectable="on">Verdana</FONT></TD></TR>
              <TR>
                <TD class=popupmenu_option 
                onclick="discuzcode('fontname', 'Times New Roman')" 
                unselectable="on"><FONT face="Times New Roman" 
                  unselectable="on">Times New 
            Roman</FONT></TD></TR></TBODY></TABLE></DIV>
            <DIV class=popupmenu_popup id=posteditor_popup_fontsize_menu 
            style="DISPLAY: none">
            <TABLE cellSpacing=0 cellPadding=4 border=0 unselectable="on">
              <TBODY>
              <TR align=middle>
                <TD class=popupmenu_option onclick="discuzcode('fontsize', 1)" 
                unselectable="on"><FONT size=1 
unselectable="on">1</FONT></TD></TR>
              <TR align=middle>
                <TD class=popupmenu_option onclick="discuzcode('fontsize', 2)" 
                unselectable="on"><FONT size=2 
unselectable="on">2</FONT></TD></TR>
              <TR align=middle>
                <TD class=popupmenu_option onclick="discuzcode('fontsize', 3)" 
                unselectable="on"><FONT size=3 
unselectable="on">3</FONT></TD></TR>
              <TR align=middle>
                <TD class=popupmenu_option onclick="discuzcode('fontsize', 4)" 
                unselectable="on"><FONT size=4 
unselectable="on">4</FONT></TD></TR>
              <TR align=middle>
                <TD class=popupmenu_option onclick="discuzcode('fontsize', 5)" 
                unselectable="on"><FONT size=5 
unselectable="on">5</FONT></TD></TR>
              <TR align=middle>
                <TD class=popupmenu_option onclick="discuzcode('fontsize', 6)" 
                unselectable="on"><FONT size=6 
unselectable="on">6</FONT></TD></TR>
              <TR align=middle>
                <TD class=popupmenu_option onclick="discuzcode('fontsize', 7)" 
                unselectable="on"><FONT size=7 
              unselectable="on">7</FONT></TD></TR></TBODY></TABLE></DIV>
            <DIV class=popupmenu_popup id=posteditor_popup_forecolor_menu 
            style="DISPLAY: none">
            <TABLE cellSpacing=0 cellPadding=4 border=0 unselectable="on">
              <TBODY>
              <TR>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'Black')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: black" 
                unselectable="on"></DIV></TD>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'Sienna')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: sienna" 
                unselectable="on"></DIV></TD>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'DarkOliveGreen')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: darkolivegreen" 
                  unselectable="on"></DIV></TD>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'DarkGreen')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: darkgreen" 
                  unselectable="on"></DIV></TD>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'DarkSlateBlue')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: darkslateblue" 
                  unselectable="on"></DIV></TD>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'Navy')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: navy" 
unselectable="on"></DIV></TD>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'Indigo')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: indigo" 
                unselectable="on"></DIV></TD>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'DarkSlateGray')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: darkslategray" 
                  unselectable="on"></DIV></TD></TR>
              <TR>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'DarkRed')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: darkred" 
                unselectable="on"></DIV></TD>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'DarkOrange')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: darkorange" 
                  unselectable="on"></DIV></TD>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'Olive')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: olive" 
                unselectable="on"></DIV></TD>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'Green')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: green" 
                unselectable="on"></DIV></TD>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'Teal')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: teal" 
unselectable="on"></DIV></TD>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'Blue')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: blue" 
unselectable="on"></DIV></TD>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'SlateGray')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: slategray" 
                  unselectable="on"></DIV></TD>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'DimGray')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: dimgray" 
                unselectable="on"></DIV></TD></TR>
              <TR>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'Red')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: red" unselectable="on"></DIV></TD>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'SandyBrown')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: sandybrown" 
                  unselectable="on"></DIV></TD>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'YellowGreen')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: yellowgreen" 
                  unselectable="on"></DIV></TD>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'SeaGreen')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: seagreen" 
                  unselectable="on"></DIV></TD>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'MediumTurquoise')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: mediumturquoise" 
                  unselectable="on"></DIV></TD>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'RoyalBlue')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: royalblue" 
                  unselectable="on"></DIV></TD>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'Purple')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: purple" 
                unselectable="on"></DIV></TD>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'Gray')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: gray" 
              unselectable="on"></DIV></TD></TR>
              <TR>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'Magenta')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: magenta" 
                unselectable="on"></DIV></TD>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'Orange')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: orange" 
                unselectable="on"></DIV></TD>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'Yellow')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: yellow" 
                unselectable="on"></DIV></TD>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'Lime')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: lime" 
unselectable="on"></DIV></TD>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'Cyan')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: cyan" 
unselectable="on"></DIV></TD>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'DeepSkyBlue')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: deepskyblue" 
                  unselectable="on"></DIV></TD>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'DarkOrchid')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: darkorchid" 
                  unselectable="on"></DIV></TD>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'Silver')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: silver" 
                unselectable="on"></DIV></TD></TR>
              <TR>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'Pink')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: pink" 
unselectable="on"></DIV></TD>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'Wheat')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: wheat" 
                unselectable="on"></DIV></TD>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'LemonChiffon')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: lemonchiffon" 
                  unselectable="on"></DIV></TD>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'PaleGreen')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: fpalegreen" 
                  unselectable="on"></DIV></TD>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'PaleTurquoise')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: paleturquoise" 
                  unselectable="on"></DIV></TD>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'LightBlue')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: lightblue" 
                  unselectable="on"></DIV></TD>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'Plum')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: plum" 
unselectable="on"></DIV></TD>
                <TD class=editor_colornormal 
                onmouseover="colorContext(this, 'mouseover')" 
                onclick="discuzcode('forecolor', 'White')" 
                onmouseout="colorContext(this, 'mouseout')" unselectable="on">
                  <DIV style="BACKGROUND-COLOR: white" 
                unselectable="on"></DIV></TD></TR>
              <TR></TR></TBODY></TABLE></DIV>
            <DIV class=popupmenu_popup id=posteditor_popup_table_menu title=menu 
            style="DISPLAY: none">
            <TABLE cellSpacing=0 cellPadding=4 border=0 unselectable="on">
              <TBODY>
              <TR class=popupmenu_option>
                <TD>表格行数:<br></TD>
                <TD><INPUT id=posteditor_table_rows size=5 value=2><br></TD>
                <TD>表格列数:<br></TD>
                <TD><INPUT id=posteditor_table_columns size=5 value=2><br></TD></TR>
              <TR class=popupmenu_option>
                <TD>表格宽度:<br></TD>
                <TD><INPUT id=posteditor_table_width size=5><br></TD>
                <TD>背景颜色:<br></TD>
                <TD><INPUT id=posteditor_table_bgcolor size=5><br></TD></TR>
              <TR class=popupmenu_option>
                <TD align=right colSpan=2><INPUT onclick="discuzcode('table')" type=button value="提 &nbsp; 交"><br></TD>
                <TD align=left colSpan=2>&nbsp;<INPUT onclick=hideMenu() type=button value="取 &nbsp; 消"><br></TD></TR></TBODY></TABLE></DIV></TD></TR></TBODY></TABLE>
      <DIV class=editor_text><TEXTAREA id="posteditor_textarea" name="message" 
      onkeydown=ctlent(event); onkeyup=javascript:storeCaret(this); style="WIDTH: 99%; HEIGHT: 150px; text-align:left" onclick="javascript: storeCaret(this);" tabIndex=100 rows=10 cols=60 onselect="javascript: storeCaret(this);">
<c:out value="${theForm.bean.content}"/></TEXTAREA><INPUT id=posteditor_mode type=hidden value=1 name=wysiwyg> <INPUT id=fid 
      type=hidden value=1 name=fid></DIV><input type=hidden name="bean.content" value="">

</DIV>

<SCRIPT type=text/javascript>
	var textobj = $(editorid + '_textarea');
	newEditor(wysiwyg);
	checkFocus();
	setCaretAtEnd();
	if(!(is_ie >= 5 || is_moz >= 2)) {
		$('restoredata').style.display = 'none';
	}
</SCRIPT>
    </td>
  </tr>
</table>
</div>
<!-- 相关附件 Start -->
<div class="update_subhead" >
	<span class="switch_open" onClick="StyleControl.switchDiv(this, divId_scrollLing3)">相关附件</span>
</div>
<div class="list_scroll" id="divId_scrollLing3">
    <table border="0" cellpadding="0" cellspacing="0" id="listtable3" class="Listing">
		<thead name="tabtitle">
		    <tr>
		      <td>
				<input type="button" class="list_create" onclick="addTableRow(listtable3);" id="tbl3_createtop"/> 
		      </td>
		      <td>公共资源</td>
		      <td>文件名称</td>
		      <td>文件描述</td>
			  <td>文件路径</td>
		    </tr>
		</thead>
		<tbody>
		<c:forEach var="item" items="${theForm.bean.tblUfNewsAttach}" varStatus="status">
			<tr id="tbl_trId_[<c:out value="${status.index}"/>]">
				<td align="center">
					<input type="button" class="list_create" onclick="addTableRow(listtable3);" id="tbl3_create[<c:out value="${status.index}"/>]" title="添加"/> 
			        <input type="button" class="list_delete" onclick="delTableRow(listtable3,this);" id="tbl3_delete[<c:out value="${status.index}"/>]" row="<c:out value='${status.index}'/>" name="delBtn" title="删除"/>
			        <input type="button" class="list_renew" onClick="renTableRow(listtable3,this);return false" id="tbl3_rec[<c:out value="${status.index}"/>]" style="display:none" row="<c:out value="${status.index}"/>" name="oldBtn" holdObj=true title="恢复"/>
				</td>
				<td align="center"><input type="hidden" name="attachement[<c:out value="${status.index}"/>].id" value="<c:out value="${item.id}"/>" id="attachement[<c:out value='${status.index}'/>].id"/>
					<input type="hidden" name="attachement[<c:out value='${status.index}'/>].delFlg" id="attachement[<c:out value='${status.index}'/>].delFlg" value="<c:out value='${item.delFlg}' />"/>
					<input type="hidden" name="attachement[<c:out value='${status.index}'/>].version" value="<c:out value='${item.version}' />"/>
					<input type="hidden" name="attachement[<c:out value='${status.index}'/>].savePath" value="<c:out value='${item.savePath}' />"/>
	 				<input type="hidden" name="attachement[<c:out value='${status.index}'/>].pubResource.code" value="<c:out value="${item.pubResource.code}"/>" />
 					<input type="checkbox" name="chk.attachement[<c:out value='${status.index}'/>].pubResource" value="0" <c:if test="${item.pubResource.code=='1'}">checked</c:if> disabled/>
					<!-- <c:out value="${item.pubResource.name}"/> -->
 				<input type="hidden" name="attachement[<c:out value='${status.index}'/>].fileName" value="<c:out value="${item.fileName }"/>"/></td>
				<td align="left"><a href="<c:url value="/uf/news/NewsPublishAction.do?step=download&savepath=${item.savePath }&filename=${item.fileName}"/>" onclick="return CurrentPage.download(this);"><c:out value="${item.fileName }"/></a></td>
				<td align="left">
					<input type="text" name="attachement[<c:out value='${status.index}'/>].description" value="<c:out value="${item.description }"/>" maxlength="50"/>
					<input type="button" id="edit_longText" onClick="definedWin.openLongTextWin(document.getElementsByName('attachement[<c:out value='${status.index}'/>].description')[0],'','');"/>
				</td>
				<td align="left"><c:if test="${item.pubResource.code=='1'}"><c:out value='${theForm.resourceUrl}'/></c:if><c:out value='${item.savePath}'/><c:if test="${item.pubResource.code=='1'}">/<c:out value='${item.fileName}'/></c:if>&nbsp;</td>
			</tr>
		</c:forEach>						
		</tbody>
  	</table>
</div>
<!-- 相关附件 End -->
<!-- 发布对象 Start -->
<div class="update_subhead" id="divId_target_title">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td>
			<span class="switch_open" onClick="StyleControl.switchDiv(this, listtable2)">发布对象</span>
		</td>
		<td align="right">
	    </td>
	</tr>
</table>
</div>
<div id="divId_target" class="list_scroll">
	<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="listtable2">
		<thead name="tabtitle">
			<tr>
				<td nowrap="nowrap" align="center">
				<input type="button" class="list_create" onclick="experBeanController.addListingRow(listtable2);" id="addPubObj" /> 
				</td>
				<td nowrap="nowrap">员工号</td>
				<td nowrap="nowrap">姓名</td>
				<td nowrap="nowrap">部门编号</td>
				<td nowrap="nowrap">部门</td>
				<td nowrap="nowrap">反馈信息</td>
			</tr>
		</thead>
		<tbody id=tablist>
		<c:forEach var="item" items="${theForm.bean.tblUfNewsFdbks}" varStatus="status">
			<tr id="experBeanController_trId_[<c:out value="${status.index}"/>]">
			<td align="center">
			<div>
				<input type="button" class="list_create" onClick="experBeanController.addListingRow(listtable2);" id="experBeanController_[<c:out value = "${status.index}" />].add" style="display:" title="添加"/> 									
				<%-- <input type="button" class="list_delete" onClick="CurrentPage.remove('<c:out value="${item.id}"/>')"/>	--%>
				<input type="button" class="list_delete" onClick="experBeanController.delRowCur(listtable2,this);return false" id="experBeanController_[<c:out value='${status.index}'/>].del" style="display:" row="<c:out value='${status.index}'/>" name="delBtn" title="删除"/>
				<input type="button" class="list_renew" onClick="experBeanController.renRowCur(listtable2,this);return false" id="experBeanController_[<c:out value='${status.index}'/>].old" style="display:none" row="<c:out value='${status.index}'/>" name="oldBtn" holdObj=true title="恢复"/>
			</div>
			</td>
				<td align="left">
					<input type="hidden" name="newsFdbk[<c:out value="${status.index}"/>].id" value="<c:out value="${item.id}"/>" id="experBeanController_[<c:out value='${status.index}'/>].id"/>
					<input type="hidden" name="newsFdbk[<c:out value='${status.index}'/>].delFlg" id="newsFdbk[<c:out value='${status.index}'/>].delFlg" value="<c:out value='${item.delFlg}' />"/>
					<input type="hidden" name="newsFdbk[<c:out value='${status.index}'/>].version" value="<c:out value='${item.version}' />"/>
				    <input name="experEmpInfo[<c:out value="${status.index}"/>].id" type="hidden" value="<c:out value="${item.tblHrEmpInfo.id}"/>" class="readonly" readonly="readonly" />
					<input name="experEmpInfo[<c:out value="${status.index}"/>].empCd" type="text" value="<c:out value="${item.tblHrEmpInfo.empCd}"/>" class="readonly" readonly="readonly" onchange="doTxtChange(<c:out value="${status.index}"/>)"/>&nbsp;<span class="font_request">*</span>
					<input type="button" name="edit_query" id="edit_query" onClick="selectEmp('experEmpInfo['+this.row+']', definedWin, 'checkbox')" row="<c:out value='${status.index}'/>"/></td>
				<td align="left"><input name="experEmpInfo[<c:out value="${status.index}"/>].empName" type="text" value="<c:out value="${item.tblHrEmpInfo.empName}"/>" class="readonly" readonly="readonly" />&nbsp;</td>
				<td align="left"><input id="experEmpInfo[<c:out value="${status.index}"/>].deptCd" name="experBean[<c:out value="${status.index}"/>].deptCd" 
						type="text" value="<c:out value="${item.tblHrEmpInfo.tblHrDept.deptCd}"/>" class="readonly" readonly="readonly" />&nbsp; </td>
				<td align="left"><input id="experEmpInfo[<c:out value="${status.index}"/>].deptName" name="experBean[<c:out value="${status.index}"/>].deptName" 
						type="text" value="<c:out value="${item.tblHrEmpInfo.tblHrDept.deptName}"/>" class="readonly" readonly="readonly" />&nbsp;</td>
				<td align="left">
					<input type="text" name="newsFdbk[<c:out value='${status.index}'/>].feedback" value="<c:out value="${item.feedback }"/>" readonly="readonly" class="readonly"/>
					<input type="button" id="edit_longText" onClick="definedWin.openLongTextWin(document.getElementsByName('newsFdbk[<c:out value='${status.index}'/>].feedback')[0],'',true);"/>
				</td>
			</tr>
		</c:forEach>

		</tbody>
    </table>
</div>
<!-- 发布对象 Start -->

<!-- 信息发布用 End -->
<script type="text/javascript" language="javascript">
	// 注意, 动态生成行必须放在 CurrentPage.initValidateInfo 方法后以保证验证信息被复制
	var experBeanController = new EditPage("experBeanController");
	/**
	  * 在弹出人员选择窗体进行多选时，需要在此重载该函数，来对本页面做相应处理
	  */
	definedWin.setListObjectInFor = function(arr,attribute,arrWithAtt) {
		var txtName, i1, i2, index, i;
	
		txtName = definedWin.txtName;
		i1 = txtName.indexOf("["); 
		i2 = txtName.indexOf("]");
		index = txtName.substring(i1+1, i2);
		i = parseInt(index);
		
		for (i = parseInt(index); i < newcount; i++) {
			txtName = txtName.substring(0, i1) + "[" + i + "]";
			var td = document.getElementById(txtName + "." + "id");
			if (td) {
				break;
			}
		}
		if (i == newcount && parseInt(index) < newcount)
			txtName = txtName.substring(0, i1) + "[" + i + "]";
		var td = document.getElementById(txtName + "." + "id");
		if (td == 'undefined' || td == null) experBeanController.addListingRow(listtable2);
	
		for(var t in arr) {		
			tt = t.replace(/_/gi, "."); //在列表定义field时候使用"_"代替"."
			temp = document.getElementById(txtName + "." + tt);
			if (temp) {
				if(temp.tagName == "INPUT") {
					temp.value = arr[t];
					continue;
				} else {
					temp.innerText = arr[t];
					continue;
				}					
			}else{
				//如果声明field值，这里则可以填充多个
				var objs = document.getElementsByTagName("INPUT");
				for( n=0;n<objs.length;n++) {
					if(objs[n].field == (txtName + "." + tt)){
						objs[n].value = arr[t];
					}
				}
			}
		}
		
		// 设置下一个控件名
		txtName = txtName.substring(0, i1);
		definedWin.txtName = txtName + "[" + (i+1)+ "]";	
	}
	//CheckBox单击时进行的操作
	doCheck = function(chk,hdn){
	    if (chk.checked ){
	        hdn.value = "1";
	    }else{
	        hdn.value = "0";
	    }
//        alert(hdn.value);
	}
    //打开预览窗口
    openPreview = function(){
        var content = document.getElementsByName("message")[0];
        document.getElementById('bbcodemode').click();
        document.getElementById('wysiwygmode').click();
        document.getElementsByName('bean.content')[0].value = bbcode2html(content.value);
//        if ((content.value == null)||(content.value.trim().length ==0))
//        {
//            alert("请填写信息内容！");
//            return;
//        }else{
            window.open("../../jsp/uf/news/newsPreview.html","newsPreview");
//        }
    }
	var msgInfo_ = new msgInfo();
	if (CurrentPage == null) {
		var CurrentPage = {};
	}
    //--取出select的值--
	//----------------------------------------------------------------------------------------------------------
	CurrentPage.columnList = new Array();
	CurrentPage.feedback = '0';
	CurrentPage.chgColumn = function () {
		for (var i = 0; i < CurrentPage.columnList.length; i++) {
		    if (document.getElementsByName('tblUfColumnid')[0].value == CurrentPage.columnList[i][0]) {
		    	CurrentPage.feedback = CurrentPage.columnList[i][1];
		    	break;
		    }
		}
		if (CurrentPage.feedback == '0') {
			document.getElementById('divId_target_title').style.display = "none";
			document.getElementById('divId_target').style.display = "none";
		} else {
			document.getElementById('divId_target_title').style.display = "";
			document.getElementById('divId_target').style.display = "";
			if (document.getElementById("listtable2").getElementsByTagName("tr").length < 2) {
				experBeanController.addListingRow(listtable2);
			}
		}
		Global.setHeight();
	}
	<c:forEach var="item" items="${theForm.columnList}" varStatus="status">
	CurrentPage.columnList[CurrentPage.columnList.length] =
	new Array("<c:out value="${item.id}"/>","<c:out value="${item.feedbackFlg}"/>");  
	</c:forEach>
	//----------------------------------------------------------------------------------------------------------

	CurrentPage.reset = function () {
		document.f.reset();
	}
	var id = $('oid').value;
	var published = $('bean.published.code').value;
	var cur_d = DateUtils.GetDBDate();
	var cur_mon = cur_d.getMonth() < 9 ?'0' + (cur_d.getMonth() + 1) : (cur_d.getMonth() + 1);
	var cur_day = cur_d.getDate() < 10 ?'0' + cur_d.getDate() : cur_d.getDate();
	var strDate = cur_d.getFullYear() + '-' + cur_mon + '-' + cur_day;

	if (id == null || id.length == 0 || published != "1" || strDate > document.getElementById('bean.invalidTime').value) {
	    //页面保存（只有在新增时可用）进行的操作
		CurrentPage.submit = function () {
			if (!CurrentPage.validation()) {
				return;
			}
			if ($('bean.published.code').value == "1" && !confirm(msgInfo_.getMsgById('UF_I011_A_0'))){
			    return false;
			}
			var content = document.getElementsByName("message")[0];
			document.getElementById('bbcodemode').click();
			document.getElementById('wysiwygmode').click();
			document.getElementsByName('bean.content')[0].value = bbcode2html(content.value);
//            alert(content.value);
//            alert(document.getElementsByName('bean.content')[0].value);
//            return false;
/*            if (document.getElementById('posteditor_textarea').value == null){
			   alert("内容为空！");
			   //document.getElementById('posteditor_textarea').focus();
			   return false;
			}*/
			$('step').value = 'save';
			FormUtils.post(document.forms[0], '<c:url value='/uf/news/NewsPublishAction.do?step=save'/>');
		}
	}else{
	   document.getElementById('bean.title').readOnly = true;
	   document.getElementById('bean.author').readOnly = true;
	   document.getElementById('input_select').disabled = true;
	   document.getElementById('bean.source').readOnly = true;
	   document.getElementById('bean.keyword').readOnly = true;
	   document.getElementById('input_date').disabled = true;
	   document.getElementById('posteditor_textarea').readOnly = true;
	   document.getElementById('chk.recommend').disabled = true;
	   document.getElementById('chk.published').disabled = true;
	   document.getElementById('bbcodemode').click();
	   document.getElementById('bbcodemode').disabled = true;
	   document.getElementById('wysiwygmode').disabled = true;
	   document.getElementById('addPubObj').disabled = true;
//       document.getElementById("listtable2").disabled = true;
//       document.getElementById("listtable3").disabled = true;
       document.getElementById("tbl3_createtop").disabled = true;
       var arr = document.getElementsByName("edit_query");
       for (var i = 0; i < arr.length; i++){
           arr[i].disabled = true;
       }
       arr = document.getElementById("posteditor").all;
       for (var i = 0; i < arr.length; i++) {
           if(arr[i].onclick != null && arr[i].onclick != ""){
               arr[i].disabled = true;
               arr[i].onmouseover = "";
               arr[i].onmouseout = "";
               arr[i].onclick = "";
           }
       }
       var icount = document.getElementById("listtable2").getElementsByTagName("tr").length -1;
       for (var i = 0; i < icount; i++){
           document.getElementById("experBeanController_[" + i + "].add").disabled = true;
           document.getElementById("experBeanController_[" + i + "].del").disabled = true;
           document.getElementById("experBeanController_[" + i + "].old").disabled = true;
       }
       var icount = document.getElementById("listtable3").getElementsByTagName("tr").length -1;
       for (var i = 0; i < icount; i++){
           document.getElementById("tbl3_create[" + i + "]").disabled = true;
           document.getElementById("tbl3_delete[" + i + "]").disabled = true;
           document.getElementsByName("attachement[" + i + "].description")[0].className="readonly";
       }
//       listtable3.disabled = true;   
//	   listtable2.disabled = true;
	}
	CurrentPage.validation = function () {
		if(!Validator.Validate(document.forms[0], 4)){
			return false;
		}
		if (!verifyAllform()) {
			return false;
		}
//		var message = bbinsert && wysiwyg ? html2bbcode(getEditorContents()) : (!theform.parseurloff.checked ? parseurl(document.getElementsByName("bean.content")[0].value) : document.getElementsByName("bean.content")[0].value);
//		var message = bbinsert && wysiwyg ? html2bbcode(getEditorContents()) : document.getElementsByName("bean.content")[0].value;
		var message = html2bbcode(getEditorContents()).trim();
//		alert(message);
		if(!disablepostctrl && ((postminchars != 0 && mb_strlen(message) < postminchars) || (postmaxchars != 0 && mb_strlen(message) > postmaxchars))) {
			Validator.warnMessage(msgInfo_.getMsgById('UF_I018_C_4',['内容',mb_strlen(message),postminchars,postmaxchars]));
			return false;
		}
		if(!CurrentPage.validateFiles()) {
			return false;
		}
		if (CurrentPage.feedback != '0' && !CurrentPage.validateEmpItems()) {
			return false;
		}
		return true;
	}

	/**
	 * 验证文件
	 */
	CurrentPage.validateFiles = function() {
//		var tableRowNum=document.all.listtable3.rows.length;
		var tableRowNum=newrow;
		for (var i = 0; i < tableRowNum; i++) {
			if($('flb.files[' + i + '].formFile')){
				var flnm = $('flb.files[' + i + '].formFile').value;
				if(flnm==""){
					Validator.clearValidateInfo();
					Validator.warnMessage(msgInfo_.getMsgById('UF_I003_R_1',['文件路径']));
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 验证员工信息
	 */
	CurrentPage.validateEmpItems = function() {
		var empMap = new Object();
		if (CurrentPage.feedback == '0') {
			return true;
		}
	//	var empItemsCount = <c:out value = "${pageScope['experBeanCount']}" default = "0" />;
//		var tableRowNum=document.all.listtable2.rows.length;
		var tableRowNum=newcount;
		var hasEmpCd = false;
		for (var i = 0; i < tableRowNum; i++) {
			if($('experEmpInfo[' + i + '].empCd')){
				var empCd = $('experEmpInfo[' + i + '].empCd').value;
				if(empCd==""){
					continue;
				}
				hasEmpCd = true;
				var empName = $('experEmpInfo[' + i + '].empName').value;
				var entryValue = empMap[empCd + ' - ' + empName];
				if (entryValue == null) {
					entryValue = 0;
				}
				entryValue++;
				empMap[empCd + ' - ' + empName] = entryValue;
			}
		}
		if (!hasEmpCd) {
			Validator.clearValidateInfo();
			Validator.warnMessage(msgInfo_.getMsgById('UF_I003_R_1',['员工号']));
			return false;
		}
		var repeatedKeys = new Array();
		for (entryKey in empMap) {
			var entryValue = empMap[entryKey];
			if (entryValue > 1) {
				repeatedKeys[repeatedKeys.length] = entryKey;
			}
		}
		
		if (repeatedKeys.length > 0) {
			Validator.clearValidateInfo();
			Validator.warnMessage(msgInfo_.getMsgById('UF_I017_C_1',['员工号']));
			for (var i = 0; i < repeatedKeys.length; i++) {
				Validator.warnMessage('   ' + repeatedKeys[i]);
			}
			return false;
		}
		
		return true;
	}

	CurrentPage.initValideInput = function () {
		document.getElementById('bean.title').dataType = 'Require';
		document.getElementById('bean.title').msg = msgInfo_.getMsgById('UF_I003_R_1',['标题']);
		document.getElementsByName('tblUfColumnid')[0].dataType = 'Require';
		document.getElementsByName('tblUfColumnid')[0].msg = msgInfo_.getMsgById('UF_I003_R_1',['所属栏目']);
		document.getElementById('bean.invalidTime').dataType = 'Require|Compare';
		document.getElementById('bean.invalidTime').operator = 'GreaterThanEqual';
		
		document.getElementById('bean.invalidTime').to = strDate;
		
		document.getElementById('bean.invalidTime').msg = msgInfo_.getMsgById('UF_I003_R_1',['有效期'])
												+ '|' + msgInfo_.getMsgById('UF_I020_C_1',['有效期']);
//		document.getElementById('posteditor_textarea').dataType = 'Require';
//		document.getElementById('posteditor_textarea').msg = '必须填写内容';
	}
		
	CurrentPage.initValideInput();
	CurrentPage.download = function(obj) {
	    FormUtils.post(document.forms[0], obj.href, true);
	    return false;
	}
	CurrentPage.create = function() {
	    $('oid').value = '';
	    FormUtils.post(document.forms[1], '<c:url value='/uf/news/NewsPublishAction.do?step=info'/>');
	}
	//对发布对象表进行的操作（增加，删除以及对对一行的恢复）
	var newcount = document.getElementById("listtable2").getElementsByTagName("tr").length -1;
//	var newcount = 0;
	//在发布对象表中增加一行
	experBeanController.addListingRow = function(tbl){ 
	    var testTbl =  document.getElementById("listtable2");
	    var newTr = testTbl.insertRow();	
	    newTr.id="experBeanController_trId_[" + newcount + "]";
		var cellhtml;
		//添加2列 
		var newTd0 = newTr.insertCell(); 
		newTd0.align = 'center';
		var newTd1 = newTr.insertCell();
		var newTd2 = newTr.insertCell();
		var newTd3 = newTr.insertCell();
		var newTd4 = newTr.insertCell();
		var newTd5 = newTr.insertCell();
		//设置列内容和属性 
		cellhtml = 	'<div>'	+
		"<input type=button class='list_create' onclick='experBeanController.addListingRow(listtable2);' id=experBeanController_[" + newcount + "].add title='添加' /> " +									
		'<input type="button" class="list_delete" onClick="experBeanController.delRowCur(listtable2,this);return false" id="experBeanController_[' + newcount + '].del"  row="' + newcount + '" name="delBtn" title="删除"/>' +
		'<input type="button" class="list_renew" onClick="experBeanController.renRowCur(listtable2,this);return false" id="experBeanController_[' + newcount + '].old" style="display:none" row="'+ newcount + '" name="oldBtn" holdObj=true title="恢复"/></div>';
		newTd0.innerHTML = cellhtml;
		cellhtml = 
		'<input type="hidden" name="newsFdbk[' + newcount + '].id" value="" id="experBeanController_[' + newcount + '].id"/>' +
		'<input type="hidden" name="newsFdbk[' + newcount + '].delFlg" value="0" id="newsFdbk[' + newcount + '].delFlg"/>' +
		'<input name="experEmpInfo[' + newcount + '].id" type="hidden" />' +
 		'<input name="experEmpInfo[' + newcount + '].empCd" type="text"  class="readonly" readonly="readonly" onchange="doTxtChange(' + newcount + ')"/>&nbsp;<span class="font_request">*</span>' + 
        '<input type="button" id="edit_query" onClick="selectEmp(\'experEmpInfo[\'+this.row+\']\', definedWin, \'checkbox\')" row="' + newcount + '"/>';
		newTd1.innerHTML = cellhtml;
		cellhtml = '<input name="experEmpInfo[' + newcount + '].empName" type="text" class="readonly" readonly="readonly" />&nbsp;';
		newTd2.innerHTML = cellhtml;
		cellhtml = '<input id="experEmpInfo[' + newcount + '].deptCd" name="experBean[' + newcount + '].deptCd" ' +
				   'type="text" class="readonly" readonly="readonly" />&nbsp; ';
		newTd3.innerHTML = cellhtml;		   
		cellhtml = '<input id="experEmpInfo[' + newcount + '].deptName" name="experBean[' + newcount + '].deptName"' +
						'type="text" class="readonly" readonly="readonly" />&nbsp; ';
		newTd4.innerHTML = cellhtml;				
		cellhtml = '&nbsp;';
		newTd5.innerHTML = cellhtml;				

		newcount++; 
		Global.setHeight();
	}
	CurrentPage.chgColumn();
	//在发布对象表中删除一行
	experBeanController.delRowCur = function (table,sel){
		var inum = sel.row;
		
		var del = document.getElementById("newsFdbk["+inum+"].delFlg").value;
		
		var str = document.getElementById(experBeanController.name+"_["+inum+"].id").value;
		
		var obj = document.getElementById(experBeanController.name+"_trId_["+inum+"]");
		experBeanController.afterDeleteRow(table,sel)
		if(str ==""){
//			if(inum == 0){
//				experBeanController.disableButton(obj);
//				experBeanController.switchButton(inum,"none","none","")
//			}else{
				table.deleteRow(obj.rowIndex);
				Global.setHeight();
//			}
		}else{
			//experBeanController.disableButton(obj);
			obj.className = "disabled";
			document.getElementById("newsFdbk["+inum+"].delFlg").value="1";
			experBeanController.switchButton(inum,"none","none","");
		}
	}
	//在发布对象表第一行按删除按钮后进行的恢复操作
	experBeanController.renRowCur = function (table,sel){
		var inum = sel.row;
		document.getElementById("newsFdbk["+inum+"].delFlg").value="0";
		experBeanController.renRow(table,sel);
	}
	//增加附件操作Table用(增加删除行)
	var newrow = document.getElementById("listtable3").getElementsByTagName("tr").length -1;
	//在增加附件表中添加一行
	addTableRow = function(tbl){ 
	    var newTr = tbl.insertRow();	
	    newTr.id="tbl_trId_[" + newrow + "]";
		var cellhtml;
		//添加2列 
		var newTd0 = newTr.insertCell(); 
		newTd0.align='center';
		var newTd1 = newTr.insertCell();
		newTd1.align='center';
		var newTd2 = newTr.insertCell();
		newTd2.align='left';
		var newTd3 = newTr.insertCell();
		newTd3.align='left';
		var newTd4 = newTr.insertCell();
		newTd4.align='left';
		//设置列内容和属性 
		cellhtml = 
				'<input type="button" class="list_create" onclick="addTableRow(listtable3);" id="tbl3_create[' + newrow + ']" title="添加"/>&nbsp;' +
			    '<input type="button" class="list_delete" onclick="delTableRow(listtable3,this);" row="' + newrow + '" id="tbl3_delete[' + newrow + ']" name="delBtn" title="删除"/>' +
			    '<input type="button" class="list_renew" onClick="renTableRow(listtable3,this);return false" id="tbl3_rec[' + newrow + ']" style="display:none" row="'+ newrow + '" name="oldBtn" holdObj=true title="恢复"/>';
        newTd0.innerHTML = cellhtml;
        cellhtml =
		        '<input type="hidden" name="attachement[' + newrow + '].id" value="" id="attachement[' + newrow + '].id"/>' +
				'<input type="hidden" name="attachement[' + newrow + '].delFlg" id="attachement[' + newrow + '].delFlg" value="0"/>' +
				'<input type="hidden" name="attachement[' + newrow + '].savePath" value=""/>' + 
				'<input type="hidden" name="attachement[' + newrow + '].version" value=""/>' +
 				'<input type="checkbox" name="chk.attachement[' + newrow + '].pubResource" value="0" onclick="doCheck(this,document.getElementsByName(\'attachement[' + newrow + '].pubResource.code\')[0])" />' +
 				'<input type="hidden" name="attachement[' + newrow + '].pubResource.code" value="0" />' +
 				'<input type="hidden" name="attachement[' + newrow + '].fileName" value=""/>';

        newTd1.innerHTML = cellhtml;
        cellhtml = '<a href=""></a>&nbsp;';
        newTd2.innerHTML = cellhtml;
		cellhtml = '<input type="text" name="attachement[' + newrow + '].description" maxlength="50"/>&nbsp;' +
				   '<input type="button" id="edit_longText" onClick="definedWin.openLongTextWin(document.getElementsByName(\'attachement[' + newrow + '].description\')[0],\'\',\'\');"/>';

		newTd3.innerHTML = cellhtml;
		cellhtml = '<input type="file" name="flb.files[' + newrow + '].formFile" value="" row="' + newrow + '" onchange="changeSrc(this)" />';
		newTd4.innerHTML = cellhtml;
		   
		newrow++; 
		Global.setHeight();
	}	
	//在增加附件表中删除一行
	delTableRow = function (table,sel){
		var inum = sel.row;
		var del = document.getElementById("attachement["+inum+"].delFlg").value;
		var str = document.getElementById("attachement["+inum+"].id").value;
		var obj = document.getElementById("tbl_trId_["+inum+"]");
		if(str ==""){
			table.deleteRow(obj.rowIndex);
			Global.setHeight();
		}else{
			document.getElementById("tbl3_create[" + inum + "]").style.display = "none";
			document.getElementById("tbl3_delete[" + inum + "]").style.display = "none";
			document.getElementById("tbl3_rec[" + inum + "]").style.display = "";
			document.getElementById("attachement["+inum+"].delFlg").value="1";
		}
		//table.deleteRow(obj.rowIndex);
		//Global.setHeight();
		
	}
	//在增加附件表中只有一行时进行恢复	
	renTableRow = function(table,sel){
	    var inum = sel.row;
		document.getElementById("attachement["+inum+"].delFlg").value="0";
		document.getElementById("tbl3_create[" + inum + "]").style.display = "";
		document.getElementById("tbl3_delete[" + inum + "]").style.display = "";
		document.getElementById("tbl3_rec[" + inum + "]").style.display = "none";
	}
	//当发布对象表列表空时自动加一行
	//if (newcount == 0) {
	//    experBeanController.addListingRow(listtable2);
	//}
	function changeSrc(sel){	   
	   var file = sel.value;
	   var arrfile = file.split("\\");
	   var curcell = sel.parentNode;
	   var currow = curcell.parentNode;
	   var inum = sel.row;
	   document.getElementsByName("attachement[" + inum + "].fileName")[0].value = arrfile[arrfile.length - 1];
	   currow.cells[curcell.cellIndex - 2].innerHTML = "<a href='" + file + "'>" + arrfile[arrfile.length - 1] + "</a>";
	}
</script>
</form>
<form name="empty" action="" method="post">
</form>
</body>
</html>
