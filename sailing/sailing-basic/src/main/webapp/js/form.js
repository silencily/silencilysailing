		//checkSomeForm,true/false
		function checkOnchangeSomeForm(forms){
			for(i=0;i<forms.length;i++){
				if(checkOnchangeForm(forms[i])){
					return true;
				}
				//alert(i+":"+forms.length);
			}
			return false;
		}
		//checkAllForm,true/false
		function checkOnchangeAllForm(){
			if(checkOnchangeSomeForm(document.forms)){
					return true;
			}
			return false;
		}
		//checkForm,true/false
		function checkOnchangeForm(form){
			var el = form.length;
			for(j=0;j<el;j++){
				hiddenID = form.elements[j].name;
				//hiddenNameValue
				if(!(hiddenID == null) && !(hiddenID.search("hiddenValueDefault") == -1)){
					name = hiddenID.substr(18,hiddenID.length);
					//onchange
					if(form.elements[j].value != document.getElementsByName(name)[0].value
						|| isDelFlg(form.elements[j])){
					//alert(document.getElementsByName(name)[0].value+" & "+form.elements[j].value);
						//onsubmit
						//if(confirm("本画面页面已修改,是否继续迁移?")){
						//		return false;
						//}
						return true;
					}
				}
			}
			return false;
		}
		//hintOnchange
		function hintOnchange(hintMsg){
			return confirm(hintMsg);
		}
		
		
function pagebodyonbeforeunloadallform(){
	if(checkOnchangeAllForm()){
		window.event.returnValue="本画面页面已修改,是否继续迁移?"
	}
}
		
function pagebodyonbeforeunloadsomeform(forms){
	if(checkOnchangeSomeForm(forms)){
		window.event.returnValue="本画面页面已修改,是否继续迁移?"
	}
}
		
function pagebodyonbeforeunloadform(form){
	if(checkOnchangeForm(form)){
		window.event.returnValue="本画面页面已修改,是否继续迁移?"
	}
}