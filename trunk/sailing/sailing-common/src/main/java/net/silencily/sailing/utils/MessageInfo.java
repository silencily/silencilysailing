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
	 * 没有埋字时
	 * 
	 * @param code
	 *            消息的编码
	 * @return 消息
	 */
	public String getMessage(String code) {
//		String message=null;
//		message=getInfo(code);
		return getInfo(code);
	}

	/**
	 * 有一个埋字时
	 * 
	 * @param code
	 *            消息的编码
	 * @param insertString1
	 *            埋字的内容
	 * @return 消息
	 */
	public String getMessage(String code, String insertString1) {
//		String message=null;
//		message=getInfo(code);
//		message = message.replaceAll("%1", insertString1);
//		return message;
		return MessageFormat.format(getInfo(code),new Object[]{insertString1});
	}

	/**
	 * 有两个埋字时
	 * 
	 * @param code
	 *            消息编码
	 * @param insertString1
	 *            第一个埋字内容
	 * @param insertString2
	 *            第二个埋字内容
	 * @return 消息
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
	 * 有三个埋字时
	 * 
	 * @param code
	 *            消息编码
	 * @param insertString1
	 *            第一个埋字内容
	 * @param insertString2
	 *            第二个埋字内容
	 * @param insertString3
	 *            第三个埋字内容
	 * @return 消息
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
	 * 有四个埋字时
	 * 
	 * @param code
	 *            消息编码
	 * @param insertString1
	 *            第一个埋字内容
	 * @param insertString2
	 *            第二个埋字内容
	 * @param insertString3
	 *            第三个埋字内容
	 * @param insertString4
	 *            第四个埋字内容
	 * @return 消息
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
	 * 从文件中读出消息
	 * @param code 消息编码
	 * @return 从文件中读出的消息
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
//			// 读取文件异常
//			e.printStackTrace();
//		} catch (IOException e) {
//			// Properties文件加载异常
//			e.printStackTrace();
//		}
//		message = dealRes.getProperty(code);
//		try {
//			message = new String(message.getBytes("ISO-8859-1"), "GBK");
//		} catch (UnsupportedEncodingException e) {
//			// 字符串转换异常
//			e.printStackTrace();
//		}
		ResourceBundle rb=ResourceBundle.getBundle("message");
		return rb.getString(code);
	}	
}
