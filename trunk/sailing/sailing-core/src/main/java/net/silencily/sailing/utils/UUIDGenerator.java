package net.silencily.sailing.utils;

import java.io.Serializable;
import java.net.InetAddress;

/** 
 * 生成全局唯一主键的<code>utils</code>, 来自于<code>Hibernate</code>. 生成的主键是长度
 * 为<b>32</b>位的字符串
 * 
 * @author from internet, JDon
 * @version $Id: UUIDGenerator.java,v 1.1 2010/12/10 10:54:21 silencily Exp $
 * @since 2006/03/28
 */
public class UUIDGenerator {
    private static final int IP;
    static {
        int ipadd;
        try {
            ipadd = BytesHelper.toInt(InetAddress.getLocalHost().getAddress());
        } catch (Exception e) {
            ipadd = 0;
        }

        IP = ipadd;
    }

    private static short counter = (short) 0;

    private static final int JVM = (int) (System.currentTimeMillis() >>> 8);

    public UUIDGenerator() {
    }

    /**
     * * Unique across JVMs on this machine (unless they load this class in the *
     * same quater second - very unlikely)
     */
    protected int getJVM() {
        return JVM;
    }

    /**
     * * Unique in a millisecond for this JVM instance (unless there are > *
     * Short.MAX_VALUE instances created in a millisecond)
     */
    protected short getCount() {
        synchronized (UUIDGenerator.class) {
            if (counter < 0)
                counter = 0;
            return counter++;
        }
    }

    /** * Unique in a local network */
    protected int getIP() {
        return IP;
    }

    /** * Unique down to millisecond */
    protected short getHiTime() {
        return (short) (System.currentTimeMillis() >>> 32);
    }

    protected int getLoTime() {
        return (int) System.currentTimeMillis();
    }

    private static final String SEPERATOR = "";

    protected String format(int intval) {
        String formatted = Integer.toHexString(intval);
        StringBuffer buf = new StringBuffer("00000000");
        buf.replace(8 - formatted.length(), 8, formatted);
        return buf.toString();
    }

    protected String format(short shortval) {
        String formatted = Integer.toHexString(shortval);
        StringBuffer buf = new StringBuffer("0000");
        buf.replace(4 - formatted.length(), 4, formatted);
        return buf.toString();
    }

    public Serializable generate() {
        return new StringBuffer(36).append(format(getIP())).append(
            UUIDGenerator.SEPERATOR).append(format(getJVM())).append(
            UUIDGenerator.SEPERATOR).append(format(getHiTime())).append(
            UUIDGenerator.SEPERATOR).append(format(getLoTime())).append(
            UUIDGenerator.SEPERATOR).append(format(getCount())).toString();
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 100; i++)
            System.out.println(new UUIDGenerator().generate());
    }

}