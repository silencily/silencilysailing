
/* CurrentPage ��Ĭ��ʵ��
 * 2007-02-22 liuz
 */

if(CurrentPage == null){
	var CurrentPage = {};
}

CurrentPage.defaultActionUrl = "/security/defaultAction.do?step=opera";

CurrentPage.getActionUrl = function(url){
	if(url == null){
		var url = CurrentPage.defaultActionUrl;
	}
	return url;
}

CurrentPage.remove = function(oid,url) {
	if (! confirm(" ɾ�������ݲ��ָܻ�, ��ȷ��Ҫɾ���� ? ")) {
		return;
	}
	var url = CurrentPage.getActionUrl(url);
	if(url.indexOf('?') > 0 )
		url = url + '&';
	else
		url = url + '?';
	FormUtils.redirect(url +'oid='+ oid);
}

CurrentPage.query = function(url) {
    if (document.getElementsByName("paginater.page") != null) {
        document.getElementsByName("paginater.page").value = 0;
    }    
	url = CurrentPage.getActionUrl(url);
    FormUtils.post(document.forms[0], url);
}
