package net.silencily.sailing.utils;

import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContext;

import net.silencily.sailing.common.crud.domain.CommonTableView;
import net.silencily.sailing.common.dbtime.DBTime;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

/**
 * @author Administrator
 * 
 */
public class Tools {

	private static int seq = 1;

	private static final int max = 999999;

	public static final String RESOURCE_FILE = "/WEB-INF/classes/resource.properties";

	public synchronized static String getPKCode() {
		Calendar c = Calendar.getInstance();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		StringBuffer pk = new StringBuffer();
		pk.append("qware");
		pk.append(sdf.format(c.getTime()));

		String seqs = Integer.toString(seq);
		int num = seqs.length();

		if (num < 6) {
			char[] temp = new char[6 - num];
			Arrays.fill(temp, '0');
			pk.append(temp);
		}
		pk.append(seqs);
		if (seq < max) {
			seq++;
		} else {
			seq = 1;
		}
		return pk.toString();
	}

	public static Date getNowTime() {
		return DBTime.getDBTime();
	}

	public static Date getTheTime(String t) {
		if (t == null || t.equals(""))
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			return sdf.parse(t);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return null;
		}

	}

	public static Date getTheTime(String t, String style) {
		SimpleDateFormat sdf = new SimpleDateFormat(style);

		try {
			return sdf.parse(t);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return null;
		}

	}

	public static String getTheTime(Date time, String style) {
		if (time == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat(style);

		return sdf.format(time);
	}

	public static String getTime(Date d) {
		if (d == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(d);
	}

	public static String getTimeStyle(Date d) {

		if (d == null)
			return "";
		String style = "yyyy-MM-dd HH:mm:ss";
		if (d.getHours() + d.getMinutes() + d.getSeconds() == 0)
			style = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(style);
		return sdf.format(d);
	}

	public static Double addDouble(Double d1, Double d2) {
		String add1 = d1.toString();
		String add2 = d2.toString();
		if (add1.startsWith("-") && !add2.startsWith("-"))
			subDouble(d2, d1);

		int i1 = add1.indexOf(".");
		int i2 = add2.indexOf(".");
		long add1a = Long.parseLong(add1.substring(0, i1));
		long add2a = Long.parseLong(add2.substring(0, i2));
		add1a = add1a + add2a;
		String add1s = add1.substring(i1 + 1, add1.length());
		String add2s = add2.substring(i2 + 1, add2.length());

		if (add1s.length() > add2s.length()) {
			char[] temp = new char[add1s.length() - add2s.length()];
			for (int i = 0, size = temp.length; i < size; i++)
				temp[i] = '0';
			add2s = add2s + new String(temp);
		}
		long add1b = Long.parseLong(add1s);
		long add2b = Long.parseLong(add2s);

		char[] temp1 = new char[add1s.length()];
		for (int i = 0, size = temp1.length; i < size; i++)
			temp1[i] = '0';
		long size1 = Long.parseLong("1" + new String(temp1));
		add1b = add1b + add2b;
		add1a = add1a + add1b / size1;
		return Double.valueOf(Long.toString(add1a) + "."
				+ Long.toString(add1b % size1));
	}

	public static Double subDouble(Double minuend, Double subtrahend) {
		// ͨ��DecimalFormat�����ַ�����ʽ����ֹ��ѧ��������ʾ�� add by yushn 20080704
		DecimalFormat f = new DecimalFormat("0.0#########");
		// String sub1 = minuend.toString();
		// String sub2 = subtrahend.toString();
		String sub1 = f.format(minuend);
		String sub2 = f.format(subtrahend);
		String aaa = "";
		String tmp;
		if (minuend.compareTo(subtrahend) <= 0) {
			tmp = sub2;
			sub2 = sub1;
			sub1 = tmp;
			aaa = "-";
		}
		int i1 = sub1.indexOf(".");
		int i2 = sub2.indexOf(".");
		long sub1a = Long.parseLong(sub1.substring(0, i1));
		long sub2a = Long.parseLong(sub2.substring(0, i2));
		sub1a = sub1a - sub2a;
		String sub1s = sub1.substring(i1 + 1, sub1.length());
		String sub2s = sub2.substring(i2 + 1, sub2.length());

		if (sub1s.length() > sub2s.length()) {
			char[] temp = new char[sub1s.length() - sub2s.length()];
			for (int i = 0, size = temp.length; i < size; i++)
				temp[i] = '0';
			sub2s = sub2s + new String(temp);
		}
		long sub1b = Long.parseLong(sub1s);
		long sub2b = Long.parseLong(sub2s);

		char[] temp1 = new char[sub1s.length()];
		for (int i = 0, size = temp1.length; i < size; i++)
			temp1[i] = '0';
		long size1 = Long.parseLong("1" + new String(temp1));
		sub1b = sub1b + size1;
		sub1b = sub1b - sub2b;
		sub1a = sub1a - 1;
		sub1a = sub1a + sub1b / size1;
		return Double.valueOf(aaa + Long.toString(sub1a) + "."
				+ Long.toString(sub1b % size1));

	}

	public static Object getProperty(Object object, String propertyname) {

		if (object == null)
			return null;
		try {
			// if(propertyname.indexOf(".")!=-1)
			// return PropertyUtils.getNestedProperty(object, propertyname);
			// else
			// return PropertyUtils.getSimpleProperty(object, propertyname);
			return PropertyUtils.getProperty(object, propertyname);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static boolean setProperty(Object object, String propertyname,
			Object value) {

		if (object == null)
			return false;
		try {
			// if(propertyname.indexOf(".")!=-1)
			// PropertyUtils.setNestedProperty(object, propertyname, value);
			// else
			// PropertyUtils.setSimpleProperty(object, propertyname, value);
			PropertyUtils.setProperty(object, propertyname, value);
			return true;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static Set getProperties(Object object) {
		Map map;
		Set set = null;
		try {
			map = PropertyUtils.describe(object);
			set = map.keySet();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return set;
	}

	public static List split(String source, String sp) {
		String[] temp = source.split(sp);
		return Arrays.asList(temp);
	}

	/**
	 * @param args
	 *            ����ʱ�䣺����09:42:30 �����ˣ�Administrator ���ƣ�main
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PropertyDescriptor[] a = PropertyUtils
				.getPropertyDescriptors(CommonTableView.class);
		for (int i = 0, size = a.length; i < size; i++) {
			if (Set.class.isAssignableFrom(a[i].getPropertyType()))
				System.out.println(a[i].getName());
		}

	}

	// public static String changeRMB(double rmb)
	// {
	// StringBuffer r=new StringBuffer();
	//
	// return r.toString();
	// }
	public static String changeMoney(double smallmoney) {
		String value = String.valueOf(smallmoney);
		if (null == value || "".equals(value.trim()))
			return "��";

		String strCheck, strArr, strFen, strDW, strNum, strBig, strNow;
		double d = 0;
		try {
			d = Double.parseDouble(value);
		} catch (Exception e) {
			return "����" + value + "�Ƿ���";
		}

		strCheck = value + ".";
		int dot = strCheck.indexOf(".");
		if (dot > 12) {
			return "����" + value + "�����޷�����";
		}

		try {
			int i = 0;
			strBig = "";
			strDW = "";
			strNum = "";
			long intFen = (long) (d * 100);
			strFen = String.valueOf(intFen);
			int lenIntFen = strFen.length();
			while (lenIntFen != 0) {
				i++;
				switch (i) {
				case 1:
					strDW = "��";
					break;
				case 2:
					strDW = "��";
					break;
				case 3:
					strDW = "Ԫ";
					break;
				case 4:
					strDW = "ʰ";
					break;
				case 5:
					strDW = "��";
					break;
				case 6:
					strDW = "Ǫ";
					break;
				case 7:
					strDW = "��";
					break;
				case 8:
					strDW = "ʰ";
					break;
				case 9:
					strDW = "��";
					break;
				case 10:
					strDW = "Ǫ";
					break;
				case 11:
					strDW = "��";
					break;
				case 12:
					strDW = "ʰ";
					break;
				case 13:
					strDW = "��";
					break;
				case 14:
					strDW = "Ǫ";
					break;
				}
				switch (strFen.charAt(lenIntFen - 1)) // ѡ������
				{
				case '1':
					strNum = "Ҽ";
					break;
				case '2':
					strNum = "��";
					break;
				case '3':
					strNum = "��";
					break;
				case '4':
					strNum = "��";
					break;
				case '5':
					strNum = "��";
					break;
				case '6':
					strNum = "½";
					break;
				case '7':
					strNum = "��";
					break;
				case '8':
					strNum = "��";
					break;
				case '9':
					strNum = "��";
					break;
				case '0':
					strNum = "��";
					break;
				}
				// �����������
				strNow = strBig;
				// ��Ϊ��ʱ�����
				if ((i == 1) && (strFen.charAt(lenIntFen - 1) == '0'))
					strBig = "��";
				// ��Ϊ��ʱ�����
				else if ((i == 2) && (strFen.charAt(lenIntFen - 1) == '0')) { // �Ƿ�ͬʱΪ��ʱ�����
					if (!strBig.equals("��"))
						strBig = "��" + strBig;
				}
				// ԪΪ������
				else if ((i == 3) && (strFen.charAt(lenIntFen - 1) == '0'))
					strBig = "Ԫ" + strBig;
				// ʰ��Ǫ��һλΪ������ǰһλ��Ԫ���ϣ���Ϊ������ʱ����
				else if ((i < 7) && (i > 3)
						&& (strFen.charAt(lenIntFen - 1) == '0')
						&& (strNow.charAt(0) != '��')
						&& (strNow.charAt(0) != 'Ԫ'))
					strBig = "��" + strBig;
				// ʰ��Ǫ��һλΪ������ǰһλ��Ԫ���ϣ�ҲΪ������ʱ���
				else if ((i < 7) && (i > 3)
						&& (strFen.charAt(lenIntFen - 1) == '0')
						&& (strNow.charAt(0) == '��')) {
				}
				// ʰ��Ǫ��һλΪ������ǰһλ��Ԫ��Ϊ������ʱ���
				else if ((i < 7) && (i > 3)
						&& (strFen.charAt(lenIntFen - 1) == '0')
						&& (strNow.charAt(0) == 'Ԫ')) {
				}
				// ����Ϊ��ʱ���벹������
				else if ((i == 7) && (strFen.charAt(lenIntFen - 1) == '0'))
					strBig = "��" + strBig;
				// ʰ��Ǫ����һλΪ������ǰһλ�������ϣ���Ϊ������ʱ����
				else if ((i < 11) && (i > 7)
						&& (strFen.charAt(lenIntFen - 1) == '0')
						&& (strNow.charAt(0) != '��')
						&& (strNow.charAt(0) != '��'))
					strBig = "��" + strBig;
				// ʰ��Ǫ����һλΪ������ǰһλ�������ϣ�ҲΪ������ʱ���
				else if ((i < 11) && (i > 7)
						&& (strFen.charAt(lenIntFen - 1) == '0')
						&& (strNow.charAt(0) == '��')) {
				}
				// ʰ��Ǫ����һλΪ������ǰһλΪ��λ��Ϊ������ʱ���
				else if ((i < 11) && (i > 7)
						&& (strFen.charAt(lenIntFen - 1) == '0')
						&& (strNow.charAt(0) == '��')) {
				}
				// ��λΪ���Ҵ���Ǫλ��ʮ������ʱ������Ǫ�䲹��
				else if ((i < 11) && (i > 8)
						&& (strFen.charAt(lenIntFen - 1) == '0')
						&& (strNow.charAt(0) == '��')
						&& (strNow.charAt(2) == 'Ǫ'))
					strBig = strNum + strDW + "����"
							+ strBig.substring(1, strBig.length());
				// ����������λ
				else if (i == 11) {
					// ��λΪ������ȫΪ�����Ǫλʱ��ȥ����Ϊ��
					if ((strFen.charAt(lenIntFen - 1) == '0')
							&& (strNow.charAt(0) == '��')
							&& (strNow.charAt(2) == 'Ǫ'))
						strBig = "��" + "��"
								+ strBig.substring(1, strBig.length());
					// ��λΪ������ȫΪ�㲻����Ǫλʱ��ȥ����
					else if ((strFen.charAt(lenIntFen - 1) == '0')
							&& (strNow.charAt(0) == '��')
							&& (strNow.charAt(2) != 'Ǫ'))
						strBig = "��" + strBig.substring(1, strBig.length());
					// ��λ��Ϊ������ȫΪ�����Ǫλʱ��ȥ����Ϊ��
					else if ((strNow.charAt(0) == '��')
							&& (strNow.charAt(2) == 'Ǫ'))
						strBig = strNum + strDW + "��"
								+ strBig.substring(1, strBig.length());
					// ��λ��Ϊ������ȫΪ�㲻����Ǫλʱ��ȥ����
					else if ((strNow.charAt(0) == '��')
							&& (strNow.charAt(2) != 'Ǫ'))
						strBig = strNum + strDW
								+ strBig.substring(1, strBig.length());
					// �����������
					else
						strBig = strNum + strDW + strBig;
				}
				// ʰ�ڣ�Ǫ����һλΪ������ǰһλ�������ϣ���Ϊ������ʱ����
				else if ((i < 15) && (i > 11)
						&& (strFen.charAt(lenIntFen - 1) == '0')
						&& (strNow.charAt(0) != '��')
						&& (strNow.charAt(0) != '��'))
					strBig = "��" + strBig;
				// ʰ�ڣ�Ǫ����һλΪ������ǰһλ�������ϣ�ҲΪ������ʱ���
				else if ((i < 15) && (i > 11)
						&& (strFen.charAt(lenIntFen - 1) == '0')
						&& (strNow.charAt(0) == '��')) {
				}
				// ʰ�ڣ�Ǫ����һλΪ������ǰһλΪ��λ��Ϊ������ʱ���
				else if ((i < 15) && (i > 11)
						&& (strFen.charAt(lenIntFen - 1) == '0')
						&& (strNow.charAt(0) == '��')) {
				}
				// ��λΪ���Ҳ�����Ǫ��λ��ʮ������ʱȥ���ϴ�д�����
				else if ((i < 15) && (i > 11)
						&& (strFen.charAt(lenIntFen - 1) != '0')
						&& (strNow.charAt(0) == '��')
						&& (strNow.charAt(1) == '��')
						&& (strNow.charAt(3) != 'Ǫ'))
					strBig = strNum + strDW
							+ strBig.substring(1, strBig.length());
				// ��λΪ���Ҵ���Ǫ��λ��ʮ������ʱ������Ǫ��䲹��
				else if ((i < 15) && (i > 11)
						&& (strFen.charAt(lenIntFen - 1) != '0')
						&& (strNow.charAt(0) == '��')
						&& (strNow.charAt(1) == '��')
						&& (strNow.charAt(3) == 'Ǫ'))
					strBig = strNum + strDW + "����"
							+ strBig.substring(2, strBig.length());
				else
					strBig = strNum + strDW + strBig;
				strFen = strFen.substring(0, lenIntFen - 1);
				lenIntFen--;
			}
			return strBig;
		} catch (Exception e) {
			return "";
		}
	}

	public static String changeHtml(String html) {
		if (StringUtils.isBlank(html))
			return html;
		return html.replaceAll("<", "&lt;").replaceAll(">", "&gt;")
				.replaceAll("\"", "&quot;").replaceAll(" ", "&nbsp;");
	}

	public static String abbreviate(String s, int i) {
		if (i <= 0)
			return s;
		else
			return StringUtils.abbreviate(s, i);
	}

	/**
	 * 
	 * �������� ������URLת�ɴ���Ȩ�޵�URL��ڣ�ͨ����ת����·������Ȩ�޵ļ��� ǰ�᣺��URL�Ǵ��������URL
	 * 
	 * @param wfName
	 *            ����������
	 * @param wfStep
	 *            ����������
	 * @param url
	 *            ������URL
	 * @return
	 * @throws Exception
	 *             2008-01-28 ����09:57:40
	 * @version 1.0
	 * @author wenjb
	 */
	public static String getWFSecurityURL(String wfName, String wfStep,
			String url) {
		// ��URL�е�&ת����|
		String wfurl = (url + "&urlKey=waitList").replace('&', '|');
		String temp = "/wf/personWfSearchAction.do?step=startStep" + "&wfname="
				+ wfName + "&wfstep=" + wfStep + "&wfurl=" + wfurl;
		// ����wfname��wfstep����Ȩ�޵ļ���
		return temp;
	}

	public static String encodeDLFileName(String fileName) {
		String encoded = null;
		try {
			encoded = new String(fileName.getBytes("GBK"), "ISO8859-1");
		} catch (Exception e) {
			encoded = fileName;
		}
		return encoded;
	}

	public static Object depthClone(Object srcObj) {
		Object cloneObj = null;
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream oo = new ObjectOutputStream(out);
			oo.writeObject(srcObj);

			ByteArrayInputStream in = new ByteArrayInputStream(
					out.toByteArray());
			ObjectInputStream oi = new ObjectInputStream(in);
			cloneObj = oi.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return cloneObj;
	}

	/**
	 * ��ȡProperties
	 * 
	 * @return
	 */
	public static Properties getProperties(ServletContext context) {
		Properties propFile = new Properties();
		String path = context.getRealPath("/");
		String fileName = path + RESOURCE_FILE;
		try {
			FileInputStream fis = new FileInputStream(fileName);
			propFile.load(fis);
			if (null == propFile) {
				System.out.println("resource.properties�ļ������û���ҵ�!");
			}
			return propFile;
		} catch (Exception e) {
			// ��ȡ�ļ��쳣
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ��ȡProperty
	 * 
	 * @return
	 */
	public static String getProperty(ServletContext context, String key) {
		Properties prop = getProperties(context);
		String value = null;
		if (prop != null) {
			value = prop.getProperty(key);
		}
		if (StringUtils.isBlank(value)) {
			value = "";
		}
		return value;
	}

	public static Method getGetMethod(String fieldName, Class cls)
			throws NoSuchMethodException {
		return getGetSetMethod(fieldName, cls, null, "get");
	}

	public static Method getSetMethod(String fieldName, Class cls, Class[] args)
			throws NoSuchMethodException {
		return getGetSetMethod(fieldName, cls, args, "set");
	}

	private static Method getGetSetMethod(String fieldName, Class cls,
			Class[] args, String prefix) throws NoSuchMethodException {
		String methodName = prefix + fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);
		return cls.getMethod(methodName, args);
	}

	public static Class getUnProxyClass(Class proxyClass) {
		String proxyClassName = "";
		Class unProxyClass = null;
		if (proxyClass != null) {
			proxyClassName = proxyClass.getName();
			int x = proxyClassName.indexOf(("$"));
			if (-1 != x) {
				proxyClassName = proxyClassName.substring(0, x);
			}
			try {
				unProxyClass = Class.forName(proxyClassName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return unProxyClass;
	}
}
