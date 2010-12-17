var y_lo=document.getElementById("y_js").src.toString();
var y_re_p=new RegExp('pubid=([0-9]+)','ig');
var y_re_w=new RegExp('wid=([0-9]+)','ig');
y_re_p.exec(y_lo);y_pid=RegExp.$1;
y_re_w.exec(y_lo);y_wid=RegExp.$1;
var y_rf=document.referrer;
var y_d=new Date();
if(y_pid!="")document.write("<img src='http://stat.aliunion.cn.yahoo.com/logo.jpg?pubid="+y_pid+"&refer="+y_rf+"&d="+y_d.getTime()+"' title='pubid: "+y_pid+"' border='0'>");
else if(y_wid!="")document.write("<img src='http://stat.aliunion.cn.yahoo.com/logo.jpg?wid="+y_wid+"&refer="+y_rf+"&d="+y_d.getTime()+"' title='websiteid: "+y_wid+"' border='0'>");