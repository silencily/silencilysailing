<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<HTML xmlns:v>
	<head>
	    
	    <title>工作流图形化显示</title>
		<STYLE>v\:*{behavior:url(#default#VML);}</STYLE>
		<script type="text/javascript" src = "<%=request.getContextPath()%>/public/scripts/xmllyt.js"></script>
		<script>
         function window.onerror(){return true;}  //防止您的程序出现错误时弹出提示
        </script>
  
			<%			  
			 //--工作流定义中的所有步骤ID列表＜这里不包含开始＞--
			   List listStepID = (List)request.getAttribute("listStepId");
			   
			   //历史步骤ID列表
			
			   List listHistoryStepId = new ArrayList();
			   if(request.getAttribute("listHistoryStepID") != null) {
			   List listHistoryStepIdTemp = (List)request.getAttribute("listHistoryStepID");
			   //slistHistoryStepId.add(listStepID.get(0).toString());
			   for(int i = 0; i < listHistoryStepIdTemp.size(); i++){
				   listHistoryStepId.add(listHistoryStepIdTemp.get(i).toString());				  
			   }
			   }
			  
			   //当前步骤ID列表
			   List listCurrentStepId = new ArrayList();
			   if(request.getAttribute("listCurrentStepID") != null){
			       listCurrentStepId = (List)request.getAttribute("listCurrentStepID");
			   }
			   String wfName = null;
			   if(request.getAttribute("listWorkFlowName") != null) {
			       wfName = (String)((List)request.getAttribute("listWorkFlowName")).get(0);
			   }
			   
			   //历史与当前步骤ID列表
			   List listTotalStepId = new ArrayList();	
			   		 
			   for(int i=0; i < listHistoryStepId.size(); i++){
			       listTotalStepId.add(listHistoryStepId.get(i).toString());			       
			   }
			   for(int i=0; i < listCurrentStepId.size(); i++){
			       listTotalStepId.add(listCurrentStepId.get(i).toString());			       
			   }
			  
			
			
				
				//--工作流定义中所有步骤名称列表--
				List listStepName = new ArrayList();
				if(request.getAttribute("listStepName") != null) {
				     listStepName = (List)request.getAttribute("listStepName");
				}

				//--在每一个Step中的所有下一步的步骤的ID--
				List listNextStepID = new ArrayList();
				if(request.getAttribute("listCommitStepId") != null) {
				List listNextStepIDTemp = (List)request.getAttribute("listCommitStepId");			
				 for(int i = 0; i < listNextStepIDTemp.size(); i++){
					 listNextStepID.add(listNextStepIDTemp.get(i).toString());					 
				 }
				}
				 //--从工作流实力中读出状态列表--
				List listStatus = new ArrayList();
				if(request.getAttribute("listStepStatus") != null){
					 listStatus  = (List)request.getAttribute("listStepStatus");
				 }
				else{
					for(int i = 0; i < listStepName.size(); i++) {
						listStatus.add("0");
					}
				}
				//--审批显示------
				List listHistoryInfoTemp = new ArrayList();
				if(request.getAttribute("listHistoryInfo") != null)
				listHistoryInfoTemp = (List)request.getAttribute("listHistoryInfo");
				
				List listHistoryInfo = new ArrayList();				
				for(int i = 0; i < listHistoryInfoTemp.size(); i++){
					String strHistoryInfo = listHistoryInfoTemp.get(i).toString();
					listHistoryInfo.add(strHistoryInfo);					
				}	
				
				
		%>	

<script language="javascript">
var getHtml = "";
function useXmlDraw(xmlLayOut){     
    var xmlLayOut = '../jsp/wf/graphics/wfChartTemplate/' + xmlLayOut +'.lyt.xml';
    var xmlHttp = XmlHttp.create();  
    var async = false;
    var specialNextID = new Array();
    var specialStartID = new Array();
    var totleSum = new Array();
    var number = 0
    var totleNumber = 0  
    xmlHttp.open("GET", xmlLayOut, async);
    xmlHttp.onreadystatechange = function () {
    if (xmlHttp.readyState == 4){         
        var cells = xmlHttp.responseXML.getElementsByTagName("cell");       
        var actions = xmlHttp.responseXML.getElementsByTagName("action");
        var stepID = "";
        <%for(int i = 0; i < listStepID.size(); i++){%>            
           for(var n = 0; n < cells.length; n++){
             var cell = cells[n]; 
             if(cell.getAttribute("id")==('<%=listStepID.get(i).toString()%>')){ 
                 var name = cell.getAttribute("name");  
                 var X = parseInt(cell.getAttribute("x"));
                 var Y = parseInt(cell.getAttribute("y"));
                 stepID = cell.getAttribute("id");
                 var stepStatus = '<%=listStatus.get(i).toString()%>'; 
                 var hisrtoryInfo = name;
                  if('<%=listStepID.get(i).toString()%>' == 1 || name == "结束"){   
                    getHtml = getHtml + startAndEnd(name,X,Y); 
                  }else{
                     getHtml = getHtml + step(stepStatus,name,X,Y,80,50,name);
                  }
              <%
		         try{
		         for(int w = 0; w < listHistoryStepId.size(); w++){		        	 
		             if(listStepID.get(i).toString().equals(listHistoryStepId.get(w).toString())){%>
		              if('<%=listStepID.get(i).toString()%>' == 1 || name == "结束"){   
                        getHtml = getHtml + startAndEnd(name,X,Y); 
                      }else{
		                getHtml = getHtml + step(stepStatus,name,X,Y,80,50,'<%=listHistoryInfo.get(w).toString()%>');
		              }
              <%     }
                 }}catch(Exception e){%>
                      if('<%=listStepID.get(i).toString()%>' == 1 || name == "结束"){   
                      getHtml = getHtml +  startAndEnd(name,X,Y); 
                      }else{
                	 getHtml = getHtml + step(stepStatus,name,X,Y,80,50,name);
                	 }
              <%  }%>                
              
                 var nextStepIdString = '<%=listNextStepID.get(i).toString()%>';  
                 nextStepIdString = nextStepIdString.split(',');                 
                 for(k = 0; k < nextStepIdString.length; k++){                 
                     //画转折线                      
                     for(var s = 0; s < actions.length; s++){
                        var actionTemp = actions[s];
                        var startID = actionTemp.getAttribute("startId");
                        var nextID = actionTemp.getAttribute("nextId");
                        var point = actionTemp.getAttribute("points"); 
                        
                                                                
                        if(stepID == startID && nextStepIdString[k] == nextID){                           
                           specialNextID[number] = nextID; 
                           specialStartID[number] = startID;
                           number++;                          
                           getHtml = getHtml + actionOther('0',point);                        
                        }
                     }           
                 }
                
             }
           }
         <%}%> 
         

		<%for(int i = 0; i < listStepID.size(); i++){%>            
           for(var n = 0; n < cells.length; n++){
             var cell = cells[n]; 
             if(cell.getAttribute("id")==('<%=listStepID.get(i).toString()%>')){ 
                 var name = cell.getAttribute("name");  
                 var X = parseInt(cell.getAttribute("x"));
                 var Y = parseInt(cell.getAttribute("y"));
                 stepID = cell.getAttribute("id");
                 var stepStatus = '<%=listStatus.get(i).toString()%>'; 
                 var hisrtoryInfo = name;
                
                         
              
                 var nextStepIdString = '<%=listNextStepID.get(i).toString()%>';  
                 nextStepIdString = nextStepIdString.split(',');                 
                  for(k = 0; k < nextStepIdString.length; k++){                 
                     //画转折线                      
                     for(var s = 0; s < actions.length; s++){
                        var actionTemp = actions[s];
                        var startID = actionTemp.getAttribute("startId");
                        var nextID = actionTemp.getAttribute("nextId");
                        var point = actionTemp.getAttribute("points"); 
                        
                                                                
                        if(stepID == startID && nextStepIdString[k] == nextID){                           
                           specialNextID[number] = nextID; 
                           specialStartID[number] = startID;
                           number++; 
                         <%for(int a = 0; a < listTotalStepId.size(); a++){
                              String string1 = listTotalStepId.get(a).toString();
                              String string2 = listTotalStepId.get((a+1)>=listTotalStepId.size()? a : a+1).toString();
                              if(listStepID.get(i).toString().equals(string1)){
                                                             
                         %>
                                
                              if(nextStepIdString[k] == '<%=string2%>'){
                                
                              getHtml = getHtml + actionOther('1',point);
		                      }
                         <%
                           }} %>
                        }
                     }           
                 }
                
             }
           }
         <%}%> 
         
         for(var h = 0; h < specialStartID.length; h++){
            //alert("折线步骤"+specialStartID[h]+"-->"+specialNextID[h]);
         }
         <%for(int i = 0; i < listStepID.size(); i++){%>  
           for(var n = 0; n < cells.length; n++){
             var cell = cells[n]; 
             if(cell.getAttribute("id")==('<%=listStepID.get(i).toString()%>')){                  
                 var X = parseInt(cell.getAttribute("x"));
                 var Y = parseInt(cell.getAttribute("y"));
                 stepID = cell.getAttribute("id");
                 var stepStatus = '<%=listStatus.get(i).toString()%>';                 
              
                 var nextStepIdString = '<%=listNextStepID.get(i).toString()%>';  
                 nextStepIdString = nextStepIdString.split(',');
                 
                 for(k = 0; k < nextStepIdString.length; k++){ 
                    for(j = 0; j < specialStartID.length; j++){                        
                        if(stepID == specialStartID[j] && specialNextID[j] ==nextStepIdString[k]){                            
                            nextStepIdString.splice(k,1);                                                      
                        }
                    }
                 }
                 for(k = 0; k < nextStepIdString.length; k++){ 
                     for(var f = 0; f < cells.length; f++){
                        var cell2 = cells[f];
                        if(cell2.getAttribute("id") == nextStepIdString[k]){ 
                             toX = parseInt(cell2.getAttribute("x"));
                             toY = parseInt(cell2.getAttribute("y"));
                             var xmlFromY = Y;
                             var xmlToY = toY;
                             var xmlFromX = X+27
                             var xmlToX = toX+27
                             var secant = 0;
		                     var clipeLenth;
		                     
		                     if(xmlFromX<=xmlToX){
		                         clipeLenth = 47;
		                     }
		                     else{
		                         clipeLenth = -47;
		                     }
		                     
		                     if(xmlToY!=xmlFromY){
		                     if(xmlFromX!=xmlToX){
		                              secant = (xmlToY - xmlFromY)/(xmlToX -xmlFromX);
		                              angle = Math.atan(secant);
		                              xmlFromX = clipeLenth*Math.cos(angle)+xmlFromX;
		                              xmlFromY = clipeLenth*Math.sin(angle)+xmlFromY;
		                              xmlToX = xmlToX - clipeLenth*Math.cos(angle);
		                              xmlToY = xmlToY - clipeLenth*Math.sin(angle);	
		                     } 	                     
		                     }
		                    if(xmlToY==xmlFromY){
		                          if(xmlFromX<xmlToX){
		                              xmlFromX = xmlFromX + 50;
		                              xmlToX  = xmlToX - 42;
		                          }
		                          if(xmlFromX>xmlToX){
		                              xmlFromX = xmlFromX - 45;
		                              xmlToX  = xmlToX + 47;
		                          }
		                     }
		                     if(xmlFromX==xmlToX){
		                          if(xmlToY<xmlFromY){
		                              xmlFromY = xmlFromY - 25;
		                              xmlToY = xmlToY + 35;
		                          }
		                          if(xmlToY>xmlFromY){
		                              xmlFromY = xmlFromY + 40;
		                              xmlToY = xmlToY - 20;
		                          }
		                     }
		                    getHtml = getHtml + action('0',xmlFromX,xmlFromY,xmlToX,xmlToY);                            
                        }
                     }
                 }
                 
              }
           }
         <%}%>  
         
         <%for(int i = 0; i < listStepID.size(); i++){%>  
           for(var n = 0; n < cells.length; n++){
             var cell = cells[n]; 
             if(cell.getAttribute("id")==('<%=listStepID.get(i).toString()%>')){                  
                 var X = parseInt(cell.getAttribute("x"));
                 var Y = parseInt(cell.getAttribute("y"));
                 stepID = cell.getAttribute("id");
                 var stepStatus = '<%=listStatus.get(i).toString()%>';                 
              
                 var nextStepIdString = '<%=listNextStepID.get(i).toString()%>';  
                 nextStepIdString = nextStepIdString.split(',');
                 
                 for(k = 0; k < nextStepIdString.length; k++){ 
                    for(j = 0; j < specialStartID.length; j++){                        
                        if(stepID == specialStartID[j] && specialNextID[j] ==nextStepIdString[k]){                            
                            nextStepIdString.splice(k,1);                                                      
                        }
                    }
                 }
                 for(k = 0; k < nextStepIdString.length; k++){ 
                     for(var f = 0; f < cells.length; f++){
                        var cell2 = cells[f];
                        if(cell2.getAttribute("id") == nextStepIdString[k]){ 
                             toX = parseInt(cell2.getAttribute("x"));
                             toY = parseInt(cell2.getAttribute("y"));
                             var xmlFromY = Y;
                             var xmlToY = toY;
                             var xmlFromX = X+27
                             var xmlToX = toX+27
                             var secant = 0;
		                     var clipeLenth;
		                     
		                     if(xmlFromX<=xmlToX){
		                         clipeLenth = 47;
		                     }
		                     else{
		                         clipeLenth = -47;
		                     }
		                     
		                     if(xmlToY!=xmlFromY){
		                     if(xmlFromX!=xmlToX){
		                              secant = (xmlToY - xmlFromY)/(xmlToX -xmlFromX);
		                              angle = Math.atan(secant);
		                              xmlFromX = clipeLenth*Math.cos(angle)+xmlFromX;
		                              xmlFromY = clipeLenth*Math.sin(angle)+xmlFromY;
		                              xmlToX = xmlToX - clipeLenth*Math.cos(angle);
		                              xmlToY = xmlToY - clipeLenth*Math.sin(angle);	
		                     } 	                     
		                     }
		                    if(xmlToY==xmlFromY){
		                          if(xmlFromX<xmlToX){
		                              xmlFromX = xmlFromX + 50;
		                              xmlToX  = xmlToX - 42;
		                          }
		                          if(xmlFromX>xmlToX){
		                              xmlFromX = xmlFromX - 45;
		                              xmlToX  = xmlToX + 47;
		                          }
		                     }
		                     if(xmlFromX==xmlToX){
		                          if(xmlToY<xmlFromY){
		                              xmlFromY = xmlFromY - 25;
		                              xmlToY = xmlToY + 35;
		                          }
		                          if(xmlToY>xmlFromY){
		                              xmlFromY = xmlFromY + 40;
		                              xmlToY = xmlToY - 20;
		                          }
		                     }
		                         <%for(int a = 0; a < listTotalStepId.size(); a++){
                                 String string1 = listTotalStepId.get(a).toString();
                                 String string2 = listTotalStepId.get((a+1)>=listTotalStepId.size()? a : a+1).toString();
                                 if(listStepID.get(i).toString().equals(string1)){
                                                             
                             %>
                                
                                 if(nextStepIdString[k] == '<%=string2%>'){                                
                                   getHtml = getHtml + action('1',xmlFromX,xmlFromY,xmlToX,xmlToY);
		                         }
                             <%
                             }} %>
                        }
                     }
                 }
                 
              }
           }
         <%}%>
         
     }
    }
    xmlHttp.send(null);  
    return getHtml;
    
 };

function step(state,stepname,pleft,ptop,pwidth,pheight,tooltip){
 var pleft = pleft / 1280 * 1024;
 var ptop = ptop / 1024 * 768;
 var pwidth = pwidth / 1280 * 1024;
 var pheight = pheight / 1024 * 768;
 var color1Step=new Array("#FFBF23","#FF5723","#23FFAF");
 
 var tempstate = state;

 var color1=color1Step[parseInt(tempstate)];
 var getVML1 = '<v:roundrect style="left:'+pleft+';top:'+ptop+';width:'+pwidth+';height:'+pheight+';position:absolute;text-align:center;padding-top:10;color:darkblue;font-size:13;" fillcolor="yellow" onmouseover=showtip("'+tooltip+'") onmouseout=closetip()>';
 var getVML2 = '<v:shadow on="t" type="single" color="#DADADA" offset="4px,1"/>' ; 
 var getVML3 = '<v:fill type="gradient" color="'+color1+'" color2="white" />';
 var getVML4 = '<v:extrusion on="f" backdepth="10px" />'+ stepname;
 var getVML5 = '</v:roundrect>';
 var getVML =getVML1 + getVML2 + getVML3 + getVML4 + getVML5;
 return getVML;
 
 }
 
function action(status,fromX,fromY,toX,toY){
 var fromX = fromX / 1280 * 1024;
 var toX = toX / 1280 * 1024;
 var fromY = fromY / 1024 * 768;
 var toY = toY / 1024 * 768;
 var tempstate = status;
 var colorAction=new Array("#3E6456","#FF0000");
 var color2= colorAction[parseInt(tempstate)];
 var str1 = '<v:line style=z-index:3;position:absolute; from="'+fromX+'px,'+fromY+'px" to="'+toX+'px,'+toY+'px" strokecolor="'+color2+'" strokeweight="1.0">';
 var str2 = '<v:stroke startarrow="none" endarrow="block"/></v:line>';
 return (str1+str2);
 }
 
 function actionOther(status,points){
 var re = / /g ;
 var points = points.replace(re,",");
 var points = points.split(",");
 
 for(var i = 0; i < points.length; i++){
  if(i % 2 == 0){
     points[i] = parseInt(points[i]) / 1280 * 1024;  
  }
  else{
     points[i] = parseInt(points[i]) / 1024 * 768;
  }
}
  var tmpPoints = "";
  for(var i = 0; i < points.length; i++){
    if(i % 2 == 0){
      tmpPoints = tmpPoints + points[i] + ",";
    }
    else{
      tmpPoints = tmpPoints + points[i] + " ";
    }    
  }

 var tempstate = status;
 var colorAction=new Array("#3E6456","#FF0000");
 var color2= colorAction[parseInt(tempstate)];
 str1 = '<v:PolyLine style=z-index:3;position:absolute; Points="'+tmpPoints+'" strokecolor="'+color2+'" strokeweight="1.0" filled="false">';
 str2 = '<v:stroke startarrow="none" endarrow="block"/></v:PolyLine>';
 return (str1+str2);
 }

 function startAndEnd(stepname,pleft,ptop){
 var pleft = pleft / 1280 * 1024;
 var ptop = ptop / 1024 * 768;
 var width = 75 / 1280 * 1024;
 var heigth = 55 / 1024 * 768;
 var getVML1 = '<v:oval style="position:absolute;WIDTH:65px; HEIGHT:45px;left:'+pleft+';top:'+ptop+';text-align:center;padding-top:20;color:darkblue;font-size:13" coordsize = "21600,21600" fillcolor ="#76eeff" stroked="t" strokecolor="#AFAFAF">';
 var getVML2 = ' <v:shadow on ="t" color = "#dadada" opacity = "52428f" offset = "1.5pt,1.5pt"/>';
 var getVML3 = '<v:fill type = "gradient" opacity = ".9" color2 = "white" colors = "19660f #76eeff;.3 #76eeff"/>';
 var getVML4 = stepname;
 var getVML5 = '</v:oval>';
 var getVML =getVML1 + getVML2 + getVML3 + getVML4 + getVML5;
 return getVML;
 }

 function drawExample(state,pleft,ptop,pwidth,pheight){

 var color1Example=new Array("#FFBF23","#FF5723","#23FFAF");
 var color1ExampleString = new Array("已经完成","进行中","未完成");
 var color1="";
 var color1ExampleName="";
 color1=color1Example[parseInt(state)];
 color1ExampleName=color1ExampleString[parseInt(state)];
 document.write('<v:roundrect style="left:'+pleft+';top:'+ptop+';width:'+pwidth+';height:'+pheight+';position:absolute;text-align:center;padding-top:10;color:darkblue;font-size:13;" fillcolor="yellow">');
 document.write('<v:shadow on="t" type="single" color="#DADADA" offset="4px,1"/>');
 document.write('<v:fill type="gradient" color="'+color1+'" color2="white" />');
 document.write('<v:extrusion on="f" backdepth="10px" />'); 
 

 document.write('</v:roundrect>');  
 
 document.write('<v:textbox style="left:'+parseInt(parseInt(pleft)+parseInt(30))+';top:'+ptop+';width:'+parseInt(parseInt(pwidth)+parseInt(40))+';height:'+pheight+';position:absolute;text-align:center;padding-top:5;color:darkblue;font-size:13;" inset="3pt,3pt,3pt,3pt">'+color1ExampleString[parseInt(state)]+'</v:textbox>');

 document.write('</v:roundrect>');   

 }
 
</script>
</head>
<body onload="">
<script language="javascript">
getHtml = useXmlDraw('<%=wfName%>');
document.write(getHtml);
step('0','',0,0,0,0,'');
drawExample('0',350,10,20,20);
drawExample('1',450,10,20,20);
drawExample('2',550,10,20,20);


/******************************************************************************************/
document.write('<div id="mytip" style="position:absolute; z-index:99;"></div>');
document.write('<div id="tipsd" style="position:absolute; z-index:98;"></div>');
Xoffset=-70;
Yoffset= 20;
var nav,old,iex=(document.all),yyy=-1000;

if(navigator.appName=="Netscape"){
 (document.layers)?nav=true:old=true;
}
if(!old){
 var skn=(nav)?document.mytip:mytip.style;
 var sd=(nav)?document.tipsd:tipsd.style;
 if(nav){
  document.captureEvents(Event.MOUSEMOVE)
 }
 document.onmousemove=get_mouse;
}

function showtip(msg){
 if(msg==""){
  return;
 }
 var temp=msg.split(",");
 var tMsg=""; 
 for (i=0;i<temp.length;i++){
  tMsg+=temp[i]+"<br>";
 }
 var content="";
 content+="<table width=140 border=2 bordercolor=#EDCDB9 cellspacing=0 cellpadding=0 style='word-break:break-all;width:fixed;border-collapse:collapse'>";
 content+="<tr style='background-color:#FAEBDD;font-size:9pt;' ><td>&nbsp;</td></tr>";
 content+="<tr style='background-color:#FFFAF6;font-size:9pt;padding-left:5px;padding-right:2px;' height=100><td>"+tMsg+"</td></tr></table>";
 if(old){
  alert(tMsg);
  return;
 }else{yyy=Yoffset;
  if(nav){skn.document.write(content);skn.document.close();skn.visibility="visible"}
  if(iex){
   document.all("mytip").innerHTML=content;skn.visibility="visible";

   content="<table width=140 border=0 cellspacing=0 cellpadding=0 style='filter:progid:DXImageTransform.Microsoft.Alpha(opacity=60);word-break:break-all;width:fixed'>";
   content+="<tr style='background-color:#DADADA;font-size:9pt;' ><td>&nbsp;</td></tr>";
   content+="<tr style='background-color:#DADADA;font-size:9pt;' height=104><td>&nbsp;</td></tr>";
   document.all("tipsd").innerHTML=content;sd.visibility="visible";
  }
 }
}

function get_mouse(e){

 var x=(nav)?e.pageX:event.x+document.body.scrollLeft;skn.left=x+Xoffset;
 var y=(nav)?e.pageY:event.y+document.body.scrollTop;skn.top=y+yyy;
 var x1=(nav)?e.pageX:event.x+document.body.scrollLeft;sd.left=x+Xoffset+5;
 var y1=(nav)?e.pageY:event.y+document.body.scrollTop;sd.top=y+yyy+5;
}

function closetip(){
 if(!old){yyy=-1000;skn.visibility="hidden";sd.visibility="hidden";}
}
</script>
</body>

</html>