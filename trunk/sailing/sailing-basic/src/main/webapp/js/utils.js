/*********************
 * �������뵽С�������λ
ȼ����ϵͳ�кܶ����ݶ�Ҫ���ǹ̶��ĸ�ʽ����Ҫ���������룬һ�㶼��number(12, 2)��
�������������ݣ� 3.1230032..���������myRound(arg1, arg2)����
����
var num = 3.1260032;
var result = myRound(num, 2);    // result = 3.13
 
��num = 1������Ϊ1.00
 *********************/
/*
 * �������뵽ָ��λ��
 *
 * num Ҫ���������
 * n С�����λ
 */
function myRound(num,n){
    var   dd=1;  
    var   tempnum;  
    for(i=0;i<n;i++){  
        dd*=10;
    }  
    tempnum=num*dd;
    
    // ������������֣���ֵ��Ϊ0
    // modified by liyj 2008/6/16 PM
    if (tempnum+""=="NaN") {
		return formatnumber(0,n);  
	}
      
    tempnum=Math.round(tempnum);
    return formatnumber(tempnum/dd,n);  
}  
 /*
  * ��ʽ������
  *
  * value  ��ֵ
  * num λ��
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
