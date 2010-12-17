/*********************
 * 四舍五入到小数点后两位
燃料子系统有很多数据都要求是固定的格式，并要求四舍五入，一般都是number(12, 2)，
如果画面出现数据： 3.1230032..此类可以用myRound(arg1, arg2)处理。
例：
var num = 3.1260032;
var result = myRound(num, 2);    // result = 3.13
 
若num = 1，则结果为1.00
 *********************/
/*
 * 四舍五入到指定位数
 *
 * num 要处理的数字
 * n 小数点后几位
 */
function myRound(num,n){
    var   dd=1;  
    var   tempnum;  
    for(i=0;i<n;i++){  
        dd*=10;
    }  
    tempnum=num*dd;
    
    // 如果参数非数字，将值置为0
    // modified by liyj 2008/6/16 PM
    if (tempnum+""=="NaN") {
		return formatnumber(0,n);  
	}
      
    tempnum=Math.round(tempnum);
    return formatnumber(tempnum/dd,n);  
}  
 /*
  * 格式化数字
  *
  * value  数值
  * num 位数
  */
function formatnumber(value, num) {
    var a, b, c, i;
    a = value.toString();
    b = a.indexOf(".");
    c = a.length;
    if (num == 0) {
        if (b != -1) {
            a = a.substring(0, b);
        }
    } else {
        if (b == -1) {
            a = a + ".";
            for (i = 1; i <= num; i++) {
                a = a + "0";
            }
        } else {
            a = a.substring(0, b + num + 1);
            for (i = c; i <= b + num; i++) {
                a = a + "0";
            }
        }
    }
    return a;
} 
