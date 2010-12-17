package net.silencily.sailing.utils;

import java.text.MessageFormat;
import java.util.ResourceBundle;


public class MessageInfo {
	String path = null;
	static MessageInfo me=new MessageInfo();	
	private MessageInfo()
	{
		path = this.getClass().getResource("/").getPath();
	}
	public static MessageInfo factory()
	{
		return me;
	}
	/**
	 * û������ʱ
	 * 
	 * @param code
	 *            ��Ϣ�ı���
	 * @return ��Ϣ
	 */
	public String getMessage(String code) {
//		String message=null;
//		message=getInfo(code);
		return getInfo(code);
	}

	/**
	 * ��һ������ʱ
	 * 
	 * @param code
	 *            ��Ϣ�ı���
	 * @param insertString1
	 *            ���ֵ�����
	 * @return ��Ϣ
	 */
	public String getMessage(String code, String insertString1) {
//		String message=null;
//		message=getInfo(code);
//		message = message.replaceAll("%1", insertString1);
//		return message;
		return MessageFormat.format(getInfo(code),new Object[]{insertString1});
	}

	/**
	 * ����������ʱ
	 * 
	 * @param code
	 *            ��Ϣ����
	 * @param insertString1
	 *            ��һ����������
	 * @param insertString2
	 *            �ڶ�����������
	 * @return ��Ϣ
	 */
	public String getMessage(String code, String insertString1,
			String insertString2) {
//		String message=null;
//		message=getInfo(code);
//		message = message.replaceAll("%1", insertString1);
//		message = message.replaceAll("%2", insertString2);
//		return message;
		return MessageFormat.format(getInfo(code),new Object[]{insertString1,insertString2});
	}

	/**
	 * ����������ʱ
	 * 
	 * @param code
	 *            ��Ϣ����
	 * @param insertString1
	 *            ��һ����������
	 * @param insertString2
	 *            �ڶ�����������
	 * @param insertString3
	 *            ��������������
	 * @return ��Ϣ
	 */
	public String getMessage(String code, String insertString1,
			String insertString2, String insertString3) {
//		String message=null;
//		message=getInfo(code);
//		message = message.replaceAll("%1", insertString1);
//		message = message.replaceAll("%2", insertString2);
//		message = message.replaceAll("%3", insertString3);
//		return message;
		return MessageFormat.format(getInfo(code),new Object[]{insertString1,insertString2,insertString3});
	}

	/**
	 * ���ĸ�����ʱ
	 * 
	 * @param code
	 *            ��Ϣ����
	 * @param insertString1
	 *            ��һ����������
	 * @param insertString2
	 *            �ڶ�����������
	 * @param insertString3
	 *            ��������������
	 * @param insertString4
	 *            ���ĸ���������
	 * @return ��Ϣ
	 */
	public String getMessage(String code, String insertString1,
			String insertString2, String insertString3, String insertString4) {
//		String message=null;
//		message=getInfo(code);
//		message = message.replaceAll("%1", insertString1);
//		message = message.replaceAll("%2", insertString2);
//		message = message.replaceAll("%3", insertString3);
//		message = message.replaceAll("%4", insertString4);
		return MessageFormat.format(getInfo(code),new Object[]{insertString1,insertString2,insertString3,insertString4});
	}
	public String getMessage(String code, Object[] iss) {
//		String message=null;
//		message=getInfo(code);
//		message = message.replaceAll("%1", insertString1);
//		message = message.replaceAll("%2", insertString2);
//		message = message.replaceAll("%3", insertString3);
//		message = message.replaceAll("%4", insertString4);
		return MessageFormat.format(getInfo(code),iss);
	}
	/**
	 * ���ļ��ж�����Ϣ
	 * @param code ��Ϣ����
	 * @return ���ļ��ж�������Ϣ
	 */
	private String getInfo(String code) {
//		String path = null;
//		path = this.getClass().getResource("/").getPath();
//		String fileName = path + "message.properties";
//		Properties dealRes = new Properties();
//		String message = null;
//		try {
//			FileInputStream FIS = new FileInputStream(fileName);
//			dealRes.load(FIS);
//		} catch (FileNotFoundException e) {
//			// ��ȡ�ļ��쳣
//			e.printStackTrace();
//		} catch (IOException e) {
//			// Properties�ļ������쳣
//			e.printStackTrace();
//		}
//		message = dealRes.getProperty(code);
//		try {
//			message = new String(message.getBytes("ISO-8859-1"), "GBK");
//		} catch (UnsupportedEncodingException e) {
//			// �ַ���ת���쳣
//			e.printStackTrace();
//		}
		ResourceBundle rb=ResourceBundle.getBundle("message");
		return rb.getString(code);
	}	
}
